package dev.zontreck.essentials.client;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.Instant;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = AriasEssentials.MODID, value = Dist.CLIENT)
public class AutoWalk {
    private static boolean isWalking = false;
    private static boolean autoJump = false;
    static Thread runner;
    static long lastPress;

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        if(Keybindings.AUTOWALK.matches(event.getKey(), event.getScanCode()) && Minecraft.getInstance().screen == null && Keybindings.AUTOWALK.isDown())
        {
            lastPress = Instant.now().getEpochSecond();
            if(isWalking)
            {
                stopWalking();
            } else startWalking();
        }
    }


    private static void startWalking() {
        isWalking=true;
        autoJump = Minecraft.getInstance().options.autoJump().get();
        Minecraft.getInstance().options.autoJump().set(true);


        Minecraft.getInstance().player.displayClientMessage(ChatHelpers.macro(Messages.ESSENTIALS_PREFIX + "!Dark_Green!AutoWalking started"), false);

        runner = new Thread(()->{
            while(AutoWalk.isWalking)
            {
                if(!Minecraft.getInstance().options.keyUp.isDown())
                    Minecraft.getInstance().options.keyUp.setDown(true);
            }
        });
        runner.start();
    }

    private static void stopWalking() {
        isWalking=false;
        runner.interrupt();
        runner=null;
        Minecraft.getInstance().options.autoJump().set(autoJump);
        Minecraft.getInstance().options.keyUp.setDown(false);

        Minecraft.getInstance().player.displayClientMessage(ChatHelpers.macro(Messages.ESSENTIALS_PREFIX + "!Dark_Green!AutoWalking stopped"), false);

    }
}
