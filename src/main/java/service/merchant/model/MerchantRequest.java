package service.merchant.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MerchantRequest {

    @NotBlank
    private String name;
}
