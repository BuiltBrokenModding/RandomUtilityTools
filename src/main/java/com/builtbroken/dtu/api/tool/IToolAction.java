package com.builtbroken.dtu.api.tool;

import com.builtbroken.dtu.content.tool.actions.imp.ActionResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public interface IToolAction
{
    /**
     * Cost to run the tool, per tick
     *
     * @param player
     * @param tool
     * @return
     */
    default int getEnergyCost(EntityPlayer player, ItemStack tool)
    {
        return 0;
    }

    /**
     * Cost to complete an action on the block
     *
     * @param player - player holding the tool
     * @param tool   - current tool
     * @param world  - hit position
     * @param x-     hit position
     * @param y-     hit position
     * @param z-     hit position
     * @param side-  hit position
     * @return true to consume action
     */
    default int getActionCost(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        return 0;
    }

    /**
     * Should the tool path water
     *
     * @return true to include water blocks
     */
    default boolean rayFluids()
    {
        return false;
    }

    /**
     * Limit of path range
     *
     * @param player
     * @param tool
     * @return
     */
    default int getRange(EntityPlayer player, ItemStack tool)
    {
        return 10;
    }

    /**
     * Called when the path hits a block
     *
     * @param player - player holding the tool
     * @param tool   - current tool
     * @param world  - hit position
     * @param x-     hit position
     * @param y-     hit position
     * @param z-     hit position
     * @param side-  hit position
     * @return true to consume action
     */
    ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side);

    /**
     * Icon path to use
     *
     * @param iconName - default item icon path
     * @return path, null will be ignored
     */
    default String getIconName(String iconName)
    {
        return iconName + "." + getName();
    }

    /**
     * Name of the item, without localization prefixes
     * Example: melter
     *
     * @return
     */
    String getName();

    /**
     * Item name localization key
     *
     * @return
     */
    String getItemName();

    /**
     * Item tooltip localization key
     *
     * @return
     */
    String getToolTipName();

    /**
     * Item tooltip info localization key
     *
     * @return
     */
    String getToolTipInfo();

    /**
     * Required upgrade to use the action
     *
     * @return
     */
    ToolUpgrade getUpgradeRequired();
}
