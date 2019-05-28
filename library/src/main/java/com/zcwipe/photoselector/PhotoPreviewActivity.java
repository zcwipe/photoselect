package com.zcwipe.photoselector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcwipe.photoselector.model.ImageModel;
import com.zcwipe.photoselector.view.PhotoPreview;

import java.util.ArrayList;
import java.util.List;


/**
 * Version：1.0
 * Created：2019/5/25 0025
 * Author：KoalaWolf
 * Description：浏览大图
 */
public class PhotoPreviewActivity extends Activity implements ViewPager.OnPageChangeListener {

    private List<String> dates;
    private List<ImageModel> imageList = null;
    private int currentIndex;
    private VPAdapter adapter;
    private TextView tv_title;
    private ViewPager mViewPager;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        initView();
        initListener();
        initDatas();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(this);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPreviewActivity.this.finish();
            }
        });
    }

    private void initDatas() {
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        imageList = (List<ImageModel>) getIntent().getExtras().getSerializable("images");
        currentIndex = getIntent().getIntExtra("index", 0);
        adapter = new VPAdapter();
        mViewPager.setAdapter(adapter);
        onPageSelected(currentIndex);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp);
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mViewPager.setCurrentItem(position);
//        tv_title.setText((position + 1) + "/" + dates.size());
        tv_title.setText((position + 1) + "/" + imageList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class VPAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//            return dates == null ? 0 : dates.size();
            return imageList == null ? 0 : imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoPreview photoPreview = new PhotoPreview(PhotoPreviewActivity.this);
            container.addView(photoPreview);
            photoPreview.loadImage(imageList.get(position));
            return photoPreview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
