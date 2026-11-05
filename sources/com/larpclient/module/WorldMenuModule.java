package com.larpclient.module;

import com.larpclient.fr;
import com.larpclient.j1;
import com.larpclient.mj;
import com.larpclient.oh;
import com.larpclient.ou;
import com.larpclient.p0;
import com.larpclient.ps;
import com.larpclient.r;
import com.larpclient.r4;
import com.larpclient.r7;
import com.larpclient.rk;
import com.larpclient.rv;
import com.larpclient.u7;
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
   private static WorldMenuModule Z;
   private final lp M = this.addSetting(
      new lp("Placement", "Pinned stays where you leave it and closes once you walk away; Follow moves along with you", "Pinned", "Pinned", "Follow")
   );
   private final lc n = this.addSetting(new lc("Close Distance", "A pinned menu closes once you are this many blocks from it", 15.0, 4.0, 48.0, 1.0));
   private final lc B = this.addSetting(
      new lc("Menu Height", "Share of your view the menu fills when it opens in front of you, in percent", 50.0, 30.0, 90.0, 1.0)
   );
   private final lc K = this.addSetting(new lc("Panel Size", "Height of the menu panel in blocks", 1.5, 0.5, 4.0, 0.1));
   private static final double t = 0.5;
   private static final double P = 16.0;
   private static final long H = 50000000L;
   private static final long X = 250L;
   private static final int T = 76;
   private static final int h = 6;
   private static final int F = 9;
   private static final int s = 16;
   private static final int O = 9;
   private static final int U = 16;
   private static final int i = 22;
   private j1.y S;
   private Object g;
   private p0 G;
   private p0 m;
   private Vec3 L;
   private boolean x;
   private double q;
   private double u;
   private int c;
   private double a;
   private double f;
   private long A;
   private int N = -1;
   private WorldMenuModule.l w;
   private final u7 R = new u7(0.0);
   private final u7 V = new u7(0.0);
   private double E = 1.0;
   private long y;

   public WorldMenuModule() {
      super("World Menu", "Opens the ClickGUI on a panel in the world while you keep playing", Category.RENDER, -1);
      Z = this;
   }

   public static WorldMenuModule Y() {
      return Z;
   }

   @Override
   public String getDisplayName() {
      return "3D Click UI";
   }

   private boolean u() {
      Minecraft var1 = getMc();
      return this.isRuntimeActive() && var1.player != null && var1.level != null && var1.gameRenderer.mainCamera().isInitialized();
   }

   private boolean C() {
      Minecraft var1 = getMc();
      return var1.gui.screen() == null && var1.gui.overlay() == null && var1.mouseHandler.isMouseGrabbed();
   }

   private double N(p0 var1) {
      double[] var2 = var1.a();
      return this.K.getValue() / Math.max(1.0, var2[3] - var2[1]);
   }

   private rk.n r(p0 var1, j1.y var2) {
      Camera var3 = getMc().gameRenderer.mainCamera();
      Vec3 var4 = var3.position();
      return j1.c(
         var2,
         this.N(var1),
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

   private static double M(p0 var0, rk.n var1) {
      double[] var2 = var0.a();
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

   private double t() {
      return this.M.i("Pinned") ? Math.clamp(this.n.getValue() - 1.0, 0.5, 16.0) : 16.0;
   }

   public static boolean F(p0 var0) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var1 = Z;
         return var1 != null && var1.g(var0);
      } else {
         return false;
      }
   }

   private boolean g(p0 var1) {
      if (!this.u()) {
         return false;
      }

      if (var1 == this.G) {
         return true;
      }

      if (var1.Y() != null) {
         return false;
      }

      if (this.G != null) {
         this.z("a new window replaced it");
      }

      this.m = null;
      this.G = var1;
      this.J();
      Window var2 = getMc().getWindow();
      var1.init(var2.getGuiScaledWidth(), var2.getGuiScaledHeight());
      this.g(var1);
      return true;
   }

   private void g(p0 var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      Vec3 var3 = var2.position();
      j1.y var4 = j1.d(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), 1.0);
      double var5 = M(var1, this.r(var1, var4));
      double var7 = var5 > 0.0 ? Math.clamp(var5 / (this.B.getValue() / 100.0), 0.5, 16.0) : 2.0;
      this.S = j1.d(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), var7);
      this.g = getMc().level;
      this.L = var3;
      this.R.c(1.0);
      this.E = 1.0;
      this.y = 0L;
      r4.g(
         "GUI",
         "World Menu opened in the world, {} blocks ahead, yaw {}, pitch {}, {}",
         u(var7),
         Math.round(this.S.yaw()),
         Math.round(this.S.pitch()),
         this.M.i("Pinned") ? "closes beyond " + this.n.getValue().intValue() + " blocks" : "following"
      );
   }

   private double x() {
      if (this.S == null) {
         return 0.0;
      }

      Vec3 var1 = getMc().gameRenderer.mainCamera().position();
      return this.S.distance(var1.x, var1.y, var1.z);
   }

   private static String u(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static boolean j() {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var0 = Z;
         return var0 != null && var0.G != null && var0.u();
      } else {
         return false;
      }
   }

   public static boolean Q() {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var0 = Z;
         if (var0 != null && var0.G != null && var0.u()) {
            r4.g("GUI", "World Menu closed with the menu key");
            var0.G.onClose();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean O(p0 var0) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var1 = Z;
         return var1 != null && var1.R(var0);
      } else {
         return false;
      }
   }

   private boolean R(p0 var1) {
      if (var1 != null && var1 == this.G) {
         this.G = null;
         this.m = var1;
         this.J();
         var1.removed();
         r4.g("GUI", "World Menu closing {} blocks away", u(this.x()));
         return true;
      } else {
         return false;
      }
   }

   private void z(String var1) {
      p0 var2 = this.G;
      this.G = this.m = null;
      this.J();
      this.S = null;
      this.g = null;
      this.L = null;
      if (var2 != null) {
         r4.g("GUI", "World Menu removed its panel ({})", var1);

         try {
            var2.removed();
         } catch (RuntimeException var4) {
            r4.m("GUI", "World Menu window cleanup failed after {}: {}", var1, var4.toString());
         }
      }
   }

   private void J() {
      this.c = 0;
      this.w = null;
      this.x = (boolean)0;
   }

   @Override
   public void onTick() {
      p0 var1 = this.G;
      if (var1 != null) {
         int var10000 = this.u();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.g == getMc().level) {
            var1.tick();
            if (this.G == var1) {
               int var11 = this.M.i("Pinned");
               boolean var7 = false;
               byte var6 = (byte)var11;
               var11 = Integer.signum(var6);
               if (((var11 | -var11) >>> 31 ^ 1) == 0 && this.w == null) {
                  double var2 = this.x();
                  double var14;
                  var11 = (var14 = var2 - this.n.getValue()) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
                  boolean var5 = false;
                  int var4 = var11;
                  if (-Integer.signum(var4) >>> 31 == 0) {
                     return;
                  }

                  r4.g("GUI", "World Menu closed: you are {} blocks from it, past Close Distance {}", u(var2), this.n.getValue().intValue());
                  var1.onClose();
                  return;
               }
            }
         } else {
            this.z("left the level");
         }
      }
   }

   public static void O(GuiGraphicsExtractor var0, float var1) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var2 = Z;
         if (var2 != null) {
            var2.J(var0, var1);
         }
      }
   }

   private void J(GuiGraphicsExtractor var1, float var2) {
      this.x = (boolean)0;
      p0 var3 = this.G != null ? this.G : this.m;
      if (var3 != null) {
         Minecraft var4 = getMc();
         if (!this.u() || this.S == null || this.g != var4.level) {
            this.z(this.u() ? "left the level" : "unavailable");
         } else if (var3 == this.m && var3.y()) {
            this.m = null;
         } else {
            if (var3.width != var1.guiWidth() || var3.height != var1.guiHeight()) {
               if (var3 != this.G) {
                  this.m = null;
                  return;
               }

               var3.init(var1.guiWidth(), var1.guiHeight());
               r4.g("GUI", "World Menu window laid out again at {}x{}", var3.width, var3.height);
            }

            if (!var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden && !(var4.gui.screen() instanceof ps)) {
               Vec3 var5 = var4.gameRenderer.mainCamera().position();
               if (this.M.i("Follow") && this.L != null && this.w == null) {
                  this.S = new j1.y(
                     this.S.x() + var5.x - this.L.x, this.S.y() + var5.y - this.L.y, this.S.z() + var5.z - this.L.z, this.S.yaw(), this.S.pitch()
                  );
               }

               this.L = var5;
               if (this.w != null) {
                  this.e(var3);
               }

               if (!this.S.faces(var5.x, var5.y, var5.z)) {
                  this.R.c(0.0);
               } else {
                  rk.n var6 = this.r(var3, this.S);
                  double[] var7 = var3.a();
                  double[] var8 = var6.bounds(0.0, 0.0, var3.width, Math.max(var3.height, var7[3] + 24.0));
                  if (!(var8[2] < 0.0) && !(var8[3] < 0.0) && !(var8[0] > var3.width) && !(var8[1] > var3.height)) {
                     long var9 = System.nanoTime();
                     if (var9 - this.y >= 50000000L) {
                        this.y = var9;
                        this.E = this.Q(var3, var5);
                     }

                     double var11 = this.R.y(this.E, 160);
                     if (!(var11 < 0.01)) {
                        double[] var13 = var6.inverse(var3.width / 2.0, var3.height / 2.0);
                        int var14 = var3 == this.G && var13[0] != -10000.0 ? 1 : 0;
                        this.x = (boolean)(var14 != 0 && var11 > 0.5 && this.C() && var3.h(var13[0], var13[1]) && !this.p(var3, var13, var5) ? 1 : 0);
                        if (this.x) {
                           this.q = var13[0];
                           this.u = var13[1];
                        }

                        if (var14 != 0 && this.c != 0 && this.w == null && (var13[0] != this.a || var13[1] != this.f)) {
                           MouseButtonInfo var15 = new MouseButtonInfo(Integer.numberOfTrailingZeros(this.c), 0);
                           var3.mouseDragged(new MouseButtonEvent(var13[0], var13[1], var15), var13[0] - this.a, var13[1] - this.f);
                           this.a = var13[0];
                           this.f = var13[1];
                        }

                        int var18 = this.x || var14 != 0 && this.c != 0 ? 1 : 0;
                        int var16 = var18 != 0 ? (int)Math.floor(var13[0]) : -10000;
                        int var17 = var18 != 0 ? (int)Math.floor(var13[1]) : -10000;
                        var1.nextStratum();
                        rk.H(var1, var6, false, () -> {
                           var1.nextStratum();

                           try (r var7x = r.p(var11)) {
                              var3.extractRenderState(var1, var16, var17, var2);
                           }
                        });
                        var1.nextStratum();
                     }
                  } else {
                     this.R.c(0.0);
                  }
               }
            }
         }
      }
   }

   private boolean p(p0 var1, double[] var2, Vec3 var3) {
      HitResult var4 = getMc().hitResult;
      if (var4 != null && var4.getType() != Type.MISS) {
         double var5 = this.N(var1);
         double[] var7 = this.S.point((var2[0] - var1.width / 2.0) * var5, (var1.height / 2.0 - var2[1]) * var5);
         return var4.getLocation().distanceTo(var3) < new Vec3(var7[0], var7[1], var7[2]).distanceTo(var3);
      } else {
         return false;
      }
   }

   private void e(p0 var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      double[] var3 = j1.h(var2.getViewRotationProjectionMatrix(new Matrix4f()), var1.width / 2.0, var1.height / 2.0, var1.width, var1.height);
      if (var3 != null) {
         Vec3 var4 = var2.position();
         this.S = j1.I(var4.x, var4.y, var4.z, var3, this.w.distance(), this.w.right(), this.w.up());
      }
   }

   private double Q(p0 var1, Vec3 var2) {
      Minecraft var3 = getMc();
      double[] var4 = var1.a();
      double var5 = this.N(var1);
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
         double[] var25 = this.S.point((var24[0] - var7) * var5, (var9 - var24[1]) * var5);
         Vec3 var26 = new Vec3(var25[0], var25[1], var25[2]);
         BlockHitResult var27 = var3.level.clip(new ClipContext(var2, var26, Block.VISUAL, Fluid.NONE, var3.player));
         if (var27.getType() == Type.MISS || var27.getLocation().distanceTo(var2) >= var26.distanceTo(var2) - 0.05) {
            var20++;
         }
      }

      return (double)var20 / var19.length;
   }

   public static boolean u(MouseButtonInfo var0, int var1) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var2 = Z;
         return var2 != null && var2.N(var0, var1);
      } else {
         return false;
      }
   }

   private boolean N(MouseButtonInfo var1, int var2) {
      p0 var3 = this.G;
      int var4 = 1 << Math.clamp(r7.r(var1), 0, 30);
      if (var2 == 0) {
         if ((this.c & var4) == 0) {
            return false;
         }

         this.c &= ~var4;
         if (r7.r(var1) == 0) {
            this.Z();
         }

         if (var3 != null) {
            var3.mouseReleased(new MouseButtonEvent(this.a, this.f, var1));
         }

         return getMc().gui.screen() == null;
      } else {
         if (var2 != 1 || var3 == null || !this.u() || !this.C()) {
            return false;
         }

         if (!this.x) {
            if (var3.M()) {
               var3.E();
               r4.g("GUI", "World Menu typing ended by a click in the world");
            }

            return false;
         } else {
            long var5 = Util.getMillis();
            int var7 = r7.r(var1) == this.N && var5 - this.A < 250L ? 1 : 0;
            this.A = var5;
            this.N = r7.r(var1);
            this.c |= var4;
            this.a = this.q;
            this.f = this.u;
            var3.mouseClicked(new MouseButtonEvent(this.q, this.u, var1), (boolean)var7);
            return true;
         }
      }
   }

   public static boolean Y() {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var0 = Z;
         return var0 != null && var0.G != null && (var0.x || var0.c != 0) && var0.C();
      } else {
         return false;
      }
   }

   public static boolean u(double var0) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var2 = Z;
         return var2 != null && var2.a(var0);
      } else {
         return false;
      }
   }

   private boolean a(double var1) {
      p0 var3 = this.G;
      if (var3 == null || !this.u() || !this.C()) {
         return false;
      }

      if (this.w != null) {
         this.w = new WorldMenuModule.l(Math.clamp(this.w.distance() * Math.pow(1.12, var1), 0.5, this.t()), this.w.right(), this.w.up());
         return true;
      }

      if (!this.x) {
         return false;
      }

      var3.mouseScrolled(this.q, this.u, 0.0, var1);
      return true;
   }

   private boolean x(p0 var1) {
      return var1 != null && var1 == this.G && this.u() && this.C() && var1.M();
   }

   public static boolean w() {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var0 = Z;
         return var0 != null && var0.x(var0.G);
      } else {
         return false;
      }
   }

   public static boolean S(int var0, KeyEvent var1) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var2 = Z;
         if (var2 != null && var0 != 0 && var2.x(var2.G)) {
            if (r7.g(var1) == 256) {
               var2.G.v();
               r4.g("GUI", "World Menu typing ended with Escape");
            } else {
               var2.G.keyPressed(var1);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean q(CharacterEvent var0) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var1 = Z;
         if (var1 != null && var1.x(var1.G)) {
            var1.G.charTyped(var0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean f(p0 var0) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var1 = Z;
         return var1 != null && var0 != null && (var0 == var1.G || var0 == var1.m) && var1.u();
      } else {
         return false;
      }
   }

   private static int[] a(mj var0) {
      return new int[]{var0.left() + (var0.width() - 76) / 2, var0.top() + var0.height() + 9, 76, 6};
   }

   private static boolean z(mj var0, double var1, double var3) {
      int[] var5 = a(var0);
      return var1 >= var5[0] - 16 && var1 < var5[0] + var5[2] + 16 && var3 >= var5[1] - 9 && var3 < var5[1] + var5[3] + 9;
   }

   private static int[] S(mj var0) {
      int[] var1 = a(var0);
      return new int[]{var1[0] + var1[2] + 22, var1[1] + var1[3] / 2 - 8, 16, 16};
   }

   private static boolean g(mj var0, double var1, double var3) {
      int[] var5 = S(var0);
      return var1 >= var5[0] - 4 && var1 < var5[0] + var5[2] + 4 && var3 >= var5[1] - 4 && var3 < var5[1] + var5[3] + 4;
   }

   public static boolean B(mj var0, double var1, double var3) {
      return z(var0, var1, var3) || g(var0, var1, var3);
   }

   public static void A(GuiGraphicsExtractor var0, p0 var1, mj var2, int var3, int var4) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var5 = Z;
         if (var5 != null && f(var1)) {
            var5.x(var0, var1, var2, var3, var4);
         }
      }
   }

   private void x(GuiGraphicsExtractor var1, p0 var2, mj var3, int var4, int var5) {
      int var6 = var2 == this.G ? 1 : 0;
      int var7 = var6 != 0 && this.w != null ? 1 : 0;
      int var8 = var7 == 0 && (var6 == 0 || !z(var3, var4, var5)) ? 0 : 1;
      double var9 = this.V.y(var8 != 0 ? 1.0 : 0.0, 180);
      int[] var11 = a(var3);
      int var12 = var11[2] + (int)Math.round(18.0 * var9);
      int var13 = var11[0] - (var12 - var11[2]) / 2;
      int var14 = var7 != 0 ? rv.a(var13, rv.w) : oh.i(rv.P, 0.34 + 0.42 * var9);
      fr.S(var1, var13, var11[1], var12, var11[3], var11[3] / 2, var14);
      int[] var15 = S(var3);
      int var16 = var6 != 0 && this.w == null && g(var3, var4, var5) ? 1 : 0;
      fr.S(var1, var15[0], var15[1], var15[2], var15[3], var15[2] / 2, var16 != 0 ? -1750963 : oh.i(rv.P, 0.16));
      ou.u(var1, ou.e("x"), var15[0] + 3, var15[1] + 3, var15[2] - 6, var16 != 0 ? -1 : oh.i(rv.P, 0.8));
   }

   public static boolean H(p0 var0, double var1, double var3, int var5, mj var6) {
      if (Z != null && Z.isEnabled()) {
         WorldMenuModule var7 = Z;
         return var7 != null && var7.g(var0, var1, var3, var5, var6);
      } else {
         return false;
      }
   }

   private boolean g(p0 var1, double var2, double var4, int var6, mj var7) {
      if (var6 != 0 || var1 != this.G || !this.u()) {
         return false;
      } else if (g(var7, var2, var4)) {
         r4.g("GUI", "World Menu closed with its X");
         var1.onClose();
         return true;
      } else if (z(var7, var2, var4) && this.x) {
         double var8 = this.N(var1);
         double var10 = (this.q - var1.width / 2.0) * var8;
         double var12 = (var1.height / 2.0 - this.u) * var8;
         double[] var14 = this.S.point(var10, var12);
         Vec3 var15 = getMc().gameRenderer.mainCamera().position();
         double var16 = new Vec3(var14[0], var14[1], var14[2]).distanceTo(var15);
         this.w = new WorldMenuModule.l(Math.clamp(var16, 0.5, this.t()), var10, var12);
         r4.g("GUI", "World Menu grabbed {} blocks away", u(var16));
         return true;
      } else {
         return false;
      }
   }

   private void Z() {
      if (this.w != null) {
         this.w = null;
         r4.g(
            "GUI",
            "World Menu set down {} blocks away, yaw {}, pitch {}",
            u(this.x()),
            this.S == null ? 0L : Math.round(this.S.yaw()),
            this.S == null ? 0L : Math.round(this.S.pitch())
         );
      }
   }

   @Override
   public void onEnable() {
      r4.g(
         "GUI",
         "World Menu active: placement={}, close distance={}, height={}%, size={}",
         this.M.getValue(),
         this.n.getValue().intValue(),
         this.B.getValue().intValue(),
         this.K.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.z("turned off");
      this.R.c(0.0);
   }

   private record l(double distance, double right, double up) {
   }
}
