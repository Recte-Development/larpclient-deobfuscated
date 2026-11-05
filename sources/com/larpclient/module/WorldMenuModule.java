package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.c7;
import com.larpclient.cp;
import com.larpclient.de;
import com.larpclient.ef;
import com.larpclient.hu;
import com.larpclient.hw;
import com.larpclient.mo;
import com.larpclient.mp;
import com.larpclient.mt;
import com.larpclient.tp;
import com.larpclient.z7;
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
   private static WorldMenuModule G;
   private final d6 z = this.addSetting(
      new d6("Placement", "Pinned stays where you leave it and closes once you walk away; Follow moves along with you", "Pinned", "Pinned", "Follow")
   );
   private final d9 m = this.addSetting(new d9("Close Distance", "A pinned menu closes once you are this many blocks from it", 15.0, 4.0, 48.0, 1.0));
   private final d9 E = this.addSetting(
      new d9("Menu Height", "Share of your view the menu fills when it opens in front of you, in percent", 50.0, 30.0, 90.0, 1.0)
   );
   private final d9 a = this.addSetting(new d9("Panel Size", "Height of the menu panel in blocks", 1.5, 0.5, 4.0, 0.1));
   private static final double d = 0.5;
   private static final double C = 16.0;
   private static final long Z = 50000000L;
   private static final long c = 250L;
   private static final int H = 76;
   private static final int N = 6;
   private static final int A = 9;
   private static final int V = 16;
   private static final int l = 9;
   private static final int b = 16;
   private static final int T = 22;
   private z7.g Y;
   private Object y;
   private hw J;
   private hw L;
   private Vec3 s;
   private boolean p;
   private double x;
   private double k;
   private int f;
   private double P;
   private double B;
   private long q;
   private int D = -1;
   private WorldMenuModule.r U;
   private final ef F = new ef(0.0);
   private final ef t = new ef(0.0);
   private double v = 1.0;
   private long h;

   public WorldMenuModule() {
      super("World Menu", "Opens the ClickGUI on a panel in the world while you keep playing", Category.RENDER, -1);
      G = this;
   }

   public static WorldMenuModule S() {
      return G;
   }

   @Override
   public String getDisplayName() {
      return "3D Click UI";
   }

   private boolean L() {
      Minecraft var1 = getMc();
      return this.isRuntimeActive() && var1.player != null && var1.level != null && var1.gameRenderer.mainCamera().isInitialized();
   }

   private boolean j() {
      Minecraft var1 = getMc();
      return var1.gui.screen() == null && var1.gui.overlay() == null && var1.mouseHandler.isMouseGrabbed();
   }

   private double E(hw var1) {
      double[] var2 = var1.C();
      return this.a.P() / Math.max(1.0, var2[3] - var2[1]);
   }

   private cp.p T(hw var1, z7.g var2) {
      Camera var3 = getMc().gameRenderer.mainCamera();
      Vec3 var4 = var3.position();
      return z7.f(
         var2,
         this.E(var1),
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

   private static double L(hw var0, cp.p var1) {
      double[] var2 = var0.C();
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

   private double j() {
      return this.z.S("Pinned") ? Math.clamp(this.m.P() - 1.0, 0.5, 16.0) : 16.0;
   }

   public static boolean o(hw var0) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var1 = G;
         return var1 != null && var1.Y(var0);
      } else {
         return false;
      }
   }

   private boolean Y(hw var1) {
      if (!this.L()) {
         return false;
      }

      if (var1 == this.J) {
         return true;
      }

      if (var1.S() != null) {
         return false;
      }

      if (this.J != null) {
         this.R("a new window replaced it");
      }

      this.L = null;
      this.J = var1;
      this.l();
      Window var2 = getMc().getWindow();
      var1.init(var2.getGuiScaledWidth(), var2.getGuiScaledHeight());
      this.g(var1);
      return true;
   }

   private void g(hw var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      Vec3 var3 = var2.position();
      z7.g var4 = z7.A(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), 1.0);
      double var5 = L(var1, this.T(var1, var4));
      double var7 = var5 > 0.0 ? Math.clamp(var5 / (this.E.P() / 100.0), 0.5, 16.0) : 2.0;
      this.Y = z7.A(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), var7);
      this.y = getMc().level;
      this.s = var3;
      this.F.Q(1.0);
      this.v = 1.0;
      this.h = 0L;
      mt.U(
         "GUI",
         "World Menu opened in the world, {} blocks ahead, yaw {}, pitch {}, {}",
         y(var7),
         Math.round(this.Y.yaw()),
         Math.round(this.Y.pitch()),
         this.z.S("Pinned") ? "closes beyond " + this.m.P().intValue() + " blocks" : "following"
      );
   }

   private double O() {
      if (this.Y == null) {
         return 0.0;
      }

      Vec3 var1 = getMc().gameRenderer.mainCamera().position();
      return this.Y.distance(var1.x, var1.y, var1.z);
   }

   private static String y(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static boolean n() {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var0 = G;
         return var0 != null && var0.J != null && var0.L();
      } else {
         return false;
      }
   }

   public static boolean W() {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var0 = G;
         if (var0 != null && var0.J != null && var0.L()) {
            mt.U("GUI", "World Menu closed with the menu key");
            var0.J.onClose();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean s(hw var0) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var1 = G;
         return var1 != null && var1.L(var0);
      } else {
         return false;
      }
   }

   private boolean L(hw var1) {
      if (var1 != null && var1 == this.J) {
         this.J = null;
         this.L = var1;
         this.l();
         var1.removed();
         mt.U("GUI", "World Menu closing {} blocks away", y(this.O()));
         return true;
      } else {
         return false;
      }
   }

   private void R(String var1) {
      hw var2 = this.J;
      this.J = this.L = null;
      this.l();
      this.Y = null;
      this.y = null;
      this.s = null;
      if (var2 != null) {
         mt.U("GUI", "World Menu removed its panel ({})", var1);

         try {
            var2.removed();
         } catch (RuntimeException var4) {
            mt.T("GUI", "World Menu window cleanup failed after {}: {}", var1, var4.toString());
         }
      }
   }

   private void l() {
      this.f = 0;
      this.U = null;
      this.p = (boolean)0;
   }

   @Override
   public void onTick() {
      hw var1 = this.J;
      if (var1 != null) {
         int var10000 = this.L();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.y == getMc().level) {
            var1.tick();
            if (this.J == var1) {
               int var11 = this.z.S("Pinned");
               boolean var5 = false;
               byte var4 = (byte)var11;
               var11 = Integer.signum(var4);
               if (((var11 | -var11) >>> 31 ^ 1) == 0 && this.U == null) {
                  double var2 = this.O();
                  double var14;
                  var11 = (var14 = var2 - this.m.P()) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
                  boolean var7 = false;
                  int var6 = var11;
                  if (-Integer.signum(var6) >>> 31 == 0) {
                     return;
                  }

                  mt.U("GUI", "World Menu closed: you are {} blocks from it, past Close Distance {}", y(var2), this.m.P().intValue());
                  var1.onClose();
                  return;
               }
            }
         } else {
            this.R("left the level");
         }
      }
   }

   public static void m(GuiGraphicsExtractor var0, float var1) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var2 = G;
         if (var2 != null) {
            var2.p(var0, var1);
         }
      }
   }

   private void p(GuiGraphicsExtractor var1, float var2) {
      this.p = (boolean)0;
      hw var3 = this.J != null ? this.J : this.L;
      if (var3 != null) {
         Minecraft var4 = getMc();
         if (!this.L() || this.Y == null || this.y != var4.level) {
            this.R(this.L() ? "left the level" : "unavailable");
         } else if (var3 == this.L && var3.G()) {
            this.L = null;
         } else {
            if (var3.width != var1.guiWidth() || var3.height != var1.guiHeight()) {
               if (var3 != this.J) {
                  this.L = null;
                  return;
               }

               var3.init(var1.guiWidth(), var1.guiHeight());
               mt.U("GUI", "World Menu window laid out again at {}x{}", var3.width, var3.height);
            }

            if (!var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden && !(var4.gui.screen() instanceof hu)) {
               Vec3 var5 = var4.gameRenderer.mainCamera().position();
               if (this.z.S("Follow") && this.s != null && this.U == null) {
                  this.Y = new z7.g(
                     this.Y.x() + var5.x - this.s.x, this.Y.y() + var5.y - this.s.y, this.Y.z() + var5.z - this.s.z, this.Y.yaw(), this.Y.pitch()
                  );
               }

               this.s = var5;
               if (this.U != null) {
                  this.j(var3);
               }

               if (!this.Y.faces(var5.x, var5.y, var5.z)) {
                  this.F.Q(0.0);
               } else {
                  cp.p var6 = this.T(var3, this.Y);
                  double[] var7 = var3.C();
                  double[] var8 = var6.bounds(0.0, 0.0, var3.width, Math.max(var3.height, var7[3] + 24.0));
                  if (!(var8[2] < 0.0) && !(var8[3] < 0.0) && !(var8[0] > var3.width) && !(var8[1] > var3.height)) {
                     long var9 = System.nanoTime();
                     if (var9 - this.h >= 50000000L) {
                        this.h = var9;
                        this.v = this.M(var3, var5);
                     }

                     double var11 = this.F.m(this.v, 160);
                     if (!(var11 < 0.01)) {
                        double[] var13 = var6.inverse(var3.width / 2.0, var3.height / 2.0);
                        int var14 = var3 == this.J && var13[0] != -10000.0 ? 1 : 0;
                        this.p = (boolean)(var14 != 0 && var11 > 0.5 && this.j() && var3.J(var13[0], var13[1]) && !this.f(var3, var13, var5) ? 1 : 0);
                        if (this.p) {
                           this.x = var13[0];
                           this.k = var13[1];
                        }

                        if (var14 != 0 && this.f != 0 && this.U == null && (var13[0] != this.P || var13[1] != this.B)) {
                           MouseButtonInfo var15 = new MouseButtonInfo(Integer.numberOfTrailingZeros(this.f), 0);
                           var3.mouseDragged(new MouseButtonEvent(var13[0], var13[1], var15), var13[0] - this.P, var13[1] - this.B);
                           this.P = var13[0];
                           this.B = var13[1];
                        }

                        int var18 = !this.p && (var14 == 0 || this.f == 0) ? 0 : 1;
                        int var16 = var18 != 0 ? (int)Math.floor(var13[0]) : -10000;
                        int var17 = var18 != 0 ? (int)Math.floor(var13[1]) : -10000;
                        var1.nextStratum();
                        cp.H(var1, var6, false, () -> {
                           var1.nextStratum();

                           try (de var7x = de.l(var11)) {
                              var3.extractRenderState(var1, var16, var17, var2);
                           }
                        });
                        var1.nextStratum();
                     }
                  } else {
                     this.F.Q(0.0);
                  }
               }
            }
         }
      }
   }

   private boolean f(hw var1, double[] var2, Vec3 var3) {
      HitResult var4 = getMc().hitResult;
      if (var4 != null && var4.getType() != Type.MISS) {
         double var5 = this.E(var1);
         double[] var7 = this.Y.point((var2[0] - var1.width / 2.0) * var5, (var1.height / 2.0 - var2[1]) * var5);
         return var4.getLocation().distanceTo(var3) < new Vec3(var7[0], var7[1], var7[2]).distanceTo(var3);
      } else {
         return false;
      }
   }

   private void j(hw var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      double[] var3 = z7.P(var2.getViewRotationProjectionMatrix(new Matrix4f()), var1.width / 2.0, var1.height / 2.0, var1.width, var1.height);
      if (var3 != null) {
         Vec3 var4 = var2.position();
         this.Y = z7.u(var4.x, var4.y, var4.z, var3, this.U.distance(), this.U.right(), this.U.up());
      }
   }

   private double M(hw var1, Vec3 var2) {
      Minecraft var3 = getMc();
      double[] var4 = var1.C();
      double var5 = this.E(var1);
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
         double[] var25 = this.Y.point((var24[0] - var7) * var5, (var9 - var24[1]) * var5);
         Vec3 var26 = new Vec3(var25[0], var25[1], var25[2]);
         BlockHitResult var27 = var3.level.clip(new ClipContext(var2, var26, Block.VISUAL, Fluid.NONE, var3.player));
         if (var27.getType() == Type.MISS || var27.getLocation().distanceTo(var2) >= var26.distanceTo(var2) - 0.05) {
            var20++;
         }
      }

      return (double)var20 / var19.length;
   }

   public static boolean N(MouseButtonInfo var0, int var1) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var2 = G;
         return var2 != null && var2.i(var0, var1);
      } else {
         return false;
      }
   }

   private boolean i(MouseButtonInfo var1, int var2) {
      hw var3 = this.J;
      int var4 = 1 << Math.clamp(mo.i(var1), 0, 30);
      if (var2 == 0) {
         if ((this.f & var4) == 0) {
            return false;
         }

         this.f &= ~var4;
         if (mo.i(var1) == 0) {
            this.o();
         }

         if (var3 != null) {
            var3.mouseReleased(new MouseButtonEvent(this.P, this.B, var1));
         }

         return getMc().gui.screen() == null;
      } else {
         if (var2 != 1 || var3 == null || !this.L() || !this.j()) {
            return false;
         }

         if (!this.p) {
            if (var3.m()) {
               var3.F();
               mt.U("GUI", "World Menu typing ended by a click in the world");
            }

            return false;
         } else {
            long var5 = Util.getMillis();
            int var7 = mo.i(var1) == this.D && var5 - this.q < 250L ? 1 : 0;
            this.q = var5;
            this.D = mo.i(var1);
            this.f |= var4;
            this.P = this.x;
            this.B = this.k;
            var3.mouseClicked(new MouseButtonEvent(this.x, this.k, var1), (boolean)var7);
            return true;
         }
      }
   }

   public static boolean d() {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var0 = G;
         return var0 != null && var0.J != null && (var0.p || var0.f != 0) && var0.j();
      } else {
         return false;
      }
   }

   public static boolean N(double var0) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var2 = G;
         return var2 != null && var2.y(var0);
      } else {
         return false;
      }
   }

   private boolean y(double var1) {
      hw var3 = this.J;
      if (var3 == null || !this.L() || !this.j()) {
         return false;
      }

      if (this.U != null) {
         this.U = new WorldMenuModule.r(Math.clamp(this.U.distance() * Math.pow(1.12, var1), 0.5, this.j()), this.U.right(), this.U.up());
         return true;
      }

      if (!this.p) {
         return false;
      }

      var3.mouseScrolled(this.x, this.k, 0.0, var1);
      return true;
   }

   private boolean H(hw var1) {
      return var1 != null && var1 == this.J && this.L() && this.j() && var1.m();
   }

   public static boolean K() {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var0 = G;
         return var0 != null && var0.H(var0.J);
      } else {
         return false;
      }
   }

   public static boolean O(int var0, KeyEvent var1) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var2 = G;
         if (var2 != null && var0 != 0 && var2.H(var2.J)) {
            if (mo.O(var1) == 256) {
               var2.J.H();
               mt.U("GUI", "World Menu typing ended with Escape");
            } else {
               var2.J.keyPressed(var1);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean V(CharacterEvent var0) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var1 = G;
         if (var1 != null && var1.H(var1.J)) {
            var1.J.charTyped(var0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean c(hw var0) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var1 = G;
         return var1 != null && var0 != null && (var0 == var1.J || var0 == var1.L) && var1.L();
      } else {
         return false;
      }
   }

   private static int[] z(mp var0) {
      return new int[]{var0.left() + (var0.width() - 76) / 2, var0.top() + var0.height() + 9, 76, 6};
   }

   private static boolean w(mp var0, double var1, double var3) {
      int[] var5 = z(var0);
      return var1 >= var5[0] - 16 && var1 < var5[0] + var5[2] + 16 && var3 >= var5[1] - 9 && var3 < var5[1] + var5[3] + 9;
   }

   private static int[] w(mp var0) {
      int[] var1 = z(var0);
      return new int[]{var1[0] + var1[2] + 22, var1[1] + var1[3] / 2 - 8, 16, 16};
   }

   private static boolean L(mp var0, double var1, double var3) {
      int[] var5 = w(var0);
      return var1 >= var5[0] - 4 && var1 < var5[0] + var5[2] + 4 && var3 >= var5[1] - 4 && var3 < var5[1] + var5[3] + 4;
   }

   public static boolean c(mp var0, double var1, double var3) {
      return w(var0, var1, var3) || L(var0, var1, var3);
   }

   public static void h(GuiGraphicsExtractor var0, hw var1, mp var2, int var3, int var4) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var5 = G;
         if (var5 != null && c(var1)) {
            var5.G(var0, var1, var2, var3, var4);
         }
      }
   }

   private void G(GuiGraphicsExtractor var1, hw var2, mp var3, int var4, int var5) {
      int var6 = var2 == this.J ? 1 : 0;
      int var7 = var6 != 0 && this.U != null ? 1 : 0;
      int var8 = var7 != 0 || var6 != 0 && w(var3, var4, var5) ? 1 : 0;
      double var9 = this.t.m(var8 != 0 ? 1.0 : 0.0, 180);
      int[] var11 = z(var3);
      int var12 = var11[2] + (int)Math.round(18.0 * var9);
      int var13 = var11[0] - (var12 - var11[2]) / 2;
      int var14 = var7 != 0 ? com.larpclient.f.Q(var13, com.larpclient.f.u) : _3.A(com.larpclient.f.J, 0.34 + 0.42 * var9);
      c7.n(var1, var13, var11[1], var12, var11[3], var11[3] / 2, var14);
      int[] var15 = w(var3);
      int var16 = var6 != 0 && this.U == null && L(var3, var4, var5) ? 1 : 0;
      c7.n(var1, var15[0], var15[1], var15[2], var15[3], var15[2] / 2, var16 != 0 ? -1750963 : _3.A(com.larpclient.f.J, 0.16));
      tp.N(var1, tp.j("x"), var15[0] + 3, var15[1] + 3, var15[2] - 6, var16 != 0 ? -1 : _3.A(com.larpclient.f.J, 0.8));
   }

   public static boolean p(hw var0, double var1, double var3, int var5, mp var6) {
      if (G != null && G.isEnabled()) {
         WorldMenuModule var7 = G;
         return var7 != null && var7.c(var0, var1, var3, var5, var6);
      } else {
         return false;
      }
   }

   private boolean c(hw var1, double var2, double var4, int var6, mp var7) {
      if (var6 != 0 || var1 != this.J || !this.L()) {
         return false;
      } else if (L(var7, var2, var4)) {
         mt.U("GUI", "World Menu closed with its X");
         var1.onClose();
         return true;
      } else if (w(var7, var2, var4) && this.p) {
         double var8 = this.E(var1);
         double var10 = (this.x - var1.width / 2.0) * var8;
         double var12 = (var1.height / 2.0 - this.k) * var8;
         double[] var14 = this.Y.point(var10, var12);
         Vec3 var15 = getMc().gameRenderer.mainCamera().position();
         double var16 = new Vec3(var14[0], var14[1], var14[2]).distanceTo(var15);
         this.U = new WorldMenuModule.r(Math.clamp(var16, 0.5, this.j()), var10, var12);
         mt.U("GUI", "World Menu grabbed {} blocks away", y(var16));
         return true;
      } else {
         return false;
      }
   }

   private void o() {
      if (this.U != null) {
         this.U = null;
         mt.U(
            "GUI",
            "World Menu set down {} blocks away, yaw {}, pitch {}",
            y(this.O()),
            this.Y == null ? 0L : Math.round(this.Y.yaw()),
            this.Y == null ? 0L : Math.round(this.Y.pitch())
         );
      }
   }

   @Override
   public void onEnable() {
      mt.U(
         "GUI", "World Menu active: placement={}, close distance={}, height={}%, size={}", this.z.P(), this.m.P().intValue(), this.E.P().intValue(), this.a.P()
      );
   }

   @Override
   public void onDisable() {
      this.R("turned off");
      this.F.Q(0.0);
   }

   private record r(double distance, double right, double up) {
   }
}
