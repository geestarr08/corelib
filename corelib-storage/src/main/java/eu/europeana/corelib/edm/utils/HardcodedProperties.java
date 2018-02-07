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

package eu.europeana.corelib.edm.utils;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by luthien on 07/02/2018.
 *
 * NOTE I created this class after having tried three different ways of accessing europeana.properties without either
 * copying the .properties in this module, creating extra dependencies or other worrisome complications.
 * Since we should be moving away from corelib anyhow this may be the lesser evil.
 */

public class HardcodedProperties {

    @Value( "${portal.url}" )
    public static final String PORTALURL = "https://www.europeana.eu/";
    public static final String API2URL = "https://www.europeana.eu/api";

}
