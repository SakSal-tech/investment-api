package com.sakhiya.investment.portfoliomanagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

// This annotation tells JPA that this class is a converter for entity attributes
@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String, Double>, String> {

    // ObjectMapper is used to convert Java objects to JSON strings and back
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts the Map<String, Double> (entity attribute) to a JSON string (database column)
     * @param attribute the Map from the entity
     * @return a JSON string to store in the database
     */
    @Override
    public String convertToDatabaseColumn(Map<String, Double> attribute) {
        // If the map is null or empty, we store an empty JSON object "{}"
        if (attribute == null || attribute.isEmpty()) {
            return "{}";
        }

        try {
            // Use Jackson to convert the Map to a JSON string
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // If conversion fails, throw an exception so we know something went wrong
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    /**
     * Converts a JSON string (from database column) back to a Map<String, Double> (entity attribute)
     * @param dbData the JSON string from the database
     * @return a Map<String, Double> to use in the entity
     */
    @Override
    public Map<String, Double> convertToEntityAttribute(String dbData) {
        // If the database value is null or empty, return an empty map
        if (dbData == null || dbData.isEmpty()) {
            return new HashMap<>();
        }

        try {
            // Use Jackson TypeReference for type safety
            return objectMapper.readValue(dbData, new TypeReference<Map<String, Double>>(){});
        } catch (JsonProcessingException e) {
            // If conversion fails, throw an exception
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
