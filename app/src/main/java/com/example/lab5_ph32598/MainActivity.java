package com.example.lab5_ph32598;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<Model> list;
    Adapter adapter ;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call<List<Model>> call = apiService.getModels();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    adapter = new Adapter(context,list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

            }
        });
        FloatingActionButton buttonAdd = findViewById(R.id.btn_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moDialogNhapDuLieu();
            }
        });

        TextInputEditText edt_timkiem = findViewById(R.id.edt_timkiem);
        edt_timkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Khi văn bản thay đổi, gọi hàm timKiem() để tìm kiếm
                timKiem();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý sau khi văn bản thay đổi
            }
        });
    }
    private void timKiem() {
        EditText edtTimKiem = findViewById(R.id.edt_timkiem);
        String tuKhoa = edtTimKiem.getText().toString().trim();

        // Kiểm tra xem từ khóa tìm kiếm có rỗng không
        if (tuKhoa.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi yêu cầu tìm kiếm đến API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call<List<Model>> call = apiService.searchModels(tuKhoa);
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if(response.isSuccessful()){
                    // Hiển thị kết quả tìm kiếm trên RecyclerView
                    List<Model> searchResults = response.body();
                    adapter = new Adapter(context, searchResults);
                    recyclerView.setAdapter(adapter);
                } else {
                    // Xử lý khi không có kết quả tìm kiếm
                    Toast.makeText(MainActivity.this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình tìm kiếm
                Toast.makeText(MainActivity.this, "Đã xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SearchError", "Đã xảy ra lỗi: " + t.getMessage());
            }
        });
    }
    private void moDialogNhapDuLieu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        builder.setView(dialogView);

        EditText edtTen = dialogView.findViewById(R.id.edt_addTen);


        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ten = edtTen.getText().toString().trim();

                // Kiểm tra nếu bất kỳ trường nào trống
                if (ten.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return; // Trả về mà không thực hiện các thao tác tiếp theo
                }
                Model newModal = new Model(ten);
                // Gửi yêu cầu POST đến API để thêm sản phẩm mới
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.DOMAIN)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIService apiService = retrofit.create(APIService.class);
                Call<Model> call = apiService.addModel(newModal);
                call.enqueue(new Callback<Model>() {
                                 @Override
                                 public void onResponse(Call<Model> call, Response<Model> response) {
                                     if (response.isSuccessful()) {
                                         // Nếu thêm thành công, thêm sản phẩm vào danh sách và cập nhật adapter
                                         list.add(newModal);
                                         adapter.notifyDataSetChanged();
                                         Toast.makeText(MainActivity.this, "Thêm  thành công", Toast.LENGTH_SHORT).show();

                                     } else {
                                         // Xử lý khi thêm không thành công
                                         Toast.makeText(MainActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<Model> call, Throwable t) {
                                     Toast.makeText(MainActivity.this, "Có lỗi xảy ra khi thêm " + t.toString(), Toast.LENGTH_SHORT).show();
                                     Log.e("DanhSachLaptop", "Có lỗi xảy ra khi thêm  " + t.toString());
                                 }
                             }

                );
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}