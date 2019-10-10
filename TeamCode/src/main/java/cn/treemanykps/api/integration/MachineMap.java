package cn.treemanykps.api.integration;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Map;

import cn.treemanykps.api.integration.structure.IMachineComponent;

/**
 * A prettier approach of {@link HardwareMap}.
 *
 * @param <T> An enum of components. It should extend {@link IMachineComponent}.
 */
public class MachineMap<T extends IMachineComponent> {

    private Map<T, HardwareDevice> machineMap;

    public MachineMap() {
        this.machineMap = new HashMap<>();
    }

    public void initDevice(T component, HardwareMap map) {
        this.machineMap.put(component, map.get(component.getName()));
    }

    public <D extends HardwareDevice> D get(T component) {
        return (D) this.machineMap.get(component);
    }
}
