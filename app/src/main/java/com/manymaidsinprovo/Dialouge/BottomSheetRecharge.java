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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Activities.UserProfileActivity;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.PaypalCredentialsID;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.CartItem;
import com.manymaidsinprovo.Model.Promo;
import com.manymaidsinprovo.Model.User;
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

public class BottomSheetRecharge extends BottomSheetDialogFragment {
    private Context mContext;
    private TextView tvRechargeNow;
    private TextInputLayout tilRechargeAmount;
    private TextInputEditText titRechargeAmount;
    private static String walletAmount;

    private int Payment_Request_code = 12;
    private static PayPalConfiguration palConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalCredentialsID.PAYPAL_CLIENT_ID);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recharge_wallet, container, false);
        mContext = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvRechargeNow= view.findViewById(R.id.tvRechargeWallet);
        tilRechargeAmount= view.findViewById(R.id.tilRechargeAmount);
        titRechargeAmount= view.findViewById(R.id.titRechargeAmount);

        Intent intent = new Intent(mContext, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
        mContext.startService(intent);


        tvRechargeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rechargeWalletAmount=titRechargeAmount.getText().toString();
                tilRechargeAmount.setErrorEnabled(false);
                if(rechargeWalletAmount.length()==0){
                   tilRechargeAmount.setErrorEnabled(true);
                   tilRechargeAmount.setError("please Enter amount");
                }else if(rechargeWalletAmount.length()>10){
                    tilRechargeAmount.setErrorEnabled(true);
                    tilRechargeAmount.setError("please Enter lower amount than this");
                }else {
                    walletAmount=rechargeWalletAmount;
                    PayPalPaymentMethod(rechargeWalletAmount);
                }
            }
        });

}



    private void PayPalPaymentMethod(String rechargeWalletAmount) {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(rechargeWalletAmount), "USD", "Many maids", PayPalPayment.PAYMENT_INTENT_SALE);

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

                updateWallet();
                Toast.makeText(mContext, "Recharge SuccessFull", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                //updateWallet();
                dismiss();
                Toast.makeText(mContext, "Error For Recharge.Try again Later", Toast.LENGTH_SHORT).show();
            }
        }

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
                param.put("walletAmount", walletAmount);
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

