package dev.zontreck.essentials.networking;

import dev.zontreck.essentials.configs.AEClientConfig;
import dev.zontreck.libzontreck.networking.packets.IPacket;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class S2CUpdateHearts implements IPacket
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

    @Override
    public void deserialize(CompoundTag compoundTag) {

    }

    @Override
    public void serialize(CompoundTag compoundTag) {

    }

    @Override
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(current);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx=supplier.get();

        ctx.enqueueWork(()->{
            AEClientConfig.ENABLE_HEARTS_RENDER.set(current);
        });

        return true;
    }

    @Override
    public NetworkDirection getDirection() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }

    @Override
    public void register(SimpleChannel simpleChannel) {
        ServerUtilities.registerPacket(simpleChannel, S2CUpdateHearts.class, this, S2CUpdateHearts::new);

    }
}
