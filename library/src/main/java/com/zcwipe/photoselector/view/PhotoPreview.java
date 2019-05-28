package com.zcwipe.photoselector.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zcwipe.photoselector.R;
import com.zcwipe.photoselector.model.ImageModel;

/**
 * 大图预览view
 */
public class PhotoPreview extends LinearLayout {

    private ProgressBar pbLoading;
    private PinchImageView pinchImageView;
    private Context mContext;

    public PhotoPreview(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);
        pbLoading = findViewById(R.id.pb_loading);
        pinchImageView = findViewById(R.id.iv_content);
    }

    public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
        this(context);
    }

    public PhotoPreview(Context context, AttributeSet attrs) {
        this(context);
    }

    //显示图片
    public void loadImage(ImageModel imageModel) {
        SimpleTarget target = new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                DisplayMetrics metric = new DisplayMetrics();
                ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
                int height = (int) (metric.heightPixels * 1.5);
                ViewGroup.LayoutParams para = pinchImageView.getLayoutParams();
                para.height = height;
                pinchImageView.setLayoutParams(para);
                pinchImageView.setImageBitmap(bitmap);
            }
        };

        Glide.with(mContext)
                .load(imageModel.getOriginalPath())
                .asBitmap()
                .fitCenter()
                .error(R.drawable.mis_default_error)
                .listener(listener)
                .into(pinchImageView);
//                .into(target);  //宽度铺满,高度自适应 易造成头部显示不全
    }

    private RequestListener listener = new RequestListener() {
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            pbLoading.setVisibility(View.GONE);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            pbLoading.setVisibility(View.GONE);
            return false;
        }
    };

}
