package com.larpclient.module;

import com.larpclient.b0;
import com.larpclient.b5;
import com.larpclient.cn;
import com.larpclient.ho;
import com.larpclient.jo;
import com.larpclient.sp;
import com.larpclient.uh;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class WorldLyricsModule extends Module {
   private static final double s = 0.75;
   private static WorldLyricsModule D;
   private final l t = this.addSetting(new l("Style", "Fixed plane tilt for new lyric lines", "Calm", "Calm", "Floating", "Dreamy"));
   private final l k = this.addSetting(new l("Motion", "How letters enter, move while sung and leave", "Pop", "Pop", "Wave", "Fade"));
   private final e V = this.addSetting(new e("Density", "Length and amount of fading lyric trail", 16.0, 4.0, 32.0, 1.0));
   private final e a = this.addSetting(new e("Size", "World text size", 1.0, 0.5, 2.0, 0.1));
   private final i W = this.addSetting(new i("Color", "Lyric tint", -724993));
   private final s N = this.addSetting(new s("Gradient", "Flow the lyric tint into the client accent color", true));
   private final s q = this.addSetting(new s("Glow", "Soft text bloom", true));
   private final s b = this.addSetting(new s("Through Walls", "Keep lyrics visible behind blocks and entities", true));
   private final sp m = new sp();
   private final jo v = new jo();
   private final Quaterniond y = new Quaterniond();
   private cn w;
   private b0 n;
   private Object K;
   private Object e;
   private boolean c;
   private boolean z;
   private long S;
   private double Z;

   public WorldLyricsModule() {
      super(
         "WorldLyrics",
         "Whole Spotify lyric lines spawn near your crosshair, travel with you and animate letter by letter. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.VISUALS,
         -1
      );
      D = this;
   }

   public static WorldLyricsModule e() {
      return D;
   }

   @Override
   public String getDisplayName() {
      return "Spotify World Lyrics";
   }

   @Override
   public String getStatus() {
      return this.w == null ? "Play a song in Spotify desktop" : this.w.g().status();
   }

   @Override
   public void onEnable() {
      this.L();
      this.w = cn.L(this);
      com.larpclient.h.A(
         "WorldLyrics",
         "Spotify World Lyrics enabled; motion="
            + this.k.getValue()
            + ", throughWalls="
            + this.b.getValue()
            + ", lines follow the player with fixed birth orientation"
      );
   }

   @Override
   public void onDisable() {
      this.L();
   }

   public void L() {
      if (this.w != null) {
         cn.D(this);
         this.w = null;
      }

      this.A();
      com.larpclient.h.U("WorldLyrics", "Stopped playback worker and released world text resources");
   }

   public void A() {
      this.m.h();
      this.v.h();
      if (this.n != null) {
         this.n.close();
         this.n = null;
      }

      this.K = this.e = null;
      this.z = this.c = (boolean)0;
      this.S = 0L;
      this.Z = 0.0;
   }

   public void D(CameraRenderState var1) {
      if (this.isEnabled()) {
         this.z = this.c = (boolean)0;
         Minecraft var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame() && this.getPlayer().isAlive()) {
            if (this.K != var2.level || this.e != var2.player) {
               this.A();
               this.K = var2.level;
               this.e = var2.player;
               com.larpclient.h.A(
                  "WorldLyrics",
                  "Lyric planes initialized; player-following placement, per-letter motion, "
                     + (this.b.getValue() ? "drawn through blocks" : "native scene depth with surface-aware birth")
               );
            }

            long var3 = System.nanoTime();
            double var5 = this.S == 0L ? 0.0 : Math.clamp((var3 - this.S) / 1.0E9, 0.0, 0.1);
            this.S = var3;
            if (var2.isPaused()) {
               var5 = 0.0;
            }

            this.Z += var5;
            this.v.Y(this.V.getValue().intValue());
            if (!var2.isPaused() && this.w != null) {
               boolean var7 = this.m.x(this.w.g(), var3);
               if (this.m.o()) {
                  this.v.h();
               }

               if (var7 || this.m.o()) {
                  if (!this.m.Z().isEmpty()) {
                     this.v.r(this.b.getValue() ? 8.0 : this.v(var1));
                  }

                  this.v.X(this.m.Z(), this.m.U());
               }
            }

            if (this.n == null) {
               this.n = new b0();
            }

            double var13 = this.t.O("Floating") ? 1.5 : (this.t.O("Dreamy") ? 2.2 : 0.65);
            List var9 = this.v
               .L(
                  var1.pos.x,
                  var1.pos.y,
                  var1.pos.z,
                  this.y.set(var1.orientation),
                  1.0F / var1.projectionMatrix.m00(),
                  1.0F / var1.projectionMatrix.m11(),
                  this.a.getValue(),
                  var13,
                  var5,
                  this.n::B
               );
            int var10 = this.k.O("Pop") ? 1 : (this.k.O("Wave") ? 2 : 0);
            int var11 = this.N.getValue() ? ho.J | 0xFF000000 : 0;
            List var12 = var9.stream()
               .map(
                  var3x -> new b0.k(
                     var3x.text(),
                     var3x.x(),
                     var3x.y(),
                     var3x.z(),
                     var3x.scale(),
                     uh.F(this.W.getValue(), var10 == 0 ? var3x.opacity() : 1.0),
                     (float)var3x.qx(),
                     (float)var3x.qy(),
                     (float)var3x.qz(),
                     (float)var3x.qw(),
                     true,
                     var10 == 0 && var11 == 0 ? null : new b0.b(var10, Math.min(1.0, var3x.age() / 0.75), var3x.exit(), this.Z, var11)
                  )
               )
               .toList();
            this.n.Q(var12, var1);
            this.z = (boolean)(!var12.isEmpty() ? 1 : 0);
            this.c = (boolean)(this.z && this.q.getValue() ? 1 : 0);
         }
      }
   }

   private double v(CameraRenderState var1) {
      Vector3d var2 = new Vector3d(0.0, 0.0, -1.0).rotate(this.y.set(var1.orientation)).normalize();
      Vec3 var3 = var1.pos.add(var2.x * 7.99, var2.y * 7.99, var2.z * 7.99);
      b5.e var4 = b5.r(getMc().level, var1.pos, var3);
      return var4 == null ? 8.0 : Math.max(0.6, var1.pos.distanceTo(var4.point()) - 0.35);
   }

   public boolean l() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.c && this.n != null && this.n.M();
   }

   public void e(PoseStack var1, CameraRenderState var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.z && this.n != null && this.K == getMc().level) {
            if (!var3 || this.c) {
               this.n.f(var1, var2, this.b.getValue(), !var3, var3 ? 0.3 : 1.0);
            }
         }
      }
   }

   public void n() {
      if (this.n != null) {
         this.n.i();
      }
   }
}
