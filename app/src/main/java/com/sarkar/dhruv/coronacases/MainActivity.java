package com.sarkar.dhruv.coronacases;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.sarkar.dhruv.coronacases.adapters.StateAdapter;
import com.sarkar.dhruv.coronacases.handlers.HttpRequest;
import com.sarkar.dhruv.coronacases.models.StateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    RecyclerView stateRecyclerView;
    ArrayList<StateModel> stateList;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAllCases();
        new HttpHandler().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAllCases() {
        MaterialCardView confirmed,recovered,deaths;
        confirmed = findViewById(R.id.confirmed_case);
        recovered = findViewById(R.id.recovered_case);
        deaths = findViewById(R.id.deaths_case);

        TextView c_no_case,c_no_new_case,c_type_case;
        TextView r_no_case,r_no_new_case,r_type_case;
        TextView d_no_case,d_no_new_case,d_type_case;
        c_no_case = confirmed.findViewById(R.id.no_case);
        c_no_new_case = confirmed.findViewById(R.id.no_new_case);
        c_type_case = confirmed.findViewById(R.id.case_type);

        r_no_case = recovered.findViewById(R.id.no_case);
        r_no_new_case = recovered.findViewById(R.id.no_new_case);
        r_type_case = recovered.findViewById(R.id.case_type);

        d_no_case = deaths.findViewById(R.id.no_case);
        d_no_new_case = deaths.findViewById(R.id.no_new_case);
        d_type_case = deaths.findViewById(R.id.case_type);

        r_no_case.setTextColor(getColor(R.color.recovered));
        r_type_case.setText(getString(R.string.recovered_text));

        d_no_case.setTextColor(getColor(R.color.deaths));
        d_type_case.setText(getString(R.string.deaths_text));

    }

    private void setStateRecyclerView() {
        stateRecyclerView = findViewById(R.id.states_wise_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        StateAdapter stateAdapter = new StateAdapter(this,stateList);
        stateRecyclerView.hasFixedSize();
        stateRecyclerView.setLayoutManager(linearLayoutManager);
        stateRecyclerView.setAdapter(stateAdapter);

    }

    private class HttpHandler extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Http request started", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpRequest httpRequest = HttpRequest.getInstance();
            JSONObject jsonObject = httpRequest.getStateWise();
            try {
                if(jsonObject!=null) {
                    setStateList(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(stateList!=null) {
                setStateRecyclerView();
            }
        }
    }

    private void setStateList(JSONObject jsonObject) throws JSONException {
        stateList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray(STATEWISE);
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            stateList.add(new StateModel(jsonObject1.getString(Active),
                    jsonObject1.getString(Confirmed),
                    jsonObject1.getString(Deaths),
                    jsonObject1.getString(Deltaconfirmed),
                    jsonObject1.getString(Deltadeaths),
                    jsonObject1.getString(Deltarecovered),
                    jsonObject1.getString(Lastupdatedtime),
                    jsonObject1.getString(Recovered),
                    jsonObject1.getString(State),
                    jsonObject1.getString(Statecode),
                    jsonObject1.getString(Statenotes)
                    ));
        }
    }
}
