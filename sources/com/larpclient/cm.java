package com.larpclient;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class cm {
   public static final cm R = new cm();
   public final kg n;
   private volatile boolean v;
   private volatile boolean B;
   private volatile boolean k;
   private volatile md.g M;
   private volatile long P;
   private CompletableFuture<JsonObject> A;

   public cm() {
      this(new kg(kg.c(), PublicApiAccess::anonymous));
   }

   public cm(kg var1) {
      this.n = var1;
   }

   public static void a(Builder var0, URI var1) {
      if ("https".equals(var1.getScheme())
         && "larpclient.com".equals(var1.getHost())
         && (var1.getPort() == -1 || var1.getPort() == 443)
         && var1.getUserInfo() == null
         && var1.getRawQuery() == null
         && var1.getRawFragment() == null
         && var1.getRawPath().matches("/api/updates/artifacts/[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
         var0.uri(var1).GET();
         PublicApiAccess.anonymous().Y(var0, "GET", var1, new byte[0]);
      } else {
         throw new IllegalArgumentException("Update credentials require the official artifact endpoint");
      }
   }

   public md.g D() {
      return System.currentTimeMillis() - this.P < 300000L ? this.M : null;
   }

   public boolean R() {
      return this.k && !this.B;
   }

   public synchronized void m() {
      if (!this.v) {
         this.v = (boolean)1;
         this.M();
         this.C().exceptionally(var0 -> null);
      }
   }

   public synchronized CompletableFuture<JsonObject> M() {
      if (this.A == null || this.A.isDone() && System.currentTimeMillis() - this.P >= 60000L) {
         this.P = System.currentTimeMillis();
         CompletableFuture var1 = this.n.F("/api/status", false);
         this.A = var1.thenApply(var1x -> {
            this.M = md.D(var1x);
            mt.U("Status", "Service status received");
            return (JsonObject)var1x;
         });
         this.A.whenComplete((var2, var3) -> {
            if (var3 != null) {
               this.M = null;
            }

            if (this.A.isCancelled()) {
               var1.cancel(true);
            }
         });
         return this.A;
      } else {
         return this.A;
      }
   }

   public CompletableFuture<JsonObject> C() {
      return this.x("GET", "/api/profiles", null, null);
   }

   public CompletableFuture<JsonObject> x(String var1, String var2, JsonObject var3, String var4) {
      if (this.B) {
         return CompletableFuture.failedFuture(new pe(403, "FORBIDDEN"));
      }

      CompletableFuture var5 = this.n.b(var1, var2, var3, true, var4);
      CompletableFuture var6 = var5.whenComplete((var3x, var4x) -> {
         if (var4x == null) {
            if (var2.equals("/api/profiles") && var1.equals("GET")) {
               try {
                  md.B(var3x, true);
                  this.k = (boolean)1;
               } catch (RuntimeException var6x) {
                  this.k = (boolean)0;
               }
            }
         } else if (G(var4x).F() == 403) {
            this.B = (boolean)1;
            this.k = (boolean)0;
         }
      });
      var6.whenComplete((var2x, var3x) -> {
         if (var6.isCancelled()) {
            var5.cancel(true);
         }
      });
      return var6;
   }

   public static pe G(Throwable var0) {
      while ((var0 instanceof CompletionException || var0 instanceof ExecutionException) && var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0 instanceof pe var1 ? var1 : kg.f(var0);
   }

   public String Y(Throwable var1) {
      pe var2 = G(var1);
      md.g var3 = this.D();
      return var3 == null || !var3.authIssue() || var2.F() != 401 && var2.F() != 503 && !var2.l().equals("NETWORK")
         ? var2.getMessage()
         : var2.getMessage() + " Larp authentication is currently reporting a service issue.";
   }
}
