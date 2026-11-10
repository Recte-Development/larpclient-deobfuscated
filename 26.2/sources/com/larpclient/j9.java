package com.larpclient;

import com.larpclient.module.GUIAnimationModule;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.lwjgl.glfw.GLFW;

public final class j9 extends jd {
   public String H = "";
   private final String m;
   private final int a;
   private boolean Q;
   private final jp b = new jp(0.0);
   private final z3 k = new z3();
   private final Consumer<String> o;
   private Runnable I;
   private Function<String, List<String>> Y;
   private List<String> B = List.of();
   private String l;
   private int f;
   private boolean j;
   private boolean V;
   public String X = "";
   public int z = -1;

   public j9 u() {
      this.V = (boolean)1;
      return this;
   }

   public String R() {
      return this.V ? "*".repeat(this.H.codePointCount(0, this.H.length())) : this.H;
   }

   public j9 b(Function<String, List<String>> var1) {
      this.Y = var1;
      this.l = null;
      return this;
   }

   public String m() {
      if (!this.V && this.Y != null && !this.H.isBlank()) {
         if (!this.H.equals(this.l)) {
            this.l = this.H;
            this.f = 0;
            this.j = (boolean)0;
            this.B = this.Y.apply(this.H).stream().filter(var1 -> var1.length() <= this.a).filter(var1 -> !var1.equalsIgnoreCase(this.H)).limit(6L).toList();
         }

         return !this.j && !this.B.isEmpty() ? this.B.get(this.f) : "";
      } else {
         return "";
      }
   }

   public boolean l() {
      if (!this.T) {
         return false;
      }

      String var1 = this.m();
      if (var1.isEmpty()) {
         return false;
      }

      this.H = var1;
      this.Q = (boolean)0;
      this.o();
      this.j = (boolean)1;
      this.l = this.H;
      return true;
   }

   private void o() {
      this.l = null;
      this.j = (boolean)0;
      this.o.accept(this.H);
   }

   private boolean j(int var1) {
      if (var1 == 262) {
         return this.l();
      }

      if ((var1 == 264 || var1 == 265 || var1 == 256) && !this.m().isEmpty()) {
         if (var1 == 256) {
            this.j = (boolean)1;
         } else {
            this.f = Math.floorMod(this.f + (var1 == 265 ? -1 : 1), this.B.size());
         }

         return true;
      } else {
         return false;
      }
   }

   public void j() {
      this.Q = (boolean)1;
   }

   public j9 B(Runnable var1) {
      this.I = var1;
      return this;
   }

   public j9(String var1, Consumer<String> var2) {
      this(var1, var2, 64);
   }

