package com.manymaidsinprovo.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Activities.HomeActivity;
import com.manymaidsinprovo.Activities.UserBookingActivity;
import com.manymaidsinprovo.Adapter.BookingAdapter;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Booking;
import com.manymaidsinprovo.Notifications.ReminderBroadCast;
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
public class AdminOrderFragment extends Fragment {

    private ArrayList<Booking> bookingArrayList;
    private RecyclerView rvBooking;
    private BookingAdapter bookingAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noBookingLayout;
    private Context mContext;
    private FirebaseDatabase mDataBase;
    private DatabaseReference mRef;


    public AdminOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext=getContext();
        rvBooking = view.findViewById(R.id.rvBooking);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        noBookingLayout = view.findViewById(R.id.layoutNoItemInCart);
        bookingArrayList = new ArrayList<>();


        mDataBase=FirebaseDatabase.getInstance();
        mRef=mDataBase.getReference().child("orders");


        ReminderBroadCast.title = "Many Maids";
        ReminderBroadCast.text = "you have new Booking.";
        ReminderBroadCast.landingClass=mContext.getClass();

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                showNotification();
                fetchBookings();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(noBookingLayout.isShown()){
                    fetchBookings();
                }else {
                    fetchBookings();
                }
            }
        });
        fetchBookings();

    }

    private void showNotification() {
        Intent intent = new Intent(mContext, ReminderBroadCast.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        long currentTime = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + 1000, pendingIntent);

    }


    private void fetchBookings() {
        swipeRefreshLayout.setRefreshing(true);
        bookingArrayList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getBooking, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    Log.i("mytag",message);
                    if (status == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray ordersArray=jsonObject.getJSONArray("orders");
                    Gson gson=new Gson();
                    Type bookingType=new  TypeToken<ArrayList<Booking>>(){}.getType();
                    bookingArrayList=gson.fromJson(ordersArray.toString(),bookingType);

                    if(bookingArrayList.size()==0){
                        noBookingLayout.setVisibility(View.VISIBLE);
                    }else {
                        noBookingLayout.setVisibility(View.GONE);
                    }

                    bookingAdapter=new BookingAdapter(bookingArrayList);
                    rvBooking.setAdapter(bookingAdapter);
                    rvBooking.setLayoutManager(new LinearLayoutManager(mContext));

                    String json=gson.toJson(bookingArrayList,bookingType);
                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
