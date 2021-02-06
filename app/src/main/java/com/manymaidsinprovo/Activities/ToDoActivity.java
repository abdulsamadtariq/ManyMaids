package com.manymaidsinprovo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manymaidsinprovo.Adapter.ToDoTaskAdapter;
import com.manymaidsinprovo.Helper.SessionHelper;
import com.manymaidsinprovo.Model.Task;
import com.manymaidsinprovo.Model.UserM;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.CartHelper;
import com.manymaidsinprovo.SQLite.TaskHelper;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

import java.util.ArrayList;

public class ToDoActivity extends AppCompatActivity {


    private Spinner spPeriod, spGroupBy, spAssignedTo;
    private RecyclerView rvTodo;
    private TaskHelper taskHelper;
    private ArrayList<UserM> userMArrayList;
    private ToDoTaskAdapter toDoTaskAdapter;
    private AdView adView;
    private ArrayList<Task> taskArrayList;
    private int dueWithInNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        setTitle("To-Do");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spPeriod = findViewById(R.id.spPeriod);
        spGroupBy = findViewById(R.id.spGroupBy);
        spAssignedTo = findViewById(R.id.spAssignedTo);
        rvTodo = findViewById(R.id.rvTodo);
        taskHelper = new TaskHelper(ToDoActivity.this);
        taskArrayList = new ArrayList<>();


        userMArrayList = taskHelper.getUser();
        if (userMArrayList.size() > 0) {

            ArrayAdapter<UserM> userAdapter = new ArrayAdapter<UserM>(getApplicationContext(), R.layout.spinner_item, userMArrayList);

            spAssignedTo.setAdapter(userAdapter);
        }

        loadAdmobAdd();
        fetchToDoItems();


        spPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchToDoItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spGroupBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchToDoItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spAssignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fetchToDoItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadAdmobAdd() {

        adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void fetchToDoItems() {

        taskArrayList.clear();
        String dueWithin = spPeriod.getSelectedItem().toString().trim();
        String groupBy = spGroupBy.getSelectedItem().toString().trim();
        String userName = spAssignedTo.getSelectedItem().toString().trim();

        checkDue(dueWithin);
        taskArrayList = taskHelper.getToDoTask(dueWithInNo, groupBy, userName);

        toDoTaskAdapter = new ToDoTaskAdapter(taskArrayList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //on book maid
                Task task = taskArrayList.get(i);

                int userRole = SessionHelper.getUserRole(ToDoActivity.this);
                boolean isGuest = SessionHelper.getGuest(ToDoActivity.this);

                if (userRole == 0) {
                    Intent intent = new Intent(ToDoActivity.this, UserScheduleActivity.class);
                    intent.putExtra("selectedTask", task);
                    startActivity(intent);
                    finish();
                } else if (userRole == 1) {
                    startActivity(new Intent(ToDoActivity.this, AdminHomeActivity.class));
                    finish();
                } else {
                    if (isGuest) {
                        Intent intent = new Intent(ToDoActivity.this, UserScheduleActivity.class);
                        intent.putExtra("selectedTask", task);
                        startActivity(intent);
                        finish();
                    } else {
                        startActivity(new Intent(ToDoActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        });

        rvTodo.setAdapter(toDoTaskAdapter);
        rvTodo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void checkDue(String dueWithin) {

        switch (dueWithin) {
            case "Today":
                dueWithInNo = 1;
                break;
            case "1 Day":
                dueWithInNo = 2;
            case "2 Days":
                dueWithInNo = 3;
                break;
            case "3 Days":
                dueWithInNo = 4;
            case "4 Days":
                dueWithInNo = 5;
                break;
            case "1 week":
                dueWithInNo = 8;
            case "2 weeks":
                dueWithInNo = 15;
                break;
            case "All":
                dueWithInNo = 0;
                break;

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);

        CartHelper cartHelper=new CartHelper(ToDoActivity.this);
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
            startActivity(new Intent(ToDoActivity.this,CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        CartHelper cartHelper=new CartHelper(ToDoActivity.this);
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


}
