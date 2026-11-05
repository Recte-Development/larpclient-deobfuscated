package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.k1;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.ph;
import com.larpclient.tq;
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
   private static AimAssistModule n;
   private final d9 z = this.addSetting(new d9("Anubis Range", "Maximum eye-to-hitbox acquisition distance", 4.0, 3.0, 6.0, 0.1) {
      @Override
      public String getDisplayName() {
         return "Range";
      }
   });
   private final d9 S = this.addSetting(new d9("Anubis FOV", "Total acquisition width around the crosshair", 60.0, 10.0, 180.0, 5.0) {
      @Override
      public String getDisplayName() {
         return "FOV";
      }
   });
   private final d9 K = this.addSetting(
      new d9("Anubis Speed", "Maximum correction in degrees per second, varied slightly for each target", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "Speed";
         }
      }
   );
   private final dh D = this.addSetting(new dh("Anubis Weapons Only", "Require a sword, axe, spear, mace or trident", true) {
      @Override
      public String getDisplayName() {
         return "Weapons Only";
      }
   });
   private final dh l = this.addSetting(new dh("Anubis Vertical", "Also ease pitch toward the target's core", true) {
      @Override
      public String getDisplayName() {
         return "Vertical";
      }
   });
   private final k1.j B = new k1.j();
   private Player C;
   private Player m;
   private long U;
   private long b;
   private double d = 1.0;
   private LocalPlayer I;
   private Object R;
   private String O = "Waiting for input";

   public AimAssistModule() {
      super("Aim Assist", "Smooth visible aim correction while you actively aim", Category.COMBAT, -1);
      n = this;

      for (df var2 : this.getSettings()) {
         var2.h(var1 -> this.g("Settings changed"));
      }

      this.Z();
   }

   private void Z() {
      for (df var2 : List.of(
         new dh("Require Attack", "Retired profile value", true),
         new dh("Weapons Only", "Retired profile value", true),
         new dh("Players Only", "Retired profile value", true),
         new dh("Vertical Assistance", "Retired profile value", false),
         new d9("Range", "Retired profile value", 3.0, 0.5, 6.0, 0.25),
         new d9("Aim Cone", "Retired profile value", 60.0, 10.0, 180.0, 5.0),
         new d9("Correction Speed", "Retired profile value", 60.0, 5.0, 180.0, 5.0)
      )) {
         this.addSetting(var2).H(() -> false);
      }
   }

   private String w() {
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
         if (var1.getCameraEntity() != var1.player || tq.l() != tq.y.A || m9.R()) {
            return "Paused: rotation owned";
         } else if (var1.player.isUsingItem() || var1.options.keyUse.isDown()) {
            return "Paused: using an item";
         } else {
            return this.D.P() && !D(var1.player) ? "Waiting for a melee weapon" : null;
         }
      } else {
         return "Paused: mouse not captured";
      }
   }

   private static boolean D(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.SPEARS) || var1.is(Items.MACE) || var1.is(Items.TRIDENT);
   }

   public static void E(Player var0, Entity var1) {
      if (n != null && n.isEnabled()) {
         AimAssistModule var2 = n;
         Minecraft var3 = getMc();
         if (var2 != null && var0 == var3.player && var1 instanceof Player var4 && var4.isAlive() && !var4.isRemoved() && var4.level() == var3.level) {
            var2.m = var4;
            var2.U = System.nanoTime() / 1000000L;
         }
      }
   }

   public static k1.y L(LocalPlayer var0, double var1, double var3, double var5) {
      if (n != null && n.isEnabled()) {
         AimAssistModule var7 = n;
         if (var7 != null && var7.isRuntimeActive()) {
            try {
               return var7.g(var0, var1, var3, var5);
            } catch (RuntimeException var9) {
               var7.g("Paused: aim correction failed");
               mt.P("AimAssist", "Mouse correction failed; disabling and retaining native input", var9);
               var7.disable();
               return k1.y.STILL;
            }
         } else {
            return k1.y.STILL;
         }
      } else {
         return k1.y.STILL;
      }
   }

   private k1.y g(LocalPlayer var1, double var2, double var4, double var6) {
      String var8 = this.w();
      if (var8 == null && var1 == mc.player) {
         this.X(var1);
         long var9 = System.nanoTime() / 1000000L;
         int var11 = var2 == 0.0 && var4 == 0.0 && !this.B.R(var9) ? 0 : 1;
         if (var11 == 0) {
            this.B.r();
            this.z("Waiting for mouse movement");
            return k1.y.STILL;
         }

         float[] var12 = k1.S(var1.getYRot(), var1.getXRot(), var2, var4);
         Vec3 var13 = Vec3.directionFromRotation(var12[1], var12[0]);
         this.J(this.a(var1, var9, var12[0], var12[1], var13));
         float[] var14 = this.C == null ? null : k1.z(var1.getEyePosition(), var13, k1.u(this.C.getBoundingBox()), this.z.P());
         double var15 = var14 == null ? 0.0 : ph.N(var14[0] - var12[0]);
         double var17 = var14 == null ? 0.0 : var14[1] - var12[1];
         double var19 = k1.R((Double)mc.options.sensitivity().get(), mc.options.getCameraType().isFirstPerson(), var1.isScoping());
         k1.y var21 = this.B.a(var2, var4, var15, var17, var14 != null, this.l.P(), var6, this.K.P(), this.d, var19, var9);
         if (this.C == null) {
            this.z("No eligible target");
         } else if (var14 == null) {
            this.z("On target");
         } else if (this.B.L(var15, var17, this.l.P())) {
            this.z("Following your mouse direction");
         } else {
            this.z("Assisting " + this.C.getName().getString());
         }

         return var21;
      } else {
         this.g(var8 == null ? "Waiting for input" : var8);
         return k1.y.STILL;
      }
   }

   private void X(LocalPlayer var1) {
      if (this.I != var1 || this.R != mc.level) {
         if (this.I != null || this.m != null && this.m.level() != mc.level) {
            this.m = null;
         }

         this.g("Waiting for input");
         this.I = var1;
         this.R = mc.level;
      }
   }

   private Player a(LocalPlayer var1, long var2, float var4, float var5, Vec3 var6) {
      ArrayList var7 = new ArrayList(2);
      long var8 = var2 - this.U;
      if (var8 >= 0L && var8 <= 1500L) {
         k1.w var10 = this.n(this.m, var4, var5, var6);
         if (var10 != null) {
            var7.add(var10);
         }
      }

      k1.w var16 = this.n(this.C, var4, var5, var6);
      if (var16 != null) {
         var7.add(var16);
      }

      Player var11 = k1.x(var7, this.m, var8, this.C, this.z.P(), this.S.P());
      if (var11 != null) {
         return var11;
      }

      ArrayList var12 = new ArrayList();

      for (Player var14 : mc.level.getEntitiesOfClass(Player.class, var1.getBoundingBox().inflate(this.z.P() + 0.5))) {
         k1.w var15 = this.n(var14, var4, var5, var6);
         if (var15 != null) {
            var12.add(var15);
         }
      }

      return k1.x(var12, this.m, var2 - this.U, this.C, this.z.P(), this.S.P());
   }

   private void J(Player var1) {
      if (this.C != var1) {
         this.C = var1;
         this.d = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
         this.B.Y();
      }
   }

   private k1.w<Player> n(Player var1, float var2, float var3, Vec3 var4) {
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
         LarpClient var5 = LarpClient.F();
         MCFriendModule var6 = var5 != null && var5.x() != null ? var5.x().V(MCFriendModule.class) : null;
         if (var6 != null && var6.V(var1.getName().getString())) {
            return null;
         }

         Vec3 var7 = mc.player.getEyePosition();
         double var8 = var7.distanceTo(k1.U(var7, var1.getBoundingBox()));
         if (var8 > this.z.P() + 0.5) {
            return null;
         }

         AABB var10 = k1.u(var1.getBoundingBox());
         float[] var11 = k1.z(var7, var4, var10, this.z.P());
         double var12 = k1.F(var2, var3, var11);
         if (var12 > this.S.P() / 2.0 * 1.3) {
            return null;
         }

         Vec3 var14 = k1.x(var7, var4, var10, this.z.P());
         BlockHitResult var15 = mc.level.clip(new ClipContext(var7, var14, Block.COLLIDER, Fluid.NONE, mc.player));
         return var15.getType() != Type.MISS && var15.getLocation().distanceToSqr(var7) < var14.distanceToSqr(var7) - 0.001
            ? null
            : new k1.w<>(var1, var8, var12);
      } else {
         return null;
      }
   }

   private void z(String var1) {
      if (!this.O.equals(var1)) {
         this.O = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.b >= 1000L) {
            this.b = var2;
            mt.U("AimAssist", "{}", var1);
         }
      }
   }

   private void g(String var1) {
      this.B.r();
      this.C = null;
      this.z(var1);
   }

   public static void o(LocalPlayer var0) {
      AimAssistModule var1 = n;
      if (var1 != null && var1.I == var0) {
         var1.g("Waiting for input");
      }
   }

   public static void R() {
      AimAssistModule var0 = n;
      if (var0 != null) {
         Minecraft var1 = getMc();
         if (var0.m != null && (!var0.m.isAlive() || var0.m.isRemoved() || var0.m.level() != var1.level || System.nanoTime() / 1000000L - var0.U > 30000L)) {
            var0.m = null;
         }

         if (!var0.isRuntimeActive()) {
            var0.g("Waiting for input");
         } else {
            String var2 = var0.w();
            if (var2 != null || var0.I != var1.player || var0.R != var1.level) {
               var0.g(var2 == null ? "Waiting for input" : var2);
            }
         }
      }
   }

   @Override
   public void onTick() {
      R();
   }

   @Override
   public void onEnable() {
      this.g("Waiting for input");
      mt.U(
         "AimAssist", "Aim assistance active; range={}, FOV={}, speed={}, weapons={}, vertical={}", this.z.P(), this.S.P(), this.K.P(), this.D.P(), this.l.P()
      );
   }

   @Override
   public void onDisable() {
      this.g("Waiting for input");
      this.I = null;
      this.R = null;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   @Override
   public String getStatus() {
      return this.O;
   }
}
