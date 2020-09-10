package com.findmysalon.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.helpers.PlaceApi;
import com.findmysalon.api.UserApi;
import com.findmysalon.utils.Helper;
import com.findmysalon.view.CustomerActivity;
import com.findmysalon.view.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.findmysalon.utils.abcConstants.BASE_URL;

public class RegisterCustomerFragment extends Fragment {
    public static final String EXTRAS_USER_ID = "user_id";
    static final int REQUEST_CAPTURE_IMAGE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;

    private ImageView mImgView;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mRePassword;
    private UserApi userApi;
    private HashMap<String, Object> addressApi;
    private Uri imageUri = null;
    private View v;
    private int userId;
    private boolean editMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int requestCode = 200;
        String [] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            userId = (int)extras.getSerializable(EXTRAS_USER_ID);
            editMode = true;
        }
        Log.d("User id ", ""+userId);
        Toast.makeText(getActivity(), "Hello "+ userId, Toast.LENGTH_LONG).show();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_register_customer, container, false);

        mImgView = (ImageView) v.findViewById(R.id.img_profile_photo);
        mImgView.setOnClickListener(v12 -> {
            selectImage();
        });

        /*Button mUpload = (Button) v.findViewById(R.id.btn_upload_photo);
        mUpload.setOnClickListener(v1 -> selectImage());*/

        // Address autocomplete suggestions
        PlaceApi placeApi = new PlaceApi();
        addressApi = placeApi.fetchAddressLatLng((AutoCompleteTextView) v.findViewById(R.id.etx_address), getActivity());
        mFirstName = (EditText) v.findViewById(R.id.etx_first_name);
        mLastName = (EditText) v.findViewById(R.id.etx_last_name);
        mEmail = (EditText) v.findViewById(R.id.etx_email);
        mPhone = (EditText) v.findViewById(R.id.etx_phone);
        mPassword = (EditText) v.findViewById(R.id.etx_password);
        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);
        CardView mSubmitButton = v.findViewById(R.id.btn_submit);
        mSubmitButton.setOnClickListener(v1 -> customerSignUp());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void selectImage() {
        final String[] options = getResources().getStringArray(R.array.photo_options);;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ADD PROFILE PHOTO");
        builder.setItems(options, (dialog, item) -> {
            Log.d("PHOTO", item+"");
            switch (item){
                case 0:
                    // Check if camera is supported in user's device
                    if(isDeviceSupportCamera()){
                        takePhoto();
                    }
                    else{// Display message if camera is not supported
                        Helper.errorMsgDialog(getActivity().getApplication(),R.string.camera_not_supported);
                        //dialog.dismiss();
                    }
                    break;
                case 1:
                    chooseFromGallery();
                    break;
            }
        });
        builder.show();
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    private void takePhoto() {
        // Intent to open camera
        Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Checking if there any app that can handle the IMAGE_CAPTURE intent to avoid app crash
        if (intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            // For camera capture
            if(requestCode == REQUEST_CAPTURE_IMAGE){
                /*Bitmap imageBitmap = BitmapFactory.decodeFile(pathToFile);
                Log.d("Path to file", ""+pathToFile);*/
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageUri = getImageUri(getActivity().getApplicationContext(), imageBitmap);
                mImgView.setImageBitmap(imageBitmap);
            }
            // For gallery image
            else if(requestCode == REQUEST_GALLERY_IMAGE){
                //Get selected image uri here
                imageUri = data.getData();
                mImgView.setImageURI(imageUri);
            }
            // Plugin to display image
            Glide.with(getContext())
                    .load(imageUri)
                    .circleCrop()
                    .placeholder(R.drawable.photos_default)
                    .into(mImgView);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // Method to handle customer signUp
    public void customerSignUp(){
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String rePassword = mRePassword.getText().toString();
        Log.d("ADDRESS", addressApi+"");
        String address = "";
        double latitude = 0.0;
        double longitude = 0.0;

        if(!addressApi.isEmpty()){
            address = (String) addressApi.get("address");
            latitude = (double) addressApi.get("lat");
            longitude = (double) addressApi.get("lng");
        }
        /*String address = null;
        double latitude = 0.0;
        double longitude = 0.0;
        if(addressApi.containsKey("address")){
            address = (String) addressApi.get("address");
        }
        if(addressApi.containsKey("lat")){
            latitude = (double) addressApi.get("lat");
        }
        if(addressApi.containsKey("lng")){
            longitude = (double) addressApi.get("lng");
        }*/

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || rePassword.isEmpty())
        {
            Helper.errorMsgDialog(getActivity(), R.string.incomplete);
        }
        // Validation of email
        else if(!email.matches(emailPattern)){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_email);
        }
        // Validation of phone
        else if(!phone.matches(phonePattern)){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_phone);
        }
        else if(!password.equals(rePassword)){
            Helper.errorMsgDialog(getActivity(), R.string.password_no_match);
        }
        else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            userApi = retrofit.create(UserApi.class);
            userSignUp(firstName, lastName, email, password, address, latitude, longitude, phone);
            //userSignUp("Abhi", "Ejam", "ejam@gmail.com", "hey", "Lucerny 3/11, Warsaw, Poland", 52.21476550000000000000, 21.13416250000000000000, "9999999999");
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    // Method to handle user sign up
    private void userSignUp(String firstName, String lastName, String email, String password, String address, double latitude, double longitude, String phone ){
        MultipartBody.Part mProfilePhoto = null;
        // Process only if image is uploaded
        if(imageUri != null){
            File file = new File(getRealPathFromURI(imageUri));
            //Log.d("Path ", getRealPathFromURI(imageUri)+"");
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            mProfilePhoto = MultipartBody.Part.createFormData(
                    "profile_photo", file.getName(), requestFile);
        }

        RequestBody mFirstName = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody mLastName = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody mEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mPassword = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody mPhone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody mAddress = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody mLatitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
        RequestBody mLongitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
        RequestBody mUserType = RequestBody.create(MediaType.parse("text/plain"), "C");

        Call<ResponseBody> call =  userApi.customerSignUp(mFirstName, mLastName, mEmail, mPassword, mPhone, mAddress, mLatitude, mLongitude, mUserType, mProfilePhoto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    //Customer resp = response.body();
                    Log.d("Response: ", ""+response.body());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Helper.errorMsgDialog(getActivity(), R.string.response_error);
                    Log.d("Error: ",""+response.message());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Helper.errorMsgDialog(getActivity(), R.string.network_error);
                Log.d("Fail: ", t.getMessage());
            }
        });

        //Customer customer = new Customer(firstName, lastName, email, password, phone, address, latitude,longitude, "C" );
        /*Call<Customer> call = userApi.customerSignUp(firstName, lastName, email, password, phone, address, latitude,longitude, "C" );

        // Using enqueue to make network call asynchronous
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    Customer resp = response.body();
                    Log.d("Response: ", ""+resp);
                }
                else{
                    Helper.errorMsgDialog(getActivity(), R.string.response_error);
                    Log.d("Error: ",""+response.message());
                }

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Helper.errorMsgDialog(getActivity(), R.string.network_error);
                Log.d("Fail: ", t.getMessage());
            }
        });*/
        /*Customer customer = new Customer(firstName, lastName, email, password, phone, address, latitude,longitude, "C" );
        Call<Customer> call = userApi.customerSignUp(customer);

        // Using enqueue to make network call asynchronous
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    Customer resp = response.body();
                    Log.d("Response: ", ""+resp);
                }
                else{
                    Helper.errorMsgDialog(getActivity(), R.string.response_error);
                    Log.d("Error: ",""+response.message());
                }

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Helper.errorMsgDialog(getActivity(), R.string.network_error);
                Log.d("Fail: ", t.getMessage());
            }
        });*/
    }


}
