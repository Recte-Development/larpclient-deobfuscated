package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.HalloweenModule;
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

public final class mo {
   private static final Gson H = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
   private final Path y;
   private final Path w;
   private final Path E;
   private final fn r3 = new fn(mo::w);
   private String C = "Default";
   private String r5;
   private fs W = new fs();
   private final Map<String, JsonObject> P = new HashMap<>();
   private JsonObject J = new JsonObject();
   private JsonObject S = new JsonObject();
   public static boolean A;
   public static boolean M;
   public static boolean n;
   public static boolean X;
   public static int rG;
   public static int rT;
   public static int f;
   public static int v;
   public static int p;
   public static int r4;
   public static boolean l;
   public static int b;
   public static int s;
   public static int x;
   public static boolean r8;
   public static boolean i;
   public static int R;
   public static int T;
   public static boolean m;
   public static boolean g;
   public static boolean z;
   public static int B;
   public static int D;
   public static boolean Z;
   public static boolean r9;
   public static int Q;
   public static int F;
   public static boolean U;
   public static int O;
   public static int k;
   public static boolean rF;
   public static int rM;
   public static boolean V;
   public static int rV;
   public static boolean c;
   public static int e;
   public static boolean a;
   public static int Y;
   public static boolean q;
   public static int o;
   public static int rk;
   public static boolean N;
   public static boolean G;
   public static int r;
   public static boolean rI;
   public static boolean u;
   public static boolean j;
   public static boolean rK;
   public static int rp;
   public static boolean d;
   public static boolean t;
   public static boolean h;
   public static int rn;
   public static int L;
   public static int I;
   public static int K;

   public mo() {
      this(FabricLoader.getInstance().getConfigDir().resolve("larp-client"));
   }

   public mo(Path var1) {
      this.y = var1;
      this.w = var1.resolve("profiles");
      this.E = var1.resolve("active-profile.txt");
   }

   public fs E() {
      return this.W;
   }

   public boolean I(boolean var1) {
      boolean var2 = this.W.L;
      if (var2 == var1) {
         return true;
      }

      this.W.L = var1;
      if (!this.Y()) {
         this.W.L = var2;
         return false;
      }

      LarpClient var3 = LarpClient.y();
      if (var3 != null) {
         var3.s(var1);
      }

      return true;
   }

   public boolean q(boolean var1, List<Module> var2) {
      return var1 && this.W.e.S() && !this.W.e.B(false, var2, this.C, this::Y) ? false : this.W.X.V(var1, var2, this.C, this::Y);
   }

   public boolean f(boolean var1, List<Module> var2) {
      return var1 && this.W.X.r() && !this.W.X.V(false, var2, this.C, this::Y) ? false : this.W.e.B(var1, var2, this.C, this::Y);
   }

   public boolean s(boolean var1) {
      if (this.W.J == var1) {
         return true;
      } else {
         boolean var2 = this.W.J;
         this.W.J = var1;
         if (!this.Y()) {
            this.W.J = var2;
            return false;
         } else {
            jx.s = var1;
            return true;
         }
      }
   }

   public boolean I(jk.x var1) {
      Objects.requireNonNull(var1);
      jk.x var2 = this.W.M;
      if (var1 == var2) {
         return true;
      } else {
         this.W.M = var1;
         if (!this.Y()) {
            this.W.M = var2;
            return false;
         } else {
            lo.q = var1;
            return true;
         }
      }
   }

   public boolean c(Module var1, com.larpclient.module.l6<?> var2, int var3) {
      m5 var4 = this.W.B.j();
      this.W.B.O(var1, var2, var3);
      if (this.Y()) {
         com.larpclient.r4.V("Input", "Setting shortcut {}.{} set to code {}", var1.getName(), var2.k(), var3);
         return true;
      } else {
         this.W.B = var4;
         return false;
      }
   }

   public boolean Y() {
      try {
         this.r3.R(this.y.resolve("menu.json"), H.toJson(this.W.E()));
         return true;
      } catch (IOException var2) {
         com.larpclient.r4.r("Menu", "Could not save local interface preferences", var2);
         return false;
      }
   }

   public void a() {
      this.r3.n(this.y.resolve("menu.json"), H.toJson(this.W.E()));
   }

   public void A() {
      if (this.r5 != null) {
         this.o();
      } else {
         String var1;
         try {
            var1 = m(this.M());
         } catch (IllegalArgumentException var3) {
            com.larpclient.r4.r("Config", "Profile " + this.C + " was not saved; its previous saved copy is unchanged", var3);
            return;
         }

         this.r3.n(this.R(this.C), var1);
         this.r3.n(this.E, this.C);
         this.a();
      }
   }

   public String m() {
      return this.r3.X();
   }

