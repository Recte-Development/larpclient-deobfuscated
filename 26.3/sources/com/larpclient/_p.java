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

public final class _p {
   private static final List<_p.f> n = new ArrayList<>();
   private static boolean x;
   private static boolean y;
   private static boolean Y;
   private static final List<_p.l> P = new ArrayList<>();
   private static _p.l p;
   private static int S;
   private static long N = Long.MIN_VALUE;
   private static final Set<Integer> t = new HashSet<>();
   private static String O;
   private static boolean E;

   public static void w(Module var0, Runnable var1, BooleanSupplier var2) {
      n.add(new _p.f(var0, var1, var2));
   }

   public static boolean Q(Module var0) {
      F();
      return D(var0) && (O == null || O.equals(var0.getName()));
   }

   public static boolean T(Module var0, Runnable var1, BooleanSupplier var2) {
      F();
      if (var0 != null && var0.isRuntimeActive() && g() && var2.getAsBoolean() && p == null && !x && t.isEmpty() && (O == null || O.equals(var0.getName()))) {
         O = var0.getName();
         x = (boolean)1;
         y = (boolean)1;

         try {
            var1.run();
         } finally {
            y = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean Y(Runnable var0) {
      F();
      if (B() && !ij.y() && p == null && !x && t.isEmpty() && O == null) {
         O = "Eating";
         x = (boolean)1;
         y = (boolean)1;

         try {
            var0.run();
         } finally {
            y = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean e(Module var0, Entity var1, double var2) {
      F();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && p == null && !x && L(var0.getName(), var1) && f(var1, var2)) {
         Minecraft var4 = Minecraft.getInstance();
         _p.l var5 = new _p.l(var0, var1, var2, false, () -> true, 1);
         O(var5);
         return qg.Y(var4.player);
      } else {
         return false;
      }
   }

   public static boolean d(Module var0, Entity var1, Runnable var2) {
      F();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && D(var0) && p == null && !x && B(var0.getName(), var1)) {
         E = (boolean)1;

         try {
            var2.run();
         } finally {
            E = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean i(Module var0, Entity var1, Runnable var2) {
      F();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && D(var0) && t.contains(var1.getId())) {
         E = (boolean)1;

         try {
            var2.run();
         } finally {
            E = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean I() {
      return E || y;
   }

   public static boolean y() {
      if (y) {
         return true;
      } else if (!g()) {
         return false;
      } else {
         F();
         if (AnchorTapModule.e() || AnchorMacroModule.C()) {
            return false;
         } else if (CrystalTapModule.X()) {
            return false;
         } else if (p == null && (O == null || O.equals("Manual"))) {
            x = (boolean)1;
            O = "Manual";
            Y = (boolean)1;
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean A() {
      F();
      return Y;
   }

   public static void e() {
      Y = (boolean)0;
   }

   public static boolean x() {
      return B() && Minecraft.getInstance().gui.screen() == null;
   }

   private static boolean B() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && var0.level != null && var0.gameMode != null && var0.player.isAlive() && !var0.player.isSpectator();
   }

   public static boolean g() {
      return x() && !ij.y();
   }

   public static boolean D(Module var0) {
      return x(var0) && (!ij.y() || t(var0));
   }

   public static boolean t(Module var0) {
      return var0 != null && var0.isRuntimeActive() && var0.allowsDetachedCamera();
   }

   public static boolean x(Module var0) {
      return B() && h(Minecraft.getInstance().gui.screen() != null, var0);
   }

   public static boolean h(boolean var0, Module var1) {
      return !var0 || var1 != null && var1.isRuntimeActive() && var1.allowsScreenInteractions();
   }

   public static boolean T() {
      return !P.isEmpty() || !n.isEmpty() || p != null;
   }

   public static void j() {
      P.clear();
      n.clear();
      F();
   }

   private static void F() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != null) {
         k(var0.getGameTime());
      }
   }

   private static void k(long var0) {
      if (N != var0) {
         N = var0;
         t.clear();
         O = null;
         x = (boolean)0;
         Y = (boolean)0;
      }
   }

   public static void X() {
      q1.r();
      vi.o();
      P.clear();
      n.clear();
      x = (boolean)0;
      Y = (boolean)0;
      y = (boolean)0;
      p = null;
      t.clear();
      O = null;
      N = Long.MIN_VALUE;
      E = (boolean)0;
   }

   public static void e(Module var0) {
      P.removeIf(var1 -> var1.source == var0);
      n.removeIf(var1 -> var1.source == var0);
      if (p != null && p.source == var0) {
         p = null;
      }
   }

   public static void y(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5) {
      k(var0, var1, var2, var4, var5, 1);
   }

   public static void k(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6) {
      P.add(new _p.l(var0, var1, var2, var4, var5, var6));
   }

   public static void O(Module var0, Entity var1, double var2, BooleanSupplier var4, Supplier<float[]> var5, Runnable var6) {
      P.add(new _p.l(var0, var1, var2, false, var4, 1, var5, var6, var0x -> {}));
   }

   public static double y(Entity var0) {
      return jz.j(Minecraft.getInstance().player.getEyePosition(), var0.getBoundingBox());
   }

   public static boolean f(Entity var0, double var1) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      return var3 != null && b(var0, var1, var3.getLookAngle());
   }

   public static void U(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6, Runnable var7, Consumer<String> var8) {
      P.add(new _p.l(var0, var1, var2, var4, var5, var6, null, var7, var8));
   }

   public static boolean P() {
      return Minecraft.getInstance().player != null && Minecraft.getInstance().player.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   public static double M(double var0) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 == null ? 0.0 : Math.max(0.0, Math.min(var0, var2.entityInteractionRange()));
   }

   public static boolean b(Entity var0, double var1, Vec3 var3) {
      return e(var0, var1, var3) == null;
   }

   public static String e(Entity var0, double var1, Vec3 var3) {
      if (!g() || var0 == null) {
         return "interaction ownership";
      }

      if (RodSwapModule.m()) {
         return "temporary rod selection";
      }

      Minecraft var4 = Minecraft.getInstance();
      if (var0 != var4.player && var0.level() == var4.level && var0.isAlive() && !var0.isRemoved() && !var0.isSpectator() && var0.isPickable()) {
         double var5 = M(var1);
         Vec3 var7 = var4.player.getEyePosition();
         if (y(var0) > var5 + 1.0E-7) {
            return "range";
         } else {
            Optional var8 = jz.D(var7, var3, var0.getBoundingBox().inflate(var0.getPickRadius()), var5);
            if (var8.isEmpty()) {
               return "alignment";
            } else {
               return B(var0, var7, (Vec3)var8.get(), M(var0, var7)) ? null : "obstruction";
            }
         }
      } else {
         return "target invalid";
      }
   }

   private static List<Entity> M(Entity var0, Vec3 var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2.level.getEntities(var2.player, var0.getBoundingBox().minmax(new AABB(var1, var1)).inflate(1.0));
   }

   private static boolean B(Entity var0, Vec3 var1, Vec3 var2, List<Entity> var3) {
      Minecraft var4 = Minecraft.getInstance();
      BlockHitResult var5 = var4.level.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, var4.player));
      if (var5.getType() != Type.MISS && var1.distanceToSqr(var5.getLocation()) + 1.0E-7 < var1.distanceToSqr(var2)) {
         return false;
      }

      for (Entity var7 : var3) {
         if (var7 != var0 && var7.isPickable() && !var7.isSpectator() && jz.P(var1, var2, var7.getBoundingBox())) {
            return false;
         }
      }

      return true;
   }

   public static BlockHitResult A(BlockPos var0, double var1) {
      return !g() ? null : X(var0, var1, Minecraft.getInstance().player.getLookAngle());
   }

   public static BlockHitResult X(BlockPos var0, double var1, Vec3 var3) {
      if (!g()) {
         return null;
      }

      Minecraft var4 = Minecraft.getInstance();
      double var5 = Math.max(0.0, Math.min(var1, var4.player.blockInteractionRange()));
      Vec3 var7 = var4.player.getEyePosition();
      Vec3 var8 = var7.add(var3.normalize().scale(var5));
      BlockHitResult var9 = var4.level.clip(new ClipContext(var7, var8, Block.OUTLINE, Fluid.NONE, var4.player));
      if (var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0)) {
         for (Entity var11 : var4.level.getEntities(var4.player, new AABB(var7, var8).inflate(1.0))) {
            if (var11.isPickable() && !var11.isSpectator() && jz.P(var7, var9.getLocation(), var11.getBoundingBox())) {
               return null;
            }
         }

         return var9;
      } else {
         return null;
      }
   }

   public static Optional<Vec3> V(Entity var0, double var1, Vec3 var3, Vec3 var4) {
      if (g() && var0 != null && !(y(var0) > var1 + 1.0E-7)) {
         Vec3 var5 = Minecraft.getInstance().player.getEyePosition();
         List var6 = M(var0, var5);
         return ow.B(var0.getBoundingBox(), var5, var3, var4, var5x -> {
            Optional var6x = jz.D(var5, var5x.subtract(var5), var0.getBoundingBox(), var1);
            return var6x.isPresent() && B(var0, var5, (Vec3)var6x.get(), var6);
         });
      } else {
         return Optional.empty();
      }
   }

   public static int x(String var0) {
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

   private static Vec3 H(_p.l var0) {
      if (var0.aim != null) {
         float[] var1 = var0.aim.get();
         return Vec3.directionFromRotation(var1[1], var1[0]);
      } else {
         return Minecraft.getInstance().player.getLookAngle();
      }
   }

   public static void h() {
      F();
      if (B()) {
         AutoCrystalModule var0 = AutoCrystalModule.d();
         if (x() && var0 != null && !ij.y()) {
            var0.z();
         }

         vi.T();
      }
   }

   private static boolean B(_p.l var0) {
      Object var1 = null;
      if ((var0.source == null || var0.source.isRuntimeActive()) && var0.valid.getAsBoolean()) {
         if (var0.cooldown && !P()) {
            var1 = "cooldown";
         } else {
            var1 = e(var0.target, var0.range, H(var0));
         }
      } else {
         var1 = "intent expired / item use";
      }

      if (var1 != null) {
         var0.rejected.accept((String)var1);
      }

      return var1 == null;
   }

   public static void P() {
      F();
      if (B() && (!ij.y() || !n.stream().noneMatch(var0x -> t(var0x.source)))) {
         if (ij.y()) {
            P.clear();
            p = null;
            n.removeIf(var0x -> !t(var0x.source));
            vi.C();
         }

         if (!x()) {
            P.clear();
            p = null;
            vi.M();
         }

         Minecraft var0 = Minecraft.getInstance();
         P.sort(Comparator.<_p.l>comparingInt(var0x -> x(var0x.source.getName())).reversed());
         if (p != null) {
            int var1 = !n.stream().anyMatch(var0x -> var0x.source.isRuntimeActive() && var0x.valid.getAsBoolean() && x(var0x.source.getName()) > x(x(p)))
                  && B(p)
                  && CriticalsModule.B()
                  && (var0.player.tickCount == S || !var0.player.onGround())
                  && P.stream().noneMatch(var0x -> B(var0x) && x(x(var0x)) > x(x(p)))
               ? 1
               : 0;
            switch (h3.x(var0.player.tickCount - S, (boolean)var1, CriticalsModule.p())) {
               case x:
                  p.rejected.accept("critical delay cancelled");
                  p = null;
                  break;
               case F:
                  O(p);
                  p = null;
                  break;
               case w:
                  P.clear();
                  n.clear();
                  return;
            }
         }

         n.sort(Comparator.<_p.f>comparingInt(var0x -> x(var0x.source.getName())).reversed());

         for (_p.l var2 : List.copyOf(P)) {
            if (n.stream().anyMatch(var1x -> var1x.source.isRuntimeActive() && var1x.valid.getAsBoolean() && x(var1x.source.getName()) > x(x(var2)))) {
               var2.rejected.accept("interaction ownership");
            } else if (L(x(var2), var2.target) && t.size() < var2.limit) {
               if (B(var2)) {
                  if (n(var2)) {
                     break;
                  }

                  O(var2);
               }
            } else {
               var2.rejected.accept("interaction ownership");
            }
         }

         P.clear();
         if (p == null && !x) {
            for (_p.f var8 : List.copyOf(n)) {
               if (D(var8.source) && var8.source.isRuntimeActive() && var8.valid.getAsBoolean() && (O == null || O.equals(var8.source.getName()))) {
                  O = var8.source.getName();
                  x = (boolean)1;
                  y = (boolean)1;

                  try {
                     var8.action.run();
                     break;
                  } finally {
                     y = (boolean)0;
                  }
               }
            }
         }

         n.clear();
      } else {
         X();
      }
   }

   private static String x(_p.l var0) {
      return var0.source == null ? "Manual" : var0.source.getName();
   }

   private static boolean n(_p.l var0) {
      if (var0.target instanceof LivingEntity && CriticalsModule.Z()) {
         var0.rejected.accept("critical delay");
         p = var0;
         S = Minecraft.getInstance().player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private static void O(_p.l var0) {
      if (B(var0)) {
         if (t.size() < var0.limit && B(x(var0), var0.target)) {
            Minecraft var1 = Minecraft.getInstance();
            int var2 = var0.aim != null ? 1 : 0;
            E = (boolean)1;

            try {
               if (var2 != 0) {
                  float[] var3 = var0.aim.get();
                  var1.player.connection.send(new Rot(var3[0], var3[1], var1.player.onGround(), var1.player.horizontalCollision));
               }

               var1.gameMode.attack(var1.player, var0.target);
               h8.Q(var1.player, InteractionHand.MAIN_HAND);
               var0.completed.run();
            } finally {
               E = (boolean)0;
               if (var2 != 0) {
                  var1.player.connection.send(new Rot(var1.player.getYRot(), var1.player.getXRot(), var1.player.onGround(), var1.player.horizontalCollision));
               }
            }
         }
      }
   }

   private static boolean L(String var0, Entity var1) {
      return (O == null || O.equals(var0)) && !t.contains(var1.getId());
   }

   private static boolean B(String var0, Entity var1) {
      if (!L(var0, var1)) {
         return false;
      }

      O = var0;
      t.add(var1.getId());
      return true;
   }

   public static boolean a(Entity var0) {
      if (!E) {
         RodSwapModule.G();
      }

      if (E) {
         return true;
      }

      if (!f(var0, Double.MAX_VALUE)) {
         return false;
      }

      F();
      if (p != null) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      _p.l var2 = new _p.l(
         null,
         var0,
         var1.player.entityInteractionRange(),
         true,
         () -> var1.options.keyAttack.isDown() && var1.hitResult instanceof EntityHitResult var2x && var2x.getEntity() == var0,
         1
      );
      return L("Manual", var0) && !n(var2) ? B("Manual", var0) : false;
   }

   public static boolean k() {
      return p != null;
   }

   public static boolean v() {
      return E;
   }

   private _p() {
   }

   private record f(Module source, Runnable action, BooleanSupplier valid) {
   }

   private record l(
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
      l(Module var1, Entity var2, double var3, boolean var5, BooleanSupplier var6, int var7) {
         this(var1, var2, var3, var5, var6, var7, null, () -> {}, var0 -> {});
      }
   }
}
