package com.healthcare.queuesystem.service;

import com.healthcare.queuesystem.model.Patient;
import java.util.List;

public interface PatientService {
    Patient savePatient(Patient patient);
    void deletePatient(String patientId);
    Patient getPatientById(String patientId);
    List<Patient> getAllPatients();
}
