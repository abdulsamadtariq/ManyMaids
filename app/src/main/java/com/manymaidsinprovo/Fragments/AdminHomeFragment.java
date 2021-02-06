package com.manymaidsinprovo.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Activities.UserBookingActivity;
import com.manymaidsinprovo.Adapter.BookingAdapter;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Booking;
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
public class AdminHomeFragment extends Fragment {


    private SwipeRefreshLayout swipeRefresh;
    private TextView tvUsersCount, tvBookingsCount, tvPackagesCount;
    private ArrayList<CleaningType> cleaningTypeList;
    private ArrayList<Booking> bookingArrayList;
    private Context mContext;

    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        tvUsersCount = view.findViewById(R.id.tvUsersCount);
        tvBookingsCount = view.findViewById(R.id.tvBookingsCount);
        tvPackagesCount = view.findViewById(R.id.tvPackagesCount);

        bookingArrayList = new ArrayList<>();
        cleaningTypeList = new ArrayList<>();


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCleaningTypesFromServer();
                fetchBookings();
                fetchUsers();

            }
        });
        fetchCleaningTypesFromServer();
        fetchBookings();
        fetchUsers();

    }

    private void fetchUsers() {
        swipeRefresh.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getUsers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefresh.setRefreshing(false);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    int numberofUsers = jsonObject.getInt("nuuserOfUsers");
                    if (status == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (numberofUsers == 0) {

                        tvUsersCount.setText("0");
                    } else {
                        tvUsersCount.setText("" + numberofUsers);
                    }
                } catch (JSONException e) {
                    swipeRefresh.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefresh.setRefreshing(false);

                Toast.makeText(mContext, "Network Issue.Try again later", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(SessionHelper.getCurrentUser(mContext).getUserId()));
                return map;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);

    }


    private void fetchBookings() {
        swipeRefresh.setRefreshing(true);
        bookingArrayList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getBooking, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefresh.setRefreshing(false);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    Log.i("mytag", message);
                    if (status == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray ordersArray = jsonObject.getJSONArray("orders");
                    Gson gson = new Gson();
                    Type bookingType = new TypeToken<ArrayList<Booking>>(){}.getType();
                    bookingArrayList = gson.fromJson(ordersArray.toString(), bookingType);

                    int bookings = bookingArrayList.size();
                    if (bookingArrayList.size() == 0) {
                        tvBookingsCount.setText("0");
                    } else {
                        tvBookingsCount.setText(String.valueOf(bookings));
                    }
                    //String json=gson.toJson(bookingArrayList,bookingType);
                } catch (JSONException e) {
                    swipeRefresh.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefresh.setRefreshing(false);

                Toast.makeText(mContext, "Network Issue.Try again later", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(SessionHelper.getCurrentUser(mContext).getUserId()));
                return map;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);

    }


    private void fetchCleaningTypesFromServer() {
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
                    Type cleaningType = new TypeToken<ArrayList<CleaningType>>(){}.getType();

                    cleaningTypeList = gson.fromJson(cleaningTypeArray.toString(), cleaningType);
                    int cleaningPackages = cleaningTypeList.size();
                    if (cleaningPackages == 0) {
                        tvPackagesCount.setText("0");
                    } else {
                        tvPackagesCount.setText("" + cleaningPackages);
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

}
