package com.larpclient.render.entity;

import com.larpclient.gui.theme.Theme;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

public final class GhostEcho {
   private final ArrayDeque<GhostEcho.EchoSnapshot> echoes = new ArrayDeque<>();
   private double lastSpawnTime = -100.0;
   private Vec3 lastPosition;
   private boolean capturePending;
   private List<GhostEcho.EchoRenderEntry> visibleEchoes = List.of();
   private boolean hasBloom;
   public double spawnInterval = 1.2;
   public double lifetime = 2.4;
   public double opacity = 0.45;
   public int maxEchoes = 6;
   public int color = -8798209;
   public boolean bloom = (boolean)1;
   public String style = "Original";
   public String fadeMode = "Smooth";

   public boolean hasBloom() {
      return this.hasBloom;
   }

   public void update(Vec3 var1) {
      if (this.capturePending) {
         this.capturePending = (boolean)0;
         this.captureEcho();
      }

      ArrayList var2 = new ArrayList();
      this.hasBloom = (boolean)0;

      for (GhostEcho.EchoSnapshot var4 : this.echoes) {
         EntityRenderState var5 = var4.state;
         double var6 = var5.x - var1.x;
         double var8 = var5.y - var1.y;
         double var10 = var5.z - var1.z;
         if (!(var6 * var6 + var8 * var8 + var10 * var10 > 2304.0)) {
            double var12 = Math.clamp((TickTimer.getTime() - var4.born) / var4.lifetime, 0.0, 1.0);
            double var14 = this.opacity * (this.fadeMode.equals("Linear") ? 1.0 - var12 : Easing.smoothstep(1.0 - var12));
            if (!(var14 <= 0.0)) {
               ModelVisualRenderer.Material var16 = switch (this.style) {
                  case "Original" -> ModelVisualRenderer.Material.NORMAL;
                  case "Silhouette" -> ModelVisualRenderer.Material.OUTLINE;
                  case "Hologram" -> ModelVisualRenderer.Material.HOLOGRAM;
                  default -> ModelVisualRenderer.Material.TINT;
               };
               var2.add(new GhostEcho.EchoRenderEntry(var5, var16, this.style.equals("Theme Tint") ? Theme.accent : this.color, var14, this.bloom));
               this.hasBloom = this.hasBloom | this.bloom;
            }
         }
      }

      this.visibleEchoes = List.copyOf(var2);
   }

   public void tick() {
      while (this.echoes.size() > Math.clamp(this.maxEchoes, 1, 12)) {
         this.echoes.removeFirst();
      }

      this.echoes.removeIf(var0 -> TickTimer.getTickTime() - var0.born >= var0.lifetime);
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || !var1.player.isAlive()) {
         this.clear();
      }
   }

   public void requestCapture() {
      this.capturePending = (boolean)1;
   }

   private void captureEcho() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.isAlive()) {
         double var2 = TickTimer.getTickTime();
         Vec3 var4 = var1.player.position();
         if (!(var2 - this.lastSpawnTime < this.spawnInterval) && (this.lastPosition == null || !(this.lastPosition.distanceToSqr(var4) < 0.04))) {
            if (this.lastPosition == null) {
               this.lastPosition = var4;
               this.lastSpawnTime = var2;
            } else if (this.lastPosition.distanceToSqr(var4) > 64.0) {
               this.clear();
               this.lastPosition = var4;
               this.lastSpawnTime = var2;
            } else {
               EntityRenderState var5 = ModelVisualRenderer.extractEntity(var1.player, 1.0F);
               var5.nameTag = null;
               var5.scoreText = null;
               var5.shadowPieces.clear();
               var5.outlineColor = 0;

               while (this.echoes.size() >= Math.clamp(this.maxEchoes, 1, 12)) {
                  this.echoes.removeFirst();
               }

               this.echoes.addLast(new GhostEcho.EchoSnapshot(var5, var2, this.lifetime));
               this.lastPosition = var4;
               this.lastSpawnTime = var2;
            }
         }
      }
   }

   public boolean isEmpty() {
      return this.echoes.isEmpty();
   }

   public void clear() {
      this.echoes.clear();
      this.visibleEchoes = List.of();
      this.hasBloom = this.capturePending = (boolean)0;
      this.lastPosition = null;
      this.lastSpawnTime = -100.0;
   }

   public void render(PoseStack var1, EffectBatch var2, boolean var3) {
      long var4 = PerfProfiler.startTimer();

      for (GhostEcho.EchoRenderEntry var7 : this.visibleEchoes) {
         if (!var3 || var7.bloom) {
            ModelVisualRenderer.renderEntity(var7.state, var1, var2, var7.material, var7.color, var7.alpha * (var3 ? 0.5 : 1.0), false);
         }
      }

      PerfProfiler.endTimer("GhostEcho", var4);
   }

   private record EchoRenderEntry(EntityRenderState state, ModelVisualRenderer.Material material, int color, double alpha, boolean bloom) {
   }

   private record EchoSnapshot(EntityRenderState state, double born, double lifetime) {
   }
}
