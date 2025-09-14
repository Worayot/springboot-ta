package ta.sf212.demo.model.response;

public class LoginResponse {
    private int statusCode;
    private String description;
    private String token;

    public LoginResponse(int statusCode, String description, String token) {
        this.statusCode = statusCode;
        this.description = description;
        this.token = token;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
