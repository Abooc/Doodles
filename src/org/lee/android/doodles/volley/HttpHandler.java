package org.lee.android.doodles.volley;

import com.google.gson.Gson;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.lee.android.util.Log;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author liruiyu allnet@live.cn
 */
public abstract class HttpHandler<T> extends BaseJsonHttpResponseHandler<T> {

    private Gson jsonParser;

    public HttpHandler() {
        this(DEFAULT_CHARSET);
    }

    public HttpHandler(String encoding) {
        super(encoding);
        jsonParser = new Gson();
    }

    @Override
    protected T parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        Log.anchor(rawJsonData);
        if (isFailure) {
            Log.anchor("isFailure:" + isFailure);
            return null;
        }

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return jsonParser.fromJson(rawJsonData, params[0]);
    }

//    @Override
//    public final void sendResponseMessage(HttpResponse response) throws IOException {
//        // do not process if request has been cancelled
//        if (!Thread.currentThread().isInterrupted()) {
//            StatusLine status = response.getStatusLine();
//            byte[] responseBody;
//            responseBody = getResponseData(response.getEntity());
//            // additional cancellation check as getResponseData() can take
//            // non-zero time to process
//            if (!Thread.currentThread().isInterrupted()) {
//                if (status.getStatusCode() >= 300 && status.getStatusCode() != 400) {
//                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody,
//                            new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
//                } else {
//                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
//                }
//            }
//        }
//    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, T errorResponse) {
        String error = getErrorMessage(throwable);
        Log.anchor("statusCode:" + statusCode + ", error:" + error + "\n" + throwable.toString());

        onFailure(statusCode, error);
    }

    public abstract void onFailure(int statusCode, String error);

    private String getErrorMessage(Throwable throwable) {
        if (throwable instanceof ConnectTimeoutException) {
            return "网络请求超时，请稍后重试！";
        } else if (throwable instanceof IOException) {
            return "请检查网络连接是否可用！";
        } else {
            return throwable.getMessage();
        }
    }
}
