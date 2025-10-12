package com.healthcare.queuesystem.repository;

import com.healthcare.queuesystem.model.Appointment;
import com.healthcare.queuesystem.model.enums.AppointmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    // Find appointments by doctor
    List<Appointment> findByDoctorId(String doctorId);

    // Find appointments by patient
    List<Appointment> findByPatientId(String patientId);

    // Find appointments by status
    List<Appointment> findByStatus(AppointmentStatus status);

    // Find appointments by date range
    @Query("{ 'appointmentDateTime' : { $gte: ?0, $lte: ?1 } }")
    List<Appointment> findAppointmentsWithinDateRange(LocalDateTime start, LocalDateTime end);

    // Find pending appointments for a doctor
    @Query("{ 'doctorId' : ?0, 'status' : 'PENDING' }")
    List<Appointment> findPendingAppointmentsByDoctor(String doctorId);
}
