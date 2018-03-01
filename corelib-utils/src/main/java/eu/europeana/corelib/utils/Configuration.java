package eu.europeana.corelib.utils;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration {

	@Resource
	private Properties europeanaProperties;

	@Value("#{europeanaProperties['portal.url']}")
	private String portalUrl;

	@Value("#{europeanaProperties['testportal.url']}")
	private String testPortalUrl;

	@Value("#{europeanaProperties['imageCacheUrl']}")
	private String imageCacheUrl;

	@Value("#{europeanaProperties['api2.url']}")
	private String api2Url;

	@Value("#{europeanaProperties['api.rowLimit']}")
	private int apiRowLimit;

	@Value("#{europeanaProperties['data.url']}")
	private String dataUrl;

	@Value("#{europeanaProperties['cc.url']}")
	private String ccUrl;

	@Value("#{europeanaProperties['rightsstatement.url']}")
	private String rightsstatementUrl;


	//	TODO consider removing, is ONLY available in europeana-test.properties
//	@Value("#{europeanaProperties['portal.bing.translate.key']}")
//	private String bingTranslateId;

	// Google Field Trip channel attributes
	private Map<String, String> gftChannelAttributes;

	public String getPortalUrl() {
		return portalUrl;
	}

	public String getTestPortalUrl() {
		return testPortalUrl;
	}

	public String getApi2Url() {
		return api2Url;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public String getEURightsUrl() {
		return portalUrl + "rights/";
	}

	public String getCCUrl() {
		return ccUrl;
	}

	public String getRightsstatementUrl() {
		return rightsstatementUrl;
	}

	public String getImageCacheUrl() {
		return imageCacheUrl;
	}

	// Google Field Trip attribute getter
	public Map<String, String> getGftChannelAttributes(String channel) {
		gftChannelAttributes = new HashMap<String, String>();
		int i = 1;
		while (europeanaProperties.containsKey("gft.channel." + channel + "." + i)) {
			String[] parts = europeanaProperties.getProperty("gft.channel." + channel + "." + i).split("=", 2);
			gftChannelAttributes.put(parts[0].trim(), parts[1].trim());
			i++;
		}
		return gftChannelAttributes;
	}

	public int getApiRowLimit() {
		return apiRowLimit;
	}

	// TODO only ever called via test
//	@Deprecated
//	public String getBingTranslateId() {
//		return bingTranslateId;
//	}

}
