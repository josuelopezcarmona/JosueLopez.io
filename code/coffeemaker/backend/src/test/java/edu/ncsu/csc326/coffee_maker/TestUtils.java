package edu.ncsu.csc326.coffee_maker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for handy utils shared across all of the API tests
 *
 * @author Kai Presler-Marshall
 *
 */
public final class TestUtils {

    /**
     * Jackson is the serializer that Spring Boot itself uses for the REST API, so
     * using it here means the JSON a test expects is built the same way as the JSON
     * the application actually produces.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Utility class; not meant to be instantiated.
     */
    private TestUtils () {
    }

    /**
     * Uses Jackson to serialize a Java object to JSON. Useful for creating JSON
     * representations of our objects when calling API methods.
     *
     * @param obj
     *            to serialize to JSON
     * @return JSON string associated with object
     */
    public static String asJsonString ( final Object obj ) {
        try {
            return MAPPER.writeValueAsString( obj );
        }
        catch ( final JsonProcessingException e ) {
            throw new IllegalArgumentException( "Could not serialize " + obj, e );
        }
    }

}
