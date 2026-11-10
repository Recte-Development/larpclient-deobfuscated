package com.larpclient.module;

import com.larpclient.q8;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_746;

public final class HitBoxesModule extends Module {
   private static HitBoxesModule R;
   private final lo Q = this.addSetting(new lo("Expand", "Extra blocks added on every side of a hitbox", 0.3, 0.05, 1.0, 0.05));
   private final l3 j = this.addSetting(new l3("Players", "Expand other players", true));
   private final l3 y = this.addSetting(new l3("Mobs", "Expand mobs, animals and other living entities", true));

   public HitBoxesModule() {
      super("HitBoxes", "Enlarge other entities' hitboxes for your crosshair; reach is unchanged", Category.COMBAT, -1);
      R = this;
   }

   public static HitBoxesModule b() {
      return R;
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
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onEnable() {
      q8.v("HitBoxes", "Enabled: expand={} players={} mobs={}; pick radius only, range unchanged", this.Q.getValue(), this.j.getValue(), this.y.getValue());
   }

   @Override
   public void onDisable() {
      q8.v("HitBoxes", "Disabled; native pick radius restored");
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return !this.j.getValue() && !this.y.getValue()
            ? "No target types selected"
            : String.format(Locale.ROOT, "Expanding by %.2f blocks", this.Q.getValue());
      }
   }

   public static float U(class_1297 var0) {
      HitBoxesModule var1 = R;
      if (var1 != null && var1.isEnabled() && var0 instanceof class_1309) {
         class_310 var2 = getMc();
         class_746 var3 = var2.field_1724;
         if (var3 == null || var0 == var3 || var0.method_73183() != var2.field_1687 || var0.method_5668() == var3.method_5668()) {
            return 0.0F;
         } else if (!var0 instanceof class_1657 ? var1.j.getValue() : var1.y.getValue()) {
            return 0.0F;
         } else {
            return !var1.isRuntimeActive() ? 0.0F : var1.Q.getValue().floatValue();
         }
      } else {
         return 0.0F;
      }
   }
}
