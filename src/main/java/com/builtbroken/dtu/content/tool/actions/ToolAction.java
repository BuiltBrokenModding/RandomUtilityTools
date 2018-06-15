package com.builtbroken.dtu.content.tool.actions;

import com.builtbroken.dtu.DTUMod;
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
    private final String name;
    private final String itemLocalKey;
    private final String toolTipLocalKey;
    private final String toolTipLocalInfo;
    private final ToolUpgrade upgradeRequired;

    public ToolAction(String name, ToolUpgrade upgradeRequired)
    {
        this.name = name;
        this.itemLocalKey = DTUMod.itemMultiToolGun.getUnlocalizedName() + "." + name;
        this.toolTipLocalKey = DTUMod.itemMultiToolGun.getUnlocalizedName() + ".action." + name + ".name";
        this.toolTipLocalInfo = DTUMod.itemMultiToolGun.getUnlocalizedName() + ".action." + name + ".info";
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

    public String getIconName(String iconName)
    {
        return iconName + "." + getName();
    }

    public String getName()
    {
        return name;
    }

    public String getItemName()
    {
        return itemLocalKey;
    }

    public String getToolTipName()
    {
        return toolTipLocalKey;
    }

    public String getToolTipInfo()
    {
        return toolTipLocalInfo;
    }

    public ToolUpgrade getUpgradeRequired()
    {
        return upgradeRequired;
    }
}
