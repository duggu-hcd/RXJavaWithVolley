package rxjava.com.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

public class AppStoragePreference  {
    private static volatile SharedPreferences sharedPreferences;
    private static final String PREFERENCE_NAME = "schooller_preferences";
    private AppStoragePreference() {
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            synchronized (AppStoragePreference.class) {
                if (sharedPreferences == null) {
                    sharedPreferences =
                            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
                }
            }
        }
        return sharedPreferences;
    }

    public static void putInt(Context context, String key, int value) {
        if (context != null) {
            getSharedPreferences(context).edit().putInt(key, value).apply();
        }
    }

    public static void putString(Context context, String key, String value) {
        if (context != null) {
            SharedPreferences.Editor edit = getSharedPreferences(context).edit();
            if (TextUtils.isEmpty(value)) {
                edit.remove(key);
            } else {
                edit.putString(key, value);
            }
            edit.apply();
        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (context != null) {
            getSharedPreferences(context).edit().putBoolean(key, value).apply();
        }
    }

    public static String getString(Context context, String key) {
        if (context != null) {
            return getSharedPreferences(context).getString(key, null);
        } else {
            return getString(context, key, null);
        }
    }

    public static String getString(Context context, String key, String def) {
        if (context != null) {
            return getSharedPreferences(context).getString(key, def);
        } else {
            return null;
        }
    }

    public static int getInt(Context context, String key) {
        if (context != null) {
            return getInt(context, key, 0);
        } else {
            return 0;
        }
    }

    public static boolean getBoolean(Context context, String key) {
        if (context != null) {
            return getBoolean(context, key, false);
        } else {
            return false;
        }
    }

    public static boolean getBoolean(Context context, String key, boolean def) {
        if (context != null) {
            try {
                return getSharedPreferences(context).getBoolean(key, def);
            } catch (ClassCastException ex) {
                try {
                    String val = getSharedPreferences(context).getString(key, null);
                    if (!TextUtils.isEmpty(val)) {
                        try {
                            boolean boolValue = Boolean.parseBoolean(val);
                            putBoolean(context, key, boolValue);
                            return boolValue;
                        } catch (Exception numberExp) {
                            Log.d("secureMigration", "Boolean Error 2 " + key);
                            return def;
                        }
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                return def;
            }
        } else {
            return def;
        }

    }

    public static int getInt(Context context, String key, int fallBack) {
        if (context != null) {
            try {
                return getSharedPreferences(context).getInt(key, fallBack);
            } catch (ClassCastException ex) {
                String val = getSharedPreferences(context).getString(key, null);
                if (!TextUtils.isEmpty(val)) {
                    try {
                        int intValue = Integer.parseInt(val);
                        putInt(context, key, intValue);
                        return intValue;
                    } catch (NumberFormatException numberExp) {
                        Log.d("secureMigration", "Int Error 2 " + key);
                        return fallBack;
                    }
                }
                return fallBack;
            }
        } else {
            return fallBack;
        }
    }
}
