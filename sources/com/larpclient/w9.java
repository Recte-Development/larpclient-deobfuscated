package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.HalloweenModule;
import com.larpclient.module.Module;
import com.larpclient.module.df;
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

public final class w9 {
   private static final Gson Z = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
   private final Path I;
   private final Path v;
   private final Path b5;
   private final ck R = new ck(w9::K);
   private String p = "Default";
   private String q;
   private xv h = new xv();
   private final Map<String, JsonObject> i = new HashMap<>();
   private JsonObject U = new JsonObject();
   private JsonObject M = new JsonObject();
   public static boolean e;
   public static int O;
   public static boolean H;
   public static boolean c;
   public static int P;
   public static int bE;
   public static boolean bT;
   public static boolean W;
   public static int bO;
   public static boolean V;
   public static boolean bP;
   public static boolean K;
   public static boolean s;
   public static boolean Q;
   public static int bs;
   public static boolean F;
   public static boolean C;
   public static boolean B;
   public static boolean G;
   public static int x;
   public static boolean Y;
   public static int j;
   public static boolean bk;
   public static boolean L;
   public static int A;
   public static boolean w;
   public static int bZ;
   public static boolean E;
   public static boolean g;
   public static int z;
   public static int bJ;
   public static int n;
   public static int bF;
   public static boolean d;
   public static int b;
   public static int bY;
   public static boolean y;
   public static int D;
   public static int o;
   public static boolean f;
   public static boolean bh;
   public static boolean t;
   public static int X;
   public static boolean S;
   public static int bM;
   public static boolean m;
   public static boolean l;
   public static int a;
   public static boolean T;
   public static int bW;
   public static boolean u;
   public static int J;
   public static int N;
   public static int bC;
   public static boolean k;
   public static boolean r;

   public w9() {
      this(FabricLoader.getInstance().getConfigDir().resolve("larp-client"));
   }

   public w9(Path var1) {
      this.I = var1;
      this.v = var1.resolve("profiles");
      this.b5 = var1.resolve("active-profile.txt");
   }

   public xv t() {
      return this.h;
   }

   public boolean X(boolean var1) {
      boolean var2 = this.h.d;
      if (var2 == var1) {
         return true;
      }

      this.h.d = var1;
      if (!this.S()) {
         this.h.d = var2;
         return false;
      }

      LarpClient var3 = LarpClient.F();
      if (var3 != null) {
         var3.a(var1);
      }

      return true;
   }

   public boolean N(boolean var1, List<Module> var2) {
      return var1 && this.h.K.X() && !this.h.K.g(false, var2, this.p, this::S) ? false : this.h.C.o(var1, var2, this.p, this::S);
   }

   public boolean v(boolean var1, List<Module> var2) {
      return var1 && this.h.C.Y() && !this.h.C.o(false, var2, this.p, this::S) ? false : this.h.K.g(var1, var2, this.p, this::S);
   }

   public boolean S(boolean var1) {
      if (this.h.L == var1) {
         return true;
      } else {
         boolean var2 = this.h.L;
         this.h.L = var1;
         if (!this.S()) {
            this.h.L = var2;
            return false;
         } else {
            _v.e = var1;
            return true;
         }
      }
   }

   public boolean x(m5.n var1) {
      Objects.requireNonNull(var1);
      m5.n var2 = this.h.B;
      if (var1 == var2) {
         return true;
      } else {
         this.h.B = var1;
         if (!this.S()) {
            this.h.B = var2;
            return false;
         } else {
            _k.t = var1;
            return true;
         }
      }
   }

   public boolean s(Module var1, df<?> var2, int var3) {
      um var4 = this.h.s.T();
      this.h.s.v(var1, var2, var3);
      if (this.S()) {
         mt.K("Input", "Setting shortcut {}.{} set to code {}", var1.getName(), var2.D(), var3);
         return true;
      } else {
         this.h.s = var4;
         return false;
      }
   }

   public boolean S() {
      try {
         this.R.W(this.I.resolve("menu.json"), Z.toJson(this.h.t()));
         return true;
      } catch (IOException var2) {
         mt.P("Menu", "Could not save local interface preferences", var2);
         return false;
      }
   }

   public void P() {
      this.R.v(this.I.resolve("menu.json"), Z.toJson(this.h.t()));
   }

   public void f() {
      if (this.q != null) {
         this.d();
      } else {
         String var1;
         try {
            var1 = d(this.w());
         } catch (IllegalArgumentException var3) {
            mt.P("Config", "Profile " + this.p + " was not saved; its previous saved copy is unchanged", var3);
            return;
         }

         this.R.v(this.c(this.p), var1);
         this.R.v(this.b5, this.p);
         this.P();
      }
   }

