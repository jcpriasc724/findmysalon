package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findmysalon.R;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.view.adapters.CategoryAdapter;
import com.findmysalon.view.adapters.ServiceAdapter;

import java.util.ArrayList;

public class ServicesByCategoryFragment extends Fragment {

    TextView txtNameCategory;

    RecyclerView recServicesByCategory;
    //RecyclerView recServices;
    ArrayList<Category> categoryList;
    CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_services_by_category, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        txtNameCategory = view.findViewById(R.id.txt_service_by_category);

        recServicesByCategory = view.findViewById(R.id.rec_category);
        recServicesByCategory.setLayoutManager(new LinearLayoutManager(getActivity()));


        categoryList = new ArrayList<Category>();

        ArrayList<Service> servicesList1 = new ArrayList<Service>();

        Service service1 = new Service("Basic hair cut", 20d, 20l, "The hair cut is beautiful");
        Service service2 = new Service("Hair cut with style", 30d, 30l, "The hair cut is beautiful");
        Service service3 = new Service("Hair cut and Beard", 50d, 50l, "The hair cut is beautiful");

        servicesList1.add(service1);
        servicesList1.add(service2);
        servicesList1.add(service3);

        Category category1 = new Category("Category Basic 1", "","", servicesList1);

        ArrayList<Service> servicesList2 = new ArrayList<Service>();

        Service service4 = new Service("Basic hair cut", 20d, 20l, "The hair cut is beautiful");

        servicesList2.add(service4);

        Category category2 = new Category("Category Basic sdsdf 2", "","", servicesList2);

        ArrayList<Service> servicesList3 = new ArrayList<Service>();

        Service service5 = new Service("Basic hair cut", 20d, 20l, "The hair cut is beautiful");
        Service service6 = new Service("Hair cut with style", 30d, 30l, "The hair cut is beautiful");
        Service service7 = new Service("Hair cut and Beard", 50d, 50l, "The hair cut is beautiful");
        Service service8 = new Service("Hair cut and Beard", 50d, 50l, "The hair cut is beautiful");

        servicesList3.add(service5);
        servicesList3.add(service6);
        servicesList3.add(service7);
        servicesList3.add(service8);

        Category category3 = new Category("Category Basic 3", "","", servicesList3);


        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
        recServicesByCategory.setAdapter(categoryAdapter);

        return view;
    }
}
