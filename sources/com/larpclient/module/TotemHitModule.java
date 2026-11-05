package com.larpclient.module;

import com.larpclient._m;
import com.larpclient.mt;
import com.larpclient.ti;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

public final class TotemHitModule extends Module {
   private static TotemHitModule Q;
   private final d9 j = this.addSetting(new d9("Switch Delay", "Milliseconds before returning to your totem after the sword hit", 30.0, 10.0, 100.0, 1.0));
   private final ti w = new ti(this);
   private long b;
   private String v = "Ready";

   public TotemHitModule() {
      super("Totem Hit", "Hit with your sword while holding a totem, then switch back", Category.COMBAT, -1);
      Q = this;
   }

   public static int M(LocalPlayer var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (var0.getInventory().getItem(var1).is(ItemTags.SWORDS)) {
            return var1;
         }
      }

      return -1;
   }

   public static boolean D(long var0, long var2, double var4) {
      return var2 - var0 >= (long)(var4 * 1000000.0);
   }

   public static void e(LocalPlayer var0, Entity var1) {
      if (Q != null && Q.isEnabled()) {
         TotemHitModule var2 = Q;
         if (var2 != null && var2.isRuntimeActive() && var1 != null && !_m.w()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player && !var2.w.k() && var0.getMainHandItem().is(Items.TOTEM_OF_UNDYING)) {
               int var4 = M(var0);
               if (var4 < 0) {
                  var2.v = "No sword in hotbar";
               } else if (!var2.w.e(var0, var4)) {
                  var2.v = "Hotbar busy";
               } else {
                  var2.b = System.nanoTime();
                  var2.v = "Sword hit";
                  mt.U("TotemHit", "Hit {} with hotbar sword {}", var1.getId(), var4 + 1);
               }
            }
         }
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.v : "";
   }

   @Override
   public void onEnable() {
      this.v = "Ready";
      mt.K("TotemHit", "Enabled; switch delay {} ms", this.j.P().intValue());
   }

   @Override
   public void onDisable() {
      if (this.w.k()) {
         this.w.W(getMc(), true);
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.w.k();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         int var6 = D(this.b, System.nanoTime(), this.j.P());
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 != 0) {
            this.w.W(mc, true);
            this.v = "Ready";
            return;
         }
      }
   }
}
