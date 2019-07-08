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

    private Bitmap cropFace(Bitmap bitmap){
        Bitmap croppedBitmap = null;
        try {
            MTCNN mtcnn = new MTCNN(getAssets());
            Vector<Box> boxes = mtcnn.detectFaces(bitmap, 1);

            int left = boxes.get(0).left();
            int top = boxes.get(0).top();
            int width = boxes.get(0).width();
            int height = boxes.get(0).height();

            croppedBitmap = Bitmap.createBitmap(bitmap, top, left, width, height);
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
                    //testMTCNN(image);

                    FaceNet facenet = null;
                    try {

                        facenet = new FaceNet(getAssets());

                        /*float[][] embeddings = facenet.runFloat(image);
                        for (int i = 0; i < 512; i++){
                            Log.i("embeddings", String.valueOf(embeddings[0][i]));
                        }*/

                        Bitmap face1 = cropFace(image);
                        Bitmap face2 = cropFace(image2);

                        if (face1 != null && face2 != null) {
                            double score = facenet.getSimilarityScore(cropFace(image), cropFace(image2));
                            Log.i("score", String.valueOf(score));
                        }else{
                            if (face1 == null)
                                Log.i("score", "Couldn't crop image 1.");
                            if (face2 == null)
                                Log.i("score", "Couldn't crop image 2.");
                        }
                        facenet.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void testMTCNN(Bitmap bitmap){
        MTCNN mtcnn = new MTCNN(getAssets());
        try {
            Vector<Box> boxes = mtcnn.detectFaces(bitmap, 40);
            Log.i("MTCNN", String.valueOf(boxes.size()));
            for (int i = 0; i < boxes.size(); i++) {
                Log.i("MTCNN", String.valueOf(boxes.get(i).left()));
                Log.i("MTCNN", String.valueOf(boxes.get(i).top()));
                Log.i("MTCNN", String.valueOf(boxes.get(i).bottom()));
                Log.i("MTCNN", String.valueOf(boxes.get(i).right()));
                Log.i("MTCNN", "width: " + String.valueOf(boxes.get(i).width()));
                Log.i("MTCNN", "width: " + String.valueOf(boxes.get(i).width()));
            }

            int left = boxes.get(0).left();
            int top = boxes.get(0).top();
            int right = boxes.get(0).right();
            int bottom = boxes.get(0).bottom();
            int width = boxes.get(0).width();
            int height = boxes.get(0).height();

            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, top, left, width, height);

            imageView.setImageBitmap(croppedBitmap);


        }catch (Exception e){
            e.printStackTrace();
        }
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
