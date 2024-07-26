package ch.dirtools.app.http;

import lombok.Getter;
import lombok.Setter;

public class Response<T> {

    @Getter
    @Setter
    private int status;

    @Getter
    @Setter
    private T data;

    public Response(int status) {
        this.status = status;
    }

}