package com.example.sidemanagementbe.resume.infrastructure.converter;

import com.example.sidemanagementbe.resume.entity.value.TechnologyStacks;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class TechnologyStackConverter implements AttributeConverter<TechnologyStacks, String> {

    @Override
    public String convertToDatabaseColumn(TechnologyStacks attribute) {
        return null;
    }

    @Override
    public TechnologyStacks convertToEntityAttribute(String dbData) {
        return null;
    }
}