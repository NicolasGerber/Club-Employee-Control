package club_employee_control.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AjusteSalarioRequest(
        @NotNull(message = "Percentual é obrigatório")
        @DecimalMin(value = "0.01", message = "Percentual deve ser maior que zero")
        BigDecimal percentual

) {

}