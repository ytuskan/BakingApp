package com.app.tuskan.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tuskan.bakingapp.R;
import com.app.tuskan.bakingapp.models.Video;

import java.util.ArrayList;

/**
 * Created by Yakup on 18.7.2018.
 */

public class DescriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private ArrayList<Video> videos;

    private View previousSelectedItem;

    private final DescriptionAdapterOnClickHandler mClickHandler;

    public interface DescriptionAdapterOnClickHandler {
        void onClick(int position);
    }

    public DescriptionAdapter(Context context, ArrayList<Video> videos, DescriptionAdapterOnClickHandler mClickHandler) {
        this.context = context;
        this.videos = videos;
        this.mClickHandler = mClickHandler;
    }

    public class descriptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shortDescriptionTextView;
        LinearLayout linearLayout;

        public descriptionHolder(View itemView) {
            super(itemView);
            shortDescriptionTextView = itemView.findViewById(R.id.video_list_short_description_textview);
            linearLayout = itemView.findViewById(R.id.video_list_layout);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (previousSelectedItem!=null) {
                previousSelectedItem.setBackgroundResource(R.drawable.border_transparent_style);
            }
            previousSelectedItem=v;
            v.setBackgroundResource(R.drawable.border_style);
            mClickHandler.onClick(adapterPosition);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return new descriptionHolder(layoutInflater.inflate(R.layout.video_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Video video = videos.get(position);
        final descriptionHolder descriptionHolder = (DescriptionAdapter.descriptionHolder) holder;

        descriptionHolder.shortDescriptionTextView.setText(video.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

}
