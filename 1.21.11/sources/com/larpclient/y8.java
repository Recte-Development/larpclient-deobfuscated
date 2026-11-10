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

public final class y8 implements AutoCloseable {
   public static final String W = "1491537245591572480";
   static final String x = "Larp Client";
   static final String M = "https://larpclient.com";
   static final String Y = "https://larpclient.com/larp-logo.png";
   static final String f = "In the menus";
   static final String D = "Singleplayer";
   static final String r = "Realms";
   static final String y = "LAN world";
   static final String w = "Multiplayer";
   private static final int c = 64;
   private static final long g = TimeUnit.SECONDS.toNanos(10L);
   private final Object j = new Object();
   private final List<String> u;
   private final y8.u E;
   private final Consumer<String> b;
   private final long H;
   private final long e;
   private final long i = System.currentTimeMillis() / 1000L;
   private final ScheduledExecutorService d = Executors.newSingleThreadScheduledExecutor(
      var0 -> Thread.ofPlatform().daemon().name("Larp-Discord-Guard").unstarted(var0)
   );
   private volatile boolean T;
   private volatile boolean I;
   private volatile long U;
   private volatile String n = "In the menus";
   private volatile y8.g l;
   private Thread X;

   public y8(boolean var1) {
      this(h4.n(System.getProperty("os.name", ""), System.getenv()), h4::P, var0 -> q8.G("Discord", var0), 15000L, 5000L);
      this.O(var1);
   }

   y8(List<String> var1, y8.u var2, Consumer<String> var3, long var4, long var6) {
      this.u = List.copyOf(var1);
      this.E = var2;
      this.b = var3;
      this.H = var4;
      this.e = TimeUnit.MILLISECONDS.toNanos(var6);
   }

