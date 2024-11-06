package com.example.computerstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private int clientId;
    //public ProductAdapter(List<Product> productList, Context context) {
    public ProductAdapter(List<Product> productList, Context context, int clientId) {
        this.productList = productList;
        this.context = context;
        this.clientId = clientId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textProductName.setText(product.getProductName());
        holder.textDescription.setText(product.getDescription());
        holder.textPrice.setText("Price: $" + product.getPrice());
        holder.textDiscount.setText("Discount: " + product.getDiscount() + "%");
        holder.textStockQuantity.setText("In Stock: " + product.getStockQuantity());
        Glide.with(context).load(product.getImgProduct()).into(holder.productImageView);

        holder.addCart.setOnClickListener(v -> addToCart(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView textProductName;
        private TextView textDescription;
        private TextView textPrice;
        private TextView textDiscount;
        private TextView textStockQuantity;
        private ImageView productImageView;
        private ImageView addCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textDescription = itemView.findViewById(R.id.text_description);
            textPrice = itemView.findViewById(R.id.text_price);
            textDiscount = itemView.findViewById(R.id.text_discount);
            textStockQuantity = itemView.findViewById(R.id.text_stock_quantity);
            productImageView = itemView.findViewById(R.id.img_product);
            addCart = itemView.findViewById(R.id.add_cart);
        }
    }

    private void addToCart(Product product) {
        int quantity = 1; // Default quantity
        Cart cartItem = new Cart(0, clientId, product.getProductId(), product.getPrice(), quantity, new Date());

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        if (isProductInCart(product.getProductId())) {
            // Nếu có, tăng quantity
            updateCartItemQuantity(product.getProductId());
            Toast.makeText(context, product.getProductName() + " quantity updated in cart", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu không, thêm sản phẩm vào giỏ hàng
            if (addCartItem(cartItem)) {
                Toast.makeText(context, product.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add " + product.getProductName() + " to cart", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean addCartItem(Cart cartItem) {
        Connection connection = null;
        Statement statement = null;

        try {
            Connect connect = new Connect();
            connection = connect.connection();
            if (connection != null) {
                String sqlInsert = "INSERT INTO cart (clientID, productId, quantity, dateAdded) " +
                        "VALUES (" + cartItem.getClientId() + ", " +
                        cartItem.getProductId() + ", " +
                        cartItem.getQuantity() + ", '" +
                        new java.sql.Timestamp(cartItem.getDateAdded().getTime())+ "')";

                statement = connection.createStatement();
                int rowsInserted = statement.executeUpdate(sqlInsert);
                return rowsInserted > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
        return false; // Return false if connection is null
    }

    private boolean isProductInCart(int productId) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Connect connect = new Connect();
            connection = connect.connection();
            if (connection != null) {
                String sqlCheck = "SELECT quantity FROM cart WHERE clientID = " + clientId + " AND productId = " + productId;
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sqlCheck);
                return resultSet.next(); // Trả về true nếu sản phẩm có trong giỏ hàng
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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
        return false; // Return false if connection is null
    }

    private void updateCartItemQuantity(int productId) {
        Connection connection = null;
        Statement statement = null;

        try {
            Connect connect = new Connect();
            connection = connect.connection();
            if (connection != null) {
                String sqlUpdate = "UPDATE cart SET quantity = quantity + 1 WHERE clientID = " + clientId + " AND productId = " + productId;
                statement = connection.createStatement();
                statement.executeUpdate(sqlUpdate);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    }
}