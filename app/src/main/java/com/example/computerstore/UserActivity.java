package com.example.computerstore;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private TextView textName, textEmail;
    private int clientId;
private  Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        intent = getIntent();

        if (intent != null) {
            this.clientId = intent.getIntExtra("key_int", -1);
        } else {
            // Xử lý trường hợp intent là null nếu cần
            Toast.makeText(this, "No Intent data available", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity nếu không có dữ liệu
            return;
        }

        profileImageView = findViewById(R.id.profileImageView);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);

        // Load user data
        loadUserData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent homeIntent = new Intent(UserActivity.this, ClientDashboardActivity.class);
                    homeIntent.putExtra("key_int", clientId);
                    startActivity(homeIntent);
                    return true;
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent cartIntent = new Intent(UserActivity.this, CartActivity.class);
                    cartIntent.putExtra("key_int", clientId);
                    startActivity(cartIntent);
                    return true;
                } else if (item.getItemId() == R.id.nav_user) {
                    Intent userIntent = new Intent(UserActivity.this, UserActivity.class);
                    userIntent.putExtra("key_int", clientId);
                    startActivity(userIntent);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadUserData() {
        Connection connection = null; // Khai báo biến kết nối ở bên ngoài try-catch
        try {
            Connect connect = new Connect();
            connection = connect.connection(); // Kết nối đến cơ sở dữ liệu
            if (connection != null) {
                String sqlSelect = "SELECT username, email FROM client JOIN [user] ON client.userid = [user].userid WHERE client.clientid = " + this.clientId;
                Statement st = connection.createStatement();
                ResultSet resultSet = st.executeQuery(sqlSelect);
                Log.d("CartAdapter", String.valueOf(clientId));
                // Lặp qua tất cả các bản ghi trong ResultSet
                if (resultSet.next()) { // Sử dụng if thay vì while vì clientId là duy nhất
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    textName.setText(username);
                    textEmail.setText(email);

                    // Tạo đối tượng Client
                    Client client = new Client(username, email);
                    // Nếu cần, có thể lưu client vào biến instance để sử dụng sau này
                } else {
                    Toast.makeText(this, "No user found with the given ID", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to connect to the database", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            // Đảm bảo rằng kết nối được đóng
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

