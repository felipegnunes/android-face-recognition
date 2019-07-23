# FaceRecognition
An Android application for face recognition.

O objetivo original deste projeto é que ele sirva como base para que outros estudantes possam implantar seus modelos em aplicativos Android. Originalmente, queria-se que todas as redes utilizadas fossem implementadas através da nova API do TensorFlow para Android (TensorFlow Lite). Infelizmente, eu não consegui fazer funcionar a MTCNN com a nova API (redes com entradas e saídas de tamanho variável são mais difíceis de se fazer trabalhar). 

## Face Detection (MTCNN)
Para esse propósito foi usada a seguinte API: https://github.com/vcvycy/MTCNN4Android, que utiliza o MTCNN através de uma API antiga de Tensorflow para Android (TensorFlowInferenceInterface).

## Face Recognition (FaceNet)
O reconhecimento facial foi feito usando um modelo convertido através das instruções deste repositório: https://github.com/jiangxiluning/facenet_mtcnn_to_mobile. Em posse do modelo no formato .tflite, foi usada a nova API (TensorFlow Lite) para execução das redes

## Instruções de Uso
Se você desejar usar os códigos deste projeto na sua aplicação, deve ter algumas coisas em mente.

- Copiar os modelos (mtcnn_freezed_model.pb e facenet.tflite) para sua pasta "assets".
- Adicionar o seguinte código ao seu "build.gradle".
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
- Copiar os pacotes FaceRecognition e FaceDetection.
- Para calcular o score de similaridade de duas faces através da FaceNet:
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
