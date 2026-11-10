package com.larpclient.module;

import com.larpclient.h7;
import com.larpclient.hf;
import com.larpclient.ho;
import com.larpclient.jp;
import com.larpclient.k2;
import com.larpclient.kv;
import com.larpclient.m_;
import com.larpclient.u0;
import com.larpclient.ua;
import com.larpclient.uc;
import com.larpclient.uh;
import com.larpclient.ur;
import com.mojang.blaze3d.platform.Window;
import java.util.Locale;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.util.Util;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;

public final class WorldMenuModule extends Module {
   private static WorldMenuModule R;
   private final com.larpclient.module.l o = this.addSetting(
      new com.larpclient.module.l(
         "Placement", "Pinned stays where you leave it and closes once you walk away; Follow moves along with you", "Pinned", "Pinned", "Follow"
      )
   );
   private final e d = this.addSetting(new e("Close Distance", "A pinned menu closes once you are this many blocks from it", 15.0, 4.0, 48.0, 1.0));
   private final e p = this.addSetting(
      new e("Menu Height", "Share of your view the menu fills when it opens in front of you, in percent", 50.0, 30.0, 90.0, 1.0)
   );
   private final e O = this.addSetting(new e("Panel Size", "Height of the menu panel in blocks", 1.5, 0.5, 4.0, 0.1));
   private static final double F = 0.5;
   private static final double B = 16.0;
   private static final long Z = 50000000L;
   private static final long z = 250L;
   private static final int y = 76;
   private static final int Y = 6;
   private static final int v = 9;
   private static final int K = 16;
   private static final int L = 9;
   private static final int f = 16;
   private static final int w = 22;
   private u0.b j;
   private Object x;
   private hf e;
   private hf k;
   private Vec3 u;
   private boolean N;
   private double c;
   private double t;
   private int P;
   private double T;
   private double n;
   private long b;
   private int g = -1;
   private WorldMenuModule.l G;
   private final jp A = new jp(0.0);
   private final jp i = new jp(0.0);
   private double q = 1.0;
   private long S;

   public WorldMenuModule() {
      super("World Menu", "Opens the ClickGUI on a panel in the world while you keep playing", Category.RENDER, -1);
      R = this;
   }

   public static WorldMenuModule H() {
      return R;
   }

   @Override
   public String getDisplayName() {
      return "3D Click UI";
   }

   private boolean h() {
      Minecraft var1 = getMc();
      return this.isRuntimeActive() && var1.player != null && var1.level != null && var1.gameRenderer.mainCamera().isInitialized();
   }

   private boolean P() {
      Minecraft var1 = getMc();
      return var1.gui.screen() == null && var1.gui.overlay() == null && var1.mouseHandler.isMouseGrabbed();
   }

   private double y(hf var1) {
      double[] var2 = var1.c();
      return this.O.getValue() / Math.max(1.0, var2[3] - var2[1]);
   }

   private m_.r Z(hf var1, u0.b var2) {
      Camera var3 = getMc().gameRenderer.mainCamera();
      Vec3 var4 = var3.position();
      return u0.W(
         var2,
         this.y(var1),
         var1.width / 2.0,
         var1.height / 2.0,
         var4.x,
         var4.y,
         var4.z,
         var3.getViewRotationProjectionMatrix(new Matrix4f()),
         var1.width,
         var1.height
      );
   }

   private static double Q(hf var0, m_.r var1) {
      double[] var2 = var0.c();
      double[] var3 = var1.bounds(var2[0], var2[1], var2[2], var2[3]);
      if (var3[0] == -10000.0) {
         return 0.0;
      }

      double var4 = getMc().gameRenderer.mainCamera().getFov();
      double var6 = ((Integer)getMc().options.fov().get()).intValue();
      double var8 = var4 > 1.0 && var4 < 179.0 && var6 > 1.0 && var6 < 179.0
         ? Math.tan(Math.toRadians(var4) / 2.0) / Math.tan(Math.toRadians(var6) / 2.0)
         : 1.0;
      return (var3[3] - var3[1]) / Math.max(1, var0.height) * var8;
   }

   private double K() {
      return this.o.O("Pinned") ? Math.clamp(this.d.getValue() - 1.0, 0.5, 16.0) : 16.0;
   }

