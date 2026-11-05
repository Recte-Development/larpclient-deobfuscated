package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

public final class fs {
   public f7 A = f7.o;
   public boolean o = (boolean)1;
   public boolean h = (boolean)1;
   public String G = "";
   public String m = "";
   public boolean L = (boolean)1;
   public xb.x I = xb.x.r;
   public xb.s S = xb.s.O;
   public double P = 0.33;
   public boolean W = (boolean)1;
   public boolean NB = (boolean)1;
   public boolean Nw;
   public boolean b;
   public String s = "Fade";
   public static final List<String> K;
   public String V = "Reverse";
   public int U = 280;
   public double w = 0.67;
   public static final double N = 0.5;
   public static final double x = 2.0;
   public double Y = 1.0;
   public static final List<String> g;
   public String z = "None";
   public static final double i = 0.9;
   public double E = 0.42;
   public static final int Q = 4;
   public int Na = 2;
   public static final List<String> D;
   public String C = "Window";
   public static final List<String> f;
   public static final List<String> T;
   public static final List<String> R;
   public static final List<String> y;
   public static final double l = 2.0;
   public String F = "Filled";
   public String j = "Glow";
   public String H = "Pill";
   public String v = "Pill";
   public double q = 1.0;
   public boolean n = (boolean)1;
   public boolean Z = (boolean)1;
   public boolean u = (boolean)1;
   private static final int d = 2;
   public boolean J = (boolean)1;
   public jk.x M = jk.x.V;
   public String k = "World";
   public rq e = new rq();
   public o9 X = new o9();
   public m5 B = new m5();
   public j8 p = new j8();
   private final Set<String> a = new LinkedHashSet<>();
   private final Set<String> Nf = new LinkedHashSet<>();
   private final Set<String> O = new LinkedHashSet<>();
   public boolean t;
   private final Map<String, Long> r = new LinkedHashMap<>();
   public static boolean c;
   private static final String[] ab;

   public rv.t e() {
      return new rv.t(this.F, this.j, this.H, this.v, this.q, this.n, this.Z, this.u);
   }

   public boolean K(String var1) {
      return this.O.contains(var1);
   }

