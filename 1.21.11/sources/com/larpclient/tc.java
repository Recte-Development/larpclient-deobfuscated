package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public interface tc {
   default tc.g hudCard() {
      return null;
   }

   List<tc.k> commands();

   String summary();

   default String detail() {
      return "";
   }

   default lc preview() {
      return null;
   }

   default tc.b status() {
      return null;
   }

   default void panelTick() {
   }

   List<tc.v> settingGroups();

   default tc.q g(String var1, String var2) {
      return this.commands().stream().filter(var1x -> var1x.label().equals(var2)).findFirst().map(var1x -> new tc.q(var1, var1x.run())).orElseThrow();
   }

   static String T(long var0) {
      long var2 = Math.max(0L, var0 / 1000L);
      return var2 >= 3600L ? var2 / 3600L + "h " + var2 % 3600L / 60L + "m" : (var2 >= 60L ? var2 / 60L + "m " + var2 % 60L + "s" : var2 + "s");
   }

   static String q(Module var0) {
      LarpClient var1 = LarpClient.b();
      if (var1 != null && var1.G() != null) {
         for (Module var3 : var1.G().M()) {
            if (var3 != var0 && var3 instanceof tc && var3.isRuntimeActive()) {
               return var3.getName();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   record b(String text, boolean problem) {
   }

   record g(String headline, String value, String action, String time, double progress, boolean alert) {
      public g(String var1, String var2, String var3, String var4, double var5) {
         this(var1, var2, var3, var4, var5, false);
      }
   }

   record k(String section, String label, Supplier<String> run) {
   }

   record l(Supplier<String> value, List<List<tc.q>> rows, Supplier<lc> picture, boolean settingsFirst) {
      public l(Supplier<String> var1, List<List<tc.q>> var2) {
         this(var1, var2, () -> null, false);
      }
   }

   record q(String label, Supplier<String> run, BooleanSupplier available) {
      public q(String var1, Supplier<String> var2) {
         this(var1, var2, () -> true);
      }

      public tc.q availableWhen(BooleanSupplier var1) {
         return new tc.q(this.label, this.run, var1);
      }
   }

   record v(String title, List<com.larpclient.module.lb<?>> settings, boolean advanced, tc.l setup) {
      public v(String var1, List<com.larpclient.module.lb<?>> var2) {
         this(var1, var2, false, null);
      }

      public static tc.v advanced(String var0, List<com.larpclient.module.lb<?>> var1) {
         return new tc.v(var0, var1, true, null);
      }

      public tc.v with(tc.l var1) {
         return new tc.v(this.title, this.settings, this.advanced, var1);
      }
   }
}
