package com.example.readify;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        transaction.replace(R.id.fragmentContainerView, DashBoardFrag.class, null);*/

        ImageButton AuthorView = (ImageButton) findViewById(R.id.AuthorView);

        AuthorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                transaction.replace(R.id.fragmentContainerView, Authorsview.class, null);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        ImageButton HomeButt = (ImageButton) findViewById(R.id.HomeButt);

        HomeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                transaction.replace(R.id.fragmentContainerView, DashBoardFrag.class, null);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
    }
}