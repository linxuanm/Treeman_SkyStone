package org.firstinspires.ftc.teamcode.subsystem.video;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

public class StoneDetection {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    private static final String VUFORIA_KEY =
            "AegblNT/////AAABmQbC4gavNk/2oo4dxCeTz2iGYV+lkOtnpdmw2WqsQ5CjQ0t6bRbHhpWDKJdkCBBg3B1Lt/JXq+Na0bEv0VAFZwwnG3QK0RAmXEnHlSzjRxeb2c7PGaUQfwXIerK6R8Wc9HQfkR4Pgrw/9Ux0/QemHNU0nogkiIoxY2aLsYqsFBtUUnXVq6QxQ4MQ5ITOXI4dz+r/A1304CIrDAVEqemBGsu21xBdVJWFWuct9v1LQ5EAbHyX8nnf6uF2ybqHL8sJayYcfbdk5ZaDDUx/O/kFLJHTF7wTkAHLCnMvOndHFqoLpc7SUD0zliqGP/sI5t1hlxHl9ntQQDTlefoRE+eItIogKXNbTUMawanzTbKZYzSd";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public void init(HardwareMap hardwareMap){
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }


    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod(HardwareMap hardwareMap) {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public void start(HardwareMap hardwareMap){
        init(hardwareMap);

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod(hardwareMap);
        } else {
            //telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        if (tfod != null) {
            tfod.activate();
        }
    }

    /***
     *
     * @return 返回识别到的所有对象，对象信息中包括Label、在图像中的位置、与摄像头的角度
     */
    public List<Recognition> detect(){
        List<Recognition> results = new ArrayList<>();
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                //telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    results.add(recognition);
//                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
//                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                            recognition.getLeft(), recognition.getTop());
//                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                            recognition.getRight(), recognition.getBottom());
                }
//                telemetry.update();
            }
        }
        return results;
    }

    /***
     *
     * @return 返回识别到的所有对象，对象信息中包括Label、在图像中的位置、与摄像头的角度
     */
    public List<Recognition> detectSkyStone(){
        List<Recognition> results = new ArrayList<>();
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                //telemetry.addData("# Object Detected", updatedRecognitions.size());

                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    if(recognition.getLabel().equals(LABEL_SECOND_ELEMENT))
                        results.add(recognition);
//                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
//                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                            recognition.getLeft(), recognition.getTop());
//                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                            recognition.getRight(), recognition.getBottom());
                }
                //results = updatedRecognitions;
//                telemetry.update();
            }
        }
        return results;
    }

    public void stop(){
        if (tfod != null) {
            tfod.shutdown();
        }
    }
}
