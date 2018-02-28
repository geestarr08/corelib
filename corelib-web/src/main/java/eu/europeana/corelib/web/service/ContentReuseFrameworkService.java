package eu.europeana.corelib.web.service;

import eu.europeana.harvester.client.HarvesterClient;
import eu.europeana.harvester.domain.SourceDocumentReferenceMetaInfo;

import javax.annotation.Resource;

/**
 * @deprecated
 * CRF is not accessed directly by API and Metis
 */
@Deprecated
public class ContentReuseFrameworkService {

	@Resource
	private HarvesterClient harvesterClient;

	public ContentReuseFrameworkService() {}

	public SourceDocumentReferenceMetaInfo getMetadata(String url) {
		return harvesterClient.retrieveMetaInfoByUrl(url);
	}
}
