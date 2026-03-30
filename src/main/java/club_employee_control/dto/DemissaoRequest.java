package club_employee_control.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DemissaoRequest(
        @NotNull(message = "Data de demissão é obrigatória")
        LocalDate dataDemissao
) {}