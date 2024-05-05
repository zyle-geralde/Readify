package com.example.readify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView gobacklogin = (TextView) findViewById(R.id.gobacklogin);
        gobacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button SignupBut = (Button) findViewById(R.id.SignupBut);
        TextView nameText = (TextView) findViewById(R.id.nameText);
        TextView emailText = (TextView) findViewById(R.id.emailText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);
        RadioButton maleRad = (RadioButton) findViewById(R.id.maleText);
        RadioButton femaleRad = (RadioButton) findViewById(R.id.femaleText);

        SignupBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameText.getText().toString().trim().equals("") || emailText.getText().toString().trim().equals("")
                || passwordText.getText().toString().trim().equals("")){
                    runOnUiThread(() -> {
                        TextView errorText = (TextView) findViewById(R.id.ErrorRegister);
                        errorText.setText("Incomplete");
                        errorText.setVisibility(View.VISIBLE);
                    });
                }
                else if(maleRad.isChecked() || femaleRad.isChecked()){
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

                            boolean prcd = true;

                            while(resultSet.next()){
                                String email = resultSet.getString("Email");
                                if(emailText.getText().toString().trim().equals(email)){
                                    runOnUiThread(() -> {
                                        TextView errorText = (TextView) findViewById(R.id.ErrorRegister);
                                        errorText.setText("Email Already Taken");
                                        errorText.setVisibility(View.VISIBLE);
                                    });
                                    prcd = false;
                                    break;
                                }
                            }

                            if(prcd){
                                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (Name,Email, Password, Type, Gender) VALUES (?, ?, ?, ?, ?)");
                                String name = emailText.getText().toString();
                                String pass = passwordText.getText().toString();
                                String name2 = nameText.getText().toString();
                                String typm = "User";
                                String gender;
                                if(maleRad.isChecked()){
                                    gender = maleRad.getText().toString();
                                }
                                else
                                {
                                    gender  = femaleRad.getText().toString();
                                }

                                preparedStatement.setString(1, name2);
                                preparedStatement.setString(2, name);
                                preparedStatement.setString(3, pass);
                                preparedStatement.setString(4, typm);
                                preparedStatement.setString(5, gender);


                                int rowsInserted = preparedStatement.executeUpdate();
                                if (rowsInserted > 0) {
                                    System.out.println("Inserted User Successfully");
                                    runOnUiThread(() -> {
                                        TextView errorText = (TextView) findViewById(R.id.ErrorRegister);

                                        errorText.setVisibility(View.GONE);
                                    });

                                    startActivity(new Intent(SignUp.this, MainActivity.class));
                                }

                            }

                        } catch (Exception e) {
                            System.out.println("Errorme"+e);
                        }

                    });
                }
                else{
                    runOnUiThread(() -> {
                        TextView errorText = (TextView) findViewById(R.id.ErrorRegister);
                        errorText.setText("Incomplete");
                        errorText.setVisibility(View.VISIBLE);
                    });
                }
            }
        });

    }
}