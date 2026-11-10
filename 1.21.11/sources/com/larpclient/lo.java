package com.larpclient;

import com.larpclient.module.GUIAnimationModule;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.class_310;
import net.minecraft.class_332;
import org.lwjgl.glfw.GLFW;

public final class lo extends lc {
   public String h = "";
   private final String o;
   private final int V;
   private boolean Q;
   private final qb L = new qb(0.0);
   private final bp i = new bp();
   private final Consumer<String> K;
   private Runnable O;
   private Function<String, List<String>> e;
   private List<String> T = List.of();
   private String k;
   private int t;
   private boolean A;
   private boolean S;
   public String R = "";
   public int H = -1;

   public lo d() {
      this.S = (boolean)1;
      return this;
   }

   public String e() {
      return this.S ? "*".repeat(this.h.codePointCount(0, this.h.length())) : this.h;
   }

   public lo G(Function<String, List<String>> var1) {
      this.e = var1;
      this.k = null;
      return this;
   }

   public String J() {
      if (!this.S && this.e != null && !this.h.isBlank()) {
         if (!this.h.equals(this.k)) {
            this.k = this.h;
            this.t = 0;
            this.A = (boolean)0;
            this.T = this.e.apply(this.h).stream().filter(var1 -> var1.length() <= this.V).filter(var1 -> !var1.equalsIgnoreCase(this.h)).limit(6L).toList();
         }

         return !this.A && !this.T.isEmpty() ? this.T.get(this.t) : "";
      } else {
         return "";
      }
   }

   public boolean T() {
      if (!this.M) {
         return false;
      }

      String var1 = this.J();
      if (var1.isEmpty()) {
         return false;
      }

      this.h = var1;
      this.Q = (boolean)0;
      this.t();
      this.A = (boolean)1;
      this.k = this.h;
      return true;
   }

   private void t() {
      this.k = null;
      this.A = (boolean)0;
      this.K.accept(this.h);
   }

   private boolean j(int var1) {
      if (var1 == 262) {
         return this.T();
      }

      if ((var1 == 264 || var1 == 265 || var1 == 256) && !this.J().isEmpty()) {
         if (var1 == 256) {
            this.A = (boolean)1;
         } else {
            this.t = Math.floorMod(this.t + (var1 == 265 ? -1 : 1), this.T.size());
         }

         return true;
      } else {
         return false;
      }
   }

   public void T() {
      this.Q = (boolean)1;
   }

   public lo T(Runnable var1) {
      this.O = var1;
      return this;
   }

   public lo(String var1, Consumer<String> var2) {
      this(var1, var2, 64);
   }

