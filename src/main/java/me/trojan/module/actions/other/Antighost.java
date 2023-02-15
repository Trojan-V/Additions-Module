package me.trojan.module.actions.other;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class Antighost extends BaseScriptAction {
    public Antighost() {
        super("antighost");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        NetHandlerPlayClient connection = mc.getConnection();
        if(connection == null) return new ReturnValue(false);

        sendDiggingPackets(connection);
        return new ReturnValue(true);

    }

    private void sendDiggingPackets(NetHandlerPlayClient connection) {
        EntityPlayerSP player = mc.player;
        BlockPos pos = player.getPosition();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    CPacketPlayerDigging packet = new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos.add(dx, dy, dz), EnumFacing.UP);
                    connection.sendPacket(packet);
                }
            }
        }
    }
}
