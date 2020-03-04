package am.neovision.api.service.impl;

import am.neovision.api.model.EmailCodes;
import am.neovision.api.repository.EmailCodesRepository;
import am.neovision.api.service.EmailCodeService;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailCodeServiceImpl implements EmailCodeService {

    private EmailCodesRepository emailCodesRepository;

    public EmailCodeServiceImpl(EmailCodesRepository emailCodesRepository) {
        this.emailCodesRepository = emailCodesRepository;
    }

    @Override
    public EmailCodes getByCode(long code) {
        Optional<EmailCodes> emailCodes = emailCodesRepository.findByCode(code);
        if (emailCodes.isPresent()) {
            emailCodesRepository.delete(emailCodes.get());
            return emailCodes.get();
        }
        return null;
    }

    public long generateCode(@NonNull String email) {
        EmailCodes emailCodes = emailCodesRepository.findByEmail(email).orElse(new EmailCodes());
        long code = (long) (Math.random()*Long.MAX_VALUE);
        emailCodes.setCode(code);
        emailCodes.setEmail(email);
        emailCodesRepository.save(emailCodes);
        return code;
    }

    @Override
    public EmailCodes getByEmail(String email) {
        Optional<EmailCodes> emailCodes = emailCodesRepository.findByEmail(email);
        if (emailCodes.isPresent()) {
            emailCodesRepository.delete(emailCodes.get());
            return emailCodes.get();
        }
        return null;
    }
}
