package com.findmysalon.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.api.UserApi;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.model.Token;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.BusinessActivity;
import com.findmysalon.view.LoginActivity;
import com.findmysalon.view.adapters.ServiceAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.findmysalon.utils.abcConstants.ACCESS_TOKEN;
import static com.findmysalon.utils.abcConstants.REFRESH_TOKEN;

public class ServicesFragment extends Fragment {

    TextView txtNameService, txtCategory, txtPrice, txtDuration, txtDescription, txtNoServices;

    RecyclerView recServices;
    List<Service> servicesList;
    ServiceAdapter serviceAdapter;

    Button btnNext;
    private ServiceApi serviceApi;
    Button btnAddService;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_services, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameService = view.findViewById(R.id.txt_name_service);
        txtCategory = view.findViewById(R.id.txt_category);
        txtPrice = view.findViewById(R.id.txt_price);
        txtDuration = view.findViewById(R.id.txt_duration);
        txtDescription = view.findViewById(R.id.txt_description);
        txtNoServices = view.findViewById(R.id.txt_no_services);

        recServices = view.findViewById(R.id.rec_services);
        recServices.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnNext = view.findViewById(R.id.btn_next);
        btnAddService = view.findViewById(R.id.btn_add_service);

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", 0);
                Navigation.findNavController(v).navigate(R.id.nav_add_service, bundle);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_reg_business_staff);
            }
        });

        servicesList = new ArrayList<Service>();

//        Category category = new Category("Basicos", "","", null);
//
//        Service service1 = new Service("Basic hair cut", category, 20d, 20l, "The hair cut is beautiful");
//        Service service2 = new Service("Hair cut with style", category, 30d, 30l, "The hair cut is beautiful");
//        Service service3 = new Service("Hair cut and Beard", category, 50d, 50l, "The hair cut is beautiful");
//        servicesList.add(service1);
//        servicesList.add(service2);
//        servicesList.add(service3);

        serviceAdapter = new ServiceAdapter(getActivity(), servicesList);
        recServices.setAdapter(serviceAdapter);

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        serviceApi = retrofit.create(ServiceApi.class);
        Call<ArrayList<Service>> call = serviceApi.serviceList();
        call.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if(response.code() == 200){
                    servicesList.addAll(response.body());
                    Log.i("SERVICE_LIT", servicesList.toString());
                    serviceAdapter.notifyDataSetChanged();

                    if(servicesList.size() > 0){
                        txtNoServices.setVisibility(View.GONE);
                    }
                }
//
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End



        return view;
    }
}
