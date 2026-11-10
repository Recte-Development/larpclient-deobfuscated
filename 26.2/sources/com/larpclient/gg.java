package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.Module;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;

public final class gg {
   private static final Gson i = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
   private final Path O;
   private final Path l;
   private final Path Et;
   private final zc o = new zc(gg::K);
   private String E1 = "Default";
   private String Ez;
   private hb j = new hb();
   private final Map<String, JsonObject> K = new HashMap<>();
   private JsonObject x = new JsonObject();
   private JsonObject EY = new JsonObject();
   public static int I;
   public static boolean X;
   public static int A;
   public static boolean Q;
   public static int EQ;
   public static int s;
   public static int n;
   public static int E;
   public static int m;
   public static boolean w;
   public static int u;
   public static int U;
   public static boolean r;
   public static boolean N;
   public static int V;
   public static boolean k;
   public static int p;
   public static int v;
   public static int b;
   public static boolean EM;
   public static int W;
   public static boolean Y;
   public static int C;
   public static boolean D;
   public static int T;
   public static int E3;
   public static boolean z;
   public static boolean EO;
   public static int EZ;
   public static boolean E4;
   public static boolean EJ;
   public static int J;
   public static int Eu;
   public static int Ex;
   public static int F;
   public static boolean Z;
   public static boolean t;
   public static int Ec;
   public static boolean a;
   public static boolean G;
   public static boolean L;
   public static int Ew;
   public static boolean d;
   public static boolean B;
   public static int H;
   public static int q;
   public static boolean M;
   public static int EG;
   public static int f;
   public static int e;
   public static int g;
   public static int EU;
   public static int R;
   public static int y;
   public static int P;
   public static boolean S;
   public static int h;

   public gg() {
      this(FabricLoader.getInstance().getConfigDir().resolve("larp-client"));
   }

   public gg(Path var1) {
      this.O = var1;
      this.l = var1.resolve("profiles");
      this.Et = var1.resolve("active-profile.txt");
   }

   public hb g() {
      return this.j;
   }

   public boolean A(boolean var1) {
      boolean var2 = this.j.w;
      if (var2 == var1) {
         return true;
      }

      this.j.w = var1;
      if (!this.r()) {
         this.j.w = var2;
         return false;
      }

      LarpClient var3 = LarpClient.R();
      if (var3 != null) {
         var3.I(var1);
      }

      return true;
   }

   public boolean g(boolean var1, List<Module> var2) {
      return var1 && this.j.d.X() && !this.j.d.q(false, var2, this.E1, this::r) ? false : this.j.p.J(var1, var2, this.E1, this::r);
   }

   public boolean j(boolean var1, List<Module> var2) {
      return var1 && this.j.p.s() && !this.j.p.J(false, var2, this.E1, this::r) ? false : this.j.d.q(var1, var2, this.E1, this::r);
   }

   public boolean v(boolean var1) {
      if (this.j.u == var1) {
         return true;
      } else {
         boolean var2 = this.j.u;
         this.j.u = var1;
         if (!this.r()) {
            this.j.u = var2;
            return false;
         } else {
            he.C = var1;
            return true;
         }
      }
   }

   public boolean D(wa.a var1) {
      Objects.requireNonNull(var1);
      wa.a var2 = this.j.V;
      if (var1 == var2) {
         return true;
      } else {
         this.j.V = var1;
         if (!this.r()) {
            this.j.V = var2;
            return false;
         } else {
            com.larpclient.y.m = var1;
            return true;
         }
      }
   }

   public boolean m(Module var1, com.larpclient.module.q<?> var2, int var3) {
      wr var4 = this.j.K.O();
      this.j.K.Q(var1, var2, var3);
      if (this.r()) {
         com.larpclient.h.A("Input", "Setting shortcut {}.{} set to code {}", var1.getName(), var2.e(), var3);
         return true;
      } else {
         this.j.K = var4;
         return false;
      }
   }

   public boolean r() {
      try {
         this.o.p(this.O.resolve("menu.json"), i.toJson(this.j.E()));
         return true;
      } catch (IOException var2) {
         com.larpclient.h.m("Menu", "Could not save local interface preferences", var2);
         return false;
      }
   }

   public void W() {
      this.o.q(this.O.resolve("menu.json"), i.toJson(this.j.E()));
   }

   public void C() {
      if (this.Ez != null) {
         this.i();
      } else {
         String var1;
         try {
            var1 = O(this.A());
         } catch (IllegalArgumentException var3) {
            com.larpclient.h.m("Config", "Profile " + this.E1 + " was not saved; its previous saved copy is unchanged", var3);
            return;
         }

         this.o.q(this.J(this.E1), var1);
         this.o.q(this.Et, this.E1);
         this.W();
      }
   }

