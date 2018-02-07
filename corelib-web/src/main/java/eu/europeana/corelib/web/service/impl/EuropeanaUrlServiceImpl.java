package eu.europeana.corelib.web.service.impl;

import eu.europeana.corelib.definitions.ApplicationContextContainer;
import eu.europeana.corelib.definitions.model.ThumbSize;
import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.utils.Configuration;
import eu.europeana.corelib.utils.EuropeanaUriUtils;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.utils.UrlBuilder;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

public class EuropeanaUrlServiceImpl implements EuropeanaUrlService {

	// GENERAL PATHS
	String PATH_RECORD 			= "record";

	// PORTAL PATHS
	String PATH_PORTAL_RESOLVE 	= "resolve";

	// API PATHS
	String PATH_API_V1 			= "v1";
	String PATH_API_V2 			= "v2";
	String PATH_API_REDIRECT 	= "redirect";

	// EXTENTIONS
	String EXT_JSON				= ".json";
	String EXT_HTML				= ".html";

	// GENERAL PARAMS
	String PARAM_SEARCH_QUERY 	= "query";
	String PARAM_SEARCH_ROWS 	= "rows";

	// API PARAMS
	String PARAM_API_APIKEY	= "wskey";
	String PARAM_API_V1_SEARCH_QUERY	= "searchTerms";
	String PARAM_API_V1_SEARCH_START	= "startPage";
	
	@Resource
	private Configuration configuration;

	public static EuropeanaUrlService getBeanInstance() {
		return ApplicationContextContainer.getBean(EuropeanaUrlServiceImpl.class);
	}

	@Override
	public UrlBuilder getApi2Redirect(String apikey, String shownAt, String provider, String europeanaId, String profile) {
		UrlBuilder url = new UrlBuilder(configuration.getApi2Url());
		url.addPath(String.valueOf(apikey), PATH_API_REDIRECT).disableTrailingSlash();
		url.addParam("shownAt", shownAt);
		url.addParam("provider", provider);
		url.addParam("id", getPortalResolve(europeanaId));
		url.addParam("profile", profile);
		return url;
	}

	@Override
	public UrlBuilder getThumbnailUrl(String thumbnail, DocType type) {
		if (null == configuration.getImageCacheUrl()) return new UrlBuilder("");
		UrlBuilder url = new UrlBuilder(configuration.getImageCacheUrl());
		if (thumbnail != null) url.addParam("uri", thumbnail.trim());
		url.addParam("size", ThumbSize.LARGE.toString());
		if (type != null) url.addParam("type", type.toString());
		return url;
	}

	@Override
	public String getPortalResolve(String europeanaId) {
		UrlBuilder url = new UrlBuilder(configuration.getPortalUrl());
		url.addPath(PATH_PORTAL_RESOLVE, PATH_RECORD, europeanaId).disableTrailingSlash();
		return url.toString();
	}

	@Override
	public UrlBuilder getPortalRecord(boolean relative, String europeanaId) {
		UrlBuilder url = getPortalHome(relative);
		url.addPath(PATH_RECORD).addPage(europeanaId + EXT_HTML);
		return url;
	}

	@Override
	public UrlBuilder getPortalHome(boolean relative) {
		return relative ? new UrlBuilder("") : new UrlBuilder(configuration.getPortalUrl());
	}

	// NOTE everything below is deprecated: either not used, only in this class (where the calling method is also
	// deprecated), or called by a deprecated API class (e.g. user- or myeuropeana-related)


