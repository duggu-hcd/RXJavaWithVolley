package rxjava.com.util;

import rxjava.com.BuildConfig;

public class APIURL {
    private static final String BASE_URL = BuildConfig.API_URL;
    public static final String ALBUM_LIST = BASE_URL + BuildConfig.ALBUM_LIST;
    public static final String SORT_ASC = ALBUM_LIST + BuildConfig.SORT_ASC;
    public static final String SORT_DESC = ALBUM_LIST + BuildConfig.SORT_DESC;
}
