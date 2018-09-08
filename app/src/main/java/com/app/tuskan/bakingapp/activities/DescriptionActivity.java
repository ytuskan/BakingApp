package com.app.tuskan.bakingapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.tuskan.bakingapp.R;
import com.app.tuskan.bakingapp.adapters.RecipeAdapter;
import com.app.tuskan.bakingapp.fragments.DescriptionFragment;
import com.app.tuskan.bakingapp.fragments.StepFragment;
import com.app.tuskan.bakingapp.models.Recipe;
import com.app.tuskan.bakingapp.models.Video;
import com.app.tuskan.bakingapp.utilities.JsonUtils;
import com.app.tuskan.bakingapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity implements DescriptionFragment.OnDescriptionClickListener {

    private static final String TAG = DescriptionActivity.class.getSimpleName();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String VIDEOS = "videos";
    private static final String POSITION = "position";

    private static ArrayList<Video> videos;
    private Recipe recipe;

    FragmentManager mFragmentManager = getSupportFragmentManager();

    private void setViewValues() {
        Intent intent = getIntent();
        if (intent.hasExtra(RecipeAdapter.INTENT_KEY)) {
            Bundle parcelable = intent.getExtras();
            recipe = parcelable.getParcelable(RecipeAdapter.INTENT_KEY);
        }

    }

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        setViewValues();
        getSupportActionBar().setTitle(recipe.getName());

        if (savedInstanceState == null) {
            getVideos(BASE_URL);
        }
    }

    @Override
    public void onDescriptionSelected(int position) {
        //Toast.makeText(this, "" + position + " clicked", Toast.LENGTH_LONG).show();
        if (mTwoPane) {
            DescriptionFragment descriptionFragment = new DescriptionFragment();
            descriptionFragment.setmVideos(videos);
            descriptionFragment.setIngredients(recipe.getIngredients());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.description_framelayout, descriptionFragment)
                    .commit();

            StepFragment stepFragment = new StepFragment();
            stepFragment.setmVideos(videos);
            stepFragment.setIndex(position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment_frame, stepFragment)
                    .commit();

        } else {
            Bundle b = new Bundle();
            b.putParcelableArrayList(VIDEOS, videos);
            b.putInt(POSITION, position);

            final Intent intent = new Intent(this, StepActivity.class);
            intent.putExtras(b);
            Button mNextButton = findViewById(R.id.description_next_button);
            mNextButton.setVisibility(View.VISIBLE);
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
        }


    }

    public class RecipeQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String bakingResult = null;
            try {
                bakingResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bakingResult;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                try {
                    videos = JsonUtils.parseVideoJson(searchResults, recipe.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (findViewById(R.id.step_fragment_frame) != null) {
                    mTwoPane = true;
                    DescriptionFragment descriptionFragment = new DescriptionFragment();
                    descriptionFragment.setIngredients(recipe.getIngredients());
                    descriptionFragment.setmVideos(videos);

                    mFragmentManager.beginTransaction()
                            .add(R.id.description_framelayout, descriptionFragment)
                            .commit();

                    StepFragment stepFragment = new StepFragment();
                    stepFragment.setmVideos(videos);
                    stepFragment.setIndex(0);

                    mFragmentManager.beginTransaction()
                            .add(R.id.step_fragment_frame, stepFragment)
                            .commit();

                } else {
                    mTwoPane = false;
                    DescriptionFragment descriptionFragment = new DescriptionFragment();
                    descriptionFragment.setIngredients(recipe.getIngredients());
                    descriptionFragment.setmVideos(videos);

                    mFragmentManager.beginTransaction()
                            .add(R.id.description_framelayout, descriptionFragment)
                            .commit();
                }

            }
        }
    }

    private void getVideos(String baseUrl) {
        URL videoSearchUrl = NetworkUtils.buildUrl(baseUrl);
        new RecipeQueryTask().execute(videoSearchUrl);
    }
}
