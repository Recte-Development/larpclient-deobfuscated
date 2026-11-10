package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class gl {
   public gl.g t = gl.g.z;
   public double o = Double.NaN;
   public Vec3 c = new Vec3(0.0, 1.0, 0.0);
   public gl.k d = gl.k.s;
   public gl.x x = gl.x.B;
   public c3.a B = c3.a.Z;
   public gl.c i = gl.c.E;
   public st.z M = st.z.u;
   public st.c e = st.c.u;
   public int u = 8;
   public int j = -1;
   public int Z = -1;
   public int F;
   public double O = 0.3;
   public double Q = 0.65;
   public double n = 0.04;
   public double f = 0.1;
   public double D = 1.0;
   public double z = 1.0;
   public double P = 1.0;
   public double s = 0.7;
   public double V = 1.0;
   public double C = 0.3;
   public double W = 0.4;
   public double K = 1.2;
   public double H;
   public double p = 0.2;
   public double v;
   public double m = 3.0;
   public double Y;
   public double r;
   public double g;
   public double w = 1.0;
   public double X = 0.035;
   public double T;
   public double G;
   public double b;
   public double J = 5.0;
   public int L = 2048;
   public double E = Double.NaN;
   public boolean y;
   public boolean a;
   public double l = 1.0;
   public double I = Double.NaN;
   public boolean R = (boolean)1;

   public enum c {
      K,
      E,
      V;
   }

   public enum g {
      z,
      e,
      K;
   }

   public enum k {
      s("effects.png"),
      D("hitfx.png"),
      T("bloom.png");

      public final String w;

      k(String var3) {
         this.w = var3;
      }
   }

   public enum x {
      B,
      R,
      C,
      l,
      O;
   }
}
