package com.zcwipe.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.zcwipe.photoselector.PhotoSelectorActivity;
import com.zcwipe.photoselector.adapter.PhotoGridAdapter;
import com.zcwipe.photoselector.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class SimpleActivity extends AppCompatActivity {

    private GridView gridView;
    private List<ImageModel> list = new ArrayList<>();
    private PhotoGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        findViewById(R.id.btn_view_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SimpleActivity.this, PhotoSelectorActivity.class), 1000);
            }
        });
        gridView = findViewById(R.id.gridView);
        adapter = new PhotoGridAdapter(this, list, true);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageModel> intentImages = (List<ImageModel>) data.getSerializableExtra("images");
        if (null != intentImages && intentImages.size() > 0) {
            adapter.appendList(intentImages);
        }
    }
}
