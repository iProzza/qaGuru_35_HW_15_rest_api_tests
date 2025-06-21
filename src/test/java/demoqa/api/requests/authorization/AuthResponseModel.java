package demoqa.api.requests.authorization;
import lombok.Data;

@Data
public class AuthResponseModel {
    private String userId;
    private String username;
    private String password;
    private String token;
    private String expires;
    private String created_date;
    private Boolean isActive;
}
