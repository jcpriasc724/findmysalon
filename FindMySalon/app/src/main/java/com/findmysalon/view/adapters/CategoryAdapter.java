package com.findmysalon.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    Context context;
    ArrayList<Category> list;
    int businessId;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.txtNameCategory.setText(list.get(position).getNameCategory());

        ServiceByCategoryAdapter serviceAdapter = new ServiceByCategoryAdapter(context, list.get(position).getListServices());
        holder.recServices.setAdapter(serviceAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        TextView txtNameCategory;
        RecyclerView recServices;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCategory = (TextView) itemView.findViewById(R.id.txt_category);

            recServices = itemView.findViewById(R.id.rec_services_by_category);
            recServices.setLayoutManager(new LinearLayoutManager(context));

        }
    }
}
