package io.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.spring.domain.Smartphone;
import io.spring.repository.SmartphoneRepository;

@Service
public class SmartphoneService {

    private final SmartphoneRepository smartphoneRepository;

    public SmartphoneService(SmartphoneRepository smartphoneRepository) {
        this.smartphoneRepository = smartphoneRepository;
    }

    public Smartphone saveSmartphone(Smartphone smartphone) {
        return smartphoneRepository.save(smartphone);
    }

    public List<Smartphone> getAllSmartphones() {
        return smartphoneRepository.findAll();
    }

    public Optional<Smartphone> getSmartphoneById(Long id) {
        return smartphoneRepository.findById(id);
    }

    public void deleteSmartphone(Long id) {
        smartphoneRepository.deleteById(id);
    }
}
