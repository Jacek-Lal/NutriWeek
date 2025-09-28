package com.jacek.nutriweek.auth.entity;

import com.jacek.nutriweek.common.BaseEntity;
import com.jacek.nutriweek.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VerificationToken extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String token;

    @NotNull
    @Column(nullable = false)
    private Instant expirationDate;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    public VerificationToken(String token, User user){
        this.token = token;
        this.user = user;
        this.expirationDate = Instant.now().plus(24, ChronoUnit.HOURS);
    }
}
