package com.example.readify;

import static com.example.readify.R.*;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link library_page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class library_page extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String uid;

    String walletme;

    String email;

    String name;

    public library_page() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment library_page.
     */
    // TODO: Rename and change types and number of parameters
    public static library_page newInstance(String param1, String param2) {
        library_page fragment = new library_page();
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
        return inflater.inflate(R.layout.fragment_library_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            name = bundle.getString("name");
            email = bundle.getString("email");
            String passme = bundle.getString("passme");
            String typeme = bundle.getString("typeme");
            walletme = bundle.getString("wallet");
            System.out.println("Goals");
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
                String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12709204";

                // Provide database credentials
                String username = "sql12709204";
                String password = "aHVxXZQU8u";

                // Establish the database connection
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connected...");

                // SQL statement to insert the URI into the database
                String sql = "SELECT * FROM savedbooks WHERE userid = ?";

                // Create a prepared statement
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,Integer.parseInt(uid));


                LinearLayout savedBooksView = view.findViewById(R.id.savedBooksView);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        savedBooksView.removeAllViews();
                    }
                });


                ResultSet resultSet= pstmt.executeQuery();
                // Execute the statement


                while(resultSet.next()){

                    int bookid = resultSet.getInt("bookid");

                    String sqlme = "SELECT * FROM books WHERE bookid = ?";

                    // Create a prepared statement
                    PreparedStatement pstmtme = connection.prepareStatement(sqlme);
                    pstmtme.setInt(1,bookid);

                    ResultSet psRes = pstmtme.executeQuery();

                    while(psRes.next()){
                        int bookidme = psRes.getInt("bookid");
                        Uri cover = Uri.parse(psRes.getString("cover"));
                        String title = psRes.getString("title");
                        double price = psRes.getDouble("price");
                        double rate = psRes.getDouble("rate");
                        String aboutbook = psRes.getString("aboutbook");
                        String aboutauthor = psRes.getString("aboutauthor");
                        int buyers = psRes.getInt("buyers");
                        Uri bookpdf = Uri.parse(psRes.getString("bookpdf"));
                        String authorOfbook = psRes.getString("authorname");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout linearLayout = new LinearLayout(getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                layoutParams.setMargins(0, 0, 0, 40);
                                linearLayout.setLayoutParams(layoutParams);
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                linearLayout.setGravity(Gravity.CENTER_VERTICAL);

                                // Create the first ImageView
                                ImageView imageView4 = new ImageView(getContext());
                                LinearLayout.LayoutParams imageView4Params = new LinearLayout.LayoutParams(
                                        280,
                                        280
                                );
                                imageView4.setLayoutParams(imageView4Params);
                                imageView4.setImageURI(cover);
                                imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);


                                // Create the TextView
                                TextView textView21 = new TextView(getContext());
                                LinearLayout.LayoutParams textView21Params = new LinearLayout.LayoutParams(
                                        280,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                textView21Params.setMargins(40, 0, 40, 0);
                                textView21.setLayoutParams(textView21Params);
                                textView21.setText(title);
                                textView21.setTypeface(null,Typeface.BOLD);
                                textView21.setTextSize(12);

                                // Create the second ImageView
                                ImageView imageView5 = new ImageView(getContext());
                                LinearLayout.LayoutParams imageView5Params = new LinearLayout.LayoutParams(
                                        30,
                                        30
                                );
                                imageView5.setLayoutParams(imageView5Params);
                                imageView5.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.delete));

                                TextView textView22 = new TextView(getContext());
                                textView22.setText(bookidme+"");
                                textView22.setTextSize(1);

                                // Add views to the LinearLayout
                                linearLayout.addView(imageView4);
                                linearLayout.addView(textView21);
                                linearLayout.addView(textView22);
                                linearLayout.addView(imageView5);

                                linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), BookViewMain.class);
                                        intent.putExtra("userid",uid+"");
                                        intent.putExtra("name",authorOfbook+"");
                                        intent.putExtra("bookid",bookid+"");
                                        intent.putExtra("title",title);
                                        intent.putExtra("aboutbook",aboutbook);
                                        intent.putExtra("aboutauthor",aboutauthor);
                                        intent.putExtra("price",price+"");
                                        intent.putExtra("rate",rate+"");
                                        intent.putExtra("cover",cover+"");
                                        intent.putExtra("buyers",buyers+"");
                                        intent.putExtra("bookpdf",bookpdf+"");
                                        intent.putExtra("wallet",walletme+"");
                                        intent.putExtra("buyername",email+"");
                                        startActivity(intent);
                                    }
                                });


                                imageView5.setOnClickListener(new View.OnClickListener() {
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

                                                // SQL statement to insert the URI into the database
                                                String sql = "DELETE FROM savedbooks WHERE bookid = ?";

                                                // Create a prepared statement
                                                PreparedStatement pstmt = connection.prepareStatement(sql);
                                                pstmt.setInt(1,bookidme);

                                                // Execute the statement
                                                pstmt.executeUpdate();

                                                // Close resources
                                                pstmt.close();
                                                connection.close();


                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                                                transaction.setReorderingAllowed(true);

                                                library_page fragment = new library_page();
                                                fragment.setArguments(bundle);

                                                transaction.replace(R.id.fragmentContainerView, fragment);

                                                transaction.addToBackStack(null);

                                                transaction.commit();



                                            } catch (Exception e) {
                                                System.out.println("Errorme"+e);
                                            }

                                        });
                                    }
                                });


                                savedBooksView.addView(linearLayout);
                            }
                        });
                    }


                }

                // Close resources
                pstmt.close();
                connection.close();

            } catch (Exception e) {
                System.out.println("Errorme"+e);
            }

        });



        for(int loop = 0; loop<6; loop++){
            // Create the LinearLayout

        }


    }
}