package dev.zontreck.essentials.rtp;

import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.time.Instant;

/**
 * The factory system used to start searching for a random teleport position
 */
public class RandomPositionFactory {

    public static RTP beginRTPSearch(ServerLevel level)
    {
        RTP tmp = new RTP(level);
        tmp.position = new WorldPosition(new Vector3(0,0,0), WorldPosition.getDim(level));
        Thread tx = new Thread(new RandomPositionLocator(tmp));
        tx.setName("RTPTask");
        tx.start();

        return tmp;
    }

    public static void beginRTP(ServerPlayer player, ServerLevel level)
    {
        RTP tmp = RTP.getRTP(level);
        if(tmp == null)
        {
            throw new RuntimeException("No valid destinations were found");
        }

        TeleportActioner.ApplyTeleportEffect(player);
        TeleportContainer cont = new TeleportContainer(player, tmp.position.Position.asMinecraftVector(), player.getRotationVector(), level);

        TeleportActioner.PerformTeleport(cont, false);
    }
}
