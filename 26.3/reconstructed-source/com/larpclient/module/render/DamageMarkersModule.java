package com.larpclient.module.render;

import com.larpclient.LarpClient;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.world.DamageNumbers;
import com.larpclient.render.world.WorldTextRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class DamageMarkersModule extends Module {
   private static DamageMarkersModule instance;
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Damage text size", 1.0, 0.5, 2.5, 0.1));
   private final NumberSetting lifeTime = this.addSetting(new NumberSetting("Life Time", "Marker lifetime in milliseconds", 1000.0, 300.0, 2500.0, 100.0));
   private final BooleanSetting selfDamage = this.addSetting(new BooleanSetting("Self Damage", "Show your own health loss", true));
   private final BooleanSetting friendHits = this.addSetting(new BooleanSetting("Friend Hits", "Show damage attributed to friends", true));
   private final ModeSetting colorMode = this.addSetting(new ModeSetting("Color", "Color by damage or a chosen tint", "Damage", "Damage", "Custom"));
   private final ColorSetting customColor = this.addSetting(new ColorSetting("Custom Color", "Chosen damage marker tint", -48060));
   private final DamageNumbers damageNumbers = new DamageNumbers(21463920019784013L);
   private final HashMap<LivingEntity, Double> friendSwingExpiry = new HashMap<>();
   private WorldTextRenderer textRenderer;
   private Object trackedLevel;
   private Object trackedPlayer;
   private Vec3 lastPlayerPos;

   public DamageMarkersModule() {
      super("Damage Markers", "Show dealt damage and your own health loss as floating numbers", Category.VISUALS, -1);
      instance = this;
      this.customColor.visibleWhen(() -> this.colorMode.isMode("Custom"));
   }

   public static DamageMarkersModule getInstance() {
      return instance;
   }

   public List<Setting<?>> getFilterSettings() {
      return List.of(this.selfDamage, this.friendHits);
   }

   public List<Setting<?>> getAppearanceSettings() {
      return List.of(this.size, this.lifeTime, this.colorMode, this.customColor);
   }

   @Override
   public void onEnable() {
      this.reset();
      LarpLog.info("DamageMarkers", "Enabled; observed health loss, 8-block trigger range, 256 marker budget");
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   public void reset() {
      this.damageNumbers.clear();
      this.friendSwingExpiry.clear();
      this.trackedLevel = this.trackedPlayer = null;
      this.lastPlayerPos = null;
      if (this.textRenderer != null) {
         this.textRenderer.close();
         this.textRenderer = null;
      }
   }

   private boolean isFriend(Entity var1) {
      LarpClient var2 = LarpClient.getInstance();
      if (var1 instanceof Player && var2 != null) {
         MCFriendModule var3 = var2.getModuleManager().getModule(MCFriendModule.class);
         return var3 != null && var3.isFriend(var1.getName().getString());
      } else {
         return false;
      }
   }

   private boolean validateContext() {
      if (this.isRuntimeActive() && this.isInGame()) {
         if (this.trackedLevel != getMc().level || this.trackedPlayer != this.getPlayer()) {
            this.reset();
            this.trackedLevel = getMc().level;
            this.trackedPlayer = this.getPlayer();
         }

         return true;
      } else {
         return false;
      }
   }

   public void isFriend(Entity var1) {
      if (this.isEnabled()) {
         if (this.validateContext() && var1 instanceof LivingEntity var2 && var1 != this.getPlayer()) {
            this.damageNumbers.expectDamage(var1.getId(), var1, var2.getHealth(), TickTimer.getTickTime());
         }
      }
   }

   public void onDamageEvent(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.validateContext()) {
            Entity var2 = getMc().level.getEntity(var1.entityId());
            if (var2 instanceof LivingEntity var3 && (var2 != this.getPlayer() || this.selfDamage.getValue())) {
               Entity var4 = var1.getSource(getMc().level).getEntity();
               if (var2 == this.getPlayer() || var4 == this.getPlayer() || this.friendHits.getValue() && this.isFriend(var4)) {
                  this.damageNumbers.expectDamage(var2.getId(), var2, var3.getHealth(), TickTimer.getTickTime());
               }
            }
         }
      }
   }

   public void onAnimatePacket(ClientboundAnimatePacket var1) {
      if (this.isEnabled()) {
         if (this.validateContext() && this.friendHits.getValue() && (var1.getAction() == 0 || var1.getAction() == 3)) {
            if (getMc().level.getEntity(var1.getId()) instanceof LivingEntity var3
               && this.isFriend(var3)
               && (this.friendSwingExpiry.size() < 128 || this.friendSwingExpiry.containsKey(var3))) {
               this.friendSwingExpiry.put(var3, TickTimer.getTickTime() + 2.5);
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.validateContext();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.reset();
      } else {
         Vec3 var1 = this.getPlayer().position();
         if (this.lastPlayerPos != null) {
            double var18;
            var10000 = (var18 = var1.distanceToSqr(this.lastPlayerPos) - 256.0) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
            boolean var14 = false;
            int var13 = var10000;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               this.reset();
               this.trackedLevel = getMc().level;
               this.trackedPlayer = this.getPlayer();
            }
         }

         this.lastPlayerPos = var1;
         double var2 = TickTimer.getTickTime();
         this.friendSwingExpiry.entrySet().removeIf(var2x -> var2 > var2x.getValue() || var2x.getKey().isRemoved());
         this.damageNumbers.prune(var2, var0 -> ((Entity)var0).isRemoved());

         for (Entity var5 : getMc().level.entitiesForRendering()) {
            if (var5 instanceof LivingEntity var6 && !var5.isRemoved() && !(var5.distanceToSqr(this.getPlayer()) > 64.0)) {
               Object var7 = var6.getLastHurtByMob();
               if (var7 == null) {
                  var7 = var6.getLastHurtByPlayer();
               }

               int var8 = var5 == this.getPlayer() ? this.selfDamage.getValue() : (var7 == this.getPlayer() ? 1 : 0);
               if (var5 != this.getPlayer() && this.friendHits.getValue()) {
                  var8 |= this.isFriend((Entity)var7);
                  if (var8 == 0) {
                     for (LivingEntity var10 : this.friendSwingExpiry.keySet()) {
                        if (var5.distanceToSqr(var10) <= 16.0) {
                           var8 = 1;
                           break;
                        }
                     }
                  }
               }

               if (var5 == this.getPlayer() && !this.selfDamage.getValue()) {
                  this.damageNumbers.forget(var5.getId());
               }

               Vec3 var15 = var5.position();
               this.damageNumbers
                  .trackHealth(
                     var5.getId(),
                     var5,
                     var6.getHealth(),
                     var15.x,
                     var15.y,
                     var15.z,
                     var6.getBbHeight(),
                     (boolean)var8,
                     var2,
                     this.lifeTime.getValue() / 1000.0
                  );
            }
         }
      }
   }

   public void submitLabels(CameraRenderState var1) {
      if (this.isEnabled()) {
         if (this.validateContext()) {
            if (this.textRenderer == null) {
               this.textRenderer = new WorldTextRenderer();
            }

            ArrayList var2 = new ArrayList();
            double var3 = TickTimer.getTime();
            Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.orientation);
            double var6 = Math.max(0.01, Math.abs(var1.projectionMatrix.m11()));
            double var8 = Math.max(1, getMc().getWindow().getGuiScaledHeight());

            for (DamageNumbers.NumberSnapshot var11 : this.damageNumbers.snapshot(var3)) {
               double var12 = var11.getX() - var1.pos.x;
               double var14 = var11.getY() - var1.pos.y;
               double var16 = var11.getZ() - var1.pos.z;
               double var18 = var12 * var5.x + var14 * var5.y + var16 * var5.z;
               if (!(var18 <= 0.05)) {
                  int var20 = this.colorMode.isMode("Custom") ? this.customColor.getValue() : DamageNumbers.damageColor(var11.damage());
                  double var21 = 0.5 * var18 / (var8 * var6 * 0.01890625);
                  var2.add(
                     new WorldTextRenderer.WorldLabel(
                        var11.text(),
                        var11.getX(),
                        var11.getY(),
                        var11.getZ(),
                        var21 * this.size.getValue() * var11.scale(),
                        ColorUtil.withAlpha(var20, var11.opacity())
                     )
                  );
               }
            }

            this.textRenderer.submitLabels(var2, var1);
         }
      }
   }

   public void renderLabels(PoseStack var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.textRenderer != null && this.trackedLevel == getMc().level) {
            this.textRenderer.renderLabels(var1, var2, true);
         }
      }
   }

   public void endBatch() {
      if (this.textRenderer != null) {
         this.textRenderer.endBatch();
      }
   }
}
