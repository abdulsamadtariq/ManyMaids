package com.manymaidsinprovo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Adapter.CartAdapter;
import com.manymaidsinprovo.Dialouge.BottomSheetCheckout;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.PaypalCredentialsID;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.Model.CartItem;
import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.Model.Contact;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.CartHelper;
import com.manymaidsinprovo.SQLite.TaskHelper;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private LinearLayout layoutNoItemInCart;
    private RelativeLayout layoutCheckout;
    private Button btnCheckout;
    private TextView tvTotalBill;
    private CartHelper cartHelper;
    private ArrayList<CartItem> cartItemList;
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private TaskHelper taskHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");

        layoutNoItemInCart = findViewById(R.id.layoutNoItemInCart);
        layoutCheckout = findViewById(R.id.layoutCheckout);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvTotalBill = findViewById(R.id.tvTotalBill);
        rvCart = findViewById(R.id.rvCart);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        cartItemList = new ArrayList<>();
        cartHelper = new CartHelper(CartActivity.this);
        taskHelper = new TaskHelper(CartActivity.this);



        cartItemList = cartHelper.getAllCartItems(CartActivity.this);
        if (cartItemList.size() == 0) {
            layoutNoItemInCart.setVisibility(View.VISIBLE);
            layoutCheckout.setVisibility(View.GONE);
            return;
        }
        totalBill();

        fetchCartItems();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCheckout bottomSheetCheckout=new BottomSheetCheckout();
                bottomSheetCheckout.show(getSupportFragmentManager(),"checkout");
            }
        });

    }


    private void fetchCartItems() {

        cartAdapter = new CartAdapter(cartItemList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Remove
                CartItem selectedCartItem = cartItemList.get(position);
                int numberOfRowsDeleted = cartHelper.deleteFromCart(selectedCartItem.getCartId());
                if (numberOfRowsDeleted == 0) {
                    Toast.makeText(CartActivity.this, "Unable to delete", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<CartItem> tempList = cartHelper.getAllCartItems(CartActivity.this);
                    cartItemList.clear();
                    cartItemList.addAll(tempList);
                    cartAdapter.notifyItemRemoved(position);
                    totalBill();
                    if (cartItemList.size() == 0) {
                        layoutNoItemInCart.setVisibility(View.VISIBLE);
                        layoutCheckout.setVisibility(View.GONE);

                    }
                }

            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //decrement
                int oldQuantity = cartItemList.get(position).getQuantity();
                if (oldQuantity > 1) {
                    int newQty = oldQuantity - 1;
                    Double newBill = Double.parseDouble(cartItemList.get(position).getTotalBill()) * newQty;
                    cartHelper.updateItemInCart(cartItemList.get(position).getCartId(), newQty, String.valueOf(newBill));

                    ArrayList<CartItem> tempList = cartHelper.getAllCartItems(CartActivity.this);
                    cartItemList.clear();
                    cartItemList.addAll(tempList);
                    cartAdapter.notifyItemChanged(position);
                    totalBill();

                }


            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //increment

                int oldQuantity = cartItemList.get(position).getQuantity();
                if (oldQuantity < 4) {
                    int newQty = oldQuantity + 1;
                    Double newBill = Double.parseDouble(cartItemList.get(position).getTotalBill()) * newQty;
                    cartHelper.updateItemInCart(cartItemList.get(position).getCartId(), newQty, String.valueOf(newBill));

                    ArrayList<CartItem> tempList = cartHelper.getAllCartItems(CartActivity.this);
                    cartItemList.clear();
                    cartItemList.addAll(tempList);
                    cartAdapter.notifyItemChanged(position);
                    totalBill();
                }
            }
        });

        rvCart.setAdapter(cartAdapter);
        rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));

    }

    private void totalBill() {
        tvTotalBill.setText("$" + cartHelper.getTotalBill());
    }

}
