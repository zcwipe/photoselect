package com.zcwipe.photoselector.model;

import java.io.Serializable;

/**
 * Created by zhangc on 2017/9/13.
 * 图片对象
 */

public class ImageModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String originalPath;//图片地址
    private String originalWidth;
    private String originalHeight;

    private String thumbnailPath;//缩略图
    private String thumbnailWidth;
    private String thumbnailHeight;


    public String getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(String originalWidth) {
        this.originalWidth = originalWidth;
    }

    public String getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(String originalHeight) {
        this.originalHeight = originalHeight;
    }

    public String getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(String thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public String getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(String thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        } else  if(obj == null || !(obj instanceof ImageModel)){
            return false;
        }else if(((ImageModel) obj).getOriginalPath().equals(this.originalPath)){
            return true;
        }
        return false;
    }
}
