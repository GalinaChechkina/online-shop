package de.ait.os.repositories;

import de.ait.os.models.ConfirmationCode;
import de.ait.os.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // найдем всех пользователей, у кот. среди кодов есть code
    Optional<User> findFirstByCodesContains(ConfirmationCode code);
}

