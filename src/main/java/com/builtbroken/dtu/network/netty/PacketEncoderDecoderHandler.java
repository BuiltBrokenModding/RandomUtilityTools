package com.builtbroken.dtu.network.netty;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.network.IPacket;
import com.builtbroken.dtu.network.packet.PacketPlayerItem;
import com.builtbroken.dtu.network.packet.client.PacketSpawnParticle;
import com.builtbroken.dtu.network.packet.trigger.PacketMouse;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author tgame14
 * @since 31/05/14
 */
public class PacketEncoderDecoderHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public boolean silenceStackTrace = false; //TODO add command and config

    private int nextID = 0;

    public PacketEncoderDecoderHandler()
    {
        addPacket(PacketPlayerItem.class);
        addPacket(PacketSpawnParticle.class);
        addPacket(PacketMouse.class);
    }

    public void addPacket(Class<? extends IPacket> clazz)
    {
        addDiscriminator(nextID++, clazz);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket packet, ByteBuf target) throws Exception
    {
        try
        {
            packet.encodeInto(ctx, target);
        }
        catch (Exception e)
        {
            if (!silenceStackTrace)
            {
                DTUMod.logger.error("Failed to encode packet " + packet, e);
            }
            else
            {
                DTUMod.logger.error("Failed to encode packet " + packet + " E: " + e.getMessage());
            }
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket packet)
    {
        try
        {
            packet.decodeInto(ctx, source);
        }
        catch (Exception e)
        {
            if (!silenceStackTrace)
            {
                DTUMod.logger.error("Failed to decode packet " + packet, e);
            }
            else
            {
                DTUMod.logger.error("Failed to decode packet " + packet + " E: " + e.getMessage());
            }
        }
    }
}
