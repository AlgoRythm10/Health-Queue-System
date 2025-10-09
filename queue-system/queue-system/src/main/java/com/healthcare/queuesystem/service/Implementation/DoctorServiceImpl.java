package com.healthcare.queuesystem.service.Implementation;

import com.healthcare.queuesystem.model.Doctor;
import com.healthcare.queuesystem.repository.DoctorRepository;
import com.healthcare.queuesystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public void deleteDoctor(String doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public Doctor getDoctorById(String doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
