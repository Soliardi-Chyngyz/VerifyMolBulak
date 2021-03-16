package com.chyngyz.verifymolbulak.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.chyngyz.verifymolbulak.R;


public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavComponents();
    }

    private void initNavComponents() {
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.authNumberFragment
        ).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}