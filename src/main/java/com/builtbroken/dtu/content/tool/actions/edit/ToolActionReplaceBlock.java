package com.builtbroken.dtu.content.tool.actions.edit;

import com.builtbroken.dtu.api.tool.ToolUpgrade;
import com.builtbroken.dtu.content.tool.actions.imp.ActionResult;
import com.builtbroken.dtu.content.tool.actions.imp.ToolAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Replaces one block with another
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/19/2018.
 */
public class ToolActionReplaceBlock extends ToolAction
{
    public ToolActionReplaceBlock()
    {
        super("edit.replace", ToolUpgrade.EDIT_REPLACE_BLOCK);
    }

    @Override
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            if (player.canPlayerEdit(x, y, z, side, tool))
            {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile == null)
                {
                   harvestBlock(world, player, tool, x, y, z);

                }
                return ActionResult.NO_RUN;
            }
            return ActionResult.PASS;
        }
        return ActionResult.CONTINUE;
    }

    public void harvestBlock(World world, EntityPlayer player, ItemStack tool, int x, int y, int z)
    {

    }
}