   public String u() {
      return this.R.K();
   }

   public void y() {
      try {
         this.R.close();
      } catch (IOException var2) {
         mt.P("Config", "Final preference flush failed", var2);
      }
   }

   private void W() {
      Path var1 = this.I.resolve("menu.json");
      if (Files.exists(var1)) {
         try {
            if (Files.isSymbolicLink(var1) || Files.size(var1) > 131072L) {
               throw new IOException("Unsafe menu preferences");
            }

            this.h = xv.c(this.T(var1));
         } catch (Exception var3) {
            mt.P("Menu", "Local interface preferences unavailable; using safe defaults", var3);
         }
      }
   }

   public synchronized void G(String var1, String var2) throws IOException {
      this.R.W();
      Path var3 = this.c(var1);
      Path var4 = this.c(var2);
      if (!var1.equals(var2)) {
         if (Files.isSymbolicLink(this.v) || Files.isSymbolicLink(var3) || Files.isSymbolicLink(var4)) {
            throw new IOException("Unsafe profile path");
         }

         if (Files.exists(var4)) {
            throw new FileAlreadyExistsException("That profile name already exists");
         }

         Files.createLink(var4, var3);

         try {
            if (this.p.equals(var1)) {
               K(this.b5, var2);
            }
         } catch (IOException var7) {
            Files.deleteIfExists(var4);
            throw var7;
         }

         try {
            Files.delete(var3);
         } catch (IOException var6) {
            if (this.p.equals(var1)) {
               K(this.b5, var1);
            }

            Files.deleteIfExists(var4);
            throw var6;
         }

         if (this.p.equals(var1)) {
            this.p = var2;
         }

         this.h.c(var1, var2);
         this.S();
         mt.K("Config", "Renamed profile {} to {}", var1, var2);
      }
   }

   public synchronized void h(String var1) throws IOException {
      this.R.W();
      if (this.p.equals(var1)) {
         throw new IOException("Load another profile before deleting the current one");
      }

      Path var2 = this.c(var1);
      if (!Files.isSymbolicLink(this.v) && !Files.isSymbolicLink(var2)) {
         Files.delete(var2);
         this.h.Y(var1);
         this.S();
         mt.K("Config", "Deleted profile {}", var1);
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   public synchronized void Z(String var1, boolean var2) throws IOException {
      this.Z(var1, this.w(), var2, true);
   }

   public synchronized void s(String var1, String var2) throws IOException {
      this.Z(var2, this.s(var1), false, true);
   }

   public String w() {
      return this.p;
   }

   private Path c(String var1) {
      p_.g(var1);
      return this.v.resolve(var1 + ".json");
   }

   public List<String> H() {
      try {
         if (!Files.isDirectory(this.v)) {
            return List.of();
         }

         try (Stream var1 = Files.list(this.v)) {
            return var1.filter(var0 -> var0.toString().endsWith(".json") && Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS))
               .map(var0 -> var0.getFileName().toString().replaceFirst("\\.json$", ""))
               .filter(var0 -> {
                  try {
                     p_.g(var0);
                     return true;
                  } catch (IllegalArgumentException var2) {
                     return false;
                  }
               })
               .sorted()
               .toList();
         }
      } catch (IOException var6) {
         mt.P("Config", "Cannot list profiles at " + this.v, var6);
         return List.of();
      }
   }

   public void b() {
      if (this.q != null) {
         this.d();
      } else {
         this.E(this.p);
      }
   }

   public boolean E(String var1) {
      try {
         this.R.W();
         Path var2 = this.c(var1);
         K(var2, d(this.w()));
         K(this.b5, var1);
         this.h.K.k(this.p, var1);
         this.h.C.f(this.p, var1);
         this.p = var1;
         this.q = null;
         if (this.h.K.X() || this.h.C.Y()) {
            this.S();
         }

         mt.K("Config", "Saved {}", var2);
         return true;
      } catch (Exception var3) {
         mt.P("Config", "Failed to save profile " + var1, var3);
         return false;
      }
   }

   public boolean X(String var1) {
      try {
         this.R.W();
         Path var2 = this.c(var1);
         if (!Files.exists(var2)) {
            mt.T("Config", "Profile not found: {}", var2);
            return false;
         }

         JsonObject var3 = this.s(var1);
         String var4 = this.p;
         K(this.b5, var1);

         try {
            this.w(var3);
         } catch (RuntimeException var6) {
            K(this.b5, var4);
            throw var6;
         }

         this.p = var1;
         this.q = null;
         this.h.q(var1, System.currentTimeMillis());
         this.S();
         mt.K("Config", "Loaded {}", var2);
         return true;
      } catch (Exception var7) {
         mt.P("Config", "Failed to load profile " + var1 + "; existing values retained where possible", var7);
         return false;
      }
   }

   public void F(String var1) {
      try {
         this.h(var1);
      } catch (Exception var3) {
         mt.P("Config", "Failed to delete profile " + var1, var3);
      }
   }

   public void g() {
      mt.K("Config", "Configuration directory: {}", this.I);
      this.W();

      try {
         Files.createDirectories(this.v);
         if (Files.exists(this.b5)) {
            String var1 = Files.readString(this.b5).trim();

            try {
               this.c(var1);
               this.p = var1;
            } catch (IllegalArgumentException var3) {
               mt.T("Config", "Invalid active profile; using Default");
            }
         }

         Path var5 = this.c(this.p);
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS)) {
            if (!this.X(this.p)) {
               this.q = this.p;
               this.d();
            }
         } else {
            Path var2 = this.I.resolve("modules.json");
            if (Files.exists(var2)) {
               this.w(this.T(var2));
               mt.K("Config", "Migrated legacy {} to schema 2", var2);
               this.b();
            }
         }
      } catch (Exception var4) {
         this.q = this.p;
         mt.P("Config", "Configuration load failed; protecting original profile and retaining defaults", var4);
         this.d();
      }

