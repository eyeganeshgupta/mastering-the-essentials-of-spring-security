package io.spring.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.spring.domain.Smartphone;
import io.spring.service.SmartphoneService;

@RestController
@RequestMapping("/api/v1/{tenantId}/smartphones")
public class SmartphoneController {

    private static final Logger logger = LoggerFactory.getLogger(SmartphoneController.class);

    private final SmartphoneService smartphoneService;

    public SmartphoneController(SmartphoneService smartphoneService) {
        this.smartphoneService = smartphoneService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_MANAGER')")
    @PostMapping
    public ResponseEntity<Smartphone> createSmartphone(@PathVariable String tenantId,
                                                       @RequestBody Smartphone smartphone) {
        logger.info("Creating a new smartphone for tenant {}: {}", tenantId, smartphone.getName());
        Smartphone savedSmartphone = smartphoneService.saveSmartphone(smartphone);
        logger.info("Smartphone created with ID: {}", savedSmartphone.getId());
        return new ResponseEntity<>(savedSmartphone, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES', 'PRODUCT_MANAGER')")
    @GetMapping
    public ResponseEntity<List<Smartphone>> getAllSmartphones(@PathVariable String tenantId) {
        logger.info("Fetching all smartphones for tenant {}", tenantId);
        List<Smartphone> smartphones = smartphoneService.getAllSmartphones();
        logger.info("Found {} smartphones for tenant {}", smartphones.size(), tenantId);
        return new ResponseEntity<>(smartphones, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES', 'PRODUCT_MANAGER', 'TEST_ENGINEER')")
    @GetMapping("/{id}")
    public ResponseEntity<Smartphone> getSmartphoneById(@PathVariable String tenantId, @PathVariable Long id) {
        logger.info("Fetching smartphone with ID {} for tenant {}", id, tenantId);
        Optional<Smartphone> smartphone = smartphoneService.getSmartphoneById(id);
        return smartphone
                .map(s -> {
                    logger.info("Smartphone found: {}", s.getName());
                    return new ResponseEntity<>(s, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.warn("Smartphone with ID {} not found for tenant {}", id, tenantId);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Smartphone> updateSmartphone(
            @PathVariable String tenantId,
            @PathVariable Long id,
            @RequestBody Smartphone smartphone) {
        logger.info("Updating smartphone with ID {} for tenant {}", id, tenantId);
        if (!smartphoneService.getSmartphoneById(id).isPresent()) {
            logger.warn("Smartphone with ID {} not found for tenant {}", id, tenantId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        smartphone.setId(id);
        Smartphone updatedSmartphone = smartphoneService.saveSmartphone(smartphone);
        logger.info("Smartphone with ID {} updated", id);
        return new ResponseEntity<>(updatedSmartphone, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSmartphone(@PathVariable String tenantId, @PathVariable Long id) {
        logger.info("Deleting smartphone with ID {} for tenant {}", id, tenantId);
        if (!smartphoneService.getSmartphoneById(id).isPresent()) {
            logger.warn("Smartphone with ID {} not found for tenant {}", id, tenantId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        smartphoneService.deleteSmartphone(id);
        logger.info("Smartphone with ID {} deleted for tenant {}", id, tenantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
