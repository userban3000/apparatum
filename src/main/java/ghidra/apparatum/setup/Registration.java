package ghidra.apparatum.setup;

import ghidra.apparatum.Apparatum;
import ghidra.apparatum.block.DeepPumpBlock;
import ghidra.apparatum.container.DeepPumpContainer;
import ghidra.apparatum.tile.DeepPumpTile;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Apparatum.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Apparatum.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Apparatum.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Apparatum.MOD_ID);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILE_ENTITIES.register(bus);
        CONTAINERS.register(bus);
    }

    //COMMON
    public static final BlockBehaviour.Properties MACHINE_PROPERTIES = BlockBehaviour.Properties.of(Material.STONE).strength(1f).requiresCorrectToolForDrops();
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);

    //BLOCKS
    public static final RegistryObject<DeepPumpBlock> DEEP_PUMP = BLOCKS.register("deep_pump", () -> new DeepPumpBlock(MACHINE_PROPERTIES));
    public static final RegistryObject<Item> DEEP_PUMP_ITEM = fromBlock(DEEP_PUMP);
    public static final RegistryObject<BlockEntityType<DeepPumpTile>> DEEP_PUMP_TE = TILE_ENTITIES.register("deep_pump", () -> BlockEntityType.Builder.of(DeepPumpTile::new, DEEP_PUMP.get()).build(null));
    public static final RegistryObject<MenuType<DeepPumpContainer>> DEEP_PUMP_CONTAINER = CONTAINERS.register("deep_pump",
            () -> IForgeMenuType.create(((windowId, inv, data) -> new DeepPumpContainer(windowId, data.readBlockPos(), inv, inv.player))));

    //ITEMS
    public static final RegistryObject<Item> CONFIGURATOR = ITEMS.register("configurator", () -> new Item(ITEM_PROPERTIES));

    //TILE ENTITIES

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
}
