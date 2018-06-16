package com.builtbroken.dtu.content.tool.actions.fluid;

import com.builtbroken.dtu.content.tool.actions.imp.ActionResult;
import com.builtbroken.dtu.content.tool.actions.imp.ToolAction;
import com.builtbroken.dtu.api.tool.ToolUpgrade;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public class ToolActionFluidDelete extends ToolAction
{
    public ToolActionFluidDelete()
    {
        super("fluid.remover", ToolUpgrade.FLUID_VAC);
    }

    @Override
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            if (player.canPlayerEdit(x, y, z, side, tool))
            {
                Block block = world.getBlock(x, y, z);
                if (block == Blocks.water) //TODO support any fluid
                {
                    world.playSoundEffect(x, y, z, "random.click", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                    world.setBlock(x, y, z, Blocks.air, 0, 2);
                    return ActionResult.STOP;
                }
            }
            return ActionResult.NO_RUN;
        }
        return ActionResult.CONTINUE;
    }

    @Override
    public boolean rayFluids()
    {
        return true;
    }
}
