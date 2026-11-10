package com.larpclient.module;

import com.larpclient.co;
import com.larpclient.fv;
import com.larpclient.gs;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.sl;
import com.larpclient.un;
import com.larpclient.w1;
import com.larpclient.w6;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3489;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import net.minecraft.class_6880.class_6883;

public final class SwordModelsModule extends Module {
   public final li e = this.addSetting(
      new li("Model", "First-person sword replacement", "Bayonet", "Bayonet", "Stiletto", "Butterfly", "Luxury Watch", "Ledger Nano S")
   );
   public final li g = this.addSetting(new li("Finish", "Moving galaxy colors on the blade", "Galaxy", "Galaxy", "Violet", "Rose", "Ice", "Steel"));
   public final lo R = this.addSetting(new lo("Model Scale", "Knife size or watch case size", 1.0, 0.7, 1.3, 0.05));
   public final lo N = this.addSetting(new lo("Hand X", "Horizontal hand offset", 0.0, -0.2, 0.2, 0.01));
   public final lo W = this.addSetting(new lo("Hand Y", "Vertical hand offset", 0.0, -0.2, 0.2, 0.01));
   public final lo O = this.addSetting(new lo("Hand Z", "Depth of the hand", 0.0, -0.2, 0.15, 0.01));
   public final lw r = this.addSetting(new lw("Inspect Key", "Inspect main-hand knife or watch, or offhand if main hand is not a sword", 71));
   public final lo c = this.addSetting(new lo("Inspect Speed", "2 matches the reference showcase tempo", 2.0, 0.5, 2.0, 0.05));
   public final l3 U = this.addSetting(new l3("Idle Motion", "Subtle breathing motion", true));
   public final l3 Y = this.addSetting(new l3("Enchantment Glint", "Show enchantment shimmer on the model geometry", true));
   private final l3 b = this.addSetting(new l3("Reset Defaults", "Restore all Sword Skins settings", false));
   private static SwordModelsModule q;
   private final sl[] J = new sl[]{new sl(), new sl()};
   private final un[] D = new un[]{new un(), new un()};
   private final fv[] l = new fv[]{new fv(), new fv()};
   private final gs[] h = new gs[]{new gs(), new gs()};
   private final class_1799[] v = new class_1799[]{class_1799.field_8037, class_1799.field_8037};
   private final class_1799[] n = new class_1799[]{class_1799.field_8037, class_1799.field_8037};
   private final Object[] A = new Object[]{new Object(), new Object()};
   private final Set<String> C = new HashSet<>();
   private int i = -1;

   public SwordModelsModule() {
      super("Sword Models", "Replace held swords with CS:GO knives, a Ledger or a wristwatch", Category.VISUALS, -1);
      q = this;
      this.g.O(() -> !this.R().v());
      this.b.O(() -> false);
      this.r.O(() -> this.R().f());
      this.c.O(() -> this.R().f());
      this.U.O(() -> !this.R().v());
      this.e.a(var1 -> this.x());
      this.b.a(var1 -> {
         if (var1) {
            for (lb var3 : this.getSettings()) {
               var3.T();
            }

            this.x();
         }
      });
   }

   @Override
   public String getDisplayName() {
      return "Sword Skins";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("CS:GO Swords", "Knives");
   }

   public static SwordModelsModule X() {
      return q;
   }

   public static boolean F(class_1799 var0) {
      return var0 != null && !var0.method_7960() && (var0.method_31573(class_3489.field_42611) || G(var0) != null);
   }

   private static class_2960 G(class_1799 var0) {
      if (var0.method_31573(class_3489.field_42611)) {
         return null;
      }

      class_2960 var1 = (class_2960)var0.method_58694(class_9334.field_54199);
      if (var1 == null) {
         return null;
      }

      Optional var2 = class_7923.field_41178.method_10223(var1);
      return var2.isPresent() && ((class_6883)var2.get()).method_40220(class_3489.field_42611) ? var1 : null;
   }

   public static boolean C(class_1799 var0) {
      return G(var0) != null;
   }

   public static boolean Z(class_1799 var0, class_1799 var1) {
      return F(var0) && F(var1) && (class_1799.method_7984(var0, var1) || G(var0) != null || G(var1) != null);
   }

   public Object x(class_1268 var1, class_1799 var2) {
      int var3 = var1 == class_1268.field_5808 ? 0 : 1;
      if (var2 != this.n[var3]) {
         if (!Z(this.n[var3], var2)) {
            this.A[var3] = new Object();
         }

         this.n[var3] = var2;
      }

      return this.A[var3];
   }

   public co R() {
      return co.o(this.e.getValue());
   }

