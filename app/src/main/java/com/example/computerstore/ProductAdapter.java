package com.example.computerstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
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
        holder.textProductName.setText(product.getProductName());
        holder.textDescription.setText(product.getDescription());
        holder.textPrice.setText("Price: $" + product.getPrice());
        holder.textDiscount.setText("Discount: " + product.getDiscount() + "%");
        holder.textStockQuantity.setText("In Stock: " + product.getStockQuantity());
        Glide.with(context).load(product.getImgProduct()).into(holder.productImageView);

        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addToCart(product);
                Toast.makeText(context, product.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();

            }
        });

        //Picasso.get().load(product.getImgProduct()).into(holder.productImageView);
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
            addCart=itemView.findViewById(R.id.add_cart);
        }
    }

    private void addToCart(Product product) {
        // Add your logic to add the product to the cart


        Toast.makeText(context, product.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();
    }
}
