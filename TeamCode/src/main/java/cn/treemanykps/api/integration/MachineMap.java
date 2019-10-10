package cn.treemanykps.api.integration;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Map;

import cn.treemanykps.api.integration.structure.MachineComponent;

public class MachineMap {

    private Map<MachineComponent, HardwareDevice> machineMap;

    public MachineMap() {
        this.machineMap = new HashMap<>();
    }

    public void initDevice(MachineComponent component, HardwareMap map) {
        this.machineMap.put(component, map.get(component.getName()));
    }

    public <T extends HardwareDevice> T get(MachineComponent component) {
        return (T) this.machineMap.get(component);
    }
}
