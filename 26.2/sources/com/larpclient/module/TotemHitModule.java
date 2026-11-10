package com.larpclient.module;

import com.larpclient.hu;
import com.larpclient.ta;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

public final class TotemHitModule extends Module {
   private static TotemHitModule N;
   private final e D = this.addSetting(new e("Switch Delay", "Milliseconds before returning to your totem after the sword hit", 30.0, 10.0, 100.0, 1.0));
   private final hu T = new hu(this);
   private long J;
   private String W = "Ready";

   public TotemHitModule() {
      super("Totem Hit", "Hit with your sword while holding a totem, then switch back", Category.COMBAT, -1);
      N = this;
   }

   public static int j(LocalPlayer var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (var0.getInventory().getItem(var1).is(ItemTags.SWORDS)) {
            return var1;
         }
      }

      return -1;
   }

   public static boolean i(long var0, long var2, double var4) {
      return var2 - var0 >= (long)(var4 * 1000000.0);
   }

   public static void L(LocalPlayer var0, Entity var1) {
      if (N != null && N.isEnabled()) {
         TotemHitModule var2 = N;
         if (var2 != null && var2.isRuntimeActive() && var1 != null && !ta.U()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player && !var2.T.p() && var0.getMainHandItem().is(Items.TOTEM_OF_UNDYING)) {
               int var4 = j(var0);
               if (var4 < 0) {
                  var2.W = "No sword in hotbar";
               } else if (!var2.T.O(var0, var4)) {
                  var2.W = "Hotbar busy";
               } else {
                  var2.J = System.nanoTime();
                  var2.W = "Sword hit";
                  com.larpclient.h.U("TotemHit", "Hit {} with hotbar sword {}", var1.getId(), var4 + 1);
               }
            }
         }
      }
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K, m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.W : "";
   }

   @Override
   public void onEnable() {
      this.W = "Ready";
      com.larpclient.h.A("TotemHit", "Enabled; switch delay {} ms", this.D.getValue().intValue());
   }

   @Override
   public void onDisable() {
      if (this.T.p()) {
         this.T.V(getMc(), true);
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.T.p();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         int var6 = i(this.J, System.nanoTime(), this.D.getValue());
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 != 0) {
            this.T.V(mc, true);
            this.W = "Ready";
            return;
         }
      }
   }
}
