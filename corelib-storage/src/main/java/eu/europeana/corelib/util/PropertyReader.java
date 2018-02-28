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

package eu.europeana.corelib.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

/**
 * Created by luthien on 27/02/2018.
 */
public class PropertyReader {

    private static final Logger     LOG   = LogManager.getLogger(PropertyReader.class);
    private static       Properties props = new Properties();
    private static final String PROPERTIESFILENAME = "europeana.properties";



    static {

        String propertyPath = System.getProperty("user.dir") + File.separator + ".." + File.separator +
                              ".." + File.separator + "api2" + File.separator + "api2-war" + File.separator + "src" +
                              File.separator + "main" + File.separator + "resources" + File.separator + PROPERTIESFILENAME;

        File europeanaProperties = new File(propertyPath);
        try (FileInputStream fis = new FileInputStream(europeanaProperties)){
            props.load(fis);
        } catch (IOException e) {
                LOG.error("IOException reading Properties file", e);
        }
    }

    public static String getEuropeanaURL(){
        return props.getProperty("portal.url");
    }


}
