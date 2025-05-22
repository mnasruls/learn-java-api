package javabasicapi.restful.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
    private Integer currentPage;
    private Integer totalPages;
    private Integer size;
    private Long totalData;
}