   public String H() {
      return this.o.x();
   }

   public void k() {
      try {
         this.o.close();
      } catch (IOException var2) {
         com.larpclient.h.m("Config", "Final preference flush failed", var2);
      }
   }

   private void j() {
      Path var1 = this.O.resolve("menu.json");
      if (Files.exists(var1)) {
         try {
            if (Files.isSymbolicLink(var1) || Files.size(var1) > 131072L) {
               throw new IOException("Unsafe menu preferences");
            }

            this.j = hb.E(this.n(var1));
         } catch (Exception var3) {
            com.larpclient.h.m("Menu", "Local interface preferences unavailable; using safe defaults", var3);
         }
      }
   }

   public synchronized void T(String var1, String var2) throws IOException {
      this.o.g();
      Path var3 = this.J(var1);
      Path var4 = this.J(var2);
      if (!var1.equals(var2)) {
         if (Files.isSymbolicLink(this.l) || Files.isSymbolicLink(var3) || Files.isSymbolicLink(var4)) {
            throw new IOException("Unsafe profile path");
         }

         if (Files.exists(var4)) {
            throw new FileAlreadyExistsException("That profile name already exists");
         }

         Files.createLink(var4, var3);

         try {
            if (this.E1.equals(var1)) {
               K(this.Et, var2);
            }
         } catch (IOException var7) {
            Files.deleteIfExists(var4);
            throw var7;
         }

         try {
            Files.delete(var3);
         } catch (IOException var6) {
            if (this.E1.equals(var1)) {
               K(this.Et, var1);
            }

            Files.deleteIfExists(var4);
            throw var6;
         }

         if (this.E1.equals(var1)) {
            this.E1 = var2;
         }

         this.j.r(var1, var2);
         this.r();
         com.larpclient.h.A("Config", "Renamed profile {} to {}", var1, var2);
      }
   }

   public synchronized void S(String var1) throws IOException {
      this.o.g();
      if (this.E1.equals(var1)) {
         throw new IOException("Load another profile before deleting the current one");
      }

      Path var2 = this.J(var1);
      if (!Files.isSymbolicLink(this.l) && !Files.isSymbolicLink(var2)) {
         Files.delete(var2);
         this.j.C(var1);
         this.r();
         com.larpclient.h.A("Config", "Deleted profile {}", var1);
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   public synchronized void M(String var1, boolean var2) throws IOException {
      this.I(var1, this.A(), var2, true);
   }

   public synchronized void E(String var1, String var2) throws IOException {
      this.I(var2, this.Z(var1), false, true);
   }

   public String M() {
      return this.E1;
   }

   private Path J(String var1) {
      km.r(var1);
      return this.l.resolve(var1 + ".json");
   }

   public List<String> B() {
      try {
         if (!Files.isDirectory(this.l)) {
            return List.of();
         }

         try (Stream var1 = Files.list(this.l)) {
            return var1.filter(var0 -> var0.toString().endsWith(".json") && Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS))
               .map(var0 -> var0.getFileName().toString().replaceFirst("\\.json$", ""))
               .filter(var0 -> {
                  try {
                     km.r(var0);
                     return true;
                  } catch (IllegalArgumentException var2) {
                     return false;
                  }
               })
               .sorted()
               .toList();
         }
      } catch (IOException var6) {
         com.larpclient.h.m("Config", "Cannot list profiles at " + this.l, var6);
         return List.of();
      }
   }

   public void m() {
      if (this.Ez != null) {
         this.i();
      } else {
         this.K(this.E1);
      }
   }

   public boolean K(String var1) {
      try {
         this.o.g();
         Path var2 = this.J(var1);
         K(var2, O(this.A()));
         K(this.Et, var1);
         this.j.d.i(this.E1, var1);
         this.j.p.H(this.E1, var1);
         this.E1 = var1;
         this.Ez = null;
         if (this.j.d.X() || this.j.p.s()) {
            this.r();
         }

         com.larpclient.h.A("Config", "Saved {}", var2);
         return true;
      } catch (Exception var3) {
         com.larpclient.h.m("Config", "Failed to save profile " + var1, var3);
         return false;
      }
   }

   public boolean T(String var1) {
      try {
         this.o.g();
         Path var2 = this.J(var1);
         if (!Files.exists(var2)) {
            com.larpclient.h.g("Config", "Profile not found: {}", var2);
            return false;
         }

         JsonObject var3 = this.Z(var1);
         String var4 = this.E1;
         K(this.Et, var1);

         try {
            this.U(var3);
         } catch (RuntimeException var6) {
            K(this.Et, var4);
            throw var6;
         }

         this.E1 = var1;
         this.Ez = null;
         this.j.u(var1, System.currentTimeMillis());
         this.r();
         com.larpclient.h.A("Config", "Loaded {}", var2);
         return true;
      } catch (Exception var7) {
         com.larpclient.h.m("Config", "Failed to load profile " + var1 + "; existing values retained where possible", var7);
         return false;
      }
   }

   public void A(String var1) {
      try {
         this.S(var1);
      } catch (Exception var3) {
         com.larpclient.h.m("Config", "Failed to delete profile " + var1, var3);
      }
   }

   public void a() {
      com.larpclient.h.A("Config", "Configuration directory: {}", this.O);
      this.j();

      try {
         Files.createDirectories(this.l);
         if (Files.exists(this.Et)) {
            String var1 = Files.readString(this.Et).trim();

            try {
               this.J(var1);
               this.E1 = var1;
            } catch (IllegalArgumentException var3) {
               com.larpclient.h.g("Config", "Invalid active profile; using Default");
            }
         }

         Path var5 = this.J(this.E1);
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS)) {
            if (!this.T(this.E1)) {
               this.Ez = this.E1;
               this.i();
            }
         } else {
            Path var2 = this.O.resolve("modules.json");
            if (Files.exists(var2)) {
               this.U(this.n(var2));
               com.larpclient.h.A("Config", "Migrated legacy {} to schema 2", var2);
               this.m();
            }
         }
      } catch (Exception var4) {
         this.Ez = this.E1;
         com.larpclient.h.m("Config", "Configuration load failed; protecting original profile and retaining defaults", var4);
         this.i();
      }

