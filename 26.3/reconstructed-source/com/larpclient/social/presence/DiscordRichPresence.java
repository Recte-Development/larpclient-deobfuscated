package com.larpclient.social.presence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.core.BuildInfo;
import com.larpclient.network.ConnectionStatus;
import com.larpclient.util.misc.LarpLog;
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

public final class DiscordRichPresence implements AutoCloseable {
   public static final String cLIENT_ID = "1491537245591572480";
   static final String aPP_NAME = "Larp Client";
   static final String sITE_URL = "https://larpclient.com";
   static final String lOGO_URL = "https://larpclient.com/larp-logo.png";
   static final String sTATE_MENUS = "In the menus";
   static final String sTATE_SINGLEPLAYER = "Singleplayer";
   static final String sTATE_REALMS = "Realms";
   static final String sTATE_LAN = "LAN world";
   static final String sTATE_MULTIPLAYER = "Multiplayer";
   private static final int mAX_HOST_LENGTH = 64;
   private static final long hANDSHAKE_TIMEOUT_NANOS = TimeUnit.SECONDS.toNanos(10L);
   private final Object lock = new Object();
   private final List<String> pipePaths;
   private final DiscordRichPresence.ChannelConnector connector;
   private final Consumer<String> logger;
   private final long retryDelayMs;
   private final long updateIntervalNanos;
   private final long startTimestamp = System.currentTimeMillis() / 1000L;
   private final ScheduledExecutorService watchdog = Executors.newSingleThreadScheduledExecutor(
      var0 -> Thread.ofPlatform().daemon().name("Larp-Discord-Guard").unstarted(var0)
   );
   private volatile boolean enabled;
   private volatile boolean closed;
   private volatile long generation;
   private volatile String state = "In the menus";
   private volatile DiscordRichPresence.Connection session;
   private Thread rpcThread;

   public DiscordRichPresence(boolean var1) {
      this(
         DiscordIpc.candidatePaths(System.getProperty("os.name", ""), System.getenv()),
         DiscordIpc::connect,
         var0 -> LarpLog.info("Discord", var0),
         15000L,
         5000L
      );
      this.setEnabled(var1);
   }

   DiscordRichPresence(List<String> var1, DiscordRichPresence.ChannelConnector var2, Consumer<String> var3, long var4, long var6) {
      this.pipePaths = List.copyOf(var1);
      this.connector = var2;
      this.logger = var3;
      this.retryDelayMs = var4;
      this.updateIntervalNanos = TimeUnit.MILLISECONDS.toNanos(var6);
   }

