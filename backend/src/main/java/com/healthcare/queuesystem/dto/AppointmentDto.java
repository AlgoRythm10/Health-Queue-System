package com.healthcare.queuesystem.dto;

import com.healthcare.queuesystem.model.enums.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentDto {

    private String appointmentId;
    private String doctorId;
    private String patientId;
    private String doctorName;  // Additional field for display
    private String patientName; // Additional field for display
    private LocalDateTime appointmentDate;
    private String purpose;
    private AppointmentStatus status;

    // Default constructor
    public AppointmentDto() {}

    // Constructor with essential fields
    public AppointmentDto(String doctorId, String patientId, LocalDateTime appointmentDate, String purpose) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.purpose = purpose;
        this.status = AppointmentStatus.SCHEDULED; // Default status
    }

    // Getters and Setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}