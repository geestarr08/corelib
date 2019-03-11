package eu.europeana.corelib.edm.model.schemaorg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Text implements BaseType {
	
    @JsonIgnore
    private String value;

    public Text() {
        this.value = null;
    }

    public Text(String value) {
        this.value = value;
    }

    @JacksonXmlText
    @Override
    public String toString() {
        return value;
    }

    @JsonIgnore
    @Override
    public String getTypeName() {
        return "Text";
    }
}
