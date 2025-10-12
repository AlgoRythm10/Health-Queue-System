package com.healthcare.queuesystem.controller;

import com.healthcare.queuesystem.dto.DoctorDto;
import com.healthcare.queuesystem.model.Doctor;
import com.healthcare.queuesystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Create a new doctor
    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto doctorDto) {
        try {
            Doctor doctor = convertToEntity(doctorDto);
            Doctor savedDoctor = doctorService.saveDoctor(doctor);
            DoctorDto responseDto = convertToDto(savedDoctor);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all doctors
    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            List<DoctorDto> doctorDtos = doctors.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(doctorDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable String id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            if (doctor != null) {
                DoctorDto doctorDto = convertToDto(doctor);
                return new ResponseEntity<>(doctorDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update doctor
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable String id, @RequestBody DoctorDto doctorDto) {
        try {
            Doctor existingDoctor = doctorService.getDoctorById(id);
            if (existingDoctor != null) {
                doctorDto.setDoctorId(id);
                Doctor doctor = convertToEntity(doctorDto);
                Doctor updatedDoctor = doctorService.saveDoctor(doctor);
                DoctorDto responseDto = convertToDto(updatedDoctor);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String id) {
        try {
            Doctor existingDoctor = doctorService.getDoctorById(id);
            if (existingDoctor != null) {
                doctorService.deleteDoctor(id);
                return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting doctor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get doctors by specialization
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialization(@PathVariable String specialization) {
        try {
            // Note: You'll need to add this method to your DoctorService and DoctorRepository
            List<Doctor> doctors = doctorService.getAllDoctors().stream()
                    .filter(doctor -> doctor.getSpecialization().equalsIgnoreCase(specialization))
                    .collect(Collectors.toList());
            List<DoctorDto> doctorDtos = doctors.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(doctorDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to convert Entity to DTO
    private DoctorDto convertToDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setName(doctor.getName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setPhone(doctor.getPhone());
        dto.setEmail(doctor.getEmail());
        dto.setDepartment(doctor.getDepartment());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setAvailability(doctor.getAvailability());
        dto.setGender(doctor.getGender());
        dto.setConsultationOption(doctor.getConsultationOption());
        dto.setAppointmentIds(doctor.getAppointmentIds());
        dto.setQueueEntryIds(doctor.getQueueEntryIds());
        return dto;
    }

    // Helper method to convert DTO to Entity
    private Doctor convertToEntity(DoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(dto.getDoctorId());
        doctor.setName(dto.getName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setPhone(dto.getPhone());
        doctor.setEmail(dto.getEmail());
        doctor.setDepartment(dto.getDepartment());
        doctor.setExperienceYears(dto.getExperienceYears());
        doctor.setAvailability(dto.getAvailability());
        doctor.setGender(dto.getGender());
        doctor.setConsultationOption(dto.getConsultationOption());
        doctor.setAppointmentIds(dto.getAppointmentIds());
        doctor.setQueueEntryIds(dto.getQueueEntryIds());
        return doctor;
    }
}