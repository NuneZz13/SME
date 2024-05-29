package pt.at.sme.rest.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractRestResponse implements RestResponse {

    protected final String msg;

}
