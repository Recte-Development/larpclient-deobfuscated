package com.larpclient.module;

import com.larpclient._q;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class HitBoxesModule extends Module {
   private static HitBoxesModule k;
   private final b Q = this.addSetting(new b("Expand", "Extra blocks added on every side of a hitbox", 0.3, 0.05, 1.0, 0.05));
   private final m o = this.addSetting(new m("Players", "Expand other players", true));
   private final m V = this.addSetting(new m("Mobs", "Expand mobs, animals and other living entities", true));

   public HitBoxesModule() {
      super("HitBoxes", "Enlarge other entities' hitboxes for your crosshair; reach is unchanged", Category.COMBAT, -1);
      k = this;
   }

   public static HitBoxesModule e() {
      return k;
   }

   @Override
   public String getDisplayName() {
      return "Hitbox Expander";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("HitBoxes", "Hitbox");
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public void onEnable() {
      _q.c("HitBoxes", "Enabled: expand={} players={} mobs={}; pick radius only, range unchanged", this.Q.getValue(), this.o.getValue(), this.V.getValue());
   }

   @Override
   public void onDisable() {
      _q.c("HitBoxes", "Disabled; native pick radius restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return !this.o.getValue() && !this.V.getValue()
            ? "No target types selected"
            : String.format(Locale.ROOT, "Expanding by %.2f blocks", this.Q.getValue());
      }
   }

   public static float u(Entity var0) {
      HitBoxesModule var1 = k;
      if (var1 != null && var1.isEnabled() && var0 instanceof LivingEntity) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var0 == var3 || var0.level() != var2.level || var0.getRootVehicle() == var3.getRootVehicle()) {
            return 0.0F;
         } else if (!var0 instanceof Player ? var1.o.getValue() : var1.V.getValue()) {
            return 0.0F;
         } else {
            return !var1.isRuntimeActive() ? 0.0F : var1.Q.getValue().floatValue();
         }
      } else {
         return 0.0F;
      }
   }
}
