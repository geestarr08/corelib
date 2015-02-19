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
package eu.europeana.corelib.solr.entity;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.google.code.morphia.annotations.Entity;

import eu.europeana.corelib.utils.StringArrayUtils;

/**
 * @see eu.europeana.corelib.definitions.edm.entity.Timespan
 * 
 * @author Yorgos.Mamakis@ kb.nl
 */
@JsonSerialize(include = Inclusion.NON_EMPTY)
@Entity("Timespan")
public class TimespanImpl extends ContextualClassImpl implements
		eu.europeana.corelib.definitions.edm.entity.Timespan {

	private Map<String,List<String>> begin;
	private Map<String,List<String>> end;
	private Map<String,List<String>> isPartOf;
	private Map<String,List<String>> dctermsHasPart;

	private String[] owlSameAs;

	@Override
	public Map<String,List<String>> getBegin() {
		return this.begin;
	}

	@Override
	public Map<String,List<String>> getEnd() {
		return this.end;
	}

	@Override
	public Map<String,List<String>> getIsPartOf() {
		return this.isPartOf;
	}

	@Override
	public void setBegin(Map<String,List<String>> begin) {
		this.begin = begin;
	}

	@Override
	public void setEnd(Map<String,List<String>> end) {
		this.end = end;
	}

	@Override
	public void setIsPartOf(Map<String,List<String>> isPartOf) {
		this.isPartOf = isPartOf;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null){
			return false;
		}
		if (o.getClass() == this.getClass()){
			return this.getAbout().equals(((TimespanImpl) o).getAbout());
		}
		return false;
	}

	@Override
	public int hashCode(){ 
		return this.getAbout().hashCode();
	}

	@Override
	public Map<String,List<String>> getDctermsHasPart() {
		return this.dctermsHasPart;
	}

	@Override
	public void setDctermsHasPart(Map<String,List<String>> dctermsHasPart) {
		this.dctermsHasPart = dctermsHasPart;
	}

	@Override
	public String[] getOwlSameAs() {
		return (StringArrayUtils.isNotBlank(owlSameAs) ? this.owlSameAs.clone() : null);
	}

	@Override
	public void setOwlSameAs(String[] owlSameAs) {
		this.owlSameAs = owlSameAs!=null?owlSameAs.clone():null;
	}

	
}