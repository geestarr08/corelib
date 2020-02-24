package eu.europeana.corelib.storage.impl;

import com.mongodb.*;
import com.mongodb.event.*;
import eu.europeana.corelib.storage.MongoProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class to create a MongoClient
 */
public class MongoProviderImpl implements MongoProvider, ConnectionPoolListener {
    private static final Logger LOG = LogManager.getLogger(MongoProviderImpl.class);
    private static final int    THREADS_THRESHOLD     = 10;
    private static final int    MAX_THREADS_THRESHOLD = 20;

    private MongoClient mongo;
    private String      defaultDatabase;

    // from OAI-PMH2, values taken from the oai-pmh2 properties for now
    // number of threads from configuration
    private int threadsCount = 10;

    // TODO figure out difference between threadsCount and maxThreadCount.
    // Looks like both are static numbers from the configuration and never changed (at least not after initialization in initThreadPool method)
    private int maxThreadsCount = 20;

    // for some reason we always get 2 connections directly after start-up that are not registered by the ConnectionPoolListener
    private int nrConnections = 2;

    private ExecutorService threadPool;

    /**
     * Create a new MongoClient based on a connectionUrl, e.g.
     * mongodb://user:password@mongo1.eanadev.org:27000/europeana_1?replicaSet=europeana
     * Called directly from corelib-db ApiMongoConnector
     *
     * @param connectionUrl the connection URL (!)
     * @see <a href="http://api.mongodb.com/java/current/com/mongodb/MongoClientURI.html">
     * MongoClientURI documentation</a>
     */
    public MongoProviderImpl(String connectionUrl) {
        // Let's add a connectionPoolListener so we can keep track of the number of connections
        MongoClientOptions.Builder clientOptions = new MongoClientOptions.Builder().addConnectionPoolListener(this);
        MongoClientURI uri = new MongoClientURI(connectionUrl, clientOptions);
//        MongoClientURI uri = new MongoClientURI(connectionUrl);
        defaultDatabase = uri.getDatabase();
        LOG.info("[corelib.storage MongoProvider] Creating new MongoClient for {}, database: {}",
                 uri.getHosts(),
                 (StringUtils.isEmpty(defaultDatabase) ? "" : ", database " + defaultDatabase));
        mongo = new MongoClient(uri);
        initThreadPool();
        LOG.info("===> the number of counted connections is now: {}", this.nrConnections);
    }

    /**
     * Threads count must be at least 1. When it's bigger than <code>THREADS_THRESHOLD</code> but smaller than <code>maxThreadsCount</code>
     * a warning is displayed. When it exceeds <code>maxThreadsCount</code> a warning is displayed and the value is set to <code>MAX_THREADS_THRESHOLD</code>
     */
    private void initThreadPool() {
        // init thread pool
        if (maxThreadsCount < THREADS_THRESHOLD) {
            maxThreadsCount = MAX_THREADS_THRESHOLD;
        }

        if (threadsCount < 1) {
            threadsCount = 1;
        } else if (threadsCount > THREADS_THRESHOLD && threadsCount <= maxThreadsCount) {
            LOG.warn("Number of threads exceeds " + THREADS_THRESHOLD + " which may narrow the number of clients working in parallel");
        } else if (threadsCount > maxThreadsCount) {
            LOG.warn("Number of threads exceeds {}, which may highly narrow the number of clients working in parallel. Changing to {}",
                     maxThreadsCount, MAX_THREADS_THRESHOLD);
            threadsCount = MAX_THREADS_THRESHOLD;
        }
        LOG.info("Creating new thread pool with {} threads.", threadsCount);
        threadPool = Executors.newFixedThreadPool(threadsCount);
    }

    /**
     * Create a new MongoClient without any credentials
     * @deprecated This constuctor is not used anywhere
     *
     * @param hosts comma-separated host names
     * @param ports omma-separated port numbers
     */
    @Deprecated
    public MongoProviderImpl(String hosts, String ports) {
        this(hosts, ports, null, null, null, null);
    }

    /**
     * Create a new MongoClient with the supplied credentials
     * <p>
     * Used only in corelib.lookup EuropeanaIdRegistryMongoServerImpl; two other usages (in corelib-db ApiMongoConnector
     * and in corelib.lookup CollectionMongoServerImpl) are unused themselves
     *
     * @param hosts    comma-separated host names
     * @param ports    comma-separated port numbers
     * @param dbName   optional
     * @param username optional
     * @param password optional
     */
    public MongoProviderImpl(String hosts, String ports, String dbName, String username, String password) {
        this(hosts, ports, dbName, username, password, null);
    }

