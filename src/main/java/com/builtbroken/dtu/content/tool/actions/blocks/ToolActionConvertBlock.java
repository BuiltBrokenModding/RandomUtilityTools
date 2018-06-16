package com.builtbroken.dtu.content.tool.actions.blocks;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.api.tool.IBlockEffect;
import com.builtbroken.dtu.api.tool.IToolActionConvertBlocks;
import com.builtbroken.dtu.content.tool.actions.imp.ActionResult;
import com.builtbroken.dtu.content.tool.actions.imp.ToolAction;
import com.builtbroken.dtu.api.tool.ToolUpgrade;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/15/2018.
 */
public class ToolActionConvertBlock extends ToolAction implements IToolActionConvertBlocks
{
    public final HashMap<Block, HashMap<Integer, IBlockEffect>> conversionMap = new HashMap();

    public ToolActionConvertBlock(String name, ToolUpgrade upgradeRequired)
    {
        super(name, upgradeRequired);
    }

    @Override
    public ActionResult onHitBlock(EntityPlayer player, ItemStack tool, World world, int x, int y, int z, int side)
    {
        if (!world.isRemote)
        {
            if (player.canPlayerEdit(x, y, z, side, tool))
            {
                Block block = world.getBlock(x, y, z);
                int meta = world.getBlockMetadata(x, y, z);

                if (conversionMap.containsKey(block))
                {
                    IBlockEffect newBlock = getEffect(block, meta);
                    if (newBlock != null)
                    {
                        if (newBlock.apply(world, x, y, z, 3))
                        {
                            playEffect(world, x, y, z);
                        }
                        return ActionResult.STOP;
                    }
                }
            }
            return ActionResult.NO_RUN;
        }
        return ActionResult.CONTINUE;
    }

    public IBlockEffect getEffect(Block block, int meta)
    {
        if (conversionMap.containsKey(block))
        {
            HashMap<Integer, IBlockEffect> map = conversionMap.get(block);
            if (map != null)
            {
                IBlockEffect effect = map.get(meta);
                if (effect == null)
                {
                    effect = map.get(-1);
                }
                return effect;
            }
        }
        return null;
    }

    protected void playEffect(World world, int x, int y, int z)
    {
        world.playSoundEffect(x, y, z, "random.click", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
    }

    @Override
    public void addConversion(Block block, IBlockEffect effect)
    {
        addConversion(block, -1, effect);
    }

    @Override
    public void addConversion(Block block, int meta, Block newBlock, int newMeta)
    {
        if (newBlock == null)
        {
            DTUMod.logger.error("ToolActionConvertBlock#addConversion(" + block + ", " + meta + ", >" + newBlock + "<, " + newMeta + ") -> output block is null... ignoring conversion to prevent crash.");
            return;
        }
        if (newMeta < 0 || newMeta >= 16)
        {
            DTUMod.logger.error("ToolActionConvertBlock#addConversion(" + block + ", " + meta + ", " + newBlock + ", >" + newMeta + "<) -> invalid meta value, must be between 0-15... ignoring conversion to prevent crash.");
            return;
        }
        addConversion(block, meta, new BlockConversion(newBlock, newMeta));
    }

    @Override
    public void addConversion(Block block, int meta, IBlockEffect effect)
    {
        if (block == null)
        {
            DTUMod.logger.error("ToolActionConvertBlock#addConversion(>" + block + "<, " + meta + ", " + effect + ") -> input block is null... ignoring conversion to prevent crash.");
            return;
        }
        if (meta < -1 || meta >= 16)
        {
            DTUMod.logger.error("ToolActionConvertBlock#addConversion(" + block + ", >" + meta + "<, " + effect + ") -> invalid meta value, must be between 0-15 or -1 to ignore meta... ignoring conversion to prevent crash.");
            return;
        }

        if (!conversionMap.containsKey(block))
        {
            conversionMap.put(block, new HashMap());
        }

        HashMap<Integer, IBlockEffect> map = conversionMap.get(block);
        map.put(meta, effect);

        conversionMap.put(block, map);
    }
}
