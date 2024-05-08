package com.example.readify;

import android.content.Intent;
import android.graphics.Color;
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

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthorRegisteredView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthorRegisteredView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String uid;

    public AuthorRegisteredView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthorRegisteredView.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthorRegisteredView newInstance(String param1, String param2) {
        AuthorRegisteredView fragment = new AuthorRegisteredView();
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
        return inflater.inflate(R.layout.fragment_author_registered_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String passme = bundle.getString("passme");
            String typeme = bundle.getString("typeme");
            System.out.println("Goals");
        }
        else{
            System.out.println("Pass now");
        }

        LinearLayout parentLinearLayout = view.findViewById(R.id.authorbookbox);

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

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books WHERE userid = ?");
                preparedStatement.setInt(1,Integer.parseInt(uid));
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    int bookid = resultSet.getInt("bookid");
                    Uri cover = Uri.parse(resultSet.getString("cover"));
                    String title = resultSet.getString("title");

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> {
                        if(((LinearLayout) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount()-1)).getChildCount() >=2){
                            LinearLayout firstHorizontalLayout = new LinearLayout(requireContext());
                            firstHorizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            firstHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                            firstHorizontalLayout.setPadding(0, 20, 0, 20);
                            parentLinearLayout.addView(firstHorizontalLayout);

                            LinearLayout firstVerticalLayout = new LinearLayout(requireContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            params.setMargins(15, 0, 15, 0);
                            firstVerticalLayout.setLayoutParams(params);
                            firstVerticalLayout.setOrientation(LinearLayout.VERTICAL);
                            firstHorizontalLayout.addView(firstVerticalLayout);
                            firstVerticalLayout.setPadding(20,20,20,20);
                /*int color = ContextCompat.getColor(requireContext(), R.color.bookback);
                firstVerticalLayout.setBackgroundColor(Color.WHITE);*/
                            // Get the existing drawable resource
                            Drawable existingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.curved_background);

// Set the background drawable for your LinearLayout
                            firstVerticalLayout.setBackground(existingDrawable);


// Create and add ImageView to the first vertical LinearLayout
                            ImageView imageView1 = new ImageView(requireContext());
                            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                    600, 700);
                            imageView1.setLayoutParams(imageViewParams);
                            imageView1.setImageURI(cover);
                            firstVerticalLayout.addView(imageView1);

// Create and add TextView to the first vertical LinearLayout
                            TextView textView11 = new TextView(requireContext());
                            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            textViewParams.setMargins(0, 30, 0, 0);
                            textView11.setLayoutParams(textViewParams);
                            textView11.setText(title+"");
                            textView11.setTextSize(17);
                            textView11.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView11.setTypeface(null, Typeface.BOLD);
                            firstVerticalLayout.addView(textView11);


                            TextView textViewID = new TextView(requireContext());
                            LinearLayout.LayoutParams textViewParamsID = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            textViewParamsID.setMargins(0, 0, 0, 0);
                            textViewID.setLayoutParams(textViewParams);
                            textViewID.setText(bookid+"");
                            textViewID.setTextSize(2);
                            textViewID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            firstVerticalLayout.addView(textViewID);
                        }
                        else{
                            LinearLayout firstVerticalLayout = new LinearLayout(requireContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            params.setMargins(15, 0, 15, 0);
                            firstVerticalLayout.setLayoutParams(params);
                            firstVerticalLayout.setOrientation(LinearLayout.VERTICAL);
                            ((LinearLayout) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1)).addView(firstVerticalLayout);
                            firstVerticalLayout.setBackgroundColor(Color.WHITE);
                            firstVerticalLayout.setPadding(20,20,20,20);
                /*int color = ContextCompat.getColor(requireContext(), R.color.bookback);
                firstVerticalLayout.setBackgroundColor(Color.WHITE);*/


                            Drawable existingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.curved_background);

// Set the background drawable for your LinearLayout
                            firstVerticalLayout.setBackground(existingDrawable);


