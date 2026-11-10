package com.larpclient;

import com.larpclient.module.AnchorMacroModule;
import com.larpclient.module.AnchorTapModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.CriticalsModule;
import com.larpclient.module.CrystalTapModule;
import com.larpclient.module.Module;
import com.larpclient.module.RodSwapModule;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Rot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class ta {
   private static final List<ta.g> l = new ArrayList<>();
   private static boolean H;
   private static boolean a;
   private static boolean u;
   private static final List<ta.h> o = new ArrayList<>();
   private static ta.h T;
   private static int F;
   private static long p = Long.MIN_VALUE;
   private static final Set<Integer> Z = new HashSet<>();
   private static String R;
   private static boolean q;

   public static void F(Module var0, Runnable var1, BooleanSupplier var2) {
      l.add(new ta.g(var0, var1, var2));
   }

   public static boolean E(Module var0) {
      N();
      return S(var0) && (R == null || R.equals(var0.getName()));
   }

   public static boolean k(Module var0, Runnable var1, BooleanSupplier var2) {
      N();
      if (var0 != null && var0.isRuntimeActive() && Y() && var2.getAsBoolean() && T == null && !H && Z.isEmpty() && (R == null || R.equals(var0.getName()))) {
         R = var0.getName();
         H = (boolean)1;
         a = (boolean)1;

         try {
            var1.run();
         } finally {
            a = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean g(Runnable var0) {
      N();
      if (h() && !p_.V() && T == null && !H && Z.isEmpty() && R == null) {
         R = "Eating";
         H = (boolean)1;
         a = (boolean)1;

         try {
            var0.run();
         } finally {
            a = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean k(Module var0, Entity var1, double var2) {
      N();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && T == null && !H && W(var0.getName(), var1) && q(var1, var2)) {
         Minecraft var4 = Minecraft.getInstance();
         ta.h var5 = new ta.h(var0, var1, var2, false, () -> true, 1);
         k(var5);
         return si.o(var4.player);
      } else {
         return false;
      }
   }

   public static boolean B(Module var0, Entity var1, Runnable var2) {
      N();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && S(var0) && T == null && !H && l(var0.getName(), var1)) {
         q = (boolean)1;

         try {
            var2.run();
         } finally {
            q = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean j(Module var0, Entity var1, Runnable var2) {
      N();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && S(var0) && Z.contains(var1.getId())) {
         q = (boolean)1;

         try {
            var2.run();
         } finally {
            q = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean d() {
      return q || a;
   }

   public static boolean O() {
      if (a) {
         return true;
      } else if (!Y()) {
         return false;
      } else {
         N();
         if (AnchorTapModule.b() || AnchorMacroModule.K()) {
            return false;
         } else if (CrystalTapModule.L()) {
            return false;
         } else if (T == null && (R == null || R.equals("Manual"))) {
            H = (boolean)1;
            R = "Manual";
            u = (boolean)1;
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean i() {
      N();
      return u;
   }

   public static void o() {
      u = (boolean)0;
   }

   public static boolean H() {
      return h() && Minecraft.getInstance().gui.screen() == null;
   }

   private static boolean h() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && var0.level != null && var0.gameMode != null && var0.player.isAlive() && !var0.player.isSpectator();
   }

   public static boolean Y() {
      return H() && !p_.V();
   }

   public static boolean S(Module var0) {
      return e(var0) && (!p_.V() || X(var0));
   }

   public static boolean X(Module var0) {
      return var0 != null && var0.isRuntimeActive() && var0.allowsDetachedCamera();
   }

   public static boolean e(Module var0) {
      return h() && J(Minecraft.getInstance().gui.screen() != null, var0);
   }

   public static boolean J(boolean var0, Module var1) {
      return !var0 || var1 != null && var1.isRuntimeActive() && var1.allowsScreenInteractions();
   }

   public static boolean t() {
      return !o.isEmpty() || !l.isEmpty() || T != null;
   }

   public static void U() {
      o.clear();
      l.clear();
      N();
   }

   private static void N() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != null) {
         c(var0.getGameTime());
      }
   }

   private static void c(long var0) {
      if (p != var0) {
         p = var0;
         Z.clear();
         R = null;
         H = (boolean)0;
         u = (boolean)0;
      }
   }

   public static void B() {
      kq.Q();
      t1.r();
      o.clear();
      l.clear();
      H = (boolean)0;
      u = (boolean)0;
      a = (boolean)0;
      T = null;
      Z.clear();
      R = null;
      p = Long.MIN_VALUE;
      q = (boolean)0;
   }

   public static void s(Module var0) {
      o.removeIf(var1 -> var1.source == var0);
      l.removeIf(var1 -> var1.source == var0);
      if (T != null && T.source == var0) {
         T = null;
      }
   }

   public static void Z(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5) {
      F(var0, var1, var2, var4, var5, 1);
   }

   public static void F(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6) {
      o.add(new ta.h(var0, var1, var2, var4, var5, var6));
   }

   public static void I(Module var0, Entity var1, double var2, BooleanSupplier var4, Supplier<float[]> var5, Runnable var6) {
      o.add(new ta.h(var0, var1, var2, false, var4, 1, var5, var6, var0x -> {}));
   }

   public static double M(Entity var0) {
      return wi.j(Minecraft.getInstance().player.getEyePosition(), var0.getBoundingBox());
   }

   public static boolean q(Entity var0, double var1) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      return var3 != null && f(var0, var1, var3.getLookAngle());
   }

   public static void M(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6, Runnable var7, Consumer<String> var8) {
      o.add(new ta.h(var0, var1, var2, var4, var5, var6, null, var7, var8));
   }

   public static boolean K() {
      return Minecraft.getInstance().player != null && Minecraft.getInstance().player.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   public static double c(double var0) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 == null ? 0.0 : Math.max(0.0, Math.min(var0, var2.entityInteractionRange()));
   }

   public static boolean f(Entity var0, double var1, Vec3 var3) {
      return I(var0, var1, var3) == null;
   }

   public static String I(Entity var0, double var1, Vec3 var3) {
      if (!Y() || var0 == null) {
         return "interaction ownership";
      }

      if (RodSwapModule.o()) {
         return "temporary rod selection";
      }

      Minecraft var4 = Minecraft.getInstance();
      if (var0 != var4.player && var0.level() == var4.level && var0.isAlive() && !var0.isRemoved() && !var0.isSpectator() && var0.isPickable()) {
         double var5 = c(var1);
         Vec3 var7 = var4.player.getEyePosition();
         if (M(var0) > var5 + 1.0E-7) {
            return "range";
         } else {
            Optional var8 = wi.x(var7, var3, var0.getBoundingBox().inflate(var0.getPickRadius()), var5);
            if (var8.isEmpty()) {
               return "alignment";
            } else {
               return v(var0, var7, (Vec3)var8.get(), A(var0, var7)) ? null : "obstruction";
            }
         }
      } else {
         return "target invalid";
      }
   }

   private static List<Entity> A(Entity var0, Vec3 var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2.level.getEntities(var2.player, var0.getBoundingBox().minmax(new AABB(var1, var1)).inflate(1.0));
   }

   private static boolean v(Entity var0, Vec3 var1, Vec3 var2, List<Entity> var3) {
      Minecraft var4 = Minecraft.getInstance();
      BlockHitResult var5 = var4.level.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, var4.player));
      if (var5.getType() != Type.MISS && var1.distanceToSqr(var5.getLocation()) + 1.0E-7 < var1.distanceToSqr(var2)) {
         return false;
      }

      for (Entity var7 : var3) {
         if (var7 != var0 && var7.isPickable() && !var7.isSpectator() && wi.s(var1, var2, var7.getBoundingBox())) {
            return false;
         }
      }

      return true;
   }

   public static BlockHitResult D(BlockPos var0, double var1) {
      return !Y() ? null : t(var0, var1, Minecraft.getInstance().player.getLookAngle());
   }

   public static BlockHitResult t(BlockPos var0, double var1, Vec3 var3) {
      if (!Y()) {
         return null;
      }

      Minecraft var4 = Minecraft.getInstance();
      double var5 = Math.max(0.0, Math.min(var1, var4.player.blockInteractionRange()));
      Vec3 var7 = var4.player.getEyePosition();
      Vec3 var8 = var7.add(var3.normalize().scale(var5));
      BlockHitResult var9 = var4.level.clip(new ClipContext(var7, var8, Block.OUTLINE, Fluid.NONE, var4.player));
      if (var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0)) {
         for (Entity var11 : var4.level.getEntities(var4.player, new AABB(var7, var8).inflate(1.0))) {
            if (var11.isPickable() && !var11.isSpectator() && wi.s(var7, var9.getLocation(), var11.getBoundingBox())) {
               return null;
            }
         }

         return var9;
      } else {
         return null;
      }
   }

   public static Optional<Vec3> m(Entity var0, double var1, Vec3 var3, Vec3 var4) {
      if (Y() && var0 != null && !(M(var0) > var1 + 1.0E-7)) {
         Vec3 var5 = Minecraft.getInstance().player.getEyePosition();
         List var6 = A(var0, var5);
         return mk.L(var0.getBoundingBox(), var5, var3, var4, var5x -> {
            Optional var6x = wi.x(var5, var5x.subtract(var5), var0.getBoundingBox(), var1);
            return var6x.isPresent() && v(var0, var5, (Vec3)var6x.get(), var6);
         });
      } else {
         return Optional.empty();
      }
   }

   public static int o(String var0) {
      return switch (var0) {
         case "Aura" -> 4;
         case "AutoCart" -> 5;
         case "AutoCrystal" -> 6;
         case "Auto MLG" -> 7;
         case "Auto Pearl Catch" -> 8;
         case "Wind Charge Hop" -> 8;
         case "TriggerBot" -> 2;
         case "AutoClicker" -> 1;
         case "Scaffold" -> 1;
         case "Auto Drain" -> 1;
         case "Auto Area Miner" -> 1;
         case "Auto Elytra Pilot" -> 1;
         case "Anchor Hunter" -> 1;
         case "Auto Builder" -> 1;
         case "Rod Swap" -> 0;
         default -> 5;
      };
   }

   private static Vec3 X(ta.h var0) {
      if (var0.aim != null) {
         float[] var1 = var0.aim.get();
         return Vec3.directionFromRotation(var1[1], var1[0]);
      } else {
         return Minecraft.getInstance().player.getLookAngle();
      }
   }

   public static void j() {
      N();
      if (h()) {
         AutoCrystalModule var0 = AutoCrystalModule.S();
         if (H() && var0 != null && !p_.V()) {
            var0.g();
         }

         t1.T();
      }
   }

   private static boolean w(ta.h var0) {
      Object var1 = null;
      if ((var0.source == null || var0.source.isRuntimeActive()) && var0.valid.getAsBoolean()) {
         if (var0.cooldown && !K()) {
            var1 = "cooldown";
         } else {
            var1 = I(var0.target, var0.range, X(var0));
         }
      } else {
         var1 = "intent expired / item use";
      }

      if (var1 != null) {
         var0.rejected.accept((String)var1);
      }

      return var1 == null;
   }

   public static void K() {
      N();
      if (h() && (!p_.V() || !l.stream().noneMatch(var0x -> X(var0x.source)))) {
         if (p_.V()) {
            o.clear();
            T = null;
            l.removeIf(var0x -> !X(var0x.source));
            t1.M();
         }

         if (!H()) {
            o.clear();
            T = null;
            t1.Q();
         }

         Minecraft var0 = Minecraft.getInstance();
         o.sort(Comparator.<ta.h>comparingInt(var0x -> o(var0x.source.getName())).reversed());
         if (T != null) {
            int var1 = !l.stream().anyMatch(var0x -> var0x.source.isRuntimeActive() && var0x.valid.getAsBoolean() && o(var0x.source.getName()) > o(E(T)))
                  && w(T)
                  && CriticalsModule.e()
                  && (var0.player.tickCount == F || !var0.player.onGround())
                  && o.stream().noneMatch(var0x -> w(var0x) && o(E(var0x)) > o(E(T)))
               ? 1
               : 0;
            switch (l1.H(var0.player.tickCount - F, (boolean)var1, CriticalsModule.g())) {
               case x:
                  T.rejected.accept("critical delay cancelled");
                  T = null;
                  break;
               case v:
                  k(T);
                  T = null;
                  break;
               case G:
                  o.clear();
                  l.clear();
                  return;
            }
         }

         l.sort(Comparator.<ta.g>comparingInt(var0x -> o(var0x.source.getName())).reversed());

         for (ta.h var2 : List.copyOf(o)) {
            if (l.stream().anyMatch(var1x -> var1x.source.isRuntimeActive() && var1x.valid.getAsBoolean() && o(var1x.source.getName()) > o(E(var2)))) {
               var2.rejected.accept("interaction ownership");
            } else if (W(E(var2), var2.target) && Z.size() < var2.limit) {
               if (w(var2)) {
                  if (l(var2)) {
                     break;
                  }

                  k(var2);
               }
            } else {
               var2.rejected.accept("interaction ownership");
            }
         }

         o.clear();
         if (T == null && !H) {
            for (ta.g var8 : List.copyOf(l)) {
               if (S(var8.source) && var8.source.isRuntimeActive() && var8.valid.getAsBoolean() && (R == null || R.equals(var8.source.getName()))) {
                  R = var8.source.getName();
                  H = (boolean)1;
                  a = (boolean)1;

                  try {
                     var8.action.run();
                     break;
                  } finally {
                     a = (boolean)0;
                  }
               }
            }
         }

         l.clear();
      } else {
         B();
      }
   }

   private static String E(ta.h var0) {
      return var0.source == null ? "Manual" : var0.source.getName();
   }

   private static boolean l(ta.h var0) {
      if (var0.target instanceof LivingEntity && CriticalsModule.c()) {
         var0.rejected.accept("critical delay");
         T = var0;
         F = Minecraft.getInstance().player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private static void k(ta.h var0) {
      if (w(var0)) {
         if (Z.size() < var0.limit && l(E(var0), var0.target)) {
            Minecraft var1 = Minecraft.getInstance();
            int var2 = var0.aim != null ? 1 : 0;
            q = (boolean)1;

            try {
               if (var2 != 0) {
                  float[] var3 = var0.aim.get();
                  var1.player.connection.send(new Rot(var3[0], var3[1], var1.player.onGround(), var1.player.horizontalCollision));
               }

               var1.gameMode.attack(var1.player, var0.target);
               ks.X(var1.player, InteractionHand.MAIN_HAND);
               var0.completed.run();
            } finally {
               q = (boolean)0;
               if (var2 != 0) {
                  var1.player.connection.send(new Rot(var1.player.getYRot(), var1.player.getXRot(), var1.player.onGround(), var1.player.horizontalCollision));
               }
            }
         }
      }
   }

   private static boolean W(String var0, Entity var1) {
      return (R == null || R.equals(var0)) && !Z.contains(var1.getId());
   }

   private static boolean l(String var0, Entity var1) {
      if (!W(var0, var1)) {
         return false;
      }

      R = var0;
      Z.add(var1.getId());
      return true;
   }

   public static boolean R(Entity var0) {
      if (!q) {
         RodSwapModule.Q();
      }

      if (q) {
         return true;
      }

      if (!q(var0, Double.MAX_VALUE)) {
         return false;
      }

      N();
      if (T != null) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      ta.h var2 = new ta.h(
         null,
         var0,
         var1.player.entityInteractionRange(),
         true,
         () -> var1.options.keyAttack.isDown() && var1.hitResult instanceof EntityHitResult var2x && var2x.getEntity() == var0,
         1
      );
      return W("Manual", var0) && !l(var2) ? l("Manual", var0) : false;
   }

   public static boolean T() {
      return T != null;
   }

   public static boolean U() {
      return q;
   }

   private ta() {
   }

   private record g(Module source, Runnable action, BooleanSupplier valid) {
   }

   private record h(
      Module source,
      Entity target,
      double range,
      boolean cooldown,
      BooleanSupplier valid,
      int limit,
      Supplier<float[]> aim,
      Runnable completed,
      Consumer<String> rejected
   ) {
      h(Module var1, Entity var2, double var3, boolean var5, BooleanSupplier var6, int var7) {
         this(var1, var2, var3, var5, var6, var7, null, () -> {}, var0 -> {});
      }
   }
}
