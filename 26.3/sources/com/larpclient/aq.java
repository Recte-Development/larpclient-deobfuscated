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

public final class aq {
   private static final Gson F = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
   private final Path U;
   private final Path N0;
   private final Path d;
   private final og J = new og(aq::v);
   private String P = "Default";
   private String Nq;
   private i3 V = new i3();
   private final Map<String, JsonObject> p = new HashMap<>();
   private JsonObject N_ = new JsonObject();
   private JsonObject N = new JsonObject();
   public static boolean n;
   public static int NM;
   public static int A;
   public static int Nz;
   public static boolean g;
   public static boolean c;
   public static int B;
   public static int I;
   public static int W;
   public static boolean O;
   public static int NJ;
   public static boolean f;
   public static int y;
   public static boolean NV;
   public static boolean R;
   public static boolean NA;
   public static boolean h;
   public static int l;
   public static boolean v;
   public static int j;
   public static boolean NL;
   public static boolean Nh;
   public static int T;
   public static int NE;
   public static int H;
   public static int K;
   public static int C;
   public static boolean q;
   public static boolean w;
   public static int e;
   public static int S;
   public static int m;
   public static boolean a;
   public static boolean Nr;
   public static int s;
   public static int Nx;
   public static boolean u;
   public static int r;
   public static int G;
   public static int Nb;
   public static boolean D;
   public static boolean i;
   public static int E;
   public static int k;
   public static boolean N6;
   public static int b;
   public static int L;
   public static int NI;
   public static boolean X;
   public static boolean o;
   public static boolean NU;
   public static int Q;
   public static int M;
   public static boolean z;
   public static boolean t;
   public static int Y;
   public static boolean N7;
   public static boolean Z;
   public static boolean x;

   public aq() {
      this(FabricLoader.getInstance().getConfigDir().resolve("larp-client"));
   }

   public aq(Path var1) {
      this.U = var1;
      this.N0 = var1.resolve("profiles");
      this.d = var1.resolve("active-profile.txt");
   }

   public i3 k() {
      return this.V;
   }

   public boolean l(boolean var1) {
      boolean var2 = this.V.f;
      if (var2 == var1) {
         return true;
      }

      this.V.f = var1;
      if (!this.P()) {
         this.V.f = var2;
         return false;
      }

      LarpClient var3 = LarpClient.N();
      if (var3 != null) {
         var3.j(var1);
      }

      return true;
   }

   public boolean y(boolean var1, List<Module> var2) {
      return var1 && this.V.Y.w() && !this.V.Y.o(false, var2, this.P, this::P) ? false : this.V.c.t(var1, var2, this.P, this::P);
   }

   public boolean s(boolean var1, List<Module> var2) {
      return var1 && this.V.c.w() && !this.V.c.t(false, var2, this.P, this::P) ? false : this.V.Y.o(var1, var2, this.P, this::P);
   }

   public boolean r(boolean var1) {
      if (this.V.t == var1) {
         return true;
      } else {
         boolean var2 = this.V.t;
         this.V.t = var1;
         if (!this.P()) {
            this.V.t = var2;
            return false;
         } else {
            d3.y = var1;
            return true;
         }
      }
   }

   public boolean W(nl.q var1) {
      Objects.requireNonNull(var1);
      nl.q var2 = this.V.W;
      if (var1 == var2) {
         return true;
      } else {
         this.V.W = var1;
         if (!this.P()) {
            this.V.W = var2;
            return false;
         } else {
            a4.h = var1;
            return true;
         }
      }
   }

   public boolean V(Module var1, com.larpclient.module.r<?> var2, int var3) {
      b1 var4 = this.V.Q.v();
      this.V.Q.q(var1, var2, var3);
      if (this.P()) {
         _q.z("Input", "Setting shortcut {}.{} set to code {}", var1.getName(), var2.X(), var3);
         return true;
      } else {
         this.V.Q = var4;
         return false;
      }
   }

   public boolean P() {
      try {
         this.J.X(this.U.resolve("menu.json"), F.toJson(this.V.M()));
         return true;
      } catch (IOException var2) {
         _q.o("Menu", "Could not save local interface preferences", var2);
         return false;
      }
   }

   public void c() {
      this.J.a(this.U.resolve("menu.json"), F.toJson(this.V.M()));
   }

   public void z() {
      if (this.Nq != null) {
         this.r();
      } else {
         String var1;
         try {
            var1 = k(this.G());
         } catch (IllegalArgumentException var3) {
            _q.o("Config", "Profile " + this.P + " was not saved; its previous saved copy is unchanged", var3);
            return;
         }

         this.J.a(this.H(this.P), var1);
         this.J.a(this.d, this.P);
         this.c();
      }
   }

