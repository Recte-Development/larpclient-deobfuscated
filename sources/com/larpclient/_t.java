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

public final class _t implements AutoCloseable {
   public static final String f = "1491537245591572480";
   private static final long y = TimeUnit.SECONDS.toNanos(10L);
   private final Object h = new Object();
   private final List<String> s;
   private final _t.d U;
   private final Consumer<String> u;
   private final long P;
   private final long q = System.currentTimeMillis() / 1000L;
   private final ScheduledExecutorService z = Executors.newSingleThreadScheduledExecutor(
      var0 -> Thread.ofPlatform().daemon().name("Larp-Discord-Guard").unstarted(var0)
   );
   private volatile boolean E;
   private volatile boolean S;
   private volatile long c;
   private volatile _t.s B;
   private Thread V;

   public _t(boolean var1) {
      this(c5.A(System.getProperty("os.name", ""), System.getenv()), c5::D, var0 -> mt.K("Discord", var0), 15000L);
      this.K(var1);
   }

   _t(List<String> var1, _t.d var2, Consumer<String> var3, long var4) {
      this.s = List.copyOf(var1);
      this.U = var2;
      this.u = var3;
      this.P = var4;
   }

   public void K(boolean var1) {
      synchronized (this.h) {
         if (!this.S && this.E != var1) {
            this.E = var1;
            this.c++;
            hm.publish(var1 ? hm.u.L : hm.u.f, null);
            if (this.V == null && var1) {
               this.z.scheduleWithFixedDelay(this::Z, 0L, 100L, TimeUnit.MILLISECONDS);
               this.V = Thread.ofPlatform().daemon().name("Larp-Discord-RPC").start(this::J);
            }

            this.h.notifyAll();
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.h) {
         this.S = (boolean)1;
         this.E = (boolean)0;
         this.c++;
         hm.publish(hm.u.f, null);
         this.h.notifyAll();
         if (this.V == null) {
            this.z.shutdown();
         }
      }
   }

   private boolean R(_t.s var1) {
      return !this.S && this.E && var1.T == this.c;
   }

   private void Z() {
      _t.s var1 = this.B;
      if (var1 != null && (!this.R(var1) || System.nanoTime() > var1.Q)) {
         var1.close();
      }
   }

   private void J() {
      byte var1 = 0;

      try {
         while (!this.S) {
            synchronized (this.h) {
               while (true) {
                  if (!this.E && !this.S) {
                     this.h.wait();
                  } else {
                     if (this.S) {
                        return;
                     }
                     break;
                  }
               }
            }

            _t.s var34 = new _t.s(this.c);
            this.B = var34;
            String var3 = "desktop discovery";

            try {
               for (String var5 : this.s) {
                  if (!this.R(var34)) {
                     break;
                  }

                  try {
                     var34.g = this.U.o(var5);
                     break;
                  } catch (IOException var28) {
                  }
               }

               if (var34.g == null) {
                  throw new IOException("Discord desktop unavailable");
               }

               label462:
               if (this.R(var34)) {
                  var3 = "handshake";
                  var34.Q = System.nanoTime() + y;
                  JsonObject var35 = new JsonObject();
                  var35.addProperty("v", 1);
                  var35.addProperty("client_id", "1491537245591572480");
                  o(var34, 0, var35);
                  JsonObject var37 = M(var34);
                  if ("DISPATCH".equals(k(var37, "cmd")) && "READY".equals(k(var37, "evt"))) {
                     c9 var6 = c9.read(G(G(var37, "data"), "user"));
                     if (!this.R(var34)) {
                        continue;
                     }

                     String var7 = UUID.randomUUID().toString();
                     var3 = "activity acknowledgement";
                     o(var34, 1, E(this.q, var7));
                     JsonObject var8 = M(var34);
                     if ("SET_ACTIVITY".equals(k(var8, "cmd")) && var7.equals(k(var8, "nonce"))) {
                        if (!this.R(var34)) {
                           continue;
                        }

                        var34.Q = Long.MAX_VALUE;
                        var1 = 0;
                        hm.publish(hm.u.D, var6);
                        this.u
                           .accept("Rich Presence connected: Playing with larpclient.com" + (var6 == null ? "" : "; desktop account shown on the account card"));
                        var3 = "connected session";

                        while (true) {
                           if (!this.R(var34)) {
                              break label462;
                           }

                           M(var34);
                        }
                     }

                     throw new IOException("Discord activity acknowledgement missing");
                  }

                  throw new IOException("Discord handshake rejected");
               }
            } catch (IOException | RuntimeException var29) {
               if (this.R(var34) && var1 == 0) {
                  this.u.accept("Rich Presence unavailable at " + var3 + "; retrying every 15 seconds while enabled");
                  var1 = 1;
               }
            } finally {
               var34.close();
               this.B = null;
               hm.publish(this.E && !this.S ? hm.u.L : hm.u.f, null);
               if (!this.E && !this.S && var34.g != null) {
                  this.u.accept("Rich Presence disabled; local IPC released");
               }
            }

            synchronized (this.h) {
               if (this.R(var34)) {
                  this.h.wait(this.P);
               }
            }
         }
      } catch (InterruptedException var32) {
         Thread.currentThread().interrupt();
      } finally {
         _t.s var12 = this.B;
         if (var12 != null) {
            var12.close();
         }

         this.z.shutdown();
         this.u.accept("Rich Presence stopped; local IPC released");
      }
   }

   static JsonObject E(long var0, String var2) {
      JsonObject var3 = new JsonObject();
      var3.addProperty("type", 0);
      var3.addProperty("name", "larpclient.com");
      var3.addProperty("details", "Playing with larpclient.com");
      var3.addProperty("state", "Minecraft " + zq.C);
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

   private static void o(_t.s var0, int var1, JsonObject var2) throws IOException {
      c5.P(var0.g, var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   private static JsonObject M(_t.s var0) throws IOException {
      while (true) {
         c5.w var1 = c5.F(var0.g);
         if (var1.opcode() == 3) {
            c5.P(var0.g, 4, var1.data());
         } else if (var1.opcode() != 4) {
            if (var1.opcode() != 1) {
               throw new IOException("Discord closed IPC");
            }

            JsonObject var2 = JsonParser.parseString(new String(var1.data(), StandardCharsets.UTF_8)).getAsJsonObject();
            if ("ERROR".equals(k(var2, "evt"))) {
               throw new IOException("Discord rejected RPC command");
            }

            return var2;
         }
      }
   }

   static JsonObject G(JsonObject var0, String var1) {
      JsonElement var2 = var0 == null ? null : var0.get(var1);
      return var2 != null && var2.isJsonObject() ? var2.getAsJsonObject() : null;
   }

   static String k(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isString() ? var2.getAsString() : "";
   }

   @FunctionalInterface
   interface d {
      ByteChannel o(String var1) throws IOException;
   }

   private static final class s implements AutoCloseable {
      final long T;
      volatile ByteChannel g;
      volatile long Q = Long.MAX_VALUE;

      s(long var1) {
         this.T = var1;
      }

      @Override
      public void close() {
         ByteChannel var1 = this.g;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var3) {
            }
         }
      }
   }
}
