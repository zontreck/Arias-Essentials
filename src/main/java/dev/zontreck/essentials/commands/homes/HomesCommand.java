package dev.zontreck.essentials.commands.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportActioner;
import dev.zontreck.essentials.commands.teleport.TeleportContainer;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.homes.Home;
import dev.zontreck.essentials.homes.Homes;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.libzontreck.chestgui.ChestGUI;
import dev.zontreck.libzontreck.chestgui.ChestGUIButton;
import dev.zontreck.libzontreck.chestgui.ChestGUIIdentifier;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector2i;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HomesCommand {
    private static final ChestGUIIdentifier HOMES_GUI_ID = new ChestGUIIdentifier("homes-gui");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("homes").executes(HomesCommand::getHomes));
    }

    private static int getHomes(CommandContext<CommandSourceStack> ctx)
    {
        // Request homes
        try{
            ServerPlayer player = ctx.getSource().getPlayerOrException();

            Homes homes = AriasEssentials.player_homes.get(player.getUUID());

            ChatHelpers.broadcastTo(player.getUUID(), ChatHelpers.macro(Messages.HOME_COUNT, String.valueOf(homes.count())), player.server);

            ChestGUI gui = ChestGUI.builder().withGUIId(HOMES_GUI_ID).withTitle("Homes").withPlayer(player.getUUID());

            int iconX = 0;
            int iconY = 0;
            
            for (Home string : homes.getList()) {
                Style st = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(HoverTip.get(ChatHelpers.macroize(Messages.HOME_HOVER_TEXT))).withClickEvent(Clickable.command("/home "+string.homeName));

                ItemStack stack = string.homeIcon.copy();
                if(stack.is(Items.AIR))
                {
                    stack = new ItemStack(Items.GRASS_BLOCK, 1);
                }
                stack.setHoverName(Component.literal(string.homeName));

                ChestGUIButton button = new ChestGUIButton(stack, (stackx, container, lore)-> {

                    TeleportDestination dest = string.destination;
                    TeleportActioner.ApplyTeleportEffect(player);
                    TeleportContainer cont = new TeleportContainer(player, dest.Position.asMinecraftVector(), dest.Rotation.asMinecraftVector(), dest.getActualDimension());
                    TeleportActioner.PerformTeleport(cont, false);
                    gui.close();
                }, new Vector2i(iconX, iconY))
                        .withInfo(new LoreEntry.Builder().text(ChatColor.doColors("!Dark_Green!Click here to go to this home")).build())
                        .withInfo(new LoreEntry.Builder().text(ChatHelpers.macro("!Dark_Purple!This home is in the dimension [0]", string.destination.Dimension).getString()).bold(true).build());

                iconY++;
                if(iconY>=9)
                {
                    iconY=0;
                    iconX++;
                }
                if(homes.count() > (2*9))
                    ChatHelpers.broadcastTo(player.getUUID(), ChatHelpers.macro(Messages.HOME_FORMAT, string.homeName).setStyle(st), ctx.getSource().getServer());
                else
                    gui.withButton(button); // Put this in the else case, to prevent a error when exceeding inventory slots
                
            }

            if(homes.count()<=27)
                gui.open();
        }catch(CommandSyntaxException ex)
        {
            ex.printStackTrace();

        }



        return 0;
    }
    
}
