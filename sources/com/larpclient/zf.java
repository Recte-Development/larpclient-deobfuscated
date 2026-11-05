package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public final class zf {
   public static final int z = 300;
   public static final int A = 20;
   static final int b = 44;
   static final int D = 10;
   static final int I = 13;
   static final long e = 380000000L;
   static final long U = 520000000L;
   static final double Z = 1.6;
   private static final int u = -1250068;
   private static final int E = -4671294;
   private static final int i = 805306368;
   private static final Identifier S = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final zf.u B = new zf.u(zf.o.L, "Island Lyrics preview line", "sample", true, 0.0);
   private final zf.c K = new zf.c(44.0F, 170.0F, 19.0F);
   private final zf.c Y = new zf.c(14.0F, 170.0F, 19.0F);
   private final zf.c x = new zf.c(0.0F, 90.0F, 18.0F);
   private final zf.c w = new zf.c(0.0F, 120.0F, 20.0F);
   private zf.u c = zf.u.IDLE;
   private zf.u v = zf.u.IDLE;
   private long h;
   private long T;
   private double H;

   static zf.u g(wj.h var0, long var1) {
      ha var3 = var0 == null ? null : var0.playback();
      if (var3 != null && var1 - var3.observedAt() <= 4000000000L) {
         String var4 = var3.key();
         e6 var5 = var0.lyrics() == null ? null : var0.lyrics().timed();
         double var6 = var3.positionAt(var1);
         if (var5 != null && var3.playing()) {
            int var8 = var5.M(var6);
            String var9 = var5.W(var8, var6);
            if (!var9.isBlank()) {
               double var10 = var5.K().get(var8).seconds();
               double var12 = var8 + 1 < var5.K().size() ? var5.K().get(var8 + 1).seconds() : var3.duration();
               double var14 = Math.clamp((var6 - var10) / Math.clamp(var12 - var10, 0.2, 18.0), 0.0, 1.0);
               return new zf.u(zf.o.L, var9, "l" + var4 + "\u001f" + var8, true, var14);
            }
         }

         String var16 = var3.artist().isBlank() ? var3.title() : var3.title() + "  ·  " + var3.artist();
         return new zf.u(zf.o.E, var16, "s" + var4, var3.playing(), 0.0);
      } else {
         return zf.u.IDLE;
      }
   }

   public boolean d(wj.h var1, long var2) {
      return g(var1, var2).kind != zf.o.u || this.w.W >= 0.02F;
   }

   public void i() {
      this.c = this.v = zf.u.IDLE;
      this.K.d(44.0F);
      this.Y.d(14.0F);
      this.x.d(0.0F);
      this.w.d(0.0F);
      this.h = this.T = 0L;
      this.H = 0.0;
   }

   public void b(GuiGraphicsExtractor var1, int var2, int var3, int var4, wj.h var5, long var6) {
      float var8 = this.T == 0L ? 0.0F : Math.min(0.05F, (float)(var6 - this.T) / 1.0E9F);
      this.T = var6;
      zf.u var9 = g(var5, var6);
      if (!var9.key.equals(this.c.key)) {
         this.v = this.c;
         this.c = var9;
         this.h = var6;
         if (var9.kind == zf.o.L) {
            this.x.d(0.0F);
         }
      } else {
         this.c = var9;
      }

      if (this.c.playing) {
         this.H = (this.H + var8 * 1.6 * Math.PI * 2.0) % (Math.PI * 2);
      }

      float var10 = this.c.kind == zf.o.u ? 44.0F : d(this.c, var4);
      float var11 = this.w.W(this.c.kind == zf.o.u ? 0.0F : 1.0F, var8);
      float var12 = this.K.W(var10, var8);
      float var13 = this.Y.W(this.c.kind == zf.o.u ? 14.0F : 20.0F, var8);
      if (!(var11 < 0.02F)) {
         float var14 = t(Math.clamp((float)(var6 - this.h) / 3.8E8F, 0.0F, 1.0F));
         float var15 = 0.0F;
         if (this.c.kind == zf.o.L) {
            float var16 = ew.N(this.c.text, 10.0) - (Math.round(var12) - 50);
            var15 = this.x.W(var16 <= 0.0F ? 0.0F : (float)Math.clamp(this.c.progress * (var16 + 30.0F) - 15.0, 0.0, var16), var8);
         }

         float var17 = Math.clamp((float)(var6 - this.h) / 5.2E8F, 0.0F, 1.0F);
         this.O(var1, var2, var3, var4, Math.max(12, Math.round(var12)), Math.max(8, Math.round(var13)), var11, var14, var17, var15, var6);
      }
   }

   public void F(GuiGraphicsExtractor var1, int var2, int var3, int var4, long var5) {
      zf.u var7 = this.c;
      this.c = B;

      try {
         this.O(var1, var2, var3, var4, Math.round(d(B, var4)), 20, 1.0F, 1.0F, 1.0F, 0.0F, var5);
      } finally {
         this.c = var7;
      }
   }

   private static float d(zf.u var0, int var1) {
      return Math.clamp(ew.N(var0.text, 10.0) + 62.0F, 110.0F, Math.max(110, var1));
   }

   private void O(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, float var10, long var11) {
      int var13 = var2 + (var4 - var5) / 2;
      int var14 = var3 + (20 - var6) / 2;
      int var15 = var6 / 2;

      try (de var16 = de.l(Math.clamp(var7, 0.0F, 1.0F))) {
         tl.j(var1, var13, var14, var5, var6, var15, 3.0F, 805306368, 805306368);
         tl.g(var1, var13, var14, var5, var6, var15, mn.W());
         var1.enableScissor(var13 + 2, var14, var13 + var5 - 2, var14 + var6);

         try {
            if (var8 < 1.0F && this.v.kind != zf.o.u) {
               try (de var17 = de.l((1.0F - var8) * 0.5)) {
                  int var18 = Math.round(var8 * 3.0F);
                  this.B(var1, this.v, var13 - var18, var14, var5, var6, var11, 0.0F, 1.0F);
                  this.B(var1, this.v, var13 + var18, var14, var5, var6, var11, 0.0F, 1.0F);
               }
            }

            try (de var35 = de.l(var8)) {
               if (this.c.kind == zf.o.u) {
                  double var36 = 0.5 + 0.5 * Math.sin(var11 / 9.0E8);
                  x(var1, var13 + var5 / 2.0F, var14 + var6 / 2.0F, 2.2F, _3.U(f.C, 0.55 + 0.35 * var36));
               } else {
                  this.B(var1, this.c, var13, var14, var5, var6, var11, var10, var9);
               }
            }
         } finally {
            var1.disableScissor();
         }
      }
   }

   private void B(GuiGraphicsExtractor var1, zf.u var2, int var3, int var4, int var5, int var6, long var7, float var9, float var10) {
      float var11 = var4 + var6 / 2.0F;
      this.M(var1, var3 + 12, var11);
      B(var1, var3 + var5 - 22, var11, var2.playing, var7);
      int var12 = var3 + 23;
      int var13 = var5 - 50;
      double var14 = ew.N(var4, var6, 10.0);
      if (var2.kind == zf.o.L) {
         var1.enableScissor(var12 - 1, var4, var12 + var13 + 1, var4 + var6);

         try {
            if (var10 < 1.0F) {
               O(var1, var2.text, var12 - var9, var14, var10);
            } else {
               we.F(var1, var2.text, var12 - var9, var14, 10.0, -1250068);
            }
         } finally {
            var1.disableScissor();
         }
      } else {
         we.F(var1, ew.S(var2.text, var13, 10.0), var12, var14, 10.0, -4671294);
      }
   }

   private static void O(GuiGraphicsExtractor var0, String var1, double var2, double var4, float var6) {
      float var7 = Math.max(1.0F, ew.N(var1, 10.0));
      float var8 = 0.0F;
      int var9 = 0;

      while (var9 < var1.length()) {
         int var10 = var1.codePointAt(var9);
         String var11 = new String(Character.toChars(var10));
         float var12 = ew.N(var11, 10.0);
         float var13 = Math.clamp((var6 - (var8 + var12 / 2.0F) / var7 * 0.5F) / 0.5F, 0.0F, 1.0F);
         if (var13 > 0.0F && !Character.isWhitespace(var10)) {
            try (de var14 = de.l(var13)) {
               we.F(var0, var11, var2 + var8, var4 + 4.0F * (1.0F - t(var13)), 10.0, -1250068);
            }
         }

         var8 += var12;
         var9 += Character.charCount(var10);
      }
   }

   private void M(GuiGraphicsExtractor var1, float var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2, var3).rotate((float)this.H);
         if (!ew.r(var1, "logo", -6.5, -6.5, 13.0, -1)) {
            c7.o(var1, S, -6, -6, 13, 13, 64, 64);
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   private static void B(GuiGraphicsExtractor var0, int var1, float var2, boolean var3, long var4) {
      double var6 = var4 / 1.0E9;

      for (int var8 = 0; var8 < 4; var8++) {
         float var9 = var3 ? (float)(3.0 + 5.0 * Math.abs(Math.sin(var6 * (2.6 + var8 * 1.1) + var8 * 1.7))) : 2.5F;
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1 + var8 * 3.5F, var2 - var9 / 2.0F);
            tl.g(var0, 0.0F, 0.0F, 2.0F, Math.max(2, Math.round(var9)), 1.0F, _3.U(f.C, 0.95));
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void x(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2).scale(var3 / 8.0F, var3 / 8.0F);
         tl.g(var0, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F, var4);
      } finally {
         var0.pose().popMatrix();
      }
   }

   static float t(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   static final class c {
      float W;
      float H;
      final float J;
      final float N;

      c(float var1, float var2, float var3) {
         this.W = var1;
         this.J = var2;
         this.N = var3;
      }

      float W(float var1, float var2) {
         int var3 = Math.max(1, (int)Math.ceil(var2 / 0.008F));
         float var4 = var2 / var3;

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = this.J * (var1 - this.W) - this.N * this.H;
            this.H += var6 * var4;
            this.W = this.W + this.H * var4;
         }

         return this.W;
      }

      void d(float var1) {
         this.W = var1;
         this.H = 0.0F;
      }
   }

   enum o {
      u,
      E,
      L;
   }

   record u(zf.o kind, String text, String key, boolean playing, double progress) {
      static final zf.u IDLE = new zf.u(zf.o.u, "", "i", false, 0.0);
   }
}
