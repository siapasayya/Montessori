package com.example.montessori;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.montessori.ui.AddFragment;
import com.example.montessori.ui.ChatFragment;
import com.example.montessori.ui.HomeFragment;
import com.example.montessori.ui.ProfileFragment;
import com.example.montessori.ui.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.montessori.databinding.ActivityUserBinding;
import com.google.android.material.navigation.NavigationBarView;

public class UserActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_user,new HomeFragment()).commit();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.navigation_home:
                        fragment= new HomeFragment();
                        break;
                    case R.id.navigation_search:
                        fragment= new SearchFragment();
                        break;
                    case R.id.navigation_add:
                        fragment= new AddFragment();
                        break;
                    case R.id.navigation_chat:
                        fragment= new ChatFragment();
                        break;
                    case R.id.navigation_profile:
                        fragment= new ProfileFragment();
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_user,fragment).commit();
                return true;
            }
        });
    }
//
//    private NavigationBarView.OnItemSelectedListener navigation = new NavigationBarView.OnItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment f = null;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    f = new HomeFragment();
//                    break;
//                case R.id.navigation_search:
//                    f = new SearchFragment();
//                    break;
//                case R.id.navigation_add:
//                    f = new AddFragment();
//                    break;
//                case R.id.navigation_chat:
//                    f = new ChatFragment();
//                    break;
//                case R.id.navigation_profile:
//                    f = new ProfileFragment();
//                    break;
//            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_user,f).commit();
//            return true;
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user);
//        bottomNavigationView = findViewById(R.id.nav_view);
////        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
////        NavigationBarView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener)
//        bottomNavigationView.setOnItemSelectedListener(navigation);
//    }

}