package cn.treemanykps.teamcode.hardware;

import cn.treemanykps.api.integration.structure.IMachineComponent;

public enum TreebotParts implements IMachineComponent {

    RIGHT_TOP_MOTOR("right_top_motor"),
    RIGHT_BOTTOM_MOTOR("right_bottom_motor"),
    LEFT_TOP_MOTOR("left_top_motor"),
    LEFT_BOTTOM_MOTOR("left_bottom_motor");

    private String name;

    TreebotParts(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
