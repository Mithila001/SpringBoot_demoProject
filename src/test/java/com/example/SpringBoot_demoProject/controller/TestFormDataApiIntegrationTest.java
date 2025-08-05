package com.example.SpringBoot_demoProject.controller;

import com.example.SpringBoot_demoProject.SpringBootDemoProjectApplication;
import com.example.SpringBoot_demoProject.model.TestFormData;
import com.example.SpringBoot_demoProject.repository.TestFormDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for {@link TestFormDataApiController}.
 * These tests load the full Spring application context and use MockMvc to
 * simulate HTTP requests to the controller, interacting with a real
 * (in-memory H2) database.
 */
@SpringBootTest(classes = SpringBootDemoProjectApplication.class) // Loads the complete Spring Boot application context.
@AutoConfigureMockMvc // Auto-configures MockMvc, allowing us to perform HTTP requests without a running server.
@ActiveProfiles("test") // Activates the "test" Spring profile, which configures the in-memory H2 database.
public class TestFormDataApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Used to send mock HTTP requests to the controller.

    @Autowired
    private ObjectMapper objectMapper; // Converts Java objects to JSON and vice-versa for request/response bodies.

    @Autowired
    private TestFormDataRepository testFormDataRepository; // Direct access to the repository for test data setup/teardown.

    /**
     * Set up method executed before each test.
     * It ensures test isolation by clearing the database and populating it with
     * a consistent set of initial data for each test run.
     */
    @BeforeEach
    void setUp() {
        testFormDataRepository.deleteAll(); // Clear all existing data to ensure a clean state.

        // Populate the database with initial test data.
        testFormDataRepository.save(new TestFormData(null, "John Doe"));
        testFormDataRepository.save(new TestFormData(null, "Jane Smith"));
    }

    //------------------------------------------------------------------------------------------------------------------
    // Test Cases
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Tests the GET /api/test-data endpoint.
     * Verifies that all existing TestFormData entries can be retrieved successfully.
     */
    @Test
    void testGetAllFormDataApi() throws Exception {
        mockMvc.perform(get("/api/test-data") // Perform a GET request to the endpoint.
                        .contentType(MediaType.APPLICATION_JSON)) // Set the request content type.
                .andExpect(status().isOk()) // Expect HTTP 200 OK status.
                .andExpect(jsonPath("$", hasSize(2))) // Expect the JSON array to contain two elements.
                .andExpect(jsonPath("$[0].name", is("John Doe"))) // Assert the name of the first element.
                .andExpect(jsonPath("$[1].name", is("Jane Smith"))); // Assert the name of the second element.
    }

    /**
     * Tests the POST /api/test-data endpoint for successful creation.
     * Verifies that a new TestFormData entry can be saved and retrieved.
     */
    @Test
    void testSaveFormDataApi() throws Exception {
        TestFormData newFormData = new TestFormData("Alice Wonderland"); // Create a new TestFormData object.

        mockMvc.perform(post("/api/test-data") // Perform a POST request to the endpoint.
                        .contentType(MediaType.APPLICATION_JSON) // Set request content type to JSON.
                        .content(objectMapper.writeValueAsString(newFormData))) // Convert object to JSON and set as body.
                .andExpect(status().isCreated()) // Expect HTTP 201 Created status.
                .andExpect(jsonPath("$.name", is("Alice Wonderland"))); // Assert the name in the returned JSON.

        // Verify that the total count of entities in the database has increased by one.
        assertEquals(3, testFormDataRepository.count());
    }

    /**
     * Tests the POST /api/test-data endpoint for bad requests due to validation errors.
     * Verifies that sending an invalid TestFormData (e.g., blank name) results in a 400 Bad Request
     * and contains specific error details in the JSON response.
     */
    @Test
    void testSaveFormDataApiBadRequest() throws Exception {
        TestFormData invalidFormData = new TestFormData(); // Create an object with invalid data.
        invalidFormData.setName(""); // Set an invalid (blank) name to trigger @NotBlank validation.

        mockMvc.perform(post("/api/test-data") // Perform a POST request.
                        .contentType(MediaType.APPLICATION_JSON) // Set content type.
                        .content(objectMapper.writeValueAsString(invalidFormData))) // Send invalid data as JSON.
                .andExpect(status().isBadRequest()) // Expect HTTP 400 Bad Request status.
                .andExpect(jsonPath("$.name").exists()); // Expect a JSON path for 'name' indicating a validation error.
    }
}