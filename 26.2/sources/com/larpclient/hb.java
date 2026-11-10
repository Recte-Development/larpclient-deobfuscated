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

public final class hb {
   public t2 FH = t2.K;
   public boolean L = (boolean)1;
   public boolean C = (boolean)1;
   public String h = "";
   public String y = "";
   public boolean w = (boolean)1;
   public boolean J;
   public int m = 47;
   public j.r FQ = j.r.C;
   public j.v o = j.v.s;
   public double G = 0.33;
   public boolean M = (boolean)1;
   public boolean t = (boolean)1;
   public boolean f;
   public boolean W;
   public String E = "Fade";
   public static final List<String> i;
   public String l = "Reverse";
   public int c = 280;
   public double r = 0.67;
   public static final double k = 0.5;
   public static final double R = 2.0;
   public double q = 1.0;
   public static final List<String> v;
   public String FF = "None";
   public static final double Ft = 0.9;
   public double Z = 0.42;
   public static final int F = 4;
   public int g = 2;
   public static final List<String> j;
   public String H = "Window";
   public static final List<String> B;
   public static final List<String> n;
   public static final List<String> A;
   public static final List<String> b;
   public static final double D = 2.0;
   public String I = "Filled";
   public String Fw = "Glow";
   public String S = "Pill";
   public String T = "Pill";
   public double O = 1.0;
   public boolean N = (boolean)1;
   public boolean s = (boolean)1;
   public boolean U = (boolean)1;
   private static final int z = 2;
   public boolean u = (boolean)1;
   public wa.a V = wa.a.Y;
   public String e = "World";
   public u3 d = new u3();
   public kf p = new kf();
   public wr K = new wr();
   public sv P = new sv();
   private final Set<String> X = new LinkedHashSet<>();
   private final Set<String> Fa = new LinkedHashSet<>();
   private final Set<String> Q = new LinkedHashSet<>();
   public boolean x;
   private final Map<String, Long> F2 = new LinkedHashMap<>();
   public static int Y;
   private static final String[] a;

   public ho.p b() {
      return new ho.p(this.I, this.Fw, this.S, this.T, this.O, this.N, this.s, this.U);
   }

   public boolean t(String var1) {
      return this.Q.contains(var1);
   }

