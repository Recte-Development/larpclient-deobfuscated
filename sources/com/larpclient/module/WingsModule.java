package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.ea;
import com.larpclient.h_;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.rn;
import com.larpclient.rt;
import com.larpclient.rv;
import com.larpclient.uv;
import com.larpclient.y2;
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
   public static final String w = "Luminous";
   public static final String J = "Ender Dragon";
   private static WingsModule u;
   private final ln G = this.addSetting(new ln("Self", "Show your wings in third person", true));
   private final ln q = this.addSetting(new ln("Friends", "Show wings on friends", true));
   private final ln M = this.addSetting(new ln("Others", "Show wings on other players", false));
   private final lc A = this.addSetting(new lc("Size", "Wing size", 1.0, 0.75, 1.35, 0.05));
   private final ln a = this.addSetting(new ln("Custom Colors", "Cycle between your two colors", true));
   private final lv L = this.addSetting(new lv("Color 1", "First wing color", -52586));
   private final lv h = this.addSetting(new lv("Color 2", "Second wing color", -13461761));
   private final lp m = this.addSetting(new lp("Style", "Wing design", "Luminous", "Luminous", "Ender Dragon"));
   private final lv y = this.addSetting(new lv("Dragon Color", "Tints the Ender Dragon texture; white keeps it vanilla and alpha sets opacity", -1));
   public static final int H = 32;
   private List<WingsModule.l> f = List.of();
   private List<WingsModule.m> i = List.of();
   private final IdentityHashMap<Object, rt.p> V = new IdentityHashMap<>();
   private ClientLevel l;
   private ClientLevel c;
   private rn.k D;
   private final PoseStack e = new PoseStack();
   private final PoseStack T = new PoseStack();

   public WingsModule() {
      super("Wings", "Wear luminous membrane wings or Ender Dragon wings", Category.VISUALS, -1);
      u = this;
      this.a.x(() -> this.m.i("Luminous"));
      this.L.x(() -> this.m.i("Luminous") && this.a.getValue());
      this.h.x(() -> this.m.i("Luminous") && this.a.getValue());
      this.y.x(() -> this.m.i("Ender Dragon"));
      this.m.W(var0 -> r4.V("Wings", "Style={}", var0));
   }

   public static WingsModule i() {
      return u;
   }

   public List<l6<?>> j() {
      return List.of(this.G, this.q, this.M);
   }

   public List<l6<?>> o() {
      return List.of(this.m, this.A, this.a, this.L, this.h, this.y);
   }

   public boolean G() {
      return this.isEnabled() ? this.m.i("Ender Dragon") : false;
   }

   public WingsModule.u B() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.G.getValue()) {
            return null;
         } else if (this.G()) {
            return this.y.getValue() >>> 24 == 0 ? null : new WingsModule.u(true, this.A.getValue().floatValue(), 0, 0, this.y.getValue());
         } else {
            return new WingsModule.u(
               false, this.A.getValue().floatValue(), this.a.getValue() ? this.L.getValue() : rv.e, this.a.getValue() ? this.h.getValue() : rv.GQ, 0
            );
         }
      } else {
         return null;
      }
   }

   public static int A(int var0, int var1, double var2) {
      return oh.j(var0, var1, (Math.sin(var2 * 2.0) + 1.0) / 2.0);
   }

   public static int O(int var0, int var1, double var2) {
      return oh.j(var0, var1, (Math.sin(var2 * 2.0 + 2.1) + 1.0) / 2.0);
   }

   @Override
   public void onEnable() {
      r4.V(
         "Wings",
         this.G()
            ? "Ender Dragon wings enabled; vanilla dragon wing parts on the native torso, 48 quads per wearer, 32 nearby previews"
            : "Articulated membrane wings enabled on the native torso; shared bloom, 146 base quads per wearer, 32 nearby previews"
      );
   }

   @Override
   public void onDisable() {
      this.f = List.of();
      this.i = List.of();
      this.l = null;
      this.V.clear();
      this.c = null;
   }

   public void i(float var1) {
      if (this.isEnabled()) {
         this.f = List.of();
         this.i = List.of();
         this.l = null;
         if (this.isOperational() && this.isInGame()) {
            if (this.c != mc.level) {
               this.V.clear();
               this.c = mc.level;
            }

            MCFriendModule var2 = LarpClient.y().e().y(MCFriendModule.class);
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Set var5 = Collections.newSetFromMap(new IdentityHashMap());
            int var6 = 0;
            float var7 = this.A.getValue().floatValue();
            boolean var8 = this.G();
            int var9 = this.y.getValue();
            int var10 = this.a.getValue() ? this.L.getValue() : rv.e;
            int var11 = this.a.getValue() ? this.h.getValue() : rv.GQ;
            int var12 = A(var10, var11, ea.y());
            int var13 = O(var10, var11, ea.y());

            for (AbstractClientPlayer var15 : mc.level.players()) {
               if (var15 == mc.player) {
                  if (!this.G.getValue() || mc.options.getCameraType().isFirstPerson()) {
                     continue;
                  }
               } else {
                  int var16 = var2 != null && var2.e(var15.getName().getString()) ? 1 : 0;
                  if (!var16 != 0 ? this.q.getValue() : this.M.getValue() || var6 >= 32) {
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

                  float var30 = Feature3DUIModule.t(var15, Mth.rotLerp(var1, var15.yBodyRotO, var15.yBodyRot), var1);
                  float var17 = var15.tickCount + var1;
                  float var18 = var15.walkAnimation.speed(var1);
                  Vec3 var19 = var15.getPosition(var1);
                  rt.p var20 = this.V.computeIfAbsent(var15, var0 -> new rt.p());
                  var20.P(var17, var15.isCrouching(), var18, var15.isFallFlying(), G(var15), var19);
                  var5.add(var15);
                  if (var8) {
                     float var31 = rn.A(var20.e(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     var20.r(rn.s(var20.u(), var20.T(), var20.A(), var31));
                     if (var9 >>> 24 != 0) {
                        rn.z var32 = rn.a(var20.E() + rn.W(var15.getId()), var20.u(), var20.T(), var20.A(), var31);
                        var4.add(new WingsModule.m(uv.v(var15, var1), var32, var7, var9));
                     }
                  } else {
                     EntityRenderState var21 = uv.v(var15, var1);
                     Pose var22 = uv.o(var21, this.T);
                     int var23 = var21 instanceof HumanoidRenderState var24 && var24.isCrouching ? 1 : 0;
                     float var33 = var22 == null ? var20.e() : rn.A(var20.e(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     rt.j var25 = rt.k(var20.T(), var33, var15.getFallFlyingTicks() + var1, var15.getViewXRot(var1));
                     rt.b var26 = rt.c(var17, var20.u(), var20.T(), var33);
                     Vec3 var27 = var22 == null ? var19 : mc.gameRenderer.mainCamera().position();
                     Matrix4f var28 = var22 == null
                        ? rt.C(var25, var30, var17, var20.u(), var7, -1)
                        : new Matrix4f(var22.pose()).mul(rt.A(var25, var17, var20.u(), var7, -1, (boolean)var23));
                     Matrix4f var29 = var22 == null
                        ? rt.C(var25, var30, var17, var20.u(), var7, 1)
                        : new Matrix4f(var22.pose()).mul(rt.A(var25, var17, var20.u(), var7, 1, (boolean)var23));
                     var3.add(new WingsModule.l(rt.i(var27, var28, -1, var26, var12, var13, var7), rt.i(var27, var29, 1, var26, var12, var13, var7)));
                  }
               }
            }

            this.V.keySet().removeIf(var1x -> !var5.contains(var1x));
            this.l = mc.level;
            this.f = List.copyOf(var3);
            this.i = List.copyOf(var4);
         } else {
            this.V.clear();
            this.c = null;
         }
      }
   }

   private static boolean G(Player var0) {
      return !var0.onGround() && !var0.isPassenger() && !var0.onClimbable() && !var0.isFallFlying();
   }

   public boolean C(PoseStack var1, y2 var2) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational() && this.l == mc.level && !this.i.isEmpty()) {
         if (this.D == null) {
            this.D = rn.k.H();
            r4.V("Wings", "Ender Dragon wing parts ready from the vanilla layer definition; texture={}", rn.d);
         }

         long var3 = h_.U();
         byte var5 = 0;

         for (WingsModule.m var7 : this.i) {
            Pose var8 = uv.o(var7.state, var1);
            if (var8 != null) {
               this.e.last().set(var8);
               this.D.o(var7.angles);
               int var9 = var7.state instanceof HumanoidRenderState var10 && var10.isCrouching ? 1 : 0;
               this.D.p(this.e, var2.a(rn.X(var7.color)), var7.size, (boolean)var9, var7.state.lightCoords, var7.color);
               var5 = 1;
            }
         }

         h_.n("Wings Ender Dragon attachment and submission", var3);
         return (boolean)var5;
      } else {
         return false;
      }
   }

   public void D(r6 var1, y2 var2) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.l == mc.level) {
            for (rt.f var6 : rt.f.values()) {
               for (WingsModule.l var8 : this.f) {
                  rt.X(var1, var8.left, var6);
                  rt.X(var1, var8.right, var6);
               }

               var2.v();
            }
         }
      }
   }

   public boolean j() {
      return !this.isEnabled() ? false : this.isOperational() && this.l == mc.level && this.f.stream().anyMatch(var0 -> rt.W(var0.left));
   }

   public void m(r6 var1, y2 var2) {
      if (this.isEnabled()) {
         if (this.j()) {
            for (WingsModule.l var4 : this.f) {
               rt.u(var1, var4.left);
               rt.u(var1, var4.right);
            }

            var2.v();
         }
      }
   }

   private record l(rt.i left, rt.i right) {
   }

   private record m(EntityRenderState state, rn.z angles, float size, int color) {
   }

   public record u(boolean dragon, float size, int first, int second, int dragonColor) {
   }
}
