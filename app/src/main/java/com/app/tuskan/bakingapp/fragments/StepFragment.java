package com.app.tuskan.bakingapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.tuskan.bakingapp.R;
import com.app.tuskan.bakingapp.models.Video;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by Yakup on 25.7.2018.
 */

public class StepFragment extends Fragment implements ExoPlayer.EventListener, View.OnClickListener {

    private static final String TAG = StepFragment.class.getSimpleName();
    public static final String VIDEOS = "videos";
    public static final String INDEX = "index";

    private static ArrayList<Video> mVideos;
    private static int index;
    private long position = 0;

    private View rootView;

    public StepFragment() {
    }

    private SimpleExoPlayerView exoPlayerView;
    private TextView descriptionTextView;
    private TextView shortDescriptionTextView;
    private Button stepNextButton;
    private Button stepPreviousButton;

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public ArrayList<Video> getmVideos() {
        return mVideos;
    }

    public void setmVideos(ArrayList<Video> mVideos) {
        this.mVideos = mVideos;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private void setViewValues(int index) {
        int temp = index;
        descriptionTextView.setText(mVideos.get(temp).getDescription());
        shortDescriptionTextView.setText(mVideos.get(temp).getShortDescription());
        if (index == mVideos.size() - 1) {
            stepNextButton.setText("Last Video");
        } else {
            stepNextButton.setText("Next: " + mVideos.get(temp + 1).getShortDescription());
        }
        initializePlayer(Uri.parse(mVideos.get(temp).getVideoUrl()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mVideos = savedInstanceState.getParcelableArrayList(VIDEOS);
            index = savedInstanceState.getInt(INDEX);
        }

        rootView = inflater.inflate(R.layout.fragment_step_part, container, false);
        exoPlayerView = rootView.findViewById(R.id.step_fragment_videoview);
        descriptionTextView = rootView.findViewById(R.id.step_fragment_description_textview);
        shortDescriptionTextView = rootView.findViewById(R.id.step_fragment_short_description_textview);
        stepNextButton = rootView.findViewById(R.id.step_fragment_next_button);
        stepPreviousButton = rootView.findViewById(R.id.step_fragment_previous_button);
        initializeMediaSession();

        stepNextButton.setOnClickListener(this);
        stepPreviousButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (mVideos != null) {
                setViewValues(index);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            if (mVideos != null || mExoPlayer == null) {
                setViewValues(index);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT <= 23) {
            if(mExoPlayer != null){
                position = mExoPlayer.getCurrentPosition();
            }
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT > 23) {
            if(mExoPlayer != null){
                position = mExoPlayer.getCurrentPosition();
            }
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE
                );

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.seekTo(position);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.step_fragment_next_button) {
            if (index != mVideos.size() - 1) {
                index++;
                releasePlayer();
                initializeMediaSession();
                setViewValues(index);
            }
        }
        if (id == R.id.step_fragment_previous_button) {
            if (index != 0) {
                index--;
                releasePlayer();
                initializeMediaSession();
                setViewValues(index);
            }
        }

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(VIDEOS, mVideos);
        outState.putInt(INDEX, index);
    }


}
