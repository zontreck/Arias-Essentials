package dev.zontreck.essentials.client;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class Keybindings {
    public static final String KEY_CATEGORY_ESSENTIALS = "key.category.ariasessentials";
    public static final String KEY_AUTOWALK = "key.ariasessentials.autowalk";

    public static final KeyMapping AUTOWALK = createKeyMapping(KEY_AUTOWALK, InputConstants.KEY_CAPSLOCK, KEY_CATEGORY_ESSENTIALS);

    private static KeyMapping createKeyMapping(String name, int keycode, String category){
        final KeyMapping key = new KeyMapping(name, keycode, category);
        return key;
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event)
    {
        event.register(AUTOWALK);
    }
}

