package com.healthcare.queuesystem.controller;

import com.healthcare.queuesystem.dto.QueueDto;
import com.healthcare.queuesystem.model.Doctor;
import com.healthcare.queuesystem.model.Patient;
import com.healthcare.queuesystem.model.Queue;
import com.healthcare.queuesystem.model.enums.QueueStatus;
import com.healthcare.queuesystem.service.DoctorService;
import com.healthcare.queuesystem.service.PatientService;
import com.healthcare.queuesystem.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "*")
public class QueueController {

    @Autowired
    private QueueService queueService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    // Add patient to queue
    @PostMapping
    public ResponseEntity<QueueDto> addToQueue(@RequestBody QueueDto queueDto) {
        try {
            // Calculate the next position in queue for the doctor
            int nextPosition = getNextQueuePosition(queueDto.getDoctorId());

            Queue queue = convertToEntity(queueDto);
            queue.setPosition(nextPosition);
            queue.setStatus(QueueStatus.WAITING);
            queue.setCreatedAt(LocalDateTime.now());

            Queue savedQueue = queueService.saveQueue(queue);
            QueueDto responseDto = convertToDto(savedQueue);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all queue entries
    @GetMapping
    public ResponseEntity<List<QueueDto>> getAllQueueEntries() {
        try {
            List<Queue> queues = queueService.getAllQueues();
            List<QueueDto> queueDtos = queues.stream()
                    .map(this::convertToDto)
                    .sorted(Comparator.comparing(QueueDto::getPosition))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(queueDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get queue entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<QueueDto> getQueueById(@PathVariable String id) {
        try {
            Queue queue = queueService.getQueueById(id);
            if (queue != null) {
                QueueDto queueDto = convertToDto(queue);
                return new ResponseEntity<>(queueDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get queue by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<QueueDto>> getQueueByDoctor(@PathVariable String doctorId) {
        try {
            List<Queue> queues = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getDoctorId().equals(doctorId))
                    .sorted(Comparator.comparing(Queue::getPosition))
                    .collect(Collectors.toList());

            List<QueueDto> queueDtos = queues.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(queueDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get active queue by doctor ID (waiting and in-progress)
    @GetMapping("/doctor/{doctorId}/active")
    public ResponseEntity<List<QueueDto>> getActiveQueueByDoctor(@PathVariable String doctorId) {
        try {
            List<Queue> queues = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getDoctorId().equals(doctorId))
                    .filter(queue -> queue.getStatus() == QueueStatus.WAITING || queue.getStatus() == QueueStatus.IN_PROGRESS)
                    .sorted(Comparator.comparing(Queue::getPosition))
                    .collect(Collectors.toList());

            List<QueueDto> queueDtos = queues.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(queueDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get queue by patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<QueueDto>> getQueueByPatient(@PathVariable String patientId) {
        try {
            List<Queue> queues = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getPatientId().equals(patientId))
                    .collect(Collectors.toList());

            List<QueueDto> queueDtos = queues.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(queueDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update queue status
    @PatchMapping("/{id}/status")
    public ResponseEntity<QueueDto> updateQueueStatus(@PathVariable String id, @RequestParam QueueStatus status) {
        try {
            Queue queue = queueService.getQueueById(id);
            if (queue != null) {
                queue.setStatus(status);
                Queue updatedQueue = queueService.saveQueue(queue);
                QueueDto responseDto = convertToDto(updatedQueue);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Move to next patient in queue
    @PostMapping("/doctor/{doctorId}/next")
    public ResponseEntity<QueueDto> moveToNextPatient(@PathVariable String doctorId) {
        try {
            List<Queue> waitingQueues = queueService.getAllQueues().stream()
                    .filter(queue -> queue.getDoctorId().equals(doctorId))
                    .filter(queue -> queue.getStatus() == QueueStatus.WAITING)
                    .sorted(Comparator.comparing(Queue::getPosition))
                    .collect(Collectors.toList());

            if (!waitingQueues.isEmpty()) {
                Queue nextQueue = waitingQueues.get(0);
                nextQueue.setStatus(QueueStatus.IN_PROGRESS);
                Queue updatedQueue = queueService.saveQueue(nextQueue);
                QueueDto responseDto = convertToDto(updatedQueue);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Complete current patient consultation
    @PostMapping("/complete/{id}")
    public ResponseEntity<QueueDto> completeConsultation(@PathVariable String id) {
        try {
            Queue queue = queueService.getQueueById(id);
            if (queue != null && queue.getStatus() == QueueStatus.IN_PROGRESS) {
                queue.setStatus(QueueStatus.COMPLETED);
                Queue updatedQueue = queueService.saveQueue(queue);
                QueueDto responseDto = convertToDto(updatedQueue);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Skip patient in queue
    @PostMapping("/skip/{id}")
    public ResponseEntity<QueueDto> skipPatient(@PathVariable String id) {
        try {
            Queue queue = queueService.getQueueById(id);
            if (queue != null) {
                queue.setStatus(QueueStatus.SKIPPED);
                Queue updatedQueue = queueService.saveQueue(queue);
                QueueDto responseDto = convertToDto(updatedQueue);
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Remove from queue
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeFromQueue(@PathVariable String id) {
        try {
            Queue existingQueue = queueService.getQueueById(id);
            if (existingQueue != null) {
                queueService.deleteQueue(id);
                return new ResponseEntity<>("Queue entry removed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Queue entry not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error removing queue entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to get next queue position for a doctor
    private int getNextQueuePosition(String doctorId) {
        List<Queue> doctorQueues = queueService.getAllQueues().stream()
                .filter(queue -> queue.getDoctorId().equals(doctorId))
                .filter(queue -> queue.getStatus() == QueueStatus.WAITING || queue.getStatus() == QueueStatus.IN_PROGRESS)
                .collect(Collectors.toList());

        return doctorQueues.size() + 1;
    }

    // Helper method to calculate estimated wait time (in minutes)
    private int calculateEstimatedWaitTime(String doctorId, int position) {
        // Average consultation time of 15 minutes per patient
        int avgConsultationTime = 15;
        return (position - 1) * avgConsultationTime;
    }

    // Helper method to convert Entity to DTO
    private QueueDto convertToDto(Queue queue) {
        QueueDto dto = new QueueDto();
        dto.setQueueId(queue.getQueueId());
        dto.setDoctorId(queue.getDoctorId());
        dto.setPatientId(queue.getPatientId());
        dto.setPosition(queue.getPosition());
        dto.setStatus(queue.getStatus());
        dto.setCreatedAt(queue.getCreatedAt());

        // Calculate estimated wait time
        if (queue.getStatus() == QueueStatus.WAITING) {
            dto.setEstimatedWaitTime(calculateEstimatedWaitTime(queue.getDoctorId(), queue.getPosition()));
        }

        // Set doctor and patient names for better display
        try {
            if (queue.getDoctorId() != null) {
                Doctor doctor = doctorService.getDoctorById(queue.getDoctorId());
                if (doctor != null) {
                    dto.setDoctorName(doctor.getName());
                }
            }
            if (queue.getPatientId() != null) {
                Patient patient = patientService.getPatientById(queue.getPatientId());
                if (patient != null) {
                    dto.setPatientName(patient.getName());
                }
            }
        } catch (Exception e) {
            // Log error but don't fail the conversion
            System.err.println("Error setting names in queue DTO: " + e.getMessage());
        }

        return dto;
    }

    // Helper method to convert DTO to Entity
    private Queue convertToEntity(QueueDto dto) {
        Queue queue = new Queue();
        queue.setQueueId(dto.getQueueId());
        queue.setDoctorId(dto.getDoctorId());
        queue.setPatientId(dto.getPatientId());
        queue.setPosition(dto.getPosition());
        queue.setStatus(dto.getStatus());
        queue.setCreatedAt(dto.getCreatedAt());
        return queue;
    }
}