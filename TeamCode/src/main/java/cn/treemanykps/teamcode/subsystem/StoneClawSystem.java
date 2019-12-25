package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class StoneClawSystem {
    private Servo claw = null;
    private DcMotor arm = null;
    double armPower = 1;
    public int status = 0;//0 停止；1：伸展；2：收缩；9:
    int armMoveTime = 500;

    final static int ARM_STOP = 0;
    final static int ARM_STRETCHING = 1;
    final static int ARM_SHRINKING = 2;
    final static int ARM_STRETCHED = 9;

    ElapsedTime timer = new ElapsedTime();

    public void init(Servo claw){
        this.claw = claw;
    }
    double clawPosOpen = 0.95;
    double clawPosClose = 0.55;

    public void init(Servo claw, DcMotor arm){
        this.claw = claw;
        this.claw.close();
        this.arm = arm;
        this.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.arm.setPower(0);
        status = ARM_STOP;
    }

    public void open(){
        claw.setPosition(clawPosOpen);

    }

    public void close(){
        claw.setPosition(clawPosClose);
    }

    /**
     * 机械臂伸展
     * */
    public void armStretch(){
        status=ARM_STRETCHING;
        timer.reset();
    }
    /**
     * 机械臂收缩
     * */
    public void armShrink(){
        status=ARM_SHRINKING;
        timer.reset();
    }

    /**
     * 机械臂停止
     * */
    public void armStop(){
        arm.setPower(0);
    }

    public void loop(){
        double power = 0;

        if(status==ARM_SHRINKING){
            power =-armPower            ;
        }
        if(status==ARM_STRETCHING){
            power=armPower;
        }
        if(timer.milliseconds()>armMoveTime){
            if(status==ARM_SHRINKING){
                status = ARM_STOP;
            }
            if(status==ARM_STRETCHING){
                status = ARM_STRETCHED;
            }
        }
        if(status == ARM_STOP || status == ARM_STRETCHED){

            timer.reset();
        }

        arm.setPower(power);
    }
}
