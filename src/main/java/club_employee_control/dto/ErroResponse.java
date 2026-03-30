package club_employee_control.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ErroResponse {

    @JsonProperty("status")
    private int status;

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("timestamp")
    private String timestamp;

    public ErroResponse() {}

    public ErroResponse(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now().toString();
    }

    public int getStatus()       { return status; }
    public String getMensagem()  { return mensagem; }
    public String getTimestamp() { return timestamp; }
}