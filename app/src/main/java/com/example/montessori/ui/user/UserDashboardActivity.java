package com.example.montessori.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.montessori.R;
import com.example.montessori.ui.HomeFragment;
import com.example.montessori.ui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserDashboardActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_user, new HomeFragment()).commit();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_user, fragment).commit();
            } else if (itemId == R.id.navigation_add) {
                startActivity(new Intent(UserDashboardActivity.this, PostActivity.class));
            } else if (itemId == R.id.navigation_profile) {
                fragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_user, fragment).commit();
            }

            return true;
        });
    }
}