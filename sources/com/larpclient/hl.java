package com.larpclient;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpRequest.Builder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public final class hl {
   public static final hl E = new hl();
   public final re f;
   private volatile boolean Z;
   private volatile boolean H;
   private volatile boolean A;
   private volatile _4.f l;
   private volatile long Y;
   private CompletableFuture<JsonObject> S;

   public hl() {
      this(new re(re.n(), PublicApiAccess::anonymous));
   }

   public hl(re var1) {
      this.f = var1;
   }

   public static void D(Builder var0, URI var1) {
      if ("https".equals(var1.getScheme())
         && "larpclient.com".equals(var1.getHost())
         && (var1.getPort() == -1 || var1.getPort() == 443)
         && var1.getUserInfo() == null
         && var1.getRawQuery() == null
         && var1.getRawFragment() == null
         && var1.getRawPath().matches("/api/updates/artifacts/[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
         var0.uri(var1).GET();
         PublicApiAccess.anonymous().T(var0, "GET", var1, new byte[0]);
      } else {
         throw new IllegalArgumentException("Update credentials require the official artifact endpoint");
      }
   }

   public _4.f P() {
      return System.currentTimeMillis() - this.Y < 300000L ? this.l : null;
   }

   public boolean r() {
      return this.A && !this.H;
   }

   public synchronized void e() {
      if (!this.Z) {
         this.Z = (boolean)1;
         this.f();
         this.j().exceptionally(var0 -> null);
      }
   }

   public synchronized CompletableFuture<JsonObject> f() {
      if (this.S == null || this.S.isDone() && System.currentTimeMillis() - this.Y >= 60000L) {
         this.Y = System.currentTimeMillis();
         CompletableFuture var1 = this.f.X("/api/status", false);
         this.S = var1.thenApply(var1x -> {
            this.l = _4.h(var1x);
            r4.g("Status", "Service status received");
            return (JsonObject)var1x;
         });
         this.S.whenComplete((var2, var3) -> {
            if (var3 != null) {
               this.l = null;
            }

            if (this.S.isCancelled()) {
               var1.cancel(true);
            }
         });
         return this.S;
      } else {
         return this.S;
      }
   }

   public CompletableFuture<JsonObject> j() {
      return this.D("GET", "/api/profiles", null, null);
   }

   public CompletableFuture<JsonObject> D(String var1, String var2, JsonObject var3, String var4) {
      if (this.H) {
         return CompletableFuture.failedFuture(new oj(403, "FORBIDDEN"));
      }

      CompletableFuture var5 = this.f.M(var1, var2, var3, true, var4);
      CompletableFuture var6 = var5.whenComplete((var3x, var4x) -> {
         if (var4x == null) {
            if (var2.equals("/api/profiles") && var1.equals("GET")) {
               try {
                  _4.H(var3x, true);
                  this.A = (boolean)1;
               } catch (RuntimeException var6x) {
                  this.A = (boolean)0;
               }
            }
         } else if (D(var4x).M() == 403) {
            this.H = (boolean)1;
            this.A = (boolean)0;
         }
      });
      var6.whenComplete((var2x, var3x) -> {
         if (var6.isCancelled()) {
            var5.cancel(true);
         }
      });
      return var6;
   }

   public static oj D(Throwable var0) {
      while ((var0 instanceof CompletionException || var0 instanceof ExecutionException) && var0.getCause() != null) {
         var0 = var0.getCause();
      }

      return var0 instanceof oj var1 ? var1 : re.Q(var0);
   }

   public String V(Throwable var1) {
      oj var2 = D(var1);
      _4.f var3 = this.P();
      return var3 == null || !var3.authIssue() || var2.M() != 401 && var2.M() != 503 && !var2.f().equals("NETWORK")
         ? var2.getMessage()
         : var2.getMessage() + " Larp authentication is currently reporting a service issue.";
   }
}
