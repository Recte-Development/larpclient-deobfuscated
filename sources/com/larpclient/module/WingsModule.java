package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._3;
import com.larpclient.hk;
import com.larpclient.ki;
import com.larpclient.kx;
import com.larpclient.mt;
import com.larpclient.tj;
import com.larpclient.ty;
import com.larpclient.ut;
import com.larpclient.xh;
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
   public static final String t = "Luminous";
   public static final String l = "Ender Dragon";
   private static WingsModule i;
   private final dh D = this.addSetting(new dh("Self", "Show your wings in third person", true));
   private final dh W = this.addSetting(new dh("Friends", "Show wings on friends", true));
   private final dh V = this.addSetting(new dh("Others", "Show wings on other players", false));
   private final d9 U = this.addSetting(new d9("Size", "Wing size", 1.0, 0.75, 1.35, 0.05));
   private final dh m = this.addSetting(new dh("Custom Colors", "Cycle between your two colors", true));
   private final dk q = this.addSetting(new dk("Color 1", "First wing color", -52586));
   private final dk c = this.addSetting(new dk("Color 2", "Second wing color", -13461761));
   private final d6 b = this.addSetting(new d6("Style", "Wing design", "Luminous", "Luminous", "Ender Dragon"));
   private final dk y = this.addSetting(new dk("Dragon Color", "Tints the Ender Dragon texture; white keeps it vanilla and alpha sets opacity", -1));
   public static final int a = 32;
   private List<WingsModule.o> d = List.of();
   private List<WingsModule.i> X = List.of();
   private final IdentityHashMap<Object, ut.r> N = new IdentityHashMap<>();
   private ClientLevel Y;
   private ClientLevel S;
   private hk.t n;
   private final PoseStack J = new PoseStack();
   private final PoseStack K = new PoseStack();

   public WingsModule() {
      super("Wings", "Wear luminous membrane wings or Ender Dragon wings", Category.VISUALS, -1);
      i = this;
      this.m.H(() -> this.b.S("Luminous"));
      this.q.H(() -> this.b.S("Luminous") && this.m.P());
      this.c.H(() -> this.b.S("Luminous") && this.m.P());
      this.y.H(() -> this.b.S("Ender Dragon"));
      this.b.h(var0 -> mt.K("Wings", "Style={}", var0));
   }

   public static WingsModule d() {
      return i;
   }

   public List<df<?>> x() {
      return List.of(this.D, this.W, this.V);
   }

   public List<df<?>> o() {
      return List.of(this.b, this.U, this.m, this.q, this.c, this.y);
   }

   public boolean P() {
      return this.isEnabled() ? this.b.S("Ender Dragon") : false;
   }

   public WingsModule.n X() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.D.P()) {
            return null;
         } else if (this.P()) {
            return this.y.P() >>> 24 == 0 ? null : new WingsModule.n(true, this.U.P().floatValue(), 0, 0, this.y.P());
         } else {
            return new WingsModule.n(
               false, this.U.P().floatValue(), this.m.P() ? this.q.P() : com.larpclient.f.C, this.m.P() ? this.c.P() : com.larpclient.f.O, 0
            );
         }
      } else {
         return null;
      }
   }

   public static int F(int var0, int var1, double var2) {
      return _3.D(var0, var1, (Math.sin(var2 * 2.0) + 1.0) / 2.0);
   }

   public static int G(int var0, int var1, double var2) {
      return _3.D(var0, var1, (Math.sin(var2 * 2.0 + 2.1) + 1.0) / 2.0);
   }

   @Override
   public void onEnable() {
      mt.K(
         "Wings",
         this.P()
            ? "Ender Dragon wings enabled; vanilla dragon wing parts on the native torso, 48 quads per wearer, 32 nearby previews"
            : "Articulated membrane wings enabled on the native torso; shared bloom, 146 base quads per wearer, 32 nearby previews"
      );
   }

   @Override
   public void onDisable() {
      this.d = List.of();
      this.X = List.of();
      this.Y = null;
      this.N.clear();
      this.S = null;
   }

   public void E(float var1) {
      if (this.isEnabled()) {
         this.d = List.of();
         this.X = List.of();
         this.Y = null;
         if (this.isOperational() && this.isInGame()) {
            if (this.S != mc.level) {
               this.N.clear();
               this.S = mc.level;
            }

            MCFriendModule var2 = LarpClient.F().x().V(MCFriendModule.class);
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Set var5 = Collections.newSetFromMap(new IdentityHashMap());
            int var6 = 0;
            float var7 = this.U.P().floatValue();
            boolean var8 = this.P();
            int var9 = this.y.P();
            int var10 = this.m.P() ? this.q.P() : com.larpclient.f.C;
            int var11 = this.m.P() ? this.c.P() : com.larpclient.f.O;
            int var12 = F(var10, var11, ty.K());
            int var13 = G(var10, var11, ty.K());

            for (AbstractClientPlayer var15 : mc.level.players()) {
               if (var15 == mc.player) {
                  if (!this.D.P() || mc.options.getCameraType().isFirstPerson()) {
                     continue;
                  }
               } else {
                  int var16 = var2 != null && var2.V(var15.getName().getString()) ? 1 : 0;
                  if (!var16 != 0 ? this.W.P() : this.V.P() || var6 >= 32) {
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

                  float var30 = Feature3DUIModule.j(var15, Mth.rotLerp(var1, var15.yBodyRotO, var15.yBodyRot), var1);
                  float var17 = var15.tickCount + var1;
                  float var18 = var15.walkAnimation.speed(var1);
                  Vec3 var19 = var15.getPosition(var1);
                  ut.r var20 = this.N.computeIfAbsent(var15, var0 -> new ut.r());
                  var20.i(var17, var15.isCrouching(), var18, var15.isFallFlying(), C(var15), var19);
                  var5.add(var15);
                  if (var8) {
                     float var31 = hk.f(var20.x(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     var20.I(hk.g(var20.d(), var20.e(), var20.B(), var31));
                     if (var9 >>> 24 != 0) {
                        hk.v var32 = hk.w(var20.l() + hk.f(var15.getId()), var20.d(), var20.e(), var20.B(), var31);
                        var4.add(new WingsModule.i(tj.s(var15, var1), var32, var7, var9));
                     }
                  } else {
                     EntityRenderState var21 = tj.s(var15, var1);
                     Pose var22 = tj.o(var21, this.K);
                     int var23 = var21 instanceof HumanoidRenderState var24 && var24.isCrouching ? 1 : 0;
                     float var33 = var22 == null ? var20.x() : hk.f(var20.x(), var15.isFallFlying(), var15.getFallFlyingTicks() + var1);
                     ut.i var25 = ut.O(var20.e(), var33, var15.getFallFlyingTicks() + var1, var15.getViewXRot(var1));
                     ut.c var26 = ut.Z(var17, var20.d(), var20.e(), var33);
                     Vec3 var27 = var22 == null ? var19 : mc.gameRenderer.mainCamera().position();
                     Matrix4f var28 = var22 == null
                        ? ut.o(var25, var30, var17, var20.d(), var7, -1)
                        : new Matrix4f(var22.pose()).mul(ut.G(var25, var17, var20.d(), var7, -1, (boolean)var23));
                     Matrix4f var29 = var22 == null
                        ? ut.o(var25, var30, var17, var20.d(), var7, 1)
                        : new Matrix4f(var22.pose()).mul(ut.G(var25, var17, var20.d(), var7, 1, (boolean)var23));
                     var3.add(new WingsModule.o(ut.m(var27, var28, -1, var26, var12, var13, var7), ut.m(var27, var29, 1, var26, var12, var13, var7)));
                  }
               }
            }

            this.N.keySet().removeIf(var1x -> !var5.contains(var1x));
            this.Y = mc.level;
            this.d = List.copyOf(var3);
            this.X = List.copyOf(var4);
         } else {
            this.N.clear();
            this.S = null;
         }
      }
   }

   private static boolean C(Player var0) {
      return !var0.onGround() && !var0.isPassenger() && !var0.onClimbable() && !var0.isFallFlying();
   }

   public boolean D(PoseStack var1, xh var2) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational() && this.Y == mc.level && !this.X.isEmpty()) {
         if (this.n == null) {
            this.n = hk.t.T();
            mt.K("Wings", "Ender Dragon wing parts ready from the vanilla layer definition; texture={}", hk.S);
         }

         long var3 = kx.G();
         byte var5 = 0;

         for (WingsModule.i var7 : this.X) {
            Pose var8 = tj.o(var7.state, var1);
            if (var8 != null) {
               this.J.last().set(var8);
               this.n.A(var7.angles);
               int var9 = var7.state instanceof HumanoidRenderState var10 && var10.isCrouching ? 1 : 0;
               this.n.u(this.J, var2.U(hk.Y(var7.color)), var7.size, (boolean)var9, var7.state.lightCoords, var7.color);
               var5 = 1;
            }
         }

         kx.u("Wings Ender Dragon attachment and submission", var3);
         return (boolean)var5;
      } else {
         return false;
      }
   }

   public void b(ki var1, xh var2) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.Y == mc.level) {
            for (ut.k var6 : ut.k.values()) {
               for (WingsModule.o var8 : this.d) {
                  ut.Q(var1, var8.left, var6);
                  ut.Q(var1, var8.right, var6);
               }

               var2.b();
            }
         }
      }
   }

   public boolean K() {
      return !this.isEnabled() ? false : this.isOperational() && this.Y == mc.level && this.d.stream().anyMatch(var0 -> ut.A(var0.left));
   }

   public void E(ki var1, xh var2) {
      if (this.isEnabled()) {
         if (this.K()) {
            for (WingsModule.o var4 : this.d) {
               ut.q(var1, var4.left);
               ut.q(var1, var4.right);
            }

            var2.b();
         }
      }
   }

   private record i(EntityRenderState state, hk.v angles, float size, int color) {
   }

   public record n(boolean dragon, float size, int first, int second, int dragonColor) {
   }

   private record o(ut.v left, ut.v right) {
   }
}
