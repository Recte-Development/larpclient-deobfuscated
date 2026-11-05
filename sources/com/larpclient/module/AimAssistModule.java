package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.ep;
import com.larpclient.j6;
import com.larpclient.px;
import com.larpclient.r4;
import com.larpclient.rr;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AimAssistModule extends Module {
   private static AimAssistModule a;
   private final lc t = this.addSetting(new lc("Anubis Range", "Maximum eye-to-hitbox acquisition distance", 4.0, 3.0, 6.0, 0.1) {
      @Override
      public String getDisplayName() {
         return "Range";
      }
   });
   private final lc L = this.addSetting(new lc("Anubis FOV", "Total acquisition width around the crosshair", 60.0, 10.0, 180.0, 5.0) {
      @Override
      public String getDisplayName() {
         return "FOV";
      }
   });
   private final lc k = this.addSetting(
      new lc("Anubis Speed", "Maximum correction in degrees per second, varied slightly for each target", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "Speed";
         }
      }
   );
   private final ln V = this.addSetting(new ln("Anubis Weapons Only", "Require a sword, axe, spear, mace or trident", true) {
      @Override
      public String getDisplayName() {
         return "Weapons Only";
      }
   });
   private final ln n = this.addSetting(new ln("Anubis Vertical", "Also ease pitch toward the target's core", true) {
      @Override
      public String getDisplayName() {
         return "Vertical";
      }
   });
   private final px.f w = new px.f();
   private Player G;
   private Player F;
   private long P;
   private long f;
   private double e = 1.0;
   private LocalPlayer r;
   private Object Z;
   private String Y = "Waiting for input";

   public AimAssistModule() {
      super("Aim Assist", "Smooth visible aim correction while you actively aim", Category.COMBAT, -1);
      a = this;

      for (l6 var2 : this.getSettings()) {
         var2.W(var1 -> this.i("Settings changed"));
      }

      this.a();
   }

   private void a() {
      for (l6 var2 : List.of(
         new ln("Require Attack", "Retired profile value", true),
         new ln("Weapons Only", "Retired profile value", true),
         new ln("Players Only", "Retired profile value", true),
         new ln("Vertical Assistance", "Retired profile value", false),
         new lc("Range", "Retired profile value", 3.0, 0.5, 6.0, 0.25),
         new lc("Aim Cone", "Retired profile value", 60.0, 10.0, 180.0, 5.0),
         new lc("Correction Speed", "Retired profile value", 60.0, 5.0, 180.0, 5.0)
      )) {
         this.addSetting(var2).x(() -> false);
      }
   }

   private String Y() {
      Minecraft var1 = getMc();
      if (var1.player == null || var1.level == null || var1.gameMode == null) {
         return "Waiting for world";
      }

      if (!var1.player.isAlive() || var1.player.isSpectator() || var1.player.isSleeping()) {
         return "Paused: player unavailable";
      }

      if (var1.gui.screen() != null || var1.gui.overlay() != null) {
         return "Paused: menu open";
      }

      if (var1.isWindowActive() && var1.mouseHandler.isMouseGrabbed()) {
         if (var1.getCameraEntity() != var1.player || rr.u() != rr.f.i || ep.f()) {
            return "Paused: rotation owned";
         } else if (var1.player.isUsingItem() || var1.options.keyUse.isDown()) {
            return "Paused: using an item";
         } else {
            return this.V.getValue() && !I(var1.player) ? "Waiting for a melee weapon" : null;
         }
      } else {
         return "Paused: mouse not captured";
      }
   }

   private static boolean I(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.SPEARS) || var1.is(Items.MACE) || var1.is(Items.TRIDENT);
   }

   public static void F(Player var0, Entity var1) {
      if (a != null && a.isEnabled()) {
         AimAssistModule var2 = a;
         Minecraft var3 = getMc();
         if (var2 != null && var0 == var3.player && var1 instanceof Player var4 && var4.isAlive() && !var4.isRemoved() && var4.level() == var3.level) {
            var2.F = var4;
            var2.P = System.nanoTime() / 1000000L;
         }
      }
   }

   public static px.k P(LocalPlayer var0, double var1, double var3, double var5) {
      if (a != null && a.isEnabled()) {
         AimAssistModule var7 = a;
         if (var7 != null && var7.isRuntimeActive()) {
            try {
               return var7.z(var0, var1, var3, var5);
            } catch (RuntimeException var9) {
               var7.i("Paused: aim correction failed");
               r4.r("AimAssist", "Mouse correction failed; disabling and retaining native input", var9);
               var7.disable();
               return px.k.STILL;
            }
         } else {
            return px.k.STILL;
         }
      } else {
         return px.k.STILL;
      }
   }

   private px.k z(LocalPlayer var1, double var2, double var4, double var6) {
      String var8 = this.Y();
      if (var8 == null && var1 == mc.player) {
         this.f(var1);
         long var9 = System.nanoTime() / 1000000L;
         int var11 = var2 == 0.0 && var4 == 0.0 && !this.w.T(var9) ? 0 : 1;
         if (var11 == 0) {
            this.w.g();
            this.R("Waiting for mouse movement");
            return px.k.STILL;
         }

         float[] var12 = px.h(var1.getYRot(), var1.getXRot(), var2, var4);
         Vec3 var13 = Vec3.directionFromRotation(var12[1], var12[0]);
         this.j(this.Y(var1, var9, var12[0], var12[1], var13));
         float[] var14 = this.G == null ? null : px.k(var1.getEyePosition(), var13, px.L(this.G.getBoundingBox()), this.t.getValue());
         double var15 = var14 == null ? 0.0 : j6.U(var14[0] - var12[0]);
         double var17 = var14 == null ? 0.0 : var14[1] - var12[1];
         double var19 = px.c((Double)mc.options.sensitivity().get(), mc.options.getCameraType().isFirstPerson(), var1.isScoping());
         px.k var21 = this.w.O(var2, var4, var15, var17, var14 != null, this.n.getValue(), var6, this.k.getValue(), this.e, var19, var9);
         if (this.G == null) {
            this.R("No eligible target");
         } else if (var14 == null) {
            this.R("On target");
         } else if (this.w.R(var15, var17, this.n.getValue())) {
            this.R("Following your mouse direction");
         } else {
            this.R("Assisting " + this.G.getName().getString());
         }

         return var21;
      } else {
         this.i(var8 == null ? "Waiting for input" : var8);
         return px.k.STILL;
      }
   }

   private void f(LocalPlayer var1) {
      if (this.r != var1 || this.Z != mc.level) {
         if (this.r != null || this.F != null && this.F.level() != mc.level) {
            this.F = null;
         }

         this.i("Waiting for input");
         this.r = var1;
         this.Z = mc.level;
      }
   }

   private Player Y(LocalPlayer var1, long var2, float var4, float var5, Vec3 var6) {
      ArrayList var7 = new ArrayList(2);
      long var8 = var2 - this.P;
      if (var8 >= 0L && var8 <= 1500L) {
         px.l var10 = this.H(this.F, var4, var5, var6);
         if (var10 != null) {
            var7.add(var10);
         }
      }

      px.l var16 = this.H(this.G, var4, var5, var6);
      if (var16 != null) {
         var7.add(var16);
      }

      Player var11 = px.S(var7, this.F, var8, this.G, this.t.getValue(), this.L.getValue());
      if (var11 != null) {
         return var11;
      }

      ArrayList var12 = new ArrayList();

      for (Player var14 : mc.level.getEntitiesOfClass(Player.class, var1.getBoundingBox().inflate(this.t.getValue() + 0.5))) {
         px.l var15 = this.H(var14, var4, var5, var6);
         if (var15 != null) {
            var12.add(var15);
         }
      }

      return px.S(var12, this.F, var2 - this.P, this.G, this.t.getValue(), this.L.getValue());
   }

   private void j(Player var1) {
      if (this.G != var1) {
         this.G = var1;
         this.e = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
         this.w.h();
      }
   }

   private px.l<Player> H(Player var1, float var2, float var3, Vec3 var4) {
      if (var1 != null
         && var1 != mc.player
         && var1.isAlive()
         && !var1.isRemoved()
         && var1.level() == mc.level
         && !var1.isSpectator()
         && !var1.isCreative()
         && var1.isAttackable()
         && !var1.isInvisible()
         && mc.player.getVehicle() != var1
         && mc.player.canHarmPlayer(var1)
         && mc.player.connection.getPlayerInfo(var1.getUUID()) != null) {
         LarpClient var5 = LarpClient.y();
         MCFriendModule var6 = var5 != null && var5.e() != null ? var5.e().y(MCFriendModule.class) : null;
         if (var6 != null && var6.e(var1.getName().getString())) {
            return null;
         }

         Vec3 var7 = mc.player.getEyePosition();
         double var8 = var7.distanceTo(px.U(var7, var1.getBoundingBox()));
         if (var8 > this.t.getValue() + 0.5) {
            return null;
         }

         AABB var10 = px.L(var1.getBoundingBox());
         float[] var11 = px.k(var7, var4, var10, this.t.getValue());
         double var12 = px.U(var2, var3, var11);
         if (var12 > this.L.getValue() / 2.0 * 1.3) {
            return null;
         }

         Vec3 var14 = px.c(var7, var4, var10, this.t.getValue());
         BlockHitResult var15 = mc.level.clip(new ClipContext(var7, var14, Block.COLLIDER, Fluid.NONE, mc.player));
         return var15.getType() != Type.MISS && var15.getLocation().distanceToSqr(var7) < var14.distanceToSqr(var7) - 0.001
            ? null
            : new px.l<>(var1, var8, var12);
      } else {
         return null;
      }
   }

   private void R(String var1) {
      if (!this.Y.equals(var1)) {
         this.Y = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.f >= 1000L) {
            this.f = var2;
            r4.g("AimAssist", "{}", var1);
         }
      }
   }

   private void i(String var1) {
      this.w.g();
      this.G = null;
      this.R(var1);
   }

   public static void w(LocalPlayer var0) {
      AimAssistModule var1 = a;
      if (var1 != null && var1.r == var0) {
         var1.i("Waiting for input");
      }
   }

   public static void U() {
      AimAssistModule var0 = a;
      if (var0 != null) {
         Minecraft var1 = getMc();
         if (var0.F != null && (!var0.F.isAlive() || var0.F.isRemoved() || var0.F.level() != var1.level || System.nanoTime() / 1000000L - var0.P > 30000L)) {
            var0.F = null;
         }

         if (!var0.isRuntimeActive()) {
            var0.i("Waiting for input");
         } else {
            String var2 = var0.Y();
            if (var2 != null || var0.r != var1.player || var0.Z != var1.level) {
               var0.i(var2 == null ? "Waiting for input" : var2);
            }
         }
      }
   }

   @Override
   public void onTick() {
      U();
   }

   @Override
   public void onEnable() {
      this.i("Waiting for input");
      r4.g(
         "AimAssist",
         "Aim assistance active; range={}, FOV={}, speed={}, weapons={}, vertical={}",
         this.t.getValue(),
         this.L.getValue(),
         this.k.getValue(),
         this.V.getValue(),
         this.n.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.i("Waiting for input");
      this.r = null;
      this.Z = null;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   @Override
   public String getStatus() {
      return this.Y;
   }
}
