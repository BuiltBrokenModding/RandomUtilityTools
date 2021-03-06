package com.builtbroken.dtu.content.tool.actions.fluid;

import com.builtbroken.dtu.api.tool.ToolUpgrade;
import com.builtbroken.dtu.content.tool.actions.imp.ActionResult;
import com.builtbroken.dtu.content.tool.actions.imp.ToolAction;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public class ToolActionFluidRemove extends ToolAction
{
    public final boolean updateBlock;

    public ToolActionFluidRemove(String name, ToolUpgrade toolUpgrade, boolean updateBlock)
    {
        super(name, toolUpgrade);
        this.updateBlock = updateBlock;
    }

    @Override
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            if (player.canPlayerEdit(x, y, z, side, tool))
            {
                final FluidStack fluidStack = getFluid(world, x, y, z);
                if (fluidStack != null)
                {
                    int slot = getSlotWithEmptyContainer(player, fluidStack);

                    if (slot >= 0)
                    {
                        if (world.setBlock(x, y, z, Blocks.air, 0, updateBlock ? 3 : 2))
                        {
                            world.playSoundEffect(x, y, z, "random.click", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                            fillContainerInSlot(player, slot, fluidStack);
                        }
                        return ActionResult.STOP;
                    }
                }
            }
            return ActionResult.NO_RUN;
        }
        return ActionResult.CONTINUE;
    }

    public FluidStack getFluid(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (block == Blocks.water)
        {
            return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
        }
        else if (block == Blocks.lava)
        {
            return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
        }
        else if (block instanceof IFluidBlock && ((IFluidBlock) block).canDrain(world, x, y, z))
        {
            return ((IFluidBlock) block).drain(world, x, y, z, false);
        }
        return null;
    }


    public int getSlotWithEmptyContainer(EntityPlayer player, final FluidStack fluidStack)
    {
        IInventory inventory = player.inventory;
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
        {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null)
            {
                if (stack.getItem() instanceof IFluidContainerItem)
                {
                    int fill = ((IFluidContainerItem) stack.getItem()).fill(stack, fluidStack.copy(), false);
                    if (fill >= fluidStack.amount)
                    {
                        return slot;
                    }
                }
                else if (FluidContainerRegistry.isEmptyContainer(stack))
                {
                    ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluidStack.copy(), stack);
                    if (filledContainer != null)
                    {
                        return slot;
                    }
                }
            }
        }
        return -1;
    }

    public void fillContainerInSlot(EntityPlayer player, int slot, FluidStack fluidStack)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack != null)
        {
            if (stack.getItem() instanceof IFluidContainerItem)
            {
                ItemStack filledContainer = stack.splitStack(1);
                if (stack.stackSize <= 0)
                {
                    stack = null;
                }
                ((IFluidContainerItem) stack.getItem()).fill(filledContainer, fluidStack, true);

                if (stack != null)
                {
                    if (!player.inventory.addItemStackToInventory(filledContainer))
                    {
                        player.entityDropItem(stack, 0);
                    }
                    player.inventory.setInventorySlotContents(slot, stack);
                }
                else
                {
                    player.inventory.setInventorySlotContents(slot, filledContainer);
                }
            }
            else if (FluidContainerRegistry.isEmptyContainer(stack))
            {
                ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluidStack, stack);
                if (filledContainer != null)
                {
                    stack.stackSize--;
                    if (stack.stackSize <= 0)
                    {
                        stack = null;
                    }

                    if (stack != null)
                    {
                        if (!player.inventory.addItemStackToInventory(filledContainer))
                        {
                            player.entityDropItem(stack, 0);
                        }
                        player.inventory.setInventorySlotContents(slot, stack);
                    }
                    else
                    {
                        player.inventory.setInventorySlotContents(slot, filledContainer);
                    }
                }
            }
        }
        player.inventoryContainer.detectAndSendChanges();
    }

    @Override
    public boolean rayFluids()
    {
        return true;
    }
}
