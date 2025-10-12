package com.healthcare.queuesystem.service.Implementation;

import com.healthcare.queuesystem.model.Queue;
import com.healthcare.queuesystem.repository.QueueRepository;
import com.healthcare.queuesystem.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Override
    public Queue saveQueue(Queue queue) {
        return queueRepository.save(queue);
    }

    @Override
    public void deleteQueue(String queueId) {
        queueRepository.deleteById(queueId);
    }

    @Override
    public Queue getQueueById(String queueId) {
        return queueRepository.findById(queueId).orElse(null);
    }

    @Override
    public List<Queue> getAllQueues() {
        return queueRepository.findAll();
    }
}
