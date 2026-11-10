package com.larpclient;

import net.minecraft.class_2960;
import net.minecraft.class_332;

public final class gl {
   public static final int l = 300;
   public static final int w = 20;
   static final int a = 44;
   static final int c = 10;
   static final int e = 13;
   static final long B = 380000000L;
   static final long o = 520000000L;
   static final double N = 1.6;
   private static final int Z = -1250068;
   private static final int R = -4671294;
   private static final int L = 805306368;
   private static final class_2960 s = class_2960.method_60655("larp-client", "textures/gui/logo.png");
   private static final gl.z y = new gl.z(gl.s.g, "Island Lyrics preview line", "sample", true, 0.0);
   private final gl.r z = new gl.r(44.0F, 170.0F, 19.0F);
   private final gl.r u = new gl.r(14.0F, 170.0F, 19.0F);
   private final gl.r v = new gl.r(0.0F, 90.0F, 18.0F);
   private final gl.r F = new gl.r(0.0F, 120.0F, 20.0F);
   private gl.z n = gl.z.IDLE;
   private gl.z I = gl.z.IDLE;
   private long p;
   private long Q;
   private double A;

   static gl.z h(h0.s var0, long var1) {
      yl var3 = var0 == null ? null : var0.playback();
      if (var3 != null && var1 - var3.observedAt() <= 4000000000L) {
         String var4 = var3.key();
         bc var5 = var0.lyrics() == null ? null : var0.lyrics().timed();
         double var6 = var3.positionAt(var1);
         if (var5 != null && var3.playing()) {
            int var8 = var5.F(var6);
            String var9 = var5.K(var8, var6);
            if (!var9.isBlank()) {
               double var10 = var5.S().get(var8).seconds();
               double var12 = var8 + 1 < var5.S().size() ? var5.S().get(var8 + 1).seconds() : var3.duration();
               double var14 = Math.clamp((var6 - var10) / Math.clamp(var12 - var10, 0.2, 18.0), 0.0, 1.0);
               return new gl.z(gl.s.g, var9, "l" + var4 + "\u001f" + var8, true, var14);
            }
         }

         String var16 = var3.artist().isBlank() ? var3.title() : var3.title() + "  ·  " + var3.artist();
         return new gl.z(gl.s.Z, var16, "s" + var4, var3.playing(), 0.0);
      } else {
         return gl.z.IDLE;
      }
   }

   public boolean I(h0.s var1, long var2) {
      return h(var1, var2).kind != gl.s.X || this.F.t >= 0.02F;
   }

   public void K() {
      this.n = this.I = gl.z.IDLE;
      this.z.H(44.0F);
      this.u.H(14.0F);
      this.v.H(0.0F);
      this.F.H(0.0F);
      this.p = this.Q = 0L;
      this.A = 0.0;
   }

   public void B(class_332 var1, int var2, int var3, int var4, h0.s var5, long var6) {
      float var8 = this.Q == 0L ? 0.0F : Math.min(0.05F, (float)(var6 - this.Q) / 1.0E9F);
      this.Q = var6;
      gl.z var9 = h(var5, var6);
      if (!var9.key.equals(this.n.key)) {
         this.I = this.n;
         this.n = var9;
         this.p = var6;
         if (var9.kind == gl.s.g) {
            this.v.H(0.0F);
         }
      } else {
         this.n = var9;
      }

      if (this.n.playing) {
         this.A = (this.A + var8 * 1.6 * Math.PI * 2.0) % (Math.PI * 2);
      }

      float var10 = this.n.kind == gl.s.X ? 44.0F : B(this.n, var4);
      float var11 = this.F.h(this.n.kind == gl.s.X ? 0.0F : 1.0F, var8);
      float var12 = this.z.h(var10, var8);
      float var13 = this.u.h(this.n.kind == gl.s.X ? 14.0F : 20.0F, var8);
      if (!(var11 < 0.02F)) {
         float var14 = u(Math.clamp((float)(var6 - this.p) / 3.8E8F, 0.0F, 1.0F));
         float var15 = 0.0F;
         if (this.n.kind == gl.s.g) {
            float var16 = q2.U(this.n.text, 10.0) - (Math.round(var12) - 50);
            var15 = this.v.h(var16 <= 0.0F ? 0.0F : (float)Math.clamp(this.n.progress * (var16 + 30.0F) - 15.0, 0.0, var16), var8);
         }

         float var17 = Math.clamp((float)(var6 - this.p) / 5.2E8F, 0.0F, 1.0F);
         this.j(var1, var2, var3, var4, Math.max(12, Math.round(var12)), Math.max(8, Math.round(var13)), var11, var14, var17, var15, var6);
      }
   }

   public void p(class_332 var1, int var2, int var3, int var4, long var5) {
      gl.z var7 = this.n;
      this.n = y;

      try {
         this.j(var1, var2, var3, var4, Math.round(B(y, var4)), 20, 1.0F, 1.0F, 1.0F, 0.0F, var5);
      } finally {
         this.n = var7;
      }
   }

   private static float B(gl.z var0, int var1) {
      return Math.clamp(q2.U(var0.text, 10.0) + 62.0F, 110.0F, Math.max(110, var1));
   }

