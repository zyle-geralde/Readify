package com.example.readify;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditBook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditBook extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String bookidme;

    int YOUR_REQUEST_CODE = 1;

    Uri imageuri;
    Uri pdfuri;

    Uri hold;

    Bundle bundle;

    public EditBook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditBook.
     */
    // TODO: Rename and change types and number of parameters
    public static EditBook newInstance(String param1, String param2) {
        EditBook fragment = new EditBook();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        if (bundle != null) {
            String uid = bundle.getString("uid");
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String passme = bundle.getString("passme");
            String typeme = bundle.getString("typeme");
            bookidme = bundle.getString("bookid");
        }
        else{
            System.out.println("Pass now");
        }

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


                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books WHERE bookid = ?");
                preparedStatement.setInt(1, Integer.parseInt(bookidme));
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    String titledb = resultSet.getString("title");
                    String aboutbookdb = resultSet.getString("aboutbook");
                    String aboutauthdb = resultSet.getString("aboutauthor");
                    int buyersdb = resultSet.getInt("buyers");
                    double ratedb = resultSet.getDouble("rate");
                    double pricedb = resultSet.getDouble("price");
                    Uri coverdb = Uri.parse(resultSet.getString("cover"));
                    Uri bookpdfdb = Uri.parse(resultSet.getString("bookpdf"));

                    pdfuri = bookpdfdb;
                    imageuri = coverdb;

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI here
                            ImageView coverimghold = (ImageView) view.findViewById(R.id.CoverHolder);
                            coverimghold.setImageURI(imageuri);
                        }
                    });


                    Button viewpdfbut = (Button) view.findViewById(R.id.ViewBookBut);
                    viewpdfbut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(requireContext(), PDFViewAct.class);
                            intent.putExtra("pdfUri", pdfuri.toString());
                            startActivity(intent);
                        }
                    });

                    Button ChangeCoverEditBut = (Button) view.findViewById(R.id.ChangeCoverEditBut);
                    ChangeCoverEditBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.setType("image/jpeg"); // Accept all file types
                            startActivityForResult(intent, YOUR_REQUEST_CODE);
                        }
                    });

                    Button ChangeBookEditBut = (Button) view.findViewById(R.id.ChangeBookEditBut);
                    ChangeBookEditBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, YOUR_REQUEST_CODE);
                        }
                    });

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI here
                            EditText TitleEdit = (EditText) view.findViewById(R.id.TitleEdit);
                            TitleEdit.setText(titledb);

                            EditText AboutBookEdit = (EditText) view.findViewById(R.id.AboutBookEdit);
                            AboutBookEdit.setText(aboutbookdb);

                            EditText AboutAuthEdit = (EditText) view.findViewById(R.id.AboutAuthEdit);
                            AboutAuthEdit.setText(aboutauthdb);

                            EditText PriceEdit = (EditText) view.findViewById(R.id.PriceEdit);
                            PriceEdit.setText(pricedb+"");

                            TextView BuyersEdit = (TextView) view.findViewById(R.id.BuyersEdit);
                            BuyersEdit.setText(buyersdb+"");

                            TextView RatingEdit = (TextView) view.findViewById(R.id.RatingEdit);
                            RatingEdit.setText(ratedb+"");

                            RatingBar RatingBarEdit = (RatingBar) view.findViewById(R.id.RatingBarEdit);
                            RatingBarEdit.setRating(Float.parseFloat(ratedb+""));
                            RatingBarEdit.setIsIndicator(true);
                        }
                    });

                    PreparedStatement preparedStatementmyst = connection.prepareStatement("SELECT * FROM mystery WHERE bookid = ?");
                    preparedStatementmyst.setInt(1, Integer.parseInt(bookidme));
                    ResultSet resultSetmyst = preparedStatementmyst.executeQuery();

                    int rowMyst = 0;
                    while(resultSetmyst.next()){
                        rowMyst++;
                    }
                    if(rowMyst>0){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI here
                                CheckBox MysteryCheckEdit = (CheckBox) view.findViewById(R.id.MysteryCheckEdit);
                                MysteryCheckEdit.setChecked(true);
                            }
                        });
                    }

                    //
                    PreparedStatement preparedStatementhorr = connection.prepareStatement("SELECT * FROM horror WHERE bookid = ?");
                    preparedStatementhorr.setInt(1, Integer.parseInt(bookidme));
                    ResultSet resultSethorr = preparedStatementhorr.executeQuery();

                    rowMyst = 0;
                    while(resultSethorr.next()){
                        rowMyst++;
                    }
                    if(rowMyst>0){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI here
                                CheckBox MysteryCheckEdit = (CheckBox) view.findViewById(R.id.HorrorCheckEdit);
                                MysteryCheckEdit.setChecked(true);
                            }
                        });

                    }

                    //
                    PreparedStatement preparedStatementfant = connection.prepareStatement("SELECT * FROM fantasy WHERE bookid = ?");
                    preparedStatementfant.setInt(1, Integer.parseInt(bookidme));
                    ResultSet resultSetfant = preparedStatementfant.executeQuery();

                    rowMyst = 0;
                    while(resultSetfant.next()){
                        rowMyst++;
                    }
                    if(rowMyst>0){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI here
                                CheckBox MysteryCheckEdit = (CheckBox) view.findViewById(R.id.FantasyCheckEdit);
                                MysteryCheckEdit.setChecked(true);
                            }
                        });
                    }

                    //
                    PreparedStatement preparedStatementrom = connection.prepareStatement("SELECT * FROM romance WHERE bookid = ?");
                    preparedStatementrom.setInt(1, Integer.parseInt(bookidme));
                    ResultSet resultSetrom = preparedStatementrom.executeQuery();

                    rowMyst = 0;
                    while(resultSetrom.next()){
                        rowMyst++;
                    }
                    if(rowMyst>0){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI here
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update UI here
                                        CheckBox MysteryCheckEdit = (CheckBox) view.findViewById(R.id.RomanceCheckEdit);
                                        MysteryCheckEdit.setChecked(true);
                                    }
                                });
                            }
                        });
                    }

                    //
                    PreparedStatement preparedStatementoth = connection.prepareStatement("SELECT * FROM others WHERE bookid = ?");
                    preparedStatementoth.setInt(1, Integer.parseInt(bookidme));
                    ResultSet resultSetoth = preparedStatementoth.executeQuery();

                    rowMyst = 0;
                    while(resultSetoth.next()){
                        rowMyst++;
                    }
                    if(rowMyst>0){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI here
                                CheckBox MysteryCheckEdit = (CheckBox) view.findViewById(R.id.OthersCheckEdit);
                                MysteryCheckEdit.setChecked(true);
                            }
                        });
                    }


                }

                Button ApplyEditBut = (Button) view.findViewById(R.id.ApplyEditBut);
                ApplyEditBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText TitleEdit = (EditText) view.findViewById(R.id.TitleEdit);

                        EditText AboutBookEdit = (EditText) view.findViewById(R.id.AboutBookEdit);

                        EditText AboutAuthEdit = (EditText) view.findViewById(R.id.AboutAuthEdit);

                        EditText PriceEdit = (EditText) view.findViewById(R.id.PriceEdit);

                        CheckBox MysteryCheckEdit = (CheckBox) view.findViewById(R.id.MysteryCheckEdit);
                        CheckBox HorrorCheckEdit = (CheckBox) view.findViewById(R.id.HorrorCheckEdit);
                        CheckBox FantasyCheckEdit = (CheckBox) view.findViewById(R.id.FantasyCheckEdit);
                        CheckBox RommanceCheckEdit = (CheckBox) view.findViewById(R.id.RomanceCheckEdit);
                        CheckBox OthersCheckEdit = (CheckBox) view.findViewById(R.id.OthersCheckEdit);

                        if(TitleEdit.getText().toString().trim().equals("") || AboutBookEdit.getText().toString().trim().equals("")
                                ||AboutAuthEdit.getText().toString().trim().equals("") || PriceEdit.getText().toString().trim().equals("")){
                            Context context = requireContext().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, "Invalid Inputs", duration);
                            toast.show();
                        }
                        else if(!MysteryCheckEdit.isChecked() && !HorrorCheckEdit.isChecked() && !FantasyCheckEdit.isChecked() && !RommanceCheckEdit.isChecked() &&
                                !OthersCheckEdit.isChecked()){
                            Context context = requireContext().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, "Invalid Inputs", duration);
                            toast.show();
                        }
                        else{
                            ExecutorService executorService = Executors.newSingleThreadExecutor();
                            CountDownLatch latch = new CountDownLatch(1);
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
                                    String sql = "UPDATE books SET title = ?, aboutbook = ?, aboutauthor = ?, price = ?, cover = ?, bookpdf = ? WHERE bookid = ?";

                                    // Create a prepared statement
                                    PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                                    pstmt.setString(1, TitleEdit.getText().toString());
                                    pstmt.setString(2, AboutBookEdit.getText().toString());
                                    pstmt.setString(3, AboutAuthEdit.getText().toString());
                                    pstmt.setDouble(4, Double.parseDouble(PriceEdit.getText().toString()));
                                    pstmt.setString(5,imageuri+"");
                                    pstmt.setString(6,pdfuri+"");
                                    pstmt.setInt(7,Integer.parseInt(bookidme));

                                    // Execute the statement
                                    pstmt.executeUpdate();

                                    int generatedId = Integer.parseInt(bookidme);

                                    String delme = "DELETE FROM mystery WHERE bookid = ?";


                                    PreparedStatement del1 = connection.prepareStatement(delme);
                                    del1.setInt(1, generatedId);

                                    // Execute the statement
                                    del1.executeUpdate();

                                    //

                                    String delme2 = "DELETE FROM horror WHERE bookid = ?";


                                    PreparedStatement del2 = connection.prepareStatement(delme2);
                                    del2.setInt(1, generatedId);

                                    // Execute the statement
                                    del2.executeUpdate();


                                    //

                                    String delme3 = "DELETE FROM fantasy WHERE bookid = ?";


                                    PreparedStatement del3 = connection.prepareStatement(delme3);
                                    del3.setInt(1, generatedId);

                                    // Execute the statement
                                    del3.executeUpdate();

                                    //

                                    String delme4 = "DELETE FROM romance WHERE bookid = ?";


                                    PreparedStatement del4 = connection.prepareStatement(delme4);
                                    del4.setInt(1, generatedId);

                                    // Execute the statement
                                    del4.executeUpdate();

                                    //

                                    String delme5 = "DELETE FROM others WHERE bookid = ?";


                                    PreparedStatement del5 = connection.prepareStatement(delme5);
                                    del5.setInt(1, generatedId);

                                    // Execute the statement
                                    del5.executeUpdate();


                                    if(MysteryCheckEdit.isChecked()){
                                        String mm = "INSERT INTO mystery (bookid) VALUES (?)";

                                        // Create a prepared statement
                                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                                        pstmtm.setInt(1, generatedId);


                                        // Execute the statement
                                        pstmtm.executeUpdate();
                                    }
                                    if(HorrorCheckEdit.isChecked()){
                                        String mm = "INSERT INTO horror (bookid) VALUES (?)";

                                        // Create a prepared statement
                                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                                        pstmtm.setInt(1, generatedId);


                                        // Execute the statement
                                        pstmtm.executeUpdate();
                                    }
                                    if(RommanceCheckEdit.isChecked()){
                                        String mm = "INSERT INTO romance (bookid) VALUES (?)";

                                        // Create a prepared statement
                                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                                        pstmtm.setInt(1, generatedId);


                                        // Execute the statement
                                        pstmtm.executeUpdate();
                                    }
                                    if(FantasyCheckEdit.isChecked()){
                                        String mm = "INSERT INTO fantasy (bookid) VALUES (?)";

                                        // Create a prepared statement
                                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                                        pstmtm.setInt(1, generatedId);


                                        // Execute the statement
                                        pstmtm.executeUpdate();
                                    }
                                    if(OthersCheckEdit.isChecked()){
                                        String mm = "INSERT INTO others (bookid) VALUES (?)";

                                        // Create a prepared statement
                                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                                        pstmtm.setInt(1, generatedId);


                                        // Execute the statement
                                        pstmtm.executeUpdate();
                                    }



                                    // Close resources
                                    pstmt.close();
                                    connection.close();

                                    latch.countDown();



                                } catch (Exception e) {
                                    System.out.println("Errorme"+e);
                                }

                            });

                            // Wait for the executorService to complete
                            try {
                                latch.await();
                            } catch (InterruptedException e) {
                                // Handle InterruptedException
                            }

                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                            transaction.setReorderingAllowed(true);

                            AuthorRegisteredView fragment = new AuthorRegisteredView();
                            fragment.setArguments(bundle);

                            transaction.replace(R.id.fragmentContainerView, fragment);

                            transaction.addToBackStack(null);

                            transaction.commit();

                        }
                    }
                });




            } catch (Exception e) {
                System.out.println("Errorme"+e);
            }

        });

        ImageView delteImg = (ImageView) view.findViewById(R.id.delteImg);
        delteImg.setOnClickListener(new View.OnClickListener() {
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

                        // SQL statement to insert the URI into the database
                        String sql = "DELETE FROM books WHERE bookid = ?";

                        // Create a prepared statement
                        PreparedStatement pstmt = connection.prepareStatement(sql);
                        pstmt.setInt(1,Integer.parseInt(bookidme));

                        // Execute the statement
                        pstmt.executeUpdate();



                        // Close resources
                        pstmt.close();
                        connection.close();



                    } catch (Exception e) {
                        System.out.println("Errorme"+e);
                    }

                });

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                AuthorRegisteredView fragment = new AuthorRegisteredView();
                fragment.setArguments(bundle);

                transaction.replace(R.id.fragmentContainerView, fragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        Button CancelEditBut = (Button) view.findViewById(R.id.CancelEditBut);
        CancelEditBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                AuthorRegisteredView fragment = new AuthorRegisteredView();
                fragment.setArguments(bundle);

                transaction.replace(R.id.fragmentContainerView, fragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            /*uri = data.getData();
            TextView tt = (TextView) getView().findViewById(R.id.TextCover);
            tt.setText("Uploaded");*/
            if (data != null && data.getData()!=null) {
                hold = data.getData();

                String mimeType = requireActivity().getContentResolver().getType(hold);

                // Check if the selected file is a PDF
                if (mimeType != null && mimeType.equals("application/pdf")) {
                    // Handle PDF file
                    pdfuri = data.getData();

                } else if(mimeType != null && (mimeType.equals("image/jpeg") || mimeType.equals("image/png"))){
                    // Handle image file
                    imageuri = data.getData();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI here
                            ImageView coverimghold = (ImageView) getView().findViewById(R.id.CoverHolder);
                            coverimghold.setImageURI(imageuri);
                        }
                    });

                }
                else{
                    Context context = requireContext().getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Wrong file", duration);
                    toast.show();
                }
            }
            else{
            }
        }
        else{
        }
    }
}