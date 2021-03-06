package com.builtbroken.dtu.api.tool;

/**
 * Enum of upgrades used by the MultiTool
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
public enum ToolUpgrade //Eventually the enum will be replaced with a registry to allow add new upgrades
{
    FREEZE,
    MELT,
    FLUID_VAC,
    FLUID_PLACE,
    FLUID_DRAIN,
    FLUID_DELETE,
    COLOR,
    EDIT,
    EDIT_REMOVE_BLOCK,
    EDIT_REPLACE_BLOCK;
}
