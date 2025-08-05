package com.example.SpringBoot_demoProject.controller;

import com.example.SpringBoot_demoProject.model.TestFormData;
import com.example.SpringBoot_demoProject.service.TestFormDataService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

/**
 * REST controller for managing TestFormData entities.
 * This controller provides a set of RESTful APIs for CRUD operations.
 * It's designed to be used if the application transitions from Server-Side Rendering (Thymeleaf)
 * to a more API-driven architecture in the future.
 */
@RestController // Marks this class as a REST controller, handling incoming web requests and returning JSON/XML responses.
@RequestMapping("/api/test-data") // Specifies the base URI for all endpoints in this controller.
public class TestFormDataApiController {

    // Dependency injection of the service layer.
    private final TestFormDataService testFormDataService;

    /**
     * Constructor for TestFormDataApiController.
     * Spring automatically injects TestFormDataService.
     * @param testFormDataService The service responsible for business logic related to TestFormData.
     */
    public TestFormDataApiController(TestFormDataService testFormDataService) {
        this.testFormDataService = testFormDataService;
    }

    //------------------------------------------------------------------------------------------------------------------
    // API Endpoints for CRUD Operations
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Handles GET requests to retrieve all test form data.
     * Endpoint: GET /api/test-data
     * @return A ResponseEntity containing a list of TestFormData objects and an HTTP 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<TestFormData>> getAllTestData() {
        List<TestFormData> data = testFormDataService.getAllTestFormData();
        return ResponseEntity.ok(data);
    }

    /**
     * Handles GET requests to retrieve a single test form data by its ID.
     * Endpoint: GET /api/test-data/{id}
     * @param id The unique identifier of the TestFormData.
     * @return A ResponseEntity containing the TestFormData object and HTTP 200 OK if found,
     * or HTTP 404 Not Found if no data with the given ID exists.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestFormData> getTestDataById(@PathVariable Long id) {
        Optional<TestFormData> data = testFormDataService.getTestFormDataById(id);
        // Uses Optional to safely handle the presence or absence of the entity.
        return data.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Handles POST requests to create new test form data.
     * Endpoint: POST /api/test-data
     * @param testFormData The TestFormData object to be created.
     * The @Valid annotation triggers validation checks defined in TestFormData model.
     * The @RequestBody annotation binds the HTTP request body to the TestFormData object.
     * @return A ResponseEntity containing the newly created TestFormData object and an HTTP 201 Created status.
     */
    @PostMapping
    public ResponseEntity<TestFormData> createTestData(@Valid @RequestBody TestFormData testFormData) {
        TestFormData savedData = testFormDataService.saveTestFormData(testFormData);
        return new ResponseEntity<>(savedData, HttpStatus.CREATED);
    }

    /**
     * Handles PUT requests to update existing test form data.
     * Endpoint: PUT /api/test-data/{id}
     * @param id The ID of the TestFormData to be updated, extracted from the URL path.
     * @param testFormData The updated TestFormData object from the request body.
     * @return A ResponseEntity containing the updated TestFormData object and HTTP 200 OK if successful,
     * HTTP 400 Bad Request if the ID in the path doesn't match the ID in the body,
     * or HTTP 404 Not Found if no existing data matches the provided ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestFormData> updateTestData(@PathVariable Long id, @RequestBody TestFormData testFormData) {
        // Basic check to ensure path ID matches body ID for consistency.
        if (!id.equals(testFormData.getId())) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        // Verify if the entity exists before attempting to update it.
        if (testFormDataService.getTestFormDataById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // The save method in the service handles both creation and update logic.
        TestFormData updatedData = testFormDataService.saveTestFormData(testFormData);
        return ResponseEntity.ok(updatedData); // 200 OK
    }

    /**
     * Handles DELETE requests to remove test form data by its ID.
     * Endpoint: DELETE /api/test-data/{id}
     * @param id The ID of the TestFormData to be deleted.
     * @return A ResponseEntity with HTTP 204 No Content if deletion is successful,
     * or HTTP 404 Not Found if the data with the given ID does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestData(@PathVariable Long id) {
        // Check if the entity exists before attempting to delete to provide a meaningful response.
        if (testFormDataService.getTestFormDataById(id).isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        testFormDataService.deleteTestFormData(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    //------------------------------------------------------------------------------------------------------------------
    // Exception Handling
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Global exception handler for validation failures.
     * Catches MethodArgumentNotValidException, which is thrown when @Valid annotation fails.
     * This method transforms validation errors into a structured Map, which Spring then converts to JSON.
     * This ensures client-side applications receive clear error details.
     * @param ex The MethodArgumentNotValidException containing validation errors.
     * @return A Map where keys are field names and values are their corresponding error messages.
     * Returns with an HTTP 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}