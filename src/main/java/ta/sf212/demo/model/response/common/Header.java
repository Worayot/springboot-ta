package ta.sf212.demo.model.response.common;

public class Header {
    final private int statusCode;
    final private String description;

    public Header(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }
}
