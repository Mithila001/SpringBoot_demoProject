package com.example.SpringBoot_demoProject.service;

import com.example.SpringBoot_demoProject.model.TestFormData;
import com.example.SpringBoot_demoProject.repository.TestFormDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TestFormDataServiceImpl} using Mockito.
 * These tests focus on the business logic of the service layer in isolation,
 * by mocking its dependencies (e.g., TestFormDataRepository).
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito annotations for JUnit 5.
public class TestFormDataServiceUnitTest {

    @Mock // Creates a mock instance of TestFormDataRepository.
    private TestFormDataRepository testFormDataRepository;

    @InjectMocks // Injects the mock(s) into TestFormDataServiceImpl.
    private TestFormDataServiceImpl testFormDataService;

    // Test data instances to be used across multiple test methods.
    private TestFormData formData1;
    private TestFormData formData2;

    /**
     * Set up method executed before each test case.
     * Initializes common test data for consistent test execution.
     */
    @BeforeEach
    void setUp() {
        // Initialize the first test data object.
        formData1 = new TestFormData();
        formData1.setId(1L);
        formData1.setName("TestName1");

        // Initialize the second test data object.
        formData2 = new TestFormData();
        formData2.setId(2L);
        formData2.setName("TestName2");
    }

    //------------------------------------------------------------------------------------------------------------------
    // Test Cases for Service Methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Tests the {@code saveTestFormData} method.
     * Verifies that the service correctly calls the repository's save method
     * and returns the saved entity.
     */
    @Test
    void testSaveFormData() {
        // Given: Configure the mock repository's save method to return the input object.
        when(testFormDataRepository.save(formData1)).thenReturn(formData1);

        // When: Call the service method under test.
        TestFormData savedData = testFormDataService.saveTestFormData(formData1);

        // Then: Assertions to verify the outcome and mock interactions.
        assertNotNull(savedData, "Saved data should not be null.");
        assertEquals(formData1.getName(), savedData.getName(), "Saved data name should match input name.");
        // Verify that the repository's save method was invoked exactly once with formData1.
        verify(testFormDataRepository, times(1)).save(formData1);
    }

    /**
     * Tests the {@code getAllTestFormData} method.
     * Verifies that the service retrieves all data from the repository correctly.
     */
    @Test
    void testGetAllFormData() {
        // Given: Configure the mock repository's findAll method to return a predefined list of data.
        List<TestFormData> allData = Arrays.asList(formData1, formData2);
        when(testFormDataRepository.findAll()).thenReturn(allData);

        // When: Call the service method.
        List<TestFormData> retrievedData = testFormDataService.getAllTestFormData();

        // Then: Assertions to verify the returned list.
        assertNotNull(retrievedData, "Retrieved data list should not be null.");
        assertEquals(2, retrievedData.size(), "Retrieved list should contain 2 elements.");
        assertTrue(retrievedData.contains(formData1), "Retrieved list should contain formData1.");
        assertTrue(retrievedData.contains(formData2), "Retrieved list should contain formData2.");
        // Verify that the repository's findAll method was invoked exactly once.
        verify(testFormDataRepository, times(1)).findAll();
    }

    /**
     * Tests the {@code getTestFormDataById} method when the data is found.
     * Verifies that the service correctly retrieves an Optional containing the data.
     */
    @Test
    void testGetFormDataByIdFound() {
        // Given: Configure the mock repository's findById to return an Optional containing formData1.
        when(testFormDataRepository.findById(1L)).thenReturn(Optional.of(formData1));

        // When: Call the service method.
        Optional<TestFormData> foundData = testFormDataService.getTestFormDataById(1L);

        // Then: Assertions.
        assertTrue(foundData.isPresent(), "Data should be present for ID 1L.");
        assertEquals(formData1.getName(), foundData.get().getName(), "Found data name should match formData1's name.");
        // Verify that the repository's findById method was invoked exactly once with ID 1L.
        verify(testFormDataRepository, times(1)).findById(1L);
    }

    /**
     * Tests the {@code getTestFormDataById} method when the data is not found.
     * Verifies that the service returns an empty Optional.
     */
    @Test
    void testGetFormDataByIdNotFound() {
        // Given: Configure the mock repository's findById to return an empty Optional for an non-existent ID.
        when(testFormDataRepository.findById(3L)).thenReturn(Optional.empty());

        // When: Call the service method.
        Optional<TestFormData> foundData = testFormDataService.getTestFormDataById(3L);

        // Then: Assertions.
        assertFalse(foundData.isPresent(), "Data should not be present for ID 3L.");
        // Verify that the repository's findById method was invoked exactly once with ID 3L.
        verify(testFormDataRepository, times(1)).findById(3L);
    }

    /**
     * Tests the {@code deleteTestFormData} method.
     * Verifies that the service correctly calls the repository's deleteById method.
     */
    @Test
    void testDeleteFormData() {
        // When: Call the service method.
        testFormDataService.deleteTestFormData(1L);

        // Then: Verify that the repository's deleteById method was invoked exactly once with ID 1L.
        // No return value to assert for void method, only verification of interaction.
        verify(testFormDataRepository, times(1)).deleteById(1L);
    }
}