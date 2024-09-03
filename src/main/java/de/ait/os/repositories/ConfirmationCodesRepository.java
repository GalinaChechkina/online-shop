package de.ait.os.repositories;

import de.ait.os.models.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationCodesRepository extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByCodeAndExpiredDateTimeAfter(String code, LocalDateTime now);
}