   public lo(String var1, Consumer<String> var2, int var3) {
      this.V = var3;
      this.o = var1;
      this.K = var2;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      double var4 = this.L.a(this.M ? 1.0 : 0.0, 180);
      if (this.H < 0) {
         p.x(var1, this.D, this.g, this.d, this.w, var4);
      } else {
         p.j(var1, this.D, this.g, this.d, this.w, this.H, var4);
      }

      GUIAnimationModule var6 = GUIAnimationModule.Z();
      String var7 = this.e();
      this.i.r(var7, System.nanoTime(), !this.S && this.M && var6 != null && var6.r());
      int var8 = this.g + (this.w - 9) / 2;
      int var9 = this.R.isEmpty() ? 6 : 26;
      if (!this.R.isEmpty()) {
         lx.N(var1, lx.n(this.R), this.D + 9, this.g + (this.w - 12) / 2, 12, ei.W(ez.t, ez.H, var4 * 0.6));
      }

      var1.method_44379(this.D + var9, this.g + 2, this.D + this.d - 6, this.g + this.w - 2);

      try {
         if (this.h.isEmpty()) {
            uw.G(var1, uw.J(this.o, this.d - var9 - 6), this.D + var9, var8, ez.t);
         } else {
            int var10 = uw.r(var7);
            int var11 = this.D + var9 - Math.max(0, var10 + (this.M ? uw.r("_") : 0) - (this.d - var9 - 6));
            if (!this.i.q()) {
               uw.G(var1, var7, var11, var8, ez.H);
            } else {
               int var12 = 0;

               while (var12 < var7.length()) {
                  int var13 = var7.codePointAt(var12);
                  String var14 = new String(Character.toChars(var13));
                  int var15 = var11 + uw.r(var7.substring(0, var12));
                  if (var15 + uw.r(var14) >= this.D + var9 && var15 < this.D + this.d - 6) {
                     var1.method_51448().pushMatrix();
                     var1.method_51448().translate(0.0F, (float)this.i.j(var12));

                     try {
                        uw.G(var1, var14, var15, var8, this.i.d(ez.H, var12));
                     } finally {
                        var1.method_51448().popMatrix();
                     }
                  }

                  var12 += Character.charCount(var13);
               }
            }

            if (this.M) {
               String var24 = this.J();
               if (!var24.isEmpty()) {
                  if (var24.regionMatches(true, 0, this.h, 0, this.h.length())) {
                     uw.G(var1, var24.substring(this.h.length()), var11 + var10, var8, ei.R(ez.PH, 0.65));
                  } else {
                     String var25 = "Tab: " + var24;
                     int var26 = uw.C(var25, uw.c.M);
                     if (var10 + var26 + 18 <= this.d) {
                        uw.X(var1, var25, this.D + this.d - var26 - 6, var8, ez.PH, uw.c.M);
                     }
                  }
               }

               gu.Z(var1, var11 + var10, var8, 1, 9, ez.H);
            }
         }
      } finally {
         var1.method_44380();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      this.Q = (boolean)0;
      this.M = (boolean)(var5 == 0 && this.contains(var1, var3) ? 1 : 0);
      return this.M;
   }

   @Override
   public boolean character(int var1) {
      if (this.M && !Character.isISOControl(var1)) {
         if (this.Q) {
            this.h = "";
            this.Q = (boolean)0;
         }

         if (this.h.length() + Character.charCount(var1) > this.V) {
            return false;
         }

         this.h = this.h + new String(Character.toChars(var1));
         this.t();
         c_.b(c_.d.Q);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.M) {
         return false;
      }

      if (this.j(var1)) {
         return true;
      }

      if ((var1 == 257 || var1 == 335) && this.O != null) {
         this.O.run();
         return true;
      }

      class_310 var2 = class_310.method_1551();
      long var3 = var2.method_22683().method_4490();
      int var5 = GLFW.glfwGetKey(var3, 341) != 1 && GLFW.glfwGetKey(var3, 345) != 1 ? 0 : 1;
      if (var5 != 0 && var1 == 86) {
         this.v(var2.field_1774.method_1460());
      } else if (var5 != 0 && var1 == 65) {
         this.Q = (boolean)1;
      } else if (var1 == 259 && !this.h.isEmpty()) {
         c_.b(c_.d.Q);
         if (this.Q) {
            this.h = "";
            this.Q = (boolean)0;
            this.t();
            return true;
         }

         this.h = this.h.substring(0, this.h.offsetByCodePoints(this.h.length(), -1));
         this.t();
      } else if (var1 == 256) {
         this.M = (boolean)0;
      }

      return true;
   }

   public void v(String var1) {
      if (this.M && var1 != null) {
         String var2 = var1.replaceAll("[\\p{Cntrl}]", " ");
         if (this.Q) {
            this.h = "";
            this.Q = (boolean)0;
         }

         int var3 = Math.min(var2.length(), Math.max(0, this.V - this.h.length()));
         if (var3 > 0 && var3 < var2.length() && Character.isHighSurrogate(var2.charAt(var3 - 1)) && Character.isLowSurrogate(var2.charAt(var3))) {
            var3--;
         }

         this.h = this.h + var2.substring(0, var3);
         this.t();
         c_.b(c_.d.Q);
      }
   }
}
