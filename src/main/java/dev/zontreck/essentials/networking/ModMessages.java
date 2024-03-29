package dev.zontreck.essentials.networking;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.networking.packets.s2c.S2CUpdateHearts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int PACKET_ID = 0;
    private static int id()
    {
        return PACKET_ID++;
    }

    public static void register()
    {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(AriasEssentials.MODID, "messages"))
                .networkProtocolVersion(()-> "1.0")
                .clientAcceptedVersions(s->true)
                .serverAcceptedVersions(s->true)
                .simpleChannel();

        INSTANCE=net;


        net.messageBuilder(S2CUpdateHearts.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                        .decoder(S2CUpdateHearts::new)
                                .encoder(S2CUpdateHearts::toBytes)
                                        .consumerMainThread(S2CUpdateHearts::handle)
                                                .add();

    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player), message);
    }

    public static <MSG> void sendToAll(MSG message)
    {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
