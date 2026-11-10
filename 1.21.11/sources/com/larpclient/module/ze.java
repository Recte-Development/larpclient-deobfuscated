package com.larpclient.module;

import com.larpclient.fe;
import com.larpclient.h5;
import com.larpclient.i2;
import com.larpclient.l2;
import com.larpclient.q8;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2636;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_8961;
import net.minecraft.class_8963;

public final class ze extends z8 {
   public static final int X = 64;
   public static final int A = 256;
   public static final int m = 24;
   public static final int O = 16;
   private final l3 z = this.addSetting(new l3("Trial Spawners", "Also find received trial spawners", false));
   private final l3 Z = this.addSetting(new l3("Spawner Boxes", "Outline exact received spawner positions", true));
   private final l3 W = this.addSetting(new l3("Spawner Labels", "Received mob type, coordinates and distance", true));
   private final l3 Y = this.addSetting(new l3("Range Rings", "16-block reference ring; server activation ranges can differ", true));
   private final l3 L = this.addSetting(new l3("Spawner Tracers", "Lines from the camera to exact spawner positions", false));
   private final List<h5.v> P = new ArrayList<>();
   private int v;
   private int d;
   private int V;

   public ze() {
      super("SpawnerFinder", "Highlight loaded spawners and their mob types");
      this.z.a(var1 -> this.m("spawner types changed"));
   }

   @Override
   public List<lb<?>> detectionSettings() {
      return List.of(this.z);
   }

   @Override
   public List<lb<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.addAll(List.of(this.Z, this.W, this.Y, this.L));
      return List.copyOf(var1);
   }

   public static boolean B(class_2680 var0) {
      return var0.method_27852(class_2246.field_10260);
   }

   @Override
   protected boolean candidate(class_2680 var1) {
      return B(var1) || this.z.getValue() && var1.method_27852(class_2246.field_47336);
   }

   @Override
   protected void beginScan() {
      this.P.clear();
   }

   @Override
   protected void clearEvidence() {
      this.P.clear();
   }

   @Override
   protected void count(class_2680 var1, int var2) {
      if (this.candidate(var1)) {
         this.d(var2);
      }
   }

   @Override
   protected void scanBlock(class_2818 var1, class_2680 var2, int var3, int var4, int var5) {
      if (this.candidate(var2)) {
         this.d(var4);
         if (this.P.size() < 64) {
            class_2338 var6 = new class_2338(var1.method_12004().method_8326() + var3, var4, var1.method_12004().method_8328() + var5);
            boolean var7 = var2.method_27852(class_2246.field_47336);
            String var8 = var7 ? "Trial Spawner" : "Spawner";

            try {
               class_2586 var9 = var1.method_8321(var6);
               class_1297 var10 = null;
               if (var9 instanceof class_2636 var11) {
                  var10 = var11.method_11390().method_8283(getMc().field_1687, var6);
               } else if (var9 instanceof class_8961 var12) {
                  class_8963 var13 = var12.method_55150();
                  var10 = var13.method_55174().method_55190(var13, getMc().field_1687, var12.method_55151());
               }

               if (var10 != null) {
                  var8 = var10.method_5864().method_5897().getString() + (var7 ? " Trial Spawner" : " Spawner");
               }
            } catch (RuntimeException var14) {
               q8.O("BaseFinding", "Spawner mob label unavailable; retaining generic name and exact position", var14);
            }

            this.P.add(new h5.v(var3, var4, var5, var7, var8));
         }
      }
   }

   @Override
   protected h5.n evidence() {
      h5.n var1 = super.evidence();
      return new h5.n(0, 0, var1.spawners(), var1.sampleY(), com.larpclient.z.EMPTY, fe.EMPTY, this.P);
   }

   @Override
   protected boolean qualifies(h5.n var1) {
      return var1.spawners() > 0;
   }

   @Override
   protected String describe(h5.n var1) {
      String var2 = var1.spawners() == 1 ? "" : "s";
      if (!var1.hits().isEmpty()) {
         h5.v var3 = var1.hits().getFirst();
         return var3.name() + " · " + var1.spawners() + " block" + var2 + " · Y:" + var3.y();
      } else {
         return var1.spawners() + " spawner block" + var2 + "; first at Y:" + var1.sampleY();
      }
   }

   @Override
   protected i2 alert(class_1923 var1, h5.n var2) {
      return i2.spawners(var1.method_8326(), var1.method_8328(), var2);
   }

   @Override
   protected void beginDraw() {
      this.v = this.d = this.V = 0;
   }

   @Override
   protected void drawEvidence(class_1923 var1, h5.n var2, l2 var3, int var4) {
      for (h5.v var6 : var2.hits()) {
         if (this.v >= 256) {
            break;
         }

         class_2338 var7 = new class_2338(var1.method_8326() + var6.x(), var6.y(), var1.method_8328() + var6.z());
         class_2680 var8 = getMc().field_1687.method_8320(var7);
         if (var6.trial() ? this.z.getValue() && var8.method_27852(class_2246.field_47336) : B(var8)) {
            this.v++;
            class_243 var9 = class_243.method_24953(var7);
            if (this.Z.getValue()) {
               var3.X(new class_238(var7).method_1014(0.03), "Box", var4);
            }

            if (this.W.getValue() && this.d++ < 24) {
               var3.n(
                  var9.method_1031(0.0, 1.0, 0.0),
                  var6.name()
                     + " · "
                     + Math.round(var9.method_1022(getMc().field_1724.method_73189()))
                     + "m · "
                     + var7.method_10263()
                     + ", "
                     + var7.method_10264()
                     + ", "
                     + var7.method_10260(),
                  var4
               );
            }

            if (this.L.getValue()) {
               var3.O(getMc().field_1773.method_19418().method_71156(), var9, var4);
            }

            if (this.Y.getValue() && this.V++ < 16) {
               for (int var10 = 0; var10 < 48; var10++) {
                  double var11 = var10 * Math.PI * 2.0 / 48.0;
                  double var13 = (var10 + 1) * Math.PI * 2.0 / 48.0;
                  var3.O(
                     var9.method_1031(Math.cos(var11) * 16.0, 0.0, Math.sin(var11) * 16.0),
                     var9.method_1031(Math.cos(var13) * 16.0, 0.0, Math.sin(var13) * 16.0),
                     var4
                  );
               }
            }
         }
      }
   }

   @Override
   protected int tint() {
      return -943189;
   }
}
