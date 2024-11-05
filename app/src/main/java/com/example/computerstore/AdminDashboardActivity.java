package com.example.computerstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView adminListView;
    Intent intent = new Intent();
    private List<String> adminOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String[] arr = new String[]{"Client Management", "Product Management", "Warranty Management", "Inventory Management", "Inventory Receipt Management", "Supplier Management"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminDashboardActivity.this, android.R.layout.simple_list_item_1, arr);

        adminListView = findViewById(R.id.adminListView);
        adminListView.setAdapter(adapter);
        adminListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String itemValue = adminListView.getItemAtPosition(position).toString();
                view.setTag(position);
                switch (position) {
                    case 0:
                        intent = new Intent(AdminDashboardActivity.this, manageClientsActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(AdminDashboardActivity.this, manageProductsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