// Create and add ImageView to the first vertical LinearLayout
                            ImageView imageView1 = new ImageView(requireContext());
                            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                    600, 700);
                            imageView1.setLayoutParams(imageViewParams);
                            imageView1.setImageURI(cover);
                            firstVerticalLayout.addView(imageView1);

// Create and add TextView to the first vertical LinearLayout
                            TextView textView11 = new TextView(requireContext());
                            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            textViewParams.setMargins(0, 30, 0, 0);
                            textView11.setLayoutParams(textViewParams);
                            textView11.setText(title+"");
                            textView11.setTextSize(17);
                            textView11.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView11.setTypeface(null, Typeface.BOLD);
                            firstVerticalLayout.addView(textView11);

                            TextView textViewID = new TextView(requireContext());
                            LinearLayout.LayoutParams textViewParamsID = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            textViewParamsID.setMargins(0, 0, 0, 0);
                            textViewID.setLayoutParams(textViewParams);
                            textViewID.setText(bookid+"");
                            textViewID.setTextSize(2);
                            textViewID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            firstVerticalLayout.addView(textViewID);
                        }
                    });
                }

            } catch (Exception e) {
                System.out.println("Errorme"+e);
            }

        });

        TextView SearchBarAuthor = (TextView) view.findViewById(R.id.SearchBarAuthor);
        SearchBarAuthor.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Check if the Enter key was pressed and it's an action down event
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    LinearLayout parentLinearLayout = view.findViewById(R.id.authorbookbox);
                    parentLinearLayout.removeAllViews();
                    LinearLayout firstHorizontalLayout1 = new LinearLayout(requireContext());
                    firstHorizontalLayout1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    firstHorizontalLayout1.setOrientation(LinearLayout.HORIZONTAL);
                    firstHorizontalLayout1.setPadding(0, 20, 0, 20);
                    parentLinearLayout.addView(firstHorizontalLayout1);

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

                            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM books WHERE userid = ? AND title LIKE ?");
                            preparedStatement.setInt(1, Integer.parseInt(uid)); // Set the value for the first placeholder
                            preparedStatement.setString(2, "%" + SearchBarAuthor.getText().toString() + "%");
                            ResultSet resultSet = preparedStatement.executeQuery();

                            int rowcount = 0;
                            while(resultSet.next()){
                                rowcount++;
                                int bookid = resultSet.getInt("bookid");
                                Uri cover = Uri.parse(resultSet.getString("cover"));
                                String title = resultSet.getString("title");

                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(() -> {
                                    if(((LinearLayout) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount()-1)).getChildCount() >=2){
                                        LinearLayout firstHorizontalLayout = new LinearLayout(requireContext());
                                        firstHorizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        firstHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                                        firstHorizontalLayout.setPadding(0, 20, 0, 20);
                                        parentLinearLayout.addView(firstHorizontalLayout);

                                        LinearLayout firstVerticalLayout = new LinearLayout(requireContext());
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                        params.setMargins(15, 0, 15, 0);
                                        firstVerticalLayout.setLayoutParams(params);
                                        firstVerticalLayout.setOrientation(LinearLayout.VERTICAL);
                                        firstHorizontalLayout.addView(firstVerticalLayout);
                                        firstVerticalLayout.setPadding(20,20,20,20);
                /*int color = ContextCompat.getColor(requireContext(), R.color.bookback);
                firstVerticalLayout.setBackgroundColor(Color.WHITE);*/
                                        // Get the existing drawable resource
                                        Drawable existingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.curved_background);

// Set the background drawable for your LinearLayout
                                        firstVerticalLayout.setBackground(existingDrawable);


// Create and add ImageView to the first vertical LinearLayout
                                        ImageView imageView1 = new ImageView(requireContext());
                                        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                                600, 700);
                                        imageView1.setLayoutParams(imageViewParams);
                                        imageView1.setImageURI(cover);
                                        firstVerticalLayout.addView(imageView1);

// Create and add TextView to the first vertical LinearLayout
                                        TextView textView11 = new TextView(requireContext());
                                        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
                                        textViewParams.setMargins(0, 30, 0, 0);
                                        textView11.setLayoutParams(textViewParams);
                                        textView11.setText(title+"");
                                        textView11.setTextSize(17);
                                        textView11.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        textView11.setTypeface(null, Typeface.BOLD);
                                        firstVerticalLayout.addView(textView11);


                                        TextView textViewID = new TextView(requireContext());
                                        LinearLayout.LayoutParams textViewParamsID = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
                                        textViewParamsID.setMargins(0, 0, 0, 0);
                                        textViewID.setLayoutParams(textViewParams);
                                        textViewID.setText(bookid+"");
                                        textViewID.setTextSize(2);
                                        textViewID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        firstVerticalLayout.addView(textViewID);
                                    }
                                    else{
                                        LinearLayout firstVerticalLayout = new LinearLayout(requireContext());
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                        params.setMargins(15, 0, 15, 0);
                                        firstVerticalLayout.setLayoutParams(params);
                                        firstVerticalLayout.setOrientation(LinearLayout.VERTICAL);
                                        ((LinearLayout) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1)).addView(firstVerticalLayout);
                                        firstVerticalLayout.setBackgroundColor(Color.WHITE);
                                        firstVerticalLayout.setPadding(20,20,20,20);
                /*int color = ContextCompat.getColor(requireContext(), R.color.bookback);
                firstVerticalLayout.setBackgroundColor(Color.WHITE);*/


                                        Drawable existingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.curved_background);

