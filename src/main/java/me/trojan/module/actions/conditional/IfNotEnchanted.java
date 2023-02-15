package me.trojan.module.actions.conditional;

import me.trojan.base.BaseConditionalScriptAction;
import me.trojan.helpers.ContainerUtils;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.item.ItemStack;

public class IfNotEnchanted extends BaseConditionalScriptAction {
    public IfNotEnchanted() {
        super("ifEnchanted");
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ItemStack itemStack = null;
        if(params.length > 0)
            itemStack = ContainerUtils.getItemStackInSlot(Integer.parseInt(provider.expand(macro, params[0], false)));
        return itemStack != null && !itemStack.isItemEnchanted();
    }
}
