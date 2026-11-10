package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.fz;
import com.larpclient.he;
import com.larpclient.hz;
import com.larpclient.n4;
import com.larpclient.n9;
import com.larpclient.vv;
import com.larpclient.w7;
import com.larpclient.wh;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class WingsModule extends Module {
   public static final String j = "Luminous";
   public static final String s = "Ender Dragon";
   private static WingsModule O;
   private final m P = this.addSetting(new m("Self", "Show your wings in third person", true));
   private final m e = this.addSetting(new m("Friends", "Show wings on friends", true));
   private final m b = this.addSetting(new m("Others", "Show wings on other players", false));
   private final b Z = this.addSetting(new b("Size", "Wing size", 1.0, 0.75, 1.35, 0.05));
   private final m q = this.addSetting(new m("Custom Colors", "Cycle between your two colors", true));
   private final h Y = this.addSetting(new h("Color 1", "First wing color", -52586));
   private final h k = this.addSetting(new h("Color 2", "Second wing color", -13461761));
   private final com.larpclient.module.o M = this.addSetting(new com.larpclient.module.o("Style", "Wing design", "Luminous", "Luminous", "Ender Dragon"));
   private final h U = this.addSetting(new h("Dragon Color", "Tints the Ender Dragon texture; white keeps it vanilla and alpha sets opacity", -1));
   public static final int a = 32;
   private List<WingsModule.y> u = List.of();
   private List<WingsModule.x> T = List.of();
   private final IdentityHashMap<Object, n4.m> I = new IdentityHashMap<>();
   private ClientLevel N;
   private ClientLevel B;
   private n9.j d;
   private final PoseStack y = new PoseStack();
   private final PoseStack E = new PoseStack();

   public WingsModule() {
      super("Wings", "Wear luminous membrane wings or Ender Dragon wings", Category.VISUALS, -1);
      O = this;
      this.q.a(() -> this.M.w("Luminous"));
      this.Y.a(() -> this.M.w("Luminous") && this.q.getValue());
      this.k.a(() -> this.M.w("Luminous") && this.q.getValue());
      this.U.a(() -> this.M.w("Ender Dragon"));
      this.M.w(var0 -> _q.z("Wings", "Style={}", var0));
   }

   public static WingsModule i() {
      return O;
   }

   public List<r<?>> V() {
      return List.of(this.P, this.e, this.b);
   }

   public List<r<?>> r() {
      return List.of(this.M, this.Z, this.q, this.Y, this.k, this.U);
   }

   public boolean Z() {
      return this.isEnabled() ? this.M.w("Ender Dragon") : false;
   }

   public WingsModule.o T() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.P.getValue()) {
            return null;
         } else if (this.Z()) {
            return this.U.getValue() >>> 24 == 0 ? null : new WingsModule.o(true, this.Z.getValue().floatValue(), 0, 0, this.U.getValue());
         } else {
            return new WingsModule.o(
               false,
               this.Z.getValue().floatValue(),
               this.q.getValue() ? this.Y.getValue() : com.larpclient.c.y,
               this.q.getValue() ? this.k.getValue() : com.larpclient.c.t,
               0
            );
         }
      } else {
         return null;
      }
   }

   public static int V(int var0, int var1, double var2) {
      return vv.Q(var0, var1, (Math.sin(var2 * 2.0) + 1.0) / 2.0);
   }

   public static int D(int var0, int var1, double var2) {
      return vv.Q(var0, var1, (Math.sin(var2 * 2.0 + 2.1) + 1.0) / 2.0);
   }

   @Override
   public void onEnable() {
      _q.z(
         "Wings",
         this.Z()
            ? "Ender Dragon wings enabled; vanilla dragon wing parts on the native torso, 48 quads per wearer, 32 nearby previews"
            : "Articulated membrane wings enabled on the native torso; shared bloom, 146 base quads per wearer, 32 nearby previews"
      );
   }

   @Override
   public void onDisable() {
      this.u = List.of();
      this.T = List.of();
      this.N = null;
      this.I.clear();
      this.B = null;
   }

   public void l(float var1) {
      if (this.isEnabled()) {
         this.u = List.of();
         this.T = List.of();
         this.N = null;
         if (this.isOperational() && this.isInGame()) {
            if (this.B != mc.level) {
               this.I.clear();
               this.B = mc.level;
            }

            MCFriendModule var2 = LarpClient.N().j().y(MCFriendModule.class);
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Set var5 = Collections.newSetFromMap(new IdentityHashMap());
            int var6 = 0;
            float var7 = this.Z.getValue().floatValue();
            boolean var8 = this.Z();
            int var9 = this.U.getValue();
            int var10 = this.q.getValue() ? this.Y.getValue() : com.larpclient.c.y;
            int var11 = this.q.getValue() ? this.k.getValue() : com.larpclient.c.t;
            int var12 = V(var10, var11, hz.Q());
            int var13 = D(var10, var11, hz.Q());

            for (AbstractClientPlayer var15 : mc.level.players()) {
               if (var15 == mc.player) {
                  if (!this.P.getValue() || mc.options.getCameraType().isFirstPerson()) {
                     continue;
                  }
               } else {
                  int var16 = var2 != null && var2.M(var15.getName().getString()) ? 1 : 0;
                  if (!var16 != 0 ? this.e.getValue() : this.b.getValue() || var6 >= 32) {
                     continue;
                  }
               }

               if (var15.isAlive()
                  && !var15.isInvisible()
                  && !var15.isSpectator()
                  && !var15.isSleeping()
                  && !var15.isInWater()
                  && !var15.isVisuallyCrawling()
                  && !var15.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
                  && !(var15.distanceToSqr(mc.player) > 9216.0)) {
                  if (var15 != mc.player) {
                     var6++;
                  }

                  float var30 = Feature3DUIModule.C(var15, Mth.rotLerp(var1, var15.yBodyRotO, var15.yBodyRot), var1);
                  float var17 = var15.tickCount + var1;
                  float var18 = var15.walkAnimation.speed(var1);
                  Vec3 var19 = var15.getPosition(var1);
                  n4.m var20 = this.I.computeIfAbsent(var15, var0 -> new n4.m());
                  var20.H(var17, var15.isCrouching(), var18, var15.isFallFlying(), b(var15), var19);
                  var5.add(var15);
                  if (var8) {
                     float var31 = n9.L(var20.V(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     var20.y(n9.L(var20.N(), var20.z(), var20.f(), var31));
                     if (var9 >>> 24 != 0) {
                        n9.d var32 = n9.z(var20.m() + n9.w(var15.getId()), var20.N(), var20.z(), var20.f(), var31);
                        var4.add(new WingsModule.x(w7.u(var15, var1), var32, var7, var9));
                     }
                  } else {
                     EntityRenderState var21 = w7.u(var15, var1);
                     Pose var22 = w7.a(var21, this.E);
                     int var23 = var21 instanceof HumanoidRenderState var24 && var24.isCrouching ? 1 : 0;
                     float var33 = var22 == null ? var20.V() : n9.L(var20.V(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     n4.i var25 = n4.l(var20.z(), var33, var15.getFallFlyingTicks() + var1, var15.getViewXRot(var1));
                     n4.z var26 = n4.y(var17, var20.N(), var20.z(), var33);
                     Vec3 var27 = var22 == null ? var19 : mc.gameRenderer.mainCamera().position();
                     Matrix4f var28 = var22 == null
                        ? n4.n(var25, var30, var17, var20.N(), var7, -1)
                        : new Matrix4f(var22.pose()).mul(n4.b(var25, var17, var20.N(), var7, -1, (boolean)var23));
                     Matrix4f var29 = var22 == null
                        ? n4.n(var25, var30, var17, var20.N(), var7, 1)
                        : new Matrix4f(var22.pose()).mul(n4.b(var25, var17, var20.N(), var7, 1, (boolean)var23));
                     var3.add(new WingsModule.y(n4.f(var27, var28, -1, var26, var12, var13, var7), n4.f(var27, var29, 1, var26, var12, var13, var7)));
                  }
               }
            }

            this.I.keySet().removeIf(var1x -> !var5.contains(var1x));
            this.N = mc.level;
            this.u = List.copyOf(var3);
            this.T = List.copyOf(var4);
         } else {
            this.I.clear();
            this.B = null;
         }
      }
   }

   private static boolean b(Player var0) {
      return !var0.onGround() && !var0.isPassenger() && !var0.onClimbable() && !var0.isFallFlying();
   }

   public boolean B(PoseStack var1, wh var2) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational() && this.N == mc.level && !this.T.isEmpty()) {
         if (this.d == null) {
            this.d = n9.j.C();
            _q.z("Wings", "Ender Dragon wing parts ready from the vanilla layer definition; texture={}", n9.c);
         }

         long var3 = he.s();
         byte var5 = 0;

         for (WingsModule.x var7 : this.T) {
            Pose var8 = w7.a(var7.state, var1);
            if (var8 != null) {
               this.y.last().set(var8);
               this.d.Y(var7.angles);
               int var9 = var7.state instanceof HumanoidRenderState var10 && var10.isCrouching ? 1 : 0;
               this.d.d(this.y, var2.w(n9.M(var7.color)), var7.size, (boolean)var9, var7.state.lightCoords, var7.color);
               var5 = 1;
            }
         }

         he.T("Wings Ender Dragon attachment and submission", var3);
         return (boolean)var5;
      } else {
         return false;
      }
   }

   public void B(fz var1, wh var2) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.N == mc.level) {
            for (n4.t var6 : n4.t.values()) {
               for (WingsModule.y var8 : this.u) {
                  n4.N(var1, var8.left, var6);
                  n4.N(var1, var8.right, var6);
               }

               var2.S();
            }
         }
      }
   }

   public boolean L() {
      return !this.isEnabled() ? false : this.isOperational() && this.N == mc.level && this.u.stream().anyMatch(var0 -> n4.D(var0.left));
   }

   public void p(fz var1, wh var2) {
      if (this.isEnabled()) {
         if (this.L()) {
            for (WingsModule.y var4 : this.u) {
               n4.F(var1, var4.left);
               n4.F(var1, var4.right);
            }

            var2.S();
         }
      }
   }

   public record o(boolean dragon, float size, int first, int second, int dragonColor) {
   }

   private record x(EntityRenderState state, n9.d angles, float size, int color) {
   }

   private record y(n4.b left, n4.b right) {
   }
}
