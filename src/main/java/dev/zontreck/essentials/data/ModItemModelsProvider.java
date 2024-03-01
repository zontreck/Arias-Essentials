package dev.zontreck.essentials.data;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModItemModelsProvider extends ItemModelProvider
{
    public ModItemModelsProvider(PackOutput output, ExistingFileHelper helper)
    {
        super(output, AriasEssentials.MODID, helper);
    }

    @Override
    protected void registerModels() {

        item(ModItems.METAL_BAR);
        item(ModItems.TIME_IN_A_BOTTLE);

        /*
        Engineer's Decor Items
         */



        /*
        DEPRECATED ITEMS
         */

    }



    private ItemModelBuilder item(RegistryObject<Item> ite) {
        return this.item((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ite.get())));
    }

    private ItemModelBuilder item(ResourceLocation item) {
        return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(new ModelFile.UncheckedModelFile("item/generated"))).texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }
    private ItemModelBuilder deprecated(RegistryObject<Item> ite) {
        return this.deprecated((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ite.get())));
    }

    private ItemModelBuilder deprecated(ResourceLocation item) {
        return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(new ModelFile.UncheckedModelFile("item/generated"))).texture("layer0", new ResourceLocation(item.getNamespace(), "item/deprecated"));
    }
}
