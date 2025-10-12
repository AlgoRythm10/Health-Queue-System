package com.healthcare.queuesystem.controller;

import com.healthcare.queuesystem.dto.PatientDto;
import com.healthcare.queuesystem.model.Patient;
import com.healthcare.queuesystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Create a new patient
    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto) {
        try {
            Patient patient = convertToEntity(patientDto);
            Patient savedPatient = patientService.savePatient(patient);
            PatientDto responseDto = convertToDto(savedPatient);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all patients
    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            List<PatientDto> patientDtos = patients.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(patientDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable String id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient != null) {
                PatientDto patientDto = convertToDto(patient);
                return new ResponseEntity<>(patientDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update patient
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable String id, @RequestBody PatientDto patientDto) {
        try {
            Patient existingPatient = patientService.getPatientById(id);
            if (existingPatient != null) {
                patientDto.setPatientId(id);
                Patient patient = convertToEntity(patientDto);
                Patient updatedPatient = patientService.savePatient(patient);
                PatientDto responseDto = convertToDto(updatedPatient);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete patient
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable String id) {
        try {
            Patient existingPatient = patientService.getPatientById(id);
            if (existingPatient != null) {
                patientService.deletePatient(id);
                return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting patient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to convert Entity to DTO
    private PatientDto convertToDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setAge(patient.getAge());
        dto.setGender(patient.getGender());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setAddress(patient.getAddress());
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getEmail());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setAppointmentIds(patient.getAppointmentIds());
        dto.setQueueEntryIds(patient.getQueueEntryIds());
        return dto;
    }

    // Helper method to convert DTO to Entity
    private Patient convertToEntity(PatientDto dto) {
        Patient patient = new Patient();
        patient.setPatientId(dto.getPatientId());
        patient.setName(dto.getName());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setAddress(dto.getAddress());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setAppointmentIds(dto.getAppointmentIds());
        patient.setQueueEntryIds(dto.getQueueEntryIds());
        return patient;
    }
}