package com.magicfame.captionthis;

import android.graphics.Canvas;
import android.media.Image;
import android.util.Size;

import org.json.JSONArray;

import java.util.List;
import ai.fritz.poseestimationmodelfast.PoseEstimationOnDeviceModelFast;
import ai.fritz.vision.FritzVision;
import ai.fritz.vision.FritzVisionImage;
import ai.fritz.vision.FritzVisionOrientation;
import ai.fritz.vision.ImageRotation;
import ai.fritz.vision.poseestimation.FritzVisionPosePredictor;
import ai.fritz.vision.poseestimation.FritzVisionPoseResult;
import ai.fritz.vision.poseestimation.Keypoint;
import ai.fritz.vision.poseestimation.Pose;

import static java.lang.Math.sqrt;


public class CameraActivity extends LiveCameraActivity{
    FritzVisionPosePredictor predictor;
    FritzVisionImage visionImage;
    FritzVisionPoseResult poseResult;
    @Override
    protected void setupPredictor() {
        // STEP 1: Get the predictor and set the options.
        // ----------------------------------------------
        // A FritzOnDeviceModel object is available when a model has been
        // successfully downloaded and included with the app.
        PoseEstimationOnDeviceModelFast poseEstimationOnDeviceModel = new PoseEstimationOnDeviceModelFast();
        predictor = FritzVision.PoseEstimation.getPredictor(poseEstimationOnDeviceModel);

        // ----------------------------------------------
        // END STEP 1
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
        // ----------------------------------
        // END STEP 4
    }

    protected void traitementPoint(int type, Pose pose) {
        if(type == 1){
            Keypoint epauleGauche = null, epauleDroite = null, poignetGauche = null, poignetDroit = null;
            Keypoint[] key = pose.getKeypoints();
            for (Keypoint var : key) {
                if (var.getPartName().compareTo("leftShoulder") == 0) epauleGauche = var;
                if (var.getPartName().compareTo("rightShoulder") == 0) epauleDroite = var;
                if (var.getPartName().compareTo("leftWrist") == 0) poignetGauche = var;
                if (var.getPartName().compareTo("rightWrist") == 0) poignetDroit = var;
                // System.out.println(var.calculateSquaredDistanceFromCoordinates();

                if (epauleDroite != null
                        && epauleDroite.getScore() > 0.7
                        && epauleGauche != null
                        && epauleGauche.getScore() > 0.7
                        && poignetDroit != null
                        && poignetDroit.getScore() > 0.7
                        && poignetGauche != null
                        && poignetGauche.getScore() > 0.7) {
                    System.out.println(epauleDroite.getScore());
                    System.out.println(poignetDroit.getScore());
                    System.out.println(epauleGauche.getScore());
                    System.out.println(poignetGauche.getScore());
                    System.out.println("CALCULATION : droite " + sqrt(epauleDroite.calculateSquaredDistanceFromCoordinates(poignetDroit.getPosition())));
                    System.out.println("CALCULATION : gauche " + sqrt(epauleGauche.calculateSquaredDistanceFromCoordinates(poignetGauche.getPosition())));
                    finish();
                }
            }
        }

    }
}
