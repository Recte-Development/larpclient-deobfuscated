package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public final class pb {
   public static final int c = 300;
   public static final int p = 20;
   static final int E = 44;
   static final int n = 10;
   static final int i = 13;
   static final long S = 380000000L;
   static final long C = 520000000L;
   static final double H = 1.6;
   private static final int h = -1250068;
   private static final int z = -4671294;
   private static final int F = 805306368;
   private static final Identifier R = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final pb.y a = new pb.y(pb.e.q, "Island Lyrics preview line", "sample", true, 0.0);
   private final pb.i B = new pb.i(44.0F, 170.0F, 19.0F);
   private final pb.i d = new pb.i(14.0F, 170.0F, 19.0F);
   private final pb.i T = new pb.i(0.0F, 90.0F, 18.0F);
   private final pb.i o = new pb.i(0.0F, 120.0F, 20.0F);
   private pb.y r = pb.y.IDLE;
   private pb.y D = pb.y.IDLE;
   private long s;
   private long m;
   private double j;

   static pb.y X(ov.i var0, long var1) {
      p6 var3 = var0 == null ? null : var0.playback();
      if (var3 != null && var1 - var3.observedAt() <= 4000000000L) {
         String var4 = var3.key();
         j var5 = var0.lyrics() == null ? null : var0.lyrics().timed();
         double var6 = var3.positionAt(var1);
         if (var5 != null && var3.playing()) {
            int var8 = var5.N(var6);
            String var9 = var5.Z(var8, var6);
            if (!var9.isBlank()) {
               double var10 = var5.t().get(var8).seconds();
               double var12 = var8 + 1 < var5.t().size() ? var5.t().get(var8 + 1).seconds() : var3.duration();
               double var14 = Math.clamp((var6 - var10) / Math.clamp(var12 - var10, 0.2, 18.0), 0.0, 1.0);
               return new pb.y(pb.e.q, var9, "l" + var4 + "\u001f" + var8, true, var14);
            }
         }

         String var16 = var3.artist().isBlank() ? var3.title() : var3.title() + "  ·  " + var3.artist();
         return new pb.y(pb.e.x, var16, "s" + var4, var3.playing(), 0.0);
      } else {
         return pb.y.IDLE;
      }
   }

   public boolean C(ov.i var1, long var2) {
      return X(var1, var2).kind != pb.e.h || this.o.Z >= 0.02F;
   }

   public void w() {
      this.r = this.D = pb.y.IDLE;
      this.B.w(44.0F);
      this.d.w(14.0F);
      this.T.w(0.0F);
      this.o.w(0.0F);
      this.s = this.m = 0L;
      this.j = 0.0;
   }

   public void a(GuiGraphicsExtractor var1, int var2, int var3, int var4, ov.i var5, long var6) {
      float var8 = this.m == 0L ? 0.0F : Math.min(0.05F, (float)(var6 - this.m) / 1.0E9F);
      this.m = var6;
      pb.y var9 = X(var5, var6);
      if (!var9.key.equals(this.r.key)) {
         this.D = this.r;
         this.r = var9;
         this.s = var6;
         if (var9.kind == pb.e.q) {
            this.T.w(0.0F);
         }
      } else {
         this.r = var9;
      }

      if (this.r.playing) {
         this.j = (this.j + var8 * 1.6 * Math.PI * 2.0) % (Math.PI * 2);
      }

      float var10 = this.r.kind == pb.e.h ? 44.0F : S(this.r, var4);
      float var11 = this.o.q(this.r.kind == pb.e.h ? 0.0F : 1.0F, var8);
      float var12 = this.B.q(var10, var8);
      float var13 = this.d.q(this.r.kind == pb.e.h ? 14.0F : 20.0F, var8);
      if (!(var11 < 0.02F)) {
         float var14 = C(Math.clamp((float)(var6 - this.s) / 3.8E8F, 0.0F, 1.0F));
         float var15 = 0.0F;
         if (this.r.kind == pb.e.q) {
            float var16 = n8.S(this.r.text, 10.0) - (Math.round(var12) - 50);
            var15 = this.T.q(var16 <= 0.0F ? 0.0F : (float)Math.clamp(this.r.progress * (var16 + 30.0F) - 15.0, 0.0, var16), var8);
         }

         float var17 = Math.clamp((float)(var6 - this.s) / 5.2E8F, 0.0F, 1.0F);
         this.x(var1, var2, var3, var4, Math.max(12, Math.round(var12)), Math.max(8, Math.round(var13)), var11, var14, var17, var15, var6);
      }
   }

   public void v(GuiGraphicsExtractor var1, int var2, int var3, int var4, long var5) {
      pb.y var7 = this.r;
      this.r = a;

      try {
         this.x(var1, var2, var3, var4, Math.round(S(a, var4)), 20, 1.0F, 1.0F, 1.0F, 0.0F, var5);
      } finally {
         this.r = var7;
      }
   }

   private static float S(pb.y var0, int var1) {
      return Math.clamp(n8.S(var0.text, 10.0) + 62.0F, 110.0F, Math.max(110, var1));
   }

   private void x(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, float var10, long var11) {
      int var13 = var2 + (var4 - var5) / 2;
      int var14 = var3 + (20 - var6) / 2;
      int var15 = var6 / 2;

      try (oa var16 = oa.j(Math.clamp(var7, 0.0F, 1.0F))) {
         w1.h(var1, var13, var14, var5, var6, var15, 3.0F, 805306368, 805306368);
         w1.a(var1, var13, var14, var5, var6, var15, nc.U());
         var1.enableScissor(var13 + 2, var14, var13 + var5 - 2, var14 + var6);

         try {
            if (var8 < 1.0F && this.D.kind != pb.e.h) {
               try (oa var17 = oa.j((1.0F - var8) * 0.5)) {
                  int var18 = Math.round(var8 * 3.0F);
                  this.P(var1, this.D, var13 - var18, var14, var5, var6, var11, 0.0F, 1.0F);
                  this.P(var1, this.D, var13 + var18, var14, var5, var6, var11, 0.0F, 1.0F);
               }
            }

            try (oa var35 = oa.j(var8)) {
               if (this.r.kind == pb.e.h) {
                  double var36 = 0.5 + 0.5 * Math.sin(var11 / 9.0E8);
                  a(var1, var13 + var5 / 2.0F, var14 + var6 / 2.0F, 2.2F, vv.O(com.larpclient.c.y, 0.55 + 0.35 * var36));
               } else {
                  this.P(var1, this.r, var13, var14, var5, var6, var11, var10, var9);
               }
            }
         } finally {
            var1.disableScissor();
         }
      }
   }

   private void P(GuiGraphicsExtractor var1, pb.y var2, int var3, int var4, int var5, int var6, long var7, float var9, float var10) {
      float var11 = var4 + var6 / 2.0F;
      this.Z(var1, var3 + 12, var11);
      o(var1, var3 + var5 - 22, var11, var2.playing, var7);
      int var12 = var3 + 23;
      int var13 = var5 - 50;
      double var14 = n8.b(var4, var6, 10.0);
      if (var2.kind == pb.e.q) {
         var1.enableScissor(var12 - 1, var4, var12 + var13 + 1, var4 + var6);

         try {
            if (var10 < 1.0F) {
               x(var1, var2.text, var12 - var9, var14, var10);
            } else {
               i5.i(var1, var2.text, var12 - var9, var14, 10.0, -1250068);
            }
         } finally {
            var1.disableScissor();
         }
      } else {
         i5.i(var1, n8.I(var2.text, var13, 10.0), var12, var14, 10.0, -4671294);
      }
   }

   private static void x(GuiGraphicsExtractor var0, String var1, double var2, double var4, float var6) {
      float var7 = Math.max(1.0F, n8.S(var1, 10.0));
      float var8 = 0.0F;
      int var9 = 0;

      while (var9 < var1.length()) {
         int var10 = var1.codePointAt(var9);
         String var11 = new String(Character.toChars(var10));
         float var12 = n8.S(var11, 10.0);
         float var13 = Math.clamp((var6 - (var8 + var12 / 2.0F) / var7 * 0.5F) / 0.5F, 0.0F, 1.0F);
         if (var13 > 0.0F && !Character.isWhitespace(var10)) {
            try (oa var14 = oa.j(var13)) {
               i5.i(var0, var11, var2 + var8, var4 + 4.0F * (1.0F - C(var13)), 10.0, -1250068);
            }
         }

         var8 += var12;
         var9 += Character.charCount(var10);
      }
   }

   private void Z(GuiGraphicsExtractor var1, float var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2, var3).rotate((float)this.j);
         if (!n8.y(var1, "logo", -6.5, -6.5, 13.0, -1)) {
            h5.r(var1, R, -6, -6, 13, 13, 64, 64);
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   private static void o(GuiGraphicsExtractor var0, int var1, float var2, boolean var3, long var4) {
      double var6 = var4 / 1.0E9;

      for (int var8 = 0; var8 < 4; var8++) {
         float var9 = var3 ? (float)(3.0 + 5.0 * Math.abs(Math.sin(var6 * (2.6 + var8 * 1.1) + var8 * 1.7))) : 2.5F;
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1 + var8 * 3.5F, var2 - var9 / 2.0F);
            w1.a(var0, 0.0F, 0.0F, 2.0F, Math.max(2, Math.round(var9)), 1.0F, vv.O(com.larpclient.c.y, 0.95));
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void a(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2).scale(var3 / 8.0F, var3 / 8.0F);
         w1.a(var0, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F, var4);
      } finally {
         var0.pose().popMatrix();
      }
   }

   static float C(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   enum e {
      h,
      x,
      q;
   }

   static final class i {
      float Z;
      float T;
      final float B;
      final float g;

      i(float var1, float var2, float var3) {
         this.Z = var1;
         this.B = var2;
         this.g = var3;
      }

      float q(float var1, float var2) {
         int var3 = Math.max(1, (int)Math.ceil(var2 / 0.008F));
         float var4 = var2 / var3;

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = this.B * (var1 - this.Z) - this.g * this.T;
            this.T += var6 * var4;
            this.Z = this.Z + this.T * var4;
         }

         return this.Z;
      }

      void w(float var1) {
         this.Z = var1;
         this.T = 0.0F;
      }
   }

   record y(pb.e kind, String text, String key, boolean playing, double progress) {
      static final pb.y IDLE = new pb.y(pb.e.h, "", "i", false, 0.0);
   }
}
