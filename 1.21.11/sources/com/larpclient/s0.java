package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494.q8569136e5ccd2c8f;
import com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59.q5a3f607535c664c2;
import com.larpclient.module.StaffListModule;
import com.larpclient.module.aj;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_10185;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_156;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2338;
import net.minecraft.class_2349;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2745;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3708;
import net.minecraft.class_3965;
import net.minecraft.class_4050;
import net.minecraft.class_408;
import net.minecraft.class_412;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_465;
import net.minecraft.class_500;
import net.minecraft.class_634;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_7202;
import net.minecraft.class_7743;
import net.minecraft.class_8881;
import net.minecraft.class_8886;
import net.minecraft.class_8898;
import net.minecraft.class_9334;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_2350.class_2353;
import net.minecraft.class_239.class_240;

public final class s0 extends s4 implements tc, fk.h {
   private static s0 mm;
   private static boolean L6;
   static final int ka = 4096;
   static final int gO = 2048;
   static final int ml = 24;
   static final int o = 12;
   static final double Lu = 0.3;
   private static final int p = 600;
   static final int m4 = 3;
   static final int mx = 40;
   static final int gJ = 40;
   static final int mn = 400;
   static final int mP = 3;
   static final int mT = 2400;
   static final int LF = 20;
   static final int qp = 8;
   static final int gj = 1200;
   static final long mO = 300000L;
   static final int n = 3;
   static final long mN = 5000L;
   static final long a = 10000L;
   static final long q0 = 120000L;
   static final int LG = 3;
   static final long kt = 3000L;
   private static final int k_ = 0;
   private static final int qJ = 1;
   private static final int kh = 2;
   private static final int mv = 3;
   static final int me = 16000;
   static final int k1 = 4;
   static final double R = 32.0;
   static final int S = -1000000000;
   static final int Z = 10;
   static final int gV = 200000;
   static final float q_ = 2.0F;
   static final int L2 = 100;
   static final int gw = 6;
   static final int gu = 900;
   static final int gs = 60;
   static final int m5 = 400;
   static final int mt = 32;
   static final int LZ = 10;
   static final double k2 = 35.0;
   static final int kT = 8;
   static final int mQ = 600;
   static final int kB = 3;
   static final float gH = 1.6F;
   static final float mr = 6.0F;
   static final float mk = 26.0F;
   static final int kz = 600;
   static final int mF = 80;
   static final int mB = 200;
   static final int Ly = 24;
   static final int b = 3;
   static final int LH = 80;
   static final int mi = 160;
   static final int m9 = 3;
   static final int gE = 120;
   static final double gd = 0.66;
   static final double kI = 0.54;
   static final double Lv = 0.08;
   static final double gt = 0.25;
   static final int qZ = 64;
   static final int L8 = 3;
   static final int L_ = 3;
   static final int k7 = 120;
   static final int LC = 2;
   static final int gX = 1200;
   static final int j = 200;
   static final int q2 = 400;
   static final int Lm = 400;
   static final int mG = 2;
   static final int kl = 2;
   static final int ko = 400;
   static final int qC = 3;
   static final double kC = 0.6;
   private final aj m = this.addSetting(
      new aj("Schematic", "File name in the schematics folder (a .litematic or .schem file). The list at the top picks one", "").A("house.litematic")
   );
   private final aj mJ = this.addSetting(
      new aj("Origin", "Where the schematic's origin goes, as x y z. The buttons under Set where it goes set it", "").A("x y z")
   );
   private final com.larpclient.module.li gp = this.addSetting(
      new com.larpclient.module.li("Rotation", "Turn the schematic clockwise around its origin, seen from above", "0", "0", "90", "180", "270")
   );
   private final com.larpclient.module.li Ls = this.addSetting(
      new com.larpclient.module.li("Mirror", "Mirror the schematic first: X flips east and west, Z flips north and south", "None", "None", "X", "Z")
   );
   private final com.larpclient.module.l3 qB = this.G(
      "Auto Move", "Walk to blocks that are out of reach. Off: only blocks within reach of where you stand", true
   );
   private final com.larpclient.module.l3 kw = this.G("Sprint", "Sprint on longer walks", true);
   private final com.larpclient.module.l3 U = this.G("Safe Sneak", "Crouch next to drops so the bot never walks off an edge while it places", true);
   private final com.larpclient.module.l3 LE = this.G(
      "Refill Hotbar",
      "Move the block it needs from your inventory into the hotbar, standing still. Only empty slots and blocks the build no longer needs are replaced",
      true
   );
   private final com.larpclient.module.lo mC = this.Z("Reach", "How far blocks are placed from, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final com.larpclient.module.lo qw = this.Z("Place Delay", "Ticks between two placements (20 ticks are one second)", 3.0, 1.0, 20.0, 1.0);
   private final com.larpclient.module.l3 g5 = this.G("Auto Eat", "Eat food from your hotbar at 10 hunger or less", true);
   private final com.larpclient.module.l3 gb = this.G(
      "Show Preview", "Draw the schematic in the world: missing blocks in the theme color, wrong blocks red, wrongly turned blocks orange", true
   );
   private final com.larpclient.module.l3 gx = this.G("Show Correct", "Also draw the blocks that are already right, in green", false);
   private final com.larpclient.module.lo gB = this.Z("Preview Range", "How far from you the preview draws blocks", 48.0, 8.0, 160.0, 8.0);
   private final com.larpclient.module.l3 C = this.G("Show Path", "Draw the route the builder walks and the block it places next", true);
   private final com.larpclient.module.l3 q = this.G(
      "Status Card", "Show a card at the top of the screen with the progress and what the builder is doing", true
   );
   private final com.larpclient.module.l3 mS = this.G("Notify When Done", "Show a notification when the build is finished", true);
   private final com.larpclient.module.l3 kr = this.G(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final com.larpclient.module.l3 ke = this.G(
      "Temporary Pillars", "Pillar up on throwaway blocks to place blocks that are too high to reach from the ground, then dig the pillar away", true
   );
   private final com.larpclient.module.lo m2 = this.Z("Max Pillar Height", "Tallest temporary pillar, in blocks", 6.0, 1.0, 64.0, 1.0);
   private final com.larpclient.module.li N = this.addSetting(
      new com.larpclient.module.li(
         "Pillar Block",
         "Throwaway block the pillars are built of. Never one the schematic uses",
         "Dirt",
         "Dirt",
         "Cobblestone",
         "Netherrack",
         "Cobbled Deepslate"
      )
   );
   private final com.larpclient.module.l3 kd = this.G(
      "Restock From Containers", "When a block runs out, walk to a chest, barrel or shulker box you marked and take more from it", true
   );
   private final com.larpclient.module.l3 kF = this.G(
      "Put Back Spares", "On a restock trip, put blocks the build no longer needs into the container, so the marked containers are the build's stockpile", true
   );
   private final com.larpclient.module.lw kb = this.addSetting(
      new com.larpclient.module.lw(
         "Mark Restock Key", "Look at a chest, barrel or shulker box and press this to mark it as a restock container, or to unmark it", -1
      )
   );
   private final aj O = this.addSetting(
      new aj("Restock Containers", "Marked containers, as dimension x y z, separated by commas. The Mark Restock Key and the Restock buttons set them", "")
         .A("minecraft:overworld x y z")
   );
   private final com.larpclient.module.lo mw = this.Z(
      "Restock Distance", "Farthest a marked container may be for the builder to walk to it", 64.0, 8.0, 256.0, 8.0
   );
   private final com.larpclient.module.l3 LY = this.G("Show Restock Containers", "Outline the marked containers in the world", true);
   private final com.larpclient.module.l3 m6 = this.G(
      "Stop On Staff Nearby", "Stop building and log off when someone Staff List counts as staff comes within range, invisible ones included", true
   );
   private final com.larpclient.module.lo kc = this.Z("Staff Range", "How close staff may come before the builder stops, in blocks", 48.0, 8.0, 128.0, 4.0);
   private final com.larpclient.module.lo K = this.Z("Log Off Delay", "Seconds between stopping and logging off", 5.0, 0.0, 30.0, 0.5);
   private final aj ms = this.addSetting(
      new aj("Log Off Message", "Shown on the disconnect screen when staff nearby makes the builder log off", "Auto Builder stopped: staff nearby")
   );
   private final com.larpclient.module.l3 Lb = this.G(
      "Buy Missing Blocks",
      "When a block runs out and no marked container has it, buy it from /ah on DonutSMP, then go on building. Spends your in-game money",
      false
   );
   private final com.larpclient.module.lo qX = this.Z(
         "Max Price Per Item", "Most it pays for one block, whatever the size of the listing. Type amounts like 50, 2K or 1.5M", 100.0, 1.0, 1.0E7, 1.0
      )
      .j();
   private final com.larpclient.module.lo A = this.Z(
         "Max Price Per Buy", "Most it pays for one listing. Type amounts like 10K, 5M or 1B", 10000.0, 100.0, 1.0E10, 100.0
      )
      .j();
   private final com.larpclient.module.lo gU = this.Z(
         "Max Total Spend", "Most it spends in one run, all purchases together. Type amounts like 100K, 10M or 1.5B", 100000.0, 1000.0, 1.0E12, 1000.0
      )
      .j();
   private final com.larpclient.module.li mD = this.addSetting(
      new com.larpclient.module.li(
         "Placement Mode",
         "Normal: your camera turns to every block with the builder, like a player's. Aggressive: while it places, your camera stays where you look and only the look sent to the server turns, easing back to your look afterwards. Walking still turns your camera",
         "Normal",
         "Normal",
         "Aggressive"
      )
   );
   private final com.larpclient.module.l3 Lq = this.G(
      "Follow Recording",
      "Build in the order you placed the blocks when you recorded this schematic with the Build Recorder. Ignored when it has no recording",
      true
   );
   private final com.larpclient.module.l3 q9 = this.G(
      "Relog On Ghost Blocks",
      "When the server keeps taking placed blocks back (8 in a minute), log off, rejoin the same server and carry on building. At most once every 5 minutes and 3 times a build, never while temporary blocks stand",
      false
   );
   private static final String k4 = "No schematic";
   private int g2;
   private final com.larpclient.module.li ku = new com.larpclient.module.li(
      "File", "A .litematic or .schem file from the schematics folder. New files appear here by themselves", "No schematic", "No schematic"
   ) {
      public String getValue() {
         String var1 = s0.this.m.getValue().trim();
         return !var1.isEmpty() && !s0.this.Lp ? var1 : "No schematic";
      }

      @Override
      public boolean acceptsValue(String var1) {
         return true;
      }

      @Override
      public void setValue(String var1) {
         s0.this.I(var1);
      }

      @Override
      public boolean isSearchable() {
         return this.e().size() > 8;
      }

      @Override
      public String getModeLabel(String var1) {
         return var1.replaceFirst("(?i)\\.(litematic|schem|schematic)$", "");
      }
   };
   private volatile s0.c kD;
   private volatile boolean q7;
   private int e;
   private String mg = "";
   private n mV;
   private String kk;
   private s0.j l;
   private boolean gl = (boolean)1;
   private boolean Lp;
   private boolean Lg;
   private Object gP;
   private Object Lz;
   private String kg;
   private String mY;
   private final u5 go = new u5(u5.p.QUICK);
   private final tn gF = new tn(this);
   private final e3 L1 = new e3(this, this.go, this::P);
   private final Map<Integer, Integer> mA = new HashMap<>();
   private final Map<Integer, Integer> mo = new HashMap<>();
   private final Map<Integer, Integer> mR = new HashMap<>();
   private final Set<Integer> Ll = new HashSet<>();
   private final Set<Integer> LS = new HashSet<>();
   private final Map<Integer, String> kW = new HashMap<>();
   private final Map<class_1792, Integer> J = new LinkedHashMap<>();
   private l1.i m3;
   private l1.i LQ;
   private int kS = -1000000000;
   private s0.l gy = s0.l.s;
   private class_2680 mZ;
   private final Map<Integer, s0.l> LA = new HashMap<>();
   private final Set<Integer> kn = new HashSet<>();
   private Integer my;
   private int mb;
   private int mf = -1000000000;
   private int qT = -1000000000;
   private int LU;
   private int km;
   private final Map<Integer, class_2680> k6 = new HashMap<>();
   private l1.e mu;
   private s0.a LM;
   private final List<class_2338> gh = new ArrayList<>();
   private final Set<Long> gQ = new HashSet<>();
   private int qs;
   private int gR;
   private int i;
   private int Lf;
   private l1.f gY;
   private s0.p kA;
   private final List<class_2338> L7 = new ArrayList<>();
   private final Set<Long> gL = new HashSet<>();
   private final Map<Integer, Integer> qY = new HashMap<>();
   private int L9;
   private int LD;
   private int LV = -1000000000;
   private int gc;
   private int Lj;
   private int kR;
   private final Set<Long> LK = new HashSet<>();
   private class_2338 gn;
   private int B;
   private int F;
   private final Map<Integer, Integer> Q = new HashMap<>();
   private final Set<Integer> L5 = new HashSet<>();
   private int Lr;
   private long qG = Long.MIN_VALUE;
   private long gC = Long.MIN_VALUE;
   private boolean k8;
   private int gZ;
   private int m_;
   private final List<class_2338> kj = new ArrayList<>();
   private final Set<Long> v = new HashSet<>();
   private boolean M;
   private int kL;
   private int gS = -1000000000;
   private final SplittableRandom qy = new SplittableRandom();
   private int gf;
   private int k9 = -1000000000;
   private int kK = -1000000000;
   private double LP = 0.6;
   private double kM;
   private boolean LT;
   private boolean x;
   private float m0 = Float.NaN;
   private float m1;
   private float LX;
   private int w;
   private int kE;
   private int g8;
   private final List<class_2338> kY = new ArrayList<>();
   private boolean mh;
   private final Set<class_2338> ks = new HashSet<>();
   private final Set<Integer> h = new HashSet<>();
   private List<l1.c> gq;
   private final Set<Integer> md = new HashSet<>();
   private final Set<Integer> g1 = new HashSet<>();
   private l1.c gN;
   private l1.c mU;
   private l1.e gI;
   private final List<class_2338> LN = new ArrayList<>();
   private int mE;
   private int LJ;
   private int La = -1;
   private class_2338 L;
   private int mW;
   private int qN = -1000000000;
   private int mK;
   private int kG;
   private int g_;
   private int Lc;
   private class_2350 gG;
   private class_2338 H;
   private int ge;
   private final List<List<l1.i>> g = new ArrayList<>();
   private int ga;
   private int Le;
   private s0.g qF;
   private s0.s T;
   private final Set<s0.s> kq = new HashSet<>();
   private final Map<class_1792, Integer> c = new HashMap<>();
   private final Set<Integer> mq = new HashSet<>();
   private int g0;
   private int gK;
   private int k5;
   private int kN;
   private int qP = -1000000000;
   private int Ln = Integer.MAX_VALUE;
   private int V;
   private boolean qz;
   private String LW;
   private final gi mL = new gi("Auto Builder");
   private class_1792 m7;
   private long f;
   private int g4;
   private int mM;
   private boolean k0;
   private final Set<class_1792> kf = new HashSet<>();
   static final long kP = 10000L;
   static final long LR = 2000L;
   private boolean ki;
   private boolean kU;
   private boolean gM;
   private long g6;
   private long kJ;
   private s0.o mz;
   private final List<s0.s> gT = new ArrayList<>();
   private final Map<s0.s, Map<class_1792, Integer>> r = new HashMap<>();
   private final Map<s0.s, Integer> Y = new HashMap<>();
   private final Set<s0.s> I = new HashSet<>();
   private final Set<s0.s> g7 = new HashSet<>();
   private final Set<s0.s> k3 = new HashSet<>();
   private final Map<class_1792, Integer> mp = new LinkedHashMap<>();
   private final Map<class_1792, Integer> D = new LinkedHashMap<>();
   private final Map<class_1792, Integer> gi = new LinkedHashMap<>();
   private final Map<class_1792, Integer> kO = new LinkedHashMap<>();
   private final Map<class_1792, Long> W = new LinkedHashMap<>();
   private int L3;
   private final gn g9 = new gn();
   private boolean Ld;
   private final List<Integer> u = new ArrayList<>();
   private final Set<Integer> kV = new HashSet<>();
   private BitSet Lx;
   private int[] Lo;
   private s0.j gg;
   private String LI;
   private int gW = -1000000000;
   private final ArrayDeque<Integer> LB = new ArrayDeque<>();
   private int gz = 0;
   private int mX;
   private int ma;
   private class_642 mH;
   private long gm;
   private long X;
   private long qn;
   private long gk;
   private boolean L4;
   private boolean g3;
   private class_3965 qo;
   private int gr;
   private int Lt = -1000000000;
   private int gA = -1;
   private int mI;
   private int gD = -1000000000;
   private int ky = -1000000000;
   private int gv;
   private int mj;
   private int k = -1000000000;
   private int LO;
   private int m8;
   private final l1.u qe = new l1.u();
   private class_2338 kQ;
   private final Map<Integer, Integer> kH = new HashMap<>();
   private List<l1.i> Lk = List.of();
   private class_2338 L0;
   private long d;
   private int E;
   private String kp = "";
   private class_243 kv = class_243.field_1353;
   private boolean kx;
   private boolean Lh;
   private boolean kZ;
   private int z = Integer.MIN_VALUE;
   private String Lw = "";
   private boolean LL;
   private final Map<class_1792, String> kX = new LinkedHashMap<>();
   private static final Pattern Li = Pattern.compile("(?i)\\.(litematic|schematic|schem)$");

   static List<s0.s> T(String var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         return var1;
      }

      for (String var5 : var0.split(",")) {
         String[] var6 = var5.trim().split(" +");
         if (var6.length == 4 && !var6[0].isEmpty()) {
            try {
               s0.s var7 = new s0.s(var6[0], Integer.parseInt(var6[1]), Integer.parseInt(var6[2]), Integer.parseInt(var6[3]));
               if (!var1.contains(var7)) {
                  var1.add(var7);
               }
            } catch (NumberFormatException var8) {
            }
         }
      }

      return var1;
   }

   static String N(List<s0.s> var0) {
      ArrayList var1 = new ArrayList();

      for (s0.s var3 : var0) {
         var1.add(var3.toString());
      }

      return String.join(", ", var1);
   }

   public s0() {
      super("Auto Builder", "Build a .litematic or .schem schematic block by block, walking to what is out of reach; or mine it out");
      mm = this;
      this.qw.M("Ticks Between Blocks");
      this.kr.M("Pause In Other Menus");
      this.m.M("Schematic (file name)");
      this.mJ.M("Origin (x y z)");
      this.gp.M("Rotation");
      this.O.M("Restock Containers");
      this.kc.O(this.m6::getValue);
      this.K.O(this.m6::getValue);
      this.ms.O(this.m6::getValue);
      this.kF.O(this.kd::getValue);
      this.mw.O(this.kd::getValue);
      this.m2.O(this.ke::getValue);
      this.N.O(this.ke::getValue);
      this.gx.O(this.gb::getValue);
      this.gB.O(this.gb::getValue);
      this.m.a(var1 -> this.T());
      this.mJ.a(var1 -> this.gl = (boolean)1);
      this.gp.a(var1 -> this.gl = (boolean)1);
      this.Ls.a(var1 -> this.gl = (boolean)1);
      if (!L6) {
         L6 = (boolean)1;
         WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var0 -> {
            s0 var1 = mm;
            if (var1 != null) {
               var1.a();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            s0 var2 = mm;
            if (!var1 && var2 != null && var2.mL.x()) {
               com.larpclient.gi.g(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            s0 var1 = mm;
            if (var1 != null) {
               var1.I();
               var1.s();
            }
         });
      }
   }

   public static s0 u() {
      return mm;
   }

   public static Path b() {
      return FabricLoader.getInstance().getGameDir().resolve("schematics");
   }

   @Override
   public void disable() {
      this.kg = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      class_437 var1 = getMc() == null ? null : mc.field_1755;
      return var1 == null || var1 instanceof class_408 || var1 instanceof class_433 || !this.kr.getValue();
   }

   @Override
   public List<tc.k> commands() {
      return List.of(
         new tc.k("Schematic", "Next schematic", () -> this.K(1)),
         new tc.k("Schematic", "Previous schematic", () -> this.K(-1)),
         new tc.k("Schematic", "Reload it", this::w),
         new tc.k("Schematic", "Unload schematic", this::u),
         new tc.k("Schematic", "Open the schematics folder", this::v),
         new tc.k("Where it goes", "Origin at my feet", this::s),
         new tc.k("Where it goes", "Origin on the block I look at", this::o),
         new tc.k("Where it goes", "Turn it 90 degrees", this::r),
         new tc.k("Move it", "East (+X)", () -> this.S(1, 0, 0)),
         new tc.k("Move it", "West (-X)", () -> this.S(-1, 0, 0)),
         new tc.k("Move it", "Up", () -> this.S(0, 1, 0)),
         new tc.k("Move it", "Down", () -> this.S(0, -1, 0)),
         new tc.k("Move it", "South (+Z)", () -> this.S(0, 0, 1)),
         new tc.k("Move it", "North (-Z)", () -> this.S(0, 0, -1)),
         new tc.k("Restock", "Mark the container I look at", this::I),
         new tc.k("Restock", "Clear the restock marks", this::D),
         new tc.k("Restock", "Stock the materials", this::q),
         new tc.k("Build Recorder", "Start recording", this::W),
         new tc.k("Build Recorder", "Stop and save the recording", this::x),
         new tc.k("Build Recorder", "Delete the recording", this::K),
         new tc.k("Build", "List the materials", this::G),
         new tc.k("Build", "Mine it out instead", this::j)
      );
   }

   @Override
   public String summary() {
      String var1 = this.m.getValue().trim();
      if (var1.isEmpty()) {
         return "Put .litematic or .schem files in the schematics folder and press Next schematic.";
      }

      if (this.Lp) {
         return "No schematic loaded. Next schematic or Reload it loads " + var1 + " or another one again.";
      }

      if (this.q7) {
         return "Loading " + var1 + "...";
      }

      if (this.kk != null) {
         return var1 + ": " + this.kk + ".";
      }

      if (this.mV == null) {
         return "Loading " + var1 + "...";
      }

      String var2 = this.mV.D() + ": " + this.mV.r() + " x " + this.mV.C() + " x " + this.mV.X() + ", " + this.mV.I() + " blocks";
      n.v var3 = this.D();
      if (var3 == null) {
         return var2 + ". Set the origin to place it.";
      }

      String var4 = " at " + var3.origin() + this.e();
      if (this.Lg && this.isRuntimeActive()) {
         return var2 + var4 + ". " + this.R() + "." + this.L();
      }

      s0.j var5 = this.l;
      return var5 != null && var5.k && this.isInGame()
         ? var2
            + var4
            + ". "
            + var5.l(uo.t.L)
            + " missing, "
            + (var5.l(uo.t.D) + var5.l(uo.t.m))
            + " wrong, "
            + var5.l(uo.t.V)
            + " done"
            + (this.mY != null ? ". " + this.mY : "")
            + "."
            + this.L()
         : var2 + var4 + "." + this.L();
   }

   private String e() {
      String var1 = this.gp.getValue().equals("0") ? "" : ", turned " + this.gp.getValue();
      return this.Ls.getValue().equals("None") ? var1 : var1 + ", mirrored " + this.Ls.getValue();
   }

   @Override
   public String detail() {
      s0.j var1 = this.l;
      if (var1 != null && var1.k && this.isInGame()) {
         Map var2 = this.C(var1);
         if (var2.isEmpty()) {
            return var1.l(uo.t.L) > 0 ? "Missing blocks cannot be placed yet; List the materials says why." : "";
         }

         StringBuilder var3 = new StringBuilder("Needs: ");
         int var4 = 0;

         for (Entry var6 : var2.entrySet()) {
            if (var4 == 3) {
               var3.append(", ").append(var2.size() - 3).append(" more");
               break;
            }

            if (var4++ > 0) {
               var3.append(", ");
            }

            int var7 = this.R((class_1792)var6.getKey());
            var3.append(var6.getValue())
               .append(' ')
               .append(R((class_1792)var6.getKey()))
               .append(var7 >= var6.getValue() ? "" : " (" + (var7 == 0 ? "none" : var7) + " on you)");
         }

         return var3.append('.').toString();
      } else {
         return "";
      }
   }

   @Override
   public List<tc.v> settingGroups() {
      BooleanSupplier var1 = () -> this.D() != null;
      return List.of(
         new tc.v("Schematic", List.of(this.ku))
            .with(
               new tc.l(
                  this::c,
                  List.of(
                     List.of(
                        this.g("Reload", "Reload it").availableWhen(() -> !this.m.getValue().isBlank()),
                        this.g("Unload", "Unload schematic"),
                        this.g("Open folder", "Open the schematics folder")
                     )
                  ),
                  this::preview,
                  true
               )
            ),
         new tc.v("Placement", List.of(this.gp, this.Ls))
            .with(
               new tc.l(
                  this::C,
                  List.of(
                     List.of(this.g("At my feet", "Origin at my feet"), this.g("Block I look at", "Origin on the block I look at")),
                     List.of(
                        this.g("West", "West (-X)").availableWhen(var1),
                        this.g("East", "East (+X)").availableWhen(var1),
                        this.g("North", "North (-Z)").availableWhen(var1),
                        this.g("South", "South (+Z)").availableWhen(var1)
                     ),
                     List.of(this.g("Up", "Up").availableWhen(var1), this.g("Down", "Down").availableWhen(var1))
                  )
               )
            ),
         new tc.v("Blocks", List.of(this.LE)).with(new tc.l(this::g, List.of(List.of(this.g("List in chat", "List the materials"))))),
         new tc.v("While building", List.of(this.mD, this.Lq, this.qB, this.kw, this.U, this.g5, this.ke)),
         new tc.v("Material chests", List.of(this.kd, this.kF, this.kb))
            .with(
               new tc.l(
                  this::i,
                  List.of(
                     List.of(this.g("Select chest", "Mark the container I look at"), this.g("Clear chests", "Clear the restock marks")),
                     List.of(this.g("Stock materials", "Stock the materials"))
                  )
               )
            ),
         new tc.v("Buying on DonutSMP", List.of(this.Lb, this.qX, this.A, this.gU)),
         new tc.v("Safety", List.of(this.m6, this.kc, this.q9)),
         new tc.v("Display", List.of(this.gb, this.gx, this.C, this.q, this.mS, this.LY)),
         tc.v.advanced("Advanced", List.of(this.m, this.mJ, this.mC, this.qw, this.gB, this.kr, this.m2, this.N, this.O, this.mw, this.K, this.ms))
            .with(
               new tc.l(
                  () -> "",
                  List.of(
                     List.of(
                        this.g("Record build", "Start recording"),
                        this.g("Save recording", "Stop and save the recording"),
                        this.g("Delete recording", "Delete the recording"),
                        this.g("Mine it out", "Mine it out instead")
                     )
                  )
               )
            )
      );
   }

   private String c() {
      String var1 = this.m.getValue().trim();
      if (this.ku.e().size() <= 1 && var1.isEmpty()) {
         return "The schematics folder is empty.";
      }

      if (!var1.isEmpty() && !this.Lp) {
         if (!this.q7 && (this.mV != null || this.kk != null)) {
            return this.kk != null
               ? "Could not load " + var1 + ": " + this.kk + "."
               : this.mV.r() + " x " + this.mV.C() + " x " + this.mV.X() + ", " + String.format(Locale.ROOT, "%,d", this.mV.I()) + " blocks";
         } else {
            return "Loading.";
         }
      } else {
         return "";
      }
   }

   private String C() {
      n.v var1 = this.D();
      return var1 == null ? "Not set." : "Origin at " + var1.origin() + this.e();
   }

   private int W(s0.j var1) {
      int var2 = 0;

      for (Entry var4 : this.C(var1).entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.R((class_1792)var4.getKey()));
      }

      return var2;
   }

   private String g() {
      s0.j var1 = this.l;
      if (var1 != null && this.isInGame() && var1.k) {
         String var2 = this.detail();
         return var2.isEmpty() ? "Nothing more is needed." : var2;
      } else {
         return "";
      }
   }

   @Override
   public void panelTick() {
      if (this.g2-- <= 0) {
         this.g2 = 40;
         ArrayList var1 = new ArrayList();
         var1.add("No schematic");
         var1.addAll(com.larpclient.n.R(b()));
         this.ku.k(var1);
      }
   }

   @Override
   public tc.b status() {
      if (this.Lg && this.isRuntimeActive()) {
         return new tc.b(this.R() + ".", false);
      }

      String var1 = this.m.getValue().trim();
      if (var1.isEmpty() || this.Lp) {
         return new tc.b("Choose a schematic, then set where it goes.", false);
      }

      if (this.kk != null) {
         return new tc.b("Could not load " + var1 + ": " + this.kk + ".", true);
      }

      if (this.q7 || this.mV == null) {
         return new tc.b("Loading " + var1 + ".", false);
      }

      if (this.D() == null) {
         return new tc.b("Set where it goes, then turn the module on.", false);
      }

      if (!this.isInGame()) {
         return new tc.b("Schematic set. Join the world it goes in.", false);
      }

      s0.j var2 = this.l;
      if (var2 != null && var2.k) {
         String var3 = tc.q(this);
         if (var3 != null) {
            return new tc.b(var3 + " is running. One bot runs at a time.", true);
         }

         if (this.Ld) {
            return new tc.b("Build Recorder is recording. Stop and save it first.", true);
         }

         class_2338 var4 = mc.field_1724.method_24515();
         int var5 = Math.max(
            Math.max(var2.S - var4.method_10263(), var4.method_10263() - var2.E), Math.max(var2.N - var4.method_10260(), var4.method_10260() - var2.M)
         );
         if (var5 > 32.0) {
            return new tc.b("You are " + var5 + " blocks from the build. Walk next to it.", true);
         }

         int var6 = var2.l(uo.t.L);
         int var7 = var2.l(uo.t.D) + var2.l(uo.t.m);
         if (var6 == 0 && var7 == 0) {
            return new tc.b("Every block already matches the schematic.", false);
         }

         String var8 = String.format(Locale.ROOT, "Will place %,d blocks", var6) + (var7 > 0 ? ", " + var7 + " wrong ones are left alone" : "") + ".";
         int var9 = this.W(var2);
         if (var9 == 0) {
            return new tc.b(var8, false);
         }

         int var10 = 0;

         for (s0.s var12 : T(this.O.getValue())) {
            if (var12.dimension().equals(this.b())) {
               var10++;
            }
         }

         int var13 = (!this.kd.getValue() || var10 <= 0) && !this.Lb.getValue() ? 0 : 1;
         return new tc.b(var8 + String.format(Locale.ROOT, " You are short of %,d.", var9), var13 == 0);
      } else {
         return new tc.b("Checking the world.", false);
      }
   }

   private String K(int var1) {
      List var2 = com.larpclient.n.R(b());
      if (var2.isEmpty()) {
         return "No schematics in " + b() + ". Put .litematic or .schem files there.";
      }

      int var3 = var2.indexOf(this.m.getValue().trim());
      int var4 = var3 < 0 ? (var1 > 0 ? 0 : var2.size() - 1) : Math.floorMod(var3 + var1, var2.size());
      if (this.Lg) {
         return "Turn the builder off before picking another schematic.";
      }

      this.S((String)var2.get(var4));
      return "Picked " + (String)var2.get(var4) + " (" + (var4 + 1) + " of " + var2.size() + ").";
   }

   private void S(String var1) {
      boolean var2 = var1.equals(this.m.getValue().trim());
      this.m.Y(var1);
      if (var2 && this.Lp) {
         this.T();
      }

      this.e();
   }

   private void I(String var1) {
      if ("No schematic".equals(var1)) {
         this.u();
      } else if (this.Lg) {
         this.N("turn the builder off before picking another schematic.");
      } else {
         this.S(var1);
      }
   }

   private String w() {
      if (this.m.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      if (this.Lg) {
         return "Turn the builder off before reloading.";
      }

      this.T();
      return "Reloading " + this.m.getValue().trim() + ".";
   }

   private String u() {
      if (this.Lg) {
         this.N("turn the builder off before unloading the schematic.");
         return "Turn the builder off before unloading the schematic.";
      }

      boolean var1 = sh.T("the schematic was unloaded");
      if (this.Lp || this.mV == null && !this.q7 && this.kk == null) {
         if (!var1) {
            this.N("no schematic is loaded.");
         }

         return var1 ? "Stopped Mine Out; no schematic was loaded." : "No schematic is loaded.";
      } else {
         String var2 = this.mV != null ? this.mV.D() : this.m.getValue().trim();
         if (this.Ld) {
            this.e("the schematic was unloaded");
         }

         this.e++;
         this.kD = null;
         this.q7 = (boolean)0;
         this.mV = null;
         this.kk = null;
         this.l = null;
         this.gl = (boolean)0;
         this.Lo = null;
         this.gg = null;
         this.mY = null;
         this.Lp = (boolean)1;
         q8.G(this.getName(), "Unloaded {}{}; settings kept", var2, var1 ? " after stopping its Mine Out" : "");
         this.N(
            "unloaded "
               + var2
               + (var1 ? " and stopped mining it out" : "")
               + ". Your settings are kept; pick one in the list or press Reload to load one again."
         );
         return "Unloaded " + var2 + (var1 ? ", Mine Out stopped." : ".");
      }
   }

   private String v() {
      Path var1 = b();

      try {
         Files.createDirectories(var1);
      } catch (IOException var3) {
         return "Could not create " + var1 + ": " + var3.getMessage();
      }

      class_156.method_668().method_60932(var1);
      return "Opened " + var1 + ". Put .litematic or .schem files there, then choose one in the list.";
   }

   private String s() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.Lg) {
         return "Turn the builder off before moving the schematic.";
      }

      class_2338 var1 = mc.field_1724.method_24515();
      return this.w(var1.method_10263(), var1.method_10264(), var1.method_10260());
   }

   private String o() {
      if (!this.isInGame()) {
         return "Join a world first.";
      } else if (this.Lg) {
         return "Turn the builder off before moving the schematic.";
      } else if (mc.field_1765 instanceof class_3965 var1 && var1.method_17783() == class_240.field_1332) {
         class_2338 var3 = var1.method_17777().method_10093(var1.method_17780());
         return this.w(var3.method_10263(), var3.method_10264(), var3.method_10260());
      } else {
         return "Look at a block first.";
      }
   }

   private String w(int var1, int var2, int var3) {
      this.mJ.Y(fx.A(var1, var2, var3));
      this.e();
      return "Origin set to " + var1 + " " + var2 + " " + var3 + ".";
   }

   private String r() {
      if (this.Lg) {
         return "Turn the builder off before turning the schematic.";
      }

      this.gp.P();
      this.e();
      return "Turned to " + this.gp.getValue() + " degrees.";
   }

   private String S(int var1, int var2, int var3) {
      if (this.Lg) {
         return "Turn the builder off before moving the schematic.";
      }

      int[] var4 = fx.C(this.mJ.getValue());
      return var4 == null ? "Set the origin first." : this.w(var4[0] + var1, var4[1] + var2, var4[2] + var3);
   }

   private String G() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      s0.j var1 = this.l;
      if (var1 == null) {
         return this.mV == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var1.k) {
         return "Still checking the world; try again in a moment.";
      }

      Map var2 = this.C(var1);
      this.N("materials for " + this.mV.D() + " (" + var1.l(uo.t.L) + " missing, " + var1.l(uo.t.D) + " wrong blocks, " + var1.l(uo.t.m) + " turned wrong):");

      for (Entry var4 : var2.entrySet()) {
         int var5 = this.R((class_1792)var4.getKey());
         this.N(
            "  "
               + var4.getValue()
               + " "
               + R((class_1792)var4.getKey())
               + ", you carry "
               + var5
               + (var5 >= var4.getValue() ? "" : ", short by " + ((Integer)var4.getValue() - var5))
         );
      }

      TreeMap var6 = new TreeMap();

      for (int var7 = 0; var7 < var1.n; var7++) {
         String var9 = var1.t[var1.K[var7]];
         if (var9 != null && var1.M(var7) != uo.t.V) {
            var6.merge(com.larpclient.n.s(this.mV.a().get(var1.K[var7])).replace("minecraft:", "") + " (" + var9 + ")", 1, Integer::sum);
         }
      }

      for (Entry var10 : var6.entrySet()) {
         this.N("  left out: " + var10.getValue() + " " + (String)var10.getKey());
      }

      return var2.isEmpty() && var6.isEmpty() ? "Nothing is missing." : "Listed " + (var2.size() + var6.size()) + " materials in chat.";
   }

