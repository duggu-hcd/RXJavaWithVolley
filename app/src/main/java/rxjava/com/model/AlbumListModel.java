package rxjava.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlbumListModel {
    public List<AlbumDataModel> getAlbumDataModels() {
        return albumDataModels;
    }

    public void setAlbumDataModels(List<AlbumDataModel> albumDataModels) {
        this.albumDataModels = albumDataModels;
    }

    @SerializedName("data")
    @Expose
    private List<AlbumDataModel> albumDataModels;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("url")
    @Expose
    private String url;
}
