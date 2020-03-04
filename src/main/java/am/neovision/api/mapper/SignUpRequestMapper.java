package am.neovision.api.mapper;

import am.neovision.api.dto.SignUpRequestDto;
import am.neovision.api.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Function;

import static am.neovision.api.model.Role.ROLE_CLIENT;


@Component
public final class SignUpRequestMapper implements Function<SignUpRequestDto, User> {
    /**
     * Maps {@link SignUpRequestDto} to {@link User}
     *
     * @param signUpRequest the entity
     * @return the User
     */
    @Override
    public User apply(final SignUpRequestDto signUpRequest) {
        final User user = new User();
        user.setEmail(signUpRequest.getEmail().toLowerCase(Locale.ENGLISH));
        user.setLastName(signUpRequest.getLastName());
        user.setFirstName(signUpRequest.getFirstName());
        user.setCountry(signUpRequest.getCountry());
        user.setAddress(signUpRequest.getAddress());
        user.setEnabled(false);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setUsername(signUpRequest.getUserName().toLowerCase(Locale.ENGLISH));
        user.setPassword(signUpRequest.getPassword());
        user.setRoles(new ArrayList<>(Collections.singletonList(ROLE_CLIENT)));
        return user;
    }
}
