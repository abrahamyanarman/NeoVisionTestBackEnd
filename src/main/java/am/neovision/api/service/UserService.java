package am.neovision.api.service;

import am.neovision.api.dto.SignUpRequestDto;
import am.neovision.api.dto.SignUpResponseDto;
import am.neovision.api.dto.UserInfoRequest;
import am.neovision.api.dto.UserInfoResponse;
import am.neovision.api.exception.AccountExistsException;
import am.neovision.api.exception.UserNotFoundException;
import am.neovision.api.model.User;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    SignUpResponseDto addUser(SignUpRequestDto userRegRequest) throws AccountExistsException;

    User findByEmailOrUserName(String login) throws UsernameNotFoundException;

    boolean confirmUser(@NonNull String code) throws UserNotFoundException;

    long generateCode(@NonNull String username);

    void checkEmail(@NonNull String email) throws Exception;

    void changePassword(@NonNull String emailCode, String password) throws UserNotFoundException;

    void deleteUser(long id) throws UserNotFoundException;

    List<UserInfoResponse> getAll();

    void activateOrDeactivateUser(long id, boolean activationStatus) throws UserNotFoundException;

    void updateUserInfoByUser(UserInfoRequest userInfoRequest, Authentication authentication);

    void updateUserInfoByAdmin(UserInfoRequest userInfoRequest, Long id);

    User findById(long id);
}
