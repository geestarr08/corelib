/*
 * Copyright 2007-2013 The Europeana Foundation
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

package eu.europeana.corelib.definitions.model;

import org.apache.commons.lang.StringUtils;

/**
 * Class represents a set of rights that can be attached to an item in the Europeana website.
 *
 * @author Andy MacLean
 * @author Luthien
 */
public enum RightsOption {


    /* = = = | OPEN LICENCES | = = = = = = = = = = = = = = = = = = = = = = = = = = */

    /**
     * open
     * https://creativecommons.org/publicdomain/mark/1.0/
     */
    CC_NOC(BaseUrls.CC + "publicdomain/mark/", "Public Domain marked", "icon-pd", true),

    /**
     * open
     * https://creativecommons.org/publicdomain/zero
     */
    CC_ZERO(BaseUrls.CC + "publicdomain/zero", "CC0", "icon-cczero", true),

    /**
     * open
     * https://creativecommons.org/licenses/by/
     */
    CC_BY(BaseUrls.CC + "licenses/by/", "CC BY", "icon-cc icon-by",  true),

    /**
     * open
     * https://creativecommons.org/licenses/by-sa/
     */
    CC_BY_SA(BaseUrls.CC + "licenses/by-sa/", "CC BY-SA", "icon-cc icon-by icon-sa", true),


    /* = = = | RESTRICTED LICENCES | = = = = = = = = = = = = = = = = = = = = = = = */

    /**
     * restricted
     * https://creativecommons.org/licenses/by-nc/
     */
    CC_BY_NC(BaseUrls.CC + "licenses/by-nc/", "CC BY-NC", "icon-cc icon-by icon-nceu", true),

    /**
     * restricted
     * https://creativecommons.org/licenses/by-nc-sa/
     */
    CC_BY_NC_SA(BaseUrls.CC + "licenses/by-nc-sa/", "CC BY-NC-SA", "icon-cc icon-by icon-nceu icon-sa", true),

    /**
     * restricted
     * https://creativecommons.org/licenses/by-nc-nd/
     */
    CC_BY_NC_ND(BaseUrls.CC + "licenses/by-nc-nd/", "CC BY-NC-ND", "icon-cc icon-by icon-nceu icon-nd", true),

    /**
     * restricted
     * https://creativecommons.org/licenses/by-nd/
     */
    CC_BY_ND(BaseUrls.CC + "licenses/by-nd/", "CC BY-ND", "icon-cc icon-by icon-nd", true),

    /**
     * restricted
     * https://www.europeana.eu/rights/out-of-copyright-non-commercial/
     */
    EU_OOC_NC(BaseUrls.EU + "out-of-copyright-non-commercial/",
            "Out of copyright - non commercial re-use", "icon-publicdomain icon-nceu", false, true),

    /**
     * restricted
     * http://rightsstatements.org/vocab/InC-EDU/1.0/
     */
    RS_INC_EDU(BaseUrls.RS + "InC-EDU/1.0/", "In copyright - educational user permitted", "", true),

    /**
     * restricted
     * http://rightsstatements.org/vocab/NoC-NC/1.0/
     */
    RS_NOC_NC(BaseUrls.RS + "NoC-NC/1.0/", "No copyright - non-commercial use only", "", true),

    /**
     * restricted
     * http://rightsstatements.org/vocab/NoC-OKLR/1.0/
     */
    RS_NOC_OKLR(BaseUrls.RS + "NoC-OKLR/1.0/", "No copyright - other known legal restrictions", "", true),


    /* = = = | PERMISSION LICENCES | = = = = = = = = = = = = = = = = = = = = = = = */

    /**
     * permission
     * https://www.europeana.eu/rights/rr-f/
     */
    EU_RR_F(BaseUrls.EU + "rr-f/", "Rights Reserved - Free Access", "icon-copyright", false, true),

