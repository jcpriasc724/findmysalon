package com.findmysalon.view.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.helpers.PlaceApi;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.BusinessActivity;
import com.findmysalon.view.CustomerActivity;
import com.findmysalon.view.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

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

public class UpdateBusinessFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;

    private EditText mBusinessName;
    private EditText mBusinessType;
    private RadioGroup mRadioGroup;
    private ImageView mImgView;
    private RadioButton mRadioButton;
    private AutoCompleteTextView mAddress;
    private EditText mEmail;
    private EditText mPhone;
    private HashMap<String, Object> addressApi;
    private String currentAddress = "";
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private UserApi userApi;
    private Uri imageUri;
    private View v;
    BottomSheetDialog dialogSuccess;

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
        v = inflater.inflate(R.layout.fragment_update_business, container, false);

        mImgView = (ImageView) v.findViewById(R.id.img_profile_photo);
        mImgView.setOnClickListener(v12 -> {
            selectImage();
        });

        // Address autocomplete suggestions
        PlaceApi placeApi = new PlaceApi();
        addressApi = placeApi.fetchAddressLatLng(v.findViewById(R.id.etx_address), getActivity());

        mBusinessName = (EditText) v.findViewById(R.id.etx_business_name);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.rdg_type_business);

        mEmail = (EditText) v.findViewById(R.id.etx_email);
        mPhone = (EditText) v.findViewById(R.id.etx_phone);
        mAddress = (AutoCompleteTextView) v.findViewById(R.id.etx_address);
        CardView mUpdateButton = v.findViewById(R.id.btn_update);
        mUpdateButton.setOnClickListener(v1 -> businessUpdate());

        // retrofit
        Retrofit retrofit = RetrofitClient.getInstance(getActivity());
        userApi = retrofit.create(UserApi.class);
        Call<BusinessProfile> call = userApi.getBusinessDetails();
        call.enqueue(new Callback<BusinessProfile>() {
            @Override
            public void onResponse(Call<BusinessProfile> call, Response<BusinessProfile> response) {
                if(response.isSuccessful()){
                    // Setting profile photo in edit form
                    Uri profilePhoto = Uri.parse(response.body().getProfilePhoto());
                    // Plugin to display profile photo
                    Glide.with(getActivity())
                            .load(profilePhoto)
                            .circleCrop()
                            .placeholder(R.drawable.add_photo)
                            .into(mImgView);
                    mBusinessName.setText(response.body().getBusinessName());
                    //mBusinessType.setText(response.body().getBusinessType());
                    if(response.body().getBusinessType().equals("S")){
                        mRadioGroup.check(R.id.rb_hair_salon);
                    }
                    else{
                        mRadioGroup.check(R.id.rb_barbershop);
                    }

                    mEmail.setText(response.body().getEmail());
                    mAddress.setText(response.body().getAddress());
                    mPhone.setText(response.body().getPhone());
                    currentAddress = response.body().getAddress();
                    currentLatitude = response.body().getLatitude();
                    currentLongitude = response.body().getLongitude();
                }
            }

            @Override
            public void onFailure(Call<BusinessProfile> call, Throwable t) {
                Log.d("Fail: ", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private void selectImage() {
        final String[] options = getResources().getStringArray(R.array.photo_options);;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("UPDATE PROFILE PHOTO");
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
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void takePhoto() {
        // Intent to open camera
        Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Checking if there any app that can handle the IMAGE_CAPTURE intent to avoid app crash
        if (intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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
                imageUri = getImageUri(getActivity().getApplicationContext(), imageBitmap);
                mImgView.setImageBitmap(imageBitmap);
            }
            else if(requestCode == REQUEST_GALLERY){
                //Get selected image uri here
                imageUri = data.getData();
                mImgView.setImageURI(imageUri);
            }
            // Plugin to display image
            Glide.with(getContext())
                    .load(imageUri)
                    .circleCrop()
                    .placeholder(R.drawable.add_photo)
                    .into(mImgView);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
                Navigation.findNavController(getActivity(), R.id.nav_business_host_fragment).navigate(R.id.nav_list_bookings);
            }
        });

        dialogSuccess.setContentView(viewBottomSheet);
        dialogSuccess.show();

    }

    // Method to handle update for business
    public void businessUpdate(){
        String businessName = mBusinessName.getText().toString();
        // find the radiobutton by returned id
        mRadioButton = (RadioButton) v.findViewById(mRadioGroup.getCheckedRadioButtonId());
        String bType = mRadioButton.getText().toString();
        Log.d("Type: ", ""+bType);
        String businessType;
        if(bType.equals(getString(R.string.lbl_salons))){
            businessType = "S";
        }
        else{
            businessType = "B";
        }
        String email = mEmail.getText().toString().trim();
        String phone = mPhone.getText().toString();
        String address = "";
        double latitude = currentLatitude;
        double longitude = currentLongitude;

        // If user fill new address in the form, then use it
        if(!addressApi.isEmpty()){
            address = (String) addressApi.get("address");
            latitude = (double) addressApi.get("lat");
            longitude = (double) addressApi.get("lng");
        }
        // Else use the old address
        else if(currentAddress.equals(mAddress.getText().toString())){
            address = mAddress.getText().toString();
        }

        // Validation of empty inputs
        if (businessName.isEmpty() || email.isEmpty() || phone.isEmpty())
        {
            Helper.errorMsgDialog(getActivity(), R.string.incomplete);
        }
        else if(address.isEmpty()){
            Helper.errorMsgDialog(getActivity(), R.string.address_incomplete);
        }
        // Validation of email


        else if(!email.matches(getContext().getResources().getString(R.string.email_pattern))){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_email);
        }
        // Validation of address
        /*else if(!addressApi.containsKey("address") || !addressApi.containsKey("lat") || !addressApi.containsKey("lng")){
            Helper.errorMsgDialog(Objects.requireNonNull(getActivity()), R.string.address_incomplete);
        }*/
        // Validation of phone
        else if(!phone.matches(getContext().getResources().getString(R.string.phone_pattern))){
            Helper.errorMsgDialog(getActivity(), R.string.invalid_phone);
        }
        else{
            Retrofit retrofit = RetrofitClient.getInstance(getActivity());
            userApi = retrofit.create(UserApi.class);

            MultipartBody.Part mProfilePhoto = null;
            // Process only if image is uploaded
            if(imageUri != null){
                File file = new File(getRealPathFromURI(imageUri));
                //Log.d("Path ", getRealPathFromURI(imageUri)+"");
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                mProfilePhoto = MultipartBody.Part.createFormData(
                        "profile_photo", file.getName(), requestFile);
            }

            RequestBody mBusinessName = RequestBody.create(MediaType.parse("text/plain"), businessName);
            RequestBody mBusinessType = RequestBody.create(MediaType.parse("text/plain"), businessType);
            RequestBody mEmail = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody mPhone = RequestBody.create(MediaType.parse("text/plain"), phone);
            RequestBody mAddress = RequestBody.create(MediaType.parse("text/plain"), address);
            RequestBody mLatitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
            RequestBody mLongitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
            RequestBody mUserType = RequestBody.create(MediaType.parse("text/plain"), "B");

            Call<ResponseBody> call =  userApi.businessUpdate(mBusinessName, mBusinessType, mEmail, mPhone, mAddress, mLatitude, mLongitude, mUserType, mProfilePhoto);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        //Customer resp = response.body();

                        showPopUp(v);

                        //Log.d("Response: ", ""+response.body());

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

            /*BusinessProfile businessProfile = new BusinessProfile(businessName, businessType, email, address, latitude, longitude, phone,"B");
            Call<BusinessProfile> call =  userApi.businessUpdate(businessProfile);
            call.enqueue(new Callback<BusinessProfile>() {
                @Override
                public void onResponse(Call<BusinessProfile> call, Response<BusinessProfile> response) {
                    if(response.isSuccessful()){
                        Log.d("Response: ", ""+response.body());
                        Intent intent = new Intent(getActivity(), BusinessActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Helper.errorMsgDialog(getActivity(), R.string.response_error);
                        Log.d("Error: ",""+response.message());

                    }
                }

                @Override
                public void onFailure(Call<BusinessProfile> call, Throwable t) {
                    Helper.errorMsgDialog(getActivity(), R.string.network_error);
                    Log.d("Fail: ", t.getMessage());
                }
            });*/

        }

    }

}
