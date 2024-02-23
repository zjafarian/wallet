package service.profile.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import validation.annotation.NationalCode;

@Data
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
