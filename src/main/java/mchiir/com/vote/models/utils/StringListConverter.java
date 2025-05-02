package mchiir.com.vote.models.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return list == null || list.isEmpty()
                ? ""
                : String.join(",", list);
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
        return data == null || data.isEmpty()
                ? List.of()
                : Arrays.stream(data.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}

