package io.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.spring.domain.Smartphone;

public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {

}
