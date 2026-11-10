package com.larpclient;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class uq {
   public static final uq x = new uq();
   public final ww h;
   private volatile boolean M;
   private volatile boolean n;
   private volatile boolean C;
   private volatile ut.q A;
   private volatile long e;
   private CompletableFuture<JsonObject> U;

   public uq() {
      this(new ww(ww.m(), PublicApiAccess::anonymous));
   }

   public uq(ww var1) {
      this.h = var1;
   }

   public static void e(Builder var0, URI var1) {
      if ("https".equals(var1.getScheme())
         && "larpclient.com".equals(var1.getHost())
         && (var1.getPort() == -1 || var1.getPort() == 443)
         && var1.getUserInfo() == null
         && var1.getRawQuery() == null
         && var1.getRawFragment() == null
         && var1.getRawPath().matches("/api/updates/artifacts/[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
         var0.uri(var1).GET();
         PublicApiAccess.anonymous().Q(var0, "GET", var1, new byte[0]);
      } else {
         throw new IllegalArgumentException("Update credentials require the official artifact endpoint");
      }
   }

   public ut.q z() {
      return System.currentTimeMillis() - this.e < 300000L ? this.A : null;
   }

   public boolean A() {
      return this.C && !this.n;
   }

   public synchronized void M() {
      if (!this.M) {
         this.M = (boolean)1;
         this.l();
         this.A().exceptionally(var0 -> null);
      }
   }

   public synchronized CompletableFuture<JsonObject> l() {
      if (this.U == null || this.U.isDone() && System.currentTimeMillis() - this.e >= 60000L) {
         this.e = System.currentTimeMillis();
         CompletableFuture var1 = this.h.Y("/api/status", false);
         this.U = var1.thenApply(var1x -> {
            this.A = ut.b(var1x);
            com.larpclient.h.U("Status", "Service status received");
            return (JsonObject)var1x;
         });
         this.U.whenComplete((var2, var3) -> {
            if (var3 != null) {
               this.A = null;
            }

            if (this.U.isCancelled()) {
               var1.cancel(true);
            }
         });
         return this.U;
      } else {
         return this.U;
      }
   }

   public CompletableFuture<JsonObject> A() {
      return this.v("GET", "/api/profiles", null, null);
   }

   public CompletableFuture<JsonObject> v(String var1, String var2, JsonObject var3, String var4) {
      if (this.n) {
         return CompletableFuture.failedFuture(new bh(403, "FORBIDDEN"));
      }

      CompletableFuture var5 = this.h.D(var1, var2, var3, true, var4);
      CompletableFuture var6 = var5.whenComplete((var3x, var4x) -> {
         if (var4x == null) {
            if (var2.equals("/api/profiles") && var1.equals("GET")) {
               try {
                  ut.G(var3x, true);
                  this.C = (boolean)1;
               } catch (RuntimeException var6x) {
                  this.C = (boolean)0;
               }
            }
         } else if (q(var4x).i() == 403) {
            this.n = (boolean)1;
            this.C = (boolean)0;
         }
      });
      var6.whenComplete((var2x, var3x) -> {
         if (var6.isCancelled()) {
            var5.cancel(true);
         }
      });
      return var6;
   }

   public static bh q(Throwable var0) {
      while ((var0 instanceof CompletionException || var0 instanceof ExecutionException) && var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0 instanceof bh var1 ? var1 : ww.h(var0);
   }

   public String C(Throwable var1) {
      bh var2 = q(var1);
      ut.q var3 = this.z();
      return var3 == null || !var3.authIssue() || var2.i() != 401 && var2.i() != 503 && !var2.j().equals("NETWORK")
         ? var2.getMessage()
         : var2.getMessage() + " Larp authentication is currently reporting a service issue.";
   }
}
