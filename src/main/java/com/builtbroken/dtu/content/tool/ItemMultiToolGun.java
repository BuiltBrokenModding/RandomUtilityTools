package com.builtbroken.dtu.content.tool;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.content.upgrade.ToolUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    @SideOnly(Side.CLIENT)
    private HashMap<ToolAction, IIcon> textures = new HashMap();

    public ItemMultiToolGun()
    {
        setCreativeTab(DTUMod.creativeTab);
        setUnlocalizedName(DTUMod.PREFX + "multiTool");
        setTextureName(DTUMod.PREFX + "multitool/multitool");
        setMaxStackSize(1);
        setHasSubtypes(true);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean p_77624_4_)
    {
        ToolMode mode = getMode(stack);
        ToolAction action = getAction(stack);

        lines.add("Mode: " + mode.name());
        lines.add("Action: " + (action != null ? action.localization : "null"));
    }

    @Override
    public void onCreated(ItemStack p_77622_1_, World p_77622_2_, EntityPlayer p_77622_3_)
    {
        //TODO save owner ID
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 0;
    }

    @Override //TODO implement charge mechanic
    public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_)
    {
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            ToolAction action = getAction(stack);
            if (action != null)
            {
                int range = action.getRange(player, stack);
                MovingObjectPosition rayHit = traceBlocks(world, player, range, action.rayFluids());
                if (rayHit != null && rayHit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    action.onHitBlock(player, stack, world, rayHit.blockX, rayHit.blockY, rayHit.blockZ, rayHit.sideHit);
                }
            }
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
        if (!world.isRemote)
        {
            ToolAction action = getAction(stack);
            if (action != null)
            {
                action.onHitBlock(player, stack, world, x, y, z, side);
            }
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        ItemStack completeItem = new ItemStack(item, 1, 0);

        int[] upgrades = new int[ToolUpgrade.values().length];
        for (ToolUpgrade upgrade : ToolUpgrade.values())
        {
            upgrades[upgrade.ordinal()] = upgrade.ordinal();
        }

        setUpgradeArray(completeItem, upgrades);

        list.add(completeItem);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        ToolAction action = getAction(stack);
        if (action != null)
        {
            return action.localization;
        }
        return super.getUnlocalizedName(stack);
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
        ToolMode nextMode = currentMode;
        do
        {
            nextMode = forward ? nextMode.next() : nextMode.prev();
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

    protected ToolAction getAction(ItemStack stack)
    {
        ToolMode toolMode = getMode(stack);
        int subMode = getSubMode(stack);
        int maxModes = getSubModes(stack);
        if (subMode >= 0 && subMode < maxModes)
        {
            if (subMode < toolMode.toolActions.size())
            {
                return toolMode.toolActions.get(subMode);
            }
        }
        else if (subMode != 0)
        {
            setSubMode(stack, 0);
        }
        return null;
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

    public void setUpgradeArray(ItemStack stack, int[] upgrades)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setIntArray(NBT_UPGRADES, upgrades);
    }

    public void addUpgrade(ItemStack stack, ToolUpgrade upgrade)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        //Get array and expand
        int[] upgrades = getUpgradeArray(stack);
        upgrades = Arrays.copyOf(upgrades, upgrades.length + 1);

        //Add upgrade to end
        upgrades[upgrades.length - 1] = upgrade.ordinal();

        //Sort so upgrades are in order
        Arrays.sort(upgrades);
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

    protected MovingObjectPosition traceBlocks(World world, EntityPlayer player, double distance, boolean traceWater)
    {
        //Rotation
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
        float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);

        //Position
        double px = player.prevPosX + (player.posX - player.prevPosX);
        double py = player.prevPosY + (player.posY - player.prevPosY)
                + (double) (world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight());
        double pz = player.prevPosZ + (player.posZ - player.prevPosZ);

        //Convert rotation into a vector
        float f3 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-pitch * 0.017453292F);
        float vy = MathHelper.sin(-pitch * 0.017453292F);
        float vx = f4 * f5;
        float vz = f3 * f5;

        //Generate start and end of ray
        Vec3 start = Vec3.createVectorHelper(px, py, pz);
        Vec3 end = start.addVector((double) vx * distance, (double) vy * distance, (double) vz * distance);

        //Ray trace
        return world.func_147447_a(start, end, traceWater, !traceWater, false);
    }

    //<editor-fold desc="icons">

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        ToolAction action = getAction(stack);
        if (action != null)
        {
            String textureName = action.getIconName(getIconString());
            if (textureName != null)
            {
                IIcon icon = textures.get(action);
                if (icon != null)
                {
                    return icon;
                }
            }
        }
        return itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.itemIcon = reg.registerIcon(this.getIconString());

        for (ToolMode mode : ToolMode.values())
        {
            for (ToolAction action : mode.toolActions)
            {
                String textureName = action.getIconName(getIconString());
                if (textureName != null)
                {
                    textures.put(action, reg.registerIcon(textureName));
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 1;
    }

    //</editor-fold>
}
