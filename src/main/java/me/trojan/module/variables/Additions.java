package me.trojan.module.variables;

import me.trojan.ModuleInfo;
import me.trojan.base.BaseVariableProvider;
import me.trojan.helpers.ContainerUtils;
import me.trojan.helpers.HypixelLocationIdentifier;

public class Additions extends BaseVariableProvider {

    @Override
    public void updateVariables(boolean clock) {
        if(!clock)
            return;
        storeVariable("MODULEADDITIONS", true);
        storeVariable("ADDITIONSVERSION", ModuleInfo.MODULE_VERSION);

        storeVariable("INVISFULL", ContainerUtils.checkIfInvIsFull());
        storeVariable("MAINHANDITEMDISPLAYNAME", ContainerUtils.getMainhandItemDisplayName());
        storeVariable("SLOTSFULL", ContainerUtils.getFullInventorySlots());
        storeVariable("SLOTSEMPTY", ContainerUtils.getEmptyInventorySlots(false));
        storeVariable("CHESTNAME", ContainerUtils.getChestName() != null ? ContainerUtils.getChestName() : "NONE");

        storeVariable("HYPIXELLOCATION", HypixelLocationIdentifier.getHypixelLocation());

        /* Variables from the unicodeChar-Module. */
        storeVariable("P", "§");
        storeVariable("DOLLAR", "$");
        storeVariable("DOLLARS", "$$");
    }
}
