package com.findmysalon.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.AppointmentApi;
import com.findmysalon.model.Booking;
import com.findmysalon.utils.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClosedAppointmentAdapter extends RecyclerView.Adapter<ClosedAppointmentAdapter.ClosedAppointmentHolder> {

    Context context;
    ArrayList<Booking> list;
    AppointmentApi appointmentApi;

    public ClosedAppointmentAdapter(Context context, ArrayList<Booking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ClosedAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClosedAppointmentHolder(LayoutInflater.from(context).inflate(R.layout.item_closed_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClosedAppointmentHolder holder, int position) {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getResources().getString(R.string.pattern));
        String dateBooking = simpleDateFormat.format(list.get(position).getDateBooking());

        holder.txtDateBooking.setText(dateBooking);
        holder.txtTimeBooking.setText(list.get(position).getStartTime()+" - "+list.get(position).getEndTime());
        holder.txtStaff.setText(list.get(position).getStaff().getName());
        holder.txtBusinessName.setText(list.get(position).getBusiness().getStoreName());
        holder.txtAddress.setText(list.get(position).getBusiness().getAddress());
        holder.txtService.setText(list.get(position).getService().getNameService());

        holder.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_rate_service);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ClosedAppointmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        TextView txtDateBooking, txtTimeBooking, txtStaff, txtBusinessName, txtAddress, txtService;
        TextView txtDateBooking, txtTimeBooking, txtStaff, txtBusinessName, txtAddress, txtService, textStatus;
        ImageView btnFeedback;


        public ClosedAppointmentHolder(@NonNull View itemView) {
            super(itemView);

            txtDateBooking = (TextView) itemView.findViewById(R.id.txt_date_booking);
            txtTimeBooking = (TextView) itemView.findViewById(R.id.txt_time_booking);
            txtStaff = (TextView) itemView.findViewById(R.id.txt_staff);
            txtBusinessName = (TextView) itemView.findViewById(R.id.txt_business);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtService = (TextView) itemView.findViewById(R.id.txt_service);
            textStatus = (TextView) itemView.findViewById(R.id.txt_status);
            btnFeedback = (ImageView) itemView.findViewById(R.id.img_feedback);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
