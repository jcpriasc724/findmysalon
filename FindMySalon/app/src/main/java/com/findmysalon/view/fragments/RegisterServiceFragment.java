package com.findmysalon.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Path;

import java.util.ArrayList;
import java.util.List;

public class RegisterServiceFragment extends Fragment {

    Button btnSave;
    Button btnDelete;
    ServiceApi serviceApi;
    ArrayList<Category> servicesCategoryList;
    ArrayAdapter<String> dataAdapter;
    View v;

    Spinner categorySpinner;
    TextView txtName;
    TextView txtDescription;
    TextView txtPrice;
    TextView txtDuration;
    Switch switchDisplayStatus;
    TextView txtTags;
    TextView txtOrder;


    int categorySpinnerVal = 0;
    String statusSwitchVal = "H";

    // if editing
    int editId = 0;
    Service serviceObject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_services, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        editId = getArguments().getInt("id", 0);
        v = view;
        btnSave = view.findViewById(R.id.btn_save);
        btnDelete = view.findViewById(R.id.btn_delete);
        categorySpinner = view.findViewById(R.id.spr_category);
        txtName = view.findViewById(R.id.etx_service_name);
        txtDescription = view.findViewById(R.id.etx_description_service);
        txtPrice = view.findViewById(R.id.etx_price_service);
        txtDuration = view.findViewById(R.id.etx_duration_service);
        switchDisplayStatus = view.findViewById(R.id.stc_status);
        txtTags = view.findViewById(R.id.etx_tags);
        txtOrder = view.findViewById(R.id.etx_order);

        btnSave.setOnClickListener(v1 -> submit());

        // 1. set up category spinner
        categorySpinnerSetting();
        // 2. switch listener
        displayStatusSwitch();
        // 3. delete listener
        deleteBtnSetting();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     *  fill up form
     */
    private void fillEditForm(){
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        serviceApi = retrofit.create(ServiceApi.class);
        Call<Service> call = serviceApi.serviceDetail(editId);
        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
//                Log.i(null,String.valueOf(response.code()));
                if(response.code() == 200){
                    serviceObject = response.body();

                    // setup spinner
                    for (int i = 0; i < servicesCategoryList.size(); i++){
                        if(servicesCategoryList.get(i).getId() == serviceObject.getCategory().getId()){
                            categorySpinnerVal = i;
                            categorySpinner.setSelection(categorySpinnerVal);
                            break;
                        }
                    }
                    // switch
                    switchDisplayStatus.setChecked(serviceObject.getDisplayStatus().equals("S") ? true : false);
                    txtName.setText(serviceObject.getNameService());
                    txtDescription.setText(serviceObject.getDescription());
                    txtPrice.setText(String.valueOf(serviceObject.getPrice()));
                    txtDuration.setText(String.valueOf(serviceObject.getDuration()));
                    txtTags.setText(serviceObject.getTags());
                    txtOrder.setText(String.valueOf(serviceObject.getOrder()));

                }
            }
            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End
    }

    /**
     *  date submit
     */
    private void submit(){
//        int categoryId = categorySpinnerVal;
        String serviceName = txtName.getText().toString();
        String serviceDesc = txtDescription.getText().toString();
        String servicePrice = txtPrice.getText().toString();
        String serviceDuration = txtDuration.getText().toString();
        String serviceDisStatus = statusSwitchVal;
        String serviceTags = txtTags.getText().toString();
        String serviceOrder = txtOrder.getText().toString();

        if(serviceName.isEmpty() || serviceName.length() <= 3) {
            Helper.errorMsgDialog(getActivity(), R.string.invalid_service_name);
            return;
        }
        if(servicePrice.isEmpty()) {
            Helper.errorMsgDialog(getActivity(), R.string.invalid_service_price);
            return;
        }
        if(serviceDuration.isEmpty()) {
            Helper.errorMsgDialog(getActivity(), R.string.invalid_service_duration);
            return;
        }

        Service serviceObj = new Service(
                    servicesCategoryList.get(categorySpinnerVal).getId(),
                    serviceName,
                    Double.valueOf(servicePrice),
                    Long.valueOf(serviceDuration),
                    serviceDesc,
                    serviceTags,
                    serviceDisStatus,
                    Integer.valueOf(serviceOrder.isEmpty() ? "1" : serviceOrder)
                );
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        serviceApi = retrofit.create(ServiceApi.class);
        Call<Service> call;
        if(editId > 0 ) {
            serviceObj.setId(editId);
            call = serviceApi.serviceUpdate(editId, serviceObj);
        } else {
            call = serviceApi.serviceSubmit(serviceObj);
        }
        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), R.string.submit_success, Toast.LENGTH_LONG).show();
                    Navigation.findNavController(v).popBackStack();
                } else {
                    Toast.makeText(getActivity(), R.string.submit_fail, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End

    }

    /**
     *  display status switch
     */
    private void displayStatusSwitch(){
        switchDisplayStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    statusSwitchVal = "S"; // show
                } else {
                    statusSwitchVal = "H"; // Hide
                }
            }
        });
    }

    /**
     *  category Spinner Set up
     */
    private void categorySpinnerSetting(){
        // fetch services category list
        servicesCategoryList = new ArrayList<Category>();
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        serviceApi = retrofit.create(ServiceApi.class);
        Call<ArrayList<Category>> call = serviceApi.serviceCategoryList();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if(response.isSuccessful()){
                    servicesCategoryList.addAll(response.body());
                    List<String> items = new ArrayList<String>();
                    for(int i = 0; i < servicesCategoryList.size(); i++){
                        items.add(servicesCategoryList.get(i).getNameCategory());
                    }
                    dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                    dataAdapter.notifyDataSetChanged();
                    categorySpinner.setAdapter(dataAdapter);
                    categorySpinner.setSelection(categorySpinnerVal);

                    // listener
                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String info = (String) categorySpinner.getSelectedItem();
                            categorySpinnerVal = position;
//                            Toast.makeText(getActivity(), String.valueOf(categorySpinnerVal), Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if(editId > 0)
                        fillEditForm();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
            }
        });
        // retrofit End
    }

    /**
     *  delete btn
     */
    private void deleteBtnSetting(){
        if(editId > 0 ){
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(getActivity()).setTitle(R.string.delete_confirm)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // retrofit
                                Retrofit retrofit = RetrofitClient.getInstance(getActivity());
                                serviceApi = retrofit.create(ServiceApi.class);

                                Call<ResponseBody> call = serviceApi.serviceDelete(editId);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(getActivity(), R.string.delete_success, Toast.LENGTH_LONG).show();
                                            Navigation.findNavController(v).popBackStack();
                                        } else {
                                            Toast.makeText(getActivity(), R.string.delete_fail, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d("Fail: ", t.getMessage());
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
    }




}
