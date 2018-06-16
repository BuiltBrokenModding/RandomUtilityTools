package com.builtbroken.dtu.api.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum of supported tool modes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
public enum ToolMode //Eventually the enum will be replaced with a registry to allow add new modes
{
    //Disable mod, or no upgrades
    OFF,
    FREEZE(ToolUpgrade.FREEZE),
    MELT(ToolUpgrade.MELT),
    FLUID(ToolUpgrade.FLUID_VAC, ToolUpgrade.FLUID_PLACE, ToolUpgrade.FLUID_DELETE),
    COLOR(ToolUpgrade.COLOR);

    public final ToolUpgrade[] upgrades;
    public List<IToolAction> toolActions = new ArrayList();

    ToolMode(ToolUpgrade... upgrades)
    {
        this.upgrades = upgrades;
    }

    public static ToolMode get(int value)
    {
        if (value >= 0 && value < values().length)
        {
            return values()[value];
        }
        return OFF;
    }

    public ToolMode next()
    {
        int i = ordinal() + 1;
        if (i >= values().length)
        {
            i = 0;
        }
        return values()[i];
    }

    public ToolMode prev()
    {
        int i = ordinal() - 1;
        if (i < 0)
        {
            i = values().length - 1;
        }
        return values()[i];
    }
}
