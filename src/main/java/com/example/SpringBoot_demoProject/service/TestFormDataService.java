package com.example.SpringBoot_demoProject.service;

import com.example.SpringBoot_demoProject.model.TestFormData;
import java.util.List;
import java.util.Optional;

public interface TestFormDataService {

     TestFormData saveTestFormData(TestFormData testFormData);
     
     List<TestFormData> getAllTestFormData();

     Optional<TestFormData> getTestFormDataById(Long id);

     void deleteTestFormData(Long id);
}