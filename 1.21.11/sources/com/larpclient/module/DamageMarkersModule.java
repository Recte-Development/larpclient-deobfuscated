package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.b9;
import com.larpclient.ct;
import com.larpclient.ei;
import com.larpclient.im;
import com.larpclient.q8;
import com.larpclient.qo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.class_12075;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2616;
import net.minecraft.class_4587;
import net.minecraft.class_8143;
import org.joml.Vector3f;

public final class DamageMarkersModule extends Module {
   private static DamageMarkersModule x;
   private final lo Z = this.addSetting(new lo("Size", "Damage text size", 1.0, 0.5, 2.5, 0.1));
   private final lo A = this.addSetting(new lo("Life Time", "Marker lifetime in milliseconds", 1000.0, 300.0, 2500.0, 100.0));
   private final l3 a = this.addSetting(new l3("Self Damage", "Show your own health loss", true));
   private final l3 W = this.addSetting(new l3("Friend Hits", "Show damage attributed to friends", true));
   private final li B = this.addSetting(new li("Color", "Color by damage or a chosen tint", "Damage", "Damage", "Custom"));
   private final l0 M = this.addSetting(new l0("Custom Color", "Chosen damage marker tint", -48060));
   private final ct v = new ct(21463920019784013L);
   private final HashMap<class_1309, Double> P = new HashMap<>();
   private im V;
   private Object U;
   private Object t;
   private class_243 s;

   public DamageMarkersModule() {
      super("Damage Markers", "Show dealt damage and your own health loss as floating numbers", Category.VISUALS, -1);
      x = this;
      this.M.O(() -> this.B.G("Custom"));
   }

   public static DamageMarkersModule g() {
      return x;
   }

   public List<lb<?>> e() {
      return List.of(this.a, this.W);
   }

   public List<lb<?>> z() {
      return List.of(this.Z, this.A, this.B, this.M);
   }

   @Override
   public void onEnable() {
      this.H();
      q8.G("DamageMarkers", "Enabled; observed health loss, 8-block trigger range, 256 marker budget");
   }

   @Override
   public void onDisable() {
      this.H();
   }

   public void H() {
      this.v.m();
      this.P.clear();
      this.U = this.t = null;
      this.s = null;
      if (this.V != null) {
         this.V.close();
         this.V = null;
      }
   }

   private boolean Y(class_1297 var1) {
      LarpClient var2 = LarpClient.b();
      if (var1 instanceof class_1657 && var2 != null) {
         MCFriendModule var3 = var2.G().b(MCFriendModule.class);
         return var3 != null && var3.M(var1.method_5477().getString());
      } else {
         return false;
      }
   }

   private boolean y() {
      if (this.isRuntimeActive() && this.isInGame()) {
         if (this.U != getMc().field_1687 || this.t != this.getPlayer()) {
            this.H();
            this.U = getMc().field_1687;
            this.t = this.getPlayer();
         }

         return true;
      } else {
         return false;
      }
   }

   public void U(class_1297 var1) {
      if (this.isEnabled()) {
         if (this.y() && var1 instanceof class_1309 var2 && var1 != this.getPlayer()) {
            this.v.m(var1.method_5628(), var1, var2.method_6032(), qo.K());
         }
      }
   }

   public void b(class_8143 var1) {
      if (this.isEnabled()) {
         if (this.y()) {
            class_1297 var2 = getMc().field_1687.method_8469(var1.comp_1267());
            if (var2 instanceof class_1309 var3 && (var2 != this.getPlayer() || this.a.getValue())) {
               class_1297 var4 = var1.method_49071(getMc().field_1687).method_5529();
               if (var2 == this.getPlayer() || var4 == this.getPlayer() || this.W.getValue() && this.Y(var4)) {
                  this.v.m(var2.method_5628(), var2, var3.method_6032(), qo.K());
               }
            }
         }
      }
   }

