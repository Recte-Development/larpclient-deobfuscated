package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.o_;
import com.larpclient.r4;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

public final class TotemHitModule extends Module {
   private static TotemHitModule M;
   private final lc H = this.addSetting(new lc("Switch Delay", "Milliseconds before returning to your totem after the sword hit", 30.0, 10.0, 100.0, 1.0));
   private final o_ O = new o_(this);
   private long n;
   private String v = "Ready";

   public TotemHitModule() {
      super("Totem Hit", "Hit with your sword while holding a totem, then switch back", Category.COMBAT, -1);
      M = this;
   }

   public static int n(LocalPlayer var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (var0.getInventory().getItem(var1).is(ItemTags.SWORDS)) {
            return var1;
         }
      }

      return -1;
   }

   public static boolean k(long var0, long var2, double var4) {
      return var2 - var0 >= (long)(var4 * 1000000.0);
   }

   public static void k(LocalPlayer var0, Entity var1) {
      if (M != null && M.isEnabled()) {
         TotemHitModule var2 = M;
         if (var2 != null && var2.isRuntimeActive() && var1 != null && !d8.E()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player && !var2.O.K() && var0.getMainHandItem().is(Items.TOTEM_OF_UNDYING)) {
               int var4 = n(var0);
               if (var4 < 0) {
                  var2.v = "No sword in hotbar";
               } else if (!var2.O.s(var0, var4)) {
                  var2.v = "Hotbar busy";
               } else {
                  var2.n = System.nanoTime();
                  var2.v = "Sword hit";
                  r4.g("TotemHit", "Hit {} with hotbar sword {}", var1.getId(), var4 + 1);
               }
            }
         }
      }
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.v : "";
   }

   @Override
   public void onEnable() {
      this.v = "Ready";
      r4.V("TotemHit", "Enabled; switch delay {} ms", this.H.getValue().intValue());
   }

   @Override
   public void onDisable() {
      if (this.O.K()) {
         this.O.m(getMc(), true);
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.O.K();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         int var6 = k(this.n, System.nanoTime(), this.H.getValue());
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 != 0) {
            this.O.m(mc, true);
            this.v = "Ready";
            return;
         }
      }
   }
}
