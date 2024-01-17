package dev.zontreck.essentials.homes;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomesSuggestionProvider {
    public static SuggestionProvider<CommandSourceStack> PROVIDER = (ctx,suggestionsBuilder)->{
        Homes homes = HomesProvider.getHomesForPlayer(ctx.getSource().getPlayerOrException().getUUID().toString());

        List<String> homesList = new ArrayList<>();

        for(Home home : homes.getList())
        {
            homesList.add(home.homeName);
        }


        return SharedSuggestionProvider.suggest((String[]) homesList.toArray(), suggestionsBuilder);
    };
}
