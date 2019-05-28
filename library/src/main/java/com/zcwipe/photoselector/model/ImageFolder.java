package com.zcwipe.photoselector.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9/009.
 * 图片文件夹对象
 */

public class ImageFolder {

    private String name;//图库名称
    private String dir;//图库路径
    private String firstImgPath;//第一张图片路径
    private int count;//图片数量
    private List<String> mImgs;


    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    @Override
    public String toString() {
        return "imagefolder:{文件夹名称:" + name + ",图库路径:" + dir + ",图片数量：" + count + ",第一张图片路径：" + firstImgPath + "}";
    }
}
