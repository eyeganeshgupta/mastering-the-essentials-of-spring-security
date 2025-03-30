package io.spring.repository;

import io.spring.domain.Earbud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EarbudRepository extends JpaRepository<Earbud, Long> {

}
