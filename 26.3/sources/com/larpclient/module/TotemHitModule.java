package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.s6;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

public final class TotemHitModule extends Module {
   private static TotemHitModule G;
   private final b P = this.addSetting(new b("Switch Delay", "Milliseconds before returning to your totem after the sword hit", 30.0, 10.0, 100.0, 1.0));
   private final s6 y = new s6(this);
   private long d;
   private String u = "Ready";

   public TotemHitModule() {
      super("Totem Hit", "Hit with your sword while holding a totem, then switch back", Category.COMBAT, -1);
      G = this;
   }

   public static int M(LocalPlayer var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (var0.getInventory().getItem(var1).is(ItemTags.SWORDS)) {
            return var1;
         }
      }

      return -1;
   }

   public static boolean V(long var0, long var2, double var4) {
      return var2 - var0 >= (long)(var4 * 1000000.0);
   }

   public static void u(LocalPlayer var0, Entity var1) {
      if (G != null && G.isEnabled()) {
         TotemHitModule var2 = G;
         if (var2 != null && var2.isRuntimeActive() && var1 != null && !_p.v()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player && !var2.y.j() && var0.getMainHandItem().is(Items.TOTEM_OF_UNDYING)) {
               int var4 = M(var0);
               if (var4 < 0) {
                  var2.u = "No sword in hotbar";
               } else if (!var2.y.h(var0, var4)) {
                  var2.u = "Hotbar busy";
               } else {
                  var2.d = System.nanoTime();
                  var2.u = "Sword hit";
                  _q.c("TotemHit", "Hit {} with hotbar sword {}", var1.getId(), var4 + 1);
               }
            }
         }
      }
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.u : "";
   }

   @Override
   public void onEnable() {
      this.u = "Ready";
      _q.z("TotemHit", "Enabled; switch delay {} ms", this.P.getValue().intValue());
   }

   @Override
   public void onDisable() {
      if (this.y.j()) {
         this.y.X(getMc(), true);
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.y.j();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         int var6 = V(this.d, System.nanoTime(), this.P.getValue());
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 != 0) {
            this.y.X(mc, true);
            this.u = "Ready";
            return;
         }
      }
   }
}
