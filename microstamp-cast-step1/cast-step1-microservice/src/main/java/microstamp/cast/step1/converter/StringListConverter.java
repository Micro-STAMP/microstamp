package microstamp.cast.step1.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<String>> TYPE = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<String> items) {
        if (items == null) return null;
        List<String> cleaned = items.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        if (cleaned.isEmpty()) return null;
        try {
            return MAPPER.writeValueAsString(cleaned);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize list column", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String value) {
        if (value == null || value.isBlank()) return new ArrayList<>();
        String trimmed = value.trim();
        if (trimmed.startsWith("[")) {
            try {
                return new ArrayList<>(MAPPER.readValue(trimmed, TYPE));
            } catch (JsonProcessingException ignored) {
            }
        }
        return new ArrayList<>(List.of(trimmed));
    }
}
