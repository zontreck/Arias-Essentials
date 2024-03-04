package dev.zontreck.essentials.blocks;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.items.CreativeModeTabs;
import dev.zontreck.libzontreck.edlibmc.Auxiliaries;
import dev.zontreck.libzontreck.edlibmc.StandardBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class ModBlocks {

    private static BlockBehaviour.StatePredicate shulkerState = (p_152653_, p_152654_, p_152655_) -> {
        BlockEntity blockentity = p_152654_.getBlockEntity(p_152655_);
        if (!(blockentity instanceof ShulkerBoxBlockEntity)) {
            return true;
        } else {
            ShulkerBoxBlockEntity shulkerboxblockentity = (ShulkerBoxBlockEntity)blockentity;
            return shulkerboxblockentity.isClosed();
        }
    };


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AriasEssentials.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AriasEssentials.MODID);

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
    private static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }


    private static boolean neverSpawn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        AriasEssentials.LOGGER.info("Registering all blocks...");
    }

    private static BlockBehaviour.Properties standardBehavior()
    {
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(7F).destroyTime(6).isValidSpawn(ModBlocks::neverSpawn);
    }
    private static BlockBehaviour.Properties gratingBlock()
    {
        return standardBehavior()
                .noOcclusion()
                .strength(0.5f, 2000f)
                .isViewBlocking(ModBlocks::never);
    }

    private static BlockBehaviour.Properties stoneLikeBehavior()
    {
        return BlockBehaviour.Properties.copy(Blocks.COBBLESTONE).isValidSpawn(ModBlocks::neverSpawn);
    }

    private static BlockBehaviour.Properties explosionResistance()
    {
        return standardBehavior().explosionResistance(1200);
    }

    private static BlockBehaviour.Properties noViewBlocking()
    {
        return standardBehavior().noOcclusion().isViewBlocking(ModBlocks::never);
    }

    private static BlockBehaviour.Properties fullBright()
    {
        return standardBehavior().lightLevel((X)->{
            return 15;
        }).noOcclusion();
    }

    private static BlockBehaviour.Properties standard = standardBehavior();

    private static BlockBehaviour.Properties explosionResistance = explosionResistance();

    private static BlockBehaviour.Properties noViewBlocking = noViewBlocking();

    private static BlockBehaviour.Properties stone = stoneLikeBehavior();

    private static BlockBehaviour.Properties gratingBlock = gratingBlock();

    private static BlockBehaviour.Properties poolLightClean = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 15);
    private static BlockBehaviour.Properties poolLightDirty = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 12);
    private static BlockBehaviour.Properties poolLightFilthy = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 4);


    public static RegistryObject<Block> registerWithItem(RegistryObject<Block> blk, Item.Properties props)
    {
        CreativeModeTabs.addToAETab(ITEMS.register(blk.getId().getPath(), ()->new BlockItem(blk.get(), props)));

        return blk;
    }


    /*

            ENGINEERS DECOR BLOCKS

     */

    public static final RegistryObject<Block> CLINKER_BRICK_BLOCK = registerWithItem(BLOCKS.register("clinker_brick_block", ()->new StandardBlocks.BaseBlock(
            StandardBlocks.CFG_DEFAULT,
            BlockBehaviour.Properties.of().strength(0.5f, 7f).sound(SoundType.STONE)
    )), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_RECESSED = registerWithItem(BLOCKS.register("clinker_brick_recessed", ()->new StandardBlocks.HorizontalWaterLoggable(
            StandardBlocks.CFG_CUTOUT|StandardBlocks.CFG_HORIZIONTAL|StandardBlocks.CFG_LOOK_PLACEMENT,
            BlockBehaviour.Properties.of().strength(0.5f, 7f).sound(SoundType.STONE),
            new AABB[] {
                    Auxiliaries.getPixeledAABB( 3,0, 0, 13,16, 1),
                    Auxiliaries.getPixeledAABB( 0,0, 1, 16,16,11),
                    Auxiliaries.getPixeledAABB( 4,0,11, 12,16,13)
            }
    )), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_VERTICALLY_SLIT = registerWithItem(BLOCKS.register("clinker_brick_vertically_slit", ()->new StandardBlocks.HorizontalWaterLoggable(
            StandardBlocks.CFG_CUTOUT|StandardBlocks.CFG_HORIZIONTAL|StandardBlocks.CFG_LOOK_PLACEMENT,
            BlockBehaviour.Properties.of().strength(0.5f, 7f).sound(SoundType.STONE),
            new AABB[] {
                    Auxiliaries.getPixeledAABB( 3,0, 0, 13,16, 1),
                    Auxiliaries.getPixeledAABB( 3,0,15, 13,16,16),
                    Auxiliaries.getPixeledAABB( 0,0, 1, 16,16,15)
            }
    )), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_SLAB = registerWithItem(BLOCKS.register("clinker_brick_slab", ()->new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB))), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_STAIRS = registerWithItem(BLOCKS.register("clinker_brick_stairs", ()->new StairBlock(CLINKER_BRICK_BLOCK.get()::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS))), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_WALL = registerWithItem(BLOCKS.register("clinker_brick_wall", ()->new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL))), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_STAINED_BLOCK = registerWithItem(BLOCKS.register("clinker_brick_stained_block", ()->new Block(BlockBehaviour.Properties.copy(Blocks.STONE))), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_STAINED_SLAB = registerWithItem(BLOCKS.register("clinker_brick_stained_slab", ()-> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE))), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_STAINED_STAIRS = registerWithItem(BLOCKS.register("clinker_brick_stained_stairs", ()-> new StairBlock(CLINKER_BRICK_STAINED_BLOCK.get()::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.STONE))), new Item.Properties());

    public static final RegistryObject<Block> CLINKER_BRICK_SASTOR_CORNER_BLOCK = registerWithItem(BLOCKS.register("clinker_brick_sastor_corner_block", ()-> new RotatableBlock(BlockBehaviour.Properties.copy(Blocks.STONE))), new Item.Properties());

    /*public static final RegistryObject<Block> CLINKER_BRICK_VERTICAL_SLAB_STRUCTURED = registerWithItem(BLOCKS.register("clinker_brick_vertical_slab_structured", () -> new StandardBlocks.HorizontalWaterLoggable(
            StandardBlocks.CFG_CUTOUT | StandardBlocks.CFG_HORIZIONTAL | StandardBlocks.CFG_LOOK_PLACEMENT,
            BlockBehaviour.Properties.of().strength(0.5f, 7f).sound(SoundType.STONE),
            new AABB[]{
                    Auxiliaries.getPixeledAABB(0, 0, 0, 16, 16, 8),
            }
    )), new Item.Properties());*/

    public static final RegistryObject<Block> SLAG_BRICK_BLOCK = registerWithItem(BLOCKS.register("slag_brick_block", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.STONE))), new Item.Properties());

    public static final RegistryObject<Block> SLAG_BRICK_SLAB = registerWithItem(BLOCKS.register("slag_brick_slab", ()-> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE))), new Item.Properties());

    public static final RegistryObject<Block> SLAG_BRICK_STAIRS = registerWithItem(BLOCKS.register("slag_brick_stairs", ()-> new StairBlock(SLAG_BRICK_BLOCK.get()::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.STONE))), new Item.Properties());

    public static final RegistryObject<Block> SLAG_BRICK_WALL = registerWithItem(BLOCKS.register("slag_brick_wall", ()-> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL))), new Item.Properties());

    public static final RegistryObject<Block> REBAR_CONCRETE_BLOCK = registerWithItem(BLOCKS.register("rebar_concrete", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).isValidSpawn(ModBlocks::neverSpawn).strength(1f, 2000f))), new Item.Properties());

    public static final RegistryObject<Block> REBAR_CONCRETE_SLAB = registerWithItem(BLOCKS.register("rebar_concrete_slab", ()-> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1f, 2000f).isValidSpawn(ModBlocks::neverSpawn))), new Item.Properties());

    public static final RegistryObject<Block> REBAR_CONCRETE_STAIRS = registerWithItem(BLOCKS.register("rebar_concrete_stairs", ()-> new StairBlock(ModBlocks.REBAR_CONCRETE_BLOCK.get()::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.STONE).strength(1f, 2000f).isValidSpawn(ModBlocks::neverSpawn))), new Item.Properties());

    public static final RegistryObject<Block> REBAR_CONCRETE_WALL = registerWithItem(BLOCKS.register("rebar_concrete_wall", ()-> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1f, 2000f).isValidSpawn(ModBlocks::neverSpawn))), new Item.Properties());

    public static final RegistryObject<Block> REBAR_CONCRETE_TILE_BLOCK = registerWithItem(BLOCKS.register("rebar_concrete_tile", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).isValidSpawn(ModBlocks::neverSpawn).strength(1f, 2000f))), new Item.Properties());

    public static final RegistryObject<Block> REBAR_CONCRETE_TILE_SLAB = registerWithItem(BLOCKS.register("rebar_concrete_tile_slab", ()-> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1f, 2000f).isValidSpawn(ModBlocks::neverSpawn))), new Item.Properties());

    public static final RegistryObject<Block> REBAR_CONCRETE_TILE_STAIRS = registerWithItem(BLOCKS.register("rebar_concrete_tile_stairs", ()-> new StairBlock(ModBlocks.REBAR_CONCRETE_BLOCK.get()::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.STONE).strength(1f, 2000f).isValidSpawn(ModBlocks::neverSpawn))), new Item.Properties());

    /*public static final RegistryObject<Block> REBAR_CONCRETE_HALFSLAB = registerWithItem(BLOCKS.register("halfslab_rebar_concrete", () -> new SlabSliceBlock(
            StandardBlocks.CFG_CUTOUT,
            BlockBehaviour.Properties.of().strength(1.0f, 2000f).sound(SoundType.STONE).isValidSpawn(ModBlocks::neverSpawn)
    )), new Item.Properties());*/

    public static final RegistryObject<Block> PANZER_GLASS_BLOCK = registerWithItem(BLOCKS.register("panzerglass_block", ()-> new PartialTransparentBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f, 2000f).isValidSpawn(ModBlocks::neverSpawn).sound(SoundType.METAL))), new Item.Properties());

    public static final RegistryObject<Block> PANZER_GLASS_SLAB = registerWithItem(BLOCKS.register("panzerglass_slab", ()-> new PartialTransparentSlabBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f, 2000f).isValidSpawn(ModBlocks::neverSpawn).sound(SoundType.METAL))), new Item.Properties());

    public static final RegistryObject<Block> OLD_INDUSTRIAL_WOOD_PLANKS = registerWithItem(BLOCKS.register("old_industrial_wood_planks", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5f, 6f).sound(SoundType.WOOD))), new Item.Properties());

    public static final RegistryObject<Block> OLD_INDUSTRIAL_WOOD_SLAB = registerWithItem(BLOCKS.register("old_industrial_wood_slab", ()-> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5f, 6f).sound(SoundType.WOOD))), new Item.Properties());

    public static final RegistryObject<Block> OLD_INDUSTRIAL_WOOD_STAIRS = registerWithItem(BLOCKS.register("old_industrial_wood_stairs", ()-> new StairBlock(ModBlocks.OLD_INDUSTRIAL_WOOD_PLANKS.get()::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5f, 6f).sound(SoundType.WOOD))), new Item.Properties());

    public static final RegistryObject<Block> OLD_INDUSTRIAL_WOOD_DOOR = registerWithItem(BLOCKS.register("old_industrial_wood_door", ()-> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5f, 6f).noOcclusion(), BlockSetType.DARK_OAK)), new Item.Properties());

    public static final RegistryObject<Block> STEEL_CATWALK = registerWithItem(BLOCKS.register("steel_catwalk", ()-> new BlockCustomVoxels(gratingBlock, Block.box(0, 0, 0, 16, 2, 16))), new Item.Properties());

    public static final RegistryObject<Block> STEEL_CATWALK_TOP = registerWithItem(BLOCKS.register("steel_catwalk_top", ()-> new BlockCustomVoxels(gratingBlock, Block.box(0, 14, 0, 16, 16, 16))), new Item.Properties());

    public static final RegistryObject<Block> STEEL_GRATING = registerWithItem(BLOCKS.register("steel_floor_grating", ()-> new BlockCustomVoxels(gratingBlock, Block.box(0, 0, 0, 16, 2, 16))), new Item.Properties());

    public static final RegistryObject<Block> STEEL_GRATING_TOP = registerWithItem(BLOCKS.register("steel_floor_grating_top", ()-> new BlockCustomVoxels(gratingBlock, Block.box(0, 14, 0, 16, 16, 16))), new Item.Properties());

    public static final RegistryObject<Block> STEEL_TABLE = registerWithItem(BLOCKS.register("steel_table", ()-> new BlockCustomVoxels(gratingBlock, Block.box(0, 0, 0, 16, 16, 16))), new Item.Properties());

    private static final VoxelShape STEEL_CATWALK_STAIRS_NORTH = Shapes.join(Block.box(1, 10, 0, 15, 12, 8), Block.box(1, 2, 8, 15, 4, 16), BooleanOp.OR);

    private static final VoxelShape STEEL_CATWALK_STAIRS_SOUTH = Shapes.join(Block.box(1, 10, 8, 15, 12, 16), Block.box(1, 2, 0, 15, 4, 8), BooleanOp.OR);

    private static final VoxelShape STEEL_CATWALK_STAIRS_EAST = Shapes.join(Block.box(8, 10, 1, 16, 12, 15), Block.box(0, 2, 1, 8, 4, 15), BooleanOp.OR);

    private static final VoxelShape STEEL_CATWALK_STAIRS_WEST = Shapes.join(Block.box(0, 10, 1, 8, 12, 15), Block.box(8, 2, 1, 16, 4, 15), BooleanOp.OR);

    public static final RegistryObject<Block> STEEL_CATWALK_STAIRS = registerWithItem(BLOCKS.register("steel_catwalk_stairs", ()-> new RotatableBlockCustomVoxels(gratingBlock, STEEL_CATWALK_STAIRS_NORTH, STEEL_CATWALK_STAIRS_SOUTH, STEEL_CATWALK_STAIRS_WEST, STEEL_CATWALK_STAIRS_EAST)), new Item.Properties());

    private static final VoxelShape STEEL_CATWALK_STAIRS_LR_NORTH = Stream.of(
            Block.box(1, 2, 8, 15, 4, 16),
            Block.box(1, 10, 0, 15, 12, 8),
            Block.box(0, 0, 0, 1, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_LR_SOUTH = Stream.of(
            Block.box(1, 2, 0, 15, 4, 8),
            Block.box(1, 10, 8, 15, 12, 16),
            Block.box(15, 0, 0, 16, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_LR_EAST = Stream.of(
            Block.box(0, 2, 1, 8, 4, 15),
            Block.box(8, 10, 1, 16, 12, 15),
            Block.box(0, 0, 0, 16, 21, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_LR_WEST = Stream.of(
            Block.box(8, 2, 1, 16, 4, 15),
            Block.box(0, 10, 1, 8, 12, 15),
            Block.box(0, 0, 15, 16, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final RegistryObject<Block> STEEL_CATWALK_STAIRS_LR = registerWithItem(BLOCKS.register("steel_catwalk_stairs_lr", ()-> new RotatableBlockCustomVoxels(gratingBlock, STEEL_CATWALK_STAIRS_LR_NORTH, STEEL_CATWALK_STAIRS_LR_SOUTH, STEEL_CATWALK_STAIRS_LR_WEST, STEEL_CATWALK_STAIRS_LR_EAST)), new Item.Properties());

    private static final VoxelShape STEEL_CATWALK_STAIRS_RR_NORTH = Stream.of(
            Block.box(1, 2, 8, 15, 4, 16),
            Block.box(1, 10, 0, 15, 12, 8),
            Block.box(15, 0, 0, 16, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_RR_SOUTH = Stream.of(
            Block.box(1, 2, 0, 15, 4, 8),
            Block.box(1, 10, 8, 15, 12, 16),
            Block.box(0, 0, 0, 1, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_RR_EAST = Stream.of(
            Block.box(0, 2, 1, 8, 4, 15),
            Block.box(8, 10, 1, 16, 12, 15),
            Block.box(0, 0, 15, 16, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_RR_WEST = Stream.of(
            Block.box(8, 2, 1, 16, 4, 15),
            Block.box(0, 10, 1, 8, 12, 15),
            Block.box(0, 0, 0, 16, 21, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final RegistryObject<Block> STEEL_CATWALK_STAIRS_RR = registerWithItem(BLOCKS.register("steel_catwalk_stairs_rr", ()-> new RotatableBlockCustomVoxels(gratingBlock, STEEL_CATWALK_STAIRS_RR_NORTH, STEEL_CATWALK_STAIRS_RR_SOUTH, STEEL_CATWALK_STAIRS_RR_WEST, STEEL_CATWALK_STAIRS_RR_EAST)), new Item.Properties());

    private static final VoxelShape STEEL_CATWALK_STAIRS_DR_NORTH = Stream.of(
            Block.box(1, 10, 0, 15, 12, 8),
            Block.box(1, 2, 8, 15, 4, 16),
            Block.box(0, 0, 0, 1, 21, 16),
            Block.box(15, 0, 0, 16, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_DR_SOUTH = Stream.of(
            Block.box(1, 10, 8, 15, 12, 16),
            Block.box(1, 2, 0, 15, 4, 8),
            Block.box(15, 0, 0, 16, 21, 16),
            Block.box(0, 0, 0, 1, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_DR_WEST = Stream.of(
            Block.box(8, 10, 1, 16, 12, 15),
            Block.box(0, 2, 1, 8, 4, 15),
            Block.box(0, 0, 0, 16, 21, 1),
            Block.box(0, 0, 15, 16, 21, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape STEEL_CATWALK_STAIRS_DR_EAST = Stream.of(
            Block.box(0, 10, 1, 8, 12, 15),
            Block.box(8, 2, 1, 16, 4, 15),
            Block.box(0, 0, 15, 16, 21, 16),
            Block.box(0, 0, 0, 16, 21, 1)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final RegistryObject<Block> STEEL_CATWALK_STAIRS_DR = registerWithItem(BLOCKS.register("steel_catwalk_stairs_dr", ()-> new RotatableBlockCustomVoxels(gratingBlock, STEEL_CATWALK_STAIRS_DR_NORTH, STEEL_CATWALK_STAIRS_DR_SOUTH, STEEL_CATWALK_STAIRS_DR_WEST, STEEL_CATWALK_STAIRS_DR_EAST)), new Item.Properties());

    private static final VoxelShape STEEL_RAILING_NORTH = Block.box(0.25, 0.25, 0.25, 15.75, 16, 1.25);
    private static final VoxelShape STEEL_RAILING_SOUTH = Block.box(0.25, 0.25, 14.75, 15.75, 16, 15.75);

    private static final VoxelShape STEEL_RAILING_WEST = Block.box(14.75, 0.25, 0.25, 15.75, 16, 15.75);
    private static final VoxelShape STEEL_RAILING_EAST = Block.box(0.25, 0.25, 0.25, 1.25, 16, 15.75);
    public static final RegistryObject<Block> STEEL_RAILING = registerWithItem(BLOCKS.register("steel_railing", ()->new RotatableBlockCustomVoxels(gratingBlock, STEEL_RAILING_NORTH, STEEL_RAILING_SOUTH, STEEL_RAILING_WEST, STEEL_RAILING_EAST)), new Item.Properties());

    public static final RegistryObject<Block> STEEL_CATWALK_BLOCK = registerWithItem(BLOCKS.register("steel_catwalk_block", ()-> new Block(gratingBlock)), new Item.Properties());


}
