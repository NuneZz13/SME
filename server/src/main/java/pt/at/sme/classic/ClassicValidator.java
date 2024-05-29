package pt.at.sme.classic;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Service
public class ClassicValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ClassicRequest.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        ClassicRequest req = (ClassicRequest) target;

        String name = req.getName();

        if (ObjectUtils.isEmpty(name)) {
            errors.rejectValue("name", "required");
            return;
        }

        String email = req.getEmail();

        if (ObjectUtils.isEmpty(email)) {
            errors.rejectValue("email", "required");
            return;
        }

        String phoneNumber = req.getPhoneNumber();

        if (!ObjectUtils.isEmpty(phoneNumber)) {

            if (!phoneNumber.startsWith("2") && !phoneNumber.startsWith("9")) {
                errors.rejectValue("phoneNumber", "invalidFirstChar");
                return;
            }

            if (phoneNumber.length() != 9) {
                errors.rejectValue("phoneNumber", "invalidLength");
                return;
            }

        }

        Date birthDate = req.getBirthDate();

        if (ObjectUtils.isEmpty(birthDate)) {
            errors.rejectValue("birthDate", "required");
        }

    }

}
