package com.example.computerstore;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Client> clientList;
    private Context context;

    public ClientAdapter(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_client, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clientList.get(position);
        holder.tvId.setText(String.valueOf(client.getId()));
        holder.tvUsername.setText(client.getUsername());
        holder.tvPhone.setText(client.getPhone());
        holder.tvEmail.setText(client.getEmail());
        holder.tvAddress.setText(client.getAddress());

        holder.btnEdit.setOnClickListener(v -> {
            String username = holder.tvUsername.getText().toString();
            String phone = holder.tvPhone.getText().toString();
            String email = holder.tvEmail.getText().toString();
            String address = holder.tvAddress.getText().toString();

            boolean isUpdated = updateUser(client.getId(), username, phone,email, address);

            if (isUpdated) {
                // Thông báo cho người dùng rằng thông tin đã được cập nhật
                Toast.makeText(context, "User  updated successfully", Toast.LENGTH_SHORT).show();
                // Cập nhật danh sách client trong adapter
                client.setUsername(username);
                client.setPhone(phone);
                client.setEmail(email);
                client.setAddress(address);
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            boolean isDeleted = deleteUser(client.getId());
            if (isDeleted) {
                Toast.makeText(context, "User  updated successfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        EditText tvId, tvUsername, tvPhone, tvEmail, tvAddress;
        Button btnEdit, btnDelete;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail=itemView.findViewById(R.id.tvEmail);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public boolean updateUser(int clientId, String username, String phone, String email, String address) {
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            if (connection != null) {

                // Chèn tài khoản mới
                String sqlUpdate = "Update [user] \n" +
                        "set Username='" + username + "', phone='" + phone + "',email='" + email + "', [Address]='" + address + "'\n" +
                        "where UserID=(select UserID from client where ClientID="+clientId+");";
                Statement st = connection.createStatement();
                int rowsInserted = st.executeUpdate(sqlUpdate);
                if (rowsInserted > 0) {
                    return true; // Tạo tài khoản thành công
                }
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    private boolean deleteUser(int clientId) {
        try {
            Connect connect = new Connect();
            Connection connection = connect.connection();
            Statement st = connection.createStatement();
            int userId;
            if (connection != null) {
            String sqlSelect = "select UserID from client where ClientID="+clientId;
            ResultSet resultSet = st.executeQuery(sqlSelect);

            if (resultSet.next()) {
                userId = resultSet.getInt("UserID");

                String sqlDelete = "delete from client where clientId="+clientId+";\n" +
                        "DELETE FROM [User] \n" +
                        "WHERE UserID ="+userId;
                int rowsInserted = st.executeUpdate(sqlDelete);
                if (rowsInserted > 0) {
                    return true;
                }
                connection.close();
            }}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
