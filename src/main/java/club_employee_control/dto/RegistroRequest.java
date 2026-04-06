package club_employee_control.dto;

import club_employee_control.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String senha,
        @NotNull Role role
) {}