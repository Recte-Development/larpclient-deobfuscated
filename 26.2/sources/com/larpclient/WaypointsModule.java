package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class WaypointsModule extends Module {
   private static final int y = 64;
   private static final int[] g = new int[]{-8600321, -30070, -7347266, -14249, -4291585, -24728, -723723};
   private static final int x = -1;
   private static final int B = 14211288;
   private static final int N = 1073741824;
   private static final int v = 5;
   private final com.larpclient.module.s S = this.addSetting(
      new com.larpclient.module.s("Beams", "Draw a vertical beam at each waypoint, visible through walls", true)
   );
   private final com.larpclient.module.s U = this.addSetting(new com.larpclient.module.s("Tracers", "Draw a line from your view to each waypoint", false));
   private final com.larpclient.module.s X = this.addSetting(new com.larpclient.module.s("Labels", "Show each waypoint's name on screen", true));
   private final com.larpclient.module.s o = this.addSetting(new com.larpclient.module.s("Distance", "Show how far away each waypoint is", true));
   private final com.larpclient.module.e W = this.addSetting(new com.larpclient.module.e("Label Scale", "Size of the on-screen labels", 1.0, 0.5, 2.0, 0.1));
   private final com.larpclient.module.v A = this.addSetting(
      new com.larpclient.module.v("Add Waypoint Key", "Press in game to add a waypoint where you stand", -1)
   );
   private br z;
   private boolean r;
   private Object s;
   private String R;

   public WaypointsModule() {
      super("Waypoints", "Save your own locations and see them in the world", Category.RENDER, -1);
      this.o.P(this.X::getValue);
      this.W.P(this.X::getValue);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.s());
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "waypoints"), this::z);
   }

   public List<com.larpclient.module.q<?>> p() {
      return List.of(this.S, this.U, this.X, this.o, this.W);
   }

   public List<com.larpclient.module.q<?>> Y() {
      return List.of(this.A);
   }

   private br u() {
      if (this.z == null) {
         this.z = new br(FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("waypoints.json"));
      }

      return this.z;
   }

   public String Z() {
      if (!this.isInGame()) {
         this.s = null;
         this.R = null;
         return null;
      }

      if (mc.level != this.s) {
         this.s = mc.level;
         IntegratedServer var1 = mc.getSingleplayerServer();
         if (var1 != null) {
            this.R = br.u(String.valueOf(var1.getWorldPath(LevelResource.ROOT).normalize().getFileName()));
         } else {
            this.R = br.w(mc.getCurrentServer() == null ? null : mc.getCurrentServer().ip);
         }
      }

      return this.R;
   }

   public boolean f() {
      return this.isInGame() && mc.getSingleplayerServer() == null;
   }

   public List<sn> F() {
      String var1 = this.Z();
      return var1 == null ? List.of() : this.u().D(var1);
   }

   public Optional<sn> e(String var1) {
      String var2 = this.Z();
      return var2 == null ? Optional.empty() : this.u().Y(var2, var1);
   }

   public boolean x() {
      String var1 = this.Z();
      return var1 != null && this.u().N(var1);
   }

   public String S() {
      return this.u().d();
   }

   public int v() {
      return g[this.F().size() % g.length];
   }

   public String Y() {
      return this.isInGame() ? pr.M(this.o()) : "";
   }

   private pr.v o() {
      BlockPos var1 = this.getPlayer().blockPosition();
      return new pr.v(var1.getX(), var1.getY(), var1.getZ());
   }

   private String J() {
      return mc.level.dimension().identifier().toString();
   }

   public String v(String var1, String var2, int var3) {
      String var4 = this.Z();
      if (var4 == null) {
         return "Join a world first";
      }

      try {
         pr.v var5 = pr.U(var2, this.o());
         String var6 = sn.cleanName(var1);
         this.u().I(var4, new sn(UUID.randomUUID().toString(), var6.isEmpty() ? this.C() : var6, var5.x(), var5.y(), var5.z(), this.J(), var3, true));
         h.A("Waypoints", "Added a waypoint in {}", this.J());
         return this.u().d();
      } catch (IllegalArgumentException var7) {
         return var7.getMessage();
      }
   }

   private String C() {
      HashSet var1 = new HashSet();

      for (sn var3 : this.F()) {
         var1.add(var3.name());
      }

      int var4 = this.F().size() + 1;

      while (var1.contains("Waypoint " + var4)) {
         var4++;
      }

      return "Waypoint " + var4;
   }

   public String i(String var1, String var2, String var3, int var4) {
      String var5 = this.Z();
      sn var6 = this.e(var1).orElse(null);
      if (var5 != null && var6 != null) {
         String var7 = sn.cleanName(var2);
         if (var7.isEmpty()) {
            return "Enter a name";
         }

         try {
            this.u().F(var5, var6.edited(var7, pr.U(var3, var6.position()), var4));
            h.A("Waypoints", "Edited a waypoint in {}", var6.dimension());
            return this.u().d();
         } catch (IllegalArgumentException var9) {
            return var9.getMessage();
         }
      } else {
         return "That waypoint no longer exists";
      }
   }

   public void k(String var1, boolean var2) {
      String var3 = this.Z();
      this.e(var1).ifPresent(var3x -> this.u().F(var3, var3x.withVisible(var2)));
   }

   public void Z(String var1) {
      String var2 = this.Z();
      if (var2 != null && this.u().h(var2, var1)) {
         h.A("Waypoints", "Deleted a waypoint");
      }
   }

   public String p(sn var1) {
      String var2 = pr.M(var1.position());
      if (!this.isInGame()) {
         return var2;
      } else {
         return !var1.dimension().equals(this.J())
            ? var2 + "  ·  " + sn.dimensionLabel(var1.dimension())
            : var2 + "  ·  " + pr.v(Math.sqrt(this.getPlayer().distanceToSqr(var1.x() + 0.5, var1.y() + 0.5, var1.z() + 0.5)));
      }
   }

   @Override
   public void onDisable() {
      this.r = (boolean)0;
   }

   @Override
   public void onTick() {
      int var13;
      label23: {
         int var1 = this.A.getValue();
         byte var4 = -1;
         int var3 = var1;
         var13 = Integer.compare(var3, var4);
         if (((var13 | -var13) >>> 31 ^ 1) == 0 && mc.gui.screen() == null && mc.isWindowActive()) {
            int var11 = bc.I(mc.getWindow().handle(), var1);
            boolean var6 = false;
            byte var5 = (byte)var11;
            var11 = Integer.signum(var5);
            if (((var11 | -var11) >>> 31 ^ 1) == 0) {
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
         byte var9 = this.r;
         var13 = Integer.signum(var9);
         if ((var13 | -var13) >>> 31 == 0) {
            this.A();
         }
      }

      this.r = (boolean)var2;
   }

   private void A() {
      int var1 = this.F().size();
      String var2 = this.v("", "", this.v());
      s0 var3 = s0.s();
      if (!var2.isEmpty()) {
         var3.V("Waypoints", var2, s0.u.p);
      } else {
         List var4 = this.F();
         if (var4.size() > var1) {
            var3.V("Waypoint added", ((sn)var4.getLast()).name(), s0.u.m);
         }
      }
   }

   private List<sn> b(Vec3 var1) {
      String var2 = this.J();
      ArrayList var3 = new ArrayList();

      for (sn var5 : this.F()) {
         if (var5.visible() && var5.dimension().equals(var2)) {
            var3.add(var5);
         }
      }

      var3.sort(Comparator.comparingDouble(var1x -> var1.distanceToSqr(var1x.x() + 0.5, var1x.y() + 0.5, var1x.z() + 0.5)));
      return var3.size() > 64 ? var3.subList(0, 64) : var3;
   }

   private static Vec3 O(Vec3 var0, double var1, double var3, double var5, double var7) {
      return new Vec3(pr.k(var0.x, var1, var7), pr.k(var0.y, var3, var7), pr.k(var0.z, var5, var7));
   }

   private void s() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && (this.S.getValue() || this.U.getValue())) {
            long var1 = zj.m();

            try {
               Camera var3 = mc.gameRenderer.mainCamera();
               Vec3 var4 = var3.position();
               Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
               Vec3 var6 = var4.add(var5.x * 0.5, var5.y * 0.5, var5.z * 0.5);
               int var7 = mc.level.getMinY();
               int var8 = mc.level.getMaxY() + 1;

               for (sn var10 : this.b(var4)) {
                  double var11 = var10.x() + 0.5;
                  double var13 = var10.y() + 0.5;
                  double var15 = var10.z() + 0.5;
                  double var17 = Math.sqrt(var4.distanceToSqr(var11, var13, var15));
                  int var19 = uh.H(var10.color(), Math.clamp((var17 - 1.0) / 6.0, 0.0, 1.0) * 0.85);
                  if (this.S.getValue()) {
                     double var20 = pr.G(Math.hypot(var11 - var4.x, var15 - var4.z));
                     Gizmos.line(O(var4, var11, var7, var15, var20), O(var4, var11, var8, var15, var20), var19, 2.5F).setAlwaysOnTop();
                     if (var17 <= 64.0) {
                        Gizmos.cuboid(
                              new AABB(var10.x(), var10.y(), var10.z(), var10.x() + 1, var10.y() + 1, var10.z() + 1),
                              GizmoStyle.strokeAndFill(var19, 1.5F, uh.H(var10.color(), 0.12))
                           )
                           .setAlwaysOnTop();
                     }
                  }

                  if (this.U.getValue()) {
                     double var23 = pr.G(var17);
                     Gizmos.line(var6, O(var4, var11, var13, var15, var23), uh.H(var10.color(), 0.7), 1.5F).setAlwaysOnTop();
                  }
               }
            } catch (RuntimeException var22) {
               h.w("Waypoints", "World markers failed", var22);
            }

            zj.l("Waypoints", var1);
         }
      }
   }

   private void z(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.isEnabled()) {
         if (this.X.getValue() && this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
            var1 = p2.x(var1, false, false);
            if (var1 != null) {
               long var3 = zj.m();

               try {
                  Camera var5 = mc.gameRenderer.mainCamera();
                  Vec3 var6 = var5.position();
                  Matrix4f var7 = z4.l(new Matrix4f());
                  Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F, 0.0F));
                  int var9 = mc.getWindow().getGuiScaledWidth();
                  int var10 = mc.getWindow().getGuiScaledHeight();
                  boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
                  ArrayList var12 = new ArrayList();

                  for (sn var14 : this.b(var6)) {
                     double var15 = var14.x() + 0.5;
                     double var17 = var14.y() + 1.5;
                     double var19 = var14.z() + 0.5;
                     double var21 = Math.sqrt(var6.distanceToSqr(var15, var17, var19));
                     double var23 = pr.G(var21);
                     k7.o var25 = k7.I(O(var6, var15, var17, var19, var23), var6, var7, var8, var9, var10, var11);
                     if (var25 != null) {
                        this.M(var1, var14, var25, var21, var12);
                     }
                  }
               } catch (RuntimeException var26) {
                  h.w("Waypoints", "Labels failed", var26);
               }

               zj.l("Waypoints labels", var3);
            }
         }
      }
   }

   private float t(float var1) {
      double var2 = mc.getWindow().getGuiScale();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private void M(GuiGraphicsExtractor var1, sn var2, k7.o var3, double var4, List<float[]> var6) {
      MutableComponent var7 = Component.literal(var2.name());
      if (this.o.getValue()) {
         var7.append(Component.literal(" " + pr.v(var4)).withColor(14211288));
      }

      int var8 = mc.font.width(var7);
      float var9 = this.W.getValue().floatValue();
      float var10 = (var8 + 2) * var9 / 2.0F;
      float var11 = 5.5F * var9;
      float var12 = this.S.getValue() ? var3.x() + 5.0F + var10 : var3.x();
      float var13 = pr.q(var6, var12, var3.y(), var10, var11);
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.t(var12 - var10), this.t(var13 - var11)).scale(var9);
         kv.d(var1, 0, 0, var8 + 2, 11, 1073741824);
         var1.text(mc.font, var7, 1, 1, -1, true);
      } finally {
         var1.pose().popMatrix();
      }
   }
}
