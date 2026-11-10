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

public final class f2 implements AutoCloseable {
   public static final String S = "1491537245591572480";
   static final String P = "Larp Client";
   static final String w = "https://larpclient.com";
   static final String h = "https://larpclient.com/larp-logo.png";
   static final String d = "In the menus";
   static final String N = "Singleplayer";
   static final String u = "Realms";
   static final String v = "LAN world";
   static final String o = "Multiplayer";
   private static final int Q = 64;
   private static final long l = TimeUnit.SECONDS.toNanos(10L);
   private final Object D = new Object();
   private final List<String> f;
   private final f2.g A;
   private final Consumer<String> z;
   private final long j;
   private final long O;
   private final long e = System.currentTimeMillis() / 1000L;
   private final ScheduledExecutorService C = Executors.newSingleThreadScheduledExecutor(
      var0 -> Thread.ofPlatform().daemon().name("Larp-Discord-Guard").unstarted(var0)
   );
   private volatile boolean y;
   private volatile boolean T;
   private volatile long x;
   private volatile String X = "In the menus";
   private volatile f2.x i;
   private Thread H;

   public f2(boolean var1) {
      this(nh.H(System.getProperty("os.name", ""), System.getenv()), nh::J, var0 -> _q.z("Discord", var0), 15000L, 5000L);
      this.X(var1);
   }

   f2(List<String> var1, f2.g var2, Consumer<String> var3, long var4, long var6) {
      this.f = List.copyOf(var1);
      this.A = var2;
      this.z = var3;
      this.j = var4;
      this.O = TimeUnit.MILLISECONDS.toNanos(var6);
   }

