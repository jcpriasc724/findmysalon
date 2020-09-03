package com.findmysalon.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.findmysalon.view.CustomerActivity;
import com.findmysalon.view.LoginActivity;

import static com.findmysalon.utils.abcConstants.ACCESS_TOKEN;
import static com.findmysalon.utils.abcConstants.REFRESH_TOKEN;
import static com.findmysalon.utils.abcConstants.TOKEN_EXPIRED;

public class LogoutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Logout ","Logout user");
        // Remove all token related information saved during login in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCESS_TOKEN);
        editor.remove(REFRESH_TOKEN);
        editor.remove(TOKEN_EXPIRED);
        editor.commit();

        // Redirect to login screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        // Flags are used to prevent user to go back to previous screen after logout
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
