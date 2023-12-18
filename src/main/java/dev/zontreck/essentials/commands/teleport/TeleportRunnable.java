package dev.zontreck.essentials.commands.teleport;


import dev.zontreck.ariaslib.terminal.Task;
import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.events.TeleportEvent;
import net.minecraftforge.common.MinecraftForge;

public class TeleportRunnable extends Task
{
    
    public final TeleportContainer Action;
    public TeleportRunnable(TeleportContainer cont){
        super("TP",true);
        Action = cont;
    }

    @Override
    public void run() {

        if(MinecraftForge.EVENT_BUS.post(new TeleportEvent(Action)))
        {
            return;
        }

        Action.PlayerInst.teleportTo(Action.Dimension, Action.Position.x, Action.Position.y, Action.Position.z, Action.Rotation.y, Action.Rotation.x);

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
