package com.healthcare.queuesystem.controller;

import com.healthcare.queuesystem.model.enums.AppointmentStatus;
import com.healthcare.queuesystem.model.enums.QueueStatus;
import com.healthcare.queuesystem.service.AppointmentService;
import com.healthcare.queuesystem.service.DoctorService;
import com.healthcare.queuesystem.service.PatientService;
import com.healthcare.queuesystem.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private QueueService queueService;

    // Get dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // Count totals
            stats.put("totalPatients", patientService.getAllPatients().size());
            stats.put("totalDoctors", doctorService.getAllDoctors().size());
            stats.put("totalAppointments", appointmentService.getAllAppointments().size());
            stats.put("totalQueueEntries", queueService.getAllQueues().size());

            // Count by status
            long scheduledAppointments = appointmentService.getAllAppointments().stream()
                    .filter(app -> app.getStatus() == AppointmentStatus.SCHEDULED)
                    .count();
            stats.put("scheduledAppointments", scheduledAppointments);

            long completedAppointments = appointmentService.getAllAppointments().stream()
                    .filter(app -> app.getStatus() == AppointmentStatus.COMPLETED)
                    .count();
            stats.put("completedAppointments", completedAppointments);

            long pendingAppointments = appointmentService.getAllAppointments().stream()
                    .filter(app -> app.getStatus() == AppointmentStatus.PENDING)
                    .count();
            stats.put("pendingAppointments", pendingAppointments);

            long waitingInQueue = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getStatus() == QueueStatus.WAITING)
                    .count();
            stats.put("waitingInQueue", waitingInQueue);

            long inProgressQueue = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getStatus() == QueueStatus.IN_PROGRESS)
                    .count();
            stats.put("inProgressQueue", inProgressQueue);

            // Today's appointments
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

            long todaysAppointments = appointmentService.getAllAppointments().stream()
                    .filter(app -> app.getAppointmentDate().isAfter(startOfDay) &&
                            app.getAppointmentDate().isBefore(endOfDay))
                    .count();
            stats.put("todaysAppointments", todaysAppointments);

            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get recent activities
    @GetMapping("/dashboard/recent-activities")
    public ResponseEntity<Map<String, Object>> getRecentActivities() {
        try {
            Map<String, Object> activities = new HashMap<>();

            // Recent appointments (last 10)
            var recentAppointments = appointmentService.getAllAppointments().stream()
                    .sorted((a1, a2) -> a2.getAppointmentDate().compareTo(a1.getAppointmentDate()))
                    .limit(10)
                    .collect(Collectors.toList());
            activities.put("recentAppointments", recentAppointments);

            // Recent queue entries (last 10)
            var recentQueueEntries = queueService.getAllQueues().stream()
                    .sorted((q1, q2) -> q2.getCreatedAt().compareTo(q1.getCreatedAt()))
                    .limit(10)
                    .collect(Collectors.toList());
            activities.put("recentQueueEntries", recentQueueEntries);

            // Recent patients (last 10)
            var recentPatients = patientService.getAllPatients().stream()
                    .limit(10) // Note: You might want to add a createdAt field to Patient model
                    .collect(Collectors.toList());
            activities.put("recentPatients", recentPatients);

            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get appointments by date range
    @GetMapping("/reports/appointments")
    public ResponseEntity<Map<String, Object>> getAppointmentReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String doctorId) {
        try {
            var appointments = appointmentService.getAllAppointments().stream();

            // Filter by date range if provided
            if (startDate != null && endDate != null) {
                LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
                LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59, 59);
                appointments = appointments.filter(app ->
                        app.getAppointmentDate().isAfter(start) &&
                                app.getAppointmentDate().isBefore(end));
            }

            // Filter by doctor if provided
            if (doctorId != null && !doctorId.isEmpty()) {
                appointments = appointments.filter(app -> app.getDoctorId().equals(doctorId));
            }

            var filteredAppointments = appointments.collect(Collectors.toList());

            Map<String, Object> report = new HashMap<>();
            report.put("appointments", filteredAppointments);
            report.put("totalCount", filteredAppointments.size());

            // Group by status
            Map<AppointmentStatus, Long> statusCount = filteredAppointments.stream()
                    .collect(Collectors.groupingBy(
                            appointment -> appointment.getStatus(),
                            Collectors.counting()));
            report.put("statusBreakdown", statusCount);

            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get queue report
    @GetMapping("/reports/queue")
    public ResponseEntity<Map<String, Object>> getQueueReport(
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) String date) {
        try {
            var queues = queueService.getAllQueues().stream();

            // Filter by doctor if provided
            if (doctorId != null && !doctorId.isEmpty()) {
                queues = queues.filter(queue -> queue.getDoctorId().equals(doctorId));
            }

            // Filter by date if provided
            if (date != null && !date.isEmpty()) {
                LocalDateTime startOfDay = LocalDate.parse(date).atStartOfDay();
                LocalDateTime endOfDay = LocalDate.parse(date).atTime(23, 59, 59);
                queues = queues.filter(queue ->
                        queue.getCreatedAt().isAfter(startOfDay) &&
                                queue.getCreatedAt().isBefore(endOfDay));
            }

            var filteredQueues = queues.collect(Collectors.toList());

            Map<String, Object> report = new HashMap<>();
            report.put("queueEntries", filteredQueues);
            report.put("totalCount", filteredQueues.size());

            // Group by status
            Map<QueueStatus, Long> statusCount = filteredQueues.stream()
                    .collect(Collectors.groupingBy(
                            queue -> queue.getStatus(),
                            Collectors.counting()));
            report.put("statusBreakdown", statusCount);

            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get doctor utilization report
    @GetMapping("/reports/doctor-utilization")
    public ResponseEntity<Map<String, Object>> getDoctorUtilizationReport() {
        try {
            var doctors = doctorService.getAllDoctors();
            Map<String, Object> report = new HashMap<>();

            for (var doctor : doctors) {
                Map<String, Object> doctorStats = new HashMap<>();

                // Count appointments for this doctor
                long appointmentCount = appointmentService.getAllAppointments().stream()
                        .filter(app -> app.getDoctorId().equals(doctor.getDoctorId()))
                        .count();

                // Count queue entries for this doctor
                long queueCount = queueService.getAllQueues().stream()
                        .filter(queue -> queue.getDoctorId().equals(doctor.getDoctorId()))
                        .count();

                // Count completed consultations
                long completedCount = queueService.getAllQueues().stream()
                        .filter(queue -> queue.getDoctorId().equals(doctor.getDoctorId()))
                        .filter(queue -> queue.getStatus() == QueueStatus.COMPLETED)
                        .count();

                doctorStats.put("doctorName", doctor.getName());
                doctorStats.put("specialization", doctor.getSpecialization());
                doctorStats.put("totalAppointments", appointmentCount);
                doctorStats.put("totalQueueEntries", queueCount);
                doctorStats.put("completedConsultations", completedCount);

                report.put(doctor.getDoctorId(), doctorStats);
            }

            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // System health check
    @GetMapping("/system/health")
    public ResponseEntity<Map<String, String>> getSystemHealth() {
        try {
            Map<String, String> health = new HashMap<>();
            health.put("status", "healthy");
            health.put("timestamp", LocalDateTime.now().toString());
            health.put("version", "1.0.0");

            // Basic connectivity tests
            try {
                patientService.getAllPatients();
                health.put("database", "connected");
            } catch (Exception e) {
                health.put("database", "error: " + e.getMessage());
                health.put("status", "unhealthy");
            }

            return new ResponseEntity<>(health, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            error.put("timestamp", LocalDateTime.now().toString());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Clear completed queue entries (cleanup operation)
    @DeleteMapping("/cleanup/completed-queues")
    public ResponseEntity<Map<String, Object>> clearCompletedQueues() {
        try {
            var completedQueues = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getStatus() == QueueStatus.COMPLETED)
                    .collect(Collectors.toList());

            int deletedCount = 0;
            for (var queue : completedQueues) {
                queueService.deleteQueue(queue.getQueueId());
                deletedCount++;
            }

            Map<String, Object> result = new HashMap<>();
            result.put("deletedCount", deletedCount);
            result.put("message", deletedCount + " completed queue entries deleted");
            result.put("timestamp", LocalDateTime.now().toString());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to cleanup completed queues");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Emergency queue reset for a specific doctor
    @PostMapping("/emergency/reset-queue/{doctorId}")
    public ResponseEntity<Map<String, Object>> emergencyQueueReset(@PathVariable String doctorId) {
        try {
            var doctorQueues = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getDoctorId().equals(doctorId))
                    .filter(queue -> queue.getStatus() != QueueStatus.COMPLETED)
                    .collect(Collectors.toList());

            int resetCount = 0;
            for (var queue : doctorQueues) {
                queue.setStatus(QueueStatus.WAITING);
                queueService.saveQueue(queue);
                resetCount++;
            }

            Map<String, Object> result = new HashMap<>();
            result.put("resetCount", resetCount);
            result.put("doctorId", doctorId);
            result.put("message", "Queue reset completed for doctor " + doctorId);
            result.put("timestamp", LocalDateTime.now().toString());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to reset queue");
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}