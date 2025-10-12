package com.healthcare.queuesystem.service.Implementation;

import com.healthcare.queuesystem.model.Patient;
import com.healthcare.queuesystem.repository.PatientRepository;
import com.healthcare.queuesystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(String patientId) {
        patientRepository.deleteById(patientId);
    }

    @Override
    public Patient getPatientById(String patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
