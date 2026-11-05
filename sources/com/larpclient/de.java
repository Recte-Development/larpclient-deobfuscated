package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public final class de {
   public static final int l = 300;
   public static final int a = 20;
   static final int x = 44;
   static final int O = 10;
   static final int r = 13;
   static final long e = 380000000L;
   static final long Y = 520000000L;
   static final double P = 1.6;
   private static final int u = -1250068;
   private static final int E = -4671294;
   private static final int N = 805306368;
   private static final Identifier z = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final de.s A = new de.s(de.q.n, "Island Lyrics preview line", "sample", true, 0.0);
   private final de.r q = new de.r(44.0F, 170.0F, 19.0F);
   private final de.r L = new de.r(14.0F, 170.0F, 19.0F);
   private final de.r b = new de.r(0.0F, 90.0F, 18.0F);
   private final de.r n = new de.r(0.0F, 120.0F, 20.0F);
   private de.s g = de.s.IDLE;
   private de.s s = de.s.IDLE;
   private long i;
   private long R;
   private double v;

   static de.s G(oc.k var0, long var1) {
      _k var3 = var0 == null ? null : var0.playback();
      if (var3 != null && var1 - var3.observedAt() <= 4000000000L) {
         String var4 = var3.key();
         y6 var5 = var0.lyrics() == null ? null : var0.lyrics().timed();
         double var6 = var3.positionAt(var1);
         if (var5 != null && var3.playing()) {
            int var8 = var5.Q(var6);
            String var9 = var5.Y(var8, var6);
            if (!var9.isBlank()) {
               double var10 = var5.w().get(var8).seconds();
               double var12 = var8 + 1 < var5.w().size() ? var5.w().get(var8 + 1).seconds() : var3.duration();
               double var14 = Math.clamp((var6 - var10) / Math.clamp(var12 - var10, 0.2, 18.0), 0.0, 1.0);
               return new de.s(de.q.n, var9, "l" + var4 + "\u001f" + var8, true, var14);
            }
         }

         String var16 = var3.artist().isBlank() ? var3.title() : var3.title() + "  ·  " + var3.artist();
         return new de.s(de.q.Q, var16, "s" + var4, var3.playing(), 0.0);
      } else {
         return de.s.IDLE;
      }
   }

   public boolean T(oc.k var1, long var2) {
      return G(var1, var2).kind != de.q.U || this.n.a >= 0.02F;
   }

   public void f() {
      this.g = this.s = de.s.IDLE;
      this.q.Y(44.0F);
      this.L.Y(14.0F);
      this.b.Y(0.0F);
      this.n.Y(0.0F);
      this.i = this.R = 0L;
      this.v = 0.0;
   }

   public void q(GuiGraphicsExtractor var1, int var2, int var3, int var4, oc.k var5, long var6) {
      float var8 = this.R == 0L ? 0.0F : Math.min(0.05F, (float)(var6 - this.R) / 1.0E9F);
      this.R = var6;
      de.s var9 = G(var5, var6);
      if (!var9.key.equals(this.g.key)) {
         this.s = this.g;
         this.g = var9;
         this.i = var6;
         if (var9.kind == de.q.n) {
            this.b.Y(0.0F);
         }
      } else {
         this.g = var9;
      }

      if (this.g.playing) {
         this.v = (this.v + var8 * 1.6 * Math.PI * 2.0) % (Math.PI * 2);
      }

      float var10 = this.g.kind == de.q.U ? 44.0F : W(this.g, var4);
      float var11 = this.n.J(this.g.kind == de.q.U ? 0.0F : 1.0F, var8);
      float var12 = this.q.J(var10, var8);
      float var13 = this.L.J(this.g.kind == de.q.U ? 14.0F : 20.0F, var8);
      if (!(var11 < 0.02F)) {
         float var14 = r(Math.clamp((float)(var6 - this.i) / 3.8E8F, 0.0F, 1.0F));
         float var15 = 0.0F;
         if (this.g.kind == de.q.n) {
            float var16 = rz.V(this.g.text, 10.0) - (Math.round(var12) - 50);
            var15 = this.b.J(var16 <= 0.0F ? 0.0F : (float)Math.clamp(this.g.progress * (var16 + 30.0F) - 15.0, 0.0, var16), var8);
         }

         float var17 = Math.clamp((float)(var6 - this.i) / 5.2E8F, 0.0F, 1.0F);
         this.o(var1, var2, var3, var4, Math.max(12, Math.round(var12)), Math.max(8, Math.round(var13)), var11, var14, var17, var15, var6);
      }
   }

   public void A(GuiGraphicsExtractor var1, int var2, int var3, int var4, long var5) {
      de.s var7 = this.g;
      this.g = A;

      try {
         this.o(var1, var2, var3, var4, Math.round(W(A, var4)), 20, 1.0F, 1.0F, 1.0F, 0.0F, var5);
      } finally {
         this.g = var7;
      }
   }

   private static float W(de.s var0, int var1) {
      return Math.clamp(rz.V(var0.text, 10.0) + 62.0F, 110.0F, Math.max(110, var1));
   }

   private void o(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, float var10, long var11) {
      int var13 = var2 + (var4 - var5) / 2;
      int var14 = var3 + (20 - var6) / 2;
      int var15 = var6 / 2;

      try (com.larpclient.r var16 = com.larpclient.r.p(Math.clamp(var7, 0.0F, 1.0F))) {
         ow.A(var1, var13, var14, var5, var6, var15, 3.0F, 805306368, 805306368);
         ow.P(var1, var13, var14, var5, var6, var15, m0.z());
         var1.enableScissor(var13 + 2, var14, var13 + var5 - 2, var14 + var6);

         try {
            if (var8 < 1.0F && this.s.kind != de.q.U) {
               try (com.larpclient.r var17 = com.larpclient.r.p((1.0F - var8) * 0.5)) {
                  int var18 = Math.round(var8 * 3.0F);
                  this.m(var1, this.s, var13 - var18, var14, var5, var6, var11, 0.0F, 1.0F);
                  this.m(var1, this.s, var13 + var18, var14, var5, var6, var11, 0.0F, 1.0F);
               }
            }

            try (com.larpclient.r var35 = com.larpclient.r.p(var8)) {
               if (this.g.kind == de.q.U) {
                  double var36 = 0.5 + 0.5 * Math.sin(var11 / 9.0E8);
                  G(var1, var13 + var5 / 2.0F, var14 + var6 / 2.0F, 2.2F, oh.S(rv.e, 0.55 + 0.35 * var36));
               } else {
                  this.m(var1, this.g, var13, var14, var5, var6, var11, var10, var9);
               }
            }
         } finally {
            var1.disableScissor();
         }
      }
   }

   private void m(GuiGraphicsExtractor var1, de.s var2, int var3, int var4, int var5, int var6, long var7, float var9, float var10) {
      float var11 = var4 + var6 / 2.0F;
      this.C(var1, var3 + 12, var11);
      S(var1, var3 + var5 - 22, var11, var2.playing, var7);
      int var12 = var3 + 23;
      int var13 = var5 - 50;
      double var14 = rz.W(var4, var6, 10.0);
      if (var2.kind == de.q.n) {
         var1.enableScissor(var12 - 1, var4, var12 + var13 + 1, var4 + var6);

         try {
            if (var10 < 1.0F) {
               G(var1, var2.text, var12 - var9, var14, var10);
            } else {
               rd.v(var1, var2.text, var12 - var9, var14, 10.0, -1250068);
            }
         } finally {
            var1.disableScissor();
         }
      } else {
         rd.v(var1, rz.H(var2.text, var13, 10.0), var12, var14, 10.0, -4671294);
      }
   }

   private static void G(GuiGraphicsExtractor var0, String var1, double var2, double var4, float var6) {
      float var7 = Math.max(1.0F, rz.V(var1, 10.0));
      float var8 = 0.0F;
      int var9 = 0;

      while (var9 < var1.length()) {
         int var10 = var1.codePointAt(var9);
         String var11 = new String(Character.toChars(var10));
         float var12 = rz.V(var11, 10.0);
         float var13 = Math.clamp((var6 - (var8 + var12 / 2.0F) / var7 * 0.5F) / 0.5F, 0.0F, 1.0F);
         if (var13 > 0.0F && !Character.isWhitespace(var10)) {
            try (com.larpclient.r var14 = com.larpclient.r.p(var13)) {
               rd.v(var0, var11, var2 + var8, var4 + 4.0F * (1.0F - r(var13)), 10.0, -1250068);
            }
         }

         var8 += var12;
         var9 += Character.charCount(var10);
      }
   }

   private void C(GuiGraphicsExtractor var1, float var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2, var3).rotate((float)this.v);
         if (!rz.n(var1, "logo", -6.5, -6.5, 13.0, -1)) {
            fr.O(var1, z, -6, -6, 13, 13, 64, 64);
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   private static void S(GuiGraphicsExtractor var0, int var1, float var2, boolean var3, long var4) {
      double var6 = var4 / 1.0E9;

      for (int var8 = 0; var8 < 4; var8++) {
         float var9 = var3 ? (float)(3.0 + 5.0 * Math.abs(Math.sin(var6 * (2.6 + var8 * 1.1) + var8 * 1.7))) : 2.5F;
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1 + var8 * 3.5F, var2 - var9 / 2.0F);
            ow.P(var0, 0.0F, 0.0F, 2.0F, Math.max(2, Math.round(var9)), 1.0F, oh.S(rv.e, 0.95));
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void G(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2).scale(var3 / 8.0F, var3 / 8.0F);
         ow.P(var0, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F, var4);
      } finally {
         var0.pose().popMatrix();
      }
   }

   static float r(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   enum q {
      U,
      Q,
      n;
   }

   static final class r {
      float a;
      float w;
      final float P;
      final float D;

      r(float var1, float var2, float var3) {
         this.a = var1;
         this.P = var2;
         this.D = var3;
      }

      float J(float var1, float var2) {
         int var3 = Math.max(1, (int)Math.ceil(var2 / 0.008F));
         float var4 = var2 / var3;

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = this.P * (var1 - this.a) - this.D * this.w;
            this.w += var6 * var4;
            this.a = this.a + this.w * var4;
         }

         return this.a;
      }

      void Y(float var1) {
         this.a = var1;
         this.w = 0.0F;
      }
   }

   record s(de.q kind, String text, String key, boolean playing, double progress) {
      static final de.s IDLE = new de.s(de.q.U, "", "i", false, 0.0);
   }
}
