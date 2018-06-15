package com.builtbroken.dtu.network.netty;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.network.IPacket;
import com.builtbroken.jlib.data.vector.IPos3D;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.EnumMap;

/**
 * @author tgame14
 * @since 26/05/14
 *
 * Modified for current mod by Dark 5/?/2018
 */
public class PacketSystem
{
    public static PacketSystem INSTANCE = new PacketSystem(DTUMod.DOMAIN);

    public final String channel;
    protected EnumMap<Side, FMLEmbeddedChannel> channelEnumMap;

    public PacketSystem(String channel)
    {
        this.channel = channel;
    }

    public void init()
    {
        channelEnumMap = NetworkRegistry.INSTANCE.newChannel(channel, new PacketEncoderDecoderHandler(), new PacketChannelInboundHandler());
    }

    public Packet toMCPacket(IPacket packet)
    {
        return channelEnumMap.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
    }

    /**
     * @param packet the packet to send to the player
     * @param player the player MP object
     */
    public void sendToPlayer(IPacket packet, EntityPlayerMP player)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
        }
        else
        {
            DTUMod.logger.error("Packet sent to player[" + player + "]");
        }
    }

    /**
     * @param packet the packet to send to the players in the dimension
     * @param dimId  the dimension ID to send to.
     */
    public void sendToAllInDimension(IPacket packet, int dimId)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimId);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
        }
        else
        {
            DTUMod.logger.error("Packet sent to dim[" + dimId + "]");
        }
    }

    public void sendToAllInDimension(IPacket packet, World world)
    {
        sendToAllInDimension(packet, world.provider.dimensionId);
    }

    /**
     * sends to all clients connected to the server
     *
     * @param packet the packet to send.
     */
    public void sendToAll(IPacket packet)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(packet);
        }
        else
        {
            DTUMod.logger.error("Packet sent to all");
        }
    }

    public void sendToAllAround(IPacket message, NetworkRegistry.TargetPoint point)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            this.channelEnumMap.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
            this.channelEnumMap.get(Side.SERVER).writeAndFlush(message);
        }
        else
        {
            DTUMod.logger.error("Packet sent to target point: " + point);
        }
    }

    public void sendToAllAround(IPacket message, World world, IPos3D point, double range)
    {
        sendToAllAround(message, world, point.x(), point.y(), point.z(), range);
    }

    public void sendToAllAround(IPacket message, TileEntity tile)
    {
        sendToAllAround(message, tile, 64);
    }

    public void sendToAllAround(IPacket message, TileEntity tile, double range)
    {
        sendToAllAround(message, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, range);
    }

    public void sendToAllAround(IPacket message, World world, double x, double y, double z, double range)
    {
        if (world != null)
        {
            sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, range));
        }
    }

    public void sendToServer(IPacket packet)
    {
        //Null check is for JUnit
        if (channelEnumMap != null)
        {
            if (this.channelEnumMap.get(Side.CLIENT) != null)
            {
                this.channelEnumMap.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
                this.channelEnumMap.get(Side.CLIENT).writeAndFlush(packet);
            }
            else
            {
                DTUMod.logger.error("PacketManager#sendToServer(packet): Attempted to fire client to server packet on server, this is not allowed. Packet = " + packet);
            }
        }
        else
        {
            DTUMod.logger.error("PacketManager#sendToServer(packet): Channel enum map is empty, can't send packet. Packet = " + packet);
        }
    }
}


