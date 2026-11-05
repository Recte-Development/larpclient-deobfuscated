package com.larpclient;

import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class _l {
   public static final int V = 128;
   private final f4 P = new f4();
   private final fk q = new fk();

   public _l() {
      this.P.P = 128;
      this.q.p = 2048;
   }

   public void a(Vec3 var1, Vec3 var2, ol var3) {
      this.P.P = Math.min(128, lo.H(128));
      this.q.t = var3 == ol.G ? 3 : 5;
      this.q.D = f4.a.b;
      this.q.C = fk.h.z;
      this.q.N = yc.d.k;
      this.q.U = yc.y.J;
      this.q.B = oh.i(-7026494, 0.75 * var3.D);
      this.q.v = oh.i(-15260879, 0.05);
      this.q.Y = var3.c * 0.7;
      this.q.d = var3.c;
      this.q.V = 0.025;
      this.q.u = 0.055;
      this.q.e = 0.45;
      this.q.F = 0.6;
      this.q.k = 0.3;
      this.q.H = 0.1;
      this.q.b = 1.5;
      this.q.s = 0.08;
      this.q.h = var2.x * 0.25;
      this.q.x = var2.y * 0.15;
      this.q.W = var2.z * 0.25;
      this.q.K = 0.0;
      this.q.X = 0.04;
      this.q.T = 1.0;
      this.q.Q = 40.0;
      this.q.G = var3.y();
      this.P.p(var1, this.q);
      this.q.t = 2;
      this.q.D = f4.a.d;
      this.q.B = oh.i(-3046331, 0.7 * var3.D);
      this.q.v = oh.i(-6540233, 0.1);
      this.q.V = 0.018;
      this.q.u = 0.03;
      this.q.Y = 0.14;
      this.q.d = 0.24;
      this.q.e = 0.65;
      this.P.p(var1, this.q);
      this.q.t = 1;
      this.q.D = f4.a.U;
      this.q.B = oh.i(-7026494, 0.2 * var3.D);
      this.q.v = oh.i(-7026494, 0.0);
      this.q.Y = this.q.d = var3.c * 0.8;
      this.q.V = this.q.u = 0.08;
      this.q.o = 0.32;
      this.q.z = 0.012;
      this.q.S = var2.lengthSqr() < 1.0E-8 ? new Vec3(0.0, 1.0, 0.0) : var2;
      this.q.e = this.q.k = this.q.H = this.q.h = this.q.x = this.q.W = 0.0;
      this.q.s = this.q.X = 0.0;
      this.q.G = (boolean)0;
      this.P.p(var1, this.q);
   }

   public void s() {
      int var1 = lo.H(128);
      if (this.P.j() > var1) {
         this.P.U();
      }

      this.P.P = var1;
      this.P.W(1.0);
   }

   public void Q(float var1, CameraRenderState var2) {
      this.P.j(var2.pos, var1, var2.cullFrustum);
   }

   public void r(r6 var1, boolean var2) {
      this.P.Y(var1, Vec3.ZERO, 0.0F, var2);
   }

   public boolean A() {
      return this.P.t();
   }

   public boolean X() {
      return this.P.C();
   }

   public int u() {
      return this.P.j();
   }

   public void S() {
      this.P.U();
   }
}
