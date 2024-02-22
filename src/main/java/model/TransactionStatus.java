package model;

import base.ValueEnum;

public enum TransactionStatus implements ValueEnum<Integer> {
    Create(0),
    Processing(1),
    Failed(2),
    Succeed(3),
    Canceled(4);

    private final Integer code;

    TransactionStatus(Integer code) {
        this.code = code;
    }

    public static TransactionStatus valueOf(Integer code) {
        return switch (code) {
            case 0 -> TransactionStatus.Create;
            case 1 -> TransactionStatus.Processing;
            case 2 -> TransactionStatus.Failed;
            case 3 -> TransactionStatus.Succeed;
            case 4 -> TransactionStatus.Canceled;

            default -> null;
        };
    }

    @Override
    public Integer fetchVal() {
        return this.code;
    }
    }
