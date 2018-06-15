package com.builtbroken.dtu.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Applied to an object that received packet server or client side with an ID
 * Created by Darkguardsman on 8/5/2014.
 */
public interface IPacketIDReceiver
{
    /**
     * Called to read the packet when received
     *
     * @param buf    - packet data
     * @param id     - packet ID
     * @param player - player who received or sent the packet
     * @param type   - packet object
     * @return true if the data was read
     */
    boolean read(ByteBuf buf, int id, EntityPlayer player, IPacket type);

    /**
     * Called to check if the packet should be read at all.
     * <p>
     * Use this to validate packet data to prevent users with hacked clients from corrupting data.
     *
     * @param player - player who sent the packet
     * @return true if the packet should be read
     */
    default boolean shouldReadPacket(EntityPlayer player, World world, double x, double y, double z, IPacket packet)
    {
        return true;
    }
}
