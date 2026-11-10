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

public final class qk {
   private static final Gson M = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
   private final Path b;
   private final Path u;
   private final Path cK;
   private final yj j = new yj(qk::r);
   private String J = "Default";
   private String cI;
   private y0 a = new y0();
   private final Map<String, JsonObject> c = new HashMap<>();
   private JsonObject cC = new JsonObject();
   private JsonObject G = new JsonObject();
   public static boolean F;
   public static int cM;
   public static int g;
   public static boolean D;
   public static int cc;
   public static int I;
   public static boolean z;
   public static int l;
   public static boolean cW;
   public static int cF;
   public static boolean cb;
   public static int c1;
   public static boolean c6;
   public static boolean p;
   public static int T;
   public static boolean k;
   public static boolean cg;
   public static boolean Z;
   public static int ci;
   public static boolean S;
   public static boolean ce;
   public static boolean B;
   public static boolean o;
   public static boolean v;
   public static boolean A;
   public static boolean e;
   public static boolean t;
   public static int Q;
   public static int n;
   public static boolean cn;
   public static int s;
   public static int q;
   public static boolean h;
   public static int R;
   public static boolean W;
   public static int C;
   public static boolean N;
   public static boolean c8;
   public static int U;
   public static int m;
   public static boolean cu;
   public static int f;
   public static boolean r;
   public static boolean x;
   public static boolean E;
   public static int i;
   public static boolean y;
   public static int Y;
   public static boolean O;
   public static int w;
   public static boolean L;
   public static int d;
   public static boolean H;
   public static boolean K;
   public static boolean X;
   public static boolean c0;
   public static boolean V;

   public qk() {
      this(FabricLoader.getInstance().getConfigDir().resolve("larp-client"));
   }

   public qk(Path var1) {
      this.b = var1;
      this.u = var1.resolve("profiles");
      this.cK = var1.resolve("active-profile.txt");
   }

   public y0 F() {
      return this.a;
   }

   public boolean M(boolean var1) {
      boolean var2 = this.a.G4;
      if (var2 == var1) {
         return true;
      }

      this.a.G4 = var1;
      if (!this.N()) {
         this.a.G4 = var2;
         return false;
      }

      LarpClient var3 = LarpClient.b();
      if (var3 != null) {
         var3.c(var1);
      }

      return true;
   }

   public boolean h(boolean var1, List<Module> var2) {
      return var1 && this.a.B.B() && !this.a.B.m(false, var2, this.J, this::N) ? false : this.a.d.X(var1, var2, this.J, this::N);
   }

   public boolean V(boolean var1, List<Module> var2) {
      return var1 && this.a.d.C() && !this.a.d.X(false, var2, this.J, this::N) ? false : this.a.B.m(var1, var2, this.J, this::N);
   }

   public boolean p(boolean var1) {
      if (this.a.l == var1) {
         return true;
      } else {
         boolean var2 = this.a.l;
         this.a.l = var1;
         if (!this.N()) {
            this.a.l = var2;
            return false;
         } else {
            lj.B = var1;
            return true;
         }
      }
   }

   public boolean I(t_.t var1) {
      Objects.requireNonNull(var1);
      t_.t var2 = this.a.Z;
      if (var1 == var2) {
         return true;
      } else {
         this.a.Z = var1;
         if (!this.N()) {
            this.a.Z = var2;
            return false;
         } else {
            b0.r = var1;
            return true;
         }
      }
   }

   public boolean L(Module var1, com.larpclient.module.lb<?> var2, int var3) {
      je var4 = this.a.j.b();
      this.a.j.v(var1, var2, var3);
      if (this.N()) {
         q8.G("Input", "Setting shortcut {}.{} set to code {}", var1.getName(), var2.R(), var3);
         return true;
      } else {
         this.a.j = var4;
         return false;
      }
   }

   public boolean N() {
      try {
         this.j.D(this.b.resolve("menu.json"), M.toJson(this.a.K()));
         return true;
      } catch (IOException var2) {
         q8.u("Menu", "Could not save local interface preferences", var2);
         return false;
      }
   }

   public void g() {
      this.j.K(this.b.resolve("menu.json"), M.toJson(this.a.K()));
   }

   public void U() {
      if (this.cI != null) {
         this.v();
      } else {
         String var1;
         try {
            var1 = D(this.Z());
         } catch (IllegalArgumentException var3) {
            q8.u("Config", "Profile " + this.J + " was not saved; its previous saved copy is unchanged", var3);
            return;
         }

         this.j.K(this.n(this.J), var1);
         this.j.K(this.cK, this.J);
         this.g();
      }
   }

