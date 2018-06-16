package com.builtbroken.dtu.content.tool.actions;

import com.builtbroken.dtu.content.tool.actions.blocks.ToolActionConvertBlock;
import com.builtbroken.dtu.api.tool.ToolUpgrade;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public class ToolActionFreeze extends ToolActionConvertBlock
{
    public ToolActionFreeze()
    {
        super("freezer", ToolUpgrade.FREEZE);
    }

    @Override
    public boolean rayFluids()
    {
        return true;
    }
}
