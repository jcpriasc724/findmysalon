package com.findmysalon.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.findmysalon.R;
import com.findmysalon.model.BusinessProfile;
import com.findmysalon.model.Service;

public class BusinessDetailFragment extends Fragment {

    private CardView btnBook;
    private BusinessProfile business;
    private TextView txtNameBusiness, txtAddress, txtPhoneNumber;
    private RatingBar rtbBusiness;
    private ImageView imgAvatar;;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_detail, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        // Get bundle arguments sent from BusinessAdapter
        business = (BusinessProfile) getArguments().getSerializable("business");

        txtNameBusiness = (TextView) view.findViewById(R.id.txt_name_business);
        txtNameBusiness.setText(business.getBusinessName());

        txtAddress = (TextView) view.findViewById(R.id.txt_address);
        txtAddress.setText(business.getAddress());

        txtPhoneNumber = (TextView) view.findViewById(R.id.txt_phone_number);
        txtPhoneNumber.setText(business.getPhone());

        rtbBusiness = (RatingBar) view.findViewById(R.id.rtb_business);
        rtbBusiness.setRating(business.getRating());

        imgAvatar = (ImageView) view.findViewById(R.id.img_profile_photo);
        // Plugin to display image
        if(business.getProfilePhoto() != null) {
            Glide.with(imgAvatar.getContext())
                    .load(business.getProfilePhoto())
                    .circleCrop()
                    .placeholder(R.drawable.photos_default)
                    .into(imgAvatar);
        }else {
            Glide.with(imgAvatar.getContext()).clear(imgAvatar);
        }

        btnBook = view.findViewById(R.id.btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Working there"," BOOK "+business.getBusinessId());
                Bundle bundle = new Bundle();
                bundle.putInt("id", business.getBusinessId()); // !!!!! please business id here !!!!!
                Navigation.findNavController(v).navigate(R.id.nav_list_services_by_category, bundle);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }




}
