package com.larpclient.module.combat;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class HitBoxesModule extends Module {
   private static HitBoxesModule instance;
   private final NumberSetting expand = this.addSetting(new NumberSetting("Expand", "Extra blocks added on every side of a hitbox", 0.3, 0.05, 1.0, 0.05));
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Expand other players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Expand mobs, animals and other living entities", true));

   public HitBoxesModule() {
      super("HitBoxes", "Enlarge other entities' hitboxes for your crosshair; reach is unchanged", Category.COMBAT, -1);
      instance = this;
   }

   public static HitBoxesModule getInstance() {
      return instance;
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
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      LarpLog.debug(
         "HitBoxes",
         "Enabled: expand={} players={} mobs={}; pick radius only, range unchanged",
         this.expand.getValue(),
         this.players.getValue(),
         this.mobs.getValue()
      );
   }

   @Override
   public void onDisable() {
      LarpLog.debug("HitBoxes", "Disabled; native pick radius restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return !this.players.getValue() && !this.mobs.getValue()
            ? "No target types selected"
            : String.format(Locale.ROOT, "Expanding by %.2f blocks", this.expand.getValue());
      }
   }

   public static float getExpansion(Entity var0) {
      HitBoxesModule var1 = instance;
      if (var1 != null && var1.isEnabled() && var0 instanceof LivingEntity) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var0 == var3 || var0.level() != var2.level || var0.getRootVehicle() == var3.getRootVehicle()) {
            return 0.0F;
         } else if (!var0 instanceof Player ? var1.players.getValue() : var1.mobs.getValue()) {
            return 0.0F;
         } else {
            return !var1.isRuntimeActive() ? 0.0F : var1.expand.getValue().floatValue();
         }
      } else {
         return 0.0F;
      }
   }
}
