package eu.europeana.corelib.edm.model.schemaorg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class MultilingualString implements BaseType {
    
    @JacksonXmlText
    @JsonProperty(SchemaOrgConstants.VALUE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    @JacksonXmlProperty(isAttribute= true, localName = "xml:language")
    @JsonProperty(SchemaOrgConstants.LANGUAGE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String language;

    public void setValue(String value) {
	this.value = value;
    }

    public void setLanguage(String language) {
	this.language = language;
    }

    public String getValue() {
	return value;
    }

    public String getLanguage() {
	return language;
    }

    @JsonIgnore
    @Override
    public String getTypeName() {
	return "MultilingualString";
    }
}
