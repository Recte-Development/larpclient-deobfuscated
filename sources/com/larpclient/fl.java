package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class fl implements AutoCloseable {
   public static final String k = "1491537245591572480";
   private static final long J = TimeUnit.SECONDS.toNanos(10L);
   private final Object t = new Object();
   private final List<String> A;
   private final fl.t O;
   private final Consumer<String> M;
   private final long p;
   private final long w = System.currentTimeMillis() / 1000L;
   private final ScheduledExecutorService H = Executors.newSingleThreadScheduledExecutor(
      var0 -> Thread.ofPlatform().daemon().name("Larp-Discord-Guard").unstarted(var0)
   );
   private volatile boolean K;
   private volatile boolean j;
   private volatile long W;
   private volatile fl.v b;
   private Thread o;

   public fl(boolean var1) {
      this(j0.m(System.getProperty("os.name", ""), System.getenv()), j0::a, var0 -> r4.V("Discord", var0), 15000L);
      this.d(var1);
   }

   fl(List<String> var1, fl.t var2, Consumer<String> var3, long var4) {
      this.A = List.copyOf(var1);
      this.O = var2;
      this.M = var3;
      this.p = var4;
   }

   public void d(boolean var1) {
      synchronized (this.t) {
         if (!this.j && this.K != var1) {
            this.K = var1;
            this.W++;
            com.larpclient.o.publish(var1 ? o.g.z : o.g.N, null);
            if (this.o == null && var1) {
               this.H.scheduleWithFixedDelay(this::M, 0L, 100L, TimeUnit.MILLISECONDS);
               this.o = Thread.ofPlatform().daemon().name("Larp-Discord-RPC").start(this::g);
            }

            this.t.notifyAll();
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.t) {
         this.j = (boolean)1;
         this.K = (boolean)0;
         this.W++;
         com.larpclient.o.publish(o.g.N, null);
         this.t.notifyAll();
         if (this.o == null) {
            this.H.shutdown();
         }
      }
   }

   private boolean r(fl.v var1) {
      return !this.j && this.K && var1.U == this.W;
   }

   private void M() {
      fl.v var1 = this.b;
      if (var1 != null && (!this.r(var1) || System.nanoTime() > var1.K)) {
         var1.close();
      }
   }

   private void g() {
      byte var1 = 0;

      try {
         while (!this.j) {
            synchronized (this.t) {
               while (true) {
                  if (!this.K && !this.j) {
                     this.t.wait();
                  } else {
                     if (this.j) {
                        return;
                     }
                     break;
                  }
               }
            }

            fl.v var34 = new fl.v(this.W);
            this.b = var34;
            String var3 = "desktop discovery";

            try {
               for (String var5 : this.A) {
                  if (!this.r(var34)) {
                     break;
                  }

                  try {
                     var34.O = this.O.q(var5);
                     break;
                  } catch (IOException var28) {
                  }
               }

               if (var34.O == null) {
                  throw new IOException("Discord desktop unavailable");
               }

               label462:
               if (this.r(var34)) {
                  var3 = "handshake";
                  var34.K = System.nanoTime() + J;
                  JsonObject var35 = new JsonObject();
                  var35.addProperty("v", 1);
                  var35.addProperty("client_id", "1491537245591572480");
                  k(var34, 0, var35);
                  JsonObject var37 = G(var34);
                  if ("DISPATCH".equals(w(var37, "cmd")) && "READY".equals(w(var37, "evt"))) {
                     xp var6 = xp.read(j(j(var37, "data"), "user"));
                     if (!this.r(var34)) {
                        continue;
                     }

                     String var7 = UUID.randomUUID().toString();
                     var3 = "activity acknowledgement";
                     k(var34, 1, P(this.w, var7));
                     JsonObject var8 = G(var34);
                     if ("SET_ACTIVITY".equals(w(var8, "cmd")) && var7.equals(w(var8, "nonce"))) {
                        if (!this.r(var34)) {
                           continue;
                        }

                        var34.K = Long.MAX_VALUE;
                        var1 = 0;
                        com.larpclient.o.publish(o.g.v, var6);
                        this.M
                           .accept("Rich Presence connected: Playing with larpclient.com" + (var6 == null ? "" : "; desktop account shown on the account card"));
                        var3 = "connected session";

                        while (true) {
                           if (!this.r(var34)) {
                              break label462;
                           }

                           G(var34);
                        }
                     }

                     throw new IOException("Discord activity acknowledgement missing");
                  }

                  throw new IOException("Discord handshake rejected");
               }
            } catch (IOException | RuntimeException var29) {
               if (this.r(var34) && var1 == 0) {
                  this.M.accept("Rich Presence unavailable at " + var3 + "; retrying every 15 seconds while enabled");
                  var1 = 1;
               }
            } finally {
               var34.close();
               this.b = null;
               com.larpclient.o.publish(this.K && !this.j ? o.g.z : o.g.N, null);
               if (!this.K && !this.j && var34.O != null) {
                  this.M.accept("Rich Presence disabled; local IPC released");
               }
            }

            synchronized (this.t) {
               if (this.r(var34)) {
                  this.t.wait(this.p);
               }
            }
         }
      } catch (InterruptedException var32) {
         Thread.currentThread().interrupt();
      } finally {
         fl.v var12 = this.b;
         if (var12 != null) {
            var12.close();
         }

         this.H.shutdown();
         this.M.accept("Rich Presence stopped; local IPC released");
      }
   }

   static JsonObject P(long var0, String var2) {
      JsonObject var3 = new JsonObject();
      var3.addProperty("type", 0);
      var3.addProperty("name", "larpclient.com");
      var3.addProperty("details", "Playing with larpclient.com");
      var3.addProperty("state", "Minecraft " + xo.D);
      JsonObject var4 = new JsonObject();
      var4.addProperty("start", var0);
      var3.add("timestamps", var4);
      JsonObject var5 = new JsonObject();
      var5.addProperty("label", "larpclient.com");
      var5.addProperty("url", "https://larpclient.com");
      JsonArray var6 = new JsonArray();
      var6.add(var5);
      var3.add("buttons", var6);
      JsonObject var7 = new JsonObject();
      var7.addProperty("pid", ProcessHandle.current().pid());
      var7.add("activity", var3);
      JsonObject var8 = new JsonObject();
      var8.addProperty("cmd", "SET_ACTIVITY");
      var8.addProperty("nonce", var2);
      var8.add("args", var7);
      return var8;
   }

   private static void k(fl.v var0, int var1, JsonObject var2) throws IOException {
      j0.n(var0.O, var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   private static JsonObject G(fl.v var0) throws IOException {
      while (true) {
         j0.u var1 = j0.F(var0.O);
         if (var1.opcode() == 3) {
            j0.n(var0.O, 4, var1.data());
         } else if (var1.opcode() != 4) {
            if (var1.opcode() != 1) {
               throw new IOException("Discord closed IPC");
            }

            JsonObject var2 = JsonParser.parseString(new String(var1.data(), StandardCharsets.UTF_8)).getAsJsonObject();
            if ("ERROR".equals(w(var2, "evt"))) {
               throw new IOException("Discord rejected RPC command");
            }

            return var2;
         }
      }
   }

   static JsonObject j(JsonObject var0, String var1) {
      JsonElement var2 = var0 == null ? null : var0.get(var1);
      return var2 != null && var2.isJsonObject() ? var2.getAsJsonObject() : null;
   }

   static String w(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isString() ? var2.getAsString() : "";
   }

   @FunctionalInterface
   interface t {
      ByteChannel q(String var1) throws IOException;
   }

   private static final class v implements AutoCloseable {
      final long U;
      volatile ByteChannel O;
      volatile long K = Long.MAX_VALUE;

      v(long var1) {
         this.U = var1;
      }

      @Override
      public void close() {
         ByteChannel var1 = this.O;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var3) {
            }
         }
      }
   }
}
