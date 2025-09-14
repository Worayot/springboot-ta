package ta.sf212.demo.model.response.common;

public class Response<T> {
    final private Header header;
    final private T data;

    public Response(Header header, T data) {
        this.header = header;
        this.data = data;
    }

    public Header getHeader() {
        return header;
    }

    public T getData() {
        return data;
    }
}
