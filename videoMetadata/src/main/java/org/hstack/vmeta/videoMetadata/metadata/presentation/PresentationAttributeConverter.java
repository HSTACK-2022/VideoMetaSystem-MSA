package org.hstack.vmeta.videoMetadata.metadata.presentation;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PresentationAttributeConverter implements AttributeConverter<Presentation, String> {

    @Override
    public String convertToDatabaseColumn(Presentation n) {
        String res;
        switch (n) {
            case DYNAMIC -> res = "DYNAMIC";
            case STATIC -> res = "STATIC";
            default -> res = null;
        }
        return res;
    }

    @Override
    public Presentation convertToEntityAttribute(String s) {
        if (s.equals("DYNAMIC")) {
            return Presentation.DYNAMIC;
        } else if (s.equals("STATIC")) {
            return Presentation.STATIC;
        }
        return null;
    }
}
