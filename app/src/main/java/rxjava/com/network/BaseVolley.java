package rxjava.com.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import org.reactivestreams.Publisher;

import rxjava.com.model.AlbumListModel;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class BaseVolley {
    private static RequestQueue mRequestQueue;

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context,new HurlStack());
    }

    private static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    private Flowable<AlbumListModel> baseModelFlowable(RequestFuture<AlbumListModel> future) {
        return Flowable.defer(
                () -> {
                    try {
                        return Flowable.just(future.get());
                    } catch (Exception e) {
                        Log.e("routes", e.getMessage());
                        return Flowable.error(e);
                    }
                });
    }

    public void startVolleyRequest(int id , String url , Class<?> modelClass , DisposableSubscriber<AlbumListModel> d, Context context) {
        RequestFuture<AlbumListModel> future = RequestFuture.newFuture();
        VolleyGsonRequest<AlbumListModel> req = new VolleyGsonRequest(url,modelClass, null,future, future,context);
        req.setTag(id);
        BaseVolley.getRequestQueue().add(req);

        baseModelFlowable(future)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(new Function<Throwable, Publisher<? extends AlbumListModel>>() {
                        @Override
                        public Publisher<? extends AlbumListModel> apply(Throwable throwable) throws Exception {
                            Log.e("BaseVolley error",throwable.getMessage());
                            return null;
                        }
                    })
                    .subscribe(d);
    }

}