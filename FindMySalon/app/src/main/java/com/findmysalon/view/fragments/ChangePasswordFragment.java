package com.findmysalon.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.findmysalon.R;
import com.findmysalon.api.UserApi;
import com.findmysalon.model.CustomerProfile;
import com.findmysalon.utils.Helper;
import com.findmysalon.utils.RetrofitClient;
import com.findmysalon.view.CustomerActivity;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordFragment extends Fragment {
    private EditText mCurrentPassword;
    private EditText mNewPassword;
    private EditText mRePassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        mCurrentPassword = (EditText) v.findViewById(R.id.etx_current_password);
        mNewPassword = (EditText) v.findViewById(R.id.etx_new_password);
        mRePassword = (EditText) v.findViewById(R.id.etx_rePassword);
        CardView mUpdateButton = v.findViewById(R.id.btn_update);
        mUpdateButton.setOnClickListener(v1 -> changePassword());
        return v;
    }

    private void changePassword() {
        String currentPassword = mCurrentPassword.getText().toString();
        String newPassword = mNewPassword.getText().toString();
        String rePassword = mRePassword.getText().toString();

        if(currentPassword.isEmpty() && newPassword.isEmpty() && rePassword.isEmpty()){
            Helper.errorMsgDialog(getActivity(), R.string.incomplete);
        }
        else if(!newPassword.equals(rePassword)){
            Helper.errorMsgDialog(getActivity(), R.string.password_no_match);
        }
        else{
            Retrofit retrofit = RetrofitClient.getInstance(getActivity());
            UserApi userApi = retrofit.create(UserApi.class);
            Call<JsonObject> call =  userApi.changePassword(currentPassword, newPassword);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        Log.d("Response: ", ""+response.body());
                        Intent intent = new Intent(getActivity(), CustomerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Helper.errorMsgDialog(getActivity(), R.string.incorrect_current_password);
                        Log.d("Error: ",""+response.message());

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Helper.errorMsgDialog(getActivity(), R.string.network_error);
                    Log.d("Fail: ", t.getMessage());
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();


    }
}
