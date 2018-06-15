package com.builtbroken.dtu.network.packet.client;

import com.builtbroken.dtu.DTUMod;
import com.builtbroken.dtu.network.IPacket;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/15/2018.
 */
public class PacketSpawnParticle implements IPacket
{
    public int dim;
    public double x;
    public double y;
    public double z;
    public double vx;
    public double vy;
    public double vz;

    public String particle;

    public PacketSpawnParticle()
    {
        //Empty for packet creation
    }

    public PacketSpawnParticle(int dim, double x, double y, double z, double vx, double vy, double vz, String particle)
    {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.particle = particle;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(dim);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        ByteBufUtils.writeUTF8String(buffer, particle);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        dim = buffer.readInt();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        particle = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
       if(player != null && player.worldObj.provider.dimensionId == dim)
       {
           DTUMod.sideProxy.spawnParticle(particle, x, y, z, vx, vy, vz);
       }
    }
}
