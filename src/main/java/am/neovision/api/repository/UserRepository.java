package am.neovision.api.repository;

import am.neovision.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    void deleteById(Long aLong);

    boolean existsByUsername(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrUsername(String email, String userName);
}
