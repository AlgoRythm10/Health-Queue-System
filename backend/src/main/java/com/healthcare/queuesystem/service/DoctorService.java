package com.healthcare.queuesystem.service;

import com.healthcare.queuesystem.model.Doctor;
import java.util.List;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    void deleteDoctor(String doctorId);
    Doctor getDoctorById(String doctorId);
    List<Doctor> getAllDoctors();
}
