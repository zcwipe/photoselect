package com.zcwipe.photoselector.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zcwipe.photoselector.R;
import com.zcwipe.photoselector.model.ImageFolder;

import java.util.List;


/**
 * Created by zhangcheng on 2017/3/9/009.
 */

public class PopFolderAdapter<T> extends CommonAdapter<T> {

    public PopFolderAdapter(Context context, List<T> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_pop_folder, null);
        }
        TextView item_pop_file_name = ViewHolder.get(view, R.id.item_pop_file_name);
        TextView item_pop_file_number = ViewHolder.get(view, R.id.item_pop_file_number);
        ImageView item_pop_image = ViewHolder.get(view, R.id.item_pop_image);
        ImageFolder imageFolder = (ImageFolder) mDatas.get(i);
        if (imageFolder != null) {
            item_pop_file_name.setText(imageFolder.getName());
            item_pop_file_number.setText(imageFolder.getCount() + "");
            Glide.with(mContext).load(imageFolder.getFirstImgPath()).into(item_pop_image);
        }
        return view;
    }
}
