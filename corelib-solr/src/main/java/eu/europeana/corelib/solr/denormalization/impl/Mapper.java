package eu.europeana.corelib.solr.denormalization.impl;

import java.util.Map.Entry;
import java.util.Set;

import com.google.code.morphia.query.Query;

import eu.europeana.corelib.definitions.model.EdmLabel;
import eu.europeana.corelib.solr.denormalization.ControlledVocabulary;
/**
 * Mapper class for Controlled Vocabularies
 * @author yorgos.mamakis@ kb.nl
 *
 */
public class Mapper {

	
	private VocabularyMongoServer mongoServer;
	
	private ControlledVocabulary controlledVocabulary;
	
	public Mapper(){
		
	}
	public Mapper(ControlledVocabulary controlledVocabulary, VocabularyMongoServer mongoServer){
		this.controlledVocabulary=controlledVocabulary;
		this.mongoServer = mongoServer;
	}
	
	/**
	 * Mapping function between a Europeana Field and an Element of the Controlled Vocabulary
	 * @param field The ControlledVocabulary Field
	 * @param europeanaField The EuropeanaField
	 */
	public void mapField(String field, EdmLabel europeanaField){
		if(controlledVocabulary!=null){
		controlledVocabulary.setMappedField(field, europeanaField);
		}
	}
	
	/**
	 * Method saving the mapping of the controlled vocabulary
	 */
	public void saveMapping(){
		if(controlledVocabulary!=null){
		sanitize(controlledVocabulary);
		mongoServer.getDatastore().save(controlledVocabulary);
		}
	}
	
	/**
	 * Method removing a vocabulary by name
	 * @param vocabularyName The vocabulary name to remove
	 */
	public void removeVocabulary(String vocabularyName){
		Query<ControlledVocabularyImpl> query = mongoServer.getDatastore().find(ControlledVocabularyImpl.class).filter("name", vocabularyName);
		mongoServer.getDatastore().delete(query);
	}
	
	/**
	 * Sanitizing method that removes unmapped fields (the controlled vocabulary is initialized with full mappings)
	 * @param controlledVocabulary The controlled vocabulary to sanitize
	 */
	private void sanitize(ControlledVocabulary controlledVocabulary){
		Set<Entry<String,EdmLabel>> elements = controlledVocabulary.getElements().entrySet();
		while(elements.iterator().hasNext()){
			Entry<String,EdmLabel> entry = elements.iterator().next();
			if(entry.getValue()==null){
				elements.iterator().remove();
			}
		}
	}
}