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

public class manageOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter OrderAdapter;
    private List<Order> OrderList;
    private Button btnUpdate;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Khởi tạo danh sách Order
        OrderList = new ArrayList<>();
        loadOrderData(); // Tải dữ liệu từ cơ sở dữ liệu

        OrderAdapter = new OrderAdapter(OrderList, this);
        recyclerView.setAdapter(OrderAdapter);

        btnUpdate = findViewById(R.id.btnUpdate1);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }



    private void loadOrderData() {
        OrderList.clear(); // Clear the list before adding new data
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                String sqlSelect = "SELECT * FROM [Order]";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);

                    // Loop through all records in ResultSet
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("OrderID");
                    int clientId = resultSet.getInt("clientId");
                    int productId = resultSet.getInt("productID");
                    String productName = resultSet.getString("productName");
                    double price = resultSet.getDouble("Price");
                    int quantity=resultSet.getInt("quantity");
                    double total = resultSet.getDouble("total");
                    String dateAdded = String.valueOf(resultSet.getDate("dateadded"));
                    // Create a new Order object and add it to the list
                    Order order = new Order(orderId, clientId, productId, productName, price, quantity, total, dateAdded);
                    OrderList.add(order);
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

