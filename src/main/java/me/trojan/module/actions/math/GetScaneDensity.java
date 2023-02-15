package me.trojan.module.actions.math;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.eq2online.util.Game;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class GetScaneDensity extends BaseScriptAction {
    private int reeds;
    private int blocksScanned;

    public GetScaneDensity() {
        super("getscanedensity");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

        /*
         GETDENSITY([N/E/S/W],<limit_search>,<initial_position>,<#blocks_searched>,<#sugarcane_found>);
         Searches for sugarcane in the given direction, and stores the blocks searched and sugarcane found in order to calculate the density.
         North/South - Use the Z coordinate for the <limit_search>.
         East/West - Use the X coordinate for the <limit_search>. <initial_position> should be "%XPOS%|%YPOS%|%ZPOS%"
        */
        String direction = provider.expand(macro, params[0], false);
        int limitSearch = ScriptCore.tryParseInt(provider.expand(macro, params[1], false), -1);
        if(limitSearch == -1)
            return null;

        String positionToUse = provider.expand(macro, params[2], false);
        String[] pos = positionToUse.split("\\|");
        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        int z = Integer.parseInt(pos[2]);
        getBlocksInDirection(direction, limitSearch, x, y, z);
        return new ReturnValue(String.valueOf(calculateDensity()));
    }


    private double calculateDensity() {
        return (reeds * 100.0D) / blocksScanned;
    }

    private void getBlocksInDirection(String direction, int limitSearch, int x, int y, int z) {
        reeds = 0;
        blocksScanned = 0;

        switch (direction) {
            case "N":
                for (int i = z; i > (z - limitSearch); i--) {
                    Block block = mc.world.getBlockState(new BlockPos(x, y + 1, i)).getBlock();
                    if (Game.getBlockName(block).equals("reeds")) {
                        reeds++;
                        Block blockAbove = mc.world.getBlockState(new BlockPos(x, y + 2, i)).getBlock();
                        if (Game.getBlockName(blockAbove).equals("reeds")) {
                            reeds++;
                        }
                    }
                    blocksScanned += 2;
                }
                break;

            case "S":
                for (int i = z; i < (z + limitSearch); i++) {
                    Block block = mc.world.getBlockState(new BlockPos(x, y + 1, i)).getBlock();
                    if (Game.getBlockName(block).equals("reeds")) {
                        reeds++;
                        Block blockAbove = mc.world.getBlockState(new BlockPos(x, y + 2, i)).getBlock();
                        if (Game.getBlockName(blockAbove).equals("reeds")) {
                            reeds++;
                        }
                    }
                    blocksScanned += 2;
                }
                break;

            case "E":
                for (int i = x; i < (x + limitSearch); i++) {
                    Block block = mc.world.getBlockState(new BlockPos(i, y + 1, z)).getBlock();
                    if (Game.getBlockName(block).equals("reeds")) {
                        reeds++;
                        Block blockAbove = mc.world.getBlockState(new BlockPos(i, y + 2, z)).getBlock();
                        if (Game.getBlockName(blockAbove).equals("reeds")) {
                            reeds++;
                        }
                    }
                    blocksScanned += 2;
                }
                break;

            case "W":
                for (int i = x; i > (x - limitSearch); i--) {
                    Block block = mc.world.getBlockState(new BlockPos(i, y + 1, z)).getBlock();
                    if (Game.getBlockName(block).equals("reeds")) {
                        reeds++;
                        Block blockAbove = mc.world.getBlockState(new BlockPos(i, y + 2, z)).getBlock();
                        if (Game.getBlockName(blockAbove).equals("reeds")) {
                            reeds++;
                        }
                    }
                    blocksScanned += 2;
                }
                break;
        }
    }
}
