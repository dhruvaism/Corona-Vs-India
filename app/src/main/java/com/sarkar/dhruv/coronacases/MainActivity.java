package com.sarkar.dhruv.coronacases;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.RichPathAnimator;
import com.sarkar.dhruv.coronacases.adapters.StateAdapter;
import com.sarkar.dhruv.coronacases.fragments.About;
import com.sarkar.dhruv.coronacases.fragments.India;
import com.sarkar.dhruv.coronacases.fragments.World;
import com.sarkar.dhruv.coronacases.handlers.HttpRequest;
import com.sarkar.dhruv.coronacases.models.CountryModel;
import com.sarkar.dhruv.coronacases.models.StateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.sarkar.dhruv.coronacases.constants.Constant.Active;
import static com.sarkar.dhruv.coronacases.constants.Constant.Confirmed;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deaths;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deltaconfirmed;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deltadeaths;
import static com.sarkar.dhruv.coronacases.constants.Constant.Deltarecovered;
import static com.sarkar.dhruv.coronacases.constants.Constant.Lastupdatedtime;
import static com.sarkar.dhruv.coronacases.constants.Constant.Recovered;
import static com.sarkar.dhruv.coronacases.constants.Constant.STATEWISE;
import static com.sarkar.dhruv.coronacases.constants.Constant.State;
import static com.sarkar.dhruv.coronacases.constants.Constant.Statecode;
import static com.sarkar.dhruv.coronacases.constants.Constant.Statenotes;

public class MainActivity extends AppCompatActivity {


    public static Activity activity;
    India indiaFragment;
    World worldFragment;
    About aboutFragment;
    BottomNavigationView bottomNavigationView;
    public static TextView title;
    public static ArrayList<StateModel> stateList;
    public static ArrayList<CountryModel> countryModels;

    public static CoordinatorLayout titleLayout;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        activity = this;
        indiaFragment = new India();
        worldFragment = new World();
        aboutFragment = new About();

        //setting initial fragment to be home fragment
        switchFragment(indiaFragment, India.id);

        //Initializing Bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        //Event listener for bottom navigation view
        bottomNavigationViewListener(bottomNavigationView);

        activity = this;
        title = findViewById(R.id.title);
        titleLayout = findViewById(R.id.title_layout);

    }


    public static String formatter(String str) {
        boolean first = true;
        String ans = "";
        int count = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (first && count == 3) {
                String newstring = str.charAt(i) + "," + ans;
                ans = newstring;
                count = 0;
                first = false;
            } else if (!first && count == 2) {
                String newstring = str.charAt(i) + "," + ans;
                ans = newstring;
                count = 0;
            }else {
                String newstring = str.charAt(i)+ans;
                ans = newstring;
            }
            count++;
        }
        return ans;
    }

    private void switchFragment(Fragment fragment, String id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.host_fragment, fragment);
        fragmentTransaction.commit();

    }


    //Bottom navigation view listener
    private void bottomNavigationViewListener(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.bottom_nav_bar_india:
                        switchFragment(indiaFragment, India.id);
                        break;
                    case R.id.bottom_nav_bar_world:
                        switchFragment(worldFragment, World.id);
                        break;
                    case R.id.bottom_nav_bar_about:
                        switchFragment(aboutFragment, About.id);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if(selectedItemId != R.id.bottom_nav_bar_india){
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_bar_india);
            switchFragment(indiaFragment,India.id);
        }else{
            super.onBackPressed();
        }
    }


}
