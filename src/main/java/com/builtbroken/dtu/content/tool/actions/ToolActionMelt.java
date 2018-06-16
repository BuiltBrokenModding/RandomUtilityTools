package com.builtbroken.dtu.content.tool.actions;

import com.builtbroken.dtu.content.tool.actions.blocks.ToolActionConvertBlock;
import com.builtbroken.dtu.api.tool.ToolUpgrade;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public class ToolActionMelt extends ToolActionConvertBlock
{
    public ToolActionMelt()
    {
        super("melter", ToolUpgrade.MELT);
    }

    @Override
    protected void playEffect(World world, int x, int y, int z)
    {
        world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
    }
}
