package com.zcwipe.photoselector.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zcwipe.photoselector.R;
import com.zcwipe.photoselector.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Version：1.0
 * Created：2019/6/2 0002
 * Author：KoalaWolf
 * Description：相册recyclerview adapter
 */
public class PhotoAlbumAdapter extends RecyclerView.Adapter<ViewHolder> {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<ImageModel> mDatas;


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

    private PhotoAlbumAdapter.OnItemClickImpl listener;

    public interface OnItemClickImpl {
        void onSelectImages(List<ImageModel> selectImages);
    }

    public void setOnItemClickListener(PhotoAlbumAdapter.OnItemClickImpl listener) {
        this.listener = listener;
    }

    public PhotoAlbumAdapter(Context context, List<ImageModel> mDatas) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = ViewHolder.get(mContext, viewGroup, R.layout.item_photo_album);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final ImageView item_iv = viewHolder.getView(R.id.item_iv);
        final ImageView item_check = viewHolder.getView(R.id.item_check);
        final View mask = viewHolder.getView(R.id.mask);

        final ImageModel model = mDatas.get(position);
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
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void notifyDataSetChanged(List<String> mDatas, String dirPath) {
//        this.mDatas = mDatas;
//        this.dirPath = dirPath;
        List<String> paths = (List<String>) mDatas;
        List<ImageModel> imageList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ImageModel model = new ImageModel();
            model.setOriginalPath(dirPath + "/" + paths.get(i));
            imageList.add(model);
        }
        this.mDatas = imageList;
        selectImageList.clear();
        selected = true;
        super.notifyDataSetChanged();
    }

    public int getSelectedImagesCount() {
        return selectImageList.size();
    }

    public ArrayList<ImageModel> getSelectedImages() {
        return selectImageList;
    }

}
