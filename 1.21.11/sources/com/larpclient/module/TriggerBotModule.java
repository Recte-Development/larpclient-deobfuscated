package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.g9;
import com.larpclient.q8;
import com.larpclient.ug;
import com.larpclient.y2;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;

public class TriggerBotModule extends Module {
   private final lo v = this.addSetting(new lo("Delay", "Attack delay (ticks)", 0.0, 0.0, 10.0, 1.0));
   private final l3 r = this.addSetting(new l3("Players Only", "Only attack players", false));
   private final l3 P = this.addSetting(new l3("Smart Delay", "Wait for attack cooldown", true));
   private final y2 x = new y2();
   private class_1297 f;
   private String Y = "Waiting for a target";
   private long M;

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   @Override
   public void onDisable() {
      this.j("Waiting for a target");
      q8.v("TriggerBot", "Released target and queued attack");
   }

   private void j(String var1) {
      this.f = null;
      this.x.N();
      cr.p(this);
      this.K(var1);
   }

   public TriggerBotModule() {
      super("TriggerBot", "Attack the entity you aim at", Category.COMBAT, -1);
   }

   @Override
   public void onTick() {
      int var10000 = this.q();
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.j("Paused: input unavailable");
      } else {
         class_1297 var1 = this.r();
         int var12 = this.u(var1);
         boolean var6 = false;
         byte var5 = (byte)var12;
         var12 = Integer.signum(var5);
         if ((var12 | -var12) >>> 31 == 0) {
            this.j("Waiting for a target");
         } else {
            String var2 = cr.l(var1, mc.field_1724.method_55755(), mc.field_1724.method_5720());
            if (var2 != null) {
               this.j("Waiting: " + var2);
            } else {
               this.f = var1;
               int var14 = this.x.n(var1, mc.field_1687.method_75260(), this.v.getValue().intValue());
               boolean var8 = false;
               byte var7 = (byte)var14;
               var14 = Integer.signum(var7);
               if ((var14 | -var14) >>> 31 == 0) {
                  this.K("Waiting for delay");
               } else {
                  if (this.P.getValue()) {
                     int var16 = cr.A();
                     boolean var10 = false;
                     byte var9 = (byte)var16;
                     var16 = Integer.signum(var9);
                     if ((var16 | -var16) >>> 31 == 0) {
                        this.K("Waiting for attack cooldown");
                        return;
                     }
                  }

                  this.K("Ready");
                  cr.U(this, var1, mc.field_1724.method_55755(), this.P.getValue(), () -> this.q() && this.f == var1 && this.u(var1), 1, () -> {
                     this.x.F(var1, mc.field_1687.method_75260());
                     g9.n();
                     this.K("Attacked " + var1.method_5477().getString());
                  }, var1x -> this.K("Waiting: " + var1x));
               }
            }
         }
      }
   }

   private boolean q() {
      return this.isInGame()
         && cr.f()
         && mc.method_1569()
         && mc.method_18506() == null
         && !mc.field_1724.method_6115()
         && !mc.field_1690.field_1904.method_1434();
   }

   private boolean u(class_1297 var1) {
      return var1 instanceof class_1309 var2
         && var2.method_5805()
         && !var1.method_31481()
         && var1.method_73183() == mc.field_1687
         && var1 != mc.field_1724
         && (!this.r.getValue() || var1 instanceof class_1657);
   }

   private class_1297 r() {
      class_243 var1 = mc.field_1724.method_33571();
      class_243 var2 = mc.field_1724.method_5720();
      double var3 = mc.field_1724.method_55755();
      class_243 var5 = var1.method_1019(var2.method_1021(var3));
      return j(mc.field_1724, var1, var2, var3, mc.field_1687.method_8335(mc.field_1724, new class_238(var1, var5).method_1014(1.0)));
   }

   private static class_1297 j(class_1297 var0, class_243 var1, class_243 var2, double var3, Iterable<class_1297> var5) {
      class_1297 var6 = null;
      double var7 = var3 * var3 + 1.0E-7;

      for (class_1297 var10 : var5) {
         if (var10 != var0 && var10.method_5863() && !var10.method_7325() && !var10.method_31481() && var10.method_5668() != var0.method_5668()) {
            class_238 var11 = var10.method_5829().method_1014(var10.method_5871());
            Optional var12 = ug.p(var1, var2, var11, var3);
            if (!var12.isEmpty()) {
               double var13 = var1.method_1025((class_243)var12.get());
               if (var13 < var7) {
                  var6 = var10;
                  var7 = var13;
               }
            }
         }
      }

      return var6;
   }

   private void K(String var1) {
      if (!this.Y.equals(var1)) {
         this.Y = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.M >= 1000L) {
            this.M = var2;
            q8.v("TriggerBot", "{}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.j("Waiting for a target");
      q8.v("TriggerBot", "Fresh body-ray trigger active; delay={}, cooldown={}, players={}", this.v.getValue(), this.P.getValue(), this.r.getValue());
   }

   @Override
   public String getStatus() {
      return this.Y;
   }
}
