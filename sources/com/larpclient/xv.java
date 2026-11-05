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

public final class xv {
   public _w n = _w.N;
   public boolean V = (boolean)1;
   public boolean o = (boolean)1;
   public String l = "";
   public String a = "";
   public boolean d = (boolean)1;
   public tf.g b = tf.g.u;
   public tf.f A = tf.f.f;
   public double Z = 0.33;
   public boolean r = (boolean)1;
   public boolean U = (boolean)1;
   public boolean c;
   public boolean i;
   public String sb = "Fade";
   public static final List<String> W;
   public String Y = "Reverse";
   public int S = 280;
   public double T = 0.67;
   public static final double x = 0.5;
   public static final double J = 2.0;
   public double s8 = 1.0;
   public static final List<String> Q;
   public String F = "None";
   public static final double j = 0.9;
   public double X = 0.42;
   public static final int E = 4;
   public int q = 2;
   public static final List<String> h;
   public String R = "Window";
   public static final List<String> f;
   public static final List<String> v;
   public static final List<String> u;
   public static final List<String> D;
   public static final double k = 2.0;
   public String g = "Filled";
   public String sS = "Glow";
   public String M = "Pill";
   public String G = "Pill";
   public double m = 1.0;
   public boolean y = (boolean)1;
   public boolean sz = (boolean)1;
   public boolean P = (boolean)1;
   private static final int w = 2;
   public boolean L = (boolean)1;
   public m5.n B = m5.n.h;
   public String z = "World";
   public kr K = new kr();
   public _4 C = new _4();
   public um s = new um();
   public e8 O = new e8();
   private final Set<String> t = new LinkedHashSet<>();
   private final Set<String> I = new LinkedHashSet<>();
   private final Set<String> H = new LinkedHashSet<>();
   public boolean p;
   private final Map<String, Long> e = new LinkedHashMap<>();
   public static int N;
   private static final String[] ab;

   public f.a O() {
      return new f.a(this.g, this.sS, this.M, this.G, this.m, this.y, this.sz, this.P);
   }

   public boolean c(String var1) {
      return this.H.contains(var1);
   }

