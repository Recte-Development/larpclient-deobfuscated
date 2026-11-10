package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public final class pn {
   public static final int s = 300;
   public static final int O = 20;
   static final int v = 44;
   static final int t = 10;
   static final int C = 13;
   static final long K = 380000000L;
   static final long o = 520000000L;
   static final double P = 1.6;
   private static final int m = -1250068;
   private static final int h = -4671294;
   private static final int B = 805306368;
   private static final Identifier a = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final pn.d u = new pn.d(pn.i.q, "Island Lyrics preview line", "sample", true, 0.0);
   private final pn.o E = new pn.o(44.0F, 170.0F, 19.0F);
   private final pn.o F = new pn.o(14.0F, 170.0F, 19.0F);
   private final pn.o A = new pn.o(0.0F, 90.0F, 18.0F);
   private final pn.o j = new pn.o(0.0F, 120.0F, 20.0F);
   private pn.d g = pn.d.IDLE;
   private pn.d S = pn.d.IDLE;
   private long J;
   private long X;
   private double D;

   static pn.d a(cn.j var0, long var1) {
      mt var3 = var0 == null ? null : var0.playback();
      if (var3 != null && var1 - var3.observedAt() <= 4000000000L) {
         String var4 = var3.key();
         h1 var5 = var0.lyrics() == null ? null : var0.lyrics().timed();
         double var6 = var3.positionAt(var1);
         if (var5 != null && var3.playing()) {
            int var8 = var5.g(var6);
            String var9 = var5.h(var8, var6);
            if (!var9.isBlank()) {
               double var10 = var5.y().get(var8).seconds();
               double var12 = var8 + 1 < var5.y().size() ? var5.y().get(var8 + 1).seconds() : var3.duration();
               double var14 = Math.clamp((var6 - var10) / Math.clamp(var12 - var10, 0.2, 18.0), 0.0, 1.0);
               return new pn.d(pn.i.q, var9, "l" + var4 + "\u001f" + var8, true, var14);
            }
         }

         String var16 = var3.artist().isBlank() ? var3.title() : var3.title() + "  ·  " + var3.artist();
         return new pn.d(pn.i.X, var16, "s" + var4, var3.playing(), 0.0);
      } else {
         return pn.d.IDLE;
      }
   }

   public boolean h(cn.j var1, long var2) {
      return a(var1, var2).kind != pn.i.p || this.j.D >= 0.02F;
   }

   public void l() {
      this.g = this.S = pn.d.IDLE;
      this.E.T(44.0F);
      this.F.T(14.0F);
      this.A.T(0.0F);
      this.j.T(0.0F);
      this.J = this.X = 0L;
      this.D = 0.0;
   }

   public void n(GuiGraphicsExtractor var1, int var2, int var3, int var4, cn.j var5, long var6) {
      float var8 = this.X == 0L ? 0.0F : Math.min(0.05F, (float)(var6 - this.X) / 1.0E9F);
      this.X = var6;
      pn.d var9 = a(var5, var6);
      if (!var9.key.equals(this.g.key)) {
         this.S = this.g;
         this.g = var9;
         this.J = var6;
         if (var9.kind == pn.i.q) {
            this.A.T(0.0F);
         }
      } else {
         this.g = var9;
      }

      if (this.g.playing) {
         this.D = (this.D + var8 * 1.6 * Math.PI * 2.0) % (Math.PI * 2);
      }

      float var10 = this.g.kind == pn.i.p ? 44.0F : A(this.g, var4);
      float var11 = this.j.l(this.g.kind == pn.i.p ? 0.0F : 1.0F, var8);
      float var12 = this.E.l(var10, var8);
      float var13 = this.F.l(this.g.kind == pn.i.p ? 14.0F : 20.0F, var8);
      if (!(var11 < 0.02F)) {
         float var14 = I(Math.clamp((float)(var6 - this.J) / 3.8E8F, 0.0F, 1.0F));
         float var15 = 0.0F;
         if (this.g.kind == pn.i.q) {
            float var16 = c2.M(this.g.text, 10.0) - (Math.round(var12) - 50);
            var15 = this.A.l(var16 <= 0.0F ? 0.0F : (float)Math.clamp(this.g.progress * (var16 + 30.0F) - 15.0, 0.0, var16), var8);
         }

         float var17 = Math.clamp((float)(var6 - this.J) / 5.2E8F, 0.0F, 1.0F);
         this.t(var1, var2, var3, var4, Math.max(12, Math.round(var12)), Math.max(8, Math.round(var13)), var11, var14, var17, var15, var6);
      }
   }

   public void R(GuiGraphicsExtractor var1, int var2, int var3, int var4, long var5) {
      pn.d var7 = this.g;
      this.g = u;

      try {
         this.t(var1, var2, var3, var4, Math.round(A(u, var4)), 20, 1.0F, 1.0F, 1.0F, 0.0F, var5);
      } finally {
         this.g = var7;
      }
   }

   private static float A(pn.d var0, int var1) {
      return Math.clamp(c2.M(var0.text, 10.0) + 62.0F, 110.0F, Math.max(110, var1));
   }

   private void t(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, float var10, long var11) {
      int var13 = var2 + (var4 - var5) / 2;
      int var14 = var3 + (20 - var6) / 2;
      int var15 = var6 / 2;

      try (ua var16 = ua.K(Math.clamp(var7, 0.0F, 1.0F))) {
         tb.Q(var1, var13, var14, var5, var6, var15, 3.0F, 805306368, 805306368);
         tb.E(var1, var13, var14, var5, var6, var15, u9.w());
         var1.enableScissor(var13 + 2, var14, var13 + var5 - 2, var14 + var6);

         try {
            if (var8 < 1.0F && this.S.kind != pn.i.p) {
               try (ua var17 = ua.K((1.0F - var8) * 0.5)) {
                  int var18 = Math.round(var8 * 3.0F);
                  this.C(var1, this.S, var13 - var18, var14, var5, var6, var11, 0.0F, 1.0F);
                  this.C(var1, this.S, var13 + var18, var14, var5, var6, var11, 0.0F, 1.0F);
               }
            }

            try (ua var35 = ua.K(var8)) {
               if (this.g.kind == pn.i.p) {
                  double var36 = 0.5 + 0.5 * Math.sin(var11 / 9.0E8);
                  Y(var1, var13 + var5 / 2.0F, var14 + var6 / 2.0F, 2.2F, uh.F(ho.J, 0.55 + 0.35 * var36));
               } else {
                  this.C(var1, this.g, var13, var14, var5, var6, var11, var10, var9);
               }
            }
         } finally {
            var1.disableScissor();
         }
      }
   }

   private void C(GuiGraphicsExtractor var1, pn.d var2, int var3, int var4, int var5, int var6, long var7, float var9, float var10) {
      float var11 = var4 + var6 / 2.0F;
      this.N(var1, var3 + 12, var11);
      N(var1, var3 + var5 - 22, var11, var2.playing, var7);
      int var12 = var3 + 23;
      int var13 = var5 - 50;
      double var14 = c2.X(var4, var6, 10.0);
      if (var2.kind == pn.i.q) {
         var1.enableScissor(var12 - 1, var4, var12 + var13 + 1, var4 + var6);

         try {
            if (var10 < 1.0F) {
               H(var1, var2.text, var12 - var9, var14, var10);
            } else {
               gw.S(var1, var2.text, var12 - var9, var14, 10.0, -1250068);
            }
         } finally {
            var1.disableScissor();
         }
      } else {
         gw.S(var1, c2.M(var2.text, var13, 10.0), var12, var14, 10.0, -4671294);
      }
   }

   private static void H(GuiGraphicsExtractor var0, String var1, double var2, double var4, float var6) {
      float var7 = Math.max(1.0F, c2.M(var1, 10.0));
      float var8 = 0.0F;
      int var9 = 0;

      while (var9 < var1.length()) {
         int var10 = var1.codePointAt(var9);
         String var11 = new String(Character.toChars(var10));
         float var12 = c2.M(var11, 10.0);
         float var13 = Math.clamp((var6 - (var8 + var12 / 2.0F) / var7 * 0.5F) / 0.5F, 0.0F, 1.0F);
         if (var13 > 0.0F && !Character.isWhitespace(var10)) {
            try (ua var14 = ua.K(var13)) {
               gw.S(var0, var11, var2 + var8, var4 + 4.0F * (1.0F - I(var13)), 10.0, -1250068);
            }
         }

         var8 += var12;
         var9 += Character.charCount(var10);
      }
   }

   private void N(GuiGraphicsExtractor var1, float var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2, var3).rotate((float)this.D);
         if (!c2.p(var1, "logo", -6.5, -6.5, 13.0, -1)) {
            kv.I(var1, a, -6, -6, 13, 13, 64, 64);
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   private static void N(GuiGraphicsExtractor var0, int var1, float var2, boolean var3, long var4) {
      double var6 = var4 / 1.0E9;

      for (int var8 = 0; var8 < 4; var8++) {
         float var9 = var3 ? (float)(3.0 + 5.0 * Math.abs(Math.sin(var6 * (2.6 + var8 * 1.1) + var8 * 1.7))) : 2.5F;
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1 + var8 * 3.5F, var2 - var9 / 2.0F);
            tb.E(var0, 0.0F, 0.0F, 2.0F, Math.max(2, Math.round(var9)), 1.0F, uh.F(ho.J, 0.95));
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void Y(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2).scale(var3 / 8.0F, var3 / 8.0F);
         tb.E(var0, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F, var4);
      } finally {
         var0.pose().popMatrix();
      }
   }

   static float I(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   record d(pn.i kind, String text, String key, boolean playing, double progress) {
      static final pn.d IDLE = new pn.d(pn.i.p, "", "i", false, 0.0);
   }

   enum i {
      p,
      X,
      q;
   }

   static final class o {
      float D;
      float O;
      final float W;
      final float C;

      o(float var1, float var2, float var3) {
         this.D = var1;
         this.W = var2;
         this.C = var3;
      }

      float l(float var1, float var2) {
         int var3 = Math.max(1, (int)Math.ceil(var2 / 0.008F));
         float var4 = var2 / var3;

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = this.W * (var1 - this.D) - this.C * this.O;
            this.O += var6 * var4;
            this.D = this.D + this.O * var4;
         }

         return this.D;
      }

      void T(float var1) {
         this.D = var1;
         this.O = 0.0F;
      }
   }
}
