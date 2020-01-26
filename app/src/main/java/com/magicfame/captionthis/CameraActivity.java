package com.magicfame.captionthis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.Image;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Size;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

import ai.fritz.vision.FritzVision;
import ai.fritz.vision.FritzVisionImage;
import ai.fritz.vision.FritzVisionModels;
import ai.fritz.vision.FritzVisionOrientation;
import ai.fritz.vision.ImageRotation;
import ai.fritz.vision.ModelVariant;
import ai.fritz.vision.filter.OneEuroFilterMethod;
import ai.fritz.vision.poseestimation.FritzVisionPosePredictor;
import ai.fritz.vision.poseestimation.FritzVisionPosePredictorOptions;
import ai.fritz.vision.poseestimation.FritzVisionPoseResult;
import ai.fritz.vision.poseestimation.Keypoint;
import ai.fritz.vision.poseestimation.Pose;
import ai.fritz.vision.poseestimation.PoseOnDeviceModel;

import static java.lang.Math.sqrt;


public class CameraActivity extends LiveCameraActivity{
    FritzVisionPosePredictor predictor;
    FritzVisionImage visionImage;
    FritzVisionPoseResult poseResult;
    TextToSpeech textToSpeech;
    int stepOfExercice = 0;
    @Override
    protected void setupPredictor() {
        // STEP 1: Get the predictor and set the options.
        // ----------------------------------------------
        // A FritzOnDeviceModel object is available when a model has been
        // successfully downloaded and included with the app.
        PoseOnDeviceModel onDeviceModel= FritzVisionModels.getHumanPoseEstimationOnDeviceModel(ModelVariant.ACCURATE);
        FritzVisionPosePredictorOptions posePredictorOptions = new FritzVisionPosePredictorOptions();
        posePredictorOptions.smoothingOptions = new OneEuroFilterMethod();
        predictor = FritzVision.PoseEstimation.getPredictor(onDeviceModel, posePredictorOptions);
        // ----------------------------------------------
        // END STEP 1
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.FRANCE);
                    String toSpeak = "Veuillez vous placer devant la caméra";
                    textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        textToSpeech.shutdown();
    }

    @Override
    protected void setupImageForPrediction(Image image) {
        // Set the rotation
        ImageRotation imageRotation = FritzVisionOrientation.getImageRotationFromCamera(this, cameraId);
        // STEP 2: Create the FritzVisionImage object from media.Image
        // ------------------------------------------------------------------------
        visionImage = FritzVisionImage.fromMediaImage(image, imageRotation);
        // ------------------------------------------------------------------------
        // END STEP 2
    }

    @Override
    protected void runInference() {
        // STEP 3: Run predict on the image
        // ---------------------------------------------------
        poseResult = predictor.predict(visionImage);
        // ----------------------------------------------------
        // END STEP 3
    }

    @Override
    protected void showResult(Canvas canvas, Size cameraSize) {
        // STEP 4: Draw the prediction result
        // ----------------------------------
        if (poseResult != null) {
            List<Pose> poses = poseResult.getPoses();
            for (Pose pose : poses) {
                pose.draw(canvas);
                // Traitement sur les points
                traitementPoint(getType(), pose);
            }
        }
    }

    protected void traitementPoint(int type, Pose pose) {
        if(type == 1){
           calibration(pose);
        } else if(type == 2){
            pushUp(pose);
        }

    }

    protected void calibration (Pose pose) {
        Keypoint leftShoulder = null,
                rightShoulder = null,
                poignetGauche = null,
                poignetDroit = null,
                nez = null,
                piedGauche = null,
                piedDroit = null;
        Keypoint[] key = pose.getKeypoints();
        for (Keypoint var : key) {
            if (var.getName().compareTo("leftShoulder") == 0) leftShoulder = var;
            if (var.getName().compareTo("rightShoulder") == 0) rightShoulder = var;
            if (var.getName().compareTo("leftWrist") == 0) poignetGauche = var;
            if (var.getName().compareTo("rightWrist") == 0) poignetDroit = var;

            if (var.getName().compareTo("nose") == 0) nez = var;
            if (var.getName().compareTo("leftAnkle") == 0) piedGauche = var;
            if (var.getName().compareTo("rightAnkle") == 0) piedDroit = var;
        }

        if(nez != null && rightShoulder != null && leftShoulder != null && poignetDroit != null && poignetGauche != null && piedDroit != null && piedGauche != null) {
            System.out.println("Score : (" + nez.getScore() + rightShoulder.getScore() +
                    leftShoulder.getScore() + poignetDroit.getScore() + poignetGauche.getScore()
                    + piedDroit.getScore() + piedGauche.getScore() + ")");
        }
        // ET CALCULER LE RATIO
        if (isDefineAndGoodScore(rightShoulder,0.9)
                && isDefineAndGoodScore(leftShoulder ,0.9)
                && isDefineAndGoodScore(poignetDroit, 0.9)
                && isDefineAndGoodScore(poignetGauche, 0.9)
                && isDefineAndGoodScore(nez, 0.9)
                && isDefineAndGoodScore(piedGauche,0.5)
                && isDefineAndGoodScore(piedDroit, 0.5)) {
            // Moyenne taille en pixel
            int taillePixel = (int)
                    ((sqrt(nez.calculateSquaredDistanceFromCoordinates(piedDroit.getPosition()))
                            + sqrt(nez.calculateSquaredDistanceFromCoordinates(piedGauche.getPosition()))) / 2);
            int droit = (int)(sqrt(rightShoulder.calculateSquaredDistanceFromCoordinates(poignetDroit.getPosition())));
            int gauche = (int)(sqrt(leftShoulder.calculateSquaredDistanceFromCoordinates(poignetGauche.getPosition())));
            alertCalibration(droit, gauche, taillePixel);
        }
    }

    protected void alertCalibration(final int droit, final int gauche, final int taillePixel) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Votre taille réelle");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder1.setView(input);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int tailleBrasReel = ((droit + gauche) / 2 * Integer.parseInt(input.getText().toString()) / taillePixel);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("tailleBrasReel", tailleBrasReel);
                        returnIntent.putExtra("tailleCorpsReel", Integer.parseInt(input.getText().toString()));
                        setResult(CameraActivity.RESULT_OK, returnIntent);
                        dialog.cancel();
                        finish();
                    }
                });
        builder1.show();
    }

    protected void pushUp(Pose pose){
        if(stepOfExercice == 0){
            String toSpeak = "Bievenue sur l'exercice de pompes";
            textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            stepOfExercice = 1;
        }

        Keypoint leftShoulder = null,
                rightShoulder = null,
                coudeDroit = null,
                coudeGauche = null,
                poignetGauche = null,
                poignetDroit = null,
                hancheGauche = null,
                hancheDroite = null,
                genouGauche = null,
                genouDroit = null,
                piedGauche = null,
                piedDroit = null;
        Keypoint[] key = pose.getKeypoints();
        for (Keypoint var : key) {
            if (var.getName().compareTo("leftShoulder") == 0) leftShoulder = var;
            if (var.getName().compareTo("rightShoulder") == 0) rightShoulder = var;

            if (var.getName().compareTo("rightElbow") == 0) coudeDroit = var;
            if (var.getName().compareTo("leftElbow") == 0) coudeGauche = var;

            if (var.getName().compareTo("leftWrist") == 0) poignetGauche = var;
            if (var.getName().compareTo("rightWrist") == 0) poignetDroit = var;

            if (var.getName().compareTo("leftHip") == 0) hancheGauche = var;
            if (var.getName().compareTo("rightHip") == 0) hancheDroite = var;

            if (var.getName().compareTo("leftKnee") == 0) genouGauche = var;
            if (var.getName().compareTo("rightKnee") == 0) genouDroit = var;

            if (var.getName().compareTo("leftAnkle") == 0) piedGauche = var;
            if (var.getName().compareTo("rightAnkle") == 0) piedDroit = var;
        }

        // Membre de départs
        if(stepOfExercice == 1){
            if(isDefineAndGoodScore(leftShoulder, 0.8) &&
                    isDefineAndGoodScore(coudeGauche, 0.8) &&
                    isDefineAndGoodScore(poignetGauche, 0.8)&&
                    isDefineAndGoodScore(rightShoulder, 0.8) &&
                    isDefineAndGoodScore(coudeDroit, 0.8) &&
                    isDefineAndGoodScore(poignetDroit, 0.8)
                   ){
                String toSpeak = "Prenez la position de départ";
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                stepOfExercice = 2;
            }
        }

    }

    public boolean isDefineAndGoodScore(Keypoint key, double score){
        if(key != null && key.getScore() > score){
            return true;
        } else {
            return false;
        }
    }
}
