package pt.at.sme.classic;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ClassicRequest {

    private String name;
    private String email;
    private String phoneNumber;
    private Date birthDate;

}
