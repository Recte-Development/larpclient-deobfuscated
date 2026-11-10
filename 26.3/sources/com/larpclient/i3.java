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

public final class i3 {
   public pz A = pz.Z;
   public boolean w = (boolean)1;
   public boolean N = (boolean)1;
   public String B = "";
   public String C = "";
   public boolean f = (boolean)1;
   public boolean G;
   public int D = 47;
   public ng.i V = ng.i.j;
   public ng.c Jw = ng.c.f;
   public double O = 0.33;
   public boolean I = (boolean)1;
   public boolean M = (boolean)1;
   public boolean z;
   public boolean b;
   public String p = "Fade";
   public static final List<String> y;
   public String q = "Reverse";
   public int n = 280;
   public double j = 0.67;
   public static final double Jz = 0.5;
   public static final double T = 2.0;
   public double U = 1.0;
   public static final List<String> J0;
   public String Ju = "None";
   public static final double e = 0.9;
   public double Z = 0.42;
   public static final int m = 4;
   public int F = 2;
   public static final List<String> X;
   public String i = "Window";
   public static final List<String> g;
   public static final List<String> o;
   public static final List<String> H;
   public static final List<String> JO;
   public static final double l = 2.0;
   public String R = "Filled";
   public String J = "Glow";
   public String S = "Pill";
   public String a = "Pill";
   public double K = 1.0;
   public boolean JH = (boolean)1;
   public boolean d = (boolean)1;
   public boolean E = (boolean)1;
   private static final int x = 2;
   public boolean t = (boolean)1;
   public nl.q W = nl.q.C;
   public String P = "World";
   public hi Y = new hi();
   public r c = new r();
   public b1 Q = new b1();
   public vd u = new vd();
   private final Set<String> h = new LinkedHashSet<>();
   private final Set<String> r = new LinkedHashSet<>();
   private final Set<String> L = new LinkedHashSet<>();
   public boolean v;
   private final Map<String, Long> s = new LinkedHashMap<>();
   public static boolean k;
   private static final String[] ab;

   public c.j m() {
      return new c.j(this.R, this.J, this.S, this.a, this.K, this.JH, this.d, this.E);
   }

   public boolean o(String var1) {
      return this.L.contains(var1);
   }

