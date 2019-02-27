package eu.europeana.corelib.edm.model.schemaorg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Root node used for serializing multiple schema.org things
 * @author Patrick Ehlert
 * Created on 27-02-2019
 */
@JsonPropertyOrder({"@context", "@graph"})
public class GraphRoot {

    @JsonProperty("@graph")
    private List<Thing> things;

    public GraphRoot() {
        this.things = new ArrayList<>();
    }

    public GraphRoot(List<Thing> things) {
        this.things = things;
    }

    /**
     * @return list of all things
     */
    public List<Thing> getThings() {
        return things;
    }

    /**
     * Add a schema.org Thing to the list of objects to serialize
     * @param thing
     * @return true if the thing was added
     */
    public boolean addThing(Thing thing) {
        return things.add(thing);
    }
}
