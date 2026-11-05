package com.larpclient.module;

import com.larpclient.hg;
import com.larpclient.r4;
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
   private final lc d = this.addSetting(new lc("Opacity", "Opacity of detected chunk volumes", 140.0, 10.0, 255.0, 1.0));
   private final lc Y = this.addSetting(new lc("Sensitivity", "Minimum nearby anomalous chunks required for a discovery", 1.0, 1.0, 5.0, 1.0));
   private final ln h = this.addSetting(new ln("HUD Notification", "Show a local discovery notification", true));
   private final ln H = this.addSetting(new ln("Sound Notification", "Play the source note-block chime on a discovery", true));
   private final ln p = this.addSetting(new ln("Chat Notifications", "Write discoveries to local chat", true));
   private final ln C = this.addSetting(new ln("Show Coords", "Include representative chunk-center coordinates in chat", true));
   private final ln V = this.addSetting(new ln("Show Tracers", "Draw camera-to-discovery lines", false));
   private final lc F = this.addSetting(new lc("Tracers Alpha", "Opacity of camera-to-discovery lines", 70.0, 0.0, 255.0, 1.0));
   private final lv P = this.addSetting(new lv("Highlight Color", "Detected chunk and tracer color; source blue by default", -14122241));
   private final com.larpclient.l8 K = new com.larpclient.l8();
   private Set<Long> i = Set.of();
   private ClientLevel Z;
   private boolean T;
   private static MotionScannerModule W;

   public MotionScannerModule() {
      super("Motion Scanner", "Detect unusual received skylight patterns below sea level", Category.RENDER, -1);
      W = this;
      this.F.x(this.V::getValue);
      this.C.x(this.p::getValue);
      this.Y.W(var1 -> this.T = (boolean)1);
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.Z) {
            this.K.y(var2.getPos().pack());
            this.T = (boolean)1;
            this.i = this.K.U();
         }
      });
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.o());
   }

   public List<l6<?>> h() {
      return List.of(this.Y);
   }

   public List<l6<?>> s() {
      return List.of(this.h, this.H, this.p, this.C);
   }

   public List<l6<?>> j() {
      return List.of(this.d, this.P, this.V, this.F);
   }

   private void H(ClientLevel var1) {
      this.K.v();
      this.i = Set.of();
      this.T = (boolean)0;
      this.Z = var1;
      hg.B().M(this.getName());
   }

   @Override
   public void onEnable() {
      this.H(getMc().level);
      r4.V("BaseFinding", "Motion Scanner active: explicit zeroed sky-light sections below Y 62, cluster threshold {}", this.Y.getValue());
   }

   @Override
   public void onDisable() {
      r4.g("BaseFinding", "Motion Scanner cleanup: {} tracked chunks", this.K.H());
      this.H(null);
   }

   public static void C(ClientLevel var0, int var1, int var2, ClientboundLightUpdatePacketData var3) {
      if (W != null && W.isEnabled()) {
         MotionScannerModule var4 = W;
         if (var4 != null && var4.isRuntimeActive() && var0 != null && var3 != null) {
            Minecraft var5 = getMc();
            if (var5.isSameThread() && var5.level == var0 && var5.player != null && !(var5.player.getY() < 0.0)) {
               if (var4.Z != var0) {
                  var4.H(var0);
               }

               if (var0.getChunkSource().getChunkNow(var1, var2) != null) {
                  try {
                     List var6 = var3.skyUpdates().stream().map(com.larpclient.l8::V).toList();
                     boolean var7 = var4.K.f(var1, var2, var0.getSectionsCount(), var0.getMinSectionY(), var3.skyYMask(), var3.emptySkyYMask(), var6);
                     var4.T |= var7;
                  } catch (RuntimeException var8) {
                     r4.T("BaseFinding", "Motion Scanner packet classification failed; later light updates can recover", var8);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.Z != mc.level) {
            this.H(mc.level);
         }

         int var10000 = this.K.T(var1x -> {
            ChunkPos var3x = ChunkPos.unpack(var1x);
            return this.Z.getChunkSource().getChunkNow(var3x.x(), var3x.z()) != null;
         });
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.i = this.K.U();
         }

         double var14;
         var10000 = (var14 = mc.player.getY() - 0.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
         boolean var8 = false;
         int var7 = var10000;
         if (Integer.signum(var7) >>> 31 == 0) {
            boolean var10 = false;
            byte var9 = this.T;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 != 0) {
               ChunkPos var1 = mc.player.chunkPosition();

               for (long var3 : this.K.N(this.Y.getValue().intValue(), var1.x(), var1.z())) {
                  this.S(var3);
               }

               this.i = this.K.U();
               this.T = (boolean)0;
               return;
            }
         }
      }
   }

   private void S(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      int var4 = var3.getMinBlockX() + 8;
      int var5 = var3.getMinBlockZ() + 8;
      r4.g("BaseFinding", "Motion Scanner discovery: chunk [{}, {}], center {}, {}", var3.x(), var3.z(), var4, var5);
      if (this.H.getValue()) {
         mc.player.playSound((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.2F, 1.2F);
      }

      if (this.p.getValue()) {
         mc.gui
            .chatListener()
            .handleSystemMessage(Component.literal("(Motion Scanner) Base detected" + (this.C.getValue() ? ": " + var4 + ", " + var5 : "")), false);
      }

      if (this.h.getValue()) {
         hg.B().o(this.getName(), "Base detected", hg.u.U);
      }
   }

   private void o() {
      if (this.isRuntimeActive() && this.isInGame() && this.Z == mc.level) {
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.P.getValue() & 16777215 | this.d.getValue().intValue() << 24;
         Camera var3 = mc.gameRenderer.mainCamera();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
         Vec3 var5 = var3.position().add(var4.x * 0.1, var4.y * 0.1, var4.z * 0.1);

         try {
            for (long var7 : this.i) {
               ChunkPos var9 = ChunkPos.unpack(var7);
               if (Math.abs((long)var9.x() - var1.x()) <= 17L
                  && Math.abs((long)var9.z() - var1.z()) <= 17L
                  && this.Z.getChunkSource().getChunkNow(var9.x(), var9.z()) != null) {
                  AABB var10 = new AABB(var9.getMinBlockX(), -64.0, var9.getMinBlockZ(), var9.getMinBlockX() + 16, 320.0, var9.getMinBlockZ() + 16);
                  Gizmos.cuboid(var10, GizmoStyle.fill(var2)).setAlwaysOnTop();
                  if (this.V.getValue() && this.F.getValue() > 0.0) {
                     Gizmos.line(var5, var10.getCenter(), var2 & 16777215 | this.F.getValue().intValue() << 24, 1.0F).setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var11) {
            r4.T("BaseFinding", "Motion Scanner render failed; evidence retained", var11);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.i.size() + " discoveries";
   }
}
