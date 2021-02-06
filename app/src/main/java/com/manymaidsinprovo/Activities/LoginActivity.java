package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText titEmail, titPassword;
    private Button btnLogin;
    private TextView tvRegister, tvCreateGuest;
    private ProgressBar pbLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        titEmail = findViewById(R.id.titEmail);
        titPassword = findViewById(R.id.titPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvCreateAccount);
        tvCreateGuest = findViewById(R.id.tvCreateGuest);
        pbLogin=findViewById(R.id.pbLogin);


        removeError();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilEmail.setError(null);
                tilPassword.setError(null);
                checkUserData();
                removeError();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        tvCreateGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionHelper.setGuest(LoginActivity.this,true);
                startActivity(new Intent(LoginActivity.this, ToDoActivity.class));
            }
        });
    }

    private void removeError() {
        titEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email=titEmail.getText().toString().trim();
                if(email.matches("^[a-zA-Z0-9._٪+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                    tilEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email=titEmail.getText().toString().trim();
                if(email.matches("^[a-zA-Z0-9._٪+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                    tilEmail.setError(null);
                }
            }
        });
        titPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkUserData() {
        String email = titEmail.getText().toString().trim();
        String password = titPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Email field is required.");
            titEmail.requestFocus();
        }else if (email.length() < 2) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Email too short detected");
            titEmail.requestFocus();
        }
        else if (email.length() > 30) {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Email too long detected");
            titEmail.requestFocus();
        }else if(!email.matches("^[a-zA-Z0-9._٪+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Invalid Email format");
            titEmail.requestFocus();
        }
        else  if (TextUtils.isEmpty(password)) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Password field is required");
            titPassword.requestFocus();
        }
       else if (password.length() < 5) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("password too short detected");
            titPassword.requestFocus();
        }
       else if (password.length() > 30) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("password too long detected");
            titPassword.requestFocus();
        }
        else {
            signIn(email, password);
        }
    }

    private void signIn(final String email, final String password) {

        pbLogin.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        StringRequest request=new StringRequest(Request.Method.POST, ApiUrl.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbLogin.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                Log.i("mytag",response);

                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    int status=jsonResponse.getInt("status");
                    String message=jsonResponse.getString("message");
                    if(status==0){
                        Toast.makeText(LoginActivity.this, "Login failure"+message, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        JSONObject userObject=jsonResponse.getJSONObject("user");
                        int userRole=jsonResponse.getInt("role");
                        User user=new Gson().fromJson(userObject.toString(),User.class);
                        SessionHelper.setUserRole(LoginActivity.this,userRole);
                        if(userRole==1){
                            SessionHelper.setGuest(LoginActivity.this,false);
                            SessionHelper.createUserSession(LoginActivity.this,user);
                            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                            finish();
                        }else {
                            SessionHelper.setGuest(LoginActivity.this,false);
                            SessionHelper.createUserSession(LoginActivity.this,user);
                            startActivity(new Intent(LoginActivity.this,ToDoActivity.class));
                            finish();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, R.string.json_error, Toast.LENGTH_SHORT).show();
                    pbLogin.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbLogin.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);

                error.printStackTrace();
                Toast.makeText(LoginActivity.this, R.string.volly_error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);

    }
}
