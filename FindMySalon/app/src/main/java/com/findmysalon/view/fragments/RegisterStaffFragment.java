package com.findmysalon.view.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.api.ServiceApi;
import com.findmysalon.api.StaffApi;
import com.findmysalon.model.Category;
import com.findmysalon.model.Service;
import com.findmysalon.model.Staff;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.findmysalon.utils.abcConstants.MAXIMUM_IMAGE_SIZE;

public class RegisterStaffFragment extends Fragment {

    Button btnSave, btnDelete, btnImgUpload;
    Spinner categorySpinner;
    ProgressBar progress;
    TextView txtFullName, txtPhoneNumber;
    ImageView imgProfile;
    View v;
    StaffApi staffApi;
    ServiceApi serviceApi;
    ArrayList<Category> servicesCategoryList;
    ArrayAdapter<String> dataAdapter;
    int categorySpinnerVal;
    String phonePattern = "^[0-9]{10}$";
    String mCurrentPhotoPath = ""; // if image upload successfully
    Uri photoUri = null;
    File photoPath = null;
    File uploadPhotoPath = null;

    Staff staffObject;
    // if editing
    int editId = 0;

    String TAG = "Staff_fragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_staff, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        editId = getArguments().getInt("id", 0);
        v = view;

        btnSave = view.findViewById(R.id.btn_save);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnImgUpload = view.findViewById(R.id.btn_upload_photo);
        categorySpinner = view.findViewById(R.id.spr_category);
        txtFullName = view.findViewById(R.id.etx_staff_name);
        txtPhoneNumber = view.findViewById(R.id.etx_phone);
        imgProfile = view.findViewById(R.id.img_profile_photo);
        progress = view.findViewById(R.id.progress);

        btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v1 -> submit());
        btnImgUpload.setOnClickListener(v1->imageUpload());

        // 1. set up category spinner
        categorySpinnerSetting();
        // 2. delete
        deleteBtnSetting();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    /**
     * image Upload
     */
    private void imageUpload(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext())
        .setTitle("Select picture：")
        .setItems(new String[]{"Camera","Gallery"}, new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            openCamera();
                        }
                        break;
                    case 1:
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                        } else {
                            openAlbum();
                        }
                        break;
                    default:
                        break;
                }

            }});
        builder.create().show();
    }

    protected void openCamera(){

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "cameraOutput" + System.currentTimeMillis() + ".jpg";
        File imagePath = new File(getContext().getFilesDir(), "images");
        photoPath = new File(imagePath, fileName);
        photoUri = FileProvider.getUriForFile(getContext(), "com.findmysalon.fileprovider",
                photoPath);
        if (!photoPath.getParentFile().exists()) {
            photoPath.getParentFile().mkdir();
        }
//        Log.i(TAG, String.valueOf(photoPath));
//        Log.i(TAG, String.valueOf(photoUri));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

        startActivityForResult(intent, 100);
    }

    protected void openAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri tempUri = null;
        switch (requestCode){
            case 100:
                // camera
                if(resultCode == RESULT_OK ) {
                    tempUri = photoUri;
                    uploadPhotoPath = photoPath;
                }
                break;
            case 101:
                // photo album
                if(resultCode == RESULT_OK ) {
                    tempUri = data.getData();
                    uploadPhotoPath = new File(getPath(tempUri));
                }
                break;
        }
        if(resultCode == RESULT_OK){
            if(Helper.toFileSize(uploadPhotoPath.toString()) > MAXIMUM_IMAGE_SIZE){
                Toast.makeText(getActivity(), R.string.invalid_image_size, Toast.LENGTH_LONG).show();
                return;
            }
            if(Helper.checkAndRotateImg(uploadPhotoPath.toString()) == false){
                Log.i(TAG,"Img rotation change failed ");
            }
            progress.setVisibility(View.VISIBLE);
            RequestBody requestFile =
                    RequestBody.Companion.create(uploadPhotoPath, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", uploadPhotoPath.getName(), requestFile);

            Toast.makeText(getActivity(), R.string.lbl_image_loading, Toast.LENGTH_LONG).show();
            // retrofit
            Retrofit retrofit = RetrofitClient.getInstance(getActivity());
            staffApi = retrofit.create(StaffApi.class);
            Call<JsonObject> call;
            call = staffApi.staffAvatarUpload(body);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
                    if(response.isSuccessful()){
                        Log.i(TAG, response.body().get("image").toString().replace("\"",""));
//                        JsonObject jsonObject = new JsonObject().get(response.body().toString()).getAsJsonObject();

                        Glide.with(getContext())
                                .load(response.body().get("image").toString().replace("\"",""))
                                .circleCrop()
                                .placeholder(R.drawable.photos_default)
                                .into(imgProfile);
                        mCurrentPhotoPath = response.body().get("save_image").toString().replace("\"","");
                    } else {
                        Toast.makeText(getActivity(), R.string.submit_fail, Toast.LENGTH_LONG).show();
                    }
                    // Finish remove the progress bar
                    progress.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Fail: ", t.getMessage());
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    // Finish remove the progress bar
                    progress.setVisibility(View.GONE);
                }
            });
            // retrofit End

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    /**
     *  date submit
     */
    private void submit(){
//        int categoryId = categorySpinnerVal;
        String staffFullName = txtFullName.getText().toString();
        String staffPhoneNumber = txtPhoneNumber.getText().toString();

        if(staffFullName.isEmpty() || staffFullName.length() < 2 ) {
            Helper.errorMsgDialog(getActivity(), R.string.invalid_full_name);
            return;
        }

        if(staffPhoneNumber.isEmpty() || !staffPhoneNumber.matches(phonePattern)) {
            Helper.errorMsgDialog(getActivity(), R.string.invalid_phone);
            return;
        }

        Staff staffObj = new Staff(
                servicesCategoryList.get(categorySpinnerVal).getId(),
                staffFullName,
                staffPhoneNumber,
                mCurrentPhotoPath
        );
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        staffApi = retrofit.create(StaffApi.class);
        Call<Staff> call;
        if(editId > 0 ) {
//            staffObj.setId(editId);
            call = staffApi.staffUpdate(editId, staffObj);
        } else {
            call = staffApi.staffSubmit(staffObj);
        }
        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                Log.i(TAG, String.valueOf(response.errorBody()));
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), R.string.submit_success, Toast.LENGTH_LONG).show();
                    Navigation.findNavController(v).popBackStack();
                } else {
                    Toast.makeText(getActivity(), R.string.submit_fail, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End

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
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // retrofit End
    }

    /**
     *  fill up form
     */
    private void fillEditForm(){
        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        staffApi = retrofit.create(StaffApi.class);
        Call<Staff> call = staffApi.staffDetail(editId);
        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
//                Log.i(null,String.valueOf(response.code()));
                if(response.code() == 200){
                    staffObject = response.body();

                    // setup spinner
                    for (int i = 0; i < servicesCategoryList.size(); i++){
                        if(servicesCategoryList.get(i).getId() == staffObject.getCategory().getId()){
                            categorySpinnerVal = i;
                            categorySpinner.setSelection(categorySpinnerVal);
                            break;
                        }
                    }

                    txtFullName.setText(staffObject.getName());
                    txtPhoneNumber.setText(staffObject.getPhoneNumber());

                    Glide.with(getContext())
                            .load(staffObject.getImage())
                            .circleCrop()
                            .placeholder(R.drawable.photos_default)
                            .into(imgProfile);
                }
            }
            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
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
                    new androidx.appcompat.app.AlertDialog.Builder(getActivity()).setTitle(R.string.delete_confirm)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // retrofit
                                Retrofit retrofit = RetrofitClient.getInstance(getActivity());
                                staffApi = retrofit.create(StaffApi.class);

                                Call<ResponseBody> call = staffApi.staffDelete(editId);
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
                                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
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

    /**
     *
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

}
