package com.builtbroken.dtu.content.tool.actions.edit;

import com.builtbroken.dtu.api.tool.ToolUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Replaces one block with another
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/19/2018.
 */
public class ToolActionReplaceBlock extends ToolActionRemoveBlock
{
    public ToolActionReplaceBlock()
    {
        super("edit.replace", ToolUpgrade.EDIT_REPLACE_BLOCK);
    }

    @Override
    protected boolean shouldHarvest(World world, EntityPlayer player, ItemStack tool, int x, int y, int z)
    {
        if (super.shouldHarvest(world, player, tool, x, y, z))
        {
            return getPlacementStack(player) != null;
        }
        return false;
    }

    @Override
    protected void onBlockRemoved(World world, EntityPlayer player, ItemStack tool, int x, int y, int z)
    {
        ItemStack stack = getPlacementStack(player);
        if (stack != null && stack.getItem() instanceof ItemBlock)
        {
            //Copy to prevent issues
            stack = stack.copy();

            //if creative mode, just place the block
            if (player.capabilities.isCreativeMode)
            {
                placeBlock(world, player, x, y, z, stack);
            }
            //Check that we have a block in the inventory
            else if (player.inventory.hasItemStack(stack))
            {
                boolean removed = false;

                //Remove backwards out of inventory to prevent consuming item next to tool
                for (int slot = player.inventory.getSizeInventory() - 1; slot >= 0; slot--)
                {
                    ItemStack slotStack = player.inventory.getStackInSlot(slot);
                    if (slotStack != null && slotStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(slotStack, stack))
                    {
                        slotStack.stackSize--;
                        if (slotStack.stackSize <= 0)
                        {
                            slotStack = null;
                        }
                        player.inventory.setInventorySlotContents(slot, slotStack);
                        player.inventoryContainer.detectAndSendChanges();
                        removed = true;
                        break;
                    }
                }

                if (removed)
                {
                    placeBlock(world, player, x, y, z, stack);
                }
            }
        }
    }

    protected void placeBlock(World world, EntityPlayer player, int x, int y, int z, ItemStack blockStack)
    {
        blockStack.getItem().onItemUse(blockStack, player, world,
                x, y - 1, z, 1,
                0, 0, 0);
    }

    protected ItemStack getPlacementStack(EntityPlayer player)
    {
        int slot = player.inventory.currentItem;
        if (slot == 8)
        {
            slot--;
        }
        else
        {
            slot++;
        }

        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack != null && stack.getItem() instanceof ItemBlock)
        {
            return stack;
        }
        return null;
    }
}
