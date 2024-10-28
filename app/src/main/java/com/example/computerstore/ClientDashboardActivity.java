package com.example.computerstore;

import android.content.Intent;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDashboardActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard); // Đảm bảo bạn đã tạo file activity_client.xml

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // Thiết lập RecyclerView
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        loadProductData(); // Tải dữ liệu từ cơ sở dữ liệu

        productAdapter = new ProductAdapter(productList, this);
        productRecyclerView.setAdapter(productAdapter);

        // Thiết lập Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent homeIntent = new Intent(ClientDashboardActivity.this, ClientDashboardActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent cartIntent = new Intent(ClientDashboardActivity.this, CartActivity.class);
                    startActivity(cartIntent);
                    return true;
                } else if (item.getItemId() == R.id.nav_user) {
                    Intent userIntent = new Intent(ClientDashboardActivity.this, UserActivity.class);
                    startActivity(userIntent);
                    return true;
                }
                return false;
            }
        });
    }
    private void loadProductData() {
        productList.clear(); // Xóa danh sách trước khi thêm dữ liệu mới
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                String sqlSelect = "select* from Product;";
                Statement st = connection.createStatement();
                ResultSet resultSet = st.executeQuery(sqlSelect);

                // Lặp qua tất cả các bản ghi trong ResultSet
                while (resultSet.next()) {
                    int productId = resultSet.getInt("ProductId");
                    String productName = resultSet.getString("productName");
                    String description= resultSet.getString("description");
                    double price= resultSet.getDouble("price");;
                    double discount= resultSet.getDouble("discount");
                    int stockQuantity= resultSet.getInt("stockQuantity");
                    int categoryId= resultSet.getInt("categoryId");
                    String imgProduct= resultSet.getString("imgProduct");

                    Product product= new Product(productId, productName, description, price, discount, stockQuantity, categoryId, imgProduct);
                    productList.add(product);
                }
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            // Xử lý sự kiện cho mục tìm kiếm
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.category) {
            // Xử lý sự kiện cho mục danh mục
            Toast.makeText(this, "Category clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
