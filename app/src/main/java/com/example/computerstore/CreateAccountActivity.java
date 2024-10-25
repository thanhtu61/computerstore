package com.example.computerstore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String address = addressEditText.getText().toString();
                new CreateAccountTask().execute(username, password, email, phone, address);
            }
        });
    }

    private class CreateAccountTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            String phone = params[3];
            String address = params[4];
            boolean accountCreated = false;

            try {
                Connect connect = new Connect();
                Connection connection = connect.connection();
                if (connection != null) {
                    // Chèn tài khoản mới
                    String sqlInsert = "INSERT INTO [User] (Username, [Password], Email, Phone, [Address]) " +
                            "VALUES ('" + username + "', '" + password + "', '" + email + "', '" + phone + "', '" + address + "')";
                    Statement st = connection.createStatement();
                    int rowsInserted = st.executeUpdate(sqlInsert);

                    if (rowsInserted > 0) {
                        // Lấy UserID vừa được chèn
                        String sqlSelect = "SELECT UserID FROM [User ] WHERE Phone = '" + phone + "'";
                        ResultSet resultSet = st.executeQuery(sqlSelect);
                        if (resultSet.next()) {
                            int userId = resultSet.getInt("UserID"); // Lấy UserID

                            // Chèn vào bảng client
                            String sqlInsertClient = "INSERT INTO client (UserId) VALUES (" + userId + ")";
                            int rowsInsertedClient = st.executeUpdate(sqlInsertClient);

                            if (rowsInsertedClient > 0) {
                                accountCreated = true; // Tạo tài khoản thành công
                            }
                        }
                    }
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return accountCreated;
        }

        @Override
        protected void onPostExecute(Boolean accountCreated) {
            if (accountCreated) {
                Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại màn hình đăng nhập
            } else {
                Toast.makeText(CreateAccountActivity.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}