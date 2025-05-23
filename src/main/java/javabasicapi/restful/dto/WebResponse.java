package javabasicapi.restful.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebResponse<T> {
    private T data;
    private String errors;
    private PaginationResponse pagination;
}