   public j9(String var1, Consumer<String> var2, int var3) {
      this.a = var3;
      this.m = var1;
      this.o = var2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.b.k(this.T ? 1.0 : 0.0, 180);
      if (this.z < 0) {
         wp.u(var1, this.S, this.u, this.A, this.J, var4);
      } else {
         wp.s(var1, this.S, this.u, this.A, this.J, this.z, var4);
      }

      GUIAnimationModule var6 = GUIAnimationModule.a();
      String var7 = this.R();
      this.k.t(var7, System.nanoTime(), !this.V && this.T && var6 != null && var6.b());
      int var8 = this.u + (this.J - 9) / 2;
      int var9 = this.X.isEmpty() ? 6 : 26;
      if (!this.X.isEmpty()) {
         ur.O(var1, ur.V(this.X), this.S + 9, this.u + (this.J - 12) / 2, 12, uh.q(ho.j, ho.u, var4 * 0.6));
      }

      var1.enableScissor(this.S + var9, this.u + 2, this.S + this.A - 6, this.u + this.J - 2);

      try {
         if (this.H.isEmpty()) {
            bj.E(var1, bj.T(this.m, this.A - var9 - 6), this.S + var9, var8, ho.j);
         } else {
            int var10 = bj.u(var7);
            int var11 = this.S + var9 - Math.max(0, var10 + (this.T ? bj.u("_") : 0) - (this.A - var9 - 6));
            if (!this.k.h()) {
               bj.E(var1, var7, var11, var8, ho.u);
            } else {
               int var12 = 0;

               while (var12 < var7.length()) {
                  int var13 = var7.codePointAt(var12);
                  String var14 = new String(Character.toChars(var13));
                  int var15 = var11 + bj.u(var7.substring(0, var12));
                  if (var15 + bj.u(var14) >= this.S + var9 && var15 < this.S + this.A - 6) {
                     var1.pose().pushMatrix();
                     var1.pose().translate(0.0F, (float)this.k.A(var12));

                     try {
                        bj.E(var1, var14, var15, var8, this.k.B(ho.u, var12));
                     } finally {
                        var1.pose().popMatrix();
                     }
                  }

                  var12 += Character.charCount(var13);
               }
            }

            if (this.T) {
               String var24 = this.m();
               if (!var24.isEmpty()) {
                  if (var24.regionMatches(true, 0, this.H, 0, this.H.length())) {
                     bj.E(var1, var24.substring(this.H.length()), var11 + var10, var8, uh.F(ho.Z, 0.65));
                  } else {
                     String var25 = "Tab: " + var24;
                     int var26 = bj.e(var25, bj.u.M);
                     if (var10 + var26 + 18 <= this.A) {
                        bj.k(var1, var25, this.S + this.A - var26 - 6, var8, ho.Z, bj.u.M);
                     }
                  }
               }

               kv.d(var1, var11 + var10, var8, 1, 9, ho.u);
            }
         }
      } finally {
         var1.disableScissor();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      this.Q = (boolean)0;
      this.T = (boolean)(var5 == 0 && this.contains(var1, var3) ? 1 : 0);
      return this.T;
   }

   @Override
   public boolean character(int var1) {
      if (this.T && !Character.isISOControl(var1)) {
         if (this.Q) {
            this.H = "";
            this.Q = (boolean)0;
         }

         if (this.H.length() + Character.charCount(var1) > this.a) {
            return false;
         }

         this.H = this.H + new String(Character.toChars(var1));
         this.o();
         sr.v(sr.e.g);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.T) {
         return false;
      }

      if (this.j(var1)) {
         return true;
      }

      if ((var1 == 257 || var1 == 335) && this.I != null) {
         this.I.run();
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      long var3 = var2.getWindow().handle();
      int var5 = GLFW.glfwGetKey(var3, 341) != 1 && GLFW.glfwGetKey(var3, 345) != 1 ? 0 : 1;
      if (var5 != 0 && var1 == 86) {
         this.X(var2.keyboardHandler.getClipboard());
      } else if (var5 != 0 && var1 == 65) {
         this.Q = (boolean)1;
      } else if (var1 == 259 && !this.H.isEmpty()) {
         sr.v(sr.e.g);
         if (this.Q) {
            this.H = "";
            this.Q = (boolean)0;
            this.o();
            return true;
         }

         this.H = this.H.substring(0, this.H.offsetByCodePoints(this.H.length(), -1));
         this.o();
      } else if (var1 == 256) {
         this.T = (boolean)0;
      }

      return true;
   }

   public void X(String var1) {
      if (this.T && var1 != null) {
         String var2 = var1.replaceAll("[\\p{Cntrl}]", " ");
         if (this.Q) {
            this.H = "";
            this.Q = (boolean)0;
         }

         int var3 = Math.min(var2.length(), Math.max(0, this.a - this.H.length()));
         if (var3 > 0 && var3 < var2.length() && Character.isHighSurrogate(var2.charAt(var3 - 1)) && Character.isLowSurrogate(var2.charAt(var3))) {
            var3--;
         }

         this.H = this.H + var2.substring(0, var3);
         this.o();
         sr.v(sr.e.g);
      }
   }
}