   public String u() {
      return this.J.t();
   }

   public void M() {
      try {
         this.J.close();
      } catch (IOException var2) {
         _q.o("Config", "Final preference flush failed", var2);
      }
   }

   private void e() {
      Path var1 = this.U.resolve("menu.json");
      if (Files.exists(var1)) {
         try {
            if (Files.isSymbolicLink(var1) || Files.size(var1) > 131072L) {
               throw new IOException("Unsafe menu preferences");
            }

            this.V = i3.s(this.T(var1));
         } catch (Exception var3) {
            _q.o("Menu", "Local interface preferences unavailable; using safe defaults", var3);
         }
      }
   }

   public synchronized void K(String var1, String var2) throws IOException {
      this.J.E();
      Path var3 = this.H(var1);
      Path var4 = this.H(var2);
      if (!var1.equals(var2)) {
         if (Files.isSymbolicLink(this.N0) || Files.isSymbolicLink(var3) || Files.isSymbolicLink(var4)) {
            throw new IOException("Unsafe profile path");
         }

         if (Files.exists(var4)) {
            throw new FileAlreadyExistsException("That profile name already exists");
         }

         Files.createLink(var4, var3);

         try {
            if (this.P.equals(var1)) {
               v(this.d, var2);
            }
         } catch (IOException var7) {
            Files.deleteIfExists(var4);
            throw var7;
         }

         try {
            Files.delete(var3);
         } catch (IOException var6) {
            if (this.P.equals(var1)) {
               v(this.d, var1);
            }

            Files.deleteIfExists(var4);
            throw var6;
         }

         if (this.P.equals(var1)) {
            this.P = var2;
         }

         this.V.n(var1, var2);
         this.P();
         _q.z("Config", "Renamed profile {} to {}", var1, var2);
      }
   }

   public synchronized void v(String var1) throws IOException {
      this.J.E();
      if (this.P.equals(var1)) {
         throw new IOException("Load another profile before deleting the current one");
      }

      Path var2 = this.H(var1);
      if (!Files.isSymbolicLink(this.N0) && !Files.isSymbolicLink(var2)) {
         Files.delete(var2);
         this.V.Q(var1);
         this.P();
         _q.z("Config", "Deleted profile {}", var1);
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   public synchronized void s(String var1, boolean var2) throws IOException {
      this.u(var1, this.G(), var2, true);
   }

   public synchronized void a(String var1, String var2) throws IOException {
      this.u(var2, this.m(var1), false, true);
   }

   public String S() {
      return this.P;
   }

   private Path H(String var1) {
      _4.p(var1);
      return this.N0.resolve(var1 + ".json");
   }

   public List<String> n() {
      try {
         if (!Files.isDirectory(this.N0)) {
            return List.of();
         }

         try (Stream var1 = Files.list(this.N0)) {
            return var1.filter(var0 -> var0.toString().endsWith(".json") && Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS))
               .map(var0 -> var0.getFileName().toString().replaceFirst("\\.json$", ""))
               .filter(var0 -> {
                  try {
                     _4.p(var0);
                     return true;
                  } catch (IllegalArgumentException var2) {
                     return false;
                  }
               })
               .sorted()
               .toList();
         }
      } catch (IOException var6) {
         _q.o("Config", "Cannot list profiles at " + this.N0, var6);
         return List.of();
      }
   }

   public void g() {
      if (this.Nq != null) {
         this.r();
      } else {
         this.Q(this.P);
      }
   }

   public boolean Q(String var1) {
      try {
         this.J.E();
         Path var2 = this.H(var1);
         v(var2, k(this.G()));
         v(this.d, var1);
         this.V.Y.h(this.P, var1);
         this.V.c.c(this.P, var1);
         this.P = var1;
         this.Nq = null;
         if (this.V.Y.w() || this.V.c.w()) {
            this.P();
         }

         _q.z("Config", "Saved {}", var2);
         return true;
      } catch (Exception var3) {
         _q.o("Config", "Failed to save profile " + var1, var3);
         return false;
      }
   }

   public boolean y(String var1) {
      try {
         this.J.E();
         Path var2 = this.H(var1);
         if (!Files.exists(var2)) {
            _q.R("Config", "Profile not found: {}", var2);
            return false;
         }

         JsonObject var3 = this.m(var1);
         String var4 = this.P;
         v(this.d, var1);

         try {
            this.D(var3);
         } catch (RuntimeException var6) {
            v(this.d, var4);
            throw var6;
         }

         this.P = var1;
         this.Nq = null;
         this.V.m(var1, System.currentTimeMillis());
         this.P();
         _q.z("Config", "Loaded {}", var2);
         return true;
      } catch (Exception var7) {
         _q.o("Config", "Failed to load profile " + var1 + "; existing values retained where possible", var7);
         return false;
      }
   }

   public void j(String var1) {
      try {
         this.v(var1);
      } catch (Exception var3) {
         _q.o("Config", "Failed to delete profile " + var1, var3);
      }
   }

   public void p() {
      _q.z("Config", "Configuration directory: {}", this.U);
      this.e();

      try {
         Files.createDirectories(this.N0);
         if (Files.exists(this.d)) {
            String var1 = Files.readString(this.d).trim();

            try {
               this.H(var1);
               this.P = var1;
            } catch (IllegalArgumentException var3) {
               _q.R("Config", "Invalid active profile; using Default");
            }
         }

         Path var5 = this.H(this.P);
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS)) {
            if (!this.y(this.P)) {
               this.Nq = this.P;
               this.r();
            }
         } else {
            Path var2 = this.U.resolve("modules.json");
            if (Files.exists(var2)) {
               this.D(this.T(var2));
               _q.z("Config", "Migrated legacy {} to schema 2", var2);
               this.g();
            }
         }
      } catch (Exception var4) {
         this.Nq = this.P;
         _q.o("Config", "Configuration load failed; protecting original profile and retaining defaults", var4);
         this.r();
      }

