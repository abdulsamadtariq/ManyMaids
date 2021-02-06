package com.manymaidsinprovo.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.manymaidsinprovo.Adapter.AddressAdapter;
import com.manymaidsinprovo.Dialouge.BottomSheetAddress;
import com.manymaidsinprovo.Dialouge.BottomSheetRecharge;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.UserAddressHelper;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements BottomSheetAddress.AddressSavedBroadCast {

    private LinearLayout layoutGuest, layoutMe;
    private Button btnLogin;
    private AddressAdapter addressAdapter;
    private ArrayList<Address> addressArrayList;
    private RecyclerView rvAddress;
    private UserAddressHelper userAddressHelper;
    private TextView tvInfo;
    private ImageView ivAddAddress;

    private TextInputLayout tilFirstName, tilLastName;
    private TextInputEditText titFirstName, titLastName;
    private TextView tvEmail;
    private Button btnUpdateName;
    private CircleImageView civProfile;
    private ImageButton ibPick;
    private TextView tvConfirmUpload;
    private ProgressDialog progressDialog;
    private Button btnLogout;
    private TextView tvWalletAmount, tvRecharge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        layoutGuest = findViewById(R.id.layoutGuest);
        btnLogin = findViewById(R.id.btnLogin);

        layoutMe = findViewById(R.id.layoutMe);

        addressArrayList = new ArrayList<>();
        userAddressHelper = new UserAddressHelper(UserProfileActivity.this);

        //check if guest/or not
        Log.i("mytag", SessionHelper.getGuest(UserProfileActivity.this) + " Login " + SessionHelper.isUserLoggedIn(UserProfileActivity.this));
        if (SessionHelper.getGuest(UserProfileActivity.this)) {
            if (!SessionHelper.isUserLoggedIn(UserProfileActivity.this)) {
                layoutGuest.setVisibility(View.VISIBLE);
                layoutMe.setVisibility(View.GONE);

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            }
            return;
        }
        //ends here

        init();
        ivAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* UserAddressDialog userAddressDialog=new UserAddressDialog();
                userAddressDialog.show(getSupportFragmentManager(),"UserAddressDialog");*/
                BottomSheetAddress bottomSheetAddress = new BottomSheetAddress();
                bottomSheetAddress.show(getSupportFragmentManager(), "Address dialog");
            }
        });


        fetchAddressFromDevice();
        fetchWallet();


        tvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetRecharge bottomSheetRecharge=new BottomSheetRecharge();
                bottomSheetRecharge.show(getSupportFragmentManager(),"recharge");
            }
        });

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
                        tvWalletAmount.setText("$0");
                        PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this)
                                .edit().putString("walletAmount", "0")
                                .apply();
                    } else if (status == 0) {
                        PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this)
                                .edit().putString("walletAmount", "0")
                                .apply();
                        Toast.makeText(UserProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {

                        JSONObject walletObj = jsonObject.getJSONObject("wallet");
                        String walletAmount = walletObj.getString("amount");

                        tvWalletAmount.setText("$" + walletAmount);
                        PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this)
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
                User currentUser = SessionHelper.getCurrentUser(UserProfileActivity.this);
                HashMap<String, String> param = new HashMap<>();
                param.put("requestCode", "1");
                param.put("userId", String.valueOf(currentUser.getUserId()));

                return param;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    private void init(){

        rvAddress = findViewById(R.id.rvAddress);
        tvInfo = findViewById(R.id.tvInfo);
        ivAddAddress = findViewById(R.id.ivAddAddress);

        tilFirstName = findViewById(R.id.tilFirstName);
        tilLastName = findViewById(R.id.tilLastName);
        titFirstName = findViewById(R.id.titFirstName);
        titLastName = findViewById(R.id.titLastName);
        tvEmail = findViewById(R.id.tvEmail);

        tvWalletAmount = findViewById(R.id.tvWalletAmount);
        tvRecharge = findViewById(R.id.tvRechargeWallet);
        btnUpdateName = findViewById(R.id.btnUpadteName);

        civProfile = findViewById(R.id.civProfile);
        ibPick = findViewById(R.id.ibChangeProfile);
        tvConfirmUpload = findViewById(R.id.btnUpload);
        btnLogout = findViewById(R.id.btnLogout);
        progressDialog = new ProgressDialog(UserProfileActivity.this);
        progressDialog.setMessage("Uploading Image.Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        User user = SessionHelper.getCurrentUser(UserProfileActivity.this);
        titFirstName.setText(user.getFirstName());
        titLastName.setText(user.getLastName());
        tvEmail.setText(user.getEmail());

        String profileImage = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this)
                .getString("profileImage", null);
        User currentUser =SessionHelper.getCurrentUser(UserProfileActivity.this);

        if (profileImage != null) {
            try {

                Picasso.get().load(ApiUrl.uploadsFolder + profileImage).placeholder(R.drawable.ic_profile_gray).into(civProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (currentUser.getImageLink() != null && currentUser.getImageLink().length()>0) {
            try {

                Picasso.get().load(ApiUrl.uploadsFolder + currentUser.getImageLink()).placeholder(R.drawable.ic_profile_gray).into(civProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        removeError();
        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserData();
                removeError();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionHelper.logout(UserProfileActivity.this);
                startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        ibPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        String walletAmount = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("walletAmount", null);
        if(walletAmount==null){

            tvWalletAmount.setText("$"+"0");
        }else {
            tvWalletAmount.setText("$"+walletAmount);

        }
    }

    private void updateProfile() {

        Dexter.withActivity(UserProfileActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(UserProfileActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                            builder.setTitle("Permission required!")
                                    .setMessage("Permission is required to update the profile image")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivityForResult(intent, 51);
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void fetchAddressFromDevice() {

        User user = SessionHelper.getCurrentUser(UserProfileActivity.this);
        addressArrayList = userAddressHelper.getAddressDetails(user);


        if (addressArrayList.size() == 0) {
            tvInfo.setVisibility(View.VISIBLE);
        }

        addressAdapter = new AddressAdapter(addressArrayList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Address address = addressArrayList.get(i);

                Bundle bundle = new Bundle();
                bundle.putSerializable("address", address);

                BottomSheetAddress bottomSheetAddress = new BottomSheetAddress();
                bottomSheetAddress.setArguments(bundle);
                bottomSheetAddress.show(getSupportFragmentManager(), "address dialog");

            }
        }, new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        rvAddress.setAdapter(addressAdapter);
        rvAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
    }

    private void checkUserData() {

        String firstName = titFirstName.getText().toString().trim();
        String lastName = titLastName.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            tilFirstName.setErrorEnabled(true);
            tilFirstName.setError("FirstName field is required");
            titFirstName.requestFocus();
        } else if (firstName.length() < 3) {
            tilFirstName.setErrorEnabled(true);
            tilFirstName.setError("FirstName too short detected");
            titFirstName.requestFocus();
        } else if (firstName.length() > 20) {
            tilFirstName.setErrorEnabled(true);
            tilFirstName.setError("FirstName too long detected");
            titFirstName.requestFocus();
        } else if (TextUtils.isEmpty(lastName)) {
            tilLastName.setErrorEnabled(true);
            tilLastName.setError("LastName field is required");
            titLastName.requestFocus();
        } else if (lastName.length() < 3) {
            tilLastName.setErrorEnabled(true);
            tilLastName.setError("LastName too short detected");
            titLastName.requestFocus();
        } else if (lastName.length() > 20) {
            tilLastName.setErrorEnabled(true);
            tilLastName.setError("LastName too long detected");
            titLastName.requestFocus();
        } else {
            //signUp(firstName,lastName,email, password);
        }
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                final Uri uri = result.getUri();
                civProfile.setImageURI(uri);
                tvConfirmUpload.setVisibility(View.VISIBLE);
                tvConfirmUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        uploadProfile(uri);
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception exception = result.getError();
            }
        }

    }

    private void uploadProfile(Uri uri) {
        File imageFile = new File(uri.getPath());

        progressDialog.show();
        User user = SessionHelper.getCurrentUser(UserProfileActivity.this);
        AndroidNetworking.upload(ApiUrl.profileSetup)
                .addMultipartFile("image", imageFile)
                .addMultipartParameter("userId", String.valueOf(user.getUserId()))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                        float progress = (float) bytesUploaded / totalBytes * 100;
                        progressDialog.setProgress((int) progress);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            JSONObject jResponse = new JSONObject(response);
                            int status = jResponse.getInt("status");
                            String message = jResponse.getString("message");
                            if (status == 0) {
                                Toast.makeText(UserProfileActivity.this, "Error while uploading" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                String imageName = jResponse.getString("image");

                                PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this)
                                        .edit().putString("profileImage", imageName)
                                        .apply();
                                Toast.makeText(UserProfileActivity.this, "Image Updated Successfully!", Toast.LENGTH_SHORT).show();
                                tvConfirmUpload.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(UserProfileActivity.this, R.string.json_error, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(UserProfileActivity.this, R.string.volly_error, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void IsAddressSaved(boolean IsSaved) {

        addressArrayList.clear();
        ArrayList<Address> tempList = userAddressHelper.getAddressDetails(SessionHelper.getCurrentUser(UserProfileActivity.this));
        addressArrayList.addAll(tempList);
        addressAdapter.notifyDataSetChanged();
        tvInfo.setVisibility(View.GONE);
    }
}
