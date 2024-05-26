package com.example.readify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * Use the {@link ConfirmUpgrade#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmUpgrade extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmUpgrade() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmUpgrade.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmUpgrade newInstance(String param1, String param2) {
        ConfirmUpgrade fragment = new ConfirmUpgrade();
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
        return inflater.inflate(R.layout.fragment_confirm_upgrade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String uid = "?";
        String name = "?";
        String email = "?";
        String passme = "?";
        String typeme = "?";

        if (bundle != null) {
            uid = bundle.getString("uid");
            name = bundle.getString("name");
            email = bundle.getString("email");
            passme = bundle.getString("passme");
            typeme = bundle.getString("typeme");
            System.out.println(email+passme+"Hello");
        }
        else{
            System.out.println("Pass now");
            System.out.println(email+passme);
        }

        TextView invalidinp= view.findViewById(R.id.invalidInp);
        invalidinp.setVisibility(View.GONE);


        Button confirmbut = view.findViewById(R.id.confirmbut);


        final String typemFinal = typeme;
        final int userIdToUpdateFinal = Integer.parseInt(uid);
        final String emailm = email;
        final String passFinal = passme;
        confirmbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView emailconf = view.findViewById(R.id.emialconf);
                EditText passconf = view.findViewById(R.id.passconf);

                if(emailconf.getText().toString().equals(emailm) && passconf.getText().toString().equals(passFinal)){

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

                            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET Type = ? WHERE UserId = ?");

                            String typem = "Author";
                            int userIdToUpdate = userIdToUpdateFinal;
                            preparedStatement.setString(1, typem);
                            preparedStatement.setInt(2, userIdToUpdate);
                            int rowsUpdated = preparedStatement.executeUpdate();
                            if (rowsUpdated > 0) {
                            }
                        } catch (Exception e) {
                            System.out.println("Errorme"+e);
                        }

                    });

                    TextView errorText = view.findViewById(R.id.invalidInp);
                    errorText.setVisibility(View.GONE);


                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                    transaction.setReorderingAllowed(true);

                    transaction.replace(R.id.fragmentContainerView, CongratsMessage.class, null);

                    transaction.addToBackStack(null);

                    transaction.commit();
                }
                else{
                    TextView errorText = view.findViewById(R.id.invalidInp);
                    errorText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //confirmbut
}