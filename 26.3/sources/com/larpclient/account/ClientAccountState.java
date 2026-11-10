package com.larpclient.account;

import com.larpclient._q;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.UserApiService.UserFlag;
import com.mojang.authlib.minecraft.UserApiService.UserProperties;
import com.mojang.authlib.services.FriendsService;
import com.mojang.authlib.services.MinecraftServicesDiscoveryService;
import com.mojang.authlib.services.ProfileResult;
import com.mojang.realmsclient.RealmsAvailability;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.client.gui.screens.social.RemoteFriendListUpdateHandler;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.client.multiplayer.chat.report.ReportingContext;
import net.minecraft.client.telemetry.ClientTelemetryManager;
import net.minecraft.server.Services;

final class ClientAccountState implements AutoCloseable {
   private final User user;
   private final Services services;
   private final ProfileResult profile;
   private final UserApiService api;
   private final UserProperties properties;
   private final RemoteFriendListUpdateHandler friends;
   private final PlayerSocialManager social;
   private final ClientTelemetryManager telemetry;
   private final ProfileKeyPairManager keys;
   private final ReportingContext reporting;
   private final RealmsClient realms;
   private final RealmsDataFetcher realmsData;
   private boolean installed;

   private ClientAccountState(
      Minecraft var1,
      User var2,
      Services var3,
      ProfileResult var4,
      UserApiService var5,
      UserProperties var6,
      RemoteFriendListUpdateHandler var7,
      PlayerSocialManager var8,
      ClientTelemetryManager var9
   ) {
      this.user = var2;
      this.services = var3;
      this.profile = var4;
      this.api = var5;
      this.properties = var6;
      this.friends = var7;
      this.social = var8;
      this.telemetry = var9;
      this.keys = ProfileKeyPairManager.create(var5, var2, var1.gameDirectory.toPath());
      this.reporting = ReportingContext.create(ReportEnvironment.local(), var5);
      this.realms = new RealmsClient(var2.getSessionId(), var2.getName(), var1);
      this.realmsData = new RealmsDataFetcher(this.realms);
   }

   static ClientAccountState prepare(Minecraft var0, User var1) throws Exception {
      MinecraftServicesDiscoveryService var2 = MinecraftServicesDiscoveryService.create(var0.getProxy());
      Services var3 = Services.create(var2, var0.gameDirectory);
      ProfileResult var4 = var3.sessionService().fetchProfile(var1.getProfileId(), true);
      if (var4 == null) {
         throw new IOException("Minecraft profile is unavailable. Retry later.");
      }

      UserApiService var5 = var2.createUserApiService(var1.getAccessToken());
      UserProperties var6 = var5.fetchProperties();
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedException();
      }

      FriendsService var7 = var2.createFriendsService(var1.getAccessToken());
      RemoteFriendListUpdateHandler var8 = new RemoteFriendListUpdateHandler(var7, var0);
      ClientTelemetryManager var9 = null;

      try {
         PlayerSocialManager var10 = new PlayerSocialManager(var0, var5, var7, var8);
         var9 = new ClientTelemetryManager(var0, var5, var1);
         return new ClientAccountState(var0, var1, var3, var4, var5, var6, var8, var10, var9);
      } catch (Throwable var11) {
         var8.close();
         if (var9 != null) {
            var9.close();
         }

         throw var11;
      }
   }

   static boolean outsideWorld(Minecraft var0) {
      return var0.level == null
         && var0.player == null
         && var0.getConnection() == null
         && var0.getSingleplayerServer() == null
         && var0.gameMode == null
         && var0.getCameraEntity() == null;
   }

   void install(Minecraft var1) {
      if (var1.isSameThread() && outsideWorld(var1)) {
         RemoteFriendListUpdateHandler var2 = var1.remoteFriendListUpdateHandler;
         ClientTelemetryManager var3 = var1.telemetryManager;
         var1.services = this.services;
         var1.profileFuture = CompletableFuture.completedFuture(this.profile);
         var1.userApiService = this.api;
         var1.userPropertiesFuture = CompletableFuture.completedFuture(this.properties);
         var1.playerSocialManager = this.social;
         var1.remoteFriendListUpdateHandler = this.friends;
         var1.telemetryManager = this.telemetry;
         var1.profileKeyPairManager = this.keys;
         var1.reportingContext = this.reporting;
         var1.realmsDataFetcher = this.realmsData;
         RealmsClient.realmsClientInstance = this.realms;
         RealmsAvailability.future = null;
         var1.user = this.user;
         this.installed = true;

         try {
            var2.close();
            var3.close();
         } catch (RuntimeException var7) {
            _q.R("Accounts", "Previous account service cleanup failed ({}); new account remains active", var7.getClass().getSimpleName());
         }

         try {
            this.social.setAllowFriendRequests(this.properties.flag(UserFlag.ACCEPT_FRIEND_INVITES));
            this.social.setFriendListEnabled(this.properties.flag(UserFlag.FRIENDS_ENABLED));
         } catch (RuntimeException var6) {
            _q.R("Accounts", "Friend-list startup failed ({}); reopen the social screen to retry", var6.getClass().getSimpleName());
         }

         _q.z("Accounts", "Game account changed; client ready={}", true);

         try {
            var1.updateTitle();
         } catch (RuntimeException var5) {
            _q.R("Accounts", "Window title refresh failed ({}); account switch completed", var5.getClass().getSimpleName());
         }

         _q.z("Accounts", "Account applied; profile, signing, privacy, friends and Realms refreshed");
      } else {
         throw new IllegalStateException("Account switching requires the main menu.");
      }
   }

   @Override
   public void close() {
      if (!this.installed) {
         this.friends.close();
         this.telemetry.close();
      }
   }
}
