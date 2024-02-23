package service.wallet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import model.Financier;
import model.WalletCardType;

@Data
public class WalletCardRequest {

    @NotNull
    private Long walletId;

    @NotNull
    private Long balance;

    @NotBlank
    private String title;

    private WalletCardType walletCardType;

    private FinancierRequest financierRequest;

}
