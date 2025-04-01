package io.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.spring.domain.Earbud;

public interface EarbudRepository extends JpaRepository<Earbud, Long> {

}
