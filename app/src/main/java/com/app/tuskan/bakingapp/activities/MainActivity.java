package com.app.tuskan.bakingapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import com.app.tuskan.bakingapp.R;
import com.app.tuskan.bakingapp.adapters.RecipeAdapter;
import com.app.tuskan.bakingapp.models.Recipe;
import com.app.tuskan.bakingapp.provider.RecipeContract;
import com.app.tuskan.bakingapp.utilities.JsonUtils;
import com.app.tuskan.bakingapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public final static String LIST_STATE_KEY = "recycler_list_state";

    private Parcelable listState;
    @BindView(R.id.recipe_list_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private static List<Recipe> recipeList;
    private RecipeAdapter recipeAdapter;
    private static RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getRecipes(BASE_URL);
        }
    }

    private void addDbValues(List<Recipe> recipes){
        Cursor cursor = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI, null, null, null, null);
        if(cursor == null || cursor.getCount() < 1){
            for(int i = 0; i < recipes.size(); i++ ){
                Recipe recipe = recipes.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(RecipeContract.RecipeEntry._ID, recipe.getId());
                contentValues.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
                contentValues.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS, recipe.getIngredients());
                getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, contentValues);
            }
        }


    }

    public class RecipeQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
                    recipeList = JsonUtils.parseRecipeJson(searchResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mLoadingIndicator.setVisibility(View.GONE);
                initRecyclerView(recipeList);
                addDbValues(recipeList);

            }
        }
    }
    private void getRecipes(String baseUrl) {
        URL recipeSearchUrl = NetworkUtils.buildUrl(baseUrl);
        new RecipeQueryTask().execute(recipeSearchUrl);
    }

    private void initRecyclerView(List<Recipe> list) {
        recipeAdapter = new RecipeAdapter(this, list);
        mRecyclerView.setAdapter(recipeAdapter);
        layoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.scrollToPosition(0);
        recipeAdapter.notifyDataSetChanged();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 1;
        return nColumns;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            initRecyclerView(recipeList);
            layoutManager.onRestoreInstanceState(listState);
        }
    }
}
