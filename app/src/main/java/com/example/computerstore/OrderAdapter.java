package com.example.computerstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText(String.valueOf(order.getOrderId()));
        holder.tvClientId.setText(String.valueOf(order.getClientId()));
        holder.tvProductId.setText(String.valueOf(order.getProductId()));
        holder.tvProductName.setText(order.getProductName());
        holder.tvPrice.setText(String.format("$%.2f", order.getPrice()));
        holder.tvQuantity.setText(String.valueOf(order.getQuantity()));
        holder.tvTotal.setText(String.format("$%.2f", order.getTotal()));
        holder.tvDate.setText(order.getDateAdded());

        holder.btnDelete.setOnClickListener(v -> {
            try {
                Connect connect = new Connect();
                Connection connection = connect.connection();
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String sqlDelete = "DELETE FROM [Order] WHERE orderId=" + order.getOrderId();
                    int rowsDeleted = statement.executeUpdate(sqlDelete);
                    if (rowsDeleted > 0) {
                        Toast.makeText(context, "Order deleted successfully.", Toast.LENGTH_SHORT).show();
                        orderList.remove(position);
                        notifyItemRemoved(position);
                    } else {
                        Toast.makeText(context, "Failed to delete order.", Toast.LENGTH_SHORT).show();
                    }
                    statement.close();
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error deleting order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId;
        private TextView tvClientId;
        private TextView tvProductId;
        private TextView tvProductName;
        private TextView tvPrice;
        private TextView tvQuantity;
        private TextView tvTotal;
        private TextView tvDate;
        private Button btnDelete;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvClientId = itemView.findViewById(R.id.tvClientId);
            tvProductId = itemView.findViewById(R.id.tvProductId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
