package com.example.montessori;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.montessori.archive.ChatFragment;
import com.example.montessori.ui.HomeFragment;
import com.example.montessori.ui.ProfileFragment;
import com.example.montessori.archive.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserDashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_user, new HomeFragment()).commit();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_user, fragment).commit();
            } else if (itemId == R.id.navigation_add) {
                startActivity(new Intent(UserDashboardActivity.this, PostActivity.class));
            } else if (itemId == R.id.navigation_profile) {
                fragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_user, fragment).commit();
            }

            return true;
        });
    }
}