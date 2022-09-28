package io.sandbox.lib;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockHelper {
  public static Block getBlockFromId(String blockId) {
    if (blockId == null) { return null; }

    Block foundBlock = Registry.BLOCK.get(new Identifier(blockId));

    if (foundBlock == Blocks.AIR && blockId != "minecraft:air") { return null; }

    return foundBlock;
  }
}