   private String j() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (!this.Lg && !this.isRuntimeActive()) {
         s0.j var1 = this.l;
         if (var1 == null) {
            return "Load a schematic and set its origin first.";
         }

         ArrayList var2 = new ArrayList(var1.n);

         for (int var3 = 0; var3 < var1.n; var3++) {
            var2.add(var1.P(var3));
         }

         String var4 = sh.w(var2, "the " + this.mV.D() + " schematic");
         if (var4 != null) {
            return var4;
         }

         q8.G(this.getName(), "Mine Out: handed {} cells of {} at {} to Auto Area Miner", var2.size(), this.mV.D(), var1.A.origin());
         return "Auto Area Miner is mining out " + this.mV.D() + ".";
      } else {
         return "Turn the builder off first; one bot runs at a time.";
      }
   }

   private Map<class_1792, Integer> C(s0.j var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.n; var3++) {
         if (var1.M(var3) == uo.t.L && var1.t[var1.K[var3]] == null && !var1.h(var3)) {
            class_2680 var4 = var1.q(var3);
            class_1747 var5 = uo.w(var4);
            int var6 = uo.b(var4) && this.isInGame() ? uo.R(var4, mc.field_1687.method_8320(var1.P(var3))) : 1;
            if (var5 != null && var6 > 0) {
               var2.merge(var5, var6, Integer::sum);
            }
         }
      }

      ArrayList var7 = new ArrayList(var2.entrySet());
      var7.sort((var0, var1x) -> (Integer)var1x.getValue() - (Integer)var0.getValue());
      LinkedHashMap var8 = new LinkedHashMap();

      for (Entry var10 : var7) {
         var8.put((class_1792)var10.getKey(), (Integer)var10.getValue());
      }

      return var8;
   }

   private static String R(class_1792 var0) {
      return new class_1799(var0).method_7964().getString();
   }

   private int R(class_1792 var1) {
      if (!this.isInGame()) {
         return 0;
      }

      class_1661 var2 = mc.field_1724.method_31548();
      int var3 = 0;

      for (int var4 = 0; var4 < 36; var4++) {
         if (var2.method_5438(var4).method_31574(var1)) {
            var3 += var2.method_5438(var4).method_7947();
         }
      }

      if (mc.field_1724.method_6079().method_31574(var1)) {
         var3 += mc.field_1724.method_6079().method_7947();
      }

      return var3;
   }

   private void e() {
      LarpClient var1 = LarpClient.b();
      if (var1 != null && var1.e() != null) {
         var1.e().U();
      }
   }

   private void T() {
      this.Lp = (boolean)0;
      int var1 = ++this.e;
      String var2 = this.m.getValue().trim();
      this.mV = null;
      this.kk = null;
      this.l = null;
      this.gl = (boolean)1;
      this.mg = var2;
      if (var2.isEmpty()) {
         this.q7 = (boolean)0;
      } else if (!var2.contains("/") && !var2.contains("\\") && !var2.contains("..")) {
         this.q7 = (boolean)1;
         Path var3 = b().resolve(var2);
         Thread var4 = new Thread(() -> {
            s0.c var4x;
            try {
               if (!Files.isRegularFile(var3)) {
                  var4x = new s0.c(var2, null, "no such file in the schematics folder");
               } else {
                  var4x = new s0.c(var2, com.larpclient.n.W(var3), null);
               }
            } catch (IOException | RuntimeException var6) {
               var4x = new s0.c(var2, null, var6.getMessage() == null ? var6.getClass().getSimpleName() : var6.getMessage());
            }

            if (var1 == this.e) {
               this.kD = var4x;
            }
         }, "Larp-SchematicLoad");
         var4.setDaemon(true);
         var4.start();
      } else {
         this.kk = "only a file name inside the schematics folder works";
      }
   }

   private void I() {
      this.i();
      if (this.Lp) {
         if (this.Ld) {
            this.e("the schematic was unloaded");
         }
      } else {
         s0.c var1 = this.kD;
         if (var1 != null) {
            this.kD = null;
            this.q7 = (boolean)0;
            if (var1.file().equals(this.mg)) {
               this.mV = var1.schematic();
               this.kk = var1.error();
               this.gl = (boolean)1;
               if (this.mV != null) {
                  q8.G(
                     this.getName(),
                     "Loaded {} ({}): {}, {} regions, {} palette entries",
                     var1.file(),
                     this.mV.U(),
                     this.mV.E(),
                     this.mV.Y().size(),
                     this.mV.a().size()
                  );
               } else {
                  q8.G(this.getName(), "Could not load {}: {}", var1.file(), this.kk);
               }
            }
         }

         if (this.mV == null && !this.q7 && this.kk == null && !this.m.getValue().isBlank()) {
            this.T();
         } else {
            if (this.gl) {
               this.gl = (boolean)0;
               n.v var2 = this.D();
               this.l = this.mV != null && var2 != null ? new s0.j(this.mV, var2) : null;
               if (this.l != null) {
                  q8.v(
                     this.getName(),
                     "Layout at {}{}: box {} to {}",
                     var2.origin(),
                     this.e(),
                     this.l.S + " " + this.l.X + " " + this.l.N,
                     this.l.E + " " + this.l.D + " " + this.l.M
                  );
               }
            }

            s0.j var3 = this.l;
            if (var3 != this.gg) {
               if (this.Ld) {
                  this.e("the schematic or its origin changed");
               }

               this.V(var3);
            }

            if (var3 != null && this.isInGame()) {
               this.r(var3);
               if (var3.F != mc.field_1687) {
                  var3.F = mc.field_1687;
                  var3.O = 0;
                  var3.k = (boolean)0;
                  Arrays.fill(var3.b, 0);
                  var3.j.clear();
               }

               this.w(var3, 4096);
            } else {
               if (this.Ld) {
                  this.e("the schematic or the world changed");
               }
            }
         }
      }
   }

   private n.v D() {
      int[] var1 = fx.C(this.mJ.getValue());
      if (var1 == null) {
         return null;
      }

      int var2;
      try {
         var2 = Integer.parseInt(this.gp.getValue());
      } catch (NumberFormatException var6) {
         var2 = 0;
      }
      n.k var3 = switch ((String)this.Ls.getValue()) {
         case "X" -> n.k.I;
         case "Z" -> n.k.J;
         default -> n.k.M;
      };
      return new n.v(var1[0], var1[1], var1[2], var2, var3);
   }

   private void w(s0.j var1, int var2) {
      if (var1.n != 0) {
         class_2339 var3 = new class_2339();
         double var4 = this.gB.getValue();
         double var6 = mc.field_1724.method_23317();
         double var8 = mc.field_1724.method_23318();
         double var10 = mc.field_1724.method_23321();

         for (int var12 = 0; var12 < var2; var12++) {
            int var13 = var1.O;
            uo.t var14 = this.B(var1, var13, var3);
            var1.b[var14.ordinal()]++;
            if (var14 != uo.t.v && var1.q(var13) != null) {
               double var15 = var3.method_10263() + 0.5 - var6;
               double var17 = var3.method_10264() + 0.5 - var8;
               double var19 = var3.method_10260() + 0.5 - var10;
               double var21 = var15 * var15 + var17 * var17 + var19 * var19;
               if (var21 <= var4 * var4 && (var14 != uo.t.V || this.gx.getValue())) {
                  var1.j.add(new long[]{(long)var21, var13});
                  if (var1.j.size() > 2048) {
                     var1.j.poll();
                  }
               }
            }

            if (++var1.O >= var1.n) {
               var1.O = 0;
               System.arraycopy(var1.b, 0, var1.c, 0, var1.c.length);
               Arrays.fill(var1.b, 0);
               var1.k = (boolean)1;
               int[] var23 = new int[var1.j.size()];
               int var16 = 0;

               for (long[] var18 : var1.j) {
                  var23[var16++] = (int)var18[1];
               }

               var1.p = var23;
               var1.j.clear();
               if (var1.n < var2) {
                  break;
               }
            }
         }
      }
   }

   private uo.t B(s0.j var1, int var2, class_2339 var3) {
      var3.method_16363(var1.G[var2]);
      class_2680 var4 = var1.q(var2);
      uo.t var5;
      if (!mc.field_1687.method_22340(var3)) {
         var5 = uo.t.v;
      } else if (var4 == null) {
         var5 = uo.t.V;
      } else {
         var5 = uo.q(var4, mc.field_1687.method_8320(var3));
      }

      var1.r[var2] = (byte)var5.ordinal();
      return var5;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.U();
      this.gv = 0;
      this.mY = null;
      if (this.kg != null) {
         String var4 = this.kg;
         this.kg = null;
         if (this.gM) {
            this.gM = (boolean)0;
            this.kJ = 0L;
            this.x("stocking stopped earlier because " + var4 + ". Press Stock materials to go on");
         } else {
            this.x("it stopped earlier because " + var4 + ". Turn it on again to carry on");
         }
      } else if (this.gz != 3 && this.gz != 2) {
         this.ma = 0;
         this.L4 = (boolean)0;
         int var3 = System.currentTimeMillis() - this.kJ < 2000L ? 1 : 0;
         this.kJ = 0L;
         String var5 = tc.q(this);
         if (var5 != null) {
            this.x("turn off " + var5 + " first; one bot runs at a time");
         } else if (var3 != 0) {
            this.w();
         } else {
            this.l();
         }
      } else {
         class_642 var1 = mc.method_1558();
         int var2 = var1 != null && this.mH != null && var1.field_3761.equalsIgnoreCase(this.mH.field_3761) ? 1 : 0;
         this.gz = 0;
         this.mH = null;
         if (var2 == 0) {
            this.x("it logged off to resync blocks and you joined another server. Turn it on again to carry on");
         } else {
            this.gk = System.currentTimeMillis() + 3000L;
            q8.G(this.getName(), "Rejoined after the relog; carrying on in {} ms", 3000L);
            this.m("Rejoined; waiting for the world to load");
         }
      }
   }

   private void l() {
      if (this.Ld) {
         this.x("the Build Recorder is recording; stop and save it first");
      } else if (sh.N()) {
         this.x("Auto Area Miner is mining a schematic out; turn it off first");
      } else if (this.Lp) {
         this.x("the schematic is unloaded; pick it in the list or press Reload first");
      } else if (this.mV == null) {
         this.x(this.kk != null ? "the schematic did not load: " + this.kk : "pick a schematic first");
      } else {
         if (this.gl) {
            this.I();
         }

         s0.j var1 = this.l;
         if (var1 == null) {
            this.x("set the origin first");
         } else {
            class_2338 var2 = mc.field_1724.method_24515();
            double var3 = Math.max(
               Math.max(var1.S - var2.method_10263(), var2.method_10263() - var1.E), Math.max(var1.N - var2.method_10260(), var2.method_10260() - var1.M)
            );
            if (var3 > 32.0) {
               this.x("stand next to the build to start (you are " + (int)var3 + " blocks away)");
            } else {
               if (!var1.k || var1.n <= 200000) {
                  this.w(var1, var1.n);
               }

               this.Lg = (boolean)1;
               this.gP = mc.field_1687;
               this.Lz = mc.field_1724;
               this.gF.V();
               this.d = System.currentTimeMillis();
               this.E = var1.l(uo.t.L);
               this.LO = var1.l(uo.t.V);
               this.N(
                  "building " + this.mV.D() + " at " + var1.A.origin() + this.e() + ": " + this.E + " blocks to place, " + var1.l(uo.t.V) + " already done."
               );
               ArrayList var5 = new ArrayList();
               Map var6 = this.C(var1);
               ArrayList var7 = new ArrayList();

               for (Entry var9 : var6.entrySet()) {
                  int var10 = this.R((class_1792)var9.getKey());
                  if (var10 < (Integer)var9.getValue()) {
                     var7.add((Integer)var9.getValue() - var10 + " " + R((class_1792)var9.getKey()));
                  }
               }

               int var12 = 0;

               for (s0.s var15 : T(this.O.getValue())) {
                  if (var15.dimension().equals(this.b())) {
                     var12++;
                  }
               }

               if (!var7.isEmpty()) {
                  var5.add(
                     "you are short of "
                        + String.join(", ", var7.subList(0, Math.min(4, var7.size())))
                        + (var7.size() > 4 ? " and " + (var7.size() - 4) + " more" : "")
                        + (this.kd.getValue() && var12 > 0 ? " (it restocks from " + var12 + " marked " + (var12 == 1 ? "container)" : "containers)") : "")
                  );
               }

               int var14 = var1.l(uo.t.D) + var1.l(uo.t.m);
               int var16 = 0;

               for (int var11 = 0; var11 < var1.n; var11++) {
                  if (var1.M(var11) == uo.t.m && uo.d(var1.q(var11), mc.field_1687.method_8320(var1.P(var11))) > 0) {
                     var16++;
                  }
               }

               if (var14 > var16) {
                  var5.add(var14 - var16 + " cells hold another block or face the wrong way; they are left as they are");
               }

               if (var16 > 0) {
                  this.N(var16 + " blocks are set by right clicks (repeater delays, comparator modes, notes, open doors or levers).");
               }

               if (this.g5.getValue() && tn.h(var0 -> var0.method_58694(class_9334.field_50075) != null) < 0) {
                  var5.add("no food in the hotbar");
               }

               if (!var5.isEmpty()) {
                  this.N("heads up: " + String.join("; ", var5) + ".");
               }

               q8.G(
                  this.getName(),
                  "Started: {} at {}{}, {} to place, {} done, {} wrong, box {} {} {} to {} {} {}",
                  this.mV.D(),
                  var1.A.origin(),
                  this.e(),
                  this.E,
                  var1.l(uo.t.V),
                  var14,
                  var1.S,
                  var1.X,
                  var1.N,
                  var1.E,
                  var1.D,
                  var1.M
               );
               q8.G(this.getName(), "Player at {} when the run started", this.H().method_23854());
               if (this.E == 0) {
                  this.b();
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.Lg && this.gz != 0) {
         q8.G(this.getName(), "Logged off for the relog after {} blocks", this.gv);
      } else if (this.isEnabled() && this.Lg) {
         this.kg = "the world, the player or the connection changed";
         this.gM = this.ki;
         q8.G(this.getName(), "Interrupted after {} blocks: {}", this.gv, this.kg);
      }

      super.onDisable();
      fk.N(this);
      bv.i(this);
      if (!this.gh.isEmpty()) {
         class_2338 var1 = this.gh.getFirst();
         this.N("left " + this.gh.size() + " temporary " + (this.gh.size() == 1 ? "block" : "blocks") + " in the pillar at " + var1.method_23854() + ".");
         q8.G(this.getName(), "Stopped with {} temporary blocks standing from {}", this.gh.size(), var1.method_23854());
      }

      if (!this.LN.isEmpty()) {
         this.N(
            "left "
               + this.LN.size()
               + " temporary "
               + (this.LN.size() == 1 ? "block" : "blocks")
               + " in the tower at "
               + this.LN.getFirst().method_23854()
               + "."
         );
         q8.G(this.getName(), "Stopped with {} tower blocks standing beside a doorway from {}", this.LN.size(), this.LN.getFirst().method_23854());
      }

      if (!this.kY.isEmpty()) {
         this.N(
            "left " + this.kY.size() + " temporary " + (this.kY.size() == 1 ? "block" : "blocks") + " starting at " + this.kY.getFirst().method_23854() + "."
         );
         q8.G(this.getName(), "Stopped with {} temporary blocks not swept up, from {}", this.kY.size(), this.kY.getFirst().method_23854());
      }

      if (!this.kj.isEmpty()) {
         this.N(
            "left " + this.kj.size() + " temporary walkway " + (this.kj.size() == 1 ? "block" : "blocks") + " at " + this.kj.getFirst().method_23854() + "."
         );
         q8.G(this.getName(), "Stopped with {} walkway blocks from {}", this.kj.size(), this.kj.getFirst().method_23854());
      }

      if (!this.L7.isEmpty()) {
         this.N("left " + this.L7.size() + " temporary " + (this.L7.size() == 1 ? "block" : "blocks") + " at " + this.L7.getFirst().method_23854() + ".");
         q8.G(this.getName(), "Stopped with {} temporary support blocks at {}", this.L7.size(), this.L7.getFirst().method_23854());
      }

      this.gF.E();
      this.U();
   }

   private void U() {
      this.Lg = (boolean)0;
      this.gP = this.Lz = null;
      this.mA.clear();
      this.mo.clear();
      this.mR.clear();
      this.Ll.clear();
      this.kW.clear();
      this.LS.clear();
      this.L5.clear();
      this.Q.clear();
      this.gn = null;
      this.LK.clear();
      this.LV = -1000000000;
      this.J.clear();
      this.kH.clear();
      this.LA.clear();
      this.kn.clear();
      this.my = null;
      this.mf = this.qT = -1000000000;
      this.LU = this.km = 0;
      this.k6.clear();
      this.gy = s0.l.s;
      this.mZ = null;
      this.mu = null;
      this.LM = null;
      this.gh.clear();
      this.gQ.clear();
      this.qs = this.gR = this.i = this.Lf = 0;
      this.kj.clear();
      this.gG = null;
      this.H = null;
      this.ge = 0;
      this.m0 = Float.NaN;
      this.m1 = this.LX = 0.0F;
      this.w = this.kE = this.gf = this.g8 = 0;
      this.k9 = this.kK = -1000000000;
      this.LT = this.x = (boolean)0;
      this.gq = null;
      this.md.clear();
      this.g1.clear();
      this.h.clear();
      this.mh = (boolean)0;
      this.ks.clear();
      this.LJ = 0;
      this.La = -1;
      this.gN = this.mU = null;
      this.gI = null;
      this.LN.clear();
      this.mE = 0;
      this.v.clear();
      this.Lc = 0;
      this.M = (boolean)0;
      this.mK = this.kG = 0;
      this.kY.clear();
      this.L = null;
      this.mW = 0;
      this.g_ = 0;
      this.gY = null;
      this.kA = null;
      this.L7.clear();
      this.gL.clear();
      this.gZ = this.m_ = this.ga = this.Le = this.L9 = this.Lr = 0;
      this.qY.clear();
      this.qG = Long.MIN_VALUE;
      this.gC = Long.MIN_VALUE;
      this.k8 = (boolean)0;
      this.g.clear();
      this.qF = null;
      this.T = null;
      this.kq.clear();
      this.c.clear();
      this.mq.clear();
      this.g0 = this.gK = this.k5 = this.kN = 0;
      this.qP = -1000000000;
      this.Ln = Integer.MAX_VALUE;
      this.V = 0;
      this.LW = null;
      this.gW = -1000000000;
      if (this.mL.x()) {
         this.mL.O("the builder stopped");
      }

      this.m7 = null;
      this.f = 0L;
      this.g4 = this.mM = 0;
      this.k0 = (boolean)0;
      this.kf.clear();
      this.ki = this.kU = (boolean)0;
      this.mz = null;
      this.gT.clear();
      this.r.clear();
      this.Y.clear();
      this.I.clear();
      this.g7.clear();
      this.k3.clear();
      this.mp.clear();
      this.D.clear();
      this.gi.clear();
      this.kO.clear();
      this.W.clear();
      this.kX.clear();
      this.L3 = 0;
      this.LQ = null;
      this.kS = -1000000000;
      this.LB.clear();
      this.gk = 0L;
      this.m3 = null;
      this.qo = null;
      this.gr = 0;
      this.Lt = -1000000000;
      this.gA = -1;
      this.mI = 0;
      this.gD = -1000000000;
      this.ky = -1000000000;
      this.mj = this.LO = this.m8 = 0;
      this.qe.i();
      this.kQ = null;
      this.k = -1000000000;
      this.Lk = List.of();
      this.L0 = null;
      this.L1.i();
      this.go.k();
      this.kv = class_243.field_1353;
      this.kx = this.Lh = this.kZ = (boolean)0;
      this.z = Integer.MIN_VALUE;
      this.kp = "";
      this.E = 0;
   }

   @Override
   public void onTick() {
      byte var6 = 1;
      int var5 = this.kL;
      this.kL = var5 + var6;
      this.kv = class_243.field_1353;
      this.kx = this.Lh = this.kZ = (boolean)0;
      if (!this.Lg) {
         if (this.gk != 0L) {
            this.k();
         }
      } else if (mc.field_1687 == this.gP && mc.field_1724 == this.Lz) {
         s0.j var1 = this.l;
         if (var1 == null) {
            this.x("the schematic or its origin changed");
         } else {
            this.z = mc.field_1724.field_6012;
            int var10001;
            if (!mc.field_1724.method_5715()) {
               var10001 = -1000000000;
            } else {
               int var8 = -1000000000;
               int var7 = this.qN;
               var10001 = Integer.compare(var7, var8);
               var10001 = (var10001 | -var10001) >>> 31 == 0 ? mc.field_1724.field_6012 : this.qN;
            }

            this.qN = var10001;
            this.C();
            if (this.LW == null && (!this.m6.getValue() || !this.X())) {
               String var2 = tn.b();
               if (var2 != null) {
                  this.u();
                  this.gF.K();
                  this.L1.i();
                  this.kx = (boolean)1;
                  this.m(var2 + ": swimming up");
               } else if (this.g5.getValue() && this.gF.d(10)) {
                  this.u();
                  this.m("Eating");
               } else {
                  if (mc.field_1755 instanceof class_7743 var3) {
                     int var10000 = mc.field_1724.field_6012 - this.Lt;
                     byte var10 = 40;
                     int var9 = var10000;
                     if ((Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
                        var3.method_25419();
                        return;
                     }
                  }

                  if (this.my == null || !this.T(var1)) {
                     if (this.qF != s0.g.h || (this.ki ? !this.v() : !this.t(var1))) {
                        if (this.m7 != null) {
                           this.x();
                        } else if (!this.r()) {
                           this.J();
                           this.m(mc.field_1755 != null ? "Paused while a screen is open" : "Waiting for world or interaction control");
                        } else if (this.I()) {
                           this.J();
                           this.L1.i();
                           this.go.k();
                           this.m("Paused for manual movement");
                        } else if (mc.field_1755 instanceof class_465) {
                           this.J();
                           this.m("Holding still while an inventory menu is open");
                        } else {
                           this.F(var1);
                           if (this.W()) {
                              this.V();
                           } else if (this.LM == s0.a.X) {
                              this.z(var1);
                           } else if (this.LM == s0.a.K) {
                              this.j();
                           } else if (this.LM == s0.a.m) {
                              this.H();
                           } else if (this.LM == s0.a.Q) {
                              this.E();
                           } else if (this.LM == s0.a.h) {
                              this.g();
                           } else if (!mc.field_1724.method_24828() && !mc.field_1724.method_5799()) {
                              if (this.L1.G()) {
                                 this.y();
                              } else {
                                 this.u();
                                 this.m("Waiting to land");
                              }
                           } else if (this.ki) {
                              this.R(var1);
                           } else if (this.m3 != null) {
                              if (this.m()) {
                                 wp.S(wp.z.M);
                              }

                              if (this.gy == s0.l.s) {
                                 this.Y(var1);
                              } else {
                                 this.f(var1);
                              }
                           } else if (this.B() && wp.o() == wp.z.u) {
                              this.J();
                              this.m("Turning back to your look");
                           } else if (this.m8 > 0) {
                              this.W();
                           } else if (mc.field_1724.field_6012 - this.Lt < this.qw.getValue().intValue()) {
                              this.v();
                           } else {
                              int var13 = mc.field_1724.field_6012 - this.Lt;
                              int var12 = this.qw.getValue().intValue() + this.gf;
                              int var11 = var13;
                              if ((Integer.compare(var11, var12) >>> 31 ^ 1) == 0) {
                                 this.v();
                              } else if (this.C()) {
                                 if (this.L1.G()) {
                                    this.y();
                                 } else {
                                    this.v();
                                 }
                              } else {
                                 this.y(var1);
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               this.D();
            }
         }
      } else {
         this.x("the world or player changed");
      }
   }

   private boolean C() {
      class_7202 var1 = ((q5a3f607535c664c2)mc.field_1687).LarpRig$getPendingUpdateManager();
      if (((q8569136e5ccd2c8f)var1).larp$unconfirmed().isEmpty()) {
         this.kS = -1000000000;
         return false;
      }

      int var2 = mc.field_1724.field_6012;
      if (this.kS == -1000000000) {
         this.kS = var2;
      }

      if (var2 - this.kS == 40) {
         q8.G(this.getName(), "The server has not confirmed a block change for {} ticks; going on", 40);
      }

      if (var2 - this.kS >= 40) {
         return false;
      }

      this.m("Waiting for the server to confirm the last block");
      return true;
   }

   private void J() {
      this.u();
      this.kv = class_243.field_1353;
   }

   private void u() {
      fk.N(this);
      this.m3 = null;
      this.qo = null;
   }

   private boolean I() {
      return mc.method_1569()
         && mc.field_1755 == null
         && (
            com.larpclient.j.W(mc.field_1690.field_1894)
               || com.larpclient.j.W(mc.field_1690.field_1881)
               || com.larpclient.j.W(mc.field_1690.field_1913)
               || com.larpclient.j.W(mc.field_1690.field_1849)
               || com.larpclient.j.W(mc.field_1690.field_1903)
               || com.larpclient.j.W(mc.field_1690.field_1832)
         );
   }

   private double c() {
      return Math.min(this.mC.getValue(), mc.field_1724.method_55754());
   }

   private class_2338 H() {
      return class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.2, mc.field_1724.method_23321());
   }

   private void F(s0.j var1) {
      if (!this.kH.isEmpty()) {
         class_2339 var2 = new class_2339();
         Iterator var3 = this.kH.entrySet().iterator();

         while (var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            int var5 = (Integer)var4.getKey();
            uo.t var6 = this.B(var1, var5, var2);
            class_2680 var7 = this.k6.get(var5);
            int var8 = var6 != uo.t.L || var7 != null && !mc.field_1687.method_8320(var2).equals(var7) ? 0 : 1;
            if (var6 == uo.t.L && var8 == 0) {
               if (mc.field_1724.field_6012 - (Integer)var4.getValue() > 20) {
                  var3.remove();
                  this.k6.remove(var5);
               }
            } else if (var8 != 0) {
               var3.remove();
               this.k6.remove(var5);
               int var9 = this.mR.merge(var5, 1, Integer::sum);
               this.LB.addLast(mc.field_1724.field_6012);
               q8.v(this.getName(), "The server took back {} ({} times)", var2.method_23854(), var9);
               if (var9 >= 3) {
                  this.mR.remove(var5);
                  this.mA.put(var5, mc.field_1724.field_6012 + 2400);
                  this.N(var2.method_23854() + ": the server keeps removing it, skipping it for now.");
                  q8.G(this.getName(), "Server removed {} {} times; skipping for {} ticks", var2.method_23854(), 3, 2400);
               }
            } else if (var6 == uo.t.m) {
               var3.remove();
               this.k6.remove(var5);
               if (uo.d(var1.q(var5), mc.field_1687.method_8320(var2)) < 0) {
                  q8.G(this.getName(), "Placed {} facing the wrong way: {} instead of {}", var2.method_23854(), mc.field_1687.method_8320(var2), var1.q(var5));
                  if (this.Q.merge(var5, 1, Integer::sum) <= 2) {
                     this.B = var5;
                     this.gn = var2.method_10062();
                     this.F = mc.field_1724.field_6012;
                  }
               }
            } else if (mc.field_1724.field_6012 - (Integer)var4.getValue() > 20) {
               var3.remove();
               this.k6.remove(var5);
            }
         }
      }
   }

   private void y(s0.j var1) {
      class_2338 var2 = this.H();
      if (!this.M) {
         this.M = (boolean)1;
         if (this.J(var1)) {
            return;
         }
      }

      if (this.gn == null || !this.j(var1)) {
         if (this.LM == null && this.kA == null && this.kY.contains(var2.method_10074())) {
            if (this.LV == -1000000000) {
               this.LV = mc.field_1724.field_6012;
            }

            if (mc.field_1724.field_6012 - this.LV <= 200) {
               this.m("Coming off a temporary block");
               if (!this.H(var2.method_10074())) {
                  this.v(var2.method_10074());
               }

               return;
            }

            q8.G(this.getName(), "Could not dig {} from on top of it; going on without it", var2.method_10074().method_23854());
            bv.M(this.getName(), var2.method_10074());
            this.kY.remove(var2.method_10074());
         }

         this.LV = -1000000000;
         if (this.kA == null || !this.p(var1)) {
            if (this.LM == s0.a.r && this.mh) {
               this.r();
            } else if (this.LM == s0.a.u) {
               this.P(var1);
            } else if (this.qF == s0.g.N) {
               this.E(var1);
            } else if (this.qF == s0.g.C) {
               this.R();
            } else {
               List var3 = this.h(var1);
               if (!this.h(var3)) {
                  if (this.mU != null) {
                     this.k(var1);
                  } else if (var3 == null && this.w()) {
                     this.n();
                  } else {
                     if (this.LM == s0.a.r && this.gN != null && this.LN.isEmpty() && this.mu != null) {
                        this.o();
                     }

                     if (this.ga <= 0 || this.LM != null || this.kA != null || !this.b(var1)) {
                        if (this.J.isEmpty() || var3 != null && this.Ln >= this.V || !this.kd.getValue() || this.LM != null || !this.P(var1)) {
                           if (this.J.isEmpty() || var3 != null && this.Ln >= this.V || !this.Lb.getValue() || this.LM != null || !this.I(var1)) {
                              long var4 = this.gv * 1000003L + this.g.size() * 1009L + this.Ll.size() * 31L + this.mA.size();
                              long var6 = var2.method_10063() * 31L + var4;
                              if (this.ke.getValue()
                                 && this.qB.getValue()
                                 && this.kA == null
                                 && (this.LM == null || this.LM == s0.a.r)
                                 && !this.w()
                                 && var6 != this.gC) {
                                 for (List var9 : this.g) {
                                    if (this.h(var1, var9)) {
                                       return;
                                    }
                                 }

                                 this.gC = var6;
                              }

                              if (this.ke.getValue() && this.qB.getValue() && this.LM == null && this.kA == null && !this.w() && var4 != this.qG) {
                                 for (List var17 : this.g) {
                                    if (this.o(var1, var17)) {
                                       return;
                                    }
                                 }

                                 this.qG = var4;
                              }

                              if (var3 == null) {
                                 if (this.LM == s0.a.r) {
                                    this.i("the build is done");
                                 } else {
                                    this.w(var1);
                                 }
                              } else {
                                 cm var16 = new cm(var0 -> -1, false);
                                 double var18 = this.c();
                                 int var11 = this.L1.G() && mc.field_1724.field_6012 - this.k <= 100 ? 0 : 1;
                                 l1.g var12 = l1.O(
                                    var16,
                                    var3,
                                    this.LS,
                                    var2.method_10263(),
                                    var2.method_10264(),
                                    var2.method_10260(),
                                    var18,
                                    var1.V(),
                                    var2x -> this.e(var1, var2x),
                                    (var2x, var3x, var4x, var5) -> this.X(var1, var2x) != null,
                                    (var2x, var3x, var4x, var5) -> this.I(var1, var2x, var3x, var4x, var5),
                                    this.LM == s0.a.r ? 120 : 24,
                                    (boolean)var11,
                                    this.qe.t(),
                                    this.LQ,
                                    this.qB.getValue() && this.LM != s0.a.r
                                 );
                                 if ("keep walking".equals(var12.reason())) {
                                    this.y();
                                 } else if (var12.place() != null) {
                                    this.L1.i();
                                    this.Lk = List.of();
                                    this.qe.x();
                                    this.a(var1, var12.place());
                                 } else if (this.LM != s0.a.r || !this.U(var1, var16, var2, var18)) {
                                    if (this.LM != s0.a.r || !var12.walk() || !this.q(var1, var12)) {
                                       if (this.LM != s0.a.r || !this.kj.isEmpty() || !this.z(var1, var12.walkingFor())) {
                                          if (this.LM != s0.a.r || !this.b(var1, var12.walkingFor())) {
                                             if (this.LM == s0.a.r) {
                                                this.i("nothing more to place from the top");
                                             } else if (var12.walk() && this.LT) {
                                                this.LT = (boolean)0;
                                                this.x = (boolean)1;
                                                this.m8 = 12;
                                             } else {
                                                if (var12.walk() && this.qe.Y()) {
                                                   q8.v(
                                                      this.getName(),
                                                      "Nothing placeable from {}; leaving that spot out until a block goes down ({} left out)",
                                                      var2.method_23854(),
                                                      this.qe.t().size()
                                                   );
                                                }

                                                if (var12.walk()) {
                                                   if (!this.qB.getValue()) {
                                                      this.v();
                                                      this.m("Nothing in reach; walk closer (Auto Move is off)");
                                                      this.F("nothing is in reach; walk closer or turn on Auto Move");
                                                   } else {
                                                      this.i(var1, var12);
                                                   }
                                                } else {
                                                   this.L1.i();
                                                   if (var12.reason() == null) {
                                                      this.w(var1);
                                                   } else if ("no standing spot within reach".equals(var12.reason()) && this.n()) {
                                                      this.L(this.gN);
                                                   } else if (!"no standing spot within reach".equals(var12.reason())
                                                      || !this.ke.getValue()
                                                      || !this.S(var1, var12.walkingFor())) {
                                                      for (l1.i var14 : var12.walkingFor()) {
                                                         this.u(var14.id(), var12.reason());
                                                      }

                                                      this.m("Setting aside " + var12.walkingFor().size() + " blocks: " + var12.reason());
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private List<l1.i> h(s0.j var1) {
      class_2339 var2 = new class_2339();
      int var3 = mc.field_1724.field_6012;
      this.mA.values().removeIf(var1x -> var1x <= var3);
      this.J.clear();
      this.LA.clear();
      this.km = 0;
      this.Ln = Integer.MAX_VALUE;
      this.g.clear();
      this.K(var1);
      if (this.Lq.getValue() && this.Lo != null && this.gg == var1) {
         for (int var7 : this.Lo) {
            if (var1.q(var7) != null
               && var1.t[var1.K[var7]] == null
               && !var1.h(var7)
               && !this.Ll.contains(var7)
               && !this.mA.containsKey(var7)
               && !this.g1.contains(var7)
               && this.B(var1, var7, var2) == uo.t.L) {
               class_2680 var8 = var1.q(var7);
               int var9 = var2.method_10264() - var1.X;
               class_1747 var10 = uo.w(var8);
               if (var10 != null && this.R(var10) != 0) {
                  if (uo.v(var8) && mc.field_1687.method_8320(var2.method_10074()).method_45474()) {
                     this.km++;
                  } else {
                     l1.i var11 = new l1.i(var2.method_10263(), var2.method_10264(), var2.method_10260(), var7);
                     this.LA.put(var7, s0.l.s);
                     if (this.e(var1, var11)) {
                        this.V = var9;
                        return List.of(var11);
                     }
                  }
               } else {
                  this.J.merge(var10, 1, Integer::sum);
                  if (var10 != null) {
                     this.Ln = Math.min(this.Ln, var9);
                  }
               }
            }
         }

         this.LA.clear();
      }

      HashSet var14 = this.w() && !this.gN.aims().isEmpty() ? new HashSet<>(this.gN.aims().stream().map(l1.i::id).toList()) : null;

      for (int var15 = 0; var15 + 1 < var1.m.length; var15++) {
         ArrayList var16 = null;
         byte var17 = 0;

         for (int var18 = var1.m[var15]; var18 < var1.m[var15 + 1]; var18++) {
            int var19 = var1.q[var18];
            if (var1.q(var19) != null
               && var1.t[var1.K[var19]] == null
               && !var1.h(var19)
               && !this.Ll.contains(var19)
               && !this.mA.containsKey(var19)
               && !this.md.contains(var19)
               && !this.g1.contains(var19)
               && (var14 == null || var14.contains(var19))) {
               uo.t var20 = this.B(var1, var19, var2);
               class_2680 var21 = var1.q(var19);
               s0.l var12;
               if (var20 == uo.t.L) {
                  var12 = s0.l.s;
               } else if (var20 == uo.t.m && uo.d(var21, mc.field_1687.method_8320(var2)) > 0) {
                  var12 = s0.l.D;
               } else {
                  if (var20 != uo.t.V || !var1.l.containsKey(var19) || this.kn.contains(var19)) {
                     continue;
                  }

                  var12 = s0.l.M;
               }

               if (var12 == s0.l.s) {
                  class_1747 var13 = uo.w(var21);
                  if (var13 == null || this.R(var13) == 0) {
                     this.J.merge(var13, 1, Integer::sum);
                     if (var13 != null) {
                        this.Ln = Math.min(this.Ln, var15);
                     }
                     continue;
                  }

                  if (uo.v(var21) && mc.field_1687.method_8320(var2.method_10074()).method_45474()) {
                     this.km++;
                     continue;
                  }
               }

               this.LA.put(var19, var12);
               if (var16 == null) {
                  var16 = new ArrayList();
               }

               l1.i var22 = new l1.i(var2.method_10263(), var2.method_10264(), var2.method_10260(), var19);
               var16.add(var22);
               if (var17 == 0 && this.e(var1, var22)) {
                  var17 = 1;
               }
            }
         }

         if (var16 != null && var17 != 0) {
            this.V = var15;
            return this.k(var1, var16);
         }

         if (var16 != null) {
            this.g.add(var16);
         }
      }

      return null;
   }

   private void K(s0.j var1) {
      if (this.gq == null) {
         this.s(var1);
         this.X(var1);
      }

      if (!this.gq.isEmpty() && this.mU == null) {
         class_2339 var2 = new class_2339();

         for (l1.c var4 : List.copyOf(this.gq)) {
            byte var5 = 0;
            if (!var4.aims().isEmpty()) {
               for (l1.i var7 : var4.aims()) {
                  if (this.B(var1, var7.id(), var2) == uo.t.L) {
                     var5 = 1;
                     break;
                  }
               }

               if (var5 == 0) {
                  this.L(var4);
               }
            }
         }
      }
   }

   private void s(s0.j var1) {
      this.g1.clear();

      for (int var2 = 0; var2 < var1.n; var2++) {
         if (var1.q(var2) != null && mc.field_1687.method_31601(class_2338.method_10071(var1.G[var2]))) {
            this.g1.add(var2);
         }
      }

      if (!this.g1.isEmpty()) {
         String var3 = "Y " + mc.field_1687.method_31607() + " to " + mc.field_1687.method_31600();
         this.N(
            this.g1.size() + " blocks of the schematic are outside this world's height limits (" + var3 + ") and are left out; move the origin to build them."
         );
         q8.G(this.getName(), "{} cells are outside the height limits ({}) and are left out", this.g1.size(), var3);
      }
   }

   private void X(s0.j var1) {
      this.gq = new ArrayList<>();
      this.md.clear();
      if (this.ke.getValue() && this.qB.getValue() && (!this.Lq.getValue() || this.Lo == null)) {
         ArrayList var2 = new ArrayList();
         class_2339 var3 = new class_2339();

         for (int var4 = 0; var4 < var1.n; var4++) {
            class_2680 var5 = var1.q(var4);
            if (var5 != null && var1.t[var1.K[var4]] == null && !var1.h(var4) && uo.C(var5) && this.B(var1, var4, var3) == uo.t.L) {
               var2.add(new l1.i(var3.method_10263(), var3.method_10264(), var3.method_10260(), var4));
            }
         }

         if (!var2.isEmpty()) {
            HashSet var10 = new HashSet();

            for (l1.i var6 : var2) {
               var10.add(var6.id());
            }

            l1.l var12 = (var3x, var4x, var5x) -> {
               int var6x = var1.K(var3x, var4x, var5x);
               return var6x >= 0
                  && !var10.contains(var6x)
                  && var1.q(var6x) != null
                  && var1.t[var1.K[var6x]] == null
                  && !var1.h(var6x)
                  && this.B(var1, var6x, new class_2339()) == uo.t.L;
            };
            this.gq.addAll(l1.M((var2x, var3x, var4x) -> this.d(var1, var2x, var3x, var4x), var12, var2, var1.V(), this.c() - 0.3));

            for (l1.c var7 : this.gq) {
               for (int[] var9 : var7.cells()) {
                  this.md.add(var1.K(var9[0], var9[1], var9[2]));
               }

               q8.G(
                  this.getName(),
                  "Keeping {} blocks open beside {} {} {} until the {} blocks that need a look up from inside stand",
                  var7.cells().size(),
                  var7.outX(),
                  var7.outY(),
                  var7.outZ(),
                  var7.aims().size()
               );
            }
         }
      }
   }

   private boolean d(s0.j var1, int var2, int var3, int var4) {
      class_2338 var5 = new class_2338(var2, var3, var4);
      int var6 = var1.K(var2, var3, var4);
      int var7 = var6 >= 0 && var1.q(var6) != null && var1.t[var1.K[var6]] == null ? 1 : 0;
      return (var7 != 0 ? var1.q(var6) : mc.field_1687.method_8320(var5)).method_26220(mc.field_1687, var5).method_1110();
   }

   private boolean L(s0.j var1, l1.g var2) {
      if (this.kA == null && this.gq != null) {
         for (l1.c var4 : this.gq) {
            if (var4 != this.gN && this.e(var1, var4, var2.walkingFor())) {
               class_1792 var5 = this.F();
               if (var5 == null) {
                  return false;
               }

               l1.e var6 = l1.e(new cm(var0 -> -1, false), var4, this.m2.getValue().intValue(), var1::Y, var2.walkingFor());
               if (var6 == null) {
                  return false;
               }

               if (this.R(var5) < var6.height()) {
                  this.ga = var6.height();
                  return true;
               }

               this.mu = var6;
               this.LM = s0.a.u;
               this.qs = this.gR = mc.field_1724.field_6012;
               this.L1.i();
               this.Lk = List.of();
               this.gN = var4;
               q8.G(
                  this.getName(),
                  "Tower of {} at {} {} {} leads to the doorway for {} blocks that need a look up",
                  var6.height(),
                  var6.x(),
                  var6.ground(),
                  var6.z(),
                  var4.aims().size()
               );
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean q(s0.j var1, l1.g var2) {
      if (this.gN == null && this.mu != null && !var2.stands().isEmpty() && this.kj.isEmpty() && this.gv != this.La) {
         class_2338 var3 = this.H();
         HashSet var4 = new HashSet();

         for (long var6 : var2.stands()) {
            if (com.larpclient.g8.T(var6) >= var3.method_10264() - 1) {
               var4.add(var6);
            }
         }

         if (!var4.isEmpty() && this.i(var1, var4, new class_2338(var2.toward().x(), var2.toward().y(), var2.toward().z()))) {
            this.LJ = this.gv;
            this.gN = new l1.c(List.of(), var3.method_10263(), var3.method_10264(), var3.method_10260(), List.of(), Set.of());
            this.gq.add(this.gN);
            this.Lk = var2.walkingFor();
            this.o();
            this.y();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean e(s0.j var1, l1.c var2, List<l1.i> var3) {
      for (l1.i var5 : var3) {
         if (!var2.aims().stream().noneMatch(var1x -> var1x.id() == var5.id())) {
            for (long var7 : var2.space()) {
               if (this.I(var1, var5, com.larpclient.g8.x(var7), com.larpclient.g8.T(var7), com.larpclient.g8.G(var7))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean n() {
      return this.w() && this.gN.aims().isEmpty();
   }

   private boolean w() {
      return this.gN != null && !this.LN.isEmpty() && this.mU == null;
   }

   private void o() {
      this.gI = this.mu;
      this.LN.addAll(this.gh);
      this.gh.clear();
      this.mu = null;
      this.LM = null;
      this.qe.i();
      if (this.gN.aims().isEmpty()) {
         q8.G(this.getName(), "Walking off the tower at {} {} {} along the build", this.gN.outX(), this.gN.outY(), this.gN.outZ());
      } else {
         q8.G(this.getName(), "Walking off the tower at {} {} {} for {} blocks", this.gN.outX(), this.gN.outY(), this.gN.outZ(), this.gN.aims().size());
      }
   }

   private void n() {
      byte var1 = 0;

      for (l1.i var3 : this.gN.aims()) {
         if (this.mA.remove(var3.id()) != null) {
            var1 = 1;
         }
      }

      if (var1 == 0) {
         this.L(this.gN);
      }
   }

   private void L(l1.c var1) {
      if (var1 == this.gN && !this.LN.isEmpty()) {
         this.mU = var1;
         this.mE = mc.field_1724.field_6012;
         this.L1.i();
         q8.G(this.getName(), "Going back to the tower at {} {} {}", var1.outX(), var1.outY(), var1.outZ());
      } else {
         this.i(var1);
      }
   }

   private void i(l1.c var1) {
      this.gq.remove(var1);
      if (this.l != null) {
         for (int[] var3 : var1.cells()) {
            int var4 = this.l.K(var3[0], var3[1], var3[2]);
            this.md.remove(var4);
            this.LS.remove(var4);
         }
      }

      this.qe.i();
   }

   private void k(s0.j var1) {
      l1.c var2 = this.mU;
      class_2338 var3 = this.H();
      class_2338 var4 = new class_2338(var2.outX(), var2.outY(), var2.outZ());
      if (var3.equals(var4)) {
         this.L1.i();
         if (var2.aims().isEmpty() && this.gv == this.LJ) {
            this.La = this.gv;
         }

         class_243 var6 = class_243.method_24955(var4).method_1020(mc.field_1724.method_73189()).method_18805(1.0, 0.0, 1.0);
         if (var6.method_1033() >= 0.25 && mc.field_1724.field_6012 - this.mE <= 600) {
            this.Lh = (boolean)1;
            this.kv = var6;
            this.m("Stepping to the middle of the tower");
         } else {
            this.gh.addAll(this.LN);
            this.LN.clear();
            this.mu = this.gI;
            this.gI = null;
            this.LM = s0.a.r;
            this.i = this.gv;
            this.mU = null;
            this.gN = null;
            this.i(var2);
         }
      } else {
         this.m("Going back to the tower");
         int var5 = mc.field_1724.field_6012 - this.mE > 600 ? 1 : 0;
         if (var5 != 0 || !this.L1.G() && !this.i(var1, Set.of(com.larpclient.g8.M(var4.method_10263(), var4.method_10264(), var4.method_10260())), var4)) {
            q8.G(
               this.getName(),
               "No way back to the tower from {} ({}); it is swept up at the end",
               var3.method_23854(),
               var5 != 0 ? "took too long" : "no route"
            );
            if (var2.aims().isEmpty() && this.gv == this.LJ) {
               this.La = this.gv;
            }

            this.L1.i();
            this.kY.addAll(this.LN);
            this.LN.clear();
            this.gI = null;
            this.mU = null;
            this.gN = null;
            this.i(var2);
         } else {
            this.y();
         }
      }
   }

   private List<l1.i> k(s0.j var1, List<l1.i> var2) {
      HashSet var3 = new HashSet();

      for (l1.i var5 : var2) {
         class_2680 var6 = var1.q(var5.id());
         if (uo.z(var6) && this.LA.getOrDefault(var5.id(), s0.l.s) == s0.l.s) {
            class_2350 var7 = (class_2350)var6.method_11654(class_2741.field_12525);
            if (!var7.method_10166().method_10178()) {
               for (int var8 = 1; var8 <= 3; var8++) {
                  var3.add(com.larpclient.g8.M(var5.x() + var7.method_10148() * var8, var5.y(), var5.z() + var7.method_10165() * var8));
               }
            }
         }
      }

      if (var3.isEmpty()) {
         return var2;
      }

      List var9 = var2.stream().filter(var2x -> uo.z(var1.q(var2x.id())) || !var3.contains(com.larpclient.g8.M(var2x.x(), var2x.y(), var2x.z()))).toList();
      return var9.stream().anyMatch(var2x -> this.e(var1, var2x)) ? var9 : var2;
   }

   private boolean e(s0.j var1, l1.i var2) {
      class_2338 var3 = new class_2338(var2.x(), var2.y(), var2.z());
      return this.LA.getOrDefault(var2.id(), s0.l.s) != s0.l.s
         ? true
         : uo.I(var3) || mc.field_1687.method_8320(var3).method_27852(var1.q(var2.id()).method_26204());
   }

   private class_3965 X(s0.j var1, l1.i var2) {
      class_2680 var3 = var1.q(var2.id());
      class_2338 var4 = new class_2338(var2.x(), var2.y(), var2.z());
      if (this.LA.getOrDefault(var2.id(), s0.l.s) != s0.l.s) {
         return uo.l(this, var4, mc.field_1724.method_33571(), this.c(), var3);
      }

      class_1799 var5 = this.S(uo.w(var3));
      if (var5.method_7960()) {
         return null;
      }

      if (mc.field_1724.method_5829().method_994(new class_238(var4))) {
         return null;
      }

      class_2338 var6 = uo.E(var3, var4);
      return var6 != null && mc.field_1724.method_5829().method_994(new class_238(var6))
         ? null
         : uo.z(this, mc.field_1724, var5, var4, var3, mc.field_1724.method_33571(), this.c(), true);
   }

   private boolean I(s0.j var1, l1.i var2, int var3, int var4, int var5) {
      class_2680 var6 = var1.q(var2.id());
      class_2338 var7 = new class_2338(var2.x(), var2.y(), var2.z());
      double var8 = this.c() - 0.3;
      if (this.LA.getOrDefault(var2.id(), s0.l.s) != s0.l.s) {
         return uo.l(this, var7, new class_243(var3 + 0.5, var4 + 1.62, var5 + 0.5), var8, var6) != null;
      }

      class_1799 var10 = this.S(uo.w(var6));
      if (var10.method_7960()) {
         return false;
      }

      class_2338 var11 = uo.E(var6, var7);
      if (var11 == null || var11.method_10263() != var3 || var11.method_10260() != var5 || var11.method_10264() != var4 && var11.method_10264() != var4 + 1) {
         for (double var15 : new double[]{1.62, mc.field_1724.method_18381(class_4050.field_18081)}) {
            if (uo.X(this, mc.field_1724, var10, var7, var6, new class_243(var3 + 0.5, var4 + var15, var5 + 0.5), var8, true, true) == null) {
               return false;
            }

            if (!this.U.getValue()) {
               break;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private class_1799 S(class_1792 var1) {
      if (var1 == null) {
         return class_1799.field_8037;
      }

      class_1661 var2 = mc.field_1724.method_31548();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2.method_5438(var3).method_31574(var1)) {
            return var2.method_5438(var3);
         }
      }

      return class_1799.field_8037;
   }

   private void u(int var1, String var2) {
      int var3 = this.mo.merge(var1, 1, Integer::sum);
      String var4 = this.l == null ? "?" : this.l.P(var1).method_23854();
      if (var3 >= 3) {
         this.Ll.add(var1);
         this.kW.put(var1, var2);
         q8.G(this.getName(), "Giving up on {}: {}", var4, var2);
      } else {
         this.mA.put(var1, mc.field_1724.field_6012 + 400);
         q8.v(this.getName(), "Setting {} aside for now: {}", var4, var2);
      }
   }

   private void w(s0.j var1) {
      this.L1.i();
      this.qe.i();
      if (!this.mA.isEmpty()) {
         byte var2 = 0;

         for (Integer var4 : new ArrayList<>(this.mA.keySet())) {
            if (!this.Ll.contains(var4)) {
               this.mA.remove(var4);
               var2 = 1;
            }
         }

         if (var2 != 0) {
            q8.v(this.getName(), "Retrying blocks set aside");
            this.m("Trying the blocks set aside again");
            return;
         }
      }

      if (!this.kY.isEmpty()) {
         this.U(var1);
      } else if (!this.Ll.isEmpty() && this.LM == null && this.LN.isEmpty() && this.N()) {
         class_2338 var6 = this.H();
         this.x("no way to walk from where you stand at " + var6.method_23854() + "; stand on the floor beside the build and start again");
      } else if (!this.Ll.isEmpty() && this.gv > this.Le) {
         this.Le = this.gv;
         q8.G(this.getName(), "Trying the {} blocks given up on again, now that more of the build stands", this.Ll.size());
         this.h.clear();
         this.Ll.clear();
         this.kW.clear();
         this.mo.clear();
         this.m("Trying the blocks given up on again");
      } else if (this.qY.values().stream().anyMatch(var0 -> var0 >= 2) && this.gv > this.Lr) {
         this.Lr = this.gv;
         this.qY.clear();
         this.L9 = this.gv;
         this.qG = Long.MIN_VALUE;
         this.gC = Long.MIN_VALUE;
         q8.G(this.getName(), "Trying temporary supports and towers once more, {} blocks placed so far", this.gv);
         this.m("Trying towers again");
      } else if (this.gq != null && !this.gq.isEmpty()) {
         this.L(this.gq.getFirst());
      } else if (!this.J.isEmpty()) {
         ArrayList var5 = new ArrayList();

         for (Entry var8 : this.J.entrySet()) {
            var5.add(var8.getValue() + " " + (var8.getKey() == null ? "unplaceable" : R((class_1792)var8.getKey())));
         }

         this.x(
            "out of blocks: "
               + String.join(", ", var5.subList(0, Math.min(5, var5.size())))
               + (var5.size() > 5 ? " and more" : "")
               + (this.kq.isEmpty() ? "" : " (the marked containers have none)")
         );
      } else {
         this.b();
      }
   }

   private void a(s0.j var1, l1.i var2) {
      this.m3 = var2;
      this.gy = this.LA.getOrDefault(var2.id(), s0.l.s);
      this.gr = mc.field_1724.field_6012;
      this.qo = null;
      if (this.m()) {
         wp.S(wp.z.M);
      }

      if (this.gy == s0.l.s) {
         this.Y(var1);
      } else {
         this.f(var1);
      }
   }

   private void Y(s0.j var1) {
      l1.i var2 = this.m3;
      class_2338 var3 = new class_2338(var2.x(), var2.y(), var2.z());
      class_2339 var4 = new class_2339();
      if (this.B(var1, var2.id(), var4) != uo.t.L) {
         this.u();
      } else if (mc.field_1724.field_6012 - this.gr > 40) {
         q8.v(this.getName(), "Placing {} took too long: {}", var3.method_23854(), fk.c(this));
         this.u();
         this.u(var2.id(), "could not place it");
      } else {
         class_2680 var5 = var1.q(var2.id());
         class_1747 var6 = uo.w(var5);
         int var7 = this.m(var6);
         if (var7 < 0) {
            this.e(var6);
         } else {
            this.v();
            if (!this.m(var7)) {
               this.m("Taking out " + mc.field_1724.method_31548().method_5438(var7).method_7964().getString());
            } else {
               class_3965 var8 = this.X(var1, var2);
               if (var8 == null) {
                  this.u();
               } else {
                  this.qo = var8;
                  this.mZ = mc.field_1687.method_8320(var3);
                  class_2338 var9 = var8.method_17777();
                  this.q(var9);
                  double var10 = this.c();
                  boolean var12 = fk.j(
                     this,
                     var3,
                     var8,
                     var6,
                     var10,
                     true,
                     () -> fk.j(var3, var6) && !this.I(),
                     () -> fk.N(this, var9, var10) && uo.u(mc.field_1724, var8) && uo.t(mc.field_1724, var9) && this.v(var3, var9, var8.method_17780(), var5),
                     var3x -> this.B(var1, var2, var3x)
                  );
                  this.m(
                     var12
                        ? "Placing " + var5.method_26204().method_9518().getString() + " at " + var3.method_23854()
                        : "Cannot place at " + var3.method_23854() + ": " + fk.c(this)
                  );
               }
            }
         }
      }
   }

   private void q(class_2338 var1) {
      if (uo.O(mc.field_1687.method_8320(var1))) {
         this.Lh = (boolean)1;
      }
   }

   private boolean o(class_2338 var1) {
      return !uo.O(mc.field_1687.method_8320(var1)) || this.qN != -1000000000 && mc.field_1724.field_6012 - this.qN >= 3;
   }

   private boolean v(class_2338 var1, class_2338 var2, class_2350 var3, class_2680 var4) {
      if (!this.o(var2)) {
         return false;
      }

      if (!uo.d(var4)) {
         return true;
      }

      class_243 var5 = mc.field_1724.method_5720();
      class_243 var6 = mc.field_1724.method_33571();
      class_3965 var7 = fk.E(this, var6, var2, var5, this.c());
      if (var7 != null && var7.method_17780() == var3) {
         class_1799 var8 = this.S(uo.w(var4));
         class_2680 var9 = mc.field_1687.method_8320(var1);
         q586814c6d8f3640b var10 = (q586814c6d8f3640b)mc.field_1724;
         if (!(Math.abs(class_3532.method_15393(mc.field_1724.method_36454() - var10.larpYRotLast())) > 2.0F)
            && !(Math.abs(mc.field_1724.method_36455() - var10.larpXRotLast()) > 2.0F)) {
            for (float[] var14 : new float[][]{{mc.field_1724.method_36454(), mc.field_1724.method_36455()}, {var10.larpYRotLast(), var10.larpXRotLast()}}) {
               class_2680 var15 = uo.w(mc.field_1724, var8, var7, var14[0], var14[1]);
               if (!uo.i(var4, var9, var15)) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void B(s0.j var1, l1.i var2, boolean var3) {
      if (var3 && this.qo != null) {
         q8.v(
            this.getName(),
            "Click for {} {} {}: {} face of {}, held {}, player at {}",
            var2.x(),
            var2.y(),
            var2.z(),
            this.qo.method_17780().method_15434(),
            this.qo.method_17777().method_23854(),
            mc.field_1724.method_6047().method_7964().getString(),
            mc.field_1724.method_24515().method_23854()
         );
      }

      this.Lt = mc.field_1724.field_6012;
      this.m3 = null;
      this.qo = null;
      if (!var3) {
         q8.v(this.getName(), "Placement at {} {} {} was not accepted", var2.x(), var2.y(), var2.z());
      } else {
         this.LQ = var2;
         this.gv++;
         this.LT = (boolean)0;
         this.gf = this.qy.nextInt(5) + (this.qy.nextInt(100) < 6 ? 8 + this.qy.nextInt(16) : 0);
         this.kH.put(var2.id(), mc.field_1724.field_6012);
         if (this.mZ != null) {
            this.k6.put(var2.id(), this.mZ);
         }
      }
   }

   private void f(s0.j var1) {
      l1.i var2 = this.m3;
      class_2338 var3 = new class_2338(var2.x(), var2.y(), var2.z());
      class_2339 var4 = new class_2339();
      uo.t var5 = this.B(var1, var2.id(), var4);
      class_2680 var6 = var1.q(var2.id());
      int var7 = this.gy == s0.l.D
         ? (var5 == uo.t.m && uo.d(var6, mc.field_1687.method_8320(var3)) > 0 ? 1 : 0)
         : (var5 == uo.t.V && !this.kn.contains(var2.id()) && this.my == null ? 1 : 0);
      if (var7 == 0) {
         this.u();
      } else if (mc.field_1724.field_6012 - this.gr > 40) {
         q8.v(this.getName(), "Clicking {} took too long: {}", var3.method_23854(), fk.c(this));
         this.u();
         this.u(var2.id(), this.gy == s0.l.D ? "could not adjust it" : "could not open the crafter");
      } else {
         this.kv = class_243.field_1353;
         this.Lh = (boolean)0;
         int var8 = this.l();
         if (var8 >= 0 && !this.m(var8)) {
            this.m("Putting the block away");
         } else {
            class_3965 var9 = this.X(var1, var2);
            if (var9 == null) {
               this.u();
            } else {
               this.qo = var9;
               class_1799 var10 = mc.field_1724.method_6047();
               class_1792 var11 = var10.method_7960() ? class_1802.field_8162 : var10.method_7909();
               double var12 = this.c();
               boolean var14 = fk.E(
                  this,
                  var3,
                  var11,
                  var12,
                  true,
                  false,
                  () -> !this.I(),
                  () -> !mc.field_1724.method_5715()
                     && mc.field_1724.method_18798().method_37268() < 1.0E-4
                     && fk.N(this, var3, var12)
                     && uo.z(var6, mc.field_1724.method_36454())
                     && uo.z(var6, ((q586814c6d8f3640b)mc.field_1724).larpYRotLast()),
                  var2x -> this.k(var2, var2x)
               );
               String var15 = this.gy == s0.l.M ? "Opening the crafter at " : "Adjusting " + var6.method_26204().method_9518().getString() + " at ";
               this.m(var14 ? var15 + var3.method_23854() : "Cannot click " + var3.method_23854() + ": " + fk.c(this));
            }
         }
      }
   }

   private int l() {
      class_1661 var1 = mc.field_1724.method_31548();
      if (!(var1.method_7391().method_7909() instanceof class_1747)) {
         return var1.method_67532();
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.method_5438(var2).method_7960()) {
            return var2;
         }
      }

      for (int var3 = 0; var3 < 9; var3++) {
         if (!(var1.method_5438(var3).method_7909() instanceof class_1747)) {
            return var3;
         }
      }

      return -1;
   }

   private void k(l1.i var1, boolean var2) {
      this.Lt = mc.field_1724.field_6012;
      s0.l var3 = this.gy;
      this.m3 = null;
      this.qo = null;
      this.gy = s0.l.s;
      if (!var2) {
         q8.v(this.getName(), "Click on {} {} {} was not accepted", var1.x(), var1.y(), var1.z());
      } else {
         this.LQ = var1;
         if (var3 == s0.l.M) {
            this.my = var1.id();
            this.mb = mc.field_1724.field_6012;
            this.mf = -1000000000;
         } else {
            this.LU++;
         }
      }
   }

   private boolean T(s0.j var1) {
      int var2 = mc.field_1724.field_6012;
      int var3 = this.my;
      class_2338 var4 = var1.P(var3);
      this.J();
      if (mc.field_1755 instanceof class_8898 var5) {
         if (this.mf == -1000000000) {
            this.mf = var2;
         }

         if (var2 - this.mb > 100) {
            this.my = null;
            mc.field_1724.method_7346();
            this.u(var3, "could not set the crafter's slots");
            return true;
         }

         this.m("Setting the crafter's slots at " + var4.method_23854());
         if (var2 - this.mf >= 6 && var2 - this.qT >= 4) {
            class_8881 var10 = (class_8881)var5.method_17577();
            int[] var7 = var1.l.get(var3);

            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = Arrays.binarySearch(var7, var8) >= 0 ? 1 : 0;
               if (var10.method_54461(var8) != var9) {
                  if (var9 != 0 && !var10.method_7611(var8).method_7677().method_7960()) {
                     this.my = null;
                     mc.field_1724.method_7346();
                     this.u(var3, "a crafter slot to disable holds an item");
                     return true;
                  }

                  var10.method_54458(var8, var9 == 0);
                  mc.field_1761.method_54634(var8, var10.field_7763, var9 == 0);
                  this.qT = var2;
                  return true;
               }
            }

            this.kn.add(var3);
            this.my = null;
            mc.field_1724.method_7346();
            this.Lt = var2;
            q8.G(this.getName(), "Crafter at {}: disabled slots {}", var4.method_23854(), Arrays.toString(var7));
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.mb <= 50) {
         this.m("Waiting for the crafter at " + var4.method_23854() + " to open");
         return true;
      } else {
         this.my = null;
         this.u(var3, "the crafter did not open");
         return false;
      }
   }

   private int m(class_1792 var1) {
      class_1661 var2 = mc.field_1724.method_31548();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var2.method_5438(var3).method_31574(var1)) {
            return var3;
         }
      }

      return -1;
   }

   private boolean m(int var1) {
      class_1661 var2 = mc.field_1724.method_31548();
      if (var2.method_67532() != var1) {
         var2.method_61496(var1);
         this.gA = var1;
         this.mI = mc.field_1724.field_6012;
         return false;
      }

      if (this.gA != var1) {
         this.gA = var1;
         this.mI = mc.field_1724.field_6012;
      }

      return mc.field_1724.field_6012 - this.mI >= 3;
   }

   private void e(class_1792 var1) {
      l1.i var2 = this.m3;
      if (!this.LE.getValue()) {
         this.u();
         this.F("put " + R(var1) + " in your hotbar (Refill Hotbar is off)");
         this.u(var2.id(), "not in the hotbar");
      } else if (this.O(var1) == -2) {
         this.u();
         if (this.Lw.startsWith("make room")) {
            this.F(this.Lw);
         }

         this.u(var2.id(), this.Lw.startsWith("make room") ? "no hotbar room" : this.Lw);
      }
   }

   private int O(class_1792 var1) {
      int var2 = this.m(var1);
      if (var2 >= 0) {
         return var2;
      }

      class_1661 var3 = mc.field_1724.method_31548();
      int var4 = -1;

      for (int var5 = 9; var5 < 36; var5++) {
         if (var3.method_5438(var5).method_31574(var1)) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         this.Lw = "no " + R(var1) + " left";
         return -2;
      }

      int var11 = -1;

      for (int var6 = 0; var6 < 9 && var11 < 0; var6++) {
         if (var3.method_5438(var6).method_7960()) {
            var11 = var6;
         }
      }

      Map var12 = this.M();

      for (int var7 = 0; var7 < 9 && var11 < 0; var7++) {
         class_1799 var8 = var3.method_5438(var7);
         if (var8.method_7909() instanceof class_1747 && !var12.containsKey(var8.method_7909()) && var7 != var3.method_67532()) {
            var11 = var7;
         }
      }

      int var13 = Integer.MAX_VALUE;
      if (var11 < 0) {
         for (int var14 = 0; var14 < 9; var14++) {
            class_1799 var9 = var3.method_5438(var14);
            if (var9.method_7909() instanceof class_1747 && !var9.method_31574(var1) && var14 != var3.method_67532()) {
               int var10 = var12.getOrDefault(var9.method_7909(), 0);
               if (var10 < var13) {
                  var13 = var10;
                  var11 = var14;
               }
            }
         }
      }

      if (var11 < 0) {
         this.Lw = "make room in your hotbar for " + R(var1) + " (an empty slot, or a block)";
         return -2;
      }

      this.kv = class_243.field_1353;
      this.Lh = (boolean)0;
      if (mc.field_1724.method_18798().method_37268() > 1.0E-4) {
         this.ky = -1000000000;
         this.m("Stopping to move a block into the hotbar");
         return -1;
      }

      if (this.ky == -1000000000) {
         this.ky = mc.field_1724.field_6012;
      }

      if (mc.field_1724.field_6012 - this.ky >= 4 && mc.field_1724.field_6012 - this.gD >= 4) {
         class_1723 var15 = mc.field_1724.field_7498;
         mc.field_1761.method_2906(var15.field_7763, var4, var11, class_1713.field_7791, mc.field_1724);
         this.gD = mc.field_1724.field_6012;
         this.ky = -1000000000;
         q8.v(this.getName(), "Moved {} from inventory slot {} to hotbar slot {}", R(var1), var4, var11 + 1);
         this.m("Moved " + R(var1) + " into hotbar slot " + (var11 + 1));
         return -1;
      } else {
         this.m("Moving " + R(var1) + " into the hotbar");
         return -1;
      }
   }

   private Map<class_1792, Integer> M() {
      s0.j var1 = this.l;
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      }

      for (int var3 = 0; var3 < var1.n; var3++) {
         if (var1.M(var3) == uo.t.L && var1.q(var3) != null && !var1.h(var3)) {
            class_1747 var4 = uo.w(var1.q(var3));
            if (var4 != null) {
               var2.merge(var4, 1, Integer::sum);
            }
         }
      }

      return var2;
   }

   private void i(s0.j var1, l1.g var2) {
      class_2338 var3 = this.H();
      l1.i var4 = var2.toward();
      class_2338 var5 = new class_2338(var4.x(), var4.y(), var4.z());
      if (!this.L1.G() || !var5.equals(this.L0) && mc.field_1724.field_6012 - this.k > 20 || mc.field_1724.field_6012 - this.k > 100) {
         this.k = mc.field_1724.field_6012;
         this.L0 = var5;
         this.Lk = var2.walkingFor();
         g8.l var6 = new g8.l(
            Math.min(var1.S, var3.method_10263()) - 10,
            Math.min(var1.X, var3.method_10264()) - 6,
            Math.min(var1.N, var3.method_10260()) - 10,
            Math.max(var1.E, var3.method_10263()) + 10,
            Math.max(var1.D, var3.method_10264()) + 6,
            Math.max(var1.M, var3.method_10260()) + 10
         );
         g8.t var7 = com.larpclient.g8.O(
            new cm(var0 -> -1, false),
            var3.method_10263(),
            var3.method_10264(),
            var3.method_10260(),
            g8.i.S(var2.stands(), var4.x(), var4.y() - 1, var4.z(), this.c()),
            var6,
            g8.b.walking(16000)
         );
         if (!var7.found() || var7.steps().isEmpty()) {
            this.L1.i();
            if (this.n()) {
               this.L(this.gN);
               return;
            }

            q8.v(this.getName(), "No route to stand within reach of {} ({} cells searched)", var5.method_23854(), var7.explored());
            if (this.ke.getValue() && this.LM == null && this.L(var1, var2)) {
               return;
            }

            if (this.ke.getValue() && this.LM == null && this.S(var1, var2.walkingFor())) {
               return;
            }

            if (this.ke.getValue() && this.LM == null && this.T(var1, var2)) {
               return;
            }

            for (l1.i var13 : var2.walkingFor()) {
               this.u(var13.id(), "no way to walk within reach");
            }

            return;
         }

         if (this.w() && this.gN.cells().isEmpty() && this.gI != null && var7.steps().stream().anyMatch(var1x -> var1x.y() < this.gN.outY() - 1)) {
            g8.t var8 = com.larpclient.g8.O(
               new cm(var0 -> -1, false),
               this.gI.x(),
               this.gI.ground(),
               this.gI.z(),
               g8.i.S(var2.stands(), var4.x(), var4.y() - 1, var4.z(), this.c()),
               var6,
               g8.b.walking(16000)
            );
            if (var8.found()) {
               this.L1.i();
               q8.v(this.getName(), "The spot for {} needs a drop off the build; the tower comes down first", var5.method_23854());
               this.L(this.gN);
               return;
            }
         }

         this.L1.X(var7.steps());
         this.L1.p();
         this.LP = 0.3 + this.qy.nextDouble() * 0.7;
         this.kM = this.qy.nextDouble() * Math.PI * 2.0;
         class_1747 var11 = this.LA.getOrDefault(var4.id(), s0.l.s) == s0.l.s ? uo.w(var1.q(var4.id())) : null;
         int var9 = var11 == null ? -1 : this.m(var11);
         if (var9 >= 0) {
            mc.field_1724.method_31548().method_61496(var9);
         }

         g8.p var10 = var7.steps().getLast();
         this.kQ = new class_2338(var10.x(), var10.y(), var10.z());
         q8.v(this.getName(), "Route of {} steps to {} for {}", var7.steps().size(), this.kQ.method_23854(), var5.method_23854());
      }

      this.y();
   }

   private void y() {
      e3.l var1 = this.L1.d(this.c(), var0 -> -1, this.kw.getValue());
      this.kv = this.L1.g();
      this.kx = (boolean)(!this.L1.t() && !this.z() ? 0 : 1);
      this.Lh = (boolean)(!this.L1.V() && (!this.U.getValue() || !this.K() || this.L1.M().isEmpty() || this.L1.M().getFirst().move() != g8.c.S) ? 0 : 1);
      this.kZ = (boolean)(this.L1.C() && !this.Lh ? 1 : 0);
      this.m("Walking to " + (this.L0 == null ? "the next block" : this.L0.method_23854()));
      if (var1 == e3.l.L) {
         this.L1.i();
         this.mj = 0;
         this.m8 = 12;
         this.LT = (boolean)1;
         class_2338 var2 = this.H();
         this.qe.e(var2.method_10263(), var2.method_10264(), var2.method_10260());
      } else if (var1 == e3.l.s) {
         q8.v(this.getName(), "Route stuck: {}", this.L1.D());
         this.L1.i();
         if (this.LM == s0.a.u) {
            this.u("stuck on the way to it");
            return;
         }

         if (this.kA == s0.p.i) {
            this.Y("stuck on the way to it");
            return;
         }

         if (this.kA == s0.p.s) {
            return;
         }

         if (this.kQ != null) {
            this.qe.J(this.kQ.method_10263(), this.kQ.method_10264(), this.kQ.method_10260());
         }

         if (++this.mj >= 3) {
            this.mj = 0;

            for (l1.i var3 : this.Lk) {
               this.u(var3.id(), "stuck on the way");
            }
         }
      }
   }

   private void W() {
      this.m8--;
      if (!this.x) {
         if (mc.field_1724.method_18798().method_37267() < 0.03) {
            this.m8 = 0;
         }

         this.m("Stopping");
      } else {
         if (this.m8 <= 0) {
            this.x = (boolean)0;
         }

         class_2338 var1 = this.H();
         class_243 var2 = class_243.method_24955(var1);
         class_243 var3 = var2.method_1020(mc.field_1724.method_73189()).method_18805(1.0, 0.0, 1.0);
         double var4 = mc.field_1724.method_18798().method_37267();
         if (var3.method_1033() < 0.12 && var4 < 0.01) {
            this.m8 = 0;
            this.x = (boolean)0;
         } else {
            double var6 = var3.method_1027() < 1.0E-6 ? 0.0 : mc.field_1724.method_18798().method_18805(1.0, 0.0, 1.0).method_1026(var3.method_1029());
            this.Lh = (boolean)1;
            this.kv = this.go.V(var3.method_1033(), var6, cr.a(this)) ? var3 : class_243.field_1353;
            this.m("Stopping on the spot");
         }
      }
   }

   private boolean z() {
      int var1 = mc.field_1724.field_6012;
      if (this.L1.U() && this.L1.C() && mc.field_1724.method_5624() && mc.field_1724.method_24828()) {
         if (!mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(0.0, 1.3, 0.0))) {
            return false;
         }

         if (this.k9 != -1000000000 && var1 < this.k9) {
            this.kE++;
            return true;
         }

         if (this.kK != -1000000000 && var1 < this.kK) {
            return false;
         }

         this.kK = var1 + 30 + this.qy.nextInt(110);
         if (this.qy.nextInt(100) >= 60) {
            return false;
         }

         this.k9 = var1 + 12 + this.qy.nextInt(45);
         this.kE++;
         return true;
      } else {
         return false;
      }
   }

   private void C() {
      float var1 = mc.field_1724.method_36454();
      float var2 = Float.isNaN(this.m0) ? 0.0F : class_3532.method_15393(var1 - this.m0);
      this.m0 = var1;
      this.LX = Math.max(this.LX, Math.abs(var2));
      if (Math.abs(var2) > 26.0F && this.g8 < 12) {
         this.g8++;
         q8.G(
            this.getName(),
            "Sharp turn of {} degrees in one tick while: {} (rotation owner {})",
            String.format(Locale.ROOT, "%.1f", var2),
            this.getStatus(),
            t9.I() && t9.k(t9.v.e) ? "bot" : "other"
         );
      }

      if (Math.abs(var2) > 6.0F && Math.abs(this.m1) > 6.0F && Math.signum(var2) != Math.signum(this.m1)) {
         this.w++;
      }

      this.m1 = var2;
   }

   private void v() {
      if (this.U.getValue() && mc.field_1724.method_24828() && this.K()) {
         this.Lh = (boolean)1;
      }
   }

   private boolean K() {
      class_2338 var1 = this.H();

      for (class_2350 var3 : class_2353.field_11062) {
         class_2338 var4 = var1.method_10093(var3);
         if (this.E(var4) && this.E(var4.method_10074()) && this.E(var4.method_10087(2))) {
            return true;
         }
      }

      for (int[] var5 : new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}) {
         class_2338 var6 = var1.method_10069(var5[0], 0, var5[1]);
         if (this.E(var6) && this.E(var6.method_10074()) && this.E(var6.method_10087(2))) {
            return true;
         }
      }

      return false;
   }

   private boolean E(class_2338 var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1);
      return var2.method_26220(mc.field_1687, var1).method_1110();
   }

   private class_1792 F() {
      class_1792 var1 = switch ((String)this.N.getValue()) {
         case "Cobblestone" -> class_1802.field_20412;
         case "Netherrack" -> class_1802.field_8328;
         case "Cobbled Deepslate" -> class_1802.field_29025;
         default -> class_1802.field_8831;
      };
      return this.l != null && f(this.l, var1) ? null : var1;
   }

   private static boolean f(s0.j var0, class_1792 var1) {
      for (class_2680 var5 : var0.C) {
         if (var5 != null && uo.w(var5) == var1) {
            return true;
         }
      }

      return false;
   }

   private boolean S(s0.j var1, List<l1.i> var2) {
      if (this.kA != null) {
         return false;
      }

      class_1792 var3 = this.F();
      if (var3 == null) {
         this.F("the build uses " + this.N.getValue().toLowerCase(Locale.ROOT) + "; pick another Pillar Block for temporary pillars");
         return false;
      }

      class_2338 var4 = this.H();
      if (this.gv > this.Lc) {
         this.v.clear();
      }

      HashSet var5 = new HashSet<>(this.gQ);
      var5.addAll(this.v);
      var2 = var2.stream().filter(var1x -> !this.h.contains(var1x.id())).toList();
      if (var2.isEmpty()) {
         return false;
      } else {
         l1.e var6 = l1.q(
            new cm(var0 -> -1, false),
            var2,
            this.E(var1, 2),
            var4.method_10263(),
            var4.method_10264(),
            var4.method_10260(),
            this.c(),
            this.m2.getValue().intValue(),
            var1::Y,
            (var2x, var3x, var4x, var5x) -> this.c(var1, var2x, var3x, var4x, var5x),
            var5,
            600
         );
         if (var6 == null) {
            q8.v(this.getName(), "No pillar reaches {} blocks", var2.size());
            this.j(var1, (l1.i)var2.getFirst());
            return false;
         } else if (this.R(var3) < var6.height()) {
            this.ga = var6.height();
            return true;
         } else {
            this.mu = var6;
            this.LM = s0.a.u;
            this.qs = this.gR = mc.field_1724.field_6012;
            this.L1.i();
            this.Lk = List.of();
            q8.v(this.getName(), "Planned a pillar of {} at {} {} {}", var6.height(), var6.x(), var6.ground(), var6.z());
            return true;
         }
      }
   }

   private void j(s0.j var1, l1.i var2) {
      if (this.L5.add(var2.id())) {
         class_2680 var3 = var1.q(var2.id());
         class_1799 var4 = this.S(uo.w(var3));
         if (!var4.method_7960()) {
            cm var5 = new cm(var0 -> -1, false);
            class_2338 var6 = new class_2338(var2.x(), var2.y(), var2.z());
            int var7 = (int)Math.floor(this.c());
            int var8 = this.m2.getValue().intValue();
            TreeMap var9 = new TreeMap();
            String var10 = null;

            for (int var11 = -var7; var11 <= var7; var11++) {
               for (int var12 = -var7; var12 <= var7; var12++) {
                  if ((var11 != 0 || var12 != 0) && !(var11 * var11 + var12 * var12 > this.c() * this.c())) {
                     int var13 = var2.x() + var11;
                     int var14 = var2.z() + var12;
                     int var15 = Integer.MIN_VALUE;

                     for (int var16 = var2.y(); var16 >= var2.y() - var8 - 3; var16--) {
                        if (var5.loaded(var13, var16, var14)
                           && var5.floor(var13, var16 - 1, var14)
                           && var5.open(var13, var16, var14)
                           && var5.open(var13, var16 + 1, var14)) {
                           var15 = var16;
                           break;
                        }
                     }

                     if (var15 == Integer.MIN_VALUE) {
                        var9.merge("no ground", 1, Integer::sum);
                     } else {
                        for (int var24 = 1; var24 <= Math.min(var8, var2.y() - var15 + 1); var24++) {
                           if (!l1.Y(var5, var13, var15, var14, var24, var1::Y, var2.y())) {
                              var9.merge("column taken", 1, Integer::sum);
                              if (var10 == null) {
                                 for (int var26 = var15; var26 <= var15 + var24 + 1 && var10 == null; var26++) {
                                    if (!var5.open(var13, var26, var14) || var1.Y(var13, var26, var14)) {
                                       var10 = var13
                                          + " "
                                          + var26
                                          + " "
                                          + var14
                                          + (var5.open(var13, var26, var14) ? " (a cell of the build)" : " (solid)")
                                          + " from ground "
                                          + var15;
                                    }
                                 }
                              }
                              break;
                           }

                           String var17 = uo.K(
                              this, mc.field_1724, var4, var6, var3, new class_243(var13 + 0.5, var15 + var24 + 1.62, var14 + 0.5), this.c() - 0.3
                           );
                           var9.merge(var17 == null ? "would work" : var17, 1, Integer::sum);
                        }
                     }
                  }
               }
            }

            StringBuilder var19 = new StringBuilder();
            if (uo.z(var3)) {
               class_2350 var20 = (class_2350)var3.method_11654(class_2741.field_12525);
               var19.append("; below ").append(mc.field_1687.method_8320(var6.method_10074()).method_26204().method_9518().getString());

               for (int var21 = 1; var21 <= 4; var21++) {
                  int var22 = var2.x() + var20.method_10148() * var21;
                  int var23 = var2.z() + var20.method_10165() * var21;
                  var19.append("; ")
                     .append(var21)
                     .append(" out: ")
                     .append(mc.field_1687.method_8320(new class_2338(var22, var2.y(), var23)).method_26204().method_9518().getString());

                  for (int var25 = var2.y(); var25 >= var2.y() - var8 - 3; var25--) {
                     if (var5.loaded(var22, var25, var23)
                        && var5.floor(var22, var25 - 1, var23)
                        && var5.open(var22, var25, var23)
                        && var5.open(var22, var25 + 1, var23)) {
                        for (int var27 = 0; var27 <= Math.min(var8, var2.y() - var25 + 1); var27++) {
                           String var18 = uo.K(
                              this, mc.field_1724, var4, var6, var3, new class_243(var22 + 0.5, var25 + var27 + 1.62, var23 + 0.5), this.c() - 0.3
                           );
                           var19.append(" h")
                              .append(var27)
                              .append('=')
                              .append(var18 == null ? "ok" : var18)
                              .append(l1.Y(var5, var22, var25, var23, Math.max(1, var27), var1::Y, var2.y()) ? "" : "(taken)");
                        }
                        break;
                     }
                  }
               }
            }

            if (var10 != null) {
               var19.append("; first column taken at ").append(var10);
            }

            q8.v(this.getName(), "Why no tower for {} at {}: {}{}", var3.method_26204().method_9518().getString(), var6.method_23854(), var9, var19);
         }
      }
   }

   private boolean c(s0.j var1, l1.i var2, int var3, int var4, int var5) {
      return (!this.LS.contains(var2.id()) || !var1.V().containsColumn(var3, var5)) && this.I(var1, var2, var3, var4, var5);
   }

   private boolean T(s0.j var1, l1.g var2) {
      if (this.kA == null && this.gN == null) {
         class_1792 var3 = this.F();
         if (var3 == null) {
            return false;
         }

         if (this.gv > this.Lc) {
            this.v.clear();
         }

         HashSet var4 = new HashSet<>(this.gQ);
         var4.addAll(this.v);
         l1.e var5 = l1.O(new cm(var0 -> -1, false), var2.stands(), var2.walkingFor(), this.m2.getValue().intValue(), var1::Y, var4);
         if (var5 == null) {
            return false;
         } else if (this.R(var3) < var5.height()) {
            this.ga = var5.height();
            return true;
         } else {
            this.mu = var5;
            this.LM = s0.a.u;
            this.qs = this.gR = mc.field_1724.field_6012;
            this.L1.i();
            this.Lk = List.of();
            this.gN = new l1.c(List.of(), var5.x(), var5.topY(), var5.z(), List.copyOf(var2.walkingFor()), Set.of());
            this.gq.add(this.gN);
            q8.G(
               this.getName(),
               "Tower of {} at {} {} {} leads across the build to spots the ground cannot reach",
               var5.height(),
               var5.x(),
               var5.ground(),
               var5.z()
            );
            return true;
         }
      } else {
         return false;
      }
   }

   private List<l1.i> E(s0.j var1, int var2) {
      ArrayList var3 = new ArrayList();
      class_2339 var4 = new class_2339();
      int var5 = Math.min(this.V + var2, var1.m.length - 1);

      for (int var6 = var1.m[this.V]; var6 < var1.m[var5]; var6++) {
         int var7 = var1.q[var6];
         if (var1.q(var7) != null
            && var1.t[var1.K[var7]] == null
            && !var1.h(var7)
            && !this.Ll.contains(var7)
            && !this.md.contains(var7)
            && this.B(var1, var7, var4) == uo.t.L) {
            var3.add(new l1.i(var4.method_10263(), var4.method_10264(), var4.method_10260(), var7));
         }
      }

      return var3;
   }

   private boolean U(s0.j var1, cm var2, class_2338 var3, double var4) {
      ArrayList var6 = new ArrayList();
      int var7 = var1.X + this.V;

      for (l1.i var9 : this.E(var1, 65)) {
         if (var9.y() > var7) {
            var6.add(var9);
         }
      }

      for (int var12 = var7 + 1; var12 <= var7 + 64; var12++) {
         int var13 = var12;
         List var10 = this.k(var1, var6.stream().filter(var1x -> var1x.y() == var13).toList());
         if (!var10.isEmpty()) {
            l1.g var11 = l1.O(
               var2,
               var10,
               this.LS,
               var3.method_10263(),
               var3.method_10264(),
               var3.method_10260(),
               var4,
               var1.V(),
               var2x -> this.e(var1, var2x),
               (var2x, var3x, var4x, var5) -> this.X(var1, var2x) != null,
               (var2x, var3x, var4x, var5) -> this.I(var1, var2x, var3x, var4x, var5),
               120,
               false,
               this.qe.t(),
               this.LQ,
               false
            );
            if (var11.place() != null) {
               this.LA.put(var11.place().id(), s0.l.s);
               this.a(var1, var11.place());
               return true;
            }
         }
      }

      return false;
   }

   private boolean J(s0.j var1) {
      class_1792 var2 = this.F();
      if (var2 != null && this.ke.getValue()) {
         class_2248 var3 = Y(var2);
         class_2338 var4 = this.H();
         ArrayList var5 = new ArrayList();

         class_2338 var6;
         for (var6 = var4.method_10074();
            var5.size() < 16
               && mc.field_1687.method_8320(var6).method_27852(var3)
               && !var1.Y(var6.method_10263(), var6.method_10264(), var6.method_10260())
               && this.U(var6) >= 2;
            var6 = var6.method_10074()
         ) {
            var5.add(var6);
         }

         if (var5.size() >= 3 && !this.E(var6)) {
            Collections.reverse(var5);
            this.gh.clear();
            this.gh.addAll(var5);
            this.mu = new l1.e(var4.method_10263(), var6.method_10264() + 1, var4.method_10260(), var5.size(), List.of());
            this.LM = s0.a.r;
            this.i = this.gv;
            this.g_ = this.gv;
            this.N("you are standing on a tower left from an earlier run; it is used and then dug away.");
            q8.G(this.getName(), "Took over the leftover tower of {} at {}", var5.size(), ((class_2338)var5.getFirst()).method_23854());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int U(class_2338 var1) {
      int var2 = 0;

      for (class_2350 var4 : class_2353.field_11062) {
         if (this.E(var1.method_10093(var4))) {
            var2++;
         }
      }

      return var2;
   }

   private boolean N() {
      class_2338 var1 = this.H();
      return !l1.p(
         new cm(var0 -> -1, false),
         var1.method_10263(),
         var1.method_10264(),
         var1.method_10260(),
         new fx.o(
            var1.method_10263() - 3,
            var1.method_10264() - 3,
            var1.method_10260() - 3,
            var1.method_10263() + 3,
            var1.method_10264() + 3,
            var1.method_10260() + 3
         )
      );
   }

   private boolean z(s0.j var1, List<l1.i> var2) {
      class_1792 var3 = this.F();
      if (var3 != null && this.mu != null && !var2.isEmpty()) {
         int var4 = l1.m(
            new cm(var0 -> -1, false),
            var2,
            this.mu,
            this.m2.getValue().intValue(),
            this.R(var3),
            var1::Y,
            (var2x, var3x, var4x, var5) -> this.c(var1, var2x, var3x, var4x, var5)
         );
         if (var4 == 0) {
            return false;
         }

         q8.G(this.getName(), "Raising the pillar at {} {} from {} to {} for the next blocks", this.mu.x(), this.mu.z(), this.mu.height(), var4);
         this.mu = new l1.e(this.mu.x(), this.mu.ground(), this.mu.z(), var4, this.mu.unlocks());
         this.LM = s0.a.X;
         this.gR = mc.field_1724.field_6012;
         return true;
      } else {
         return false;
      }
   }

   private boolean b(s0.j var1, List<l1.i> var2) {
      class_1792 var3 = this.F();
      if (var3 != null && this.kj.size() < 24 && this.R(var3) != 0) {
         if (this.gv != this.mK) {
            this.mK = this.gv;
            this.kG = 0;
         }

         if (this.kG >= 3) {
            return false;
         }

         this.kG++;
         class_2338 var4 = this.H();
         ArrayList var5 = new ArrayList();

         for (l1.i var7 : this.E(var1, 2)) {
            if (this.e(var1, var7)) {
               var5.add(var7);
            }
         }

         HashSet var10 = new HashSet();

         for (class_2338 var8 : this.kj) {
            var10.add(com.larpclient.g8.M(var8.method_10263(), 0, var8.method_10260()));
         }

         if (this.mu != null) {
            var10.add(com.larpclient.g8.M(this.mu.x(), 0, this.mu.z()));
         }

         int[] var12 = l1.h(
            new cm(var0 -> -1, false),
            var5,
            var4.method_10263(),
            var4.method_10264(),
            var4.method_10260(),
            3,
            this.c(),
            var1::Y,
            (var2x, var3x, var4x, var5x) -> this.c(var1, var2x, var3x, var4x, var5x),
            var10
         );
         if (var12 == null) {
            return false;
         }

         for (class_2350 var9 : class_2353.field_11062) {
            if (var9.method_10148() == var12[0] && var9.method_10165() == var12[1]) {
               this.gG = var9;
            }
         }

         this.H = var4.method_10074().method_10093(this.gG);
         this.LM = s0.a.m;
         this.ge = mc.field_1724.field_6012;
         q8.G(
            this.getName(),
            "Stepping {} along the top instead of digging down, {} blocks still to place up here ({} walkway blocks so far)",
            this.gG.method_10151(),
            var5.size(),
            this.kj.size()
         );
         return true;
      } else {
         return false;
      }
   }

   private void A(String var1) {
      q8.G(this.getName(), "Walkway stopped: {}", var1);
      fk.N(this);
      this.H = null;
      this.LM = s0.a.r;
      this.i(var1);
   }

   private void H() {
      class_1792 var1 = this.F();
      if (var1 == null || mc.field_1724.field_6012 - this.ge > 120) {
         this.A("a walkway block did not go down");
      } else if (!fk.v(this.H)) {
         this.kj.add(this.H);
         this.LM = s0.a.Q;
         this.ge = mc.field_1724.field_6012;
      } else {
         this.Lh = (boolean)1;
         if (mc.field_1724.method_24828()) {
            int var2 = this.O(var1);
            if (var2 == -2) {
               this.A("no " + R(var1) + " left");
            } else if (var2 >= 0 && this.m(var2)) {
               class_2338 var3 = this.H.method_10093(this.gG.method_10153());
               class_243 var4 = class_243.method_24953(var3);
               double var5 = this.gG.method_10148();
               double var7 = this.gG.method_10165();
               double var9 = (mc.field_1724.method_23317() - var4.field_1352) * var5 + (mc.field_1724.method_23321() - var4.field_1350) * var7;
               double var11 = var7 != 0.0 ? var4.field_1352 - mc.field_1724.method_23317() : 0.0;
               double var13 = var5 != 0.0 ? var4.field_1350 - mc.field_1724.method_23321() : 0.0;
               this.kv = var9 < 0.66 ? new class_243(var5 + var11, 0.0, var7 + var13) : new class_243(var11, 0.0, var13);
               class_243 var15 = mc.field_1724.method_33571();
               class_243 var16 = var4.method_1031(var5 * 0.5, 0.3, var7 * 0.5);
               float var17 = this.gG.method_10153().method_10144();
               float var18 = (float)Math.toDegrees(Math.atan2(var15.field_1351 - var16.field_1351, Math.max(var9 - 0.5, 0.08)));
               this.aimPlacement(var17, var18);
               this.m("Laying a walkway block");
               if (!(var9 < 0.54)) {
                  double var19 = this.c();
                  class_3965 var21 = fk.E(this, var15, var3, class_243.method_1030(var18, var17), var19);
                  if (var21 != null && var21.method_17780() == this.gG) {
                     class_2338 var22 = this.H;
                     fk.j(
                        this,
                        var22,
                        var21,
                        var1,
                        var19,
                        true,
                        () -> fk.v(var22) && !this.I(),
                        () -> fk.N(this, var3, var19) && uo.u(mc.field_1724, var21) && uo.t(mc.field_1724, var3),
                        var1x -> this.Lt = mc.field_1724.field_6012
                     );
                  }
               }
            } else {
               this.m("Taking out " + R(var1) + " for the walkway");
            }
         }
      }
   }

   private void E() {
      if (mc.field_1724.field_6012 - this.ge > 120) {
         this.A("could not step onto the walkway");
      } else {
         this.Lh = (boolean)1;
         this.m("Stepping along the walkway");
         class_243 var1 = class_243.method_24955(this.H.method_10084()).method_1020(mc.field_1724.method_73189()).method_18805(1.0, 0.0, 1.0);
         if (var1.method_1033() < 0.25) {
            this.H = null;
            this.LM = s0.a.r;
            this.i = this.gv;
            this.qe.i();
         } else {
            this.kv = var1;
         }
      }
   }

   private void g() {
      s0.j var1 = this.l;
      if (var1 == null) {
         this.kj.clear();
         this.LM = s0.a.K;
      } else if (this.kj.isEmpty()) {
         bv.i(this);
         this.LM = s0.a.K;
         this.gR = mc.field_1724.field_6012;
      } else if (mc.field_1724.field_6012 - this.gR <= 80) {
         this.Lh = (boolean)1;
         class_1792 var2 = this.F();
         class_2248 var3 = Y(var2 == null ? class_1802.field_8831 : var2);
         class_2338 var4 = this.kj.getLast();
         class_2338 var5 = this.kj.size() >= 2 ? this.kj.get(this.kj.size() - 2) : (this.gh.isEmpty() ? null : this.gh.getLast());
         if (var5 == null) {
            this.kj.clear();
         } else {
            class_243 var6 = class_243.method_24955(var5.method_10084()).method_1020(mc.field_1724.method_73189()).method_18805(1.0, 0.0, 1.0);
            if (mc.field_1724.method_24828() && mc.field_1724.method_23318() < var5.method_10264() + 0.5) {
               bv.i(this);
               q8.G(
                  this.getName(),
                  "Fell off the walkway at {} with {} blocks left; sweeping them and the tower up instead",
                  this.H().method_23854(),
                  this.kj.size()
               );
               this.L(this.kj);
               this.L(this.gh);
               this.mu = null;
               this.LM = null;
            } else if (var6.method_1033() > 0.25) {
               bv.i(this);
               this.m("Walking back along the walkway");
               this.kv = var6;
            } else if (mc.field_1687.method_8320(var4).method_27852(var3) && !var1.Y(var4.method_10263(), var4.method_10264(), var4.method_10260())) {
               if (!this.H(var4)) {
                  this.m("Cannot see the walkway block at " + var4.method_23854());
               }
            } else {
               this.kj.removeLast();
               this.gR = mc.field_1724.field_6012;
            }
         }
      } else {
         bv.i(this);
         q8.G(
            this.getName(),
            "Walkway could not be taken up from the top with {} blocks left at {}; sweeping them and the tower up instead",
            this.kj.size(),
            this.kj.getFirst().method_23854()
         );
         q8.G(
            this.getName(),
            "Walkway stall: player {}, next block {}, on ground {}, speed {}, rotation owner other than the bot {}, keys tick {}, screen {}",
            mc.field_1724.method_73189(),
            this.kj.getLast().method_23854(),
            mc.field_1724.method_24828(),
            mc.field_1724.method_18798().method_37267(),
            t9.I() && !t9.k(t9.v.e),
            this.z == mc.field_1724.field_6012,
            mc.field_1755 != null
         );
         this.L(this.kj);
         this.L(this.gh);
         this.mu = null;
         this.LM = null;
      }
   }

   private boolean b(s0.j var1) {
      class_1792 var2 = this.F();
      if (var2 == null || this.R(var2) >= this.ga) {
         this.ga = 0;
         return false;
      }

      if (this.kd.getValue() && this.P(var1)) {
         return true;
      }

      if (this.Lb.getValue() && !this.k0 && !this.kf.contains(var2) && this.I(var1)) {
         return true;
      }

      this.x("out of " + R(var2) + " for temporary blocks; carry at least " + this.ga + (this.Lb.getValue() ? "" : " or turn on Buy Missing Blocks"));
      return true;
   }

   private boolean u(s0.j var1, l1.i var2) {
      class_2680 var3 = var1.q(var2.id());
      class_2338 var4 = new class_2338(var2.x(), var2.y(), var2.z());
      return var3.method_26204() instanceof class_2349
         ? true
         : !var3.method_31709() && !uo.w(var3) && !uo.d(var3) && !uo.v(var3) && var3.method_26234(mc.field_1687, var4);
   }

   private boolean h(s0.j var1, List<l1.i> var2) {
      class_1792 var3 = this.F();
      if (var3 != null && !this.o(((l1.i)var2.getFirst()).y())) {
         class_2338 var4 = this.H();
         l1.f var5 = l1.d(
            new cm(var0 -> -1, false),
            var2,
            var1::Y,
            (var0, var1x, var2x) -> uo.I(new class_2338(var0, var1x, var2x)),
            var2x -> this.u(var1, var2x),
            this.gL,
            var4.method_10263(),
            var4.method_10264(),
            var4.method_10260(),
            var2x -> this.A(var1, var2x)
         );
         if (var5 == null) {
            return false;
         } else if (this.R(var3) < var5.height()) {
            this.ga = var5.height();
            return true;
         } else {
            this.gY = var5;
            this.kA = s0.p.i;
            this.gZ = this.m_ = mc.field_1724.field_6012;
            this.L7.clear();
            this.L1.i();
            this.Lk = List.of();
            q8.G(
               this.getName(),
               "Nothing holds layer Y {} up: {} temporary {} at {} {} {} to start it",
               var5.seed().y(),
               var5.height(),
               var5.height() == 1 ? "block" : "blocks",
               var5.x(),
               var5.ground(),
               var5.z()
            );
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean h(List<l1.i> var1) {
      int var2 = mc.field_1724.field_6012;
      if (this.gv != this.gc || var2 - this.kR > 5) {
         this.gc = this.gv;
         this.Lj = var2;
      }

      this.kR = var2;
      if (var2 - this.Lj <= 1200) {
         return false;
      }

      this.Lj = var2;
      q8.G(this.getName(), "No block went down for {} ticks ({}); setting aside what it was trying", 1200, this.LM);
      if (var1 != null) {
         for (l1.i var4 : var1) {
            this.u(var4.id(), "no progress");
         }
      }

      this.u();
      this.L1.i();
      if (this.mu != null && this.LM != null && this.LM != s0.a.K && this.LM != s0.a.h && this.LM != s0.a.u) {
         this.i("no block went down for a minute");
      }

      return true;
   }

   private boolean j(s0.j var1) {
      if (this.B(var1, this.B, new class_2339()) == uo.t.m && mc.field_1724.field_6012 - this.F <= 400) {
         this.m("Breaking a block that faces the wrong way");
         return this.H(this.gn);
      } else {
         bv.i(this);
         this.gn = null;
         return false;
      }
   }

   private boolean o(int var1) {
      if (this.gv > this.L9) {
         this.qY.clear();
      }

      return this.qY.getOrDefault(var1, 0) >= 2;
   }

   private void S(int var1) {
      if (this.gv > this.L9) {
         this.qY.clear();
      }

      this.L9 = this.gv;
      if (this.qY.merge(var1, 1, Integer::sum) == 2) {
         q8.G(this.getName(), "Two temporary supports in a row gave nothing at Y {}; no more there until a block of the build goes down", var1);
      }
   }

   private boolean A(s0.j var1, l1.f var2) {
      class_2338 var3 = this.H();
      l1.i var4 = var2.seed();
      if (this.R(var2, var3.method_10263(), var3.method_10264(), var3.method_10260(), this.LM == s0.a.r)) {
         return true;
      }

      if (this.LM == s0.a.r) {
         return false;
      }

      cm var5 = new cm(var0 -> -1, false);
      Set var6 = l1.E(var5, var4, this.c(), var1.V(), false, (var2x, var3x, var4x, var5x) -> this.R(var2, var3x, var4x, var5x, false), Set.of());
      if (var6.isEmpty()) {
         return false;
      }

      g8.l var7 = new g8.l(
         Math.min(var1.S, var3.method_10263()) - 10,
         Math.min(var1.X, var3.method_10264()) - 6,
         Math.min(var1.N, var3.method_10260()) - 10,
         Math.max(var1.E, var3.method_10263()) + 10,
         Math.max(var1.D, var3.method_10264()) + 6,
         Math.max(var1.M, var3.method_10260()) + 10
      );
      return com.larpclient.g8.O(
            var5,
            var3.method_10263(),
            var3.method_10264(),
            var3.method_10260(),
            g8.i.S(var6, var4.x(), var4.y() - 1, var4.z(), this.c()),
            var7,
            g8.b.walking(16000)
         )
         .found();
   }

   private boolean R(l1.f var1, int var2, int var3, int var4, boolean var5) {
      class_1792 var6 = this.F();
      if (var6 != null && l1.X(var1, var2, var3, var4)) {
         class_2680 var7 = this.l == null ? null : this.l.q(var1.seed().id());
         if (var7 != null && var7.method_26204() instanceof class_2349) {
            class_2350 var8 = (class_2350)var7.method_11654(class_2741.field_12481);
            if (var1.x() - var1.seed().x() != var8.method_10148() || var1.z() - var1.seed().z() != var8.method_10165()) {
               return false;
            }
         }

         double var14 = this.c() - 0.3;
         if (l1.j(var2, var3, var4, var1.seed()) > var14 * var14) {
            return false;
         }

         if (var5 && Math.abs(var1.x() - var2) + Math.abs(var1.z() - var4) == 1) {
            return false;
         }

         if (var5 && var1.ground() < var3) {
            double var10 = Math.hypot(var1.x() - var2, var1.z() - var4);
            double var12 = var3 + 1.62 - var1.ground();
            if (var10 * 1.62 / var12 < 0.6) {
               return false;
            }
         }

         return this.r(var6, new class_2338(var1.x(), var1.ground(), var1.z()), new class_243(var2 + 0.5, var3 + 1.62, var4 + 0.5), var14, true) != null;
      } else {
         return false;
      }
   }

   private boolean o(s0.j var1, List<l1.i> var2) {
      class_1792 var3 = this.F();
      if (var3 != null && !this.o(((l1.i)var2.getFirst()).y())) {
         class_2338 var4 = this.H();
         if (this.gv > this.Lc) {
            this.v.clear();
         }

         HashSet var5 = new HashSet<>(this.gQ);
         var5.addAll(this.v);
         l1.e var6 = l1.I(
            new cm(var0 -> -1, false),
            var2,
            var4.method_10263(),
            var4.method_10264(),
            var4.method_10260(),
            this.c(),
            this.m2.getValue().intValue(),
            var1::Y,
            (var0, var1x, var2x) -> uo.I(new class_2338(var0, var1x, var2x)),
            var2x -> this.u(var1, var2x),
            (var1x, var2x, var3x, var4x) -> this.R(var1x, var2x, var3x, var4x, true),
            this.gL,
            var5,
            600
         );
         if (var6 == null) {
            return false;
         } else if (this.R(var3) < var6.height() + 3) {
            this.ga = var6.height() + 3;
            return true;
         } else {
            this.mu = var6;
            this.k8 = (boolean)1;
            this.LD = ((l1.i)var2.getFirst()).y();
            this.LM = s0.a.u;
            this.qs = this.gR = mc.field_1724.field_6012;
            this.L1.i();
            this.Lk = List.of();
            q8.G(
               this.getName(),
               "Layer Y {} is too high to start from the floor: tower of {} at {} {} {} to stand on",
               ((l1.i)var2.getFirst()).y(),
               var6.height(),
               var6.x(),
               var6.ground(),
               var6.z()
            );
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean p(s0.j var1) {
      switch (this.kA) {
         case i:
            this.M(var1);
            break;
         case U:
            this.i(var1);
            break;
         case Z:
            l1.i var2 = this.gY.seed();
            int var3 = this.B(var1, var2.id(), new class_2339()) != uo.t.L ? 1 : 0;
            if (var3 == 0 && !this.mA.containsKey(var2.id()) && !this.Ll.contains(var2.id()) && mc.field_1724.field_6012 - this.gZ <= 400) {
               return false;
            }

            if (var3 == 0) {
               this.gL.add(com.larpclient.g8.M(var2.x(), var2.y(), var2.z()));
               this.S(var2.y());
            }

            this.kA = s0.p.s;
            this.m_ = mc.field_1724.field_6012;
            this.B(var1);
            break;
         case s:
            this.B(var1);
      }

      return true;
   }

   private void Y(String var1) {
      l1.i var2 = this.gY.seed();
      q8.G(this.getName(), "Temporary support at {} {} {} left out: {}", this.gY.x(), this.gY.ground(), this.gY.z(), var1);
      this.gL.add(com.larpclient.g8.M(var2.x(), var2.y(), var2.z()));
      this.S(var2.y());
      this.L1.i();
      fk.N(this);
      if (this.L7.isEmpty()) {
         this.gY = null;
         this.kA = null;
      } else {
         this.kA = s0.p.s;
         this.m_ = mc.field_1724.field_6012;
      }
   }

   private class_2338 Y() {
      return new class_2338(this.gY.x(), this.gY.ground() + this.L7.size(), this.gY.z());
   }

   private class_3965 r(class_1792 var1, class_2338 var2, class_243 var3, double var4, boolean var6) {
      class_1799 var7 = this.S(var1);
      return var7.method_7960() ? null : uo.X(this, mc.field_1724, var7, var2, Y(var1).method_9564(), var3, var4, true, var6);
   }

   private boolean Z() {
      return mc.field_1724
         .method_5829()
         .method_994(new class_238(this.gY.x(), this.gY.ground(), this.gY.z(), this.gY.x() + 1, this.gY.top() + 1, this.gY.z() + 1));
   }

   private void M(s0.j var1) {
      if (mc.field_1724.field_6012 - this.gZ > 400) {
         this.Y("the walk took too long");
      } else {
         class_1792 var2 = this.F();
         if (var2 == null) {
            this.Y("the block is used by the build");
         } else {
            class_2338 var3 = this.Y();
            if (this.L1.G()) {
               this.y();
            } else if (!this.Z() && this.r(var2, var3, mc.field_1724.method_33571(), this.c(), false) != null) {
               this.kA = s0.p.U;
               this.m_ = mc.field_1724.field_6012;
            } else if (this.LM == s0.a.r) {
               this.Y("no click from the tower's top");
            } else {
               class_2338 var4 = this.H();
               l1.i var5 = this.gY.seed();
               double var6 = this.c() - 0.3;
               Set var8 = l1.E(
                  new cm(var0 -> -1, false),
                  new l1.i(var3.method_10263(), var3.method_10264(), var3.method_10260(), -1),
                  this.c(),
                  var1.V(),
                  false,
                  (var6x, var7, var8x, var9) -> (var7 != this.gY.x() || var9 != this.gY.z())
                     && l1.j(var7, var8x, var9, var5) <= var6 * var6
                     && this.r(var2, var3, new class_243(var7 + 0.5, var8x + 1.62, var9 + 0.5), var6, true) != null,
                  Set.of()
               );
               var8.remove(com.larpclient.g8.M(var4.method_10263(), var4.method_10264(), var4.method_10260()));
               if (var8.isEmpty()) {
                  this.Y("no spot to place it from");
               } else if (!this.i(var1, var8, var3)) {
                  this.Y("no way to walk to it");
               } else {
                  this.y();
               }
            }
         }
      }
   }

   private boolean i(s0.j var1, Set<Long> var2, class_2338 var3) {
      class_2338 var4 = this.H();
      g8.l var5 = new g8.l(
         Math.min(var1.S, var4.method_10263()) - 10,
         Math.min(var1.X, var4.method_10264()) - 6,
         Math.min(var1.N, var4.method_10260()) - 10,
         Math.max(var1.E, var4.method_10263()) + 10,
         Math.max(var1.D, var4.method_10264()) + 6,
         Math.max(var1.M, var4.method_10260()) + 10
      );
      g8.t var6 = com.larpclient.g8.O(
         new cm(var0 -> -1, false),
         var4.method_10263(),
         var4.method_10264(),
         var4.method_10260(),
         g8.i.S(var2, var3.method_10263(), var3.method_10264() - 1, var3.method_10260(), this.c()),
         var5,
         g8.b.walking(16000)
      );
      if (var6.found() && !var6.steps().isEmpty()) {
         this.L1.X(var6.steps());
         this.L0 = var3;
         g8.p var7 = var6.steps().getLast();
         this.kQ = new class_2338(var7.x(), var7.y(), var7.z());
         return true;
      } else {
         return false;
      }
   }

   private void i(s0.j var1) {
      class_1792 var2 = this.F();
      if (var2 == null) {
         this.Y("the block is used by the build");
      } else {
         class_2248 var3 = Y(var2);

         while (!this.L7.isEmpty() && !mc.field_1687.method_8320(this.L7.getLast()).method_27852(var3)) {
            this.L7.removeLast();
         }

         if (this.L7.size() >= this.gY.height()) {
            this.kA = s0.p.Z;
            this.gZ = mc.field_1724.field_6012;
            this.qe.i();
         } else if (mc.field_1724.field_6012 - this.m_ > 40) {
            this.Y("it did not go down");
         } else {
            int var4 = this.O(var2);
            if (var4 == -2) {
               this.Y("no " + R(var2) + " left");
            } else if (var4 >= 0 && this.m(var4)) {
               this.v();
               class_2338 var5 = this.Y();
               class_3965 var6 = this.Z() ? null : this.r(var2, var5, mc.field_1724.method_33571(), this.c(), false);
               if (var6 == null) {
                  this.kA = s0.p.i;
               } else {
                  class_2338 var7 = var6.method_17777();
                  this.q(var7);
                  double var8 = this.c();
                  fk.j(
                     this,
                     var5,
                     var6,
                     var2,
                     var8,
                     true,
                     () -> fk.v(var5) && !this.I(),
                     () -> fk.N(this, var7, var8) && uo.u(mc.field_1724, var6) && uo.t(mc.field_1724, var7) && this.o(var7),
                     var2x -> {
                        this.Lt = mc.field_1724.field_6012;
                        if (var2x && !this.L7.contains(var5)) {
                           this.L7.add(var5);
                           this.m_ = mc.field_1724.field_6012;
                        }
                     }
                  );
                  this.m("Placing a temporary block at " + var5.method_23854());
               }
            } else {
               this.m("Taking out " + R(var2) + " for a temporary block");
            }
         }
      }
   }

   private void B(s0.j var1) {
      class_1792 var2 = this.F();

      for (class_2248 var3 = Y(var2 == null ? class_1802.field_8831 : var2);
         !this.L7.isEmpty() && !mc.field_1687.method_8320(this.L7.getLast()).method_27852(var3);
         this.m_ = mc.field_1724.field_6012
      ) {
         this.L7.removeLast();
      }

      if (this.L7.isEmpty()) {
         bv.i(this);
         this.gY = null;
         this.kA = null;
         this.qe.i();
         this.m("Temporary block removed");
      } else if (this.L1.G()) {
         this.y();
      } else if (mc.field_1724.field_6012 - this.m_ > 200) {
         bv.i(this);
         q8.G(this.getName(), "Could not dig the temporary block at {} right away; sweeping it up instead", this.L7.getFirst().method_23854());
         this.L(this.L7);
         this.gY = null;
         this.kA = null;
      } else {
         this.v();
         class_2338 var4 = this.L7.getLast();
         if (!this.H(var4)) {
            if (this.LM != s0.a.r) {
               class_2338 var5 = this.H();
               Set var6 = l1.E(
                  new cm(var0 -> -1, false),
                  new l1.i(var4.method_10263(), var4.method_10264(), var4.method_10260(), -1),
                  this.c(),
                  var1.V(),
                  false,
                  (var1x, var2x, var3x, var4x) -> var2x != var4.method_10263() || var4x != var4.method_10260(),
                  Set.of()
               );
               var6.remove(com.larpclient.g8.M(var5.method_10263(), var5.method_10264(), var5.method_10260()));
               if (!var6.isEmpty() && this.i(var1, var6, var4)) {
                  this.y();
               }
            }
         }
      }
   }

   private String H(s0.j var1, l1.i var2) {
      class_2680 var3 = var1.q(var2.id());
      class_1799 var4 = this.S(uo.w(var3));
      if (var4.method_7960()) {
         return "none carried";
      }

      class_2338 var5 = new class_2338(var2.x(), var2.y(), var2.z());
      if (mc.field_1724.method_5829().method_994(new class_238(var5))) {
         return "the body is in the cell";
      }

      String var6 = uo.K(this, mc.field_1724, var4, var5, var3, mc.field_1724.method_33571(), this.c());
      return var6 == null ? "steady look missed" : var6;
   }

   private String Y() {
      s0.j var1 = this.l;
      if (var1 != null && this.mu != null) {
         StringBuilder var2 = new StringBuilder();
         class_2339 var3 = new class_2339();

         for (l1.i var5 : this.mu.unlocks()) {
            if (this.B(var1, var5.id(), var3) == uo.t.L) {
               String var6 = !this.mA.containsKey(var5.id()) && !this.Ll.contains(var5.id())
                  ? (this.X(var1, var5) == null ? "no click from here: " + this.H(var1, var5) : "click was possible")
                  : "set aside";
               var2.append(var2.isEmpty() ? "; planned but not placed: " : ", ")
                  .append(var1.q(var5.id()).method_26204().method_9518().getString())
                  .append(" at ")
                  .append(var5.x())
                  .append(' ')
                  .append(var5.y())
                  .append(' ')
                  .append(var5.z())
                  .append(" (")
                  .append(var6)
                  .append(')');
            }
         }

         return var2.toString();
      } else {
         return "";
      }
   }

   private void L(List<class_2338> var1) {
      class_1792 var2 = this.F();
      class_2248 var3 = Y(var2 == null ? class_1802.field_8831 : var2);

      for (class_2338 var5 : var1) {
         if (mc.field_1687.method_8320(var5).method_27852(var3)
            && (this.l == null || !this.l.Y(var5.method_10263(), var5.method_10264(), var5.method_10260()))
            && !this.kY.contains(var5)) {
            this.kY.add(var5);
         }
      }

      var1.clear();
   }

   private void U(s0.j var1) {
      class_1792 var2 = this.F();
      class_2248 var3 = Y(var2 == null ? class_1802.field_8831 : var2);
      this.kY.removeIf(var1x -> !mc.field_1687.method_8320(var1x).method_27852(var3));
      if (this.kY.isEmpty()) {
         bv.i(this);
         this.L = null;
      } else if (this.L1.G()) {
         this.y();
      } else {
         if (this.L == null || !this.kY.contains(this.L)) {
            this.L = this.kY.stream().max(Comparator.comparingInt(class_2382::method_10264)).orElseThrow();
            this.mW = mc.field_1724.field_6012;
         }

         if (mc.field_1724.field_6012 - this.mW > 160) {
            bv.i(this);
            this.N("could not reach the temporary block at " + this.L.method_23854() + "; break it by hand.");
            q8.G(this.getName(), "Sweep gave up on the temporary block at {}", this.L.method_23854());
            bv.M(this.getName(), this.L);
            this.kY.remove(this.L);
            this.L = null;
         } else {
            this.m("Removing the temporary blocks left standing");
            if (!this.H(this.L)) {
               class_2338 var4 = this.H();
               class_2338 var5 = this.L;
               Set var6 = l1.E(
                  new cm(var0 -> -1, false),
                  new l1.i(var5.method_10263(), var5.method_10264(), var5.method_10260(), -1),
                  this.c(),
                  var1.V(),
                  false,
                  (var2x, var3x, var4x, var5x) -> (var3x != var5.method_10263() || var5x != var5.method_10260()) && this.U(var3x, var4x, var5x, var5),
                  Set.of()
               );
               var6.remove(com.larpclient.g8.M(var4.method_10263(), var4.method_10264(), var4.method_10260()));
               if (!var6.isEmpty() && this.i(var1, var6, var5) || !this.Q(var1, var5)) {
                  if (!var6.isEmpty() && (!this.L1.M().isEmpty() || this.i(var1, var6, var5))) {
                     this.y();
                  } else {
                     this.mW = mc.field_1724.field_6012 - 160 - 1;
                  }
               }
            }
         }
      }
   }

   private boolean Q(s0.j var1, class_2338 var2) {
      class_1792 var3 = this.F();
      if (var3 != null && this.ke.getValue() && this.R(var3) != 0 && !this.ks.contains(var2)) {
         this.ks.add(var2);
         class_2338 var4 = this.H();
         l1.i var5 = new l1.i(var2.method_10263(), var2.method_10264(), var2.method_10260(), -1);
         l1.e var6 = l1.k(
            new cm(var0 -> -1, false),
            List.of(var5),
            var4.method_10263(),
            var4.method_10264(),
            var4.method_10260(),
            this.c(),
            this.m2.getValue().intValue(),
            var1::Y,
            (var2x, var3x, var4x, var5x) -> (var3x != var2.method_10263() || var5x != var2.method_10260()) && this.U(var3x, var4x, var5x, var2),
            this.gQ,
            600
         );
         if (var6 != null && this.R(var3) >= var6.height()) {
            this.L1.i();
            this.mu = new l1.e(var6.x(), var6.ground(), var6.z(), var6.height(), List.of());
            this.LM = s0.a.u;
            this.qs = this.gR = mc.field_1724.field_6012;
            this.mh = (boolean)1;
            q8.G(
               this.getName(),
               "Tower of {} at {} {} {} to dig the temporary block at {} left out of reach",
               var6.height(),
               var6.x(),
               var6.ground(),
               var6.z(),
               var2.method_23854()
            );
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void r() {
      class_1792 var1 = this.F();
      class_2248 var2 = Y(var1 == null ? class_1802.field_8831 : var1);
      this.kY.removeIf(var1x -> !mc.field_1687.method_8320(var1x).method_27852(var2));
      class_2338 var3 = this.H();
      ArrayList var4 = new ArrayList<>(this.kY);
      var4.sort(Comparator.<class_2338>comparingInt(var0 -> var0.method_10264()).reversed());

      for (class_2338 var6 : var4) {
         if ((var6.method_10263() != var3.method_10263() || var6.method_10260() != var3.method_10260())
            && this.U(var3.method_10263(), var3.method_10264(), var3.method_10260(), var6)) {
            if (!var6.equals(this.L)) {
               this.L = var6;
               this.mW = mc.field_1724.field_6012;
            }

            if (mc.field_1724.field_6012 - this.mW > 160) {
               bv.i(this);
               q8.G(this.getName(), "Sweep gave up on the temporary block at {} from a tower", var6.method_23854());
               bv.M(this.getName(), var6);
               this.N("could not break the temporary block at " + var6.method_23854() + "; break it by hand.");
               this.kY.remove(var6);
               this.L = null;
               return;
            }

            this.m("Removing the temporary blocks left standing");
            if (this.H(var6)) {
               return;
            }
         }
      }

      this.mh = (boolean)0;
      this.i("the temporary blocks in reach are gone");
   }

   private boolean U(int var1, int var2, int var3, class_2338 var4) {
      class_243 var5 = new class_243(var1 + 0.5, var2 + 1.62, var3 + 0.5);
      double var6 = this.c() - 0.3;

      for (class_2350 var11 : class_2350.values()) {
         class_243 var12 = class_243.method_24953(var4).method_1031(var11.method_10148() * 0.5, var11.method_10164() * 0.5, var11.method_10165() * 0.5);
         class_243 var13 = new class_243(var11.method_10148(), var11.method_10164(), var11.method_10165());
         if (!(var5.method_1020(var12).method_1026(var13) <= 0.02)
            && !(var5.method_1025(var12) > var6 * var6)
            && fk.E(this, var5, var4, var12.method_1020(var5), var6) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean H(class_2338 var1) {
      return bv.q(this, var1, this.c(), true, 12.0F, bv::c, () -> !this.I());
   }

   private void u(String var1) {
      q8.v(this.getName(), "Pillar at {} {}: {}", this.mu.x(), this.mu.z(), var1);
      this.mh = (boolean)0;
      if (this.gN != null && this.LN.isEmpty()) {
         if (this.gN.cells().isEmpty()) {
            this.gq.remove(this.gN);
         }

         this.gN = null;
      }

      this.gQ.add(com.larpclient.g8.M(this.mu.x(), 0, this.mu.z()));
      this.L1.i();
      fk.N(this);
      if (this.gh.isEmpty()) {
         if (this.k8) {
            this.S(this.LD);
            this.k8 = (boolean)0;
         }

         this.mu = null;
         this.LM = null;
      } else {
         this.i(var1);
      }
   }

   private void P(s0.j var1) {
      class_2338 var2 = this.H();
      class_2338 var3 = new class_2338(this.mu.x(), this.mu.ground(), this.mu.z());
      if (mc.field_1724.field_6012 - this.qs > 600) {
         this.u("the walk took too long");
      } else if (var2.equals(var3) && !this.L1.G()) {
         this.LM = s0.a.X;
         this.gR = mc.field_1724.field_6012;
         this.Lf++;
         this.g_ = this.gv;
         q8.G(
            this.getName(),
            "Pillar of {} at {} {} {} for {} blocks now, in reach of more above",
            this.mu.height(),
            this.mu.x(),
            this.mu.ground(),
            this.mu.z(),
            this.mu.unlocks().size()
         );
         this.m("Building a pillar of " + this.mu.height());
      } else {
         if (!this.L1.G()) {
            g8.l var4 = new g8.l(
               Math.min(var1.S, var2.method_10263()) - 10,
               Math.min(var1.X, var2.method_10264()) - 6,
               Math.min(var1.N, var2.method_10260()) - 10,
               Math.max(var1.E, var2.method_10263()) + 10,
               Math.max(var1.D, var2.method_10264()) + 6,
               Math.max(var1.M, var2.method_10260()) + 10
            );
            g8.t var5 = com.larpclient.g8.O(
               new cm(var0 -> -1, false),
               var2.method_10263(),
               var2.method_10264(),
               var2.method_10260(),
               g8.i.S(
                  Set.of(com.larpclient.g8.M(var3.method_10263(), var3.method_10264(), var3.method_10260())),
                  var3.method_10263(),
                  var3.method_10264(),
                  var3.method_10260(),
                  0.0
               ),
               var4,
               g8.b.walking(16000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.u("no way to walk to it");
               return;
            }

            this.L1.X(var5.steps());
            this.L0 = var3;
            this.kQ = var3;
         }

         this.y();
      }
   }

   private void z(s0.j var1) {
      class_1792 var2 = this.F();
      int var3 = this.X();
      if (var2 == null) {
         this.u("the pillar block is used by the build");
      } else if (var3 >= this.mu.height()) {
         if (!mc.field_1724.method_24828()) {
            this.m("Landing on the pillar");
         } else {
            this.LM = s0.a.r;
            this.i = this.gv;
            this.qe.i();
            q8.v(this.getName(), "On a pillar of {} at {} {}", var3, this.mu.x(), this.mu.z());
         }
      } else if (mc.field_1724.field_6012 - this.gR > 80) {
         this.u("a pillar block did not go down");
      } else {
         class_2338 var4 = new class_2338(this.mu.x(), this.mu.ground() + var3, this.mu.z());
         class_2338 var5 = var4.method_10074();
         int var6 = this.O(var2);
         if (var6 == -2) {
            this.u("no " + R(var2) + " left");
         } else if (var6 >= 0 && this.m(var6)) {
            this.Lh = (boolean)(var3 > 0 ? 1 : 0);
            class_243 var7 = new class_243(var4.method_10263() + 0.5, var4.method_10264(), var4.method_10260() + 0.5);
            float[] var8 = t9.X(mc.field_1724.method_33571(), var7);
            this.aimPlacement(var8[0], var8[1]);
            double var9 = this.c();
            if (!mc.field_1724.method_24828()) {
               if (!(mc.field_1724.method_23318() < var4.method_10264() + 1.02)) {
                  class_3965 var11 = new class_3965(var7, class_2350.field_11036, var5, false);
                  fk.j(
                     this,
                     var4,
                     var11,
                     var2,
                     var9,
                     true,
                     () -> fk.v(var4) && !this.I(),
                     () -> fk.N(this, var5, var9) && mc.field_1724.method_23318() >= var4.method_10264() + 1.02,
                     var2x -> {
                        this.Lt = mc.field_1724.field_6012;
                        if (var2x) {
                           this.gh.add(var4);
                           this.gR = mc.field_1724.field_6012;
                        }
                     }
                  );
               }
            } else {
               if (fk.N(this, var5, var9) && (var3 == 0 || mc.field_1687.method_8320(var5).method_27852(Y(var2)))) {
                  this.kx = (boolean)1;
               }

               this.m("Building a pillar: " + var3 + " of " + this.mu.height());
            }
         } else {
            this.m("Taking out " + R(var2) + " for a pillar");
         }
      }
   }

   private static class_2248 Y(class_1792 var0) {
      return var0 instanceof class_1747 var1 ? var1.method_7711() : null;
   }

   private int X() {
      class_2248 var1 = Y(this.F() == null ? class_1802.field_8831 : this.F());

      while (!this.gh.isEmpty() && !mc.field_1687.method_8320(this.gh.getLast()).method_27852(var1)) {
         this.gh.removeLast();
      }

      return this.gh.size();
   }

   private void i(String var1) {
      fk.N(this);
      this.L1.i();
      this.LM = this.kj.isEmpty() ? s0.a.K : s0.a.h;
      this.gR = mc.field_1724.field_6012;
      if (this.mu != null) {
         this.v.add(com.larpclient.g8.M(this.mu.x(), 0, this.mu.z()));
         this.Lc = this.gv;
      }

      if (this.gv == this.i && this.mu != null) {
         this.gQ.add(com.larpclient.g8.M(this.mu.x(), 0, this.mu.z()));
      }

      if (this.mu != null && this.l != null && this.gv == this.i) {
         class_2339 var2 = new class_2339();

         for (l1.i var4 : this.mu.unlocks()) {
            if (var4.id() >= 0 && this.B(this.l, var4.id(), var2) == uo.t.L) {
               this.h.add(var4.id());
            }
         }
      }

      if (this.k8 && this.gv == this.g_) {
         this.S(this.LD);
      }

      this.k8 = (boolean)0;
      if (this.mu != null) {
         q8.G(
            this.getName(),
            "Tower at {} {} comes down after {} blocks, {} walkway blocks: {}{}",
            this.mu.x(),
            this.mu.z(),
            this.gv - this.g_,
            this.kj.size(),
            var1,
            this.Y()
         );
      }
   }

   private void j() {
      class_1792 var1 = this.F();

      for (class_2248 var2 = Y(var1 == null ? class_1802.field_8831 : var1);
         !this.gh.isEmpty() && !mc.field_1687.method_8320(this.gh.getLast()).method_27852(var2);
         this.gR = mc.field_1724.field_6012
      ) {
         this.gh.removeLast();
      }

      if (this.gh.isEmpty()) {
         bv.i(this);
         this.mu = null;
         this.LM = null;
         this.qe.i();
         this.m8 = 12;
         this.m("Pillar removed");
      } else if (!mc.field_1724.method_24828() && !mc.field_1724.method_5799()) {
         this.m("Coming down");
      } else if (mc.field_1724.field_6012 - this.gR > 200) {
         bv.i(this);
         class_2338 var4 = this.gh.getFirst();
         q8.G(this.getName(), "Could not dig the pillar at {} from its top with {} blocks left; sweeping them up instead", var4.method_23854(), this.gh.size());
         this.L(this.gh);
         this.mu = null;
         this.LM = null;
      } else {
         this.Lh = (boolean)1;
         class_2338 var3 = this.gh.getLast();
         if (!this.H(var3)) {
            this.v(var3);
         }
      }
   }

   private void v(class_2338 var1) {
      if (this.LK.add(var1.method_10063())) {
         q8.G(
            this.getName(),
            "Miner refused {}: wait {}, allowed {}, breakable {}, seen {}, state {}, player {} eye {}, block above feet {}, at head {}",
            var1.method_23854(),
            this.m(),
            cr.o(this),
            bv.P(var1),
            bv.z(this, var1, this.c()) != null,
            mc.field_1687.method_8320(var1).method_26204().method_9518().getString(),
            mc.field_1724.method_73189(),
            mc.field_1724.method_33571(),
            mc.field_1687.method_8320(mc.field_1724.method_24515()).method_26204().method_9518().getString(),
            mc.field_1687.method_8320(mc.field_1724.method_24515().method_10084()).method_26204().method_9518().getString()
         );
      }
   }

   private static boolean b(class_2680 var0) {
      class_2248 var1 = var0.method_26204();
      return var1 instanceof class_2281 || var1 instanceof class_3708 || var1 instanceof class_2480;
   }

   private String b() {
      return mc.field_1687.method_27983().method_29177().toString();
   }

   private String I() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (mc.field_1765 instanceof class_3965 var1 && var1.method_17783() == class_240.field_1332 && b(mc.field_1687.method_8320(var1.method_17777()))) {
         class_2338 var7 = var1.method_17777();
         s0.s var3 = new s0.s(this.b(), var7.method_10263(), var7.method_10264(), var7.method_10260());
         List var4 = T(this.O.getValue());
         boolean var5 = var4.remove(var3);
         if (!var5) {
            var4.add(var3);
         }

         this.O.Y(N(var4));
         this.e();
         String var6 = mc.field_1687.method_8320(var7).method_26204().method_9518().getString();
         q8.G(this.getName(), "{} restock container {} at {} ({} marked)", var5 ? "Unmarked" : "Marked", var6, var7.method_23854(), var4.size());
         return (var5 ? "Unmarked the " : "Marked the ")
            + var6.toLowerCase(Locale.ROOT)
            + " at "
            + var7.method_23854()
            + " as a restock container ("
            + var4.size()
            + " marked).";
      } else {
         return "Look at a chest, barrel or shulker box first.";
      }
   }

   private String D() {
      int var1 = T(this.O.getValue()).size();
      this.O.Y("");
      this.e();
      return var1 == 0 ? "No containers were marked." : "Cleared " + var1 + " restock " + (var1 == 1 ? "mark." : "marks.");
   }

   private void i() {
      int var1 = this.kb.getValue() != -1
            && this.isInGame()
            && mc.field_1755 == null
            && mc.method_1569()
            && s5.m(mc.method_22683().method_4490(), this.kb.getValue())
         ? 1
         : 0;
      if (var1 != 0 && !this.qz) {
         this.N(this.I());
      }

      this.qz = (boolean)var1;
   }

   private boolean P(s0.j var1) {
      this.c.clear();

      for (Entry var3 : this.C(var1).entrySet()) {
         int var4 = (Integer)var3.getValue() - this.R((class_1792)var3.getKey());
         if (var4 > 0) {
            this.c.put((class_1792)var3.getKey(), var4);
         }
      }

      class_1792 var11 = this.ke.getValue() ? this.F() : null;
      if (this.ga > 0 && var11 != null && this.R(var11) < 32) {
         this.c.put(var11, 32 - this.R(var11));
      }

      if (this.c.isEmpty()) {
         return false;
      }

      if (var11 != null && this.R(var11) < 32) {
         this.c.put(var11, 32 - this.R(var11));
      }

      s0.s var12 = null;
      double var13 = this.mw.getValue() * this.mw.getValue();
      String var6 = this.b();

      for (s0.s var8 : T(this.O.getValue())) {
         if (var8.dimension().equals(var6) && !this.kq.contains(var8)) {
            double var9 = mc.field_1724.method_5707(class_243.method_24953(var8.pos()));
            if (!(var9 > var13)) {
               if (mc.field_1687.method_22340(var8.pos()) && !b(mc.field_1687.method_8320(var8.pos()))) {
                  this.kq.add(var8);
               } else {
                  var12 = var8;
                  var13 = var9;
               }
            }
         }
      }

      if (var12 == null) {
         return false;
      }

      this.T = var12;
      this.qF = s0.g.N;
      this.g0 = mc.field_1724.field_6012;
      this.gK = this.k5 = 0;
      this.mq.clear();
      this.L1.i();
      this.Lk = List.of();
      fk.N(this);
      ArrayList var14 = new ArrayList();

      for (Entry var16 : this.c.entrySet()) {
         var14.add(var16.getValue() + " " + R((class_1792)var16.getKey()));
      }

      q8.G(this.getName(), "Restock trip to {} for {}", var12.pos().method_23854(), String.join(", ", var14));
      this.m("Going to the restock container at " + var12.pos().method_23854());
      return true;
   }

   private void p(String var1) {
      if (this.ki) {
         this.Q(var1);
      } else {
         if (this.gK == 0 && this.T != null) {
            this.kq.add(this.T);
         } else if (this.gK > 0) {
            this.kq.clear();
         }

         q8.G(this.getName(), "Restock at {}: took {}, put back {} ({})", this.T == null ? "?" : this.T.pos().method_23854(), this.gK, this.k5, var1);
         if (this.gK > 0 || this.k5 > 0) {
            this.kN++;
            this.N("restocked at " + this.T.pos().method_23854() + ": took " + this.gK + (this.k5 > 0 ? ", put back " + this.k5 : "") + ".");
         }

         this.qF = null;
         this.T = null;
         this.L1.i();
         fk.N(this);
         this.m8 = 12;
      }
   }

   private Set<Long> C(class_2338 var1) {
      cm var2 = new cm(var0 -> -1, false);
      HashSet var3 = new HashSet();
      double var4 = (this.c() - 0.3) * (this.c() - 0.3);

      for (int var6 = -4; var6 <= 4; var6++) {
         for (int var7 = -4; var7 <= 4; var7++) {
            for (int var8 = -3; var8 <= 1; var8++) {
               int var9 = var1.method_10263() + var6;
               int var10 = var1.method_10264() + var8;
               int var11 = var1.method_10260() + var7;
               if (var6 != 0 || var7 != 0) {
                  double var12 = var9 + 0.5 - (var1.method_10263() + 0.5);
                  double var14 = var10 + 1.62 - (var1.method_10264() + 0.5);
                  double var16 = var11 + 0.5 - (var1.method_10260() + 0.5);
                  if (!(var12 * var12 + var14 * var14 + var16 * var16 > var4)
                     && var2.loaded(var9, var10, var11)
                     && var2.floor(var9, var10 - 1, var11)
                     && var2.open(var9, var10, var11)
                     && var2.open(var9, var10 + 1, var11)
                     && uo.l(this, var1, new class_243(var9 + 0.5, var10 + 1.62, var11 + 0.5), this.c() - 0.3, null) != null) {
                     var3.add(com.larpclient.g8.M(var9, var10, var11));
                  }
               }
            }
         }
      }

      return var3;
   }

   private void E(s0.j var1) {
      class_2338 var2 = this.T.pos();
      if (mc.field_1724.field_6012 - this.g0 > 900) {
         this.p("the walk took too long");
      } else if (mc.field_1687.method_22340(var2) && b(mc.field_1687.method_8320(var2))) {
         if (!this.L1.G() && uo.l(this, var2, mc.field_1724.method_33571(), this.c(), null) != null) {
            this.qF = s0.g.C;
            this.g0 = mc.field_1724.field_6012;
         } else {
            if (!this.L1.G()) {
               class_2338 var3 = this.H();
               Set var4 = this.C(var2);
               if (var4.isEmpty()) {
                  this.p("no spot to stand at it");
                  return;
               }

               g8.l var5 = new g8.l(
                  Math.min(var2.method_10263(), var3.method_10263()) - 12,
                  Math.min(var2.method_10264(), var3.method_10264()) - 6,
                  Math.min(var2.method_10260(), var3.method_10260()) - 12,
                  Math.max(var2.method_10263(), var3.method_10263()) + 12,
                  Math.max(var2.method_10264(), var3.method_10264()) + 6,
                  Math.max(var2.method_10260(), var3.method_10260()) + 12
               );
               g8.t var6 = com.larpclient.g8.O(
                  new cm(var0 -> -1, false),
                  var3.method_10263(),
                  var3.method_10264(),
                  var3.method_10260(),
                  g8.i.S(var4, var2.method_10263(), var2.method_10264(), var2.method_10260(), this.c()),
                  var5,
                  g8.b.walking(32000)
               );
               if (!var6.found()) {
                  this.p("no way to walk to it");
                  return;
               }

               if (var6.steps().isEmpty()) {
                  this.qF = s0.g.C;
                  this.g0 = mc.field_1724.field_6012;
                  return;
               }

               this.L1.X(var6.steps());
               this.L0 = var2;
               g8.p var7 = var6.steps().getLast();
               this.kQ = new class_2338(var7.x(), var7.y(), var7.z());
            }

            this.y();
         }
      } else {
         this.p("the container is gone");
      }
   }

   private void R() {
      class_2338 var1 = this.T.pos();
      if (mc.field_1724.field_6012 - this.g0 > 60) {
         this.p("it did not open");
      } else {
         this.kv = class_243.field_1353;
         this.Lh = (boolean)0;
         int var2 = this.l();
         if (var2 >= 0 && !this.m(var2)) {
            this.m("Putting the block away");
         } else {
            class_1799 var3 = mc.field_1724.method_6047();
            class_1792 var4 = var3.method_7960() ? class_1802.field_8162 : var3.method_7909();
            double var5 = this.c();
            fk.E(
               this,
               var1,
               var4,
               var5,
               true,
               false,
               () -> !this.I(),
               () -> !mc.field_1724.method_5715() && mc.field_1724.method_18798().method_37268() < 1.0E-4 && fk.N(this, var1, var5),
               var1x -> {
                  this.Lt = mc.field_1724.field_6012;
                  if (var1x) {
                     this.qF = s0.g.h;
                     this.g0 = mc.field_1724.field_6012;
                     this.mf = -1000000000;
                  }
               }
            );
            this.m("Opening the restock container at " + var1.method_23854());
         }
      }
   }

   private boolean t(s0.j var1) {
      int var2 = mc.field_1724.field_6012;
      this.J();
      class_437 var3 = mc.field_1755;
      class_1703 var4 = mc.field_1724.field_7512;
      if (var3 instanceof class_465 && var4 != mc.field_1724.field_7498) {
         if (this.mf == -1000000000) {
            this.mf = var2;
         }

         if (var2 - this.g0 > 400) {
            mc.field_1724.method_7346();
            this.p("the clicks took too long");
            return true;
         }

         this.m("Restocking at " + this.T.pos().method_23854());
         if (var2 - this.mf >= 6 && var2 - this.qP >= 4 && var4.method_34255().method_7960()) {
            int var5 = var4.field_7761.size() - 36;
            if (var5 <= 0) {
               mc.field_1724.method_7346();
               this.p("not a container menu");
               return true;
            }

            if (this.kF.getValue()) {
               Map var6 = this.M();
               class_1792 var7 = this.ke.getValue() ? this.F() : null;

               for (int var8 = var5; var8 < var4.field_7761.size(); var8++) {
                  class_1799 var9 = ((class_1735)var4.field_7761.get(var8)).method_7677();
                  if (!var9.method_7960()
                     && var9.method_7909() instanceof class_1747
                     && !var6.containsKey(var9.method_7909())
                     && !var9.method_31574(var7)
                     && !this.mq.contains(var8)
                     && J(var4, 0, var5, var9)) {
                     int var10 = var9.method_7947();
                     this.I(var4, var8);
                     this.k5 += var10;
                     this.mq.add(var8);
                     return true;
                  }
               }
            }

            for (int var11 = 0; var11 < var5; var11++) {
               class_1799 var12 = ((class_1735)var4.field_7761.get(var11)).method_7677();
               int var13 = var12.method_7960() ? 0 : this.c.getOrDefault(var12.method_7909(), 0);
               if (var13 > 0 && J(var4, var5, var4.field_7761.size(), var12)) {
                  int var14 = var12.method_7947();
                  class_1792 var15 = var12.method_7909();
                  this.I(var4, var11);
                  this.gK += var14;
                  this.c.merge(var15, -var14, Integer::sum);
                  return true;
               }
            }

            mc.field_1724.method_7346();
            this.p(this.c.values().stream().anyMatch(var0 -> var0 > 0) ? "took what it had" : "took everything needed");
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.g0 <= 60) {
         this.m("Waiting for the container to open");
         return true;
      } else {
         this.p("it did not open");
         return false;
      }
   }

   private void I(class_1703 var1, int var2) {
      mc.field_1761.method_2906(var1.field_7763, var2, 0, class_1713.field_7794, mc.field_1724);
      this.qP = mc.field_1724.field_6012;
   }

   private static boolean J(class_1703 var0, int var1, int var2, class_1799 var3) {
      for (int var4 = var1; var4 < var2; var4++) {
         class_1799 var5 = ((class_1735)var0.field_7761.get(var4)).method_7677();
         if (var5.method_7960() || class_1799.method_31577(var5, var3) && var5.method_7947() < var5.method_7914()) {
            return true;
         }
      }

      return false;
   }

   private String i() {
      if (!this.isInGame()) {
         return "";
      }

      int var1 = 0;

      for (s0.s var3 : T(this.O.getValue())) {
         if (var3.dimension().equals(this.b())) {
            var1++;
         }
      }

      String var4 = var1 == 0 ? "No chests selected. Look at one and press Select chest." : var1 + (var1 == 1 ? " chest selected." : " chests selected.");
      return this.ki && this.Lg ? var4 + " " + this.E() + "." : var4;
   }

   private String q() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.Lg) {
         return this.ki ? "Already stocking." : "Turn the builder off first; it stocks between builds.";
      }

      String var1 = tc.q(this);
      if (var1 != null) {
         return "Turn off " + var1 + " first; one bot runs at a time.";
      }

      if (this.Ld) {
         return "Stop and save the Build Recorder first.";
      }

      s0.j var2 = this.l;
      if (var2 == null) {
         return this.mV == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var2.k) {
         return "Still checking the world; try again in a moment.";
      }

      if (this.C(var2).isEmpty()) {
         return "The build needs no more blocks.";
      }

      int var3 = this.d().size();
      if (var3 == 0) {
         return "Select the material chests first: look at a chest, barrel or shulker box within "
            + this.mw.getValue().intValue()
            + " blocks and press Select chest.";
      }

      long var4 = System.currentTimeMillis();
      if (var4 - this.g6 > 10000L) {
         this.g6 = var4;
         return "Press Stock materials again within 10 seconds to start. It checks the "
            + var3
            + (var3 == 1 ? " chest" : " chests")
            + " for what they already hold, buys only what is still short from /ah, cheapest first, at most "
            + wh.y(this.qX.getValue().longValue())
            + " a block and "
            + wh.y(this.gU.getValue().longValue())
            + " in all, and puts it into them.";
      }

      this.g6 = 0L;
      this.kJ = var4;
      this.enable();
      if (this.ki && this.Lg) {
         return "Stocking: checking " + var3 + (var3 == 1 ? " chest" : " chests") + " first.";
      }

      this.kJ = 0L;
      return this.mY != null ? this.mY + "." : "Stocking did not start.";
   }

   private List<s0.s> d() {
      ArrayList var1 = new ArrayList();
      double var2 = this.mw.getValue() * this.mw.getValue();
      String var4 = this.b();

      for (s0.s var6 : T(this.O.getValue())) {
         if (var6.dimension().equals(var4) && mc.field_1724.method_5707(class_243.method_24953(var6.pos())) <= var2) {
            var1.add(var6);
         }
      }

      return var1;
   }

   private void w() {
      if (this.gl) {
         this.I();
      }

      s0.j var1 = this.l;
      if (var1 != null && var1.k) {
         List var2 = this.d();
         if (var2.isEmpty()) {
            this.x("no material chests are selected within Restock Distance");
         } else {
            if (var1.n <= 200000) {
               this.w(var1, var1.n);
            }

            this.Lg = (boolean)1;
            this.ki = (boolean)1;
            this.LL = (boolean)0;
            this.gP = mc.field_1687;
            this.Lz = mc.field_1724;
            this.gF.V();
            this.d = System.currentTimeMillis();
            this.mz = s0.o.t;
            this.gT.addAll(var2);
            q8.G(this.getName(), "Stocking started for {}: reading {} containers within {} blocks", this.mV.D(), var2.size(), this.mw.getValue());
            this.N("stocking " + this.mV.D() + ": checking " + var2.size() + (var2.size() == 1 ? " chest" : " chests") + " for what they hold first.");
         }
      } else {
         this.x("set the schematic and its origin first");
      }
   }

   private void R(s0.j var1) {
      if (this.qF == s0.g.N) {
         this.E(var1);
      } else if (this.qF == s0.g.C) {
         this.R();
      } else if (this.mz == s0.o.t) {
         s0.s var6 = this.r(this.gT);
         if (var6 != null) {
            this.d(var6);
         } else {
            this.e(var1);
         }
      } else {
         if (this.mz == s0.o.w) {
            s0.u var2 = this.m();
            if (var2 == s0.u.N) {
               return;
            }

            if (var2 == s0.u.r) {
               this.LL = (boolean)1;
            }

            this.mz = s0.o.e;
         }

         if (this.D.isEmpty()) {
            if (!this.LL) {
               this.mz = s0.o.w;
            } else {
               this.v((String)null);
            }
         } else {
            ArrayList var5 = new ArrayList();

            for (s0.s var4 : this.r.keySet()) {
               if (!this.I.contains(var4) && (this.Y.getOrDefault(var4, 0) > 0 || !Collections.disjoint(this.r.get(var4).keySet(), this.D.keySet()))) {
                  var5.add(var4);
               }
            }

            s0.s var7 = this.r(var5);
            if (var7 == null) {
               this.v("the selected chests are full");
            } else {
               this.d(var7);
            }
         }
      }
   }

   private s0.s r(Collection<s0.s> var1) {
      s0.s var2 = null;
      double var3 = Double.MAX_VALUE;

      for (s0.s var6 : var1) {
         if (!this.g7.contains(var6) && !this.k3.contains(var6)) {
            double var7 = mc.field_1724.method_5707(class_243.method_24953(var6.pos()));
            if (var7 < var3) {
               var2 = var6;
               var3 = var7;
            }
         }
      }

      return var2;
   }

   private void d(s0.s var1) {
      this.T = var1;
      this.qF = s0.g.N;
      this.g0 = mc.field_1724.field_6012;
      this.gK = this.k5 = 0;
      this.mq.clear();
      this.kU = (boolean)0;
      this.L1.i();
      this.Lk = List.of();
      fk.N(this);
      q8.G(this.getName(), "Stocking: going to {} to {}", var1.pos().method_23854(), this.mz == s0.o.t ? "read it" : "put " + y(this.D) + " blocks in");
      this.m((this.mz == s0.o.t ? "Checking the chest at " : "Taking the materials to the chest at ") + var1.pos().method_23854());
   }

   private void Q(String var1) {
      s0.s var2 = this.T;
      q8.G(this.getName(), "Stocking at {}: {}", var2 == null ? "?" : var2.pos().method_23854(), var1);
      if (var2 != null) {
         if (this.mz == s0.o.t) {
            this.gT.remove(var2);
         }

         if (!this.kU && this.g7.add(var2)) {
            this.N("could not use the chest at " + var2.pos().method_23854() + ": " + var1 + "; going on without it.");
         }
      }

      this.qF = null;
      this.T = null;
      this.L1.i();
      fk.N(this);
   }

   private boolean v() {
      int var1 = mc.field_1724.field_6012;
      this.J();
      class_437 var2 = mc.field_1755;
      class_1703 var3 = mc.field_1724.field_7512;
      if (var2 instanceof class_465 && var3 != mc.field_1724.field_7498) {
         if (this.mf == -1000000000) {
            this.mf = var1;
         }

         if (var1 - this.g0 > 400) {
            mc.field_1724.method_7346();
            this.p("the clicks took too long");
            return true;
         }

         if (var1 - this.mf >= 6 && var1 - this.qP >= 4 && var3.method_34255().method_7960()) {
            int var4 = var3.field_7761.size() - 36;
            if (var4 <= 0) {
               mc.field_1724.method_7346();
               this.p("not a container menu");
               return true;
            }

            s0.s var5 = this.T;
            if (this.mz == s0.o.e) {
               this.m("Putting materials into the chest at " + var5.pos().method_23854());

               for (int var6 = var4; var6 < var3.field_7761.size(); var6++) {
                  class_1799 var7 = ((class_1735)var3.field_7761.get(var6)).method_7677();
                  if (!var7.method_7960() && this.D.getOrDefault(var7.method_7909(), 0) > 0 && var7.method_57380().method_57848() && J(var3, 0, var4, var7)) {
                     class_1792 var8 = var7.method_7909();
                     int var9 = var7.method_7947();
                     this.I(var3, var6);
                     int var10 = this.D.get(var8) - var9;
                     if (var10 > 0) {
                        this.D.put(var8, var10);
                     } else {
                        this.D.remove(var8);
                     }

                     this.kO.merge(var8, var9, Integer::sum);
                     this.kU = (boolean)1;
                     return true;
                  }
               }

               if (!this.D.isEmpty()) {
                  this.I.add(var5);
               }
            }

            HashMap var11 = new HashMap();
            int var12 = 0;

            for (int var13 = 0; var13 < var4; var13++) {
               class_1799 var15 = ((class_1735)var3.field_7761.get(var13)).method_7677();
               if (var15.method_7960()) {
                  var12++;
               } else if (var15.method_57380().method_57848()) {
                  var11.merge(var15.method_7909(), var15.method_7947(), Integer::sum);
               }
            }

            if (!this.r.containsKey(var5)) {
               q8.G(
                  this.getName(),
                  "Stocking: the container at {} holds {} kinds of plain items, {} of {} slots free",
                  var5.pos().method_23854(),
                  var11.size(),
                  var12,
                  var4
               );
            }

            this.r.put(var5, var11);
            this.Y.put(var5, var12);
            s0.s var14 = this.z(var5);
            if (var14 != null) {
               this.k3.add(var14);
               this.gT.remove(var14);
               this.r.remove(var14);
               this.Y.remove(var14);
            }

            this.kU = (boolean)1;
            mc.field_1724.method_7346();
            this.p(this.mz == s0.o.t ? "read it" : (this.D.isEmpty() ? "put everything in" : "it is full"));
            return true;
         } else {
            return true;
         }
      } else if (var1 - this.g0 <= 60) {
         this.m("Waiting for the chest to open");
         return true;
      } else {
         this.p("it did not open");
         return false;
      }
   }

   private s0.s z(s0.s var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1.pos());
      if (var2.method_26204() instanceof class_2281 && var2.method_11654(class_2281.field_10770) != class_2745.field_12569) {
         class_2338 var3 = var1.pos().method_10093(class_2281.method_9758(var2));
         return new s0.s(var1.dimension(), var3.method_10263(), var3.method_10264(), var3.method_10260());
      } else {
         return null;
      }
   }

   private void e(s0.j var1) {
      int var2 = 0;

      for (s0.s var4 : this.r.keySet()) {
         if (!this.k3.contains(var4)) {
            var2++;
         }
      }

      if (var2 == 0) {
         this.x("none of the selected chests could be opened");
      } else {
         LinkedHashMap var10 = new LinkedHashMap<>(this.C(var1));
         class_1792 var11 = this.ke.getValue() ? this.F() : null;
         if (var11 != null) {
            var10.merge(var11, 32, Integer::sum);
         }

         HashMap var5 = new HashMap();

         for (Map var7 : this.r.values()) {
            var7.forEach((var1x, var2x) -> var5.merge(var1x, var2x, Integer::sum));
         }

         for (class_1792 var15 : var10.keySet()) {
            var5.merge(var15, this.R(var15), Integer::sum);
         }

         this.mp.putAll(J(var10, var5));
         this.L3 = 0;

         for (int var16 : this.Y.values()) {
            this.L3 += var16;
         }

         int var14 = y(var10);
         int var17 = var14 - y(this.mp);
         q8.G(this.getName(), "Stocking plan: {} chests read, {} of {} blocks at hand, {} free chest slots; to buy {}", var2, var17, var14, this.L3, this.mp);
         this.N(
            "checked "
               + var2
               + (var2 == 1 ? " chest" : " chests")
               + ": with your inventory they hold "
               + var17
               + " of the "
               + var14
               + " blocks the build still needs."
         );
         if (this.mp.isEmpty()) {
            this.v("nothing needs buying");
         } else {
            for (Entry var9 : this.mp.entrySet()) {
               this.N("  to buy: " + var9.getValue() + " " + R((class_1792)var9.getKey()));
            }

            this.mz = s0.o.w;
         }
      }
   }

   static Map<class_1792, Integer> J(Map<class_1792, Integer> var0, Map<class_1792, Integer> var1) {
      LinkedHashMap var2 = new LinkedHashMap();

      for (Entry var4 : var0.entrySet()) {
         int var5 = (Integer)var4.getValue() - var1.getOrDefault(var4.getKey(), 0);
         if (var5 > 0) {
            var2.put((class_1792)var4.getKey(), var5);
         }
      }

      return var2;
   }

   static int y(Map<class_1792, Integer> var0) {
      int var1 = 0;

      for (int var3 : var0.values()) {
         var1 += var3;
      }

      return var1;
   }

   private s0.u m() {
      if (this.k0) {
         return s0.u.r;
      }

      long var1 = this.gU.getValue().longValue() - this.f;
      if (var1 <= 0L) {
         this.F("Max Total Spend is used up; nothing more is bought");
         return s0.u.r;
      }

      for (Entry var4 : this.mp.entrySet()) {
         class_1792 var5 = (class_1792)var4.getKey();
         if ((Integer)var4.getValue() > 0 && !this.kf.contains(var5)) {
            if (this.L3 <= 0) {
               this.F("the selected chests have no free slots left; select more chests");
               return s0.u.r;
            }

            int var6 = new class_1799(var5).method_7914();
            int var7 = Math.min((Integer)var4.getValue(), this.L3 * var6);
            int var8 = this.Q(var5);
            if (var8 < Math.min(var7, var6)) {
               if (!this.D.isEmpty()) {
                  return s0.u.G;
               }

               this.F("no free inventory room to buy " + R(var5) + " into; empty a few slots");
               return s0.u.r;
            }

            var7 = Math.min(var7, var8);
            this.m7 = var5;
            this.mM = 0;
            this.L1.i();
            this.Lk = List.of();
            fk.N(this);
            this.mL.d(var5, var7, this.qX.getValue().longValue(), this.A.getValue().longValue(), var1);
            this.N(
               "buying "
                  + var7
                  + " "
                  + R(var5)
                  + " from /ah, cheapest first: at most "
                  + wh.y(this.qX.getValue().longValue())
                  + " a block, "
                  + wh.y(this.A.getValue().longValue())
                  + " a listing, "
                  + wh.y(var1)
                  + " left to spend."
            );
            return s0.u.N;
         }
      }

      return s0.u.r;
   }

   private int Q(class_1792 var1) {
      int var2 = new class_1799(var1).method_7914();
      int var3 = 0;
      class_1661 var4 = mc.field_1724.method_31548();

      for (int var5 = 0; var5 < 36; var5++) {
         class_1799 var6 = var4.method_5438(var5);
         if (var6.method_7960()) {
            var3 += var2;
         } else if (var6.method_31574(var1) && var6.method_57380().method_57848()) {
            var3 += Math.max(0, var2 - var6.method_7947());
         }
      }

      return var3;
   }

   private void s(class_1792 var1, int var2, long var3, String var5) {
      if (var1 != null) {
         if (var2 > 0) {
            int var6 = new class_1799(var1).method_7914();
            int var7 = this.mp.getOrDefault(var1, 0) - var2;
            if (var7 > 0) {
               this.mp.put(var1, var7);
            } else {
               this.mp.remove(var1);
            }

            this.D.merge(var1, var2, Integer::sum);
            this.gi.merge(var1, var2, Integer::sum);
            this.L3 -= (var2 + var6 - 1) / var6;
         } else {
            this.W.put(var1, var3);
            this.kX.put(var1, var5 != null && !var5.isEmpty() ? var5 : "nothing bought");
         }
      }
   }

   private void v(String var1) {
      String var2 = sh.w(System.currentTimeMillis() - this.d);
      StringBuilder var3 = new StringBuilder("stocking done in ").append(var2);
      if (var1 != null) {
         var3.append(" (").append(var1).append(')');
      }

      var3.append(": bought ").append(y(this.gi)).append(" blocks for ").append(wh.y(this.f)).append(", put ").append(y(this.kO)).append(" into the chests");
      this.N(var3 + ".");

      for (Entry var5 : this.gi.entrySet()) {
         this.N("  bought " + var5.getValue() + " " + R((class_1792)var5.getKey()) + ", put away " + this.kO.getOrDefault(var5.getKey(), 0));
      }

      if (!this.D.isEmpty()) {
         ArrayList var7 = new ArrayList();

         for (Entry var6 : this.D.entrySet()) {
            var7.add(var6.getValue() + " " + R((class_1792)var6.getKey()));
         }

         this.N("  still in your inventory, no chest had room: " + String.join(", ", var7));
      }

      for (Entry var10 : this.mp.entrySet()) {
         String var11 = this.kX.containsKey(var10.getKey())
            ? this.kX.get(var10.getKey())
            : (
               this.k0
                  ? "out of money"
                  : (this.f >= this.gU.getValue().longValue() ? "Max Total Spend reached" : (this.L3 <= 0 ? "no chest room left" : "not bought"))
            );
         this.N("  still short: " + var10.getValue() + " " + R((class_1792)var10.getKey()) + " (" + var11 + ")");
      }

      q8.G(this.getName(), "Stocking done: {}; still short {}; reasons {}", var3, this.mp, this.kX);
      this.mY = "Last stocking: bought " + y(this.gi) + ", put away " + y(this.kO) + (this.mp.isEmpty() ? "" : ", " + y(this.mp) + " still short");
      wt.T()
         .k(
            "Auto Builder",
            "Stocking done: " + y(this.kO) + " blocks put away" + (this.mp.isEmpty() ? "" : ", " + y(this.mp) + " still short"),
            this.mp.isEmpty() ? wt.s.C : wt.s.a,
            8000L
         );
      this.Lg = (boolean)0;
      this.disable();
   }

   private String E() {
      String var1 = this.mz == s0.o.t ? "checking the chests" : (this.mz == s0.o.w ? "buying" : "putting materials away");
      return "Stocking, "
         + var1
         + ": bought "
         + y(this.gi)
         + " for "
         + wh.y(this.f)
         + ", "
         + y(this.kO)
         + " put away, "
         + sh.w(System.currentTimeMillis() - this.d);
   }

   private Path G() {
      return b().resolve(com.larpclient.g3.G(this.m.getValue().trim()));
   }

   private void V(s0.j var1) {
      this.gg = var1;
      this.Lo = null;
      this.LI = null;
      if (var1 != null && !this.m.getValue().isBlank()) {
         try {
            List var2 = com.larpclient.g3.x(this.G());
            if (var2.isEmpty()) {
               return;
            }

            ArrayList var3 = new ArrayList();

            for (int[] var5 : var2) {
               int var6 = var1.M(var5[0], var5[1], var5[2]);
               if (var6 >= 0) {
                  var3.add(var6);
               }
            }

            this.Lo = var3.stream().mapToInt(Integer::intValue).toArray();
            q8.G(
               this.getName(),
               "Recorded order of {}: {} cells ({} not in the schematic)",
               this.m.getValue().trim(),
               this.Lo.length,
               var2.size() - this.Lo.length
            );
         } catch (IOException var7) {
            this.LI = var7.getMessage();
            q8.G(this.getName(), "Could not read the recorded order: {}", var7.toString());
         }
      }
   }

   private String W() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.Lg) {
         return "Turn the builder off before recording.";
      }

      s0.j var1 = this.l;
      if (var1 == null) {
         return this.mV == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (this.Ld) {
         return "Already recording: " + this.u.size() + " blocks so far.";
      }

      if (var1.n > 200000) {
         return "This schematic is too big to record (" + var1.n + " blocks).";
      }

      this.Lx = new BitSet(var1.n);

      for (int var2 = 0; var2 < var1.n; var2++) {
         class_2680 var3 = var1.q(var2);
         if (var3 != null && mc.field_1687.method_8320(var1.P(var2)).method_27852(var3.method_26204())) {
            this.Lx.set(var2);
         }
      }

      this.u.clear();
      this.kV.clear();
      this.Ld = (boolean)1;
      q8.G(this.getName(), "Build Recorder started on {} ({} of {} blocks already stand)", this.mV.D(), this.Lx.cardinality(), var1.n);
      return "Recording: place the blocks of " + this.mV.D() + " in the order you want them built, then press Stop and save the recording.";
   }

   private void r(s0.j var1) {
      if (this.Ld) {
         class_2338 var2 = mc.field_1724.method_24515();
         class_2339 var3 = new class_2339();

         for (int var4 = Math.max(var1.S, var2.method_10263() - 8); var4 <= Math.min(var1.E, var2.method_10263() + 8); var4++) {
            for (int var5 = Math.max(var1.X, var2.method_10264() - 8); var5 <= Math.min(var1.D, var2.method_10264() + 8); var5++) {
               for (int var6 = Math.max(var1.N, var2.method_10260() - 8); var6 <= Math.min(var1.M, var2.method_10260() + 8); var6++) {
                  int var7 = var1.K(var4, var5, var6);
                  if (var7 >= 0
                     && !this.kV.contains(var7)
                     && !this.Lx.get(var7)
                     && var1.q(var7) != null
                     && mc.field_1687.method_8320(var3.method_10103(var4, var5, var6)).method_27852(var1.q(var7).method_26204())) {
                     this.u.add(var7);
                     this.kV.add(var7);
                  }
               }
            }
         }
      }
   }

   private void e(String var1) {
      this.Ld = (boolean)0;
      q8.G(this.getName(), "Build Recorder stopped without saving: {} ({} blocks recorded)", var1, this.u.size());
      this.N("the Build Recorder stopped without saving: " + var1 + ".");
      this.u.clear();
      this.kV.clear();
   }

   private String x() {
      if (!this.Ld) {
         return "The Build Recorder is not recording.";
      }

      s0.j var1 = this.l;
      this.Ld = (boolean)0;
      if (var1 != null && !this.u.isEmpty()) {
         ArrayList var2 = new ArrayList();

         for (int var4 : this.u) {
            var2.add(new int[]{var1.Z.s(var4), var1.Z.r(var4), var1.Z.F(var4)});
         }

         Path var6 = this.G();

         try {
            com.larpclient.g3.g(var6, var1.Z.D(), var2);
         } catch (IOException var5) {
            return "Could not save the recording: " + var5.getMessage();
         }

         int var7 = var2.size();
         this.u.clear();
         this.kV.clear();
         this.V(var1);
         q8.G(this.getName(), "Saved the build order of {} blocks to {}", var7, var6.getFileName());
         return "Saved the order of " + var7 + " blocks to " + var6.getFileName() + ". Follow Recording builds in this order.";
      } else {
         this.u.clear();
         this.kV.clear();
         return "Nothing was placed, so nothing was saved.";
      }
   }

   private String K() {
      if (this.Ld) {
         this.e("you deleted the recording");
      }

      if (this.m.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      try {
         boolean var1 = Files.deleteIfExists(this.G());
         this.V(this.l);
         return var1 ? "Deleted the recorded order of " + this.m.getValue().trim() + "." : "This schematic has no recording.";
      } catch (IOException var2) {
         return "Could not delete the recording: " + var2.getMessage();
      }
   }

   private String L() {
      if (this.Ld) {
         return " Recording the build order: " + this.u.size() + " blocks so far.";
      } else if (this.LI != null) {
         return " Its recording could not be read: " + this.LI + ".";
      } else {
         return this.Lo != null && this.Lo.length > 0
            ? " Recorded order: " + this.Lo.length + " blocks" + (this.Lq.getValue() ? ", followed." : ", not followed.")
            : "";
      }
   }

   private boolean I(s0.j var1) {
      if (this.k0) {
         return false;
      }

      long var2 = this.gU.getValue().longValue() - this.f;
      if (var2 <= 0L) {
         this.F("Max Total Spend is used up; buying stops for this run");
         return false;
      }

      class_1792 var4 = null;
      int var5 = 0;
      class_1792 var6 = this.ga > 0 ? this.F() : null;
      if (var6 != null && !this.kf.contains(var6) && this.R(var6) < 32) {
         var4 = var6;
         var5 = 32 - this.R(var6);
      } else {
         for (Entry var8 : this.C(var1).entrySet()) {
            class_1792 var9 = (class_1792)var8.getKey();
            if (!this.kf.contains(var9) && this.J.containsKey(var9)) {
               int var10 = (Integer)var8.getValue() - this.R(var9);
               if (var10 > 0) {
                  var4 = var9;
                  var5 = var10;
                  break;
               }
            }
         }
      }

      if (var4 == null) {
         return false;
      }

      int var12 = 0;

      for (int var13 = 0; var13 < 36; var13++) {
         if (mc.field_1724.method_31548().method_5438(var13).method_7960()) {
            var12++;
         }
      }

      var5 = Math.min(var5, var12 * new class_1799(var4).method_7914());
      if (var5 <= 0) {
         this.F("no free inventory slot to buy " + R(var4) + " into");
         return false;
      } else {
         this.m7 = var4;
         this.mM = 0;
         this.L1.i();
         this.Lk = List.of();
         fk.N(this);
         this.mL.d(var4, var5, this.qX.getValue().longValue(), this.A.getValue().longValue(), var2);
         this.N(
            "buying "
               + var5
               + " "
               + R(var4)
               + " from /ah: at most "
               + wh.y(this.qX.getValue().longValue())
               + " a block, "
               + wh.y(this.A.getValue().longValue())
               + " a listing, "
               + wh.y(var2)
               + " left to spend."
         );
         return true;
      }
   }

   private void x() {
      this.J();
      this.Lh = (boolean)0;
      if (this.I() && this.mL.x()) {
         this.mL.O("you moved");
      }

      if (this.mL.x() && this.mM < 4) {
         this.mM = mc.field_1724.method_24828() && mc.field_1724.method_18798().method_37268() < 1.0E-4 ? this.mM + 1 : 0;
         this.m("Stopping to buy " + R(this.m7));
      } else if (this.mL.x() && this.mL.Z() == gi.a.D) {
         this.m("Buying " + R(this.m7) + " from /ah");
      } else {
         this.f = this.f + this.mL.G();
         this.g4 = this.g4 + this.mL.U();
         String var1 = R(this.m7);
         if (this.mL.U() > 0) {
            this.N("bought " + this.mL.U() + " " + var1 + " for " + wh.y(this.mL.G()) + (this.mL.n().isEmpty() ? "" : " (" + this.mL.n() + ")") + ".");
            wt.T().U("Auto Builder", "Bought " + this.mL.U() + " " + var1 + " for " + wh.y(this.mL.G()), wt.s.C);
         }

         if (this.mL.U() == 0) {
            this.kf.add(this.m7);
            this.N("could not buy " + var1 + ": " + (this.mL.n().isEmpty() ? "nothing bought" : this.mL.n()) + ".");
         }

         if (this.mL.L()) {
            this.k0 = (boolean)1;
         }

         if (this.ki) {
            this.s(this.m7, this.mL.U(), this.mL.u(), this.mL.n());
         }

         q8.G(
            this.getName(),
            "Buying {} ended {}: bought {} for {}; this run {} for {}",
            var1,
            this.mL.b(),
            this.mL.U(),
            wh.y(this.mL.G()),
            this.g4,
            wh.y(this.f)
         );
         this.m7 = null;
         this.Lt = mc.field_1724.field_6012;
      }
   }

   private boolean X() {
      double var1 = this.kc.getValue();
      double var3 = var1 * var1;
      String var5 = null;

      for (StaffListModule.z var7 : StaffListModule.L()) {
         if (var7.info() != null) {
            class_1657 var8 = mc.field_1687.method_18470(var7.info().method_2966().id());
            if (var8 != null && var8 != mc.field_1724) {
               double var9 = var8.method_5858(mc.field_1724);
               if (var9 <= var3) {
                  var3 = var9;
                  var5 = var7.name() + " (" + (int)Math.sqrt(var9) + " blocks away)";
               }
            }
         }
      }

      if (var5 == null) {
         return false;
      }

      this.LW = var5;
      return true;
   }

   private void D() {
      int var1 = mc.field_1724.field_6012;
      if (this.gW == -1000000000) {
         this.J();
         this.L1.i();
         this.gF.K();
         bv.i(this);
         if (mc.field_1755 instanceof class_465) {
            mc.field_1724.method_7346();
         }

         this.my = null;
         this.qF = null;
         if (this.mL.x()) {
            this.mL.O("staff nearby");
         }

         this.m7 = null;
         this.gW = var1 + (int)Math.round(this.K.getValue() * 20.0);
         q8.G(this.getName(), "Staff nearby: {}; logging off in {} s", this.LW, this.K.getValue());
         this.N("staff nearby: " + this.LW + ". Stopping and logging off.");
      }

      this.J();
      this.m("Staff nearby: " + this.LW);
      if (var1 >= this.gW) {
         class_634 var2 = mc.method_1562();
         String var3 = "staff came near: " + this.LW;
         if (!this.gh.isEmpty()) {
            var3 = var3 + ", " + this.gh.size() + " temporary blocks left standing";
         }

         if (var2 != null && !mc.method_1542()) {
            q8.G(this.getName(), "Logging off: {}", var3);
            this.x("logged off, " + var3);
            var2.method_48296().method_10747(class_2561.method_43470(this.ms.getValue().isBlank() ? "Auto Builder stopped: staff nearby" : this.ms.getValue()));
         } else {
            q8.G(this.getName(), "Staff stop in singleplayer: stopped without logging off ({})", var3);
            this.x(var3 + " (singleplayer: not logged off)");
         }
      }
   }

   private boolean W() {
      int var1 = mc.field_1724.field_6012;

      while (!this.LB.isEmpty() && var1 - this.LB.peekFirst() > 1200) {
         this.LB.removeFirst();
      }

      if (this.q9.getValue() && this.LB.size() >= 8) {
         long var2 = this.qn == 0L ? Long.MAX_VALUE : System.currentTimeMillis() - this.qn;
         if (!W(this.LB.size(), var2, this.ma)) {
            if (this.ma >= 3 && !this.L4) {
               this.L4 = (boolean)1;
               q8.G(this.getName(), "Relog On Ghost Blocks: {} relogs this build already; not relogging again", 3);
               this.N("the server keeps taking placed blocks back, but it already relogged 3 times this build, so it builds on without relogging.");
            }

            return false;
         } else {
            return !mc.method_1542() && mc.method_1558() != null && mc.method_1562() != null
               ? this.gh.isEmpty()
                  && this.LN.isEmpty()
                  && this.kY.isEmpty()
                  && this.kj.isEmpty()
                  && this.L7.isEmpty()
                  && this.LM == null
                  && this.gY == null
                  && this.qF == null
                  && this.m7 == null
                  && !this.mL.x()
                  && this.my == null
                  && !this.ki
                  && !(mc.field_1755 instanceof class_465)
                  && mc.field_1724.method_24828()
               : false;
         }
      } else {
         return false;
      }
   }

   static boolean W(int var0, long var1, int var3) {
      return var0 >= 8 && var1 >= 300000L && var3 < 3;
   }

   private void V() {
      int var1 = this.LB.size();
      this.mH = mc.method_1558();
      this.gz = 1;
      this.ma++;
      this.qn = System.currentTimeMillis();
      this.mX = 0;
      this.g3 = (boolean)0;
      this.J();
      this.L1.i();
      this.go.k();
      this.gF.K();
      q8.G(
         this.getName(),
         "Relog On Ghost Blocks: the server took back {} placed blocks in {} ticks; logging off and rejoining {} (relog {} of {}, {} blocks placed)",
         var1,
         1200,
         this.mH.field_3761,
         this.ma,
         3,
         this.gv
      );
      this.N("the server took back " + var1 + " placed blocks in the last minute. Logging off and rejoining to sync up, then building on.");
      this.m("Logging off to resync blocks");
   }

   private void s() {
      if (this.gz != 0) {
         class_310 var1 = class_310.method_1551();
         if (!this.isEnabled()) {
            this.S("the builder was turned off", false);
         } else {
            long var2 = System.currentTimeMillis();
            if (this.gz == 1) {
               this.gm = var2;
               this.X = var2 + 5000L;
               this.gz = 2;
               if (var1.field_1687 != null && var1.method_1562() != null) {
                  q8.G(this.getName(), "Logging off {} to resync blocks", this.mH.field_3761);
                  var1.method_73360(class_2561.method_43470("Larp Auto Builder: rejoining to resync blocks"));
               }
            } else {
               if (var1.field_1687 == null) {
                  this.g3 = (boolean)1;
               }

               if (this.gz == 3 || this.g3 && var1.field_1687 != null) {
                  this.gz = 3;
                  if (var2 - this.gm > 120000L) {
                     this.S("it rejoined but the build did not start again", true);
                  }
               } else if (var1.field_1687 != null) {
                  if (var2 - this.gm > 120000L) {
                     this.S("the log off did not go through", true);
                  }
               } else if (var2 - this.gm > 120000L) {
                  this.S("it could not rejoin within 120 seconds", true);
               } else {
                  class_437 var4 = var1.field_1755;
                  if (!(var4 instanceof class_412) && var2 >= this.X) {
                     if (this.mX >= 3) {
                        this.S("the server did not take 3 joins", true);
                     } else {
                        this.mX++;
                        this.X = var2 + 10000L;
                        q8.G(this.getName(), "Rejoining {} to resync blocks (try {} of {})", this.mH.field_3761, this.mX, 3);
                        Object var5 = var4 != null ? var4 : new class_500(new class_442());
                        class_412.method_36877((class_437)var5, var1, class_639.method_2950(this.mH.field_3761), this.mH, false, null);
                     }
                  }
               }
            }
         }
      }
   }

   private void S(String var1, boolean var2) {
      q8.G(this.getName(), "Relog On Ghost Blocks gave up: {}", var1);
      this.gz = 0;
      this.mH = null;
      if (this.isEnabled()) {
         this.kg = "it logged off to resync blocks and " + var1;
      }

      if (var2) {
         wt.T().U("Auto Builder relog", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), wt.s.a);
      }
   }

   private void k() {
      if (System.currentTimeMillis() >= this.gk && mc.field_1687.method_22340(mc.field_1724.method_24515())) {
         this.gk = 0L;
         String var1 = tc.q(this);
         if (var1 != null) {
            this.x("turn off " + var1 + " first; one bot runs at a time");
         } else {
            q8.G(this.getName(), "Carrying on after the relog ({} of {} this build)", this.ma, 3);
            this.N("rejoined; building on (relog " + this.ma + " of 3 this build).");
            this.l();
         }
      } else {
         this.m("Rejoined; waiting for the world to load");
      }
   }

   private void N(s0.j var1) {
      TreeMap var2 = new TreeMap();
      TreeMap var3 = new TreeMap();
      class_2339 var4 = new class_2339();
      int var5 = 0;
      int var6 = 0;

      for (int var7 = 0; var7 < var1.n; var7++) {
         if (var1.q(var7) != null && var1.M(var7) == uo.t.L) {
            if (var1.t[var1.K[var7]] != null) {
               var5++;
            } else if (this.g1.contains(var7)) {
               var6++;
            } else {
               this.B(var1, var7, var4);
               var2.merge(var4.method_10264(), 1, Integer::sum);
               var3.merge(var1.q(var7).method_26204().method_9518().getString(), 1, Integer::sum);
            }
         }
      }

      q8.G(
         this.getName(),
         "Left over: {} that are never placed (fluids and the like), {} outside the height limits; placeable, by Y: {}; by block: {}",
         var5,
         var6,
         var2,
         var3
      );
      int var9 = 0;

      for (int var8 = 0; var8 < var1.n && var9 < 12; var8++) {
         if (var1.q(var8) != null && (var1.M(var8) == uo.t.m || var1.M(var8) == uo.t.D)) {
            this.B(var1, var8, var4);
            q8.G(this.getName(), "Wrong at {}: {} instead of {}", var4.method_23854(), mc.field_1687.method_8320(var4), var1.q(var8));
            var9++;
         }
      }
   }

   private void b() {
      s0.j var1 = this.l;
      if (var1 != null && var1.n <= 200000) {
         this.w(var1, var1.n);
      }

      int var2 = var1 == null ? 0 : var1.l(uo.t.L);
      int var3 = var1 == null ? 0 : var1.l(uo.t.D) + var1.l(uo.t.m);
      if (var1 != null) {
         this.N(var1);
      }

      q8.G(
         this.getName(),
         "Movement: sharpest one-tick turn {} degrees, {} sudden reversals, {} sprint-jumps",
         String.format(Locale.ROOT, "%.1f", this.LX),
         this.w,
         this.kE
      );
      String var4 = sh.w(System.currentTimeMillis() - this.d);
      TreeMap var5 = new TreeMap();

      for (String var7 : this.kW.values()) {
         var5.merge(var7, 1, Integer::sum);
      }

      StringBuilder var10 = new StringBuilder(this.mV == null ? "build" : this.mV.D())
         .append(" done in ")
         .append(var4)
         .append(", ")
         .append(this.gv)
         .append(" blocks placed");
      if (this.LU > 0) {
         var10.append(", ").append(this.LU).append(" right clicks to set blocks");
      }

      if (!this.kn.isEmpty()) {
         var10.append(", ").append(this.kn.size()).append(this.kn.size() == 1 ? " crafter" : " crafters").append(" set");
      }

      if (this.Lf > 0) {
         var10.append(", ").append(this.Lf).append(this.Lf == 1 ? " pillar" : " pillars");
      }

      if (this.g4 > 0) {
         var10.append(", bought ").append(this.g4).append(" blocks for ").append(wh.y(this.f));
      }

      if (this.kN > 0) {
         var10.append(", ").append(this.kN).append(this.kN == 1 ? " restock trip" : " restock trips");
      }

      if (var2 > 0) {
         var10.append(", ").append(var2).append(" still missing");
      }

      if (this.km > 0) {
         var10.append(", ").append(this.km).append(" would fall with nothing below them");
      }

      if (!var5.isEmpty()) {
         ArrayList var11 = new ArrayList();

         for (Entry var9 : var5.entrySet()) {
            var11.add(var9.getValue() + " " + (String)var9.getKey());
         }

         var10.append(" (").append(String.join(", ", var11)).append(')');
      }

      if (var3 > 0) {
         var10.append(", ").append(var3).append(" wrong blocks left as they were");
      }

      this.mY = "Last run: " + this.gv + " placed" + (var2 > 0 ? ", " + var2 + " missing" : "");
      this.N(var10 + ".");
      q8.G(this.getName(), "Done: {}", var10);
      if (this.mS.getValue()) {
         wt.T()
            .k(
               "Auto Builder",
               (this.mV == null ? "Build" : this.mV.D()) + " done in " + var4 + (var2 > 0 ? ", " + var2 + " missing" : ""),
               var2 > 0 ? wt.s.a : wt.s.C,
               8000L
            );
      }

      this.Lg = (boolean)0;
      this.disable();
   }

   private boolean s() {
      if (this.gS == this.kL) {
         return true;
      }

      this.gS = this.kL;
      return false;
   }

   @Override
   protected void aimAt(class_243 var1, float var2) {
      if (this.isEnabled()) {
         if (cr.a(this) && !this.J()) {
            if (!this.s()) {
               float[] var3 = t9.X(mc.field_1724.method_33571(), var1);
               float[] var4 = this.go.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var3[0], var3[1], false);
               t9.O(this, t9.v.e, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.J()) {
            if (!this.s()) {
               int var3 = Math.hypot(class_3532.method_15393(var1 - mc.field_1724.method_36454()), var2 - mc.field_1724.method_36455()) < 1.6F ? 1 : 0;
               float[] var4 = this.go.I(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var1, var2, (boolean)var3, true);
               t9.O(this, t9.v.e, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   private void P(class_243 var1) {
      if (cr.a(this) && !this.J()) {
         if (!this.s()) {
            class_243 var2 = mc.field_1724.method_33571();
            class_243 var3 = var1.method_1031(0.0, 0.6 - this.LP + 0.12 * Math.sin(mc.field_1724.field_6012 * 0.09 + this.kM), 0.0);
            if (this.L0 != null
               && this.L1.M().size() <= 2
               && var2.method_1025(class_243.method_24953(this.L0)) <= 64.0
               && sh.j(this.L1.g(), class_243.method_24953(this.L0).method_1020(var2))) {
               var3 = class_243.method_24953(this.L0);
            }

            float[] var4 = t9.X(var2, var3);
            float[] var5 = this.go.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var4[0], var4[1], false);
            t9.O(this, t9.v.e, var5[0], var5[1], 1.0);
         }
      }
   }

   private boolean B() {
      return this.mD.getValue().equals("Aggressive");
   }

   public boolean m() {
      return !this.isEnabled()
         ? false
         : this.B()
            && this.Lg
            && this.isRuntimeActive()
            && mc.field_1724 != null
            && mc.field_1724 == this.Lz
            && !this.L1.G()
            && this.LM == null
            && this.qF == null
            && this.m7 == null
            && this.my == null
            && this.LW == null
            && (this.m3 != null || mc.field_1724.field_6012 - this.Lt < 10)
            && !this.I();
   }

   private boolean J() {
      if (!this.B()) {
         return false;
      }

      wp.z var1 = wp.o();
      return var1 == wp.z.u || var1 == wp.z.M && !this.m();
   }

   public gn F() {
      return this.g9;
   }

   public double M() {
      return 35.0;
   }

   public static class_10185 e() {
      if (mm != null && mm.isEnabled()) {
         s0 var0 = mm;
         class_310 var1 = class_310.method_1551();
         int var2 = wp.o() == wp.z.M ? 1 : 0;
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.Lg
            && var1.field_1724 != null
            && var1.field_1724 == var0.Lz
            && var1.field_1724.field_6012 == var0.z
            && !wp.d()
            && (!ff.e() || var2 != 0)
            && !(var1.field_1755 instanceof class_465)
            && (!t9.I() || t9.k(t9.v.e))) {
            int var3 = var0.kv.method_1027() > 1.0E-6 ? 1 : 0;
            if (var2 != 0 && var3 != 0) {
               return null;
            }

            if (var3 == 0 && !var0.kx && !var0.Lh) {
               return null;
            }

            class_10185 var4 = var3 != 0
               ? var0.go.V(var1.field_1724.method_36454(), var0.kv)
               : new class_10185(false, false, false, false, false, false, false);
            return new class_10185(var4.comp_3159(), var4.comp_3160(), var4.comp_3161(), var4.comp_3162(), var0.kx, var0.Lh, var0.kZ);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void N(String var1) {
      if (mc != null && mc.field_1724 != null) {
         mc.field_1724.method_7353(class_2561.method_43470("Larp Auto Builder: " + var1), false);
      }
   }

   private void F(String var1) {
      if (!var1.equals(this.kp)) {
         this.kp = var1;
         this.N(var1 + ".");
      }
   }

   private void x(String var1) {
      q8.G(this.getName(), "Stopped: {}", var1);
      if (this.l != null && mc.field_1687 != null && this.l.n <= 200000) {
         this.w(this.l, this.l.n);
         this.N(this.l);
      }

      this.N("stopped, " + var1 + ".");
      wt.T().U("Auto Builder stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), wt.s.a);
      this.m("Stopped: " + var1);
      this.mY = "Stopped: " + var1;
      this.Lg = (boolean)0;
      this.disable();
   }

   private int M() {
      s0.j var1 = this.l;
      if (var1 != null && this.E > 0) {
         int var2 = var1.l(uo.t.L);
         return Math.clamp(Math.round(100.0 * (this.E - Math.min(this.E, var2)) / this.E), 0, 100);
      } else {
         return this.Lg ? 0 : 100;
      }
   }

   private String R() {
      return this.ki ? this.E() : this.M() + "% done, " + this.gv + " placed, " + sh.w(System.currentTimeMillis() - this.d);
   }

   private void a() {
      if (this.isInGame()) {
         if (this.LY.getValue()) {
            this.A();
         }

         if (this.gb.getValue()) {
            s0.j var1 = this.l;
            if (var1 != null && var1.F == mc.field_1687) {
               try {
                  int var2 = ez.V(0.25);
                  class_12180.method_75541(
                     new class_238(var1.S, var1.X, var1.N, var1.E + 1, var1.D + 1, var1.M + 1), class_12179.method_75536(ei.D(var2, 0.8), 1.5F)
                  );
                  n.v var3 = var1.A;
                  class_12180.method_75541(
                        new class_238(var3.originX(), var3.originY(), var3.originZ(), var3.originX() + 1, var3.originY() + 1, var3.originZ() + 1)
                           .method_1014(0.04),
                        class_12179.method_75537(-14249, 2.0F, 1090504791)
                     )
                     .method_75533();
                  boolean var4 = this.gx.getValue();

                  for (int var8 : var1.p) {
                     if (var8 < var1.n) {
                        uo.t var9 = var1.M(var8);

                        int var10 = switch (var9) {
                           case L -> var2;
                           case D -> -45747;
                           case m -> -24765;
                           case V -> var4 ? -11870592 : 0;
                           case v -> 0;
                        };
                        if (var10 != 0) {
                           class_2338 var11 = var1.P(var8);
                           float var12 = var9 == uo.t.L ? 0.12F : (var9 == uo.t.V ? 0.08F : 0.3F);
                           class_12180.method_75541(
                              new class_238(var11).method_1011(0.03), class_12179.method_75537(ei.D(var10, 0.55), 1.0F, ei.D(var10, var12))
                           );
                        }
                     }
                  }

                  if (this.Lg && this.C.getValue()) {
                     l1.i var14 = this.m3;
                     if (var14 != null) {
                        class_12180.method_75541(
                              new class_238(new class_2338(var14.x(), var14.y(), var14.z())).method_1014(0.01),
                              class_12179.method_75537(ei.D(var2, 1.0), 2.0F, ei.D(var2, 0.25))
                           )
                           .method_75533();
                     }

                     if (this.L1.G()) {
                        class_243 var15 = mc.field_1724.method_73189().method_1031(0.0, 0.05, 0.0);

                        for (g8.p var17 : this.L1.M()) {
                           class_243 var18 = new class_243(var17.x() + 0.5, var17.y() + 0.05, var17.z() + 0.5);
                           class_12180.method_75546(var15, var18, ei.D(var2, 0.85), 2.0F).method_75533();
                           var15 = var18;
                        }
                     }
                  }
               } catch (RuntimeException var13) {
                  q8.O(this.getName(), "Preview failed", var13);
               }
            }
         }
      }
   }

   private void A() {
      List var1 = T(this.O.getValue());
      if (!var1.isEmpty()) {
         try {
            String var2 = this.b();
            double var3 = Math.max(32.0, this.mw.getValue());

            for (s0.s var6 : var1) {
               if (var6.dimension().equals(var2) && !(mc.field_1724.method_5707(class_243.method_24953(var6.pos())) > var3 * var3)) {
                  int var7 = this.T != null && this.T.equals(var6) ? 1 : 0;
                  int var8 = var7 != 0 ? -11870592 : -10443270;
                  class_12180.method_75541(new class_238(var6.pos()).method_1014(0.02), class_12179.method_75537(ei.D(var8, 0.9), 2.0F, ei.D(var8, 0.12)))
                     .method_75533();
               }
            }
         } catch (RuntimeException var9) {
            q8.O(this.getName(), "Restock outlines failed", var9);
         }
      }
   }

   @Override
   public tc.g hudCard() {
      if (this.isInGame() && this.q.getValue() && this.Lg && this.isRuntimeActive() && mc.field_1687 == this.gP) {
         s0.j var1 = this.l;
         int var2 = var1 != null && var1.k ? 1 : 0;
         String var3 = Li.matcher(this.m.getValue().trim()).replaceFirst("");
         String var4 = this.getStatus();
         return this.ki
            ? new tc.g(
               "Stocking " + (var3.isEmpty() ? "materials" : var3), "", var4 != null && !var4.isBlank() ? var4 : "Starting", y(this.kO) + " put away", -1.0
            )
            : new tc.g(
               var3.isEmpty() ? "Schematic" : var3,
               var2 != 0 ? this.M() + "%" : "",
               var4 != null && !var4.isBlank() ? var4 : "Starting",
               this.gv + " placed" + (var2 != 0 ? ", " + var1.l(uo.t.L) + " missing" : ""),
               var2 != 0 ? this.M() / 100.0 : -1.0
            );
      } else {
         return null;
      }
   }

   @Override
   public lc preview() {
      return new ln(this);
   }

   n C() {
      return this.Lp ? null : this.mV;
   }

   String k() {
      String var1 = this.m.getValue().trim();
      if (this.q7) {
         return "Loading " + var1;
      } else {
         return this.kk != null ? "Could not load " + (var1.isEmpty() ? "the schematic" : var1) + ": " + this.kk : "No schematic loaded";
      }
   }

   public boolean U() {
      return this.Lg;
   }

   public int J() {
      return this.gv;
   }

   public n D() {
      return this.mV;
   }

   public String N() {
      return this.kk;
   }

   public boolean k() {
      return this.q7;
   }

   public int[] H() {
      s0.j var1 = this.l;
      return var1 != null && var1.k ? (int[])var1.c.clone() : null;
   }

   enum a {
      u,
      X,
      r,
      K,
      m,
      Q,
      h;
   }

   private record c(String file, n schematic, String error) {
   }

   enum g {
      N,
      C,
      h;
   }

   static final class j {
      final n Z;
      final n.v A;
      final int n;
      final long[] G;
      final int[] K;
      final int[] q;
      final int[] m;
      final class_2680[] C;
      final String[] t;
      final boolean[] Q;
      final Map<Integer, int[]> l = new HashMap<>();
      final byte[] r;
      final int S;
      final int X;
      final int N;
      final int E;
      final int D;
      final int M;
      int O;
      final int[] c = new int[uo.t.values().length];
      final int[] b = new int[uo.t.values().length];
      boolean k;
      int[] p = new int[0];
      final PriorityQueue<long[]> j = new PriorityQueue<>((var0, var1x) -> Long.compare(var1x[0], var0[0]));
      Object F;
      private Map<Long, Integer> f;
      private Map<Long, Integer> z;

      j(n var1, n.v var2) {
         this.Z = var1;
         this.A = var2;
         this.n = var1.I();
         this.G = new long[this.n];
         this.K = new int[this.n];
         this.r = new byte[this.n];
         class_2680[] var3 = new class_2680[var1.a().size()];
         this.t = new String[var3.length];
         this.Q = new boolean[var3.length];

         for (int var4 = 0; var4 < var3.length; var4++) {
            class_2680 var5 = uo.B(var1.a().get(var4));
            var3[var4] = var5 == null ? null : uo.q(var5, var2);
            this.t[var4] = var5 == null ? "unknown block" : uo.g(var3[var4]);
            this.Q[var4] = (boolean)(var5 != null && uo.X(var3[var4]) ? 1 : 0);
         }

         this.C = var3;
         int[] var10 = var2.bounds(var1);
         this.S = var10[0];
         this.X = var10[1];
         this.N = var10[2];
         this.E = var10[3];
         this.D = var10[4];
         this.M = var10[5];
         int var11 = this.D - this.X + 1;
         this.m = new int[var11 + 1];

         for (int var6 = 0; var6 < this.n; var6++) {
            int[] var7 = var2.world(var1.s(var6), var1.r(var6), var1.F(var6));
            this.G[var6] = class_2338.method_10064(var7[0], var7[1], var7[2]);
            this.K[var6] = var1.D(var6);
            this.r[var6] = (byte)uo.t.v.ordinal();
            this.m[var7[1] - this.X + 1]++;
            class_2680 var8 = var3[this.K[var6]];
            if (var8 != null && var8.method_26204() instanceof class_8886) {
               int[] var9 = var1.w(var6);
               if (var9.length > 0) {
                  this.l.put(var6, var9);
               }
            }
         }

         for (int var12 = 1; var12 <= var11; var12++) {
            this.m[var12] = this.m[var12] + this.m[var12 - 1];
         }

         this.q = new int[this.n];
         int[] var13 = Arrays.copyOf(this.m, var11);
         int var14 = 0;

         while (var14 < this.n) {
            this.q[var13[class_2338.method_10071(this.G[var14]) - this.X]++] = var14++;
         }
      }

      boolean Y(int var1, int var2, int var3) {
         if (var1 >= this.S && var1 <= this.E && var2 >= this.X && var2 <= this.D && var3 >= this.N && var3 <= this.M) {
            if (this.f == null) {
               HashMap var4 = new HashMap(Math.max(16, this.n * 2));

               for (int var5 = 0; var5 < this.n; var5++) {
                  var4.put(this.G[var5], var5);
               }

               this.f = var4;
            }

            return this.f.containsKey(class_2338.method_10064(var1, var2, var3));
         } else {
            return false;
         }
      }

      boolean n(int var1, int var2, int var3) {
         int var4 = this.K(var1, var2, var3);
         return var4 >= 0 && this.q(var4) != null && this.t[this.K[var4]] == null;
      }

      int K(int var1, int var2, int var3) {
         return !this.Y(var1, var2, var3) ? -1 : this.f.get(class_2338.method_10064(var1, var2, var3));
      }

      int M(int var1, int var2, int var3) {
         if (this.z == null) {
            HashMap var4 = new HashMap(Math.max(16, this.n * 2));

            for (int var5 = 0; var5 < this.n; var5++) {
               var4.put(com.larpclient.g8.M(this.Z.s(var5), this.Z.r(var5), this.Z.F(var5)), var5);
            }

            this.z = var4;
         }

         return this.z.getOrDefault(com.larpclient.g8.M(var1, var2, var3), -1);
      }

      class_2680 q(int var1) {
         return this.C[this.K[var1]];
      }

      boolean h(int var1) {
         return this.Q[this.K[var1]];
      }

      class_2338 P(int var1) {
         return class_2338.method_10092(this.G[var1]);
      }

      uo.t M(int var1) {
         return uo.t.values()[this.r[var1]];
      }

      int l(uo.t var1) {
         return this.c[var1.ordinal()];
      }

      fx.o V() {
         return new fx.o(this.S, this.X, this.N, this.E, this.D, this.M);
      }
   }

   enum l {
      s,
      D,
      M;
   }

   enum o {
      t,
      w,
      e;
   }

   enum p {
      i,
      U,
      Z,
      s;
   }

   record s(String dimension, int x, int y, int z) {
      class_2338 pos() {
         return new class_2338(this.x, this.y, this.z);
      }

      @Override
      public String toString() {
         return this.dimension + " " + this.x + " " + this.y + " " + this.z;
      }
   }

   private enum u {
      N,
      G,
      r;
   }
}