   public static boolean K(hf var0) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var1 = R;
         return var1 != null && var1.t(var0);
      } else {
         return false;
      }
   }

   private boolean t(hf var1) {
      if (!this.h()) {
         return false;
      }

      if (var1 == this.e) {
         return true;
      }

      if (var1.l() != null) {
         return false;
      }

      if (this.e != null) {
         this.H("a new window replaced it");
      }

      this.k = null;
      this.e = var1;
      this.n();
      Window var2 = getMc().getWindow();
      var1.init(var2.getGuiScaledWidth(), var2.getGuiScaledHeight());
      this.z(var1);
      return true;
   }

   private void z(hf var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      Vec3 var3 = var2.position();
      u0.b var4 = u0.d(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), 1.0);
      double var5 = Q(var1, this.Z(var1, var4));
      double var7 = var5 > 0.0 ? Math.clamp(var5 / (this.p.getValue() / 100.0), 0.5, 16.0) : 2.0;
      this.j = u0.d(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), var7);
      this.x = getMc().level;
      this.u = var3;
      this.A.F(1.0);
      this.q = 1.0;
      this.S = 0L;
      com.larpclient.h.U(
         "GUI",
         "World Menu opened in the world, {} blocks ahead, yaw {}, pitch {}, {}",
         u(var7),
         Math.round(this.j.yaw()),
         Math.round(this.j.pitch()),
         this.o.O("Pinned") ? "closes beyond " + this.d.getValue().intValue() + " blocks" : "following"
      );
   }

   private double e() {
      if (this.j == null) {
         return 0.0;
      }

      Vec3 var1 = getMc().gameRenderer.mainCamera().position();
      return this.j.distance(var1.x, var1.y, var1.z);
   }

   private static String u(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static boolean e() {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var0 = R;
         return var0 != null && var0.e != null && var0.h();
      } else {
         return false;
      }
   }

   public static boolean a() {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var0 = R;
         if (var0 != null && var0.e != null && var0.h()) {
            com.larpclient.h.U("GUI", "World Menu closed with the menu key");
            var0.e.onClose();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean F(hf var0) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var1 = R;
         return var1 != null && var1.Q(var0);
      } else {
         return false;
      }
   }

   private boolean Q(hf var1) {
      if (var1 != null && var1 == this.e) {
         this.e = null;
         this.k = var1;
         this.n();
         var1.removed();
         com.larpclient.h.U("GUI", "World Menu closing {} blocks away", u(this.e()));
         return true;
      } else {
         return false;
      }
   }

   private void H(String var1) {
      hf var2 = this.e;
      this.e = this.k = null;
      this.n();
      this.j = null;
      this.x = null;
      this.u = null;
      if (var2 != null) {
         com.larpclient.h.U("GUI", "World Menu removed its panel ({})", var1);

         try {
            var2.removed();
         } catch (RuntimeException var4) {
            com.larpclient.h.g("GUI", "World Menu window cleanup failed after {}: {}", var1, var4.toString());
         }
      }
   }

   private void n() {
      this.P = 0;
      this.G = null;
      this.N = (boolean)0;
   }

   @Override
   public void onTick() {
      hf var1 = this.e;
      if (var1 != null) {
         int var10000 = this.h();
         boolean var7 = false;
         byte var6 = (byte)var10000;
         var10000 = Integer.signum(var6);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.x == getMc().level) {
            var1.tick();
            if (this.e == var1) {
               int var11 = this.o.O("Pinned");
               boolean var9 = false;
               byte var8 = (byte)var11;
               var11 = Integer.signum(var8);
               if (((var11 | -var11) >>> 31 ^ 1) == 0 && this.G == null) {
                  double var2 = this.e();
                  double var14;
                  var11 = (var14 = var2 - this.d.getValue()) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
                  boolean var5 = false;
                  int var4 = var11;
                  if (-Integer.signum(var4) >>> 31 == 0) {
                     return;
                  }

                  com.larpclient.h.U("GUI", "World Menu closed: you are {} blocks from it, past Close Distance {}", u(var2), this.d.getValue().intValue());
                  var1.onClose();
                  return;
               }
            }
         } else {
            this.H("left the level");
         }
      }
   }

   public static void d(GuiGraphicsExtractor var0, float var1) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var2 = R;
         if (var2 != null) {
            var2.x(var0, var1);
         }
      }
   }

   private void x(GuiGraphicsExtractor var1, float var2) {
      this.N = (boolean)0;
      hf var3 = this.e != null ? this.e : this.k;
      if (var3 != null) {
         Minecraft var4 = getMc();
         if (!this.h() || this.j == null || this.x != var4.level) {
            this.H(this.h() ? "left the level" : "unavailable");
         } else if (var3 == this.k && var3.D()) {
            this.k = null;
         } else {
            if (var3.width != var1.guiWidth() || var3.height != var1.guiHeight()) {
               if (var3 != this.e) {
                  this.k = null;
                  return;
               }

               var3.init(var1.guiWidth(), var1.guiHeight());
               com.larpclient.h.U("GUI", "World Menu window laid out again at {}x{}", var3.width, var3.height);
            }

            if (!var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden && !(var4.gui.screen() instanceof h7)) {
               Vec3 var5 = var4.gameRenderer.mainCamera().position();
               if (this.o.O("Follow") && this.u != null && this.G == null) {
                  this.j = new u0.b(
                     this.j.x() + var5.x - this.u.x, this.j.y() + var5.y - this.u.y, this.j.z() + var5.z - this.u.z, this.j.yaw(), this.j.pitch()
                  );
               }

               this.u = var5;
               if (this.G != null) {
                  this.B(var3);
               }

               if (!this.j.faces(var5.x, var5.y, var5.z)) {
                  this.A.F(0.0);
               } else {
                  m_.r var6 = this.Z(var3, this.j);
                  double[] var7 = var3.c();
                  double[] var8 = var6.bounds(0.0, 0.0, var3.width, Math.max(var3.height, var7[3] + 24.0));
                  if (!(var8[2] < 0.0) && !(var8[3] < 0.0) && !(var8[0] > var3.width) && !(var8[1] > var3.height)) {
                     long var9 = System.nanoTime();
                     if (var9 - this.S >= 50000000L) {
                        this.S = var9;
                        this.q = this.c(var3, var5);
                     }

                     double var11 = this.A.k(this.q, 160);
                     if (!(var11 < 0.01)) {
                        double[] var13 = var6.inverse(var3.width / 2.0, var3.height / 2.0);
                        int var14 = var3 == this.e && var13[0] != -10000.0 ? 1 : 0;
                        this.N = (boolean)(var14 != 0 && var11 > 0.5 && this.P() && var3.Y(var13[0], var13[1]) && !this.K(var3, var13, var5) ? 1 : 0);
                        if (this.N) {
                           this.c = var13[0];
                           this.t = var13[1];
                        }

                        if (var14 != 0 && this.P != 0 && this.G == null && (var13[0] != this.T || var13[1] != this.n)) {
                           MouseButtonInfo var15 = new MouseButtonInfo(Integer.numberOfTrailingZeros(this.P), 0);
                           var3.mouseDragged(new MouseButtonEvent(var13[0], var13[1], var15), var13[0] - this.T, var13[1] - this.n);
                           this.T = var13[0];
                           this.n = var13[1];
                        }

                        int var18 = this.N || var14 != 0 && this.P != 0 ? 1 : 0;
                        int var16 = var18 != 0 ? (int)Math.floor(var13[0]) : -10000;
                        int var17 = var18 != 0 ? (int)Math.floor(var13[1]) : -10000;
                        var1.nextStratum();
                        m_.T(var1, var6, false, () -> {
                           var1.nextStratum();

                           try (ua var7x = ua.K(var11)) {
                              var3.extractRenderState(var1, var16, var17, var2);
                           }
                        });
                        var1.nextStratum();
                     }
                  } else {
                     this.A.F(0.0);
                  }
               }
            }
         }
      }
   }

   private boolean K(hf var1, double[] var2, Vec3 var3) {
      HitResult var4 = getMc().hitResult;
      if (var4 != null && var4.getType() != Type.MISS) {
         double var5 = this.y(var1);
         double[] var7 = this.j.point((var2[0] - var1.width / 2.0) * var5, (var1.height / 2.0 - var2[1]) * var5);
         return var4.getLocation().distanceTo(var3) < new Vec3(var7[0], var7[1], var7[2]).distanceTo(var3);
      } else {
         return false;
      }
   }

   private void B(hf var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      double[] var3 = u0.s(var2.getViewRotationProjectionMatrix(new Matrix4f()), var1.width / 2.0, var1.height / 2.0, var1.width, var1.height);
      if (var3 != null) {
         Vec3 var4 = var2.position();
         this.j = u0.C(var4.x, var4.y, var4.z, var3, this.G.distance(), this.G.right(), this.G.up());
      }
   }

   private double c(hf var1, Vec3 var2) {
      Minecraft var3 = getMc();
      double[] var4 = var1.c();
      double var5 = this.y(var1);
      double var7 = var1.width / 2.0;
      double var9 = var1.height / 2.0;
      double var11 = (var4[0] + var4[2]) / 2.0;
      double var13 = (var4[1] + var4[3]) / 2.0;
      double var15 = (var4[2] - var4[0]) * 0.4;
      double var17 = (var4[3] - var4[1]) * 0.4;
      double[][] var19 = new double[][]{
         {var11, var13}, {var11 - var15, var13 - var17}, {var11 + var15, var13 - var17}, {var11 - var15, var13 + var17}, {var11 + var15, var13 + var17}
      };
      int var20 = 0;

      for (double[] var24 : var19) {
         double[] var25 = this.j.point((var24[0] - var7) * var5, (var9 - var24[1]) * var5);
         Vec3 var26 = new Vec3(var25[0], var25[1], var25[2]);
         BlockHitResult var27 = var3.level.clip(new ClipContext(var2, var26, Block.VISUAL, Fluid.NONE, var3.player));
         if (var27.getType() == Type.MISS || var27.getLocation().distanceTo(var2) >= var26.distanceTo(var2) - 0.05) {
            var20++;
         }
      }

      return (double)var20 / var19.length;
   }

   public static boolean J(MouseButtonInfo var0, int var1) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var2 = R;
         return var2 != null && var2.d(var0, var1);
      } else {
         return false;
      }
   }

   private boolean d(MouseButtonInfo var1, int var2) {
      hf var3 = this.e;
      int var4 = 1 << Math.clamp(k2.F(var1), 0, 30);
      if (var2 == 0) {
         if ((this.P & var4) == 0) {
            return false;
         }

         this.P &= ~var4;
         if (k2.F(var1) == 0) {
            this.F();
         }

         if (var3 != null) {
            var3.mouseReleased(new MouseButtonEvent(this.T, this.n, var1));
         }

         return getMc().gui.screen() == null;
      } else {
         if (var2 != 1 || var3 == null || !this.h() || !this.P()) {
            return false;
         }

         if (!this.N) {
            if (var3.P()) {
               var3.V();
               com.larpclient.h.U("GUI", "World Menu typing ended by a click in the world");
            }

            return false;
         } else {
            long var5 = Util.getMillis();
            int var7 = k2.F(var1) == this.g && var5 - this.b < 250L ? 1 : 0;
            this.b = var5;
            this.g = k2.F(var1);
            this.P |= var4;
            this.T = this.c;
            this.n = this.t;
            var3.mouseClicked(new MouseButtonEvent(this.c, this.t, var1), (boolean)var7);
            return true;
         }
      }
   }

   public static boolean X() {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var0 = R;
         return var0 != null && var0.e != null && (var0.N || var0.P != 0) && var0.P();
      } else {
         return false;
      }
   }

   public static boolean s(double var0) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var2 = R;
         return var2 != null && var2.r(var0);
      } else {
         return false;
      }
   }

   private boolean r(double var1) {
      hf var3 = this.e;
      if (var3 == null || !this.h() || !this.P()) {
         return false;
      }

      if (this.G != null) {
         this.G = new WorldMenuModule.l(Math.clamp(this.G.distance() * Math.pow(1.12, var1), 0.5, this.K()), this.G.right(), this.G.up());
         return true;
      }

      if (!this.N) {
         return false;
      }

      var3.mouseScrolled(this.c, this.t, 0.0, var1);
      return true;
   }

   private boolean n(hf var1) {
      return var1 != null && var1 == this.e && this.h() && this.P() && var1.P();
   }

   public static boolean K() {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var0 = R;
         return var0 != null && var0.n(var0.e);
      } else {
         return false;
      }
   }

   public static boolean s(int var0, KeyEvent var1) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var2 = R;
         if (var2 != null && var0 != 0 && var2.n(var2.e)) {
            if (k2.a(var1) == 256) {
               var2.e.Z();
               com.larpclient.h.U("GUI", "World Menu typing ended with Escape");
            } else {
               var2.e.keyPressed(var1);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean o(CharacterEvent var0) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var1 = R;
         if (var1 != null && var1.n(var1.e)) {
            var1.e.charTyped(var0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean h(hf var0) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var1 = R;
         return var1 != null && var0 != null && (var0 == var1.e || var0 == var1.k) && var1.h();
      } else {
         return false;
      }
   }

   private static int[] y(uc var0) {
      return new int[]{var0.left() + (var0.width() - 76) / 2, var0.top() + var0.height() + 9, 76, 6};
   }

   private static boolean Q(uc var0, double var1, double var3) {
      int[] var5 = y(var0);
      return var1 >= var5[0] - 16 && var1 < var5[0] + var5[2] + 16 && var3 >= var5[1] - 9 && var3 < var5[1] + var5[3] + 9;
   }

   private static int[] B(uc var0) {
      int[] var1 = y(var0);
      return new int[]{var1[0] + var1[2] + 22, var1[1] + var1[3] / 2 - 8, 16, 16};
   }

   private static boolean a(uc var0, double var1, double var3) {
      int[] var5 = B(var0);
      return var1 >= var5[0] - 4 && var1 < var5[0] + var5[2] + 4 && var3 >= var5[1] - 4 && var3 < var5[1] + var5[3] + 4;
   }

   public static boolean V(uc var0, double var1, double var3) {
      return Q(var0, var1, var3) || a(var0, var1, var3);
   }

   public static void O(GuiGraphicsExtractor var0, hf var1, uc var2, int var3, int var4) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var5 = R;
         if (var5 != null && h(var1)) {
            var5.h(var0, var1, var2, var3, var4);
         }
      }
   }

   private void h(GuiGraphicsExtractor var1, hf var2, uc var3, int var4, int var5) {
      int var6 = var2 == this.e ? 1 : 0;
      int var7 = var6 != 0 && this.G != null ? 1 : 0;
      int var8 = var7 == 0 && (var6 == 0 || !Q(var3, var4, var5)) ? 0 : 1;
      double var9 = this.i.k(var8 != 0 ? 1.0 : 0.0, 180);
      int[] var11 = y(var3);
      int var12 = var11[2] + (int)Math.round(18.0 * var9);
      int var13 = var11[0] - (var12 - var11[2]) / 2;
      int var14 = var7 != 0 ? ho.S(var13, ho.A) : uh.H(ho.u, 0.34 + 0.42 * var9);
      kv.f(var1, var13, var11[1], var12, var11[3], var11[3] / 2, var14);
      int[] var15 = B(var3);
      int var16 = var6 != 0 && this.G == null && a(var3, var4, var5) ? 1 : 0;
      kv.f(var1, var15[0], var15[1], var15[2], var15[3], var15[2] / 2, var16 != 0 ? -1750963 : uh.H(ho.u, 0.16));
      ur.O(var1, ur.V("x"), var15[0] + 3, var15[1] + 3, var15[2] - 6, var16 != 0 ? -1 : uh.H(ho.u, 0.8));
   }

   public static boolean Z(hf var0, double var1, double var3, int var5, uc var6) {
      if (R != null && R.isEnabled()) {
         WorldMenuModule var7 = R;
         return var7 != null && var7.l(var0, var1, var3, var5, var6);
      } else {
         return false;
      }
   }

   private boolean l(hf var1, double var2, double var4, int var6, uc var7) {
      if (var6 != 0 || var1 != this.e || !this.h()) {
         return false;
      } else if (a(var7, var2, var4)) {
         com.larpclient.h.U("GUI", "World Menu closed with its X");
         var1.onClose();
         return true;
      } else if (Q(var7, var2, var4) && this.N) {
         double var8 = this.y(var1);
         double var10 = (this.c - var1.width / 2.0) * var8;
         double var12 = (var1.height / 2.0 - this.t) * var8;
         double[] var14 = this.j.point(var10, var12);
         Vec3 var15 = getMc().gameRenderer.mainCamera().position();
         double var16 = new Vec3(var14[0], var14[1], var14[2]).distanceTo(var15);
         this.G = new WorldMenuModule.l(Math.clamp(var16, 0.5, this.K()), var10, var12);
         com.larpclient.h.U("GUI", "World Menu grabbed {} blocks away", u(var16));
         return true;
      } else {
         return false;
      }
   }

   private void F() {
      if (this.G != null) {
         this.G = null;
         com.larpclient.h.U(
            "GUI",
            "World Menu set down {} blocks away, yaw {}, pitch {}",
            u(this.e()),
            this.j == null ? 0L : Math.round(this.j.yaw()),
            this.j == null ? 0L : Math.round(this.j.pitch())
         );
      }
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U(
         "GUI",
         "World Menu active: placement={}, close distance={}, height={}%, size={}",
         this.o.getValue(),
         this.d.getValue().intValue(),
         this.p.getValue().intValue(),
         this.O.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.H("turned off");
      this.A.F(0.0);
   }

   private record l(double distance, double right, double up) {
   }
}
