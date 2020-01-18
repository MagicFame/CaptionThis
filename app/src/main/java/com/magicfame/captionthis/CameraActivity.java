package com.magicfame.captionthis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.media.Image;
import android.text.InputType;
import android.util.Size;
import android.widget.EditText;

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
            Keypoint epauleGauche = null,
                    epauleDroite = null,
                    poignetGauche = null,
                    poignetDroit = null,
                    nez = null,
                    piedGauche = null,
                    piedDroit = null;
            Keypoint[] key = pose.getKeypoints();
            for (Keypoint var : key) {
                if (var.getPartName().compareTo("leftShoulder") == 0) epauleGauche = var;
                if (var.getPartName().compareTo("rightShoulder") == 0) epauleDroite = var;
                if (var.getPartName().compareTo("leftWrist") == 0) poignetGauche = var;
                if (var.getPartName().compareTo("rightWrist") == 0) poignetDroit = var;

                if (var.getPartName().compareTo("nose") == 0) nez = var;
                if (var.getPartName().compareTo("leftAnkle ") == 0) piedGauche = var;
                if (var.getPartName().compareTo("rightAnkle") == 0) piedDroit = var;

                // ET CALCULER LE RATIO
                if (epauleDroite != null
                        && epauleDroite.getScore() > 0.8
                        && epauleGauche != null
                        && epauleGauche.getScore() > 0.8
                        && poignetDroit != null
                        && poignetDroit.getScore() > 0.8
                        && poignetGauche != null
                        && poignetGauche.getScore() > 0.8
                        && nez != null
                        && nez.getScore() > 0.8
                        && piedGauche != null
                        && piedDroit != null
                        && piedDroit.getScore() > 0.6
                        && piedGauche.getScore() > 0.6) {
                    System.out.println("OK");
                    // Moyenne taille en pixel
                    int taillePixel = (int)
                            ((sqrt(nez.calculateSquaredDistanceFromCoordinates(piedDroit.getPosition()))
                            + sqrt(nez.calculateSquaredDistanceFromCoordinates(piedGauche.getPosition()))) / 2);

                    int droit = (int)(sqrt(epauleDroite.calculateSquaredDistanceFromCoordinates(poignetDroit.getPosition())));
                    int gauche = (int)(sqrt(epauleGauche.calculateSquaredDistanceFromCoordinates(poignetGauche.getPosition())));
                    alertData(droit, gauche);
                }
            }
        }

    }

    protected void alertData(int droit, int gauche) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Votre taille réelle");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder1.setView(input);
        //builder1.setMessage("Distance bras droite : " + droit + " et distance bras gauche : " + gauche);
        builder1.setCancelable(true);


        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder1.show();


    }
}
