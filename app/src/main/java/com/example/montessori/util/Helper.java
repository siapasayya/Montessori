package com.example.montessori.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.montessori.R;
import com.example.montessori.ui.admin.AdminDashboardActivity;
import com.example.montessori.ui.auth.StartActivity;
import com.example.montessori.ui.user.DetailCategoryActivity;
import com.example.montessori.ui.user.UserDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static String convertToFormattedDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern, getDefaultLocale()).format(date);
    }

    public static Locale getDefaultLocale() {
        return new Locale("id", "ID");
    }

    public static void openUserDashboard(Activity activity) {
        activity.startActivity(new Intent(activity, UserDashboardActivity.class));
        activity.finish();
    }

    public static void openAdminDashboard(Activity activity) {
        activity.startActivity(new Intent(activity, AdminDashboardActivity.class));
        activity.finish();
    }

    public static void doLogout(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        activity.startActivity(new Intent(activity, StartActivity.class));
        activity.finish();
    }

    public static void openCategoryPage(Activity activity, String category, boolean isLearningCategory) {
        Intent intent = new Intent(activity, DetailCategoryActivity.class);
        intent.putExtra(IntentNameExtra.CATEGORY_DATA, category);
        intent.putExtra(IntentNameExtra.IS_LEARNING_CATEGORY, isLearningCategory);
        activity.startActivity(intent);
    }

    public static boolean isNullOrBlank(String text) {
        return text == null || text.isEmpty();
    }

    public static String generateId(String username) {
        return System.currentTimeMillis() + "_" + username;
    }

    public static Locale getLocale() {
        return new Locale("id", "ID");
    }

    public static String getTotalLikes(Context context, int total) {
        if (total <= 1) {
            return String.format(getLocale(), context.getString(R.string.total_like_template), total);
        } else {
            return String.format(getLocale(), context.getString(R.string.total_likes_template), total);
        }
    }
}