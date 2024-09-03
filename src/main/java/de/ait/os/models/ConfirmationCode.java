package de.ait.os.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 7/23/2024
 * OnlineShop
 *
 * @author Chechkina (AIT TR)
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //идентификатор кода

    @Column(nullable = false, unique = true)
    private String code; //сам код подтверждения

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //ссылка на пользователя (у пользователя м.б. много ссылок подтверждения)

    @Column(nullable = false)
    private LocalDateTime expiredDateTime; //время окончания работы ссылки
}
