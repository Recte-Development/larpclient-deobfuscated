package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_437;

public abstract class t1 extends class_437 implements tp {
   protected final List<lc> K = new ArrayList<>();
   protected lc G;
   protected lc y;

   protected t1(String var1) {
      super(class_2561.method_43470(var1));
   }

   protected void t() {
      for (lc var2 : this.K) {
         var2.blur();
      }

      this.K.clear();
      this.G = this.y = null;
   }

   protected boolean accepts(lc var1, double var2, double var4) {
      return var1.contains(var2, var4);
   }

   public boolean method_25402(class_11909 var1, boolean var2) {
      if (this.G != null && this.G.captureMouse(s2.m(var1))) {
         return true;
      }

      for (lc var4 : this.K) {
         if (this.accepts(var4, var1.comp_4798(), var1.comp_4799())) {
            if (this.G != null && this.G != var4) {
               this.G.blur();
            }

            this.G = var4;
            var4.M = (boolean)1;
            var4.q = (boolean)0;
            if (var4.click(var1.comp_4798(), var1.comp_4799(), s2.m(var1))) {
               this.y = var4;
               return true;
            }
         }
      }

      if (this.G != null) {
         this.G.blur();
      }

      this.G = null;
      return false;
   }

   public boolean method_25403(class_11909 var1, double var2, double var4) {
      if (this.y != null) {
         this.y.drag(var1.comp_4798(), var1.comp_4799());
         return true;
      } else {
         return false;
      }
   }

   public boolean method_25406(class_11909 var1) {
      if (this.y != null) {
         this.y.release();
      }

      this.y = null;
      return true;
   }

   public boolean method_25404(class_11908 var1) {
      if (s2.i(var1) != 258) {
         return this.G != null && this.G.key(s2.i(var1)) ? true : super.method_25404(var1);
      }

      int var2 = (var1.comp_4797() & 1) != 0 ? -1 : 1;
      int var3 = this.G == null ? (var2 > 0 ? -1 : 0) : this.K.indexOf(this.G);
      if (this.G != null) {
         this.G.blur();
      }

      for (int var4 = 0; var4 < this.K.size(); var4++) {
         var3 = Math.floorMod(var3 + var2, this.K.size());
         lc var5 = this.K.get(var3);
         if (var5.N && var5.I) {
            this.G = var5;
            var5.M = (boolean)1;
            var5.q = (boolean)1;
            this.focusChanged(var5);
            break;
         }
      }

      return true;
   }

   protected void focusChanged(lc var1) {
   }

   public boolean method_25400(class_11905 var1) {
      return this.G != null && this.G.character(var1.comp_4793());
   }

   public boolean method_25421() {
      return false;
   }
}
