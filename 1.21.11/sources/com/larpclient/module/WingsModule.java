package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.c5;
import com.larpclient.cw;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.gv;
import com.larpclient.ho;
import com.larpclient.i9;
import com.larpclient.q8;
import com.larpclient.qo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.class_10017;
import net.minecraft.class_10034;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_4587.class_4665;
import org.joml.Matrix4f;

public final class WingsModule extends Module {
   public static final String h = "Luminous";
   public static final String I = "Ender Dragon";
   private static WingsModule E;
   private final l3 O = this.addSetting(new l3("Self", "Show your wings in third person", true));
   private final l3 D = this.addSetting(new l3("Friends", "Show wings on friends", true));
   private final l3 T = this.addSetting(new l3("Others", "Show wings on other players", false));
   private final lo x = this.addSetting(new lo("Size", "Wing size", 1.0, 0.75, 1.35, 0.05));
   private final l3 Q = this.addSetting(new l3("Custom Colors", "Cycle between your two colors", true));
   private final l0 p = this.addSetting(new l0("Color 1", "First wing color", -52586));
   private final l0 e = this.addSetting(new l0("Color 2", "Second wing color", -13461761));
   private final li o = this.addSetting(new li("Style", "Wing design", "Luminous", "Luminous", "Ender Dragon"));
   private final l0 r = this.addSetting(new l0("Dragon Color", "Tints the Ender Dragon texture; white keeps it vanilla and alpha sets opacity", -1));
   public static final int X = 32;
   private List<WingsModule.o> y = List.of();
   private List<WingsModule.u> S = List.of();
   private final IdentityHashMap<Object, ho.b> P = new IdentityHashMap<>();
   private class_638 W;
   private class_638 J;
   private i9.g i;
   private final class_4587 k = new class_4587();
   private final class_4587 K = new class_4587();

   public WingsModule() {
      super("Wings", "Wear luminous membrane wings or Ender Dragon wings", Category.VISUALS, -1);
      E = this;
      this.Q.O(() -> this.o.G("Luminous"));
      this.p.O(() -> this.o.G("Luminous") && this.Q.getValue());
      this.e.O(() -> this.o.G("Luminous") && this.Q.getValue());
      this.r.O(() -> this.o.G("Ender Dragon"));
      this.o.a(var0 -> q8.G("Wings", "Style={}", var0));
   }

   public static WingsModule B() {
      return E;
   }

   public List<lb<?>> r() {
      return List.of(this.O, this.D, this.T);
   }

   public List<lb<?>> A() {
      return List.of(this.o, this.x, this.Q, this.p, this.e, this.r);
   }

   public boolean j() {
      return this.isEnabled() ? this.o.G("Ender Dragon") : false;
   }

