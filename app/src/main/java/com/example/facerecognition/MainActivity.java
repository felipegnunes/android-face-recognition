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

import com.example.facerecognition.FaceDetection.Box;
import com.example.facerecognition.FaceDetection.MTCNN;
import com.example.facerecognition.FaceRecognition.FaceNet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGEVIEW_CONTENT = 1;
    private static final int PICK_IMAGEVIEW2_CONTENT = 2;

    private Bitmap image;
    private Bitmap image2;
    private ImageView imageView;
    private ImageView imageView2;
    private Button button;
    private MTCNN mtcnn;

    private Bitmap cropFace(Bitmap bitmap){
        Bitmap croppedBitmap = null;
        try {
            Vector<Box> boxes = mtcnn.detectFaces(bitmap, 10);

            Log.i("MTCNN", "No. of faces detected: " + boxes.size());

            int left = boxes.get(0).left();
            int top = boxes.get(0).top();

            int x = boxes.get(0).left();
            int y = boxes.get(0).top();
            int width = boxes.get(0).width();
            int height = boxes.get(0).height();


            if (y + height >= bitmap.getHeight())
                height -= (y + height) - (bitmap.getHeight() - 1);
            if (x + width >= bitmap.getWidth())
                width -= (x + width) - (bitmap.getWidth() - 1);

            Log.i("MTCNN", "Final x: " + String.valueOf(x + width));
            Log.i("MTCNN", "Width: " + bitmap.getWidth());
            Log.i("MTCNN", "Final y: " + String.valueOf(y + width));
            Log.i("MTCNN", "Height: " + bitmap.getWidth());

            croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
        }catch (Exception e){
            e.printStackTrace();
        }
        return croppedBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        button = findViewById(R.id.button);
        mtcnn = new MTCNN(getAssets());

        imageView.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PICK_IMAGEVIEW_CONTENT);
        });

        imageView2.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PICK_IMAGEVIEW2_CONTENT);
        });

        button.setOnClickListener(view -> {
            if (image == null || image2 == null){
                Toast.makeText(getApplicationContext(), "One of the images haven't been set yet.", Toast.LENGTH_SHORT).show();
            }else{
                new Thread(() -> {
                    try {
                        FaceNet facenet = new FaceNet(getAssets());

                        Bitmap face1 = cropFace(image);
                        Bitmap face2 = cropFace(image2);

                        if (face1 != null)
                            runOnUiThread(() -> imageView.setImageBitmap(face1));
                        else
                            Log.i("detect", "Couldn't crop image 1.");

                        if (face2 != null)
                            runOnUiThread(() -> imageView2.setImageBitmap(face2));
                        else
                            Log.i("detect", "Couldn't crop image 2.");

                        if (face1 != null && face2 != null) {
                            double score = facenet.getSimilarityScore(face1, face2);
                            Log.i("score", String.valueOf(score));
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Similarity score: " + score, Toast.LENGTH_LONG).show());
                        }

                        facenet.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
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
