package model;

import base.ValueEnum;

public enum WalletCardType implements ValueEnum<Integer> {

    DebitCard(0),
    CreditCard(1);

    private final Integer code;

    WalletCardType(Integer code) {
        this.code = code;
    }

    public static WalletCardType valueOf(Integer code) {
        return switch (code) {
            case 0 -> WalletCardType.DebitCard;
            case 1 -> WalletCardType.CreditCard;

            default -> null;
        };
    }

    @Override
    public Integer fetchVal() {
        return this.code;
    }
}
