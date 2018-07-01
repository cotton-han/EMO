package com.example.ds.emodiary;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;


public class OneFragmentResult extends AppCompatActivity {

    ImageView imageP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_1_result);

        imageP = findViewById(R.id.imageP);

    }
}
