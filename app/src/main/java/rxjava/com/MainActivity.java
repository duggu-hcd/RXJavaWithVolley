package rxjava.com;

import android.annotation.SuppressLint;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.Objects;

import rxjava.com.adapter.AlbumListAdapter;
import rxjava.com.model.AlbumListModel;
import rxjava.com.network.BaseVolley;
import rxjava.com.storage.AppStoragePreference;
import rxjava.com.util.APIURL;
import rxjava.com.util.AppENUM;
import rxjava.com.util.CommonUtils;
import io.reactivex.subscribers.DisposableSubscriber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AlbumListAdapter mAdapter;
    private AppCompatImageView sort;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private BaseVolley baseVolley;
    private BottomSheetDialog mBottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseVolley.init(this);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        sort        = findViewById(R.id.sortBtn);
        sort.setOnClickListener(this);
        hideLoader();
        setUpRecyclerView();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sortBtn) {
            showBottomSheet();
        } else if(view.getId() == R.id.aToZ) {
            if(mBottomSheetDialog != null) {
                mBottomSheetDialog.dismiss();
            }
            getData(APIURL.SORT_ASC, AppENUM.ALBUM_SORT_ASC);
        } else if(view.getId() == R.id.zToA) {
            if(mBottomSheetDialog != null) {
                mBottomSheetDialog.dismiss();
            }
            getData(APIURL.SORT_DESC,AppENUM.ALBUM_SORT_DESC);
        }
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new AlbumListAdapter(R.layout.text_view);
        recyclerView.setAdapter(mAdapter);
        getData(APIURL.ALBUM_LIST, AppENUM.ALBUM_ID);
    }

    private void getData(String URL , int id ) {
        showLoader();
        try {
            if(!CommonUtils.isConnectionAvailable(this)) {
                String appStoragePreference = AppStoragePreference.getString(this,id+"");
                hideLoader();
                if(appStoragePreference.isEmpty()) {
                    CommonUtils.showToast(this, getString(R.string.no_internet));
                } else {
                    whenNetworkNotAvailable(appStoragePreference);
                }
            } else {
                getNetworkManger().startVolleyRequest(id, URL, AlbumListModel.class, getSubscriber(),this);
            }
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
    }

    protected void showLoader () {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLoader () {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public BaseVolley getNetworkManger() {
        if(baseVolley == null) {
            baseVolley = new BaseVolley();
        }
        return baseVolley;
    }

    protected DisposableSubscriber<AlbumListModel> getSubscriber() {
        return new DisposableSubscriber<AlbumListModel>() {
            @Override
            public void onNext(AlbumListModel response) {
                hideLoader();
                if(response != null && !response.getAlbumDataModels().isEmpty()) {
                    mAdapter.setArray(response.getAlbumDataModels());
                }
            }

            @Override
            public void onError(Throwable e) {
                hideLoader();
            }

            @Override
            public void onComplete() {
            }
        };
    }

    private void showBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(this));
        @SuppressLint("InflateParams") View sheetView = Objects.requireNonNull(this).getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        AppCompatTextView aToZ = sheetView.findViewById(R.id.aToZ);
        AppCompatTextView zToA = sheetView.findViewById(R.id.zToA);

        aToZ.setOnClickListener(this);
        zToA.setOnClickListener(this);

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    private void whenNetworkNotAvailable(String appStoragePreference) {
        try {
            AlbumListModel albumListModel = new Gson().fromJson(appStoragePreference, AlbumListModel.class);
            mAdapter.setArray(albumListModel.getAlbumDataModels());
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
    }
}
