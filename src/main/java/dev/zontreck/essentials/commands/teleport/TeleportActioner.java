package dev.zontreck.essentials.commands.teleport;

import dev.zontreck.ariaslib.util.DelayedExecutorService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class TeleportActioner
{
    public static void PerformTeleport(TeleportContainer contain){
        //sub_runnable run = new sub_runnable(contain);
        DelayedExecutorService.getInstance().schedule(new TeleportRunnable(contain), 2);
    }

    public static void ApplyTeleportEffect(ServerPlayer player){
        
        // 10/05/2022 - Thinking ahead here to future proof it so i can do things in threads safely
        // By adding this task onto the main server thread, any thread can call the TeleportActioner and it will be actioned on the main thread without needing to repeat the process of sending this to the server thread.
        player.server.execute(new Runnable(){
            public void run(){

                MobEffectInstance inst = new MobEffectInstance(MobEffects.DARKNESS, 250, 1, true, true);
                // 02/26/2023 - Adjusted to 400 due to 1.18.2, the teleport is slightly more delayed, and thus a regen is needed incase levitation runs out too soon
                // 05/15/2023 - Removed regen effect and replaced with feather fall.
                //MobEffectInstance regen = new MobEffectInstance(MobEffects.REGENERATION, 400, 2, true, true);

                // 10-05-2022   - Adjusted to 100 on duration due to a small issue where it would sometimes stop levitation prior to the teleport taking effect.
                // 02/26/2023   - Adjusted to 200 on duration due to 1.18.2 causing levitation to run out too quickly before teleport
                // Small tradeoff is the player now levitates slightly longer at the destination. This is acceptable. Compensated by increasing regen strength by 1
                MobEffectInstance levitate = new MobEffectInstance(MobEffects.LEVITATION, 200, 1, true, true);

                // 05/15/2023 - Add feather falling as a effect that lasts longer than levitate to avoid damaging the player.
                MobEffectInstance feathers = new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 2, true, true);
        
                
                player.addEffect(inst);
                player.addEffect(feathers);
                player.addEffect(levitate); // ensure the player can't fall into lava in the short time we are not in control (if the player was silly enough to make a home above lava!!!)
                
            }
        });
    }
}