   public String n() {
      return this.j.P();
   }

   public void r() {
      try {
         this.j.close();
      } catch (IOException var2) {
         q8.u("Config", "Final preference flush failed", var2);
      }
   }

   private void s() {
      Path var1 = this.b.resolve("menu.json");
      if (Files.exists(var1)) {
         try {
            if (Files.isSymbolicLink(var1) || Files.size(var1) > 131072L) {
               throw new IOException("Unsafe menu preferences");
            }

            this.a = y0.O(this.p(var1));
         } catch (Exception var3) {
            q8.u("Menu", "Local interface preferences unavailable; using safe defaults", var3);
         }
      }
   }

   public synchronized void v(String var1, String var2) throws IOException {
      this.j.I();
      Path var3 = this.n(var1);
      Path var4 = this.n(var2);
      if (!var1.equals(var2)) {
         if (Files.isSymbolicLink(this.u) || Files.isSymbolicLink(var3) || Files.isSymbolicLink(var4)) {
            throw new IOException("Unsafe profile path");
         }

         if (Files.exists(var4)) {
            throw new FileAlreadyExistsException("That profile name already exists");
         }

         Files.createLink(var4, var3);

         try {
            if (this.J.equals(var1)) {
               r(this.cK, var2);
            }
         } catch (IOException var7) {
            Files.deleteIfExists(var4);
            throw var7;
         }

         try {
            Files.delete(var3);
         } catch (IOException var6) {
            if (this.J.equals(var1)) {
               r(this.cK, var1);
            }

            Files.deleteIfExists(var4);
            throw var6;
         }

         if (this.J.equals(var1)) {
            this.J = var2;
         }

         this.a.H(var1, var2);
         this.N();
         q8.G("Config", "Renamed profile {} to {}", var1, var2);
      }
   }

   public synchronized void E(String var1) throws IOException {
      this.j.I();
      if (this.J.equals(var1)) {
         throw new IOException("Load another profile before deleting the current one");
      }

      Path var2 = this.n(var1);
      if (!Files.isSymbolicLink(this.u) && !Files.isSymbolicLink(var2)) {
         Files.delete(var2);
         this.a.d(var1);
         this.N();
         q8.G("Config", "Deleted profile {}", var1);
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   public synchronized void f(String var1, boolean var2) throws IOException {
      this.J(var1, this.Z(), var2, true);
   }

   public synchronized void b(String var1, String var2) throws IOException {
      this.J(var2, this.Y(var1), false, true);
   }

   public String d() {
      return this.J;
   }

   private Path n(String var1) {
      com.larpclient.h.Y(var1);
      return this.u.resolve(var1 + ".json");
   }

   public List<String> h() {
      try {
         if (!Files.isDirectory(this.u)) {
            return List.of();
         }

         try (Stream var1 = Files.list(this.u)) {
            return var1.filter(var0 -> var0.toString().endsWith(".json") && Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS))
               .map(var0 -> var0.getFileName().toString().replaceFirst("\\.json$", ""))
               .filter(var0 -> {
                  try {
                     com.larpclient.h.Y(var0);
                     return true;
                  } catch (IllegalArgumentException var2) {
                     return false;
                  }
               })
               .sorted()
               .toList();
         }
      } catch (IOException var6) {
         q8.u("Config", "Cannot list profiles at " + this.u, var6);
         return List.of();
      }
   }

   public void h() {
      if (this.cI != null) {
         this.v();
      } else {
         this.d(this.J);
      }
   }

   public boolean d(String var1) {
      try {
         this.j.I();
         Path var2 = this.n(var1);
         r(var2, D(this.Z()));
         r(this.cK, var1);
         this.a.B.e(this.J, var1);
         this.a.d.s(this.J, var1);
         this.J = var1;
         this.cI = null;
         if (this.a.B.B() || this.a.d.C()) {
            this.N();
         }

         q8.G("Config", "Saved {}", var2);
         return true;
      } catch (Exception var3) {
         q8.u("Config", "Failed to save profile " + var1, var3);
         return false;
      }
   }

   public boolean G(String var1) {
      try {
         this.j.I();
         Path var2 = this.n(var1);
         if (!Files.exists(var2)) {
            q8.L("Config", "Profile not found: {}", var2);
            return false;
         }

         JsonObject var3 = this.Y(var1);
         String var4 = this.J;
         r(this.cK, var1);

         try {
            this.D(var3);
         } catch (RuntimeException var6) {
            r(this.cK, var4);
            throw var6;
         }

         this.J = var1;
         this.cI = null;
         this.a.n(var1, System.currentTimeMillis());
         this.N();
         q8.G("Config", "Loaded {}", var2);
         return true;
      } catch (Exception var7) {
         q8.u("Config", "Failed to load profile " + var1 + "; existing values retained where possible", var7);
         return false;
      }
   }

   public void i(String var1) {
      try {
         this.E(var1);
      } catch (Exception var3) {
         q8.u("Config", "Failed to delete profile " + var1, var3);
      }
   }

   public void J() {
      q8.G("Config", "Configuration directory: {}", this.b);
      this.s();

      try {
         Files.createDirectories(this.u);
         if (Files.exists(this.cK)) {
            String var1 = Files.readString(this.cK).trim();

            try {
               this.n(var1);
               this.J = var1;
            } catch (IllegalArgumentException var3) {
               q8.L("Config", "Invalid active profile; using Default");
            }
         }

         Path var5 = this.n(this.J);
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS)) {
            if (!this.G(this.J)) {
               this.cI = this.J;
               this.v();
            }
         } else {
            Path var2 = this.b.resolve("modules.json");
            if (Files.exists(var2)) {
               this.D(this.p(var2));
               q8.G("Config", "Migrated legacy {} to schema 2", var2);
               this.h();
            }
         }
      } catch (Exception var4) {
         this.cI = this.J;
         q8.u("Config", "Configuration load failed; protecting original profile and retaining defaults", var4);
         this.v();
      }

