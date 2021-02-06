package com.manymaidsinprovo.Dialouge;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.manymaidsinprovo.Activities.UserManagmentActivity;
import com.manymaidsinprovo.Adapter.UserMAdapter;
import com.manymaidsinprovo.Fragments.BasicTaskFragment;
import com.manymaidsinprovo.Model.UserM;
import com.manymaidsinprovo.R;
import com.manymaidsinprovo.SQLite.TaskHelper;

import java.util.ArrayList;

public class BottomSheetUserAssignment extends BottomSheetDialogFragment {


    private TextInputEditText titUser;
    private TextInputLayout tilUser;
    private Button btnAddUser;
    private RecyclerView rvUser;
    private UserMAdapter userMAdapter;
    private ArrayList<UserM> userMArrayList;
    private TaskHelper taskHelper;
    private LinearLayout layoutUserInfo;
    private Context mContext;
    private OnUserSelectionListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_user_assignment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        tilUser = view.findViewById(R.id.tilUser);
        titUser = view.findViewById(R.id.titUser);
        btnAddUser = view.findViewById(R.id.btnAddUser);
        layoutUserInfo = view.findViewById(R.id.layoutUserInfo);
        rvUser = view.findViewById(R.id.rvUser);

        userMArrayList = new ArrayList<>();
        taskHelper = new TaskHelper(mContext);
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
                mListener.onUserChangeData(userM.getUserName());
                dismiss();
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
        rvUser.setLayoutManager(new LinearLayoutManager(mContext));

    }

    public interface OnUserSelectionListener {
        void onUserChangeData(CharSequence data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnUserSelectionListener) {
            mListener = (OnUserSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAreaSelectionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }


}
