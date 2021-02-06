package com.manymaidsinprovo.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.Model.CartItem;
import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.Model.Contact;
import com.manymaidsinprovo.Model.Task;

import java.util.ArrayList;

public class CartHelper extends SQLiteOpenHelper {

    private static final int version = 1;

    public CartHelper(Context context) {
        super(context, "many_maids_cart", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE mmCart (cartId INTEGER PRIMARY KEY AUTOINCREMENT,CleaningTypeId INTEGER,NumberOfCleaner INTERGER,CategoryName TEXT,CategoryPricePerCleaning  INTEGER,discount INTEGER,hours TEXT,rating TEXT,date TEXT,time TEXT,addressId INTEGER,addressTag TEXT,addressCaption TEXT,buildingNo TEXT,streetAddress TEXT ,city TEXT ,zipcode TEXT,country TEXT,phone TEXT,howOften TEXT,days TEXT ,startDate TEXT,ENDDATE TEXT,itemBill TEXT,updatedBill TEXT,instruction TEXT,status INTEGER,quantity INTEGER,taskId INTEGER)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS mmCart");
        onCreate(db);

    }


    public long addToCart(CleaningType cleaningType, Address address, String phone,String date, String time, String howOften,ArrayList<String> days, String startDate, String endDate, String itemBill,String instruction,int status,int quantity,int taskId) {


        ContentValues cv = new ContentValues();
        cv.put("CleaningTypeId", cleaningType.getCleaningTypeId());
        cv.put("CategoryName", cleaningType.getCategoryName());
        cv.put("CategoryPricePerCleaning", cleaningType.getCategoryPricePerCleaning());
        cv.put("hours", cleaningType.getHours());
        cv.put("NumberOfCleaner", cleaningType.getNumberOfCleaner());
        cv.put("discount", cleaningType.getDiscount());
        cv.put("rating", cleaningType.getRating());
        cv.put("addressId", address.getAddressId());
        cv.put("addressTag", address.getAddressTag());
        cv.put("addressCaption", address.getAddressCaption());
        cv.put("buildingNo", address.getBuildingNo());
        cv.put("streetAddress", address.getStreetAddress());
        cv.put("zipcode", address.getZipcode());
        cv.put("city", address.getCity());
        cv.put("country", address.getCountry());
        cv.put("phone", phone);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("startDate", startDate);
        cv.put("endDate", endDate);
        cv.put("howOften", howOften);
        if(days!=null && days.size()==2){
            cv.put("days", days.get(0)+","+ days.get(1));
        }
        cv.put("itemBill", itemBill);
        cv.put("updatedBill", itemBill);
        cv.put("instruction", instruction);
        cv.put("status", status);
        cv.put("quantity", quantity);
        cv.put("taskId", taskId);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("mmCart", null, cv);
    }

    public int deleteFromCart(int cartId) {


        SQLiteDatabase db = getWritableDatabase();
        return db.delete("mmCart", "cartId=" + cartId, null);
    }

    public ArrayList<CartItem> getAllCartItems(Context context) {
        String query = "SELECT * FROM mmCart WHERE status=0 Order BY CartId";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        ArrayList<CartItem> cartItemList = new ArrayList<>();
        TaskHelper taskHelper=new TaskHelper(context);
        while (cursor.moveToNext()) {

            int CleaningTypeId = cursor.getInt(cursor.getColumnIndex("CleaningTypeId"));
            String CategoryName = cursor.getString(cursor.getColumnIndex("CategoryName"));
            int NumberOfCleaner = cursor.getInt(cursor.getColumnIndex("NumberOfCleaner"));
            int CategoryPricePerCleaning = cursor.getInt(cursor.getColumnIndex("CategoryPricePerCleaning"));
            int discount = cursor.getInt(cursor.getColumnIndex("discount"));
            String hours = cursor.getString(cursor.getColumnIndex("hours"));
            String rating = cursor.getString(cursor.getColumnIndex("rating"));

            CleaningType cleaningType = new CleaningType(CleaningTypeId, NumberOfCleaner, CategoryName, CategoryPricePerCleaning, discount, hours, Float.parseFloat(rating));


            int addressId = cursor.getInt(cursor.getColumnIndex("addressId"));
            String addressTag = cursor.getString(cursor.getColumnIndex("addressTag"));
            String addressCaption = cursor.getString(cursor.getColumnIndex("addressCaption"));
            String buildingNo = cursor.getString(cursor.getColumnIndex("buildingNo"));
            String streetAddress = cursor.getString(cursor.getColumnIndex("streetAddress"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String zipcode = cursor.getString(cursor.getColumnIndex("zipcode"));
            String country = cursor.getString(cursor.getColumnIndex("country"));

            Contact contact = new Contact();


            String phone = cursor.getString(cursor.getColumnIndex("phone"));

            contact.setPhone(phone);
            ArrayList<Contact> contactArrayList = new ArrayList<>();
            contactArrayList.add(contact);
            Address address = new Address(addressId, addressTag, addressCaption, buildingNo, streetAddress, city, zipcode, country, contactArrayList);

            int cartId = cursor.getInt(cursor.getColumnIndex("cartId"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String howOften = cursor.getString(cursor.getColumnIndex("howOften"));
            String days= cursor.getString(cursor.getColumnIndex("days"));
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            String itemBill=cursor.getString(cursor.getColumnIndex("itemBill"));
            String updatedBill=cursor.getString(cursor.getColumnIndex("updatedBill"));
            String instruction=cursor.getString(cursor.getColumnIndex("instruction"));
            int quantity=cursor.getInt(cursor.getColumnIndex("quantity"));
            int taskId=cursor.getInt(cursor.getColumnIndex("taskId"));
            Task task= taskHelper.getTaskWithId(taskId);
            CartItem cartItem = new CartItem(cartId, cleaningType, date, time, address, howOften,status,days,itemBill,instruction,quantity,updatedBill,task,taskId);

            cartItemList.add(cartItem);
        }
        return cartItemList;
    }

    public int getNumberOfCartItem() {
        String query = "SELECT * FROM mmCart WHERE status=0";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        int numberOfItems = c.getCount();
        c.close();
        return numberOfItems;
    }

    public boolean isProductInCart(int CleaningTypeId, int addressId, String date) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM mmCart WHERE CleaningTypeId=" + CleaningTypeId + " AND addressId=" + addressId + " date='" + date + "'";
        Cursor c = db.rawQuery(query, null);

        int numberOfRows = c.getCount();
        return numberOfRows > 0;
    }

    public double getTotalBill() {

        String query = "SELECT updatedBill From mmCart WHERE status=0";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        double totalBill=0;
        while (c.moveToNext()){
            totalBill=totalBill+Double.parseDouble(c.getString(c.getColumnIndex("updatedBill")));
        }

        c.close();

        return totalBill;

    }

    public int updateItemInCart(int cartId,int quantity,String totalBill) {

        ContentValues cv = new ContentValues();
        cv.put("updatedBill",totalBill);
        cv.put("quantity",quantity);

        SQLiteDatabase db=getWritableDatabase();
       return db.update("mmCart",cv,"cartId="+cartId,null);
    }


    public int clearCart() {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("status",1);
        return db.update("mmCart", cv, "status=0",null);
    }

}
