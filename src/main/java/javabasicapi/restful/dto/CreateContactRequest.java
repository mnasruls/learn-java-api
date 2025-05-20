package javabasicapi.restful.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateContactRequest {
    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotBlank
    @Size(max = 255)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = "\\+?([ -]?\\d+)+|\\(\\d+\\)([ -]\\d+)")
    private String phone;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
}
