package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.dto.Response.WordResponse;
import com.enclave.FaceRecognition.entity.Word;
import com.enclave.FaceRecognition.entity.Topic;
import com.enclave.FaceRecognition.repository.WordRepository;
import com.enclave.FaceRecognition.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final TopicRepository topicRepository;

    // Get all words of a topic
    public List<WordResponse> getWordsByTopic(Long topicId) {
        return wordRepository.findByTopicId(topicId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Create new word
    public WordResponse createWord(Long topicId, Word word) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));
        word.setTopic(topic);
        return mapToResponse(wordRepository.save(word));
    }

    // Update word
    public WordResponse updateWord(Long wordId, Word updatedWord) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("Word not found"));

        word.setWord(updatedWord.getWord());
        word.setPronunciation(updatedWord.getPronunciation());
        word.setPartOfSpeech(updatedWord.getPartOfSpeech());
        word.setMean(updatedWord.getMean());

        return mapToResponse(wordRepository.save(word));
    }

    // Delete word
    public void deleteWord(Long wordId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new IllegalArgumentException("Word not found"));
        wordRepository.delete(word);
    }

    private WordResponse mapToResponse(Word word) {
        return WordResponse.builder()
                .id(word.getId())
                .word(word.getWord())
                .pronunciation(word.getPronunciation())
                .partOfSpeech(word.getPartOfSpeech())
                .mean(word.getMean())
                .createdAt(word.getCreatedAt())
                .updatedAt(word.getUpdatedAt())
                .build();
    }
}
