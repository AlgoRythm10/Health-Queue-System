package com.healthcare.queuesystem.repository;

import com.healthcare.queuesystem.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends MongoRepository<Doctor, String> {

    // Find doctor by name
    Optional<Doctor> findByName(String name);

    // Find doctors by specialization
    List<Doctor> findBySpecialization(String specialization);

    // Find doctors by availability
    @Query("{ 'available' : true }")
    List<Doctor> findAvailableDoctors();

    // Search doctor by name (case-insensitive)
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Doctor> searchDoctorsByName(String name);
}
