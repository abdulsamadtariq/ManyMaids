package com.manymaidsinprovo.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manymaidsinprovo.Activities.ReviewsActivity;
import com.manymaidsinprovo.Activities.UserScheduleActivity;
import com.manymaidsinprovo.Adapter.CategoryAdapter;
import com.manymaidsinprovo.Helper.ApiUrl;
import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.Model.Slide;
import com.manymaidsinprovo.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHomeFragment extends Fragment {

    private SliderLayout sliderLayout;
    private ArrayList<Slide> slideList;
    private Context mContext;
    private LinearLayout layoutNoInternet;
    private RelativeLayout layoutUserHome;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvCategory;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CleaningType> cleaningTypeList;
    private TextView  tvTryAgain;
    private BroadcastReceiver connectivityReceiver;

    public UserHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext=getContext();

        sliderLayout =view.findViewById(R.id.slider);
        layoutNoInternet=view.findViewById(R.id.layoutNoInternet);
        layoutUserHome=view.findViewById(R.id.layoutUserHome);
        swipeRefresh=view.findViewById(R.id.swipeRefresh);
        rvCategory=view.findViewById(R.id.rvCategory);
        tvTryAgain=view.findViewById(R.id.tvTryAgain);


        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Top);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

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

        connectivityReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                ConnectivityManager connectivityManager=(ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        mContext.registerReceiver(connectivityReceiver,filter);

        fetchDataFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(connectivityReceiver);
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
                    JSONArray slidesArray=jResponse.getJSONArray("slides");
                    JSONArray cleaningTypeArray=jResponse.getJSONArray("cleaningType");

                    Gson gson=new Gson();
                    Type slideType= new TypeToken<ArrayList<Slide>>(){}.getType();
                    Type cleaningType=new TypeToken<ArrayList<CleaningType>>(){}.getType();

                    slideList=gson.fromJson(slidesArray.toString(),slideType);
                    cleaningTypeList=gson.fromJson(cleaningTypeArray.toString(),cleaningType);

                    //adding to slides
                    for (Slide slide:slideList){
                        TextSliderView sliderView=new TextSliderView(mContext);
                        sliderView.image(ApiUrl.slidesFolder+slide.getSlideImage());
                        sliderView.description(slide.getSlideCaption());
                        sliderView.setPicasso(Picasso.get());

                        sliderLayout.addSlider(sliderView);
                    }
                    //adding to cleaningType
                    categoryAdapter=new CategoryAdapter(cleaningTypeList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            //on item click
                            Intent intent=new Intent(mContext.getApplicationContext(), UserScheduleActivity.class);
                            CleaningType cleaningType=cleaningTypeList.get(position);
                            intent.putExtra("selectedCleanType",cleaningType);
                            startActivity(intent);
                        }
                    }, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Intent intent=new Intent(mContext.getApplicationContext(), ReviewsActivity.class);
                            CleaningType cleaningType=cleaningTypeList.get(position);
                            intent.putExtra("selectedCleanType",cleaningType);
                            startActivity(intent);

                        }
                    });

                    rvCategory.setLayoutManager(new GridLayoutManager(mContext,2));
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

        Volley.newRequestQueue(mContext).add(request);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderLayout.startAutoCycle();
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderLayout.startAutoCycle();
    }
}
