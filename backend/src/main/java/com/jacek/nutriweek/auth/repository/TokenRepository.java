package com.jacek.nutriweek.auth.repository;

import com.jacek.nutriweek.auth.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    @Query("""
            select vt.user from VerificationToken vt
            where vt.expirationDate < :now and vt.user.enabled = false
            """)
    List<Long> findExpired(@Param("now") Instant now);

    @Modifying
    @Query("delete from VerificationToken vt where vt.expirationDate < :now")
    int deleteExpired(@Param("now") Instant now);
}
