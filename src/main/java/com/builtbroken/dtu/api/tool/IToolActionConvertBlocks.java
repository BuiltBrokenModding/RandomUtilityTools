package com.builtbroken.dtu.api.tool;

import net.minecraft.block.Block;

/**
 * Tools that convert one block to another
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public interface IToolActionConvertBlocks extends IToolAction
{
    /**
     * Adds a conversion for the block
     *
     * @param block  - block to match for conversion
     * @param effect - result of conversion
     */
    void addConversion(Block block, IBlockEffect effect);

    /**
     * Adds a conversion for the block
     *
     * @param block  - block to match for conversion
     * @param newBlock - block to convert to
     */
    default void addConversion(Block block, Block newBlock)
    {
        addConversion(block, -1, newBlock, 0);
    }

    /**
     * Adds a conversion for the block
     *
     * @param block  - block to match for conversion
     * @param newBlock - block to convert to
     */
    default void addConversion(Block block, Block newBlock, int meta)
    {
        addConversion(block, -1, newBlock, meta);
    }

    /**
     * Adds a conversion for the block
     *
     * @param block  - block to match for conversion
     * @param newBlock - block to convert to
     */
    void addConversion(Block block, int meta, Block newBlock, int newMeta);

    /**
     * Adds a conversion for the block
     *
     * @param block  - block to match for conversion
     * @param meta   - meta to match for conversion
     * @param effect - result of conversion
     */
    void addConversion(Block block, int meta, IBlockEffect effect);
}
