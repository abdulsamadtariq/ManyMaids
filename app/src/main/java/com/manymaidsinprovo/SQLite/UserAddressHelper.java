package com.manymaidsinprovo.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.Model.Contact;
import com.manymaidsinprovo.Model.User;

import java.util.ArrayList;

public class UserAddressHelper extends SQLiteOpenHelper {

    private static final int versionCode = 1;

    public UserAddressHelper(Context context) {
        super(context, "many_maids", null, versionCode);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE address(addressId INTEGER PRIMARY KEY AUTOINCREMENT,addressTag TEXT,addressCaption TEXT,buildingNo TEXT,streetAddress TEXT,zipCode TEXT,city TEXT,country TEXT,userId int)";
        db.execSQL(query);

        String query2 = "CREATE TABLE contact(contactId INTEGER PRIMARY KEY AUTOINCREMENT,countryCode TEXT,phone TEXT,userId INTEGER,addressId INTEGER)";
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS address");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }

    public long addAddress(Address address, User user) {

        ContentValues cv = new ContentValues();

        cv.put("addressTag", address.getAddressTag());
        cv.put("addressCaption", address.getAddressCaption());
        cv.put("buildingNo", address.getBuildingNo());
        cv.put("streetAddress", address.getStreetAddress());
        cv.put("zipCode", address.getZipcode());
        cv.put("city", address.getCity());
        cv.put("country", address.getCountry());
        cv.put("userId", user.getUserId());

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("address", null, cv);
    }

    public long addToContact(Contact contact, int addressId, int userId) {

        ContentValues cv = new ContentValues();
        cv.put("countryCode", contact.getCountryCode());
        cv.put("phone", contact.getPhone());
        cv.put("userId", userId);
        cv.put("addressId", addressId);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("contact", null, cv);
    }

    public ArrayList<Address> getAddressDetails(User user) {

        ArrayList<Address> addressList = new ArrayList<>();

        String query = "SELECT * FROM address  WHERE userId=" + user.getUserId() + " ORDER BY addressId DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            int addressId = c.getInt(c.getColumnIndex("addressId"));
            String addressTag = c.getString(c.getColumnIndex("addressTag"));
            String addressCaption = c.getString(c.getColumnIndex("addressCaption"));
            String buildingNo = c.getString(c.getColumnIndex("buildingNo"));
            String zipCode = c.getString(c.getColumnIndex("zipCode"));
            String streetAddress = c.getString(c.getColumnIndex("streetAddress"));
            String city = c.getString(c.getColumnIndex("city"));
            String country = c.getString(c.getColumnIndex("country"));
            int userId = c.getInt(c.getColumnIndex("userId"));


            Address address = new Address(addressId, addressTag, addressCaption, buildingNo, streetAddress, city, zipCode, country, userId);

            addressList.add(address);
        }

        return addressList;
    }



    public Address getAddressDetails(int addressId1,User user) {

        Address address=new Address();

        String query = "SELECT * FROM address  WHERE  userId=" + user.getUserId() + " AND addressId="+addressId1 +" ORDER BY addressId DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            int addressId = c.getInt(c.getColumnIndex("addressId"));
            String addressTag = c.getString(c.getColumnIndex("addressTag"));
            String addressCaption = c.getString(c.getColumnIndex("addressCaption"));
            String buildingNo = c.getString(c.getColumnIndex("buildingNo"));
            String zipCode = c.getString(c.getColumnIndex("zipCode"));
            String streetAddress = c.getString(c.getColumnIndex("streetAddress"));
            String city = c.getString(c.getColumnIndex("city"));
            String country = c.getString(c.getColumnIndex("country"));
            int userId = c.getInt(c.getColumnIndex("userId"));


            address = new Address(addressId, addressTag, addressCaption, buildingNo, streetAddress, city, zipCode, country, userId);

        }

        return address;
    }


    public ArrayList<Contact> getContact(int addressId, User user) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Contact> contactArrayList = new ArrayList<>();

        String query = "SELECT * FROM contact WHERE addressId=" + addressId + "  AND userId=" + user.getUserId();
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            String phone = c.getString(c.getColumnIndex("phone"));
            int contactId = c.getInt(c.getColumnIndex("contactId"));
            String countryCode = c.getString(c.getColumnIndex("countryCode"));
            Contact contact = new Contact(contactId, String.valueOf(phone), countryCode);
            contactArrayList.add(contact);
        }

        return contactArrayList;
    }


    public long updateAddress(Address address, User user) {

        ContentValues cv = new ContentValues();

        cv.put("addressTag", address.getAddressTag());
        cv.put("addressCaption", address.getAddressCaption());
        cv.put("buildingNo", address.getBuildingNo());
        cv.put("streetAddress", address.getStreetAddress());
        cv.put("zipCode", address.getZipcode());
        cv.put("city", address.getCity());
        cv.put("country", address.getCountry());

        SQLiteDatabase db = getWritableDatabase();
        return db.update("address", cv,"addressId=" + address.getAddressId() + " And userId=" + user.getUserId(), null);

    }

    public long updateContact(Contact contact) {
        ContentValues cv = new ContentValues();
        cv.put("countryCode", contact.getCountryCode());
        cv.put("phone", contact.getPhone());

        SQLiteDatabase db = getWritableDatabase();
        return db.update("contact",cv,"contactId="+contact.getContactId() , null);
    }

    /*
        public long deleteAddress(){

        }*/
    public boolean isAddressInAddressTable(String addressCaption, int userId) {

        String query = "SELECT * FROM address where addressCaption='" + addressCaption + "' AND userId=" + userId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        int rows = c.getCount();
        c.close();
        return rows > 0;
    }


}
