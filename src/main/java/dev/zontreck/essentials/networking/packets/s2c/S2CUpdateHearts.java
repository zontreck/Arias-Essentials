package dev.zontreck.essentials.networking.packets.s2c;

import dev.zontreck.essentials.configs.AEClientConfig;
import dev.zontreck.libzontreck.networking.packets.IPacket;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class S2CUpdateHearts
{
    public boolean current;

    public S2CUpdateHearts(FriendlyByteBuf buf)
    {
        current = buf.readBoolean();
    }

    public S2CUpdateHearts(boolean current)
    {
        this.current=current;
    }

    public S2CUpdateHearts(){}

    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(current);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx=supplier.get();

        ctx.enqueueWork(()->{
            AEClientConfig.ENABLE_HEARTS_RENDER.set(current);
            AEClientConfig.ENABLE_HEARTS_RENDER.save();
        });

        return true;
    }
}
