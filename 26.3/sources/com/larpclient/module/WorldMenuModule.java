package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.dx;
import com.larpclient.f8;
import com.larpclient.fq;
import com.larpclient.fv;
import com.larpclient.h5;
import com.larpclient.h_;
import com.larpclient.j2;
import com.larpclient.nf;
import com.larpclient.oa;
import com.larpclient.vv;
import com.larpclient.wz;
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
   private static WorldMenuModule W;
   private final o G = this.addSetting(
      new o("Placement", "Pinned stays where you leave it and closes once you walk away; Follow moves along with you", "Pinned", "Pinned", "Follow")
   );
   private final b L = this.addSetting(new b("Close Distance", "A pinned menu closes once you are this many blocks from it", 15.0, 4.0, 48.0, 1.0));
   private final b N = this.addSetting(
      new b("Menu Height", "Share of your view the menu fills when it opens in front of you, in percent", 50.0, 30.0, 90.0, 1.0)
   );
   private final b P = this.addSetting(new b("Panel Size", "Height of the menu panel in blocks", 1.5, 0.5, 4.0, 0.1));
   private static final double I = 0.5;
   private static final double A = 16.0;
   private static final long d = 50000000L;
   private static final long k = 250L;
   private static final int K = 76;
   private static final int U = 6;
   private static final int X = 9;
   private static final int y = 16;
   private static final int p = 9;
   private static final int u = 16;
   private static final int Q = 22;
   private nf.j c;
   private Object a;
   private f8 b;
   private f8 Y;
   private Vec3 T;
   private boolean j;
   private double D;
   private double O;
   private int v;
   private double E;
   private double n;
   private long w;
   private int H = -1;
   private WorldMenuModule.l g;
   private final fq h = new fq(0.0);
   private final fq f = new fq(0.0);
   private double z = 1.0;
   private long s;

   public WorldMenuModule() {
      super("World Menu", "Opens the ClickGUI on a panel in the world while you keep playing", Category.RENDER, -1);
      W = this;
   }

   public static WorldMenuModule f() {
      return W;
   }

   @Override
   public String getDisplayName() {
      return "3D Click UI";
   }

   private boolean G() {
      Minecraft var1 = getMc();
      return this.isRuntimeActive() && var1.player != null && var1.level != null && var1.gameRenderer.mainCamera().isInitialized();
   }

   private boolean O() {
      Minecraft var1 = getMc();
      return var1.gui.screen() == null && var1.gui.overlay() == null && var1.mouseHandler.isMouseGrabbed();
   }

   private double y(f8 var1) {
      double[] var2 = var1.u();
      return this.P.getValue() / Math.max(1.0, var2[3] - var2[1]);
   }

   private dx.z j(f8 var1, nf.j var2) {
      Camera var3 = getMc().gameRenderer.mainCamera();
      Vec3 var4 = var3.position();
      return nf.X(
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

   private static double i(f8 var0, dx.z var1) {
      double[] var2 = var0.u();
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

   private double w() {
      return this.G.w("Pinned") ? Math.clamp(this.L.getValue() - 1.0, 0.5, 16.0) : 16.0;
   }

   public static boolean I(f8 var0) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var1 = W;
         return var1 != null && var1.u(var0);
      } else {
         return false;
      }
   }

   private boolean u(f8 var1) {
      if (!this.G()) {
         return false;
      }

      if (var1 == this.b) {
         return true;
      }

      if (var1.E() != null) {
         return false;
      }

      if (this.b != null) {
         this.w("a new window replaced it");
      }

      this.Y = null;
      this.b = var1;
      this.s();
      Window var2 = getMc().getWindow();
      var1.init(var2.getGuiScaledWidth(), var2.getGuiScaledHeight());
      this.i(var1);
      return true;
   }

   private void i(f8 var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      Vec3 var3 = var2.position();
      nf.j var4 = nf.l(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), 1.0);
      double var5 = i(var1, this.j(var1, var4));
      double var7 = var5 > 0.0 ? Math.clamp(var5 / (this.N.getValue() / 100.0), 0.5, 16.0) : 2.0;
      this.c = nf.l(var3.x, var3.y, var3.z, var2.yRot(), var2.xRot(), var7);
      this.a = getMc().level;
      this.T = var3;
      this.h.p(1.0);
      this.z = 1.0;
      this.s = 0L;
      _q.c(
         "GUI",
         "World Menu opened in the world, {} blocks ahead, yaw {}, pitch {}, {}",
         A(var7),
         Math.round(this.c.yaw()),
         Math.round(this.c.pitch()),
         this.G.w("Pinned") ? "closes beyond " + this.L.getValue().intValue() + " blocks" : "following"
      );
   }

   private double i() {
      if (this.c == null) {
         return 0.0;
      }

      Vec3 var1 = getMc().gameRenderer.mainCamera().position();
      return this.c.distance(var1.x, var1.y, var1.z);
   }

   private static String A(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static boolean m() {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var0 = W;
         return var0 != null && var0.b != null && var0.G();
      } else {
         return false;
      }
   }

   public static boolean a() {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var0 = W;
         if (var0 != null && var0.b != null && var0.G()) {
            _q.c("GUI", "World Menu closed with the menu key");
            var0.b.onClose();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean d(f8 var0) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var1 = W;
         return var1 != null && var1.l(var0);
      } else {
         return false;
      }
   }

   private boolean l(f8 var1) {
      if (var1 != null && var1 == this.b) {
         this.b = null;
         this.Y = var1;
         this.s();
         var1.removed();
         _q.c("GUI", "World Menu closing {} blocks away", A(this.i()));
         return true;
      } else {
         return false;
      }
   }

   private void w(String var1) {
      f8 var2 = this.b;
      this.b = this.Y = null;
      this.s();
      this.c = null;
      this.a = null;
      this.T = null;
      if (var2 != null) {
         _q.c("GUI", "World Menu removed its panel ({})", var1);

         try {
            var2.removed();
         } catch (RuntimeException var4) {
            _q.R("GUI", "World Menu window cleanup failed after {}: {}", var1, var4.toString());
         }
      }
   }

   private void s() {
      this.v = 0;
      this.g = null;
      this.j = (boolean)0;
   }

   @Override
   public void onTick() {
      f8 var1 = this.b;
      if (var1 != null) {
         int var10000 = this.G();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.a == getMc().level) {
            var1.tick();
            if (this.b == var1) {
               int var11 = this.G.w("Pinned");
               boolean var7 = false;
               byte var6 = (byte)var11;
               var11 = Integer.signum(var6);
               if (((var11 | -var11) >>> 31 ^ 1) == 0 && this.g == null) {
                  double var2 = this.i();
                  double var14;
                  var11 = (var14 = var2 - this.L.getValue()) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
                  boolean var5 = false;
                  int var4 = var11;
                  if (-Integer.signum(var4) >>> 31 == 0) {
                     return;
                  }

                  _q.c("GUI", "World Menu closed: you are {} blocks from it, past Close Distance {}", A(var2), this.L.getValue().intValue());
                  var1.onClose();
                  return;
               }
            }
         } else {
            this.w("left the level");
         }
      }
   }

   public static void C(GuiGraphicsExtractor var0, float var1) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var2 = W;
         if (var2 != null) {
            var2.t(var0, var1);
         }
      }
   }

   private void t(GuiGraphicsExtractor var1, float var2) {
      this.j = (boolean)0;
      f8 var3 = this.b != null ? this.b : this.Y;
      if (var3 != null) {
         Minecraft var4 = getMc();
         if (!this.G() || this.c == null || this.a != var4.level) {
            this.w(this.G() ? "left the level" : "unavailable");
         } else if (var3 == this.Y && var3.K()) {
            this.Y = null;
         } else {
            if (var3.width != var1.guiWidth() || var3.height != var1.guiHeight()) {
               if (var3 != this.b) {
                  this.Y = null;
                  return;
               }

               var3.init(var1.guiWidth(), var1.guiHeight());
               _q.c("GUI", "World Menu window laid out again at {}x{}", var3.width, var3.height);
            }

            if (!var4.gameRenderer.gameRenderState().guiRenderState.isHudHidden && !(var4.gui.screen() instanceof fv)) {
               Vec3 var5 = var4.gameRenderer.mainCamera().position();
               if (this.G.w("Follow") && this.T != null && this.g == null) {
                  this.c = new nf.j(
                     this.c.x() + var5.x - this.T.x, this.c.y() + var5.y - this.T.y, this.c.z() + var5.z - this.T.z, this.c.yaw(), this.c.pitch()
                  );
               }

               this.T = var5;
               if (this.g != null) {
                  this.S(var3);
               }

               if (!this.c.faces(var5.x, var5.y, var5.z)) {
                  this.h.p(0.0);
               } else {
                  dx.z var6 = this.j(var3, this.c);
                  double[] var7 = var3.u();
                  double[] var8 = var6.bounds(0.0, 0.0, var3.width, Math.max(var3.height, var7[3] + 24.0));
                  if (!(var8[2] < 0.0) && !(var8[3] < 0.0) && !(var8[0] > var3.width) && !(var8[1] > var3.height)) {
                     long var9 = System.nanoTime();
                     if (var9 - this.s >= 50000000L) {
                        this.s = var9;
                        this.z = this.X(var3, var5);
                     }

                     double var11 = this.h.n(this.z, 160);
                     if (!(var11 < 0.01)) {
                        double[] var13 = var6.inverse(var3.width / 2.0, var3.height / 2.0);
                        int var14 = var3 == this.b && var13[0] != -10000.0 ? 1 : 0;
                        this.j = (boolean)(var14 != 0 && var11 > 0.5 && this.O() && var3.d(var13[0], var13[1]) && !this.Y(var3, var13, var5) ? 1 : 0);
                        if (this.j) {
                           this.D = var13[0];
                           this.O = var13[1];
                        }

                        if (var14 != 0 && this.v != 0 && this.g == null && (var13[0] != this.E || var13[1] != this.n)) {
                           MouseButtonInfo var15 = new MouseButtonInfo(Integer.numberOfTrailingZeros(this.v), 0);
                           var3.mouseDragged(new MouseButtonEvent(var13[0], var13[1], var15), var13[0] - this.E, var13[1] - this.n);
                           this.E = var13[0];
                           this.n = var13[1];
                        }

                        int var18 = !this.j && (var14 == 0 || this.v == 0) ? 0 : 1;
                        int var16 = var18 != 0 ? (int)Math.floor(var13[0]) : -10000;
                        int var17 = var18 != 0 ? (int)Math.floor(var13[1]) : -10000;
                        var1.nextStratum();
                        dx.X(var1, var6, false, () -> {
                           var1.nextStratum();

                           try (oa var7x = oa.j(var11)) {
                              var3.extractRenderState(var1, var16, var17, var2);
                           }
                        });
                        var1.nextStratum();
                     }
                  } else {
                     this.h.p(0.0);
                  }
               }
            }
         }
      }
   }

   private boolean Y(f8 var1, double[] var2, Vec3 var3) {
      HitResult var4 = getMc().hitResult;
      if (var4 != null && var4.getType() != Type.MISS) {
         double var5 = this.y(var1);
         double[] var7 = this.c.point((var2[0] - var1.width / 2.0) * var5, (var1.height / 2.0 - var2[1]) * var5);
         return var4.getLocation().distanceTo(var3) < new Vec3(var7[0], var7[1], var7[2]).distanceTo(var3);
      } else {
         return false;
      }
   }

   private void S(f8 var1) {
      Camera var2 = getMc().gameRenderer.mainCamera();
      double[] var3 = nf.N(var2.getViewRotationProjectionMatrix(new Matrix4f()), var1.width / 2.0, var1.height / 2.0, var1.width, var1.height);
      if (var3 != null) {
         Vec3 var4 = var2.position();
         this.c = nf.b(var4.x, var4.y, var4.z, var3, this.g.distance(), this.g.right(), this.g.up());
      }
   }

   private double X(f8 var1, Vec3 var2) {
      Minecraft var3 = getMc();
      double[] var4 = var1.u();
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
         double[] var25 = this.c.point((var24[0] - var7) * var5, (var9 - var24[1]) * var5);
         Vec3 var26 = new Vec3(var25[0], var25[1], var25[2]);
         BlockHitResult var27 = var3.level.clip(new ClipContext(var2, var26, Block.VISUAL, Fluid.NONE, var3.player));
         if (var27.getType() == Type.MISS || var27.getLocation().distanceTo(var2) >= var26.distanceTo(var2) - 0.05) {
            var20++;
         }
      }

      return (double)var20 / var19.length;
   }

   public static boolean l(MouseButtonInfo var0, int var1) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var2 = W;
         return var2 != null && var2.x(var0, var1);
      } else {
         return false;
      }
   }

   private boolean x(MouseButtonInfo var1, int var2) {
      f8 var3 = this.b;
      int var4 = 1 << Math.clamp(j2.B(var1), 0, 30);
      if (var2 == 0) {
         if ((this.v & var4) == 0) {
            return false;
         }

         this.v &= ~var4;
         if (j2.B(var1) == 0) {
            this.F();
         }

         if (var3 != null) {
            var3.mouseReleased(new MouseButtonEvent(this.E, this.n, var1));
         }

         return getMc().gui.screen() == null;
      } else {
         if (var2 != 1 || var3 == null || !this.G() || !this.O()) {
            return false;
         }

         if (!this.j) {
            if (var3.g()) {
               var3.T();
               _q.c("GUI", "World Menu typing ended by a click in the world");
            }

            return false;
         } else {
            long var5 = Util.getMillis();
            int var7 = j2.B(var1) == this.H && var5 - this.w < 250L ? 1 : 0;
            this.w = var5;
            this.H = j2.B(var1);
            this.v |= var4;
            this.E = this.D;
            this.n = this.O;
            var3.mouseClicked(new MouseButtonEvent(this.D, this.O, var1), (boolean)var7);
            return true;
         }
      }
   }

   public static boolean i() {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var0 = W;
         return var0 != null && var0.b != null && (var0.j || var0.v != 0) && var0.O();
      } else {
         return false;
      }
   }

   public static boolean l(double var0) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var2 = W;
         return var2 != null && var2.B(var0);
      } else {
         return false;
      }
   }

   private boolean B(double var1) {
      f8 var3 = this.b;
      if (var3 == null || !this.G() || !this.O()) {
         return false;
      }

      if (this.g != null) {
         this.g = new WorldMenuModule.l(Math.clamp(this.g.distance() * Math.pow(1.12, var1), 0.5, this.w()), this.g.right(), this.g.up());
         return true;
      }

      if (!this.j) {
         return false;
      }

      var3.mouseScrolled(this.D, this.O, 0.0, var1);
      return true;
   }

   private boolean t(f8 var1) {
      return var1 != null && var1 == this.b && this.G() && this.O() && var1.g();
   }

   public static boolean Y() {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var0 = W;
         return var0 != null && var0.t(var0.b);
      } else {
         return false;
      }
   }

   public static boolean p(int var0, KeyEvent var1) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var2 = W;
         if (var2 != null && var0 != 0 && var2.t(var2.b)) {
            if (j2.P(var1) == 256) {
               var2.b.J();
               _q.c("GUI", "World Menu typing ended with Escape");
            } else {
               var2.b.keyPressed(var1);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean z(CharacterEvent var0) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var1 = W;
         if (var1 != null && var1.t(var1.b)) {
            var1.b.charTyped(var0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean s(f8 var0) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var1 = W;
         return var1 != null && var0 != null && (var0 == var1.b || var0 == var1.Y) && var1.G();
      } else {
         return false;
      }
   }

   private static int[] D(h_ var0) {
      return new int[]{var0.left() + (var0.width() - 76) / 2, var0.top() + var0.height() + 9, 76, 6};
   }

   private static boolean p(h_ var0, double var1, double var3) {
      int[] var5 = D(var0);
      return var1 >= var5[0] - 16 && var1 < var5[0] + var5[2] + 16 && var3 >= var5[1] - 9 && var3 < var5[1] + var5[3] + 9;
   }

   private static int[] L(h_ var0) {
      int[] var1 = D(var0);
      return new int[]{var1[0] + var1[2] + 22, var1[1] + var1[3] / 2 - 8, 16, 16};
   }

   private static boolean g(h_ var0, double var1, double var3) {
      int[] var5 = L(var0);
      return var1 >= var5[0] - 4 && var1 < var5[0] + var5[2] + 4 && var3 >= var5[1] - 4 && var3 < var5[1] + var5[3] + 4;
   }

   public static boolean M(h_ var0, double var1, double var3) {
      return p(var0, var1, var3) || g(var0, var1, var3);
   }

   public static void w(GuiGraphicsExtractor var0, f8 var1, h_ var2, int var3, int var4) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var5 = W;
         if (var5 != null && s(var1)) {
            var5.K(var0, var1, var2, var3, var4);
         }
      }
   }

   private void K(GuiGraphicsExtractor var1, f8 var2, h_ var3, int var4, int var5) {
      int var6 = var2 == this.b ? 1 : 0;
      int var7 = var6 != 0 && this.g != null ? 1 : 0;
      int var8 = var7 != 0 || var6 != 0 && p(var3, var4, var5) ? 1 : 0;
      double var9 = this.f.n(var8 != 0 ? 1.0 : 0.0, 180);
      int[] var11 = D(var3);
      int var12 = var11[2] + (int)Math.round(18.0 * var9);
      int var13 = var11[0] - (var12 - var11[2]) / 2;
      int var14 = var7 != 0 ? com.larpclient.c.e(var13, com.larpclient.c.D) : vv.N(com.larpclient.c.H, 0.34 + 0.42 * var9);
      h5.q(var1, var13, var11[1], var12, var11[3], var11[3] / 2, var14);
      int[] var15 = L(var3);
      int var16 = var6 != 0 && this.g == null && g(var3, var4, var5) ? 1 : 0;
      h5.q(var1, var15[0], var15[1], var15[2], var15[3], var15[2] / 2, var16 != 0 ? -1750963 : vv.N(com.larpclient.c.H, 0.16));
      wz.D(var1, wz.M("x"), var15[0] + 3, var15[1] + 3, var15[2] - 6, var16 != 0 ? -1 : vv.N(com.larpclient.c.H, 0.8));
   }

   public static boolean G(f8 var0, double var1, double var3, int var5, h_ var6) {
      if (W != null && W.isEnabled()) {
         WorldMenuModule var7 = W;
         return var7 != null && var7.D(var0, var1, var3, var5, var6);
      } else {
         return false;
      }
   }

   private boolean D(f8 var1, double var2, double var4, int var6, h_ var7) {
      if (var6 != 0 || var1 != this.b || !this.G()) {
         return false;
      } else if (g(var7, var2, var4)) {
         _q.c("GUI", "World Menu closed with its X");
         var1.onClose();
         return true;
      } else if (p(var7, var2, var4) && this.j) {
         double var8 = this.y(var1);
         double var10 = (this.D - var1.width / 2.0) * var8;
         double var12 = (var1.height / 2.0 - this.O) * var8;
         double[] var14 = this.c.point(var10, var12);
         Vec3 var15 = getMc().gameRenderer.mainCamera().position();
         double var16 = new Vec3(var14[0], var14[1], var14[2]).distanceTo(var15);
         this.g = new WorldMenuModule.l(Math.clamp(var16, 0.5, this.w()), var10, var12);
         _q.c("GUI", "World Menu grabbed {} blocks away", A(var16));
         return true;
      } else {
         return false;
      }
   }

   private void F() {
      if (this.g != null) {
         this.g = null;
         _q.c(
            "GUI",
            "World Menu set down {} blocks away, yaw {}, pitch {}",
            A(this.i()),
            this.c == null ? 0L : Math.round(this.c.yaw()),
            this.c == null ? 0L : Math.round(this.c.pitch())
         );
      }
   }

   @Override
   public void onEnable() {
      _q.c(
         "GUI",
         "World Menu active: placement={}, close distance={}, height={}%, size={}",
         this.G.getValue(),
         this.L.getValue().intValue(),
         this.N.getValue().intValue(),
         this.P.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.w("turned off");
      this.h.p(0.0);
   }

   private record l(double distance, double right, double up) {
   }
}
