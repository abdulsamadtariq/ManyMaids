package com.manymaidsinprovo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.manymaidsinprovo.Model.CleaningType;
import com.manymaidsinprovo.R;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        CleaningType selectedCleanType=(CleaningType) getIntent().getSerializableExtra("selectedCleanType");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Reviews of ('"+selectedCleanType.getCategoryName() +"')");
    }
}
