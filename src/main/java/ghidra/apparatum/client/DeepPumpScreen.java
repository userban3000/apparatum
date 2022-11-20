package ghidra.apparatum.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ghidra.apparatum.Apparatum;
import ghidra.apparatum.container.DeepPumpContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.lwjgl.system.CallbackI;

import javax.swing.text.Style;
import java.util.List;

public class DeepPumpScreen extends AbstractContainerScreen<DeepPumpContainer> {
    private final ResourceLocation GUI = new ResourceLocation(Apparatum.MOD_ID, "textures/gui/deep_pump.png");
    private final ResourceLocation ENERGY = new ResourceLocation(Apparatum.MOD_ID, "textures/gui/energy.png");
    private final ResourceLocation ENERGY_SLICE = new ResourceLocation(Apparatum.MOD_ID, "textures/gui/energy_slice.png");

    public DeepPumpScreen(DeepPumpContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        drawString(pPoseStack, Minecraft.getInstance().font, "Deep Pump", 56, 6, 0x8b8b8b);
    }

    @Override
    protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
        if (isHovering(12, 9, 10, 72, pX, pY)) {
            Component t = new TextComponent(getEnergyString());
            renderTooltip(pPoseStack, t, pX, pY);
        }
    }

    protected String getEnergyString() {
        int currEnergy = menu.getEnergy();
        return currEnergy / 1000 + "." + (currEnergy % 1000)/100 + "k FE / 80.0k FE";
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        int pixelsDown = (int)Math.ceil( ((double)menu.getEnergy() / (double)menu.getMaxEnergy()) * 70.0d);

        if (Apparatum.CONSOLE_DEBUG_ENABLED) {
            System.out.println("DEEP PUMP SCREEN ========");
            System.out.println("energy:" + menu.getEnergy());
            System.out.println("max energy:" + menu.getMaxEnergy());
            System.out.println("pixdown:" + pixelsDown);
            System.out.println("=================");
        }

        if (pixelsDown > 2 && this.minecraft != null) {
            TextureAtlasSprite sprite = minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ENERGY_SLICE);
            RenderSystem.setShaderTexture(0, ENERGY);
            for (int i = 0; i < pixelsDown; i+=2 ) {
                this.blit(pPoseStack, relX + 11, relY + 76 - i, 0, 0, 10, 2);
            }
        }
    }
}
