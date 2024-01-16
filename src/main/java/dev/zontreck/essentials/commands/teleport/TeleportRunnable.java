package dev.zontreck.essentials.commands.teleport;


import dev.zontreck.ariaslib.terminal.Task;
import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.events.TeleportEvent;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraftforge.common.MinecraftForge;

public class TeleportRunnable extends Task
{

    public final boolean IgnoreEvent;
    public final TeleportContainer Action;
    public TeleportRunnable(TeleportContainer cont, boolean eventless){
        super("TP",true);
        Action = cont;
        IgnoreEvent=eventless;
    }

    @Override
    public void run() {
        Action.OldPosition = new WorldPosition(Action.PlayerInst);

        if(!IgnoreEvent)
        {

            if(MinecraftForge.EVENT_BUS.post(new TeleportEvent(Action)))
            {
                return;
            }
        }

        Action.PlayerInst.teleportTo(Action.Dimension, Action.Position.x, Action.Position.y, Action.Position.z, Action.Rotation.y, Action.Rotation.x);

        Action.PlayerInst.onUpdateAbilities();

        DelayedExecutorService.getInstance().schedule(new Task("tp_action",true){
            public TeleportContainer container=Action;
            @Override
            public void run()
            {
                container.PlayerInst.onUpdateAbilities();
                container.PlayerInst.setPos(container.Position);
                container.PlayerInst.giveExperiencePoints(1);
            }
        }, 1);
    }
}
