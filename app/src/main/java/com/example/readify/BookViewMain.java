package com.example.readify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookViewMain extends AppCompatActivity {
    Button preview, buy, write_review;
    ImageButton to_reviews;
    LinearLayout read_more_aboutAudiobook, read_more_author;
    Boolean flag = false;

    TextView book_title;
    String userid;
    String bookpdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_view_main);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        String bookid  = intent.getStringExtra("bookid");
        String name  = intent.getStringExtra("name");
        String title = intent.getStringExtra("title");
        String aboutbook = intent.getStringExtra("aboutbook");
        String aboutauthor = intent.getStringExtra("aboutauthor");
        String price = intent.getStringExtra("price");
        String rate  = intent.getStringExtra("rate");
        String cover = intent.getStringExtra("cover");
        bookpdf =  intent.getStringExtra("bookpdf");
        String buyers = intent.getStringExtra("buyers");
        String wallet =  intent.getStringExtra("wallet");
        String buyername =  intent.getStringExtra("buyername");


        preview = (Button) findViewById(R.id.preview_button);
        buy = (Button) findViewById(R.id.buy_button);
        write_review = (Button) findViewById(R.id.write_review_button);

        book_title = (TextView) findViewById(R.id.book_title);
        book_title.setText(title);

        TextView book_author = (TextView) findViewById(R.id.book_author);
        book_author.setText(name);
        ImageView book_cover_photo = (ImageView) findViewById(R.id.book_cover_photo);
        book_cover_photo.setImageURI(Uri.parse(cover));
        TextView book_rating = (TextView) findViewById(R.id.book_rating);
        book_rating.setText(String.format("%s ★",rate));
        TextView about_book_preview= (TextView) findViewById(R.id.about_book_preview);
        about_book_preview.setText(aboutbook);
        TextView about_author_preview = (TextView) findViewById(R.id.about_author_preview);
        about_author_preview.setText(aboutauthor);
        TextView overall_rating = (TextView) findViewById(R.id.overall_rating);
        overall_rating.setText(rate);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(rate));
        ratingBar.setIsIndicator(true);

        Button buy_button = (Button) findViewById(R.id.buy_button);

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

                // SQL statement to insert the URI into the database
                String sqlme= "SELECT * FROM purchasedbooks WHERE userid = ? AND bookid = ?";

                // Create a prepared statement
                PreparedStatement pstmtme = connection.prepareStatement(sqlme);
                pstmtme.setInt(1,Integer.parseInt(userid));
                pstmtme.setInt(2,Integer.parseInt(bookid));

                // Execute the statement
                ResultSet res = pstmtme.executeQuery();
                int countme = 0;
                while(res.next()){
                    countme++;
                }
                if(countme>0 || buyername.equals(name) || Double.parseDouble(price) <= 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buy_button.setText("Read");
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buy_button.setText(String.format("Buy $%.2f",Double.parseDouble(price)));
                        }
                    });

                }

                // Close resources
                pstmtme.close();

                connection.close();

            } catch (Exception e) {
                System.out.println("Errorme"+e);
            }

        });

        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buy_button.getText().toString().equals("Read")){
                        Intent intent = new Intent(BookViewMain.this, PDFViewAct.class);
                        intent.putExtra("pdfUri",bookpdf);
                        startActivity(intent);

                }
                else{
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

                            // SQL statement to insert the URI into the database
                            String sql = "SELECT * FROM users WHERE UserId = ?";

                            // Create a prepared statement
                            PreparedStatement pstmt = connection.prepareStatement(sql);
                            pstmt.setInt(1,Integer.parseInt(userid));

                            // Execute the statement
                            ResultSet res = pstmt.executeQuery();
                            double walletme = 0;
                            while(res.next()){
                                walletme = res.getDouble("wallet");
                            }
                            double finalWalletme = walletme;
                            if((walletme < Double.parseDouble(price))){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Context context = getApplicationContext();
                                        int duration = Toast.LENGTH_LONG;
                                        Toast toast = Toast.makeText(context, "Insufficient Amount", duration);
                                        toast.show();
                                    }
                                });
                            }
                            else{
                                String sql1 = "UPDATE users SET wallet = ? WHERE UserId = ?";
                                double newbal = walletme - Double.parseDouble(price);
                                String fspec = String.format("%.2f",newbal);
                                double newbal2 = Double.parseDouble(fspec);


                                // Create a prepared statement
                                PreparedStatement pstmt1 = connection.prepareStatement(sql1);
                                pstmt1.setDouble(1,newbal2);
                                pstmt1.setInt(2,Integer.parseInt(userid));

                                // Execute the statement
                                pstmt1.executeUpdate();
                                pstmt1.close();

                                String sql2 = "INSERT INTO purchasedbooks (bookid,userid) VALUES (?,?)";
                                PreparedStatement pstmt3 = connection.prepareStatement(sql2);
                                pstmt3.setInt(1,Integer.parseInt(bookid));
                                pstmt3.setInt(2,Integer.parseInt(userid));

                                pstmt3.executeUpdate();
                                pstmt3.close();

                                buy_button.setText("Read");
                            }

                            // Close resources
                            pstmt.close();

                            connection.close();

                        } catch (Exception e) {
                            System.out.println("Errorme"+e);
                        }

                    });
                }
            }
        });




        read_more_aboutAudiobook = (LinearLayout) findViewById(R.id.read_more_aboutAudiobook);
        read_more_aboutAudiobook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView review = findViewById(R.id.about_book_preview);
                TextView read_more = findViewById(R.id.audiobook_read_more_text);
                ImageView arrow = findViewById(R.id.audiobook_arrow);
                ViewGroup.LayoutParams params = review.getLayoutParams();
                if (!flag) {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    arrow.setRotationX(180);
                    read_more.setText("Read less");
                    flag = true;
                } else {
                    params.height = 60; // Set the height to a fixed value
                    arrow.setRotationX(0);
                    read_more.setText("Read more");
                    flag = false;
                }
                review.setLayoutParams(params);
            }
        });

        read_more_author = (LinearLayout) findViewById(R.id.read_more_aboutAuthor);
        read_more_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView review = findViewById(R.id.about_author_preview);
                TextView read_more = findViewById(R.id.author_read_more_text);
                ImageView arrow = findViewById(R.id.author_arrow);
                ViewGroup.LayoutParams params = review.getLayoutParams();
                if (!flag) {
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    arrow.setRotationX(180);
                    read_more.setText("Read less");
                    flag = true;
                } else {
                    params.height = 60; // Set the height to a fixed value
                    arrow.setRotationX(0);
                    read_more.setText("Read more");
                    flag = false;
                }
                review.setLayoutParams(params);
            }
        });
    }
}