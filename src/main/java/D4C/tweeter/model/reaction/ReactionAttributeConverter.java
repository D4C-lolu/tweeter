package D4C.tweeter.model.reaction;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Utility class to convert Reaction enum to a form that can be stored in the DB
 */
@Converter
public class ReactionAttributeConverter implements AttributeConverter<Reaction, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Reaction reaction) {
        if (reaction == null)
            return null;

        switch (reaction) {
            case LIKE:
                return 0;

            case DISLIKE:
                return 1;

            default:
                throw new IllegalArgumentException(reaction + " not supported.");
        }
    }

    @Override
    public Reaction convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        switch (dbData) {
            case 1:
                return Reaction.LIKE;

            case 2:
                return Reaction.DISLIKE;

            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
