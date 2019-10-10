package cn.treemanykps.api.integration.structure;

import lombok.Value;

/**
 * Should be implemented by the enum containing all the machine components.
 * Used by {@link cn.treemanykps.api.integration.MachineMap} to find
 * {@link com.qualcomm.robotcore.hardware.HardwareDevice} by its name.
 */
@Value
public class MachineComponent {

    private String name;
}
