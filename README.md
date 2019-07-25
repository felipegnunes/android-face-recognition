# FaceRecognition
An Android application for face recognition.

O objetivo original deste projeto é que ele sirva como base para que outros estudantes possam implantar seus modelos em aplicativos Android. Originalmente, queria-se que todas as redes utilizadas fossem implementadas através da nova API do TensorFlow para Android (TensorFlow Lite). Infelizmente, eu não consegui fazer funcionar a MTCNN com a nova API (redes com entradas e saídas de tamanho variável são mais difíceis de se fazer trabalhar). 

## Face Detection (MTCNN)
The face detection package was taken from [this repository](https://github.com/vcvycy/MTCNN4Android). It uses the MTCNN model for face detection and the model is runned by an old TensorFlow API for Android.

## Face Recognition (FaceNet)
The face recognition model was obtained through the instructions in [this repository](https://github.com/jiangxiluning/facenet_mtcnn_to_mobile). The model is executed using TensorFlow Lite.

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
    implementation 'org.tensorflow:tensorflow-android:+'  // Old API
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
