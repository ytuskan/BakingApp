package com.app.tuskan.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tuskan.bakingapp.R;
import com.app.tuskan.bakingapp.adapters.DescriptionAdapter;
import com.app.tuskan.bakingapp.models.Video;

import java.util.ArrayList;

/**
 * Created by Yakup on 22.7.2018.
 */

public class DescriptionFragment extends Fragment implements DescriptionAdapter.DescriptionAdapterOnClickHandler {
    public static final String INGREDIENTS = "ingredients";
    public static final String VIDEOS = "videos";

    private static ArrayList<Video> mVideos;
    private String ingredients;

    private View rootView;

    private DescriptionAdapter mDescriptionAdapter;
    private static RecyclerView.LayoutManager mDescriptionLayoutManager;
    private RecyclerView mRecyclerView;

    private Parcelable listState;

    OnDescriptionClickListener mCallback;

    @Override
    public void onClick(int position) {
        mCallback.onDescriptionSelected(position);
    }

    public interface OnDescriptionClickListener {
        void onDescriptionSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnDescriptionClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDescriptionClickListener");
        }
    }

    public DescriptionFragment() {
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Video> getmVideos() {
        return mVideos;
    }

    public void setmVideos(ArrayList<Video> mVideos) {
        this.mVideos = mVideos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            ingredients = savedInstanceState.getString(INGREDIENTS);
            listState = savedInstanceState.getParcelable(VIDEOS);
        }
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_description_part, container, false);
            TextView mIngredients = rootView.findViewById(R.id.description_fragment_ingredients_textview);
            mIngredients.setText(getIngredients());
            mRecyclerView = rootView.findViewById(R.id.description_fragment_list_recyclerview);
            if(mVideos != null){
                initVideoRecyclerView(getmVideos(), this);
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listState != null) {
            initVideoRecyclerView(mVideos, this);
            mDescriptionLayoutManager.onRestoreInstanceState(listState);
        }
    }

    private void initVideoRecyclerView(ArrayList<Video> list, DescriptionAdapter.DescriptionAdapterOnClickHandler descriptionAdapterOnClickHandler) {
        mDescriptionAdapter = new DescriptionAdapter(getContext(), list, descriptionAdapterOnClickHandler);
        mRecyclerView.setAdapter(mDescriptionAdapter);
        mDescriptionLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mDescriptionLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        listState = mDescriptionLayoutManager.onSaveInstanceState();
        outState.putString(INGREDIENTS, ingredients);
        outState.putParcelable(VIDEOS, listState);
    }


}
