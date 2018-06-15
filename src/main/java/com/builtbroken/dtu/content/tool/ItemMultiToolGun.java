package com.builtbroken.dtu.content.tool;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.content.upgrade.ToolUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Upgrade-able gun that can support several tool types
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
public class ItemMultiToolGun extends Item
{
    public static final String NBT_MODE = "toolMode";
    public static final String NBT_SUB_MODE = "toolSubMode";
    public static final String NBT_UPGRADES = "toolUpgrades";

    private static final int[] EMPTY_INT = new int[0];

    public ItemMultiToolGun()
    {
        setCreativeTab(DTUMod.creativeTab);
        setUnlocalizedName(DTUMod.PREFX + "multiTool");
        setTextureName(DTUMod.PREFX + "multiTool/multiTool");
    }

    //===============================================
    //=======Data Accessors
    //===============================================

    //<editor-fold desc="data actions">
    /**
     * Called to handle mouse wheel movement
     *
     * @param stack   - this
     * @param player  - player using the item
     * @param ctrl    - was ctrl held
     * @param forward - is mouse wheel moving forward
     */
    public void handleMouseWheelAction(ItemStack stack, EntityPlayer player, boolean ctrl, boolean forward)
    {
        if (ctrl)
        {
            toggleMode(stack, forward);
        }
        else
        {
            toggleSubMode(stack, forward);
        }
        player.inventoryContainer.detectAndSendChanges();
    }

    public void toggleMode(ItemStack stack, boolean forward)
    {
        final ToolMode currentMode = getMode(stack);

        //Find next mode that is supported
        ToolMode nextMode;
        do
        {
            nextMode = forward ? currentMode.next() : currentMode.prev();
        }
        while (nextMode != currentMode && !supportsMode(stack, nextMode));

        //Update item
        setMode(stack, nextMode);
    }

    public void toggleSubMode(ItemStack stack, boolean forward)
    {
        final ToolMode mode = getMode(stack);
        final int subMode = getSubMode(stack);
        final int max = getSubModes(stack);

        if (max > 0)
        {
            int nextMode = subMode;
            ToolAction action;
            do
            {
                nextMode += forward ? 1 : -1;
                if (nextMode < 0)
                {
                    nextMode = max - 1;
                }
                else if (nextMode >= max)
                {
                    nextMode = 0;
                }
                action = mode.toolActions.get(nextMode);
            }
            while (nextMode != subMode && !hasUpgrade(stack, action.upgradeRequired));

            setSubMode(stack, nextMode);
        }
    }
    //</editor-fold>

    //<editor-fold desc="data mode">
    public ToolMode getMode(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return ToolMode.get(stack.getTagCompound().getInteger(NBT_MODE));
        }
        return ToolMode.OFF;
    }

    public void setMode(ItemStack stack, ToolMode mode)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger(NBT_MODE, mode.ordinal());
    }

    public int getSubMode(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return stack.getTagCompound().getInteger(NBT_SUB_MODE);
        }
        return 0;
    }

    public void setSubMode(ItemStack stack, int mode)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger(NBT_SUB_MODE, mode);
    }

    public int getSubModes(ItemStack stack)
    {
        return getMode(stack).toolActions.size();
    }

    //</editor-fold>

    //<editor-fold desc="data upgrades">
    public int[] getUpgradeArray(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return stack.getTagCompound().getIntArray(NBT_UPGRADES);
        }
        return EMPTY_INT;
    }

    public boolean supportsMode(ItemStack stack, ToolMode mode)
    {
        if (mode.upgrades == null)
        {
            return true;
        }
        else
        {
            for (ToolUpgrade upgrade : mode.upgrades)
            {
                if (hasUpgrade(stack, upgrade))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasUpgrade(ItemStack stack, ToolUpgrade upgrade)
    {
        //Loop upgrades until match, not best option but simple enough
        for (int i : getUpgradeArray(stack))
        {
            if (i == upgrade.ordinal())
            {
                return true;
            }
        }
        return false;
    }
    //</editor-fold>
}