// Set the background drawable for your LinearLayout
                                        firstVerticalLayout.setBackground(existingDrawable);


// Create and add ImageView to the first vertical LinearLayout
                                        ImageView imageView1 = new ImageView(requireContext());
                                        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                                600, 700);
                                        imageView1.setLayoutParams(imageViewParams);
                                        imageView1.setImageURI(cover);
                                        firstVerticalLayout.addView(imageView1);

// Create and add TextView to the first vertical LinearLayout
                                        TextView textView11 = new TextView(requireContext());
                                        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
                                        textViewParams.setMargins(0, 30, 0, 0);
                                        textView11.setLayoutParams(textViewParams);
                                        textView11.setText(title+"");
                                        textView11.setTextSize(17);
                                        textView11.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        textView11.setTypeface(null, Typeface.BOLD);
                                        firstVerticalLayout.addView(textView11);

                                        TextView textViewID = new TextView(requireContext());
                                        LinearLayout.LayoutParams textViewParamsID = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
                                        textViewParamsID.setMargins(0, 0, 0, 0);
                                        textViewID.setLayoutParams(textViewParams);
                                        textViewID.setText(bookid+"");
                                        textViewID.setTextSize(2);
                                        textViewID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        firstVerticalLayout.addView(textViewID);
                                    }
                                });
                            }

                            System.out.println("RowCount "+rowcount);

                            if(rowcount == 0){
                                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM books WHERE userid = ?");
                                preparedStatement2.setInt(1,Integer.parseInt(uid));
                                ResultSet resultSet2 = preparedStatement2.executeQuery();

                                while(resultSet2.next()){
                                    int bookid = resultSet2.getInt("bookid");
                                    Uri cover = Uri.parse(resultSet2.getString("cover"));
                                    String title = resultSet2.getString("title");

                                    Handler mainHandler = new Handler(Looper.getMainLooper());
                                    mainHandler.post(() -> {
                                        if(((LinearLayout) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount()-1)).getChildCount() >=2){
                                            LinearLayout firstHorizontalLayout = new LinearLayout(requireContext());
                                            firstHorizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                                            firstHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                                            firstHorizontalLayout.setPadding(0, 20, 0, 20);
                                            parentLinearLayout.addView(firstHorizontalLayout);

                                            LinearLayout firstVerticalLayout = new LinearLayout(requireContext());
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.MATCH_PARENT);
                                            params.setMargins(15, 0, 15, 0);
                                            firstVerticalLayout.setLayoutParams(params);
                                            firstVerticalLayout.setOrientation(LinearLayout.VERTICAL);
                                            firstHorizontalLayout.addView(firstVerticalLayout);
                                            firstVerticalLayout.setPadding(20,20,20,20);
                /*int color = ContextCompat.getColor(requireContext(), R.color.bookback);
                firstVerticalLayout.setBackgroundColor(Color.WHITE);*/
                                            // Get the existing drawable resource
                                            Drawable existingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.curved_background);

// Set the background drawable for your LinearLayout
                                            firstVerticalLayout.setBackground(existingDrawable);


// Create and add ImageView to the first vertical LinearLayout
                                            ImageView imageView1 = new ImageView(requireContext());
                                            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                                    600, 700);
                                            imageView1.setLayoutParams(imageViewParams);
                                            imageView1.setImageURI(cover);
                                            firstVerticalLayout.addView(imageView1);

// Create and add TextView to the first vertical LinearLayout
                                            TextView textView11 = new TextView(requireContext());
                                            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                            textViewParams.setMargins(0, 30, 0, 0);
                                            textView11.setLayoutParams(textViewParams);
                                            textView11.setText(title+"");
                                            textView11.setTextSize(17);
                                            textView11.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                            textView11.setTypeface(null, Typeface.BOLD);
                                            firstVerticalLayout.addView(textView11);


                                            TextView textViewID = new TextView(requireContext());
                                            LinearLayout.LayoutParams textViewParamsID = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                            textViewParamsID.setMargins(0, 0, 0, 0);
                                            textViewID.setLayoutParams(textViewParams);
                                            textViewID.setText(bookid+"");
                                            textViewID.setTextSize(2);
                                            textViewID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                            firstVerticalLayout.addView(textViewID);
                                        }
                                        else{
                                            LinearLayout firstVerticalLayout = new LinearLayout(requireContext());
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.MATCH_PARENT);
                                            params.setMargins(15, 0, 15, 0);
                                            firstVerticalLayout.setLayoutParams(params);
                                            firstVerticalLayout.setOrientation(LinearLayout.VERTICAL);
                                            ((LinearLayout) parentLinearLayout.getChildAt(parentLinearLayout.getChildCount() - 1)).addView(firstVerticalLayout);
                                            firstVerticalLayout.setBackgroundColor(Color.WHITE);
                                            firstVerticalLayout.setPadding(20,20,20,20);
                /*int color = ContextCompat.getColor(requireContext(), R.color.bookback);
                firstVerticalLayout.setBackgroundColor(Color.WHITE);*/


                                            Drawable existingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.curved_background);

