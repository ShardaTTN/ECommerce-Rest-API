package com.tothenew.sharda.Service;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter(autoApply = true)
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute == null ? null : String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return dbData == null ? Collections.emptyList() : Arrays.asList(dbData.split(","));
    }
}