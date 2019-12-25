package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;


/***
 * 卡尔曼
 *
 *
 */
public class SensorFilterKalman {

    private long timeStamp; // millis
    private double y; // degree
    private double x; // degree
    private float variance; // P matrix. Initial estimate of error

    public SensorFilterKalman(){
        init();
    }

    public SensorFilterKalman(int avgCount){
        init();
    }

    public void init(){
        variance = -1;
    }

    public double input(double[] value){

        return 0;
    }

    public double getValue(){
        return 0;
    }

    public void process(float newSpeed, double newY, double newX, long newTimeStamp, float newAccuracy) {

        if (variance < 0) {
            // if variance < 0, object is unitialised, so initialise with current values
            setState(newY, newX, newTimeStamp, newAccuracy);
            return;
        }
        // else apply Kalman filter
        long duration = newTimeStamp - this.timeStamp;
        if (duration > 0) {
            // time has moved on, so the uncertainty in the current position increases
            variance += duration * newSpeed * newSpeed / 1000;
            timeStamp = newTimeStamp;
        }

        // Kalman gain matrix 'k' = Covariance * Inverse(Covariance + MeasurementVariance)
        // because 'k' is dimensionless,
        // it doesn't matter that variance has different units to y and x
        float k = variance / (variance + newAccuracy * newAccuracy);
        // apply 'k'
        y += k * (newY - y);
        x += k * (newX - x);
        // new Covariance matrix is (IdentityMatrix - k) * Covariance
        variance = (1 - k) * variance;

    }
    // Init method (use this after constructor, and before process)
    // if you are using last known data from gps)
    public void setState(double y, double x, long timeStamp, float accuracy) {
        this.y = y;
        this.x = x;
        this.timeStamp = timeStamp;
        this.variance = accuracy * accuracy;
    }

}
