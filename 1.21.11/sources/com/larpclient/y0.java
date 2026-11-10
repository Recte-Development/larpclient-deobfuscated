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

public final class y0 {
   public tl t;
   public boolean k;
   public boolean N;
   public String o;
   public String x;
   public boolean G4;
   public boolean u;
   public int e;
   public td.a s;
   public td.r U;
   public double w;
   public boolean z;
   public boolean A;
   public boolean Gr;
   public boolean O;
   public String K;
   public static final List<String> h;
   public String R;
   public int G;
   public double G7;
   public static final double E = 0.5;
   public static final double H = 2.0;
   public double f;
   public static final List<String> Y;
   public String L;
   public static final double r = 0.9;
   public double X;
   public static final int W = 4;
   public int p;
   public static final List<String> C;
   public String T;
   public static final List<String> v;
   public static final List<String> b;
   public static final List<String> V;
   public static final List<String> D;
   public static final double q = 2.0;
   public String F;
   public String J;
   public String y;
   public String GE;
   public double P;
   public boolean S;
   public boolean i;
   public boolean g;
   private static final int c = 2;
   public boolean l;
   public t_.t Z;
   public String m;
   public f_ B;
   public eq d;
   public je j;
   public ec M;
   private final Set<String> a;
   private final Set<String> Q;
   private final Set<String> GT;
   public boolean I;
   private final Map<String, Long> GV;
   public static int n;
   private static final String[] ab;

   public y0() {
      this.t = tl.t;
      this.k = (boolean)1;
      this.N = (boolean)1;
      this.o = "";
      this.x = "";
      this.G4 = (boolean)1;
      this.e = 47;
      this.s = td.a.E;
      this.U = td.r.g;
      this.w = 0.33;
      this.z = (boolean)1;
      this.A = (boolean)1;
      this.K = "Fade";
      this.R = "Reverse";
      this.G = 280;
      this.G7 = 0.67;
      this.f = 1.0;
      this.L = "None";
      this.X = 0.42;
      this.p = 2;
      this.T = "Window";
      this.F = "Filled";
      this.J = "Glow";
      this.y = "Pill";
      this.GE = "Pill";
      this.P = 1.0;
      this.S = (boolean)1;
      this.i = (boolean)1;
      this.g = (boolean)1;
      this.l = (boolean)1;
      this.Z = t_.t.Q;
      this.m = "World";
      this.B = new f_();
      this.d = new eq();
      this.j = new je();
      this.M = new ec();
      this.a = new LinkedHashSet<>();
      this.Q = new LinkedHashSet<>();
      this.GT = new LinkedHashSet<>();
      this.GV = new LinkedHashMap<>();
   }

   public ez.j S() {
      return new ez.j(this.F, this.J, this.y, this.GE, this.P, this.S, this.i, this.g);
   }

   public boolean j(String var1) {
      return this.GT.contains(var1);
   }

   public void J(String var1, boolean var2) {
      if (var1 != null && var1.matches("[a-z][a-z0-9_-]{0,63}")) {
         if (var2 && this.GT.size() < 128) {
            this.GT.add(var1);
         } else if (!var2) {
            this.GT.remove(var1);
         }
      } else {
         throw new IllegalArgumentException("Invalid HUD identifier");
      }
   }

   public boolean H(String var1) {
      return this.Q.contains(var1) || "CustomSky".equals(var1) && this.Q.contains("Sky Shader");
   }

