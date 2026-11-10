package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larp.obf.q4031f93fe72647cb.qad16ae5db11162ea;
import com.larpclient.hb;
import com.larpclient.iy;
import com.larpclient.q8;
import com.larpclient.wp;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_465;

public final class HoverTotemModule extends Module {
   private static HoverTotemModule M;
   private final l3 T = this.addSetting(new l3("Hotbar Refill", "After filling the offhand, move hovered totems into the chosen hotbar slot", false));
   private final lo w = this.addSetting(new lo("Hotbar Slot", "Hotbar slot to refill, replacing its current item when needed", 1.0, 1.0, 9.0, 1.0));

   public HoverTotemModule() {
      super("HoverTotem", "Move hovered totems into your offhand or chosen hotbar slot", Category.COMBAT, -1);
      this.w.O(this.T::getValue);
      M = this;
   }

   public static HoverTotemModule g() {
      return M;
   }

   @Override
   public String getDisplayName() {
      return "Hover Totem";
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onEnable() {
      q8.v("HoverTotem", "Ready for native container hover swaps; AutoTotem has priority");
   }

   @Override
   public void onTick() {
      if (this.isInGame()
         && mc.field_1761 != null
         && mc.field_1724.method_5805()
         && !mc.field_1724.method_7325()
         && !mc.field_1724.method_56992()
         && !wp.d()
         && mc.method_1569()
         && mc.field_1755 instanceof class_465 var1
         && !AutoTotemModule.h()) {
         q586814c6d8f3640b var19 = (q586814c6d8f3640b)mc.field_1724;
         class_10185 var3 = var19.larpLastSentInput();
         if (var3 != null
            && hb.G(var19.larpWasSprinting(), var3.comp_3159(), var3.comp_3160(), var3.comp_3161(), var3.comp_3162(), var3.comp_3163(), var3.comp_3164())) {
            class_1703 var4 = var1.method_17577();
            if (var4 == mc.field_1724.field_7512
               && var4.method_7597(mc.field_1724)
               && var4.method_34255().method_7960()
               && mc.field_1724.field_7498.method_34255().method_7960()) {
               class_1735 var5 = ((qad16ae5db11162ea)var1).larpHoveredSlot();
               class_1735 var6 = x(var4, var5);
               if (var6 != null && var6.method_7682() && !var6.method_55059() && var6.method_7674(mc.field_1724)) {
                  int var7 = this.w.getValue().intValue();
                  int var8 = var6.field_7871 == mc.field_1724.method_31548() ? var6.method_34266() : -1;
                  int var10000 = var6.field_7874;
                  boolean var10001 = var6.method_7677().method_31574(class_1802.field_8288);
                  boolean var10003 = mc.field_1724.method_6079().method_31574(class_1802.field_8288);
                  boolean var10004 = this.T.getValue();
                  class_1661 var10006 = mc.field_1724.method_31548();
                  byte var18 = 1;
                  int var17 = var7;
                  hb.d var9 = hb.I(var10000, var10001, var8, var10003, var10004, var7, var10006.method_5438(var17 - var18).method_31574(class_1802.field_8288));
                  if (var9 != null && this.J(var4, var9.button())) {
                     class_1799 var10 = mc.field_1724.method_31548().method_5438(var9.button());
                     if (!var10.method_7960()) {
                        if (!var6.method_7680(var10)) {
                           return;
                        }

                        var10000 = var10.method_7947();
                        int var14 = var6.method_7676(var10);
                        int var13 = var10000;
                        if ((-Integer.compare(var13, var14) >>> 31 ^ 1) == 0) {
                           return;
                        }
                     }

                     mc.field_1761.method_2906(var4.field_7763, var9.menuSlot(), var9.button(), class_1713.field_7791, mc.field_1724);
                     Object[] var10002 = new Object[]{var9.menuSlot(), null, null};
                     int var10005 = var9.button();
                     byte var16 = 40;
                     int var15 = var10005;
                     var10005 = Integer.compare(var15, var16);
                     String var22;
                     if ((var10005 | -var10005) >>> 31 == 0) {
                        var22 = "offhand";
                     } else {
                        var10005 = var9.button();
                        byte var12 = 1;
                        int var11 = var10005;
                        var22 = "hotbar " + (var11 + var12);
                     }

                     var10002[1] = var22;
                     var10002[2] = var4.field_7763;
                     q8.v("HoverTotem", "Swapped hovered menu slot {} to {} in container {}", var10002);
                  }
               }
            }
         }
      }
   }

   private static class_1735 x(class_1703 var0, class_1735 var1) {
      if (var1 != null
         && var1.method_7677().method_31574(class_1802.field_8288)
         && !iy.l(var1)
         && var1.field_7874 >= 0
         && var1.field_7874 < var0.field_7761.size()) {
         class_1735 var2 = (class_1735)var0.field_7761.get(var1.field_7874);
         if (var2 != var1 && !(var1 instanceof iy.n)) {
            return null;
         } else {
            return var2.field_7871 == var1.field_7871
                  && var2.method_34266() == var1.method_34266()
                  && class_1799.method_7973(var2.method_7677(), var1.method_7677())
               ? var2
               : null;
         }
      } else {
         return null;
      }
   }

   private boolean J(class_1703 var1, int var2) {
      if (var2 != 40 && iy.n()) {
         for (class_1735 var4 : var1.field_7761) {
            if (var4.field_7871 == mc.field_1724.method_31548() && var4.method_34266() == var2) {
               class_1735 var5 = iy.x(var1, var4);
               return var5.field_7874 == var4.field_7874 && !iy.l(var5) && class_1799.method_7973(var5.method_7677(), var4.method_7677());
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
