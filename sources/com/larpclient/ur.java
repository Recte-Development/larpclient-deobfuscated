package com.larpclient;

import com.larpclient.module.GUIAnimationModule;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ur extends b {
   public String d = "";
   private final String e;
   private final int n;
   private boolean E;
   private final ef S = new ef(0.0);
   private final zn B = new zn();
   private final Consumer<String> I;
   private Runnable r;
   private Function<String, List<String>> s;
   private List<String> Y = List.of();
   private String a;
   private int t;
   private boolean c;
   private boolean z;
   public String u = "";
   public int U = -1;

   public ur t() {
      this.z = (boolean)1;
      return this;
   }

   public String M() {
      return this.z ? "*".repeat(this.d.codePointCount(0, this.d.length())) : this.d;
   }

   public ur B(Function<String, List<String>> var1) {
      this.s = var1;
      this.a = null;
      return this;
   }

   public String f() {
      if (!this.z && this.s != null && !this.d.isBlank()) {
         if (!this.d.equals(this.a)) {
            this.a = this.d;
            this.t = 0;
            this.c = (boolean)0;
            this.Y = this.s.apply(this.d).stream().filter(var1 -> var1.length() <= this.n).filter(var1 -> !var1.equalsIgnoreCase(this.d)).limit(6L).toList();
         }

         return !this.c && !this.Y.isEmpty() ? this.Y.get(this.t) : "";
      } else {
         return "";
      }
   }

   public boolean N() {
      if (!this.l) {
         return false;
      }

      String var1 = this.f();
      if (var1.isEmpty()) {
         return false;
      }

      this.d = var1;
      this.E = (boolean)0;
      this.c();
      this.c = (boolean)1;
      this.a = this.d;
      return true;
   }

   private void c() {
      this.a = null;
      this.c = (boolean)0;
      this.I.accept(this.d);
   }

   private boolean Y(int var1) {
      if (var1 == 262) {
         return this.N();
      }

      if ((var1 == 264 || var1 == 265 || var1 == 256) && !this.f().isEmpty()) {
         if (var1 == 256) {
            this.c = (boolean)1;
         } else {
            this.t = Math.floorMod(this.t + (var1 == 265 ? -1 : 1), this.Y.size());
         }

         return true;
      } else {
         return false;
      }
   }

   public void u() {
      this.E = (boolean)1;
   }

   public ur R(Runnable var1) {
      this.r = var1;
      return this;
   }

   public ur(String var1, Consumer<String> var2) {
      this(var1, var2, 64);
   }

   public ur(String var1, Consumer<String> var2, int var3) {
      this.n = var3;
      this.e = var1;
      this.I = var2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.S.m(this.l ? 1.0 : 0.0, 180);
      if (this.U < 0) {
         pn.S(var1, this.q, this.J, this.A, this.W, var4);
      } else {
         pn.p(var1, this.q, this.J, this.A, this.W, this.U, var4);
      }

      GUIAnimationModule var6 = GUIAnimationModule.F();
      String var7 = this.M();
      this.B.I(var7, System.nanoTime(), !this.z && this.l && var6 != null && var6.e());
      int var8 = this.J + (this.W - 9) / 2;
      int var9 = this.u.isEmpty() ? 6 : 26;
      if (!this.u.isEmpty()) {
         tp.N(var1, tp.j(this.u), this.q + 9, this.J + (this.W - 12) / 2, 12, _3.D(f.G, f.J, var4 * 0.6));
      }

      var1.enableScissor(this.q + var9, this.J + 2, this.q + this.A - 6, this.J + this.W - 2);

      try {
         if (this.d.isEmpty()) {
            k6.Z(var1, k6.j(this.e, this.A - var9 - 6), this.q + var9, var8, f.G);
         } else {
            int var10 = k6.P(var7);
            int var11 = this.q + var9 - Math.max(0, var10 + (this.l ? k6.P("_") : 0) - (this.A - var9 - 6));
            if (!this.B.h()) {
               k6.Z(var1, var7, var11, var8, f.J);
            } else {
               int var12 = 0;

               while (var12 < var7.length()) {
                  int var13 = var7.codePointAt(var12);
                  String var14 = new String(Character.toChars(var13));
                  int var15 = var11 + k6.P(var7.substring(0, var12));
                  if (var15 + k6.P(var14) >= this.q + var9 && var15 < this.q + this.A - 6) {
                     var1.pose().pushMatrix();
                     var1.pose().translate(0.0F, (float)this.B.z(var12));

                     try {
                        k6.Z(var1, var14, var15, var8, this.B.V(f.J, var12));
                     } finally {
                        var1.pose().popMatrix();
                     }
                  }

                  var12 += Character.charCount(var13);
               }
            }

            if (this.l) {
               String var24 = this.f();
               if (!var24.isEmpty()) {
                  if (var24.regionMatches(true, 0, this.d, 0, this.d.length())) {
                     k6.Z(var1, var24.substring(this.d.length()), var11 + var10, var8, _3.U(f.s, 0.65));
                  } else {
                     String var25 = "Tab: " + var24;
                     int var26 = k6.a(var25, k6.d.K);
                     if (var10 + var26 + 18 <= this.A) {
                        k6.T(var1, var25, this.q + this.A - var26 - 6, var8, f.s, k6.d.K);
                     }
                  }
               }

               c7.s(var1, var11 + var10, var8, 1, 9, f.J);
            }
         }
      } finally {
         var1.disableScissor();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      this.E = (boolean)0;
      this.l = (boolean)(var5 == 0 && this.contains(var1, var3) ? 1 : 0);
      return this.l;
   }

   @Override
   public boolean character(int var1) {
      if (this.l && !Character.isISOControl(var1)) {
         if (this.E) {
            this.d = "";
            this.E = (boolean)0;
         }

         if (this.d.length() + Character.charCount(var1) > this.n) {
            return false;
         }

         this.d = this.d + new String(Character.toChars(var1));
         this.c();
         kb.d(kb.n.C);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.l) {
         return false;
      }

      if (this.Y(var1)) {
         return true;
      }

      if ((var1 == 257 || var1 == 335) && this.r != null) {
         this.r.run();
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      long var3 = var2.getWindow().handle();
      int var5 = hy.z(var3, 341) != 1 && hy.z(var3, 345) != 1 ? 0 : 1;
      if (var5 != 0 && var1 == 86) {
         this.f(var2.keyboardHandler.getClipboard());
      } else if (var5 != 0 && var1 == 65) {
         this.E = (boolean)1;
      } else if (var1 == 259 && !this.d.isEmpty()) {
         kb.d(kb.n.C);
         if (this.E) {
            this.d = "";
            this.E = (boolean)0;
            this.c();
            return true;
         }

         this.d = this.d.substring(0, this.d.offsetByCodePoints(this.d.length(), -1));
         this.c();
      } else if (var1 == 256) {
         this.l = (boolean)0;
      }

      return true;
   }

   public void f(String var1) {
      if (this.l && var1 != null) {
         String var2 = var1.replaceAll("[\\p{Cntrl}]", " ");
         if (this.E) {
            this.d = "";
            this.E = (boolean)0;
         }

         int var3 = Math.min(var2.length(), Math.max(0, this.n - this.d.length()));
         if (var3 > 0 && var3 < var2.length() && Character.isHighSurrogate(var2.charAt(var3 - 1)) && Character.isLowSurrogate(var2.charAt(var3))) {
            var3--;
         }

         this.d = this.d + var2.substring(0, var3);
         this.c();
         kb.d(kb.n.C);
      }
   }
}
