package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.oj;
import com.larpclient.oo;
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
   private final d9 z = this.addSetting(new d9("Opacity", "Opacity of detected chunk volumes", 140.0, 10.0, 255.0, 1.0));
   private final d9 o = this.addSetting(new d9("Sensitivity", "Minimum nearby anomalous chunks required for a discovery", 1.0, 1.0, 5.0, 1.0));
   private final dh t = this.addSetting(new dh("HUD Notification", "Show a local discovery notification", true));
   private final dh u = this.addSetting(new dh("Sound Notification", "Play the source note-block chime on a discovery", true));
   private final dh G = this.addSetting(new dh("Chat Notifications", "Write discoveries to local chat", true));
   private final dh g = this.addSetting(new dh("Show Coords", "Include representative chunk-center coordinates in chat", true));
   private final dh k = this.addSetting(new dh("Show Tracers", "Draw camera-to-discovery lines", false));
   private final d9 i = this.addSetting(new d9("Tracers Alpha", "Opacity of camera-to-discovery lines", 70.0, 0.0, 255.0, 1.0));
   private final dk f = this.addSetting(new dk("Highlight Color", "Detected chunk and tracer color; source blue by default", -14122241));
   private final oo s = new oo();
   private Set<Long> j = Set.of();
   private ClientLevel T;
   private boolean c;
   private static MotionScannerModule D;

   public MotionScannerModule() {
      super("Motion Scanner", "Detect unusual received skylight patterns below sea level", Category.RENDER, -1);
      D = this;
      this.i.H(this.k::P);
      this.g.H(this.G::P);
      this.o.h(var1 -> this.c = (boolean)1);
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.T) {
            this.s.n(var2.getPos().pack());
            this.c = (boolean)1;
            this.j = this.s.v();
         }
      });
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.s());
   }

   public List<df<?>> Z() {
      return List.of(this.o);
   }

   public List<df<?>> J() {
      return List.of(this.t, this.u, this.G, this.g);
   }

   public List<df<?>> c() {
      return List.of(this.z, this.f, this.k, this.i);
   }

   private void p(ClientLevel var1) {
      this.s.w();
      this.j = Set.of();
      this.c = (boolean)0;
      this.T = var1;
      oj.K().B(this.getName());
   }

   @Override
   public void onEnable() {
      this.p(getMc().level);
      mt.K("BaseFinding", "Motion Scanner active: explicit zeroed sky-light sections below Y 62, cluster threshold {}", this.o.P());
   }

   @Override
   public void onDisable() {
      mt.U("BaseFinding", "Motion Scanner cleanup: {} tracked chunks", this.s.V());
      this.p(null);
   }

   public static void Q(ClientLevel var0, int var1, int var2, ClientboundLightUpdatePacketData var3) {
      if (D != null && D.isEnabled()) {
         MotionScannerModule var4 = D;
         if (var4 != null && var4.isRuntimeActive() && var0 != null && var3 != null) {
            Minecraft var5 = getMc();
            if (var5.isSameThread() && var5.level == var0 && var5.player != null && !(var5.player.getY() < 0.0)) {
               if (var4.T != var0) {
                  var4.p(var0);
               }

               if (var0.getChunkSource().getChunkNow(var1, var2) != null) {
                  try {
                     List var6 = var3.skyUpdates().stream().map(oo::Z).toList();
                     boolean var7 = var4.s.q(var1, var2, var0.getSectionsCount(), var0.getMinSectionY(), var3.skyYMask(), var3.emptySkyYMask(), var6);
                     var4.c |= var7;
                  } catch (RuntimeException var8) {
                     mt.B("BaseFinding", "Motion Scanner packet classification failed; later light updates can recover", var8);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.T != mc.level) {
            this.p(mc.level);
         }

         int var10000 = this.s.U(var1x -> {
            ChunkPos var3x = ChunkPos.unpack(var1x);
            return this.T.getChunkSource().getChunkNow(var3x.x(), var3x.z()) != null;
         });
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.j = this.s.v();
         }

         double var14;
         var10000 = (var14 = mc.player.getY() - 0.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
         boolean var8 = false;
         int var7 = var10000;
         if (Integer.signum(var7) >>> 31 == 0) {
            boolean var10 = false;
            byte var9 = this.c;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 != 0) {
               ChunkPos var1 = mc.player.chunkPosition();

               for (long var3 : this.s.t(this.o.P().intValue(), var1.x(), var1.z())) {
                  this.K(var3);
               }

               this.j = this.s.v();
               this.c = (boolean)0;
               return;
            }
         }
      }
   }

   private void K(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      int var4 = var3.getMinBlockX() + 8;
      int var5 = var3.getMinBlockZ() + 8;
      mt.U("BaseFinding", "Motion Scanner discovery: chunk [{}, {}], center {}, {}", var3.x(), var3.z(), var4, var5);
      if (this.u.P()) {
         mc.player.playSound((SoundEvent)SoundEvents.NOTE_BLOCK_CHIME.value(), 1.2F, 1.2F);
      }

      if (this.G.P()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("(Motion Scanner) Base detected" + (this.g.P() ? ": " + var4 + ", " + var5 : "")), false);
      }

      if (this.t.P()) {
         oj.K().x(this.getName(), "Base detected", oj.u.O);
      }
   }

   private void s() {
      if (this.isRuntimeActive() && this.isInGame() && this.T == mc.level) {
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.f.P() & 16777215 | this.z.P().intValue() << 24;
         Camera var3 = mc.gameRenderer.mainCamera();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
         Vec3 var5 = var3.position().add(var4.x * 0.1, var4.y * 0.1, var4.z * 0.1);

         try {
            for (long var7 : this.j) {
               ChunkPos var9 = ChunkPos.unpack(var7);
               if (Math.abs((long)var9.x() - var1.x()) <= 17L
                  && Math.abs((long)var9.z() - var1.z()) <= 17L
                  && this.T.getChunkSource().getChunkNow(var9.x(), var9.z()) != null) {
                  AABB var10 = new AABB(var9.getMinBlockX(), -64.0, var9.getMinBlockZ(), var9.getMinBlockX() + 16, 320.0, var9.getMinBlockZ() + 16);
                  Gizmos.cuboid(var10, GizmoStyle.fill(var2)).setAlwaysOnTop();
                  if (this.k.P() && this.i.P() > 0.0) {
                     Gizmos.line(var5, var10.getCenter(), var2 & 16777215 | this.i.P().intValue() << 24, 1.0F).setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var11) {
            mt.B("BaseFinding", "Motion Scanner render failed; evidence retained", var11);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.j.size() + " discoveries";
   }
}
