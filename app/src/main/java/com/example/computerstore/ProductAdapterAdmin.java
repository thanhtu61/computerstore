package com.example.computerstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapterAdmin(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvId.setText(String.valueOf(product.getProductId()));
        holder.tvProductName.setText(product.getProductName());
        holder.tvDescription.setText(product.getDescription());
        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.tvDiscount.setText(String.valueOf(product.getDiscount()));
        holder.tvQuantity.setText(String.valueOf(product.getStockQuantity()));
        holder.tvCategory.setText(String.valueOf(product.getCategoryId()));
        holder.tvImgProduct.setText(product.getImgProduct());

        holder.btnEdit.setOnClickListener(v -> {
            String productName = holder.tvProductName.getText().toString();
            String description = holder.tvDescription.getText().toString();
            double price = Double.parseDouble(holder.tvPrice.getText().toString());
            double discount = Double.parseDouble(holder.tvDiscount.getText().toString());
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            int categoryId = Integer.parseInt(holder.tvCategory.getText().toString());
            String imgProduct = holder.tvImgProduct.getText().toString();

            boolean isUpdated = updateProduct(product.getProductId(), productName, description, price, discount, quantity, categoryId, imgProduct);
            if (isUpdated) {
                Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show();
                product.setProductName(productName);
                product.setDescription(description);
                product.setPrice(price);
                product.setDiscount(discount);
                product.setStockQuantity(quantity);
                product.setCategoryId(categoryId);
                product.setImgProduct(imgProduct);
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Failed to update product", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            boolean isDeleted = deleteProduct(product.getProductId());
            if (isDeleted) {
                Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                productList.remove(position);
                notifyItemRemoved(position);
            } else {
                Toast.makeText(context, "Failed to delete product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        EditText tvId, tvProductName, tvDescription, tvPrice, tvDiscount, tvQuantity, tvCategory, tvImgProduct;
        Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvProductName = itemView.findViewById(R.id.tvProductname);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvImgProduct = itemView.findViewById(R.id.tvImgProduct);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
    private boolean updateProduct(int productId, String productName, String description, double price, double discount, int quantity, int categoryId, String imgProduct) {
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                // Câu lệnh SQL để cập nhật sản phẩm
                String sqlUpdate = "UPDATE Product SET " +
                        "ProductName = '" + productName + "', " +
                        "Description = '" + description + "', " +
                        "Price = " + price + ", " +
                        "Discount = " + discount + ", " +
                        "StockQuantity = " + quantity + ", " +
                        "CategoryID = " + categoryId + ", " +
                        "ImgProduct = '" + imgProduct + "' " +
                        "WHERE ProductID = " + productId;

                Statement statement = connection.createStatement();
                int rowsUpdated = statement.executeUpdate(sqlUpdate);
                connection.close();

                return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Cập nhật thất bại
    }

    private boolean deleteProduct(int productId) {
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {
                // Câu lệnh SQL để xóa sản phẩm
                String sqlDelete = "DELETE FROM Product WHERE ProductID = " + productId;
                Statement statement = connection.createStatement();
                int rowsDeleted = statement.executeUpdate(sqlDelete);
                connection.close();

                return rowsDeleted > 0; // Trả về true nếu xóa thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Xóa thất bại
    }
}

