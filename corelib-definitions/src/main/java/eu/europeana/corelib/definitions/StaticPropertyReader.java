/*
 * Copyright 2007-2018 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */

package eu.europeana.corelib.definitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import org.apache.commons.lang.BooleanUtils;


/**
 * Created by luthien on 27/02/2018.
 */
public class StaticPropertyReader {

    // value is initially NULL,
    private static Boolean runtimePropertiesLoaded ;
    private static final String REPRIMAND = "Property context not specified: load either runtime or test properties first";
    private static final Logger     LOG   = LogManager.getLogger(StaticPropertyReader.class);
    private static       Properties props = new Properties();
    private static final String PROPERTYFILENAME = "europeana.properties";
    private static final String TESTPROPERTYFILENAME = "europeana-test.properties";
    private static final String TESTPROPERTYFILEPATH = StaticPropertyReader.class
            .getProtectionDomain().getCodeSource().getLocation().getPath()
            + File.separator + "test-config"
            + File.separator + TESTPROPERTYFILENAME;
    private static final String TESTPROPERTYRESOURCEPATH =
              File.separator + "test-config"
            + File.separator + TESTPROPERTYFILENAME;





    // corelib runtime ONLY; runtime properties implicitly set
    public static String getDataUrl(){
        if (BooleanUtils.isNotTrue(runtimePropertiesLoaded)) {
            loadRuntimeProps();
        }
        return props.getProperty("data.url");
    }

    // corelib test ONLY; test properties implicitly set
    public static String getTestApi2Url() {
        if (BooleanUtils.isNotFalse(runtimePropertiesLoaded)) {
            loadTestProps();
        }
        return props.getProperty("api2.url");
    }

    // runtime default: if nothing is loaded, load the runtime properties; if test props have been loaded use those
    // corelib runtime & unittest
    public static String getEuropeanaUrl(){
        if (null == runtimePropertiesLoaded){
            loadRuntimeProps();
        }
        return props.getProperty("portal.url");
    }

    // no implicit setting of properties below here; so you'll have to make sure this is done from the calling class

    // corelib runtime
    public static String getEURightsUrl(){
        if (null == runtimePropertiesLoaded){
            throw new NullPointerException(REPRIMAND);
        }
        return props.getProperty("portal.url") + "rights/";
    }

    // corelib runtime
    public static String getCCUrl(){
        if (null == runtimePropertiesLoaded){
            throw new NullPointerException(REPRIMAND);
        }
        return props.getProperty("cc.url");
    }

    // corelib runtime
    public static String getRightsstatementUrl(){
        if (null == runtimePropertiesLoaded){
            throw new NullPointerException(REPRIMAND);
        }
        return props.getProperty("rightsstatement.url");
    }

    public static void loadRuntimeProps(){
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTYFILENAME)) {
            if (in == null) {
                throw new IOException("error retrieving resource " + PROPERTYFILENAME);
            } else {
                props.load(in);
            }
        } catch (IOException e) {
            LOG.error(PROPERTYFILENAME + " file wasn't found in the currentThread() context", e);
        }
        // Checks if runtimePropertiesLoaded is not true, handling null by returning true.
        if (org.apache.commons.lang.BooleanUtils.isNotTrue(runtimePropertiesLoaded)){
            runtimePropertiesLoaded = Boolean.TRUE;
        }
    }

    public static void loadTestProps(){
        // first try and read within the jar file with getResourceAsStream
        try (InputStream is = StaticPropertyReader.class.getResourceAsStream(TESTPROPERTYRESOURCEPATH)){
            props.load(is);
        } catch (IOException e) {
            LOG.error("IOException reading europeana-test.properties file with getResourceAsStream at: "
                      + TESTPROPERTYRESOURCEPATH, e);
        }

        // if not found, then look on the filesystem
        if (props.isEmpty()){
            File europeanaTestProperties = new File(TESTPROPERTYFILEPATH);
            try (FileInputStream fis = new FileInputStream(europeanaTestProperties)){
                props.load(fis);
            } catch (IOException e) {
                LOG.error("IOException reading europeana-test.properties file with FileInputStream at: "
                          + TESTPROPERTYFILEPATH, e);
            }
        }
        // Checks if runtimePropertiesLoaded is not false, handling null by returning true.
        if (org.apache.commons.lang.BooleanUtils.isNotFalse(runtimePropertiesLoaded)){
            runtimePropertiesLoaded = Boolean.FALSE;
        }
    }
}
