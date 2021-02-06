package com.manymaidsinprovo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Adapter.BookingAdapter;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Booking;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.CartHelper;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserBookingActivity extends AppCompatActivity {


    private Spinner spFilterBooking;
    private ArrayList<Booking> bookingArrayList;
    private RecyclerView rvBooking;
    private BookingAdapter bookingAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noBookingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking);

        setTitle("Bookings");

        spFilterBooking = findViewById(R.id.spFilter);
        rvBooking = findViewById(R.id.rvBooking);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        noBookingLayout = findViewById(R.id.layoutNoItemInCart);
        bookingArrayList = new ArrayList<>();


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
                        Toast.makeText(UserBookingActivity.this, message, Toast.LENGTH_SHORT).show();
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
                    rvBooking.setLayoutManager(new LinearLayoutManager(UserBookingActivity.this));

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

                Toast.makeText(UserBookingActivity.this, "Network Issue.Try again later", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(SessionHelper.getCurrentUser(UserBookingActivity.this).getUserId()));
                return map;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);

        CartHelper cartHelper = new CartHelper(UserBookingActivity.this);
        int numberOfCartItem = cartHelper.getNumberOfCartItem();

        if (numberOfCartItem > 0) {
            Drawable cartIcon = VectorDrawableCompat.create(getResources(), R.drawable.ic_shopping_cart_black_24dp, null);
            ActionItemBadge.update(this, menu.findItem(R.id.cart), cartIcon, ActionItemBadge.BadgeStyles.YELLOW, numberOfCartItem);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.cart));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart) {
            startActivity(new Intent(UserBookingActivity.this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        CartHelper cartHelper = new CartHelper(UserBookingActivity.this);
        int numberOfCartItem = cartHelper.getNumberOfCartItem();

        if (numberOfCartItem > 0) {
            Drawable cartIcon = VectorDrawableCompat.create(getResources(), R.drawable.ic_shopping_cart_black_24dp, null);
            ActionItemBadge.update(this, menu.findItem(R.id.cart), cartIcon, ActionItemBadge.BadgeStyles.YELLOW, numberOfCartItem);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.cart));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }


}
