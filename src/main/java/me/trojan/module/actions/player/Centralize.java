package me.trojan.module.actions.player;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class Centralize extends BaseScriptAction {
    private static final double DEFAULT_OFFSET_X = 0.5D;
    private static final double DEFAULT_OFFSET_Y = 0.0D;
    private static final double DEFAULT_OFFSET_Z = 0.5D;

    public Centralize() {
        super("centralize");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        double xOffset = params.length > 0 ? tryParseDouble(provider.expand(macro, params[0],false), 0.5D) : DEFAULT_OFFSET_X;
        double yOffset = params.length > 1 ? tryParseDouble(provider.expand(macro, params[1],false), 0.0D) : DEFAULT_OFFSET_Y;
        double zOffset = params.length > 2 ? tryParseDouble(provider.expand(macro, params[2],false), 0.5D) : DEFAULT_OFFSET_Z;
        mc.player.setPosition(getCenterPosX(xOffset), getCenterPosY(yOffset), getCenterPosZ(zOffset));
        return new ReturnValue("");
    }

    private double getCenterPosZ(double zOffset) {
        return (int) mc.player.posZ + ((mc.player.posZ > 0.0D) ? zOffset : -zOffset);
    }

    private double getCenterPosY(double yOffset) {
        return (int) mc.player.posY + yOffset;
    }

    private double getCenterPosX(double xOffset) {
        return (int) mc.player.posX + ((mc.player.posX > 0.0D) ? xOffset : -xOffset);
    }

    private double tryParseDouble(String value, Double defaultValue) {
        double d;
        try {
            d = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            d = defaultValue;
        }
        return d;
    }
}
