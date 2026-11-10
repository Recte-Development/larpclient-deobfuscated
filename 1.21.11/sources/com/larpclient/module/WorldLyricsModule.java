package com.larpclient.module;

import com.larpclient.b9;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.h0;
import com.larpclient.i3;
import com.larpclient.im;
import com.larpclient.q8;
import com.larpclient.to;
import com.larpclient.yn;
import java.util.List;
import net.minecraft.class_12075;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class WorldLyricsModule extends Module {
   private static final double d = 0.75;
   private static WorldLyricsModule N;
   private final li s = this.addSetting(new li("Style", "Fixed plane tilt for new lyric lines", "Calm", "Calm", "Floating", "Dreamy"));
   private final li P = this.addSetting(new li("Motion", "How letters enter, move while sung and leave", "Pop", "Pop", "Wave", "Fade"));
   private final lo w = this.addSetting(new lo("Density", "Length and amount of fading lyric trail", 16.0, 4.0, 32.0, 1.0));
   private final lo t = this.addSetting(new lo("Size", "World text size", 1.0, 0.5, 2.0, 0.1));
   private final l0 T = this.addSetting(new l0("Color", "Lyric tint", -724993));
   private final l3 X = this.addSetting(new l3("Gradient", "Flow the lyric tint into the client accent color", true));
   private final l3 W = this.addSetting(new l3("Glow", "Soft text bloom", true));
   private final l3 a = this.addSetting(new l3("Through Walls", "Keep lyrics visible behind blocks and entities", true));
   private final to m = new to();
   private final yn g = new yn();
   private final Quaterniond k = new Quaterniond();
   private h0 M;
   private im o;
   private Object f;
   private Object q;
   private boolean u;
   private boolean r;
   private long O;
   private double Q;

   public WorldLyricsModule() {
      super(
         "WorldLyrics",
         "Whole Spotify lyric lines spawn near your crosshair, travel with you and animate letter by letter. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.VISUALS,
         -1
      );
      N = this;
   }

   public static WorldLyricsModule k() {
      return N;
   }

   @Override
   public String getDisplayName() {
      return "Spotify World Lyrics";
   }

   @Override
   public String getStatus() {
      return this.M == null ? "Play a song in Spotify desktop" : this.M.v().status();
   }

   @Override
   public void onEnable() {
      this.C();
      this.M = h0.C(this);
      q8.G(
         "WorldLyrics",
         "Spotify World Lyrics enabled; motion="
            + this.P.getValue()
            + ", throughWalls="
            + this.a.getValue()
            + ", lines follow the player with fixed birth orientation"
      );
   }

   @Override
   public void onDisable() {
      this.C();
   }

   public void C() {
      if (this.M != null) {
         h0.w(this);
         this.M = null;
      }

      this.y();
      q8.v("WorldLyrics", "Stopped playback worker and released world text resources");
   }

   public void y() {
      this.m.u();
      this.g.I();
      if (this.o != null) {
         this.o.close();
         this.o = null;
      }

      this.f = this.q = null;
      this.r = this.u = (boolean)0;
      this.O = 0L;
      this.Q = 0.0;
   }

   public void Y(class_12075 var1) {
      if (this.isEnabled()) {
         this.r = this.u = (boolean)0;
         class_310 var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame() && this.getPlayer().method_5805()) {
            if (this.f != var2.field_1687 || this.q != var2.field_1724) {
               this.y();
               this.f = var2.field_1687;
               this.q = var2.field_1724;
               q8.G(
                  "WorldLyrics",
                  "Lyric planes initialized; player-following placement, per-letter motion, "
                     + (this.a.getValue() ? "drawn through blocks" : "native scene depth with surface-aware birth")
               );
            }

            long var3 = System.nanoTime();
            double var5 = this.O == 0L ? 0.0 : Math.clamp((var3 - this.O) / 1.0E9, 0.0, 0.1);
            this.O = var3;
            if (var2.method_1493()) {
               var5 = 0.0;
            }

            this.Q += var5;
            this.g.A(this.w.getValue().intValue());
            if (!var2.method_1493() && this.M != null) {
               boolean var7 = this.m.i(this.M.v(), var3);
               if (this.m.U()) {
                  this.g.I();
               }

               if (var7 || this.m.U()) {
                  if (!this.m.y().isEmpty()) {
                     this.g.t(this.a.getValue() ? 8.0 : this.m(var1));
                  }

                  this.g.b(this.m.y(), this.m.W());
               }
            }

            if (this.o == null) {
               this.o = new im();
            }

            double var13 = this.s.G("Floating") ? 1.5 : (this.s.G("Dreamy") ? 2.2 : 0.65);
            List var9 = this.g
               .w(
                  var1.field_63078.field_1352,
                  var1.field_63078.field_1351,
                  var1.field_63078.field_1350,
                  this.k.set(var1.field_63081),
                  1.0F / b9.v().m00(),
                  1.0F / b9.v().m11(),
                  this.t.getValue(),
                  var13,
                  var5,
                  this.o::h
               );
            int var10 = this.P.G("Pop") ? 1 : (this.P.G("Wave") ? 2 : 0);
            int var11 = this.X.getValue() ? ez.a | 0xFF000000 : 0;
            List var12 = var9.stream()
               .map(
                  var3x -> new im.u(
                     var3x.text(),
                     var3x.x(),
                     var3x.y(),
                     var3x.z(),
                     var3x.scale(),
                     ei.R(this.T.getValue(), var10 == 0 ? var3x.opacity() : 1.0),
                     (float)var3x.qx(),
                     (float)var3x.qy(),
                     (float)var3x.qz(),
                     (float)var3x.qw(),
                     true,
                     var10 == 0 && var11 == 0 ? null : new im.p(var10, Math.min(1.0, var3x.age() / 0.75), var3x.exit(), this.Q, var11)
                  )
               )
               .toList();
            this.o.R(var12, var1);
            this.r = (boolean)(!var12.isEmpty() ? 1 : 0);
            this.u = (boolean)(this.r && this.W.getValue() ? 1 : 0);
         }
      }
   }

   private double m(class_12075 var1) {
      Vector3d var2 = new Vector3d(0.0, 0.0, -1.0).rotate(this.k.set(var1.field_63081)).normalize();
      class_243 var3 = var1.field_63078.method_1031(var2.x * 7.99, var2.y * 7.99, var2.z * 7.99);
      i3.j var4 = i3.c(getMc().field_1687, var1.field_63078, var3);
      return var4 == null ? 8.0 : Math.max(0.6, var1.field_63078.method_1022(var4.point()) - 0.35);
   }

   public boolean S() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.u && this.o != null && this.o.c();
   }

   public void p(class_4587 var1, class_12075 var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.r && this.o != null && this.f == getMc().field_1687) {
            if (!var3 || this.u) {
               this.o.A(var1, var2, this.a.getValue(), !var3, var3 ? 0.3 : 1.0);
            }
         }
      }
   }

   public void n() {
      if (this.o != null) {
         this.o.i();
      }
   }
}
