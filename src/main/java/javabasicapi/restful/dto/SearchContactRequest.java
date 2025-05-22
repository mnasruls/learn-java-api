package javabasicapi.restful.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchContactRequest {
    private String name;
    private String email;
    private String phone;

    @NotBlank
    private Integer page; 
     
    @NotBlank
    private Integer size;
}
