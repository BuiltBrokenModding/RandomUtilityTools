package com.builtbroken.dtu.content.tool.actions;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.content.tool.ToolAction;
import com.builtbroken.dtu.content.upgrade.ToolUpgrade;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public class ToolActionMelt extends ToolAction
{
    public ToolActionMelt()
    {
        super("tool." + DTUMod.PREFX + "mode.action.melt", ToolUpgrade.MELT);
    }

    @Override
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            if (player.canPlayerEdit(x, y, z, side, tool))
            {
                Block block = world.getBlock(x, y, z);
                if (block == Blocks.cobblestone) //TODO replace with register feed by JSON file
                {
                    world.setBlock(x, y, z, Blocks.stone);
                    return ActionResult.STOP;
                }
                else if (block == Blocks.sand)
                {
                    world.setBlock(x, y, z, Blocks.glass);
                    return ActionResult.STOP;
                }
            }
            return ActionResult.NO_RUN;
        }
        return ActionResult.CONTINUE;
    }
}
