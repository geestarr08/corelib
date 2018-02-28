package eu.europeana.corelib.web.service;

import eu.europeana.corelib.domain.MediaFile;
import eu.europeana.corelib.service.MediaStorageClient;

import javax.annotation.Resource;


public class MediaStorageService {

    @Resource(name = "corelib_mediaStorageClient")
    private MediaStorageClient mediaStorageClient;

    /**
     * @see eu.europeana.corelib.service.MediaStorageClient#checkIfExists(String)
     */
    public Boolean checkIfExists(String id) {
        return mediaStorageClient.checkIfExists(id);
    }

    /**
     * @see eu.europeana.corelib.service.MediaStorageClient#retrieve(String, Boolean)
     */
    public MediaFile retrieve(String id, Boolean withContent) {
        return mediaStorageClient.retrieve(id,withContent);
    }

    /**
     * @see eu.europeana.corelib.service.MediaStorageClient#retrieveContent(String)
     */
    public byte[] retrieveContent(String id) {return mediaStorageClient.retrieveContent(id); }

    /**
     * @see eu.europeana.corelib.service.MediaStorageClient#createOrModify(MediaFile)
     */
    public void createOrModify(MediaFile mediaFile) {
        mediaStorageClient.createOrModify(mediaFile);
    }

    /**
     * @see eu.europeana.corelib.service.MediaStorageClient#delete(String)
     */
    public void delete(String id) {
        mediaStorageClient.delete(id);
    }
}
