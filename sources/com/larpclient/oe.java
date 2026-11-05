package com.larpclient;

import com.google.gson.JsonObject;
import java.net.IDN;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public record oe(UUID id, oe.k type, String host, int port, String username, String password) {
   public static final int MAX_CREDENTIAL = 255;

   public oe(UUID id, oe.k type, String host, int port, String username, String password) {
      Objects.requireNonNull(id);
      Objects.requireNonNull(type);
      host = host(host);
      if (port >= 1 && port <= 65535) {
         username = username == null ? "" : username;
         password = password == null ? "" : password;
         if (username.getBytes(StandardCharsets.UTF_8).length > 255 || password.getBytes(StandardCharsets.UTF_8).length > 255) {
            throw new IllegalArgumentException("Proxy usernames and passwords are limited to 255 bytes.");
         }

         if (username.chars().anyMatch(Character::isISOControl) || password.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Proxy credentials cannot contain control characters.");
         }

         if (type == oe.k.q && !password.isEmpty()) {
            throw new IllegalArgumentException("SOCKS4 proxies use a user ID only, without a password.");
         }

         if (type == oe.k.u && username.contains(":")) {
            throw new IllegalArgumentException("HTTP proxy usernames cannot contain a colon.");
         }

         this.id = id;
         this.type = type;
         this.host = host;
         this.port = port;
         this.username = username;
         this.password = password;
      } else {
         throw new IllegalArgumentException("Use a proxy port from 1 to 65535.");
      }
   }

   public boolean authenticated() {
      return !this.username.isEmpty() || !this.password.isEmpty();
   }

   public String label() {
      return this.type.I + " " + this.endpoint();
   }

   public String endpoint() {
      return (this.host.indexOf(58) >= 0 ? "[" + this.host + "]" : this.host) + ":" + this.port;
   }

   @Override
   public String toString() {
      return "Saved proxy " + this.label();
   }

   public oe.u resolve() throws UnknownHostException {
      return new oe.u(this, new InetSocketAddress(InetAddress.getByName(this.host), this.port));
   }

   public JsonObject json() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("id", this.id.toString());
      var1.addProperty("type", this.type.name());
      var1.addProperty("host", this.host);
      var1.addProperty("port", this.port);
      if (!this.username.isEmpty()) {
         var1.addProperty("username", this.username);
      }

      if (!this.password.isEmpty()) {
         var1.addProperty("password", this.password);
      }

      return var1;
   }

   public static oe fromJson(JsonObject var0) {
      return new oe(
         UUID.fromString(var0.get("id").getAsString()),
         oe.k.valueOf(var0.get("type").getAsString()),
         var0.get("host").getAsString(),
         var0.get("port").getAsInt(),
         var0.has("username") ? var0.get("username").getAsString() : "",
         var0.has("password") ? var0.get("password").getAsString() : ""
      );
   }

   public static oe parse(oe.k var0, String var1, String var2, String var3) {
      String var4 = var1 == null ? "" : var1.strip();
      if (var4.isEmpty()) {
         throw new IllegalArgumentException("Enter the proxy host and port.");
      }

      String var5 = var4;
      int var6 = var0.M;
      if (var4.startsWith("[")) {
         int var7 = var4.indexOf(93);
         if (var7 < 0) {
            throw new IllegalArgumentException("Close the IPv6 address with ].");
         }

         var5 = var4.substring(1, var7);
         String var8 = var4.substring(var7 + 1);
         if (!var8.isEmpty()) {
            if (!var8.startsWith(":")) {
               throw new IllegalArgumentException("Use [address]:port for IPv6 proxies.");
            }

            var6 = port(var8.substring(1));
         }
      } else if (var4.chars().filter(var0x -> var0x == 58).count() == 1L) {
         var5 = var4.substring(0, var4.indexOf(58));
         var6 = port(var4.substring(var4.indexOf(58) + 1));
      }

      return new oe(UUID.randomUUID(), var0, var5, var6, var2 == null ? "" : var2.strip(), var3 == null ? "" : var3);
   }

   public static oe.s split(String var0, oe.k var1) {
      String var2 = var0 == null ? "" : var0.strip();
      oe.k var3 = var1;
      int var4 = var2.indexOf("://");
      if (var4 > 0) {
         var3 = switch (var2.substring(0, var4).toLowerCase(Locale.ROOT)) {
            case "socks5", "socks5h", "socks" -> oe.k.x;
            case "socks4", "socks4a" -> oe.k.q;
            case "http", "https" -> oe.k.u;
            default -> null;
         };
         if (var3 == null) {
            return null;
         }

         var2 = var2.substring(var4 + 3);
         if (var2.endsWith("/")) {
            var2 = var2.substring(0, var2.length() - 1);
         }
      }

      int var8 = var2.lastIndexOf(64);
      if (var8 > 0) {
         String var10 = var2.substring(0, var8);
         int var7 = var10.indexOf(58);
         return new oe.s(var3, var2.substring(var8 + 1), var7 < 0 ? var10 : var10.substring(0, var7), var7 < 0 ? "" : var10.substring(var7 + 1));
      }

      if (!var2.startsWith("[")) {
         String[] var9 = var2.split(":", 4);
         if (var9.length == 4) {
            return new oe.s(var3, var9[0] + ":" + var9[1], var9[2], var9[3]);
         }
      }

      return var4 > 0 ? new oe.s(var3, var2, "", "") : null;
   }

   private static int port(String var0) {
      try {
         int var1 = Integer.parseInt(var0.strip());
         if (var1 >= 1 && var1 <= 65535) {
            return var1;
         }
      } catch (NumberFormatException var2) {
      }

      throw new IllegalArgumentException("Use a proxy port from 1 to 65535.");
   }

   private static String host(String var0) {
      String var1 = var0 == null ? "" : var0.strip();
      if (var1.startsWith("[") && var1.endsWith("]")) {
         var1 = var1.substring(1, var1.length() - 1);
      }

      if (var1.isEmpty()) {
         throw new IllegalArgumentException("Enter the proxy host and port.");
      }

      if (var1.indexOf(58) >= 0) {
         if (!var1.matches("[0-9A-Fa-f:.]{2,45}")) {
            throw new IllegalArgumentException("That IPv6 proxy address is not valid.");
         } else {
            return var1.toLowerCase(Locale.ROOT);
         }
      } else {
         String var2;
         try {
            var2 = IDN.toASCII(var1, 2);
         } catch (IllegalArgumentException var4) {
            throw new IllegalArgumentException("That proxy host name is not valid.");
         }

         if (var2.length() <= 253 && var2.matches("[A-Za-z0-9.-]+") && !var2.startsWith(".") && !var2.contains("..")) {
            return var2.toLowerCase(Locale.ROOT);
         } else {
            throw new IllegalArgumentException("That proxy host name is not valid.");
         }
      }
   }

   public enum k {
      x("SOCKS5", 1080),
      q("SOCKS4", 1080),
      u("HTTP", 8080);

      public final String I;
      public final int M;

      k(String var3, int var4) {
         this.I = var3;
         this.M = var4;
      }
   }

   public record s(oe.k type, String address, String username, String password) {
   }

   public record u(oe settings, InetSocketAddress address) {
      public u(oe settings, InetSocketAddress address) {
         Objects.requireNonNull(settings);
         if (Objects.requireNonNull(address).isUnresolved()) {
            throw new IllegalArgumentException("Unresolved proxy address");
         }

         this.settings = settings;
         this.address = address;
      }

      @Override
      public String toString() {
         return this.settings.label();
      }
   }
}
