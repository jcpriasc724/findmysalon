package com.findmysalon.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;

import java.util.ArrayList;
import java.util.Date;

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguagesHolder>{

    Context context;
    ArrayList<String> languageList;

    public LanguagesAdapter(Context context, ArrayList<String> languageList) {
        this.context = context;
        this.languageList = languageList;
    }

    @NonNull
    @Override
    public LanguagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LanguagesHolder(LayoutInflater.from(context).inflate(R.layout.item_language, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguagesHolder holder, int position) {

        holder.txtLanguage.setText(languageList.get(position));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("imgDelete","Language: "+languageList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    class LanguagesHolder extends RecyclerView.ViewHolder {

        TextView txtLanguage;
        ImageView imgDelete;

        public LanguagesHolder(@NonNull View itemView) {
            super(itemView);
            txtLanguage = itemView.findViewById(R.id.txt_language);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }
}
