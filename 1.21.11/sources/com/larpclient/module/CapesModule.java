package com.larpclient.module;

import com.larpclient.bt;
import com.larpclient.q8;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_10799;
import net.minecraft.class_332;
import net.minecraft.class_8685;
import net.minecraft.class_12079.class_12081;

public final class CapesModule extends Module {
   private static CapesModule u;
   private final li H = this.addSetting(new li("Cape", "Official cape to wear. Only your client shows it", "MineCon 2011", com.larpclient.l6.E()) {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public String getModeLabel(String var1) {
         return com.larpclient.l6.s(var1);
      }

      @Override
      public int drawModeIcon(class_332 var1, String var2, int var3, int var4) {
         com.larpclient.l6.t var5 = com.larpclient.l6.e(var2);
         if (var5 == null) {
            return 0;
         }

         class_12081 var6 = bt.e(var5);
         if (var6 != null) {
            var1.method_25302(class_10799.field_56883, var6.comp_3627(), var3 + 4, var4, 1.0F, 1.0F, 10, 16, 10, 16, 64, 32);
         } else {
            var1.method_25294(var3 + 4, var4, var3 + 14, var4 + 16, 822083583);
         }

         return 18;
      }
   });
   private volatile class_12081 V;
   private long N;
   private long i;
   private volatile String m = "Choose a cape";

   public CapesModule() {
      super("Capes", "Wear any official cape, like a MineCon cape. Only you see it", Category.VISUALS, -1);
      u = this;
      this.H.a(var1 -> {
         if (this.isRuntimeActive()) {
            this.R();
         }
      });
   }

   public static CapesModule i() {
      return u;
   }

   @Override
   public String getStatus() {
      return this.m;
   }

   @Override
   public void onEnable() {
      this.R();
   }

   @Override
   public void onTick() {
      long var7;
      int var10000 = (var7 = this.i - 0L) == 0L ? 0 : (var7 < 0L ? -1 : 1);
      boolean var2 = false;
      int var1 = var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         long var8;
         var10000 = (var8 = System.nanoTime() - this.i) == 0L ? 0 : (var8 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         if (Integer.signum(var3) >>> 31 == 0) {
            this.R();
         }
      }
   }

   @Override
   public void onDisable() {
      this.N++;
      this.V = null;
      this.i = 0L;
      this.m = "Original cape restored";
      q8.G("Capes", "Disabled; original cape restored");
   }

   private void R() {
      this.i = 0L;
      long var1 = ++this.N;
      com.larpclient.l6.t var3 = com.larpclient.l6.e(this.H.getValue());
      if (var3 == null) {
         this.V = null;
         this.m = "Choose a cape";
      } else {
         CompletableFuture var4 = bt.H(var3);
         if (var4.isDone() && !var4.isCompletedExceptionally()) {
            this.V = (class_12081)var4.join();
            this.m = var3.label();
         } else {
            this.m = "Loading " + var3.label() + "...";
            var4.whenCompleteAsync((var4x, var5) -> {
               if (var1 == this.N && this.isRuntimeActive()) {
                  if (var5 != null) {
                     this.m = "Could not load " + var3.label() + ". Retrying in 30 seconds";
                     this.i = System.nanoTime() + 30000000000L;
                  } else {
                     this.V = var4x;
                     this.m = var3.label();
                     q8.G("Capes", "Wearing {}", var3.label());
                  }
               }
            }, getMc());
         }
      }
   }

   public class_8685 E(class_8685 var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.isOperational()) {
            com.larpclient.l6.t var2 = com.larpclient.l6.e(this.H.getValue());
            class_12081 var3 = var2 == null ? null : bt.e(var2);
            return var3 == null ? var1 : new class_8685(var1.comp_1626(), var3, null, var1.comp_1629(), var1.comp_1630());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public class_8685 C(UUID var1, class_8685 var2) {
      if (this.isEnabled()) {
         class_12081 var3 = this.V;
         return var3 != null && var2 != null && this.isRuntimeActive() && mc.field_1724 != null && var1.equals(mc.field_1724.method_5667())
            ? new class_8685(var2.comp_1626(), var3, null, var2.comp_1629(), var2.comp_1630())
            : var2;
      } else {
         return var2;
      }
   }
}
