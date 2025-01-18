package io.sandbox.lib;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

public class BlockHelper {
  public static Block getBlockFromId(String blockId) {
    if (blockId == null) { return null; }

    Identifier identifier = Identifier.tryParse(blockId);
    Block foundBlock = Registries.BLOCK.get(identifier);

    if (foundBlock == Blocks.AIR && blockId != "minecraft:air") { return null; }

    return foundBlock;
  }
}