    /**
     * permission
     * https://www.europeana.eu/rights/rr-p/
     */
    EU_RR_P(BaseUrls.EU + "rr-p/", "Rights Reserved - Paid Access", "icon-copyright", false, true),

    /**
     * permission
     * https://www.europeana.eu/rights/rr-r/
     */
    EU_RR_R(BaseUrls.EU + "rr-r/", "Restricted Access - Rights Reserved", "icon-copyright", false, true),

    /**
     * permission
     * https://www.europeana.eu/rights/unknown/
     */
    EU_U(BaseUrls.EU + "unknown/", "Unknown copyright status", "icon-unknown", false, true),

    /**
     * permission
     * https://www.europeana.eu/rights/test-orphan-work-test/
     */
    EU_ORPHAN(BaseUrls.EU + "test-orphan-work-test/", "Orphan Work", "icon-unknown", false, true),

    /**
     * permission
     * http://rightsstatements.org/vocab/InC/1.0/
     */
    RS_INC(BaseUrls.RS + "InC/1.0/", "In Copyright", "", true),

    /**
     * permission
     * http://rightsstatements.org/vocab/InC-OW-EU/1.0/
     */
    RS_INC_OW_EU(BaseUrls.RS + "InC-OW-EU/1.0/", "In copyright - EU orphan work", "", true),

    /**
     * permission
     * http://rightsstatements.org/vocab/CNE/1.0/
     */
    RS_CNE(BaseUrls.RS + "CNE/1.0/", "Copyright not evaluated", "", true);


    private static class BaseUrls{

        private static final String EU = "https://www.europeana.eu/rights/";
        private static final String CC = "https://creativecommons.org/";
        private static final String RS = "http://rightsstatements.org/vocab/";
    }

   

    private String url = null;
    private String rightsText = null;
    private String rightsIcon = null;
    private boolean showExternalIcon = false;
    private boolean isRelativeUrl = false;
    private String relativeUrl = null;

    /**
     * Constructor for method
     *
     * @param url        Url associated with the rights for the object
     * @param rightsText Text associated with the rights
     * @param rightsIcon Icon associated with the rights
     */
    RightsOption(String url, String rightsText, String rightsIcon, boolean showExternalIconIn) {
        this.url = url;
        this.rightsText = rightsText;
        this.rightsIcon = rightsIcon;
        this.showExternalIcon = showExternalIconIn;
    }

    RightsOption(String url, String rightsText, String rightsIcon, boolean showExternalIconIn, boolean isRelativeUrl) {
        this(url, rightsText, rightsIcon, showExternalIconIn);
        this.isRelativeUrl = isRelativeUrl;
    }

    /**
     * Returns the full Url associated with the rights
     *
     * @return Full url associated with the rights
     */
    public String getUrl() {
        return url;
    }

    public String getRelativeUrl(String portalUrl) {
        if (relativeUrl == null) {
            if (isRelativeUrl) {
                if (!portalUrl.endsWith("/")) {
                    portalUrl += "/";
                }
                relativeUrl = url.replace("https://www.europeana.eu/", portalUrl)
                        .replaceAll("/$", ".html");
            } else {
                relativeUrl = url;
            }
        }
        return relativeUrl;
    }

    /**
     * Return text associated with the rights
     *
     * @return text associated with the results
     */
    public String getRightsText() {
        return rightsText;
    }

    /**
     * Returns the url of the icon associated with the results
     *
     * @return url of icon associated with the results
     */
    public String getRightsIcon() {
        return rightsIcon;
    }

    /**
     * Returns the url of the icon associated with the results
     *
     * @return url of icon associated with the results
     */
    public boolean getShowExternalIcon() {
        return showExternalIcon;
    }

    public static RightsOption safeValueByUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            for (RightsOption option : RightsOption.values()) {
                if (url.contains(option.getUrl())) {
                    return option;
                }
            }
        }
        return null;
    }

    public boolean isRelativeUrl() {
        return isRelativeUrl;
    }
}
