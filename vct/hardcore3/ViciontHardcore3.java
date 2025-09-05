import Casino.CasinoManager;
import SlotMachine.SlotMachineManager;
import Commands.*;

// Casino
private CasinoManager casinoManager;

// Slot Machine System
private SlotMachineManager slotMachineManager;

    // Sistema de Casino
    casinoManager = new CasinoManager(this);
    
    // Sistema de Slot Machine
    slotMachineManager = new SlotMachineManager(this);

    MobCapManager.getInstance(this).shutdown();
    economyItemsFunctions.onDisable();
    
    // Limpiar SlotMachineManager
    if (slotMachineManager != null) {
        slotMachineManager.shutdown();
    }

public SuccessNotification getSuccessNotifier() {
    return successNotif;
}

public SlotMachineManager getSlotMachineManager() {
    return slotMachineManager;
}
}