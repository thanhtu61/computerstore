package com.example.computerstore;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClientLoginActivity extends AppCompatActivity {
    private EditText clientNameEditText;
    private EditText clientPasswordEditText;
    private Button clientLoginButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        clientNameEditText = findViewById(R.id.clientName);
        clientPasswordEditText = findViewById(R.id.clientPassword);
        clientLoginButton = findViewById(R.id.clientLoginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        clientLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = clientNameEditText.getText().toString();
                String password = clientPasswordEditText.getText().toString();
                new ClientLoginTask().execute(name, password);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientLoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ClientLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            boolean loginSuccessful = false;

            try {
                Connect connect = new Connect();
                Connection connection = connect.connection();
                if (connection != null) {
                    String sql = "select * from Client join [User] on Client.UserID =[User].UserID WHERE [User].Username ='" + name + "' and [User].Password='" + password + "'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(sql);
                    if (resultSet.next()) {
                        loginSuccessful = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loginSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean loginSuccessful) {
            if (loginSuccessful) {
                Intent intent = new Intent(ClientLoginActivity.this, ClientDashboardActivity.class);
                startActivity(intent);
                Log.d("MyApp", "This is a debug message1");
                Toast.makeText(ClientLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(ClientLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
