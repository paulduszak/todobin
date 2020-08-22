package in.todob.todobin.authentication.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TokenResponse {
    private String token;
}
