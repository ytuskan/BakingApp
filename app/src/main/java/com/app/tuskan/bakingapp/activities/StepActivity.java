package com.app.tuskan.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.app.tuskan.bakingapp.R;
import com.app.tuskan.bakingapp.fragments.StepFragment;
import com.app.tuskan.bakingapp.models.Video;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {
    private static final String VIDEOS = "videos";
    private static final String POSITION = "position";

    private static ArrayList<Video> videos;
    private int position;

    FragmentManager fragmentManager = getSupportFragmentManager();
    StepFragment stepFragment;

    private void setViewValues(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        videos = bundle.getParcelableArrayList(VIDEOS);
        position = bundle.getInt(POSITION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        setViewValues();


        if(savedInstanceState == null){
            stepFragment = new StepFragment();
            stepFragment.setmVideos(videos);
            stepFragment.setIndex(position);
            fragmentManager.beginTransaction()
                    .add(R.id.step_fragment_frame, stepFragment)
                    .commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
