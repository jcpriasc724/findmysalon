package com.findmysalon.view.fragments;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.findmysalon.R;
import com.findmysalon.api.BusinessHourApi;
import com.findmysalon.api.StaffApi;
import com.findmysalon.model.BusinessHour;
import com.findmysalon.model.StaffRoster;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.BusinessActivity;
import com.findmysalon.view.adapters.BusinessHourAdapter;
import com.findmysalon.view.adapters.RosterAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BusinessHourFragment extends Fragment {

    private RecyclerView recBusinessHour;
    private ArrayList<BusinessHour> list;
    private BusinessHourAdapter businessHourAdapter;
    private BusinessHourApi businessHourApi;
    private ImageButton btnSave;
    ArrayList<HashMap<String,String>> postData;
    BottomSheetDialog dialogSuccess;
    private View v;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_hour, container, false);
        v = view;
        btnSave = view.findViewById(R.id.btn_save);
        // Access reference of business hour recycler view
        recBusinessHour = view.findViewById(R.id.rec_business_hour);
        recBusinessHour.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<BusinessHour>();

        businessHourAdapter = new BusinessHourAdapter(getActivity(), list);

        // Setting up adapter for recycler view
        recBusinessHour.setAdapter(businessHourAdapter);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        businessHourApi = retrofit.create(BusinessHourApi.class);
        Call<ArrayList<BusinessHour>> call = businessHourApi.businessHourList();
        call.enqueue(new Callback<ArrayList<BusinessHour>>() {
            @Override
            public void onResponse(Call<ArrayList<BusinessHour>> call, Response<ArrayList<BusinessHour>> response) {
                if(response.code() == 200){
                    list.addAll(response.body());
                    businessHourAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BusinessHour>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End

        // retrofit End
        btnSave.setOnClickListener(v1 -> save());

        return view;
    }

    private void save() {
        // Data validation
        postData = new ArrayList<>();
        for (final BusinessHour bh : list){
            if(bh.getStatus() == 1){
                // if the closing time is earlier than opening time
                if( Integer.valueOf(bh.getOpeningHour() + bh.getOpeningMin()) >= Integer.valueOf(bh.getClosingHour() + bh.getClosingMin()) ){
                    String errMsg = " Closing time is earlier than opening time on "+bh.getDay();
                    Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Log.d("Day : ",bh.getDay()+" Opening time: "+bh.getOpeningHour()+":"+bh.getOpeningMin());
            HashMap<String,String> paramsMap = new HashMap<>();
            paramsMap.put("day",  bh.getDay());
            paramsMap.put("opening_time", bh.getOpeningHour() + ":" + bh.getOpeningMin());
            paramsMap.put("closing_time", bh.getClosingHour() + ":" + bh.getClosingMin());
            paramsMap.put("status", String.valueOf(bh.getStatus()));
            postData.add(paramsMap);
        }

        // Encode to Json
        Gson gson=new Gson();
        HashMap<String,ArrayList<?>> paramsMap= new HashMap<>();
        paramsMap.put("list",  postData);
        String obj=gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getContext());
        businessHourApi = retrofit.create(BusinessHourApi.class);
        Call<ResponseBody> call = businessHourApi.submitBusinessHourList(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    showPopUp(v);
                }
                else{
                    Log.d("Error: ",""+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        // retrofit End
    }

    public void showPopUp(View v) {
        dialogSuccess = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);

        View viewBottomSheet = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.popup_success,
                        (LinearLayout) v.findViewById(R.id.bottomSheetContainer)
                );

        CardView btnFinish = (CardView) viewBottomSheet.findViewById(R.id.btn_finish);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView imgDone = viewBottomSheet.findViewById(R.id.img_done);

                Drawable drawable = imgDone.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat){
                    AnimatedVectorDrawableCompat avdc = (AnimatedVectorDrawableCompat) drawable;
                    avdc.start();
                } else if (drawable instanceof AnimatedVectorDrawable){
                    AnimatedVectorDrawable avd = (AnimatedVectorDrawable) drawable;
                    avd.start();
                }
            }
        }, 1000);



        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                //Navigation.findNavController(v).popBackStack();
                dialogSuccess.dismiss();
                Navigation.findNavController(getActivity(), R.id.nav_business_host_fragment).navigate(R.id.nav_business_hour);
            }
        });

        dialogSuccess.setContentView(viewBottomSheet);
        dialogSuccess.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
