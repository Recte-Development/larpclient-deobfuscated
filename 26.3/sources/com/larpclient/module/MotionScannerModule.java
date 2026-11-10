package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.ib;
import com.larpclient.sx;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacketData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class MotionScannerModule extends Module {
   private final b E = this.addSetting(new b("Opacity", "Opacity of detected chunk volumes", 140.0, 10.0, 255.0, 1.0));
   private final b k = this.addSetting(new b("Sensitivity", "Minimum nearby anomalous chunks required for a discovery", 1.0, 1.0, 5.0, 1.0));
   private final m j = this.addSetting(new m("HUD Notification", "Show a local discovery notification", true));
   private final m l = this.addSetting(new m("Sound Notification", "Play the source note-block chime on a discovery", true));
   private final m J = this.addSetting(new m("Chat Notifications", "Write discoveries to local chat", true));
   private final m x = this.addSetting(new m("Show Coords", "Include representative chunk-center coordinates in chat", true));
   private final m r = this.addSetting(new m("Show Tracers", "Draw camera-to-discovery lines", false));
   private final b W = this.addSetting(new b("Tracers Alpha", "Opacity of camera-to-discovery lines", 70.0, 0.0, 255.0, 1.0));
   private final h X = this.addSetting(new h("Highlight Color", "Detected chunk and tracer color; source blue by default", -14122241));
   private final sx V = new sx();
   private Set<Long> O = Set.of();
   private ClientLevel A;
   private boolean Y;
   private static MotionScannerModule y;

   public MotionScannerModule() {
      super("Motion Scanner", "Detect unusual received skylight patterns below sea level", Category.RENDER, -1);
      y = this;
      this.W.a(this.r::getValue);
      this.x.a(this.J::getValue);
      this.k.w(var1 -> this.Y = (boolean)1);
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.A) {
            this.V.y(var2.getPos().pack());
            this.Y = (boolean)1;
            this.O = this.V.t();
         }
      });
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.X());
   }

   public List<r<?>> y() {
      return List.of(this.k);
   }

   public List<r<?>> z() {
      return List.of(this.j, this.l, this.J, this.x);
   }

   public List<r<?>> G() {
      return List.of(this.E, this.X, this.r, this.W);
   }

   private void Y(ClientLevel var1) {
      this.V.k();
      this.O = Set.of();
      this.Y = (boolean)0;
      this.A = var1;
      ib.L().i(this.getName());
   }

   @Override
   public void onEnable() {
      this.Y(getMc().level);
      _q.z("BaseFinding", "Motion Scanner active: explicit zeroed sky-light sections below Y 62, cluster threshold {}", this.k.getValue());
   }

   @Override
   public void onDisable() {
      _q.c("BaseFinding", "Motion Scanner cleanup: {} tracked chunks", this.V.b());
      this.Y(null);
   }

   public static void V(ClientLevel var0, int var1, int var2, ClientboundLightUpdatePacketData var3) {
      if (y != null && y.isEnabled()) {
         MotionScannerModule var4 = y;
         if (var4 != null && var4.isRuntimeActive() && var0 != null && var3 != null) {
            Minecraft var5 = getMc();
            if (var5.isSameThread() && var5.level == var0 && var5.player != null && !(var5.player.getY() < 0.0)) {
               if (var4.A != var0) {
                  var4.Y(var0);
               }

               if (var0.getChunkSource().getChunkNow(var1, var2) != null) {
                  try {
                     List var6 = var3.skyUpdates().stream().map(sx::j).toList();
                     boolean var7 = var4.V.t(var1, var2, var0.getSectionsCount(), var0.getMinSectionY(), var3.skyYMask(), var3.emptySkyYMask(), var6);
                     var4.Y |= var7;
                  } catch (RuntimeException var8) {
                     _q.g("BaseFinding", "Motion Scanner packet classification failed; later light updates can recover", var8);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.A != mc.level) {
            this.Y(mc.level);
         }

         int var10000 = this.V.M(var1x -> {
            ChunkPos var3x = ChunkPos.unpack(var1x);
            return this.A.getChunkSource().getChunkNow(var3x.x(), var3x.z()) != null;
         });
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.O = this.V.t();
         }

         double var14;
         var10000 = (var14 = mc.player.getY() - 0.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
         boolean var8 = false;
         int var7 = var10000;
         if (Integer.signum(var7) >>> 31 == 0) {
            boolean var10 = false;
            byte var9 = this.Y;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 != 0) {
               ChunkPos var1 = mc.player.chunkPosition();

               for (long var3 : this.V.A(this.k.getValue().intValue(), var1.x(), var1.z())) {
                  this.V(var3);
               }

               this.O = this.V.t();
               this.Y = (boolean)0;
               return;
            }
         }
      }
   }

   private void V(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      int var4 = var3.getMinBlockX() + 8;
      int var5 = var3.getMinBlockZ() + 8;
      _q.c("BaseFinding", "Motion Scanner discovery: chunk [{}, {}], center {}, {}", var3.x(), var3.z(), var4, var5);
      if (this.l.getValue()) {
         mc.player.playSound((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.2F, 1.2F);
      }

      if (this.J.getValue()) {
         mc.gui
            .chatListener()
            .handleSystemMessage(Component.literal("(Motion Scanner) Base detected" + (this.x.getValue() ? ": " + var4 + ", " + var5 : "")), false);
      }

      if (this.j.getValue()) {
         ib.L().t(this.getName(), "Base detected", ib.v.f);
      }
   }

   private void X() {
      if (this.isRuntimeActive() && this.isInGame() && this.A == mc.level) {
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.X.getValue() & 16777215 | this.E.getValue().intValue() << 24;
         Camera var3 = mc.gameRenderer.mainCamera();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
         Vec3 var5 = var3.position().add(var4.x * 0.1, var4.y * 0.1, var4.z * 0.1);

         try {
            for (long var7 : this.O) {
               ChunkPos var9 = ChunkPos.unpack(var7);
               if (Math.abs((long)var9.x() - var1.x()) <= 17L
                  && Math.abs((long)var9.z() - var1.z()) <= 17L
                  && this.A.getChunkSource().getChunkNow(var9.x(), var9.z()) != null) {
                  AABB var10 = new AABB(var9.getMinBlockX(), -64.0, var9.getMinBlockZ(), var9.getMinBlockX() + 16, 320.0, var9.getMinBlockZ() + 16);
                  Gizmos.cuboid(var10, GizmoStyle.fill(var2)).setAlwaysOnTop();
                  if (this.r.getValue() && this.W.getValue() > 0.0) {
                     Gizmos.line(var5, var10.getCenter(), var2 & 16777215 | this.W.getValue().intValue() << 24, 1.0F).setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var11) {
            _q.g("BaseFinding", "Motion Scanner render failed; evidence retained", var11);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.O.size() + " discoveries";
   }
}
