package com.example.computerstore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
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
//        holder.textProductId.setText(String.valueOf(product.getProductId()));
//        holder.textProductName.setText(product.getProductName());
//        holder.textDescription.setText(product.getDescription());
//        holder.textPrice.setText(String.valueOf(product.getPrice()));
//        holder.textDiscount.setText(String.valueOf(product.getDiscount()));
//        holder.textStockQuantity.setText(String.valueOf(product.getCategoryId()));

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView textProductId;
        private TextView textProductName;
        private TextView textDescription;
        private TextView textPrice;
        private TextView textDiscount;
        private TextView textStockQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textDescription = itemView.findViewById(R.id.text_description);
            textPrice = itemView.findViewById(R.id.text_price);
            textDiscount = itemView.findViewById(R.id.text_discount);
            textStockQuantity = itemView.findViewById(R.id.text_stock_quantity);
        }

        public void bind(Product product) {
            textProductName.setText(product.getProductName());
            textDescription.setText(product.getDescription());
            textPrice.setText("Price: $" + product.getPrice());
            textDiscount.setText("Discount: " + product.getDiscount() + "%");
            textStockQuantity.setText("In Stock: " + product.getStockQuantity());
        }
    }
}
