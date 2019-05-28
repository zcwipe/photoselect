package com.zcwipe.photoselector.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zcwipe.photoselector.R;
import com.zcwipe.photoselector.adapter.PopFolderAdapter;
import com.zcwipe.photoselector.model.ImageFolder;

import java.util.List;


/**
 * Created by zhangcheng on 2017/2/23/023.
 * 图片文件夹popwindow
 * 1 初始化rootview width height
 * 2 设置进入动画
 * 3 设置点击消失
 * 4 设置显示方式 showasdropdown  showatlocation
 */

public class PopFolder extends PopupWindow {

    private Activity context;
    private View convertView;
    private ListView lv_folder;//图片文件夹ListView
    private List<ImageFolder> list;
    private PopFolderAdapter adapter;

    private OnItemClickImpl onItemClickListener;

    public interface OnItemClickImpl {
        void onItemClick(ImageFolder imageFolder);
    }

    public void setOnItemClickListener(OnItemClickImpl listener) {
        this.onItemClickListener = listener;
    }

    public PopFolder(Activity context, List<ImageFolder> list) {
        this.context = context;
        this.list = list;
        convertView = context.getLayoutInflater().inflate(R.layout.pop_folder, null, false);
        setContentView(convertView);
        initView(convertView);

        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int popHeight = outMetrics.heightPixels * 3 / 5;
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(popHeight);
//        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.AnimationFadeBottomToTop);//pop出现动画

        setOutsideTouchable(true);//点击popuwindow之外，关闭pop
        setFocusable(true);//配合设置背景，点击pop之外，back键会使pop消失
        setBackgroundDrawable(new ColorDrawable(0x55000000));

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setScreenAlpha(1f);
            }
        });
    }

    /**
     * 初始化pop控件
     *
     * @param convertView
     */
    private void initView(View convertView) {
        lv_folder = (ListView) convertView.findViewById(R.id.lv_folder);
        adapter = new PopFolderAdapter(context, list);
        lv_folder.setAdapter(adapter);
        lv_folder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(list.get(position));
                }
            }
        });
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            setScreenAlpha(1f);
            super.dismiss();
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setScreenAlpha(0.7f);
        super.showAtLocation(parent, gravity, x, y);
    }

    private void setScreenAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
        if (alpha == 1) {
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        context.getWindow().setAttributes(lp);
    }


}
