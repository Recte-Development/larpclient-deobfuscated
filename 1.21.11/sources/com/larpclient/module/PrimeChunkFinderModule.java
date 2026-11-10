package com.larpclient.module;

import com.larpclient.l2;
import com.larpclient.q8;
import com.larpclient.sj;
import com.larpclient.wt;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_1923;
import net.minecraft.class_1944;
import net.minecraft.class_2231;
import net.minecraft.class_2246;
import net.minecraft.class_2269;
import net.minecraft.class_2312;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2377;
import net.minecraft.class_238;
import net.minecraft.class_2401;
import net.minecraft.class_2426;
import net.minecraft.class_243;
import net.minecraft.class_2457;
import net.minecraft.class_2459;
import net.minecraft.class_2561;
import net.minecraft.class_2665;
import net.minecraft.class_2680;
import net.minecraft.class_2804;
import net.minecraft.class_2818;
import net.minecraft.class_3568;
import net.minecraft.class_4076;
import net.minecraft.class_638;
import net.minecraft.class_6606;

public final class PrimeChunkFinderModule extends Module {
   private static PrimeChunkFinderModule J;
   private final lo v = this.addSetting(new lo("Scan Radius", "Loaded chunk radius", 8.0, 1.0, 12.0, 1.0));
   private final l3 e = this.addSetting(new l3("Tracers", "Draw lines to detected chunks", true));
   private final l3 f = this.addSetting(new l3("Labels", "Show the received activity signal", true));
   private final l3 E = this.addSetting(new l3("Notifications", "Notify once per discovered chunk", true));
   private final l3 X = this.addSetting(new l3("Chat Alerts", "Report discovery coordinates in local chat", true));
   private final l2 L = new l2(var1 -> this.addSetting(var1));
   private final sj N = new sj();
   private final LinkedHashMap<Long, String> P = new LinkedHashMap<>();
   private final Set<Long> D = new HashSet<>();
   private final LinkedHashMap<Long, Long> Z = new LinkedHashMap<>();
   private final ArrayDeque<class_1923> y = new ArrayDeque<>();
   private class_638 U;
   private long u;
   private long g = -20L;
   private final ArrayDeque<Long> l = new ArrayDeque<>();

