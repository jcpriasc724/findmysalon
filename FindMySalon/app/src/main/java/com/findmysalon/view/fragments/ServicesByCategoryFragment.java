package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.adapters.CategoryAdapter;
import com.findmysalon.view.adapters.ServiceAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServicesByCategoryFragment extends Fragment {

    TextView txtNameCategory, txtKeyword;

    RecyclerView recServicesByCategory;
    //RecyclerView recServices;
    ArrayList<Category> categoryList, categoryListTmp;
    ArrayList<Service> servicesList, servicesListTmp;
    CategoryAdapter categoryAdapter;
    ServiceApi serviceApi;
    ArrayList<Service> sList;
    int curCategoryId;
    int businessId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_services_by_category, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameCategory = view.findViewById(R.id.txt_service_by_category);
        txtKeyword = view.findViewById(R.id.txt_keyword);

        recServicesByCategory = view.findViewById(R.id.rec_category);
        recServicesByCategory.setLayoutManager(new LinearLayoutManager(getActivity()));


        categoryList = new ArrayList<Category>();
        servicesList =  new ArrayList<Service>();
        ArrayList<Service> servicesList1 = new ArrayList<Service>();
        businessId = (int) getArguments().getInt("id");

        categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
        recServicesByCategory.setAdapter(categoryAdapter);

        txtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(categoryListTmp == null || categoryListTmp.size() < 1 )
                    categoryListTmp = categoryList;

                if(txtKeyword.getText().toString().equalsIgnoreCase("")){
                    categoryList = categoryListTmp;
                } else {
                    boolean isContain = false;
                    categoryList = new ArrayList<>();
                    for( Category cate: categoryListTmp){
                        isContain = false;
                        servicesListTmp = new ArrayList<>();
                        for (Service service: cate.getListServices()){
                            if(service.getNameService().toLowerCase().contains(txtKeyword.getText().toString().toLowerCase())){
                                isContain = true;
                                servicesListTmp.add(service);
                            }
                        }
                        if(isContain == true)
                            categoryList.add(new Category(
                                    cate.getId(),
                                    cate.getNameCategory(),
                                    cate.getOrder(),
                                    cate.getDescription(),
                                    servicesListTmp
                            ));
                    }
                }

                categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
                recServicesByCategory.setAdapter(categoryAdapter);
            }
        });
//        Service service1 = new Service("Basic hair cut", 20d, 20l, "The hair cut is beautiful");
//        Service service2 = new Service("Hair cut with style", 30d, 30l, "The hair cut is beautiful");
//        Service service3 = new Service("Hair cut and Beard", 50d, 50l, "The hair cut is beautiful");
//
//        servicesList1.add(service1);
//        servicesList1.add(service2);
//        servicesList1.add(service3);
//
//        Category category1 = new Category("Category Basic 1", "","", servicesList1);
//
//        ArrayList<Service> servicesList2 = new ArrayList<Service>();
//
//        Service service4 = new Service("Basic hair cut", 20d, 20l, "The hair cut is beautiful");
//
//        servicesList2.add(service4);
//
//        Category category2 = new Category("Category Basic sdsdf 2", "","", servicesList2);
//
//        ArrayList<Service> servicesList3 = new ArrayList<Service>();
//
//        Service service5 = new Service("Basic hair cut", 20d, 20l, "The hair cut is beautiful");
//        Service service6 = new Service("Hair cut with style", 30d, 30l, "The hair cut is beautiful");
//        Service service7 = new Service("Hair cut and Beard", 50d, 50l, "The hair cut is beautiful");
//        Service service8 = new Service("Hair cut and Beard", 50d, 50l, "The hair cut is beautiful");
//
//        servicesList3.add(service5);
//        servicesList3.add(service6);
//        servicesList3.add(service7);
//        servicesList3.add(service8);
//
//        Category category3 = new Category("Category Basic 3", "","", servicesList3);
//
//
//        categoryList.add(category1);
//        categoryList.add(category2);
//        categoryList.add(category3);


        loadData();
        return view;
    }


    private void loadData(){
        if( categoryListTmp!=null && categoryListTmp.size() > 0 )
            return;

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        serviceApi = retrofit.create(ServiceApi.class);
        Call<ArrayList<Service>> call = serviceApi.businessServiceList(businessId);
        call.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if(response.code() == 200){
                    servicesList.addAll(response.body());
//                    Log.i("SERVICE_LIT", servicesList.toString());
                    if(servicesList.size() > 0) {
                        for (int index = 0; index < servicesList.size(); index++) {

                            if (curCategoryId == 0) {
                                curCategoryId = servicesList.get(index).getCategory().getId();
                                sList = new ArrayList<Service>();
                            } else if (curCategoryId != servicesList.get(index).getCategory().getId()) {
                                curCategoryId = servicesList.get(index).getCategory().getId();
                                categoryList.add(new Category(
                                        servicesList.get(index - 1).getCategory().getId(),
                                        servicesList.get(index - 1).getCategory().getNameCategory(),
                                        servicesList.get(index - 1).getCategory().getOrder(),
                                        servicesList.get(index - 1).getCategory().getDescription(),
                                        sList
                                ));
                                sList = new ArrayList<Service>();
                            }
                            if( servicesList.get(index).getDisplayStatus().equals("S"))
                                sList.add(servicesList.get(index));
                        }

                        
                        categoryList.add(new Category(
                                servicesList.get(servicesList.size() - 1).getCategory().getId(),
                                servicesList.get(servicesList.size() - 1).getCategory().getNameCategory(),
                                servicesList.get(servicesList.size() - 1).getCategory().getOrder(),
                                servicesList.get(servicesList.size() - 1).getCategory().getDescription(),
                                sList
                        ));
                    }
                    categoryAdapter.notifyDataSetChanged();
                    curCategoryId= 0;
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
    }

}
