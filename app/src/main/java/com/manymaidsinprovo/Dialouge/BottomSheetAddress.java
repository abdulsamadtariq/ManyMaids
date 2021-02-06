package com.manymaidsinprovo.Dialouge;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.manymaidsinprovo.Adapter.CustomTimePickerAdapter;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.Model.Contact;
import com.manymaidsinprovo.Model.CustomTimePicker;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.UserAddressHelper;

import java.util.ArrayList;

public class BottomSheetAddress extends BottomSheetDialogFragment {

    private ImageView ivClose;
    private TextInputLayout tilCaption, tilBuilding, tilStreet, tilZipCode, tilCity, tilCountry, tilPhone1, tilPhone2;
    private TextInputEditText titCaption, titBuilding, titStreet, titZipCode, titCity, titCountry, titPhone1, titPhone2;
    private Button btnSave;
    private Context mContext;
    private UserAddressHelper userAddressHelper;
    private Spinner spCountryCode;
    private RecyclerView rvAddressTag;
    private CustomTimePickerAdapter customAddressTagAdapter;
    private ArrayList<CustomTimePicker> customAddressTagList;
    private String addressTag;
    private Address address;
    private AddressSavedBroadCast mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_address_dialog, container, false);
        mContext = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

    }

    private void init(final View view) {

        ivClose = view.findViewById(R.id.ivCloseDialog);

        rvAddressTag = view.findViewById(R.id.rvAddressTag);
        tilCaption = view.findViewById(R.id.tilCaption);
        tilBuilding = view.findViewById(R.id.tilBuilding);
        tilStreet = view.findViewById(R.id.tilStreetAddress);
        tilZipCode = view.findViewById(R.id.tilZipCode);
        tilCity = view.findViewById(R.id.tilCity);
        tilCountry = view.findViewById(R.id.tilCountry);
        tilPhone1 = view.findViewById(R.id.tilPhone1);
        tilPhone2 = view.findViewById(R.id.tilPhone2);
        spCountryCode = view.findViewById(R.id.spCountryCode);

        titCaption = view.findViewById(R.id.titCaption);
        titBuilding = view.findViewById(R.id.titBuilding);
        titStreet = view.findViewById(R.id.titStreetAddress);
        titZipCode = view.findViewById(R.id.titZipCode);
        titCity = view.findViewById(R.id.titCity);
        titCountry = view.findViewById(R.id.titCountry);
        titPhone1 = view.findViewById(R.id.titPhone1);
        titPhone2 = view.findViewById(R.id.titPhone2);


        userAddressHelper = new UserAddressHelper(mContext);

        btnSave = view.findViewById(R.id.btnSave);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        customAddressTagList = new ArrayList<>();

        CustomTimePicker customTimePicker1 = new CustomTimePicker();
        customTimePicker1.setTime("Residential");
        customAddressTagList.add(customTimePicker1);
        CustomTimePicker customTimePicker2 = new CustomTimePicker();
        customTimePicker2.setTime("Rental/AirBnB");
        customAddressTagList.add(customTimePicker2);
        CustomTimePicker customTimePicker3 = new CustomTimePicker();
        customTimePicker3.setTime("office");
        customAddressTagList.add(customTimePicker3);
        CustomTimePicker customTimePicker4 = new CustomTimePicker();
        customTimePicker4.setTime("Realtor");
        customAddressTagList.add(customTimePicker4);

        customAddressTagAdapter = new CustomTimePickerAdapter(customAddressTagList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                for (int i = 0; i < customAddressTagList.size(); i++) {
                    if (i == position) {
                        customAddressTagList.get(i).setSelected(true);
                    } else {
                        customAddressTagList.get(i).setSelected(false);
                    }
                }
                customAddressTagAdapter.notifyDataSetChanged();

                addressTag = customAddressTagList.get(position).getTime();
            }
        });
        rvAddressTag.setAdapter(customAddressTagAdapter);
        rvAddressTag.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvAddressTag.setNestedScrollingEnabled(false);
        removeError();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addressTag == null) {
                    Toast.makeText(mContext, "please Select a Tag", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkData(addressTag);
                removeError();
            }
        });

        //getting address data

        address = (Address) (getArguments() != null ? getArguments().getSerializable("address") : null);

        if (address != null) {

            titCaption.setText(address.getAddressCaption());
            titBuilding.setText(address.getBuildingNo());
            titStreet.setText(address.getStreetAddress());
            titZipCode.setText(address.getZipcode());
            titCity.setText(address.getCity());
            titCountry.setText(address.getCountry());
            UserAddressHelper userAddressHelper = new UserAddressHelper(mContext);
            ArrayList<Contact> contactArrayList;
            contactArrayList = userAddressHelper.getContact(address.getAddressId(), SessionHelper.getCurrentUser(mContext));

            for (int i=0;i<contactArrayList.size();i++){
                Log.i("mytag","size"+contactArrayList.size()+contactArrayList.get(i).getCountryCode()+" " +contactArrayList.get(i).getPhone());
            }

            if (contactArrayList.size() == 1) {
                titPhone1.setText(contactArrayList.get(0).getPhone());
            } else if (contactArrayList.size() > 1) {
                titPhone1.setText(contactArrayList.get(0).getPhone());
                titPhone2.setText(contactArrayList.get(1).getPhone());
            }
        }


    }

    private void removeError() {

        titCaption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titCaption.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                titCaption.setError(null);
            }
        });
        titBuilding.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titBuilding.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        titStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titStreet.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                titStreet.setError(null);
            }
        });
        titZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titZipCode.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        titCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titCity.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                titCity.setError(null);
            }
        });
        titCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titCountry.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        titPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                titPhone1.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                titPhone1.setError(null);
            }
        });


    }

    private void checkData(String addressTag) {

        String buildingNo = titBuilding.getText().toString().trim();
        String street = titStreet.getText().toString().trim();
        String caption = titCaption.getText().toString().trim();
        String zipcode = titZipCode.getText().toString();
        String city = titCity.getText().toString();
        String country = titCountry.getText().toString();
        String phone1 = titPhone1.getText().toString();
        String phone2 = titPhone2.getText().toString();

        tilCaption.setErrorEnabled(false);
        tilBuilding.setErrorEnabled(false);
        tilStreet.setErrorEnabled(false);
        tilZipCode.setErrorEnabled(false);
        tilCity.setErrorEnabled(false);
        tilCountry.setErrorEnabled(false);
        tilPhone1.setErrorEnabled(false);
        tilPhone2.setErrorEnabled(false);

        if (TextUtils.isEmpty(caption)) {
            tilCaption.setErrorEnabled(true);
            tilCaption.setError("Caption field is required");
            titCaption.requestFocus();
        } else if (caption.length() < 3) {
            tilCaption.setErrorEnabled(true);
            tilCaption.setError("Caption too short detected");
            titCaption.requestFocus();
        } else if (caption.length() > 20) {
            tilCaption.setErrorEnabled(true);
            tilCaption.setError("Caption too long detected");
            titCaption.requestFocus();
        } else if (TextUtils.isEmpty(buildingNo)) {
            tilBuilding.setErrorEnabled(true);
            tilBuilding.setError("Building/house# is required.please enter");
            tilBuilding.requestFocus();
        } else if (buildingNo.length() < 3) {
            tilBuilding.setErrorEnabled(true);
            tilBuilding.setError("building/house# too short detected");
            titBuilding.requestFocus();
        } else if (buildingNo.length() > 30) {
            tilBuilding.setErrorEnabled(true);
            tilBuilding.setError("building/house# too long detected");
            titBuilding.requestFocus();
        } else if (TextUtils.isEmpty(street)) {
            tilStreet.setErrorEnabled(true);
            tilStreet.setError("street is required.please enter");
            tilStreet.requestFocus();
        } else if (street.length() < 3) {
            tilStreet.setErrorEnabled(true);
            tilStreet.setError("street too short detected");
            titStreet.requestFocus();
        } else if (street.length() > 30) {
            tilStreet.setErrorEnabled(true);
            tilStreet.setError("street too long detected");
            titStreet.requestFocus();
        } else if (TextUtils.isEmpty(zipcode)) {
            tilZipCode.setErrorEnabled(true);
            tilZipCode.setError("zipcode field is required");
            titZipCode.requestFocus();
        } else if (zipcode.length() < 3) {
            tilZipCode.setErrorEnabled(true);
            tilZipCode.setError("zipcode too short detected");
            titZipCode.requestFocus();
        } else if (zipcode.length() > 20) {
            tilZipCode.setErrorEnabled(true);
            tilZipCode.setError("zipCode too long detected");
            titZipCode.requestFocus();
        } else if (TextUtils.isEmpty(city)) {
            tilCity.setErrorEnabled(true);
            tilCity.setError("city is required.please enter");
            tilCity.requestFocus();
        } else if (city.length() < 3) {
            tilCity.setErrorEnabled(true);
            tilCity.setError("city too short detected");
            tilCity.requestFocus();
        } else if (city.length() > 20) {
            tilCity.setErrorEnabled(true);
            tilCity.setError("city too long detected");
            titCity.requestFocus();
        } else if (TextUtils.isEmpty(country)) {
            tilCountry.setErrorEnabled(true);
            tilCountry.setError("country field is required");
            titCountry.requestFocus();
        } else if (country.length() < 3) {
            tilCountry.setErrorEnabled(true);
            tilCountry.setError("country too short detected");
            titCountry.requestFocus();
        } else if (country.length() > 20) {
            tilCountry.setErrorEnabled(true);
            tilCountry.setError("country too long detected");
            titCountry.requestFocus();
        } else if (TextUtils.isEmpty(phone1)) {
            tilPhone1.setErrorEnabled(true);
            tilPhone1.setError("phone# is required.please enter");
            tilPhone1.requestFocus();
        } else if (phone1.length() < 10) {
            tilPhone1.setErrorEnabled(true);
            tilPhone1.setError("phone# too short detected.at least 10 numbers.please enter correct");
            titPhone1.requestFocus();
        } else if (phone1.length() > 14) {
            tilPhone1.setErrorEnabled(true);
            tilPhone1.setError("phone# too long detected.maximum should be 14.please enter correct");
            titPhone1.requestFocus();
        } else {

            saveAddress(addressTag, caption, buildingNo, street, zipcode, city, country, phone1, phone2);
        }
    }

    private void saveAddress(String addressTag, String caption, String buildingNo, String street, String zipcode, String city, String country, String phone1, String phone2) {


        User user = SessionHelper.getCurrentUser(mContext);
        boolean isDuplicate = userAddressHelper.isAddressInAddressTable(caption, user.getUserId());

        if (!isDuplicate) {


            Address address = new Address(addressTag, caption, buildingNo, street, city, zipcode, country);

            long insertedAddressId = userAddressHelper.addAddress(address, user);

            if (insertedAddressId > -1) {


                Contact contact = new Contact((int) insertedAddressId, phone1, spCountryCode.getSelectedItem().toString());
                long contactId = userAddressHelper.addToContact(contact, (int)insertedAddressId, user.getUserId());

                if (phone2.length() > 10) {
                    Contact contact2 = new Contact((int) insertedAddressId, phone2, spCountryCode.getSelectedItem().toString());
                    contactId = userAddressHelper.addToContact(contact2, (int)insertedAddressId, user.getUserId());

                }

                if(contactId>-1){
                    Toast.makeText(mContext, "Address saved successfully", Toast.LENGTH_SHORT).show();
                    mListener.IsAddressSaved(true);
                    dismiss();
                }
            }
        } else {
            Toast.makeText(mContext, "Duplicate Caption.please name it different.", Toast.LENGTH_SHORT).show();
        }

    }

    public interface AddressSavedBroadCast {
        void IsAddressSaved(boolean IsSaved);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            mListener = (AddressSavedBroadCast) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " implementation of interface not working");
        }
    }

}
