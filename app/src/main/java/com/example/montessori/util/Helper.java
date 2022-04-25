package com.example.montessori.util;


import android.app.Activity;
import android.content.Intent;

import com.example.montessori.ui.user.DetailCategoryActivity;
import com.example.montessori.ui.admin.AdminDashboardActivity;
import com.example.montessori.ui.auth.LoginActivity;
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
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

    public static void Category(String category, Activity activity) {
        Intent intent = new Intent(activity, DetailCategoryActivity.class);
        intent.putExtra(IntentNameExtra.CATEGORY_DATA, category);
        activity.startActivity(intent);
    }
}