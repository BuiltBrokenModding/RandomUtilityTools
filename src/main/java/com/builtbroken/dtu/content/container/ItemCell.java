package com.builtbroken.dtu.content.container;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Fluid cell for fluid vac
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
public class ItemCell extends Item implements IFluidContainerItem
{
    @Override
    public FluidStack getFluid(ItemStack container)
    {
        return null; //TODO
    }

    @Override
    public int getCapacity(ItemStack container)
    {
        return 0; //TODO
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        return 0; //TODO
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        return null; //TODO
    }
}