   public void setState(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.state = var1;
      }
   }

   public static String resolveState(boolean var0, boolean var1, boolean var2, boolean var3, String var4) {
      if (!var0) {
         return "In the menus";
      } else if (var1) {
         return "Singleplayer";
      } else if (var2) {
         return "Realms";
      } else {
         return var3 ? "LAN world" : formatServerHost(var4);
      }
   }

   static String formatServerHost(String var0) {
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

   public void setEnabled(boolean var1) {
      synchronized (this.lock) {
         if (!this.closed && this.enabled != var1) {
            this.enabled = var1;
            this.generation++;
            ConnectionStatus.publish(var1 ? ConnectionStatus.State.OFFLINE : ConnectionStatus.State.ACTIVITY_OFF, null);
            if (this.rpcThread == null && var1) {
               this.watchdog.scheduleWithFixedDelay(this::checkWatchdog, 0L, 100L, TimeUnit.MILLISECONDS);
               this.rpcThread = Thread.ofPlatform().daemon().name("Larp-Discord-RPC").start(this::runLoop);
            }

            this.lock.notifyAll();
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.lock) {
         this.closed = (boolean)1;
         this.enabled = (boolean)0;
         this.generation++;
         ConnectionStatus.publish(ConnectionStatus.State.ACTIVITY_OFF, null);
         this.lock.notifyAll();
         if (this.rpcThread == null) {
            this.watchdog.shutdown();
         }
      }
   }

   private boolean isCurrent(DiscordRichPresence.Connection var1) {
      return !this.closed && this.enabled && var1.generation == this.generation;
   }

   private void checkWatchdog() {
      DiscordRichPresence.Connection var1 = this.session;
      if (var1 != null) {
         if (this.isCurrent(var1) && System.nanoTime() <= var1.handshakeDeadline) {
            String var2 = this.state;
            if (var1.isReady && !var2.equals(var1.lastActivity) && System.nanoTime() - var1.lastUpdateTime >= this.updateIntervalNanos) {
               try {
                  this.runLoop(var1, var2);
                  this.logger.accept("Rich Presence updated: " + var2);
               } catch (IOException | RuntimeException var4) {
                  var1.close();
               }
            }
         } else {
            var1.close();
         }
      }
   }

   private void runLoop(DiscordRichPresence.Connection var1, String var2) throws IOException {
      var1.lastActivity = var2;
      var1.lastUpdateTime = System.nanoTime();
      writeJson(var1, 1, buildActivityCommand(this.startTimestamp, UUID.randomUUID().toString(), var2));
   }

   private void runLoop() {
      byte var1 = 0;

      try {
         while (!this.closed) {
            synchronized (this.lock) {
               while (true) {
                  if (!this.enabled && !this.closed) {
                     this.lock.wait();
                  } else {
                     if (this.closed) {
                        return;
                     }
                     break;
                  }
               }
            }

            DiscordRichPresence.Connection var35 = new DiscordRichPresence.Connection(this.generation);
            this.session = var35;
            String var3 = "desktop discovery";

            try {
               for (String var5 : this.pipePaths) {
                  if (!this.isCurrent(var35)) {
                     break;
                  }

                  try {
                     var35.channel = this.connector.connect(var5);
                     break;
                  } catch (IOException var29) {
                  }
               }

               if (var35.channel == null) {
                  throw new IOException("Discord desktop unavailable");
               }

               label462:
               if (this.isCurrent(var35)) {
                  var3 = "handshake";
                  var35.handshakeDeadline = System.nanoTime() + hANDSHAKE_TIMEOUT_NANOS;
                  JsonObject var36 = new JsonObject();
                  var36.addProperty("v", 1);
                  var36.addProperty("client_id", "1491537245591572480");
                  writeJson(var35, 0, var36);
                  JsonObject var38 = readMessage(var35);
                  if ("DISPATCH".equals(getString(var38, "cmd")) && "READY".equals(getString(var38, "evt"))) {
                     DiscordUser var6 = DiscordUser.read(setState(setState(var38, "data"), "user"));
                     if (!this.isCurrent(var35)) {
                        continue;
                     }

                     String var7 = UUID.randomUUID().toString();
                     String var8 = this.state;
                     var3 = "activity acknowledgement";
                     var35.lastActivity = var8;
                     var35.lastUpdateTime = System.nanoTime();
                     writeJson(var35, 1, buildActivityCommand(this.startTimestamp, var7, var8));
                     JsonObject var9 = readMessage(var35);
                     if ("SET_ACTIVITY".equals(getString(var9, "cmd")) && var7.equals(getString(var9, "nonce"))) {
                        if (!this.isCurrent(var35)) {
                           continue;
                        }

                        var35.handshakeDeadline = Long.MAX_VALUE;
                        var35.isReady = (boolean)1;
                        var1 = 0;
                        ConnectionStatus.publish(ConnectionStatus.State.ONLINE, var6);
                        this.logger
                           .accept(
                              "Rich Presence connected: Playing Larp Client, " + var8 + (var6 == null ? "" : "; desktop account shown on the account card")
                           );
                        var3 = "connected session";

                        while (true) {
                           if (!this.isCurrent(var35)) {
                              break label462;
                           }

                           readMessage(var35);
                        }
                     }

                     throw new IOException("Discord activity acknowledgement missing");
                  }

                  throw new IOException("Discord handshake rejected");
               }
            } catch (IOException | RuntimeException var30) {
               if (this.isCurrent(var35) && var1 == 0) {
                  this.logger.accept("Rich Presence unavailable at " + var3 + "; retrying every 15 seconds while enabled");
                  var1 = 1;
               }
            } finally {
               var35.close();
               this.session = null;
               ConnectionStatus.publish(this.enabled && !this.closed ? ConnectionStatus.State.OFFLINE : ConnectionStatus.State.ACTIVITY_OFF, null);
               if (!this.enabled && !this.closed && var35.channel != null) {
                  this.logger.accept("Rich Presence disabled; local IPC released");
               }
            }

            synchronized (this.lock) {
               if (this.isCurrent(var35)) {
                  this.lock.wait(this.retryDelayMs);
               }
            }
         }
      } catch (InterruptedException var33) {
         Thread.currentThread().interrupt();
      } finally {
         DiscordRichPresence.Connection var13 = this.session;
         if (var13 != null) {
            var13.close();
         }

         this.watchdog.shutdown();
         this.logger.accept("Rich Presence stopped; local IPC released");
      }
   }

   static JsonObject buildActivityCommand(long var0, String var2, String var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("type", 0);
      var4.addProperty("name", "Larp Client");
      var4.addProperty("details", "Larping on " + BuildInfo.minecraftVersion);
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

   private static void writeJson(DiscordRichPresence.Connection var0, int var1, JsonObject var2) throws IOException {
      writePacket(var0, var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   private static void writePacket(DiscordRichPresence.Connection var0, int var1, byte[] var2) throws IOException {
      synchronized (var0.writeLock) {
         DiscordIpc.writeFrame(var0.channel, var1, var2);
      }
   }

   private static JsonObject readMessage(DiscordRichPresence.Connection var0) throws IOException {
      while (true) {
         DiscordIpc.Frame var1 = DiscordIpc.readFrame(var0.channel);
         if (var1.opcode() == 3) {
            writePacket(var0, 4, var1.data());
         } else if (var1.opcode() != 4) {
            if (var1.opcode() != 1) {
               throw new IOException("Discord closed IPC");
            }

            JsonObject var2 = JsonParser.parseString(new String(var1.data(), StandardCharsets.UTF_8)).getAsJsonObject();
            if ("ERROR".equals(getString(var2, "evt"))) {
               throw new IOException("Discord rejected RPC command");
            }

            return var2;
         }
      }
   }

   static JsonObject setState(JsonObject var0, String var1) {
      JsonElement var2 = var0 == null ? null : var0.get(var1);
      return var2 != null && var2.isJsonObject() ? var2.getAsJsonObject() : null;
   }

   static String getString(JsonObject var0, String var1) {
      JsonElement var2 = var0.get(var1);
      return var2 != null && var2.isJsonPrimitive() && var2.getAsJsonPrimitive().isString() ? var2.getAsString() : "";
   }

   @FunctionalInterface
   interface ChannelConnector {
      ByteChannel connect(String var1) throws IOException;
   }

   private static final class Connection implements AutoCloseable {
      final long generation;
      final Object writeLock = new Object();
      volatile ByteChannel channel;
      volatile long handshakeDeadline = Long.MAX_VALUE;
      volatile boolean isReady;
      volatile String lastActivity;
      volatile long lastUpdateTime;

      Connection(long var1) {
         this.generation = var1;
      }

      @Override
      public void close() {
         ByteChannel var1 = this.channel;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var3) {
            }
         }
      }
   }
}
