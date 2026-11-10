package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1132;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_5218;
import net.minecraft.class_5250;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class WaypointsModule extends Module {
   private static final int v = 64;
   private static final int[] B = new int[]{-8600321, -30070, -7347266, -14249, -4291585, -24728, -723723};
   private static final int r = -1;
   private static final int y = 14211288;
   private static final int n = 1073741824;
   private static final int j = 5;
   private final com.larpclient.module.l3 G = this.addSetting(
      new com.larpclient.module.l3("Beams", "Draw a vertical beam at each waypoint, visible through walls", true)
   );
   private final com.larpclient.module.l3 F = this.addSetting(new com.larpclient.module.l3("Tracers", "Draw a line from your view to each waypoint", false));
   private final com.larpclient.module.l3 d = this.addSetting(new com.larpclient.module.l3("Labels", "Show each waypoint's name on screen", true));
   private final com.larpclient.module.l3 V = this.addSetting(new com.larpclient.module.l3("Distance", "Show how far away each waypoint is", true));
   private final com.larpclient.module.lo T = this.addSetting(new com.larpclient.module.lo("Label Scale", "Size of the on-screen labels", 1.0, 0.5, 2.0, 0.1));
   private final com.larpclient.module.lw s = this.addSetting(
      new com.larpclient.module.lw("Add Waypoint Key", "Press in game to add a waypoint where you stand", -1)
   );
   private l7 Y;
   private boolean X;
   private Object S;
   private String A;

   public WaypointsModule() {
      super("Waypoints", "Save your own locations and see them in the world", Category.RENDER, -1);
      this.V.O(this.d::getValue);
      this.T.O(this.d::getValue);
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.u());
      HudElementRegistry.addLast(class_2960.method_60655("larp-client", "waypoints"), this::P);
   }

   public List<com.larpclient.module.lb<?>> s() {
      return List.of(this.G, this.F, this.d, this.V, this.T);
   }

   public List<com.larpclient.module.lb<?>> v() {
      return List.of(this.s);
   }

   private l7 N() {
      if (this.Y == null) {
         this.Y = new l7(FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("waypoints.json"));
      }

      return this.Y;
   }

   public String e() {
      if (!this.isInGame()) {
         this.S = null;
         this.A = null;
         return null;
      }

      if (mc.field_1687 != this.S) {
         this.S = mc.field_1687;
         class_1132 var1 = mc.method_1576();
         if (var1 != null) {
            this.A = l7.t(String.valueOf(var1.method_27050(class_5218.field_24188).normalize().getFileName()));
         } else {
            this.A = l7.e(mc.method_1558() == null ? null : mc.method_1558().field_3761);
         }
      }

      return this.A;
   }

   public boolean K() {
      return this.isInGame() && mc.method_1576() == null;
   }

   public List<cy> Z() {
      String var1 = this.e();
      return var1 == null ? List.of() : this.N().l(var1);
   }

   public Optional<cy> R(String var1) {
      String var2 = this.e();
      return var2 == null ? Optional.empty() : this.N().W(var2, var1);
   }

   public boolean y() {
      String var1 = this.e();
      return var1 != null && this.N().u(var1);
   }

   public String p() {
      return this.N().u();
   }

   public int C() {
      return B[this.Z().size() % B.length];
   }

   public String C() {
      return this.isInGame() ? qp.N(this.D()) : "";
   }

   private qp.w D() {
      class_2338 var1 = this.getPlayer().method_24515();
      return new qp.w(var1.method_10263(), var1.method_10264(), var1.method_10260());
   }

   private String z() {
      return mc.field_1687.method_27983().method_29177().toString();
   }

   public String D(String var1, String var2, int var3) {
      String var4 = this.e();
      if (var4 == null) {
         return "Join a world first";
      }

      try {
         qp.w var5 = qp.b(var2, this.D());
         String var6 = cy.cleanName(var1);
         this.N().S(var4, new cy(UUID.randomUUID().toString(), var6.isEmpty() ? this.Z() : var6, var5.x(), var5.y(), var5.z(), this.z(), var3, true));
         q8.G("Waypoints", "Added a waypoint in {}", this.z());
         return this.N().u();
      } catch (IllegalArgumentException var7) {
         return var7.getMessage();
      }
   }

   private String Z() {
      HashSet var1 = new HashSet();

      for (cy var3 : this.Z()) {
         var1.add(var3.name());
      }

      int var4 = this.Z().size() + 1;

      while (var1.contains("Waypoint " + var4)) {
         var4++;
      }

      return "Waypoint " + var4;
   }

   public String R(String var1, String var2, String var3, int var4) {
      String var5 = this.e();
      cy var6 = this.R(var1).orElse(null);
      if (var5 != null && var6 != null) {
         String var7 = cy.cleanName(var2);
         if (var7.isEmpty()) {
            return "Enter a name";
         }

         try {
            this.N().Q(var5, var6.edited(var7, qp.b(var3, var6.position()), var4));
            q8.G("Waypoints", "Edited a waypoint in {}", var6.dimension());
            return this.N().u();
         } catch (IllegalArgumentException var9) {
            return var9.getMessage();
         }
      } else {
         return "That waypoint no longer exists";
      }
   }

   public void b(String var1, boolean var2) {
      String var3 = this.e();
      this.R(var1).ifPresent(var3x -> this.N().Q(var3, var3x.withVisible(var2)));
   }

   public void t(String var1) {
      String var2 = this.e();
      if (var2 != null && this.N().S(var2, var1)) {
         q8.G("Waypoints", "Deleted a waypoint");
      }
   }

   public String O(cy var1) {
      String var2 = qp.N(var1.position());
      if (!this.isInGame()) {
         return var2;
      } else {
         return !var1.dimension().equals(this.z())
            ? var2 + "  ·  " + cy.dimensionLabel(var1.dimension())
            : var2 + "  ·  " + qp.Z(Math.sqrt(this.getPlayer().method_5649(var1.x() + 0.5, var1.y() + 0.5, var1.z() + 0.5)));
      }
   }

   @Override
   public void onDisable() {
      this.X = (boolean)0;
   }

   @Override
   public void onTick() {
      int var13;
      label23: {
         int var1 = this.s.getValue();
         byte var4 = -1;
         int var3 = var1;
         var13 = Integer.compare(var3, var4);
         if (((var13 | -var13) >>> 31 ^ 1) == 0 && mc.field_1755 == null) {
            int var11 = mc.method_1569();
            boolean var6 = false;
            byte var5 = (byte)var11;
            var11 = Integer.signum(var5);
            if (((var11 | -var11) >>> 31 ^ 1) == 0 && s5.m(mc.method_22683().method_4490(), var1)) {
               var13 = 1;
               break label23;
            }
         }

         var13 = 0;
      }

      byte var2 = (byte)var13;
      boolean var8 = false;
      byte var7 = var2;
      var13 = Integer.signum(var7);
      if (((var13 | -var13) >>> 31 ^ 1) == 0) {
         boolean var10 = false;
         byte var9 = this.X;
         var13 = Integer.signum(var9);
         if ((var13 | -var13) >>> 31 == 0) {
            this.x();
         }
      }

      this.X = (boolean)var2;
   }

   private void x() {
      int var1 = this.Z().size();
      String var2 = this.D("", "", this.C());
      wt var3 = wt.T();
      if (!var2.isEmpty()) {
         var3.U("Waypoints", var2, wt.s.a);
      } else {
         List var4 = this.Z();
         if (var4.size() > var1) {
            var3.U("Waypoint added", ((cy)var4.getLast()).name(), wt.s.C);
         }
      }
   }

   private List<cy> V(class_243 var1) {
      String var2 = this.z();
      ArrayList var3 = new ArrayList();

      for (cy var5 : this.Z()) {
         if (var5.visible() && var5.dimension().equals(var2)) {
            var3.add(var5);
         }
      }

      var3.sort(Comparator.comparingDouble(var1x -> var1.method_1028(var1x.x() + 0.5, var1x.y() + 0.5, var1x.z() + 0.5)));
      return var3.size() > 64 ? var3.subList(0, 64) : var3;
   }

   private static class_243 C(class_243 var0, double var1, double var3, double var5, double var7) {
      return new class_243(qp.A(var0.field_1352, var1, var7), qp.A(var0.field_1351, var3, var7), qp.A(var0.field_1350, var5, var7));
   }

   private void u() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && (this.G.getValue() || this.F.getValue())) {
            long var1 = c5.X();

            try {
               class_4184 var3 = mc.field_1773.method_19418();
               class_243 var4 = var3.method_71156();
               Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.method_23767());
               class_243 var6 = var4.method_1031(var5.x * 0.5, var5.y * 0.5, var5.z * 0.5);
               int var7 = mc.field_1687.method_31607();
               int var8 = mc.field_1687.method_31600() + 1;

               for (cy var10 : this.V(var4)) {
                  double var11 = var10.x() + 0.5;
                  double var13 = var10.y() + 0.5;
                  double var15 = var10.z() + 0.5;
                  double var17 = Math.sqrt(var4.method_1028(var11, var13, var15));
                  int var19 = ei.D(var10.color(), Math.clamp((var17 - 1.0) / 6.0, 0.0, 1.0) * 0.85);
                  if (this.G.getValue()) {
                     double var20 = qp.w(Math.hypot(var11 - var4.field_1352, var15 - var4.field_1350));
                     class_12180.method_75546(C(var4, var11, var7, var15, var20), C(var4, var11, var8, var15, var20), var19, 2.5F).method_75533();
                     if (var17 <= 64.0) {
                        class_12180.method_75541(
                              new class_238(var10.x(), var10.y(), var10.z(), var10.x() + 1, var10.y() + 1, var10.z() + 1),
                              class_12179.method_75537(var19, 1.5F, ei.D(var10.color(), 0.12))
                           )
                           .method_75533();
                     }
                  }

                  if (this.F.getValue()) {
                     double var23 = qp.w(var17);
                     class_12180.method_75546(var6, C(var4, var11, var13, var15, var23), ei.D(var10.color(), 0.7), 1.5F).method_75533();
                  }
               }
            } catch (RuntimeException var22) {
               q8.O("Waypoints", "World markers failed", var22);
            }

            c5.w("Waypoints", var1);
         }
      }
   }

   private void P(class_332 var1, class_9779 var2) {
      if (this.isEnabled()) {
         if (this.d.getValue() && this.isOperational() && this.isInGame() && !mc.field_1690.field_1842) {
            var1 = wi.p(var1, false, false);
            if (var1 != null) {
               long var3 = c5.X();

               try {
                  class_4184 var5 = mc.field_1773.method_19418();
                  class_243 var6 = var5.method_71156();
                  Matrix4f var7 = l2.F(new Matrix4f());
                  Vector3f var8 = var5.method_23767().transform(new Vector3f(0.0F, 0.025F, 0.0F));
                  int var9 = mc.method_22683().method_4486();
                  int var10 = mc.method_22683().method_4502();
                  byte var11 = 0;
                  ArrayList var12 = new ArrayList();

                  for (cy var14 : this.V(var6)) {
                     double var15 = var14.x() + 0.5;
                     double var17 = var14.y() + 1.5;
                     double var19 = var14.z() + 0.5;
                     double var21 = Math.sqrt(var6.method_1028(var15, var17, var19));
                     double var23 = qp.w(var21);
                     eg.o var25 = eg.y(C(var6, var15, var17, var19, var23), var6, var7, var8, var9, var10, (boolean)var11);
                     if (var25 != null) {
                        this.U(var1, var14, var25, var21, var12);
                     }
                  }
               } catch (RuntimeException var26) {
                  q8.O("Waypoints", "Labels failed", var26);
               }

               c5.w("Waypoints labels", var3);
            }
         }
      }
   }

   private float T(float var1) {
      double var2 = mc.method_22683().method_4495();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private void U(class_332 var1, cy var2, eg.o var3, double var4, List<float[]> var6) {
      class_5250 var7 = class_2561.method_43470(var2.name());
      if (this.V.getValue()) {
         var7.method_10852(class_2561.method_43470(" " + qp.Z(var4)).method_54663(14211288));
      }

      int var8 = mc.field_1772.method_27525(var7);
      float var9 = this.T.getValue().floatValue();
      float var10 = (var8 + 2) * var9 / 2.0F;
      float var11 = 5.5F * var9;
      float var12 = this.G.getValue() ? var3.x() + 5.0F + var10 : var3.x();
      float var13 = qp.W(var6, var12, var3.y(), var10, var11);
      var1.method_51448().pushMatrix();

      try {
         var1.method_51448().translate(this.T(var12 - var10), this.T(var13 - var11)).scale(var9);
         gu.Z(var1, 0, 0, var8 + 2, 11, 1073741824);
         var1.method_51439(mc.field_1772, var7, 1, 1, -1, true);
      } finally {
         var1.method_51448().popMatrix();
      }
   }
}
