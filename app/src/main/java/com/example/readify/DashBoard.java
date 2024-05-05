package com.example.readify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("passme", passme);
        bundle.putString("typeme", typeme);

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        transaction.replace(R.id.fragmentContainerView, DashBoardFrag.class, null);*/

        ImageButton AuthorView = (ImageButton) findViewById(R.id.AuthorView);

        AuthorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(()->{
                    Connection connection = null;
                    try {
                        // Load the JDBC driver
                        Class.forName("com.mysql.jdbc.Driver");

                        // Define the connection URL
                        String url = "jdbc:mysql://10.0.2.2:3306/dbreadify";

                        // Provide database credentials
                        String username = "";
                        String password = "";

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