      lj.B = this.a.l;
      b0.r = this.a.Z;
      this.w(H());
   }

   private synchronized void v() {
      String var1 = this.cI;
      if (var1 != null) {
         try {
            if (Files.isSymbolicLink(this.u)) {
               throw new IOException("Unsafe profile path");
            }

            Files.createDirectories(this.u);
            Path var2 = Files.createTempFile(this.u, "larp-recovery-", ".tmp");

            try {
               Files.writeString(var2, D(this.Z()));
               long var3 = 1L;

               String var5;
               while (true) {
                  var5 = "Recovery " + var3;

                  try {
                     Files.createLink(this.n(var5), var2);
                     break;
                  } catch (FileAlreadyExistsException var11) {
                     var3++;
                  }
               }

               r(this.cK, var5);
               this.a.B.e(this.J, var5);
               this.a.d.s(this.J, var5);
               this.J = var5;
               this.cI = null;
               this.N();
               q8.L("Config", "Rejected startup profile {} preserved; fallback saved as {}", var1, var5);
            } finally {
               Files.deleteIfExists(var2);
            }
         } catch (Exception var13) {
            q8.u("Config", "Recovery save failed; automatic saves will preserve rejected profile " + var1, var13);
         }
      }
   }

   void w(List<Module> var1) {
      if (this.a.B.z().get("active").getAsBoolean() && this.a.d.z().get("active").getAsBoolean()) {
         this.a.B = new f_();
         this.a.d = new eq();
         this.N();
         q8.L("Menu", "Conflicting cosmetic preset journals ignored; loaded profile retained");
      }

      this.a.B.N(var1, this.J, this::N);
      this.a.d.q(var1, this.J, this::N);
   }

   private JsonObject p(Path var1) throws IOException {
      try {
         return JsonParser.parseString(Files.readString(var1)).getAsJsonObject();
      } catch (RuntimeException var4) {
         Path var3 = var1.resolveSibling(var1.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var1, var3);
         q8.u("Config", "Malformed JSON preserved at " + var3, var4);
         throw var4;
      }
   }

   public JsonObject Y(String var1) throws IOException {
      this.j.I();
      Path var2 = this.n(var1);
      if (!Files.isSymbolicLink(var2) && !Files.isSymbolicLink(this.u) && Files.size(var2) <= 131072L) {
         return this.p(var2);
      } else {
         throw new IOException("Unsafe or oversized profile");
      }
   }

   public boolean X(String var1) {
      return Files.exists(this.n(var1), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized void c(String var1, JsonObject var2, boolean var3, List<Module> var4) throws IOException {
      this.J(var1, var2, var3, false);
   }

   private synchronized void J(String var1, JsonObject var2, boolean var3, boolean var4) throws IOException {
      this.j.I();
      var2 = var4 ? com.larpclient.h.E(var2) : com.larpclient.h.L(var2);
      Path var5 = this.n(var1);
      if (!Files.isSymbolicLink(this.u) && !Files.isSymbolicLink(var5)) {
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS) && !var3) {
            throw new FileAlreadyExistsException("Choose a new profile name or explicitly confirm replacement.");
         }

         String var6 = D(var2);
         if (var3) {
            r(var5, var6);
         } else {
            Files.createDirectories(this.u);
            Path var7 = Files.createTempFile(this.u, "larp-import-", ".tmp");

            try {
               Files.writeString(var7, var6);
               Files.createLink(var5, var7);
            } finally {
               Files.deleteIfExists(var7);
            }
         }

         q8.v("Config", "Imported profile saved locally");
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   private JsonObject Z() {
      return this.U(H());
   }

   private static String D(JsonObject var0) {
      com.larpclient.h.E(var0);
      String var1 = M.toJson(var0);
      return var1.getBytes(StandardCharsets.UTF_8).length <= 131072 ? var1 : var0.toString();
   }

   JsonObject U(List<Module> var1) {
      return Q(this.cC, this.T(var1), this.G).getAsJsonObject();
   }

   private static JsonElement Q(JsonElement var0, JsonElement var1, JsonElement var2) {
      if (var0 instanceof JsonObject var3 && var1 instanceof JsonObject var4) {
         JsonObject var5 = var3.deepCopy();

         for (Entry var7 : var4.entrySet()) {
            JsonElement var8 = var2 instanceof JsonObject var9 ? var9.get((String)var7.getKey()) : null;
            var5.add((String)var7.getKey(), Q(var3.get((String)var7.getKey()), (JsonElement)var7.getValue(), var8));
         }

         return var5;
      } else {
         return var0 != null && var1.equals(var2) ? var0.deepCopy() : var1.deepCopy();
      }
   }

   private JsonObject T(List<Module> var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("schema", 2);
      var2.add("ui", hz.x());
      JsonObject var3 = new JsonObject();

      for (Module var5 : var1) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("enabled", var5.isEnabledPreference());
         var6.addProperty("keybind", var5.getKeyBind());
         JsonObject var7 = new JsonObject();

         for (com.larpclient.module.lb var9 : var5.getSettings()) {
            var7.add(var9.R(), M.toJsonTree(var9.getValue()));
         }

         var6.add("settings", var7);
         this.a.B.C(var5, var6);
         this.a.d.E(var5, var6);
         if (this.c.containsKey(var5.getName())) {
            var6.add("legacySettings", this.c.get(var5.getName()).deepCopy());
         }

         var3.add(var5.getName(), var6);
      }

      var2.add("modules", var3);
      return var2;
   }

   private void D(JsonObject var1) {
      this.J(var1, H());
   }

   private static List<Module> H() {
      LarpClient var0 = LarpClient.b();
      return var0 == null ? List.of() : var0.G().M();
   }

   void J(JsonObject var1, List<Module> var2) {
      JsonObject var3 = com.larpclient.h.E(var1);
      jp.Q(var1, var3);
      var1 = var3;
      JsonObject var4 = var1.has("modules") ? var1.getAsJsonObject("modules") : var1;
      this.a.B.n(var2, this::N);
      this.a.d.s(var2, this::N);
      if (var1.has("ui")) {
         hz.R(var1.getAsJsonObject("ui"));
      } else {
         hz.o++;
      }

      if (!var1.has("schema")) {
         q8.G("Config", "Reading legacy module configuration");
      }

      cj.Q(var4);
      JsonObject var5 = new JsonObject();

      for (Entry var7 : var4.entrySet()) {
         String var8 = var2.stream().map(Module::getName).filter(((String)var7.getKey())::equalsIgnoreCase).findFirst().orElse((String)var7.getKey());
         var5.add(var8, (JsonElement)var7.getValue());
      }

      var4 = var5;
      var1.add("modules", var4);
      this.c.clear();

      for (Entry var16 : var4.entrySet()) {
         String var17 = (String)var16.getKey();
         Module var9 = var2.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var17)).findFirst().orElse(null);
         if (var9 == null) {
            q8.G("Config", "Unknown module {} ignored; its values are kept for future saves", var17);
         } else {
            try {
               JsonObject var10 = ((JsonElement)var16.getValue()).getAsJsonObject();
               j1 var11 = j1.decode(var9, var10);
               var11.apply(var9);
               if (var11.archive() != null) {
                  this.c.put(var9.getName(), var11.archive());
               }
            } catch (RuntimeException var12) {
               q8.u("Config", "Invalid module entry " + var17, var12);
            }
         }
      }

      this.cC = var1.deepCopy();
      this.G = this.T(var2);
      q8.G("Config", "Applied supported profile values; unfamiliar values retained for future saves");
   }

   private static void r(Path var0, String var1) throws IOException {
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
