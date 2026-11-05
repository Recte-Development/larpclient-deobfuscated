package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.ct;
import com.larpclient.mt;
import com.larpclient.od;
import com.larpclient.t9;
import com.larpclient.w8;
import com.larpclient.wj;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class WorldLyricsModule extends Module {
   private static final double N = 0.75;
   private static WorldLyricsModule S;
   private final d6 P = this.addSetting(new d6("Style", "Fixed plane tilt for new lyric lines", "Calm", "Calm", "Floating", "Dreamy"));
   private final d6 O = this.addSetting(new d6("Motion", "How letters enter, move while sung and leave", "Pop", "Pop", "Wave", "Fade"));
   private final d9 W = this.addSetting(new d9("Density", "Length and amount of fading lyric trail", 16.0, 4.0, 32.0, 1.0));
   private final d9 t = this.addSetting(new d9("Size", "World text size", 1.0, 0.5, 2.0, 0.1));
   private final dk R = this.addSetting(new dk("Color", "Lyric tint", -724993));
   private final dh Q = this.addSetting(new dh("Gradient", "Flow the lyric tint into the client accent color", true));
   private final dh E = this.addSetting(new dh("Glow", "Soft text bloom", true));
   private final dh c = this.addSetting(new dh("Through Walls", "Keep lyrics visible behind blocks and entities", true));
   private final t9 d = new t9();
   private final ct n = new ct();
   private final Quaterniond b = new Quaterniond();
   private wj T;
   private od B;
   private Object C;
   private Object f;
   private boolean G;
   private boolean I;
   private long e;
   private double r;

   public WorldLyricsModule() {
      super(
         "WorldLyrics",
         "Whole Spotify lyric lines spawn near your crosshair, travel with you and animate letter by letter. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.VISUALS,
         -1
      );
      S = this;
   }

   public static WorldLyricsModule s() {
      return S;
   }

   @Override
   public String getDisplayName() {
      return "Spotify World Lyrics";
   }

   @Override
   public String getStatus() {
      return this.T == null ? "Play a song in Spotify desktop" : this.T.U().status();
   }

   @Override
   public void onEnable() {
      this.d();
      this.T = wj.O(this);
      mt.K(
         "WorldLyrics",
         "Spotify World Lyrics enabled; motion=" + this.O.P() + ", throughWalls=" + this.c.P() + ", lines follow the player with fixed birth orientation"
      );
   }

   @Override
   public void onDisable() {
      this.d();
   }

   public void d() {
      if (this.T != null) {
         wj.U(this);
         this.T = null;
      }

      this.n();
      mt.U("WorldLyrics", "Stopped playback worker and released world text resources");
   }

   public void n() {
      this.d.n();
      this.n.c();
      if (this.B != null) {
         this.B.close();
         this.B = null;
      }

      this.C = this.f = null;
      this.I = this.G = (boolean)0;
      this.e = 0L;
      this.r = 0.0;
   }

   public void H(CameraRenderState var1) {
      if (this.isEnabled()) {
         this.I = this.G = (boolean)0;
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame() && this.getPlayer().isAlive()) {
            if (this.C != var2.level || this.f != var2.player) {
               this.n();
               this.C = var2.level;
               this.f = var2.player;
               mt.K(
                  "WorldLyrics",
                  "Lyric planes initialized; player-following placement, per-letter motion, "
                     + (this.c.P() ? "drawn through blocks" : "native scene depth with surface-aware birth")
               );
            }

            long var3 = System.nanoTime();
            double var5 = this.e == 0L ? 0.0 : Math.clamp((var3 - this.e) / 1.0E9, 0.0, 0.1);
            this.e = var3;
            if (var2.isPaused()) {
               var5 = 0.0;
            }

            this.r += var5;
            this.n.g(this.W.P().intValue());
            if (!var2.isPaused() && this.T != null) {
               boolean var7 = this.d.K(this.T.U(), var3);
               if (this.d.Q()) {
                  this.n.c();
               }

               if (var7 || this.d.Q()) {
                  if (!this.d.Y().isEmpty()) {
                     this.n.s(this.c.P() ? 8.0 : this.F(var1));
                  }

                  this.n.j(this.d.Y(), this.d.P());
               }
            }

            if (this.B == null) {
               this.B = new od();
            }

            double var13 = this.P.S("Floating") ? 1.5 : (this.P.S("Dreamy") ? 2.2 : 0.65);
            List var9 = this.n
               .m(
                  var1.pos.x,
                  var1.pos.y,
                  var1.pos.z,
                  this.b.set(var1.orientation),
                  1.0F / var1.projectionMatrix.m00(),
                  1.0F / var1.projectionMatrix.m11(),
                  this.t.P(),
                  var13,
                  var5,
                  this.B::k
               );
            int var10 = this.O.S("Pop") ? 1 : (this.O.S("Wave") ? 2 : 0);
            int var11 = this.Q.P() ? com.larpclient.f.C | 0xFF000000 : 0;
            List var12 = var9.stream()
               .map(
                  var3x -> new od.l(
                     var3x.text(),
                     var3x.x(),
                     var3x.y(),
                     var3x.z(),
                     var3x.scale(),
                     _3.U(this.R.P(), var10 == 0 ? var3x.opacity() : 1.0),
                     (float)var3x.qx(),
                     (float)var3x.qy(),
                     (float)var3x.qz(),
                     (float)var3x.qw(),
                     true,
                     var10 == 0 && var11 == 0 ? null : new od.w(var10, Math.min(1.0, var3x.age() / 0.75), var3x.exit(), this.r, var11)
                  )
               )
               .toList();
            this.B.H(var12, var1);
            this.I = (boolean)(!var12.isEmpty() ? 1 : 0);
            this.G = (boolean)(this.I && this.E.P() ? 1 : 0);
         }
      }
   }

   private double F(CameraRenderState var1) {
      Vector3d var2 = new Vector3d(0.0, 0.0, -1.0).rotate(this.b.set(var1.orientation)).normalize();
      Vec3 var3 = var1.pos.add(var2.x * 7.99, var2.y * 7.99, var2.z * 7.99);
      w8.n var4 = w8.G(getMc().level, var1.pos, var3);
      return var4 == null ? 8.0 : Math.max(0.6, var1.pos.distanceTo(var4.point()) - 0.35);
   }

   public boolean L() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.G && this.B != null && this.B.Y();
   }

   public void N(PoseStack var1, CameraRenderState var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.I && this.B != null && this.C == getMc().level) {
            if (!var3 || this.G) {
               this.B.i(var1, var2, this.c.P(), !var3, var3 ? 0.3 : 1.0);
            }
         }
      }
   }

   public void r() {
      if (this.B != null) {
         this.B.J();
      }
   }
}
