package club_employee_control.dto;

import club_employee_control.entity.Role;

public record RegistroRequest(String email, String senha, Role role) {}