package com.larpclient;

import com.larpclient.module.GUIAnimationModule;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class sk extends j0 {
   public String j = "";
   private final String S;
   private final int Z;
   private boolean f;
   private final fq k = new fq(0.0);
   private final ar Q = new ar();
   private final Consumer<String> q;
   private Runnable v;
   private Function<String, List<String>> A;
   private List<String> U = List.of();
   private String d;
   private int K;
   private boolean m;
   private boolean t;
   public String o = "";
   public int J = -1;

   public sk m() {
      this.t = (boolean)1;
      return this;
   }

   public String A() {
      return this.t ? "*".repeat(this.j.codePointCount(0, this.j.length())) : this.j;
   }

   public sk z(Function<String, List<String>> var1) {
      this.A = var1;
      this.d = null;
      return this;
   }

   public String G() {
      if (!this.t && this.A != null && !this.j.isBlank()) {
         if (!this.j.equals(this.d)) {
            this.d = this.j;
            this.K = 0;
            this.m = (boolean)0;
            this.U = this.A.apply(this.j).stream().filter(var1 -> var1.length() <= this.Z).filter(var1 -> !var1.equalsIgnoreCase(this.j)).limit(6L).toList();
         }

         return !this.m && !this.U.isEmpty() ? this.U.get(this.K) : "";
      } else {
         return "";
      }
   }

   public boolean d() {
      if (!this.F) {
         return false;
      }

      String var1 = this.G();
      if (var1.isEmpty()) {
         return false;
      }

      this.j = var1;
      this.f = (boolean)0;
      this.B();
      this.m = (boolean)1;
      this.d = this.j;
      return true;
   }

   private void B() {
      this.d = null;
      this.m = (boolean)0;
      this.q.accept(this.j);
   }

   private boolean C(int var1) {
      if (var1 == 262) {
         return this.d();
      }

      if ((var1 == 264 || var1 == 265 || var1 == 256) && !this.G().isEmpty()) {
         if (var1 == 256) {
            this.m = (boolean)1;
         } else {
            this.K = Math.floorMod(this.K + (var1 == 265 ? -1 : 1), this.U.size());
         }

         return true;
      } else {
         return false;
      }
   }

   public void d() {
      this.f = (boolean)1;
   }

   public sk O(Runnable var1) {
      this.v = var1;
      return this;
   }

   public sk(String var1, Consumer<String> var2) {
      this(var1, var2, 64);
   }

   public sk(String var1, Consumer<String> var2, int var3) {
      this.Z = var3;
      this.S = var1;
      this.q = var2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.k.n(this.F ? 1.0 : 0.0, 180);
      if (this.J < 0) {
         wc.x(var1, this.P, this.T, this.L, this.O, var4);
      } else {
         wc.A(var1, this.P, this.T, this.L, this.O, this.J, var4);
      }

      GUIAnimationModule var6 = GUIAnimationModule.O();
      String var7 = this.A();
      this.Q.l(var7, System.nanoTime(), !this.t && this.F && var6 != null && var6.I());
      int var8 = this.T + (this.O - 9) / 2;
      int var9 = this.o.isEmpty() ? 6 : 26;
      if (!this.o.isEmpty()) {
         wz.D(var1, wz.M(this.o), this.P + 9, this.T + (this.O - 12) / 2, 12, vv.Q(c.f, c.H, var4 * 0.6));
      }

      var1.enableScissor(this.P + var9, this.T + 2, this.P + this.L - 6, this.T + this.O - 2);

      try {
         if (this.j.isEmpty()) {
            ic.N(var1, ic.U(this.S, this.L - var9 - 6), this.P + var9, var8, c.f);
         } else {
            int var10 = ic.H(var7);
            int var11 = this.P + var9 - Math.max(0, var10 + (this.F ? ic.H("_") : 0) - (this.L - var9 - 6));
            if (!this.Q.l()) {
               ic.N(var1, var7, var11, var8, c.H);
            } else {
               int var12 = 0;

               while (var12 < var7.length()) {
                  int var13 = var7.codePointAt(var12);
                  String var14 = new String(Character.toChars(var13));
                  int var15 = var11 + ic.H(var7.substring(0, var12));
                  if (var15 + ic.H(var14) >= this.P + var9 && var15 < this.P + this.L - 6) {
                     var1.pose().pushMatrix();
                     var1.pose().translate(0.0F, (float)this.Q.i(var12));

                     try {
                        ic.N(var1, var14, var15, var8, this.Q.B(c.H, var12));
                     } finally {
                        var1.pose().popMatrix();
                     }
                  }

                  var12 += Character.charCount(var13);
               }
            }

            if (this.F) {
               String var24 = this.G();
               if (!var24.isEmpty()) {
                  if (var24.regionMatches(true, 0, this.j, 0, this.j.length())) {
                     ic.N(var1, var24.substring(this.j.length()), var11 + var10, var8, vv.O(c.W, 0.65));
                  } else {
                     String var25 = "Tab: " + var24;
                     int var26 = ic.f(var25, ic.n.I);
                     if (var10 + var26 + 18 <= this.L) {
                        ic.N(var1, var25, this.P + this.L - var26 - 6, var8, c.W, ic.n.I);
                     }
                  }
               }

               h5.N(var1, var11 + var10, var8, 1, 9, c.H);
            }
         }
      } finally {
         var1.disableScissor();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      this.f = (boolean)0;
      this.F = (boolean)(var5 == 0 && this.contains(var1, var3) ? 1 : 0);
      return this.F;
   }

   @Override
   public boolean character(int var1) {
      if (this.F && !Character.isISOControl(var1)) {
         if (this.f) {
            this.j = "";
            this.f = (boolean)0;
         }

         if (this.j.length() + Character.charCount(var1) > this.Z) {
            return false;
         }

         this.j = this.j + new String(Character.toChars(var1));
         this.B();
         ft.h(ft.e.t);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.F) {
         return false;
      }

      if (this.C(var1)) {
         return true;
      }

      if ((var1 == 257 || var1 == 335) && this.v != null) {
         this.v.run();
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      long var3 = var2.getWindow().handle();
      int var5 = n2.R(var3, 341) != 1 && n2.R(var3, 345) != 1 ? 0 : 1;
      if (var5 != 0 && var1 == 86) {
         this.z(var2.keyboardHandler.getClipboard());
      } else if (var5 != 0 && var1 == 65) {
         this.f = (boolean)1;
      } else if (var1 == 259 && !this.j.isEmpty()) {
         ft.h(ft.e.t);
         if (this.f) {
            this.j = "";
            this.f = (boolean)0;
            this.B();
            return true;
         }

         this.j = this.j.substring(0, this.j.offsetByCodePoints(this.j.length(), -1));
         this.B();
      } else if (var1 == 256) {
         this.F = (boolean)0;
      }

      return true;
   }

   public void z(String var1) {
      if (this.F && var1 != null) {
         String var2 = var1.replaceAll("[\\p{Cntrl}]", " ");
         if (this.f) {
            this.j = "";
            this.f = (boolean)0;
         }

         int var3 = Math.min(var2.length(), Math.max(0, this.Z - this.j.length()));
         if (var3 > 0 && var3 < var2.length() && Character.isHighSurrogate(var2.charAt(var3 - 1)) && Character.isLowSurrogate(var2.charAt(var3))) {
            var3--;
         }

         this.j = this.j + var2.substring(0, var3);
         this.B();
         ft.h(ft.e.t);
      }
   }
}