	@Deprecated
	@Override
	public UrlBuilder getApi1Home(String apikey) {
		UrlBuilder url = new UrlBuilder(configuration.getApi2Url());
		url.addPath(PATH_API_V1);
		url.addParam(PARAM_API_APIKEY, apikey, true);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getApi2Home(String apikey) {
		UrlBuilder url = new UrlBuilder(configuration.getApi2Url());
		url.addPath(PATH_API_V2);
		url.addParam(PARAM_API_APIKEY, apikey, true);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getApi1SearchJson(String apikey, String query, int start) throws UnsupportedEncodingException {
		UrlBuilder url = getApi1Home(apikey);
		url.addPage("search.json");
		url.addParam(PARAM_API_V1_SEARCH_QUERY, query, true);
		url.addParam(PARAM_API_V1_SEARCH_START, start);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getApi2SearchJson(String apikey, String query, String rows) throws UnsupportedEncodingException {
		UrlBuilder url = getApi2Home(apikey);
		url.addPage("search.json");
		url.addParam(PARAM_SEARCH_QUERY, query);
		url.addParam(PARAM_SEARCH_ROWS, rows);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getApi2RecordJson(String apikey, String collectionid, String objectid) {
		UrlBuilder url = getApi2Home(apikey);
		url.addPath(PATH_RECORD, collectionid).addPage(objectid + EXT_JSON);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getApi2RecordJson(String apikey, String europeanaId) {
		return getApi2Record(apikey, europeanaId, EXT_JSON);
	}

	@Deprecated
	@Override
	public UrlBuilder getApi1Record(String apikey, String europeanaId, String extention) {
		UrlBuilder url = getApi1Home(apikey);
		url.addPath(PATH_RECORD).addPage(europeanaId+extention);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getApi2Record(String apikey, String europeanaId, String extention) {
		UrlBuilder url = getApi2Home(apikey);
		url.addPath(PATH_RECORD).addPage(europeanaId+extention);
		return url;
	}

	@Deprecated
	@Override
	public String getPortalResolve(String collectionid, String objectid) {
		UrlBuilder url = new UrlBuilder(configuration.getPortalUrl());
		url.addPath(PATH_PORTAL_RESOLVE, PATH_RECORD, collectionid, objectid).disableTrailingSlash();
		return url.toString();
	}

	@Deprecated
	@Override
	public UrlBuilder getPortalSearch() throws UnsupportedEncodingException {
		return getPortalSearch(true, "search.html", null, null);
	}

	@Deprecated
	@Override
	public UrlBuilder getPortalSearch(boolean relative, String query, String rows) throws UnsupportedEncodingException {
		return getPortalSearch(relative, "search.html", query, rows);
	}

	@Deprecated
	@Override
	public UrlBuilder getPortalSearch(boolean relative, String searchpage, String query, String rows)
			throws UnsupportedEncodingException {
		UrlBuilder url = getPortalHome(relative);
		url.addPage(searchpage);
		url.addParam(PARAM_SEARCH_QUERY, query, true);
		url.addParam(PARAM_SEARCH_ROWS, rows, true);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getPortalRecord(boolean relative, String collectionid, String objectid) {
		UrlBuilder url = getPortalHome(relative);
		url.addPath(PATH_RECORD, collectionid).addPage(objectid + EXT_HTML);
		return url;
	}

	@Deprecated
	@Override
	public UrlBuilder getCanonicalPortalRecord(String europeanaId) {
		UrlBuilder url = new UrlBuilder(configuration.getPortalUrl());
		url.addPath(PATH_RECORD).addPage(europeanaId + EXT_HTML);
		return url;
	}

	@Deprecated
	@Override
	public String extractEuropeanaId(String url) {
		if (StringUtils.contains(url, PATH_RECORD)) {
			url = StringUtils.removeEnd(url, EXT_HTML);
			String recordId = StringUtils.substringAfterLast(url, UrlBuilder.PATH_SEPERATOR);
			url = StringUtils.substringBeforeLast(url, UrlBuilder.PATH_SEPERATOR);
			String collectionId = StringUtils.substringAfterLast(url, UrlBuilder.PATH_SEPERATOR);
			return EuropeanaUriUtils.createEuropeanaId(collectionId, recordId);
		}
		return null;
	}
}
