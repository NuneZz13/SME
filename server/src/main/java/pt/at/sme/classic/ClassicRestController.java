package pt.at.sme.classic;

import pt.at.sme.rest.response.RestResponse;
import pt.at.sme.rest.response.util.RestResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Locale;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ClassicRestController {

    private final MessageSource messageSource;

    @Qualifier("classicValidator")
    private final Validator classicValidator;


    @InitBinder(value = "classicRequest")
    public void dataBindingAtualizar(WebDataBinder binder) {
        binder.addValidators(classicValidator);
    }

    @PostMapping("/classic")
    public ResponseEntity<RestResponse> classicSubmit(@Validated @RequestBody ClassicRequest classicRequest,
                                                      BindingResult result) {

        if (result.hasFieldErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResponseUtil.generateErrorResponse(
                            messageSource.getMessage(result.getFieldError().getCodes()[0], null, Locale.getDefault()),
                            result.getFieldError().getField()
                    ));
        }

        return ResponseEntity
                .ok()
                .body(RestResponseUtil.generateSuccessfulResponse(
                        new MessageFormat("Created user with name ''{0}''")
                                .format(new Object[]{classicRequest.getName()}),
                        "/"
                ));

    }

}
