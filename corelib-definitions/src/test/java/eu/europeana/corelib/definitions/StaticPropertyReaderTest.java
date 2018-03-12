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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by luthien on 07/03/2018.
 */
public class StaticPropertyReaderTest {

    @Test
    public void testFetchApi2Url() {
        String expected = "http://localhost:8080/api/";
        StaticPropertyReader.loadTestProps();
        String returned = StaticPropertyReader.getTestApi2Url();
        assertEquals(expected, returned);
    }

}
