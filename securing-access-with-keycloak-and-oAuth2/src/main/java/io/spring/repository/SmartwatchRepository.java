package io.spring.repository;

import io.spring.domain.Smartwatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmartwatchRepository extends JpaRepository<Smartwatch, Long> {

}
