package cn.treemanykps.api.integration.structure;

/**
 * Should be implemented by the enum containing all the machine components.
 * Used by {@link cn.treemanykps.api.integration.MachineMap} to find
 * {@link com.qualcomm.robotcore.hardware.HardwareDevice} by its name.
 */
public interface IMachineComponent {

    String getName();
}
