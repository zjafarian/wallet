package service.wallet.model;

import jakarta.persistence.Column;
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
public class FinancierRequest {

    @NotBlank
    private String name;

}
