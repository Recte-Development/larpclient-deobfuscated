package com.larpclient;

import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class xp {
   public static final int C = 128;
   private final cg t = new cg();
   private final pf M = new pf();

   public xp() {
      this.t.X = 128;
      this.M.X = 2048;
   }

   public void q(Vec3 var1, Vec3 var2, u8 var3) {
      this.t.X = Math.min(128, _k.m(128));
      this.M.g = var3 == u8.o ? 3 : 5;
      this.M.P = cg.u.B;
      this.M.p = pf.x.S;
      this.M.S = hp.y.h;
      this.M.l = hp.u.C;
      this.M.E = _3.A(-7026494, 0.75 * var3.w);
      this.M.N = _3.A(-15260879, 0.05);
      this.M.M = var3.m * 0.7;
      this.M.O = var3.m;
      this.M.C = 0.025;
      this.M.d = 0.055;
      this.M.Z = 0.45;
      this.M.b = 0.6;
      this.M.r = 0.3;
      this.M.W = 0.1;
      this.M.q = 1.5;
      this.M.f = 0.08;
      this.M.z = var2.x * 0.25;
      this.M.K = var2.y * 0.15;
      this.M.v = var2.z * 0.25;
      this.M.G = 0.0;
      this.M.Y = 0.04;
      this.M.m = 1.0;
      this.M.j = 40.0;
      this.M.i = var3.O();
      this.t.i(var1, this.M);
      this.M.g = 2;
      this.M.P = cg.u.M;
      this.M.E = _3.A(-3046331, 0.7 * var3.w);
      this.M.N = _3.A(-6540233, 0.1);
      this.M.C = 0.018;
      this.M.d = 0.03;
      this.M.M = 0.14;
      this.M.O = 0.24;
      this.M.Z = 0.65;
      this.t.i(var1, this.M);
      this.M.g = 1;
      this.M.P = cg.u.p;
      this.M.E = _3.A(-7026494, 0.2 * var3.w);
      this.M.N = _3.A(-7026494, 0.0);
      this.M.M = this.M.O = var3.m * 0.8;
      this.M.C = this.M.d = 0.08;
      this.M.B = 0.32;
      this.M.L = 0.012;
      this.M.V = var2.lengthSqr() < 1.0E-8 ? new Vec3(0.0, 1.0, 0.0) : var2;
      this.M.Z = this.M.r = this.M.W = this.M.z = this.M.K = this.M.v = 0.0;
      this.M.f = this.M.Y = 0.0;
      this.M.i = (boolean)0;
      this.t.i(var1, this.M);
   }

   public void a() {
      int var1 = _k.m(128);
      if (this.t.x() > var1) {
         this.t.J();
      }

      this.t.X = var1;
      this.t.Y(1.0);
   }

   public void i(float var1, CameraRenderState var2) {
      this.t.q(var2.pos, var1, var2.cullFrustum);
   }

   public void s(ki var1, boolean var2) {
      this.t.e(var1, Vec3.ZERO, 0.0F, var2);
   }

   public boolean f() {
      return this.t.r();
   }

   public boolean s() {
      return this.t.C();
   }

   public int U() {
      return this.t.x();
   }

   public void Z() {
      this.t.J();
   }
}
