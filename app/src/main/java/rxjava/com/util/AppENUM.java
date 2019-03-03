package rxjava.com.util;

public class AppENUM {

    public abstract static class KeyName {
        private KeyName() {
            throw new IllegalStateException("KeyName class");
        }
        public static final String URL = "url";
        public static final String STATUS_CODE = "statusCode";
        public static final String REQUEST_ID = "requestID";
        public static final String REQUEST = "request";
        public static final String HEADER = "header";
    }
    public static final int ALBUM_ID = 1001;
    public static final int ALBUM_SORT_ASC = 1002;
    public static final int ALBUM_SORT_DESC = 1003;
}
