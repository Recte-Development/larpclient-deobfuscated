package com.larpclient.module;

import com.larpclient.q8;
import com.mojang.authlib.GameProfile;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2398;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_5134;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import net.minecraft.class_745;
import net.minecraft.class_746;
import net.minecraft.class_7924;

public final class FakePlayerModule extends Module {
   private static final UUID J = UUID.fromString("32d0a964-137a-2c03-7cc9-df6700000000");
   private static final String b = "LARPCLIENT_NPC";
   private static final int p = -2000000000;
   private static FakePlayerModule a;
   private class_745 H;
   private long u;
   private int C;

   public FakePlayerModule() {
      super("Fake Player", "Spawns a local fake player for combat and visual-effect testing", Category.PLAYER, -1);
      a = this;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E);
   }

   public static void a(class_1297 var0) {
      if (a != null && a.isEnabled()) {
         FakePlayerModule var1 = a;
         if (var1 != null && var1.isRuntimeActive() && var1.H != null && var0 == var1.H) {
            var1.d();
         }
      }
   }

   @Override
   public void onEnable() {
      class_310 var1 = getMc();
      class_746 var2 = var1.field_1724;
      class_638 var3 = var1.field_1687;
      class_745 var4 = new class_745(var3, new GameProfile(J, "LARPCLIENT_NPC"));
      var4.method_5838(-2000000000);
      var4.method_5808(var2.method_23317(), var2.method_23318(), var2.method_23321(), var2.method_36454(), var2.method_36455());
      var4.method_6122(class_1268.field_5808, new class_1799(class_1802.field_22022));
      var4.method_6122(class_1268.field_5810, new class_1799(class_1802.field_8288));
      var4.method_6033(20.0F);
      var4.method_6073(0.0F);
      var3.method_53875(var4);
      this.H = var4;
      this.C = 0;
      q8.v("FakePlayer", "Spawned at {} {} {}", var2.method_23317(), var2.method_23318(), var2.method_23321());
   }

   @Override
   public void onDisable() {
      if (this.H != null) {
         this.H.method_31472();
         q8.v("FakePlayer", "Removed");
      }

      this.H = null;
   }

   @Override
   public void onTick() {
      class_746 var1 = this.getPlayer();
      if (var1 != null && this.H != null) {
         this.H.method_36456(0.0F);
         this.H.method_36457(0.0F);
         this.H.method_5847(0.0F);
         this.H.method_5636(0.0F);
         this.H.method_74090(var1.method_6068());
         boolean var3 = false;
         int var2 = this.C;
         if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
            byte var5 = 1;
            int var4 = this.C;
            int var10000 = (this.C = var4 - var5) % 20;
            boolean var7 = false;
            int var6 = var10000;
            var10000 = Integer.signum(var6);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.H.method_6033(Math.min(this.H.method_6032() + 1.0F, 20.0F));
            }
         }
      }
   }

   private void d() {
      class_746 var1 = this.getPlayer();
      if (var1 != null && !(var1.method_5858(this.H) > 36.0)) {
         float var2 = this.I(var1);
         long var3 = System.currentTimeMillis();
         if (var3 - this.u >= 100L) {
            var1.method_5783(c(var1) ? class_3417.field_15016 : class_3417.field_14840, 1.0F, 1.0F);
            this.u = var3;
         }

         this.w(var2);
      }
   }

   private static boolean c(class_746 var0) {
      return var0.method_18798().field_1351 < -0.08 && !var0.method_24828() && !var0.method_5624();
   }

   private float I(class_746 var1) {
      float var2 = var1.method_7261(0.5F);
      float var3 = (float)(var1.method_45325(class_5134.field_23721) * (0.2F + var2 * var2 * 0.8F));
      if (c(var1)) {
         var3 *= 1.5F;
      }

      class_1799 var4 = var1.method_6047();
      int var5 = var1.method_73183()
         .method_30349()
         .method_30530(class_7924.field_41265)
         .method_46746(class_1893.field_9118)
         .map(var1x -> class_1890.method_8225(var1x, var4))
         .orElse(0);
      return var5 > 0 ? var3 + 0.5F + 0.5F * var5 : var3;
   }

   private void w(float var1) {
      float var2 = this.H.method_6067();
      if (var2 > 0.0F) {
         float var3 = Math.min(var2, var1);
         this.H.method_6073(var2 - var3);
         var1 -= var3;
      }

      if (var1 > 0.0F) {
         this.H.method_6033(Math.max(this.H.method_6032() - var1, 0.5F));
      }

      this.H.field_6235 = 10;
      this.H.field_6008 = 10;
      this.X(class_1294.field_5924, 100, 900);
      this.X(class_1294.field_5898, 20, 100);
      if (this.H.method_6032() <= 1.0F && this.H.method_6079().method_31574(class_1802.field_8288)) {
         this.m();
      }
   }

   private void X(class_6880<class_1291> var1, int var2, int var3) {
      class_1293 var4 = this.H.method_6112(var1);
      if (var4 == null || var4.method_5584() < var2) {
         this.H.method_6092(new class_1293(var1, var3, 1));
      }
   }

   private void m() {
      class_310 var1 = getMc();
      var1.field_1724.method_5783(class_3417.field_14931, 1.0F, 1.0F);
      var1.field_1713.method_3051(this.H, class_2398.field_11220, 30);
      this.H.method_6012();
      this.H.method_6033(4.0F);
      this.H.method_6073(12.0F);
      this.C = 1400;
      q8.v("FakePlayer", "Totem popped");
   }
}
