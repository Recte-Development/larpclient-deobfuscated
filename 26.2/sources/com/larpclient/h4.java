package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class h4 implements AutoCloseable {
   public static final String s = "1491537245591572480";
   static final String k = "Larp Client";
   static final String P = "https://larpclient.com";
   static final String O = "https://larpclient.com/larp-logo.png";
   static final String X = "In the menus";
   static final String A = "Singleplayer";
   static final String p = "Realms";
   static final String B = "LAN world";
   static final String m = "Multiplayer";
   private static final int S = 64;
   private static final long y = TimeUnit.SECONDS.toNanos(10L);
   private final Object I = new Object();
   private final List<String> V;
   private final h4.r t;
   private final Consumer<String> z;
   private final long G;
   private final long T;
   private final long q = System.currentTimeMillis() / 1000L;
   private final ScheduledExecutorService w = Executors.newSingleThreadScheduledExecutor(
      var0 -> Thread.ofPlatform().daemon().name("Larp-Discord-Guard").unstarted(var0)
   );
   private volatile boolean g;
   private volatile boolean x;
   private volatile long N;
   private volatile String F = "In the menus";
   private volatile h4.g b;
   private Thread C;

   public h4(boolean var1) {
      this(s7.W(System.getProperty("os.name", ""), System.getenv()), s7::E, var0 -> h.A("Discord", var0), 15000L, 5000L);
      this.x(var1);
   }

   h4(List<String> var1, h4.r var2, Consumer<String> var3, long var4, long var6) {
      this.V = List.copyOf(var1);
      this.t = var2;
      this.z = var3;
      this.G = var4;
      this.T = TimeUnit.MILLISECONDS.toNanos(var6);
   }

   public void J(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.F = var1;
      }
   }

   public static String p(boolean var0, boolean var1, boolean var2, boolean var3, String var4) {
      if (!var0) {
         return "In the menus";
      } else if (var1) {
         return "Singleplayer";
      } else if (var2) {
         return "Realms";
      } else {
         return var3 ? "LAN world" : W(var4);
      }
   }

   static String W(String var0) {
      if (var0 == null) {
         return "Multiplayer";
      }

      String var1 = var0.strip().toLowerCase(Locale.ROOT);
      if (!var1.startsWith("[") && var1.indexOf(58) == var1.lastIndexOf(58)) {
         int var2 = var1.indexOf(58);
         if (var2 >= 0) {
            var1 = var1.substring(0, var2);
         }

         while (var1.endsWith(".")) {
            var1 = var1.substring(0, var1.length() - 1);
         }

         return !var1.isEmpty() && var1.length() <= 64 && var1.matches("[a-z0-9_-]+(\\.[a-z0-9_-]+)*") && !var1.matches("[0-9.]+") && !var1.equals("localhost")
            ? var1
            : "Multiplayer";
      } else {
         return "Multiplayer";
      }
   }

   public void x(boolean var1) {
      synchronized (this.I) {
         if (!this.x && this.g != var1) {
            this.g = var1;
            this.N++;
            bq.publish(var1 ? bq.b.R : bq.b.v, null);
            if (this.C == null && var1) {
               this.w.scheduleWithFixedDelay(this::M, 0L, 100L, TimeUnit.MILLISECONDS);
               this.C = Thread.ofPlatform().daemon().name("Larp-Discord-RPC").start(this::i);
            }

            this.I.notifyAll();
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.I) {
         this.x = (boolean)1;
         this.g = (boolean)0;
         this.N++;
         bq.publish(bq.b.v, null);
         this.I.notifyAll();
         if (this.C == null) {
            this.w.shutdown();
         }
      }
   }

   private boolean N(h4.g var1) {
      return !this.x && this.g && var1.M == this.N;
   }

   private void M() {
      h4.g var1 = this.b;
      if (var1 != null) {
         if (this.N(var1) && System.nanoTime() <= var1.j) {
            String var2 = this.F;
            if (var1.U && !var2.equals(var1.K) && System.nanoTime() - var1.r >= this.T) {
               try {
                  this.g(var1, var2);
                  this.z.accept("Rich Presence updated: " + var2);
               } catch (IOException | RuntimeException var4) {
                  var1.close();
               }
            }
         } else {
            var1.close();
         }
      }
   }

   private void g(h4.g var1, String var2) throws IOException {
      var1.K = var2;
      var1.r = System.nanoTime();
      K(var1, 1, E(this.q, UUID.randomUUID().toString(), var2));
   }

   private void i() {
      byte var1 = 0;

      try {
         while (!this.x) {
            synchronized (this.I) {
               while (true) {
                  if (!this.g && !this.x) {
                     this.I.wait();
                  } else {
                     if (this.x) {
                        return;
                     }
                     break;
                  }
               }
            }

            h4.g var35 = new h4.g(this.N);
            this.b = var35;
            String var3 = "desktop discovery";

            try {
               for (String var5 : this.V) {
                  if (!this.N(var35)) {
                     break;
                  }

                  try {
                     var35.I = this.t.E(var5);
                     break;
                  } catch (IOException var29) {
                  }
               }

               if (var35.I == null) {
                  throw new IOException("Discord desktop unavailable");
               }

               label462:
               if (this.N(var35)) {
                  var3 = "handshake";
                  var35.j = System.nanoTime() + y;
                  JsonObject var36 = new JsonObject();
                  var36.addProperty("v", 1);
                  var36.addProperty("client_id", "1491537245591572480");
                  K(var35, 0, var36);
                  JsonObject var38 = H(var35);
                  if ("DISPATCH".equals(n(var38, "cmd")) && "READY".equals(n(var38, "evt"))) {
                     ui var6 = ui.read(q(q(var38, "data"), "user"));
                     if (!this.N(var35)) {
                        continue;
                     }

                     String var7 = UUID.randomUUID().toString();
                     String var8 = this.F;
                     var3 = "activity acknowledgement";
                     var35.K = var8;
                     var35.r = System.nanoTime();
                     K(var35, 1, E(this.q, var7, var8));
                     JsonObject var9 = H(var35);
                     if ("SET_ACTIVITY".equals(n(var9, "cmd")) && var7.equals(n(var9, "nonce"))) {
                        if (!this.N(var35)) {
                           continue;
                        }

                        var35.j = Long.MAX_VALUE;
                        var35.U = (boolean)1;
                        var1 = 0;
                        bq.publish(bq.b.P, var6);
                        this.z
                           .accept(
                              "Rich Presence connected: Playing Larp Client, " + var8 + (var6 == null ? "" : "; desktop account shown on the account card")
                           );
                        var3 = "connected session";

                        while (true) {
                           if (!this.N(var35)) {
                              break label462;
                           }

                           H(var35);
                        }
                     }

                     throw new IOException("Discord activity acknowledgement missing");
                  }

                  throw new IOException("Discord handshake rejected");
               }
            } catch (IOException | RuntimeException var30) {
               if (this.N(var35) && var1 == 0) {
                  this.z.accept("Rich Presence unavailable at " + var3 + "; retrying every 15 seconds while enabled");
                  var1 = 1;
               }
            } finally {
               var35.close();
               this.b = null;
               bq.publish(this.g && !this.x ? bq.b.R : bq.b.v, null);
               if (!this.g && !this.x && var35.I != null) {
                  this.z.accept("Rich Presence disabled; local IPC released");
               }
            }

            synchronized (this.I) {
               if (this.N(var35)) {
                  this.I.wait(this.G);
               }
            }
         }
      } catch (InterruptedException var33) {
         Thread.currentThread().interrupt();
      } finally {
         h4.g var13 = this.b;
         if (var13 != null) {
            var13.close();
         }

         this.w.shutdown();
         this.z.accept("Rich Presence stopped; local IPC released");
      }
   }

   static JsonObject E(long var0, String var2, String var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("type", 0);
      var4.addProperty("name", "Larp Client");
      var4.addProperty("details", "Larping on " + z0.n);
      var4.addProperty("state", var3);
      JsonObject var5 = new JsonObject();
      var5.addProperty("start", var0);
      var4.add("timestamps", var5);
      JsonObject var6 = new JsonObject();
      var6.addProperty("large_image", "https://larpclient.com/larp-logo.png");
      var6.addProperty("large_text", "Larp Client");
      var4.add("assets", var6);
      JsonObject var7 = new JsonObject();
      var7.addProperty("label", "Get Larp Client");
      var7.addProperty("url", "https://larpclient.com");
      JsonArray var8 = new JsonArray();
      var8.add(var7);
      var4.add("buttons", var8);
      JsonObject var9 = new JsonObject();
      var9.addProperty("pid", ProcessHandle.current().pid());
      var9.add("activity", var4);
      JsonObject var10 = new JsonObject();
      var10.addProperty("cmd", "SET_ACTIVITY");
      var10.addProperty("nonce", var2);
      var10.add("args", var9);
      return var10;
   }

   private static void K(h4.g var0, int var1, JsonObject var2) throws IOException {
      M(var0, var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   private static void M(h4.g var0, int var1, byte[] var2) throws IOException {
      synchronized (var0.o) {
         s7.m(var0.I, var1, var2);
      }
   }

   private static JsonObject H(h4.g var0) throws IOException {
      while (true) {
         s7.g var1 = s7.s(var0.I);
         if (var1.opcode() == 3) {
            M(var0, 4, var1.data());
         } else if (var1.opcode() != 4) {
            if (var1.opcode() != 1) {
               throw new IOException("Discord closed IPC");
            }

            JsonObject var2 = JsonParser.parseString(new String(var1.data(), StandardCharsets.UTF_8)).getAsJsonObject();
            if ("ERROR".equals(n(var2, "evt"))) {
               throw new IOException("Discord rejected RPC command");
            }

            return var2;
         }
      }
   }

   static JsonObject q(JsonObject var0, String var1) {
      JsonElement var2 = var0 == null ? null : var0.get(var1);
      return var2 != null && var2.isJsonObject() ? var2.getAsJsonObject() : null;
   }

   static String n(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isString() ? var2.getAsString() : "";
   }

   private static final class g implements AutoCloseable {
      final long M;
      final Object o = new Object();
      volatile ByteChannel I;
      volatile long j = Long.MAX_VALUE;
      volatile boolean U;
      volatile String K;
      volatile long r;

      g(long var1) {
         this.M = var1;
      }

      @Override
      public void close() {
         ByteChannel var1 = this.I;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var3) {
            }
         }
      }
   }

   @FunctionalInterface
   interface r {
      ByteChannel E(String var1) throws IOException;
   }
}
