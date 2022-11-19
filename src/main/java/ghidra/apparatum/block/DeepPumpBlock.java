package ghidra.apparatum.block;

import ghidra.apparatum.container.DeepPumpContainer;
import ghidra.apparatum.setup.Registration;
import ghidra.apparatum.tile.DeepPumpTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeepPumpBlock extends Block implements EntityBlock {
    public static final String MESSAGE_DEEPPUMP = "message.deep_pump";
    public static final String SCREEN_APPARATUM_DEEPPUMP = "screen.apparatum.deep_pump";

    public DeepPumpBlock(Block.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(new TranslatableComponent(MESSAGE_DEEPPUMP));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null :
                (lvl, pos, blockState, t) -> {
                    if (t instanceof DeepPumpTile tile) {
                        tile.tickServer(lvl, pos);
                    }
                };
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
            if (tileEntity instanceof DeepPumpTile) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent(SCREEN_APPARATUM_DEEPPUMP);
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                        return new DeepPumpContainer(pContainerId, pPos, pPlayerInventory, pPlayer);
                    }
                };
                NetworkHooks.openGui((ServerPlayer) pPlayer, containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Container provider for DEEP_PUMP missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return Registration.DEEP_PUMP_TE.get().create(pPos, pState);
    }
}
