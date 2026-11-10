package com.larpclient.module;

import com.larpclient.lb;
import com.larpclient.s0;
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
   private final e d = this.addSetting(new e("Opacity", "Opacity of detected chunk volumes", 140.0, 10.0, 255.0, 1.0));
   private final e H = this.addSetting(new e("Sensitivity", "Minimum nearby anomalous chunks required for a discovery", 1.0, 1.0, 5.0, 1.0));
   private final s K = this.addSetting(new s("HUD Notification", "Show a local discovery notification", true));
   private final s a = this.addSetting(new s("Sound Notification", "Play the source note-block chime on a discovery", true));
   private final s P = this.addSetting(new s("Chat Notifications", "Write discoveries to local chat", true));
   private final s n = this.addSetting(new s("Show Coords", "Include representative chunk-center coordinates in chat", true));
   private final s w = this.addSetting(new s("Show Tracers", "Draw camera-to-discovery lines", false));
   private final e A = this.addSetting(new e("Tracers Alpha", "Opacity of camera-to-discovery lines", 70.0, 0.0, 255.0, 1.0));
   private final i O = this.addSetting(new i("Highlight Color", "Detected chunk and tracer color; source blue by default", -14122241));
   private final lb l = new lb();
   private Set<Long> B = Set.of();
   private ClientLevel o;
   private boolean b;
   private static MotionScannerModule M;

   public MotionScannerModule() {
      super("Motion Scanner", "Detect unusual received skylight patterns below sea level", Category.RENDER, -1);
      M = this;
      this.A.P(this.w::getValue);
      this.n.P(this.P::getValue);
      this.H.o(var1 -> this.b = (boolean)1);
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.o) {
            this.l.b(var2.getPos().pack());
            this.b = (boolean)1;
            this.B = this.l.t();
         }
      });
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.Z());
   }

   public List<q<?>> C() {
      return List.of(this.H);
   }

   public List<q<?>> N() {
      return List.of(this.K, this.a, this.P, this.n);
   }

   public List<q<?>> w() {
      return List.of(this.d, this.O, this.w, this.A);
   }

   private void Y(ClientLevel var1) {
      this.l.m();
      this.B = Set.of();
      this.b = (boolean)0;
      this.o = var1;
      s0.s().G(this.getName());
   }

   @Override
   public void onEnable() {
      this.Y(getMc().level);
      com.larpclient.h.A("BaseFinding", "Motion Scanner active: explicit zeroed sky-light sections below Y 62, cluster threshold {}", this.H.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("BaseFinding", "Motion Scanner cleanup: {} tracked chunks", this.l.L());
      this.Y(null);
   }

   public static void t(ClientLevel var0, int var1, int var2, ClientboundLightUpdatePacketData var3) {
      if (M != null && M.isEnabled()) {
         MotionScannerModule var4 = M;
         if (var4 != null && var4.isRuntimeActive() && var0 != null && var3 != null) {
            Minecraft var5 = getMc();
            if (var5.isSameThread() && var5.level == var0 && var5.player != null && !(var5.player.getY() < 0.0)) {
               if (var4.o != var0) {
                  var4.Y(var0);
               }

               if (var0.getChunkSource().getChunkNow(var1, var2) != null) {
                  try {
                     List var6 = var3.getSkyUpdates().stream().map(lb::Z).toList();
                     boolean var7 = var4.l.h(var1, var2, var0.getSectionsCount(), var0.getMinSectionY(), var3.getSkyYMask(), var3.getEmptySkyYMask(), var6);
                     var4.b |= var7;
                  } catch (RuntimeException var8) {
                     com.larpclient.h.w("BaseFinding", "Motion Scanner packet classification failed; later light updates can recover", var8);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.o != mc.level) {
            this.Y(mc.level);
         }

         int var10000 = this.l.Q(var1x -> {
            ChunkPos var3x = ChunkPos.unpack(var1x);
            return this.o.getChunkSource().getChunkNow(var3x.x(), var3x.z()) != null;
         });
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.B = this.l.t();
         }

         double var14;
         var10000 = (var14 = mc.player.getY() - 0.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
         boolean var8 = false;
         int var7 = var10000;
         if (Integer.signum(var7) >>> 31 == 0) {
            boolean var10 = false;
            byte var9 = this.b;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 != 0) {
               ChunkPos var1 = mc.player.chunkPosition();

               for (long var3 : this.l.V(this.H.getValue().intValue(), var1.x(), var1.z())) {
                  this.S(var3);
               }

               this.B = this.l.t();
               this.b = (boolean)0;
               return;
            }
         }
      }
   }

   private void S(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      int var4 = var3.getMinBlockX() + 8;
      int var5 = var3.getMinBlockZ() + 8;
      com.larpclient.h.U("BaseFinding", "Motion Scanner discovery: chunk [{}, {}], center {}, {}", var3.x(), var3.z(), var4, var5);
      if (this.a.getValue()) {
         mc.player.playSound((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.2F, 1.2F);
      }

      if (this.P.getValue()) {
         mc.gui
            .chatListener()
            .handleSystemMessage(Component.literal("(Motion Scanner) Base detected" + (this.n.getValue() ? ": " + var4 + ", " + var5 : "")), false);
      }

      if (this.K.getValue()) {
         s0.s().V(this.getName(), "Base detected", s0.u.B);
      }
   }

   private void Z() {
      if (this.isRuntimeActive() && this.isInGame() && this.o == mc.level) {
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.O.getValue() & 16777215 | this.d.getValue().intValue() << 24;
         Camera var3 = mc.gameRenderer.mainCamera();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
         Vec3 var5 = var3.position().add(var4.x * 0.1, var4.y * 0.1, var4.z * 0.1);

         try {
            for (long var7 : this.B) {
               ChunkPos var9 = ChunkPos.unpack(var7);
               if (Math.abs((long)var9.x() - var1.x()) <= 17L
                  && Math.abs((long)var9.z() - var1.z()) <= 17L
                  && this.o.getChunkSource().getChunkNow(var9.x(), var9.z()) != null) {
                  AABB var10 = new AABB(var9.getMinBlockX(), -64.0, var9.getMinBlockZ(), var9.getMinBlockX() + 16, 320.0, var9.getMinBlockZ() + 16);
                  Gizmos.cuboid(var10, GizmoStyle.fill(var2)).setAlwaysOnTop();
                  if (this.w.getValue() && this.A.getValue() > 0.0) {
                     Gizmos.line(var5, var10.getCenter(), var2 & 16777215 | this.A.getValue().intValue() << 24, 1.0F).setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var11) {
            com.larpclient.h.w("BaseFinding", "Motion Scanner render failed; evidence retained", var11);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.B.size() + " discoveries";
   }
}
