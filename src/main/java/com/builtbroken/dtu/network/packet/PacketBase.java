package com.builtbroken.dtu.network.packet;

import com.builtbroken.dtu.network.IPacket;
import com.builtbroken.jlib.data.network.IByteBufWriter;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTank;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/27/2018.
 */
public class PacketBase implements IPacket
{
    protected List<Object> dataToWrite = new ArrayList();
    protected ByteBuf dataToRead;

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        for (Object object : dataToWrite)
        {
            writeData(object, buffer);
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        dataToRead = buffer.slice().copy();
    }

    /**
     * Called to write data without manually defining the write
     *
     * @param object - object to write
     * @param buffer - location to write to
     */
    protected void writeData(Object object, ByteBuf buffer)
    {
        if (object.getClass().isArray())
        {
            for (int i = 0; i < Array.getLength(object); i++)
            {
                writeData(Array.get(object, i), buffer);
            }
        }
        else if (object instanceof Collection)
        {
            for (Object o : (Collection) object)
            {
                writeData(o, buffer);
            }
        }
        else if (object instanceof Byte)
        {
            buffer.writeByte((Byte) object);
        }
        else if (object instanceof Integer)
        {
            buffer.writeInt((Integer) object);
        }
        else if (object instanceof Short)
        {
            buffer.writeShort((Short) object);
        }
        else if (object instanceof Long)
        {
            buffer.writeLong((Long) object);
        }
        else if (object instanceof Float)
        {
            buffer.writeFloat((Float) object);
        }
        else if (object instanceof Double)
        {
            buffer.writeDouble((Double) object);
        }
        else if (object instanceof Boolean)
        {
            buffer.writeBoolean((Boolean) object);
        }
        else if (object instanceof String)
        {
            ByteBufUtils.writeUTF8String(buffer, (String) object);
        }
        else if (object instanceof NBTTagCompound)
        {
            ByteBufUtils.writeTag(buffer, (NBTTagCompound) object);
        }
        else if (object instanceof ItemStack)
        {
            ByteBufUtils.writeItemStack(buffer, (ItemStack) object);
        }
        else if (object instanceof FluidTank)
        {
            ByteBufUtils.writeTag(buffer, ((FluidTank) object).writeToNBT(new NBTTagCompound()));
        }
        else if (object instanceof IByteBufWriter)
        {
            ((IByteBufWriter) object).writeBytes(buffer);
        }
        else if (object instanceof Enum)
        {
            buffer.writeInt(((Enum) object).ordinal());
        }
        else
        {
            throw new IllegalArgumentException("PacketBase: Unsupported write data type " + object);
        }
    }

    public ByteBuf dataToRead()
    {
        return dataToRead;
    }


    @Override
    public <P extends IPacket> P addData(Object... objects)
    {
        for (Object object : objects)
        {
            dataToWrite.add(object);
        }
        return (P) this;
    }
}
