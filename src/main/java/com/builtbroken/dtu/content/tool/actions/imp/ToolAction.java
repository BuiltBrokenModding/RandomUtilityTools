package com.builtbroken.dtu.content.tool.actions.imp;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.api.tool.IToolAction;
import com.builtbroken.dtu.api.tool.ToolUpgrade;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
public abstract class ToolAction implements IToolAction
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

    @Override
    public String getIconName(String iconName)
    {
        return iconName + "." + getName();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getItemName()
    {
        return itemLocalKey;
    }

    @Override
    public String getToolTipName()
    {
        return toolTipLocalKey;
    }

    @Override
    public String getToolTipInfo()
    {
        return toolTipLocalInfo;
    }

    @Override
    public ToolUpgrade getUpgradeRequired()
    {
        return upgradeRequired;
    }
}
