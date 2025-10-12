package com.healthcare.queuesystem.dto;

import com.healthcare.queuesystem.model.enums.QueueStatus;

import java.time.LocalDateTime;

public class QueueDto {

    private String queueId;
    private String doctorId;
    private String patientId;
    private String doctorName;  // Additional field for display
    private String patientName; // Additional field for display
    private Integer position;
    private QueueStatus status;
    private LocalDateTime createdAt;
    private Integer estimatedWaitTime; // Additional field for estimated wait time in minutes

    // Default constructor
    public QueueDto() {}

    // Constructor with essential fields
    public QueueDto(String doctorId, String patientId, Integer position) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.position = position;
        this.status = QueueStatus.WAITING; // Default status
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public QueueStatus getStatus() {
        return status;
    }

    public void setStatus(QueueStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getEstimatedWaitTime() {
        return estimatedWaitTime;
    }

    public void setEstimatedWaitTime(Integer estimatedWaitTime) {
        this.estimatedWaitTime = estimatedWaitTime;
    }
}