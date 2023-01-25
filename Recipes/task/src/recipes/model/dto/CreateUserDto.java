package recipes.model.dto;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Validated
public record CreateUserDto(
        @NotBlank
        @Pattern(regexp = ".*@.*\\..*")
        String email,
        @NotBlank
        @Size(min = 8)
        String password
) {
}
