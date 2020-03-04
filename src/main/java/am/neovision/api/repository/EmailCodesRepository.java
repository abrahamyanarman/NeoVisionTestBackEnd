package am.neovision.api.repository;

import am.neovision.api.model.EmailCodes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCodesRepository extends JpaRepository<EmailCodes, Long> {
    Optional<EmailCodes> findByCode(long code);

    Optional<EmailCodes> findByEmail(String email);
}
