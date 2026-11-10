package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public interface s1 {
   default s1.u hudCard() {
      return null;
   }

   List<s1.a> commands();

   String summary();

   default String detail() {
      return "";
   }

   default jd preview() {
      return null;
   }

   default s1.o status() {
      return null;
   }

   default void panelTick() {
   }

   List<s1.w> settingGroups();

   default s1.c T(String var1, String var2) {
      return this.commands().stream().filter(var1x -> var1x.label().equals(var2)).findFirst().map(var1x -> new s1.c(var1, var1x.run())).orElseThrow();
   }

   static String h(long var0) {
      long var2 = Math.max(0L, var0 / 1000L);
      return var2 >= 3600L ? var2 / 3600L + "h " + var2 % 3600L / 60L + "m" : (var2 >= 60L ? var2 / 60L + "m " + var2 % 60L + "s" : var2 + "s");
   }

   static String U(Module var0) {
      LarpClient var1 = LarpClient.R();
      if (var1 != null && var1.z() != null) {
         for (Module var3 : var1.z().h()) {
            if (var3 != var0 && var3 instanceof s1 && var3.isRuntimeActive()) {
               return var3.getName();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   record a(String section, String label, Supplier<String> run) {
   }

   record c(String label, Supplier<String> run, BooleanSupplier available) {
      public c(String var1, Supplier<String> var2) {
         this(var1, var2, () -> true);
      }

      public s1.c availableWhen(BooleanSupplier var1) {
         return new s1.c(this.label, this.run, var1);
      }
   }

   record o(String text, boolean problem) {
   }

   record r(Supplier<String> value, List<List<s1.c>> rows, Supplier<jd> picture, boolean settingsFirst) {
      public r(Supplier<String> var1, List<List<s1.c>> var2) {
         this(var1, var2, () -> null, false);
      }
   }

   record u(String headline, String value, String action, String time, double progress, boolean alert) {
      public u(String var1, String var2, String var3, String var4, double var5) {
         this(var1, var2, var3, var4, var5, false);
      }
   }

   record w(String title, List<com.larpclient.module.q<?>> settings, boolean advanced, s1.r setup) {
      public w(String var1, List<com.larpclient.module.q<?>> var2) {
         this(var1, var2, false, null);
      }

      public static s1.w advanced(String var0, List<com.larpclient.module.q<?>> var1) {
         return new s1.w(var0, var1, true, null);
      }

      public s1.w with(s1.r var1) {
         return new s1.w(this.title, this.settings, this.advanced, var1);
      }
   }
}
