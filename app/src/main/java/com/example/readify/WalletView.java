package com.example.readify;

import android.content.Context;
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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String uid;

    String walletme;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WalletView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletView.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletView newInstance(String param1, String param2) {
        WalletView fragment = new WalletView();
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
        return inflater.inflate(R.layout.fragment_wallet_view, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            uid = bundle.getString("uid");
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String passme = bundle.getString("passme");
            String typeme = bundle.getString("typeme");
            walletme = bundle.getString("wallet");
            System.out.println("Goals");
        }
        else{
            System.out.println("Pass now");
        }
        Button Depositbut = (Button) view.findViewById(R.id.DepositBut);
        EditText Amounttf = (EditText) view.findViewById(R.id.AmountTf);
        EditText Accounttf = (EditText) view.findViewById(R.id.AccountTf);
        TextView BalanceHold = (TextView) view.findViewById(R.id.BalanceHold);
        Button WithdrawBut = (Button) view.findViewById(R.id.WithdrawBut);

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
                String sql = "SELECT wallet FROM users WHERE UserId = ?";

                // Create a prepared statement
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1,Integer.parseInt(uid));

                // Execute the statement
                ResultSet resultSet= pstmt.executeQuery();

                while(resultSet.next()){
                    double bal = resultSet.getDouble("wallet");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView BalanceHold = (TextView) view.findViewById(R.id.BalanceHold);
                            // This runs on the main thread
                            BalanceHold.setText(String.format("%.2f",bal));
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

        Depositbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Amounttf.getText().toString().trim().equals("") || Accounttf.getText().toString().trim().equals("")){

                    Context context = requireContext().getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Invalid Inputs", duration);
                    toast.show();
                }
                else{
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
                            String sql = "UPDATE users SET wallet = ? WHERE UserId = ?";
                            double newbal = Double.parseDouble(BalanceHold.getText().toString()) + Double.parseDouble(Amounttf.getText().toString());

                            CountDownLatch latch = new CountDownLatch(1);
                            getActivity().runOnUiThread(() -> {
                                try {
                                    EditText Amounttf = view.findViewById(R.id.AmountTf);
                                    EditText Accounttf = view.findViewById(R.id.AccountTf);
                                    TextView BalanceHold = view.findViewById(R.id.BalanceHold);
                                    // This runs on the main thread
                                    BalanceHold.setText(String.format("%.2f", newbal));
                                    Amounttf.setText("");
                                    Accounttf.setText("");
                                } finally {
                                    latch.countDown();  // Signal that UI update is done
                                }
                            });

                            latch.await();

                            // Create a prepared statement
                            PreparedStatement pstmt = connection.prepareStatement(sql);
                            pstmt.setDouble(1,Double.parseDouble(BalanceHold.getText().toString()));
                            pstmt.setInt(2,Integer.parseInt(uid));

                            // Execute the statement
                            pstmt.executeUpdate();

                            // Close resources
                            pstmt.close();
                            connection.close();

                        } catch (Exception e) {
                            System.out.println("Errorme"+e);
                            Context context = requireContext().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // This runs on the main thread
                                    Toast toast = Toast.makeText(context, "Amount Not Valid", duration);
                                    toast.show();
                                }
                            });

                        }

                    });
                }
            }
        });
        WithdrawBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Amounttf.getText().toString().trim().equals("") || Accounttf.getText().toString().trim().equals("")){

                    Context context = requireContext().getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Invalid Inputs", duration);
                    toast.show();
                }
                else if(Double.parseDouble(Amounttf.getText().toString()) > Double.parseDouble(BalanceHold.getText().toString())){
                    Context context = requireContext().getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Not enough balance", duration);
                    toast.show();
                }
                else{
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
                            String sql = "UPDATE users SET wallet = ? WHERE UserId = ?";
                            double newbal = Double.parseDouble(BalanceHold.getText().toString()) - Double.parseDouble(Amounttf.getText().toString());

                            CountDownLatch latch = new CountDownLatch(1);
                            getActivity().runOnUiThread(() -> {
                                try {
                                    EditText Amounttf = (EditText) view.findViewById(R.id.AmountTf);
                                    EditText Accounttf = (EditText) view.findViewById(R.id.AccountTf);
                                    TextView BalanceHold = (TextView) view.findViewById(R.id.BalanceHold);
                                    // This runs on the main thread
                                    BalanceHold.setText(String.format("%.2f",newbal));
                                    Amounttf.setText("");
                                    Accounttf.setText("");
                                } finally {
                                    latch.countDown();  // Signal that UI update is done
                                }
                            });

                            latch.await();

                            // Create a prepared statement
                            PreparedStatement pstmt = connection.prepareStatement(sql);
                            pstmt.setDouble(1,Double.parseDouble(BalanceHold.getText().toString()));
                            pstmt.setInt(2,Integer.parseInt(uid));

                            // Execute the statement
                            pstmt.executeUpdate();

                            // Close resources
                            pstmt.close();
                            connection.close();

                        } catch (Exception e) {
                            System.out.println("Errorme"+e);
                            Context context = requireContext().getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // This runs on the main thread
                                    Toast toast = Toast.makeText(context, "Amount Not Valid", duration);
                                    toast.show();
                                }
                            });

                        }

                    });
                }
            }
        });
    }
}