      d3.y = this.V.t;
      a4.h = this.V.W;
      this.k(Q());
   }

   private synchronized void r() {
      String var1 = this.Nq;
      if (var1 != null) {
         try {
            if (Files.isSymbolicLink(this.N0)) {
               throw new IOException("Unsafe profile path");
            }

            Files.createDirectories(this.N0);
            Path var2 = Files.createTempFile(this.N0, "larp-recovery-", ".tmp");

            try {
               Files.writeString(var2, k(this.G()));
               long var3 = 1L;

               String var5;
               while (true) {
                  var5 = "Recovery " + var3;

                  try {
                     Files.createLink(this.H(var5), var2);
                     break;
                  } catch (FileAlreadyExistsException var11) {
                     var3++;
                  }
               }

               v(this.d, var5);
               this.V.Y.h(this.P, var5);
               this.V.c.c(this.P, var5);
               this.P = var5;
               this.Nq = null;
               this.P();
               _q.R("Config", "Rejected startup profile {} preserved; fallback saved as {}", var1, var5);
            } finally {
               Files.deleteIfExists(var2);
            }
         } catch (Exception var13) {
            _q.o("Config", "Recovery save failed; automatic saves will preserve rejected profile " + var1, var13);
         }
      }
   }

   void k(List<Module> var1) {
      if (this.V.Y.t().get("active").getAsBoolean() && this.V.c.h().get("active").getAsBoolean()) {
         this.V.Y = new hi();
         this.V.c = new r();
         this.P();
         _q.R("Menu", "Conflicting cosmetic preset journals ignored; loaded profile retained");
      }

      this.V.Y.Z(var1, this.P, this::P);
      this.V.c.I(var1, this.P, this::P);
   }

   private JsonObject T(Path var1) throws IOException {
      try {
         return JsonParser.parseString(Files.readString(var1)).getAsJsonObject();
      } catch (RuntimeException var4) {
         Path var3 = var1.resolveSibling(var1.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var1, var3);
         _q.o("Config", "Malformed JSON preserved at " + var3, var4);
         throw var4;
      }
   }

   public JsonObject m(String var1) throws IOException {
      this.J.E();
      Path var2 = this.H(var1);
      if (!Files.isSymbolicLink(var2) && !Files.isSymbolicLink(this.N0) && Files.size(var2) <= 131072L) {
         return this.T(var2);
      } else {
         throw new IOException("Unsafe or oversized profile");
      }
   }

   public boolean g(String var1) {
      return Files.exists(this.H(var1), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized void v(String var1, JsonObject var2, boolean var3, List<Module> var4) throws IOException {
      this.u(var1, var2, var3, false);
   }

   private synchronized void u(String var1, JsonObject var2, boolean var3, boolean var4) throws IOException {
      this.J.E();
      var2 = var4 ? _4.G(var2) : _4.R(var2);
      Path var5 = this.H(var1);
      if (!Files.isSymbolicLink(this.N0) && !Files.isSymbolicLink(var5)) {
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS) && !var3) {
            throw new FileAlreadyExistsException("Choose a new profile name or explicitly confirm replacement.");
         }

         String var6 = k(var2);
         if (var3) {
            v(var5, var6);
         } else {
            Files.createDirectories(this.N0);
            Path var7 = Files.createTempFile(this.N0, "larp-import-", ".tmp");

            try {
               Files.writeString(var7, var6);
               Files.createLink(var5, var7);
            } finally {
               Files.deleteIfExists(var7);
            }
         }

         _q.c("Config", "Imported profile saved locally");
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   private JsonObject G() {
      return this.T(Q());
   }

   private static String k(JsonObject var0) {
      _4.G(var0);
      String var1 = F.toJson(var0);
      return var1.getBytes(StandardCharsets.UTF_8).length <= 131072 ? var1 : var0.toString();
   }

   JsonObject T(List<Module> var1) {
      return f(this.N_, this.m(var1), this.N).getAsJsonObject();
   }

   private static JsonElement f(JsonElement var0, JsonElement var1, JsonElement var2) {
      if (var0 instanceof JsonObject var3 && var1 instanceof JsonObject var4) {
         JsonObject var5 = var3.deepCopy();

         for (Entry var7 : var4.entrySet()) {
            JsonElement var8 = var2 instanceof JsonObject var9 ? var9.get((String)var7.getKey()) : null;
            var5.add((String)var7.getKey(), f(var3.get((String)var7.getKey()), (JsonElement)var7.getValue(), var8));
         }

         return var5;
      } else {
         return var0 != null && var1.equals(var2) ? var0.deepCopy() : var1.deepCopy();
      }
   }

   private JsonObject m(List<Module> var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("schema", 2);
      var2.add("ui", bh.Z());
      JsonObject var3 = new JsonObject();

      for (Module var5 : var1) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("enabled", var5.isEnabledPreference());
         var6.addProperty("keybind", var5.getKeyBind());
         JsonObject var7 = new JsonObject();

         for (com.larpclient.module.r var9 : var5.getSettings()) {
            var7.add(var9.X(), F.toJsonTree(var9.getValue()));
         }

         var6.add("settings", var7);
         this.V.Y.D(var5, var6);
         this.V.c.V(var5, var6);
         if (this.p.containsKey(var5.getName())) {
            var6.add("legacySettings", this.p.get(var5.getName()).deepCopy());
         }

         var3.add(var5.getName(), var6);
      }

      var2.add("modules", var3);
      return var2;
   }

   private void D(JsonObject var1) {
      this.m(var1, Q());
   }

   private static List<Module> Q() {
      LarpClient var0 = LarpClient.N();
      return var0 == null ? List.of() : var0.j().d();
   }

   void m(JsonObject var1, List<Module> var2) {
      JsonObject var3 = _4.G(var1);
      pi.J(var1, var3);
      var1 = var3;
      JsonObject var4 = var1.has("modules") ? var1.getAsJsonObject("modules") : var1;
      this.V.Y.S(var2, this::P);
      this.V.c.C(var2, this::P);
      if (var1.has("ui")) {
         bh.n(var1.getAsJsonObject("ui"));
      } else {
         bh.k++;
      }

      if (!var1.has("schema")) {
         _q.z("Config", "Reading legacy module configuration");
      }

      ju.o(var4);
      JsonObject var5 = new JsonObject();

      for (Entry var7 : var4.entrySet()) {
         String var8 = var2.stream().map(Module::getName).filter(((String)var7.getKey())::equalsIgnoreCase).findFirst().orElse((String)var7.getKey());
         var5.add(var8, (JsonElement)var7.getValue());
      }

      var4 = var5;
      var1.add("modules", var4);
      this.p.clear();

      for (Entry var16 : var4.entrySet()) {
         String var17 = (String)var16.getKey();
         Module var9 = var2.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var17)).findFirst().orElse(null);
         if (var9 == null) {
            _q.z("Config", "Unknown module {} ignored; its values are kept for future saves", var17);
         } else {
            try {
               JsonObject var10 = ((JsonElement)var16.getValue()).getAsJsonObject();
               nd var11 = nd.decode(var9, var10);
               var11.apply(var9);
               if (var11.archive() != null) {
                  this.p.put(var9.getName(), var11.archive());
               }
            } catch (RuntimeException var12) {
               _q.o("Config", "Invalid module entry " + var17, var12);
            }
         }
      }

      this.N_ = var1.deepCopy();
      this.N = this.m(var2);
      _q.z("Config", "Applied supported profile values; unfamiliar values retained for future saves");
   }

   private static void v(Path var0, String var1) throws IOException {
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
