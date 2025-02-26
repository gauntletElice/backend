package com.example.elice.fortuneCookie.repository;

import com.example.elice.fortuneCookie.domain.FortuneCookie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FortuneCookieRepository extends JpaRepository<FortuneCookie, Long> {

    @Query(value = "SELECT * FROM fortune_cookie ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    FortuneCookie findRandomCookie();

}
