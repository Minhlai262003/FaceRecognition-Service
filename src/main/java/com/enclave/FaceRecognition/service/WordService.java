package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.dto.Response.WordResponse;
import com.enclave.FaceRecognition.entity.Word;
import com.enclave.FaceRecognition.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<WordResponse> getByTopicId(Long topicId) {
        return wordRepository.findByTopic_Id(topicId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteWordById(String id) {
        Long wordId = Long.parseLong(id);
        wordRepository.deleteById(wordId);
    }

    // Update word
    public WordResponse updateWord(Long id, WordResponse wordResponse) {
        Word existingWord = wordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        existingWord.setWord(wordResponse.getWord());
        existingWord.setPronunciation(wordResponse.getPronunciation());
        existingWord.setPartOfSpeech(wordResponse.getPartOfSpeech());
        existingWord.setMean(wordResponse.getMean());
        existingWord.setUpdatedAt(LocalDateTime.now());

        Word savedWord = wordRepository.save(existingWord);
        return convertToDTO(savedWord);
    }

    private WordResponse convertToDTO(Word word) {
        WordResponse dto = new WordResponse();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setPronunciation(word.getPronunciation());
        dto.setPartOfSpeech(word.getPartOfSpeech());
        dto.setMean(word.getMean());
        dto.setCreatedAt(word.getCreatedAt());
        dto.setUpdatedAt(word.getUpdatedAt());
        return dto;
    }
}
