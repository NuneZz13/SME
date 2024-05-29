package pt.at.sme.rest.response.util;

import pt.at.sme.rest.response.ErrorRestResponse;
import pt.at.sme.rest.response.ErrorRestResponseImpl;
import pt.at.sme.rest.response.SuccessRestResponse;
import pt.at.sme.rest.response.SuccessRestResponseImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class that generates REST responses.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestResponseUtil {


    /**
     * Generates an error response with message.
     *
     * @param msg Message
     * @return Error rest response.
     */
    public static ErrorRestResponse generateErrorResponse(final String msg,
                                                          final String fieldError) {
        return new ErrorRestResponseImpl(msg, fieldError);
    }


    /**
     * Generates a successful response with message and redirect URL.
     *
     * @param msg         Message
     * @param redirectUrl Redirect URL
     * @return Succcessful rest response.
     */
    public static SuccessRestResponse generateSuccessfulResponse(final String msg,
                                                                 final String redirectUrl) {
        return new SuccessRestResponseImpl(msg, redirectUrl);
    }

}
