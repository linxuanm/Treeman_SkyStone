package org.firstinspires.ftc.teamcode.subsystem.video;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public class VuforiaFactory {

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false  ;

    private static final String VUFORIA_KEY =
            "AQB7nG7/////AAABmUex7Lm4sUbnqLGal8GC7a9R24fv4VbeB8i2ZN/dCcWpdbQuvoeJNOB7hVkkjdhpqs47pOsKLSLP3Ovv0IruID8dkk+kV1lnYmOuJu3x1j6+YAU0PDxzUi691zIQgSzz84I35SrRnrCbqII864bHCqXJyH5zWxU2lccFQnJA6drKWHppTWsgLSTKNf0Fv8hdHu+XLSOXD5Is6IX4SHk0KLULtvjCmjcR/XydYPb1mR2mVyi48H2K8BXug07KIxOs+pYRbKajUe0MHAjFPUpoRTSLHA/f6dn1bFWWyjYgDPj4UXZbbpCDVKSIAkhx7JXa5FXq/uSpBH5rkXIdH5Dzwy7OzroTpqejVYPjPpOE0emg";
    public static VuforiaLocalizer vuforia = null;

    public static VuforiaLocalizer defaultInstance(HardwareMap hardwareMap){
        if(vuforia == null)
            init(hardwareMap);
        return vuforia;
    }


    /**
     * Initialize the Vuforia localization engine.
     */
    public static void init(HardwareMap hardwareMap){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        CameraName c = hardwareMap.get(WebcamName.class, "Webcam 1");
        if(c!=null)
            parameters.cameraName = c;
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //parameters.cameraDirection   = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

}
