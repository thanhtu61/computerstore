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

public class manageProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapterAdmin ProductAdapterAdmin;
    private List<Product> ProductList;
    private Button btnUpdate, btnAddProduct;
    private EditText etProductName, etDescription, etPrice, etDiscount, etStockQuantity, etCategoryId, etImgProduct;
    Toolbar toolbar;
    Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Khởi tạo danh sách Product
        ProductList = new ArrayList<>();
        loadProductData(); // Tải dữ liệu từ cơ sở dữ liệu

        ProductAdapterAdmin = new ProductAdapterAdmin(ProductList, this);
        recyclerView.setAdapter(ProductAdapterAdmin);

        etProductName = findViewById(R.id.etProductname);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etDiscount = findViewById(R.id.etDiscount);
        etStockQuantity = findViewById(R.id.etQuantity);
        etCategoryId = findViewById(R.id.etCategory);
        etImgProduct = findViewById(R.id.etImgProduct);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnUpdate = findViewById(R.id.btnUpdate1);

        btnAddProduct.setOnClickListener(v -> {
            String productName = etProductName.getText().toString();
            String description = etDescription.getText().toString();
            double price = Double.parseDouble(etPrice.getText().toString());
            double discount = Double.parseDouble(etDiscount.getText().toString());
            int stockQuantity = Integer.parseInt(etStockQuantity.getText().toString());
            int categoryId = Integer.parseInt(etCategoryId.getText().toString());
            String imgProduct = etImgProduct.getText().toString();

            if (addProduct(productName, description, price, discount, stockQuantity, categoryId, imgProduct)) {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                loadProductData(); // Reload product data to refresh the list
                //ProductAdapter.notifyDataSetChanged(); // Notify the adapter about the data change
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }
    private boolean addProduct(String productName, String description, double price, double discount, int stockQuantity, int categoryId, String imgProduct) {
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                String sqlInsert = "INSERT INTO [Product] (ProductName, Description, Price, Discount, StockQuantity, CategoryID, ImgProduct) " +
                        "VALUES ('" + productName + "', '" + description + "', " + price + ", " + discount + ", " + stockQuantity + ", " + categoryId + ", '" + imgProduct + "')";
                Statement statement = connection.createStatement();
                int rowsInserted = statement.executeUpdate(sqlInsert);
                connection.close();
                return rowsInserted > 0; // Return true if the product was added successfully
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Failed to add product
    }


    private void loadProductData() {
        ProductList.clear(); // Clear the list before adding new data
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                String sqlSelect = "SELECT * FROM [Product]";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);

                // Loop through all records in ResultSet
                while (resultSet.next()) {
                    int productId = resultSet.getInt("ProductID");
                    String productName = resultSet.getString("ProductName");
                    String description = resultSet.getString("Description");
                    double price = resultSet.getDouble("Price");
                    double discount = resultSet.getDouble("Discount");
                    int stockQuantity = resultSet.getInt("StockQuantity");
                    int categoryId = resultSet.getInt("CategoryID");
                    String imgProduct = resultSet.getString("ImgProduct");

                    // Create a new Product object and add it to the list
                    Product product = new Product(productId, productName, description, price, discount, stockQuantity, categoryId, imgProduct);
                    ProductList.add(product);
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

