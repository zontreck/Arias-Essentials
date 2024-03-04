package dev.zontreck.essentials.data;


import dev.zontreck.essentials.AriasEssentials;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AriasEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDatagen
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();

        ExistingFileHelper helper = event.getExistingFileHelper();

        gen.addProvider(true, new ModBlockStatesProvider(output, helper));
        gen.addProvider(true, new ModItemModelsProvider(output,helper));
        gen.addProvider(true, ModLootTablesProvider.create(output));
    }
}