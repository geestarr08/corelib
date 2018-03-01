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

package eu.europeana.corelib.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by luthien on 27/02/2018.
 */
public class StaticPropertyReader {

    private static final Logger     LOG   = LogManager.getLogger(StaticPropertyReader.class);
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

    public static String getEuropeanaUrl(){
        return props.getProperty("portal.url");
    }

    public static String getEURightsUrl(){
        return props.getProperty("portal.url") + "rights/";
    }

    public static String getCCUrl(){
        return props.getProperty("cc.url");
    }

    public static String getRightsstatementUrl(){
        return props.getProperty("rightsstatement.url");
    }

    public static String getDataUrl(){
        return props.getProperty("data.url");
    }


}
