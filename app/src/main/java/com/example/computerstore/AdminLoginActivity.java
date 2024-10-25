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

public class AdminLoginActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private Button adminLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        nameEditText = findViewById(R.id.adminName);
        passwordEditText = findViewById(R.id.adminPassword);
        adminLoginButton = findViewById(R.id.adminLoginButton);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                new LoginTask().execute(name, password);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            boolean loginSuccessful = false;

            try {
                Connect connect = new Connect();
                Connection connection = connect.connection();

                if (connection != null) {
                    String sql = "select * from [Admin] join [User] on [Admin].UserID =[User].UserID  WHERE [User].Username ='"+name +"'and [User].Password='"+password+"'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(sql);

                    if (resultSet.next()) {
                        loginSuccessful = true;
                        Log.d("MyApp", "This is a debug message2");
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
                Toast.makeText(AdminLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AdminLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
