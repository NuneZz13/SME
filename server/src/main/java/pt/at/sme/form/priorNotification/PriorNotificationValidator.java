package pt.at.sme.form.priorNotification;


import io.kform.AsyncFormValidator;
import io.kform.LocatedValidationIssue;
import io.kform.Validation;
import lombok.Getter;
import org.springframework.stereotype.Service;
import pt.at.sme.kotlin.priorNotification.PriorNotificationForm;
import pt.at.sme.kotlin.priorNotification.PriorNotificationSchemas;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
@Getter
public class PriorNotificationValidator {

    private final AsyncFormValidator<PriorNotificationForm> formValidator;


    /**
     * Setups server-side only validations
     */
    public PriorNotificationValidator() {

        //TODO[nelson] Efectuar validações ao nivel do servidor
        //Número de identificação fiscal deverá corresponder ao Nif logado em sessão.
        //Os CAEs selecionados tem de coincidir com os CAEs em Cadastto
        //Se for indicador numero OSS, validar que o mesmo está presente no OSS e que corresponde ao NIF em sessão.
        Map<Object, List<Validation<?>>> serverValidations = new LinkedHashMap<>(0);

        formValidator = new AsyncFormValidator<>(
                PriorNotificationSchemas.getPriorNotificationFormSchema(),
                serverValidations
        );
    }


    /**
     * Validates the Marathon Registration form.
     *
     * @param priorNotificationForm Form.
     * @return List of issues if any, or empty list otherwise.
     */
    public List<LocatedValidationIssue> validate(final PriorNotificationForm priorNotificationForm) throws ExecutionException, InterruptedException {
        return formValidator
                .validate(priorNotificationForm)
                .get();
    }

}

