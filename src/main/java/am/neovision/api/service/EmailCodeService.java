package am.neovision.api.service;

import am.neovision.api.model.EmailCodes;
import org.springframework.lang.NonNull;

public interface EmailCodeService {
    EmailCodes getByCode(@NonNull long code);
    long generateCode(@NonNull String email);
    EmailCodes getByEmail(@NonNull String email);
}
