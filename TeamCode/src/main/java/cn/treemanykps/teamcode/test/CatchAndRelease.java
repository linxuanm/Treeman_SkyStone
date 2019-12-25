package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.subsystem.FoundationClawSystem;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSystem;
import org.firstinspires.ftc.teamcode.subsystem.LiftSystem;
import org.firstinspires.ftc.teamcode.subsystem.StoneClawSystem;
import org.firstinspires.ftc.teamcode.subsystem.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystem.sensors.IMUSystem;

@Autonomous(name = "Test: Catch and Release", group = "Autonomous")
//@Disabled

public class CatchAndRelease extends LinearOpMode{
    final static int STATUS_DEBUG_START = 0;     //蓝方建造区的起始位置BLUE_BUILDING
    final static int STATUS_BB_START = 1000;     //蓝方建造区的起始位置BLUE_BUILDING
    final static int STATUS_BL_START = 2000;     //蓝方搬砖区的起始位置BLUE_LOADING
    final static int STATUS_RB_START = 3000;     //红方建造区的起始位置RED_BUILDING
    final static int STATUS_RL_START = 4000;     //红方搬砖区的起始位置RED_LOADING

    final static String PARK_WALL = "wall";     //靠墙的位置
    final static String PARK_REAR = "center";   //靠中间的位置

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private ElapsedTime tmpTimer = new ElapsedTime();
    private ElapsedTime detectTimer = new ElapsedTime();
    //Mecanum底盘控制对象
    private MecanumDrive md;
    //用于吸Stone的对象
    private IntakeSystem intake;
    //用于获取内建IMU数据的对象
    private IMUSystem imu;
    //用于抓Foundation的2个伺服
    private FoundationClawSystem foundationClaw;

    private StoneClawSystem stoneClaw;

    private LiftSystem lift;

    boolean debug = false;
    private ElapsedTime debugTimer = new ElapsedTime();


    protected String parkPosition = PARK_WALL;
    protected boolean reposition = false;


    protected int startPosition = 0;
    protected int runStatus = 0;

    private double currentAngle = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        initRobot();

        waitForStart();

        runtime.reset();

        tmpTimer.reset();

        detectTimer.reset();

        while (opModeIsActive()) {

//            currentAngle = imu.getPitch();

            stoneClaw.armStretch();

//            // Catch
//
//            intake.intake();
//
//            sleep(1000);
//
//            lift.up();
//            sleep(1000);
//            lift.down();
//            sleep(1000);
//            lift.stop();
//            sleep(1000);
//            stoneClaw.close();
//            sleep(1000);
//
//            // Release
//
//            lift.up();
//            sleep(1000);
//            stoneClaw.armStretch();
//            sleep(1000);
//            stoneClaw.open();
//            sleep(100);
//            break;
        }
    }

    public void initRobot() {
        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "lf");
        DcMotor leftRearDrive = hardwareMap.get(DcMotor.class, "lr");
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "rf");
        DcMotor rightRearDrive = hardwareMap.get(DcMotor.class, "rr");

        DcMotor intakeLeft = hardwareMap.get(DcMotor.class, "inl");
        DcMotor intakeRight = hardwareMap.get(DcMotor.class, "inr");
        DcMotor arm = hardwareMap.get(DcMotor.class, "arm");
        DcMotor liftMotor = hardwareMap.get(DcMotor.class, "lift");
        lift = new LiftSystem();
        lift.init(liftMotor);

        md = new MecanumDrive();
        md.init(leftFrontDrive, leftRearDrive, rightFrontDrive, rightRearDrive);

        intake = new IntakeSystem();
        intake.init(intakeLeft, intakeRight);

        imu = new IMUSystem(hardwareMap.get(BNO055IMU.class, "imu"));
        imu.order = AxesOrder.YZX;

        Servo s1 = hardwareMap.get(Servo.class, "s1");
        Servo s2 = hardwareMap.get(Servo.class, "s2");

        foundationClaw = new FoundationClawSystem();
        foundationClaw.init(s1, s2);

        Servo c = hardwareMap.get(Servo.class, "c");
        stoneClaw = new StoneClawSystem();
        stoneClaw.init(c,arm);
        stoneClaw.open();
        stoneClaw.armStop();

    }

    void taskGo(double speed,Long timeOutMilSecs,int succeedStatus){
        md.go(speed);
        taskIdle(timeOutMilSecs,succeedStatus);

    }

    void taskDrift(double speed,Long timeOutMilSecs,int succeedStatus){
        md.drift(speed);
        taskIdle(timeOutMilSecs,succeedStatus);
    }

    void taskTurn(double speed,Long timeOutMilSecs,int succeedStatus){
        md.turn(speed);
        taskIdle(timeOutMilSecs,succeedStatus);
    }

    void taskIdle(Long timeOutMilSecs,int succeedStatus){
        if (timeElapse(timeOutMilSecs)) {
            runStatus = succeedStatus;
            debugWait();
        }
    }

    void debugWait(){
        if(!debug)
            return;

        while(opModeIsActive()){
            idle();
            if(gamepad1.a && debugTimer.milliseconds()>200){
                debugTimer.reset();
                break;
            }
        }
    }

    public boolean timeElapse(Long milSec) {
        return timeElapse(milSec, true);
    }

    public boolean timeElapse(Long milSec, boolean isReset) {
        boolean result = tmpTimer.milliseconds() > milSec;
        if (result && isReset) {
            tmpTimer.reset();
        }
        return result;
    }

    private void stopAll() {
        md.stop();
        stoneClaw.armStop();
    }
}