   public void R(String var1, boolean var2) {
      if (var1 != null && var1.matches("[a-z][a-z0-9_-]{0,63}")) {
         if (var2 && this.O.size() < 128) {
            this.O.add(var1);
         } else if (!var2) {
            this.O.remove(var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid HUD identifier");
      }
   }

   public boolean s(String var1) {
      return this.Nf.contains(var1) || "CustomSky".equals(var1) && this.Nf.contains("Sky Shader");
   }

   public void V(String var1, boolean var2) {
      if (var1 != null && var1.matches("[A-Za-z0-9 _-]{1,128}") && !var1.isBlank()) {
         if (var2) {
            if (this.Nf.size() >= 1000 && !this.Nf.contains(var1)) {
               return;
            }

            this.Nf.add(var1);
         } else {
            this.Nf.remove(var1);
            if (var1.equals("CustomSky")) {
               this.Nf.remove("Sky Shader");
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   public boolean V(String var1) {
      return this.a.contains(var1);
   }

   public void d(String var1, boolean var2) {
      yj.S(var1);
      if (var2) {
         this.a.add(var1);
      } else {
         this.a.remove(var1);
      }
   }

   public long F(String var1) {
      return this.r.getOrDefault(var1, 0L);
   }

   public void B(String var1, long var2) {
      yj.S(var1);
      if (var2 > 0L) {
         this.r.put(var1, var2);
      }
   }

   public void m(String var1) {
      this.a.remove(var1);
      this.r.remove(var1);
   }

   public void g(String var1, String var2) {
      yj.S(var2);
      this.e.d(var1, var2);
      this.X.W(var1, var2);
      if (this.a.remove(var1)) {
         this.a.add(var2);
      }

      Long var3 = this.r.remove(var1);
      if (var3 != null) {
         this.r.put(var2, var3);
      }
   }

   public List<String> P(List<String> var1, String var2, String var3) {
      Stream var4 = var1.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var2.toLowerCase(Locale.ROOT)));
      if (var3.equals("Favorites")) {
         var4 = var4.filter(this::V);
      }

      if (var3.equals("Recent")) {
         var4 = var4.filter(var1x -> this.F(var1x) > 0L).sorted(Comparator.comparingLong(this::F).reversed().thenComparing(Comparator.naturalOrder()));
      }

      return var4.toList();
   }

   public JsonObject E() {
      JsonObject var2 = new JsonObject();
      boolean var10000 = c;
      String[] var7 = ab;
      var2.addProperty(var7[34], this.A.e);
      var2.addProperty(var7[33], this.o);
      var2.addProperty(var7[6], this.h);
      var2.addProperty(var7[24], this.G);
      var2.addProperty(var7[7], this.m);
      var2.addProperty(var7[38], this.I.name());
      var2.addProperty(var7[41], this.S.name());
      var2.addProperty(var7[10], this.P);
      var2.addProperty(var7[1], this.W);
      var2.addProperty(var7[21], this.NB);
      var2.addProperty(var7[11], this.L);
      var2.addProperty(var7[15], this.J);
      var2.addProperty(var7[18], this.M.name());
      var2.addProperty(var7[29], this.k);
      var2.addProperty(var7[37], this.Nw);
      var2.addProperty(var7[19], this.b);
      var2.addProperty(var7[12], this.s);
      var2.addProperty(var7[42], this.V);
      var2.addProperty(var7[36], this.U);
      var2.addProperty(var7[20], this.w);
      var2.addProperty(var7[39], this.Y);
      var2.addProperty(var7[0], this.z);
      var2.addProperty(var7[30], 2);
      boolean var1 = var10000;
      var2.addProperty(var7[17], this.E);
      var2.addProperty(var7[26], this.Na);
      var2.addProperty(var7[35], this.C);
      var2.addProperty(var7[8], this.F);
      var2.addProperty(var7[3], this.j);
      var2.addProperty(var7[22], this.H);
      var2.addProperty(var7[16], this.v);
      var2.addProperty(var7[31], this.q);
      var2.addProperty(var7[4], this.n);
      var2.addProperty(var7[14], this.Z);
      var2.addProperty(var7[28], this.u);
      JsonArray var3 = new JsonArray();
      this.a.forEach(var3::add);
      var2.add(var7[23], var3);
      JsonObject var4 = new JsonObject();
      this.r.forEach(var4::addProperty);
      var2.add(var7[27], var4);
      JsonArray var5 = new JsonArray();
      this.Nf.forEach(var5::add);
      var2.add(var7[9], var5);
      var2.addProperty(var7[13], this.t);
      JsonArray var6 = new JsonArray();
      this.O.forEach(var6::add);
      var2.add(var7[40], var6);
      var2.add(var7[5], this.e.z());
      var2.add(var7[32], this.X.f());
      var2.add(var7[2], this.B.g());
      var2.add(var7[25], this.p.S());
      if (var1) {
         mo.A = (boolean)(mo.A ? 0 : 1);
      }

      return var2;
   }

   public static fs K(JsonObject var0) {
      fs var1 = new fs();

      try {
         if (var0.has("updateAutoCheck")) {
            var1.h = yj.V(var0.get("updateAutoCheck"));
         }
      } catch (RuntimeException var45) {
      }

      try {
         String var2 = yj.y(var0, "updateSkippedRelease", 128);
         if (var2.isEmpty() || var2.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.G = var2;
         }
      } catch (RuntimeException var52) {
      }

      try {
         String var53 = yj.y(var0, "updateSeenReceipt", 257);
         if (var53.isEmpty() || var53.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}:[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.m = var53;
         }
      } catch (RuntimeException var51) {
      }

      try {
         if (var0.has("discordPresence")) {
            var1.L = yj.V(var0.get("discordPresence"));
         }
      } catch (RuntimeException var44) {
      }

      try {
         String var54 = yj.y(var0, "skySource", 16);
         if (Set.of("World", "Shader").contains(var54)) {
            var1.k = var54;
         }
      } catch (RuntimeException var43) {
      }

      try {
         var1.I = xb.x.valueOf(yj.y(var0, "menuSeason", 16));
      } catch (RuntimeException var42) {
      }

      try {
         var1.S = xb.s.valueOf(yj.y(var0, "menuQuality", 16));
      } catch (RuntimeException var41) {
      }

      try {
         yj.b(var0.get("menuTime"), 0.0, 1.0, false);
         var1.P = var0.get("menuTime").getAsDouble();
      } catch (RuntimeException var40) {
      }

      try {
         if (var0.has("cinematicMenu")) {
            var1.W = yj.V(var0.get("cinematicMenu"));
         }
      } catch (RuntimeException var39) {
      }

      try {
         if (var0.has("customMenu")) {
            var1.NB = yj.V(var0.get("customMenu"));
         }
      } catch (RuntimeException var38) {
      }

      try {
         if (var0.has("clickGuiGradient")) {
            var1.b = yj.V(var0.get("clickGuiGradient"));
         }
      } catch (RuntimeException var37) {
      }

      try {
         if (var0.has("clickGuiLight")) {
            var1.Nw = yj.V(var0.get("clickGuiLight"));
         }
      } catch (RuntimeException var36) {
      }

      try {
         String var55 = yj.y(var0, "clickGuiAnimation", 16);
         if (fm.N.contains(var55)) {
            var1.s = var55;
         }
      } catch (RuntimeException var35) {
      }

      try {
         String var56 = yj.y(var0, "clickGuiCloseAnimation", 16);
         if (K.contains(var56)) {
            var1.V = var56;
         }
      } catch (RuntimeException var34) {
      }

      try {
         yj.b(var0.get("clickGuiDuration"), 100.0, 1000.0, true);
         var1.U = var0.get("clickGuiDuration").getAsInt();
      } catch (RuntimeException var33) {
      }

      try {
         yj.b(var0.get("clickGuiOpacity"), 0.0, 1.0, false);
         var1.w = var0.get("clickGuiOpacity").getAsDouble();
      } catch (RuntimeException var32) {
      }

      try {
         if (var0.has("clickGuiScale")) {
            yj.b(var0.get("clickGuiScale"), 0.5, 2.0, false);
            var1.Y = var0.get("clickGuiScale").getAsDouble();
         }
      } catch (RuntimeException var31) {
      }

      try {
         String var57 = yj.y(var0, "clickGuiBackdrop", 16);
         byte var3 = 0;

         try {
            var3 = (byte)(var0.get("clickGuiBackdropRevision").getAsInt() >= 2 ? 1 : 0);
         } catch (RuntimeException var30) {
         }

         if (var57.equals("Aurora") && var3 == 0) {
            r4.g("GUI", "Moved the former default Aurora backdrop to None");
         } else if (g.contains(var57)) {
            var1.z = var57;
         }
      } catch (RuntimeException var50) {
      }

      try {
         if (var0.has("clickGuiDim")) {
            yj.b(var0.get("clickGuiDim"), 0.0, 0.9, false);
            var1.E = var0.get("clickGuiDim").getAsDouble();
         }
      } catch (RuntimeException var29) {
      }

      try {
         if (var0.has("clickGuiBlur")) {
            yj.b(var0.get("clickGuiBlur"), 0.0, 4.0, true);
            var1.Na = var0.get("clickGuiBlur").getAsInt();
         }
      } catch (RuntimeException var28) {
      }

      try {
         String var58 = yj.y(var0, "clickGuiBlurArea", 16);
         if (D.contains(var58)) {
            var1.C = var58;
         }
      } catch (RuntimeException var27) {
      }

      try {
         String var59 = yj.y(var0, "clickGuiCardStyle", 16);
         if (f.contains(var59)) {
            var1.F = var59;
         }
      } catch (RuntimeException var26) {
      }

      try {
         String var60 = yj.y(var0, "clickGuiEnabledStyle", 16);
         if (T.contains(var60)) {
            var1.j = var60;
         }
      } catch (RuntimeException var25) {
      }

      try {
         String var61 = yj.y(var0, "clickGuiSwitchStyle", 16);
         if (R.contains(var61)) {
            var1.H = var61;
         }
      } catch (RuntimeException var24) {
      }

      try {
         String var62 = yj.y(var0, "clickGuiSliderStyle", 16);
         if (y.contains(var62)) {
            var1.v = var62;
         }
      } catch (RuntimeException var23) {
      }

      try {
         if (var0.has("clickGuiRounding")) {
            yj.b(var0.get("clickGuiRounding"), 0.0, 2.0, false);
            var1.q = var0.get("clickGuiRounding").getAsDouble();
         }
      } catch (RuntimeException var22) {
      }

      try {
         if (var0.has("clickGuiAccentGlow")) {
            var1.n = yj.V(var0.get("clickGuiAccentGlow"));
         }
      } catch (RuntimeException var21) {
      }

      try {
         if (var0.has("clickGuiRipple")) {
            var1.Z = yj.V(var0.get("clickGuiRipple"));
         }
      } catch (RuntimeException var20) {
      }

      try {
         if (var0.has("clickGuiHoverLift")) {
            var1.u = yj.V(var0.get("clickGuiHoverLift"));
         }
      } catch (RuntimeException var19) {
      }

      try {
         var1.A = f7.x(yj.y(var0, "background", 32));
      } catch (RuntimeException var18) {
      }

      try {
         if (var0.has("softParticles")) {
            var1.J = yj.V(var0.get("softParticles"));
         }
      } catch (RuntimeException var17) {
      }

      try {
         var1.M = jk.x.valueOf(yj.y(var0, "effectsQuality", 16));
      } catch (RuntimeException var16) {
      }

      try {
         var1.o = yj.V(var0.get("motion"));
      } catch (RuntimeException var15) {
      }

      try {
         JsonArray var63 = var0.getAsJsonArray("favorites");
         if (var63.size() <= 1000) {
            for (JsonElement var4 : var63) {
               try {
                  var1.d(yj.S(var4.getAsString()), true);
               } catch (RuntimeException var14) {
               }
            }
         }
      } catch (RuntimeException var49) {
      }

      try {
         JsonObject var64 = var0.getAsJsonObject("recent");
         if (var64.size() <= 1000) {
            for (Entry var72 : var64.entrySet()) {
               try {
                  yj.b((JsonElement)var72.getValue(), 1.0, 9.223372E18F, true);
                  var1.B((String)var72.getKey(), ((JsonElement)var72.getValue()).getAsLong());
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var48) {
      }

      try {
         var1.t = yj.V(var0.get("moduleFavoritesOpen"));
      } catch (RuntimeException var12) {
      }

      try {
         JsonArray var65 = var0.getAsJsonArray("hudLocks");
         if (var65.size() <= 128) {
            for (JsonElement var73 : var65) {
               try {
                  if (var73.isJsonPrimitive() && var73.getAsJsonPrimitive().isString()) {
                     var1.R(var73.getAsString(), true);
                  }
               } catch (RuntimeException var11) {
               }
            }
         }
      } catch (RuntimeException var47) {
      }

      try {
         JsonArray var66 = var0.getAsJsonArray("moduleFavorites");
         if (var66.size() <= 1000) {
            for (JsonElement var74 : var66) {
               try {
                  if (var74.isJsonPrimitive() && var74.getAsJsonPrimitive().isString()) {
                     var1.V(var74.getAsString(), true);
                  }
               } catch (RuntimeException var10) {
               }
            }
         }
      } catch (RuntimeException var46) {
      }

      try {
         if (var0.has("settingBinds")) {
            var1.B = m5.D(var0.get("settingBinds"));
         }
      } catch (RuntimeException var9) {
         r4.m("Input", "Invalid setting shortcuts ignored: {}", var9.toString());
      }

      try {
         if (var0.has("legacyClickGui")) {
            var1.p = j8.F(var0.getAsJsonObject("legacyClickGui"));
         }
      } catch (RuntimeException var8) {
         r4.m("GUI", "Invalid legacy ClickGUI preference ignored: {}", var8.toString());
      }

      try {
         if (var0.has("israelMode")) {
            var1.e = rq.Q(var0.getAsJsonObject("israelMode"));
         }
      } catch (RuntimeException var7) {
         r4.m("Israel mode", "Invalid local preference ignored: {}", var7.toString());
      }

      String var67 = var0.has("larpPreset") ? "larpPreset" : "moonMode";

      try {
         if (var0.has(var67)) {
            var1.X = o9.t(var0.getAsJsonObject(var67));
            if (!var0.has("larpPreset")) {
               r4.g("Larp Preset", "Loaded legacy cosmetic preset journal; next preference save uses larpPreset");
            }
         }
      } catch (RuntimeException var6) {
         r4.m("Larp Preset", "Invalid local preference ignored: {}", var6.toString());
      }

      return var1;
   }

   static {
      String[] var5 = new String[43];
      int var3 = 0;
      String var2 = "T\u00045#{<\u0016^*=#{\u001f\u0011X\u0018\rT\u00012%}\u001a\u0017^\u000b\u0011%~\u000e\fD\r(4y\u0015\u0004u\u00012$c\u0014T\u00045#{<\u0016^-2!r\u0017\u0006S;(9|\u001e\u0012T\u00045#{<\u0016^)?#u\u0015\u0017p\u000437\n^\u001b.!u\u0017.X\f9\u000fB\u00188!d\u001e\"B\u001c3\u0003x\u001e\u0000\\\u0011B\u00188!d\u001e0R\r2\u0012u\u0018\u0006^\u0018(\u0011T\u00045#{<\u0016^+=2t(\u0017N\u00049\u000fZ\u000785|\u001e%V\u001e32y\u000f\u0006D\bZ\r25D\u0012\u000eR\u000fS\u0001/#\u007f\t\u0007g\u001a93u\u0015\u0000R\u0011T\u00045#{<\u0016^)2)}\u001a\u0017^\u00072\u0013Z\u000785|\u001e%V\u001e32y\u000f\u0006D',%~\u000eT\u00045#{<\u0016^:50`\u0017\u0006\rD\u0007:4@\u001a\u0011C\u0001?,u\b\u0013T\u00045#{<\u0016^;0)t\u001e\u0011d\u001c%,u\u000bT\u00045#{<\u0016^,5-\u000eR\u000e:%s\u000f\u0010f\u001d=,y\u000f\u001a\u0010T\u00045#{<\u0016^/.!t\u0012\u0006Y\u001c\u000fT\u00045#{<\u0016^',!s\u0012\u0017N\nT\u001d/4\u007f\u0016.R\u0006)\u0013T\u00045#{<\u0016^;+)d\u0018\u000bd\u001c%,u\tQ\t*/b\u0012\u0017R\u001b\u0014B\u00188!d\u001e0\\\u0001,0u\u001f1R\u00049!c\u001e\u000e[\r;!s\u0002 [\u0001?+W\u000e\n\fT\u00045#{<\u0016^*05b\u0006E\r?%~\u000f\u0011T\u00045#{<\u0016^ 36u\t/^\u000e(\tD\u0003%\u0013\u007f\u000e\u0011T\r\u0018T\u00045#{<\u0016^*=#{\u001f\u0011X\u0018\u000e%f\u0012\u0010^\u00072\u0010T\u00045#{<\u0016^:35~\u001f\nY\u000f\n[\t.0@\t\u0006D\r(\u0006Z\u0007()\u007f\u0015\nU\t?+w\t\fB\u00068\u0010T\u00045#{<\u0016^*05b:\u0011R\t\u0010T\u00045#{<\u0016^,)2q\u000f\nX\u0006\rT\u00045#{<\u0016^$5'x\u000f\nZ\r25C\u001e\u0002D\u00072\rT\u00045#{<\u0016^;?!|\u001e\b_\u001d8\f\u007f\u0018\bD";
      int var4 = "T\u00045#{<\u0016^*=#{\u001f\u0011X\u0018\rT\u00012%}\u001a\u0017^\u000b\u0011%~\u000e\fD\r(4y\u0015\u0004u\u00012$c\u0014T\u00045#{<\u0016^-2!r\u0017\u0006S;(9|\u001e\u0012T\u00045#{<\u0016^)?#u\u0015\u0017p\u000437\n^\u001b.!u\u0017.X\f9\u000fB\u00188!d\u001e\"B\u001c3\u0003x\u001e\u0000\\\u0011B\u00188!d\u001e0R\r2\u0012u\u0018\u0006^\u0018(\u0011T\u00045#{<\u0016^+=2t(\u0017N\u00049\u000fZ\u000785|\u001e%V\u001e32y\u000f\u0006D\bZ\r25D\u0012\u000eR\u000fS\u0001/#\u007f\t\u0007g\u001a93u\u0015\u0000R\u0011T\u00045#{<\u0016^)2)}\u001a\u0017^\u00072\u0013Z\u000785|\u001e%V\u001e32y\u000f\u0006D',%~\u000eT\u00045#{<\u0016^:50`\u0017\u0006\rD\u0007:4@\u001a\u0011C\u0001?,u\b\u0013T\u00045#{<\u0016^;0)t\u001e\u0011d\u001c%,u\u000bT\u00045#{<\u0016^,5-\u000eR\u000e:%s\u000f\u0010f\u001d=,y\u000f\u001a\u0010T\u00045#{<\u0016^/.!t\u0012\u0006Y\u001c\u000fT\u00045#{<\u0016^',!s\u0012\u0017N\nT\u001d/4\u007f\u0016.R\u0006)\u0013T\u00045#{<\u0016^;+)d\u0018\u000bd\u001c%,u\tQ\t*/b\u0012\u0017R\u001b\u0014B\u00188!d\u001e0\\\u0001,0u\u001f1R\u00049!c\u001e\u000e[\r;!s\u0002 [\u0001?+W\u000e\n\fT\u00045#{<\u0016^*05b\u0006E\r?%~\u000f\u0011T\u00045#{<\u0016^ 36u\t/^\u000e(\tD\u0003%\u0013\u007f\u000e\u0011T\r\u0018T\u00045#{<\u0016^*=#{\u001f\u0011X\u0018\u000e%f\u0012\u0010^\u00072\u0010T\u00045#{<\u0016^:35~\u001f\nY\u000f\n[\t.0@\t\u0006D\r(\u0006Z\u0007()\u007f\u0015\nU\t?+w\t\fB\u00068\u0010T\u00045#{<\u0016^*05b:\u0011R\t\u0010T\u00045#{<\u0016^,)2q\u000f\nX\u0006\rT\u00045#{<\u0016^$5'x\u000f\nZ\r25C\u001e\u0002D\u00072\rT\u00045#{<\u0016^;?!|\u001e\b_\u001d8\f\u007f\u0018\bD"
         .length();
      char var1 = 16;
      int var7 = -1;

      label24:
      while (true) {
         String var10000 = var2.substring(++var7, var7 + var1);
         byte var10001 = -1;

         while (true) {
            String var12 = a(a(var10000));
            switch (var10001) {
               case 0:
                  var5[var3++] = var12;
                  if ((var7 += var1) >= var4) {
                     ab = var5;
                     K = List.of("Reverse", "Shatter", "Crumble", "Slice", "Implode", "CRT");
                     g = List.of("Aurora", "Nebula", "Waves", "Gradient", "Dim", "None");
                     D = List.of("Window", "Everywhere");
                     f = List.of("Filled", "Outline", "Flat");
                     T = List.of("Glow", "Tint", "Text", "None");
                     R = List.of("Pill", "Square", "Minimal");
                     y = List.of("Pill", "Minimal", "Classic");
                     return;
                  }

                  var1 = var2.charAt(var7);
                  break;
               default:
                  var5[var3++] = var12;
                  if ((var7 += var1) < var4) {
                     var1 = var2.charAt(var7);
                     continue label24;
                  }

                  var2 = "Z\r25A\u000e\u0002[\u0001(9\u0016T\u00045#{<\u0016^+0/c\u001e\"Y\u00011!d\u0012\fY";
                  var4 = "Z\r25A\u000e\u0002[\u0001(9\u0016T\u00045#{<\u0016^+0/c\u001e\"Y\u00011!d\u0012\fY".length();
                  var1 = 11;
                  var7 = -1;
            }

            var10000 = var2.substring(++var7, var7 + var1);
            var10001 = 0;
         }
      }
   }
}
