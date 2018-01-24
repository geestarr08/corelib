package eu.europeana.corelib.web.service.impl;

import eu.europeana.corelib.domain.MediaFile;
import eu.europeana.corelib.service.MediaStorageClient;
import eu.europeana.corelib.web.service.MediaStorageService;

import javax.annotation.Resource;

public class BluemixS3ServiceImpl implements MediaStorageService {

    @Resource(name = "bluemix_S3Client")
    private MediaStorageClient mediaStorageClient;

    /**
     * @see MediaStorageClient#checkIfExists(String)
     */
    @Override
    public Boolean checkIfExists(String id) {
        return mediaStorageClient.checkIfExists(id);
    }

    /**
     * @see MediaStorageClient#retrieve(String, Boolean)
     */
    @Override
    public MediaFile retrieve(String id, Boolean withContent) {
        return mediaStorageClient.retrieve(id,withContent);
    }

    /**
     * @see MediaStorageClient#retrieveContent(String)
     */
    @Override
    public byte[] retrieveContent(String id) {return mediaStorageClient.retrieveContent(id); }

    /**
     * @see MediaStorageClient#createOrModify(MediaFile)
     */
    @Override
    public void createOrModify(MediaFile mediaFile) {
        mediaStorageClient.createOrModify(mediaFile);
    }

    /**
     * @see MediaStorageClient#delete(String)
     */
    @Override
    public void delete(String id) {
        mediaStorageClient.delete(id);
    }
}
