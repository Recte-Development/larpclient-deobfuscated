package com.larpclient.module;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class HitBoxesModule extends Module {
   private static HitBoxesModule p;
   private final e k = this.addSetting(new e("Expand", "Extra blocks added on every side of a hitbox", 0.3, 0.05, 1.0, 0.05));
   private final s s = this.addSetting(new s("Players", "Expand other players", true));
   private final s j = this.addSetting(new s("Mobs", "Expand mobs, animals and other living entities", true));

   public HitBoxesModule() {
      super("HitBoxes", "Enlarge other entities' hitboxes for your crosshair; reach is unchanged", Category.COMBAT, -1);
      p = this;
   }

   public static HitBoxesModule s() {
      return p;
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
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U(
         "HitBoxes", "Enabled: expand={} players={} mobs={}; pick radius only, range unchanged", this.k.getValue(), this.s.getValue(), this.j.getValue()
      );
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("HitBoxes", "Disabled; native pick radius restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return !this.s.getValue() && !this.j.getValue()
            ? "No target types selected"
            : String.format(Locale.ROOT, "Expanding by %.2f blocks", this.k.getValue());
      }
   }

   public static float y(Entity var0) {
      HitBoxesModule var1 = p;
      if (var1 != null && var1.isEnabled() && var0 instanceof LivingEntity) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var0 == var3 || var0.level() != var2.level || var0.getRootVehicle() == var3.getRootVehicle()) {
            return 0.0F;
         } else if (!var0 instanceof Player ? var1.s.getValue() : var1.j.getValue()) {
            return 0.0F;
         } else {
            return !var1.isRuntimeActive() ? 0.0F : var1.k.getValue().floatValue();
         }
      } else {
         return 0.0F;
      }
   }
}
