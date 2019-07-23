# FaceRecognition
An Android application for face recognition.

O objetivo original deste projeto é que ele servisse como base para que outros estudantes possam implantar seus modelos em aplicativos Android. Originalmente, queria-se que tanta a rede de detecção facial (MTCNN) como a de reconhecimento facial (FaceNet) fossem implementadas através da nova API do TensorFlow para Android (TensorFlow Lite). Infelizmente, eu não consegui fazer funcionar a MTCNN com a nova API (problemas com redes com entradas e saídas de tamanho variável). 

## Face Detection (MTCNN)
Portanto, para esse propósito foi usada a seguinte API: https://github.com/vcvycy/MTCNN4Android, que utiliza o MTCNN através de uma API antiga de Tensorflow para Android (TensorFlowInferenceInterface).

## Face Recognition (FaceNet)
O reconhecimento facial foi feito usando um modelo convertido através das instruções deste repositório: https://github.com/jiangxiluning/facenet_mtcnn_to_mobile. Em posse do modelo no formato .tflite, foi usada a nova API (TensorFlow Lite) para execução das redes

## Uso
Se você desejar copiar os códigos deste projeto para sua aplicação, ou usá-la como base para a execução de outras redes no smartphone, deve ter algumas coisas em mente.
- Você deve copiar os modelos para sua pasta "assets".
- Adicionar as coisas no "build.gradle". 
- Coisa 3
