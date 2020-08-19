package com.findmysalon.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.findmysalon.R;
import com.findmysalon.helpers.PlaceApi;
import com.findmysalon.model.Customer;
import com.findmysalon.api.UserApi;
import com.findmysalon.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import static android.os.Environment.getExternalStoragePublicDirectory;

import static android.app.Activity.RESULT_OK;
import static com.findmysalon.utils.abcConstants.BASE_URL;

public class RegisterCustomerFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;
    static final int SELECT_IMAGE = 3;

    private ImageView mImgView;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mRePassword;
    private View v;
    private UserApi userApi;
    private HashMap<String, Object> addressApi;
    private Uri fileUri;
    private File photoFile;
    String pathToFile;
    private Uri imageUri;

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
        addressApi = placeApi.fetchAddressLatLng(v.findViewById(R.id.etx_address), getActivity());

        mFirstName = (EditText) v.findViewById(R.id.etx_first_name);
        mLastName = (EditText) v.findViewById(R.id.etx_last_name);
        mEmail = (EditText) v.findViewById(R.id.etx_email);
        mPhone = (EditText) v.findViewById(R.id.etx_phone);
        mPassword = (EditText) v.findViewById(R.id.etx_password);
        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);
        Button mSubmitButton = (Button) v.findViewById(R.id.btn_submit);
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
        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void takePhoto() {
        // Intent to open camera
        Intent takePictureIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Checking if there any app that can handle the IMAGE_CAPTURE intent to avoid app crash
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                /*Bitmap imageBitmap = BitmapFactory.decodeFile(pathToFile);
                Log.d("Path to file", ""+pathToFile);*/
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImgView.setImageBitmap(imageBitmap);

                imageUri = getImageUri(getActivity().getApplicationContext(), imageBitmap);
            }
            else if(requestCode == REQUEST_GALLERY){
                //Get selected image uri here
                imageUri = data.getData();
                Log.d("URI ", imageUri+"");
                mImgView.setImageURI(imageUri);
            }

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
        String address = (String) addressApi.get("address");
        /*double latitude = (double) addressApi.get("lat");
        double longitude = (double) addressApi.get("lng");*/

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phonePattern = "^[0-9]{10}$";
        // Validation of empty inputs
        /*if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || rePassword.isEmpty())
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
        else{*/
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            userApi = retrofit.create(UserApi.class);
            //userSignUp(firstName, lastName, email, password, address, latitude, longitude, phone);
            userSignUp("Abhi", "Ejam", "ejam@gmail.com", "hey", "Lucerny 3/11, Warsaw, Poland", 52.21476550000000000000, 21.13416250000000000000, "9999999999");
        //}
    }

    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    /*private String getRealPathFromURI(Uri contentURI) {

        String thePath = "no-path-found";
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return  thePath;
    }*/

    public String getRealPathFromURI1(Uri uri) {
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

    /*public static String getFileContent(
            FileInputStream fis,
            String          encoding ) throws IOException
    {
        try( BufferedReader br =
                     new BufferedReader( new InputStreamReader(fis, encoding )))
        {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        }
    }*/

    /*public String getSourceStream(Uri u) throws FileNotFoundException {
        FileInputStream out = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ParcelFileDescriptor parcelFileDescriptor =
                    getActivity().getContentResolver().openFileDescriptor(u, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            out = new FileInputStream(fileDescriptor);
        } else {
            out = (FileInputStream) getActivity().getContentResolver().openInputStream(u);
        }
        try{
            return getFileContent(out, "UTF-8");
        }
        catch (Exception e){
            return "";
        }
    }*/

    // Method to handle user sign up
    private void userSignUp(String firstName, String lastName, String email, String password, String address, double latitude, double longitude, String phone ){
        File file = new File(getRealPathFromURI1(imageUri));
        Log.d("Path ", getRealPathFromURI1(imageUri)+"");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part mProfilePhoto = MultipartBody.Part.createFormData(
                        "profile_photo", file.getName(), requestFile);

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

    /*// Method to get latitude and longitude from address
    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(getActivity());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleAddress = addressList.get(0);
                LatLng latLng = new LatLng(singleAddress.getLatitude(),singleAddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }*/

    /*private Address getAddressFromLatLng(LatLng latLng){
        Geocoder geocoder=new Geocoder(getActivity());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if(addresses!=null){
                Address address = addresses.get(0);
                return address;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }*/
}
