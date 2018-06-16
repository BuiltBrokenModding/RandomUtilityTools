package com.builtbroken.dtu;

import com.builtbroken.dtu.api.DtuAPI;
import com.builtbroken.dtu.api.tool.ToolMode;
import com.builtbroken.dtu.api.tool.ToolUpgrade;
import com.builtbroken.dtu.content.tool.ItemMultiToolGun;
import com.builtbroken.dtu.content.tool.actions.ToolActionDye;
import com.builtbroken.dtu.content.tool.actions.ToolActionFreeze;
import com.builtbroken.dtu.content.tool.actions.ToolActionMelt;
import com.builtbroken.dtu.content.tool.actions.fluid.ToolActionFluidDelete;
import com.builtbroken.dtu.content.tool.actions.fluid.ToolActionFluidRemove;
import com.builtbroken.dtu.network.netty.PacketSystem;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
@Mod(modid = DTUMod.DOMAIN, name = "Atomic Science", version = DTUMod.VERSION, dependencies = DTUMod.DEPENDENCIES)
public class DTUMod
{
    public static final String DOMAIN = "darktoolandutilities";
    public static final String PREFX = DOMAIN + ":";

    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String MC_VERSION = "@MC@";
    public static final String VERSION = MC_VERSION + "-" + MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;
    public static final String DEPENDENCIES = "";

    @Mod.Instance(DOMAIN)
    public static DTUMod INSTANCE;

    public static Logger logger = LogManager.getLogger(DOMAIN);

    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    public static CreativeTabs creativeTab;

    @SidedProxy(clientSide = "com.builtbroken.dtu.client.ClientProxy", serverSide = "com.builtbroken.dtu.server.ServerProxy")
    public static CommonProxy sideProxy;

    public static File configFolder;

    public static ItemMultiToolGun itemMultiToolGun;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configFolder = new File(event.getModConfigurationDirectory(), "/bbm/darks_tools_and_utilities");

        //Create tab
        creativeTab = new CreativeTabs(DOMAIN)
        {
            @Override
            public Item getTabIconItem()
            {
                return itemMultiToolGun;
            }
        };

        //Register items
        GameRegistry.registerItem(itemMultiToolGun = new ItemMultiToolGun(), "multiToolGun");

        //Register tool modes
        ToolMode.MELT.toolActions.add(DtuAPI.MELT_BLOCK_ACTION = new ToolActionMelt());
        ToolMode.FREEZE.toolActions.add(DtuAPI.FREEZE_BLOCK_ACTION = new ToolActionFreeze());

        ToolMode.FLUID.toolActions.add(new ToolActionFluidRemove("fluid.vac", ToolUpgrade.FLUID_VAC, true));
        ToolMode.FLUID.toolActions.add(new ToolActionFluidRemove("fluid.drain", ToolUpgrade.FLUID_DRAIN, false));
        ToolMode.FLUID.toolActions.add(new ToolActionFluidDelete());

        for (int i = 0; i < ItemDye.field_150923_a.length; i++)
        {
            ToolMode.PAINT.toolActions.add(new ToolActionDye(ItemDye.field_150923_a[i], i));
        }

        sideProxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt)
    {
        PacketSystem.INSTANCE.init();

        DtuAPI.MELT_BLOCK_ACTION.addConversion(Blocks.cobblestone, Blocks.stone);
        DtuAPI.MELT_BLOCK_ACTION.addConversion(Blocks.sand, Blocks.glass);
        DtuAPI.MELT_BLOCK_ACTION.addConversion(Blocks.ice, Blocks.water);
        DtuAPI.MELT_BLOCK_ACTION.addConversion(Blocks.obsidian, Blocks.lava);

        DtuAPI.FREEZE_BLOCK_ACTION.addConversion(Blocks.lava, Blocks.obsidian);
        DtuAPI.FREEZE_BLOCK_ACTION.addConversion(Blocks.water, Blocks.ice);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {

    }
}
