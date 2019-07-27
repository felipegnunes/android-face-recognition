# FaceRecognition
This is an Android application for face recognition. The main purpose of this project is to serve as basis for others who want to deploy their deep learning models to an Android application.

## Face Detection
The face detection package was taken from [this repository](https://github.com/vcvycy/MTCNN4Android). It uses the MTCNN model for face detection and it is runned using an old TensorFlow API (TensorflowInferenceInterface) for Android.

## Face Recognition
The face recognition model used is FaceNet. It was obtained through the instructions in [this repository](https://github.com/jiangxiluning/facenet_mtcnn_to_mobile). The model is runned using the TensorFlow Lite API.

## Usage
If you want to use the code of this project in your own application, then follow the steps below:

- Copy the model files (mtcnn_freezed_model.pb e facenet.tflite) to your "assets" folder.
- Add the following code to "build.gradle":
```
android {
    aaptOptions {
        noCompress "tflite"  // Prevents compression of tflite files
        noCompress "lite"
    }
}

dependencies {
    implementation 'org.tensorflow:tensorflow-lite:0.0.0-nightly'  // Official API
    implementation 'org.tensorflow:tensorflow-android:+'  // Old API (TensorflowInferenceInterface)
}
```
- Copy the packages FaceRecognition and FaceDetection.
- To calculate the similarity score between two faces do as follows:
```java

FaceNet facenet = new FaceNet(getAssets());

// cropFace returns null if no face could be detected in the provided image
Bitmap face1 = cropFace(bitmap1);
Bitmap face2 = cropFace(bitmap2);

if (face1 != null && face2 != null) {  // To make sure both faces were detected successfully
    double score = facenet.getSimilarityScore(face1, face2);  // Euclidian distance between the face descriptor vectors
}

facenet.close();
```