   public void U() {
      try {
         this.r3.close();
      } catch (IOException var2) {
         com.larpclient.r4.r("Config", "Final preference flush failed", var2);
      }
   }

   private void c() {
      Path var1 = this.y.resolve("menu.json");
      if (Files.exists(var1)) {
         try {
            if (Files.isSymbolicLink(var1) || Files.size(var1) > 131072L) {
               throw new IOException("Unsafe menu preferences");
            }

            this.W = fs.K(this.H(var1));
         } catch (Exception var3) {
            com.larpclient.r4.r("Menu", "Local interface preferences unavailable; using safe defaults", var3);
         }
      }
   }

   public synchronized void c(String var1, String var2) throws IOException {
      this.r3.w();
      Path var3 = this.R(var1);
      Path var4 = this.R(var2);
      if (!var1.equals(var2)) {
         if (Files.isSymbolicLink(this.w) || Files.isSymbolicLink(var3) || Files.isSymbolicLink(var4)) {
            throw new IOException("Unsafe profile path");
         }

         if (Files.exists(var4)) {
            throw new FileAlreadyExistsException("That profile name already exists");
         }

         Files.createLink(var4, var3);

         try {
            if (this.C.equals(var1)) {
               w(this.E, var2);
            }
         } catch (IOException var7) {
            Files.deleteIfExists(var4);
            throw var7;
         }

         try {
            Files.delete(var3);
         } catch (IOException var6) {
            if (this.C.equals(var1)) {
               w(this.E, var1);
            }

            Files.deleteIfExists(var4);
            throw var6;
         }

         if (this.C.equals(var1)) {
            this.C = var2;
         }

         this.W.g(var1, var2);
         this.Y();
         com.larpclient.r4.V("Config", "Renamed profile {} to {}", var1, var2);
      }
   }

   public synchronized void P(String var1) throws IOException {
      this.r3.w();
      if (this.C.equals(var1)) {
         throw new IOException("Load another profile before deleting the current one");
      }

      Path var2 = this.R(var1);
      if (!Files.isSymbolicLink(this.w) && !Files.isSymbolicLink(var2)) {
         Files.delete(var2);
         this.W.m(var1);
         this.Y();
         com.larpclient.r4.V("Config", "Deleted profile {}", var1);
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   public synchronized void H(String var1, boolean var2) throws IOException {
      this.Q(var1, this.M(), var2, true);
   }

   public synchronized void i(String var1, String var2) throws IOException {
      this.Q(var2, this.K(var1), false, true);
   }

   public String o() {
      return this.C;
   }

   private Path R(String var1) {
      yj.S(var1);
      return this.w.resolve(var1 + ".json");
   }

   public List<String> I() {
      try {
         if (!Files.isDirectory(this.w)) {
            return List.of();
         }

         try (Stream var1 = Files.list(this.w)) {
            return var1.filter(var0 -> var0.toString().endsWith(".json") && Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS))
               .map(var0 -> var0.getFileName().toString().replaceFirst("\\.json$", ""))
               .filter(var0 -> {
                  try {
                     yj.S(var0);
                     return true;
                  } catch (IllegalArgumentException var2) {
                     return false;
                  }
               })
               .sorted()
               .toList();
         }
      } catch (IOException var6) {
         com.larpclient.r4.r("Config", "Cannot list profiles at " + this.w, var6);
         return List.of();
      }
   }

   public void j() {
      if (this.r5 != null) {
         this.o();
      } else {
         this.a(this.C);
      }
   }

   public boolean a(String var1) {
      try {
         this.r3.w();
         Path var2 = this.R(var1);
         w(var2, m(this.M()));
         w(this.E, var1);
         this.W.e.d(this.C, var1);
         this.W.X.W(this.C, var1);
         this.C = var1;
         this.r5 = null;
         if (this.W.e.S() || this.W.X.r()) {
            this.Y();
         }

         com.larpclient.r4.V("Config", "Saved {}", var2);
         return true;
      } catch (Exception var3) {
         com.larpclient.r4.r("Config", "Failed to save profile " + var1, var3);
         return false;
      }
   }

   public boolean V(String var1) {
      try {
         this.r3.w();
         Path var2 = this.R(var1);
         if (!Files.exists(var2)) {
            com.larpclient.r4.m("Config", "Profile not found: {}", var2);
            return false;
         }

         JsonObject var3 = this.K(var1);
         String var4 = this.C;
         w(this.E, var1);

         try {
            this.f(var3);
         } catch (RuntimeException var6) {
            w(this.E, var4);
            throw var6;
         }

         this.C = var1;
         this.r5 = null;
         this.W.B(var1, System.currentTimeMillis());
         this.Y();
         com.larpclient.r4.V("Config", "Loaded {}", var2);
         return true;
      } catch (Exception var7) {
         com.larpclient.r4.r("Config", "Failed to load profile " + var1 + "; existing values retained where possible", var7);
         return false;
      }
   }

   public void Z(String var1) {
      try {
         this.P(var1);
      } catch (Exception var3) {
         com.larpclient.r4.r("Config", "Failed to delete profile " + var1, var3);
      }
   }

   public void x() {
      com.larpclient.r4.V("Config", "Configuration directory: {}", this.y);
      this.c();

      try {
         Files.createDirectories(this.w);
         if (Files.exists(this.E)) {
            String var1 = Files.readString(this.E).trim();

            try {
               this.R(var1);
               this.C = var1;
            } catch (IllegalArgumentException var3) {
               com.larpclient.r4.m("Config", "Invalid active profile; using Default");
            }
         }

         Path var5 = this.R(this.C);
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS)) {
            if (!this.V(this.C)) {
               this.r5 = this.C;
               this.o();
            }
         } else {
            Path var2 = this.y.resolve("modules.json");
            if (Files.exists(var2)) {
               this.f(this.H(var2));
               com.larpclient.r4.V("Config", "Migrated legacy {} to schema 2", var2);
               this.j();
            }
         }
      } catch (Exception var4) {
         this.r5 = this.C;
         com.larpclient.r4.r("Config", "Configuration load failed; protecting original profile and retaining defaults", var4);
         this.o();
      }