   private void j(class_332 var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, float var10, long var11) {
      int var13 = var2 + (var4 - var5) / 2;
      int var14 = var3 + (20 - var6) / 2;
      int var15 = var6 / 2;

      try (fp var16 = fp.O(Math.clamp(var7, 0.0F, 1.0F))) {
         ws.K(var1, var13, var14, var5, var6, var15, 3.0F, 805306368, 805306368);
         ws.r(var1, var13, var14, var5, var6, var15, ur.i());
         var1.method_44379(var13 + 2, var14, var13 + var5 - 2, var14 + var6);

         try {
            if (var8 < 1.0F && this.I.kind != gl.s.X) {
               try (fp var17 = fp.O((1.0F - var8) * 0.5)) {
                  int var18 = Math.round(var8 * 3.0F);
                  this.X(var1, this.I, var13 - var18, var14, var5, var6, var11, 0.0F, 1.0F);
                  this.X(var1, this.I, var13 + var18, var14, var5, var6, var11, 0.0F, 1.0F);
               }
            }

            try (fp var35 = fp.O(var8)) {
               if (this.n.kind == gl.s.X) {
                  double var36 = 0.5 + 0.5 * Math.sin(var11 / 9.0E8);
                  A(var1, var13 + var5 / 2.0F, var14 + var6 / 2.0F, 2.2F, ei.R(ez.a, 0.55 + 0.35 * var36));
               } else {
                  this.X(var1, this.n, var13, var14, var5, var6, var11, var10, var9);
               }
            }
         } finally {
            var1.method_44380();
         }
      }
   }

   private void X(class_332 var1, gl.z var2, int var3, int var4, int var5, int var6, long var7, float var9, float var10) {
      float var11 = var4 + var6 / 2.0F;
      this.z(var1, var3 + 12, var11);
      D(var1, var3 + var5 - 22, var11, var2.playing, var7);
      int var12 = var3 + 23;
      int var13 = var5 - 50;
      double var14 = q2.e(var4, var6, 10.0);
      if (var2.kind == gl.s.g) {
         var1.method_44379(var12 - 1, var4, var12 + var13 + 1, var4 + var6);

         try {
            if (var10 < 1.0F) {
               Q(var1, var2.text, var12 - var9, var14, var10);
            } else {
               w5.y(var1, var2.text, var12 - var9, var14, 10.0, -1250068);
            }
         } finally {
            var1.method_44380();
         }
      } else {
         w5.y(var1, q2.K(var2.text, var13, 10.0), var12, var14, 10.0, -4671294);
      }
   }

   private static void Q(class_332 var0, String var1, double var2, double var4, float var6) {
      float var7 = Math.max(1.0F, q2.U(var1, 10.0));
      float var8 = 0.0F;
      int var9 = 0;

      while (var9 < var1.length()) {
         int var10 = var1.codePointAt(var9);
         String var11 = new String(Character.toChars(var10));
         float var12 = q2.U(var11, 10.0);
         float var13 = Math.clamp((var6 - (var8 + var12 / 2.0F) / var7 * 0.5F) / 0.5F, 0.0F, 1.0F);
         if (var13 > 0.0F && !Character.isWhitespace(var10)) {
            try (fp var14 = fp.O(var13)) {
               w5.y(var0, var11, var2 + var8, var4 + 4.0F * (1.0F - u(var13)), 10.0, -1250068);
            }
         }

         var8 += var12;
         var9 += Character.charCount(var10);
      }
   }

   private void z(class_332 var1, float var2, float var3) {
      var1.method_51448().pushMatrix();

      try {
         var1.method_51448().translate(var2, var3).rotate((float)this.A);
         if (!q2.w(var1, "logo", -6.5, -6.5, 13.0, -1)) {
            gu.W(var1, s, -6, -6, 13, 13, 64, 64);
         }
      } finally {
         var1.method_51448().popMatrix();
      }
   }

   private static void D(class_332 var0, int var1, float var2, boolean var3, long var4) {
      double var6 = var4 / 1.0E9;

      for (int var8 = 0; var8 < 4; var8++) {
         float var9 = var3 ? (float)(3.0 + 5.0 * Math.abs(Math.sin(var6 * (2.6 + var8 * 1.1) + var8 * 1.7))) : 2.5F;
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var1 + var8 * 3.5F, var2 - var9 / 2.0F);
            ws.r(var0, 0.0F, 0.0F, 2.0F, Math.max(2, Math.round(var9)), 1.0F, ei.R(ez.a, 0.95));
         } finally {
            var0.method_51448().popMatrix();
         }
      }
   }

   private static void A(class_332 var0, float var1, float var2, float var3, int var4) {
      var0.method_51448().pushMatrix();

      try {
         var0.method_51448().translate(var1, var2).scale(var3 / 8.0F, var3 / 8.0F);
         ws.r(var0, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F, var4);
      } finally {
         var0.method_51448().popMatrix();
      }
   }

   static float u(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   static final class r {
      float t;
      float K;
      final float g;
      final float U;

      r(float var1, float var2, float var3) {
         this.t = var1;
         this.g = var2;
         this.U = var3;
      }

      float h(float var1, float var2) {
         int var3 = Math.max(1, (int)Math.ceil(var2 / 0.008F));
         float var4 = var2 / var3;

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = this.g * (var1 - this.t) - this.U * this.K;
            this.K += var6 * var4;
            this.t = this.t + this.K * var4;
         }

         return this.t;
      }

      void H(float var1) {
         this.t = var1;
         this.K = 0.0F;
      }
   }

   enum s {
      X,
      Z,
      g;
   }

   record z(gl.s kind, String text, String key, boolean playing, double progress) {
      static final gl.z IDLE = new gl.z(gl.s.X, "", "i", false, 0.0);
   }
}
