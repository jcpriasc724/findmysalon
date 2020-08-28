package com.findmysalon.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.StaffApi;
import com.findmysalon.api.UserApi;
import com.findmysalon.model.Staff;
import com.findmysalon.model.StaffRoster;
import com.findmysalon.model.Token;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.BusinessActivity;
import com.findmysalon.view.CustomerActivity;
import com.findmysalon.view.LoginActivity;
import com.findmysalon.view.adapters.RosterAdapter;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.findmysalon.utils.abcConstants.ACCESS_TOKEN;
import static com.findmysalon.utils.abcConstants.REFRESH_TOKEN;
import static com.findmysalon.utils.abcConstants.TOKEN_EXPIRED;
import static com.findmysalon.utils.abcConstants.TOKEN_VALID_TIME;

public class SetRosterFragment extends Fragment {

    RecyclerView recRoster;
    TextView txtStaff;
    ArrayList<StaffRoster> list;
    RosterAdapter rosterAdapter;
    StaffApi staffApi;
    ImageButton btnAdd;
    int editId;
    String editName;
    DateFormat submitDf;
    ArrayList<HashMap<String,String>> submitData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_roster, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        editId = getArguments().getInt("id", 0);
        editName = getArguments().getString("name", "");

        btnAdd = view.findViewById(R.id.btn_add_service);
        recRoster = view.findViewById(R.id.rec_roster);
        txtStaff = view.findViewById(R.id.txt_staff);
        txtStaff.setText(editName + "'s Schedule");
        recRoster.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<StaffRoster>();

//        Calendar c = Calendar.getInstance();
//
//        for (int i = 1; i <= 7; i++) {
//            Date dt = new Date();
//            c.setTime(dt);
//            c.add(Calendar.DATE, i);
//            dt = c.getTime();
//            StaffRoster staffRoster = new StaffRoster(dt,"09","00","18","00");
//            list.add(staffRoster);
//        }

        rosterAdapter = new RosterAdapter(getActivity(), list);
        recRoster.setAdapter(rosterAdapter);


        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        staffApi = retrofit.create(StaffApi.class);
        Call<ArrayList<StaffRoster>> call = staffApi.scheduleList(editId);
        call.enqueue(new Callback<ArrayList<StaffRoster>>() {
            @Override
            public void onResponse(Call<ArrayList<StaffRoster>> call, Response<ArrayList<StaffRoster>> response) {
                if(response.code() == 200){
                    list.addAll(response.body());
//                    Log.i("SERVICE_LIT", list.toString());
                    rosterAdapter.notifyDataSetChanged();
                }
//
            }

            @Override
            public void onFailure(Call<ArrayList<StaffRoster>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End
        btnAdd.setOnClickListener(v1 -> submit());
        return view;
    }

    private void submit(){
        submitDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        // validate the info
        submitData = new ArrayList<>();
        for (final StaffRoster sr : list) {
            if(sr.getStatus() == 1){

                // if the finish time is earlier than start time
                if( Integer.valueOf(sr.getStartHour() + sr.getStartMin()) >= Integer.valueOf(sr.getEndHour() + sr.getEndMin()) ){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getContext().getResources().getString(R.string.pattern));
                    String errMsg = "At " + simpleDateFormat.format(sr.getDateRoster()) + ": finish time is earlier than start time";
                    Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            HashMap<String,String> paramsMap = new HashMap<>();
            paramsMap.put("date",  submitDf.format(sr.getDateRoster()));
//            paramsMap.put("date",  sr.getDateRoster().toString());
//            Log.i(null, sr.getDateRoster().toString());
            paramsMap.put("start_time", sr.getStartHour() + ":" + sr.getStartMin() );
            paramsMap.put("finish_time", sr.getEndHour() + ":" + sr.getEndMin()  );
            paramsMap.put("status", String.valueOf(sr.getStatus()));
            submitData.add(paramsMap);
        }

        // data to Json
        Gson gson=new Gson();
        HashMap<String,ArrayList<?>> paramsMap= new HashMap<>();
        paramsMap.put("list",  submitData);
        String obj=gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getContext());
        staffApi = retrofit.create(StaffApi.class);
        Call<ResponseBody> call = staffApi.submitScheduleList(editId, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
//                    Token resp = response.body();
                    Toast.makeText(getActivity(), R.string.submit_success, Toast.LENGTH_LONG).show();
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


    @Override
    public void onStart() {
        super.onStart();


    }

}
