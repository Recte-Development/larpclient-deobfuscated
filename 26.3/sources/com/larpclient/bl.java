package com.larpclient;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class bl {
   public static final bl F = new bl();
   public final v7 E;
   private volatile boolean y;
   private volatile boolean W;
   private volatile boolean r;
   private volatile st.l q;
   private volatile long c;
   private CompletableFuture<JsonObject> Z;

   public bl() {
      this(new v7(v7.h(), PublicApiAccess::anonymous));
   }

   public bl(v7 var1) {
      this.E = var1;
   }

   public static void G(Builder var0, URI var1) {
      if ("https".equals(var1.getScheme())
         && "larpclient.com".equals(var1.getHost())
         && (var1.getPort() == -1 || var1.getPort() == 443)
         && var1.getUserInfo() == null
         && var1.getRawQuery() == null
         && var1.getRawFragment() == null
         && var1.getRawPath().matches("/api/updates/artifacts/[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
         var0.uri(var1).GET();
         PublicApiAccess.anonymous().e(var0, "GET", var1, new byte[0]);
      } else {
         throw new IllegalArgumentException("Update credentials require the official artifact endpoint");
      }
   }

   public st.l h() {
      return System.currentTimeMillis() - this.c < 300000L ? this.q : null;
   }

   public boolean H() {
      return this.r && !this.W;
   }

   public synchronized void S() {
      if (!this.y) {
         this.y = (boolean)1;
         this.S();
         this.c().exceptionally(var0 -> null);
      }
   }

   public synchronized CompletableFuture<JsonObject> S() {
      if (this.Z == null || this.Z.isDone() && System.currentTimeMillis() - this.c >= 60000L) {
         this.c = System.currentTimeMillis();
         CompletableFuture var1 = this.E.x("/api/status", false);
         this.Z = var1.thenApply(var1x -> {
            this.q = st.f(var1x);
            _q.c("Status", "Service status received");
            return (JsonObject)var1x;
         });
         this.Z.whenComplete((var2, var3) -> {
            if (var3 != null) {
               this.q = null;
            }

            if (this.Z.isCancelled()) {
               var1.cancel(true);
            }
         });
         return this.Z;
      } else {
         return this.Z;
      }
   }

   public CompletableFuture<JsonObject> c() {
      return this.e("GET", "/api/profiles", null, null);
   }

   public CompletableFuture<JsonObject> e(String var1, String var2, JsonObject var3, String var4) {
      if (this.W) {
         return CompletableFuture.failedFuture(new wr(403, "FORBIDDEN"));
      }

      CompletableFuture var5 = this.E.e(var1, var2, var3, true, var4);
      CompletableFuture var6 = var5.whenComplete((var3x, var4x) -> {
         if (var4x == null) {
            if (var2.equals("/api/profiles") && var1.equals("GET")) {
               try {
                  st.G(var3x, true);
                  this.r = (boolean)1;
               } catch (RuntimeException var6x) {
                  this.r = (boolean)0;
               }
            }
         } else if (i(var4x).A() == 403) {
            this.W = (boolean)1;
            this.r = (boolean)0;
         }
      });
      var6.whenComplete((var2x, var3x) -> {
         if (var6.isCancelled()) {
            var5.cancel(true);
         }
      });
      return var6;
   }

   public static wr i(Throwable var0) {
      while ((var0 instanceof CompletionException || var0 instanceof ExecutionException) && var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0 instanceof wr var1 ? var1 : v7.h(var0);
   }

   public String Y(Throwable var1) {
      wr var2 = i(var1);
      st.l var3 = this.h();
      return var3 == null || !var3.authIssue() || var2.A() != 401 && var2.A() != 503 && !var2.e().equals("NETWORK")
         ? var2.getMessage()
         : var2.getMessage() + " Larp authentication is currently reporting a service issue.";
   }
}
