package com.example.readify;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublishBook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishBook extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int YOUR_REQUEST_CODE = 1;

    String uid;
    Uri uri;

    Uri uripdf;

    Uri hold;

    Bundle bundle;

    public PublishBook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublishBook.
     */
    // TODO: Rename and change types and number of parameters
    public static PublishBook newInstance(String param1, String param2) {
        PublishBook fragment = new PublishBook();
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
        return inflater.inflate(R.layout.fragment_publish_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String passme = bundle.getString("passme");
            String typeme = bundle.getString("typeme");
            System.out.println(uid+name+email+passme+typeme);
        }
        else{
            System.out.println("Pass now");
        }



        Button UploadCoverBUt = (Button) view.findViewById(R.id.UploadCoverBUt);

        UploadCoverBUt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/jpeg"); // Accept all file types
                startActivityForResult(intent, YOUR_REQUEST_CODE);
            }
        });

        Button UploadBookBUt = (Button) view.findViewById(R.id.UploadBookBUt);
        UploadBookBUt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, YOUR_REQUEST_CODE);
            }
        });

        Button PublishBut = (Button) view.findViewById(R.id.PubInp);
        PublishBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUriToDatabase(uri);
            }
        });


        Button CancelBut = (Button) view.findViewById(R.id.CancelBut);
        CancelBut.setOnClickListener(new View.OnClickListener() {
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
                    uripdf = data.getData();
                    TextView tt = (TextView) getView().findViewById(R.id.UploadCover);
                    tt.setText("Uploaded");
                } else if(mimeType != null && (mimeType.equals("image/jpeg") || mimeType.equals("image/png"))){
                    // Handle image file
                    uri = data.getData();
                    TextView tt = (TextView) getView().findViewById(R.id.TextCover);
                    tt.setText("Uploaded");
                }
                else{
                    Context context = requireContext().getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "Wrong file", duration);
                    toast.show();
                    TextView tt = (TextView) getView().findViewById(R.id.TextCover);
                    tt.setText("Set");
                    TextView ttt = (TextView) getView().findViewById(R.id.UploadCover);
                    ttt.setText("Set");
                }
            }
            else{
                TextView tt = (TextView) getView().findViewById(R.id.TextCover);
                tt.setText("Set");
                TextView ttt = (TextView) getView().findViewById(R.id.UploadCover);
                ttt.setText("Set");
            }
        }
        else{
            TextView tt = (TextView) getView().findViewById(R.id.TextCover);
            tt.setText("Set");
            TextView ttt = (TextView) getView().findViewById(R.id.UploadCover);
            ttt.setText("Set");
        }
    }

    private void saveUriToDatabase(Uri uri) {
        TextView titleText = (TextView) getView().findViewById(R.id.TitleInp);
        EditText aboutbook = (EditText) getView().findViewById(R.id.AboutBookInp);
        EditText aboutauth = (EditText) getView().findViewById(R.id.AboutAuthorInp);
        EditText priceinp = (EditText) getView().findViewById(R.id.PriceINp);
        TextView tt = (TextView) getView().findViewById(R.id.TextCover);
        TextView ttt = (TextView) getView().findViewById(R.id.UploadCover);
        CheckBox myst = (CheckBox) getView().findViewById(R.id.MysteryCheck);
        CheckBox hrr = (CheckBox) getView().findViewById(R.id.HorrorCheck);
        CheckBox rom = (CheckBox) getView().findViewById(R.id.RomanceCheck);
        CheckBox fant = (CheckBox) getView().findViewById(R.id.FantasyCheck);
        CheckBox oth = (CheckBox) getView().findViewById(R.id.OthersCheck);

        if(titleText.getText().toString().trim().equals("") || aboutbook.getText().toString().trim().equals("")
        ||aboutauth.getText().toString().trim().equals("") || priceinp.getText().toString().trim().equals("")
        || tt.getText().toString().equals("Set") || ttt.getText().toString().equals("Set")){
            Context context = requireContext().getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Invalid Inputs", duration);
            toast.show();
        }
        else if(!myst.isChecked() && !hrr.isChecked() && !rom.isChecked() && !fant.isChecked() &&
                !oth.isChecked()){
            Context context = requireContext().getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Invalid Inputs", duration);
            toast.show();
        }
        else{
            System.out.println(uid);
            System.out.println(aboutauth.getText());
            System.out.println(titleText.getText());
            System.out.println(aboutauth.getText());
            System.out.println(priceinp.getText());
            System.out.println(tt.getText());
            System.out.println(tt.getText());
            System.out.println(myst.isChecked());
            System.out.println(hrr.isChecked());
            System.out.println(rom.isChecked());
            System.out.println(fant.isChecked());
            System.out.println(oth.isChecked());
            System.out.println("URI: "+uri);

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
                    String sql = "INSERT INTO books (userid,title,aboutbook,aboutauthor,buyers,rate,price,cover,bookpdf) VALUES (?,?,?,?,?,?,?,?,?)";

                    // Create a prepared statement
                    PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                    pstmt.setInt(1, Integer.parseInt(uid));
                    pstmt.setString(2, titleText.getText().toString());
                    pstmt.setString(3, aboutbook.getText().toString());
                    pstmt.setString(4, aboutauth.getText().toString());
                    pstmt.setInt(5, 0);
                    pstmt.setDouble(6, 0);
                    pstmt.setDouble(7,Double.parseDouble(priceinp.getText().toString()));
                    pstmt.setString(8,uri+"");
                    pstmt.setString(9,uripdf+"");

                    // Execute the statement
                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();
                    int generatedId = 0;
                    if(rs.next()){
                        generatedId = rs.getInt(1);
                    }
                    else{

                    }

                    if(myst.isChecked()){
                        String mm = "INSERT INTO mystery (bookid) VALUES (?)";

                        // Create a prepared statement
                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                        pstmtm.setInt(1, generatedId);


                        // Execute the statement
                        pstmtm.executeUpdate();
                    }
                    if(hrr.isChecked()){
                        String mm = "INSERT INTO horror (bookid) VALUES (?)";

                        // Create a prepared statement
                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                        pstmtm.setInt(1, generatedId);


                        // Execute the statement
                        pstmtm.executeUpdate();
                    }
                    if(rom.isChecked()){
                        String mm = "INSERT INTO romance (bookid) VALUES (?)";

                        // Create a prepared statement
                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                        pstmtm.setInt(1, generatedId);


                        // Execute the statement
                        pstmtm.executeUpdate();
                    }
                    if(fant.isChecked()){
                        String mm = "INSERT INTO fantasy (bookid) VALUES (?)";

                        // Create a prepared statement
                        PreparedStatement pstmtm = connection.prepareStatement(mm);
                        pstmtm.setInt(1, generatedId);


                        // Execute the statement
                        pstmtm.executeUpdate();
                    }
                    if(oth.isChecked()){
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
    }

}