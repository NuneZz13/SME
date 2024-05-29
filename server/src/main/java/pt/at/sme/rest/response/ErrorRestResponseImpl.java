package pt.at.sme.rest.response;

import lombok.Getter;

@Getter
public class ErrorRestResponseImpl extends AbstractRestResponse implements ErrorRestResponse {

    private final String fieldError;


    public ErrorRestResponseImpl(String msg, String fieldError) {
        super(msg);
        this.fieldError = fieldError;
    }

}
