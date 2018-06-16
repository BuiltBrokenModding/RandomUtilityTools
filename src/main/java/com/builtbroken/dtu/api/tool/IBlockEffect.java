package com.builtbroken.dtu.api.tool;

import net.minecraft.world.World;

/**
 * Applies an effect to the location
 * <p>
 * Main use for this is to convert blocks in {@link IToolActionConvertBlocks}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public interface IBlockEffect
{
    /**
     * Called to apply the effect to the block location
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @param flag  - condition to place the block, default is 3
     * @return true if the effect went through
     */
    boolean apply(World world, int x, int y, int z, int flag);
}
