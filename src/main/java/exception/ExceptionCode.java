package exception;

import base.AbstractEnumAttributeConverter;
import base.ValueEnum;

public enum ExceptionCode implements ValueEnum<Integer> {

    NotDefineFinancier(100010),
    NotActiveWallet(200010),
    NotActiveWalletCard(300010),
    NotActiveProfile(400010),
    InsufficientFund(500010),
    NotActiveMerchant(600010),
    NotFountWallet(700010);




    private int code;

    ExceptionCode(int code) {
        this.code = code;
    }

    public static ExceptionCode valueOf(int code) {
        return switch (code) {
            case 100010 -> ExceptionCode.NotDefineFinancier;
            case 200010 -> ExceptionCode.NotActiveWallet;
            case 300010 -> ExceptionCode.NotActiveWalletCard;
            case 400010 -> ExceptionCode.NotActiveProfile;
            case 500010 -> ExceptionCode.InsufficientFund;
            case 600010 -> ExceptionCode.NotActiveMerchant;
            case 700010 -> ExceptionCode.NotFountWallet;


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
