package com.larpclient;

import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class _x {
   private final cg F = new cg();
   private final pf z = new pf();
   private final Random J = new Random(184360998L);
   private double M;
   private double y;
   private double u;

   public _x() {
      this.z.X = 2048;
      this.z.g = 1;
   }

   public void t(Vec3 var1, u8 var2, boolean var3) {
      this.u = this.y;
      this.y = Math.clamp(this.y + (var3 ? 0.1 : -0.1), 0.0, 1.0);
      this.F.X = var2.n();
      if (this.F.x() > this.F.X) {
         this.F.J();
      }

      this.F.Y(1.0);
      if (!var3) {
         this.M = 0.0;
      } else {
         this.M = this.M + var2.I * 0.05;
         int var4 = (int)this.M;
         this.M -= var4;

         for (int var5 = 0; var5 < var4 && this.F.x() < this.F.X; var5++) {
            int var6 = this.J.nextInt(7) == 0 ? 1 : 0;
            this.z.P = var6 != 0 ? cg.u.a : cg.u.z;
            this.z.p = pf.x.S;
            this.z.E = _3.A(var6 != 0 ? -3046331 : -6511188, (var6 != 0 ? 0.65 : 0.3) * var2.w);
            this.z.N = _3.A(var6 != 0 ? -6540233 : -15260879, 0.08);
            this.z.i = (boolean)(var6 != 0 && var2.O() ? 1 : 0);
            this.z.M = 9.0;
            this.z.O = 12.0;
            this.z.C = var6 != 0 ? 0.013 : 0.014;
            this.z.d = var6 != 0 ? 0.029 : 0.04;
            this.z.S = hp.y.e;
            this.z.l = hp.u.J;
            this.z.W = 0.0;
            this.z.q = 0.15;
            this.z.Z = 0.045;
            this.z.b = 0.5;
            this.z.r = -0.12;
            this.z.z = 0.14;
            this.z.v = 0.06;
            this.z.f = 0.0;
            this.z.e = 0.7;
            this.z.G = 0.13;
            this.z.Y = 0.28;
            this.z.m = 6.5;
            this.z.j = var2.x * 1.45;
            this.F.i(mh.m(var1.add(0.0, 2.0, 0.0), var2.x, 8.0, this.J), this.z);
         }

         kx.s("Halloween cinders", this.F.x());
      }
   }

   public void B(float var1, CameraRenderState var2) {
      this.F.D(var2.pos, var1, var2.cullFrustum, 0.65, hp.R(this.u + (this.y - this.u) * var1));
   }

   public void Y(ki var1, boolean var2) {
      this.F.e(var1, Vec3.ZERO, 0.0F, var2);
   }

   public boolean U() {
      return this.F.r();
   }

   public boolean o() {
      return this.F.C();
   }

   public int K() {
      return this.F.x();
   }

   public void r() {
      this.F.J();
      this.M = this.y = this.u = 0.0;
   }
}