   public void p(String var1, boolean var2) {
      if (var1 != null && var1.matches("[A-Za-z0-9 _-]{1,128}") && !var1.isBlank()) {
         if (var2) {
            if (this.Q.size() >= 1000 && !this.Q.contains(var1)) {
               return;
            }

            this.Q.add(var1);
         } else {
            this.Q.remove(var1);
            if (var1.equals("CustomSky")) {
               this.Q.remove("Sky Shader");
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   public boolean i(String var1) {
      return this.a.contains(var1);
   }

   public void n(String var1, boolean var2) {
      com.larpclient.h.Y(var1);
      if (var2) {
         this.a.add(var1);
      } else {
         this.a.remove(var1);
      }
   }

   public long A(String var1) {
      return this.GV.getOrDefault(var1, 0L);
   }

   public void n(String var1, long var2) {
      com.larpclient.h.Y(var1);
      if (var2 > 0L) {
         this.GV.put(var1, var2);
      }
   }

   public void d(String var1) {
      this.a.remove(var1);
      this.GV.remove(var1);
   }

   public void H(String var1, String var2) {
      com.larpclient.h.Y(var2);
      this.B.e(var1, var2);
      this.d.s(var1, var2);
      if (this.a.remove(var1)) {
         this.a.add(var2);
      }

      Long var3 = this.GV.remove(var1);
      if (var3 != null) {
         this.GV.put(var2, var3);
      }
   }

   public List<String> P(List<String> var1, String var2, String var3) {
      Stream var4 = var1.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var2.toLowerCase(Locale.ROOT)));
      if (var3.equals("Favorites")) {
         var4 = var4.filter(this::i);
      }

      if (var3.equals("Recent")) {
         var4 = var4.filter(var1x -> this.A(var1x) > 0L).sorted(Comparator.comparingLong(this::A).reversed().thenComparing(Comparator.naturalOrder()));
      }

      return var4.toList();
   }

   public JsonObject K() {
      JsonObject var2 = new JsonObject();
      String[] var7 = ab;
      var2.addProperty(var7[21], this.t.X);
      var2.addProperty(var7[24], this.k);
      var2.addProperty(var7[38], this.N);
      var2.addProperty(var7[35], this.o);
      var2.addProperty(var7[14], this.x);
      var2.addProperty(var7[0], this.s.name());
      var2.addProperty(var7[40], this.U.name());
      var2.addProperty(var7[27], this.w);
      var2.addProperty(var7[9], this.z);
      var2.addProperty(var7[4], this.A);
      var2.addProperty(var7[8], this.G4);
      int var10000 = n;
      var2.addProperty(var7[23], this.u);
      var2.addProperty(var7[42], this.e);
      var2.addProperty(var7[12], this.l);
      var2.addProperty(var7[28], this.Z.name());
      var2.addProperty(var7[32], this.m);
      var2.addProperty(var7[17], this.Gr);
      var2.addProperty(var7[20], this.O);
      var2.addProperty(var7[10], this.K);
      var2.addProperty(var7[41], this.R);
      var2.addProperty(var7[15], this.G);
      var2.addProperty(var7[22], this.G7);
      var2.addProperty(var7[36], this.f);
      var2.addProperty(var7[37], this.L);
      var2.addProperty(var7[34], 2);
      int var1 = var10000;
      var2.addProperty(var7[18], this.X);
      var2.addProperty(var7[31], this.p);
      var2.addProperty(var7[33], this.T);
      var2.addProperty(var7[6], this.F);
      var2.addProperty(var7[11], this.J);
      var2.addProperty(var7[29], this.y);
      var2.addProperty(var7[39], this.GE);
      var2.addProperty(var7[3], this.P);
      var2.addProperty(var7[25], this.S);
      var2.addProperty(var7[16], this.i);
      var2.addProperty(var7[2], this.g);
      JsonArray var3 = new JsonArray();
      this.a.forEach(var3::add);
      var2.add(var7[13], var3);
      JsonObject var4 = new JsonObject();
      this.GV.forEach(var4::addProperty);
      var2.add(var7[30], var4);
      JsonArray var5 = new JsonArray();
      this.Q.forEach(var5::add);
      var2.add(var7[1], var5);
      var2.addProperty(var7[26], this.I);
      JsonArray var6 = new JsonArray();
      this.GT.forEach(var6::add);
      var2.add(var7[7], var6);
      var2.add(var7[43], this.B.z());
      var2.add(var7[5], this.d.z());
      var2.add(var7[44], this.j.y());
      var2.add(var7[19], this.M.t());
      if (var1 != 0) {
         qk.F = (boolean)(qk.F ? 0 : 1);
      }

      return var2;
   }

   public static y0 O(JsonObject var0) {
      y0 var1 = new y0();

      try {
         if (var0.has("updateAutoCheck")) {
            var1.N = com.larpclient.h.o(var0.get("updateAutoCheck"));
         }
      } catch (RuntimeException var47) {
      }

      try {
         String var2 = com.larpclient.h.f(var0, "updateSkippedRelease", 128);
         if (var2.isEmpty() || var2.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.o = var2;
         }
      } catch (RuntimeException var54) {
      }

      try {
         String var55 = com.larpclient.h.f(var0, "updateSeenReceipt", 257);
         if (var55.isEmpty() || var55.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}:[A-Za-z0-9][A-Za-z0-9._-]{0,127}")) {
            var1.x = var55;
         }
      } catch (RuntimeException var53) {
      }

      try {
         if (var0.has("discordPresence")) {
            var1.G4 = com.larpclient.h.o(var0.get("discordPresence"));
         }
      } catch (RuntimeException var46) {
      }

      try {
         if (var0.has("legitMode")) {
            var1.u = com.larpclient.h.o(var0.get("legitMode"));
         }
      } catch (RuntimeException var45) {
      }

      try {
         if (var0.has("legitModeKey")) {
            com.larpclient.h.X(var0.get("legitModeKey"), 32.0, 348.0, true);
            var1.e = var0.get("legitModeKey").getAsInt();
         }
      } catch (RuntimeException var44) {
      }

      try {
         String var56 = com.larpclient.h.f(var0, "skySource", 16);
         if (Set.of("World", "Shader").contains(var56)) {
            var1.m = var56;
         }
      } catch (RuntimeException var43) {
      }

      try {
         var1.s = td.a.valueOf(com.larpclient.h.f(var0, "menuSeason", 16));
      } catch (RuntimeException var42) {
      }

      try {
         var1.U = td.r.valueOf(com.larpclient.h.f(var0, "menuQuality", 16));
      } catch (RuntimeException var41) {
      }

      try {
         com.larpclient.h.X(var0.get("menuTime"), 0.0, 1.0, false);
         var1.w = var0.get("menuTime").getAsDouble();
      } catch (RuntimeException var40) {
      }

      try {
         if (var0.has("cinematicMenu")) {
            var1.z = com.larpclient.h.o(var0.get("cinematicMenu"));
         }
      } catch (RuntimeException var39) {
      }

      try {
         if (var0.has("customMenu")) {
            var1.A = com.larpclient.h.o(var0.get("customMenu"));
         }
      } catch (RuntimeException var38) {
      }

      try {
         if (var0.has("clickGuiGradient")) {
            var1.O = com.larpclient.h.o(var0.get("clickGuiGradient"));
         }
      } catch (RuntimeException var37) {
      }

      try {
         if (var0.has("clickGuiLight")) {
            var1.Gr = com.larpclient.h.o(var0.get("clickGuiLight"));
         }
      } catch (RuntimeException var36) {
      }

      try {
         String var57 = com.larpclient.h.f(var0, "clickGuiAnimation", 16);
         if (sd.I.contains(var57)) {
            var1.K = var57;
         }
      } catch (RuntimeException var35) {
      }

      try {
         String var58 = com.larpclient.h.f(var0, "clickGuiCloseAnimation", 16);
         if (h.contains(var58)) {
            var1.R = var58;
         }
      } catch (RuntimeException var34) {
      }

      try {
         com.larpclient.h.X(var0.get("clickGuiDuration"), 100.0, 1000.0, true);
         var1.G = var0.get("clickGuiDuration").getAsInt();
      } catch (RuntimeException var33) {
      }

      try {
         com.larpclient.h.X(var0.get("clickGuiOpacity"), 0.0, 1.0, false);
         var1.G7 = var0.get("clickGuiOpacity").getAsDouble();
      } catch (RuntimeException var32) {
      }

      try {
         if (var0.has("clickGuiScale")) {
            com.larpclient.h.X(var0.get("clickGuiScale"), 0.5, 2.0, false);
            var1.f = var0.get("clickGuiScale").getAsDouble();
         }
      } catch (RuntimeException var31) {
      }

      try {
         String var59 = com.larpclient.h.f(var0, "clickGuiBackdrop", 16);
         byte var3 = 0;

         try {
            var3 = (byte)(var0.get("clickGuiBackdropRevision").getAsInt() >= 2 ? 1 : 0);
         } catch (RuntimeException var30) {
         }

         if (var59.equals("Aurora") && var3 == 0) {
            q8.v("GUI", "Moved the former default Aurora backdrop to None");
         } else if (Y.contains(var59)) {
            var1.L = var59;
         }
      } catch (RuntimeException var52) {
      }

      try {
         if (var0.has("clickGuiDim")) {
            com.larpclient.h.X(var0.get("clickGuiDim"), 0.0, 0.9, false);
            var1.X = var0.get("clickGuiDim").getAsDouble();
         }
      } catch (RuntimeException var29) {
      }

      try {
         if (var0.has("clickGuiBlur")) {
            com.larpclient.h.X(var0.get("clickGuiBlur"), 0.0, 4.0, true);
            var1.p = var0.get("clickGuiBlur").getAsInt();
         }
      } catch (RuntimeException var28) {
      }

      try {
         String var60 = com.larpclient.h.f(var0, "clickGuiBlurArea", 16);
         if (C.contains(var60)) {
            var1.T = var60;
         }
      } catch (RuntimeException var27) {
      }

      try {
         String var61 = com.larpclient.h.f(var0, "clickGuiCardStyle", 16);
         if (v.contains(var61)) {
            var1.F = var61;
         }
      } catch (RuntimeException var26) {
      }

      try {
         String var62 = com.larpclient.h.f(var0, "clickGuiEnabledStyle", 16);
         if (b.contains(var62)) {
            var1.J = var62;
         }
      } catch (RuntimeException var25) {
      }

      try {
         String var63 = com.larpclient.h.f(var0, "clickGuiSwitchStyle", 16);
         if (V.contains(var63)) {
            var1.y = var63;
         }
      } catch (RuntimeException var24) {
      }

      try {
         String var64 = com.larpclient.h.f(var0, "clickGuiSliderStyle", 16);
         if (D.contains(var64)) {
            var1.GE = var64;
         }
      } catch (RuntimeException var23) {
      }

      try {
         if (var0.has("clickGuiRounding")) {
            com.larpclient.h.X(var0.get("clickGuiRounding"), 0.0, 2.0, false);
            var1.P = var0.get("clickGuiRounding").getAsDouble();
         }
      } catch (RuntimeException var22) {
      }

      try {
         if (var0.has("clickGuiAccentGlow")) {
            var1.S = com.larpclient.h.o(var0.get("clickGuiAccentGlow"));
         }
      } catch (RuntimeException var21) {
      }

      try {
         if (var0.has("clickGuiRipple")) {
            var1.i = com.larpclient.h.o(var0.get("clickGuiRipple"));
         }
      } catch (RuntimeException var20) {
      }

      try {
         if (var0.has("clickGuiHoverLift")) {
            var1.g = com.larpclient.h.o(var0.get("clickGuiHoverLift"));
         }
      } catch (RuntimeException var19) {
      }

      try {
         var1.t = tl.S(com.larpclient.h.f(var0, "background", 32));
      } catch (RuntimeException var18) {
      }

      try {
         if (var0.has("softParticles")) {
            var1.l = com.larpclient.h.o(var0.get("softParticles"));
         }
      } catch (RuntimeException var17) {
      }

      try {
         var1.Z = t_.t.valueOf(com.larpclient.h.f(var0, "effectsQuality", 16));
      } catch (RuntimeException var16) {
      }

      try {
         var1.k = com.larpclient.h.o(var0.get("motion"));
      } catch (RuntimeException var15) {
      }

      try {
         JsonArray var65 = var0.getAsJsonArray("favorites");
         if (var65.size() <= 1000) {
            for (JsonElement var4 : var65) {
               try {
                  var1.n(com.larpclient.h.Y(var4.getAsString()), true);
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
                  com.larpclient.h.X((JsonElement)var74.getValue(), 1.0, 9.223372E18F, true);
                  var1.n((String)var74.getKey(), ((JsonElement)var74.getValue()).getAsLong());
               } catch (RuntimeException var13) {
               }
            }
         }
      } catch (RuntimeException var50) {
      }

      try {
         var1.I = com.larpclient.h.o(var0.get("moduleFavoritesOpen"));
      } catch (RuntimeException var12) {
      }

      try {
         JsonArray var67 = var0.getAsJsonArray("hudLocks");
         if (var67.size() <= 128) {
            for (JsonElement var75 : var67) {
               try {
                  if (var75.isJsonPrimitive() && var75.getAsJsonPrimitive().isString()) {
                     var1.J(var75.getAsString(), true);
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
                     var1.p(var76.getAsString(), true);
                  }
               } catch (RuntimeException var10) {
               }
            }
         }
      } catch (RuntimeException var48) {
      }

      try {
         if (var0.has("settingBinds")) {
            var1.j = je.M(var0.get("settingBinds"));
         }
      } catch (RuntimeException var9) {
         q8.L("Input", "Invalid setting shortcuts ignored: {}", var9.toString());
      }

      try {
         if (var0.has("legacyClickGui")) {
            var1.M = ec.Z(var0.getAsJsonObject("legacyClickGui"));
         }
      } catch (RuntimeException var8) {
         q8.L("GUI", "Invalid legacy ClickGUI preference ignored: {}", var8.toString());
      }

      try {
         if (var0.has("israelMode")) {
            var1.B = f_.f(var0.getAsJsonObject("israelMode"));
         }
      } catch (RuntimeException var7) {
         q8.L("Israel mode", "Invalid local preference ignored: {}", var7.toString());
      }

      String var69 = var0.has("larpPreset") ? "larpPreset" : "moonMode";

      try {
         if (var0.has(var69)) {
            var1.d = eq.w(var0.getAsJsonObject(var69));
            if (!var0.has("larpPreset")) {
               q8.v("Larp Preset", "Loaded legacy cosmetic preset journal; next preference save uses larpPreset");
            }
         }
      } catch (RuntimeException var6) {
         q8.L("Larp Preset", "Invalid local preference ignored: {}", var6.toString());
      }

      return var1;
   }

   static {
      String[] var5 = new String[45];
      int var3 = 0;
      String var2 = "\u000ftM\u00005W\u001f\u0011~M\u000f\u000f~G\u0000\nW8\u0003gL\u0007\u000fF\u001b\u0011\u0011\u0001}J\u0016\ru\u000b\u000bYL\u0003\u0003@2\u000bwW\u0010\u0001}J\u0016\ru\u000b\u000bCL\u0000\bV\u0017\fv\n\u0001dP\u0001\t_3\u0007\u007fV\n\u000epQ\u00056@\u001b\u0011tW\u0011\u0001}J\u0016\ru\u000b\u000bRB\u0007\u0002a\n\u001b}F\b\ndG9\tQ\u0015\u0011\u000f\u0006xP\u0016\t@\u001a2cF\u0006\u0003\\\u001d\u0007\r\u0001xM\u0010\u000bS\n\u000brn\u0010\bG\u0011\u0001}J\u0016\ru\u000b\u000bPM\u001c\u000bS\n\u000b~M\u0014\u0001}J\u0016\ru\u000b\u000bTM\u0014\u0004^\u001b\u0006BW\f\nW\r\u0011~E\u00016S\f\u0016x@\u0019\u0003A\t\u0004pU\u001a\u0014[\n\u0007b\u0011\u0017aG\u0014\u0012W-\u0007tM'\u0003Q\u001b\u000baW\u0010\u0001}J\u0016\ru\u000b\u000bUV\u0007\u0007F\u0017\r\u007f\u000e\u0001}J\u0016\ru\u000b\u000bCJ\u0005\u0016^\u001b\r\u0001}J\u0016\ru\u000b\u000b]J\u0012\u000eF\u000b\u0001}J\u0016\ru\u000b\u000bUJ\u0018\u000e\u000etD\u0014\u0005K=\u000ex@\u001e!G\u0017\u0010\u0001}J\u0016\ru\u000b\u000bVQ\u0014\u0002[\u001b\fe\n\u0000p@\u001e\u0001@\u0011\u0017\u007fG\u000f\u0001}J\u0016\ru\u000b\u000b^S\u0014\u0005[\n\u001b\t\u000etD\u001c\u0012\u007f\u0011\u0006t\u0006\u000f~W\u001c\t\\\u0012\u0001}J\u0016\ru\u000b\u000bP@\u0016\u0003\\\n%}L\u0002\u0013\u000f~G\u0000\nW8\u0003gL\u0007\u000fF\u001b\u0011^S\u0010\b\b\u000ftM\u00002[\u0013\u0007\u000e\u0007wE\u0010\u0005F\r3dB\u0019\u000fF\u0007\u0013\u0001}J\u0016\ru\u000b\u000bBT\u001c\u0012Q\u00161eZ\u0019\u0003\u0006\u0010t@\u0010\bF\f\u0001}J\u0016\ru\u000b\u000bSO\u0000\u0014\t\u0011zZ&\tG\f\u0001t\u0010\u0001}J\u0016\ru\u000b\u000bSO\u0000\u0014s\f\u0007p\u0018\u0001}J\u0016\ru\u000b\u000bSB\u0016\rV\f\raq\u0010\u0010[\r\u000b~M\u0014\u0017aG\u0014\u0012W-\txS\u0005\u0003V,\u0007}F\u0014\u0015W\r\u0001}J\u0016\ru\u000b\u000bB@\u0014\nW\u0010\u0001}J\u0016\ru\u000b\u000bSB\u0016\rV\f\ra\u000f\u0017aG\u0014\u0012W?\u0017eL6\u000eW\u001d\t\u0013\u0001}J\u0016\ru\u000b\u000bBO\u001c\u0002W\f1eZ\u0019\u0003\u000b\u000ftM\u00007G\u001f\u000exW\f\u0016\u0001}J\u0016\ru\u000b\u000bRO\u001a\u0015W?\fxN\u0014\u0012[\u0011\f\f\u000etD\u001c\u0012\u007f\u0011\u0006th\u0010\u001f";
      int var4 = "\u000ftM\u00005W\u001f\u0011~M\u000f\u000f~G\u0000\nW8\u0003gL\u0007\u000fF\u001b\u0011\u0011\u0001}J\u0016\ru\u000b\u000bYL\u0003\u0003@2\u000bwW\u0010\u0001}J\u0016\ru\u000b\u000bCL\u0000\bV\u0017\fv\n\u0001dP\u0001\t_3\u0007\u007fV\n\u000epQ\u00056@\u001b\u0011tW\u0011\u0001}J\u0016\ru\u000b\u000bRB\u0007\u0002a\n\u001b}F\b\ndG9\tQ\u0015\u0011\u000f\u0006xP\u0016\t@\u001a2cF\u0006\u0003\\\u001d\u0007\r\u0001xM\u0010\u000bS\n\u000brn\u0010\bG\u0011\u0001}J\u0016\ru\u000b\u000bPM\u001c\u000bS\n\u000b~M\u0014\u0001}J\u0016\ru\u000b\u000bTM\u0014\u0004^\u001b\u0006BW\f\nW\r\u0011~E\u00016S\f\u0016x@\u0019\u0003A\t\u0004pU\u001a\u0014[\n\u0007b\u0011\u0017aG\u0014\u0012W-\u0007tM'\u0003Q\u001b\u000baW\u0010\u0001}J\u0016\ru\u000b\u000bUV\u0007\u0007F\u0017\r\u007f\u000e\u0001}J\u0016\ru\u000b\u000bCJ\u0005\u0016^\u001b\r\u0001}J\u0016\ru\u000b\u000b]J\u0012\u000eF\u000b\u0001}J\u0016\ru\u000b\u000bUJ\u0018\u000e\u000etD\u0014\u0005K=\u000ex@\u001e!G\u0017\u0010\u0001}J\u0016\ru\u000b\u000bVQ\u0014\u0002[\u001b\fe\n\u0000p@\u001e\u0001@\u0011\u0017\u007fG\u000f\u0001}J\u0016\ru\u000b\u000b^S\u0014\u0005[\n\u001b\t\u000etD\u001c\u0012\u007f\u0011\u0006t\u0006\u000f~W\u001c\t\\\u0012\u0001}J\u0016\ru\u000b\u000bP@\u0016\u0003\\\n%}L\u0002\u0013\u000f~G\u0000\nW8\u0003gL\u0007\u000fF\u001b\u0011^S\u0010\b\b\u000ftM\u00002[\u0013\u0007\u000e\u0007wE\u0010\u0005F\r3dB\u0019\u000fF\u0007\u0013\u0001}J\u0016\ru\u000b\u000bBT\u001c\u0012Q\u00161eZ\u0019\u0003\u0006\u0010t@\u0010\bF\f\u0001}J\u0016\ru\u000b\u000bSO\u0000\u0014\t\u0011zZ&\tG\f\u0001t\u0010\u0001}J\u0016\ru\u000b\u000bSO\u0000\u0014s\f\u0007p\u0018\u0001}J\u0016\ru\u000b\u000bSB\u0016\rV\f\raq\u0010\u0010[\r\u000b~M\u0014\u0017aG\u0014\u0012W-\txS\u0005\u0003V,\u0007}F\u0014\u0015W\r\u0001}J\u0016\ru\u000b\u000bB@\u0014\nW\u0010\u0001}J\u0016\ru\u000b\u000bSB\u0016\rV\f\ra\u000f\u0017aG\u0014\u0012W?\u0017eL6\u000eW\u001d\t\u0013\u0001}J\u0016\ru\u000b\u000bBO\u001c\u0002W\f1eZ\u0019\u0003\u000b\u000ftM\u00007G\u001f\u000exW\f\u0016\u0001}J\u0016\ru\u000b\u000bRO\u001a\u0015W?\fxN\u0014\u0012[\u0011\f\f\u000etD\u001c\u0012\u007f\u0011\u0006th\u0010\u001f"
         .length();
      char var1 = '\n';
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
                     h = List.of("Reverse", "Shatter", "Crumble", "Slice", "Implode", "CRT");
                     Y = List.of("Aurora", "Nebula", "Waves", "Gradient", "Dim", "None");
                     C = List.of("Window", "Everywhere");
                     v = List.of("Filled", "Outline", "Flat");
                     b = List.of("Glow", "Tint", "Text", "None");
                     V = List.of("Pill", "Square", "Minimal");
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

                  var2 = "\u000bbQ\u0014\u0003^3\ruF\f\u0011tW\u0001\u000f\\\u0019 xM\u0011\u0015";
                  var4 = "\u000bbQ\u0014\u0003^3\ruF\f\u0011tW\u0001\u000f\\\u0019 xM\u0011\u0015".length();
                  var1 = '\n';
                  var7 = -1;
            }

            var10000 = var2.substring(++var7, var7 + var1);
            var10001 = 0;
         }
      }
   }
}
