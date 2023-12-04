package org.hstack.vmeta.videoMetadata.metadata.narrative;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NarrativeAttributeConverter implements AttributeConverter<Narrative, String> {

    @Override
    public String convertToDatabaseColumn(Narrative n) {
        String res;
        switch (n) {
            case APPLICATION -> res = "APPLICATION";
            case DESCRIPTION -> res = "DESCRIPTION";
            default -> res = null;
        }
        return res;
    }

    @Override
    public Narrative convertToEntityAttribute(String s) {
        if (s.equals("APPLICATION")) {
            return Narrative.APPLICATION;
        } else if (s.equals("DESCRIPTION")) {
            return Narrative.DESCRIPTION;
        }
        return null;
    }
}
