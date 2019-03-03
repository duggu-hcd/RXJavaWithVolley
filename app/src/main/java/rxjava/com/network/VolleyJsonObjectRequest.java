package rxjava.com.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class VolleyJsonObjectRequest<T> extends JsonRequest<T> {

    private final Gson gson = new Gson();
//    private final Class<T> clazz;
    private Map<String, String> header;
    public Gson getGson() {
        return gson;
    }

//    public Class<?> getClazz() {
//        return clazz;
//    }

    public VolleyJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
//        this.clazz = clazz;
    }

    public VolleyJsonObjectRequest(String url, JSONObject jsonRequest,Response.Listener<T> listener,
                             Response.ErrorListener errorListener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
                listener, errorListener);
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(
                    (T) new JSONObject(json),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }
}
