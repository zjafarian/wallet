package base;

import jakarta.persistence.AttributeConverter;


public abstract class AbstractEnumAttributeConverter<EN extends ValueEnum<T>, T> implements AttributeConverter<EN, T> {
    @Override
    public T convertToDatabaseColumn(EN attribute) {
        if (attribute == null)
            return null;
        return attribute.fetchVal();
    }

    @Override
    public EN convertToEntityAttribute(T dbData) {
        if (dbData == null)
            return null;
        return find(dbData);
    }

    protected abstract EN find(T dbData);
}
