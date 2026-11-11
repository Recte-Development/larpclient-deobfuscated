package com.larpclient.module.render;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.social.cosmetic.CosmeticOverrides;
import com.larpclient.util.entity.EntityHealth;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.larpclient.util.render.ScreenProjection;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class NametagsModule extends Module {
   private final BooleanSetting playersSetting = this.addSetting(new BooleanSetting("Players", "Show for players", true));
   private final BooleanSetting selfNametagSetting = this.addSetting(
      new BooleanSetting("Self Nametag", "Show your own tag in third person or a detached camera", false)
   );
   private final BooleanSetting mobsSetting = this.addSetting(new BooleanSetting("Mobs", "Show for hostile mobs", false));
   private final BooleanSetting animalsSetting = this.addSetting(new BooleanSetting("Animals", "Show for animals", false));
   private final BooleanSetting healthSetting = this.addSetting(new BooleanSetting("Health", "Show current health", true));
   private final BooleanSetting distanceSetting = this.addSetting(new BooleanSetting("Distance", "Show distance", true));
   private final BooleanSetting itemsSetting = this.addSetting(new BooleanSetting("Items", "Show armor and held-item icons above the name", true));
   private final NumberSetting rangeSetting = this.addSetting(new NumberSetting("Range", "Max render range", 128.0, 8.0, 512.0, 8.0));
   private final NumberSetting scaleSetting = this.addSetting(new NumberSetting("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1));
   private final NumberSetting heightSetting = this.addSetting(new NumberSetting("Height", "Gap above the entity", 0.25, 0.1, 1.5, 0.05));
   private final NumberSetting opacitySetting = this.addSetting(new NumberSetting("Opacity", "Background opacity", 0.65, 0.0, 1.0, 0.05));
   private final BooleanSetting backgroundSetting = this.addSetting(new BooleanSetting("Background", "Draw label background", true));
   private final Set<Integer> renderedEntityIds = new HashSet<>();
   private static final int labelY = -15;
   private static final int cornerRadius = 4;
   private static final int labelPaddingY = 3;
   private static final int labelPaddingX = 4;
   private static final float itemScale = 0.75F;
   private static final float itemSpacing = 13.0F;
   private static final int backgroundColor = -15329768;
   private static final int nameColor = -1;
   private static final int secondaryColor = -4934470;
   private static final int healthColor = -41892;

   public NametagsModule() {
      super("Nametags", "Show names, health and gear above entities", Category.RENDER, -1);
      this.opacitySetting.visibleWhen(this.backgroundSetting::getValue);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "nametags"), this::renderNametags);
      this.selfNametagSetting
         .onChange(var0 -> LarpLog.debug("Nametags", "Self nametag {} in third person or a detached camera", var0 ? "enabled" : "disabled"));
   }

   public boolean isRendered(Entity var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.renderedEntityIds.contains(var1.getId());
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.playersSetting, this.selfNametagSetting, this.mobsSetting, this.animalsSetting);
   }

   public List<Setting<?>> getInfoSettings() {
      return List.of(this.healthSetting, this.distanceSetting, this.itemsSetting);
   }

   public List<Setting<?>> getStyleSettings() {
      return List.of(this.rangeSetting, this.scaleSetting, this.heightSetting, this.backgroundSetting, this.opacitySetting);
   }

   @Override
   public void onEnable() {
      LarpLog.debug("Nametags", "Replacement labels suppress native names and below-name scoreboard text");
   }

   @Override
   public void onDisable() {
      this.renderedEntityIds.clear();
   }

   private boolean shouldShowPlayer(boolean var1, boolean var2) {
      return var1 ? this.selfNametagSetting.getValue() && var2 : this.playersSetting.getValue();
   }

   private void renderNametags(GuiGraphicsExtractor var1, DeltaTracker var2) {
      this.renderedEntityIds.clear();
      if (this.isOperational() && this.isInGame() && !minecraft.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = StreamProofManager.selectGraphics(var1, true, false);
         if (var1 != null) {
            long var3 = PerfProfiler.startTimer();
            Camera var5 = minecraft.gameRenderer.mainCamera();
            Vec3 var6 = var5.position();
            Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
            Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F * this.scaleSetting.getValue().floatValue(), 0.0F));
            int var9 = minecraft.getWindow().getGuiScaledWidth();
            int var10 = minecraft.getWindow().getGuiScaledHeight();
            boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            float var12 = var2.getGameTimeDeltaPartialTick(false);
            int var13 = 0;

            for (Entity var15 : minecraft.level.entitiesForRendering()) {
               if (var15 instanceof LivingEntity var16
                  && var16.isAlive()
                  && (
                     var15 instanceof Player
                        ? this.shouldShowPlayer(var15 == minecraft.player, var5.isDetached())
                        : (var15 instanceof Enemy ? this.mobsSetting.getValue() : var15 instanceof Animal && this.animalsSetting.getValue())
                  )) {
                  double var17 = minecraft.player.distanceTo(var15);
                  if (!(var17 > this.rangeSetting.getValue())) {
                     Vec3 var19 = var15.getPosition(var12);
                     Vec3 var20 = var15.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, var15.getYRot(var12));
                     double var21 = Math.max(var15.getBbHeight(), var20 == null ? 0.0 : var20.y);
                     ScreenProjection.ProjectedPoint var23 = ScreenProjection.projectPoint(
                        var19.add(var20 == null ? 0.0 : var20.x, var21 + this.heightSetting.getValue(), var20 == null ? 0.0 : var20.z),
                        var6,
                        var7,
                        var8,
                        var9,
                        var10,
                        var11
                     );
                     if (var23 != null) {
                        if (++var13 > 128) {
                           break;
                        }

                        String var24 = this.healthSetting.getValue() ? String.format(Locale.ROOT, "%.1f", EntityHealth.getHealth(var16)) : null;
                        String var25 = this.distanceSetting.getValue() && var15 != minecraft.player ? (int)var17 + "m" : null;
                        var1.pose().pushMatrix();

                        try {
                           var1.pose().translate(var23.getX(), var23.getY()).scale(var23.scale());
                           MutableComponent var26 = Component.empty()
                              .append(
                                 FontRenderer.toComponent(
                                    FontRenderer.truncateToWidth(
                                       CosmeticOverrides.nameOrDefault(var15.getUUID(), var15.getName().getString()), 160, FontRenderer.Face.MEDIUM
                                    ),
                                    FontRenderer.Face.MEDIUM
                                 )
                              );
                           if (var24 != null) {
                              var26.append(coloredText(" [", -4934470)).append(coloredText(var24, -41892)).append(coloredText("]", -4934470));
                           }

                           if (var25 != null) {
                              var26.append(coloredText(" " + var25, -4934470));
                           }

                           int var27 = this.backgroundSetting.getValue() ? ColorUtil.withAlpha(-15329768, this.opacitySetting.getValue()) : 0;
                           this.drawLabel(var1, var26, -15, -1, var27);
                           if (this.itemsSetting.getValue()) {
                              ArrayList var28 = new ArrayList(6);

                              for (EquipmentSlot var32 : new EquipmentSlot[]{
                                 EquipmentSlot.MAINHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND
                              }) {
                                 ItemStack var33 = var16.getItemBySlot(var32);
                                 if (!var33.isEmpty()) {
                                    var28.add(var33);
                                 }
                              }

                              float var46 = -32.0F;

                              for (int var47 = 0; var47 < var28.size(); var47++) {
                                 var1.pose().pushMatrix();

                                 try {
                                    var1.pose().translate((var47 - (var28.size() - 1) / 2.0F) * 13.0F - 6.0F, var46).scale(0.75F);
                                    var1.item(var16, (ItemStack)var28.get(var47), 0, 0, var15.getId());
                                 } finally {
                                    var1.pose().popMatrix();
                                 }
                              }
                           }

                           this.renderedEntityIds.add(var15.getId());
                        } catch (RuntimeException var43) {
                           LarpLog.errorOnce("Render", "Nametag submission failed", var43);
                        } finally {
                           var1.pose().popMatrix();
                        }
                     }
                  }
               }
            }

            PerfProfiler.endTimer("Nametags", var3);
         }
      }
   }

   private static MutableComponent coloredText(String var0, int var1) {
      return FontRenderer.toComponent(var0, FontRenderer.Face.REGULAR).copy().withColor(var1 & 16777215);
   }

   private void drawLabel(GuiGraphicsExtractor var1, Component var2, int var3, int var4, int var5) {
      int var6 = minecraft.font.width(var2);
      int var7 = -var6 / 2;
      if (var5 != 0) {
         DrawUtil.fillRoundedRect(var1, var7 - 4, var3 - 3, var6 + 8, 15.0F, 4.0F, var5);
      }

      var1.text(minecraft.font, var2, var7, var3, var4, var5 == 0);
   }
}
