package com.example.SpringBoot_demoProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import com.example.SpringBoot_demoProject.model.TestFormData;
import com.example.SpringBoot_demoProject.service.TestFormDataService;

import java.util.List;

/**
 * Main web controller for handling user requests related to form data using Server-Side Rendering (Thymeleaf).
 * This controller manages navigation, form submission, and data display for the web interface.
 */
@Controller // Marks this class as a Spring MVC controller, handling web requests and returning view names.
public class MainController {

    private final TestFormDataService testFormDataService;

    /**
     * Constructor for MainController.
     * Spring automatically injects the TestFormDataService.
     * @param testFormDataService The service responsible for business logic related to TestFormData.
     */
    public MainController(TestFormDataService testFormDataService) {
        this.testFormDataService = testFormDataService;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Web Page Endpoints
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Handles GET requests for the application's root URL.
     * Displays the home page.
     * Endpoint: GET /
     * @return The name of the Thymeleaf template ("home.html") to render.
     */
    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    /**
     * Handles GET requests for the form input page.
     * Prepares an empty TestFormData object for the form.
     * Endpoint: GET /add-data
     * @param model The Spring Model to pass data to the view.
     * @return The name of the Thymeleaf template ("form_input.html") to render.
     */
    @GetMapping("/add-data")
    public String formInputPage(Model model) {
        model.addAttribute("testFormData", new TestFormData()); // Add an empty object for form binding.
        return "form_input";
    }

    /**
     * Handles GET requests to display all stored table data.
     * Fetches all TestFormData entries and adds them to the model for display.
     * Endpoint: GET /show-table-data
     * @param model The Spring Model to pass data to the view.
     * @return The name of the Thymeleaf template ("show_table_data.html") to render.
     */
    @GetMapping("/show-table-data")
    public String showTableData(Model model) {
        List<TestFormData> allTestData = testFormDataService.getAllTestFormData(); // Retrieve all data from the service.
        model.addAttribute("allTestData", allTestData); // Add the list of data to the model.
        return "show_table_data";
    }

    /**
     * Handles POST requests for submitting form data.
     * Saves the submitted TestFormData object to the database.
     * Endpoint: POST /save-data
     * @param testFormData The TestFormData object bound from the form submission.
     * @return A redirect instructing the browser to go back to the /add-data page,
     * with a 'success' parameter appended to the URL.
     */
    @PostMapping("/save-data")
    public String saveFormData(@ModelAttribute TestFormData testFormData) {
        testFormDataService.saveTestFormData(testFormData); // Delegate saving to the service layer.
        System.out.println("Saved data: " + testFormData.getName()); // Log the saved data's name to console.
        return "redirect:/add-data?success"; // Redirect to prevent double submission and show success.
    }
}