package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.entity.Topic;
import com.enclave.FaceRecognition.exception.TopicNotFoundException;
import com.enclave.FaceRecognition.repository.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService  {
    private final TopicRepository topicRepository;
    public List<Topic> fetchAllTopics(){
        return topicRepository.findAll();
    }
    public Optional<Topic> fetchTopicById(Long id){
        return topicRepository.findById(id);
    }
    public void deleteTopicById(Long id){
         topicRepository.deleteById(id);
    }

    public Topic updateTopicName(Long id, String newName) {
        System.out.println(">>> newName in service = '" + newName + "'");
        Topic existingTopic = topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found with id: " + id));

        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name not hollow");
        }
        if (newName.equals(existingTopic.getName())) {
            throw new IllegalArgumentException("Name new duplicate");
        }

        existingTopic.setName(newName);
        existingTopic.setUpdatedAt(LocalDateTime.now());
        return topicRepository.save(existingTopic);
    }



    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found with id: " + id));
    }


}
