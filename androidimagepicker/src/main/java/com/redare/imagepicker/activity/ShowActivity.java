/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.redare.imagepicker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.redare.imagepicker.AndroidImagePicker;
import com.redare.imagepicker.R;
import com.redare.imagepicker.Util;
import com.redare.imagepicker.bean.ImageItem;

import java.util.List;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener,AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImageCropCompleteListener,AndroidImagePicker.OnImagePickCompleteListener{
    private static final String TAG = ShowActivity.class.getSimpleName();

    private final int REQ_IMAGE = 1433;
    private final int REQ_IMAGE_CROP = 1435;

    private TextView btnSingle;
    private TextView btnSingleWithCamera;
    private TextView btnMulti;
    private TextView btnMultiWithCamera;
    private TextView btnCrop;
    private ImageView ivCrop;

    //private ImageView ivShow;
    GridView mGridView;
    SelectAdapter mAdapter;

    private int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        btnSingleWithCamera = (TextView) findViewById(R.id.btn_single_with_camera);
        btnMultiWithCamera = (TextView) findViewById(R.id.btn_multi_with_camera);
        btnSingle = (TextView) findViewById(R.id.btn_single);
        btnMulti = (TextView) findViewById(R.id.btn_multi);
        btnCrop = (TextView) findViewById(R.id.btn_crop);
        ivCrop = (ImageView) findViewById(R.id.iv_crop);
        //ivShow = (ImageView) findViewById(R.id.iv_show);
        mGridView = (GridView) findViewById(R.id.gridview);
        mAdapter = new SelectAdapter(this);
        mGridView.setAdapter(mAdapter);

        btnSingleWithCamera.setOnClickListener(this);
        btnMultiWithCamera.setOnClickListener(this);
        btnSingle.setOnClickListener(this);
        btnMulti.setOnClickListener(this);
        btnCrop.setOnClickListener(this);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();

    }

    @Override
    protected void onResume() {
        AndroidImagePicker.getInstance().setOnPictureTakeCompleteListener(this);//watching Picture taking
        AndroidImagePicker.getInstance().addOnImageCropCompleteListener(this);
        AndroidImagePicker.getInstance().setOnImagePickCompleteListener(this);
        super.onResume();

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        int requestCode = REQ_IMAGE;
        AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
        AndroidImagePicker.getInstance().setShouldShowCamera(false);

        int i = v.getId();
        if (i == R.id.btn_single) {
            AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
            AndroidImagePicker.getInstance().setShouldShowCamera(false);

        } else if (i == R.id.btn_single_with_camera) {
            AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
            AndroidImagePicker.getInstance().setShouldShowCamera(true);

        } else if (i == R.id.btn_multi) {
            AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_MULTI);
            AndroidImagePicker.getInstance().setShouldShowCamera(false);

        } else if (i == R.id.btn_multi_with_camera) {
            AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_MULTI);
            AndroidImagePicker.getInstance().setShouldShowCamera(true);

        } else if (i == R.id.btn_crop) {
            AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
            AndroidImagePicker.getInstance().setShouldShowCamera(true);
            intent.putExtra("isCrop", true);
            requestCode = REQ_IMAGE_CROP;


        } else {
        }


        intent.setClass(this,PickerActivity.class);
        startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            if (requestCode == REQ_IMAGE) {
                ivCrop.setVisibility(View.GONE);

                List<ImageItem> imageList = AndroidImagePicker.getInstance().getSelectedImages();
                mAdapter.clear();
                mAdapter.addAll(imageList);
            }/*else if(requestCode == REQ_IMAGE_CROP){
                Bitmap bmp = (Bitmap)data.getExtras().get("bitmap");
                Log.i(TAG,"-----"+bmp.getRowBytes());
            }*/
        }

    }

    @Override
    public void onPictureTakeComplete(String picturePath) {
        ivCrop.setVisibility(View.GONE);
        List<ImageItem> imageList = AndroidImagePicker.getInstance().getSelectedImages();
        imageList.clear();
        ImageItem item = new ImageItem(picturePath,"",0);
        imageList.add(item);
        mAdapter.clear();
        mAdapter.addAll(imageList);

    }

    @Override
    public void onImageCropComplete(Bitmap bmp, float ratio) {
        Log.i(TAG,"=====onImageCropComplete (get bitmap="+bmp.toString());
        ivCrop.setVisibility(View.VISIBLE);
        ivCrop.setImageBitmap(bmp);
    }

    @Override
    public void onImagePickComplete(List<ImageItem> items) {
        Log.i(TAG,"=====onImagePickComplete (get ImageItems size="+items.size());
        ivCrop.setVisibility(View.GONE);

        //List<ImageItem> imageList = AndroidImagePicker.getInstance().getSelectedImages();
        mAdapter.clear();
        mAdapter.addAll(items);
    }


    class SelectAdapter extends ArrayAdapter<ImageItem>{

        //private int mResourceId;
        public SelectAdapter(Context context) {
            super(context, 0);
            //this.mResourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageItem item = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            //View view = inflater.inflate(mResourceId, null);
            int width = (screenWidth - Util.dp2px(ShowActivity.this,10*2))/3;

            ImageView imageView = new ImageView(ShowActivity.this);
            imageView.setBackgroundColor(Color.GRAY);
            GridView.LayoutParams params = new AbsListView.LayoutParams(width, width);
            imageView.setLayoutParams(params);


            return imageView;
        }

    }


    @Override
    protected void onDestroy() {
        AndroidImagePicker.getInstance().deleteOnImagePickCompleteListener(this);
        AndroidImagePicker.getInstance().removeOnImageCropCompleteListener(this);
        AndroidImagePicker.getInstance().deleteOnPictureTakeCompleteListener(this);
        super.onDestroy();

    }
}
