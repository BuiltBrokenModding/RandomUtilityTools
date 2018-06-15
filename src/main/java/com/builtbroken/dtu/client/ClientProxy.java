package com.builtbroken.dtu.client;

import com.builtbroken.dtu.CommonProxy;
import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.network.netty.PacketSystem;
import com.builtbroken.dtu.network.packet.trigger.PacketMouse;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/14/2018.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void mouseEvent(MouseEvent e)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null && stack.getItem() == DTUMod.itemMultiToolGun)
        {
            if (player.isSneaking() && e.dwheel != 0)
            {
                boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
                PacketSystem.INSTANCE.sendToServer(new PacketMouse(player.inventory.currentItem, ctrl, e.dwheel > 0));
                e.setCanceled(true);
            }
        }
    }
}
