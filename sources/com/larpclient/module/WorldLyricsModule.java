package com.larpclient.module;

import com.larpclient.et;
import com.larpclient.o1;
import com.larpclient.oc;
import com.larpclient.oh;
import com.larpclient.ok;
import com.larpclient.or;
import com.larpclient.r4;
import com.larpclient.rv;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class WorldLyricsModule extends Module {
   private static final double u = 0.75;
   private static WorldLyricsModule M;
   private final lp w = this.addSetting(new lp("Style", "Fixed plane tilt for new lyric lines", "Calm", "Calm", "Floating", "Dreamy"));
   private final lp C = this.addSetting(new lp("Motion", "How letters enter, move while sung and leave", "Pop", "Pop", "Wave", "Fade"));
   private final lc n = this.addSetting(new lc("Density", "Length and amount of fading lyric trail", 16.0, 4.0, 32.0, 1.0));
   private final lc g = this.addSetting(new lc("Size", "World text size", 1.0, 0.5, 2.0, 0.1));
   private final lv K = this.addSetting(new lv("Color", "Lyric tint", -724993));
   private final ln k = this.addSetting(new ln("Gradient", "Flow the lyric tint into the client accent color", true));
   private final ln J = this.addSetting(new ln("Glow", "Soft text bloom", true));
   private final ln X = this.addSetting(new ln("Through Walls", "Keep lyrics visible behind blocks and entities", true));
   private final or y = new or();
   private final o1 e = new o1();
   private final Quaterniond a = new Quaterniond();
   private oc j;
   private ok x;
   private Object V;
   private Object d;
   private boolean N;
   private boolean o;
   private long W;
   private double R;

   public WorldLyricsModule() {
      super(
         "WorldLyrics",
         "Whole Spotify lyric lines spawn near your crosshair, travel with you and animate letter by letter. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.VISUALS,
         -1
      );
      M = this;
   }

   public static WorldLyricsModule A() {
      return M;
   }

   @Override
   public String getDisplayName() {
      return "Spotify World Lyrics";
   }

   @Override
   public String getStatus() {
      return this.j == null ? "Play a song in Spotify desktop" : this.j.z().status();
   }

   @Override
   public void onEnable() {
      this.v();
      this.j = oc.I(this);
      r4.V(
         "WorldLyrics",
         "Spotify World Lyrics enabled; motion="
            + this.C.getValue()
            + ", throughWalls="
            + this.X.getValue()
            + ", lines follow the player with fixed birth orientation"
      );
   }

   @Override
   public void onDisable() {
      this.v();
   }

   public void v() {
      if (this.j != null) {
         oc.m(this);
         this.j = null;
      }

      this.Q();
      r4.g("WorldLyrics", "Stopped playback worker and released world text resources");
   }

   public void Q() {
      this.y.n();
      this.e.p();
      if (this.x != null) {
         this.x.close();
         this.x = null;
      }

      this.V = this.d = null;
      this.o = this.N = (boolean)0;
      this.W = 0L;
      this.R = 0.0;
   }

   public void v(CameraRenderState var1) {
      if (this.isEnabled()) {
         this.o = this.N = (boolean)0;
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame() && this.getPlayer().isAlive()) {
            if (this.V != var2.level || this.d != var2.player) {
               this.Q();
               this.V = var2.level;
               this.d = var2.player;
               r4.V(
                  "WorldLyrics",
                  "Lyric planes initialized; player-following placement, per-letter motion, "
                     + (this.X.getValue() ? "drawn through blocks" : "native scene depth with surface-aware birth")
               );
            }

            long var3 = System.nanoTime();
            double var5 = this.W == 0L ? 0.0 : Math.clamp((var3 - this.W) / 1.0E9, 0.0, 0.1);
            this.W = var3;
            if (var2.isPaused()) {
               var5 = 0.0;
            }

            this.R += var5;
            this.e.g(this.n.getValue().intValue());
            if (!var2.isPaused() && this.j != null) {
               boolean var7 = this.y.a(this.j.z(), var3);
               if (this.y.r()) {
                  this.e.p();
               }

               if (var7 || this.y.r()) {
                  if (!this.y.X().isEmpty()) {
                     this.e.y(this.X.getValue() ? 8.0 : this.D(var1));
                  }

                  this.e.q(this.y.X(), this.y.U());
               }
            }

            if (this.x == null) {
               this.x = new ok();
            }

            double var13 = this.w.i("Floating") ? 1.5 : (this.w.i("Dreamy") ? 2.2 : 0.65);
            List var9 = this.e
               .p(
                  var1.pos.x,
                  var1.pos.y,
                  var1.pos.z,
                  this.a.set(var1.orientation),
                  1.0F / var1.projectionMatrix.m00(),
                  1.0F / var1.projectionMatrix.m11(),
                  this.g.getValue(),
                  var13,
                  var5,
                  this.x::B
               );
            int var10 = this.C.i("Pop") ? 1 : (this.C.i("Wave") ? 2 : 0);
            int var11 = this.k.getValue() ? rv.e | 0xFF000000 : 0;
            List var12 = var9.stream()
               .map(
                  var3x -> new ok.p(
                     var3x.text(),
                     var3x.x(),
                     var3x.y(),
                     var3x.z(),
                     var3x.scale(),
                     oh.S(this.K.getValue(), var10 == 0 ? var3x.opacity() : 1.0),
                     (float)var3x.qx(),
                     (float)var3x.qy(),
                     (float)var3x.qz(),
                     (float)var3x.qw(),
                     true,
                     var10 == 0 && var11 == 0 ? null : new ok.f(var10, Math.min(1.0, var3x.age() / 0.75), var3x.exit(), this.R, var11)
                  )
               )
               .toList();
            this.x.I(var12, var1);
            this.o = (boolean)(!var12.isEmpty() ? 1 : 0);
            this.N = (boolean)(this.o && this.J.getValue() ? 1 : 0);
         }
      }
   }

   private double D(CameraRenderState var1) {
      Vector3d var2 = new Vector3d(0.0, 0.0, -1.0).rotate(this.a.set(var1.orientation)).normalize();
      Vec3 var3 = var1.pos.add(var2.x * 7.99, var2.y * 7.99, var2.z * 7.99);
      et.d var4 = et.Q(getMc().level, var1.pos, var3);
      return var4 == null ? 8.0 : Math.max(0.6, var1.pos.distanceTo(var4.point()) - 0.35);
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.N && this.x != null && this.x.S();
   }

   public void e(PoseStack var1, CameraRenderState var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.o && this.x != null && this.V == getMc().level) {
            if (!var3 || this.N) {
               this.x.a(var1, var2, this.X.getValue(), !var3, var3 ? 0.3 : 1.0);
            }
         }
      }
   }

   public void w() {
      if (this.x != null) {
         this.x.E();
      }
   }
}
