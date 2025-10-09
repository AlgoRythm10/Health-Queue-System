package com.healthcare.queuesystem.service;

import com.healthcare.queuesystem.model.Appointment;
import java.util.List;

public interface AppointmentService {

    Appointment saveAppointment(Appointment appointment);

    void deleteAppointment(String appointmentId);

    Appointment getAppointmentById(String appointmentId);

    List<Appointment> getAllAppointments();
}
