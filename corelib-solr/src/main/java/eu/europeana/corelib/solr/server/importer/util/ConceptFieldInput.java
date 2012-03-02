/*
 * Copyright 2007-2012 The Europeana Foundation
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

package eu.europeana.corelib.solr.server.importer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;

import eu.europeana.corelib.definitions.model.EdmLabel;
import eu.europeana.corelib.solr.entity.ConceptImpl;
import eu.europeana.corelib.solr.server.MongoDBServer;
import eu.europeana.corelib.definitions.jibx.AltLabel;
import eu.europeana.corelib.definitions.jibx.Broader;
import eu.europeana.corelib.definitions.jibx.Concept;
import eu.europeana.corelib.definitions.jibx.Note;
import eu.europeana.corelib.definitions.jibx.PrefLabel;


/**
 * Constructor for Concepts
 * 
 * @author Yorgos.Mamakis@ kb.nl
 * 
 */
public class ConceptFieldInput {
	
	/**
	 * Retrun a SolrInputDocument with the Concept fields filled in
	 * 
	 * @param concept
	 *            The JiBX Entity holding the Concept field values
	 * @param solrInputDocument
	 *            The SolrInputDocument to alter
	 * @return The SolrInputDocument with the filled in values for a Concept
	 */
	public static SolrInputDocument createConceptSolrFields(Concept concept,
			SolrInputDocument solrInputDocument) {
		solrInputDocument.addField(EdmLabel.SKOS_CONCEPT.toString(),
				concept.getAbout());
		if (concept.getAltLabelList() != null) {
			for (AltLabel altLabel : concept.getAltLabelList()) {
				if (altLabel.getLang() != null) {
					solrInputDocument.addField(
							EdmLabel.CC_SKOS_ALT_LABEL.toString() + "."
									+ altLabel.getLang().getLang(),
							altLabel.getString());
				} else {
					solrInputDocument.addField(
							EdmLabel.CC_SKOS_ALT_LABEL.toString(),
							altLabel.getString());
				}
			}
		}
		if (concept.getPrefLabelList() != null) {
			for (PrefLabel prefLabel : concept.getPrefLabelList()) {
				if (prefLabel.getLang() != null) {
					solrInputDocument.addField(
							EdmLabel.CC_SKOS_PREF_LABEL.toString() + "."
									+ prefLabel.getLang().getLang(),
							prefLabel.getString());
				} else {
					solrInputDocument.addField(
							EdmLabel.CC_SKOS_PREF_LABEL.toString(),
							prefLabel.getString());
				}
			}
		}
		if (concept.getBroaderList() != null) {
			for (Broader broader : concept.getBroaderList()) {
				solrInputDocument.addField(EdmLabel.CC_SKOS_BROADER.toString(),
						broader.getBroader());
			}
		}
		if (concept.getNoteList() != null) {
			for (Note note : concept.getNoteList()) {
				solrInputDocument.addField(EdmLabel.CC_SKOS_NOTE.toString(),
						note.getString());
			}
		}
		return solrInputDocument;
	}

	/**
	 * Retrieves a MongoDB Concept Entity
	 * 
	 * @param concept
	 * 			The JiBX Concept Entity that has the field values of the Concept
	 * @param mongoServer
	 * 			The MongoDBServer instance that is going to be used to save the MongoDB Concept
	 * @return The MongoDB Concept Entity
	 */
	public static ConceptImpl createConceptMongoFields(Concept concept,
			MongoDBServer mongoServer) {
		ConceptImpl conceptMongo = new ConceptImpl();

		conceptMongo = (ConceptImpl) mongoServer.searchByAbout(
				ConceptImpl.class, concept.getAbout());
		if (conceptMongo == null) {
			// If it does not exist

			conceptMongo = new ConceptImpl();
			conceptMongo.setAbout(concept.getAbout());

			if (concept.getNoteList() != null) {
				List<String> noteList = new ArrayList<String>();
				for (Note note : concept.getNoteList()) {
					noteList.add(note.getString());
				}
				conceptMongo.setNote(noteList.toArray(new String[noteList
						.size()]));
			}

			if (concept.getBroaderList() != null) {
				List<String> broaderList = new ArrayList<String>();
				for (Broader broader : concept.getBroaderList()) {
					broaderList.add(broader.getBroader());
				}
				conceptMongo.setBroader(broaderList
						.toArray(new String[broaderList.size()]));
			}

			if (concept.getPrefLabelList() != null) {
				Map<String, String> prefLabelMongo = new HashMap<String, String>();
				for (PrefLabel prefLabelJibx : concept.getPrefLabelList()) {
					if (prefLabelJibx.getLang() != null) {
						prefLabelMongo.put(prefLabelJibx.getLang().getLang(),
								prefLabelJibx.getString());
					} else {
						prefLabelMongo.put("def", prefLabelJibx.getString());
					}
				}
				conceptMongo.setPrefLabel(prefLabelMongo);
			}

			if (concept.getAltLabelList() != null) {
				Map<String, String> altLabelMongo = new HashMap<String, String>();
				for (AltLabel altLabelJibx : concept.getAltLabelList()) {
					if (altLabelJibx.getLang() != null) {
						altLabelMongo.put(altLabelJibx.getLang().getLang(),
								altLabelJibx.getString());
					} else {
						altLabelMongo.put("def", altLabelJibx.getString());
					}
				}
				conceptMongo.setAltLabel(altLabelMongo);
			}
			mongoServer.getDatastore().save(conceptMongo);
		} else {
			// TODO:update concept
		}
		return conceptMongo;
	}
}