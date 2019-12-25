/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.subsystem.StoneClawSystem;
import org.firstinspires.ftc.teamcode.subsystem.FoundationClawSystem;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSystem;
import org.firstinspires.ftc.teamcode.subsystem.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystem.sensors.IMUSystem;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Auto Test", group = "Test")
@Disabled
public class
Ftc2020Auto extends LinearOpMode {
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
        if(gamepad1.a){
            debug = true;
        }
        runtime.reset();

        tmpTimer.reset();

        detectTimer.reset();


        while (opModeIsActive()) {
            currentAngle = imu.getPitch();

            switch (startPosition) {
                case STATUS_BB_START:
                    blueBuilding();
                    break;
                case STATUS_BL_START:
                    blueDepotRed();
                    break;
                case STATUS_RB_START:
                    redBuilding();
                    break;
                case STATUS_RL_START:
                    redDepotBlue();
                    break;
                case STATUS_DEBUG_START:
                    debugLoop();
                    break;
            }

            stoneClaw.loop();
            md.loop();

            // Show the elapsed game time and wheel power.
            telemetry.addData("debug", ": " + debug);
            telemetry.addData("startPosition", ": " + startPosition);
            telemetry.addData("runStatus", ": " + runStatus);
            telemetry.addData("armstatus", ": " + stoneClaw.status);
            telemetry.addData("time", ": " + tmpTimer.milliseconds());
            telemetry.update();
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
        stoneClaw.init(c);
        stoneClaw.init(c,arm);
        stoneClaw.open();
        stoneClaw.armStop();

    }

    //蓝方建造区起始的机器人运动状态BLUE_BUILDING
    public void blueBuilding() {
        switch (runStatus) {
//            case 1000:
//                taskDrift(-0.7,800L,1001);
//                break;
//            case 1001:
//                stopAll();
//                taskIdle(10000L,1002);
//                break;
//            case 1002:
//                taskDrift(-0.7,800L,1999);
//                break;
            case 1000:
                taskGo(0.7,500L,10001);
                break;
            case 10001:
                taskDrift(0.7,750L,1001);
                break;
            case 1001:
                foundationClaw.open();
                taskTurn(-0.7,1600L,1002);
                break;
            case 1002:
                taskGo(-0.3,1000L,1003);
                break;
            case 1003:
                md.stop();
                foundationClaw.close();
                taskIdle(1000L,10031);
                break;
            case 10031:
                taskTurn(0.7,200L,1004);
                break;
            case 1004:
                taskGo(1,500L,1005);
                break;
            case 1005:
                taskTurn(0.7,1400L,10051);
                break;
            case 10051:
                taskGo(-1,500L,1006);
                break;
            case 1006:
                foundationClaw.open();
                taskIdle(500L,1007);
                break;
            case 1007:
                taskDrift(-0.5,1500L,10071);
                break;
            case 10071:
                taskDrift(0.5,200L,1008);
                break;
            case 1008:
                taskGo(0.7,1500L,1999);
                break;
            case 1999:
                md.stop();
                break;
        }
    }

    Recognition targetSkyStone = null;
    int stonePos = 0;

    //蓝方搬砖区起始的机器人运动状态BLUE_LOADING
    public void blueDepotRed() {
        /*
         * 主要流程：
         * 开摄像头检查SkyStone位置
         * 前移靠近Stone列
         * 转45度
         * 平移推掉非SkyStone块
         * 转身侧向行驶吸Stone
         * 向SkyBridge所在墙平移（根据图像检测定位），同时控制机械臂抓住SkyStone
         * 向前运动穿过SkyBridge，同时移动滑轨准备释放SkyStone
         * 完成穿越后，移动滑轨释放SkyStone
         * 收回滑轨，返回LoadingZone
         * 再次识别SkyStone位置
         * */
        switch (runStatus) {
            case 2000:
                taskGo(0.7,1300L, 2001);
                break;
            case 2001:
                taskTurn(-0.7,800L,2002);
                break;
            case 2002:
                runStatus = 2003;
                intake.intake();
                break;
            case 2003:
                taskGo(0.7,450L,20031);
                break;
            case 20031:
                taskGo(-0.7,400L,2005);
                break;
            case 2005:
                taskTurn(0.7,840L,2006);
                break;
            case 2006:
                taskGo(-0.7,430L,2007);
                break;
            case 2007:
                taskDrift(0.7,2100L,2008);
                break;
            case 2008:
                intake.outPut();
                runStatus = 2009;
                break;
            case 2009:
                taskDrift(0.7,1200L,2010);
                break;
            case 2010:
                taskTurn(0.7,1500L,2011);
                break;
            case 2011:
                foundationClaw.open();
                taskGo(-0.6,700L,2012);
                break;
            case 2012:
                md.stop();
                foundationClaw.close();
                runStatus = 2013;
                break;
            case 2013:
                taskIdle(1000L,2014);
                break;
            case 2014:
                taskGo(0.6,1800L,2015);
                break;
            case 2015:
                taskGo(-0.7,300L,2016);
                break;
            case 2016:
                foundationClaw.open();
                taskDrift(0.7,1500L,2017);
                break;
            case 2017:
                taskGo(-0.7,750L,2018);
                break;
            case 2018:
                taskDrift(0.7,900L,2900);
                break;


//            case 2009:
//                taskDrift(-0.7,1800L,20095);
//                break;
//            case 20095:
//                intake.intake();
//                runStatus = 2010;
//                break;
//            case 2010:
//                taskGo(0.6,600L,2011);
//                break;
//            case 2011:
//                taskTurn(-0.7, 850L, 2012);
//                break;
//            case 2012:
//                taskGo(0.7,800L,2013);
//                break;
//            case 2013:
//                taskDrift(-0.7,800L,2014);
//                break;
//            case 2014:
//                taskTurn(0.7,740L,2015);
//                break;
//            case 2015:
//                taskDrift(0.7,2600L,2016);
//                break;
//            case 2016:
//                intake.outPut();
//                runStatus = 2017;
//                break;
//            case 2017:
//                taskDrift(-0.7,700L,2900);
//                break;

            case 2900:
                md.stop();
                break;


        }
    }

    //红方建造区起始的机器人运动状态RED_BUILDING
    public void redBuilding() {
        switch (runStatus) {
            case 3000:
                taskGo(0.7,500L,30001);
                break;
            case 30001:
                taskDrift(-0.7,750L,3001);
                break;
            case 3001:
                foundationClaw.open();
                taskTurn(0.7,1600L,3002);
                break;
            case 3002:
                taskGo(-0.3,1000L,3003);
                break;
            case 3003:
                md.stop();
                foundationClaw.close();
                taskIdle(1000L,30031);
                break;
            case 30031:
                taskTurn(-0.7,200L,3004);
                break;
            case 3004:
                taskGo(1,500L,3005);
                break;
            case 3005:
                taskTurn(-0.7,1400L,30051);
                break;
            case 30051:
                taskGo(-1,500L,3006);
                break;
            case 3006:
                foundationClaw.open();
                taskIdle(500L,3007);
                break;
            case 3007:
                taskDrift(0.5,1500L,30071);
                break;
            case 30071:
                taskDrift(-0.5,200L,3008);
                break;
            case 3008:
                taskGo(0.7,1500L,3999);
                break;
            default:
                stopAll();
                break;
        }
    }

    //红方搬砖区起始的机器人运动状态RED_LOADING
    public void redDepotBlue() {
        switch (runStatus) {
            case 4000:
                taskGo(0.7,1400L, 4001);
                break;
            case 4001:
                taskTurn(0.7,800L,4002);
                break;
            case 4002:
                runStatus = 4003;
                intake.intake();
                break;
            case 4003:
                taskGo(0.7,400L,40031);
                break;
            case 40031:
                taskGo(-0.7,350L,4005);
                break;
            case 4005:
                taskTurn(-0.7,890L,4006);
                break;
            case 4006:
                taskGo(-0.7,520L,4007);
                break;
            case 4007:
                taskDrift(-0.7,2200L,4008);
                break;
            case 4008:
                intake.outPut();
                runStatus = 4009;
                break;
            case 4009:
                taskDrift(0.7,1900L,40095);
                break;
            case 40095:
                intake.intake();
                runStatus = 4010;
                break;
            case 4010:
                taskGo(0.6,650L,4011);
                break;
            case 4011:
                taskTurn(0.7, 820L, 4012);
                break;
            case 4012:
                taskGo(0.7,900L,4013);
                break;
            case 4013:
                taskDrift(0.7,700L,4014);
                break;
            case 4014:
                taskTurn(-0.7,700L,4015);
                break;
            case 4015:
                taskDrift(-0.7,2500L,4016);
                break;
            case 4016:
                intake.outPut();
                runStatus = 4017;
                break;
            case 4017:
                taskDrift(0.7,500L,4900);
                break;

//            case 4010:
//                taskTurn(0.7,400L,4011);
//                break;
//            case 4011:
//                taskGo(0.7,900L,4012);
//                break;
//            case 4012:
//                taskTurn(0.7,200L,4013);
//                break;
//            case 4013:
//                taskGo(0.7,400L,4900);
//                break;

//            case 4007:
//                taskTurn(-0.7,900L,4008);
//                break;
//            case 4008:
//                taskGo(0.8,1800L,4009);
//                break;
//            case 4009:
//                runStatus = 4900;
//                intake.outPut();
//                break;



//            case 4000:
//                //skystong在最靠近bridge的位置
//                //checkStonePosition();
//
//                runStatus = 4001;
//            case 4001://前进一点点距离，靠近stone列
//                taskGo(0.7,300L,4002);
//                break;
//            case 4002://转到45度角
//                taskTurn(0.7,500L,4003);
//                break;
//            case 4003://向右平移到可以吸stone的位置
//                taskDrift(-0.7,1350L,40041);
//                break;
//            case 40041:
//                intake.intake();
//                taskGo(0.4,700L,40042);
//                break;
//            case 40042://前进，吸stone
//                intake.intake();
//                taskIdle(350L,4005);
//                break;
//            case 4005://后退
//                //stoneClaw.close();
//                taskGo(-0.7,1300L,40051);
//                break;
//            case 40051:
//                taskTurn(0.7, 500L,40052);
//                break;
//            case 40052:
//                taskGo(-0.7,800L,40053);
//                break;
//            case 40053:
//                taskTurn(-0.7,1000L,40054);
//            case 40054:
//                intake.outPut();
////                runStatus = 40054;
//                taskIdle(100L,400541);
//                break;
//            case 400541:
//                taskTurn(0.7,1000L,400542);
//                break;
//            case 400542:
//                taskGo(0.7,500L,4900);
//                break;
//            case 40055:
//                taskGo(-0.7,1500L,40056);
////                stopAll();
////                stop();
//                break;
//            case 40056:
//                taskTurn(-0.7,1000L,40057);
//                break;
//            case 40057:
//                intake.intake();
//                runStatus = 40058;
//                break;
//            case 40058:
//                taskGo(0.7,500L,40059);
//                break;
//            case 40059:
//                taskIdle(300L,4006);
//                break;
//            case 4006:
//                taskGo(-0.7,700L,4007);
//                break;
//            case 4007:
//                taskTurn(0.7,1000L,4008);
//                break;
//            case 4008:
//                taskGo(0.7,500L,4009);
//                break;
//            case 4009:
//                intake.outPut();
//                runStatus = 4010;
//                break;
//            case 4010:
//                taskGo(-0.7,500L,4900);
//                break;
            case 4900:
                md.stop();
                break;
        }

    }

    int statusDebug = 0;     // test

    public void debugLoop() {

        switch (runStatus) {
            case 0:
                //0 测试直行
                md.go(0.3, 0, currentAngle);
                if (timeElapse(1000L)) {
                    runStatus = 1;
                }
                break;
            case 1:
                //1 测试旋转
                if (md.turn(0.3, 150, currentAngle)) {
                    runStatus = 2;
                    tmpTimer.reset();
                }
                break;
            case 2:
                //2 测试平移
                md.drift(0.6, 90, currentAngle);
                if (timeElapse(1000L)) {
                    md.stop();
                    runStatus = 3;
                }
                break;
            case 3:
                //3 测试收块
                intake.intake();
                if (timeElapse(1000L)) {
                    intake.stop();
                    runStatus = 0;
                }

                break;

        }

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

    private void checkStonePosition() {
//        int c = stones.size();
//        stonePos = 0;
//        if (stones.size() > 0 && stones.get(c - 1).getLabel().equals("Skystone")) {
//            stonePos = 0;
//        } else if (stones.size() > 1 && stones.get(c - 2).getLabel().equals("Skystone")) {
//            stonePos = 1;
//        } else if (stones.size() > 2 && stones.get(c - 3).getLabel().equals("Skystone")) {
//            stonePos = 2;
//        }

    }

}
