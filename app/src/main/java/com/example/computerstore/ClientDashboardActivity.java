package com.example.computerstore;

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

public class ClientDashboardActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard); // Đảm bảo bạn đã tạo file activity_client.xml

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // Thiết lập RecyclerView
        //productRecyclerView = findViewById(R.id.productRecyclerView);
        //productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Thiết lập adapter cho RecyclerView
        // productRecyclerView.setAdapter(yourAdapter);

        // Thiết lập Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    // Xử lý sự kiện cho Trang Chủ
                    return true;
                } else if (item.getItemId() == R.id.nav_cart) {
                    // Xử lý sự kiện cho Giỏ Hàng
                    return true;
                } else if (item.getItemId() == R.id.nav_user) {
                    // Xử lý sự kiện cho Thông Tin User
                    return true;
                }
                return false;
            }
        });
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
