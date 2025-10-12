package com.healthcare.queuesystem.repository;

import com.healthcare.queuesystem.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends MongoRepository<Patient, String> {

    // Find patient by name
    Optional<Patient> findByName(String name);

    // Find patients by phone number
    Optional<Patient> findByPhone(String phone);

    // Find patients by gender
    List<Patient> findByGender(String gender);

    // Find patients by blood group
    List<Patient> findByBloodGroup(String bloodGroup);

    // Search patient by name (case-insensitive)
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Patient> searchPatientsByName(String name);
}
