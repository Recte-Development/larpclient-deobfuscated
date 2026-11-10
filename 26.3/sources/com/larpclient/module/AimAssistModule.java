package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._j;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.ij;
import com.larpclient.jv;
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
   private static AimAssistModule r;
   private final b Q = this.addSetting(new b("Anubis Range", "Maximum eye-to-hitbox acquisition distance", 4.0, 3.0, 6.0, 0.1) {
      @Override
      public String getDisplayName() {
         return "Range";
      }
   });
   private final b S = this.addSetting(new b("Anubis FOV", "Total acquisition width around the crosshair", 60.0, 10.0, 180.0, 5.0) {
      @Override
      public String getDisplayName() {
         return "FOV";
      }
   });
   private final b h = this.addSetting(
      new b("Anubis Speed", "Maximum correction in degrees per second, varied slightly for each target", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "Speed";
         }
      }
   );
   private final m U = this.addSetting(new m("Anubis Weapons Only", "Require a sword, axe, spear, mace or trident", true) {
      @Override
      public String getDisplayName() {
         return "Weapons Only";
      }
   });
   private final m N = this.addSetting(new m("Anubis Vertical", "Also ease pitch toward the target's core", true) {
      @Override
      public String getDisplayName() {
         return "Vertical";
      }
   });
   private final jv.n H = new jv.n();
   private Player u;
   private Player v;
   private long X;
   private long P;
   private double m = 1.0;
   private LocalPlayer t;
   private Object D;
   private String l = "Waiting for input";

   public AimAssistModule() {
      super("Aim Assist", "Smooth visible aim correction while you actively aim", Category.COMBAT, -1);
      r = this;

      for (r var2 : this.getSettings()) {
         var2.w(var1 -> this.F("Settings changed"));
      }

      this.K();
   }

   private void K() {
      for (r var2 : List.of(
         new m("Require Attack", "Retired profile value", true),
         new m("Weapons Only", "Retired profile value", true),
         new m("Players Only", "Retired profile value", true),
         new m("Vertical Assistance", "Retired profile value", false),
         new b("Range", "Retired profile value", 3.0, 0.5, 6.0, 0.25),
         new b("Aim Cone", "Retired profile value", 60.0, 10.0, 180.0, 5.0),
         new b("Correction Speed", "Retired profile value", 60.0, 5.0, 180.0, 5.0)
      )) {
         this.addSetting(var2).a(() -> false);
      }
   }

   private String B() {
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
         if (var1.getCameraEntity() != var1.player || ij.L() != ij.r.X || b6.x()) {
            return "Paused: rotation owned";
         } else if (var1.player.isUsingItem() || var1.options.keyUse.isDown()) {
            return "Paused: using an item";
         } else {
            return this.U.getValue() && !Z(var1.player) ? "Waiting for a melee weapon" : null;
         }
      } else {
         return "Paused: mouse not captured";
      }
   }

   private static boolean Z(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.SPEARS) || var1.is(Items.MACE) || var1.is(Items.TRIDENT);
   }

   public static void N(Player var0, Entity var1) {
      if (r != null && r.isEnabled()) {
         AimAssistModule var2 = r;
         Minecraft var3 = getMc();
         if (var2 != null && var0 == var3.player && var1 instanceof Player var4 && var4.isAlive() && !var4.isRemoved() && var4.level() == var3.level) {
            var2.v = var4;
            var2.X = System.nanoTime() / 1000000L;
         }
      }
   }

   public static jv.y w(LocalPlayer var0, double var1, double var3, double var5) {
      if (r != null && r.isEnabled()) {
         AimAssistModule var7 = r;
         if (var7 != null && var7.isRuntimeActive()) {
            try {
               return var7.X(var0, var1, var3, var5);
            } catch (RuntimeException var9) {
               var7.F("Paused: aim correction failed");
               _q.o("AimAssist", "Mouse correction failed; disabling and retaining native input", var9);
               var7.disable();
               return jv.y.STILL;
            }
         } else {
            return jv.y.STILL;
         }
      } else {
         return jv.y.STILL;
      }
   }

   private jv.y X(LocalPlayer var1, double var2, double var4, double var6) {
      String var8 = this.B();
      if (var8 == null && var1 == mc.player) {
         this.h(var1);
         long var9 = System.nanoTime() / 1000000L;
         int var11 = var2 == 0.0 && var4 == 0.0 && !this.H.i(var9) ? 0 : 1;
         if (var11 == 0) {
            this.H.S();
            this.g("Waiting for mouse movement");
            return jv.y.STILL;
         }

         float[] var12 = jv.P(var1.getYRot(), var1.getXRot(), var2, var4);
         Vec3 var13 = Vec3.directionFromRotation(var12[1], var12[0]);
         this.Y(this.h(var1, var9, var12[0], var12[1], var13));
         float[] var14 = this.u == null ? null : jv.g(var1.getEyePosition(), var13, jv.L(this.u.getBoundingBox()), this.Q.getValue());
         double var15 = var14 == null ? 0.0 : _j.V(var14[0] - var12[0]);
         double var17 = var14 == null ? 0.0 : var14[1] - var12[1];
         double var19 = jv.H((Double)mc.options.sensitivity().get(), mc.options.getCameraType().isFirstPerson(), var1.isScoping());
         jv.y var21 = this.H.U(var2, var4, var15, var17, var14 != null, this.N.getValue(), var6, this.h.getValue(), this.m, var19, var9);
         if (this.u == null) {
            this.g("No eligible target");
         } else if (var14 == null) {
            this.g("On target");
         } else if (this.H.Y(var15, var17, this.N.getValue())) {
            this.g("Following your mouse direction");
         } else {
            this.g("Assisting " + this.u.getName().getString());
         }

         return var21;
      } else {
         this.F(var8 == null ? "Waiting for input" : var8);
         return jv.y.STILL;
      }
   }

   private void h(LocalPlayer var1) {
      if (this.t != var1 || this.D != mc.level) {
         if (this.t != null || this.v != null && this.v.level() != mc.level) {
            this.v = null;
         }

         this.F("Waiting for input");
         this.t = var1;
         this.D = mc.level;
      }
   }

   private Player h(LocalPlayer var1, long var2, float var4, float var5, Vec3 var6) {
      ArrayList var7 = new ArrayList(2);
      long var8 = var2 - this.X;
      if (var8 >= 0L && var8 <= 1500L) {
         jv.v var10 = this.j(this.v, var4, var5, var6);
         if (var10 != null) {
            var7.add(var10);
         }
      }

      jv.v var16 = this.j(this.u, var4, var5, var6);
      if (var16 != null) {
         var7.add(var16);
      }

      Player var11 = jv.b(var7, this.v, var8, this.u, this.Q.getValue(), this.S.getValue());
      if (var11 != null) {
         return var11;
      }

      ArrayList var12 = new ArrayList();

      for (Player var14 : mc.level.getEntitiesOfClass(Player.class, var1.getBoundingBox().inflate(this.Q.getValue() + 0.5))) {
         jv.v var15 = this.j(var14, var4, var5, var6);
         if (var15 != null) {
            var12.add(var15);
         }
      }

      return jv.b(var12, this.v, var2 - this.X, this.u, this.Q.getValue(), this.S.getValue());
   }

   private void Y(Player var1) {
      if (this.u != var1) {
         this.u = var1;
         this.m = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
         this.H.e();
      }
   }

   private jv.v<Player> j(Player var1, float var2, float var3, Vec3 var4) {
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
         LarpClient var5 = LarpClient.N();
         MCFriendModule var6 = var5 != null && var5.j() != null ? var5.j().y(MCFriendModule.class) : null;
         if (var6 != null && var6.M(var1.getName().getString())) {
            return null;
         }

         Vec3 var7 = mc.player.getEyePosition();
         double var8 = var7.distanceTo(jv.N(var7, var1.getBoundingBox()));
         if (var8 > this.Q.getValue() + 0.5) {
            return null;
         }

         AABB var10 = jv.L(var1.getBoundingBox());
         float[] var11 = jv.g(var7, var4, var10, this.Q.getValue());
         double var12 = jv.O(var2, var3, var11);
         if (var12 > this.S.getValue() / 2.0 * 1.3) {
            return null;
         }

         Vec3 var14 = jv.o(var7, var4, var10, this.Q.getValue());
         BlockHitResult var15 = mc.level.clip(new ClipContext(var7, var14, Block.COLLIDER, Fluid.NONE, mc.player));
         return var15.getType() != Type.MISS && var15.getLocation().distanceToSqr(var7) < var14.distanceToSqr(var7) - 0.001
            ? null
            : new jv.v<>(var1, var8, var12);
      } else {
         return null;
      }
   }

   private void g(String var1) {
      if (!this.l.equals(var1)) {
         this.l = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.P >= 1000L) {
            this.P = var2;
            _q.c("AimAssist", "{}", var1);
         }
      }
   }

   private void F(String var1) {
      this.H.S();
      this.u = null;
      this.g(var1);
   }

   public static void H(LocalPlayer var0) {
      AimAssistModule var1 = r;
      if (var1 != null && var1.t == var0) {
         var1.F("Waiting for input");
      }
   }

   public static void d() {
      AimAssistModule var0 = r;
      if (var0 != null) {
         Minecraft var1 = getMc();
         if (var0.v != null && (!var0.v.isAlive() || var0.v.isRemoved() || var0.v.level() != var1.level || System.nanoTime() / 1000000L - var0.X > 30000L)) {
            var0.v = null;
         }

         if (!var0.isRuntimeActive()) {
            var0.F("Waiting for input");
         } else {
            String var2 = var0.B();
            if (var2 != null || var0.t != var1.player || var0.D != var1.level) {
               var0.F(var2 == null ? "Waiting for input" : var2);
            }
         }
      }
   }

   @Override
   public void onTick() {
      d();
   }

   @Override
   public void onEnable() {
      this.F("Waiting for input");
      _q.c(
         "AimAssist",
         "Aim assistance active; range={}, FOV={}, speed={}, weapons={}, vertical={}",
         this.Q.getValue(),
         this.S.getValue(),
         this.h.getValue(),
         this.U.getValue(),
         this.N.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.F("Waiting for input");
      this.t = null;
      this.D = null;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F);
   }

   @Override
   public String getStatus() {
      return this.l;
   }
}
