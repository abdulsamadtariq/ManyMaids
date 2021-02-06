package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword,tilFirstName,tilLastName;
    private TextInputEditText titEmail, titPassword,titFirstName,titLastName;
    private Button btnRegister;
    private TextView tvLogin, tvCreateGuest;
    private ProgressBar pbRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    private void initViews() {

        tilFirstName= findViewById(R.id.tilFirstName);
        tilLastName= findViewById(R.id.tilLastName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        titFirstName= findViewById(R.id.titFirstName);
        titLastName= findViewById(R.id.titLastName);
        titEmail = findViewById(R.id.titEmail);
        titPassword = findViewById(R.id.titPassword);
        btnRegister= findViewById(R.id.btnRegister);
        tvLogin= findViewById(R.id.tvLogin);
        tvCreateGuest = findViewById(R.id.tvCreateGuest);
        pbRegister=findViewById(R.id.pbRegister);


        removeError();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilEmail.setError(null);
                tilPassword.setError(null);
                checkUserData();
                removeError();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        tvCreateGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionHelper.setGuest(RegisterActivity.this,true);
                startActivity(new Intent(RegisterActivity.this, ToDoActivity.class));
            }
        });


    }

    private void checkUserData() {

        String firstName = titFirstName.getText().toString().trim();
        String lastName= titLastName.getText().toString().trim();
        String email = titEmail.getText().toString().trim();
        String password = titPassword.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            tilFirstName.setErrorEnabled(true);
            tilFirstName.setError("FirstName field is required");
            titFirstName.requestFocus();
        }
        else if (firstName.length() < 3) {
            tilFirstName.setErrorEnabled(true);
            tilFirstName.setError("FirstName too short detected");
            titFirstName.requestFocus();
        }
        else if (firstName.length() > 20) {
            tilFirstName.setErrorEnabled(true);
            tilFirstName.setError("FirstName too long detected");
            titFirstName.requestFocus();
        }else if (TextUtils.isEmpty(lastName)) {
            tilLastName.setErrorEnabled(true);
            tilLastName.setError("LastName field is required");
            titLastName.requestFocus();
        }
        else if (lastName.length() < 3) {
            tilLastName.setErrorEnabled(true);
            tilLastName.setError("LastName too short detected");
            titLastName.requestFocus();
        }
        else if (lastName.length() > 20) {
            tilLastName.setErrorEnabled(true);
            tilLastName.setError("LastName too long detected");
            titLastName.requestFocus();
        }else  if (TextUtils.isEmpty(email)) {
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
            signUp(firstName,lastName,email, password);
        }

    }

    private void signUp(final String firstName, final String lastName, final String email, final String password) {
        pbRegister.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);

        StringRequest request=new StringRequest(Request.Method.POST, ApiUrl.signUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbRegister.setVisibility(View.GONE);
                btnRegister.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonResponse=new JSONObject(response);
                    int status=jsonResponse.getInt("status");
                    String message=jsonResponse.getString("message");
                    if(status==0){
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        JSONObject userObject=jsonResponse.getJSONObject("user");
                        int userRole=jsonResponse.getInt("role");
                        User user=new Gson().fromJson(userObject.toString(),User.class);
                        SessionHelper.setUserRole(RegisterActivity.this,userRole);

                        SessionHelper.setGuest(RegisterActivity.this,false);
                        SessionHelper.createUserSession(RegisterActivity.this,user);
                        startActivity(new Intent(RegisterActivity.this,ToDoActivity.class));
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, R.string.json_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbRegister.setVisibility(View.GONE);
                btnRegister.setVisibility(View.VISIBLE);
                error.printStackTrace();
                Toast.makeText(RegisterActivity.this, R.string.volly_error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> data=new HashMap<>();
                data.put("firstName",firstName);
                data.put("lastName",lastName);
                data.put("email",email);
                data.put("password",password);
                return data;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void removeError() {

        titFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    tilFirstName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                    tilFirstName.setError(null);
            }
        });
        titLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilLastName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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

}
