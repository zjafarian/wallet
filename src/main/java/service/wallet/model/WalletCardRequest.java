package service.wallet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import model.Financier;
import model.WalletCardType;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
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
