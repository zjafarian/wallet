package exception;

import base.AbstractEnumAttributeConverter;
import base.ValueEnum;

public enum ExceptionCode implements ValueEnum<Integer> {

    NotDefineFinancier(100010);


    private int code;

    ExceptionCode(int code) {
        this.code = code;
    }

    public static ExceptionCode valueOf(int code) {
        return switch (code) {
            case 100010 -> ExceptionCode.NotDefineFinancier;



            default -> null;
        };
    }

    public int getCode() {
        return code;
    }

    @Override
    public Integer fetchVal() {
        return this.code;
    }

    public static class EnumConverter extends AbstractEnumAttributeConverter<ExceptionCode, Integer> {

        @Override
        public ExceptionCode find(Integer dbData) {
            return ExceptionCode.valueOf(dbData);
        }
    }
}
