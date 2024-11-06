package com.example.computerstore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Cart> cartList;
    private Context context;
    private int clientId;
    public CartAdapter(ArrayList<Cart> cartList, Context context,int clientId) {
        this.cartList = cartList;
        this.context = context;
        this.clientId=clientId;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cartItem = cartList.get(position);
        holder.productName.setText(cartItem.getProductName());
        holder.price.setText(String.format("$%.2f", cartItem.getPrice_d())); // Hiển thị giá sản phẩm
        holder.editTextQuantity.setText(String.valueOf(cartItem.getQuantity())); // Hiển thị số lượng
        Glide.with(context).load(cartItem.getImgProduct()).into(holder.productImageView);

        // Thiết lập sự kiện cho nút cộng
        holder.buttonPlus.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            holder.editTextQuantity.setText(String.valueOf(cartItem.getQuantity()));
            int quantity= Integer.parseInt(holder.editTextQuantity.getText().toString());
            // Cập nhật tổng tiền ở đây nếu cần
            UpdateQuantity( cartItem, quantity);
        });

        // Thiết lập sự kiện cho nút trừ
        holder.buttonMinus.setOnClickListener(v -> {

            if (cartItem.getQuantity() > 0) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.editTextQuantity.setText(String.valueOf(cartItem.getQuantity()));
                int quantity= Integer.parseInt(holder.editTextQuantity.getText().toString());
                UpdateQuantity( cartItem, quantity);
                // Cập nhật tổng tiền ở đây nếu cần
            }
        });
    }
    public void UpdateQuantity(Cart cartItem, int quantity) {
        Connection connection = null;
        Statement statement = null;

        try {
            Connect connect = new Connect();
            connection = connect.connection();
            if (connection != null) {
                statement = connection.createStatement();
                String sqlUpdate = "UPDATE Cart SET Quantity = " + quantity +
                        " WHERE ClientID = " + clientId +
                        " AND ProductID = " + cartItem.getProductId();
                Log.d("CartAdapter", String.valueOf(quantity));
                Log.d("CartAdapter", String.valueOf(cartItem.getProductId()));
                int rowsUpdated = statement.executeUpdate(sqlUpdate);
                if (rowsUpdated > 0) {
                    Log.d("CartAdapter", "Số lượng sản phẩm đã được cập nhật thành công.");
                } else {
                    Log.d("CartAdapter", "Không tìm thấy sản phẩm để cập nhật.");
                }
            } else {
                Log.e("CartAdapter", "Kết nối đến cơ sở dữ liệu thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("CartAdapter", "Lỗi khi cập nhật số lượng: " + e.getMessage());
        } finally {
            // Đảm bảo đóng các đối tượng JDBC
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView price;
        EditText editTextQuantity;
        ImageButton buttonPlus;
        ImageButton buttonMinus;
        ImageView productImageView;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            price=itemView.findViewById(R.id.product_price);
            editTextQuantity = itemView.findViewById(R.id.edit_text_quantity);
            buttonPlus = itemView.findViewById(R.id.button_plus);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            productImageView= itemView.findViewById(R.id.image_product);
        }
    }
}