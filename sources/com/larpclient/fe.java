package com.larpclient;

import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class fe {
   private final f4 x = new f4();
   private final fk L = new fk();
   private final Random v = new Random(184360998L);
   private double I;
   private double R;
   private double O;

   public fe() {
      this.L.p = 2048;
      this.L.t = 1;
   }

   public void u(Vec3 var1, ol var2, boolean var3) {
      this.O = this.R;
      this.R = Math.clamp(this.R + (var3 ? 0.1 : -0.1), 0.0, 1.0);
      this.x.P = var2.t();
      if (this.x.j() > this.x.P) {
         this.x.U();
      }

      this.x.W(1.0);
      if (!var3) {
         this.I = 0.0;
      } else {
         this.I = this.I + var2.p * 0.05;
         int var4 = (int)this.I;
         this.I -= var4;

         for (int var5 = 0; var5 < var4 && this.x.j() < this.x.P; var5++) {
            int var6 = this.v.nextInt(7) == 0 ? 1 : 0;
            this.L.D = var6 != 0 ? f4.a.u : f4.a.y;
            this.L.C = fk.h.z;
            this.L.B = oh.i(var6 != 0 ? -3046331 : -6511188, (var6 != 0 ? 0.65 : 0.3) * var2.D);
            this.L.v = oh.i(var6 != 0 ? -6540233 : -15260879, 0.08);
            this.L.G = (boolean)(var6 != 0 && var2.y() ? 1 : 0);
            this.L.Y = 9.0;
            this.L.d = 12.0;
            this.L.V = var6 != 0 ? 0.013 : 0.014;
            this.L.u = var6 != 0 ? 0.029 : 0.04;
            this.L.N = yc.d.c;
            this.L.U = yc.y.a;
            this.L.H = 0.0;
            this.L.b = 0.15;
            this.L.e = 0.045;
            this.L.F = 0.5;
            this.L.k = -0.12;
            this.L.h = 0.14;
            this.L.W = 0.06;
            this.L.s = 0.0;
            this.L.y = 0.7;
            this.L.K = 0.13;
            this.L.X = 0.28;
            this.L.T = 6.5;
            this.L.Q = var2.h * 1.45;
            this.x.p(xl.f(var1.add(0.0, 2.0, 0.0), var2.h, 8.0, this.v), this.L);
         }

         h_.S("Halloween cinders", this.x.j());
      }
   }

   public void B(float var1, CameraRenderState var2) {
      this.x.I(var2.pos, var1, var2.cullFrustum, 0.65, yc.H(this.O + (this.R - this.O) * var1));
   }

   public void l(r6 var1, boolean var2) {
      this.x.Y(var1, Vec3.ZERO, 0.0F, var2);
   }

   public boolean G() {
      return this.x.t();
   }

   public boolean T() {
      return this.x.C();
   }

   public int l() {
      return this.x.j();
   }

   public void H() {
      this.x.U();
      this.I = this.R = this.O = 0.0;
   }
}
