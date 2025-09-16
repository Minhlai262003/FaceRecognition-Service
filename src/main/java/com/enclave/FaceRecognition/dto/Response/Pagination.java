package com.enclave.FaceRecognition.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    @JsonProperty("current_page")
    private int currentPage;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("total_count")
    private int totalCount;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("has_next")
    private boolean hasNext;
    @JsonProperty("has_prev")
    private boolean hasPrev;
    @JsonProperty("next_page")
    private Integer nextPage;
    @JsonProperty("prev_page")
    private Integer prevPage;
}
