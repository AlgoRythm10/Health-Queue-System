package com.healthcare.queuesystem.controller;

import com.healthcare.queuesystem.dto.AppointmentDto;
import com.healthcare.queuesystem.model.Appointment;
import com.healthcare.queuesystem.model.Doctor;
import com.healthcare.queuesystem.model.Patient;
import com.healthcare.queuesystem.model.enums.AppointmentStatus;
import com.healthcare.queuesystem.service.AppointmentService;
import com.healthcare.queuesystem.service.DoctorService;
import com.healthcare.queuesystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    // Create a new appointment
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        try {
            Appointment appointment = convertToEntity(appointmentDto);
            Appointment savedAppointment = appointmentService.saveAppointment(appointment);
            AppointmentDto responseDto = convertToDto(savedAppointment);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all appointments
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable String id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            if (appointment != null) {
                AppointmentDto appointmentDto = convertToDto(appointment);
                return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update appointment
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable String id, @RequestBody AppointmentDto appointmentDto) {
        try {
            Appointment existingAppointment = appointmentService.getAppointmentById(id);
            if (existingAppointment != null) {
                appointmentDto.setAppointmentId(id);
                Appointment appointment = convertToEntity(appointmentDto);
                Appointment updatedAppointment = appointmentService.saveAppointment(appointment);
                AppointmentDto responseDto = convertToDto(updatedAppointment);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable String id) {
        try {
            Appointment existingAppointment = appointmentService.getAppointmentById(id);
            if (existingAppointment != null) {
                appointmentService.deleteAppointment(id);
                return new ResponseEntity<>("Appointment deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting appointment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get appointments by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctor(@PathVariable String doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments().stream()
                    .filter(appointment -> appointment.getDoctorId().equals(doctorId))
                    .collect(Collectors.toList());
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get appointments by patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatient(@PathVariable String patientId) {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments().stream()
                    .filter(appointment -> appointment.getPatientId().equals(patientId))
                    .collect(Collectors.toList());
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update appointment status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(@PathVariable String id, @RequestParam AppointmentStatus status) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            if (appointment != null) {
                appointment.setStatus(status);
                Appointment updatedAppointment = appointmentService.saveAppointment(appointment);
                AppointmentDto responseDto = convertToDto(updatedAppointment);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get appointments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments().stream()
                    .filter(appointment -> appointment.getStatus() == status)
                    .collect(Collectors.toList());
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to convert Entity to DTO
    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setPatientId(appointment.getPatientId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setPurpose(appointment.getPurpose());
        dto.setStatus(appointment.getStatus());

        // Set doctor and patient names for better display
        try {
            if (appointment.getDoctorId() != null) {
                Doctor doctor = doctorService.getDoctorById(appointment.getDoctorId());
                if (doctor != null) {
                    dto.setDoctorName(doctor.getName());
                }
            }
            if (appointment.getPatientId() != null) {
                Patient patient = patientService.getPatientById(appointment.getPatientId());
                if (patient != null) {
                    dto.setPatientName(patient.getName());
                }
            }
        } catch (Exception e) {
            // Log error but don't fail the conversion
            System.err.println("Error setting names in appointment DTO: " + e.getMessage());
        }

        return dto;
    }

    // Helper method to convert DTO to Entity
    private Appointment convertToEntity(AppointmentDto dto) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(dto.getAppointmentId());
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setPatientId(dto.getPatientId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setPurpose(dto.getPurpose());
        appointment.setStatus(dto.getStatus());
        return appointment;
    }
}