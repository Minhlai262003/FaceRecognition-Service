package com.enclave.FaceRecognition.mapper;

import com.enclave.FaceRecognition.dto.Response.TopicResponse;
import com.enclave.FaceRecognition.entity.Topic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicResponse toTopicResponse(Topic topic);
    List<TopicResponse> toTopicResponseList(List<Topic> topics);

}
