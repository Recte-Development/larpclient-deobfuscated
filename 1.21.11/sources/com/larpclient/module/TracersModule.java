package com.larpclient.module;

import com.larpclient.l2;
import com.larpclient.q8;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import org.joml.Vector3f;

public class TracersModule extends Module {
   private final l3 K = this.addSetting(new l3("Players", "Trace players", true));
   private final l3 H = this.addSetting(new l3("Mobs", "Trace hostile mobs", false));
   private final l3 S = this.addSetting(new l3("Animals", "Trace animals", false));
   private final lo U = this.addSetting(new lo("Range", "Max tracer range", 96.0, 16.0, 256.0, 8.0));
   private final lo Y = this.addSetting(new lo("Max Entities", "Nearest targets drawn", 64.0, 8.0, 256.0, 8.0));
   private final l2 d = new l2(var1 -> this.addSetting(var1), false);

   public TracersModule() {
      super("Tracers", "Draw lines to entities", Category.RENDER, -1);
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.N());
   }

   private void N() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            class_4184 var1 = mc.field_1773.method_19418();
            Vector3f var2 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.method_23767());
            class_243 var3 = var1.method_71156().method_1031(var2.x * 0.5, var2.y * 0.5, var2.z * 0.5);
            float var4 = mc.method_61966().method_60637(false);

            try {
               for (class_1297 var6 : l2.Q(
                  mc.field_1687.method_18112(),
                  mc.field_1724,
                  this.U.getValue(),
                  this.Y.getValue().intValue(),
                  var1x -> var1x instanceof class_1309
                     && (
                        var1x instanceof class_1657
                           ? this.K.getValue()
                           : (var1x instanceof class_1569 ? this.H.getValue() : var1x instanceof class_1429 && this.S.getValue())
                     )
               )) {
                  int var7 = var6 instanceof class_1657 ? -4221441 : (var6 instanceof class_1569 ? -943189 : -7677757);
                  int var8 = this.d.C(var7, mc.field_1724.method_5739(var6), this.U.getValue(), l2.l((class_1309)var6), var6.method_5628());
                  this.d.O(var3, l2.i(var6, var4).method_1031(0.0, var6.method_17682() * 0.5, 0.0), var8);
               }
            } catch (RuntimeException var9) {
               q8.O("ESP", "Tracer overlay failed", var9);
            }
         }
      }
   }

   public List<lb<?>> L() {
      return List.of(this.K, this.H, this.S);
   }

   public List<lb<?>> O() {
      ArrayList var1 = new ArrayList<>(List.of(this.U));
      var1.addAll(this.d.v());
      var1.addAll(this.d.P());
      return List.copyOf(var1);
   }

   public List<lb<?>> o() {
      return List.of(this.Y);
   }
}
