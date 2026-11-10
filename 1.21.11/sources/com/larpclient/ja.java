package com.larpclient;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class ja {
   public static final ja n = new ja();
   public final ya q;
   private volatile boolean v;
   private volatile boolean L;
   private volatile boolean T;
   private volatile t4.u x;
   private volatile long W;
   private CompletableFuture<JsonObject> H;

   public ja() {
      this(new ya(ya.N(), PublicApiAccess::anonymous));
   }

   public ja(ya var1) {
      this.q = var1;
   }

   public static void V(Builder var0, URI var1) {
      if ("https".equals(var1.getScheme())
         && "larpclient.com".equals(var1.getHost())
         && (var1.getPort() == -1 || var1.getPort() == 443)
         && var1.getUserInfo() == null
         && var1.getRawQuery() == null
         && var1.getRawFragment() == null
         && var1.getRawPath().matches("/api/updates/artifacts/[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
         var0.uri(var1).GET();
         PublicApiAccess.anonymous().K(var0, "GET", var1, new byte[0]);
      } else {
         throw new IllegalArgumentException("Update credentials require the official artifact endpoint");
      }
   }

   public t4.u M() {
      return System.currentTimeMillis() - this.W < 300000L ? this.x : null;
   }

   public boolean C() {
      return this.T && !this.L;
   }

   public synchronized void x() {
      if (!this.v) {
         this.v = (boolean)1;
         this.s();
         this.v().exceptionally(var0 -> null);
      }
   }

   public synchronized CompletableFuture<JsonObject> s() {
      if (this.H == null || this.H.isDone() && System.currentTimeMillis() - this.W >= 60000L) {
         this.W = System.currentTimeMillis();
         CompletableFuture var1 = this.q.V("/api/status", false);
         this.H = var1.thenApply(var1x -> {
            this.x = t4.L(var1x);
            q8.v("Status", "Service status received");
            return (JsonObject)var1x;
         });
         this.H.whenComplete((var2, var3) -> {
            if (var3 != null) {
               this.x = null;
            }

            if (this.H.isCancelled()) {
               var1.cancel(true);
            }
         });
         return this.H;
      } else {
         return this.H;
      }
   }

   public CompletableFuture<JsonObject> v() {
      return this.u("GET", "/api/profiles", null, null);
   }

   public CompletableFuture<JsonObject> u(String var1, String var2, JsonObject var3, String var4) {
      if (this.L) {
         return CompletableFuture.failedFuture(new u6(403, "FORBIDDEN"));
      }

      CompletableFuture var5 = this.q.E(var1, var2, var3, true, var4);
      CompletableFuture var6 = var5.whenComplete((var3x, var4x) -> {
         if (var4x == null) {
            if (var2.equals("/api/profiles") && var1.equals("GET")) {
               try {
                  t4.J(var3x, true);
                  this.T = (boolean)1;
               } catch (RuntimeException var6x) {
                  this.T = (boolean)0;
               }
            }
         } else if (E(var4x).M() == 403) {
            this.L = (boolean)1;
            this.T = (boolean)0;
         }
      });
      var6.whenComplete((var2x, var3x) -> {
         if (var6.isCancelled()) {
            var5.cancel(true);
         }
      });
      return var6;
   }

   public static u6 E(Throwable var0) {
      while ((var0 instanceof CompletionException || var0 instanceof ExecutionException) && var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0 instanceof u6 var1 ? var1 : ya.B(var0);
   }

   public String v(Throwable var1) {
      u6 var2 = E(var1);
      t4.u var3 = this.M();
      return var3 == null || !var3.authIssue() || var2.M() != 401 && var2.M() != 503 && !var2.X().equals("NETWORK")
         ? var2.getMessage()
         : var2.getMessage() + " Larp authentication is currently reporting a service issue.";
   }
}
