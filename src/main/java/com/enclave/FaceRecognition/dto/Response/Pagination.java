package com.enclave.FaceRecognition.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private int currentPage;
    private int perPage;
    private int totalCount;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;
    private Integer nextPage;
    private Integer prevPage;
}
