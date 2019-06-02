package com.zcwipe.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zcwipe.photoselector.adapter.GridDividerItemDecoration;
import com.zcwipe.photoselector.adapter.GridViewAdapter;
import com.zcwipe.photoselector.adapter.PhotoAlbumAdapter;
import com.zcwipe.photoselector.controller.AlbumController;
import com.zcwipe.photoselector.model.ImageFolder;
import com.zcwipe.photoselector.model.ImageModel;
import com.zcwipe.photoselector.view.CustomProgressDialog;
import com.zcwipe.photoselector.view.PopFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Version：1.0
 * Created：2019/5/25 0025
 * Author：KoalaWolf
 * Description：
 * 1 扫描图片文件夹
 * 2 最多图片设为当前文件夹
 */
public class PhotoSelectorActivity extends Activity implements GridViewAdapter.OnItemClickImpl, View.OnClickListener, PhotoAlbumAdapter.OnItemClickImpl {

    private final static int HANDLE_SCAN_RECENT = 1001;//扫描最近新增图片
    private final static int HANDLE_SCAN_FOLDER = 1002;//扫描图片文件夹
    public final static int MAX_SelectedNumber = 9;//最大图片数量
    public final static int MIN_SelectedNumber = 1;//最小图片数量
    public final static String SELECTED_MODE = "select_mode";
    public final static int SINGLE_SELECTED = 2001;//单选
    public final static int MULTI_SELECETD = 2002;//多选
    private int mCurrentSelectMode = 2002;//默认多选


    private RecyclerView recyclerView;
    private PhotoAlbumAdapter adapter;
    private LinearLayout ll_back;
    private RelativeLayout rl_album_folder;

    private TextView btn_ensure;//确定选择
    private TextView tv_number; //选中数量
    private TextView tv_preview; //预览

    private TextView tv_album_folder;//当前图片文件夹名称
    private TextView tv_album_count;//当前图片文件夹图片数量

    private CustomProgressDialog mCustomProgressDialog;
    private PopFolder mPopFolder;//图片文件夹popwindow
    private List<String> mImgs = new ArrayList<>();//当前文件夹下图片路径集合
    private File mCurrentFile;//当前显示的图片文件夹

    private List<ImageFolder> imageFolders = new ArrayList<>();//图片文件夹集合
    private List<ImageModel> imageModels = new ArrayList<>();//所有图片集合
    private AlbumController controller;//相册控制器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
        initView();
        initListeners();
        initRecentImages();
        initAlbums();
    }

    //获取最近照片
    private void initRecentImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断手机内存卡是否可使用
            Toast.makeText(this, "当前手机SD卡不能使用", Toast.LENGTH_SHORT).show();
            return;
        }
        mCustomProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageModels.clear();
                imageModels.addAll(controller.getRecentImageList());
                handler.sendEmptyMessage(HANDLE_SCAN_RECENT);
            }
        }).start();
    }

    //初始化相册文件夹
    private void initAlbums() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageFolders = controller.getAlbumList();
                handler.sendEmptyMessage(HANDLE_SCAN_FOLDER);
            }
        }).start();
    }

    private void initListeners() {
        rl_album_folder.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);
        tv_preview.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCustomProgressDialog.dismiss();
            switch (msg.what) {
                case HANDLE_SCAN_RECENT:
                    tv_album_count.setText(imageModels.size() + "");
                    adapter.notifyDataSetChanged();
                    break;
                case HANDLE_SCAN_FOLDER:
                    initPopFolder();
                    break;
            }
        }
    };

    //初始化相册选择popwindow
    private void initPopFolder() {
        mPopFolder = new PopFolder(this, imageFolders);
        mPopFolder.setOnItemClickListener(new PopFolder.OnItemClickImpl() {
            @Override
            public void onItemClick(ImageFolder imageFolder) {
                mCurrentFile = new File(imageFolder.getDir());
                mImgs = controller.getImageListOrderByData(mCurrentFile);

                adapter.notifyDataSetChanged(mImgs, mCurrentFile.getAbsolutePath());
                tv_album_folder.setText(mCurrentFile.getName());
                tv_album_count.setText(adapter.getItemCount() + "");
                tv_number.setText("(0)");
                mPopFolder.dismiss();
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        tv_album_folder = findViewById(R.id.tv_album_folder);
        tv_album_count = findViewById(R.id.tv_album_count);
        tv_number = findViewById(R.id.tv_number);
        tv_preview = findViewById(R.id.tv_preview);
        btn_ensure = findViewById(R.id.btn_ensure);
        ll_back = findViewById(R.id.ll_back);
        rl_album_folder = findViewById(R.id.rl_album_folder);
        mCustomProgressDialog = CustomProgressDialog.createDialog(this);
        controller = new AlbumController(this);
        mCurrentSelectMode = getIntent().getIntExtra(SELECTED_MODE, MULTI_SELECETD);


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridDividerItemDecoration(this, 4, 4, true));

        adapter = new PhotoAlbumAdapter(this, imageModels);
        tv_album_folder.setText("最近照片");
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    //gridView item 点击事件
    @Override
    public void onSelectImages(List<ImageModel> selectImages) {
        tv_number.setText("(" + selectImages.size() + ")");
        if (!adapter.isAbleSelected()) {
            Toast.makeText(this, "最多可以选9张", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_back) {
            finish();
        } else if (view.getId() == R.id.btn_ensure) {
            int count = adapter.getSelectedImagesCount();
            if (count < 1) {
                Toast.makeText(this, "请选择图片", Toast.LENGTH_LONG).show();
                return;
            }
            if (mCurrentSelectMode == SINGLE_SELECTED && count > MIN_SelectedNumber) {
                Toast.makeText(this, "已选1张", Toast.LENGTH_LONG).show();
                return;
            }
            if (mCurrentSelectMode == MULTI_SELECETD && count > MAX_SelectedNumber) {
                Toast.makeText(this, "已选9张", Toast.LENGTH_LONG).show();
                return;
            }
            Intent data = new Intent();
            setBundle(data);
            setResult(RESULT_OK, data);
            finish();
        } else if (view.getId() == R.id.tv_preview) {
            if (adapter.getSelectedImagesCount() < 1) {
                Toast.makeText(this, "请选择图片", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, PhotoPreviewActivity.class);
                setBundle(intent);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.rl_album_folder) {
            if (mPopFolder != null) {
                mPopFolder.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        }
    }

    private void setBundle(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("images", adapter.getSelectedImages());
        intent.putExtras(bundle);
    }

    @Override
    protected void onDestroy() {
        if (mCustomProgressDialog != null) {
            mCustomProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
