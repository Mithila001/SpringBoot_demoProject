package com.example.SpringBoot_demoProject.service;

import com.example.SpringBoot_demoProject.model.TestFormData;
import com.example.SpringBoot_demoProject.repository.TestFormDataRepository;
import org.springframework.stereotype.Service; // Import @Service annotation
import java.util.List;
import java.util.Optional;


@Service

public class TestFormDataServiceImpl implements TestFormDataService {

    private final TestFormDataRepository testFormDataRepository; // Inject the repository

    // Constructor Injection: Spring will automatically provide an instance of TestFormDataRepository
    public TestFormDataServiceImpl(TestFormDataRepository testFormDataRepository) {
        this.testFormDataRepository = testFormDataRepository;
    }

    @Override
    public TestFormData saveTestFormData(TestFormData testFormData) {
        // Here you could add business logic before saving, e.g.,
        // validation, data transformation, logging specific events
        System.out.println("Service layer: Saving data for: " + testFormData.getName()); // Example of service layer logging
        return testFormDataRepository.save(testFormData);
    }

    @Override
    public List<TestFormData> getAllTestFormData() {
        // Here you could add business logic before returning, e.g.,
        // filtering, sorting, or applying security checks
        return testFormDataRepository.findAll();
    }

    @Override
    public Optional<TestFormData> getTestFormDataById(Long id) {
        // Business logic for fetching by ID
        return testFormDataRepository.findById(id);
    }

    @Override
    public void deleteTestFormData(Long id) {
        // Business logic for deletion, e.g.,
        // checking if the entity exists before deleting, logging
        testFormDataRepository.deleteById(id);
    }
}