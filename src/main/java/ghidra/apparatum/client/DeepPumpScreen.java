package ghidra.apparatum.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ghidra.apparatum.Apparatum;
import ghidra.apparatum.container.DeepPumpContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;

public class DeepPumpScreen extends AbstractContainerScreen<DeepPumpContainer> {
    private final ResourceLocation GUI = new ResourceLocation(Apparatum.MOD_ID, "textures/gui/deep_pump.png");
    private final ResourceLocation ENERGY = new ResourceLocation(Apparatum.MOD_ID, "textures/gui/energy.png");

    public DeepPumpScreen(DeepPumpContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    private void drawEnergy(PoseStack matrix) {

    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        drawString(pPoseStack, Minecraft.getInstance().font, "Deep Pump", 56, 6, 0x8b8b8b);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        int percent = menu.getMaxEnergyScaled() - menu.getEnergy();

        if (percent != 0 && this.minecraft != null) {
            TextureAtlasSprite sprite = minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ENERGY);
            RenderSystem.setShaderTexture(0, ENERGY);
            this.blit(pPoseStack, relX + 11, relY + 9 + percent, 0, 0, 10, 72 - percent);
        }
    }
}
