package model;

import base.ValueEnum;

public enum TransactionStatus implements ValueEnum<Integer> {
    CREATE(0),
    PROCESSING(1),
    SUCCEED(2),
    FAILED(3),
    NOT_VERIFIED(4),
    REVERSE(5);

    private final Integer code;

    TransactionStatus(Integer code) {
        this.code = code;
    }

    public static TransactionStatus valueOf(Integer code) {
        return switch (code) {
            case 0 -> TransactionStatus.CREATE;
            case 1 -> TransactionStatus.PROCESSING;
            case 2 -> TransactionStatus.SUCCEED;
            case 3 -> TransactionStatus.FAILED;
            case 4 -> TransactionStatus.NOT_VERIFIED;
            case 5 -> TransactionStatus.REVERSE;

            default -> null;
        };
    }

    @Override
    public Integer fetchVal() {
        return this.code;
    }
    }
