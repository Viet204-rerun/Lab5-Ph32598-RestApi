package com.example.lab5_ph32598;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Adapter extends RecyclerView.Adapter<Adapter.ModelViewHolder> {

    private Context context;

    private List<Model> list;

    public Adapter(Context context, List<Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        Model model = list.get(position);
        holder.tv_ten.setText(model.getTen());

        holder.tv_stt.setText(String.valueOf(position + 1));
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ModelViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ten,tv_stt;
        ImageButton btn_xoa;
        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten = itemView.findViewById(R.id.tv_ten);
            tv_stt = itemView.findViewById(R.id.tv_stt);
            btn_xoa = itemView.findViewById(R.id.btn_xoa);
            btn_xoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Model model = list.get(position);
                        showDeleteConfirmationDialog(model.get_id()); // Hiển thị dialog xác nhận xóa
                    }
                }
            });
        }
        private void showDeleteConfirmationDialog(final String id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa?");
            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteLaptop(id);
                    Toast.makeText(context, "Xóa Thành Công!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        private void deleteLaptop(String id) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIService.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            APIService apiService = retrofit.create(APIService.class);
            Call<Model> call = apiService.deleteModel(id);
            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    if (response.isSuccessful()) {
                        // Xóa thành công, cập nhật RecyclerView
                        list.removeIf(model -> model.get_id().equals(id));
                        notifyDataSetChanged();
                    } else {
                        // Xóa thất bại, xử lý thông báo hoặc log lỗi nếu cần
                    }
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    // Xử lý lỗi khi gọi API xóa laptop
                }
            });
        }
    }
}