      _v.e = this.h.L;
      _k.t = this.h.B;
      this.D(i());
   }

   private synchronized void d() {
      String var1 = this.q;
      if (var1 != null) {
         try {
            if (Files.isSymbolicLink(this.v)) {
               throw new IOException("Unsafe profile path");
            }

            Files.createDirectories(this.v);
            Path var2 = Files.createTempFile(this.v, "larp-recovery-", ".tmp");

            try {
               Files.writeString(var2, d(this.w()));
               long var3 = 1L;

               String var5;
               while (true) {
                  var5 = "Recovery " + var3;

                  try {
                     Files.createLink(this.c(var5), var2);
                     break;
                  } catch (FileAlreadyExistsException var11) {
                     var3++;
                  }
               }

               K(this.b5, var5);
               this.h.K.k(this.p, var5);
               this.h.C.f(this.p, var5);
               this.p = var5;
               this.q = null;
               this.S();
               mt.T("Config", "Rejected startup profile {} preserved; fallback saved as {}", var1, var5);
            } finally {
               Files.deleteIfExists(var2);
            }
         } catch (Exception var13) {
            mt.P("Config", "Recovery save failed; automatic saves will preserve rejected profile " + var1, var13);
         }
      }
   }

   void D(List<Module> var1) {
      if (this.h.K.D().get("active").getAsBoolean() && this.h.C.F().get("active").getAsBoolean()) {
         this.h.K = new kr();
         this.h.C = new _4();
         this.S();
         mt.T("Menu", "Conflicting cosmetic preset journals ignored; loaded profile retained");
      }

      this.h.K.D(var1, this.p, this::S);
      this.h.C.Q(var1, this.p, this::S);
   }

   private JsonObject T(Path var1) throws IOException {
      try {
         return JsonParser.parseString(Files.readString(var1)).getAsJsonObject();
      } catch (RuntimeException var4) {
         Path var3 = var1.resolveSibling(var1.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var1, var3);
         mt.P("Config", "Malformed JSON preserved at " + var3, var4);
         throw var4;
      }
   }

   public JsonObject s(String var1) throws IOException {
      this.R.W();
      Path var2 = this.c(var1);
      if (!Files.isSymbolicLink(var2) && !Files.isSymbolicLink(this.v) && Files.size(var2) <= 131072L) {
         return this.T(var2);
      } else {
         throw new IOException("Unsafe or oversized profile");
      }
   }

   public boolean Q(String var1) {
      return Files.exists(this.c(var1), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized void w(String var1, JsonObject var2, boolean var3, List<Module> var4) throws IOException {
      this.Z(var1, var2, var3, false);
   }

   private synchronized void Z(String var1, JsonObject var2, boolean var3, boolean var4) throws IOException {
      this.R.W();
      var2 = var4 ? p_.K(var2) : p_.c(var2);
      Path var5 = this.c(var1);
      if (!Files.isSymbolicLink(this.v) && !Files.isSymbolicLink(var5)) {
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS) && !var3) {
            throw new FileAlreadyExistsException("Choose a new profile name or explicitly confirm replacement.");
         }

         String var6 = d(var2);
         if (var3) {
            K(var5, var6);
         } else {
            Files.createDirectories(this.v);
            Path var7 = Files.createTempFile(this.v, "larp-import-", ".tmp");

            try {
               Files.writeString(var7, var6);
               Files.createLink(var5, var7);
            } finally {
               Files.deleteIfExists(var7);
            }
         }

         mt.U("Config", "Imported profile saved locally");
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   private JsonObject w() {
      return this.q(i());
   }

   private static String d(JsonObject var0) {
      p_.K(var0);
      String var1 = Z.toJson(var0);
      return var1.getBytes(StandardCharsets.UTF_8).length <= 131072 ? var1 : var0.toString();
   }

   JsonObject q(List<Module> var1) {
      return i(this.U, this.O(var1), this.M).getAsJsonObject();
   }

   private static JsonElement i(JsonElement var0, JsonElement var1, JsonElement var2) {
      if (var0 instanceof JsonObject var3 && var1 instanceof JsonObject var4) {
         JsonObject var5 = var3.deepCopy();

         for (Entry var7 : var4.entrySet()) {
            JsonElement var8 = var2 instanceof JsonObject var9 ? var9.get((String)var7.getKey()) : null;
            var5.add((String)var7.getKey(), i(var3.get((String)var7.getKey()), (JsonElement)var7.getValue(), var8));
         }

         return var5;
      } else {
         return var0 != null && var1.equals(var2) ? var0.deepCopy() : var1.deepCopy();
      }
   }

   private JsonObject O(List<Module> var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("schema", 2);
      var2.add("ui", cx.l());
      JsonObject var3 = new JsonObject();

      for (Module var5 : var1) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("enabled", var5.isEnabledPreference());
         var6.addProperty("keybind", var5.getKeyBind());
         JsonObject var7 = new JsonObject();

         for (df var9 : var5.getSettings()) {
            var7.add(var9.D(), Z.toJsonTree(var9.P()));
         }

         var6.add("settings", var7);
         this.h.K.u(var5, var6);
         this.h.C.L(var5, var6);
         if (this.i.containsKey(var5.getName())) {
            var6.add("legacySettings", this.i.get(var5.getName()).deepCopy());
         }

         var3.add(var5.getName(), var6);
      }

      var2.add("modules", var3);
      return var2;
   }

   private void w(JsonObject var1) {
      this.E(var1, i());
   }

   private static List<Module> i() {
      LarpClient var0 = LarpClient.F();
      return var0 == null ? List.of() : var0.x().C();
   }

   void E(JsonObject var1, List<Module> var2) {
      JsonObject var3 = p_.K(var1);
      he.y(var1, var3);
      var1 = var3;
      JsonObject var4 = var1.has("modules") ? var1.getAsJsonObject("modules") : var1;
      this.h.K.k(var2, this::S);
      this.h.C.F(var2, this::S);
      if (var1.has("ui")) {
         cx.P(var1.getAsJsonObject("ui"));
      } else {
         cx.U++;
      }

      if (!var1.has("schema")) {
         mt.K("Config", "Reading legacy module configuration");
      }

      p9.J(var4);
      JsonObject var5 = new JsonObject();

      for (Entry var7 : var4.entrySet()) {
         String var8 = var2.stream().map(Module::getName).filter(((String)var7.getKey())::equalsIgnoreCase).findFirst().orElse((String)var7.getKey());
         var5.add(var8, (JsonElement)var7.getValue());
      }

      var4 = var5;
      var1.add("modules", var4);
      this.i.clear();

      for (Module var17 : var2) {
         if (var17 instanceof HalloweenModule var19) {
            var19.t(var4.keySet().stream().anyMatch("Halloween"::equalsIgnoreCase));
         }
      }

      for (Entry var18 : var4.entrySet()) {
         String var20 = (String)var18.getKey();
         Module var9 = var2.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var20)).findFirst().orElse(null);
         if (var9 == null) {
            mt.T("Config", "Unknown module {} ignored", var20);
         } else {
            try {
               JsonObject var10 = ((JsonElement)var18.getValue()).getAsJsonObject();
               z3 var11 = z3.decode(var9, var10);
               var11.apply(var9);
               if (var11.archive() != null) {
                  this.i.put(var9.getName(), var11.archive());
               }
            } catch (RuntimeException var12) {
               mt.P("Config", "Invalid module entry " + var20, var12);
            }
         }
      }

      this.U = var1.deepCopy();
      this.M = this.O(var2);
      mt.K("Config", "Applied supported profile values; unfamiliar values retained for future saves");
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
