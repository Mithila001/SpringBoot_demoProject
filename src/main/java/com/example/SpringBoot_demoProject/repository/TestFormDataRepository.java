package com.example.SpringBoot_demoProject.repository;

import com.example.SpringBoot_demoProject.model.TestFormData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this interface as a Spring Data repository
public interface TestFormDataRepository extends JpaRepository<TestFormData, Long> {
    // JpaRepository provides methods like save(), findAll(), findById(), delete() automatically
}