package com.example.computerstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView textViewTotal;
    private Button buttonOrder, btnUpdateData;
    private int clientId;

   // private Intent intentCart;
    // Danh sách giỏ hàng
    private ArrayList<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart); // Đảm bảo bạn có layout này


        Intent intent = getIntent();
        if (intent != null) {
            clientId = intent.getIntExtra("key_int", 0);
        } else {
            // Xử lý trường hợp intent là null nếu cần
            Toast.makeText(this, "No Intent data available", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity nếu không có dữ liệu
            return;
        }

        // Khởi tạo các thành phần UI
        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        textViewTotal = findViewById(R.id.text_view_total);
        buttonOrder = findViewById(R.id.button_order);
        btnUpdateData = findViewById(R.id.update);

        // Khởi tạo danh sách giỏ hàng
        cartList = new ArrayList<>();
        loadData(); // Tải dữ liệu vào danh sách giỏ hàng

        // Thiết lập RecyclerView
        cartAdapter = new CartAdapter(cartList, this, clientId);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);


// Tính tổng tiền và hiển thị
        double totalAmount = calculateTotal();
        textViewTotal.setText(String.format("Total: %.2f", totalAmount)); // Hiển thị tổng tiền với 2 chữ số thập phân
        // Thiết lập Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent homeIntent = new Intent(CartActivity.this, ClientDashboardActivity.class);
                    homeIntent.putExtra("key_int", clientId);
                    startActivity(homeIntent);
                    return true;
                } else if (item.getItemId() == R.id.nav_cart) {
                    Intent cartIntent = new Intent(CartActivity.this, CartActivity.class);
                    cartIntent.putExtra("key_int", clientId);
                    startActivity(cartIntent);
                    return true;
                } else if (item.getItemId() == R.id.nav_user) {
                    Intent userIntent = new Intent(CartActivity.this, UserActivity.class);
                    userIntent.putExtra("key_int", clientId);
                    startActivity(userIntent);
                    return true;
                }
                return false;
            }
        });
        btnUpdateData.setOnClickListener(v -> {
            Intent cartIntent = new Intent(CartActivity.this, CartActivity.class);
            cartIntent.putExtra("key_int", clientId);
            startActivity(cartIntent);
        });
        // Xử lý sự kiện đặt hàng
        buttonOrder.setOnClickListener(v -> {
            Connection connection = null;
            Statement statement = null;

            try {
                Connect connect = new Connect();
                connection = connect.connection();
                if (connection != null) {
                    statement = connection.createStatement();
                    for (Cart cartItem : cartList) {
                        // Calculate total price if needed
                        //double totalPrice = calculateTotal();
                        // Format current date
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        String sqlInsert = "INSERT INTO [Order] (ClientID, ProductID, ProductName, Price, Quantity, Total, DateAdded) " +
                                "VALUES (" + clientId + ", "
                                + cartItem.getProductId() + ", '"
                                + cartItem.getProductName() + "', "
                                + cartItem.getPrice_d() + ", "
                                + cartItem.getQuantity() + ", "
                                + cartItem.getPrice_d() * cartItem.getQuantity()  + ", '"
                                + currentDate + "')";

                        int rowsInserted = statement.executeUpdate(sqlInsert);
                        if (rowsInserted <= 0) {
                            Toast.makeText(this, "Failed to place order for " + cartItem.getProductName(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // End activity after placing order
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Remove items from cart after placing order
            try {
                Connect connect = new Connect();
                connection = connect.connection();
                if (connection != null) {
                    statement = connection.createStatement();
                    String sqlDelete = "DELETE FROM [Cart] WHERE ClientID = " + clientId;
                    int rowsDeleted = statement.executeUpdate(sqlDelete);
                    if (rowsDeleted > 0) {
                        Log.d("CartAdapter", "Cart cleared successfully.");
                    } else {
                        Log.d("CartAdapter", "Failed to clear cart.");
                    }
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("CartAdapter", "Error clearing cart: " + e.getMessage());
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

        private double calculateTotal() {
        double total = 0.0;

        for (Cart cartItem : cartList) {
            total += cartItem.getQuantity() * cartItem.getPrice_d();
        }

        return total;
    }

    private void loadData() {
        cartList.clear(); // Xóa danh sách cũ
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                String sqlSelect = "SELECT cartId, ProductName, Price, Quantity, ImgProduct, discount, Product.ProductID " +
                        "FROM Cart JOIN Product ON Cart.ProductID = Product.ProductID " +
                        "WHERE ClientID = " + clientId;
                Statement st = connection.createStatement();
                ResultSet resultSet = st.executeQuery(sqlSelect);
                Log.d("Error", String.valueOf(clientId));
                while (resultSet.next()) {
                    int cartId = resultSet.getInt("cartId");
                    String productName = resultSet.getString("ProductName");
                    double price = resultSet.getDouble("Price");
                    int quantity = resultSet.getInt("Quantity");
                    String imgProduct = resultSet.getString("ImgProduct");
                    double discount= resultSet.getDouble("discount");
                    int productId=resultSet.getInt("productID");
                    // Tạo đối tượng Cart và thêm vào danh sách
                    Cart cartItem = new Cart(cartId, productName, price, quantity, imgProduct, discount, productId);
                    cartList.add(cartItem);
                }
                resultSet.close();
                st.close();
                connection.close();
                Log.d("CartActivity", "Data loaded successfully. Cart size: " + cartList.size());
            } else {
                Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
        }
    }}