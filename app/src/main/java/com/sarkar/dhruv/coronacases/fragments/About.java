package com.sarkar.dhruv.coronacases.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sarkar.dhruv.coronacases.MainActivity;
import com.sarkar.dhruv.coronacases.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment {
    public static final String id ="about";


    public About() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.title.setText("About app");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);

        CircleImageView profImage = view.findViewById(R.id.profile_image);
        Glide.with(getContext())
                .load(R.drawable.prof)
                .into(profImage);

        ImageView github,fb,linkedIn;
        github = view.findViewById(R.id.github);
        fb = view.findViewById(R.id.facebook);
        linkedIn = view.findViewById(R.id.linkedin);

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dhruvaism/")));

            }
        });
        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/")));

            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String urlFb = "fb://page/" + "dhrubajit.sarkar.3";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlFb));

                // If a Facebook app is installed, use it. Otherwise, launch
                // a browser
                final PackageManager packageManager = getActivity().getPackageManager();
                List<ResolveInfo> list =
                        packageManager.queryIntentActivities(intent,
                                PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() == 0) {
                    final String urlBrowser = "https://www.facebook.com/" + "dhrubajit.sarkar.3";
                    intent.setData(Uri.parse(urlBrowser));
                }


                startActivity(intent);
            }
            });

        return view;
    }


}
