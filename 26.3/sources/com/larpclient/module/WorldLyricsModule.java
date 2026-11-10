package com.larpclient.module;

import com.larpclient.__;
import com.larpclient._q;
import com.larpclient.nw;
import com.larpclient.or;
import com.larpclient.ov;
import com.larpclient.v9;
import com.larpclient.vv;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class WorldLyricsModule extends Module {
   private static final double v = 0.75;
   private static WorldLyricsModule B;
   private final o H = this.addSetting(new o("Style", "Fixed plane tilt for new lyric lines", "Calm", "Calm", "Floating", "Dreamy"));
   private final o E = this.addSetting(new o("Motion", "How letters enter, move while sung and leave", "Pop", "Pop", "Wave", "Fade"));
   private final b m = this.addSetting(new b("Density", "Length and amount of fading lyric trail", 16.0, 4.0, 32.0, 1.0));
   private final b f = this.addSetting(new b("Size", "World text size", 1.0, 0.5, 2.0, 0.1));
   private final h a = this.addSetting(new h("Color", "Lyric tint", -724993));
   private final m l = this.addSetting(new m("Gradient", "Flow the lyric tint into the client accent color", true));
   private final m t = this.addSetting(new m("Glow", "Soft text bloom", true));
   private final m O = this.addSetting(new m("Through Walls", "Keep lyrics visible behind blocks and entities", true));
   private final or W = new or();
   private final nw e = new nw();
   private final Quaterniond F = new Quaterniond();
   private ov y;
   private __ s;
   private Object Z;
   private Object K;
   private boolean V;
   private boolean N;
   private long q;
   private double o;

   public WorldLyricsModule() {
      super(
         "WorldLyrics",
         "Whole Spotify lyric lines spawn near your crosshair, travel with you and animate letter by letter. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.VISUALS,
         -1
      );
      B = this;
   }

   public static WorldLyricsModule f() {
      return B;
   }

   @Override
   public String getDisplayName() {
      return "Spotify World Lyrics";
   }

   @Override
   public String getStatus() {
      return this.y == null ? "Play a song in Spotify desktop" : this.y.X().status();
   }

   @Override
   public void onEnable() {
      this.i();
      this.y = ov.Y(this);
      _q.z(
         "WorldLyrics",
         "Spotify World Lyrics enabled; motion="
            + this.E.getValue()
            + ", throughWalls="
            + this.O.getValue()
            + ", lines follow the player with fixed birth orientation"
      );
   }

   @Override
   public void onDisable() {
      this.i();
   }

   public void i() {
      if (this.y != null) {
         ov.E(this);
         this.y = null;
      }

      this.y();
      _q.c("WorldLyrics", "Stopped playback worker and released world text resources");
   }

   public void y() {
      this.W.A();
      this.e.G();
      if (this.s != null) {
         this.s.close();
         this.s = null;
      }

      this.Z = this.K = null;
      this.N = this.V = (boolean)0;
      this.q = 0L;
      this.o = 0.0;
   }

   public void N(CameraRenderState var1) {
      if (this.isEnabled()) {
         this.N = this.V = (boolean)0;
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame() && this.getPlayer().isAlive()) {
            if (this.Z != var2.level || this.K != var2.player) {
               this.y();
               this.Z = var2.level;
               this.K = var2.player;
               _q.z(
                  "WorldLyrics",
                  "Lyric planes initialized; player-following placement, per-letter motion, "
                     + (this.O.getValue() ? "drawn through blocks" : "native scene depth with surface-aware birth")
               );
            }

            long var3 = System.nanoTime();
            double var5 = this.q == 0L ? 0.0 : Math.clamp((var3 - this.q) / 1.0E9, 0.0, 0.1);
            this.q = var3;
            if (var2.isPaused()) {
               var5 = 0.0;
            }

            this.o += var5;
            this.e.j(this.m.getValue().intValue());
            if (!var2.isPaused() && this.y != null) {
               boolean var7 = this.W.N(this.y.X(), var3);
               if (this.W.p()) {
                  this.e.G();
               }

               if (var7 || this.W.p()) {
                  if (!this.W.F().isEmpty()) {
                     this.e.t(this.O.getValue() ? 8.0 : this.s(var1));
                  }

                  this.e.x(this.W.F(), this.W.B());
               }
            }

            if (this.s == null) {
               this.s = new __();
            }

            double var13 = this.H.w("Floating") ? 1.5 : (this.H.w("Dreamy") ? 2.2 : 0.65);
            List var9 = this.e
               .N(
                  var1.pos.x,
                  var1.pos.y,
                  var1.pos.z,
                  this.F.set(var1.orientation),
                  1.0F / var1.projectionMatrix.m00(),
                  1.0F / var1.projectionMatrix.m11(),
                  this.f.getValue(),
                  var13,
                  var5,
                  this.s::R
               );
            int var10 = this.E.w("Pop") ? 1 : (this.E.w("Wave") ? 2 : 0);
            int var11 = this.l.getValue() ? com.larpclient.c.y | 0xFF000000 : 0;
            List var12 = var9.stream()
               .map(
                  var3x -> new __.n(
                     var3x.text(),
                     var3x.x(),
                     var3x.y(),
                     var3x.z(),
                     var3x.scale(),
                     vv.O(this.a.getValue(), var10 == 0 ? var3x.opacity() : 1.0),
                     (float)var3x.qx(),
                     (float)var3x.qy(),
                     (float)var3x.qz(),
                     (float)var3x.qw(),
                     true,
                     var10 == 0 && var11 == 0 ? null : new __.l(var10, Math.min(1.0, var3x.age() / 0.75), var3x.exit(), this.o, var11)
                  )
               )
               .toList();
            this.s.a(var12, var1);
            this.N = (boolean)(!var12.isEmpty() ? 1 : 0);
            this.V = (boolean)(this.N && this.t.getValue() ? 1 : 0);
         }
      }
   }

   private double s(CameraRenderState var1) {
      Vector3d var2 = new Vector3d(0.0, 0.0, -1.0).rotate(this.F.set(var1.orientation)).normalize();
      Vec3 var3 = var1.pos.add(var2.x * 7.99, var2.y * 7.99, var2.z * 7.99);
      v9.d var4 = v9.u(getMc().level, var1.pos, var3);
      return var4 == null ? 8.0 : Math.max(0.6, var1.pos.distanceTo(var4.point()) - 0.35);
   }

   public boolean B() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.V && this.s != null && this.s.H();
   }

   public void T(PoseStack var1, CameraRenderState var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.N && this.s != null && this.Z == getMc().level) {
            if (!var3 || this.V) {
               this.s.r(var1, var2, this.O.getValue(), !var3, var3 ? 0.3 : 1.0);
            }
         }
      }
   }

   public void J() {
      if (this.s != null) {
         this.s.b();
      }
   }
}