   public void z(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.n = var1;
      }
   }

   public static String z(boolean var0, boolean var1, boolean var2, boolean var3, String var4) {
      if (!var0) {
         return "In the menus";
      } else if (var1) {
         return "Singleplayer";
      } else if (var2) {
         return "Realms";
      } else {
         return var3 ? "LAN world" : G(var4);
      }
   }

   static String G(String var0) {
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

   public void O(boolean var1) {
      synchronized (this.j) {
         if (!this.I && this.T != var1) {
            this.T = var1;
            this.U++;
            bh.publish(var1 ? bh.d.q : bh.d.k, null);
            if (this.X == null && var1) {
               this.d.scheduleWithFixedDelay(this::r, 0L, 100L, TimeUnit.MILLISECONDS);
               this.X = Thread.ofPlatform().daemon().name("Larp-Discord-RPC").start(this::Z);
            }

            this.j.notifyAll();
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.j) {
         this.I = (boolean)1;
         this.T = (boolean)0;
         this.U++;
         bh.publish(bh.d.k, null);
         this.j.notifyAll();
         if (this.X == null) {
            this.d.shutdown();
         }
      }
   }

   private boolean s(y8.g var1) {
      return !this.I && this.T && var1.P == this.U;
   }

   private void r() {
      y8.g var1 = this.l;
      if (var1 != null) {
         if (this.s(var1) && System.nanoTime() <= var1.o) {
            String var2 = this.n;
            if (var1.y && !var2.equals(var1.L) && System.nanoTime() - var1.q >= this.e) {
               try {
                  this.s(var1, var2);
                  this.b.accept("Rich Presence updated: " + var2);
               } catch (IOException | RuntimeException var4) {
                  var1.close();
               }
            }
         } else {
            var1.close();
         }
      }
   }

   private void s(y8.g var1, String var2) throws IOException {
      var1.L = var2;
      var1.q = System.nanoTime();
      F(var1, 1, i(this.i, UUID.randomUUID().toString(), var2));
   }

   private void Z() {
      byte var1 = 0;

      try {
         while (!this.I) {
            synchronized (this.j) {
               while (true) {
                  if (!this.T && !this.I) {
                     this.j.wait();
                  } else {
                     if (this.I) {
                        return;
                     }
                     break;
                  }
               }
            }

            y8.g var35 = new y8.g(this.U);
            this.l = var35;
            String var3 = "desktop discovery";

            try {
               for (String var5 : this.u) {
                  if (!this.s(var35)) {
                     break;
                  }

                  try {
                     var35.K = this.E.x(var5);
                     break;
                  } catch (IOException var29) {
                  }
               }

               if (var35.K == null) {
                  throw new IOException("Discord desktop unavailable");
               }

               label462:
               if (this.s(var35)) {
                  var3 = "handshake";
                  var35.o = System.nanoTime() + g;
                  JsonObject var36 = new JsonObject();
                  var36.addProperty("v", 1);
                  var36.addProperty("client_id", "1491537245591572480");
                  F(var35, 0, var36);
                  JsonObject var38 = j(var35);
                  if ("DISPATCH".equals(w(var38, "cmd")) && "READY".equals(w(var38, "evt"))) {
                     ui var6 = ui.read(j(j(var38, "data"), "user"));
                     if (!this.s(var35)) {
                        continue;
                     }

                     String var7 = UUID.randomUUID().toString();
                     String var8 = this.n;
                     var3 = "activity acknowledgement";
                     var35.L = var8;
                     var35.q = System.nanoTime();
                     F(var35, 1, i(this.i, var7, var8));
                     JsonObject var9 = j(var35);
                     if ("SET_ACTIVITY".equals(w(var9, "cmd")) && var7.equals(w(var9, "nonce"))) {
                        if (!this.s(var35)) {
                           continue;
                        }

                        var35.o = Long.MAX_VALUE;
                        var35.y = (boolean)1;
                        var1 = 0;
                        bh.publish(bh.d.f, var6);
                        this.b
                           .accept(
                              "Rich Presence connected: Playing Larp Client, " + var8 + (var6 == null ? "" : "; desktop account shown on the account card")
                           );
                        var3 = "connected session";

                        while (true) {
                           if (!this.s(var35)) {
                              break label462;
                           }

                           j(var35);
                        }
                     }

                     throw new IOException("Discord activity acknowledgement missing");
                  }

                  throw new IOException("Discord handshake rejected");
               }
            } catch (IOException | RuntimeException var30) {
               if (this.s(var35) && var1 == 0) {
                  this.b.accept("Rich Presence unavailable at " + var3 + "; retrying every 15 seconds while enabled");
                  var1 = 1;
               }
            } finally {
               var35.close();
               this.l = null;
               bh.publish(this.T && !this.I ? bh.d.q : bh.d.k, null);
               if (!this.T && !this.I && var35.K != null) {
                  this.b.accept("Rich Presence disabled; local IPC released");
               }
            }

            synchronized (this.j) {
               if (this.s(var35)) {
                  this.j.wait(this.H);
               }
            }
         }
      } catch (InterruptedException var33) {
         Thread.currentThread().interrupt();
      } finally {
         y8.g var13 = this.l;
         if (var13 != null) {
            var13.close();
         }

         this.d.shutdown();
         this.b.accept("Rich Presence stopped; local IPC released");
      }
   }

   static JsonObject i(long var0, String var2, String var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("type", 0);
      var4.addProperty("name", "Larp Client");
      var4.addProperty("details", "Larping on " + sc.E);
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

   private static void F(y8.g var0, int var1, JsonObject var2) throws IOException {
      f(var0, var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   private static void f(y8.g var0, int var1, byte[] var2) throws IOException {
      synchronized (var0.J) {
         h4.I(var0.K, var1, var2);
      }
   }

   private static JsonObject j(y8.g var0) throws IOException {
      while (true) {
         h4.y var1 = h4.E(var0.K);
         if (var1.opcode() == 3) {
            f(var0, 4, var1.data());
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

   private static final class g implements AutoCloseable {
      final long P;
      final Object J = new Object();
      volatile ByteChannel K;
      volatile long o = Long.MAX_VALUE;
      volatile boolean y;
      volatile String L;
      volatile long q;

      g(long var1) {
         this.P = var1;
      }

      @Override
      public void close() {
         ByteChannel var1 = this.K;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var3) {
            }
         }
      }
   }

   @FunctionalInterface
   interface u {
      ByteChannel x(String var1) throws IOException;
   }
}
