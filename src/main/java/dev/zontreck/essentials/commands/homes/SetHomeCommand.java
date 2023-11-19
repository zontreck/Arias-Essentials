package dev.zontreck.essentials.commands.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.Messages;
import dev.zontreck.essentials.commands.teleport.TeleportDestination;
import dev.zontreck.essentials.homes.Home;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector2;
import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class SetHomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("sethome").executes(c->setHome(c.getSource(), "default")).then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> setHome(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setHome(CommandSourceStack ctx, String homeName)
    {
        // Request homes
//        String homeName = "";
//        CommandSourceStack ctx = ctx2.getSource();
//        homeName = StringArgumentType.getString(ctx2, "nickname");
//        if(homeName==null)return 0;
        
        ServerPlayer p;
        try {
            p = ctx.getPlayerOrException();
            Vec3 position = p.position();
            Vec2 rot = p.getRotationVector();
    
            TeleportDestination dest = new TeleportDestination(new Vector3(position), new Vector2(rot), p.serverLevel());
    
            Home newhome = new Home(p, homeName, dest);
            AriasEssentials.player_homes.get(p.getUUID()).add(newhome);
            
                
            ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(Messages.HOME_CREATE_SUCCESS), ctx.getServer());
        } catch (CommandSyntaxException e) {
            
            ChatHelpers.broadcastTo(ctx.getEntity().getUUID(), ChatHelpers.macro(Messages.HOME_CREATE_FAIL), ctx.getServer());
            e.printStackTrace();
        }
        

        return 0;
    }
    
}
