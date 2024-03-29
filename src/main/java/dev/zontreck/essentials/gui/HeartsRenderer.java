/*
 *
 *    DISCLAIMER: This code was taken from Mantle, and will be modified to fit the needs of this mod, such as adding more heat options. This code is subject to Mantle's license of MIT.
 *  Despite this code being taken from, and modified/updated to be modern, all textures are my own creation
 *  This disclaimer is here to give credit where credit is due. The author(s) of mantle have done a absolutely fantastic job. And if Mantle gets updated this shall be removed along with future plans of extra hearts and color options.
 *
 *
 */

package dev.zontreck.essentials.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.zontreck.essentials.AriasEssentials;
import dev.zontreck.essentials.configs.client.AEClientConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class HeartsRenderer {
    private static final ResourceLocation ICON_HEARTS = new ResourceLocation(AriasEssentials.MODID,
            "textures/gui/hearts.png");
    private static final ResourceLocation ICON_ABSORB = new ResourceLocation(AriasEssentials.MODID,
            "textures/gui/absorb.png");
    private static final ResourceLocation ICON_VANILLA = Gui.GUI_ICONS_LOCATION;

    private final Minecraft mc = Minecraft.getInstance();

    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long healthUpdateCounter = 0;
    private long lastSystemTime = 0;
    private final Random rand = new Random();

    private int regen;

    /**
     * Draws a texture to the screen
     *
     * @param matrixStack Matrix stack instance
     * @param x           X position
     * @param y           Y position
     * @param textureX    Texture X
     * @param textureY    Texture Y
     * @param width       Width to draw
     * @param height      Height to draw
     */
    private void blit(GuiGraphics matrixStack, int x, int y, int textureX, int textureY, int width, int height, ResourceLocation resource) {
        matrixStack.blit(resource, x, y, textureX, textureY, width, height);
    }

    /* HUD */

    /**
     * Event listener
     *
     * @param event Event instance
     */
    @SubscribeEvent(priority = EventPriority.LOW)
    public void renderHealthbar(RenderGuiOverlayEvent.Pre event) {
        NamedGuiOverlay ActualOverlay = GuiOverlayManager.findOverlay(new ResourceLocation("minecraft:player_health"));

        if (ActualOverlay == null) {
            if (GuiOverlayManager.getOverlays() == null) {
                AriasEssentials.LOGGER.info("Overlays non existent?!");
            }
            for (NamedGuiOverlay overlay : GuiOverlayManager.getOverlays()) {
                // Next print
                // LibZontreck.LOGGER.info("GUI OVERLAY: "+overlay.id().getPath());

                if (overlay.id().getPath().equals("player_health")) {
                    ActualOverlay = overlay;
                    break;
                }
            }
        }
        if (event.isCanceled() || !AEClientConfig.getInstance().EnableHearts || event.getOverlay() != ActualOverlay) {
            return;
        }
        // ensure its visible
        if (!(mc.gui instanceof ForgeGui gui) || mc.options.hideGui || !gui.shouldDrawSurvivalElements()) {
            return;
        }
        Entity renderViewEnity = this.mc.getCameraEntity();
        if (!(renderViewEnity instanceof Player player)) {
            return;
        }
        gui.setupOverlayRenderState(true, false);

        this.mc.getProfiler().push("health");

        // extra setup stuff from us
        int left_height = gui.leftHeight;
        int width = this.mc.getWindow().getGuiScaledWidth();
        int height = this.mc.getWindow().getGuiScaledHeight();
        int updateCounter = this.mc.gui.getGuiTicks();

        // start default forge/mc rendering
        // changes are indicated by comment

        int health = Mth.ceil(player.getHealth());
        boolean highlight = this.healthUpdateCounter > (long) updateCounter
                && (this.healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

        if (health < this.playerHealth && player.invulnerableTime > 0) {
            this.lastSystemTime = Util.getMillis();
            this.healthUpdateCounter = (updateCounter + 20);
        } else if (health > this.playerHealth && player.invulnerableTime > 0) {
            this.lastSystemTime = Util.getMillis();
            this.healthUpdateCounter = (updateCounter + 10);
        }

        if (Util.getMillis() - this.lastSystemTime > 1000L) {
            this.playerHealth = health;
            this.lastPlayerHealth = health;
            this.lastSystemTime = Util.getMillis();
        }

        this.playerHealth = health;
        int healthLast = this.lastPlayerHealth;

        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = attrMaxHealth == null ? 0 : (float) attrMaxHealth.getValue();
        float absorb = Mth.ceil(player.getAbsorptionAmount());

        // CHANGE: simulate 10 hearts max if there's more, so vanilla only renders one
        // row max
        healthMax = Math.min(healthMax, 20f);
        health = Math.min(health, 20);
        absorb = Math.min(absorb, 20);

        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        this.rand.setSeed(updateCounter * 312871L);

        int left = width / 2 - 91;
        int top = height - left_height;
        // change: these are unused below, unneeded? should these adjust the Forge
        // variable?
        // left_height += (healthRows * rowHeight);
        // if (rowHeight != 10) left_height += 10 - rowHeight;

        this.regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            this.regen = updateCounter % 25;
        }

        assert this.mc.level != null;
        final int TOP = 9 * (this.mc.level.getLevelData().isHardcore() ? 5 : 0);
        final int BACKGROUND = (highlight ? 25 : 16);
        int MARGIN = 16;
        if (player.hasEffect(MobEffects.POISON))
            MARGIN += 36;
        else if (player.hasEffect(MobEffects.WITHER))
            MARGIN += 72;
        float absorbRemaining = absorb;

        GuiGraphics matrixStack = event.getGuiGraphics();
        for (int i = Mth.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
            int row = Mth.ceil((float) (i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4)
                y += this.rand.nextInt(2);
            if (i == this.regen)
                y -= 2;

            this.blit(matrixStack, x, y, BACKGROUND, TOP, 9, 9, ICON_VANILLA);

            if (highlight) {
                if (i * 2 + 1 < healthLast) {
                    this.blit(matrixStack, x, y, MARGIN + 54, TOP, 9, 9, ICON_VANILLA); // 6
                } else if (i * 2 + 1 == healthLast) {
                    this.blit(matrixStack, x, y, MARGIN + 63, TOP, 9, 9, ICON_VANILLA); // 7
                }
            }

            if (absorbRemaining > 0.0F) {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F) {
                    this.blit(matrixStack, x, y, MARGIN + 153, TOP, 9, 9, ICON_VANILLA); // 17
                    absorbRemaining -= 1.0F;
                } else {
                    this.blit(matrixStack, x, y, MARGIN + 144, TOP, 9, 9, ICON_VANILLA); // 16
                    absorbRemaining -= 2.0F;
                }
            } else {
                if (i * 2 + 1 < health) {
                    this.blit(matrixStack, x, y, MARGIN + 36, TOP, 9, 9, ICON_VANILLA); // 4
                } else if (i * 2 + 1 == health) {
                    this.blit(matrixStack, x, y, MARGIN + 45, TOP, 9, 9, ICON_VANILLA); // 5
                }
            }
        }

        this.renderExtraHearts(matrixStack, left, top, player);
        this.renderExtraAbsorption(matrixStack, left, top - rowHeight, player);

        RenderSystem.setShaderTexture(0, ICON_VANILLA);
        gui.leftHeight += 10;
        if (absorb > 0) {
            gui.leftHeight += 10;
        }

        event.setCanceled(true);
        RenderSystem.disableBlend();
        this.mc.getProfiler().pop();
        MinecraftForge.EVENT_BUS
                .post(new RenderGuiOverlayEvent.Post(mc.getWindow(), event.getGuiGraphics(), event.getPartialTick(), ActualOverlay));
    }

    /**
     * Gets the texture from potion effects
     *
     * @param player Player instance
     * @return Texture offset for potion effects
     */
    private int getPotionOffset(Player player) {
        int potionOffset = 0;
        MobEffectInstance potion = player.getEffect(MobEffects.WITHER);
        if (potion != null) {
            potionOffset = 18;
        }
        potion = player.getEffect(MobEffects.POISON);
        if (potion != null) {
            potionOffset = 9;
        }
        assert this.mc.level != null;
        if (this.mc.level.getLevelData().isHardcore()) {
            potionOffset += 27;
        }
        return potionOffset;
    }

    /**
     * Renders the health above 10 hearts
     *
     * @param matrixStack Matrix stack instance
     * @param xBasePos    Health bar top corner
     * @param yBasePos    Health bar top corner
     * @param player      Player instance
     */
    private void renderExtraHearts(GuiGraphics matrixStack, int xBasePos, int yBasePos, Player player) {
        int potionOffset = this.getPotionOffset(player);

        // Extra hearts
        RenderSystem.setShaderTexture(0, ICON_HEARTS);
        int hp = Mth.ceil(player.getHealth());
        this.renderCustomHearts(matrixStack, xBasePos, yBasePos, potionOffset, hp, false);
    }

    /**
     * Renders the absorption health above 10 hearts
     *
     * @param matrixStack Matrix stack instance
     * @param xBasePos    Health bar top corner
     * @param yBasePos    Health bar top corner
     * @param player      Player instance
     */
    private void renderExtraAbsorption(GuiGraphics matrixStack, int xBasePos, int yBasePos, Player player) {
        int potionOffset = this.getPotionOffset(player);

        // Extra hearts
        RenderSystem.setShaderTexture(0, ICON_ABSORB);
        int absorb = Mth.ceil(player.getAbsorptionAmount());
        this.renderCustomHearts(matrixStack, xBasePos, yBasePos, potionOffset, absorb, true);
    }

    /**
     * Gets the texture offset from the regen effect
     *
     * @param i      Heart index
     * @param offset Current offset
     */
    private int getYRegenOffset(int i, int offset) {
        return i + offset == this.regen ? -2 : 0;
    }

    /**
     * Shared logic to render custom hearts
     *
     * @param matrixStack  Matrix stack instance
     * @param xBasePos     Health bar top corner
     * @param yBasePos     Health bar top corner
     * @param potionOffset Offset from the potion effect
     * @param count        Number to render
     * @param absorb       If true, render absorption hearts
     */
    private void renderCustomHearts(GuiGraphics matrixStack, int xBasePos, int yBasePos, int potionOffset, int count,
                                    boolean absorb) {
        int regenOffset = absorb ? 10 : 0;
        for (int iter = 0; iter < count / 20; iter++) {
            int renderHearts = (count - 20 * (iter + 1)) / 2;
            int heartIndex = iter % 11;
            if (renderHearts > 10) {
                renderHearts = 10;
            }
            for (int i = 0; i < renderHearts; i++) {
                int y = this.getYRegenOffset(i, regenOffset);
                if (absorb) {
                    this.blit(matrixStack, xBasePos + 8 * i, yBasePos + y, 0, 54, 9, 9, ICON_ABSORB);
                }
                this.blit(matrixStack, xBasePos + 8 * i, yBasePos + y, 18 * heartIndex, potionOffset, 9, 9, ICON_HEARTS);
            }
            if (count % 2 == 1 && renderHearts < 10) {
                int y = this.getYRegenOffset(renderHearts, regenOffset);
                if (absorb) {
                    this.blit(matrixStack, xBasePos + 8 * renderHearts, yBasePos + y, 0, 54, 9, 9, ICON_ABSORB);
                }
                this.blit(matrixStack, xBasePos + 8 * renderHearts, yBasePos + y, 9 + 18 * heartIndex, potionOffset, 9, 9, ICON_HEARTS);
            }
        }
    }
}
