package com.salvin.mayatest.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.salvin.mayatest.R;
import com.thebluealliance.spectrum.SpectrumDialog;


public class MyMayaFragment extends Fragment {

    ImageView headerCoverImage;
    ImageButton userProfileImage, addImageFromGallery, addSolidBackground;
    RelativeLayout profileLayout;

    TextView userName, userProfile, userEmail, userGender;

    String user_name, user_email, user_profile_url, user_gender, userBannerPath;

    int userColor;

    ImageLoader imageLoader;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mymaya, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageLoader = ImageLoader.getInstance();

        editor = getActivity().getSharedPreferences("localinfo", getActivity().MODE_PRIVATE).edit();
        sharedPreferences = getActivity().getSharedPreferences("localinfo", getActivity().MODE_PRIVATE);

        Bundle extras = getArguments();
        user_name = extras.getString("userName","");
        user_email = extras.getString("userEmail","");
        user_profile_url = extras.getString("userProfilePicture","");
        user_gender = extras.getString("userGender","");

        userColor = sharedPreferences.getInt("userColor", R.color.maya);
        userBannerPath = sharedPreferences.getString("userBannerPath", "");

        headerCoverImage = (ImageView) view.findViewById(R.id.header_cover_image);
        userProfileImage = (ImageButton) view.findViewById(R.id.user_profile_photo);
        addImageFromGallery = (ImageButton) view.findViewById(R.id.add_banner_from_gallery);
        addSolidBackground = (ImageButton) view.findViewById(R.id.add_solid_background_color);

        profileLayout = (RelativeLayout) view.findViewById(R.id.profile_layout);

        userName = (TextView) view.findViewById(R.id.user_profile_name);
        userProfile = (TextView) view.findViewById(R.id.userProfileUrl);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        userGender = (TextView) view.findViewById(R.id.userGender);

        imageLoader.displayImage(user_profile_url, userProfileImage);
        userName.setText(user_name);
        userProfile.setText(user_profile_url);
        userEmail.setText(user_email);
        userGender.setText(user_gender);

        if(!userBannerPath.isEmpty() || !userBannerPath.equals(null))
            imageLoader.displayImage(userBannerPath, headerCoverImage);

        profileLayout.setBackgroundColor(userColor);

        addImageFromGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);

            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);

            }
        });


        addSolidBackground.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new SpectrumDialog.Builder(getContext())
                        .setColors(R.array.demo_colors)
                        .setSelectedColorRes(R.color.md_blue_500)
                        .setDismissOnColorSelected(true)
                        .setOutlineWidth(2)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                if (positiveResult) {
                                    profileLayout.setBackgroundColor(color);
                                    editor.putInt("userColor", color);
                                    editor.commit();

                                   // Toast.makeText(getContext(), "Color selected: #" + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Sorry cannot set background color", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).build().show(getFragmentManager(), "dialog_demo_1");


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
        System.out.println(data.getData().toString());
        Uri selectedImageUri = Uri.parse(data.getDataString());
        String photoPath = "file://" + getRealPathFromURI(selectedImageUri).replaceAll("[* *]","\\ ");
        System.out.println(photoPath);
        editor.putString("userBannerPath", photoPath);
        editor.commit();
            imageLoader.displayImage(photoPath,headerCoverImage);

        }catch (Exception e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
