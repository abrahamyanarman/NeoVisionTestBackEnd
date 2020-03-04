package am.neovision.api.service.impl;


import am.neovision.api.dto.SignUpRequestDto;
import am.neovision.api.dto.SignUpResponseDto;
import am.neovision.api.dto.UserInfoRequest;
import am.neovision.api.dto.UserInfoResponse;
import am.neovision.api.entity.ResponseStatus;
import am.neovision.api.exception.AccountExistsException;
import am.neovision.api.exception.UserNotFoundException;
import am.neovision.api.mapper.SignUpRequestMapper;
import am.neovision.api.mapper.UserMapper;
import am.neovision.api.model.EmailCodes;
import am.neovision.api.model.User;
import am.neovision.api.repository.UserRepository;
import am.neovision.api.service.EmailCodeService;
import am.neovision.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static am.neovision.api.model.Role.ROLE_ADMIN;
import static am.neovision.api.model.Role.ROLE_CLIENT;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCodeService emailCodeService;
    private final SignUpRequestMapper signUpRequestMapper;

    @Override
    @Transactional
    public SignUpResponseDto addUser(final SignUpRequestDto request) throws AccountExistsException {

        if (this.userRepository.existsByEmail(request.getEmail())) {
            log.info(
                    "Calling addUser for {}, {} and getting duplicate email",
                    request.getEmail(),
                    request.getUserName());
            throw new AccountExistsException(ResponseStatus.EMAIL_EXISTS.getMessage());
        }
        if (this.userRepository.existsByUsername(request.getUserName())) {
            log.info(
                    "Calling addUser for {}, {} and getting duplicate username",
                    request.getEmail(),
                    request.getUserName());
            throw new AccountExistsException(ResponseStatus.USER_NAME_EXISTS.getMessage());
        }

        final User user = this.signUpRequestMapper.apply(request);
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        this.userRepository.save(user);

        return new SignUpResponseDto(ResponseStatus.ACCOUNT_CREATED.getMessage());
    }

    @PostConstruct
    private void createAdmin() {
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User user = new User();
            user.setCredentialsNonExpired(true);
            user.setAccountNonLocked(true);
            user.setAccountNonExpired(true);
            user.setEnabled(true);
            user.setAddress("Yerevan");
            user.setCountry("Yerevan");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setEmail("admin@gmail.com");//TODO need to be real email
            user.setPassword(new BCryptPasswordEncoder().encode("1234"));//TODO password for admin
            user.setUsername("admin");
            user.setRoles(new ArrayList<>(Collections.singletonList(ROLE_ADMIN)));
            userRepository.save(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmailOrUserName(final String userName) throws UsernameNotFoundException {
        return this.userRepository
                .findByEmailOrUsername(userName, userName)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Account not found for provided " + userName));
    }

    @Override
    public boolean confirmUser(@NonNull String code) throws UserNotFoundException {
        EmailCodes emailCodes = emailCodeService.getByCode(Long.parseLong(code));
        if (emailCodes != null) {
            String email = emailCodes.getEmail();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                user.setEnabled(true);
                userRepository.save(user);
                return true;
            }
        }
        throw new UserNotFoundException(-1);
    }

    @Override
    public long generateCode(@NonNull String email) {
        return emailCodeService.generateCode(email);
    }

    @Override
    public void checkEmail(String email) throws Exception {
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("user with email " + email + " Not Found");
        }
    }

    @Override
    public void changePassword(String emailCode, String password) throws UserNotFoundException, NumberFormatException {
        if (emailCode != null && !emailCode.equals("")) {
            EmailCodes emailCodes = emailCodeService.getByCode(Long.parseLong(emailCode));
            if (emailCodes != null) {
                String email = emailCodes.getEmail();
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    user.setPassword(passwordEncoder.encode(password));
                    userRepository.save(user);
                    return;
                }
            }
        }
        throw new UserNotFoundException(-1);
    }

    @Override
    public void deleteUser(@NonNull long id) throws UserNotFoundException {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public List<UserInfoResponse> getAll() {
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
         userRepository.findAll().forEach(user -> {
            UserMapper mapper = new UserMapper();
            userInfoResponses.add(mapper.apply(user));
        });

         return userInfoResponses;
    }

    @Override
    public void activateOrDeactivateUser(long id, boolean activationStatus) throws UserNotFoundException {
        if (userRepository.existsById(id)){
            User user = userRepository.getOne(id);
            user.setEnabled(activationStatus);
            userRepository.save(user);
        }else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public void updateUserInfoByUser(UserInfoRequest userInfoRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();//TODO Need to validate fields but unfortunately  have no enough time
        user.setAddress(userInfoRequest.getAddress());
        user.setCountry(userInfoRequest.getCountry());
        user.setFirstName(userInfoRequest.getFirstName());
        user.setLastName(userInfoRequest.getLastName());
        userRepository.save(user);
    }

    @Override
    public void updateUserInfoByAdmin(UserInfoRequest userInfoRequest, Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        user.setFirstName(userInfoRequest.getFirstName());
        user.setLastName(userInfoRequest.getLastName());
        userRepository.save(user);
    }

    public User findById(long id){
        return userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
