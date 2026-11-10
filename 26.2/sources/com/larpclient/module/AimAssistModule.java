package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.h0;
import com.larpclient.j6;
import com.larpclient.p_;
import com.larpclient.ze;
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
   private static AimAssistModule M;
   private final e v = this.addSetting(new e("Anubis Range", "Maximum eye-to-hitbox acquisition distance", 4.0, 3.0, 6.0, 0.1) {
      @Override
      public String getDisplayName() {
         return "Range";
      }
   });
   private final e d = this.addSetting(new e("Anubis FOV", "Total acquisition width around the crosshair", 60.0, 10.0, 180.0, 5.0) {
      @Override
      public String getDisplayName() {
         return "FOV";
      }
   });
   private final e s = this.addSetting(
      new e("Anubis Speed", "Maximum correction in degrees per second, varied slightly for each target", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "Speed";
         }
      }
   );
   private final s x = this.addSetting(new s("Anubis Weapons Only", "Require a sword, axe, spear, mace or trident", true) {
      @Override
      public String getDisplayName() {
         return "Weapons Only";
      }
   });
   private final s g = this.addSetting(new s("Anubis Vertical", "Also ease pitch toward the target's core", true) {
      @Override
      public String getDisplayName() {
         return "Vertical";
      }
   });
   private final ze.f Q = new ze.f();
   private Player l;
   private Player Y;
   private long e;
   private long K;
   private double O = 1.0;
   private LocalPlayer n;
   private Object T;
   private String u = "Waiting for input";

   public AimAssistModule() {
      super("Aim Assist", "Smooth visible aim correction while you actively aim", Category.COMBAT, -1);
      M = this;

      for (q var2 : this.getSettings()) {
         var2.o(var1 -> this.K("Settings changed"));
      }

      this.r();
   }

   private void r() {
      for (q var2 : List.of(
         new s("Require Attack", "Retired profile value", true),
         new s("Weapons Only", "Retired profile value", true),
         new s("Players Only", "Retired profile value", true),
         new s("Vertical Assistance", "Retired profile value", false),
         new e("Range", "Retired profile value", 3.0, 0.5, 6.0, 0.25),
         new e("Aim Cone", "Retired profile value", 60.0, 10.0, 180.0, 5.0),
         new e("Correction Speed", "Retired profile value", 60.0, 5.0, 180.0, 5.0)
      )) {
         this.addSetting(var2).P(() -> false);
      }
   }

   private String s() {
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
         if (var1.getCameraEntity() != var1.player || p_.A() != p_.o.T || j6.j()) {
            return "Paused: rotation owned";
         } else if (var1.player.isUsingItem() || var1.options.keyUse.isDown()) {
            return "Paused: using an item";
         } else {
            return this.x.getValue() && !i(var1.player) ? "Waiting for a melee weapon" : null;
         }
      } else {
         return "Paused: mouse not captured";
      }
   }

   private static boolean i(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.SPEARS) || var1.is(Items.MACE) || var1.is(Items.TRIDENT);
   }

   public static void J(Player var0, Entity var1) {
      if (M != null && M.isEnabled()) {
         AimAssistModule var2 = M;
         Minecraft var3 = getMc();
         if (var2 != null && var0 == var3.player && var1 instanceof Player var4 && var4.isAlive() && !var4.isRemoved() && var4.level() == var3.level) {
            var2.Y = var4;
            var2.e = System.nanoTime() / 1000000L;
         }
      }
   }

   public static ze.i b(LocalPlayer var0, double var1, double var3, double var5) {
      if (M != null && M.isEnabled()) {
         AimAssistModule var7 = M;
         if (var7 != null && var7.isRuntimeActive()) {
            try {
               return var7.o(var0, var1, var3, var5);
            } catch (RuntimeException var9) {
               var7.K("Paused: aim correction failed");
               com.larpclient.h.m("AimAssist", "Mouse correction failed; disabling and retaining native input", var9);
               var7.disable();
               return ze.i.STILL;
            }
         } else {
            return ze.i.STILL;
         }
      } else {
         return ze.i.STILL;
      }
   }

   private ze.i o(LocalPlayer var1, double var2, double var4, double var6) {
      String var8 = this.s();
      if (var8 == null && var1 == mc.player) {
         this.n(var1);
         long var9 = System.nanoTime() / 1000000L;
         int var11 = var2 == 0.0 && var4 == 0.0 && !this.Q.C(var9) ? 0 : 1;
         if (var11 == 0) {
            this.Q.D();
            this.g("Waiting for mouse movement");
            return ze.i.STILL;
         }

         float[] var12 = ze.H(var1.getYRot(), var1.getXRot(), var2, var4);
         Vec3 var13 = Vec3.directionFromRotation(var12[1], var12[0]);
         this.a(this.X(var1, var9, var12[0], var12[1], var13));
         float[] var14 = this.l == null ? null : ze.L(var1.getEyePosition(), var13, ze.T(this.l.getBoundingBox()), this.v.getValue());
         double var15 = var14 == null ? 0.0 : h0.R(var14[0] - var12[0]);
         double var17 = var14 == null ? 0.0 : var14[1] - var12[1];
         double var19 = ze.v((Double)mc.options.sensitivity().get(), mc.options.getCameraType().isFirstPerson(), var1.isScoping());
         ze.i var21 = this.Q.L(var2, var4, var15, var17, var14 != null, this.g.getValue(), var6, this.s.getValue(), this.O, var19, var9);
         if (this.l == null) {
            this.g("No eligible target");
         } else if (var14 == null) {
            this.g("On target");
         } else if (this.Q.B(var15, var17, this.g.getValue())) {
            this.g("Following your mouse direction");
         } else {
            this.g("Assisting " + this.l.getName().getString());
         }

         return var21;
      } else {
         this.K(var8 == null ? "Waiting for input" : var8);
         return ze.i.STILL;
      }
   }

   private void n(LocalPlayer var1) {
      if (this.n != var1 || this.T != mc.level) {
         if (this.n != null || this.Y != null && this.Y.level() != mc.level) {
            this.Y = null;
         }

         this.K("Waiting for input");
         this.n = var1;
         this.T = mc.level;
      }
   }

   private Player X(LocalPlayer var1, long var2, float var4, float var5, Vec3 var6) {
      ArrayList var7 = new ArrayList(2);
      long var8 = var2 - this.e;
      if (var8 >= 0L && var8 <= 1500L) {
         ze.b var10 = this.r(this.Y, var4, var5, var6);
         if (var10 != null) {
            var7.add(var10);
         }
      }

      ze.b var16 = this.r(this.l, var4, var5, var6);
      if (var16 != null) {
         var7.add(var16);
      }

      Player var11 = ze.b(var7, this.Y, var8, this.l, this.v.getValue(), this.d.getValue());
      if (var11 != null) {
         return var11;
      }

      ArrayList var12 = new ArrayList();

      for (Player var14 : mc.level.getEntitiesOfClass(Player.class, var1.getBoundingBox().inflate(this.v.getValue() + 0.5))) {
         ze.b var15 = this.r(var14, var4, var5, var6);
         if (var15 != null) {
            var12.add(var15);
         }
      }

      return ze.b(var12, this.Y, var2 - this.e, this.l, this.v.getValue(), this.d.getValue());
   }

   private void a(Player var1) {
      if (this.l != var1) {
         this.l = var1;
         this.O = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
         this.Q.W();
      }
   }

   private ze.b<Player> r(Player var1, float var2, float var3, Vec3 var4) {
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
         LarpClient var5 = LarpClient.R();
         MCFriendModule var6 = var5 != null && var5.z() != null ? var5.z().p(MCFriendModule.class) : null;
         if (var6 != null && var6.S(var1.getName().getString())) {
            return null;
         }

         Vec3 var7 = mc.player.getEyePosition();
         double var8 = var7.distanceTo(ze.X(var7, var1.getBoundingBox()));
         if (var8 > this.v.getValue() + 0.5) {
            return null;
         }

         AABB var10 = ze.T(var1.getBoundingBox());
         float[] var11 = ze.L(var7, var4, var10, this.v.getValue());
         double var12 = ze.v(var2, var3, var11);
         if (var12 > this.d.getValue() / 2.0 * 1.3) {
            return null;
         }

         Vec3 var14 = ze.n(var7, var4, var10, this.v.getValue());
         BlockHitResult var15 = mc.level.clip(new ClipContext(var7, var14, Block.COLLIDER, Fluid.NONE, mc.player));
         return var15.getType() != Type.MISS && var15.getLocation().distanceToSqr(var7) < var14.distanceToSqr(var7) - 0.001
            ? null
            : new ze.b<>(var1, var8, var12);
      } else {
         return null;
      }
   }

   private void g(String var1) {
      if (!this.u.equals(var1)) {
         this.u = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.K >= 1000L) {
            this.K = var2;
            com.larpclient.h.U("AimAssist", "{}", var1);
         }
      }
   }

   private void K(String var1) {
      this.Q.D();
      this.l = null;
      this.g(var1);
   }

   public static void k(LocalPlayer var0) {
      AimAssistModule var1 = M;
      if (var1 != null && var1.n == var0) {
         var1.K("Waiting for input");
      }
   }

   public static void J() {
      AimAssistModule var0 = M;
      if (var0 != null) {
         Minecraft var1 = getMc();
         if (var0.Y != null && (!var0.Y.isAlive() || var0.Y.isRemoved() || var0.Y.level() != var1.level || System.nanoTime() / 1000000L - var0.e > 30000L)) {
            var0.Y = null;
         }

         if (!var0.isRuntimeActive()) {
            var0.K("Waiting for input");
         } else {
            String var2 = var0.s();
            if (var2 != null || var0.n != var1.player || var0.T != var1.level) {
               var0.K(var2 == null ? "Waiting for input" : var2);
            }
         }
      }
   }

   @Override
   public void onTick() {
      J();
   }

   @Override
   public void onEnable() {
      this.K("Waiting for input");
      com.larpclient.h.U(
         "AimAssist",
         "Aim assistance active; range={}, FOV={}, speed={}, weapons={}, vertical={}",
         this.v.getValue(),
         this.d.getValue(),
         this.s.getValue(),
         this.x.getValue(),
         this.g.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.K("Waiting for input");
      this.n = null;
      this.T = null;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K);
   }

   @Override
   public String getStatus() {
      return this.u;
   }
}