   public sl o(class_1268 var1) {
      return this.J[var1 == class_1268.field_5808 ? 0 : 1];
   }

   public un I(class_1268 var1) {
      return this.D[var1 == class_1268.field_5808 ? 0 : 1];
   }

   public fv Y(class_1268 var1) {
      return this.l[var1 == class_1268.field_5808 ? 0 : 1];
   }

   public gs F(class_1268 var1) {
      return this.h[var1 == class_1268.field_5808 ? 0 : 1];
   }

   private void P(int var1) {
      this.J[var1].e();
      this.D[var1].J();
      this.l[var1].S();
      this.h[var1].l();
   }

   public boolean b(class_1799 var1) {
      return !this.isEnabled() ? false : this.isOperational() && F(var1) && w1.P(this.R()) != null;
   }

   public int u() {
      return switch ((String)this.g.getValue()) {
         case "Violet" -> 1;
         case "Rose" -> 2;
         case "Ice" -> 3;
         case "Steel" -> 4;
         default -> 0;
      };
   }

   private void x() {
      for (int var1 = 0; var1 < this.J.length; var1++) {
         this.P(var1);
      }

      this.v[0] = this.v[1] = class_1799.field_8037;
      this.n[0] = this.n[1] = class_1799.field_8037;
      this.i = -1;
   }

   @Override
   public void onEnable() {
      this.x();
      this.C.clear();
   }

   @Override
   public void onDisable() {
      this.x();
   }

   @Override
   public void onTick() {
      class_746 var1 = this.getPlayer();
      if (var1 == null) {
         this.x();
      } else {
         int var2 = var1.method_31548().method_67532();
         int var11 = this.i;
         int var10 = var2;
         int var10000 = Integer.compare(var10, var11);
         int var3 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? 1 : 0;
         boolean var13 = false;
         int var12 = var3;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.P(0);
            this.i = var2;
         }

         for (class_1268 var7 : class_1268.values()) {
            int var8 = var7 == class_1268.field_5808 ? 0 : 1;
            class_1799 var9 = var1.method_5998(var7);
            if (var9 != this.v[var8] && (var3 == 0 || var7 != class_1268.field_5808)) {
               this.N(var7, this.v[var8], var9);
            }

            if (var9 != this.v[var8] && !Z(this.v[var8], var9) || !F(var9)) {
               this.P(var8);
            }

            this.v[var8] = var9;
            if (w6.z(var1) == var7) {
               this.J[var8].L();
            }
         }
      }
   }

   private void N(class_1268 var1, class_1799 var2, class_1799 var3) {
      if (F(var2) && !var3.method_7960()) {
         class_2960 var4 = G(var3);
         if (var4 != null || !F(var3)) {
            String var5 = class_7923.field_41178.method_10221(var3.method_7909()).toString();
            class_2960 var6 = (class_2960)var3.method_58694(class_9334.field_54199);
            if (this.C.add(var1 + "|" + var5 + "|" + var6)) {
               if (var4 != null) {
                  q8.G("SwordModels", "{} sword swapped to {} drawn with sword model {}; keeping the skin", var1, var5, var4);
               } else {
                  q8.G(
                     "SwordModels",
                     "{} sword swapped to {} (item model {}, use {}, using {}); not a sword model, vanilla draws it",
                     var1,
                     var5,
                     var6,
                     var3.method_7976(),
                     this.getPlayer().method_6115()
                  );
               }
            }
         }
      }
   }

   public void l(class_1268 var1, class_1799 var2) {
      String var3 = class_7923.field_41178.method_10221(var2.method_7909()).toString();
      if (this.C.add("use|" + var1 + "|" + var3)) {
         q8.G("SwordModels", "{} {} is being used ({}); vanilla draws its use pose instead of {}", var1, var3, var2.method_7976(), this.R().L);
      }
   }

   public void o(int var1) {
      if (this.isEnabled()) {
         class_310 var2 = getMc();
         int var3 = var1 == this.r.getValue()
               && this.isOperational()
               && var2.field_1724 != null
               && var2.field_1755 == null
               && var2.method_1569()
               && !var2.method_1493()
            ? 1
            : 0;
         if (var3 != 0) {
            class_746 var4 = var2.field_1724;
            class_1268 var5 = F(var4.method_6047()) ? class_1268.field_5808 : class_1268.field_5810;
            if (this.R().f()
               && this.b(var4.method_5998(var5))
               && !w6.l(var4)
               && !var4.method_6115()
               && this.o(var5).o(qo.x(), this.c.getValue())
               && this.R() == co.F) {
               q8.v("SwordModels", "Watch inspect started in {} at speed {}", var5, this.c.getValue());
            }
         }
      }
   }
}
