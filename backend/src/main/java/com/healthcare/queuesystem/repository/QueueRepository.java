package com.healthcare.queuesystem.repository;

import com.healthcare.queuesystem.model.Queue;
import com.healthcare.queuesystem.model.enums.QueueStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QueueRepository extends MongoRepository<Queue, String> {

    // Find queue by doctor
    List<Queue> findByDoctorId(String doctorId);

    // Find queue by status
    List<Queue> findByStatus(QueueStatus status);

    // Find active queues for a doctor
    @Query("{ 'doctorId' : ?0, 'status' : 'ACTIVE' }")
    List<Queue> findActiveQueuesByDoctor(String doctorId);

    // Find all completed queues
    @Query("{ 'status' : 'COMPLETED' }")
    List<Queue> findCompletedQueues();
}
