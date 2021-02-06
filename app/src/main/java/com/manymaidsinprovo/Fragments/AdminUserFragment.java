package com.manymaidsinprovo.Fragments;


import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Adapter.AdminUserAdapter;
import com.manymaidsinprovo.Adapter.BookingAdapter;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Booking;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdminUserFragment extends Fragment {


    private ArrayList<User> userArrayList;
    private RecyclerView rvUser;
    private AdminUserAdapter adminUserAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private Context mContext;


    public AdminUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext=getContext();

        rvUser=view.findViewById(R.id.rvUser);
        userArrayList=new ArrayList<>();
        swipeRefresh=view.findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchUsers();
            }
        });
        fetchUsers();
    }

    private void fetchUsers() {
        swipeRefresh.setRefreshing(true);
        userArrayList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getUsers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefresh.setRefreshing(false);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    Log.i("mytag",message);
                    if (status == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray usersArray=jsonObject.getJSONArray("users");
                    Gson gson=new Gson();
                    Type userType=new  TypeToken<ArrayList<User>>(){}.getType();
                    userArrayList=gson.fromJson(usersArray.toString(),userType);

                    if(userArrayList.size()==0){

                        Toast.makeText(mContext, "No user registered", Toast.LENGTH_SHORT).show();
                    }

                    adminUserAdapter=new AdminUserAdapter(userArrayList);
                    rvUser.setAdapter(adminUserAdapter);
                    rvUser.setLayoutManager(new LinearLayoutManager(mContext));

//                    String json=gson.toJson(userArrayList,bookingType);
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


}
