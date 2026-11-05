package com.larpclient;

import com.larpclient.module.AnchorTapModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.CriticalsModule;
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

public final class d8 {
   private static final List<d8.e> F = new ArrayList<>();
   private static boolean o;
   private static boolean T;
   private static boolean r;
   private static final List<d8.k> K = new ArrayList<>();
   private static d8.k l;
   private static int P;
   private static long I = Long.MIN_VALUE;
   private static final Set<Integer> v = new HashSet<>();
   private static String G;
   private static boolean c;

   public static void A(Module var0, Runnable var1, BooleanSupplier var2) {
      F.add(new d8.e(var0, var1, var2));
   }

   public static boolean m(Module var0) {
      r();
      return r(var0) && (G == null || G.equals(var0.getName()));
   }

   public static boolean R(Module var0, Runnable var1, BooleanSupplier var2) {
      r();
      if (var0 != null && var0.isRuntimeActive() && a() && var2.getAsBoolean() && l == null && !o && v.isEmpty() && (G == null || G.equals(var0.getName()))) {
         G = var0.getName();
         o = (boolean)1;
         T = (boolean)1;

         try {
            var1.run();
         } finally {
            T = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean a(Module var0, Entity var1, double var2) {
      r();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && l == null && !o && z(var0.getName(), var1) && M(var1, var2)) {
         Minecraft var4 = Minecraft.getInstance();
         d8.k var5 = new d8.k(var0, var1, var2, false, () -> true, 1);
         V(var5);
         return j5.n(var4.player);
      } else {
         return false;
      }
   }

   public static boolean z(Module var0, Entity var1, Runnable var2) {
      r();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && r(var0) && l == null && !o && E(var0.getName(), var1)) {
         c = (boolean)1;

         try {
            var2.run();
         } finally {
            c = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean j(Module var0, Entity var1, Runnable var2) {
      r();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && r(var0) && v.contains(var1.getId())) {
         c = (boolean)1;

         try {
            var2.run();
         } finally {
            c = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean N() {
      return c || T;
   }

   public static boolean O() {
      if (!a()) {
         return false;
      } else {
         r();
         if (T) {
            return true;
         } else if (AnchorTapModule.b()) {
            return false;
         } else if (l == null && (G == null || G.equals("Manual"))) {
            o = (boolean)1;
            G = "Manual";
            r = (boolean)1;
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean p() {
      r();
      return r;
   }

   public static void f() {
      r = (boolean)0;
   }

   public static boolean T() {
      return x() && Minecraft.getInstance().gui.screen() == null;
   }

   private static boolean x() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && var0.level != null && var0.gameMode != null && var0.player.isAlive() && !var0.player.isSpectator();
   }

   public static boolean a() {
      return T() && !rr.o();
   }

   public static boolean r(Module var0) {
      return T(var0) && !rr.o();
   }

   public static boolean T(Module var0) {
      return x() && M(Minecraft.getInstance().gui.screen() != null, var0);
   }

   public static boolean M(boolean var0, Module var1) {
      return !var0 || var1 != null && var1.isRuntimeActive() && var1.allowsScreenInteractions();
   }

   public static boolean P() {
      return !K.isEmpty() || !F.isEmpty() || l != null;
   }

   public static void U() {
      K.clear();
      F.clear();
      r();
   }

   private static void r() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != null) {
         x(var0.getGameTime());
      }
   }

   private static void x(long var0) {
      if (I != var0) {
         I = var0;
         v.clear();
         G = null;
         o = (boolean)0;
         r = (boolean)0;
      }
   }

   public static void h() {
      xq.y();
      m.l();
      K.clear();
      F.clear();
      o = (boolean)0;
      r = (boolean)0;
      T = (boolean)0;
      l = null;
      v.clear();
      G = null;
      I = Long.MIN_VALUE;
      c = (boolean)0;
   }

   public static void K(Module var0) {
      K.removeIf(var1 -> var1.source == var0);
      F.removeIf(var1 -> var1.source == var0);
      if (l != null && l.source == var0) {
         l = null;
      }
   }

   public static void E(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5) {
      n(var0, var1, var2, var4, var5, 1);
   }

   public static void n(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6) {
      K.add(new d8.k(var0, var1, var2, var4, var5, var6));
   }

   public static void v(Module var0, Entity var1, double var2, BooleanSupplier var4, Supplier<float[]> var5, Runnable var6) {
      K.add(new d8.k(var0, var1, var2, false, var4, 1, var5, var6, var0x -> {}));
   }

   public static double L(Entity var0) {
      return d_.T(Minecraft.getInstance().player.getEyePosition(), var0.getBoundingBox());
   }

   public static boolean M(Entity var0, double var1) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      return var3 != null && e(var0, var1, var3.getLookAngle());
   }

   public static void n(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6, Runnable var7, Consumer<String> var8) {
      K.add(new d8.k(var0, var1, var2, var4, var5, var6, null, var7, var8));
   }

   public static boolean R() {
      return Minecraft.getInstance().player != null && Minecraft.getInstance().player.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   public static double T(double var0) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 == null ? 0.0 : Math.max(0.0, Math.min(var0, var2.entityInteractionRange()));
   }

   public static boolean e(Entity var0, double var1, Vec3 var3) {
      return R(var0, var1, var3) == null;
   }

   public static String R(Entity var0, double var1, Vec3 var3) {
      if (!a() || var0 == null) {
         return "interaction ownership";
      }

      if (RodSwapModule.k()) {
         return "temporary rod selection";
      }

      Minecraft var4 = Minecraft.getInstance();
      if (var0 != var4.player && var0.level() == var4.level && var0.isAlive() && !var0.isRemoved() && !var0.isSpectator() && var0.isPickable()) {
         double var5 = T(var1);
         Vec3 var7 = var4.player.getEyePosition();
         if (L(var0) > var5 + 1.0E-7) {
            return "range";
         } else {
            Optional var8 = d_.b(var7, var3, var0.getBoundingBox(), var5);
            if (var8.isEmpty()) {
               return "alignment";
            } else {
               return m(var0, var7, (Vec3)var8.get(), W(var0, var7)) ? null : "obstruction";
            }
         }
      } else {
         return "target invalid";
      }
   }

   private static List<Entity> W(Entity var0, Vec3 var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2.level.getEntities(var2.player, var0.getBoundingBox().minmax(new AABB(var1, var1)).inflate(1.0));
   }

   private static boolean m(Entity var0, Vec3 var1, Vec3 var2, List<Entity> var3) {
      Minecraft var4 = Minecraft.getInstance();
      BlockHitResult var5 = var4.level.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, var4.player));
      if (var5.getType() != Type.MISS && var1.distanceToSqr(var5.getLocation()) + 1.0E-7 < var1.distanceToSqr(var2)) {
         return false;
      }

      for (Entity var7 : var3) {
         if (var7 != var0 && var7.isPickable() && !var7.isSpectator() && d_.c(var1, var2, var7.getBoundingBox())) {
            return false;
         }
      }

      return true;
   }

   public static BlockHitResult F(BlockPos var0, double var1) {
      return !a() ? null : e(var0, var1, Minecraft.getInstance().player.getLookAngle());
   }

   public static BlockHitResult e(BlockPos var0, double var1, Vec3 var3) {
      if (!a()) {
         return null;
      }

      Minecraft var4 = Minecraft.getInstance();
      double var5 = Math.max(0.0, Math.min(var1, var4.player.blockInteractionRange()));
      Vec3 var7 = var4.player.getEyePosition();
      Vec3 var8 = var7.add(var3.normalize().scale(var5));
      BlockHitResult var9 = var4.level.clip(new ClipContext(var7, var8, Block.OUTLINE, Fluid.NONE, var4.player));
      if (var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0)) {
         for (Entity var11 : var4.level.getEntities(var4.player, new AABB(var7, var8).inflate(1.0))) {
            if (var11.isPickable() && !var11.isSpectator() && d_.c(var7, var9.getLocation(), var11.getBoundingBox())) {
               return null;
            }
         }

         return var9;
      } else {
         return null;
      }
   }

   public static Optional<Vec3> u(Entity var0, double var1, Vec3 var3, Vec3 var4) {
      if (a() && var0 != null && !(L(var0) > var1 + 1.0E-7)) {
         Vec3 var5 = Minecraft.getInstance().player.getEyePosition();
         List var6 = W(var0, var5);
         return e9.S(var0.getBoundingBox(), var5, var3, var4, var5x -> {
            Optional var6x = d_.b(var5, var5x.subtract(var5), var0.getBoundingBox(), var1);
            return var6x.isPresent() && m(var0, var5, (Vec3)var6x.get(), var6);
         });
      } else {
         return Optional.empty();
      }
   }

   public static int J(String var0) {
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
         case "Rod Swap" -> 0;
         default -> 5;
      };
   }

   private static Vec3 E(d8.k var0) {
      if (var0.aim != null) {
         float[] var1 = var0.aim.get();
         return Vec3.directionFromRotation(var1[1], var1[0]);
      } else {
         return Minecraft.getInstance().player.getLookAngle();
      }
   }

   public static void I() {
      r();
      if (a()) {
         AutoCrystalModule var0 = AutoCrystalModule.T();
         if (var0 != null) {
            var0.A();
         }

         m.W();
      }
   }

   private static boolean e(d8.k var0) {
      Object var1 = null;
      if ((var0.source == null || var0.source.isRuntimeActive()) && var0.valid.getAsBoolean()) {
         if (var0.cooldown && !R()) {
            var1 = "cooldown";
         } else {
            var1 = R(var0.target, var0.range, E(var0));
         }
      } else {
         var1 = "intent expired / item use";
      }

      if (var1 != null) {
         var0.rejected.accept((String)var1);
      }

      return var1 == null;
   }

   public static void d() {
      r();
      if (x() && !rr.o()) {
         if (!T()) {
            K.clear();
            l = null;
            m.l();
         }

         Minecraft var0 = Minecraft.getInstance();
         K.sort(Comparator.<d8.k>comparingInt(var0x -> J(var0x.source.getName())).reversed());
         if (l != null) {
            int var1 = !F.stream().anyMatch(var0x -> var0x.source.isRuntimeActive() && var0x.valid.getAsBoolean() && J(var0x.source.getName()) > J(t(l)))
                  && e(l)
                  && CriticalsModule.p()
                  && (var0.player.tickCount == P || !var0.player.onGround())
                  && K.stream().noneMatch(var0x -> e(var0x) && J(t(var0x)) > J(t(l)))
               ? 1
               : 0;
            switch (hc.B(var0.player.tickCount - P, (boolean)var1, CriticalsModule.A())) {
               case M:
                  l.rejected.accept("critical delay cancelled");
                  l = null;
                  break;
               case B:
                  V(l);
                  l = null;
                  break;
               case m:
                  K.clear();
                  F.clear();
                  return;
            }
         }

         F.sort(Comparator.<d8.e>comparingInt(var0x -> J(var0x.source.getName())).reversed());

         for (d8.k var2 : List.copyOf(K)) {
            if (F.stream().anyMatch(var1x -> var1x.source.isRuntimeActive() && var1x.valid.getAsBoolean() && J(var1x.source.getName()) > J(t(var2)))) {
               var2.rejected.accept("interaction ownership");
            } else if (z(t(var2), var2.target) && v.size() < var2.limit) {
               if (e(var2)) {
                  if (s(var2)) {
                     break;
                  }

                  V(var2);
               }
            } else {
               var2.rejected.accept("interaction ownership");
            }
         }

         K.clear();
         if (l == null && !o) {
            for (d8.e var8 : List.copyOf(F)) {
               if (r(var8.source) && var8.source.isRuntimeActive() && var8.valid.getAsBoolean() && (G == null || G.equals(var8.source.getName()))) {
                  G = var8.source.getName();
                  o = (boolean)1;
                  T = (boolean)1;

                  try {
                     var8.action.run();
                     break;
                  } finally {
                     T = (boolean)0;
                  }
               }
            }
         }

         F.clear();
      } else {
         h();
      }
   }

   private static String t(d8.k var0) {
      return var0.source == null ? "Manual" : var0.source.getName();
   }

   private static boolean s(d8.k var0) {
      if (var0.target instanceof LivingEntity && CriticalsModule.t()) {
         var0.rejected.accept("critical delay");
         l = var0;
         P = Minecraft.getInstance().player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private static void V(d8.k var0) {
      if (e(var0)) {
         if (v.size() < var0.limit && E(t(var0), var0.target)) {
            Minecraft var1 = Minecraft.getInstance();
            int var2 = var0.aim != null ? 1 : 0;
            c = (boolean)1;

            try {
               if (var2 != 0) {
                  float[] var3 = var0.aim.get();
                  var1.player.connection.send(new Rot(var3[0], var3[1], var1.player.onGround(), var1.player.horizontalCollision));
               }

               var1.gameMode.attack(var1.player, var0.target);
               hp.o(var1.player, InteractionHand.MAIN_HAND);
               var0.completed.run();
            } finally {
               c = (boolean)0;
               if (var2 != 0) {
                  var1.player.connection.send(new Rot(var1.player.getYRot(), var1.player.getXRot(), var1.player.onGround(), var1.player.horizontalCollision));
               }
            }
         }
      }
   }

   private static boolean z(String var0, Entity var1) {
      return (G == null || G.equals(var0)) && !v.contains(var1.getId());
   }

   private static boolean E(String var0, Entity var1) {
      if (!z(var0, var1)) {
         return false;
      }

      G = var0;
      v.add(var1.getId());
      return true;
   }

   public static boolean F(Entity var0) {
      if (!c) {
         RodSwapModule.x();
      }

      if (c) {
         return true;
      }

      if (!M(var0, Double.MAX_VALUE)) {
         return false;
      }

      r();
      if (l != null) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      d8.k var2 = new d8.k(
         null,
         var0,
         var1.player.entityInteractionRange(),
         true,
         () -> var1.options.keyAttack.isDown() && var1.hitResult instanceof EntityHitResult var2x && var2x.getEntity() == var0,
         1
      );
      return z("Manual", var0) && !s(var2) ? E("Manual", var0) : false;
   }

   public static boolean V() {
      return l != null;
   }

   public static boolean E() {
      return c;
   }

   private d8() {
   }

   private record e(Module source, Runnable action, BooleanSupplier valid) {
   }

   private record k(
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
      k(Module var1, Entity var2, double var3, boolean var5, BooleanSupplier var6, int var7) {
         this(var1, var2, var3, var5, var6, var7, null, () -> {}, var0 -> {});
      }
   }
}