   public void f(String var1, boolean var2) {
      if (var1 != null && var1.matches("[a-z][a-z0-9_-]{0,63}")) {
         if (var2 && this.H.size() < 128) {
            this.H.add(var1);
         } else if (!var2) {
            this.H.remove(var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid HUD identifier");
      }
   }

   public boolean J(String var1) {
      return this.I.contains(var1) || "CustomSky".equals(var1) && this.I.contains("Sky Shader");
   }

   public void W(String var1, boolean var2) {
      if (var1 != null && var1.matches("[A-Za-z0-9 _-]{1,128}") && !var1.isBlank()) {
         if (var2) {
            if (this.I.size() >= 1000 && !this.I.contains(var1)) {
               return;
            }

            this.I.add(var1);
         } else {
            this.I.remove(var1);
            if (var1.equals("CustomSky")) {
               this.I.remove("Sky Shader");
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   public boolean V(String var1) {
      return this.t.contains(var1);
   }

   public void z(String var1, boolean var2) {
      p_.g(var1);
      if (var2) {
         this.t.add(var1);
      } else {
         this.t.remove(var1);
      }
   }

   public long y(String var1) {
      return this.e.getOrDefault(var1, 0L);
   }

   public void q(String var1, long var2) {
      p_.g(var1);
      if (var2 > 0L) {
         this.e.put(var1, var2);
      }
   }

   public void Y(String var1) {
      this.t.remove(var1);
      this.e.remove(var1);
   }

   public void c(String var1, String var2) {
      p_.g(var2);
      this.K.k(var1, var2);
      this.C.f(var1, var2);
      if (this.t.remove(var1)) {
         this.t.add(var2);
      }

      Long var3 = this.e.remove(var1);
      if (var3 != null) {
         this.e.put(var2, var3);
      }
   }

   public List<String> A(List<String> var1, String var2, String var3) {
      Stream var4 = var1.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var2.toLowerCase(Locale.ROOT)));
      if (var3.equals("Favorites")) {
         var4 = var4.filter(this::V);
      }

      if (var3.equals("Recent")) {
         var4 = var4.filter(var1x -> this.y(var1x) > 0L).sorted(Comparator.comparingLong(this::y).reversed().thenComparing(Comparator.naturalOrder()));
      }

      return var4.toList();
   }

   public JsonObject t() {
      JsonObject var2 = new JsonObject();
      String[] var7 = ab;
      var2.addProperty(var7[17], this.n.G);
      var2.addProperty(var7[18], this.V);
      var2.addProperty(var7[12], this.o);
      int var10000 = N;
      var2.addProperty(var7[6], this.l);
      var2.addProperty(var7[9], this.a);
      var2.addProperty(var7[33], this.b.name());
      var2.addProperty(var7[0], this.A.name());
      var2.addProperty(var7[35], this.Z);
      var2.addProperty(var7[16], this.r);
      var2.addProperty(var7[30], this.U);
      var2.addProperty(var7[34], this.d);
      var2.addProperty(var7[8], this.L);
      var2.addProperty(var7[14], this.B.name());
      int var1 = var10000;
      var2.addProperty(var7[36], this.z);
      var2.addProperty(var7[13], this.c);
      var2.addProperty(var7[38], this.i);
      var2.addProperty(var7[10], this.sb);
      var2.addProperty(var7[7], this.Y);
      var2.addProperty(var7[31], this.S);
      var2.addProperty(var7[24], this.T);
      var2.addProperty(var7[1], this.s8);
      var2.addProperty(var7[27], this.F);
      var2.addProperty(var7[4], 2);
      var2.addProperty(var7[15], this.X);
      var2.addProperty(var7[32], this.q);
      var2.addProperty(var7[29], this.R);
      var2.addProperty(var7[40], this.g);
      var2.addProperty(var7[22], this.sS);
      var2.addProperty(var7[26], this.M);
      var2.addProperty(var7[11], this.G);
      var2.addProperty(var7[19], this.m);
      var2.addProperty(var7[42], this.y);
      var2.addProperty(var7[41], this.sz);
      var2.addProperty(var7[25], this.P);
      JsonArray var3 = new JsonArray();
      this.t.forEach(var3::add);
      var2.add(var7[2], var3);
      JsonObject var4 = new JsonObject();
      this.e.forEach(var4::addProperty);
      var2.add(var7[20], var4);
      JsonArray var5 = new JsonArray();
      this.I.forEach(var5::add);
      var2.add(var7[23], var5);
      var2.addProperty(var7[37], this.p);
      JsonArray var6 = new JsonArray();
      this.H.forEach(var6::add);
      var2.add(var7[21], var6);
      var2.add(var7[5], this.K.D());
      var2.add(var7[28], this.C.F());
      var2.add(var7[39], this.s.S());
      var2.add(var7[3], this.O.O());
      if (var1 != 0) {
         w9.e = (boolean)(w9.e ? 0 : 1);
      }

      return var2;
   }

   public static xv c(JsonObject var0) {
      xv var1 = new xv();

      try {
         if (var0.has("updateAutoCheck")) {
            var1.o = p_.v(var0.get("updateAutoCheck"));
         }
      } catch (RuntimeException var45) {
      }

      try {
         String var2 = p_.Q(var0, "updateSkippedRelease", 128);
         if (var2.isEmpty() || var2.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.l = var2;
         }
      } catch (RuntimeException var52) {
      }

      try {
         String var53 = p_.Q(var0, "updateSeenReceipt", 257);
         if (var53.isEmpty() || var53.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}:[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.a = var53;
         }
      } catch (RuntimeException var51) {
      }

      try {
         if (var0.has("discordPresence")) {
            var1.d = p_.v(var0.get("discordPresence"));
         }
      } catch (RuntimeException var44) {
      }

      try {
         String var54 = p_.Q(var0, "skySource", 16);
         if (Set.of("World", "Shader").contains(var54)) {
            var1.z = var54;
         }
      } catch (RuntimeException var43) {
      }

      try {
         var1.b = tf.g.valueOf(p_.Q(var0, "menuSeason", 16));
      } catch (RuntimeException var42) {
      }

      try {
         var1.A = tf.f.valueOf(p_.Q(var0, "menuQuality", 16));
      } catch (RuntimeException var41) {
      }

      try {
         p_.w(var0.get("menuTime"), 0.0, 1.0, false);
         var1.Z = var0.get("menuTime").getAsDouble();
      } catch (RuntimeException var40) {
      }

      try {
         if (var0.has("cinematicMenu")) {
            var1.r = p_.v(var0.get("cinematicMenu"));
         }
      } catch (RuntimeException var39) {
      }

      try {
         if (var0.has("customMenu")) {
            var1.U = p_.v(var0.get("customMenu"));
         }
      } catch (RuntimeException var38) {
      }

      try {
         if (var0.has("clickGuiGradient")) {
            var1.i = p_.v(var0.get("clickGuiGradient"));
         }
      } catch (RuntimeException var37) {
      }

      try {
         if (var0.has("clickGuiLight")) {
            var1.c = p_.v(var0.get("clickGuiLight"));
         }
      } catch (RuntimeException var36) {
      }

      try {
         String var55 = p_.Q(var0, "clickGuiAnimation", 16);
         if (p2.i.contains(var55)) {
            var1.sb = var55;
         }
      } catch (RuntimeException var35) {
      }

      try {
         String var56 = p_.Q(var0, "clickGuiCloseAnimation", 16);
         if (W.contains(var56)) {
            var1.Y = var56;
         }
      } catch (RuntimeException var34) {
      }

      try {
         p_.w(var0.get("clickGuiDuration"), 100.0, 1000.0, true);
         var1.S = var0.get("clickGuiDuration").getAsInt();
      } catch (RuntimeException var33) {
      }

      try {
         p_.w(var0.get("clickGuiOpacity"), 0.0, 1.0, false);
         var1.T = var0.get("clickGuiOpacity").getAsDouble();
      } catch (RuntimeException var32) {
      }

      try {
         if (var0.has("clickGuiScale")) {
            p_.w(var0.get("clickGuiScale"), 0.5, 2.0, false);
            var1.s8 = var0.get("clickGuiScale").getAsDouble();
         }
      } catch (RuntimeException var31) {
      }

      try {
         String var57 = p_.Q(var0, "clickGuiBackdrop", 16);
         byte var3 = 0;

         try {
            var3 = (byte)(var0.get("clickGuiBackdropRevision").getAsInt() >= 2 ? 1 : 0);
         } catch (RuntimeException var30) {
         }

         if (var57.equals("Aurora") && var3 == 0) {
            mt.U("GUI", "Moved the former default Aurora backdrop to None");
         } else if (Q.contains(var57)) {
            var1.F = var57;
         }
      } catch (RuntimeException var50) {
      }

      try {
         if (var0.has("clickGuiDim")) {
            p_.w(var0.get("clickGuiDim"), 0.0, 0.9, false);
            var1.X = var0.get("clickGuiDim").getAsDouble();
         }
      } catch (RuntimeException var29) {
      }

      try {
         if (var0.has("clickGuiBlur")) {
            p_.w(var0.get("clickGuiBlur"), 0.0, 4.0, true);
            var1.q = var0.get("clickGuiBlur").getAsInt();
         }
      } catch (RuntimeException var28) {
      }

      try {
         String var58 = p_.Q(var0, "clickGuiBlurArea", 16);
         if (h.contains(var58)) {
            var1.R = var58;
         }
      } catch (RuntimeException var27) {
      }

      try {
         String var59 = p_.Q(var0, "clickGuiCardStyle", 16);
         if (f.contains(var59)) {
            var1.g = var59;
         }
      } catch (RuntimeException var26) {
      }

      try {
         String var60 = p_.Q(var0, "clickGuiEnabledStyle", 16);
         if (v.contains(var60)) {
            var1.sS = var60;
         }
      } catch (RuntimeException var25) {
      }

      try {
         String var61 = p_.Q(var0, "clickGuiSwitchStyle", 16);
         if (u.contains(var61)) {
            var1.M = var61;
         }
      } catch (RuntimeException var24) {
      }

      try {
         String var62 = p_.Q(var0, "clickGuiSliderStyle", 16);
         if (D.contains(var62)) {
            var1.G = var62;
         }
      } catch (RuntimeException var23) {
      }

      try {
         if (var0.has("clickGuiRounding")) {
            p_.w(var0.get("clickGuiRounding"), 0.0, 2.0, false);
            var1.m = var0.get("clickGuiRounding").getAsDouble();
         }
      } catch (RuntimeException var22) {
      }

      try {
         if (var0.has("clickGuiAccentGlow")) {
            var1.y = p_.v(var0.get("clickGuiAccentGlow"));
         }
      } catch (RuntimeException var21) {
      }

      try {
         if (var0.has("clickGuiRipple")) {
            var1.sz = p_.v(var0.get("clickGuiRipple"));
         }
      } catch (RuntimeException var20) {
      }

      try {
         if (var0.has("clickGuiHoverLift")) {
            var1.P = p_.v(var0.get("clickGuiHoverLift"));
         }
      } catch (RuntimeException var19) {
      }

      try {
         var1.n = _w.b(p_.Q(var0, "background", 32));
      } catch (RuntimeException var18) {
      }

      try {
         if (var0.has("softParticles")) {
            var1.L = p_.v(var0.get("softParticles"));
         }
      } catch (RuntimeException var17) {
      }

      try {
         var1.B = m5.n.valueOf(p_.Q(var0, "effectsQuality", 16));
      } catch (RuntimeException var16) {
      }

      try {
         var1.V = p_.v(var0.get("motion"));
      } catch (RuntimeException var15) {
      }

      try {
         JsonArray var63 = var0.getAsJsonArray("favorites");
         if (var63.size() <= 1000) {
            for (JsonElement var4 : var63) {
               try {
                  var1.z(p_.g(var4.getAsString()), true);
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
                  p_.w((JsonElement)var72.getValue(), 1.0, 9.223372E18F, true);
                  var1.q((String)var72.getKey(), ((JsonElement)var72.getValue()).getAsLong());
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var48) {
      }

      try {
         var1.p = p_.v(var0.get("moduleFavoritesOpen"));
      } catch (RuntimeException var12) {
      }

      try {
         JsonArray var65 = var0.getAsJsonArray("hudLocks");
         if (var65.size() <= 128) {
            for (JsonElement var73 : var65) {
               try {
                  if (var73.isJsonPrimitive() && var73.getAsJsonPrimitive().isString()) {
                     var1.f(var73.getAsString(), true);
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
                     var1.W(var74.getAsString(), true);
                  }
               } catch (RuntimeException var10) {
               }
            }
         }
      } catch (RuntimeException var46) {
      }

      try {
         if (var0.has("settingBinds")) {
            var1.s = um.q(var0.get("settingBinds"));
         }
      } catch (RuntimeException var9) {
         mt.T("Input", "Invalid setting shortcuts ignored: {}", var9.toString());
      }

      try {
         if (var0.has("legacyClickGui")) {
            var1.O = e8.c(var0.getAsJsonObject("legacyClickGui"));
         }
      } catch (RuntimeException var8) {
         mt.T("GUI", "Invalid legacy ClickGUI preference ignored: {}", var8.toString());
      }

      try {
         if (var0.has("israelMode")) {
            var1.K = kr.x(var0.getAsJsonObject("israelMode"));
         }
      } catch (RuntimeException var7) {
         mt.T("Israel mode", "Invalid local preference ignored: {}", var7.toString());
      }

      String var67 = var0.has("larpPreset") ? "larpPreset" : "moonMode";

      try {
         if (var0.has(var67)) {
            var1.C = _4.M(var0.getAsJsonObject(var67));
            if (!var0.has("larpPreset")) {
               mt.U("Larp Preset", "Loaded legacy cosmetic preset journal; next preference save uses larpPreset");
            }
         }
      } catch (RuntimeException var6) {
         mt.T("Larp Preset", "Invalid local preference ignored: {}", var6.toString());
      }

      return var1;
   }

   static {
      String[] var5 = new String[43];
      int var3 = 0;
      String var2 = "M\u000fNW6\u0001\u0019L\u0003T[\rC\u0006IA\f3\rI9CC\u000b\u0011\tF\u000bVM\u0015\u001d\fE\u0019\u000eL\u000fGC\u0004\r;L\u0003CI \u0001\u0011\u0018C\u0006IA\f3\rI(AA\f\u0010\nO\u001arG\u0011\u001d\u000bI\u0005N\nI\u0019RC\u0002\u00185O\u000eE\u0014U\u001aDC\u0013\u0011+K\u0003PR\u0002\u0010*E\u0006EC\u0014\u0011\u0016C\u0006IA\f3\rI)LM\u0014\u00119N\u0003MC\u0013\u001d\u0017N\rS\u0005FV7\u0015\nT\u0003CN\u0002\u0007\u0011U\u001aDC\u0013\u0011+E\u000fNp\u0002\u0017\u001dI\u001aT\u0011C\u0006IA\f3\rI+NK\n\u0015\fI\u0005N\u0013C\u0006IA\f3\rI9LK\u0003\u0011\ns\u001eYN\u0002\u000fU\u001aDC\u0013\u00119U\u001eOa\u000f\u0011\u001bK\rC\u0006IA\f3\rI&IE\u000f\u0000\u000eE\fFG\u0004\u0000\u000bq\u001fAN\u000e\u0000\u0001\u000bC\u0006IA\f3\rI.IO\rC\u0003NG\n\u0015\fI\tmG\t\u0001\nB\u000bCI\u0000\u0006\u0017U\u0004D\u0006M\u0005TK\b\u001a\u0010C\u0006IA\f3\rI8OW\t\u0010\u0011N\r\u0006R\u000fCG\t\u0000\bH\u001fDn\b\u0017\u0013S\u0014C\u0006IA\f3\rI/NC\u0005\u0018\u001dD9T[\u000b\u0011\u000fM\u0005DW\u000b\u0011>A\u001cOP\u000e\u0000\u001dS\u000fC\u0006IA\f3\rI%PC\u0004\u001d\fY\u0011C\u0006IA\f3\rI\"OT\u0002\u00064I\fT\u0013C\u0006IA\f3\rI9WK\u0013\u0017\u0010s\u001eYN\u0002\u0010C\u0006IA\f3\rI(AA\f\u0010\nO\u001a\nL\u000bRR7\u0006\u001dS\u000fT\u0010C\u0006IA\f3\rI(LW\u00155\nE\u000b\nC\u001fSV\b\u00195E\u0004U\u0010C\u0006IA\f3\rI.UP\u0006\u0000\u0011O\u0004\fC\u0006IA\f3\rI(LW\u0015\nM\u000fNW4\u0011\u0019S\u0005N\u000fD\u0003SA\b\u0006\u001cp\u0018EQ\u0002\u001a\u001bE\bM\u000fNW3\u001d\u0015E\tS\u0001Yq\b\u0001\nC\u000f\u0013M\u0005DW\u000b\u0011>A\u001cOP\u000e\u0000\u001dS%PG\t\u0010C\u0006IA\f3\rI-RC\u0003\u001d\u001dN\u001e\fS\u000fTV\u000e\u001a\u001fb\u0003NF\u0014\u0011C\u0006IA\f3\rI)AP\u0003'\fY\u0006E";
      int var4 = "M\u000fNW6\u0001\u0019L\u0003T[\rC\u0006IA\f3\rI9CC\u000b\u0011\tF\u000bVM\u0015\u001d\fE\u0019\u000eL\u000fGC\u0004\r;L\u0003CI \u0001\u0011\u0018C\u0006IA\f3\rI(AA\f\u0010\nO\u001arG\u0011\u001d\u000bI\u0005N\nI\u0019RC\u0002\u00185O\u000eE\u0014U\u001aDC\u0013\u0011+K\u0003PR\u0002\u0010*E\u0006EC\u0014\u0011\u0016C\u0006IA\f3\rI)LM\u0014\u00119N\u0003MC\u0013\u001d\u0017N\rS\u0005FV7\u0015\nT\u0003CN\u0002\u0007\u0011U\u001aDC\u0013\u0011+E\u000fNp\u0002\u0017\u001dI\u001aT\u0011C\u0006IA\f3\rI+NK\n\u0015\fI\u0005N\u0013C\u0006IA\f3\rI9LK\u0003\u0011\ns\u001eYN\u0002\u000fU\u001aDC\u0013\u00119U\u001eOa\u000f\u0011\u001bK\rC\u0006IA\f3\rI&IE\u000f\u0000\u000eE\fFG\u0004\u0000\u000bq\u001fAN\u000e\u0000\u0001\u000bC\u0006IA\f3\rI.IO\rC\u0003NG\n\u0015\fI\tmG\t\u0001\nB\u000bCI\u0000\u0006\u0017U\u0004D\u0006M\u0005TK\b\u001a\u0010C\u0006IA\f3\rI8OW\t\u0010\u0011N\r\u0006R\u000fCG\t\u0000\bH\u001fDn\b\u0017\u0013S\u0014C\u0006IA\f3\rI/NC\u0005\u0018\u001dD9T[\u000b\u0011\u000fM\u0005DW\u000b\u0011>A\u001cOP\u000e\u0000\u001dS\u000fC\u0006IA\f3\rI%PC\u0004\u001d\fY\u0011C\u0006IA\f3\rI\"OT\u0002\u00064I\fT\u0013C\u0006IA\f3\rI9WK\u0013\u0017\u0010s\u001eYN\u0002\u0010C\u0006IA\f3\rI(AA\f\u0010\nO\u001a\nL\u000bRR7\u0006\u001dS\u000fT\u0010C\u0006IA\f3\rI(LW\u00155\nE\u000b\nC\u001fSV\b\u00195E\u0004U\u0010C\u0006IA\f3\rI.UP\u0006\u0000\u0011O\u0004\fC\u0006IA\f3\rI(LW\u0015\nM\u000fNW4\u0011\u0019S\u0005N\u000fD\u0003SA\b\u0006\u001cp\u0018EQ\u0002\u001a\u001bE\bM\u000fNW3\u001d\u0015E\tS\u0001Yq\b\u0001\nC\u000f\u0013M\u0005DW\u000b\u0011>A\u001cOP\u000e\u0000\u001dS%PG\t\u0010C\u0006IA\f3\rI-RC\u0003\u001d\u001dN\u001e\fS\u000fTV\u000e\u001a\u001fb\u0003NF\u0014\u0011C\u0006IA\f3\rI)AP\u0003'\fY\u0006E"
         .length();
      char var1 = 11;
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
                     W = List.of("Reverse", "Shatter", "Crumble", "Slice", "Implode", "CRT");
                     Q = List.of("Aurora", "Nebula", "Waves", "Gradient", "Dim", "None");
                     h = List.of("Window", "Everywhere");
                     f = List.of("Filled", "Outline", "Flat");
                     v = List.of("Glow", "Tint", "Text", "None");
                     u = List.of("Pill", "Square", "Minimal");
                     D = List.of("Pill", "Minimal", "Classic");
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

                  var2 = "C\u0006IA\f3\rI8IR\u0017\u0018\u001d\u0012C\u0006IA\f3\rI+CA\u0002\u001a\fg\u0006OU";
                  var4 = "C\u0006IA\f3\rI8IR\u0017\u0018\u001d\u0012C\u0006IA\f3\rI+CA\u0002\u001a\fg\u0006OU".length();
                  var1 = 14;
                  var7 = -1;
            }

            var10000 = var2.substring(++var7, var7 + var1);
            var10001 = 0;
         }
      }
   }
}
