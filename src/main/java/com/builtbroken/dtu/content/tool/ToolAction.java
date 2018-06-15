package com.builtbroken.dtu.content.tool;

import com.builtbroken.dtu.content.tool.actions.ActionResult;
import com.builtbroken.dtu.content.upgrade.ToolUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
public class ToolAction
{
    public final String localization;
    public final ToolUpgrade upgradeRequired;

    public ToolAction(String localization, ToolUpgrade upgradeRequired)
    {
        this.localization = localization;
        this.upgradeRequired = upgradeRequired;
    }

    /**
     * Cost to run the tool, per tick
     *
     * @param player
     * @param tool
     * @return
     */
    public int getEnergyCost(EntityPlayer player, ItemStack tool)
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
    public int getActionCost(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        return 0;
    }

    /**
     * Should the tool path water
     *
     * @return true to include water blocks
     */
    public boolean rayFluids()
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
    public int getRange(EntityPlayer player, ItemStack tool)
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
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        return ActionResult.PASS;
    }
}
