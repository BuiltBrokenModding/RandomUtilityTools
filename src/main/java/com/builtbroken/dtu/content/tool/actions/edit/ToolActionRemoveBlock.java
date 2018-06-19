package com.builtbroken.dtu.content.tool.actions.edit;

import com.builtbroken.dtu.api.tool.ToolUpgrade;
import com.builtbroken.dtu.content.tool.actions.imp.ActionResult;
import com.builtbroken.dtu.content.tool.actions.imp.ToolAction;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * Replaces one block with another
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/19/2018.
 */
public class ToolActionRemoveBlock extends ToolAction
{
    public ToolActionRemoveBlock()
    {
        super("edit.remove", ToolUpgrade.EDIT_REMOVE_BLOCK);
    }

    public ToolActionRemoveBlock(String name, ToolUpgrade upgrade)
    {
        super(name, upgrade);
    }

    @Override
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            if (player.canPlayerEdit(x, y, z, side, tool))
            {
                if (shouldHarvest(world, player, tool, x, y, z))
                {
                    if (harvestBlock(world, player, tool, x, y, z))
                    {
                        onBlockRemoved(world, player, tool, x, y, z);
                        return ActionResult.STOP;
                    }
                }
                return ActionResult.NO_RUN;
            }
            return ActionResult.PASS;
        }
        return ActionResult.CONTINUE;
    }

    protected boolean shouldHarvest(World world, EntityPlayer player, ItemStack tool, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null)
        {
            return true;
        }
        return false;
    }

    protected void onBlockRemoved(World world, EntityPlayer player, ItemStack tool, int x, int y, int z)
    {

    }

    public boolean harvestBlock(World world, EntityPlayer player, ItemStack tool, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (block != null && player instanceof EntityPlayerMP)
        {
            if (((EntityPlayerMP) player).theItemInWorldManager.tryHarvestBlock(x, y, z))
            {
                AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
                List list = world.getEntitiesWithinAABB(EntityItem.class, bounds);
                for (Object object : list)
                {
                    if (object instanceof EntityItem)
                    {
                        EntityItem entityItem = (EntityItem) object;
                        ItemStack stack = entityItem.getEntityItem();

                        if (!player.inventory.addItemStackToInventory(stack))
                        {
                            entityItem.setEntityItemStack(stack);
                            entityItem.setPosition(
                                    player.posX,
                                    player.posY + (player.height / 2),
                                    player.posZ);
                        }
                        else
                        {
                            entityItem.setDead();
                        }
                        player.inventoryContainer.detectAndSendChanges();
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSwitchToMode(ItemStack tool, EntityPlayer player)
    {
        tool.addEnchantment(Enchantment.silkTouch, 1);
    }

    @Override
    public void onSwitchFromMode(ItemStack tool, EntityPlayer player)
    {
        if (tool.getTagCompound() != null)
        {
            tool.getTagCompound().removeTag("ench");
        }
    }
}