   public WingsModule.g X() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.O.getValue()) {
            return null;
         } else if (this.j()) {
            return this.r.getValue() >>> 24 == 0 ? null : new WingsModule.g(true, this.x.getValue().floatValue(), 0, 0, this.r.getValue());
         } else {
            return new WingsModule.g(
               false, this.x.getValue().floatValue(), this.Q.getValue() ? this.p.getValue() : ez.a, this.Q.getValue() ? this.e.getValue() : ez.f, 0
            );
         }
      } else {
         return null;
      }
   }

   public static int D(int var0, int var1, double var2) {
      return ei.W(var0, var1, (Math.sin(var2 * 2.0) + 1.0) / 2.0);
   }

   public static int Z(int var0, int var1, double var2) {
      return ei.W(var0, var1, (Math.sin(var2 * 2.0 + 2.1) + 1.0) / 2.0);
   }

   @Override
   public void onEnable() {
      q8.G(
         "Wings",
         this.j()
            ? "Ender Dragon wings enabled; vanilla dragon wing parts on the native torso, 48 quads per wearer, 32 nearby previews"
            : "Articulated membrane wings enabled on the native torso; shared bloom, 146 base quads per wearer, 32 nearby previews"
      );
   }

   @Override
   public void onDisable() {
      this.y = List.of();
      this.S = List.of();
      this.W = null;
      this.P.clear();
      this.J = null;
   }

   public void d(float var1) {
      if (this.isEnabled()) {
         this.y = List.of();
         this.S = List.of();
         this.W = null;
         if (this.isOperational() && this.isInGame()) {
            if (this.J != mc.field_1687) {
               this.P.clear();
               this.J = mc.field_1687;
            }

            MCFriendModule var2 = LarpClient.b().G().b(MCFriendModule.class);
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Set var5 = Collections.newSetFromMap(new IdentityHashMap());
            int var6 = 0;
            float var7 = this.x.getValue().floatValue();
            boolean var8 = this.j();
            int var9 = this.r.getValue();
            int var10 = this.Q.getValue() ? this.p.getValue() : ez.a;
            int var11 = this.Q.getValue() ? this.e.getValue() : ez.f;
            int var12 = D(var10, var11, qo.x());
            int var13 = Z(var10, var11, qo.x());

            for (class_742 var15 : mc.field_1687.method_18456()) {
               if (var15 == mc.field_1724) {
                  if (!this.O.getValue() || mc.field_1690.method_31044().method_31034()) {
                     continue;
                  }
               } else {
                  int var16 = var2 != null && var2.M(var15.method_5477().getString()) ? 1 : 0;
                  if (!var16 != 0 ? this.D.getValue() : this.T.getValue() || var6 >= 32) {
                     continue;
                  }
               }

               if (var15.method_5805()
                  && !var15.method_5767()
                  && !var15.method_7325()
                  && !var15.method_6113()
                  && !var15.method_5799()
                  && !var15.method_20448()
                  && !var15.method_6118(class_1304.field_6174).method_31574(class_1802.field_8833)
                  && !(var15.method_5858(mc.field_1724) > 9216.0)) {
                  if (var15 != mc.field_1724) {
                     var6++;
                  }

                  float var30 = Feature3DUIModule.n(var15, class_3532.method_17821(var1, var15.field_6220, var15.field_6283), var1);
                  float var17 = var15.field_6012 + var1;
                  float var18 = var15.field_42108.method_48570(var1);
                  class_243 var19 = var15.method_30950(var1);
                  ho.b var20 = this.P.computeIfAbsent(var15, var0 -> new ho.b());
                  var20.R(var17, var15.method_18276(), var18, var15.method_6128(), T(var15), var19);
                  var5.add(var15);
                  if (var8) {
                     float var31 = i9.s(var20.k(), var15.method_6128(), var15.method_6003() + var1);
                     var20.i(i9.v(var20.N(), var20.x(), var20.T(), var31));
                     if (var9 >>> 24 != 0) {
                        i9.m var32 = i9.y(var20.V() + i9.P(var15.method_5628()), var20.N(), var20.x(), var20.T(), var31);
                        var4.add(new WingsModule.u(gv.i(var15, var1), var32, var7, var9));
                     }
                  } else {
                     class_10017 var21 = gv.i(var15, var1);
                     class_4665 var22 = gv.s(var21, this.K);
                     int var23 = var21 instanceof class_10034 var24 && var24.field_53410 ? 1 : 0;
                     float var33 = var22 == null ? var20.k() : i9.s(var20.k(), var15.method_6128(), var15.method_6003() + var1);
                     ho.z var25 = ho.O(var20.x(), var33, var15.method_6003() + var1, var15.method_5695(var1));
                     ho.x var26 = ho.M(var17, var20.N(), var20.x(), var33);
                     class_243 var27 = var22 == null ? var19 : mc.field_1773.method_19418().method_71156();
                     Matrix4f var28 = var22 == null
                        ? ho.o(var25, var30, var17, var20.N(), var7, -1)
                        : new Matrix4f(var22.method_23761()).mul(ho.d(var25, var17, var20.N(), var7, -1, (boolean)var23));
                     Matrix4f var29 = var22 == null
                        ? ho.o(var25, var30, var17, var20.N(), var7, 1)
                        : new Matrix4f(var22.method_23761()).mul(ho.d(var25, var17, var20.N(), var7, 1, (boolean)var23));
                     var3.add(new WingsModule.o(ho.e(var27, var28, -1, var26, var12, var13, var7), ho.e(var27, var29, 1, var26, var12, var13, var7)));
                  }
               }
            }

            this.P.keySet().removeIf(var1x -> !var5.contains(var1x));
            this.W = mc.field_1687;
            this.y = List.copyOf(var3);
            this.S = List.copyOf(var4);
         } else {
            this.P.clear();
            this.J = null;
         }
      }
   }

   private static boolean T(class_1657 var0) {
      return !var0.method_24828() && !var0.method_5765() && !var0.method_6101() && !var0.method_6128();
   }

   public boolean L(class_4587 var1, cw var2) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational() && this.W == mc.field_1687 && !this.S.isEmpty()) {
         if (this.i == null) {
            this.i = i9.g.U();
            q8.G("Wings", "Ender Dragon wing parts ready from the vanilla layer definition; texture={}", i9.k);
         }

         long var3 = c5.X();
         byte var5 = 0;

         for (WingsModule.u var7 : this.S) {
            class_4665 var8 = gv.s(var7.state, var1);
            if (var8 != null) {
               this.k.method_23760().method_66521(var8);
               this.i.d(var7.angles);
               int var9 = var7.state instanceof class_10034 var10 && var10.field_53410 ? 1 : 0;
               this.i.t(this.k, var2.N(i9.z(var7.color)), var7.size, (boolean)var9, var7.state.field_61820, var7.color);
               var5 = 1;
            }
         }

         c5.w("Wings Ender Dragon attachment and submission", var3);
         return (boolean)var5;
      } else {
         return false;
      }
   }

   public void C(f1 var1, cw var2) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.W == mc.field_1687) {
            for (ho.t var6 : ho.t.values()) {
               for (WingsModule.o var8 : this.y) {
                  ho.B(var1, var8.left, var6);
                  ho.B(var1, var8.right, var6);
               }

               var2.P();
            }
         }
      }
   }

   public boolean k() {
      return !this.isEnabled() ? false : this.isOperational() && this.W == mc.field_1687 && this.y.stream().anyMatch(var0 -> ho.v(var0.left));
   }

   public void i(f1 var1, cw var2) {
      if (this.isEnabled()) {
         if (this.k()) {
            for (WingsModule.o var4 : this.y) {
               ho.X(var1, var4.left);
               ho.X(var1, var4.right);
            }

            var2.P();
         }
      }
   }

   public record g(boolean dragon, float size, int first, int second, int dragonColor) {
   }

   private record o(ho.o left, ho.o right) {
   }

   private record u(class_10017 state, i9.m angles, float size, int color) {
   }
}
