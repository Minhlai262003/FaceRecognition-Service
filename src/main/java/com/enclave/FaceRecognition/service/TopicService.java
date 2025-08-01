package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.entity.Topic;
import com.enclave.FaceRecognition.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
