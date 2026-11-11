package com.larpclient.network;

import com.google.gson.JsonObject;
import com.larpclient.core.PublicApiAccess;
import com.larpclient.util.misc.LarpLog;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class ServiceStatusClient {
   public static final ServiceStatusClient iNSTANCE = new ServiceStatusClient();
   public final PlatformApiClient api;
   private volatile boolean started;
   private volatile boolean forbidden;
   private volatile boolean profilesLoaded;
   private volatile ApiResponseValidator.ServiceStatus status;
   private volatile long statusFetchedAt;
   private CompletableFuture<JsonObject> statusFuture;

   public ServiceStatusClient() {
      this(new PlatformApiClient(PlatformApiClient.toApiException(), PublicApiAccess::anonymous));
   }

   public ServiceStatusClient(PlatformApiClient var1) {
      this.api = var1;
   }

   public static void signUpdateRequest(Builder var0, URI var1) {
      if ("https".equals(var1.getScheme())
         && "larpclient.com".equals(var1.getHost())
         && (var1.getPort() == -1 || var1.getPort() == 443)
         && var1.getUserInfo() == null
         && var1.getRawQuery() == null
         && var1.getRawFragment() == null
         && var1.getRawPath().matches("/api/updates/artifacts/[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
         var0.uri(var1).GET();
         PublicApiAccess.anonymous().applyAuth(var0, "GET", var1, new byte[0]);
      } else {
         throw new IllegalArgumentException("Update credentials require the official artifact endpoint");
      }
   }

   public ApiResponseValidator.ServiceStatus getCachedStatus() {
      return System.currentTimeMillis() - this.statusFetchedAt < 300000L ? this.status : null;
   }

   public boolean isProfileReady() {
      return this.profilesLoaded && !this.forbidden;
   }

   public synchronized void fetchStatus() {
      if (!this.started) {
         this.started = (boolean)1;
         this.fetchStatus();
         this.fetchProfiles().exceptionally(var0 -> null);
      }
   }

   public synchronized CompletableFuture<JsonObject> fetchStatus() {
      if (this.statusFuture == null || this.statusFuture.isDone() && System.currentTimeMillis() - this.statusFetchedAt >= 60000L) {
         this.statusFetchedAt = System.currentTimeMillis();
         CompletableFuture var1 = this.api.get("/api/status", false);
         this.statusFuture = var1.thenApply(var1x -> {
            this.status = ApiResponseValidator.readId(var1x);
            LarpLog.debug("Status", "Service status received");
            return (JsonObject)var1x;
         });
         this.statusFuture.whenComplete((var2, var3) -> {
            if (var3 != null) {
               this.status = null;
            }

            if (this.statusFuture.isCancelled()) {
               var1.cancel(true);
            }
         });
         return this.statusFuture;
      } else {
         return this.statusFuture;
      }
   }

   public CompletableFuture<JsonObject> fetchProfiles() {
      return this.request("GET", "/api/profiles", null, null);
   }

   public CompletableFuture<JsonObject> request(String var1, String var2, JsonObject var3, String var4) {
      if (this.forbidden) {
         return CompletableFuture.failedFuture(new ApiException(403, "FORBIDDEN"));
      }

      CompletableFuture var5 = this.api.request(var1, var2, var3, true, var4);
      CompletableFuture var6 = var5.whenComplete((var3x, var4x) -> {
         if (var4x == null) {
            if (var2.equals("/api/profiles") && var1.equals("GET")) {
               try {
                  ApiResponseValidator.validateItems(var3x, true);
                  this.profilesLoaded = (boolean)1;
               } catch (RuntimeException var6x) {
                  this.profilesLoaded = (boolean)0;
               }
            }
         } else if (unwrapError(var4x).getStatusCode() == 403) {
            this.forbidden = (boolean)1;
            this.profilesLoaded = (boolean)0;
         }
      });
      var6.whenComplete((var2x, var3x) -> {
         if (var6.isCancelled()) {
            var5.cancel(true);
         }
      });
      return var6;
   }

   public static ApiException unwrapError(Throwable var0) {
      while ((var0 instanceof CompletionException || var0 instanceof ExecutionException) && var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0 instanceof ApiException var1 ? var1 : PlatformApiClient.toApiException(var0);
   }

   public String describeError(Throwable var1) {
      ApiException var2 = unwrapError(var1);
      ApiResponseValidator.ServiceStatus var3 = this.getCachedStatus();
      return var3 == null || !var3.authIssue() || var2.getStatusCode() != 401 && var2.getStatusCode() != 503 && !var2.getErrorCode().equals("NETWORK")
         ? var2.getMessage()
         : var2.getMessage() + " Larp authentication is currently reporting a service issue.";
   }
}
