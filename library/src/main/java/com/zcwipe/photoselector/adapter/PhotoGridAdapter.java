package com.zcwipe.photoselector.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcwipe.photoselector.PhotoPreviewActivity;
import com.zcwipe.photoselector.R;
import com.zcwipe.photoselector.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Version：1.0
 * Created：2019/6/2 0002
 * Author：KoalaWolf
 * Description：gridview 图片适配器
 */
public class PhotoGridAdapter extends BaseAdapter {

    private List<ImageModel> list;
    private Context mContext;
    private AnimationDrawable animation;

    private boolean isDel;//删除按钮是否显示 true 显示

    public PhotoGridAdapter(Context mContext, List<ImageModel> list, boolean isDel) {
        this.mContext = mContext;
        this.list = list;
        this.isDel = isDel;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private void updateView() {
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_grid, null);
            viewHolder.img_photo = view.findViewById(R.id.img_photo);
            viewHolder.lv_photo = view.findViewById(R.id.lv_photo);
            viewHolder.img_del = view.findViewById(R.id.img_del);
            viewHolder.btn_photo = view.findViewById(R.id.btn_photo);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (isDel)
            viewHolder.img_del.setVisibility(View.VISIBLE);
        else
            viewHolder.img_del.setVisibility(View.GONE);

        viewHolder.btn_photo.setVisibility(View.INVISIBLE);

        final ImageModel photoModel = list.get(position);
        viewHolder.lv_photo.setVisibility(View.VISIBLE);

        Glide.with(mContext).load(photoModel.getOriginalPath())
//                .crossFade()
//                .thumbnail(0.1f)
                .centerCrop()
                .placeholder(R.drawable.ic_picture_loading)
                .dontAnimate()
                .error(R.drawable.mis_default_error)
                .into(viewHolder.img_photo);

        viewHolder.lv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ImageModel> photoList = new ArrayList<>();
                int index = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (position == i)
                        index = position;
                    photoList.add(list.get(i));
                }
                Intent intent = new Intent(mContext, PhotoPreviewActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("images", photoList);
                mContext.startActivity(intent);
            }
        });


        viewHolder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                updateView();
            }
        });
        return view;
    }

    final static class ViewHolder {
        ImageView img_photo;
        LinearLayout lv_photo;
        ImageView img_del;
        TextView btn_photo;
    }

    public void appendList(List<ImageModel> imageModels) {
        list.clear();
        list.addAll(imageModels);
        notifyDataSetChanged();
    }


}