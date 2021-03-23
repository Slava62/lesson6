package ru.slava62.dto;

import lombok.Data;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.json.JSONException;
import retrofit2.Converter;
import retrofit2.Response;
import ru.slava62.util.RetrofitUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import org.json.JSONObject;

@Data
public class ErrorBody {
    private Integer status;
    private String message;
    private String error;
    private String path;
    private String timestamp;

    public static String getErrorMessage(Response response) throws IOException {
        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            return errorBody.getError();
        }
        return "---";
    }
    @SneakyThrows
    public static JSONObject getJsonErrorMessage(Response response) {
        return new JSONObject(response.errorBody().string());
    }
}
