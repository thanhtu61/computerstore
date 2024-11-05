package com.example.computerstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class manageClientsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClientAdapter clientAdapter;
    private List<Client> clientList;
    private Button btnUpdate, btnAddClient;
    private EditText etUsername,etEmail,etPhone,etAddress, etPassword;
    Toolbar toolbar;
    Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_client);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Khởi tạo danh sách client
        clientList = new ArrayList<>();
        loadClientData(); // Tải dữ liệu từ cơ sở dữ liệu

        clientAdapter = new ClientAdapter(clientList, this);
        recyclerView.setAdapter(clientAdapter);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etAddress = findViewById(R.id.etAddress);
        Button btnAddClient = findViewById(R.id.btnAddClient);

        btnUpdate=findViewById(R.id.btnUpdate1);
        btnAddClient.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String address = etAddress.getText().toString();

            if (addClient(username,password, email, phone, address)) {
                Toast.makeText(this, "Client added successfully", Toast.LENGTH_SHORT).show();
                loadClientData(); // Reload client data to refresh the list
                //clientAdapter.notifyDataSetChanged(); // Notify the adapter about the data change
            } else {
                Toast.makeText(this, "Failed to add client", Toast.LENGTH_SHORT).show();
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }
    private boolean addClient(String username,String password, String email, String phone, String address) {
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                // Insert the new user
                String sqlInsertUser  = "INSERT INTO [User] (Username,Password, Email, Phone, Address) VALUES ('" +
                        username + "','" + password + "', '" + email + "', '" + phone + "', '" + address + "')";
                Statement statement = connection.createStatement();
                int rowsInserted = statement.executeUpdate(sqlInsertUser );

                if (rowsInserted > 0) {
                    // If the user was added successfully, now add the client
                    String Id = "SELECT UserID FROM [User] WHERE Username = '" + username + "'";
                    ResultSet resultSet = statement.executeQuery(Id);

                    if (resultSet.next()) {
                        int userId = resultSet.getInt("UserID");
                        String sqlInsertClient = "INSERT INTO [Client] (UserID) VALUES (" + userId + ")";
                        statement.executeUpdate(sqlInsertClient);
                    }
                    connection.close();
                    return true; // Client added successfully
                }
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Failed to add client
    }


    private void loadClientData() {
        clientList.clear(); // Xóa danh sách trước khi thêm dữ liệu mới
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                String sqlSelect = "SELECT * FROM [Client] JOIN [user] ON Client.UserID = [user].UserID";
                Statement st = connection.createStatement();
                ResultSet resultSet = st.executeQuery(sqlSelect);

                // Lặp qua tất cả các bản ghi trong ResultSet
                while (resultSet.next()) {
                    int clientId = resultSet.getInt("clientID");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    String address = resultSet.getString("address");

                    // Tạo đối tượng Client và thêm vào clientList
                    Client client = new Client(clientId, username, email, phone, address);
                    clientList.add(client);
                }
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

}