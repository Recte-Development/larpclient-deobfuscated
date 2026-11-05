package com.larpclient;

import com.larpclient.module.GUIAnimationModule;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class p_ extends oq {
   public String V = "";
   private final String f;
   private final int H;
   private boolean w;
   private final u7 R = new u7(0.0);
   private final lt a = new lt();
   private final Consumer<String> P;
   private Runnable p;
   private Function<String, List<String>> X;
   private List<String> J = List.of();
   private String T;
   private int C;
   private boolean D;
   private boolean m;
   public String S = "";
   public int U = -1;

   public p_ h() {
      this.m = (boolean)1;
      return this;
   }

   public String o() {
      return this.m ? "*".repeat(this.V.codePointCount(0, this.V.length())) : this.V;
   }

   public p_ Y(Function<String, List<String>> var1) {
      this.X = var1;
      this.T = null;
      return this;
   }

   public String A() {
      if (!this.m && this.X != null && !this.V.isBlank()) {
         if (!this.V.equals(this.T)) {
            this.T = this.V;
            this.C = 0;
            this.D = (boolean)0;
            this.J = this.X.apply(this.V).stream().filter(var1 -> var1.length() <= this.H).filter(var1 -> !var1.equalsIgnoreCase(this.V)).limit(6L).toList();
         }

         return !this.D && !this.J.isEmpty() ? this.J.get(this.C) : "";
      } else {
         return "";
      }
   }

   public boolean q() {
      if (!this.A) {
         return false;
      }

      String var1 = this.A();
      if (var1.isEmpty()) {
         return false;
      }

      this.V = var1;
      this.w = (boolean)0;
      this.k();
      this.D = (boolean)1;
      this.T = this.V;
      return true;
   }

   private void k() {
      this.T = null;
      this.D = (boolean)0;
      this.P.accept(this.V);
   }

   private boolean f(int var1) {
      if (var1 == 262) {
         return this.q();
      }

      if ((var1 == 264 || var1 == 265 || var1 == 256) && !this.A().isEmpty()) {
         if (var1 == 256) {
            this.D = (boolean)1;
         } else {
            this.C = Math.floorMod(this.C + (var1 == 265 ? -1 : 1), this.J.size());
         }

         return true;
      } else {
         return false;
      }
   }

   public void d() {
      this.w = (boolean)1;
   }

   public p_ T(Runnable var1) {
      this.p = var1;
      return this;
   }

   public p_(String var1, Consumer<String> var2) {
      this(var1, var2, 64);
   }

   public p_(String var1, Consumer<String> var2, int var3) {
      this.H = var3;
      this.f = var1;
      this.P = var2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.R.y(this.A ? 1.0 : 0.0, 180);
      if (this.U < 0) {
         ll.j(var1, this.B, this.L, this.v, this.O, var4);
      } else {
         ll.P(var1, this.B, this.L, this.v, this.O, this.U, var4);
      }

      GUIAnimationModule var6 = GUIAnimationModule.s();
      String var7 = this.o();
      this.a.p(var7, System.nanoTime(), !this.m && this.A && var6 != null && var6.n());
      int var8 = this.L + (this.O - 9) / 2;
      int var9 = this.S.isEmpty() ? 6 : 26;
      if (!this.S.isEmpty()) {
         ou.u(var1, ou.e(this.S), this.B + 9, this.L + (this.O - 12) / 2, 12, oh.j(rv.r, rv.P, var4 * 0.6));
      }

      var1.enableScissor(this.B + var9, this.L + 2, this.B + this.v - 6, this.L + this.O - 2);

      try {
         if (this.V.isEmpty()) {
            j_.E(var1, j_.S(this.f, this.v - var9 - 6), this.B + var9, var8, rv.r);
         } else {
            int var10 = j_.a(var7);
            int var11 = this.B + var9 - Math.max(0, var10 + (this.A ? j_.a("_") : 0) - (this.v - var9 - 6));
            if (!this.a.n()) {
               j_.E(var1, var7, var11, var8, rv.P);
            } else {
               int var12 = 0;

               while (var12 < var7.length()) {
                  int var13 = var7.codePointAt(var12);
                  String var14 = new String(Character.toChars(var13));
                  int var15 = var11 + j_.a(var7.substring(0, var12));
                  if (var15 + j_.a(var14) >= this.B + var9 && var15 < this.B + this.v - 6) {
                     var1.pose().pushMatrix();
                     var1.pose().translate(0.0F, (float)this.a.O(var12));

                     try {
                        j_.E(var1, var14, var15, var8, this.a.X(rv.P, var12));
                     } finally {
                        var1.pose().popMatrix();
                     }
                  }

                  var12 += Character.charCount(var13);
               }
            }

            if (this.A) {
               String var24 = this.A();
               if (!var24.isEmpty()) {
                  if (var24.regionMatches(true, 0, this.V, 0, this.V.length())) {
                     j_.E(var1, var24.substring(this.V.length()), var11 + var10, var8, oh.S(rv.G0, 0.65));
                  } else {
                     String var25 = "Tab: " + var24;
                     int var26 = j_.o(var25, j_.m.U);
                     if (var10 + var26 + 18 <= this.v) {
                        j_.L(var1, var25, this.B + this.v - var26 - 6, var8, rv.G0, j_.m.U);
                     }
                  }
               }

               fr.k(var1, var11 + var10, var8, 1, 9, rv.P);
            }
         }
      } finally {
         var1.disableScissor();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      this.w = (boolean)0;
      this.A = (boolean)(var5 == 0 && this.contains(var1, var3) ? 1 : 0);
      return this.A;
   }

   @Override
   public boolean character(int var1) {
      if (this.A && !Character.isISOControl(var1)) {
         if (this.w) {
            this.V = "";
            this.w = (boolean)0;
         }

         if (this.V.length() + Character.charCount(var1) > this.H) {
            return false;
         }

         this.V = this.V + new String(Character.toChars(var1));
         this.k();
         hx.w(hx.t.i);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.A) {
         return false;
      }

      if (this.f(var1)) {
         return true;
      }

      if ((var1 == 257 || var1 == 335) && this.p != null) {
         this.p.run();
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      long var3 = var2.getWindow().handle();
      int var5 = eo.F(var3, 341) != 1 && eo.F(var3, 345) != 1 ? 0 : 1;
      if (var5 != 0 && var1 == 86) {
         this.E(var2.keyboardHandler.getClipboard());
      } else if (var5 != 0 && var1 == 65) {
         this.w = (boolean)1;
      } else if (var1 == 259 && !this.V.isEmpty()) {
         hx.w(hx.t.i);
         if (this.w) {
            this.V = "";
            this.w = (boolean)0;
            this.k();
            return true;
         }

         this.V = this.V.substring(0, this.V.offsetByCodePoints(this.V.length(), -1));
         this.k();
      } else if (var1 == 256) {
         this.A = (boolean)0;
      }

      return true;
   }

   public void E(String var1) {
      if (this.A && var1 != null) {
         String var2 = var1.replaceAll("[\\p{Cntrl}]", " ");
         if (this.w) {
            this.V = "";
            this.w = (boolean)0;
         }

         int var3 = Math.min(var2.length(), Math.max(0, this.H - this.V.length()));
         if (var3 > 0 && var3 < var2.length() && Character.isHighSurrogate(var2.charAt(var3 - 1)) && Character.isLowSurrogate(var2.charAt(var3))) {
            var3--;
         }

         this.V = this.V + var2.substring(0, var3);
         this.k();
         hx.w(hx.t.i);
      }
   }
}
