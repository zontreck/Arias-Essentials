package dev.zontreck.essentials.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import dev.zontreck.libzontreck.chestgui.ChestGUIIdentifier;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;

/**
 * This is added because of a petty complaint about the effects being annoying. I've added it so someone can opt out if they are truly just that impatient.
 */
public class TPEffectsCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("tpeffects").then(Commands.argument("enable", BoolArgumentType.bool()).executes(x->tpeffects(x.getSource(), BoolArgumentType.getBool(x, "enable")))));
    }

    public static int tpeffects(CommandSourceStack source, boolean enabled)
    {
        ServerPlayer player = source.getPlayer();

        try {
            Profile prof = Profile.get_profile_of(player.getStringUUID());
            prof.NBT.putBoolean("tpeffects", enabled);

            return 0;
        } catch (UserProfileNotYetExistsException e) {
            throw new RuntimeException(e);
        }

    }
}
