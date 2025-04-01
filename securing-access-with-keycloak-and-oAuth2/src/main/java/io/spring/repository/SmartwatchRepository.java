package io.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.spring.domain.Smartwatch;

public interface SmartwatchRepository extends JpaRepository<Smartwatch, Long> {

}
