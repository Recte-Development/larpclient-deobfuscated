package com.larpclient;

import com.larpclient.module.AutoTotemModule;
import com.larpclient.module.LungeSwapModule;
import com.larpclient.module.Module;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class fs {
   private static fs N;
   private final Module a;
   private final ea p = new ea();
   private class_746 k;
   private Object R;

   public fs(Module var1) {
      this.a = var1;
   }

   public boolean I() {
      return this.p.B();
   }

   public boolean N() {
      return this.I() && this.G() && N == this && this.p.F(this.k.method_31548().method_67532());
   }

   public static boolean o(Module var0) {
      if (N != null && !N.G()) {
         N.o();
      }

      if ((N == null || N.a == var0) && !AutoTotemModule.A()) {
         LarpClient var1 = LarpClient.b();
         LungeSwapModule var2 = var1 != null && var1.G() != null ? var1.G().b(LungeSwapModule.class) : null;
         return var2 == null || var2 == var0 || !var2.k();
      } else {
         return false;
      }
   }

   public boolean m(class_746 var1, int var2) {
      if (var2 >= 0 && var2 < 9 && var1 != null && o(this.a)) {
         class_310 var3 = class_310.method_1551();
         if (var1 == var3.field_1724 && var1.method_73183() == var3.field_1687) {
            if (this.I() && !this.G()) {
               this.o();
            }

            int var4 = var1.method_31548().method_67532();
            if (var4 == var2 && !this.I()) {
               return true;
            }

            this.p.X(var4, var2);
            this.k = var1;
            this.R = var3.field_1687;
            N = this;
            var1.method_31548().method_61496(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void u(class_310 var1, boolean var2) {
      if (this.I()) {
         if (!this.G()) {
            this.o();
         } else {
            if (AutoTotemModule.A()) {
               if (N == this) {
                  N = null;
               }
            } else if (var1.field_1755 == null && !bn.w(this.k)) {
               this.o(var1, var2);
            }
         }
      }
   }

   public void o(class_310 var1, boolean var2) {
      boolean var3 = this.G();
      int var4 = this.p.w(var3 ? this.k.method_31548().method_67532() : -1, var2 && var3);
      if (var4 >= 0) {
         this.k.method_31548().method_61496(var4);
      }

      this.o();
   }

   private boolean G() {
      class_310 var1 = class_310.method_1551();
      return this.k != null && this.k == var1.field_1724 && this.R == var1.field_1687 && this.k.method_5805();
   }

   private void o() {
      if (N == this) {
         N = null;
      }

      this.p.o();
      this.k = null;
      this.R = null;
   }
}
