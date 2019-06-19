package com.example.facerecognition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facerecognition.FaceRecognition.FaceNet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGEVIEW_CONTENT = 1;
    private static final int PICK_IMAGEVIEW2_CONTENT = 2;

    private Bitmap image;
    private Bitmap image2;
    private ImageView imageView;
    private ImageView imageView2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        button = findViewById(R.id.button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGEVIEW_CONTENT);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGEVIEW2_CONTENT);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image == null || image2 == null){
                    Toast.makeText(getApplicationContext(), "One of the images haven't been set yet.", Toast.LENGTH_SHORT).show();
                }else{
                    FaceNet facenet = null;
                    try {
                        facenet = new FaceNet(getAssets());

                        /*float[][] embeddings = facenet.runFloat(image);

                        for (int i = 0; i < 512; i++){
                            Log.i("embeddings", String.valueOf(embeddings[0][i]));
                        }*/

                        double score = facenet.getSimilarityScore(image, image2);
                        Log.i("score", String.valueOf(score));

                        facenet.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGEVIEW_CONTENT && resultCode == RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                image = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error loading gallery image.", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == PICK_IMAGEVIEW2_CONTENT && resultCode == RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                image2 = BitmapFactory.decodeStream(imageStream);
                imageView2.setImageBitmap(image2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error loading gallery image.", Toast.LENGTH_LONG).show();
            }
        }
    }



}