   public void e(class_2616 var1) {
      if (this.isEnabled()) {
         if (this.y() && this.W.getValue() && (var1.method_11267() == 0 || var1.method_11267() == 3)) {
            if (getMc().field_1687.method_8469(var1.method_11269()) instanceof class_1309 var3
               && this.Y(var3)
               && (this.P.size() < 128 || this.P.containsKey(var3))) {
               this.P.put(var3, qo.K() + 2.5);
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.y();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.H();
      } else {
         class_243 var1 = this.getPlayer().method_73189();
         if (this.s != null) {
            double var18;
            var10000 = (var18 = var1.method_1025(this.s) - 256.0) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
            boolean var14 = false;
            int var13 = var10000;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               this.H();
               this.U = getMc().field_1687;
               this.t = this.getPlayer();
            }
         }

         this.s = var1;
         double var2 = qo.K();
         this.P.entrySet().removeIf(var2x -> var2 > var2x.getValue() || var2x.getKey().method_31481());
         this.v.E(var2, var0 -> ((class_1297)var0).method_31481());

         for (class_1297 var5 : getMc().field_1687.method_18112()) {
            if (var5 instanceof class_1309 var6 && !var5.method_31481() && !(var5.method_5858(this.getPlayer()) > 64.0)) {
               Object var7 = var6.method_6065();
               if (var7 == null) {
                  var7 = var6.method_66280();
               }

               int var8 = var5 == this.getPlayer() ? this.a.getValue() : (var7 == this.getPlayer() ? 1 : 0);
               if (var5 != this.getPlayer() && this.W.getValue()) {
                  var8 |= this.Y((class_1297)var7);
                  if (var8 == 0) {
                     for (class_1309 var10 : this.P.keySet()) {
                        if (var5.method_5858(var10) <= 16.0) {
                           var8 = 1;
                           break;
                        }
                     }
                  }
               }

               if (var5 == this.getPlayer() && !this.a.getValue()) {
                  this.v.X(var5.method_5628());
               }

               class_243 var15 = var5.method_73189();
               this.v
                  .U(
                     var5.method_5628(),
                     var5,
                     var6.method_6032(),
                     var15.field_1352,
                     var15.field_1351,
                     var15.field_1350,
                     var6.method_17682(),
                     (boolean)var8,
                     var2,
                     this.A.getValue() / 1000.0
                  );
            }
         }
      }
   }

   public void k(class_12075 var1) {
      if (this.isEnabled()) {
         if (this.y()) {
            if (this.V == null) {
               this.V = new im();
            }

            ArrayList var2 = new ArrayList();
            double var3 = qo.x();
            Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.field_63081);
            double var6 = Math.max(0.01, Math.abs(b9.v().m11()));
            double var8 = Math.max(1, getMc().method_22683().method_4502());

            for (ct.t var11 : this.v.Y(var3)) {
               double var12 = var11.x() - var1.field_63078.field_1352;
               double var14 = var11.y() - var1.field_63078.field_1351;
               double var16 = var11.z() - var1.field_63078.field_1350;
               double var18 = var12 * var5.x + var14 * var5.y + var16 * var5.z;
               if (!(var18 <= 0.05)) {
                  int var20 = this.B.G("Custom") ? this.M.getValue() : ct.s(var11.damage());
                  double var21 = 0.5 * var18 / (var8 * var6 * 0.01890625);
                  var2.add(new im.u(var11.text(), var11.x(), var11.y(), var11.z(), var21 * this.Z.getValue() * var11.scale(), ei.D(var20, var11.opacity())));
               }
            }

            this.V.R(var2, var1);
         }
      }
   }

   public void N(class_4587 var1, class_12075 var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.V != null && this.U == getMc().field_1687) {
            this.V.Y(var1, var2, true);
         }
      }
   }

   public void R() {
      if (this.V != null) {
         this.V.i();
      }
   }
}
