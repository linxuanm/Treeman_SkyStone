package cn.treemanykps.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.EnumSet;

import cn.treemanykps.api.integration.robot.IRobotBase;

public class Treebot extends IRobotBase<TreebotParts> {

    public Treebot(HardwareMap hardwareMap) {
        super(hardwareMap);
    }

    @Override
    protected Iterable<TreebotParts> getParts() {
        return EnumSet.allOf(TreebotParts.class);
    }
}
