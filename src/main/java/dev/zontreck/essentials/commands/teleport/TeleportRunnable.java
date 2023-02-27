package dev.zontreck.essentials.commands.teleport;

import dev.zontreck.libzontreck.util.DelayedExecutorService;

public class TeleportRunnable implements Runnable
{
    
    public final TeleportContainer Action;
    public TeleportRunnable(TeleportContainer cont){
        Action = cont;
    }

    @Override
    public void run() {
        Action.PlayerInst.teleportTo(Action.Dimension, Action.Position.x, Action.Position.y, Action.Position.z, Action.Rotation.y, Action.Rotation.x);

        DelayedExecutorService.getInstance().schedule(new Runnable(){
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
