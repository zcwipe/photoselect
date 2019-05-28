package com.zcwipe.photoselector.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zcwipe.photoselector.model.ImageFolder;
import com.zcwipe.photoselector.model.ImageModel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangc on 2017/9/8.
 * 相册控制器
 */

public class AlbumController {

    private Context context;
    private ContentResolver contentResolver;
    private Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; //查询图片的uri
    private int mMaxCount;//最大图片数量
    private File mCurrentFile;//当前显示的图片文件夹

    public AlbumController(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    /**
     * 获取指定相册所有图片
     *
     * @param albumName 相册名称
     * @return
     */
    public List<ImageModel> getImagesByAlbumName(String albumName) {
        List<ImageModel> imageModelList = new ArrayList<>();
        Cursor cursor = contentResolver.query(uri,
                new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME},
                "bucket_display_name = ?",
                new String[]{albumName},
                MediaStore.Images.Media.DATE_ADDED + " desc");
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            ImageModel mdoel = new ImageModel();
            mdoel.setId(id);
            mdoel.setOriginalPath(originalPath);
            imageModelList.add(mdoel);
        }
        cursor.close();

        for (ImageModel bean : imageModelList) {
            setThumbnailPath(bean);
        }
        return imageModelList;
    }

    /**
     * 获取系统相册列表
     *
     * @return List<ImageFolder>
     */
    public List<ImageFolder> getAlbumList() {
        List<ImageFolder> beans = new ArrayList<>();
        //查询字段
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME};
        //查询条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        //查询条件参数
        String[] selectionArgs = {"image/jpeg", "image/png", "image/jpg"};
        //排序
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
        Set<String> mDirPaths = new HashSet<String>();
        while (cursor.moveToNext()) {
            //获取图片路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //验证图片有效性
            File file = new File(path);
            if (!file.exists()) {
                continue;
            }
            File parentFile = new File(path).getParentFile();
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            ImageFolder bean = null;
            //防止多次扫描同一个文件夹
            if (mDirPaths.contains(dirPath)) {
                continue;
            } else {
                mDirPaths.add(dirPath);
                bean = new ImageFolder();
                bean.setDir(dirPath);
                bean.setFirstImgPath(path);
                bean.setName(parentFile.getName());
            }
            if (parentFile.list() == null) {
                continue;
            }
            int picSize = parentFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String filename) {
                    if (filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg")
                            || filename.endsWith(".png")
                            || filename.endsWith("JPG"))
                        return true;
                    return false;
                }
            }).length;
            bean.setCount(picSize);
            beans.add(bean);
        }
        cursor.close();
        return beans;
    }

    /**
     * 获取最近新增照片 照片大于10KB
     *
     * @return List<ImageModel>
     */
    public List<ImageModel> getRecentImageList() {
        List<ImageModel> imageBeanList = new ArrayList<>();
        //查询条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        //查询条件参数
        String[] selectionArgs = {"image/jpeg", "image/png", "image/jpg"};
//        Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, MediaStore.Images.Media.DATE_ADDED + " desc");
        Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, MediaStore.Images.Media.DATE_ADDED + " desc");
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            //获取图片大小
            int size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            //大于10KB 添加到最近相册
            if (size > 10 * 1024) {
                String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                ImageModel imageBean = new ImageModel();
                imageBean.setId(id);
                imageBean.setOriginalPath(originalPath);
                imageBeanList.add(imageBean);
            }
        }
        cursor.close();
//        for (ImageModel bean : imageBeanList) {
//            setThumbnailPath(bean);
//        }
        return imageBeanList;
    }

    /**
     * 根据images表中的id 查询Thumbnails表中缩略图
     *
     * @param imageBean MediaStore.Images.Thumbnails.DATA 缩略图地址
     * @return
     */
    public ImageModel setThumbnailPath(ImageModel imageBean) {
        Cursor cursor = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.WIDTH, MediaStore.Images.Thumbnails.HEIGHT},
                "image_id = ?",
                new String[]{imageBean.getId()},
                null);
        while (cursor.moveToNext()) {
            String thumbnailPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            imageBean.setThumbnailPath(thumbnailPath);
        }
        cursor.close();
        return imageBean;
    }

    /**
     * 按文件修改时间排序
     * @param file
     * @return
     */
    public List<String> getImageListOrderByData(final File file) {
        List<String> mImgs = Arrays.asList(file.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".jpeg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".JPG"))
                    return true;
                return false;
            }
        }));

        Collections.sort(mImgs, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Long time1 = new File(file.getAbsolutePath() + "/" + lhs).lastModified();
                Long time2 = new File(file.getAbsolutePath() + "/" + rhs).lastModified();
                return time2.compareTo(time1);
            }
        });
        return mImgs;
    }
}
