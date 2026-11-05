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

public final class _m {
   private static final List<_m.m> g = new ArrayList<>();
   private static boolean a;
   private static boolean v;
   private static boolean s;
   private static final List<_m.b> k = new ArrayList<>();
   private static _m.b S;
   private static int X;
   private static long n = Long.MIN_VALUE;
   private static final Set<Integer> G = new HashSet<>();
   private static String O;
   private static boolean F;

   public static void F(Module var0, Runnable var1, BooleanSupplier var2) {
      g.add(new _m.m(var0, var1, var2));
   }

   public static boolean f(Module var0) {
      H();
      return n(var0) && (O == null || O.equals(var0.getName()));
   }

   public static boolean h(Module var0, Runnable var1, BooleanSupplier var2) {
      H();
      if (var0 != null && var0.isRuntimeActive() && z() && var2.getAsBoolean() && S == null && !a && G.isEmpty() && (O == null || O.equals(var0.getName()))) {
         O = var0.getName();
         a = (boolean)1;
         v = (boolean)1;

         try {
            var1.run();
         } finally {
            v = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean q(Module var0, Entity var1, double var2) {
      H();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && S == null && !a && q(var0.getName(), var1) && b(var1, var2)) {
         Minecraft var4 = Minecraft.getInstance();
         _m.b var5 = new _m.b(var0, var1, var2, false, () -> true, 1);
         q(var5);
         return w7.H(var4.player);
      } else {
         return false;
      }
   }

   public static boolean y(Module var0, Entity var1, Runnable var2) {
      H();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && n(var0) && S == null && !a && a(var0.getName(), var1)) {
         F = (boolean)1;

         try {
            var2.run();
         } finally {
            F = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean q(Module var0, Entity var1, Runnable var2) {
      H();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && n(var0) && G.contains(var1.getId())) {
         F = (boolean)1;

         try {
            var2.run();
         } finally {
            F = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean W() {
      return F || v;
   }

   public static boolean f() {
      if (!z()) {
         return false;
      } else {
         H();
         if (v) {
            return true;
         } else if (AnchorTapModule.J()) {
            return false;
         } else if (S == null && (O == null || O.equals("Manual"))) {
            a = (boolean)1;
            O = "Manual";
            s = (boolean)1;
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean A() {
      H();
      return s;
   }

   public static void o() {
      s = (boolean)0;
   }

   public static boolean k() {
      return U() && Minecraft.getInstance().gui.screen() == null;
   }

   private static boolean U() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && var0.level != null && var0.gameMode != null && var0.player.isAlive() && !var0.player.isSpectator();
   }

   public static boolean z() {
      return k() && !tq.f();
   }

   public static boolean n(Module var0) {
      return B(var0) && !tq.f();
   }

   public static boolean B(Module var0) {
      return U() && x(Minecraft.getInstance().gui.screen() != null, var0);
   }

   public static boolean x(boolean var0, Module var1) {
      return !var0 || var1 != null && var1.isRuntimeActive() && var1.allowsScreenInteractions();
   }

   public static boolean N() {
      return !k.isEmpty() || !g.isEmpty() || S != null;
   }

   public static void u() {
      k.clear();
      g.clear();
      H();
   }

   private static void H() {
      ClientLevel var0 = Minecraft.getInstance().level;
      if (var0 != null) {
         g(var0.getGameTime());
      }
   }

   private static void g(long var0) {
      if (n != var0) {
         n = var0;
         G.clear();
         O = null;
         a = (boolean)0;
         s = (boolean)0;
      }
   }

   public static void U() {
      pl.l();
      _d.d();
      k.clear();
      g.clear();
      a = (boolean)0;
      s = (boolean)0;
      v = (boolean)0;
      S = null;
      G.clear();
      O = null;
      n = Long.MIN_VALUE;
      F = (boolean)0;
   }

   public static void w(Module var0) {
      k.removeIf(var1 -> var1.source == var0);
      g.removeIf(var1 -> var1.source == var0);
      if (S != null && S.source == var0) {
         S = null;
      }
   }

   public static void B(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5) {
      X(var0, var1, var2, var4, var5, 1);
   }

   public static void X(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6) {
      k.add(new _m.b(var0, var1, var2, var4, var5, var6));
   }

   public static void g(Module var0, Entity var1, double var2, BooleanSupplier var4, Supplier<float[]> var5, Runnable var6) {
      k.add(new _m.b(var0, var1, var2, false, var4, 1, var5, var6, var0x -> {}));
   }

   public static double p(Entity var0) {
      return p1.X(Minecraft.getInstance().player.getEyePosition(), var0.getBoundingBox());
   }

   public static boolean b(Entity var0, double var1) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      return var3 != null && w(var0, var1, var3.getLookAngle());
   }

   public static void f(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6, Runnable var7, Consumer<String> var8) {
      k.add(new _m.b(var0, var1, var2, var4, var5, var6, null, var7, var8));
   }

   public static boolean d() {
      return Minecraft.getInstance().player != null && Minecraft.getInstance().player.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   public static double o(double var0) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 == null ? 0.0 : Math.max(0.0, Math.min(var0, var2.entityInteractionRange()));
   }

   public static boolean w(Entity var0, double var1, Vec3 var3) {
      return V(var0, var1, var3) == null;
   }

   public static String V(Entity var0, double var1, Vec3 var3) {
      if (!z() || var0 == null) {
         return "interaction ownership";
      }

      if (RodSwapModule.j()) {
         return "temporary rod selection";
      }

      Minecraft var4 = Minecraft.getInstance();
      if (var0 != var4.player && var0.level() == var4.level && var0.isAlive() && !var0.isRemoved() && !var0.isSpectator() && var0.isPickable()) {
         double var5 = o(var1);
         Vec3 var7 = var4.player.getEyePosition();
         if (p(var0) > var5 + 1.0E-7) {
            return "range";
         } else {
            Optional var8 = p1.S(var7, var3, var0.getBoundingBox(), var5);
            if (var8.isEmpty()) {
               return "alignment";
            } else {
               return v(var0, var7, (Vec3)var8.get(), I(var0, var7)) ? null : "obstruction";
            }
         }
      } else {
         return "target invalid";
      }
   }

   private static List<Entity> I(Entity var0, Vec3 var1) {
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
         if (var7 != var0 && var7.isPickable() && !var7.isSpectator() && p1.H(var1, var2, var7.getBoundingBox())) {
            return false;
         }
      }

      return true;
   }

   public static BlockHitResult F(BlockPos var0, double var1) {
      return !z() ? null : y(var0, var1, Minecraft.getInstance().player.getLookAngle());
   }

   public static BlockHitResult y(BlockPos var0, double var1, Vec3 var3) {
      if (!z()) {
         return null;
      }

      Minecraft var4 = Minecraft.getInstance();
      double var5 = Math.max(0.0, Math.min(var1, var4.player.blockInteractionRange()));
      Vec3 var7 = var4.player.getEyePosition();
      Vec3 var8 = var7.add(var3.normalize().scale(var5));
      BlockHitResult var9 = var4.level.clip(new ClipContext(var7, var8, Block.OUTLINE, Fluid.NONE, var4.player));
      if (var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0)) {
         for (Entity var11 : var4.level.getEntities(var4.player, new AABB(var7, var8).inflate(1.0))) {
            if (var11.isPickable() && !var11.isSpectator() && p1.H(var7, var9.getLocation(), var11.getBoundingBox())) {
               return null;
            }
         }

         return var9;
      } else {
         return null;
      }
   }

   public static Optional<Vec3> z(Entity var0, double var1, Vec3 var3, Vec3 var4) {
      if (z() && var0 != null && !(p(var0) > var1 + 1.0E-7)) {
         Vec3 var5 = Minecraft.getInstance().player.getEyePosition();
         List var6 = I(var0, var5);
         return xm.d(var0.getBoundingBox(), var5, var3, var4, var5x -> {
            Optional var6x = p1.S(var5, var5x.subtract(var5), var0.getBoundingBox(), var1);
            return var6x.isPresent() && v(var0, var5, (Vec3)var6x.get(), var6);
         });
      } else {
         return Optional.empty();
      }
   }

   public static int a(String var0) {
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

   private static Vec3 J(_m.b var0) {
      if (var0.aim != null) {
         float[] var1 = var0.aim.get();
         return Vec3.directionFromRotation(var1[1], var1[0]);
      } else {
         return Minecraft.getInstance().player.getLookAngle();
      }
   }

   public static void j() {
      H();
      if (z()) {
         AutoCrystalModule var0 = AutoCrystalModule.S();
         if (var0 != null) {
            var0.A();
         }

         _d.h();
      }
   }

   private static boolean v(_m.b var0) {
      Object var1 = null;
      if ((var0.source == null || var0.source.isRuntimeActive()) && var0.valid.getAsBoolean()) {
         if (var0.cooldown && !d()) {
            var1 = "cooldown";
         } else {
            var1 = V(var0.target, var0.range, J(var0));
         }
      } else {
         var1 = "intent expired / item use";
      }

      if (var1 != null) {
         var0.rejected.accept((String)var1);
      }

      return var1 == null;
   }

   public static void w() {
      H();
      if (U() && !tq.f()) {
         if (!k()) {
            k.clear();
            S = null;
            _d.d();
         }

         Minecraft var0 = Minecraft.getInstance();
         k.sort(Comparator.<_m.b>comparingInt(var0x -> a(var0x.source.getName())).reversed());
         if (S != null) {
            int var1 = !g.stream().anyMatch(var0x -> var0x.source.isRuntimeActive() && var0x.valid.getAsBoolean() && a(var0x.source.getName()) > a(A(S)))
                  && v(S)
                  && CriticalsModule.d()
                  && (var0.player.tickCount == X || !var0.player.onGround())
                  && k.stream().noneMatch(var0x -> v(var0x) && a(A(var0x)) > a(A(S)))
               ? 1
               : 0;
            switch (xx.z(var0.player.tickCount - X, (boolean)var1, CriticalsModule.o())) {
               case n:
                  S.rejected.accept("critical delay cancelled");
                  S = null;
                  break;
               case v:
                  q(S);
                  S = null;
                  break;
               case S:
                  k.clear();
                  g.clear();
                  return;
            }
         }

         g.sort(Comparator.<_m.m>comparingInt(var0x -> a(var0x.source.getName())).reversed());

         for (_m.b var2 : List.copyOf(k)) {
            if (g.stream().anyMatch(var1x -> var1x.source.isRuntimeActive() && var1x.valid.getAsBoolean() && a(var1x.source.getName()) > a(A(var2)))) {
               var2.rejected.accept("interaction ownership");
            } else if (q(A(var2), var2.target) && G.size() < var2.limit) {
               if (v(var2)) {
                  if (o(var2)) {
                     break;
                  }

                  q(var2);
               }
            } else {
               var2.rejected.accept("interaction ownership");
            }
         }

         k.clear();
         if (S == null && !a) {
            for (_m.m var8 : List.copyOf(g)) {
               if (n(var8.source) && var8.source.isRuntimeActive() && var8.valid.getAsBoolean() && (O == null || O.equals(var8.source.getName()))) {
                  O = var8.source.getName();
                  a = (boolean)1;
                  v = (boolean)1;

                  try {
                     var8.action.run();
                     break;
                  } finally {
                     v = (boolean)0;
                  }
               }
            }
         }

         g.clear();
      } else {
         U();
      }
   }

   private static String A(_m.b var0) {
      return var0.source == null ? "Manual" : var0.source.getName();
   }

   private static boolean o(_m.b var0) {
      if (var0.target instanceof LivingEntity && CriticalsModule.D()) {
         var0.rejected.accept("critical delay");
         S = var0;
         X = Minecraft.getInstance().player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private static void q(_m.b var0) {
      if (v(var0)) {
         if (G.size() < var0.limit && a(A(var0), var0.target)) {
            Minecraft var1 = Minecraft.getInstance();
            int var2 = var0.aim != null ? 1 : 0;
            F = (boolean)1;

            try {
               if (var2 != 0) {
                  float[] var3 = var0.aim.get();
                  var1.player.connection.send(new Rot(var3[0], var3[1], var1.player.onGround(), var1.player.horizontalCollision));
               }

               var1.gameMode.attack(var1.player, var0.target);
               _8.f(var1.player, InteractionHand.MAIN_HAND);
               var0.completed.run();
            } finally {
               F = (boolean)0;
               if (var2 != 0) {
                  var1.player.connection.send(new Rot(var1.player.getYRot(), var1.player.getXRot(), var1.player.onGround(), var1.player.horizontalCollision));
               }
            }
         }
      }
   }

   private static boolean q(String var0, Entity var1) {
      return (O == null || O.equals(var0)) && !G.contains(var1.getId());
   }

   private static boolean a(String var0, Entity var1) {
      if (!q(var0, var1)) {
         return false;
      }

      O = var0;
      G.add(var1.getId());
      return true;
   }

   public static boolean m(Entity var0) {
      if (!F) {
         RodSwapModule.v();
      }

      if (F) {
         return true;
      }

      if (!b(var0, Double.MAX_VALUE)) {
         return false;
      }

      H();
      if (S != null) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      _m.b var2 = new _m.b(
         null,
         var0,
         var1.player.entityInteractionRange(),
         true,
         () -> var1.options.keyAttack.isDown() && var1.hitResult instanceof EntityHitResult var2x && var2x.getEntity() == var0,
         1
      );
      return q("Manual", var0) && !o(var2) ? a("Manual", var0) : false;
   }

   public static boolean l() {
      return S != null;
   }

   public static boolean w() {
      return F;
   }

   private _m() {
   }

   private record b(
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
      b(Module var1, Entity var2, double var3, boolean var5, BooleanSupplier var6, int var7) {
         this(var1, var2, var3, var5, var6, var7, null, () -> {}, var0 -> {});
      }
   }

   private record m(Module source, Runnable action, BooleanSupplier valid) {
   }
}
