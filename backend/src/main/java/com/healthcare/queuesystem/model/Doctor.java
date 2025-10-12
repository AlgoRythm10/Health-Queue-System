package com.healthcare.queuesystem.model;

import com.healthcare.queuesystem.model.enums.ConsultationOption;
import com.healthcare.queuesystem.model.enums.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "doctors")
public class Doctor {

    @Id
    private String doctorId;

    private String name;
    private String specialization;
    private String phone;
    private String email;
    private String department;
    private Integer experienceYears;
    private String availability;   // e.g., "Mon-Fri 10am-4pm"

    private Gender gender;
    private ConsultationOption consultationOption;

    private List<String> appointmentIds;
    private List<String> queueEntryIds;

    // --- Getters & Setters ---
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ConsultationOption getConsultationOption() {
        return consultationOption;
    }

    public void setConsultationOption(ConsultationOption consultationOption) {
        this.consultationOption = consultationOption;
    }

    public List<String> getAppointmentIds() {
        return appointmentIds;
    }

    public void setAppointmentIds(List<String> appointmentIds) {
        this.appointmentIds = appointmentIds;
    }

    public List<String> getQueueEntryIds() {
        return queueEntryIds;
    }

    public void setQueueEntryIds(List<String> queueEntryIds) {
        this.queueEntryIds = queueEntryIds;
    }
}
