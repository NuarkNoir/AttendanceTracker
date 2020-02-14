package xyz.nuark.attendancetracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import xyz.nuark.attendancetracker.C;

public class PreferenceUtils {
    private Context context;

    private PreferenceUtils(Context context) {
        this.context = context;
    }

    public static PreferenceUtils init(Context context) {
        return new PreferenceUtils(context);
    }

    public <T> List<T> getList(String str, Class<T[]> cls) {
        return Arrays.asList((T[]) new Gson().fromJson(getDefault().getString(str, "[]"), cls));
    }

    public <T> void setList(String str, List<T> list) {
        getDefault().edit().putString(str, new GsonBuilder().create().toJson(list)).apply();
    }

    public void remove(String str) {
        getDefault().edit().remove(str).apply();
    }

    public void clear() {
        getDefault().edit().clear().apply();
    }

    private SharedPreferences getDefault() {
        return context.getSharedPreferences(C.APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
