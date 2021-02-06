package com.manymaidsinprovo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Adapter.AddressAdapter;
import com.manymaidsinprovo.Adapter.CategoryAdapter;
import com.manymaidsinprovo.Adapter.CustomTimePickerAdapter;
import com.manymaidsinprovo.Dialouge.BottomSheetAddress;
import com.manymaidsinprovo.Dialouge.BottomSheetOrderSummary;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Address;
import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.Model.CustomTimePicker;
import com.manymaidsinprovo.Model.Slide;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.Model.User;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.CartHelper;
import com.manymaidsinprovo.SQLite.UserAddressHelper;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class UserScheduleActivity extends AppCompatActivity  implements BottomSheetAddress.AddressSavedBroadCast {

    private TextView tvSelectedDate;
    private SimpleDateFormat format;
    private RecyclerView rvTime;
    private ArrayList<CustomTimePicker> customTimePickersList;
    private CustomTimePickerAdapter customTimePickerAdapter;
    private TextView tvMaidArriveTime;

    private AddressAdapter addressAdapter;
    private ArrayList<Address> addressArrayList;
    private RecyclerView rvAddress;
    private UserAddressHelper userAddressHelper;
    private TextView tvInfo;
    private ImageView ivAddAddress;
    private LinearLayout layoutGuest;
    private NestedScrollView UserScheduleLayout;
    private Button btnSummary;
    private Button btnLogin;
    private Task selectedTask;

    private String dateOfSchedule,timeOfSchedule;
    private int addressId=-1;
    private String howOften;
    private RadioButton rbOneTime,rbTwoTimesAWeek,rbEveryWeek,rbEveryOtherWeek,rbEveryFourWeek,rbOneYear;
    private LinearLayout layout2xPerWeek;
    private CheckBox chkMon,chkTue,chkWed,chkThu,chkFri,chkSat,chkSun;

    private CleaningType selectedCleanType;

    private LinearLayout layoutNoInternet;
    private RelativeLayout layoutUserHome;
    private RecyclerView rvCategory;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CleaningType> cleaningTypeList;
    private TextView  tvTryAgain;
    private BroadcastReceiver connectivityReceiver;
    private SwipeRefreshLayout swipeRefresh;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);


        layoutNoInternet=findViewById(R.id.layoutNoInternet);
        layoutUserHome=findViewById(R.id.layoutUserHome);
        rvCategory=findViewById(R.id.rvCategory);
        swipeRefresh=findViewById(R.id.swipeRefresh);
        tvTryAgain=findViewById(R.id.tvTryAgain);

        tvSelectedDate=findViewById(R.id.tvSelectedDate);
        tvMaidArriveTime=findViewById(R.id.tvMaidArriveTime);
        rvTime=findViewById(R.id.rvTime);
        rvAddress = findViewById(R.id.rvAddress);
        tvInfo = findViewById(R.id.tvInfo);
        ivAddAddress = findViewById(R.id.ivAddAddress);
        layoutGuest=findViewById(R.id.layoutGuest);
        UserScheduleLayout=findViewById(R.id.svSchedule);
        btnSummary=findViewById(R.id.btnSummary);
        btnLogin=findViewById(R.id.btnLogin);

        rbOneTime=findViewById(R.id.rbOneTime);
        rbTwoTimesAWeek=findViewById(R.id.rbTwoTimesAWeek);
        rbEveryWeek=findViewById(R.id.rbEveryWeek);
        rbEveryOtherWeek=findViewById(R.id.rbEveryOtherWeek);
        rbEveryFourWeek=findViewById(R.id.rbEveryFourWeek);
        rbOneYear=findViewById(R.id.rbOneYear);

        layout2xPerWeek=findViewById(R.id.layout2xPerWeek);

        chkMon=findViewById(R.id.chkMonday);
        chkTue=findViewById(R.id.chkTue);
        chkWed=findViewById(R.id.chkWed);
        chkThu=findViewById(R.id.chkThu);
        chkFri=findViewById(R.id.chkFri);
        chkSat=findViewById(R.id.chkSat);
        chkSun=findViewById(R.id.chkSunday);

        connectivityReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if(networkInfo !=null && networkInfo.isConnected()){
                    if(layoutNoInternet.isShown()){
                        fetchDataFromServer();
                    }
                }else {
                    swipeRefresh.setRefreshing(false);
                    layoutNoInternet.setVisibility(View.VISIBLE);
                    layoutUserHome.setVisibility(View.GONE);
                    Toast.makeText(context, "Internet Disconnected!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityReceiver,filter);



        if(!SessionHelper.isUserLoggedIn(UserScheduleActivity.this)){
            layoutGuest.setVisibility(View.VISIBLE);
            UserScheduleLayout.setVisibility(View.GONE);
            btnSummary.setVisibility(View.GONE);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(UserScheduleActivity.this, LoginActivity.class));
                    finish();
                }
            });

            return;
        }

        selectedTask=(Task) getIntent().getSerializableExtra("selectedTask");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Schedule");

       getSupportActionBar().setSubtitle(selectedTask.getTaskName());


        //Category

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataFromServer();
            }
        });
        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefresh.setRefreshing(true);
            }
        });


        fetchDataFromServer();

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_WEEK_IN_MONTH, 0);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        format=new SimpleDateFormat("EEEE, MMMM d , yyyy", Locale.US);

        Calendar calendar=Calendar.getInstance();
        dateOfSchedule=format.format(calendar.getTime());
        tvSelectedDate.setText(format.format(calendar.getTime()));

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
                dateOfSchedule=format.format(date.getTime());
                tvSelectedDate.setText(format.format(date.getTime()));
            }
        });


        ///time picker
        customTimePickersList=new ArrayList<>();

        CustomTimePicker customTimePicker1=new CustomTimePicker("9:00 Am");
        customTimePickersList.add(customTimePicker1);
        CustomTimePicker customTimePicker2=new CustomTimePicker("10:00 Am");
        customTimePickersList.add(customTimePicker2);
        CustomTimePicker customTimePicker3=new CustomTimePicker("11:00 Am");
        customTimePickersList.add(customTimePicker3);
        CustomTimePicker customTimePicker4=new CustomTimePicker("12:00 pm");
        customTimePickersList.add(customTimePicker4);
        CustomTimePicker customTimePicker5=new CustomTimePicker("1:00 pm");
        customTimePickersList.add(customTimePicker5);
        CustomTimePicker customTimePicker6=new CustomTimePicker("2:00 pm");
        customTimePickersList.add(customTimePicker6);
        CustomTimePicker customTimePicker7=new CustomTimePicker("3:00 pm");
        customTimePickersList.add(customTimePicker7);
        CustomTimePicker customTimePicker8=new CustomTimePicker("4:00 pm");
        customTimePickersList.add(customTimePicker8);
        CustomTimePicker customTimePicker9=new CustomTimePicker("5:00 pm");
        customTimePickersList.add(customTimePicker9);
        CustomTimePicker customTimePicker10=new CustomTimePicker("6:00 pm");
        customTimePickersList.add(customTimePicker10);
        CustomTimePicker customTimePicker11=new CustomTimePicker("7:00 pm");
        customTimePickersList.add(customTimePicker11);
        CustomTimePicker customTimePicker12=new CustomTimePicker("8:00 pm");
        customTimePickersList.add(customTimePicker12);
        CustomTimePicker customTimePicker13=new CustomTimePicker("9:00 pm");
        customTimePickersList.add(customTimePicker13);
        CustomTimePicker customTimePicker14=new CustomTimePicker("10:00 pm");
        customTimePickersList.add(customTimePicker14);


        customTimePickerAdapter=new CustomTimePickerAdapter(customTimePickersList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                for (int i=0;i<customTimePickersList.size();i++){
                    if(i==position){
                        customTimePickersList.get(i).setSelected(true);

                        if(position<customTimePickersList.size()-1){
                          timeOfSchedule=customTimePickersList.get(i).getTime()+" to "+customTimePickersList.get(position+1).getTime() ;
                        }else {
                            timeOfSchedule=customTimePickersList.get(i).getTime() ;
                        }

//                        timeOfSchedule=customTimePickersList.get(i).getTime();
                    }else {
                        customTimePickersList.get(i).setSelected(false);
                    }
                }
                customTimePickerAdapter.notifyDataSetChanged();

                if(position<customTimePickersList.size()-1){
                    tvMaidArriveTime.setText("Your maid will arrive between \n \t\t\t "+customTimePickersList.get(position).getTime()+" and "+customTimePickersList.get(position+1).getTime() );
                }else {
                    tvMaidArriveTime.setText("Your maid will arrive at \n \t\t\t\t\t "+customTimePickersList.get(position).getTime() );

                }
            }
        });


        rvTime.setAdapter(customTimePickerAdapter);
        rvTime.setLayoutManager(new LinearLayoutManager(UserScheduleActivity.this,RecyclerView.HORIZONTAL,false));


        ///for showing address

        ivAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetAddress bottomSheetAddress = new BottomSheetAddress();
                bottomSheetAddress.show(getSupportFragmentManager(), "Address dialog");
            }
        });


        if(!SessionHelper.isUserLoggedIn(UserScheduleActivity.this)){
            return;
        }
        User user = SessionHelper.getCurrentUser(UserScheduleActivity.this);

        userAddressHelper=new UserAddressHelper(UserScheduleActivity.this);
        addressArrayList = userAddressHelper.getAddressDetails(user);
        if (addressArrayList.size() == 0) {
            tvInfo.setVisibility(View.VISIBLE);
        }


        addressAdapter = new AddressAdapter(addressArrayList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                for (int i = 0; i < addressArrayList.size(); i++) {
                    if (i == position) {
                        addressArrayList.get(i).setSelected(true);
                        addressId=addressArrayList.get(i).getAddressId();
                    } else {
                        addressArrayList.get(i).setSelected(false);
                    }
                }
                addressAdapter.notifyDataSetChanged();


            }
        }, new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Address address = addressArrayList.get(i);

                Bundle bundle = new Bundle();
                bundle.putSerializable("address", address);

                BottomSheetAddress bottomSheetAddress = new BottomSheetAddress();
                bottomSheetAddress.setArguments(bundle);
                bottomSheetAddress.show(getSupportFragmentManager(), "address dialog");
                return true;
            }
        });
        rvAddress.setAdapter(addressAdapter);
        rvAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));




        rbTwoTimesAWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    layout2xPerWeek.setVisibility(View.VISIBLE);
                }else{
                    layout2xPerWeek.setVisibility(View.GONE);
                }
            }
        });



        ///Adding the scheduled service to cart
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedCleanType ==null){
                    Toast.makeText(UserScheduleActivity.this, "please select a Cleaning Type", Toast.LENGTH_SHORT).show();
                }else if(dateOfSchedule == null || dateOfSchedule.length()== 0){
                    Toast.makeText(UserScheduleActivity.this, "please select a date", Toast.LENGTH_SHORT).show();
                }else if(timeOfSchedule == null || timeOfSchedule.length()==0){
                    Toast.makeText(UserScheduleActivity.this, "please select a time", Toast.LENGTH_SHORT).show();

                }else if(addressId == -1){
                    Toast.makeText(UserScheduleActivity.this, "please select an address", Toast.LENGTH_SHORT).show();

                }else if(howOften != null && howOften.length()==0){
                    Toast.makeText(UserScheduleActivity.this, "please select howOften", Toast.LENGTH_SHORT).show();

                }else {
                    //add to cart
                    ArrayList<String> days=new ArrayList<>();
                    if(rbOneTime.isChecked()){
                        howOften=rbOneTime.getText().toString();

                    }else if(rbTwoTimesAWeek.isChecked()){
                        howOften=rbTwoTimesAWeek.getText().toString();


                       /*//
                        if(chkMon.isChecked() && chkTue.isChecked() &&!chkWed.isChecked()&&!chkThu.isChecked()&&!chkFri.isChecked()&&!chkSat.isChecked()&&!chkSun.isChecked()){
                         days[0]="Monday";
                         days[1]="Tuesday";
                        }else if(chkMon.isChecked() && chkWed.isChecked() && !chkTue.isChecked()&&!chkThu.isChecked()&&!chkFri.isChecked()&&!chkSat.isChecked()&&!chkSun.isChecked()){
                            days[0]="Monday";
                            days[1]="Wednesday";
                        }
                        else if(chkMon.isChecked() && chkThu.isChecked() && !chkWed.isChecked()&&!chkTue.isChecked()&&!chkFri.isChecked()&&!chkSat.isChecked()&&!chkSun.isChecked()){
                            days[0]="Monday";
                            days[1]="Thursday";
                        }
                        else if(chkMon.isChecked() && chkFri.isChecked() && !chkWed.isChecked()&&!chkThu.isChecked()&&!chkTue.isChecked()&&!chkSat.isChecked()&&!chkSun.isChecked()){
                            days[0]="Monday";
                            days[1]="Friday";
                        }
                        else if(chkMon.isChecked() && chkSat.isChecked() && !chkWed.isChecked()&&!chkThu.isChecked()&&!chkFri.isChecked()&&!chkTue.isChecked()&&!chkSun.isChecked()){
                            days[0]="Monday";
                            days[1]="Saturday";
                        }
                        else if(chkMon.isChecked() && chkSun.isChecked() && !chkWed.isChecked()&&!chkThu.isChecked()&&!chkFri.isChecked()&&!chkSat.isChecked()&&!chkTue.isChecked()){
                            days[0]="Monday";
                            days[1]="Sunday";
                        }
                        //*/

                       if(chkMon.isChecked()){
                           days.add(chkMon.getText().toString());
                       }
                       if(chkTue.isChecked()){
                           days.add(chkTue.getText().toString());
                       }

                       if(chkWed.isChecked()){
                           days.add(chkWed.getText().toString());

                       }
                       if(chkThu.isChecked()){
                           days.add(chkThu.getText().toString());

                       }
                       if(chkFri.isChecked()){
                           days.add(chkFri.getText().toString());

                       }
                       if (chkSat.isChecked()){
                           days.add(chkSat.getText().toString());

                       }
                       if(chkSun.isChecked()){
                           days.add(chkSun.getText().toString());
                       }

                    }else if(rbEveryWeek.isChecked()){
                     howOften=rbEveryWeek.getText().toString();

                    }else if(rbEveryOtherWeek.isChecked()){
                      howOften=rbEveryOtherWeek.getText().toString();

                    }else if(rbEveryFourWeek.isChecked()){
                     howOften=rbEveryFourWeek.getText().toString();

                    }else if(rbOneYear.isChecked()){
                        howOften=rbOneYear.getText().toString();
                    }


                    if(rbTwoTimesAWeek.isChecked()){
                        if(days.size()>2){
                            Toast.makeText(UserScheduleActivity.this, "please select maximum 2 days", Toast.LENGTH_SHORT).show();
                        }else if(days.size()<2){
                            Toast.makeText(UserScheduleActivity.this, "please select 2 days", Toast.LENGTH_SHORT).show();
                        }else {

                            Bundle bundle=new Bundle();

                            bundle.putSerializable("cleaningType",selectedCleanType);
                            bundle.putSerializable("selectedTask",selectedTask);
                            bundle.putString("date",dateOfSchedule);
                            bundle.putString("time",timeOfSchedule);
                            bundle.putInt("addressId",addressId);
                            bundle.putString("howOften",howOften);
                            bundle.putStringArrayList("days",days);

                            BottomSheetOrderSummary bottomSheetOrderSummary=new BottomSheetOrderSummary();
                            bottomSheetOrderSummary.setArguments(bundle);
                            bottomSheetOrderSummary.show(getSupportFragmentManager(),"orderSummary");
                           // Toast.makeText(UserScheduleActivity.this, "ok date:"+dateOfSchedule+ " time:"+timeOfSchedule+" addressId:"+addressId +" howOften"+howOften+days.get(0)+days.get(1), Toast.LENGTH_LONG).show();
                        }
                    }else {

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("cleaningType",selectedCleanType);
                        bundle.putSerializable("selectedTask",selectedTask);
                        bundle.putString("date",dateOfSchedule);
                        bundle.putString("time",timeOfSchedule);
                        bundle.putInt("addressId",addressId);
                        bundle.putString("howOften",howOften);


                        BottomSheetOrderSummary bottomSheetOrderSummary=new BottomSheetOrderSummary();
                        bottomSheetOrderSummary.setArguments(bundle);
                        bottomSheetOrderSummary.show(getSupportFragmentManager(),"orderSummary");

//                        Toast.makeText(UserScheduleActivity.this, "ok date:"+dateOfSchedule+ " time:"+timeOfSchedule+" addressId:"+addressId +" howOften"+howOften, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    @Override
    public void IsAddressSaved(boolean IsSaved) {
        if(IsSaved){
            addressArrayList.clear();
            ArrayList<Address> tempList = userAddressHelper.getAddressDetails(SessionHelper.getCurrentUser(UserScheduleActivity.this));
            addressArrayList.addAll(tempList);
            addressAdapter.notifyDataSetChanged();
            tvInfo.setVisibility(View.GONE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);

        CartHelper cartHelper=new CartHelper(UserScheduleActivity.this);
        int numberOfCartItem=cartHelper.getNumberOfCartItem();

        if(numberOfCartItem>0){
            Drawable cartIcon= VectorDrawableCompat.create(getResources(),R.drawable.ic_shopping_cart_black_24dp,null);
            ActionItemBadge.update(this,menu.findItem(R.id.cart),cartIcon,ActionItemBadge.BadgeStyles.YELLOW,numberOfCartItem);
        }else {
            ActionItemBadge.hide(menu.findItem(R.id.cart));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.cart){
            startActivity(new Intent(UserScheduleActivity.this,CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        CartHelper cartHelper=new CartHelper(UserScheduleActivity.this);
        int numberOfCartItem=cartHelper.getNumberOfCartItem();

        if(numberOfCartItem>0){
            Drawable cartIcon= VectorDrawableCompat.create(getResources(),R.drawable.ic_shopping_cart_black_24dp,null);
            ActionItemBadge.update(this,menu.findItem(R.id.cart),cartIcon,ActionItemBadge.BadgeStyles.YELLOW,numberOfCartItem);
        }else {
            ActionItemBadge.hide(menu.findItem(R.id.cart));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
    }

    private void fetchDataFromServer() {
        swipeRefresh.setRefreshing(true);
        layoutNoInternet.setVisibility(View.GONE);
        layoutUserHome.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(ApiUrl.slides, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefresh.setRefreshing(false);
                layoutNoInternet.setVisibility(View.GONE);
                layoutUserHome.setVisibility(View.VISIBLE);

                try {
                    JSONObject jResponse= new JSONObject(response);
                    JSONArray cleaningTypeArray=jResponse.getJSONArray("cleaningType");

                    Gson gson=new Gson();
                    Type cleaningType=new TypeToken<ArrayList<CleaningType>>(){}.getType();

                    cleaningTypeList=gson.fromJson(cleaningTypeArray.toString(),cleaningType);

                    //adding to cleaningType
                    categoryAdapter=new CategoryAdapter(cleaningTypeList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            //on item click

                            for (int i = 0; i < cleaningTypeList.size(); i++) {
                                if (i == position) {
                                    cleaningTypeList.get(i).setSelected(true);
                                    selectedCleanType=cleaningTypeList.get(position);
                                } else {
                                    cleaningTypeList.get(i).setSelected(false);
                                }
                            }
                            categoryAdapter.notifyDataSetChanged();



                        }
                    }, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            Intent intent=new Intent(UserScheduleActivity.this, ReviewsActivity.class);
                            CleaningType cleaningType=cleaningTypeList.get(position);
                            intent.putExtra("selectedCleanType",cleaningType);
                            startActivity(intent);

                        }
                    });

                    rvCategory.setLayoutManager(new LinearLayoutManager(UserScheduleActivity.this,LinearLayoutManager.HORIZONTAL,false));
                    rvCategory.setAdapter(categoryAdapter);

                } catch (JSONException e) {
                    swipeRefresh.setRefreshing(false);
                    layoutNoInternet.setVisibility(View.GONE);
                    layoutUserHome.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                swipeRefresh.setRefreshing(false);
                layoutNoInternet.setVisibility(View.VISIBLE);
                layoutUserHome.setVisibility(View.GONE);
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }


}
