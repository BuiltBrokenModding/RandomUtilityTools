package com.builtbroken.dtu.content.tool.actions.blocks;

import com.builtbroken.dtu.api.tool.IBlockEffect;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Objects;

/**
 * Very simple set block effect
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public class BlockConversion implements IBlockEffect
{
    public final Block block;
    public final int meta;

    public BlockConversion(Block block, int meta)
    {
        this.block = block;
        this.meta = meta;
    }

    @Override
    public boolean apply(World world, int x, int y, int z, int flag)
    {
        if (world.setBlock(x, y, z, block, meta, flag))
        {
            block.onNeighborBlockChange(world, x, y, z, block);
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        String id = Block.blockRegistry.getNameForObject(block);
        return "BlockConversion[" + (id != null ? id : "NO_REG_KEY<" + block + ">") + ", " + meta + "]@" + hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        else if (other instanceof BlockConversion)
        {
            return block == ((BlockConversion) other).block && meta == ((BlockConversion) other).meta;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(block, meta);
    }
}
