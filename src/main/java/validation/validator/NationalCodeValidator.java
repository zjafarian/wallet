package validation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import utils.Utils;
import validation.annotation.NationalCode;



public class NationalCodeValidator implements ConstraintValidator<NationalCode, String> {

    @Override
    public void initialize(NationalCode parameters) {
        //no-op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (null == value) {
            return true;
        }
        if (value.length() == 0) {
            return true;
        }
        if (value.trim().length() != 10) {
            return false;
        }
        return Utils.validationNationalCode(value.trim());
    }
}
