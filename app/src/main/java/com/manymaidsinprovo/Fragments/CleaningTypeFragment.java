package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Activities.ReviewsActivity;
import com.manymaidsinprovo.Adapter.CategoryAdapter;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CleaningTypeFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private ArrayList<CleaningType> cleaningTypeList;
    private RecyclerView rvCategory;
    private CategoryAdapter categoryAdapter;
    private TextInputLayout tilCleaningTypeName, tilCleaningPrice;
    private TextInputEditText titCleaningTypeName, titCleaningPrice;
    private Spinner spNumberOfCleaner, spHours, spPriority;
    private Button btnAddCategory, btnUpdateCategory;
    private Context mContext;
    private static CleaningType selectedCleanType;
    private ArrayList<Integer> numberOfCleanerList, priorityList;
    private ArrayList<String> hoursList;

    public CleaningTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cleaning_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();

        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        rvCategory = view.findViewById(R.id.rvCategory);
        tilCleaningTypeName = view.findViewById(R.id.tilCleaningTypeName);
        tilCleaningPrice = view.findViewById(R.id.tilCleaningPrice);
        titCleaningTypeName = view.findViewById(R.id.titCleaningTypeName);
        titCleaningPrice = view.findViewById(R.id.titCleaningPrice);
        spNumberOfCleaner = view.findViewById(R.id.spNumberOfCleaner);
        spPriority = view.findViewById(R.id.spPriority);
        spHours = view.findViewById(R.id.spHours);
        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        btnUpdateCategory = view.findViewById(R.id.btnUpdateCategory);

        cleaningTypeList = new ArrayList<>();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                btnAddCategory.setVisibility(View.VISIBLE);
                btnUpdateCategory.setVisibility(View.GONE);
                tilCleaningTypeName.setErrorEnabled(false);
                tilCleaningPrice.setErrorEnabled(false);

                fetchDataFromServer();
            }
        });
        fetchDataFromServer();
        /////////

        numberOfCleanerList = new ArrayList<>();
        hoursList = new ArrayList<>();
        priorityList = new ArrayList<>();

        numberOfCleanerList.add(1);
        numberOfCleanerList.add(2);

        hoursList.add("1 hour");
        hoursList.add("1.5 hours");
        hoursList.add("2 hours");
        hoursList.add("2.5 hours");
        hoursList.add("3 hours");
        hoursList.add("3.5 hours");
        hoursList.add("4 hours");

        priorityList.add(1);
        priorityList.add(2);
        priorityList.add(3);
        priorityList.add(4);
        priorityList.add(5);

        if (numberOfCleanerList.size() > 0) {

            ArrayAdapter<Integer> numberOfCleanerAdapter = new ArrayAdapter<Integer>(mContext.getApplicationContext(), R.layout.spinner_item, numberOfCleanerList);
            ArrayAdapter<String> hoursAdapter = new ArrayAdapter<String>(mContext.getApplicationContext(), R.layout.spinner_item, hoursList);
            ArrayAdapter<Integer> priorityAdapter = new ArrayAdapter<Integer>(mContext.getApplicationContext(), R.layout.spinner_item, priorityList);

            spNumberOfCleaner.setAdapter(numberOfCleanerAdapter);
            spHours.setAdapter(hoursAdapter);
            spPriority.setAdapter(priorityAdapter);
        }

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cleaningTypeName = titCleaningTypeName.getText().toString().trim();
                String cleaningPrice = titCleaningPrice.getText().toString().trim();
                String numberOfCleaner = spNumberOfCleaner.getSelectedItem().toString();
                String hours = spHours.getSelectedItem().toString();
                String priority = spPriority.getSelectedItem().toString();
                tilCleaningTypeName.setErrorEnabled(false);
                tilCleaningPrice.setErrorEnabled(false);

                if (titCleaningTypeName != null && TextUtils.isEmpty(cleaningTypeName)) {
                    tilCleaningTypeName.setErrorEnabled(true);
                    tilCleaningTypeName.setError("please Enter Package Name.");
                } else if (titCleaningTypeName != null && cleaningTypeName.length() < 2) {
                    tilCleaningTypeName.setErrorEnabled(true);
                    tilCleaningTypeName.setError("Package Name Should be greater than 2");
                } else if (titCleaningPrice != null && TextUtils.isEmpty(cleaningPrice)) {
                    tilCleaningPrice.setErrorEnabled(true);
                    tilCleaningPrice.setError("please Enter package Price");
                } else {
                    addCategory(cleaningTypeName, cleaningPrice, numberOfCleaner, hours, priority);
                }
            }
        });

        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cleaningTypeName = titCleaningTypeName.getText().toString().trim();
                String cleaningPrice = titCleaningPrice.getText().toString().trim();
                String numberOfCleaner = spNumberOfCleaner.getSelectedItem().toString();
                String hours = spHours.getSelectedItem().toString();
                String priority = spPriority.getSelectedItem().toString();
                tilCleaningTypeName.setErrorEnabled(false);
                tilCleaningPrice.setErrorEnabled(false);

                if (titCleaningTypeName != null && TextUtils.isEmpty(cleaningTypeName)) {
                    tilCleaningTypeName.setErrorEnabled(true);
                    tilCleaningTypeName.setError("please Enter Package Name.");
                } else if (titCleaningTypeName != null && cleaningTypeName.length() < 5) {
                    tilCleaningTypeName.setErrorEnabled(true);
                    tilCleaningTypeName.setError("Package Name Should be greater than 5");
                } else if (titCleaningPrice != null && TextUtils.isEmpty(cleaningPrice)) {
                    tilCleaningPrice.setErrorEnabled(true);
                    tilCleaningTypeName.setError("please Enter package Price");
                } else {
                    updateCategory(cleaningTypeName, cleaningPrice, numberOfCleaner, hours, priority);
                }
            }
        });

    }

    private void updateCategory(final String cleaningTypeName, final String cleaningPrice, final String numberOfCleaner, final String hours, final String priority) {

        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getCleaningType, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefresh.setRefreshing(false);

                try {
                    JSONObject jResponse = new JSONObject(response);
                    int status = jResponse.getInt("status");
                    String message = jResponse.getString("message");
                    if (status == 0) {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        fetchDataFromServer();
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    swipeRefresh.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("requestCode", "1");
                map.put("NumberOfCleaner", String.valueOf(numberOfCleaner));
                map.put("CategoryName", cleaningTypeName);
                map.put("CategoryPricePerCleaning", cleaningPrice);
                map.put("hours", hours);
                map.put("catPriority", priority);
                map.put("cleaningTypeId", String.valueOf(selectedCleanType.getCleaningTypeId()));
                return map;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);

    }

    private void addCategory(final String cleaningTypeName, final String cleaningPrice, final String numberOfCleaner, final String hours, final String priority) {

        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getCleaningType, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefresh.setRefreshing(false);

                try {
                    JSONObject jResponse = new JSONObject(response);
                    int status = jResponse.getInt("status");
                    String message = jResponse.getString("message");
                    if (status == 0) {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        fetchDataFromServer();
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    swipeRefresh.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("requestCode", "3");
                map.put("NumberOfCleaner", numberOfCleaner);
                map.put("CategoryName", cleaningTypeName);
                map.put("CategoryPricePerCleaning", cleaningPrice);
                map.put("hours", hours);
                map.put("catPriority", priority);
                return map;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);

    }

    private void fetchDataFromServer() {
        swipeRefresh.setRefreshing(true);
        cleaningTypeList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getCleaningType, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefresh.setRefreshing(false);

                try {
                    JSONObject jResponse = new JSONObject(response);
                    int status = jResponse.getInt("status");
                    String message = jResponse.getString("message");
                    if (status == 0) {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray cleaningTypeArray = jResponse.getJSONArray("cleaningType");

                    Gson gson = new Gson();
                    Type cleaningType = new TypeToken<ArrayList<CleaningType>>() {
                    }.getType();

                    cleaningTypeList = gson.fromJson(cleaningTypeArray.toString(), cleaningType);

                    //adding to cleaningType
                    categoryAdapter = new CategoryAdapter(cleaningTypeList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            //on item click

                            for (int i = 0; i < cleaningTypeList.size(); i++) {
                                if (i == position) {
                                    cleaningTypeList.get(i).setSelected(true);
                                    selectedCleanType = cleaningTypeList.get(position);
                                    loadUpdateCategory();

                                } else {
                                    cleaningTypeList.get(i).setSelected(false);
                                }
                            }
                            categoryAdapter.notifyDataSetChanged();

                        }
                    }, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            Intent intent = new Intent(mContext, ReviewsActivity.class);
                            CleaningType cleaningType = cleaningTypeList.get(position);
                            intent.putExtra("selectedCleanType", cleaningType);
                            startActivity(intent);

                        }
                    });

                    rvCategory.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    rvCategory.setAdapter(categoryAdapter);

                } catch (JSONException e) {
                    swipeRefresh.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                swipeRefresh.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("requestCode", "0");
                return map;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);
    }

    private void loadUpdateCategory() {

        titCleaningTypeName.setText(selectedCleanType.getCategoryName());
        titCleaningPrice.setText("" + selectedCleanType.getCategoryPricePerCleaning());
        btnAddCategory.setVisibility(View.GONE);
        btnUpdateCategory.setVisibility(View.VISIBLE);
        for (int i = 0; i < numberOfCleanerList.size(); i++) {

            if (numberOfCleanerList.get(i) == selectedCleanType.getNumberOfCleaner()) {
                spNumberOfCleaner.setSelection(i);
            }
        }
        for (int i = 0; i < hoursList.size(); i++) {

            if (hoursList.get(i).equals(selectedCleanType.getHours())) {
                spHours.setSelection(i);
            }
        }
        for (int i = 0; i < priorityList.size(); i++) {

            if (priorityList.get(i) == selectedCleanType.getCatPriority()) {
                spPriority.setSelection(i);
            }
        }

    }

}
