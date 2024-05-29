package pt.at.sme.rest.response;

import lombok.Getter;

@Getter
public class SuccessRestResponseImpl extends AbstractRestResponse implements SuccessRestResponse {

    private final String redirectUrl;


    public SuccessRestResponseImpl(String msg, String redirectUrl) {
        super(msg);
        this.redirectUrl = redirectUrl;
    }

}
