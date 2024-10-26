package com.example.computerstore;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard); // Đảm bảo bạn có layout này

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Khởi tạo danh sách client
        clientList = new ArrayList<>();
        loadClientData(); // Tải dữ liệu từ cơ sở dữ liệu

        clientAdapter = new ClientAdapter(clientList, this);
        recyclerView.setAdapter(clientAdapter);
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
}