      jx.s = this.W.J;
      lo.q = this.W.M;
      this.X(R());
   }

   private synchronized void o() {
      String var1 = this.r5;
      if (var1 != null) {
         try {
            if (Files.isSymbolicLink(this.w)) {
               throw new IOException("Unsafe profile path");
            }

            Files.createDirectories(this.w);
            Path var2 = Files.createTempFile(this.w, "larp-recovery-", ".tmp");

            try {
               Files.writeString(var2, m(this.M()));
               long var3 = 1L;

               String var5;
               while (true) {
                  var5 = "Recovery " + var3;

                  try {
                     Files.createLink(this.R(var5), var2);
                     break;
                  } catch (FileAlreadyExistsException var11) {
                     var3++;
                  }
               }

               w(this.E, var5);
               this.W.e.d(this.C, var5);
               this.W.X.W(this.C, var5);
               this.C = var5;
               this.r5 = null;
               this.Y();
               com.larpclient.r4.m("Config", "Rejected startup profile {} preserved; fallback saved as {}", var1, var5);
            } finally {
               Files.deleteIfExists(var2);
            }
         } catch (Exception var13) {
            com.larpclient.r4.r("Config", "Recovery save failed; automatic saves will preserve rejected profile " + var1, var13);
         }
      }
   }

   void X(List<Module> var1) {
      if (this.W.e.z().get("active").getAsBoolean() && this.W.X.f().get("active").getAsBoolean()) {
         this.W.e = new rq();
         this.W.X = new o9();
         this.Y();
         com.larpclient.r4.m("Menu", "Conflicting cosmetic preset journals ignored; loaded profile retained");
      }

      this.W.e.L(var1, this.C, this::Y);
      this.W.X.P(var1, this.C, this::Y);
   }

   private JsonObject H(Path var1) throws IOException {
      try {
         return JsonParser.parseString(Files.readString(var1)).getAsJsonObject();
      } catch (RuntimeException var4) {
         Path var3 = var1.resolveSibling(var1.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var1, var3);
         com.larpclient.r4.r("Config", "Malformed JSON preserved at " + var3, var4);
         throw var4;
      }
   }

   public JsonObject K(String var1) throws IOException {
      this.r3.w();
      Path var2 = this.R(var1);
      if (!Files.isSymbolicLink(var2) && !Files.isSymbolicLink(this.w) && Files.size(var2) <= 131072L) {
         return this.H(var2);
      } else {
         throw new IOException("Unsafe or oversized profile");
      }
   }

   public boolean p(String var1) {
      return Files.exists(this.R(var1), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized void O(String var1, JsonObject var2, boolean var3, List<Module> var4) throws IOException {
      this.Q(var1, var2, var3, false);
   }

   private synchronized void Q(String var1, JsonObject var2, boolean var3, boolean var4) throws IOException {
      this.r3.w();
      var2 = var4 ? yj.F(var2) : yj.n(var2);
      Path var5 = this.R(var1);
      if (!Files.isSymbolicLink(this.w) && !Files.isSymbolicLink(var5)) {
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS) && !var3) {
            throw new FileAlreadyExistsException("Choose a new profile name or explicitly confirm replacement.");
         }

         String var6 = m(var2);
         if (var3) {
            w(var5, var6);
         } else {
            Files.createDirectories(this.w);
            Path var7 = Files.createTempFile(this.w, "larp-import-", ".tmp");

            try {
               Files.writeString(var7, var6);
               Files.createLink(var5, var7);
            } finally {
               Files.deleteIfExists(var7);
            }
         }

         com.larpclient.r4.g("Config", "Imported profile saved locally");
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   private JsonObject M() {
      return this.C(R());
   }

   private static String m(JsonObject var0) {
      yj.F(var0);
      String var1 = H.toJson(var0);
      return var1.getBytes(StandardCharsets.UTF_8).length <= 131072 ? var1 : var0.toString();
   }

   JsonObject C(List<Module> var1) {
      return H(this.J, this.f(var1), this.S).getAsJsonObject();
   }

   private static JsonElement H(JsonElement var0, JsonElement var1, JsonElement var2) {
      if (var0 instanceof JsonObject var3 && var1 instanceof JsonObject var4) {
         JsonObject var5 = var3.deepCopy();

         for (Entry var7 : var4.entrySet()) {
            JsonElement var8 = var2 instanceof JsonObject var9 ? var9.get((String)var7.getKey()) : null;
            var5.add((String)var7.getKey(), H(var3.get((String)var7.getKey()), (JsonElement)var7.getValue(), var8));
         }

         return var5;
      } else {
         return var0 != null && var1.equals(var2) ? var0.deepCopy() : var1.deepCopy();
      }
   }

   private JsonObject f(List<Module> var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("schema", 2);
      var2.add("ui", fc.e());
      JsonObject var3 = new JsonObject();

      for (Module var5 : var1) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("enabled", var5.isEnabledPreference());
         var6.addProperty("keybind", var5.getKeyBind());
         JsonObject var7 = new JsonObject();

         for (com.larpclient.module.l6 var9 : var5.getSettings()) {
            var7.add(var9.k(), H.toJsonTree(var9.getValue()));
         }

         var6.add("settings", var7);
         this.W.e.E(var5, var6);
         this.W.X.p(var5, var6);
         if (this.P.containsKey(var5.getName())) {
            var6.add("legacySettings", this.P.get(var5.getName()).deepCopy());
         }

         var3.add(var5.getName(), var6);
      }

      var2.add("modules", var3);
      return var2;
   }

   private void f(JsonObject var1) {
      this.H(var1, R());
   }

   private static List<Module> R() {
      LarpClient var0 = LarpClient.y();
      return var0 == null ? List.of() : var0.e().X();
   }

   void H(JsonObject var1, List<Module> var2) {
      JsonObject var3 = yj.F(var1);
      com.larpclient.w.j(var1, var3);
      var1 = var3;
      JsonObject var4 = var1.has("modules") ? var1.getAsJsonObject("modules") : var1;
      this.W.e.i(var2, this::Y);
      this.W.X.U(var2, this::Y);
      if (var1.has("ui")) {
         fc.H(var1.getAsJsonObject("ui"));
      } else {
         fc.S++;
      }

      if (!var1.has("schema")) {
         com.larpclient.r4.V("Config", "Reading legacy module configuration");
      }

      mt.q(var4);
      JsonObject var5 = new JsonObject();

      for (Entry var7 : var4.entrySet()) {
         String var8 = var2.stream().map(Module::getName).filter(((String)var7.getKey())::equalsIgnoreCase).findFirst().orElse((String)var7.getKey());
         var5.add(var8, (JsonElement)var7.getValue());
      }

      var4 = var5;
      var1.add("modules", var4);
      this.P.clear();

      for (Module var17 : var2) {
         if (var17 instanceof HalloweenModule var19) {
            var19.x(var4.keySet().stream().anyMatch("Halloween"::equalsIgnoreCase));
         }
      }

      for (Entry var18 : var4.entrySet()) {
         String var20 = (String)var18.getKey();
         Module var9 = var2.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var20)).findFirst().orElse(null);
         if (var9 == null) {
            com.larpclient.r4.m("Config", "Unknown module {} ignored", var20);
         } else {
            try {
               JsonObject var10 = ((JsonElement)var18.getValue()).getAsJsonObject();
               _y var11 = _y.decode(var9, var10);
               var11.apply(var9);
               if (var11.archive() != null) {
                  this.P.put(var9.getName(), var11.archive());
               }
            } catch (RuntimeException var12) {
               com.larpclient.r4.r("Config", "Invalid module entry " + var20, var12);
            }
         }
      }

      this.J = var1.deepCopy();
      this.S = this.f(var2);
      com.larpclient.r4.V("Config", "Applied supported profile values; unfamiliar values retained for future saves");
   }

   private static void w(Path var0, String var1) throws IOException {
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
