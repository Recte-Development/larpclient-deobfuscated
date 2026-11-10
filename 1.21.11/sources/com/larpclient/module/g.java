package com.larpclient.module;

import com.larpclient.fk;
import com.larpclient.q8;
import com.larpclient.qt;
import com.larpclient.t9;
import com.larpclient.um;
import com.larpclient.uu;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_2350.class_2353;

public final class g extends fk.y implements uu.m {
   private final uu n = new uu(this, var1 -> this.addSetting(var1), 0.75, 0.35);
   private static g X;
   private final li M = this.addSetting(
      new li("Mode", "Legit aims smoothly; Blatant aims directly every tick. Both keep your view free", "Legit", "Legit", "Blatant")
   );
   private final li o = this.addSetting(
      new li("Blocks", "Hotbar trap building material", "Obsidian", "Obsidian", "Crying Obsidian", "Ender Chest", "Stone", "Oak Planks", "Held Block")
   );
   private final l3 V = this.O("Only Standing", "Pause self trapping while standing on the ground", true);
   private final li Y = this.addSetting(new li("Trap Mode", "Lower perimeter, upper perimeter or full enclosure", "Full", "Full", "Legs", "Head") {
      @Override
      public String getDisplayName() {
         return "Trap Pattern";
      }
   });
   private final l3 h = this.O("Trap Predict", "Predict opponent displacement before choosing the trap footprint", false);
   private final lo K = this.X("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final l3 w = this.O("Predict Sort", "Build blocks closest to the predicted opponent first", true);
   private final lo d = this.X("Placement Preview", "Ticks to show the next block before placing", 6.0, 2.0, 40.0, 1.0);
   private final lo a = this.X("Turn Speed", "Maximum player aim turn speed in degrees per second", 180.0, 30.0, 360.0, 15.0);
   private final um b = new um();
   private final qt I = new qt();
   private class_1657 k;
   private Object S;
   private Object Z;
   private class_2338 e;
   private int u;
   private class_243 x;

   @Override
   public uu placementHighlight() {
      return this.n;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.Z();
      this.n.d();
      q8.G(
         this.getName(),
         "Placement mode={}; Legit preview={} ticks, delay={} ticks, turn cap={} degrees/s; Blatant uses direct turns and one native action per tick; mouse look stays free and movement stays manual",
         this.M.getValue(),
         this.d.getValue(),
         this.q.getValue(),
         this.a.getValue()
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.Z();
      this.n.p();
   }

   private void Z() {
      wp.k(wp.z.v);
      this.b.W();
      this.I.H();
      this.k = null;
      this.e = null;
      this.S = this.Z = null;
      this.x = null;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (wp.h(wp.z.v)) {
            if (this.B()) {
               super.aim(var1, var2);
            } else {
               float[] var3 = this.I.p(mc.field_1724.field_6012, mc.field_1724.method_36454(), mc.field_1724.method_36455(), var1, var2, this.a.getValue());
               t9.X(t9.v.j, var3[0], var3[1], 1.0);
            }

            if (t9.k(t9.v.j)) {
               wp.S(wp.z.v);
            } else {
               wp.k(wp.z.v);
            }
         }
      }
   }

   public g() {
      super("AutoTrap", "Trap with smooth Legit or fast Blatant aim while looking freely", 6);
      this.M.a(var1 -> {
         fk.N(this);
         this.Z();
         this.n.p();
      });
      this.m.O(() -> !this.B());
      this.m.a(var1 -> {
         fk.N(this);
         this.Z();
      });
      this.q.O(() -> !this.B());
      this.d.O(() -> !this.B());
      this.a.O(() -> !this.B() && this.m.G("Visible"));
      X = this;
      this.K.O(this.h::getValue);
      this.w.O(this.h::getValue);
      this.V.O(() -> false);
   }

   public boolean B() {
      return this.M.G("Blatant");
   }

   private boolean s() {
      return this.B() || this.m.G("Visible");
   }

   public boolean x() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.s()
            && mc.field_1724 != null
            && this.S == mc.field_1687
            && this.Z == mc.field_1724
            && this.I(this.k)
            && (this.b.O() != null || this.e != null)
            && (!this.C.getValue() || !mc.field_1724.method_6115())
            && (!this.R.getValue() || !mc.field_1690.field_1886.method_1434());
   }

