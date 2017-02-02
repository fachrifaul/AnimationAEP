package com.fachrifaul.animationaep;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.model.LottieComposition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    static ListFragment newInstance() {
        return new ListFragment();
    }

    @BindView(R.id.container) ViewGroup container;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.animation_view) LottieAnimationView animationView;

    private List<String> files = Collections.emptyList();
    private FileAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        try {
            files = AssetUtils.getJsonAssets(getContext(), "");
        } catch (IOException e) {
            //noinspection ConstantConditions
            Snackbar.make(getView(), R.string.invalid_assets, Snackbar.LENGTH_LONG).show();
        }

        adapter = new FileAdapter(files);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        animationView.setProgress(0f);
        animationView.playAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
        animationView.cancelAnimation();
    }

    final class FileAdapter extends RecyclerView.Adapter<StringViewHolder> {
        private static final String TAG_VIEWER = "viewer";
        private static final String TAG_TYPOGRAPHY = "typography";
        private static final String TAG_APP_INTRO = "app_intro";
        private List<String> files = new ArrayList<>();

        public FileAdapter(List<String> files) {
            this.files = files;
        }

        @Override
        public StringViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StringViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(StringViewHolder holder, int position) {
            if (files != null) {
                holder.bind(files.get(position), TAG_VIEWER);
                if (position == 0) {
                    holder.animationContainer.setActivated(holder.animationContainer.isActivated());
                }
            }
        }

        @Override
        public int getItemCount() {
            return files.size();
        }
    }

    final class StringViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.animation_view) LottieAnimationView animationView;
        @BindView(R.id.animation_container) ViewGroup animationContainer;

        StringViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_file, parent, false));
            ButterKnife.bind(this, itemView);
        }

        void bind(String assetName, String tag) {
            itemView.setTag(tag);

            LottieComposition.fromAssetFileName(getContext(), assetName, new LottieComposition.OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(LottieComposition composition) {
                    animationView.setComposition(composition);
                }
            });

            animationView.playAnimation();

        }
    }
}
