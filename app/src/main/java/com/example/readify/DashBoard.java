package com.example.readify;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        Intent intent = getIntent();
        String uid = intent.getStringExtra("userid");
        String name = intent.getStringExtra("name");
        final String email = intent.getStringExtra("email");
        String passme = intent.getStringExtra("pass");
        String typeme = intent.getStringExtra("type");
        String walletme = intent.getStringExtra("wallet");

        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("passme", passme);
        bundle.putString("typeme", typeme);
        bundle.putString("wallet",walletme);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        transaction.setReorderingAllowed(true);

        DashBoardFrag fragment = new DashBoardFrag();
        fragment.setArguments(bundle);

        transaction.replace(R.id.fragmentContainerView, fragment);

        transaction.addToBackStack(null);

        transaction.commit();

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        transaction.replace(R.id.fragmentContainerView, DashBoardFrag.class, null);*/

        ImageButton AuthorView = (ImageButton) findViewById(R.id.AuthorView);
        ImageButton HomeButt = (ImageButton) findViewById(R.id.HomeButt);
        ImageButton LibraryBut = (ImageButton) findViewById(R.id.LibraryBut);
        ImageButton WalletBut = (ImageButton) findViewById(R.id.WalletBut);
        TextView HomeText = (TextView) findViewById(R.id.HomeText);
        TextView AuthText = (TextView) findViewById(R.id.AuthorText);
        TextView LibText = (TextView) findViewById(R.id.LibText);
        TextView WalletText = (TextView) findViewById(R.id.WalletText);

        AuthorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hexColor = "#FF969A";
                AuthorView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexColor)));
                HomeButt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                LibraryBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                WalletBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

                AuthText.setTextColor(Color.parseColor(hexColor));
                HomeText.setTextColor(Color.parseColor("#FFFFFF"));
                LibText.setTextColor(Color.parseColor("#FFFFFF"));
                WalletText.setTextColor(Color.parseColor("#FFFFFF"));


                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(()->{
                    Connection connection = null;
                    try {
                        // Load the JDBC driver
                        Class.forName("com.mysql.jdbc.Driver");

                        // Define the connection URL
                        String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12709204";

                        // Provide database credentials
                        String username = "sql12709204";
                        String password = "aHVxXZQU8u";

                        // Establish the database connection
                        connection = DriverManager.getConnection(url, username, password);
                        System.out.println("Connected...");

                        Statement statement = connection.createStatement();

                        String selectQuery = "SELECT * FROM users";
                        ResultSet resultSet = statement.executeQuery(selectQuery);


                        while(resultSet.next()){
                            int euserid = resultSet.getInt("UserId");
                            String ename = resultSet.getString("Name");
                            String eemail = resultSet.getString("Email");
                            String epasswordhold = resultSet.getString("Password");
                            String typecheck = resultSet.getString("Type");

                            if(email.equals(eemail)){
                                if(typecheck.equals("Author")){
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                                    transaction.setReorderingAllowed(true);

                                    AuthorRegisteredView fragment = new AuthorRegisteredView();
                                    fragment.setArguments(bundle);

                                    transaction.replace(R.id.fragmentContainerView, fragment);

                                    transaction.addToBackStack(null);

                                    transaction.commit();
                                }
                                else{
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                                    transaction.setReorderingAllowed(true);

                                    Authorsview fragment = new Authorsview();
                                    fragment.setArguments(bundle);

                                    transaction.replace(R.id.fragmentContainerView, fragment);

                                    transaction.addToBackStack(null);

                                    transaction.commit();
                                }
                                return;
                            }

                        }

                    } catch (Exception e) {
                        System.out.println("Errorme"+e);
                    }

                });


            }
        });



        HomeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hexColor = "#FF969A";
                HomeButt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexColor)));
                AuthorView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                LibraryBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                WalletBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

                HomeText.setTextColor(Color.parseColor(hexColor));
                AuthText.setTextColor(Color.parseColor("#FFFFFF"));
                LibText.setTextColor(Color.parseColor("#FFFFFF"));
                WalletText.setTextColor(Color.parseColor("#FFFFFF"));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                DashBoardFrag fragment = new DashBoardFrag();
                fragment.setArguments(bundle);

                transaction.replace(R.id.fragmentContainerView, fragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });


        WalletBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hexColor = "#FF969A";
                WalletBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexColor)));
                HomeButt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                LibraryBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                AuthorView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

                WalletText.setTextColor(Color.parseColor(hexColor));
                AuthText.setTextColor(Color.parseColor("#FFFFFF"));
                LibText.setTextColor(Color.parseColor("#FFFFFF"));
                HomeText.setTextColor(Color.parseColor("#FFFFFF"));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                WalletView fragment = new WalletView();
                fragment.setArguments(bundle);

                transaction.replace(R.id.fragmentContainerView, fragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });


        LibraryBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hexColor = "#FF969A";
                LibraryBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexColor)));
                HomeButt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                AuthorView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                WalletBut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

                LibText.setTextColor(Color.parseColor(hexColor));
                AuthText.setTextColor(Color.parseColor("#FFFFFF"));
                WalletText.setTextColor(Color.parseColor("#FFFFFF"));
                HomeText.setTextColor(Color.parseColor("#FFFFFF"));

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                library_page fragment = new library_page();
                fragment.setArguments(bundle);

                transaction.replace(R.id.fragmentContainerView, fragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
    }
}