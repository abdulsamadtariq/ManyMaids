package com.manymaidsinprovo.Dialouge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Activities.CartActivity;
import com.manymaidsinprovo.Activities.UserProfileActivity;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.PaypalCredentialsID;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.CartItem;
import com.manymaidsinprovo.Model.Promo;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.Model.UserM;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.CartHelper;
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

public class BottomSheetCheckout extends BottomSheetDialogFragment {
    private Context mContext;
    private TextView tvBill, tvTrustFee, tvTotalBill, tvPayNow;
    private Spinner spPromo;
    private ArrayList<Promo> promoArrayList;

    private CartHelper cartHelper;
    private ArrayList<CartItem> cartItemList;
    private static double totalBill;
    private static double updatedTotalBill;
    private static int promoPercentage;
    private int trustFee = 5;
    private RadioButton rbWallet,rbPaypal;
    private static double walletAmountAvailable;
    private static  String paymentMethod;


    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    private int Payment_Request_code = 12;
    private static PayPalConfiguration palConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalCredentialsID.PAYPAL_CLIENT_ID);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottomsheet_checkout, container, false);
        mContext = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBill = view.findViewById(R.id.tvBill);
        tvTrustFee = view.findViewById(R.id.tvTrustFee);
        tvTotalBill = view.findViewById(R.id.tvTotalBill);
        tvPayNow = view.findViewById(R.id.tvPayNow);
        spPromo = view.findViewById(R.id.spPromo);
        rbWallet=view.findViewById(R.id.rbWallet);
        rbPaypal=view.findViewById(R.id.rbPaypal);


        Intent intent = new Intent(mContext, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
        mContext.startService(intent);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("orders");

        cartHelper = new CartHelper(mContext);
        tvBill.setText("$" + cartHelper.getTotalBill());
        cartItemList = new ArrayList<>();
        cartItemList = cartHelper.getAllCartItems(mContext);
        promoArrayList = new ArrayList<>();
        totalBill = cartHelper.getTotalBill();

        tvTrustFee.setText("+$" + trustFee);

        fetchPromoCode();
        totalBillCount();

        fetchWallet();
        tvPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rbWallet.isChecked()){
                    paymentMethod="Wallet";
                    double remaining=walletAmountAvailable-updatedTotalBill;
                    if(remaining<0){
                        Toast.makeText(mContext, "Please Recharge wallet Not enough Balance", Toast.LENGTH_SHORT).show();
                    }else {
                        updateWallet();
                        submitBookingDetails();
                    }

                }else if(rbPaypal.isChecked()){
                    paymentMethod="Paypal";
                    PayPalPaymentMethod();
                }else {
                    Toast.makeText(mContext, "please select payment method", Toast.LENGTH_SHORT).show();
                }

            }
        });


        spPromo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (promoArrayList.size() > 0) {
                    promoPercentage = promoArrayList.get(position).getPercentage();
                }
                totalBillCount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void totalBillCount() {
        updatedTotalBill = (totalBill - (totalBill * promoPercentage / 100)) + trustFee;
        tvTotalBill.setText("$" + updatedTotalBill);
    }

    private void fetchPromoCode() {

        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.promoCode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");


                    if (status == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray promoArray = jsonObject.getJSONArray("codes");

                        Log.i("mytag", promoArray.toString());
                        Gson gson = new Gson();
                        Type promoType = new TypeToken<ArrayList<Promo>>() {
                        }.getType();
                        promoArrayList = gson.fromJson(promoArray.toString(), promoType);

                        if (promoArrayList.size() > 0) {

                            ArrayAdapter<Promo> promoAdapter = new ArrayAdapter<Promo>(mContext.getApplicationContext(), R.layout.spinner_item, promoArrayList);

                            spPromo.setAdapter(promoAdapter);
                        }

                    }
                } catch (JSONException e) {
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
                map.put("requestCode", "2");
                return map;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);
    }


    private void PayPalPaymentMethod() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(updatedTotalBill), "USD", "Many maids", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(mContext, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, Payment_Request_code);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Payment_Request_code) {
            if (resultCode == Activity.RESULT_OK) {

                submitBookingDetails();
                Toast.makeText(mContext, "Payment SuccessFull", Toast.LENGTH_SHORT).show();
            } else {
               // submitBookingDetails();
                Toast.makeText(mContext, "Error For payment.Try again Later", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void submitBookingDetails() {

        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.saveOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = jsonResponse.getInt("status");
                    String message = jsonResponse.getString("message");
                    String orderId = jsonResponse.getString("orderId");

                    if (status == 0) {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                        int updatedCart = cartHelper.clearCart();
                        if (updatedCart > -1) {
                            ArrayList<CartItem> tempList = cartHelper.getAllCartItems(mContext);
                            cartItemList.clear();
                            cartItemList.addAll(tempList);
                                   /* cartAdapter.notifyDataSetChanged();
                                    totalBill();
                                   */
                            if (cartItemList.size() == 0) {
                                    /*    layoutNoItemInCart.setVisibility(View.VISIBLE);
                                        layoutCheckout.setVisibility(View.GONE);
                                    */
                            }
                        }


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("orderId", orderId);
                        mRef.push().updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("mytag", "id added");
                                dismiss();
                                getActivity().finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Log.i("mytag", "exception id added");
                                dismiss();
                                getActivity().finish();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext, "Network error occurred!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                long currentTimeMillis=System.currentTimeMillis();

                params.put("userId", String.valueOf(SessionHelper.getCurrentUser(mContext).getUserId()));
                params.put("totalBill", String.valueOf(updatedTotalBill));
                params.put("paymentStatus", String.valueOf(1));
                params.put("paymentMethod", paymentMethod);
                params.put("orderTime", String.valueOf(currentTimeMillis));

                Gson gson = new Gson();
                Type cartItemType = new TypeToken<ArrayList<CartItem>>() {
                }.getType();
                String json = gson.toJson(cartItemList, cartItemType);

                Log.i("mytag",json);

                params.put("cartArray", json);

                return params;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);

    }

    private void fetchWallet() {

        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getWallet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message").trim();

                    if (status == 0 && message.equals("Wallet not Found")) {
                       walletAmountAvailable=0;
                        PreferenceManager.getDefaultSharedPreferences(mContext)
                                .edit().putString("walletAmount", "0")
                                .apply();
                    } else if (status == 0) {
                        walletAmountAvailable=0;
                        PreferenceManager.getDefaultSharedPreferences(mContext)
                                .edit().putString("walletAmount", "0")
                                .apply();
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    } else {

                        JSONObject walletObj = jsonObject.getJSONObject("wallet");
                        String walletAmount = walletObj.getString("amount");

                        walletAmountAvailable=Double.parseDouble(walletAmount);
                        PreferenceManager.getDefaultSharedPreferences(mContext)
                                .edit().putString("walletAmount", walletAmount)
                                .apply();
                    }
                } catch (JSONException e) {
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
                User currentUser = SessionHelper.getCurrentUser(mContext);
                HashMap<String, String> param = new HashMap<>();
                param.put("requestCode", "1");
                param.put("userId", String.valueOf(currentUser.getUserId()));

                return param;
            }
        };

        Volley.newRequestQueue(mContext).add(request);

    }

    private void updateWallet() {

        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.getWallet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message").trim();

                    if(status==0){
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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
                User currentUser = SessionHelper.getCurrentUser(mContext);
                HashMap<String, String> param = new HashMap<>();
                param.put("requestCode", "0");
                param.put("updatedTotalBill", String.valueOf(updatedTotalBill));
                param.put("userId", String.valueOf(currentUser.getUserId()));

                return param;
            }
        };

        Volley.newRequestQueue(mContext.getApplicationContext()).add(request);
    }



    @Override
    public void onDestroy() {
        mContext.stopService(new Intent(mContext, PayPalService.class));

        super.onDestroy();

    }
}