   public PrimeChunkFinderModule() {
      super("Prime Chunk Finder", "Detect underground light and redstone activity", Category.RENDER, -1);
      J = this;
      this.v.a(var1 -> {
         if (this.U != null) {
            this.q(getMc().field_1687);
         }
      });
      this.X.O(this.E::getValue);
      this.E.a(var1 -> this.l.clear());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.U) {
            long var3 = var2.method_12004().method_8324();
            this.N.N(var3);
            this.P.remove(var3);
            this.D.remove(var3);
            this.l.remove(var3);
         }
      });
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.d());
   }

   private void q(class_638 var1) {
      this.N.t();
      this.P.clear();
      this.D.clear();
      this.Z.clear();
      this.y.clear();
      this.l.clear();
      this.U = var1;
      this.u = 0L;
      this.g = -20L;
      wt.T().w(this.getName());
      if (var1 != null && getMc().field_1724 != null) {
         class_1923 var2 = mc.field_1724.method_31476();
         int var3 = this.v.getValue().intValue();
         ArrayList var4 = new ArrayList();

         for (int var5 = -var3; var5 <= var3; var5++) {
            for (int var6 = -var3; var6 <= var3; var6++) {
               var4.add(new class_1923(var2.field_9181 + var5, var2.field_9180 + var6));
            }
         }

         var4.sort(
            Comparator.comparingLong(
               var1x -> (long)(var1x.field_9181 - var2.field_9181) * (var1x.field_9181 - var2.field_9181)
                  + (long)(var1x.field_9180 - var2.field_9180) * (var1x.field_9180 - var2.field_9180)
            )
         );
         this.y.addAll(var4);
      }
   }

   @Override
   public void onEnable() {
      this.q(getMc().field_1687);
      q8.G("BaseFinding", "Prime Chunk Finder active: received light/redstone changes below Y0, loaded radius {}, initial light is baseline", this.v.getValue());
   }

   @Override
   public void onDisable() {
      q8.v("BaseFinding", "Prime Chunk Finder cleanup: {} light histories, {} findings", this.N.f(), this.P.size());
      this.q((class_638)null);
   }

   private boolean E(int var1, int var2) {
      if (this.U != null && mc.field_1724 != null) {
         class_1923 var3 = mc.field_1724.method_31476();
         int var4 = this.v.getValue().intValue();
         return Math.abs((long)var1 - var3.field_9181) <= var4
            && Math.abs((long)var2 - var3.field_9180) <= var4
            && this.U.method_2935().method_21730(var1, var2) != null;
      } else {
         return false;
      }
   }

   public static void k(class_638 var0, int var1, int var2, class_6606 var3, boolean var4) {
      if (J != null && J.isEnabled()) {
         PrimeChunkFinderModule var5 = J;
         if (var5 != null && var5.isRuntimeActive() && var0 != null && var0 == getMc().field_1687 && mc.method_18854() && mc.field_1724 != null) {
            if (var5.U != var0) {
               var5.q(var0);
            }

            if (var5.E(var1, var2)) {
               try {
                  var5.N.m(var1, var2, var0.method_2935().method_12130().method_31929(), var3.method_38608(), var3.method_38609(), var3.method_38610(), var4);
               } catch (RuntimeException var7) {
                  q8.O("BaseFinding", "Prime light capture failed; later received packets can recover", var7);
               }
            }
         }
      }
   }

   public static boolean P(class_2680 var0) {
      return var0.method_26204() instanceof class_2457
         || var0.method_26204() instanceof class_2312
         || var0.method_26204() instanceof class_2665
         || var0.method_26204() instanceof class_2426
         || var0.method_26204() instanceof class_2401
         || var0.method_26204() instanceof class_2269
         || var0.method_26204() instanceof class_2231
         || var0.method_26204() instanceof class_2377
         || var0.method_26204() instanceof class_2315
         || var0.method_26204() instanceof class_2459
         || var0.method_27852(class_2246.field_10524)
         || var0.method_27852(class_2246.field_10589)
         || var0.method_27852(class_2246.field_10348);
   }

   public static void G(class_638 var0, class_2338 var1, class_2680 var2) {
      if (J != null && J.isEnabled()) {
         PrimeChunkFinderModule var3 = J;
         if (var3 != null
            && var3.isRuntimeActive()
            && var3.U == var0
            && var0 == getMc().field_1687
            && mc.method_18854()
            && var1.method_10264() < 0
            && var3.E(var1.method_10263() >> 4, var1.method_10260() >> 4)) {
            class_2680 var4 = var0.method_8320(var1);
            if (!var4.equals(var2)) {
               if (var4.method_26213() > var2.method_26213()) {
                  var3.Z.put(var1.method_10063(), var3.u + 100L);

                  while (var3.Z.size() > 256) {
                     var3.Z.remove(var3.Z.keySet().iterator().next());
                  }
               }

               if (P(var4) || P(var2)) {
                  var3.w(var1, "Redstone activity");
               }
            }
         }
      }
   }

   private boolean g(long var1) {
      return this.D.contains(var1) || mc.field_1724 != null && mc.field_1724.method_31476().method_8324() == var1;
   }

   private void w(class_2338 var1, String var2) {
      long var3 = class_1923.method_8331(var1.method_10263() >> 4, var1.method_10260() >> 4);
      if (!this.g(var3) && !this.P.containsKey(var3)) {
         this.P.put(var3, var2);

         while (this.P.size() > 256) {
            long var5 = this.P.keySet().iterator().next();
            this.P.remove(var5);
            this.l.remove(var5);
         }

         q8.v("BaseFinding", "Prime discovery: chunk [{}, {}], {}", var1.method_10263() >> 4, var1.method_10260() >> 4, var2);
         if (this.E.getValue()) {
            this.l.add(var3);
         }
      }
   }

   private class_2680 i(class_2338 var1) {
      class_2818 var2 = this.U.method_2935().method_21730(var1.method_10263() >> 4, var1.method_10260() >> 4);
      return var2 == null ? class_2246.field_10124.method_9564() : var2.method_8320(var1);
   }

   private boolean H(sj.l var1) {
      int var2 = Math.max(var1.before(), var1.after());
      class_2338 var3 = var1.pos();
      int var4 = Math.max(5, 15 - var2);

      for (int var5 = -var4; var5 <= var4; var5++) {
         for (int var6 = -var4 + Math.abs(var5); var6 <= var4 - Math.abs(var5); var6++) {
            int var7 = var4 - Math.abs(var5) - Math.abs(var6);

            for (int var8 = -var7; var8 <= var7; var8++) {
               int var9 = Math.abs(var5) + Math.abs(var6) + Math.abs(var8);
               class_2680 var10 = this.i(var3.method_10069(var5, var6, var8));
               if (var10.method_26213() >= var2 + var9) {
                  return true;
               }

               if (var2 <= 5
                  && var9 <= 5
                  && (
                     var10.method_27852(class_2246.field_27159)
                        || var10.method_27852(class_2246.field_27160)
                        || var10.method_27852(class_2246.field_27161)
                        || var10.method_27852(class_2246.field_27164)
                        || var10.method_27852(class_2246.field_27163)
                        || var10.method_27852(class_2246.field_27162)
                  )) {
                  return true;
               }

               if (var2 <= 9 && var9 <= 9 - var2 && (var10.method_27852(class_2246.field_10080) || var10.method_27852(class_2246.field_29030))) {
                  return true;
               }
            }
         }
      }

      for (Entry var12 : this.Z.entrySet()) {
         if (var3.method_19455(class_2338.method_10092((Long)var12.getKey())) <= 15 - var2) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.U != mc.field_1687) {
            this.q(mc.field_1687);
         }

         this.u++;
         class_1923 var1 = mc.field_1724.method_31476();
         double var28;
         int var10000 = (var28 = mc.field_1724.method_23318() - 0.0) == 0.0 ? 0 : (var28 < 0.0 ? -1 : 1);
         boolean var10 = false;
         int var9 = var10000;
         if ((Integer.signum(var9) >>> 31 ^ 1) == 0) {
            this.D.add(var1.method_8324());
            this.P.remove(var1.method_8324());
            this.l.remove(var1.method_8324());
         }

         long var29;
         var10000 = (var29 = this.u % 20L - 0L) == 0L ? 0 : (var29 < 0L ? -1 : 1);
         boolean var12 = false;
         int var11 = var10000;
         var10000 = Integer.signum(var11);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.N.B(var1x -> {
               class_1923 var3x = new class_1923(var1x);
               return this.E(var3x.field_9181, var3x.field_9180);
            });
            this.P.keySet().removeIf(var1x -> {
               class_1923 var2x = new class_1923(var1x);
               return !this.E(var2x.field_9181, var2x.field_9180);
            });
            this.D.removeIf(var1x -> {
               class_1923 var2x = new class_1923(var1x);
               return !this.E(var2x.field_9181, var2x.field_9180);
            });
            this.Z.values().removeIf(var1x -> var1x < this.u);
         }

         for (int var2 = 0; var2 < 8 && !this.y.isEmpty(); var2++) {
            class_1923 var3 = this.y.remove();
            if (this.E(var3.field_9181, var3.field_9180) && !this.N.c(var3.method_8324())) {
               class_3568 var4 = this.U.method_2935().method_12130();
               BitSet var5 = new BitSet();
               ArrayList var6 = new ArrayList();

               for (int var7 = Math.max(-4, this.U.method_32891()); var7 < 0; var7++) {
                  class_2804 var8 = var4.method_15562(class_1944.field_9282).method_15544(class_4076.method_18676(var3.field_9181, var7, var3.field_9180));
                  if (var8 != null) {
                     var5.set(var7 - var4.method_31929());
                     var6.add(var8.method_12137());
                  }
               }

               this.N.m(var3.field_9181, var3.field_9180, var4.method_31929(), var5, new BitSet(), var6, true);
            }
         }

         for (sj.l var19 : this.N.W(8)) {
            class_2338 var20 = var19.pos();
            long var22 = class_1923.method_8331(var20.method_10263() >> 4, var20.method_10260() >> 4);
            if (!this.g(var22) && !this.P.containsKey(var22) && this.E(var20.method_10263() >> 4, var20.method_10260() >> 4) && !this.H(var19)) {
               this.w(var20, "Underground light changed");
            }
         }

         if (this.u - this.g >= 20L && !this.l.isEmpty()) {
            long var18 = this.l.remove();
            String var21 = this.P.get(var18);
            if (var21 != null && this.E.getValue()) {
               class_1923 var23 = new class_1923(var18);
               int var10001 = var23.method_8326();
               byte var14 = 8;
               int var13 = var10001;
               var10001 = var13 + var14;
               int var10002 = var23.method_8328();
               byte var16 = 8;
               int var15 = var10002;
               String var24 = var21 + ": " + var10001 + ", " + (var15 + var16);
               wt.T().U(this.getName(), var24, wt.s.R);
               if (this.X.getValue()) {
                  mc.method_44714().method_44736(class_2561.method_43470("(Prime Chunk Finder) " + var24), false);
               }

               this.g = this.u;
            }
         }
      }
   }

   private void d() {
      if (this.isRuntimeActive() && this.isInGame() && this.U == mc.field_1687) {
         int var1 = 0;

         for (Entry var3 : this.P.entrySet()) {
            class_1923 var4 = new class_1923((Long)var3.getKey());
            if (this.E(var4.field_9181, var4.field_9180)) {
               class_243 var5 = new class_243(var4.method_8326() + 8, 63.0, var4.method_8328() + 8);
               int var6 = this.L.C(-10101600, var5.method_1022(mc.field_1724.method_73189()), (this.v.getValue() + 1.0) * 16.0, 1.0, var4.hashCode());
               this.L.X(new class_238(var4.method_8326(), 62.9, var4.method_8328(), var4.method_8326() + 16, 63.1, var4.method_8328() + 16), "Filled", var6);
               if (this.e.getValue()) {
                  this.L.O(mc.field_1773.method_19418().method_71156(), var5, var6);
               }

               if (this.f.getValue() && var1++ < 24) {
                  this.L.n(var5.method_1031(0.0, 1.0, 0.0), (String)var3.getValue(), var6);
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.P.size() + " activity chunks";
   }
}