// Set the background drawable for your LinearLayout
                                            firstVerticalLayout.setBackground(existingDrawable);


// Create and add ImageView to the first vertical LinearLayout
                                            ImageView imageView1 = new ImageView(requireContext());
                                            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                                                    600, 700);
                                            imageView1.setLayoutParams(imageViewParams);
                                            imageView1.setImageURI(cover);
                                            firstVerticalLayout.addView(imageView1);

// Create and add TextView to the first vertical LinearLayout
                                            TextView textView11 = new TextView(requireContext());
                                            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                            textViewParams.setMargins(0, 30, 0, 0);
                                            textView11.setLayoutParams(textViewParams);
                                            textView11.setText(title+"");
                                            textView11.setTextSize(17);
                                            textView11.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                            textView11.setTypeface(null, Typeface.BOLD);
                                            firstVerticalLayout.addView(textView11);

                                            TextView textViewID = new TextView(requireContext());
                                            LinearLayout.LayoutParams textViewParamsID = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                            textViewParamsID.setMargins(0, 0, 0, 0);
                                            textViewID.setLayoutParams(textViewParams);
                                            textViewID.setText(bookid+"");
                                            textViewID.setTextSize(2);
                                            textViewID.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                            firstVerticalLayout.addView(textViewID);
                                        }
                                    });
                                }
                            }


                        } catch (Exception e) {
                            System.out.println("Errorme"+e);
                        }

                    });
                    return true;
                }
                return false; // Return false to let the event be handled normally
            }
        });


        Button publishbook = (Button) view.findViewById(R.id.publishbutton);

        publishbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                transaction.setReorderingAllowed(true);

                PublishBook fragment = new PublishBook();
                fragment.setArguments(bundle);

                transaction.replace(R.id.fragmentContainerView, fragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

    }

}