package com.zcwipe.photoselector.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zcwipe.photoselector.R;
import com.zcwipe.photoselector.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9/009.
 */
/**
 * Version：1.0
 * Created：2019/6/2 0002
 * Author：KoalaWolf
 * Description：相册GridView adapter
 */
public class GridViewAdapter<T> extends CommonAdapter<T> {

    private int itemWidth;
    private int horizentalNum = 4;
    private AbsListView.LayoutParams itemLayoutParams;

    private boolean selected = true;//是否可选

    public void setSelectedBoolean(boolean selected) {
        this.selected = selected;
    }

    public boolean isAbleSelected() {
        return selected;
    }

    public String getDirPath() {
        return dirPath;
    }

    private String dirPath;//文件夹路径
    private ArrayList<ImageModel> selectImageList = new ArrayList<>();//选中图片集合
    private GridViewAdapter.OnItemClickImpl listener;

    /**
     * 设置每一个Item的宽高
     */
    public void setItemWidth() {
        int horizentalSpace = mContext.getResources().getDimensionPixelSize(R.dimen.gridview_item_horizontalSpacing);
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int screenWidth = metric.widthPixels;
        this.itemWidth = (screenWidth - (horizentalSpace * (horizentalNum - 1))) / horizentalNum;
        this.itemLayoutParams = new AbsListView.LayoutParams(itemWidth, itemWidth);
    }

    public interface OnItemClickImpl {
        void onSelectImages(List<ImageModel> selectImages);
    }

    public void setOnItemClickListener(GridViewAdapter.OnItemClickImpl listener) {
        this.listener = listener;
    }


    private RequestListener<? super String, GlideDrawable> reqlistener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    /**
     * @param context
     * @param mDatas  文件夹下所有图片路径
     * @param dirPath 文件夹路径
     */
    public GridViewAdapter(Context context, List<T> mDatas, String dirPath) {
        super(context, mDatas);
        this.dirPath = dirPath;
        setItemWidth();
    }

    public GridViewAdapter(Context context, List<T> mDatas) {
        super(context, mDatas);
        setItemWidth();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_photo_album, null);
            view.setLayoutParams(itemLayoutParams);
        }
        final ImageView item_iv = ViewHolder.get(view, R.id.item_iv);
        final ImageView item_check = ViewHolder.get(view, R.id.item_check);
        final View mask = ViewHolder.get(view, R.id.mask);//选中遮罩

        final ImageModel model = (ImageModel) mDatas.get(position);
        final String localImagePath = model.getOriginalPath();

        if (selectImageList.contains(model)) {
            item_check.setImageResource(R.mipmap.icon_check_on);
            mask.setVisibility(View.VISIBLE);
        } else {
            item_check.setImageResource(R.mipmap.icon_check_nm);
            item_iv.setColorFilter(null);
            mask.setVisibility(View.GONE);
        }
        item_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectImageList.contains(model)) {
                    if (selectImageList.size() == 9) {
                        selected = false;
                    } else {
                        selectImageList.add(model);
                        item_check.setImageResource(R.mipmap.icon_check_on);
                        mask.setVisibility(View.VISIBLE);
                    }
                } else {
                    selectImageList.remove(model);
                    item_check.setImageResource(R.mipmap.icon_check_nm);
                    item_iv.setColorFilter(null);
                    mask.setVisibility(View.GONE);
                    selected = true;
                }
                if (listener != null) {
                    listener.onSelectImages(selectImageList);
                }
            }
        });


        Glide.with(mContext).load(localImagePath)
//                .crossFade()
//                .thumbnail(0.1f)
                .centerCrop()
                .placeholder(R.drawable.ic_picture_loading)
                .dontAnimate()
                .error(R.drawable.mis_default_error)
                .into(item_iv);

        return view;
    }


    public int getSelectedImagesCount() {
        return selectImageList.size();
    }

    public ArrayList<ImageModel> getSelectedImages() {
        return selectImageList;
    }

    public void notifyDataSetChanged(List<T> mDatas, String dirPath) {
//        this.mDatas = mDatas;
//        this.dirPath = dirPath;
        List<String> paths = (List<String>) mDatas;
        List<ImageModel> iamgeList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ImageModel model = new ImageModel();
            model.setOriginalPath(dirPath + "/" + paths.get(i));
            iamgeList.add(model);
        }
        this.mDatas = (List<T>) iamgeList;
        selectImageList.clear();
        selected = true;
        super.notifyDataSetChanged();
    }

}
