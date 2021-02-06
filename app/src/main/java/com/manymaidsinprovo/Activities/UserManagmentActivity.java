package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.manymaidsinprovo.Adapter.UserMAdapter;
import com.manymaidsinprovo.Model.UserM;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.util.ArrayList;

public class UserManagmentActivity extends AppCompatActivity {

    private TextInputEditText titUser;
    private TextInputLayout tilUser;
    private Button btnAddUser;
    private RecyclerView rvUser;
    private UserMAdapter userMAdapter;
    private ArrayList<UserM> userMArrayList;
    private TaskHelper taskHelper;
    private LinearLayout layoutUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managment);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Users");
        tilUser = findViewById(R.id.tilUser);
        titUser = findViewById(R.id.titUser);
        btnAddUser = findViewById(R.id.btnAddUser);
        layoutUserInfo = findViewById(R.id.layoutUserInfo);
        rvUser = findViewById(R.id.rvUser);

        userMArrayList = new ArrayList<>();
        taskHelper = new TaskHelper(UserManagmentActivity.this);
        fetchUser();


        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilUser.setErrorEnabled(false);
                String userName = titUser.getText().toString().trim();
                boolean isAlready = taskHelper.isUserAlready(userName);
                if (TextUtils.isEmpty(userName)) {
                    tilUser.setErrorEnabled(true);
                    tilUser.setError("please Enter some name");
                    return;
                } else if (userName.length() > 30) {
                    tilUser.setErrorEnabled(true);
                    tilUser.setError("please enter short name.very long given");
                    return;
                } else if (isAlready) {
                    tilUser.setErrorEnabled(true);
                    tilUser.setError("This user already present");

                    return;
                }
                long insert = taskHelper.addToUserM(userName, 1);

                if (insert > -1) {
                    fetchUser();
                }
            }
        });

    }

    private void fetchUser() {
        userMArrayList.clear();
        userMArrayList = taskHelper.getUser();

        userMArrayList.remove(0);
        if (userMArrayList.size() == 0) {
            layoutUserInfo.setVisibility(View.VISIBLE);
            return;
        }
        layoutUserInfo.setVisibility(View.GONE);

        userMAdapter = new UserMAdapter(userMArrayList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //on Item

                UserM userM = userMArrayList.get(position);
                PreferenceManager.getDefaultSharedPreferences(UserManagmentActivity.this)
                        .edit().putString("selectedUser", userM.getUserName()).apply();
                finish();
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //onDelete
                taskHelper.deleteUser(userMArrayList.get(position).getUserId());
                ArrayList<UserM> tempList = taskHelper.getUser();
                tempList.remove(0);
                userMArrayList.clear();
                userMArrayList.addAll(tempList);
                userMAdapter.notifyItemRemoved(position);

                if (userMArrayList.size() == 0) {
                    layoutUserInfo.setVisibility(View.VISIBLE);
                }
            }
        });

        rvUser.setAdapter(userMAdapter);
        rvUser.setLayoutManager(new LinearLayoutManager(UserManagmentActivity.this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUser();
    }
}