      he.C = this.j.u;
      com.larpclient.y.m = this.j.V;
      this.y(R());
   }

   private synchronized void i() {
      String var1 = this.Ez;
      if (var1 != null) {
         try {
            if (Files.isSymbolicLink(this.l)) {
               throw new IOException("Unsafe profile path");
            }

            Files.createDirectories(this.l);
            Path var2 = Files.createTempFile(this.l, "larp-recovery-", ".tmp");

            try {
               Files.writeString(var2, O(this.A()));
               long var3 = 1L;

               String var5;
               while (true) {
                  var5 = "Recovery " + var3;

                  try {
                     Files.createLink(this.J(var5), var2);
                     break;
                  } catch (FileAlreadyExistsException var11) {
                     var3++;
                  }
               }

               K(this.Et, var5);
               this.j.d.i(this.E1, var5);
               this.j.p.H(this.E1, var5);
               this.E1 = var5;
               this.Ez = null;
               this.r();
               com.larpclient.h.g("Config", "Rejected startup profile {} preserved; fallback saved as {}", var1, var5);
            } finally {
               Files.deleteIfExists(var2);
            }
         } catch (Exception var13) {
            com.larpclient.h.m("Config", "Recovery save failed; automatic saves will preserve rejected profile " + var1, var13);
         }
      }
   }

   void y(List<Module> var1) {
      if (this.j.d.a().get("active").getAsBoolean() && this.j.p.j().get("active").getAsBoolean()) {
         this.j.d = new u3();
         this.j.p = new kf();
         this.r();
         com.larpclient.h.g("Menu", "Conflicting cosmetic preset journals ignored; loaded profile retained");
      }

      this.j.d.u(var1, this.E1, this::r);
      this.j.p.m(var1, this.E1, this::r);
   }

   private JsonObject n(Path var1) throws IOException {
      try {
         return JsonParser.parseString(Files.readString(var1)).getAsJsonObject();
      } catch (RuntimeException var4) {
         Path var3 = var1.resolveSibling(var1.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var1, var3);
         com.larpclient.h.m("Config", "Malformed JSON preserved at " + var3, var4);
         throw var4;
      }
   }

   public JsonObject Z(String var1) throws IOException {
      this.o.g();
      Path var2 = this.J(var1);
      if (!Files.isSymbolicLink(var2) && !Files.isSymbolicLink(this.l) && Files.size(var2) <= 131072L) {
         return this.n(var2);
      } else {
         throw new IOException("Unsafe or oversized profile");
      }
   }

   public boolean m(String var1) {
      return Files.exists(this.J(var1), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized void X(String var1, JsonObject var2, boolean var3, List<Module> var4) throws IOException {
      this.I(var1, var2, var3, false);
   }

   private synchronized void I(String var1, JsonObject var2, boolean var3, boolean var4) throws IOException {
      this.o.g();
      var2 = var4 ? km.l(var2) : km.z(var2);
      Path var5 = this.J(var1);
      if (!Files.isSymbolicLink(this.l) && !Files.isSymbolicLink(var5)) {
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS) && !var3) {
            throw new FileAlreadyExistsException("Choose a new profile name or explicitly confirm replacement.");
         }

         String var6 = O(var2);
         if (var3) {
            K(var5, var6);
         } else {
            Files.createDirectories(this.l);
            Path var7 = Files.createTempFile(this.l, "larp-import-", ".tmp");

            try {
               Files.writeString(var7, var6);
               Files.createLink(var5, var7);
            } finally {
               Files.deleteIfExists(var7);
            }
         }

         com.larpclient.h.U("Config", "Imported profile saved locally");
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   private JsonObject A() {
      return this.S(R());
   }

   private static String O(JsonObject var0) {
      km.l(var0);
      String var1 = i.toJson(var0);
      return var1.getBytes(StandardCharsets.UTF_8).length <= 131072 ? var1 : var0.toString();
   }

   JsonObject S(List<Module> var1) {
      return o(this.x, this.Y(var1), this.EY).getAsJsonObject();
   }

   private static JsonElement o(JsonElement var0, JsonElement var1, JsonElement var2) {
      if (var0 instanceof JsonObject var3 && var1 instanceof JsonObject var4) {
         JsonObject var5 = var3.deepCopy();

         for (Entry var7 : var4.entrySet()) {
            JsonElement var8 = var2 instanceof JsonObject var9 ? var9.get((String)var7.getKey()) : null;
            var5.add((String)var7.getKey(), o(var3.get((String)var7.getKey()), (JsonElement)var7.getValue(), var8));
         }

         return var5;
      } else {
         return var0 != null && var1.equals(var2) ? var0.deepCopy() : var1.deepCopy();
      }
   }

   private JsonObject Y(List<Module> var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("schema", 2);
      var2.add("ui", c7.w());
      JsonObject var3 = new JsonObject();

      for (Module var5 : var1) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("enabled", var5.isEnabledPreference());
         var6.addProperty("keybind", var5.getKeyBind());
         JsonObject var7 = new JsonObject();

         for (com.larpclient.module.q var9 : var5.getSettings()) {
            var7.add(var9.e(), i.toJsonTree(var9.getValue()));
         }

         var6.add("settings", var7);
         this.j.d.n(var5, var6);
         this.j.p.W(var5, var6);
         if (this.K.containsKey(var5.getName())) {
            var6.add("legacySettings", this.K.get(var5.getName()).deepCopy());
         }

         var3.add(var5.getName(), var6);
      }

      var2.add("modules", var3);
      return var2;
   }

   private void U(JsonObject var1) {
      this.P(var1, R());
   }

   private static List<Module> R() {
      LarpClient var0 = LarpClient.R();
      return var0 == null ? List.of() : var0.z().h();
   }

   void P(JsonObject var1, List<Module> var2) {
      JsonObject var3 = km.l(var1);
      com.larpclient.p.y(var1, var3);
      var1 = var3;
      JsonObject var4 = var1.has("modules") ? var1.getAsJsonObject("modules") : var1;
      this.j.d.O(var2, this::r);
      this.j.p.i(var2, this::r);
      if (var1.has("ui")) {
         c7.S(var1.getAsJsonObject("ui"));
      } else {
         c7.w++;
      }

      if (!var1.has("schema")) {
         com.larpclient.h.A("Config", "Reading legacy module configuration");
      }

      kg.a(var4);
      JsonObject var5 = new JsonObject();

      for (Entry var7 : var4.entrySet()) {
         String var8 = var2.stream().map(Module::getName).filter(((String)var7.getKey())::equalsIgnoreCase).findFirst().orElse((String)var7.getKey());
         var5.add(var8, (JsonElement)var7.getValue());
      }

      var4 = var5;
      var1.add("modules", var4);
      this.K.clear();

      for (Entry var16 : var4.entrySet()) {
         String var17 = (String)var16.getKey();
         Module var9 = var2.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var17)).findFirst().orElse(null);
         if (var9 == null) {
            com.larpclient.h.A("Config", "Unknown module {} ignored; its values are kept for future saves", var17);
         } else {
            try {
               JsonObject var10 = ((JsonElement)var16.getValue()).getAsJsonObject();
               jc var11 = jc.decode(var9, var10);
               var11.apply(var9);
               if (var11.archive() != null) {
                  this.K.put(var9.getName(), var11.archive());
               }
            } catch (RuntimeException var12) {
               com.larpclient.h.m("Config", "Invalid module entry " + var17, var12);
            }
         }
      }

      this.x = var1.deepCopy();
      this.EY = this.Y(var2);
      com.larpclient.h.A("Config", "Applied supported profile values; unfamiliar values retained for future saves");
   }

   private static void K(Path var0, String var1) throws IOException {
      Files.createDirectories(var0.getParent());
      Path var2 = Files.createTempFile(var0.getParent(), "larp-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
