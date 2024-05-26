package com.example.readify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button preview, buy, write_review;
    ImageButton to_reviews;
    LinearLayout read_more_aboutAudiobook, read_more_author;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        TextView errorText = (TextView) findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);

        TextView gotosignup = (TextView) findViewById(R.id.gotosignup);
        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        Button LogInbut = (Button) findViewById(R.id.LogInbut);

        LogInbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        TextView emailText = (TextView) findViewById(R.id.EmailLogText);
                        EditText passText= (EditText) findViewById(R.id.PassLogText);

                        while(resultSet.next()){
                            int userid = resultSet.getInt("UserId");
                            String name = resultSet.getString("Name");
                            String email = resultSet.getString("Email");
                            String passwordhold = resultSet.getString("Password");
                            String typeme = resultSet.getString("Type");
                            double walletme = resultSet.getDouble("wallet");

                            if(emailText.getText().toString().equals(email) && passText.getText().toString().equals(passwordhold)){
                                Intent intent = new Intent(MainActivity.this, DashBoard.class);
                                intent.putExtra("userid",userid+"");
                                intent.putExtra("name",name);
                                intent.putExtra("email",email);
                                intent.putExtra("pass",passwordhold);
                                intent.putExtra("type",typeme);
                                intent.putExtra("wallet",walletme+"");

                                runOnUiThread(() -> {
                                    TextView errorText = (TextView) findViewById(R.id.errorText);
                                    errorText.setVisibility(View.GONE);
                                });

                                startActivity(intent);
                                return;
                            }

                        }
                        runOnUiThread(() -> {
                            TextView errorText = (TextView) findViewById(R.id.errorText);
                            errorText.setVisibility(View.VISIBLE);
                        });

                    } catch (Exception e) {
                        System.out.println("Errorme"+e);
                    }

                });
            }
        });


    }
}