package com.jacek.nutriweek.auth.entity;

import com.jacek.nutriweek.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Instant expirationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    public VerificationToken(String token, User user){
        this.token = token;
        this.user = user;
        this.expirationDate = Instant.now().plus(24, ChronoUnit.HOURS);
    }
}
