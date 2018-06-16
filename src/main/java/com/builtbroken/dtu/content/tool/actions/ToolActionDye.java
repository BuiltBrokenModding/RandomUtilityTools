package com.builtbroken.dtu.content.tool.actions;

import com.builtbroken.dtu.api.tool.ToolUpgrade;
import com.builtbroken.dtu.content.tool.actions.imp.ActionResult;
import com.builtbroken.dtu.content.tool.actions.imp.ToolAction;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/16/2018.
 */
public class ToolActionDye extends ToolAction
{
    public final int colorIndex;

    public ToolActionDye(String name, int colorIndex)
    {
        super("color." + name, ToolUpgrade.COLOR);
        this.colorIndex = colorIndex;
    }

    @Override
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            if (player.canPlayerEdit(x, y, z, side, tool))
            {
                Block block = world.getBlock(x, y, z);
                if (block != null)
                {
                    if (block.recolourBlock(world, x, y, z, ForgeDirection.getOrientation(side), ~colorIndex & 15))
                    {
                        return ActionResult.STOP;
                    }
                    else if (block == Blocks.carpet) //TODO move to conversion map
                    {
                        if (world.setBlockMetadataWithNotify(x, y, z, ~colorIndex & 15, 3))
                        {
                            return ActionResult.STOP;
                        }
                    }
                    else if(block == Blocks.stained_hardened_clay) //TODO move to conversion map
                    {
                        if (world.setBlockMetadataWithNotify(x, y, z, ~colorIndex & 15, 3))
                        {
                            return ActionResult.STOP;
                        }
                    }
                    else if(block == Blocks.stained_glass) //TODO move to conversion map
                    {
                        if (world.setBlockMetadataWithNotify(x, y, z, ~colorIndex & 15, 3))
                        {
                            return ActionResult.STOP;
                        }
                    }
                    else if(block == Blocks.stained_glass_pane) //TODO move to conversion map
                    {
                        if (world.setBlockMetadataWithNotify(x, y, z, ~colorIndex & 15, 3))
                        {
                            return ActionResult.STOP;
                        }
                    }
                }
                return ActionResult.NO_RUN;
            }
            return ActionResult.PASS;
        }
        return ActionResult.CONTINUE;
    }
}
