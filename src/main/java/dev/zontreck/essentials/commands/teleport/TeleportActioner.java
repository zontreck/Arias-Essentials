package dev.zontreck.essentials.commands.teleport;

import dev.zontreck.ariaslib.util.DelayedExecutorService;
import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.configs.server.AEServerConfig;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TeleportActioner
{
    public static void PerformTeleport(TeleportContainer contain, boolean eventless){
        //sub_runnable run = new sub_runnable(contain);
        DelayedExecutorService.getInstance().schedule(new TeleportRunnable(contain, eventless), 2);
    }

    public static boolean isBlacklistedDimension(ServerLevel level)
    {
        WorldPosition pos = new WorldPosition(Vector3.ZERO, level);
        if(AEServerConfig.getInstance().teleport.Blacklist.contains(pos.Dimension))
        {
            return true;
        } else return false;
    }

    public static void ApplyTeleportEffect(ServerPlayer player){
        if(isBlacklistedDimension(player.serverLevel())){
            return;
        }
        // 10/05/2022 - Thinking ahead here to future proof it so i can do things in threads safely
        // By adding this task onto the main server thread, any thread can call the TeleportActioner and it will be actioned on the main thread without needing to repeat the process of sending this to the server thread.
        player.server.execute(new Runnable(){
            public void run(){

                // 12/18/2023 - Updated to store effects in the config, and make duration and amplifier random!
                var effects = AEServerConfig.getInstance().teleport.Effects;
                for(int i = 0; i < effects.size(); i++) {
                    RegistryObject<MobEffect> effect = RegistryObject.create(new ResourceLocation(effects.get(i)), ForgeRegistries.MOB_EFFECTS);

                    int duration = AriasEssentials.random.nextInt(5, 10) * 20;
                    int amplifier = AriasEssentials.random.nextInt(1, 3);

                    if (effects.get(i).equals("minecraft:slow_falling"))
                    {
                        duration = duration*2;
                    }

                    MobEffectInstance inst = new MobEffectInstance(effect.get(), duration, amplifier, true, true);

                    player.addEffect(inst);
                }
                
            }
        });
    }
}
