package com.findmysalon.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.AppointmentApi;
import com.findmysalon.api.StaffApi;
import com.findmysalon.model.Booking;
import com.findmysalon.utils.RetrofitClient;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {

    Context context;
    ArrayList<Booking> list;
    AppointmentApi appointmentApi;

    public BookingAdapter(Context context, ArrayList<Booking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingHolder(LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getResources().getString(R.string.pattern));
        String dateBooking = simpleDateFormat.format(list.get(position).getDateBooking());

        holder.txtDateBooking.setText(dateBooking);
        holder.txtTimeBooking.setText(list.get(position).getStartTime()+" - "+list.get(position).getEndTime());
        holder.txtStaff.setText(list.get(position).getStaff().getName());
        holder.txtCustomer.setText(list.get(position).getCustomer().getFirstName()+" "+list.get(position).getCustomer().getLastName());
        holder.txtService.setText(list.get(position).getService().getNameService());
        if (list.get(position).getStatus().equals("C")){
            holder.textStatus.setText("Cancel");
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.textStatus.setText("Approval");
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Navigation.findNavController(v).navigate(R.id.nav_business_detail);
//            }
//        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(context).setTitle(R.string.cancel_confirm)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // retrofit
                            Retrofit retrofit = RetrofitClient.getInstance(context);
                            appointmentApi = retrofit.create(AppointmentApi.class);

                            Call<ResponseBody> call =
                                    appointmentApi.appointmentStatusChange(list.get(position).getId(), "C");
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(context, R.string.cancel_success, Toast.LENGTH_LONG).show();
                                        holder.btnDelete.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(context, R.string.cancel_fail, Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("Fail: ", t.getMessage());
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            // retrofit End
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDateBooking, txtTimeBooking, txtStaff, txtCustomer, txtService, textStatus;
        ImageButton btnDelete;

        public BookingHolder(@NonNull View itemView) {
            super(itemView);

            txtDateBooking = (TextView) itemView.findViewById(R.id.txt_date_booking);
            txtTimeBooking = (TextView) itemView.findViewById(R.id.txt_time_booking);
            txtStaff = (TextView) itemView.findViewById(R.id.txt_staff);
            txtCustomer = (TextView) itemView.findViewById(R.id.txt_customer);
            txtService = (TextView) itemView.findViewById(R.id.txt_service);
            textStatus = (TextView) itemView.findViewById(R.id.txt_status);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
