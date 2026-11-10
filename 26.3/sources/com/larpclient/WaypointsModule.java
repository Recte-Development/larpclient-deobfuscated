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
   private static final int z = 64;
   private static final int[] P = new int[]{-8600321, -30070, -7347266, -14249, -4291585, -24728, -723723};
   private static final int D = -1;
   private static final int T = 14211288;
   private static final int c = 1073741824;
   private static final int h = 5;
   private final com.larpclient.module.m I = this.addSetting(
      new com.larpclient.module.m("Beams", "Draw a vertical beam at each waypoint, visible through walls", true)
   );
   private final com.larpclient.module.m E = this.addSetting(new com.larpclient.module.m("Tracers", "Draw a line from your view to each waypoint", false));
   private final com.larpclient.module.m Y = this.addSetting(new com.larpclient.module.m("Labels", "Show each waypoint's name on screen", true));
   private final com.larpclient.module.m R = this.addSetting(new com.larpclient.module.m("Distance", "Show how far away each waypoint is", true));
   private final com.larpclient.module.b A = this.addSetting(new com.larpclient.module.b("Label Scale", "Size of the on-screen labels", 1.0, 0.5, 2.0, 0.1));
   private final com.larpclient.module.x H = this.addSetting(
      new com.larpclient.module.x("Add Waypoint Key", "Press in game to add a waypoint where you stand", -1)
   );
   private n3 b;
   private boolean F;
   private Object y;
   private String J;

   public WaypointsModule() {
      super("Waypoints", "Save your own locations and see them in the world", Category.RENDER, -1);
      this.R.a(this.Y::getValue);
      this.A.a(this.Y::getValue);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.w());
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "waypoints"), this::E);
   }

   public List<com.larpclient.module.r<?>> r() {
      return List.of(this.I, this.E, this.Y, this.R, this.A);
   }

   public List<com.larpclient.module.r<?>> C() {
      return List.of(this.H);
   }

   private n3 u() {
      if (this.b == null) {
         this.b = new n3(FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("waypoints.json"));
      }

      return this.b;
   }

   public String u() {
      if (!this.isInGame()) {
         this.y = null;
         this.J = null;
         return null;
      }

      if (mc.level != this.y) {
         this.y = mc.level;
         IntegratedServer var1 = mc.getSingleplayerServer();
         if (var1 != null) {
            this.J = n3.U(String.valueOf(var1.getWorldPath(LevelResource.ROOT).normalize().getFileName()));
         } else {
            this.J = n3.r(mc.getCurrentServer() == null ? null : mc.getCurrentServer().ip);
         }
      }

      return this.J;
   }

   public boolean L() {
      return this.isInGame() && mc.getSingleplayerServer() == null;
   }

   public List<nu> U() {
      String var1 = this.u();
      return var1 == null ? List.of() : this.u().n(var1);
   }

   public Optional<nu> v(String var1) {
      String var2 = this.u();
      return var2 == null ? Optional.empty() : this.u().Q(var2, var1);
   }

   public boolean C() {
      String var1 = this.u();
      return var1 != null && this.u().w(var1);
   }

   public String o() {
      return this.u().x();
   }

   public int b() {
      return P[this.U().size() % P.length];
   }

   public String G() {
      return this.isInGame() ? w.o(this.F()) : "";
   }

   private w.m F() {
      BlockPos var1 = this.getPlayer().blockPosition();
      return new w.m(var1.getX(), var1.getY(), var1.getZ());
   }

   private String U() {
      return mc.level.dimension().identifier().toString();
   }

   public String P(String var1, String var2, int var3) {
      String var4 = this.u();
      if (var4 == null) {
         return "Join a world first";
      }

      try {
         w.m var5 = w.b(var2, this.F());
         String var6 = nu.cleanName(var1);
         this.u().N(var4, new nu(UUID.randomUUID().toString(), var6.isEmpty() ? this.m() : var6, var5.x(), var5.y(), var5.z(), this.U(), var3, true));
         _q.z("Waypoints", "Added a waypoint in {}", this.U());
         return this.u().x();
      } catch (IllegalArgumentException var7) {
         return var7.getMessage();
      }
   }

   private String m() {
      HashSet var1 = new HashSet();

      for (nu var3 : this.U()) {
         var1.add(var3.name());
      }

      int var4 = this.U().size() + 1;

      while (var1.contains("Waypoint " + var4)) {
         var4++;
      }

      return "Waypoint " + var4;
   }

   public String I(String var1, String var2, String var3, int var4) {
      String var5 = this.u();
      nu var6 = this.v(var1).orElse(null);
      if (var5 != null && var6 != null) {
         String var7 = nu.cleanName(var2);
         if (var7.isEmpty()) {
            return "Enter a name";
         }

         try {
            this.u().G(var5, var6.edited(var7, w.b(var3, var6.position()), var4));
            _q.z("Waypoints", "Edited a waypoint in {}", var6.dimension());
            return this.u().x();
         } catch (IllegalArgumentException var9) {
            return var9.getMessage();
         }
      } else {
         return "That waypoint no longer exists";
      }
   }

   public void a(String var1, boolean var2) {
      String var3 = this.u();
      this.v(var1).ifPresent(var3x -> this.u().G(var3, var3x.withVisible(var2)));
   }

   public void c(String var1) {
      String var2 = this.u();
      if (var2 != null && this.u().A(var2, var1)) {
         _q.z("Waypoints", "Deleted a waypoint");
      }
   }

   public String g(nu var1) {
      String var2 = w.o(var1.position());
      if (!this.isInGame()) {
         return var2;
      } else {
         return !var1.dimension().equals(this.U())
            ? var2 + "  ·  " + nu.dimensionLabel(var1.dimension())
            : var2 + "  ·  " + w.b(Math.sqrt(this.getPlayer().distanceToSqr(var1.x() + 0.5, var1.y() + 0.5, var1.z() + 0.5)));
      }
   }

   @Override
   public void onDisable() {
      this.F = (boolean)0;
   }

   @Override
   public void onTick() {
      int var13;
      label23: {
         int var1 = this.H.getValue();
         byte var4 = -1;
         int var3 = var1;
         var13 = Integer.compare(var3, var4);
         if (((var13 | -var13) >>> 31 ^ 1) == 0 && mc.gui.screen() == null && mc.isWindowActive()) {
            int var11 = p4.Y(mc.getWindow().handle(), var1);
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
         byte var9 = this.F;
         var13 = Integer.signum(var9);
         if ((var13 | -var13) >>> 31 == 0) {
            this.x();
         }
      }

      this.F = (boolean)var2;
   }

   private void x() {
      int var1 = this.U().size();
      String var2 = this.P("", "", this.b());
      ib var3 = ib.L();
      if (!var2.isEmpty()) {
         var3.t("Waypoints", var2, ib.v.o);
      } else {
         List var4 = this.U();
         if (var4.size() > var1) {
            var3.t("Waypoint added", ((nu)var4.getLast()).name(), ib.v.x);
         }
      }
   }

   private List<nu> N(Vec3 var1) {
      String var2 = this.U();
      ArrayList var3 = new ArrayList();

      for (nu var5 : this.U()) {
         if (var5.visible() && var5.dimension().equals(var2)) {
            var3.add(var5);
         }
      }

      var3.sort(Comparator.comparingDouble(var1x -> var1.distanceToSqr(var1x.x() + 0.5, var1x.y() + 0.5, var1x.z() + 0.5)));
      return var3.size() > 64 ? var3.subList(0, 64) : var3;
   }

   private static Vec3 x(Vec3 var0, double var1, double var3, double var5, double var7) {
      return new Vec3(w.q(var0.x, var1, var7), w.q(var0.y, var3, var7), w.q(var0.z, var5, var7));
   }

   private void w() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && (this.I.getValue() || this.E.getValue())) {
            long var1 = he.s();

            try {
               Camera var3 = mc.gameRenderer.mainCamera();
               Vec3 var4 = var3.position();
               Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.rotation());
               Vec3 var6 = var4.add(var5.x * 0.5, var5.y * 0.5, var5.z * 0.5);
               int var7 = mc.level.getMinY();
               int var8 = mc.level.getMaxY() + 1;

               for (nu var10 : this.N(var4)) {
                  double var11 = var10.x() + 0.5;
                  double var13 = var10.y() + 0.5;
                  double var15 = var10.z() + 0.5;
                  double var17 = Math.sqrt(var4.distanceToSqr(var11, var13, var15));
                  int var19 = vv.N(var10.color(), Math.clamp((var17 - 1.0) / 6.0, 0.0, 1.0) * 0.85);
                  if (this.I.getValue()) {
                     double var20 = w.x(Math.hypot(var11 - var4.x, var15 - var4.z));
                     Gizmos.line(x(var4, var11, var7, var15, var20), x(var4, var11, var8, var15, var20), var19, 2.5F).setAlwaysOnTop();
                     if (var17 <= 64.0) {
                        Gizmos.cuboid(
                              new AABB(var10.x(), var10.y(), var10.z(), var10.x() + 1, var10.y() + 1, var10.z() + 1),
                              GizmoStyle.strokeAndFill(var19, 1.5F, vv.N(var10.color(), 0.12))
                           )
                           .setAlwaysOnTop();
                     }
                  }

                  if (this.E.getValue()) {
                     double var23 = w.x(var17);
                     Gizmos.line(var6, x(var4, var11, var13, var15, var23), vv.N(var10.color(), 0.7), 1.5F).setAlwaysOnTop();
                  }
               }
            } catch (RuntimeException var22) {
               _q.g("Waypoints", "World markers failed", var22);
            }

            he.T("Waypoints", var1);
         }
      }
   }

   private void E(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.isEnabled()) {
         if (this.Y.getValue() && this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
            var1 = qc.h(var1, false, false);
            if (var1 != null) {
               long var3 = he.s();

               try {
                  Camera var5 = mc.gameRenderer.mainCamera();
                  Vec3 var6 = var5.position();
                  Matrix4f var7 = j5.I(new Matrix4f());
                  Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 0.025F, 0.0F));
                  int var9 = mc.getWindow().getGuiScaledWidth();
                  int var10 = mc.getWindow().getGuiScaledHeight();
                  boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
                  ArrayList var12 = new ArrayList();

                  for (nu var14 : this.N(var6)) {
                     double var15 = var14.x() + 0.5;
                     double var17 = var14.y() + 1.5;
                     double var19 = var14.z() + 0.5;
                     double var21 = Math.sqrt(var6.distanceToSqr(var15, var17, var19));
                     double var23 = w.x(var21);
                     iy.a var25 = iy.A(x(var6, var15, var17, var19, var23), var6, var7, var8, var9, var10, var11);
                     if (var25 != null) {
                        this.i(var1, var14, var25, var21, var12);
                     }
                  }
               } catch (RuntimeException var26) {
                  _q.g("Waypoints", "Labels failed", var26);
               }

               he.T("Waypoints labels", var3);
            }
         }
      }
   }

   private float G(float var1) {
      double var2 = mc.getWindow().getGuiScale();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private void i(GuiGraphicsExtractor var1, nu var2, iy.a var3, double var4, List<float[]> var6) {
      MutableComponent var7 = Component.literal(var2.name());
      if (this.R.getValue()) {
         var7.append(Component.literal(" " + w.b(var4)).withColor(14211288));
      }

      int var8 = mc.font.width(var7);
      float var9 = this.A.getValue().floatValue();
      float var10 = (var8 + 2) * var9 / 2.0F;
      float var11 = 5.5F * var9;
      float var12 = this.I.getValue() ? var3.x() + 5.0F + var10 : var3.x();
      float var13 = w.w(var6, var12, var3.y(), var10, var11);
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.G(var12 - var10), this.G(var13 - var11)).scale(var9);
         h5.N(var1, 0, 0, var8 + 2, 11, 1073741824);
         var1.text(mc.font, var7, 1, 1, -1, true);
      } finally {
         var1.pose().popMatrix();
      }
   }
}
