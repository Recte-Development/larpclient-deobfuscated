package com.larpclient.module;

import com.larpclient.b9;
import com.larpclient.c5;
import com.larpclient.eg;
import com.larpclient.ei;
import com.larpclient.gu;
import com.larpclient.he;
import com.larpclient.j2;
import com.larpclient.q8;
import com.larpclient.uw;
import com.larpclient.wi;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_5250;
import net.minecraft.class_9064;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class NametagsModule extends Module {
   private final l3 p = this.addSetting(new l3("Players", "Show for players", true));
   private final l3 f = this.addSetting(new l3("Self Nametag", "Show your own tag in third person or a detached camera", false));
   private final l3 C = this.addSetting(new l3("Mobs", "Show for hostile mobs", false));
   private final l3 Y = this.addSetting(new l3("Animals", "Show for animals", false));
   private final l3 M = this.addSetting(new l3("Health", "Show current health", true));
   private final l3 X = this.addSetting(new l3("Distance", "Show distance", true));
   private final l3 B = this.addSetting(new l3("Items", "Show armor and held-item icons above the name", true));
   private final lo R = this.addSetting(new lo("Range", "Max render range", 128.0, 8.0, 512.0, 8.0));
   private final lo J = this.addSetting(new lo("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1));
   private final lo o = this.addSetting(new lo("Height", "Gap above the entity", 0.25, 0.1, 1.5, 0.05));
   private final lo x = this.addSetting(new lo("Opacity", "Background opacity", 0.65, 0.0, 1.0, 0.05));
   private final l3 w = this.addSetting(new l3("Background", "Draw label background", true));
   private final Set<Integer> W = new HashSet<>();
   private static final int e = -15;
   private static final int I = 4;
   private static final int v = 3;
   private static final int j = 4;
   private static final float A = 0.75F;
   private static final float T = 13.0F;
   private static final int q = -15329768;
   private static final int g = -1;
   private static final int c = -4934470;
   private static final int l = -41892;

   public NametagsModule() {
      super("Nametags", "Show names, health and gear above entities", Category.RENDER, -1);
      this.x.O(this.w::getValue);
      HudElementRegistry.addLast(class_2960.method_60655("larp-client", "nametags"), this::z);
      this.f.a(var0 -> q8.v("Nametags", "Self nametag {} in third person or a detached camera", var0 ? "enabled" : "disabled"));
   }

   public boolean M(class_1297 var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.W.contains(var1.method_5628());
   }

   public List<lb<?>> n() {
      return List.of(this.p, this.f, this.C, this.Y);
   }

   public List<lb<?>> A() {
      return List.of(this.M, this.X, this.B);
   }

   public List<lb<?>> q() {
      return List.of(this.R, this.J, this.o, this.w, this.x);
   }

   @Override
   public void onEnable() {
      q8.v("Nametags", "Replacement labels suppress native names and below-name scoreboard text");
   }

   @Override
   public void onDisable() {
      this.W.clear();
   }

   private boolean f(boolean var1, boolean var2) {
      return var1 ? this.f.getValue() && var2 : this.p.getValue();
   }

   private void z(class_332 var1, class_9779 var2) {
      this.W.clear();
      if (this.isOperational() && this.isInGame() && !mc.field_1690.field_1842) {
         var1 = wi.p(var1, true, false);
         if (var1 != null) {
            long var3 = c5.X();
            class_4184 var5 = mc.field_1773.method_19418();
            class_243 var6 = var5.method_71156();
            Matrix4f var7 = b9.n(new Matrix4f());
            Vector3f var8 = var5.method_23767().transform(new Vector3f(0.0F, 0.025F * this.J.getValue().floatValue(), 0.0F));
            int var9 = mc.method_22683().method_4486();
            int var10 = mc.method_22683().method_4502();
            byte var11 = 0;
            float var12 = var2.method_60637(false);
            int var13 = 0;

            for (class_1297 var15 : mc.field_1687.method_18112()) {
               if (var15 instanceof class_1309 var16
                  && var16.method_5805()
                  && (
                     var15 instanceof class_1657
                        ? this.f(var15 == mc.field_1724, var5.method_19333())
                        : (var15 instanceof class_1569 ? this.C.getValue() : var15 instanceof class_1429 && this.Y.getValue())
                  )) {
                  double var17 = mc.field_1724.method_5739(var15);
                  if (!(var17 > this.R.getValue())) {
                     class_243 var19 = var15.method_30950(var12);
                     class_243 var20 = var15.method_56072().method_55675(class_9064.field_47745, 0, var15.method_61415(var12));
                     double var21 = Math.max(var15.method_17682(), var20 == null ? 0.0 : var20.field_1351);
                     eg.o var23 = eg.y(
                        var19.method_1031(var20 == null ? 0.0 : var20.field_1352, var21 + this.o.getValue(), var20 == null ? 0.0 : var20.field_1350),
                        var6,
                        var7,
                        var8,
                        var9,
                        var10,
                        (boolean)var11
                     );
                     if (var23 != null) {
                        if (++var13 > 128) {
                           break;
                        }

                        String var24 = this.M.getValue() ? String.format(Locale.ROOT, "%.1f", j2.T(var16)) : null;
                        String var25 = this.X.getValue() && var15 != mc.field_1724 ? (int)var17 + "m" : null;
                        var1.method_51448().pushMatrix();

                        try {
                           var1.method_51448().translate(var23.x(), var23.y()).scale(var23.scale());
                           class_5250 var26 = class_2561.method_43473()
                              .method_10852(uw.v(uw.n(he.n(var15.method_5667(), var15.method_5477().getString()), 160, uw.r.V), uw.r.V));
                           if (var24 != null) {
                              var26.method_10852(Y(" [", -4934470)).method_10852(Y(var24, -41892)).method_10852(Y("]", -4934470));
                           }

                           if (var25 != null) {
                              var26.method_10852(Y(" " + var25, -4934470));
                           }

                           int var27 = this.w.getValue() ? ei.D(-15329768, this.x.getValue()) : 0;
                           this.j(var1, var26, -15, -1, var27);
                           if (this.B.getValue()) {
                              ArrayList var28 = new ArrayList(6);

                              for (class_1304 var32 : new class_1304[]{
                                 class_1304.field_6173,
                                 class_1304.field_6169,
                                 class_1304.field_6174,
                                 class_1304.field_6172,
                                 class_1304.field_6166,
                                 class_1304.field_6171
                              }) {
                                 class_1799 var33 = var16.method_6118(var32);
                                 if (!var33.method_7960()) {
                                    var28.add(var33);
                                 }
                              }

                              float var46 = -32.0F;

                              for (int var47 = 0; var47 < var28.size(); var47++) {
                                 var1.method_51448().pushMatrix();

                                 try {
                                    var1.method_51448().translate((var47 - (var28.size() - 1) / 2.0F) * 13.0F - 6.0F, var46).scale(0.75F);
                                    var1.method_51423(var16, (class_1799)var28.get(var47), 0, 0, var15.method_5628());
                                 } finally {
                                    var1.method_51448().popMatrix();
                                 }
                              }
                           }

                           this.W.add(var15.method_5628());
                        } catch (RuntimeException var43) {
                           q8.O("Render", "Nametag submission failed", var43);
                        } finally {
                           var1.method_51448().popMatrix();
                        }
                     }
                  }
               }
            }

            c5.w("Nametags", var3);
         }
      }
   }

   private static class_5250 Y(String var0, int var1) {
      return uw.v(var0, uw.r.n).method_27661().method_54663(var1 & 16777215);
   }

   private void j(class_332 var1, class_2561 var2, int var3, int var4, int var5) {
      int var6 = mc.field_1772.method_27525(var2);
      int var7 = -var6 / 2;
      if (var5 != 0) {
         gu.N(var1, var7 - 4, var3 - 3, var6 + 8, 15.0F, 4.0F, var5);
      }

      var1.method_71048();
      var1.method_51439(mc.field_1772, var2, var7, var3, var4, var5 == 0);
   }
}
