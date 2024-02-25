package service.profile.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import validation.annotation.NationalCode;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProfileRequest {


    @NotBlank
    private String name;

    @NotBlank
    private String family;

    @Column(name = "mobile")
    private String mobile;

    @NotBlank(message = "کد ملی الزامی هست.")
    @NationalCode(message = "کد ملی معتبر نیست.")
    private String nationalCode;

    @NotBlank
    private String password;
}
