package com.healthcare.queuesystem.service;

import com.healthcare.queuesystem.model.Queue;
import java.util.List;

public interface QueueService {
    Queue saveQueue(Queue queue);
    void deleteQueue(String queueId);
    Queue getQueueById(String queueId);
    List<Queue> getAllQueues();
}