   public void p(String var1, boolean var2) {
      if (var1 != null && var1.matches("[a-z][a-z0-9_-]{0,63}")) {
         if (var2 && this.L.size() < 128) {
            this.L.add(var1);
         } else if (!var2) {
            this.L.remove(var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid HUD identifier");
      }
   }

   public boolean m(String var1) {
      return this.r.contains(var1) || "CustomSky".equals(var1) && this.r.contains("Sky Shader");
   }

   public void h(String var1, boolean var2) {
      if (var1 != null && var1.matches("[A-Za-z0-9 _-]{1,128}") && !var1.isBlank()) {
         if (var2) {
            if (this.r.size() >= 1000 && !this.r.contains(var1)) {
               return;
            }

            this.r.add(var1);
         } else {
            this.r.remove(var1);
            if (var1.equals("CustomSky")) {
               this.r.remove("Sky Shader");
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   public boolean L(String var1) {
      return this.h.contains(var1);
   }

   public void f(String var1, boolean var2) {
      _4.p(var1);
      if (var2) {
         this.h.add(var1);
      } else {
         this.h.remove(var1);
      }
   }

   public long E(String var1) {
      return this.s.getOrDefault(var1, 0L);
   }

   public void m(String var1, long var2) {
      _4.p(var1);
      if (var2 > 0L) {
         this.s.put(var1, var2);
      }
   }

   public void Q(String var1) {
      this.h.remove(var1);
      this.s.remove(var1);
   }

   public void n(String var1, String var2) {
      _4.p(var2);
      this.Y.h(var1, var2);
      this.c.c(var1, var2);
      if (this.h.remove(var1)) {
         this.h.add(var2);
      }

      Long var3 = this.s.remove(var1);
      if (var3 != null) {
         this.s.put(var2, var3);
      }
   }

   public List<String> P(List<String> var1, String var2, String var3) {
      Stream var4 = var1.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var2.toLowerCase(Locale.ROOT)));
      if (var3.equals("Favorites")) {
         var4 = var4.filter(this::L);
      }

      if (var3.equals("Recent")) {
         var4 = var4.filter(var1x -> this.E(var1x) > 0L).sorted(Comparator.comparingLong(this::E).reversed().thenComparing(Comparator.naturalOrder()));
      }

      return var4.toList();
   }

   public JsonObject M() {
      JsonObject var2 = new JsonObject();
      String[] var7 = ab;
      var2.addProperty(var7[35], this.A.D);
      var2.addProperty(var7[11], this.w);
      var2.addProperty(var7[12], this.N);
      var2.addProperty(var7[28], this.B);
      var2.addProperty(var7[0], this.C);
      var2.addProperty(var7[6], this.V.name());
      var2.addProperty(var7[33], this.Jw.name());
      var2.addProperty(var7[37], this.O);
      var2.addProperty(var7[9], this.I);
      var2.addProperty(var7[31], this.M);
      var2.addProperty(var7[24], this.f);
      boolean var10000 = k;
      var2.addProperty(var7[40], this.G);
      boolean var1 = var10000;
      var2.addProperty(var7[16], this.D);
      var2.addProperty(var7[14], this.t);
      var2.addProperty(var7[7], this.W.name());
      var2.addProperty(var7[29], this.P);
      var2.addProperty(var7[38], this.z);
      var2.addProperty(var7[43], this.b);
      var2.addProperty(var7[17], this.p);
      var2.addProperty(var7[10], this.q);
      var2.addProperty(var7[21], this.n);
      var2.addProperty(var7[2], this.j);
      var2.addProperty(var7[44], this.U);
      var2.addProperty(var7[32], this.Ju);
      var2.addProperty(var7[20], 2);
      var2.addProperty(var7[13], this.Z);
      var2.addProperty(var7[23], this.F);
      var2.addProperty(var7[18], this.i);
      var2.addProperty(var7[19], this.R);
      var2.addProperty(var7[42], this.J);
      var2.addProperty(var7[36], this.S);
      var2.addProperty(var7[1], this.a);
      var2.addProperty(var7[3], this.K);
      var2.addProperty(var7[34], this.JH);
      var2.addProperty(var7[26], this.d);
      var2.addProperty(var7[25], this.E);
      JsonArray var3 = new JsonArray();
      this.h.forEach(var3::add);
      var2.add(var7[30], var3);
      JsonObject var4 = new JsonObject();
      this.s.forEach(var4::addProperty);
      var2.add(var7[15], var4);
      JsonArray var5 = new JsonArray();
      this.r.forEach(var5::add);
      var2.add(var7[27], var5);
      var2.addProperty(var7[4], this.v);
      JsonArray var6 = new JsonArray();
      this.L.forEach(var6::add);
      var2.add(var7[8], var6);
      var2.add(var7[5], this.Y.t());
      var2.add(var7[22], this.c.h());
      var2.add(var7[41], this.Q.O());
      var2.add(var7[39], this.u.M());
      if (var1) {
         aq.n = (boolean)(aq.n ? 0 : 1);
      }

      return var2;
   }

   public static i3 s(JsonObject var0) {
      i3 var1 = new i3();

      try {
         if (var0.has("updateAutoCheck")) {
            var1.N = _4.Y(var0.get("updateAutoCheck"));
         }
      } catch (RuntimeException var47) {
      }

      try {
         String var2 = _4.m(var0, "updateSkippedRelease", 128);
         if (var2.isEmpty() || var2.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.B = var2;
         }
      } catch (RuntimeException var54) {
      }

      try {
         String var55 = _4.m(var0, "updateSeenReceipt", 257);
         if (var55.isEmpty() || var55.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}:[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.C = var55;
         }
      } catch (RuntimeException var53) {
      }

      try {
         if (var0.has("discordPresence")) {
            var1.f = _4.Y(var0.get("discordPresence"));
         }
      } catch (RuntimeException var46) {
      }

      try {
         if (var0.has("legitMode")) {
            var1.G = _4.Y(var0.get("legitMode"));
         }
      } catch (RuntimeException var45) {
      }

      try {
         if (var0.has("legitModeKey")) {
            _4.N(var0.get("legitModeKey"), 32.0, 348.0, true);
            var1.D = var0.get("legitModeKey").getAsInt();
         }
      } catch (RuntimeException var44) {
      }

      try {
         String var56 = _4.m(var0, "skySource", 16);
         if (Set.of("World", "Shader").contains(var56)) {
            var1.P = var56;
         }
      } catch (RuntimeException var43) {
      }

      try {
         var1.V = ng.i.valueOf(_4.m(var0, "menuSeason", 16));
      } catch (RuntimeException var42) {
      }

      try {
         var1.Jw = ng.c.valueOf(_4.m(var0, "menuQuality", 16));
      } catch (RuntimeException var41) {
      }

      try {
         _4.N(var0.get("menuTime"), 0.0, 1.0, false);
         var1.O = var0.get("menuTime").getAsDouble();
      } catch (RuntimeException var40) {
      }

      try {
         if (var0.has("cinematicMenu")) {
            var1.I = _4.Y(var0.get("cinematicMenu"));
         }
      } catch (RuntimeException var39) {
      }

      try {
         if (var0.has("customMenu")) {
            var1.M = _4.Y(var0.get("customMenu"));
         }
      } catch (RuntimeException var38) {
      }

      try {
         if (var0.has("clickGuiGradient")) {
            var1.b = _4.Y(var0.get("clickGuiGradient"));
         }
      } catch (RuntimeException var37) {
      }

      try {
         if (var0.has("clickGuiLight")) {
            var1.z = _4.Y(var0.get("clickGuiLight"));
         }
      } catch (RuntimeException var36) {
      }

      try {
         String var57 = _4.m(var0, "clickGuiAnimation", 16);
         if (wv.D.contains(var57)) {
            var1.p = var57;
         }
      } catch (RuntimeException var35) {
      }

      try {
         String var58 = _4.m(var0, "clickGuiCloseAnimation", 16);
         if (y.contains(var58)) {
            var1.q = var58;
         }
      } catch (RuntimeException var34) {
      }

      try {
         _4.N(var0.get("clickGuiDuration"), 100.0, 1000.0, true);
         var1.n = var0.get("clickGuiDuration").getAsInt();
      } catch (RuntimeException var33) {
      }

      try {
         _4.N(var0.get("clickGuiOpacity"), 0.0, 1.0, false);
         var1.j = var0.get("clickGuiOpacity").getAsDouble();
      } catch (RuntimeException var32) {
      }

      try {
         if (var0.has("clickGuiScale")) {
            _4.N(var0.get("clickGuiScale"), 0.5, 2.0, false);
            var1.U = var0.get("clickGuiScale").getAsDouble();
         }
      } catch (RuntimeException var31) {
      }

      try {
         String var59 = _4.m(var0, "clickGuiBackdrop", 16);
         byte var3 = 0;

         try {
            var3 = (byte)(var0.get("clickGuiBackdropRevision").getAsInt() >= 2 ? 1 : 0);
         } catch (RuntimeException var30) {
         }

         if (var59.equals("Aurora") && var3 == 0) {
            _q.c("GUI", "Moved the former default Aurora backdrop to None");
         } else if (J0.contains(var59)) {
            var1.Ju = var59;
         }
      } catch (RuntimeException var52) {
      }

      try {
         if (var0.has("clickGuiDim")) {
            _4.N(var0.get("clickGuiDim"), 0.0, 0.9, false);
            var1.Z = var0.get("clickGuiDim").getAsDouble();
         }
      } catch (RuntimeException var29) {
      }

      try {
         if (var0.has("clickGuiBlur")) {
            _4.N(var0.get("clickGuiBlur"), 0.0, 4.0, true);
            var1.F = var0.get("clickGuiBlur").getAsInt();
         }
      } catch (RuntimeException var28) {
      }

      try {
         String var60 = _4.m(var0, "clickGuiBlurArea", 16);
         if (X.contains(var60)) {
            var1.i = var60;
         }
      } catch (RuntimeException var27) {
      }

      try {
         String var61 = _4.m(var0, "clickGuiCardStyle", 16);
         if (g.contains(var61)) {
            var1.R = var61;
         }
      } catch (RuntimeException var26) {
      }

      try {
         String var62 = _4.m(var0, "clickGuiEnabledStyle", 16);
         if (o.contains(var62)) {
            var1.J = var62;
         }
      } catch (RuntimeException var25) {
      }

      try {
         String var63 = _4.m(var0, "clickGuiSwitchStyle", 16);
         if (H.contains(var63)) {
            var1.S = var63;
         }
      } catch (RuntimeException var24) {
      }

      try {
         String var64 = _4.m(var0, "clickGuiSliderStyle", 16);
         if (JO.contains(var64)) {
            var1.a = var64;
         }
      } catch (RuntimeException var23) {
      }

      try {
         if (var0.has("clickGuiRounding")) {
            _4.N(var0.get("clickGuiRounding"), 0.0, 2.0, false);
            var1.K = var0.get("clickGuiRounding").getAsDouble();
         }
      } catch (RuntimeException var22) {
      }

      try {
         if (var0.has("clickGuiAccentGlow")) {
            var1.JH = _4.Y(var0.get("clickGuiAccentGlow"));
         }
      } catch (RuntimeException var21) {
      }

      try {
         if (var0.has("clickGuiRipple")) {
            var1.d = _4.Y(var0.get("clickGuiRipple"));
         }
      } catch (RuntimeException var20) {
      }

      try {
         if (var0.has("clickGuiHoverLift")) {
            var1.E = _4.Y(var0.get("clickGuiHoverLift"));
         }
      } catch (RuntimeException var19) {
      }

      try {
         var1.A = pz.Q(_4.m(var0, "background", 32));
      } catch (RuntimeException var18) {
      }

      try {
         if (var0.has("softParticles")) {
            var1.t = _4.Y(var0.get("softParticles"));
         }
      } catch (RuntimeException var17) {
      }

      try {
         var1.W = nl.q.valueOf(_4.m(var0, "effectsQuality", 16));
      } catch (RuntimeException var16) {
      }

      try {
         var1.w = _4.Y(var0.get("motion"));
      } catch (RuntimeException var15) {
      }

      try {
         JsonArray var65 = var0.getAsJsonArray("favorites");
         if (var65.size() <= 1000) {
            for (JsonElement var4 : var65) {
               try {
                  var1.f(_4.p(var4.getAsString()), true);
               } catch (RuntimeException var14) {
               }
            }
         }
      } catch (RuntimeException var51) {
      }

      try {
         JsonObject var66 = var0.getAsJsonObject("recent");
         if (var66.size() <= 1000) {
            for (Entry var74 : var66.entrySet()) {
               try {
                  _4.N((JsonElement)var74.getValue(), 1.0, 9.223372E18F, true);
                  var1.m((String)var74.getKey(), ((JsonElement)var74.getValue()).getAsLong());
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var50) {
      }

      try {
         var1.v = _4.Y(var0.get("moduleFavoritesOpen"));
      } catch (RuntimeException var12) {
      }

      try {
         JsonArray var67 = var0.getAsJsonArray("hudLocks");
         if (var67.size() <= 128) {
            for (JsonElement var75 : var67) {
               try {
                  if (var75.isJsonPrimitive() && var75.getAsJsonPrimitive().isString()) {
                     var1.p(var75.getAsString(), true);
                  }
               } catch (RuntimeException var11) {
               }
            }
         }
      } catch (RuntimeException var49) {
      }

      try {
         JsonArray var68 = var0.getAsJsonArray("moduleFavorites");
         if (var68.size() <= 1000) {
            for (JsonElement var76 : var68) {
               try {
                  if (var76.isJsonPrimitive() && var76.getAsJsonPrimitive().isString()) {
                     var1.h(var76.getAsString(), true);
                  }
               } catch (RuntimeException var10) {
               }
            }
         }
      } catch (RuntimeException var48) {
      }

      try {
         if (var0.has("settingBinds")) {
            var1.Q = b1.X(var0.get("settingBinds"));
         }
      } catch (RuntimeException var9) {
         _q.R("Input", "Invalid setting shortcuts ignored: {}", var9.toString());
      }

      try {
         if (var0.has("legacyClickGui")) {
            var1.u = vd.z(var0.getAsJsonObject("legacyClickGui"));
         }
      } catch (RuntimeException var8) {
         _q.R("GUI", "Invalid legacy ClickGUI preference ignored: {}", var8.toString());
      }

      try {
         if (var0.has("israelMode")) {
            var1.Y = hi.b(var0.getAsJsonObject("israelMode"));
         }
      } catch (RuntimeException var7) {
         _q.R("Israel mode", "Invalid local preference ignored: {}", var7.toString());
      }

      String var69 = var0.has("larpPreset") ? "larpPreset" : "moonMode";

      try {
         if (var0.has(var69)) {
            var1.c = com.larpclient.r.Q(var0.getAsJsonObject(var69));
            if (!var0.has("larpPreset")) {
               _q.c("Larp Preset", "Loaded legacy cosmetic preset journal; next preference save uses larpPreset");
            }
         }
      } catch (RuntimeException var6) {
         _q.R("Larp Preset", "Invalid local preference ignored: {}", var6.toString());
      }

      return var1;
   }

   static {
      String[] var5 = new String[45];
      int var3 = 0;
      String var2 = "GVY\u001d>\u00061WCS./\u0000\u0007[VI\u0013QJT\u001f!$\u0017[uQ\u0015.\u0006\u0010aRD\u0010/\u000fQJT\u001f!$\u0017[iM\u001d)\n\u0016K\u0010QJT\u001f!$\u0017[tR\t$\u0007\u000b\\A\u0013_IY\t&\u0006$SPR\u000e#\u0017\u0007AiM\u0019$\n[UO\u001d/\u000f/]BX\n_CS\t\u0019\u0006\u0003AIS\u000eW@[\u0019)\u0017\u0011cS\\\u0010#\u0017\u001b\bZSY0%\u0000\tA\rQOS\u0019'\u0002\u0016[Ep\u0019$\u0016\u0016QJT\u001f!$\u0017[eQ\u00139\u0006#\\OP\u001d>\n\r\\\u0006_II\u0015%\r\u000fGVY\u001d>\u0006#GRR?\"\u0006\u0001Y\u000bQJT\u001f!$\u0017[bT\u0011\rAI[\b\u001a\u0002\u0010FO^\u0010/\u0010\u0006@C^\u0019$\u0017\f^CZ\u0015>.\rVCv\u00193\u0011QJT\u001f!$\u0017[gS\u0015'\u0002\u0016[IS\u0010QJT\u001f!$\u0017[dQ\t8\"\u0010WG\u0011QJT\u001f!$\u0017[e\\\u000e.0\u0016KJX\u0018QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]Vo\u0019<\n\u0011[IS\u0010QJT\u001f!$\u0017[bH\u000e+\u0017\u000b]H\n^GO\f\u001a\u0011\u0007ACI\fQJT\u001f!$\u0017[dQ\t8\u000fVON\u001f%\u0011\u0006bTX\u000f/\r\u0001W\u0011QJT\u001f!$\u0017[nR\n/\u0011.[@I\u000eQJT\u001f!$\u0017[tT\f:\u000f\u0007\u000f_IY\t&\u0006$SPR\u000e#\u0017\u0007A\u0014GVY\u001d>\u00061YOM\f/\u00070WJX\u001d9\u0006\tAMD/%\u0016\u0010QC\tTGK\u00138\n\u0016WU\nQSN\b%\u000e/WHH\u0010QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]V\u000b_CS\t\u001b\u0016\u0003^OI\u0005\u0012QJT\u001f!$\u0017[g^\u001f/\r\u0016uJR\u000b\nPG^\u0017-\u0011\rGHY\u0013QJT\u001f!$\u0017[uJ\u0015>\u0000\naRD\u0010/\b_CS\t\u001e\n\u000fW\rQJT\u001f!$\u0017[jT\u001b\"\u0017\u000e^CZ\u001d)\u001a!^O^\u0017\r\u0016\u000b\t^CZ\u0015>.\rVC\fACI\b#\r\u0005pOS\u00189\u0014QJT\u001f!$\u0017[cS\u001d(\u000f\u0007VuI\u0005&\u0006";
      int var4 = "GVY\u001d>\u00061WCS./\u0000\u0007[VI\u0013QJT\u001f!$\u0017[uQ\u0015.\u0006\u0010aRD\u0010/\u000fQJT\u001f!$\u0017[iM\u001d)\n\u0016K\u0010QJT\u001f!$\u0017[tR\t$\u0007\u000b\\A\u0013_IY\t&\u0006$SPR\u000e#\u0017\u0007AiM\u0019$\n[UO\u001d/\u000f/]BX\n_CS\t\u0019\u0006\u0003AIS\u000eW@[\u0019)\u0017\u0011cS\\\u0010#\u0017\u001b\bZSY0%\u0000\tA\rQOS\u0019'\u0002\u0016[Ep\u0019$\u0016\u0016QJT\u001f!$\u0017[eQ\u00139\u0006#\\OP\u001d>\n\r\\\u0006_II\u0015%\r\u000fGVY\u001d>\u0006#GRR?\"\u0006\u0001Y\u000bQJT\u001f!$\u0017[bT\u0011\rAI[\b\u001a\u0002\u0010FO^\u0010/\u0010\u0006@C^\u0019$\u0017\f^CZ\u0015>.\rVCv\u00193\u0011QJT\u001f!$\u0017[gS\u0015'\u0002\u0016[IS\u0010QJT\u001f!$\u0017[dQ\t8\"\u0010WG\u0011QJT\u001f!$\u0017[e\\\u000e.0\u0016KJX\u0018QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]Vo\u0019<\n\u0011[IS\u0010QJT\u001f!$\u0017[bH\u000e+\u0017\u000b]H\n^GO\f\u001a\u0011\u0007ACI\fQJT\u001f!$\u0017[dQ\t8\u000fVON\u001f%\u0011\u0006bTX\u000f/\r\u0001W\u0011QJT\u001f!$\u0017[nR\n/\u0011.[@I\u000eQJT\u001f!$\u0017[tT\f:\u000f\u0007\u000f_IY\t&\u0006$SPR\u000e#\u0017\u0007A\u0014GVY\u001d>\u00061YOM\f/\u00070WJX\u001d9\u0006\tAMD/%\u0016\u0010QC\tTGK\u00138\n\u0016WU\nQSN\b%\u000e/WHH\u0010QJT\u001f!$\u0017[d\\\u001f!\u0007\u0010]V\u000b_CS\t\u001b\u0016\u0003^OI\u0005\u0012QJT\u001f!$\u0017[g^\u001f/\r\u0016uJR\u000b\nPG^\u0017-\u0011\rGHY\u0013QJT\u001f!$\u0017[uJ\u0015>\u0000\naRD\u0010/\b_CS\t\u001e\n\u000fW\rQJT\u001f!$\u0017[jT\u001b\"\u0017\u000e^CZ\u001d)\u001a!^O^\u0017\r\u0016\u000b\t^CZ\u0015>.\rVC\fACI\b#\r\u0005pOS\u00189\u0014QJT\u001f!$\u0017[cS\u001d(\u000f\u0007VuI\u0005&\u0006"
         .length();
      char var1 = 17;
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
                     y = List.of("Reverse", "Shatter", "Crumble", "Slice", "Implode", "CRT");
                     J0 = List.of("Aurora", "Nebula", "Waves", "Gradient", "Dim", "None");
                     X = List.of("Window", "Everywhere");
                     g = List.of("Filled", "Outline", "Flat");
                     o = List.of("Glow", "Tint", "Text", "None");
                     H = List.of("Pill", "Square", "Minimal");
                     JO = List.of("Pill", "Minimal", "Classic");
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

                  var2 = "QJT\u001f!$\u0017[aO\u001d.\n\u0007\\R\rQJT\u001f!$\u0017[u^\u001d&\u0006";
                  var4 = "QJT\u001f!$\u0017[aO\u001d.\n\u0007\\R\rQJT\u001f!$\u0017[u^\u001d&\u0006".length();
                  var1 = 16;
                  var7 = -1;
            }

            var10000 = var2.substring(++var7, var7 + var1);
            var10001 = 0;
         }
      }
   }
}