   public void K(String var1, boolean var2) {
      if (var1 != null && var1.matches("[a-z][a-z0-9_-]{0,63}")) {
         if (var2 && this.Q.size() < 128) {
            this.Q.add(var1);
         } else if (!var2) {
            this.Q.remove(var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid HUD identifier");
      }
   }

   public boolean M(String var1) {
      return this.Fa.contains(var1) || "CustomSky".equals(var1) && this.Fa.contains("Sky Shader");
   }

   public void e(String var1, boolean var2) {
      if (var1 != null && var1.matches("[A-Za-z0-9 _-]{1,128}") && !var1.isBlank()) {
         if (var2) {
            if (this.Fa.size() >= 1000 && !this.Fa.contains(var1)) {
               return;
            }

            this.Fa.add(var1);
         } else {
            this.Fa.remove(var1);
            if (var1.equals("CustomSky")) {
               this.Fa.remove("Sky Shader");
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   public boolean h(String var1) {
      return this.X.contains(var1);
   }

   public void x(String var1, boolean var2) {
      km.r(var1);
      if (var2) {
         this.X.add(var1);
      } else {
         this.X.remove(var1);
      }
   }

   public long H(String var1) {
      return this.F2.getOrDefault(var1, 0L);
   }

   public void u(String var1, long var2) {
      km.r(var1);
      if (var2 > 0L) {
         this.F2.put(var1, var2);
      }
   }

   public void C(String var1) {
      this.X.remove(var1);
      this.F2.remove(var1);
   }

   public void r(String var1, String var2) {
      km.r(var2);
      this.d.i(var1, var2);
      this.p.H(var1, var2);
      if (this.X.remove(var1)) {
         this.X.add(var2);
      }

      Long var3 = this.F2.remove(var1);
      if (var3 != null) {
         this.F2.put(var2, var3);
      }
   }

   public List<String> j(List<String> var1, String var2, String var3) {
      Stream var4 = var1.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var2.toLowerCase(Locale.ROOT)));
      if (var3.equals("Favorites")) {
         var4 = var4.filter(this::h);
      }

      if (var3.equals("Recent")) {
         var4 = var4.filter(var1x -> this.H(var1x) > 0L).sorted(Comparator.comparingLong(this::H).reversed().thenComparing(Comparator.naturalOrder()));
      }

      return var4.toList();
   }

   public JsonObject E() {
      JsonObject var3 = new JsonObject();
      String[] var8 = a;
      var3.addProperty(var8[28], this.FH.B);
      var3.addProperty(var8[23], this.L);
      var3.addProperty(var8[41], this.C);
      var3.addProperty(var8[44], this.h);
      var3.addProperty(var8[8], this.y);
      var3.addProperty(var8[21], this.FQ.name());
      var3.addProperty(var8[11], this.o.name());
      var3.addProperty(var8[14], this.G);
      var3.addProperty(var8[26], this.M);
      var3.addProperty(var8[25], this.t);
      var3.addProperty(var8[12], this.w);
      var3.addProperty(var8[17], this.J);
      var3.addProperty(var8[9], this.m);
      var3.addProperty(var8[16], this.u);
      var3.addProperty(var8[39], this.V.name());
      var3.addProperty(var8[42], this.e);
      var3.addProperty(var8[20], this.f);
      var3.addProperty(var8[7], this.W);
      var3.addProperty(var8[30], this.E);
      var3.addProperty(var8[31], this.l);
      var3.addProperty(var8[24], this.c);
      var3.addProperty(var8[5], this.r);
      var3.addProperty(var8[37], this.q);
      var3.addProperty(var8[27], this.FF);
      int var10000 = Y;
      var3.addProperty(var8[3], 2);
      var3.addProperty(var8[36], this.Z);
      var3.addProperty(var8[43], this.g);
      var3.addProperty(var8[13], this.H);
      var3.addProperty(var8[2], this.I);
      var3.addProperty(var8[18], this.Fw);
      var3.addProperty(var8[35], this.S);
      var3.addProperty(var8[4], this.T);
      var3.addProperty(var8[0], this.O);
      var3.addProperty(var8[19], this.N);
      int var1 = var10000;
      var3.addProperty(var8[34], this.s);
      var3.addProperty(var8[15], this.U);
      JsonArray var4 = new JsonArray();
      this.X.forEach(var4::add);
      var3.add(var8[29], var4);
      JsonObject var5 = new JsonObject();
      this.F2.forEach(var5::addProperty);
      var3.add(var8[1], var5);
      JsonArray var6 = new JsonArray();
      this.Fa.forEach(var6::add);
      var3.add(var8[32], var6);
      var3.addProperty(var8[10], this.x);
      JsonArray var7 = new JsonArray();
      this.Q.forEach(var7::add);
      var3.add(var8[6], var7);
      var3.add(var8[33], this.d.a());
      var3.add(var8[38], this.p.j());
      var3.add(var8[40], this.K.f());
      var3.add(var8[22], this.P.g());
      if (var1 != 0) {
         int var2 = gg.I;
         gg.I = ++var2;
      }

      return var3;
   }

   public static hb E(JsonObject var0) {
      hb var1 = new hb();

      try {
         if (var0.has("updateAutoCheck")) {
            var1.C = km.t(var0.get("updateAutoCheck"));
         }
      } catch (RuntimeException var47) {
      }

      try {
         String var2 = km.a(var0, "updateSkippedRelease", 128);
         if (var2.isEmpty() || var2.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.h = var2;
         }
      } catch (RuntimeException var54) {
      }

      try {
         String var55 = km.a(var0, "updateSeenReceipt", 257);
         if (var55.isEmpty() || var55.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}:[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.y = var55;
         }
      } catch (RuntimeException var53) {
      }

      try {
         if (var0.has("discordPresence")) {
            var1.w = km.t(var0.get("discordPresence"));
         }
      } catch (RuntimeException var46) {
      }

      try {
         if (var0.has("legitMode")) {
            var1.J = km.t(var0.get("legitMode"));
         }
      } catch (RuntimeException var45) {
      }

      try {
         if (var0.has("legitModeKey")) {
            km.i(var0.get("legitModeKey"), 32.0, 348.0, true);
            var1.m = var0.get("legitModeKey").getAsInt();
         }
      } catch (RuntimeException var44) {
      }

      try {
         String var56 = km.a(var0, "skySource", 16);
         if (Set.of("World", "Shader").contains(var56)) {
            var1.e = var56;
         }
      } catch (RuntimeException var43) {
      }

      try {
         var1.FQ = j.r.valueOf(km.a(var0, "menuSeason", 16));
      } catch (RuntimeException var42) {
      }

      try {
         var1.o = j.v.valueOf(km.a(var0, "menuQuality", 16));
      } catch (RuntimeException var41) {
      }

      try {
         km.i(var0.get("menuTime"), 0.0, 1.0, false);
         var1.G = var0.get("menuTime").getAsDouble();
      } catch (RuntimeException var40) {
      }

      try {
         if (var0.has("cinematicMenu")) {
            var1.M = km.t(var0.get("cinematicMenu"));
         }
      } catch (RuntimeException var39) {
      }

      try {
         if (var0.has("customMenu")) {
            var1.t = km.t(var0.get("customMenu"));
         }
      } catch (RuntimeException var38) {
      }

      try {
         if (var0.has("clickGuiGradient")) {
            var1.W = km.t(var0.get("clickGuiGradient"));
         }
      } catch (RuntimeException var37) {
      }

      try {
         if (var0.has("clickGuiLight")) {
            var1.f = km.t(var0.get("clickGuiLight"));
         }
      } catch (RuntimeException var36) {
      }

      try {
         String var57 = km.a(var0, "clickGuiAnimation", 16);
         if (by.q.contains(var57)) {
            var1.E = var57;
         }
      } catch (RuntimeException var35) {
      }

      try {
         String var58 = km.a(var0, "clickGuiCloseAnimation", 16);
         if (i.contains(var58)) {
            var1.l = var58;
         }
      } catch (RuntimeException var34) {
      }

      try {
         km.i(var0.get("clickGuiDuration"), 100.0, 1000.0, true);
         var1.c = var0.get("clickGuiDuration").getAsInt();
      } catch (RuntimeException var33) {
      }

      try {
         km.i(var0.get("clickGuiOpacity"), 0.0, 1.0, false);
         var1.r = var0.get("clickGuiOpacity").getAsDouble();
      } catch (RuntimeException var32) {
      }

      try {
         if (var0.has("clickGuiScale")) {
            km.i(var0.get("clickGuiScale"), 0.5, 2.0, false);
            var1.q = var0.get("clickGuiScale").getAsDouble();
         }
      } catch (RuntimeException var31) {
      }

      try {
         String var59 = km.a(var0, "clickGuiBackdrop", 16);
         byte var3 = 0;

         try {
            var3 = (byte)(var0.get("clickGuiBackdropRevision").getAsInt() >= 2 ? 1 : 0);
         } catch (RuntimeException var30) {
         }

         if (var59.equals("Aurora") && var3 == 0) {
            com.larpclient.h.U("GUI", "Moved the former default Aurora backdrop to None");
         } else if (v.contains(var59)) {
            var1.FF = var59;
         }
      } catch (RuntimeException var52) {
      }

      try {
         if (var0.has("clickGuiDim")) {
            km.i(var0.get("clickGuiDim"), 0.0, 0.9, false);
            var1.Z = var0.get("clickGuiDim").getAsDouble();
         }
      } catch (RuntimeException var29) {
      }

      try {
         if (var0.has("clickGuiBlur")) {
            km.i(var0.get("clickGuiBlur"), 0.0, 4.0, true);
            var1.g = var0.get("clickGuiBlur").getAsInt();
         }
      } catch (RuntimeException var28) {
      }

      try {
         String var60 = km.a(var0, "clickGuiBlurArea", 16);
         if (j.contains(var60)) {
            var1.H = var60;
         }
      } catch (RuntimeException var27) {
      }

      try {
         String var61 = km.a(var0, "clickGuiCardStyle", 16);
         if (B.contains(var61)) {
            var1.I = var61;
         }
      } catch (RuntimeException var26) {
      }

      try {
         String var62 = km.a(var0, "clickGuiEnabledStyle", 16);
         if (n.contains(var62)) {
            var1.Fw = var62;
         }
      } catch (RuntimeException var25) {
      }

      try {
         String var63 = km.a(var0, "clickGuiSwitchStyle", 16);
         if (A.contains(var63)) {
            var1.S = var63;
         }
      } catch (RuntimeException var24) {
      }

      try {
         String var64 = km.a(var0, "clickGuiSliderStyle", 16);
         if (b.contains(var64)) {
            var1.T = var64;
         }
      } catch (RuntimeException var23) {
      }

      try {
         if (var0.has("clickGuiRounding")) {
            km.i(var0.get("clickGuiRounding"), 0.0, 2.0, false);
            var1.O = var0.get("clickGuiRounding").getAsDouble();
         }
      } catch (RuntimeException var22) {
      }

      try {
         if (var0.has("clickGuiAccentGlow")) {
            var1.N = km.t(var0.get("clickGuiAccentGlow"));
         }
      } catch (RuntimeException var21) {
      }

      try {
         if (var0.has("clickGuiRipple")) {
            var1.s = km.t(var0.get("clickGuiRipple"));
         }
      } catch (RuntimeException var20) {
      }

      try {
         if (var0.has("clickGuiHoverLift")) {
            var1.U = km.t(var0.get("clickGuiHoverLift"));
         }
      } catch (RuntimeException var19) {
      }

      try {
         var1.FH = t2.k(km.a(var0, "background", 32));
      } catch (RuntimeException var18) {
      }

      try {
         if (var0.has("softParticles")) {
            var1.u = km.t(var0.get("softParticles"));
         }
      } catch (RuntimeException var17) {
      }

      try {
         var1.V = wa.a.valueOf(km.a(var0, "effectsQuality", 16));
      } catch (RuntimeException var16) {
      }

      try {
         var1.L = km.t(var0.get("motion"));
      } catch (RuntimeException var15) {
      }

      try {
         JsonArray var65 = var0.getAsJsonArray("favorites");
         if (var65.size() <= 1000) {
            for (JsonElement var4 : var65) {
               try {
                  var1.x(km.r(var4.getAsString()), true);
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
                  km.i((JsonElement)var74.getValue(), 1.0, 9.223372E18F, true);
                  var1.u((String)var74.getKey(), ((JsonElement)var74.getValue()).getAsLong());
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var50) {
      }

      try {
         var1.x = km.t(var0.get("moduleFavoritesOpen"));
      } catch (RuntimeException var12) {
      }

      try {
         JsonArray var67 = var0.getAsJsonArray("hudLocks");
         if (var67.size() <= 128) {
            for (JsonElement var75 : var67) {
               try {
                  if (var75.isJsonPrimitive() && var75.getAsJsonPrimitive().isString()) {
                     var1.K(var75.getAsString(), true);
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
                     var1.e(var76.getAsString(), true);
                  }
               } catch (RuntimeException var10) {
               }
            }
         }
      } catch (RuntimeException var48) {
      }

      try {
         if (var0.has("settingBinds")) {
            var1.K = wr.v(var0.get("settingBinds"));
         }
      } catch (RuntimeException var9) {
         com.larpclient.h.g("Input", "Invalid setting shortcuts ignored: {}", var9.toString());
      }

      try {
         if (var0.has("legacyClickGui")) {
            var1.P = sv.n(var0.getAsJsonObject("legacyClickGui"));
         }
      } catch (RuntimeException var8) {
         com.larpclient.h.g("GUI", "Invalid legacy ClickGUI preference ignored: {}", var8.toString());
      }

      try {
         if (var0.has("israelMode")) {
            var1.d = u3.v(var0.getAsJsonObject("israelMode"));
         }
      } catch (RuntimeException var7) {
         com.larpclient.h.g("Israel mode", "Invalid local preference ignored: {}", var7.toString());
      }

      String var69 = var0.has("larpPreset") ? "larpPreset" : "moonMode";

      try {
         if (var0.has(var69)) {
            var1.p = kf.z(var0.getAsJsonObject(var69));
            if (!var0.has("larpPreset")) {
               com.larpclient.h.U("Larp Preset", "Loaded legacy cosmetic preset journal; next preference save uses larpPreset");
            }
         }
      } catch (RuntimeException var6) {
         com.larpclient.h.g("Larp Preset", "Invalid local preference ignored: {}", var6.toString());
      }

      return var1;
   }

   static {
      String[] var5 = new String[45];
      int var3 = 0;
      String var2 = "AV@V'\\EKhF@\"\u007fYL]\u0006P_JP\"o\u0011AV@V'\\EKyHG(HD[VL\u0018AV@V'\\EKxHV'\u007fBMJ{P:rCKUG\u0013AV@V'\\EKiE\\(~BqNPY)\u000fAV@V'\\EKuYT/rD[\bJOMy#x[Q\u0010AV@V'\\EK}[T(rULN\u0011WJMT8~cG_Gg)xUKJ]\fN_N\\8V_F_bP5\u0013OUM@ ~vCLFG%oUQuYP\"\u000bO_G@\u001dnQNS]L\u000fFSZV#iTrHLF)uSG\u0010AV@V'\\EKxE@>ZBG[\bO_G@\u0018r]G\u0011AV@V'\\EKrFC)i|K\\]\rQUOA\u001czBVSJY)h\tN_N\\8V_F_\u0014AV@V'\\EK\u007fGT.wUFi]L ~\u0012AV@V'\\EK{JV)uDeVFB\rAV@V'\\EKv@R$o\nO_G@\u001f~QQUG\u000eN_NT/bsNSJ^\u000bnY\u0006OU]\\#u\u0010AV@V'\\EK~\\G-oYMT\nAOZA#v}GT\\\rASGP!zDKYdP\"n\u0010AV@V'\\EKxHV'\u007fBMJ\n@[J^+i_WTM\tD[_Z>rDGI\u0011AV@V'\\EK{G\\!zDKUG\u0016AV@V'\\EKyEZ?~qLSDT8r_L\u000fOUM@ ~vCLFG%oUQ\nKI[T)w}M^L\u000eAV@V'\\EKh@E<wU\u0013AV@V'\\EKi^\\8xXqNPY)\u000bAV@V'\\EK~@X\rAV@V'\\EKiJT ~\nN[[E\u001ciUQ_]\u000eG\\OP/oCsOHY%oI\fQ_]A%uW`SGQ?\u000fWJMT8~qWNFv$~SI\tQQPf#nBA_";
      int var4 = "AV@V'\\EKhF@\"\u007fYL]\u0006P_JP\"o\u0011AV@V'\\EKyHG(HD[VL\u0018AV@V'\\EKxHV'\u007fBMJ{P:rCKUG\u0013AV@V'\\EKiE\\(~BqNPY)\u000fAV@V'\\EKuYT/rD[\bJOMy#x[Q\u0010AV@V'\\EK}[T(rULN\u0011WJMT8~cG_Gg)xUKJ]\fN_N\\8V_F_bP5\u0013OUM@ ~vCLFG%oUQuYP\"\u000bO_G@\u001dnQNS]L\u000fFSZV#iTrHLF)uSG\u0010AV@V'\\EKxE@>ZBG[\bO_G@\u0018r]G\u0011AV@V'\\EKrFC)i|K\\]\rQUOA\u001czBVSJY)h\tN_N\\8V_F_\u0014AV@V'\\EK\u007fGT.wUFi]L ~\u0012AV@V'\\EK{JV)uDeVFB\rAV@V'\\EKv@R$o\nO_G@\u001f~QQUG\u000eN_NT/bsNSJ^\u000bnY\u0006OU]\\#u\u0010AV@V'\\EK~\\G-oYMT\nAOZA#v}GT\\\rASGP!zDKYdP\"n\u0010AV@V'\\EKxHV'\u007fBMJ\n@[J^+i_WTM\tD[_Z>rDGI\u0011AV@V'\\EK{G\\!zDKUG\u0016AV@V'\\EKyEZ?~qLSDT8r_L\u000fOUM@ ~vCLFG%oUQ\nKI[T)w}M^L\u000eAV@V'\\EKh@E<wU\u0013AV@V'\\EKi^\\8xXqNPY)\u000bAV@V'\\EK~@X\rAV@V'\\EKiJT ~\nN[[E\u001ciUQ_]\u000eG\\OP/oCsOHY%oI\fQ_]A%uW`SGQ?\u000fWJMT8~qWNFv$~SI\tQQPf#nBA_"
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
                     a = var5;
                     i = List.of("Reverse", "Shatter", "Crumble", "Slice", "Implode", "CRT");
                     v = List.of("Aurora", "Nebula", "Waves", "Gradient", "Dim", "None");
                     j = List.of("Window", "Everywhere");
                     B = List.of("Filled", "Outline", "Flat");
                     n = List.of("Glow", "Tint", "Text", "None");
                     A = List.of("Pill", "Square", "Minimal");
                     b = List.of("Pill", "Minimal", "Classic");
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

                  var2 = "AV@V'\\EKxE@>\u0014WJMT8~cISYE)\u007fbGVLT?~";
                  var4 = "AV@V'\\EKxE@>\u0014WJMT8~cISYE)\u007fbGVLT?~".length();
                  var1 = '\f';
                  var7 = -1;
            }

            var10000 = var2.substring(++var7, var7 + var1);
            var10001 = 0;
         }
      }
   }
}
