package com.example.computerstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClientAdapter clientAdapter;
    private List<Client> clientList;
    private Button btnUpdate;
    Toolbar toolbar;
    Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Khởi tạo danh sách client
        clientList = new ArrayList<>();
        loadClientData(); // Tải dữ liệu từ cơ sở dữ liệu

        clientAdapter = new ClientAdapter(clientList, this);
        recyclerView.setAdapter(clientAdapter);

        btnUpdate=findViewById(R.id.btnUpdate1);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
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
                    String phone = resultSet.getString("phone");
                    String address = resultSet.getString("address");

                    // Tạo đối tượng Client và thêm vào clientList
                    Client client = new Client(clientId, username, phone, address);
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