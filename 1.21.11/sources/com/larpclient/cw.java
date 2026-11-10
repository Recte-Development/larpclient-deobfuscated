package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import net.minecraft.class_10799;
import net.minecraft.class_1921;
import net.minecraft.class_4588;

public final class cw implements AutoCloseable {
   private final tq u = new tq(() -> "Larp effects", 524288);
   private final LinkedHashMap<class_1921, tq.t> p = new LinkedHashMap<>();
   private class_1921 R;
   private class_4588 P;

   public class_4588 N(class_1921 var1) {
      if (var1 == this.R && this.P != null) {
         return this.P;
      }

      this.R = var1;
      return this.P = this.u
         .l(
            this.p
               .computeIfAbsent(
                  var1,
                  var1x -> this.u.F(var1x.method_23031(), var1x.method_23033(), var1x.method_60894() ? RenderSystem.getProjectionType().method_65045() : null)
               )
         );
   }

   public void P() {
      this.y(UnaryOperator.identity());
   }

   public void y(UnaryOperator<jv> var1) {
      if (!this.p.isEmpty()) {
         this.R = null;
         this.P = null;

         try {
            this.u.a();

            for (Entry var3 : this.p.entrySet()) {
               if (!B((class_1921)var3.getKey())) {
                  tq.l var4 = this.u.a((tq.t)var3.getValue());
                  if (var4 != null) {
                     jv var5 = var1.apply(jv.prepare((class_1921)var3.getKey()));
                     if (!jg.g(var5, var4) && !lj.s(var5, var4)) {
                        yf.o(var5, var4);
                     }

                     c5.V("Effect draw groups", 1L);
                  }
               }
            }

            for (Entry var10 : this.p.entrySet()) {
               if (B((class_1921)var10.getKey())) {
                  tq.l var11 = this.u.a((tq.t)var10.getValue());
                  if (var11 != null) {
                     jv var12 = var1.apply(jv.prepare((class_1921)var10.getKey()));
                     if (!jg.g(var12, var11) && !lj.s(var12, var11)) {
                        yf.o(var12, var11);
                     }

                     c5.V("Effect draw groups", 1L);
                  }
               }
            }
         } finally {
            this.p.clear();
            this.u.S();
         }
      }
   }

   static boolean B(class_1921 var0) {
      return var0.method_73243() == class_10799.field_56816;
   }

   public void j() {
      this.R = null;
      this.P = null;
      this.p.clear();
      this.u.S();
   }

   public void W() {
      this.p.clear();
      this.R = null;
      this.P = null;
      this.u.X();
   }

   @Override
   public void close() {
      this.u.close();
   }
}