   public float B(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && wp.o() == wp.z.v && t9.k(t9.v.j)) {
            mc.field_1724.method_5847(mc.field_1724.method_36454());
            return mc.field_1724.method_36454();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public boolean x(int var1, boolean var2, boolean var3) {
      return !this.isEnabled() ? false : this.B() || var2 && this.b.H(var1, this.d.getValue().intValue(), var3);
   }

   public List<lb<?>> F() {
      return List.of(this.M, this.o, this.P, this.Y);
   }

   public List<lb<?>> R() {
      return List.of(this.r, this.m, this.y, this.C, this.R, this.q, this.d, this.a, this.h, this.K, this.w, this.V);
   }

   private boolean A(class_2338 var1, class_1792 var2) {
      return fk.U(var1, var2, this.P.getValue(), this.s()) != null;
   }

   public static g K() {
      return X;
   }

   public class_1792 c() {
      return switch ((String)this.o.getValue()) {
         case "Crying Obsidian" -> class_1802.field_22421;
         case "Ender Chest" -> class_1802.field_8466;
         case "Stone" -> class_1802.field_20391;
         case "Oak Planks" -> class_1802.field_8118;
         case "Held Block" -> mc.field_1724 != null && mc.field_1724.method_6047().method_7909() instanceof class_1747
            ? mc.field_1724.method_6047().method_7909()
            : class_1802.field_8162;
         default -> class_1802.field_8281;
      };
   }

   public List<class_2338> r(class_1657 var1) {
      if (var1 == mc.field_1724 && this.V.getValue() && var1.method_24828()) {
         return List.of();
      }

      class_243 var2 = V(var1, this.h.getValue() ? this.K.getValue() : 0.0);
      ArrayList var3 = new ArrayList<>(S(y(var1, var2), this.Y.getValue()));
      var3.sort(
         Comparator.comparingInt(class_2382::method_10264)
            .thenComparing(Comparator.<class_2338>comparingDouble(var0 -> class_243.method_24953(var0).method_1025(mc.field_1724.method_33571())).reversed())
      );
      if (this.h.getValue() && this.w.getValue()) {
         var3.sort(Comparator.comparingInt(class_2382::method_10264).thenComparingDouble(var1x -> class_243.method_24953(var1x).method_1025(var2)));
      }

      return var3;
   }

   public static List<class_2338> S(List<class_2338> var0, String var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      if (!var1.equals("Head")) {
         for (class_2338 var4 : var0) {
            var2.add(var4.method_10074());
         }
      }

      for (class_2338 var10 : var0) {
         for (class_2350 var6 : class_2353.field_11062) {
            class_2338 var7 = var10.method_10093(var6);
            if (!var0.contains(var7)) {
               var2.add(var7);
               if (!var1.equals("Legs")) {
                  var2.add(var7.method_10084());
               }
            }
         }
      }

      if (!var1.equals("Legs")) {
         for (class_2338 var11 : var0) {
            var2.add(var11.method_10086(2));
         }
      }

      return List.copyOf(var2);
   }

   public static List<class_2338> x(List<class_2338> var0) {
      if (var0.isEmpty()) {
         return List.of();
      }

      int var1 = var0.stream().mapToInt(class_2382::method_10264).max().orElse(0);
      LinkedHashSet var2 = new LinkedHashSet();

      for (class_2338 var4 : var0) {
         if (var4.method_10264() == var1) {
            for (class_2350 var6 : class_2353.field_11062) {
               class_2338 var7 = var4.method_10093(var6);
               if (!var0.contains(var7) && var0.contains(var7.method_10074())) {
                  var2.add(var7);
               }
            }
         }
      }

      return List.copyOf(var2);
   }

   public static List<class_2338> Z(List<class_2338> var0, class_243 var1) {
      return x(var0).stream().sorted(Comparator.<class_2338>comparingDouble(var1x -> class_243.method_24953(var1x).method_1025(var1)).reversed()).toList();
   }

   static class_243 V(class_1657 var0, double var1) {
      class_243 var3 = var0.method_18798();
      if (var3.method_37268() < 1.0E-8) {
         var3 = new class_243(var0.method_23317() - var0.field_6014, var0.method_23318() - var0.field_6036, var0.method_23321() - var0.field_5969);
      }

      return var0.method_73189().method_1019(var3.method_1021(var1));
   }

   static List<class_2338> y(class_1657 var0, class_243 var1) {
      class_238 var2 = var0.method_5829().method_997(var1.method_1020(var0.method_73189()));
      int var3 = (int)Math.floor(var1.field_1351 + 0.01);
      ArrayList var4 = new ArrayList(4);

      for (int var5 = (int)Math.floor(var2.field_1323 + 0.001); var5 <= (int)Math.floor(var2.field_1320 - 0.001); var5++) {
         for (int var6 = (int)Math.floor(var2.field_1321 + 0.001); var6 <= (int)Math.floor(var2.field_1324 - 0.001); var6++) {
            var4.add(new class_2338(var5, var3, var6));
         }
      }

      return var4;
   }

   private boolean u(List<class_2338> var1) {
      int var2 = var1.stream().mapToInt(class_2382::method_10264).max().orElse(0);
      return !this.Y.G("Legs") && var1.stream().anyMatch(var1x -> var1x.method_10264() == var2 && fk.v(var1x));
   }

   private boolean u(class_1657 var1, class_2338 var2) {
      List var3 = this.r(var1);
      return var3.contains(var2) || this.u(var3) && x(var3).contains(var2);
   }

   @Override
   public void onTick() {
      this.n.x(null);
      fk.N(this);
      if (!this.g()) {
         this.b.W();
         this.I.H();
      } else if (this.s() && wp.o() != wp.z.D && wp.o() != wp.z.v) {
         this.b.W();
         this.I.H();
         this.W("Waiting for camera control");
      } else {
         if (this.S != mc.field_1687 || this.Z != mc.field_1724 || this.x != null && this.x.method_1025(mc.field_1724.method_73189()) > 64.0) {
            this.Z();
         }

         this.S = mc.field_1687;
         this.Z = mc.field_1724;
         this.x = mc.field_1724.method_73189();
         if (!this.I(this.k)) {
            this.k = this.U();
            this.b.W();
            this.I.H();
         }

         if (this.k == null) {
            this.W("Waiting for a target");
         } else {
            class_1792 var1 = this.c();
            if (var1 != class_1802.field_8162) {
               int var10000 = fk.T(var1, this.y.getValue());
               byte var10 = -1;
               int var9 = var10000;
               var10000 = Integer.compare(var9, var10);
               if ((var10000 | -var10000) >>> 31 != 0) {
                  if (this.e != null) {
                     if (!fk.v(this.e)) {
                        this.e = null;
                     } else {
                        int var12 = this.u;
                        int var11 = mc.field_1724.field_6012;
                        var10000 = var11 - var12;
                        byte var14 = 10;
                        int var13 = var10000;
                        if ((Integer.compare(var13, var14) >>> 31 ^ 1) == 0) {
                           this.n.x(this.e);
                           this.W("Waiting for world update");
                           return;
                        }

                        this.e = null;
                        this.b.W();
                        this.W("Block update missing; previewing a retry");
                     }
                  }

                  List var2 = this.r(this.k);
                  ArrayList var3 = new ArrayList(var2);
                  if (this.u(var2)) {
                     var3.addAll(Z(var2, mc.field_1724.method_33571()));
                  }

                  class_2338 var4 = this.b.O();
                  if (var4 == null || !var3.contains(var4) || !this.A(var4, var1)) {
                     var4 = null;

                     for (class_2338 var6 : var3) {
                        if (this.A(var6, var1)) {
                           var4 = var6;
                           break;
                        }
                     }
                  }

                  if (var4 != null) {
                     class_2338 var18 = var4;
                     class_1657 var20 = this.k;
                     this.b.K(var18, var20, var1, mc.field_1724.field_6012);
                     this.n.x(var18);
                     class_3965 var7 = fk.U(var18, var1, this.P.getValue(), this.s());
                     if (var7 == null) {
                        this.b.W();
                        return;
                     }

                     boolean var8 = fk.m(
                        this,
                        var18,
                        var1,
                        this.P.getValue(),
                        this.s(),
                        this.y.getValue(),
                        () -> this.g() && this.k == var20 && this.I(var20) && this.c() == var1 && this.u(var20, var18),
                        () -> {
                           float[] var2x = t9.X(mc.field_1724.method_33571(), var7.method_17784());
                           int var3x = Math.abs(class_3532.method_15393(var2x[0] - mc.field_1724.method_36454())) < 2.0F
                                 && Math.abs(var2x[1] - mc.field_1724.method_36455()) < 2.0F
                              ? 1
                              : 0;
                           return (!this.s() || wp.o() == wp.z.v) && this.x(mc.field_1724.field_6012, this.e(), (boolean)var3x);
                        },
                        var2x -> {
                           this.V(var2x);
                           if (var2x) {
                              this.e = var18;
                              this.u = mc.field_1724.field_6012;
                           } else {
                              this.b.W();
                           }
                        }
                     );
                     if (var8) {
                        this.W("Preparing trap placement");
                     }

                     return;
                  }

                  this.b.W();
                  this.I.H();
                  boolean var17 = var2.stream().noneMatch(fk::v);
                  int var23 = this.Y.G("Legs");
                  boolean var16 = false;
                  byte var15 = (byte)var23;
                  var23 = Integer.signum(var15);
                  int var19 = (var23 | -var23) >>> 31 == 0
                        && var2.stream()
                           .filter(fk::v)
                           .allMatch(var1x -> var1x.method_10264() == var2.stream().mapToInt(class_2382::method_10264).max().orElse(0))
                     ? 1
                     : 0;
                  this.W(
                     var17
                        ? "Trap enclosure filled"
                        : (
                           var19 != 0
                              ? "Roof needs a higher view; jump or move to expose a supported face"
                              : "Move closer or around the target to reach the remaining trap faces"
                        )
                  );
                  return;
               }
            }

            this.b.W();
            this.I.H();
            this.W("Trap blocks missing from hotbar");
         }
      }
   }
}
