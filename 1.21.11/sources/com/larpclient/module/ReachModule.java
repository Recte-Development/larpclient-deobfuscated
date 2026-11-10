package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.fw;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_1132;
import net.minecraft.class_1297;
import net.minecraft.class_1324;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_3222;
import net.minecraft.class_5134;
import net.minecraft.class_746;
import net.minecraft.server.MinecraftServer;

public class ReachModule extends Module {
   private final lo K = this.addSetting(new lo("Combat", "Combat interaction range", 3.5, 3.0, 6.0, 0.1));
   private final lo n = this.addSetting(new lo("Block", "Block interaction range", 4.5, 4.5, 8.0, 0.1));
   private final li l = this.addSetting(new li("Mode", "Fixed or nearby-target-limited range", "Normal", "Normal", "Dynamic"));
   private final l3 a = this.addSetting(new l3("OnlySword", "Only while holding a supported weapon (includes axes, trident and mace)", false));
   private static ReachModule Z;
   private final fw x = new fw();
   private final fw E = new fw();
   private ReachModule.i m;

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public ReachModule() {
      super("Reach", "Adjust attack and block reach", Category.COMBAT, -1);
      Z = this;
   }

   public static ReachModule b() {
      return Z;
   }

   public static boolean W() {
      class_746 var0 = getMc().field_1724;
      if (var0 == null) {
         return false;
      }

      class_1799 var1 = var0.method_6047();
      return var1.method_31574(class_1802.field_8091)
         || var1.method_31574(class_1802.field_8528)
         || var1.method_31574(class_1802.field_8371)
         || var1.method_31574(class_1802.field_8845)
         || var1.method_31574(class_1802.field_8802)
         || var1.method_31574(class_1802.field_22022)
         || var1.method_31574(class_1802.field_8406)
         || var1.method_31574(class_1802.field_8062)
         || var1.method_31574(class_1802.field_8475)
         || var1.method_31574(class_1802.field_8825)
         || var1.method_31574(class_1802.field_8556)
         || var1.method_31574(class_1802.field_22025)
         || var1.method_31574(class_1802.field_8547)
         || var1.method_31574(class_1802.field_49814);
   }

   private boolean e() {
      return this.isInGame();
   }

   private boolean w() {
      return this.isRuntimeActive() && this.e() && (!this.a.getValue() || W());
   }

   @Override
   public String getStatus() {
      return this.w() ? "Configured range active" : "Waiting for player or weapon condition";
   }

   @Override
   public void onTick() {
      int var10000 = this.w();
      boolean var11 = false;
      byte var10 = (byte)var10000;
      var10000 = Integer.signum(var10);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.onDisable();
      } else {
         double var1 = this.K.getValue();
         int var18 = this.l.G("Dynamic");
         boolean var13 = false;
         byte var12 = (byte)var18;
         var18 = Integer.signum(var12);
         if (((var18 | -var18) >>> 31 ^ 1) == 0) {
            double var3 = var1;

            for (class_1297 var6 : mc.field_1687.method_18112()) {
               if (var6 != mc.field_1724 && var6.method_5805() && var6.method_5863()) {
                  var3 = Math.min(var3, cr.M(var6) + 0.5);
               }
            }

            var1 = Math.max(3.0, var3);
         }

         this.x.r(mc.field_1724.method_5996(class_5134.field_47759), var1);
         this.E.r(mc.field_1724.method_5996(class_5134.field_47758), this.n.getValue());
         class_1132 var14 = mc.method_1576();
         if (var14 == null) {
            this.y();
         } else {
            if (this.m == null || this.m.a != var14) {
               this.y();
               this.m = new ReachModule.i(var14);
            }

            ReachModule.i var4 = this.m;
            UUID var15 = mc.field_1724.method_5667();
            double var16 = var1;
            double var8 = this.n.getValue();
            var14.execute(() -> {
               class_3222 var7 = var14.method_3760().method_14602(var15);
               if (var4.N && var7 != null && var7.method_5805()) {
                  var4.Q.r(var7.method_5996(class_5134.field_47759), var16);
                  var4.q.r(var7.method_5996(class_5134.field_47758), var8);
               } else {
                  var4.g();
               }
            });
         }
      }
   }

   @Override
   public void onDisable() {
      this.x.A();
      this.E.A();
      this.y();
   }

   private void y() {
      ReachModule.i var1 = this.m;
      this.m = null;
      if (var1 != null) {
         var1.N = (boolean)0;
         var1.a.execute(var1::g);
      }
   }

   public void w(class_1324 var1) {
      this.x.S(var1);
      this.E.S(var1);
   }

   public double K() {
      return getMc().field_1724 == null ? 3.0 : getMc().field_1724.method_55755();
   }

   public double f() {
      return this.K();
   }

   public double d() {
      return this.K();
   }

   public double b() {
      return getMc().field_1724 == null ? 4.5 : getMc().field_1724.method_55754();
   }

   public boolean Z(class_1297 var1) {
      return cr.B(var1, this.K());
   }

   private static final class i {
      final MinecraftServer a;
      final fw Q = new fw();
      final fw q = new fw();
      volatile boolean N = (boolean)1;

      i(MinecraftServer var1) {
         this.a = var1;
      }

      void g() {
         this.Q.A();
         this.q.A();
      }
   }
}
