package dev.zontreck.essentials.data.loot;

import dev.zontreck.essentials.blocks.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider
{
    public ModBlockLootTablesProvider()
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {


        /*
        Engineer's Decor Blocks
         */
        dropSelf(ModBlocks.CLINKER_BRICK_BLOCK.get());
        dropSelf(ModBlocks.CLINKER_BRICK_RECESSED.get());
        dropSelf(ModBlocks.CLINKER_BRICK_VERTICALLY_SLIT.get());
        createSlabItemTable(ModBlocks.CLINKER_BRICK_SLAB);
        dropSelf(ModBlocks.CLINKER_BRICK_STAIRS.get());
        dropSelf(ModBlocks.CLINKER_BRICK_STAINED_BLOCK.get());
        createSlabItemTable(ModBlocks.CLINKER_BRICK_STAINED_SLAB);
        dropSelf(ModBlocks.CLINKER_BRICK_STAINED_STAIRS.get());
        dropSelf(ModBlocks.CLINKER_BRICK_SASTOR_CORNER_BLOCK.get());
        dropSelf(ModBlocks.CLINKER_BRICK_WALL.get());

        dropSelf(ModBlocks.SLAG_BRICK_BLOCK.get());
        createSlabItemTable(ModBlocks.SLAG_BRICK_SLAB);
        dropSelf(ModBlocks.SLAG_BRICK_WALL.get());
        dropSelf(ModBlocks.SLAG_BRICK_STAIRS.get());

        dropSelf(ModBlocks.REBAR_CONCRETE_BLOCK.get());
        createSlabItemTable(ModBlocks.REBAR_CONCRETE_SLAB);
        dropSelf(ModBlocks.REBAR_CONCRETE_STAIRS.get());
        dropSelf(ModBlocks.REBAR_CONCRETE_WALL.get());

        dropSelf(ModBlocks.REBAR_CONCRETE_TILE_BLOCK.get());
        createSlabItemTable(ModBlocks.REBAR_CONCRETE_TILE_SLAB);
        dropSelf(ModBlocks.REBAR_CONCRETE_TILE_STAIRS.get());

        dropSelf(ModBlocks.PANZER_GLASS_BLOCK.get());
        createSlabItemTable(ModBlocks.PANZER_GLASS_SLAB);

        dropSelf(ModBlocks.OLD_INDUSTRIAL_WOOD_PLANKS.get());
        createSlabItemTable(ModBlocks.OLD_INDUSTRIAL_WOOD_SLAB);
        dropSelf(ModBlocks.OLD_INDUSTRIAL_WOOD_STAIRS.get());
        createDoorTable(ModBlocks.OLD_INDUSTRIAL_WOOD_DOOR);

        dropSelf(ModBlocks.STEEL_TABLE.get());
        dropSelf(ModBlocks.STEEL_CATWALK.get());
        dropSelf(ModBlocks.STEEL_RAILING.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS_LR.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS_RR.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS_DR.get());
        dropSelf(ModBlocks.STEEL_GRATING.get());

        dropSelf(ModBlocks.STEEL_GRATING_TOP.get());
        dropSelf(ModBlocks.STEEL_CATWALK_TOP.get());

        dropSelf(ModBlocks.STEEL_CATWALK_BLOCK.get());



    }

    private void createDoorTable(RegistryObject<Block> blk)
    {
        var loot = createDoorTable(blk.get());

        add(blk.get(), loot);
    }

    private void createSlabItemTable(RegistryObject<Block> slab)
    {
        var loot = createSlabItemTable(slab.get());
        add(slab.get(), loot);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


    protected LootTable.Builder createCopperOreDrops(Block block, Item rawOre) {
        return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder) this.applyExplosionDecay(block, LootItem.lootTableItem(rawOre).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createOreDrop(Block block, Item rawOre) {
        return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)this.applyExplosionDecay(block, LootItem.lootTableItem(rawOre).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
}
