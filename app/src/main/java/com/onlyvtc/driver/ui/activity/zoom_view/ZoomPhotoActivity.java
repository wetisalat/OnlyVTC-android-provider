package com.onlyvtc.driver.ui.activity.zoom_view;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.onlyvtc.driver.R;
import com.onlyvtc.driver.base.BaseActivity;
import com.otaliastudios.zoom.ZoomImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZoomPhotoActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.zoom_image)
    ZoomImageView zoomImage;

    public static String URL = "URL";
    private String url = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_zoom_view;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        if (getIntent().getExtras() != null) {
            url = getIntent().getStringExtra(URL);
        }
        Glide.with(activity()).load(url).apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                dontAnimate().error(R.drawable.ic_user_placeholder)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                zoomImage.setImageDrawable(resource);
                zoomImage.realZoomTo(1.0f, false);
                return true;
            }
        }).into(zoomImage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
