package am.neovision.api.mapper;

import am.neovision.api.dto.UserInfoResponse;
import am.neovision.api.model.User;

import java.util.Locale;
import java.util.function.Function;


public class UserMapper implements Function<User, UserInfoResponse> {
    @Override
    public UserInfoResponse apply(User user) {
        final UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setEmail(user.getEmail().toLowerCase(Locale.ENGLISH));
        userInfo.setUsername(user.getUsername().toLowerCase(Locale.ENGLISH));
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setAddress(user.getAddress());
        userInfo.setCountry(user.getCountry());
        userInfo.setActivated(user.isEnabled());
        userInfo.setUserId(user.getId());
        userInfo.setRoles(user.getRoles());
        return userInfo;
    }
}
