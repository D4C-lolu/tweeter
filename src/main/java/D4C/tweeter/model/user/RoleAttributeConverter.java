package D4C.tweeter.model.user;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


/**
 * Utility class to convert Role enum to a form that can be stored in the DB
 */
@Converter
public class RoleAttributeConverter implements AttributeConverter<Role, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Role role) {
        if (role == null)
            return null;

        switch (role) {
            case USER:
                return 0;

            case MODERATOR:
                return 1;

            default:
                throw new IllegalArgumentException(role + " not supported.");
        }
    }

    @Override
    public Role convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;

        switch (dbData) {
            case 1:
                return Role.USER;

            case 2:
                return Role.MODERATOR;

            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