    /**
     * Create a new MongoClient with the supplied credentials and optionsBuilder.
     * If no optionsBuilder is provided a default one will be constructed.
     * Hosts array should contain one or more host names. If number of the hosts is greater
     * than 1 then the ports array should contain an entry for each host or just one port
     * which will be used for all the hosts.
     *
     * Only used from within this class
     *
     * @param hosts          array of host names
     * @param ports          array of ports
     * @param dbName         optional
     * @param username       optional
     * @param password       optional
     * @param optionsBuilder optional
     */
    public MongoProviderImpl(String[] hosts,
                             String[] ports,
                             String dbName,
                             String username,
                             String password,
                             MongoClientOptions.Builder optionsBuilder) {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        int                 i               = 0;
        for (String host : hosts) {
            if (host.length() > 0) {
                try {
                    ServerAddress address = new ServerAddress(host, getPort(ports, i));
                    serverAddresses.add(address);
                } catch (NumberFormatException e) {
                    LOG.error("[corelib.storage MongoProvider] Error parsing port numbers", e);
                }
            }
            i++;
        }

        MongoClientOptions.Builder builder = optionsBuilder;
        if (optionsBuilder == null) {
            // use defaults
            builder = MongoClientOptions.builder();
        }

        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            LOG.info("[corelib.storage MongoProvider] [params] Creating new MongoClient - {} {}",
                     Arrays.toString(hosts),
                     " (no credentials)");
            defaultDatabase = null;
            mongo           = new MongoClient(serverAddresses, builder.build());
        } else {
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(MongoCredential.createCredential(username, dbName, password.toCharArray()));
            LOG.info("[corelib.storage MongoProvider] [params] Creating new MongoClient - {} {} {} {}",
                     Arrays.toString(hosts),
                     ", database ",
                     dbName,
                     " (with credentials)");
            defaultDatabase = dbName;
            mongo           = new MongoClient(serverAddresses, credentials, builder.build());
        }
    }

    /**
     * Create a new MongoClient with the supplied credentials and optionsBuilder
     * If no optionsBuilder is provided a default one will be constructed.
     *
     * Usages:  corelib-storage:    EdmMongoServerImpl, but that method is not used
     *          internally:         #49 (deprecated, nog used)
     *                              #65 => corelib.lookup EuropeanaIdRegistryMongoServerImpl
     *
     * @param hosts          comma-separated host names
     * @param ports          comma-separated port numbers
     * @param dbName         optional
     * @param username       optional
     * @param password       optional
     * @param optionsBuilder optional
     */
    public MongoProviderImpl(String hosts,
                             String ports,
                             String dbName,
                             String username,
                             String password,
                             MongoClientOptions.Builder optionsBuilder) {
        this(StringUtils.split(hosts, ","),
             StringUtils.split(ports, ","),
             dbName,
             username,
             password,
             optionsBuilder);
    }

    private int getPort(String[] ports, int index) {
        if (ports == null || index < 0) {
            throw new NumberFormatException("Empty port");
        }
        if (ports.length > 1 && index < ports.length) {
            return Integer.parseInt(ports[index]);
        }
        return Integer.parseInt(ports[0]);
    }

    /**
     * @see MongoProvider#getMongo()
     */
    @Override
    public MongoClient getMongo() {
        LOG.info("===> [getMongo] nr of connections is now: {}", this.nrConnections);
        return mongo;
    }

    /**
     * @see MongoProvider#getDefaultDatabase()
     */
    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    /**
     * @see MongoProvider#close()
     */
    @Override
    public void close() {
        LOG.info("===> [closing down] nr of connections is now: {}", this.nrConnections);
        if (mongo != null) {
            LOG.info("[corelib.storage MongoProvider] Closing MongoClient - {}", mongo.getServerAddressList().get(0));
            mongo.close();
        }
        if (threadPool != null) {
            LOG.info("[corelib.storage MongoProvider] Shutting down threadPool...");
            threadPool.shutdown();
        }
    }

    @Override
    public void connectionPoolOpened(ConnectionPoolOpenedEvent connectionPoolOpenedEvent) {
        LOG.info("[corelib.storage MongoProvider] Connection pool opened {}", connectionPoolOpenedEvent);
    }

    @Override
    public void connectionPoolClosed(ConnectionPoolClosedEvent connectionPoolClosedEvent) {
        LOG.info("[corelib.storage MongoProvider] Connection pool closed {}", connectionPoolClosedEvent);
    }

    @Override
    public void connectionCheckedOut(ConnectionCheckedOutEvent connectionCheckedOutEvent) {
        // ignore
    }

    @Override
    public void connectionCheckedIn(ConnectionCheckedInEvent connectionCheckedInEvent) {
        // ignore
    }

    @Override
    public void waitQueueEntered(ConnectionPoolWaitQueueEnteredEvent connectionPoolWaitQueueEnteredEvent) {
        // ignore
    }

    @Override
    public void waitQueueExited(ConnectionPoolWaitQueueExitedEvent connectionPoolWaitQueueExitedEvent) {
        // ignore
    }

    @Override
    public synchronized void connectionAdded(ConnectionAddedEvent connectionAddedEvent) {
        nrConnections++;
        LOG.info("[corelib.storage] {} for corelib.storage MongoProvider {}, total Mongo connections = {}", connectionAddedEvent,
                 this.hashCode(), nrConnections);
    }

    @Override
    public synchronized void connectionRemoved(ConnectionRemovedEvent connectionRemovedEvent) {
        nrConnections--;
        LOG.info("[corelib.storage] {} for corelib.storage MongoProvider {}, total Mongo connections = {}", connectionRemovedEvent,
                 this.hashCode(), nrConnections);
    }
}
