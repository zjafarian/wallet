package service.wallet.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import model.Financier;
import model.WalletCardType;

@Data
public class FinancierRequest {

    @NotBlank
    private String name;

}
