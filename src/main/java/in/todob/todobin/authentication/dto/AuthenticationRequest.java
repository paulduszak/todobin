package in.todob.todobin.authentication.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AuthenticationRequest {
    private String username;
    private String password;
}
