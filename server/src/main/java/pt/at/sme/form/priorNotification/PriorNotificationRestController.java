package pt.at.sme.form.priorNotification;

import io.kform.LocatedValidationIssue;
import io.kform.ValidationIssues;
import io.kform.tutorial.LocatedValidationIssuesEncoder;
import pt.at.sme.rest.response.util.RestResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.at.sme.kotlin.priorNotification.PriorNotificationForm;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class PriorNotificationRestController {
    private final PriorNotificationValidator validator;


    @PostMapping("/submit-prior-notification-form")
    public ResponseEntity<Object> submitPriorNotificationForm(@RequestBody PriorNotificationForm form) throws ExecutionException, InterruptedException {

        List<LocatedValidationIssue> issues = validator.validate(form);

        if (ValidationIssues.containsErrors(issues)) {

            log.info("Rejected form: {}, due to issues {}", form, issues);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(LocatedValidationIssuesEncoder.encodeLocatedValidationIssues(issues));
        }

        log.info("Accepted form: {}", form);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(RestResponseUtil.generateSuccessfulResponse(
                        "Form is valid",
                        "/success"
                ));

    }
}
