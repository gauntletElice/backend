package com.example.elice.focus.repository;

import com.example.elice.focus.domain.Focus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FocusRepository extends JpaRepository<Focus, Long> {
}
