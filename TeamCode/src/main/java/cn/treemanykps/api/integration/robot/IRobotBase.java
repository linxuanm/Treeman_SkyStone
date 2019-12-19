package cn.treemanykps.api.integration.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import cn.treemanykps.api.integration.MachineMap;
import cn.treemanykps.api.integration.structure.IMachineComponent;

/**
 * The base class which all robots should extend.
 *
 * @param <T> An enum of components. It should extend
 * {@link cn.treemanykps.api.integration.structure.IMachineComponent}.
 */
public abstract class IRobotBase<T extends IMachineComponent> {

    private MachineMap<T> machineMap;

    public IRobotBase(HardwareMap hardwareMap) {
        this.machineMap = new MachineMap<>();
        for (T i: this.getParts()) {
            this.machineMap.initDevice(i, hardwareMap);
        }
    }

    /**
     * @return An {@link Iterable} of all robot parts.
     */
    abstract protected Iterable<T> getParts();
}
