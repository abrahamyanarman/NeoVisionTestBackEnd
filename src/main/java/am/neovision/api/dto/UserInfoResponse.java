package am.neovision.api.dto;


import am.neovision.api.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String country;
    private String address;
    private boolean activated;
    private long userId;
    private List<Role> roles;
    private String token;

}
