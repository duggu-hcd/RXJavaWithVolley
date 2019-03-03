package rxjava.com.network;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import rxjava.com.storage.AppStoragePreference;
import rxjava.com.util.AppENUM;

public class VolleyGsonRequest<T> extends JsonRequest<T> {
    private Class<T> clazz;
    private Context context;
    private Map<String, String> headers = new HashMap<>();
    private String mRequest = "";
    public VolleyGsonRequest(String url, Class<T> cls, JSONObject jsonRequest, Listener<T> listener, ErrorListener errorListener, Context context) {
        super(jsonRequest == null || jsonRequest.length() == 0 ? Request.Method.GET : Request.Method.POST, url,
                (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        clazz = cls;
        mRequest = (jsonRequest == null) ? null : jsonRequest.toString();
        this.context = context;
    }

    private String getRequest() {
        return mRequest;
    }

    public void setHeader(Map<String, String> header) {
        this.headers = header;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (clazz == null) return Response.error(new ParseError());
        try {
            String dataStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Object json = new JSONTokener(dataStr).nextValue();

            if (json instanceof JSONObject) {
                JSONObject data = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));

                data.put(AppENUM.KeyName.URL,getUrl());
                data.put(AppENUM.KeyName.STATUS_CODE,response.statusCode);
                data.put(AppENUM.KeyName.REQUEST_ID,getTag());
                data.put(AppENUM.KeyName.HEADER, getHeaders());
                data.put(AppENUM.KeyName.REQUEST, getRequest());

                return Response.success(new Gson().fromJson(data.toString(), clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                JSONObject jsonObject = new JSONObject();

                JSONArray jsonArray = new JSONArray(dataStr);
                jsonObject.put("data",jsonArray);

                jsonObject.put(AppENUM.KeyName.URL,getUrl());
                jsonObject.put(AppENUM.KeyName.STATUS_CODE,response.statusCode);
                AppStoragePreference.putString(context,getTag()+"",jsonObject.toString());
                return Response.success(new Gson().fromJson(jsonObject.toString(), clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
