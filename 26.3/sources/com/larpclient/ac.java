package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public interface ac {
   default ac.g hudCard() {
      return null;
   }

   List<ac.b> commands();

   String summary();

   default String detail() {
      return "";
   }

   default j0 preview() {
      return null;
   }

   default ac.u status() {
      return null;
   }

   default void panelTick() {
   }

   List<ac.r> settingGroups();

   default ac.f k(String var1, String var2) {
      return this.commands().stream().filter(var1x -> var1x.label().equals(var2)).findFirst().map(var1x -> new ac.f(var1, var1x.run())).orElseThrow();
   }

   static String Q(long var0) {
      long var2 = Math.max(0L, var0 / 1000L);
      return var2 >= 3600L ? var2 / 3600L + "h " + var2 % 3600L / 60L + "m" : (var2 >= 60L ? var2 / 60L + "m " + var2 % 60L + "s" : var2 + "s");
   }

   static String K(Module var0) {
      LarpClient var1 = LarpClient.N();
      if (var1 != null && var1.j() != null) {
         for (Module var3 : var1.j().d()) {
            if (var3 != var0 && var3 instanceof ac && var3.isRuntimeActive()) {
               return var3.getName();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   record b(String section, String label, Supplier<String> run) {
   }

   record f(String label, Supplier<String> run, BooleanSupplier available) {
      public f(String var1, Supplier<String> var2) {
         this(var1, var2, () -> true);
      }

      public ac.f availableWhen(BooleanSupplier var1) {
         return new ac.f(this.label, this.run, var1);
      }
   }

   record g(String headline, String value, String action, String time, double progress, boolean alert) {
      public g(String var1, String var2, String var3, String var4, double var5) {
         this(var1, var2, var3, var4, var5, false);
      }
   }

   record m(Supplier<String> value, List<List<ac.f>> rows, Supplier<j0> picture, boolean settingsFirst) {
      public m(Supplier<String> var1, List<List<ac.f>> var2) {
         this(var1, var2, () -> null, false);
      }
   }

   record r(String title, List<com.larpclient.module.r<?>> settings, boolean advanced, ac.m setup) {
      public r(String var1, List<com.larpclient.module.r<?>> var2) {
         this(var1, var2, false, null);
      }

      public static ac.r advanced(String var0, List<com.larpclient.module.r<?>> var1) {
         return new ac.r(var0, var1, true, null);
      }

      public ac.r with(ac.m var1) {
         return new ac.r(this.title, this.settings, this.advanced, var1);
      }
   }

   record u(String text, boolean problem) {
   }
}