   public void i(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.X = var1;
      }
   }

   public static String e(boolean var0, boolean var1, boolean var2, boolean var3, String var4) {
      if (!var0) {
         return "In the menus";
      } else if (var1) {
         return "Singleplayer";
      } else if (var2) {
         return "Realms";
      } else {
         return var3 ? "LAN world" : K(var4);
      }
   }

   static String K(String var0) {
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

   public void X(boolean var1) {
      synchronized (this.D) {
         if (!this.T && this.y != var1) {
            this.y = var1;
            this.x++;
            b3.publish(var1 ? b3.p.P : b3.p.T, null);
            if (this.H == null && var1) {
               this.C.scheduleWithFixedDelay(this::H, 0L, 100L, TimeUnit.MILLISECONDS);
               this.H = Thread.ofPlatform().daemon().name("Larp-Discord-RPC").start(this::L);
            }

            this.D.notifyAll();
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.D) {
         this.T = (boolean)1;
         this.y = (boolean)0;
         this.x++;
         b3.publish(b3.p.T, null);
         this.D.notifyAll();
         if (this.H == null) {
            this.C.shutdown();
         }
      }
   }

   private boolean Z(f2.x var1) {
      return !this.T && this.y && var1.I == this.x;
   }

   private void H() {
      f2.x var1 = this.i;
      if (var1 != null) {
         if (this.Z(var1) && System.nanoTime() <= var1.E) {
            String var2 = this.X;
            if (var1.N && !var2.equals(var1.k) && System.nanoTime() - var1.Y >= this.O) {
               try {
                  this.L(var1, var2);
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

   private void L(f2.x var1, String var2) throws IOException {
      var1.k = var2;
      var1.Y = System.nanoTime();
      a(var1, 1, M(this.e, UUID.randomUUID().toString(), var2));
   }

   private void L() {
      byte var1 = 0;

      try {
         while (!this.T) {
            synchronized (this.D) {
               while (true) {
                  if (!this.y && !this.T) {
                     this.D.wait();
                  } else {
                     if (this.T) {
                        return;
                     }
                     break;
                  }
               }
            }

            f2.x var35 = new f2.x(this.x);
            this.i = var35;
            String var3 = "desktop discovery";

            try {
               for (String var5 : this.f) {
                  if (!this.Z(var35)) {
                     break;
                  }

                  try {
                     var35.c = this.A.Q(var5);
                     break;
                  } catch (IOException var29) {
                  }
               }

               if (var35.c == null) {
                  throw new IOException("Discord desktop unavailable");
               }

               label462:
               if (this.Z(var35)) {
                  var3 = "handshake";
                  var35.E = System.nanoTime() + l;
                  JsonObject var36 = new JsonObject();
                  var36.addProperty("v", 1);
                  var36.addProperty("client_id", "1491537245591572480");
                  a(var35, 0, var36);
                  JsonObject var38 = t(var35);
                  if ("DISPATCH".equals(R(var38, "cmd")) && "READY".equals(R(var38, "evt"))) {
                     av var6 = av.read(i(i(var38, "data"), "user"));
                     if (!this.Z(var35)) {
                        continue;
                     }

                     String var7 = UUID.randomUUID().toString();
                     String var8 = this.X;
                     var3 = "activity acknowledgement";
                     var35.k = var8;
                     var35.Y = System.nanoTime();
                     a(var35, 1, M(this.e, var7, var8));
                     JsonObject var9 = t(var35);
                     if ("SET_ACTIVITY".equals(R(var9, "cmd")) && var7.equals(R(var9, "nonce"))) {
                        if (!this.Z(var35)) {
                           continue;
                        }

                        var35.E = Long.MAX_VALUE;
                        var35.N = (boolean)1;
                        var1 = 0;
                        b3.publish(b3.p.c, var6);
                        this.z
                           .accept(
                              "Rich Presence connected: Playing Larp Client, " + var8 + (var6 == null ? "" : "; desktop account shown on the account card")
                           );
                        var3 = "connected session";

                        while (true) {
                           if (!this.Z(var35)) {
                              break label462;
                           }

                           t(var35);
                        }
                     }

                     throw new IOException("Discord activity acknowledgement missing");
                  }

                  throw new IOException("Discord handshake rejected");
               }
            } catch (IOException | RuntimeException var30) {
               if (this.Z(var35) && var1 == 0) {
                  this.z.accept("Rich Presence unavailable at " + var3 + "; retrying every 15 seconds while enabled");
                  var1 = 1;
               }
            } finally {
               var35.close();
               this.i = null;
               b3.publish(this.y && !this.T ? b3.p.P : b3.p.T, null);
               if (!this.y && !this.T && var35.c != null) {
                  this.z.accept("Rich Presence disabled; local IPC released");
               }
            }

            synchronized (this.D) {
               if (this.Z(var35)) {
                  this.D.wait(this.j);
               }
            }
         }
      } catch (InterruptedException var33) {
         Thread.currentThread().interrupt();
      } finally {
         f2.x var13 = this.i;
         if (var13 != null) {
            var13.close();
         }

         this.C.shutdown();
         this.z.accept("Rich Presence stopped; local IPC released");
      }
   }

   static JsonObject M(long var0, String var2, String var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("type", 0);
      var4.addProperty("name", "Larp Client");
      var4.addProperty("details", "Larping on " + a0.N);
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

   private static void a(f2.x var0, int var1, JsonObject var2) throws IOException {
      U(var0, var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   private static void U(f2.x var0, int var1, byte[] var2) throws IOException {
      synchronized (var0.a) {
         nh.t(var0.c, var1, var2);
      }
   }

   private static JsonObject t(f2.x var0) throws IOException {
      while (true) {
         nh.k var1 = nh.E(var0.c);
         if (var1.opcode() == 3) {
            U(var0, 4, var1.data());
         } else if (var1.opcode() != 4) {
            if (var1.opcode() != 1) {
               throw new IOException("Discord closed IPC");
            }

            JsonObject var2 = JsonParser.parseString(new String(var1.data(), StandardCharsets.UTF_8)).getAsJsonObject();
            if ("ERROR".equals(R(var2, "evt"))) {
               throw new IOException("Discord rejected RPC command");
            }

            return var2;
         }
      }
   }

   static JsonObject i(JsonObject var0, String var1) {
      JsonElement var2 = var0 == null ? null : var0.get(var1);
      return var2 != null && var2.isJsonObject() ? var2.getAsJsonObject() : null;
   }

   static String R(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isString() ? var2.getAsString() : "";
   }

   @FunctionalInterface
   interface g {
      ByteChannel Q(String var1) throws IOException;
   }

   private static final class x implements AutoCloseable {
      final long I;
      final Object a = new Object();
      volatile ByteChannel c;
      volatile long E = Long.MAX_VALUE;
      volatile boolean N;
      volatile String k;
      volatile long Y;

      x(long var1) {
         this.I = var1;
      }

      @Override
      public void close() {
         ByteChannel var1 = this.c;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var3) {
            }
         }
      }
   }
}
