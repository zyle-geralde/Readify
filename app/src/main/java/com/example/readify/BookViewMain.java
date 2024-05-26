package com.example.readify;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.Manifest;
import android.content.pm.PackageManager;

public class BookViewMain extends AppCompatActivity {
    Button preview, buy, write_review;
    ImageButton to_reviews;
    LinearLayout read_more_aboutAudiobook, read_more_author;
    Boolean flag = false;

    TextView book_title;
    String userid;
    String bookpdf;
    String buyers;

    String name;

    String rate;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_PDF_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_view_main);



        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        String bookid  = intent.getStringExtra("bookid");
        name  = intent.getStringExtra("name");
        String title = intent.getStringExtra("title");
        String aboutbook = intent.getStringExtra("aboutbook");
        String aboutauthor = intent.getStringExtra("aboutauthor");
        String price = intent.getStringExtra("price");
        rate  = intent.getStringExtra("rate");
        String cover = intent.getStringExtra("cover");
        bookpdf =  intent.getStringExtra("bookpdf");
        buyers = intent.getStringExtra("buyers");
        String wallet =  intent.getStringExtra("wallet");
        String buyername =  intent.getStringExtra("buyername");


        preview = (Button) findViewById(R.id.dlButton);
        buy = (Button) findViewById(R.id.buy_button);
        write_review = (Button) findViewById(R.id.write_review_button);

        book_title = (TextView) findViewById(R.id.book_title);
        book_title.setText(title);

        TextView book_author = (TextView) findViewById(R.id.book_author);
        book_author.setText(name);
        ImageView book_cover_photo = (ImageView) findViewById(R.id.book_cover_photo);
        book_cover_photo.setImageURI(Uri.parse(cover));
        book_cover_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        TextView book_rating = (TextView) findViewById(R.id.book_rating);
        if(Integer.parseInt(buyers) <= 0 && Double.parseDouble(rate) <= 0){
            book_rating.setText("★0.0");
        }
        else{
            book_rating.setText(String.format("★%.1f",(Double.parseDouble(rate)/(double)Integer.parseInt(buyers))));
        }
        TextView about_book_preview= (TextView) findViewById(R.id.about_book_preview);
        about_book_preview.setText(aboutbook);
        TextView about_author_preview = (TextView) findViewById(R.id.about_author_preview);
        about_author_preview.setText(aboutauthor);
        TextView overall_rating = (TextView) findViewById(R.id.overall_rating);
        if(Integer.parseInt(buyers) <= 0 && Double.parseDouble(rate) <= 0){
            overall_rating.setText("0.0");
        }
        else{
            overall_rating.setText(String.format("%.1f",(Double.parseDouble(rate)/(double)Integer.parseInt(buyers))));
        }
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        if(Integer.parseInt(buyers) <= 0 && Double.parseDouble(rate) <= 0){
            ratingBar.setRating(0.0f);
        }
        else{
            ratingBar.setRating(Float.parseFloat(String.format("%.1f",(Double.parseDouble(rate)/(double)Integer.parseInt(buyers)))));
        }
        ratingBar.setIsIndicator(true);

        Button buy_button = (Button) findViewById(R.id.buy_button);
        Button dlButton = findViewById(R.id.dlButton);
        Button write_review_button = (Button) findViewById(R.id.write_review_button);
        RatingBar ratingBar4 = (RatingBar) findViewById(R.id.ratingBar4);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            Connection connection = null;
            try {

                Class.forName("com.mysql.jdbc.Driver");


                String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12709204";


                String username = "sql12709204";
                String password = "aHVxXZQU8u";


                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected...");


                String sqlme= "SELECT * FROM purchasedbooks WHERE userid = ? AND bookid = ?";


                PreparedStatement pstmtme = connection.prepareStatement(sqlme);
                pstmtme.setInt(1,Integer.parseInt(userid));
                pstmtme.setInt(2,Integer.parseInt(bookid));


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
                            dlButton.setEnabled(true);
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buy_button.setText(String.format("Buy $%.2f",Double.parseDouble(price)));
                            dlButton.setEnabled(false);
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

                            Class.forName("com.mysql.jdbc.Driver");


                            String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12709204";


                            String username = "sql12709204";
                            String password = "aHVxXZQU8u";


                            connection = DriverManager.getConnection(url, username, password);
                            System.out.println("Connected...");


                            String sql = "SELECT * FROM users WHERE UserId = ?";


                            PreparedStatement pstmt = connection.prepareStatement(sql);
                            pstmt.setInt(1,Integer.parseInt(userid));


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


                                PreparedStatement pstmt1 = connection.prepareStatement(sql1);
                                pstmt1.setDouble(1,newbal2);
                                pstmt1.setInt(2,Integer.parseInt(userid));


                                pstmt1.executeUpdate();
                                pstmt1.close();

                                String selectSql = "SELECT wallet FROM users WHERE email = ?";

                                PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                                selectStmt.setString(1, name);

                                ResultSet resultSet = selectStmt.executeQuery();

                                double currentBalance = 0.0;
                                if (resultSet.next()) {

                                    currentBalance = resultSet.getDouble("wallet");
                                }

                                resultSet.close();
                                selectStmt.close();


                                String sql3 = "UPDATE users SET wallet = ? WHERE email = ?";
                                double newbal3 = currentBalance + Double.parseDouble(price);
                                String fspec3 = String.format("%.2f",newbal3);
                                double newbal33 = Double.parseDouble(fspec3);


                                PreparedStatement pstmt33 = connection.prepareStatement(sql3);
                                pstmt33.setDouble(1,newbal33);
                                pstmt33.setString(2,name);


                                pstmt33.executeUpdate();
                                pstmt33.close();



                                String sql2 = "INSERT INTO purchasedbooks (bookid,userid) VALUES (?,?)";
                                PreparedStatement pstmt3 = connection.prepareStatement(sql2);
                                pstmt3.setInt(1,Integer.parseInt(bookid));
                                pstmt3.setInt(2,Integer.parseInt(userid));

                                pstmt3.executeUpdate();
                                pstmt3.close();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        buy_button.setText("Read");
                                        dlButton.setEnabled(true);
                                    }
                                });

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




        dlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    System.out.println("Hi");
                    Uri uri = Uri.parse(bookpdf);
                    String fileName = getFileName(uri);
                    String uniqueFileName = getUniqueFileName(fileName);
                    saveFileFromUri(uri, uniqueFileName);
                /*}*/
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
                    params.height = 60;
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
                    params.height = 60;
                    arrow.setRotationX(0);
                    read_more.setText("Read more");
                    flag = false;
                }
                review.setLayoutParams(params);
            }
        });

        write_review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ratingBar4.getRating() <= 0){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Please Rate", duration);
                    toast.show();
                }
                else{
                    if(write_review_button.getText().toString().equals("Rate")){
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

                                String selectSql = "SELECT buyers,rate FROM books WHERE bookid = ?";
                                PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                                selectStmt.setInt(1, Integer.parseInt(bookid));
                                ResultSet rs = selectStmt.executeQuery();

                                int currentBuyers = 0;
                                double currate = 0;
                                if (rs.next()) {
                                    currentBuyers = rs.getInt("buyers");
                                    currate = rs.getDouble("rate");
                                }


                                selectStmt.close();


                                int newBuyers = currentBuyers + 1;


                                String updateSql = "UPDATE books SET buyers = ?, rate = ? WHERE bookid = ?";


                                double rateUp = ratingBar4.getRating();
                                String formatme = String.format("%.1f", rateUp);
                                double newRate = Double.parseDouble(formatme) + currate;


                                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                                updateStmt.setInt(1, newBuyers);
                                updateStmt.setDouble(2, newRate);
                                updateStmt.setInt(3, Integer.parseInt(bookid));


                                int res = updateStmt.executeUpdate();
                                System.out.println("Update result: " + res);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Context context = getApplicationContext();
                                        int duration = Toast.LENGTH_LONG;
                                        Toast toast = Toast.makeText(context, "Rate Successful", duration);
                                        toast.show();
                                        finish();
                                    }
                                });

                                // Close the update statement
                                updateStmt.close();

                                // Close the database connection
                                connection.close();

                            } catch (Exception e) {
                                System.out.println("Errorme"+e);
                            }

                        });
                        finish();
                    }
                }
            }
        });

        ImageView return_button = (ImageView) findViewById(R.id.return_button);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton wishlist_button = findViewById(R.id.wishlist_button);
        wishlist_button.setOnClickListener(new View.OnClickListener() {
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

                        String selectSql = "SELECT * FROM savedbooks WHERE bookid = ? AND userid = ?";
                        PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                        selectStmt.setInt(1, Integer.parseInt(bookid));
                        selectStmt.setInt(2, Integer.parseInt(userid));
                        ResultSet rs = selectStmt.executeQuery();

                        double currate = 0;
                        while (rs.next()) {
                            currate++;
                        }
                        if(currate>0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Context context = getApplicationContext();
                                    int duration = Toast.LENGTH_LONG;
                                    Toast toast = Toast.makeText(context, "Book Already Saved", duration);
                                    toast.show();
                                }
                            });
                        }
                        else{
                            String sql2 = "INSERT INTO savedbooks (bookid,userid) VALUES (?,?)";
                            PreparedStatement pstmt3 = connection.prepareStatement(sql2);
                            pstmt3.setInt(1,Integer.parseInt(bookid));
                            pstmt3.setInt(2,Integer.parseInt(userid));

                            pstmt3.executeUpdate();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Context context = getApplicationContext();
                                    int duration = Toast.LENGTH_LONG;
                                    Toast toast = Toast.makeText(context, "Book Saved", duration);
                                    toast.show();

                                }
                            });
                            pstmt3.close();

                        }

                        // Close the select statement
                        selectStmt.close();

                        // Increment the buyers value


                        // Close the database connection
                        connection.close();

                    } catch (Exception e) {
                        System.out.println("Errorme"+e);
                    }

                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String fileName = getFileName(uri);
                String uniqueFileName = getUniqueFileName(fileName);
                saveFileFromUri(uri, uniqueFileName);
            }
        }
    }
    private String getFileName(Uri uri) {
        String fileName = "downloadedfile.pdf";
        if (uri.getScheme().equals("content")) {
            String[] projection = {DocumentsContract.Document.COLUMN_DISPLAY_NAME};
            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }

    private String getUniqueFileName(String fileName) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);
        String fileBaseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
        int count = 1;
        while (file.exists()) {
            fileName = fileBaseName + "(" + count + ")" + fileExtension;
            file = new File(directory, fileName);
            count++;
        }
        return fileName;
    }

    private void saveFileFromUri(Uri uri, String fileName) {
        ContentResolver contentResolver = getContentResolver();
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, fileName);

        try (InputStream inputStream = contentResolver.openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            Toast.makeText(this, "File downloaded: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                System.out.println();
            }
        }
    }
}