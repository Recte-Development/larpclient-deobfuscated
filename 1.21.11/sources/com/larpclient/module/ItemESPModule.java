package com.larpclient.module;

import com.larpclient.l2;
import com.larpclient.q8;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_238;

public class ItemESPModule extends Module {
   private final lo Y = this.addSetting(new lo("Range", "Max render range", 48.0, 8.0, 128.0, 8.0));
   private final lo s = this.addSetting(new lo("Max Items", "Nearest drops drawn", 128.0, 16.0, 512.0, 16.0));
   private final li V = this.addSetting(new li("Mode", "Drop highlight style", "Corners", "Box", "Corners", "Filled"));
   private final l3 z = this.addSetting(new l3("XP Orbs", "Include experience orbs", false));
   private final l3 b = this.addSetting(new l3("Labels", "Item name, count and distance within 24 blocks", true));
   private final l2 B = new l2(var1 -> this.addSetting(var1));

   public ItemESPModule() {
      super("ItemESP", "Highlight dropped items and experience orbs", Category.RENDER, -1);
      this.B.N(() -> true, () -> true, () -> true, () -> this.V.G("Filled"));
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.u());
   }

   private void u() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            float var1 = mc.method_61966().method_60637(false);

            try {
               int var2 = 0;

               for (class_1297 var4 : l2.Q(
                  mc.field_1687.method_18112(),
                  mc.field_1724,
                  this.Y.getValue(),
                  this.s.getValue().intValue(),
                  var1x -> var1x instanceof class_1542 || this.z.getValue() && var1x instanceof class_1303
               )) {
                  double var5 = mc.field_1724.method_5739(var4);
                  int var7 = this.B.C(var4 instanceof class_1542 ? -3627265 : -7677757, var5, this.Y.getValue(), 1.0, var4.method_5628());
                  class_238 var8 = l2.F(var4, var1);
                  this.B.X(var8, this.V.getValue(), var7);
                  if (this.b.getValue() && var5 <= 24.0 && var4 instanceof class_1542 var9 && var2++ < 24) {
                     class_1799 var10 = var9.method_6983();
                     this.B
                        .n(
                           var8.method_1005().method_1031(0.0, var8.method_17940() * 0.5 + 0.18, 0.0),
                           var10.method_7964().getString() + " ×" + var10.method_7947() + " · " + (int)var5 + "m",
                           var7
                        );
                  }
               }
            } catch (RuntimeException var11) {
               q8.O("ESP", "Item overlay failed", var11);
            }
         }
      }
   }

   public List<lb<?>> U() {
      return List.of(this.V, this.Y, this.z, this.b);
   }

   public List<lb<?>> D() {
      ArrayList var1 = new ArrayList<>(this.B.v());
      var1.addAll(this.B.P());
      var1.addAll(this.B.t());
      return List.copyOf(var1);
   }

   public List<lb<?>> d() {
      return List.of(this.s);
   }
}
