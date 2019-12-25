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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.subsystem.FoundationClawSystem;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSystem;
import org.firstinspires.ftc.teamcode.subsystem.LiftSystem;
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

@TeleOp(name = "8515 Manual", group = "Manual")
//@Disabled
public class ManualTreeman extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private MecanumDrive md;
    private IntakeSystem intake;
    private IMUSystem imu;
    private LiftSystem liftSystem;
    private FoundationClawSystem fClaw;
    private Servo s1 = null;
    private Servo s2 = null;
    private Servo claw = null;
    double currentAngle = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ElapsedTime timer = new ElapsedTime();

        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "lf");
        DcMotor leftRearDrive = hardwareMap.get(DcMotor.class, "lr");
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "rf");
        DcMotor rightRearDrive = hardwareMap.get(DcMotor.class, "rr");

        DcMotor intakeLeft = hardwareMap.get(DcMotor.class, "inl");
        DcMotor intakeRight = hardwareMap.get(DcMotor.class, "inr");
        DcMotor arm = hardwareMap.get(DcMotor.class, "arm");
        DcMotor liftMotor = hardwareMap.get(DcMotor.class, "lift");

        s1 = hardwareMap.get(Servo.class, "s1");
        s2 = hardwareMap.get(Servo.class, "s2");
        claw = hardwareMap.get(Servo.class, "c");


        imu = new IMUSystem(hardwareMap.get(BNO055IMU.class, "imu"));
        imu.order = AxesOrder.YZX;

        md = new MecanumDrive();
        md.init(leftFrontDrive, leftRearDrive, rightFrontDrive, rightRearDrive);
        intake = new IntakeSystem();
        intake.init(intakeLeft, intakeRight);
        liftSystem = new LiftSystem();
        liftSystem.init(liftMotor);

        fClaw = new FoundationClawSystem();
        fClaw.init(s1, s2);

        waitForStart();
        runtime.reset();

        timer.reset();
        double time = 0.0;
        double clawTime = 0.0;

        double clawPosition = 0.0;
        double clawPosOpen = 0.55;
        //double clawPosClose = 0.95;
        double clawPosClose = 1;

        while (opModeIsActive()) {
            currentAngle = imu.getHeading();
            // Setup a variable for each drive wheel to save power level for telemetry

            double drive = -gamepad1.left_stick_y;
            double slide_left = gamepad1.left_trigger;
            double slide_right = gamepad1.right_trigger;
            double turn = 0.65 * -gamepad1.right_stick_x;
            boolean ingest = gamepad2.left_bumper;
            boolean egest = gamepad2.right_bumper;

            boolean liftUp = -gamepad2.left_stick_y > 0;
            boolean liftDown = -gamepad2.left_stick_y < 0;

            boolean foundationClaw_a = gamepad2.a;
            boolean foundationClaw_b = gamepad2.b;
//            boolean clawOpen = gamepad2.x;
//            boolean clawClose = gamepad2.y;
            boolean clawChange = gamepad2.x;
            double armPower = gamepad2.right_stick_y;

            double slide = 0.0;

            if (slide_left > 0) {
                slide = 0.7;
            } else if (slide_right > 0) {
                slide = -0.7;
            }

            md.drive(drive, slide, turn);
            md.loop();
            //md.go(driveY, 0, currentAngle);

            if (ingest) {
                intake.intake();
            } else if (egest) {
                intake.outPut();
            } else {
                intake.stop();
            }

            if (liftUp) {
                liftSystem.up();
            } else if (liftDown) {
                liftSystem.down();
            } else {
                liftSystem.stop();
            }

            if (foundationClaw_a && timer.milliseconds() - time > 300) {
                fClaw.open();
                time = timer.milliseconds();
            } else if (foundationClaw_b && timer.milliseconds() - time > 300) {
                fClaw.close();
                time = timer.milliseconds();
            }

//            if (clawChange && timer.milliseconds() - clawTime > 5) {
//                clawPosition = clawPosOpen;
////                clawPosition -= 0.01;
////                if (clawPosition > clawPosClose)
////                    clawPosition = clawPosClose;
////                clawTime = timer.milliseconds();
//            } else if (clawChange && timer.milliseconds() - clawTime > 5) {
//                clawPosition = clawPosClose;
//
////                clawPosition += 0.01;
////                if (clawPosition > clawPosOpen)
////                    clawPosition = clawPosOpen;
////                clawTime = timer.milliseconds();
//            }

            if (clawChange && timer.milliseconds() - clawTime > 500) {
                if (clawPosition == clawPosOpen) {
                    clawPosition = clawPosClose;
                    clawTime = timer.milliseconds();
                } else {
                    clawPosition = clawPosOpen;
                    clawTime = timer.milliseconds();
                }
            }

            claw.setPosition(clawPosition);

            if (armPower > 0.1) {
                arm.setPower(0.5);
            } else if (armPower < -0.1) {
                arm.setPower(-0.5);
            } else {
                arm.setPower(0);
            }

            telemetry.addData("driveX:driveY:driveYaw:", " " + slide_left + " " + drive + " " + turn);
            telemetry.addData("clawPosition:", " " + clawPosition);
            telemetry.addData("imu:", "h:" + imu.getHeading() + " p:" + imu.getPitch() + " r:" + imu.getRoll());
            telemetry.update();
        }
    }

}


