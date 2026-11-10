package com.larpclient.account;

import com.larpclient.q8;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.UserApiService.UserProperties;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_4341;
import net.minecraft.class_5520;
import net.minecraft.class_6628;
import net.minecraft.class_7497;
import net.minecraft.class_7569;
import net.minecraft.class_7574;
import net.minecraft.class_7578;
import net.minecraft.class_7853;
import net.minecraft.class_8647;

final class ClientAccountState implements AutoCloseable {
   private final class_320 user;
   private final class_7497 services;
   private final ProfileResult profile;
   private final UserApiService api;
   private final UserProperties properties;
   private final class_5520 social;
   private final class_6628 telemetry;
   private final class_7853 keys;
   private final class_7574 reporting;
   private final class_4341 realms;
   private final class_7578 realmsData;
   private boolean installed;

   private ClientAccountState(
      class_310 var1, class_320 var2, class_7497 var3, ProfileResult var4, UserApiService var5, UserProperties var6, class_5520 var7, class_6628 var8
   ) {
      this.user = var2;
      this.services = var3;
      this.profile = var4;
      this.api = var5;
      this.properties = var6;
      this.social = var7;
      this.telemetry = var8;
      this.keys = class_7853.method_46532(var5, var2, var1.field_1697.toPath());
      this.reporting = class_7574.method_44599(class_7569.method_44586(), var5);
      this.realms = new class_4341(var2.method_1675(), var2.method_1676(), var1);
      this.realmsData = new class_7578(this.realms);
   }

   static ClientAccountState prepare(class_310 var0, class_320 var1) throws Exception {
      YggdrasilAuthenticationService var2 = new YggdrasilAuthenticationService(var0.method_1487());
      class_7497 var3 = class_7497.method_44143(var2, var0.field_1697);
      ProfileResult var4 = var3.comp_837().fetchProfile(var1.method_44717(), true);
      if (var4 == null) {
         throw new IOException("Minecraft profile is unavailable. Retry later.");
      }

      UserApiService var5 = var2.createUserApiService(var1.method_1674());
      UserProperties var6 = var5.fetchProperties();
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedException();
      }

      class_6628 var7 = null;

      try {
         class_5520 var8 = new class_5520(var0, var5);
         var7 = new class_6628(var0, var5, var1);
         return new ClientAccountState(var0, var1, var3, var4, var5, var6, var8, var7);
      } catch (Throwable var9) {
         if (var7 != null) {
            var7.close();
         }

         throw var9;
      }
   }

   static boolean outsideWorld(class_310 var0) {
      return var0.field_1687 == null
         && var0.field_1724 == null
         && var0.method_1562() == null
         && var0.method_1576() == null
         && var0.field_1761 == null
         && var0.method_1560() == null;
   }

   void install(class_310 var1) {
      if (var1.method_18854() && outsideWorld(var1)) {
         class_6628 var2 = var1.field_41331;
         var1.field_62106 = this.services;
         var1.field_45899 = CompletableFuture.completedFuture(this.profile);
         var1.field_26902 = this.api;
         var1.field_47680 = CompletableFuture.completedFuture(this.properties);
         var1.field_26842 = this.social;
         var1.field_41331 = this.telemetry;
         var1.field_39068 = this.keys;
         var1.field_39492 = this.reporting;
         var1.field_39718 = this.realmsData;
         class_4341.field_57916 = this.realms;
         class_8647.field_45183 = null;
         var1.field_1726 = this.user;
         this.installed = true;

         try {
            var2.close();
         } catch (RuntimeException var5) {
            q8.L("Accounts", "Previous account service cleanup failed ({}); new account remains active", var5.getClass().getSimpleName());
         }

         q8.G("Accounts", "Game account changed; client ready={}", true);

         try {
            var1.method_24288();
         } catch (RuntimeException var4) {
            q8.L("Accounts", "Window title refresh failed ({}); account switch completed", var4.getClass().getSimpleName());
         }

         q8.G("Accounts", "Account applied; profile, signing, privacy, friends and Realms refreshed");
      } else {
         throw new IllegalStateException("Account switching requires the main menu.");
      }
   }

   @Override
   public void close() {
      if (!this.installed) {
         this.telemetry.close();
      }
   }
}
