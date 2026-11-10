package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.aj;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_10185;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1540;
import net.minecraft.class_1542;
import net.minecraft.class_1661;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2346;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2682;
import net.minecraft.class_310;
import net.minecraft.class_3486;
import net.minecraft.class_3489;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_408;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_490;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_9334;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class sh extends s4 implements tc, fk.h {
   private static sh ih;
   private static boolean wL;
   static final int i_ = 400;
   static final int wZ = 3;
   static final int wg = 2400;
   static final int iI = 600;
   static final int HQ = 3;
   static final int f = 40;
   private static final int i7 = 8;
   private static final int HC = 20000;
   private static final int wJ = 2;
   static final int n1 = -1000000000;
   static final int wh = 200;
   static final int H5 = 60;
   private final aj wY = this.addSetting(new aj("Corner 1", "First corner as x y z, or use Pick with camera", "").A("x y z"));
   private final aj Hw = this.addSetting(new aj("Corner 2", "Second corner as x y z, or use Pick with camera", "").A("x y z"));
   private final aj F = this.addSetting(
      new aj("Drill Name", "Part of your 3x3 drill's name, not case sensitive. The drill cuts 3x3 faces and a plain pickaxe cleans up the rest", "Drill")
         .A("Drill")
   );
   private final com.larpclient.module.lo wy = this.Z(
      "Layer Height", "How tall each layer is. The area is cleared from the top down, one layer at a time", 3.0, 1.0, 6.0, 1.0
   );
   private final com.larpclient.module.lo nu = this.Z("Pass Width", "How wide each row of a layer is", 3.0, 1.0, 5.0, 1.0);
   private final com.larpclient.module.lo it = this.Z("Reach", "How far the miner reaches, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final com.larpclient.module.l3 iw = this.G(
      "Sweep Leftovers", "After the drill finishes a layer, mine every block still standing before moving down", true
   );
   private final com.larpclient.module.l3 H2 = this.G("Sprint", "Sprint while walking", true);
   private final com.larpclient.module.l3 U = this.G("Place Blocks", "Pillar up, bridge gaps and plug lava or water with solid blocks from your hotbar", true);
   private final com.larpclient.module.l3 ij = this.G("Auto Eat", "Eat food at 10 hunger or less, and a golden apple after losing the amount below", true);
   private final com.larpclient.module.lo HI = this.Z(
      "Gapple After Losing", "Hunger bars lost before a golden apple from your hotbar is eaten", 2.0, 1.0, 10.0, 1.0
   );
   private final com.larpclient.module.l3 wM = this.G("XP Repair", "When your tool runs low, throw XP bottles from your hotbar to mend it", true);
   private final com.larpclient.module.lo wI = this.Z(
      "Repair At", "Uses left on the tool that starts XP repair, or stops the run if it cannot be mended", 50.0, 1.0, 1000.0, 1.0
   );
   private final com.larpclient.module.l3 iq = this.G("Show Area", "Draw the selected area and the block being mined", true);
   private final com.larpclient.module.l3 we = this.G("Show Path", "Draw the route the miner is walking", true);
   private final com.larpclient.module.l3 wq = this.G(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final com.larpclient.module.l3 iF = this.G("Notify When Done", "Show a notification when the area is cleared", true);
   private final com.larpclient.module.l3 w7 = this.G(
      "Show Progress", "Show a small card at the top of the screen with the layer, progress and what the miner is doing", true
   );
   private final com.larpclient.module.l3 n7 = this.G("Collect Drops", "After the last layer, walk over the items lying in the area to pick them up", true);
   private final com.larpclient.module.lw ib = this.addSetting(
      new com.larpclient.module.lw(
         "Select Area Key", "Press to pick the corners with a free camera: left click sets one corner, right click the other, Esc finishes", -1
      )
   );
   private final com.larpclient.module.l3 wl = this.G(
      "Auto Tool",
      "Take the right hotbar tool for each block: a shovel for dirt, sand and gravel, an axe for wood, shears or a hoe for leaves. The drill still cuts its 3x3 faces. Off mines everything with your pickaxe",
      true
   );
   private final com.larpclient.module.li wP = this.addSetting(
      new com.larpclient.module.li(
         "Drill Cut",
         "Auto watches what each drill hit breaks and uses 3x3 faces once it sees one. 3x3 trusts your drill from the first hit; One Block uses it like a normal pickaxe",
         "Auto",
         "Auto",
         "3x3",
         "One Block"
      )
   );
   private final com.larpclient.module.l3 i0 = this.G(
      "Crouch Sometimes",
      "Crouch where players do: while cutting the step down to the next layer and beside a drop, and now and then while mining with the pickaxe. Never for drill hits, which need you standing",
      true
   );
   private final com.larpclient.module.lo na = this.Z(
      "Camera Speed",
      "How fast the free camera flies while you pick corners, in blocks per second. The mouse wheel changes it until you finish. Your normal Freecam speed stays as it is",
      6.0,
      1.0,
      40.0,
      1.0
   );
   private fx HU;
   private Object He;
   private Object nB;
   private int Hz;
   private boolean H1;
   private boolean in;
   private sh.e nD = sh.e.R;
   private final List<class_2338> j = new ArrayList<>();
   private final List<class_2338> wT = new ArrayList<>();
   private int ie = -1000000000;
   private int n4;
   private final Map<class_2338, Integer> wt = new HashMap<>();
   private final Map<class_2338, Integer> M = new HashMap<>();
   private final Map<class_2338, Integer> r = new HashMap<>();
   private final Map<class_2338, Integer> L = new HashMap<>();
   private final Map<class_2338, class_2680> wQ = new HashMap<>();
   private final Map<class_2338, String> k = new HashMap<>();
   private int wG;
   private int wn;
   private final Map<class_2338, Integer> wH = new HashMap<>();
   private static final int nX = 200;
   private final int[] nt = new int[5];
   private final int[] iC = new int[5];
   private final Map<class_2338, Integer> iz = new HashMap<>();
   private int iS;
   private final Set<class_2338> nc = new HashSet<>();
   private final Set<Long> wB = new HashSet<>();
   private int iB;
   private int I;
   private class_2338 Y;
   private class_2338 iZ;
   private class_2680 iW;
   private final Map<class_2338, class_2680> iU = new HashMap<>();
   private boolean iK;
   private class_2338 no;
   private int im;
   private Boolean Z;
   private class_2338 H7;
   private final Map<class_2338, class_2680> w5 = new HashMap<>();
   private int Hl;
   private final Set<class_2338> w_ = new HashSet<>();
   static final int ww = 10;
   private List<bg.o> D = List.of();
   private int iu;
   private int X;
   private int Hn;
   private int wp;
   private class_243 T;
   private List<bg.o> nk = List.of();
   private class_2338 wj;
   private double H3;
   private double nE;
   private double z = 0.245;
   private double E = 0.6;
   private final SplittableRandom iV = new SplittableRandom();
   private class_2338 i1;
   private int n3;
   private class_2338 HR;
   static final double w4 = 0.015;
   static final int Hd = 20;
   static final int m = 50;
   private List<wy.s> wC = List.of();
   private List<wy.s> Hf = List.of();
   private List<wy.s> np = List.of();
   private List<wy.s> i9;
   private final Set<wy.s> HV = new HashSet<>();
   private List<wy.s> HX = List.of();
   private int HW = -1;
   private int n5 = -1000000000;
   private int nP = -1;
   private int iQ = -1000000000;
   private int Q;
   private final Map<Long, Integer> w6 = new HashMap<>();
   private int nK = -1;
   private int iX;
   private int Hq;
   private int Hh = -1;
   private int HO = -1000000000;
   private int il = -1000000000;
   private int iO;
   private int J = -1;
   private int wA = -1000000000;
   private boolean Hu;
   private boolean Hg;
   private boolean HJ;
   private final Set<class_2338> v = new HashSet<>();
   private int is = -1;
   private class_2338 HH;
   private class_2350 HL;
   private boolean nU;
   private long V = Long.MIN_VALUE;
   private float Ha;
   private float Ht;
   static final float i2 = 2.5F;
   static final float nC = -2.0F;
   static final float N = 6.0F;
   static final float wD = 25.0F;
   static final double S = 0.12;
   static final int g = 3;
   static final int O = 5;
   private final j9 HM = new j9();
   private final Set<class_2338> nf = new HashSet<>();
   static final int iL = 60;
   private boolean H8;
   private int nd;
   private int nY;
   private float HG;
   private float d;
   private class_2338 Hj;
   private int HP = -1000000000;
   private int R;
   private double ws;
   static final double wo = 0.035;
   static final int i5 = 80;
   static final double u = 6.0;
   private final Set<class_2338> n = new HashSet<>();
   private class_2338 ny;
   private int b = -1;
   private boolean ii;
   private int Hc;
   private class_243 ic;
   private double Hm;
   static final int iP = 30;
   private class_243 iT = class_243.field_1353;
   private boolean HE;
   private boolean i4;
   private boolean wx;
   private int A = Integer.MIN_VALUE;
   private int iR = -1;
   private final Set<class_1792> Hi = new HashSet<>();
   private class_2338 wu;
   private class_2338 wV;
   private boolean c;
   private int wm = -1;
   private int HN;
   static final int Ho = 3;
   private final Set<class_2338> x = new HashSet<>();
   private boolean Hx;
   private boolean iE;
   private boolean ig;
   private boolean w1;
   private class_2338 wc;
   private final boolean[] iy = new boolean[2];
   private class_437 H_;
   static final double nn = 128.0;
   private boolean Hr;
   private int a;
   private int w2;
   private final Set<Integer> i3 = new HashSet<>();
   static final int nj = 1200;
   private int wi;
   private int w8;
   private boolean B;
   private final u5 iN = new u5(u5.p.QUICK);
   private final tn iJ = new tn(this);
   private String wS;
   private String HY;
   private String wE;
   private int HF = -1;
   private boolean nM;
   private boolean Hs;
   private final Set<class_2338> ix = new HashSet<>();
   private int nO = -1;
   private boolean nT;
   private fx HZ;
   private int ik;
   static final int q = 20000;
   private int nq;
   private final Set<class_2338> H0 = new HashSet<>();
   static final int nL = 3;
   private int wR = -1;
   private int HS = -1;
   private int wd = -1000000000;
   private long w;
   private long wk;
   static final int wF = 256;
   static final int wb = 200000;
   static final long i6 = 15000L;
   private Set<class_2338> HB;
   private Set<class_2338> iA;
   private String nI = "";
   private String nH = "";
   private final e3 w0 = new e3(this, this.iN, var1 -> this.aimAt(var1, 12.0F));
   private int H4 = -1000000000;
   private int iG;
   private final List<class_2338> i = new ArrayList<>();
   static final int wr = 8;
   static final int K = 12000;
   private int Hb;
   private int n_;
   private long wv;
   private String nJ = "";
   static final long H = 5000000L;
   static final int nl = 6;
   private wy.s h;
   private int wN = -1;
   private int p = -1;
   private int nw;
   private int l;
   private long o = Long.MIN_VALUE;
   private final Set<Long> i8 = new HashSet<>();
   static final int id = 32;
   static final int wz = 3;
   private class_2338 ns;
   private int w3 = -1;
   static final int wO = 2;
   private int iY;
   private int iH;
   private String wa;
   private String HA;
   private int nz;
   private String Hy;
   private String io;
   private int nh;
   static final int wW = 16;
   private Set<Long> n9 = Set.of();
   private long HD = Long.MIN_VALUE;
   private int ir = Integer.MIN_VALUE;
   private int W = -1;
   private int H9 = -1000000000;
   private int wK;
   private int nW = -1;
   static final int w9 = 6;
   static final int Hk = 8;
   static final int wX = 16;
   static final int e = 2;
   static final int Hv = 6;
   static final float wU = 1.5F;
   static final int nA = 5;
   static final int nr = 3;
   private class_2338 iM;
   private class_2338 HK;
   private int nx;
   private int ip;
   private int C = -1000000000;
   private int iv;
   private int nZ;
   private final Map<class_2338, Integer> wf = new HashMap<>();
   private float Hp = 88.0F;
   private String nv;
   static final double ia = 0.18;
   static final double iD = Math.cos(Math.toRadians(70.0));
   static final double ni = 0.75;
   private final Set<class_2338> HT = new HashSet<>();
   private int H6;
   static final int ne = 10;

   private static int j(double var0) {
      return var0 < 2.0 ? 0 : (var0 < 3.0 ? 1 : (var0 < 3.5 ? 2 : (var0 < 4.0 ? 3 : 4)));
   }

   public sh() {
      super("Auto Area Miner", "Clear the box between two corners, top layer first, with your 3x3 drill and a pickaxe");
      ih = this;
      this.HI.O(this.ij::getValue);
      this.wI.O(this.wM::getValue);
      this.HI.M("Golden Apple After");
      this.wI.M("Mend Tool Below");
      this.wq.M("Pause In Other Menus");
      this.wY.M("Corner 1 (x y z)");
      this.Hw.M("Corner 2 (x y z)");
      this.wY.a(var1 -> this.wk = System.currentTimeMillis() + 15000L);
      this.Hw.a(var1 -> this.wk = System.currentTimeMillis() + 15000L);
      if (!wL) {
         wL = (boolean)1;
         WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var0 -> {
            sh var1 = ih;
            if (var1 != null) {
               var1.J();
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            sh var1 = ih;
            if (var1 != null) {
               var1.a();
            }
         });
      }
   }

   public static sh a() {
      return ih;
   }

   public static String w(Collection<class_2338> var0, String var1) {
      sh var2 = ih;
      if (var2 == null) {
         return "Auto Area Miner is not available in this edition.";
      }

      if (var2.isEnabled()) {
         return "Auto Area Miner is already on; turn it off first.";
      }

      String var3 = tc.q(var2);
      if (var3 != null) {
         return "Turn off " + var3 + " first; one bot runs at a time.";
      }

      if (var0 != null && !var0.isEmpty()) {
         HashSet var4 = new HashSet(var0.size() * 2);

         for (class_2338 var6 : var0) {
            var4.add(var6.method_10062());
         }

         var2.wS = null;
         var2.iA = var4;
         var2.nH = var1 == null ? "" : var1;
         var2.enable();
         return null;
      } else {
         return "There is nothing to mine.";
      }
   }

   public static boolean T(String var0) {
      sh var1 = ih;
      if (var1 != null && var1.isRuntimeActive() && var1.HB != null && !var1.nT) {
         q8.G(var1.getName(), "Mine Out stopped: {}", var0);
         var1.L("stopped mining out " + var1.nI + ": " + var0 + ".");
         var1.disable();
         return true;
      } else {
         return false;
      }
   }

   public static boolean N() {
      sh var0 = ih;
      return var0 != null && var0.isRuntimeActive() && var0.HB != null && !var0.nT;
   }

   public static int S() {
      sh var0 = ih;
      return var0 != null && var0.HB != null && !var0.nT ? Math.max(0, var0.HS) : -1;
   }

   @Override
   public void disable() {
      this.wS = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      class_437 var1 = getMc() == null ? null : mc.field_1755;
      return var1 == null || var1 instanceof class_408 || var1 instanceof class_433 || !this.wq.getValue();
   }

   @Override
   public List<tc.k> commands() {
      return List.of(
         new tc.k("Area", "Select the area with a free camera", this::l),
         new tc.k("Area", "Show the area", this::N),
         new tc.k("Area", "Clear corners", this::g),
         new tc.k("Drill", "Use the item in your hand", this::L),
         new tc.k("Drill", "Pick it from your inventory", this::p),
         new tc.k("Drill", "No drill, pickaxe only", this::h)
      );
   }

   @Override
   public String detail() {
      String var1 = this.F.getValue().trim();
      if (var1.isEmpty()) {
         return "Drill: none, your best pickaxe mines everything.";
      }

      if (!this.isInGame()) {
         return "Drill: any item named like \"" + var1 + "\".";
      }

      int var2 = this.R();
      return var2 >= 0
         ? "Drill: " + mc.field_1724.method_31548().method_5438(var2).method_7964().getString() + " (hotbar slot " + (var2 + 1) + ")."
         : "Drill: \"" + var1 + "\" is not in your hotbar, so the pickaxe mines everything.";
   }

   private String N() {
      fx.o var1 = this.s();
      if (var1 == null) {
         return "Set both corners first.";
      }

      if (!this.isInGame()) {
         return "Join a world first.";
      }

      this.wk = System.currentTimeMillis() + 15000L;
      return "Showing " + var1.describe() + " for 15 seconds.";
   }

   private String l() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      String var1 = this.B();
      return var1 != null ? var1 : "Fly around and aim: left click sets corner 1, right click sets corner 2, Esc finishes.";
   }

   private String L() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      class_1799 var1 = mc.field_1724.method_6047();
      return var1.method_7960() ? "Hold your drill first." : this.Z(var1);
   }

   private String p() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the miner off before changing the drill.";
      }

      this.w1 = (boolean)1;
      this.H_ = mc.field_1755;
      mc.method_1507(new class_490(mc.field_1724));
      q8.v(this.getName(), "Picking the drill from the inventory");
      this.L("click your drill in your inventory. Nothing moves; Esc cancels.");
      return "Click your drill in your inventory. Nothing moves; Esc cancels.";
   }

   private String h() {
      this.F.Y("");
      this.S();
      return "No drill: your best pickaxe mines everything.";
   }

   private String Z(class_1799 var1) {
      String var2 = var1.method_7964().getString().trim();
      if (var2.length() > 128) {
         var2 = var2.substring(0, 128);
      }

      this.F.Y(var2);
      this.S();
      q8.G(this.getName(), "Drill set to {}", var2);
      return "Drill set to " + var2 + ".";
   }

   public static boolean R(class_1799 var0) {
      sh var1 = ih;
      if (var1 != null && var1.w1 && var0 != null && !var0.method_7960()) {
         var1.w1 = (boolean)0;
         String var2 = var1.Z(var0);
         var1.L(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         class_437 var3 = var1.H_;
         var1.H_ = null;
         getMc().method_1507(var3);
         return true;
      } else {
         return false;
      }
   }

   public static boolean p() {
      sh var0 = ih;
      return var0 != null && var0.w1;
   }

   private String g() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before clearing the corners.";
      }

      this.wY.Y("");
      this.Hw.Y("");
      this.wk = 0L;
      this.S();
      return "Corners cleared.";
   }

   private void S() {
      LarpClient var1 = LarpClient.b();
      if (var1 != null && var1.e() != null) {
         var1.e().U();
      }
   }

   @Override
   public String summary() {
      if (this.HB != null && this.HU != null && this.isRuntimeActive()) {
         return "Mining out " + this.nI + ": " + this.HU.X().describe() + ". " + this.U() + ".";
      }

      fx.o var1 = this.s();
      if (var1 == null) {
         return "Set both corners to choose the area.";
      }

      String var2 = "Area: " + var1.describe();
      return this.isRuntimeActive() && this.HU != null ? var2 + ". " + this.U() + "." : var2 + ". Stand on top of it and turn the miner on.";
   }

   @Override
   public List<tc.v> settingGroups() {
      return List.of(
         new tc.v("Area", List.of(this.na, this.ib))
            .with(
               new tc.l(
                  this::a,
                  List.of(
                     List.of(
                        this.g("Pick with camera", "Select the area with a free camera"),
                        this.g("Show", "Show the area").availableWhen(() -> this.s() != null),
                        this.g("Clear", "Clear corners").availableWhen(() -> this.s() != null)
                     )
                  )
               )
            ),
         new tc.v("Drill", List.of(this.F, this.wP))
            .with(
               new tc.l(
                  this::G,
                  List.of(
                     List.of(
                        this.g("Held item", "Use the item in your hand"),
                        this.g("From inventory", "Pick it from your inventory"),
                        this.g("No drill", "No drill, pickaxe only")
                     )
                  )
               )
            ),
         new tc.v("While mining", List.of(this.wl, this.U, this.n7, this.H2, this.i0)),
         new tc.v("Food and repair", List.of(this.ij, this.HI, this.wM, this.wI)),
         new tc.v("Display", List.of(this.iq, this.we, this.w7, this.iF)),
         tc.v.advanced("Advanced", List.of(this.wY, this.Hw, this.wy, this.nu, this.it, this.iw, this.wq))
      );
   }

   private String a() {
      fx.o var1 = this.s();
      return var1 == null ? "Not set. Pick two opposite corners." : var1.describe() + (e(var1) != null ? ", too wide to mine" : "");
   }

   private String G() {
      if (!this.isInGame()) {
         return this.detail();
      }

      int var1 = this.i();
      return this.detail()
         + (var1 >= 0 ? " Pickaxe: " + mc.field_1724.method_31548().method_5438(var1).method_7964().getString() + "." : " No pickaxe in your hotbar.");
   }

   @Override
   public tc.b status() {
      if (this.isRuntimeActive()) {
         return new tc.b(this.HU == null ? "Starting." : (this.HB != null ? "Mining out " + this.nI + ". " : "") + this.U() + ".", false);
      }

      fx.o var1 = this.s();
      if (var1 == null) {
         return new tc.b("Set the area, then turn the module on.", false);
      }

      if (e(var1) != null) {
         return new tc.b("Each side across can be at most 256 blocks. Pick the corners again.", true);
      }

      if (!this.isInGame()) {
         return new tc.b("Area set. Join the world it is in.", false);
      }

      String var2 = tc.q(this);
      if (var2 != null) {
         return new tc.b(var2 + " is running. One bot runs at a time.", true);
      }

      class_2338 var3 = this.X();
      int var4 = Math.max(
         Math.max(var1.minX() - var3.method_10263(), var3.method_10263() - var1.maxX()),
         Math.max(var1.minZ() - var3.method_10260(), var3.method_10260() - var1.maxZ())
      );
      if (var4 > 16) {
         return new tc.b("You are " + var4 + " blocks from the area. Stand on it or next to it.", true);
      }

      if (var3.method_10264() < var1.minY()) {
         return new tc.b("You are below the area. Stand on top of it or inside it.", true);
      }

      int var5 = this.i();
      int var6 = this.R();
      if (var5 < 0 && var6 < 0) {
         return new tc.b("Put a pickaxe or your drill in your hotbar.", true);
      }

      int var7 = (int)Math.ceil(var1.sizeY() / this.wy.getValue());
      ArrayList var8 = new ArrayList();

      for (int var12 : new int[]{var6, var5}) {
         if (var12 >= 0) {
            var8.add(mc.field_1724.method_31548().method_5438(var12).method_7964().getString());
         }
      }

      StringBuilder var13 = new StringBuilder(
         "Will clear " + var1.describe() + " in " + var7 + (var7 == 1 ? " layer" : " layers") + " with " + String.join(" and ", var8) + "."
      );
      if (this.U.getValue() && this.g() < 0) {
         var13.append(" No blocks in your hotbar for bridging or plugging lava.");
      }

      if (this.ij.getValue() && tn.h(var0 -> var0.method_58694(class_9334.field_50075) != null) < 0) {
         var13.append(" No food in your hotbar.");
      }

      return new tc.b(var13.toString(), false);
   }

   private fx.o s() {
      int[] var1 = fx.C(this.wY.getValue());
      int[] var2 = fx.C(this.Hw.getValue());
      return var1 != null && var2 != null ? fx.o.of(var1, var2) : null;
   }

   private String B() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before choosing a new area.";
      }

      if (this.Hx) {
         return null;
      }

      this.Hx = (boolean)1;
      this.iy[0] = this.iy[1] = (boolean)0;
      this.wc = null;
      mc.method_1507(null);
      FreecamModule var1 = this.y();
      if (var1 != null && (var1.isEnabled() || !var1.D())) {
         var1.k(this.na.getValue());
         if (!var1.isEnabled()) {
            var1.setEnabled(true);
            this.iE = var1.isEnabled();
         }

         if (!var1.isEnabled()) {
            var1.k();
         }
      }

      int var2 = var1 != null && var1.isEnabled() ? 1 : 0;
      q8.G(this.getName(), "Picking corners{}", var2 != 0 ? " with Freecam at " + var1.R() + " blocks/s" : " from your own view");
      if (var2 != 0) {
         this.L("fly with the movement keys at " + this.na.getValue().intValue() + " blocks a second; the mouse wheel changes it. Esc finishes.");
      }

      return null;
   }

   private FreecamModule y() {
      return LarpClient.b().G().b(FreecamModule.class);
   }

   static String e(fx.o var0) {
      return var0.sizeX() <= 256 && var0.sizeZ() <= 256
         ? null
         : String.format(
            Locale.ROOT,
            "the area is %,d x %,d x %,d blocks; each side across can be at most %d. One corner is probably left from another world or server, so pick both corners again",
            var0.sizeX(),
            var0.sizeY(),
            var0.sizeZ(),
            256
         );
   }

   private void S(String var1) {
      if (this.Hx) {
         this.Hx = (boolean)0;
         this.wc = null;
         FreecamModule var2 = this.y();
         if (var2 != null) {
            var2.k();
            if (this.iE && var2.isEnabled()) {
               var2.setEnabled(false);
            }
         }

         this.iE = (boolean)0;
         this.S();
         fx.o var3 = this.s();
         if (var3 != null) {
            this.wk = System.currentTimeMillis() + 15000L;
         }

         q8.G(
            this.getName(),
            "Corner picking ended ({}); area {}, picked corners {}{}",
            var1,
            var3 == null ? "incomplete" : var3.describe(),
            this.iy[0] ? "1" : "",
            this.iy[1] ? "2" : ""
         );
         if (this.isInGame()) {
            if (var3 == null) {
               this.L("corner picking ended; set both corners to choose the area.");
            } else if (e(var3) != null) {
               int var4 = this.iy[0] && !this.iy[1] ? 2 : (this.iy[1] && !this.iy[0] ? 1 : 0);
               this.L(
                  var4 > 0
                     ? "corner "
                        + var4
                        + " is still "
                        + (var4 == 1 ? this.wY : this.Hw).getValue()
                        + " from before, so the area is "
                        + var3.describe()
                        + ". Pick corner "
                        + var4
                        + " too ("
                        + (var4 == 1 ? "left" : "right")
                        + " click)."
                     : "the area is " + var3.describe() + "; each side across can be at most 256 blocks. Pick the corners again."
               );
            } else {
               this.L("area set: " + var3.describe() + ". Stand on top of it and turn the miner on.");
            }
         }
      }
   }

   public static boolean A() {
      sh var0 = ih;
      return var0 != null && var0.Hx;
   }

   public static void e(int var0) {
      sh var1 = ih;
      if (var1 != null && var1.Hx && var1.isInGame()) {
         class_2338 var2 = var1.i();
         if (var2 == null) {
            var1.L("aim at a block to set a corner.");
         } else {
            aj var3 = var0 == 0 ? var1.wY : var1.Hw;
            var3.Y(fx.A(var2.method_10263(), var2.method_10264(), var2.method_10260()));
            var1.iy[var0 == 0 ? 0 : 1] = (boolean)1;
            q8.v(var1.getName(), "Corner {} picked at {}", var0 + 1, var2.method_23854());
         }
      }
   }

   private class_2338 i() {
      boolean var1 = com.larpclient.wp.d();
      class_243 var2 = var1 ? com.larpclient.wp.W(1.0F) : mc.field_1724.method_33571();
      float var3 = var1 ? com.larpclient.wp.g() : mc.field_1724.method_36454();
      float var4 = var1 ? com.larpclient.wp.J() : mc.field_1724.method_36455();
      class_243 var5 = var2.method_1019(class_243.method_1030(var4, var3).method_1021(128.0));
      class_3965 var6 = mc.field_1687.method_17742(new class_3959(var2, var5, class_3960.field_17559, class_242.field_1348, mc.field_1724));
      return var6.method_17783() == class_240.field_1332 ? var6.method_17777().method_10062() : null;
   }

   private void a() {
      if (!this.isInGame()) {
         if (this.Hx) {
            this.Hx = (boolean)0;
            this.iE = (boolean)0;
            FreecamModule var3 = this.y();
            if (var3 != null) {
               var3.k();
            }
         }

         this.w1 = (boolean)0;
      } else {
         if (this.w1 && !(mc.field_1755 instanceof class_490)) {
            this.w1 = (boolean)0;
            this.H_ = null;
         }

         int var1 = this.ib.getValue() != -1 && mc.field_1755 == null && mc.method_1569() && s5.m(mc.method_22683().method_4490(), this.ib.getValue()) ? 1 : 0;
         if (var1 != 0 && !this.ig) {
            if (this.Hx) {
               this.S("key pressed");
            } else {
               String var2 = this.B();
               if (var2 != null) {
                  this.L(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }

         this.ig = (boolean)var1;
         if (this.Hx) {
            if (mc.field_1755 != null) {
               this.S("menu opened");
            } else {
               this.wc = this.i();
            }
         }
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.k();
      if (this.Hx) {
         this.S("miner turned on");
      }

      if (this.wS != null) {
         String var20 = this.wS;
         this.wS = null;
         this.Q("it stopped earlier because " + var20 + ". Turn it on again to carry on");
      } else if (this.iA != null) {
         Set var19 = this.iA;
         this.iA = null;
         this.X(var19, this.nH);
      } else {
         fx.o var1 = this.s();
         if (var1 == null) {
            this.Q("set both corners first");
         } else {
            String var2 = e(var1);
            if (var2 != null) {
               this.Q(var2);
            } else {
               String var3 = tc.q(this);
               if (var3 != null) {
                  this.Q("turn off " + var3 + " first; one bot runs at a time");
               } else {
                  class_2338 var4 = this.X();
                  double var5 = Math.max(
                     Math.max(var1.minX() - var4.method_10263(), var4.method_10263() - var1.maxX()),
                     Math.max(var1.minZ() - var4.method_10260(), var4.method_10260() - var1.maxZ())
                  );
                  if (var5 > 16.0) {
                     this.Q("stand inside or next to the area to start (you are " + (int)var5 + " blocks away)");
                  } else if (var4.method_10264() < var1.minY()) {
                     this.Q("you are below the area; stand on top of it or inside it");
                  } else {
                     int var7 = this.i();
                     int var8 = var7 < 0 && this.R() >= 0 ? 1 : 0;
                     if (var7 < 0 && var8 == 0) {
                        this.Q("put a pickaxe or your drill in your hotbar");
                     } else {
                        class_1799 var9 = mc.field_1724.method_31548().method_5438(var8 != 0 ? this.R() : var7);
                        this.HU = new fx(var1, this.wy.getValue().intValue(), this.nu.getValue().intValue(), var4.method_10263(), var4.method_10260());
                        this.He = mc.field_1687;
                        this.nB = mc.field_1724;
                        this.iJ.V();
                        this.H1 = (boolean)(this.R() < 0 ? 1 : 0);
                        int var10 = var4.method_10264() > var1.maxY() ? 0 : Math.max(0, this.HU.J(var4.method_10264()));
                        int var11 = var10 <= 0 || (!this.S().equals(this.wE) || var10 < this.HF) && !this.h(var4) ? 0 : 1;
                        int var12 = var11 != 0 ? var10 : this.I();
                        if (var12 == -1) {
                           q8.G(this.getName(), "Nothing to mine in {}", var1.describe());
                           this.L("there is nothing to mine in " + var1.describe() + ": every block in it is gone already.");
                           this.nM = (boolean)1;
                           this.disable();
                        } else {
                           int var13 = var12 >= 0 && var12 < var10 ? 1 : 0;
                           int var14 = var13 != 0 ? this.m(this.HU.V(var10).top()) : 0;
                           if (var13 == 0 || this.U.getValue() && this.g() >= 0) {
                              if (var13 != 0 && var5 > 2.0) {
                                 this.Q(
                                    "the layers above you still have "
                                       + var14
                                       + " blocks to mine. Stand on top of the area, or right next to it (you are "
                                       + (int)var5
                                       + " blocks away) so the miner can pillar up beside it"
                                 );
                              } else {
                                 this.Hz = var13 != 0 ? var12 : var10;
                                 this.I = this.Hz;
                                 this.w = System.currentTimeMillis();
                                 this.wR = this.HS = this.C();
                                 this.wd = mc.field_1724.field_6012;
                                 this.wk = 0L;
                                 StringBuilder var15 = new StringBuilder(var9.method_7964().getString());
                                 int var16 = this.R();
                                 if (var16 >= 0 && var8 == 0) {
                                    var15.insert(0, mc.field_1724.method_31548().method_5438(var16).method_7964().getString() + " and ");
                                 }

                                 String var17 = var13 != 0
                                    ? ", pillaring up to layer " + (this.Hz + 1) + " first: the layers above you still have " + var14 + " blocks"
                                    : (
                                       this.Hz > 0
                                          ? ", starting at layer "
                                             + (this.Hz + 1)
                                             + " where you stand (the "
                                             + this.m(this.HU.V(this.Hz).top())
                                             + " blocks in the layers above you are mined at the end)"
                                          : ""
                                    );
                                 this.L("mining " + var1.describe() + " in " + this.HU.r() + " layers with " + var15 + var17 + ".");
                                 ArrayList var18 = new ArrayList();
                                 if (var16 < 0) {
                                    var18.add("no hotbar item matches Drill Name \"" + this.F.getValue() + "\", so the pickaxe mines everything");
                                 }

                                 if (var8 != 0) {
                                    var18.add(
                                       "your drill is your only pickaxe, so it only cuts where its 3x3 stays inside the area; add a plain pickaxe to the hotbar to clear the edges too"
                                    );
                                 }

                                 if (this.U.getValue() && this.g() < 0) {
                                    var18.add("no solid blocks in the hotbar to pillar, bridge or plug lava with");
                                 }

                                 if (this.ij.getValue() && tn.h(var0 -> var0.method_58694(class_9334.field_50075) != null) < 0) {
                                    var18.add("no food in the hotbar");
                                 }

                                 if (this.wM.getValue()
                                    && (tn.S(var9) || var16 >= 0 && tn.S(mc.field_1724.method_31548().method_5438(var16)))
                                    && tn.h(var0 -> var0.method_31574(class_1802.field_8287)) < 0) {
                                    var18.add("no XP bottles in the hotbar to mend with");
                                 }

                                 if (!var18.isEmpty()) {
                                    this.L("heads up: " + String.join("; ", var18) + ".");
                                 }

                                 q8.G(
                                    this.getName(),
                                    "Started: {} from {}, {} layers from layer {}, rows along {}, pass width {}, drill slot {}, {} solid blocks{}",
                                    var1.describe(),
                                    var4.method_23854(),
                                    this.HU.r(),
                                    this.Hz + 1,
                                    this.HU.l() ? "X" : "Z",
                                    this.HU.i(),
                                    var16,
                                    this.wR,
                                    var13 != 0 ? ", pillaring up from layer " + (var10 + 1) : (var11 != 0 ? ", carrying on where you stand" : "")
                                 );
                                 this.Hs = (boolean)1;
                              }
                           } else {
                              this.Q(
                                 "the layers above you still have "
                                    + var14
                                    + " blocks to mine. Stand on top of the area to start, or put blocks in your hotbar with Place Blocks on so the miner can pillar up to them"
                              );
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.HU != null) {
         this.wS = "the world, the player or the connection changed";
         q8.G(this.getName(), "Interrupted after {} blocks: {}", this.iB, this.wS);
      }

      if (this.HU != null && this.HB == null && this.Hs) {
         this.wE = this.nM ? null : this.S();
         this.HF = this.nM ? -1 : this.Hz;
      }

      super.onDisable();
      fk.N(this);
      this.iJ.E();
      this.k();
   }

   private void k() {
      this.HU = null;
      this.He = this.nB = null;
      this.Hz = 0;
      this.H1 = this.in = (boolean)0;
      this.nD = sh.e.R;
      this.j.clear();
      this.wT.clear();
      this.ie = -1000000000;
      this.n4 = 0;
      this.wt.clear();
      this.M.clear();
      this.r.clear();
      this.L.clear();
      this.wQ.clear();
      this.k.clear();
      this.wH.clear();
      this.nc.clear();
      this.wB.clear();
      this.HM.h();
      this.nU = (boolean)0;
      this.V = Long.MIN_VALUE;
      this.iB = 0;
      this.Y = this.iZ = null;
      this.iW = null;
      this.iU.clear();
      this.iK = (boolean)0;
      this.G();
      this.T = null;
      this.nk = List.of();
      this.wj = null;
      this.Hc = 0;
      this.ic = null;
      this.i1 = this.HR = this.ny = null;
      this.n3 = 0;
      this.n.clear();
      this.b = -1;
      this.ii = (boolean)0;
      this.iT = class_243.field_1353;
      this.HE = this.i4 = this.wx = (boolean)0;
      this.A = Integer.MIN_VALUE;
      this.iR = -1;
      this.Hi.clear();
      this.wu = this.wV = null;
      this.w8 = 0;
      this.B = (boolean)0;
      this.iN.k();
      this.Hr = (boolean)0;
      this.a = this.w2 = 0;
      this.i3.clear();
      this.no = null;
      this.im = 0;
      this.I = 0;
      this.Z = null;
      this.H7 = null;
      this.w5.clear();
      this.Hl = 0;
      this.w_.clear();
      this.c = (boolean)0;
      this.x.clear();
      this.wm = -1;
      this.HN = 0;
      this.wR = this.HS = -1;
      this.wd = -1000000000;
      this.w = System.currentTimeMillis();
      this.nM = this.Hs = (boolean)0;
      this.ix.clear();
      this.nO = -1;
      this.nT = (boolean)0;
      this.HZ = null;
      this.ik = 0;
      this.nq = 0;
      this.H0.clear();
      this.HB = null;
      this.nI = "";
      this.w0.i();
      this.H4 = -1000000000;
      this.iG = 0;
      this.i.clear();
      this.wC = this.Hf = this.np = this.HX = List.of();
      this.i9 = null;
      this.HV.clear();
      this.w6.clear();
      this.nK = -1;
      this.iX = this.Hq = this.iO = 0;
      this.Hh = this.J = -1;
      this.HO = this.il = this.wA = -1000000000;
      this.Hu = this.Hg = this.H8 = this.HJ = (boolean)0;
      this.v.clear();
      this.is = -1;
      this.HH = this.Hj = null;
      this.HL = null;
      this.nf.clear();
      this.HP = -1000000000;
      this.nd = this.nY = 0;
      this.iM = this.HK = null;
      this.nx = this.ip = this.iv = this.nZ = 0;
      this.C = -1000000000;
      this.wf.clear();
      this.Hp = 88.0F;
   }

   private void G() {
      this.D = List.of();
      this.iu = 0;
      this.Hn = 0;
      this.wp = 0;
   }

   @Override
   public void onTick() {
      long var1 = System.nanoTime();

      try {
         this.c();
      } finally {
         this.A(System.nanoTime() - var1);
      }
   }

   private void A(long var1) {
      if (mc.field_1724 != null) {
         if (var1 > 5000000L) {
            this.Hb++;
            if (var1 > this.wv) {
               this.wv = var1;
               this.nJ = this.getStatus();
            }
         }

         int var3 = mc.field_1724.field_6012;
         if (var3 - this.n_ >= 200) {
            this.n_ = var3;
            this.w();
            if (this.Hb != 0) {
               q8.G(
                  this.getName(),
                  "Miner ticks over 5 ms in the last 10 s: {}, longest {} ms while: {}",
                  this.Hb,
                  String.format(Locale.ROOT, "%.1f", this.wv / 1000000.0),
                  this.nJ
               );
               this.Hb = 0;
               this.wv = 0L;
            }
         }
      }
   }

   private void c() {
      this.iT = class_243.field_1353;
      this.HE = this.i4 = this.wx = this.H8 = (boolean)0;
      this.nh = 0;
      if (this.HU != null) {
         if (mc.field_1687 == this.He && mc.field_1724 == this.nB) {
            this.A = mc.field_1724.field_6012;
            String var1 = tn.b();
            if (var1 != null) {
               bv.i(this);
               fk.N(this);
               this.iJ.K();
               this.HE = (boolean)1;
               this.m(var1 + ": swimming up");
            } else if (this.ij.getValue() && this.iJ.d(this.HI.getValue().intValue())) {
               bv.i(this);
               fk.N(this);
               this.m("Eating");
            } else if (this.wu != null) {
               this.y();
            } else {
               String var2 = this.m();
               if (var2 != null) {
                  this.L();
                  this.m(var2);
                  if (!var2.equals(this.HY)) {
                     this.HY = var2;
                     q8.G(this.getName(), "Held: {}", var2);
                  }
               } else {
                  this.HY = null;
                  if (this.U()) {
                     this.L();
                     this.iN.k();
                     this.m("Paused for manual movement");
                  } else if (mc.field_1755 instanceof class_465) {
                     this.L();
                     this.m("Holding still while an inventory menu is open");
                  } else if (!this.wM.getValue() || !this.M()) {
                     if (!this.q()) {
                        bv.i(this);
                        this.iT = class_243.field_1353;
                        this.m("Waiting for the area to load");
                     } else if (!this.n()) {
                        if (this.HB != null) {
                           this.Y();
                        } else if (!mc.field_1724.method_24828()) {
                           bv.i(this);
                           this.m("Waiting to land");
                        } else {
                           this.P();
                           this.s();
                           this.r();
                           if (this.wR >= 0 && mc.field_1724.field_6012 - this.wd >= 100) {
                              this.wd = mc.field_1724.field_6012;
                              this.HS = this.C();
                           }

                           if (this.Hr) {
                              this.t();
                           } else {
                              fx.i var3 = this.HU.V(this.Hz);
                              int var4 = this.v();
                              if (var4 >= var3.bottom()) {
                                 this.Q(var3);
                              }

                              if (var4 > var3.bottom()) {
                                 this.nD = sh.e.M;
                                 this.u(var3);
                              } else if (var4 < var3.bottom()) {
                                 this.nD = sh.e.w;
                                 this.N(var3);
                              } else {
                                 this.nD = sh.e.R;
                                 this.z(var3);
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.Q("the world or player changed");
         }
      }
   }

   private void L() {
      bv.i(this);
      fk.N(this);
      this.iT = class_243.field_1353;
   }

   private boolean U() {
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

   private boolean M() {
      int var1 = this.wI.getValue().intValue();

      for (int var5 : new int[]{this.iR, this.R()}) {
         if (var5 >= 0 && !this.v(var5)) {
            class_1799 var6 = mc.field_1724.method_31548().method_5438(var5);
            if (tn.p(var6) <= var1 || this.iJ.s()) {
               bv.i(this);
               tn.b var7 = this.iJ.k(var5, var1);
               if (var7 == tn.b.c) {
                  this.m("Mending the tool with XP bottles");
                  return true;
               }

               if (var7 == tn.b.p) {
                  this.Q(
                     "your "
                        + var6.method_7964().getString()
                        + " has "
                        + tn.p(var6)
                        + " uses left and cannot be mended (it needs Mending and XP bottles in the hotbar)"
                  );
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void z(fx.i var1) {
      if (!this.in) {
         this.in = (boolean)1;
         class_2338 var2 = this.X();
         if (this.HU.X().containsColumn(var2.method_10263(), var2.method_10260())) {
            this.wB.add(this.C(var2.method_10263(), var2.method_10260()));
         }

         q8.G(this.getName(), "Layer {} of {}: Y {} to {}", this.Hz + 1, this.HU.r(), var1.bottom(), var1.top());
      }

      if (mc.field_1724.field_6012 - this.ie >= 40) {
         this.E(var1);
      }

      if (!this.U.getValue() || !this.g(var1)) {
         if (!this.i(var1)) {
            class_2338 var7 = this.D(var1);
            if (var7 != null) {
               this.G();
               if (this.iK) {
                  this.b(var7, true);
               } else {
                  this.Q(var7, null);
               }
            } else {
               bv.i(this);
               class_2338 var3 = this.Hu ? this.O(var1) : this.m(var1);
               if (var3 == null) {
                  if (mc.field_1724.field_6012 - this.ie > 2) {
                     this.E(var1);
                  } else {
                     byte var4 = 0;
                     if (this.iB != this.nO) {
                        for (class_2338 var6 : this.j) {
                           if (this.wt.containsKey(var6) && !this.nc.contains(var6) && bv.P(var6)) {
                              this.wt.remove(var6);
                              var4 = 1;
                           }
                        }
                     }

                     if (var4 != 0) {
                        this.nO = this.iB;
                        q8.v(this.getName(), "Retrying blocks set aside in layer {}", this.Hz + 1);
                        this.G();
                     } else {
                        this.X(var1);
                     }
                  }
               } else {
                  this.t(var3, var1);
               }
            }
         }
      }
   }

   private void Q(fx.i var1) {
      if (this.nK != this.Hz) {
         this.nK = this.Hz;
         this.iX = this.Hq = this.iO = 0;
         this.Hh = this.J = -1;
         this.HO = this.il = this.wA = -1000000000;
         this.Hu = this.Hg = this.HJ = (boolean)0;
         this.v.clear();
         this.is = -1;
         this.w6.clear();
         this.h = null;
         this.wN = this.p = -1;
         this.o = Long.MIN_VALUE;
         this.i8.clear();
         this.nw = 0;
         this.l = 0;
         this.ns = null;
         this.w3 = -1;
         this.HV.clear();
         this.HX = List.of();
         this.HW = -1;
         this.n5 = -1000000000;
         this.i9 = null;
         this.nP = -1;
         this.iQ = -1000000000;
         this.Q = 0;
         if (this.HB != null) {
            this.wC = this.Hf = this.np = List.of();
            this.Hu = (boolean)1;
         } else {
            int var2 = this.v();
            this.wC = this.Hf = com.larpclient.wy.k(this.HU, this.Hz, var2);
            this.np = com.larpclient.wy.B(this.HU, this.Hz, var1.bottom(), true);
            this.T();
            long var3 = this.wC.stream().filter(var0 -> var0.kind() == wy.f.Z).count();
            String var5 = var3 > 0L
               ? "a " + var3 + " block stair down from the corner"
               : (var2 > var1.bottom() ? "the first lane is too short for a stair, so it digs down at the corner" : "standing on the layer's floor");
            q8.G(
               this.getName(),
               "Layer {} dig order: {} stands, {} lanes {} wide along {}, {}",
               this.Hz + 1,
               this.wC.size(),
               this.HU.w(),
               this.HU.i(),
               this.HU.l() ? "X" : "Z",
               var5
            );
         }
      }
   }

   private void T() {
      this.w6.clear();
      int var1 = 0;

      for (wy.s var3 : this.wC) {
         for (wy.y var5 : var3.cells()) {
            this.w6.putIfAbsent(var5.key(), var1++);
         }
      }
   }

   private boolean O() {
      return this.HU != null && this.HB == null && !this.Hu && this.nK == this.Hz;
   }

   private boolean i(fx.i var1) {
      if (!this.O()) {
         return false;
      }

      int var2 = mc.field_1724.field_6012;
      if (this.iB != this.is) {
         this.v.clear();
         this.is = this.iB;
      }

      if (var2 < this.nd && !this.Z()) {
         bv.i(this);
         this.m("Waiting for the drill's cut to land");
         return true;
      }

      if (this.f()) {
         return true;
      }

      if (var2 < this.nY) {
         this.n();
         return true;
      }

      while (this.iX < this.wC.size() && this.u(this.wC.get(this.iX), var1)) {
         this.K(false);
      }

      if (this.iX >= this.wC.size() && !this.C(var1, false)) {
         this.Hu = (boolean)1;
         this.v.clear();
         int var7 = 0;

         for (class_2338 var10 : this.j) {
            if (this.c(var10, var1)) {
               var7++;
            }
         }

         q8.G(
            this.getName(),
            "Layer {}: dig order done after {} of {} stands{}{}{}",
            this.Hz + 1,
            this.Hq,
            this.wC.size(),
            this.Q > 0 ? ", moved to the nearest face " + this.Q + " times" : "",
            var7 > 0 ? ", " + var7 + " blocks left for the clean-up" : "",
            this.l > 0 ? ", " + this.l + " passed over with no way to dig to them" : ""
         );
         return false;
      } else {
         class_2338 var3 = this.X();
         if (var3.method_10263() != this.wC.get(this.iX).standX() || var3.method_10260() != this.wC.get(this.iX).standZ()) {
            this.W(var1);
         }

         wy.s var4 = this.wC.get(this.iX);
         if (var3.method_10263() == var4.standX() && var3.method_10260() == var4.standZ()) {
            this.G();
            this.H8 = e3.c(var4.standX(), var1.bottom(), var4.standZ());
            if (this.L() && this.F(var4, var1)) {
               this.HJ = (boolean)1;
               if (this.R(var4) && bv.P(new class_2338(var4.standX() + var4.dx(), var1.bottom(), var4.standZ() + var4.dz()))) {
                  this.iT = new class_243(var4.dx(), 0.0, var4.dz());
               }

               return true;
            } else {
               class_2338 var9 = this.i(var4, var1, true);
               if (var9 != null) {
                  this.HJ = (boolean)1;
                  this.T(var9, null);
                  return true;
               } else {
                  class_2338 var6 = this.a(var4, var1);
                  if (var6 != null) {
                     this.HJ = (boolean)1;
                     this.T(var6, null);
                     return true;
                  } else {
                     this.K(true);
                     return true;
                  }
               }
            }
         } else {
            if (this.k(var4, var3, var1)) {
               return true;
            }

            if (this.V(var4, var1)) {
               return true;
            }

            class_2338 var5 = this.i(var4, var1, false);
            if (var5 != null) {
               this.G();
               this.T(var5, null);
               return true;
            }

            this.D(var4, var1);
            if (this.Z(var4, var1)) {
               return true;
            }

            this.v(var4, var1);
            this.K(false);
            return ++this.nh % 16 != 0 ? this.i(var1) : true;
         }
      }
   }

   private boolean k(wy.s var1, class_2338 var2, fx.i var3) {
      if (var1.kind() == wy.f.D && var1.drill() && this.L() && this.R(var1) && var2.method_10264() == var3.bottom()) {
         int var4 = (var1.standX() - var2.method_10263()) * var1.dx() + (var1.standZ() - var2.method_10260()) * var1.dz();
         int var5 = var1.dx() != 0 ? (var2.method_10260() == var1.standZ() ? 1 : 0) : (var2.method_10263() == var1.standX() ? 1 : 0);
         if (var5 != 0 && var4 >= 1 && var4 <= 3) {
            for (int var6 = 1; var6 <= var4; var6++) {
               class_2338 var7 = var2.method_10069(var1.dx() * var6, 0, var1.dz() * var6);
               if (!this.t(var7)
                  || !this.t(var7.method_10084())
                  || !this.x(var7.method_10074())
                  || e3.c(var7.method_10263(), var7.method_10264(), var7.method_10260())) {
                  return false;
               }
            }

            wy.y var8 = var1.drillCentre();
            class_2338 var9 = new class_2338(var8.x(), var8.y(), var8.z());
            if (!bv.P(var9.method_10074())) {
               return false;
            }

            if (this.V(var9) && this.F(var1, var3)) {
               this.HJ = (boolean)1;
            }

            this.G();
            this.iT = new class_243(var1.dx(), 0.0, var1.dz());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean R(wy.s var1) {
      return Math.abs(class_3532.method_15393(mc.field_1724.method_36454() - s(var1).method_10144())) <= 25.0F;
   }

   private void C(wy.s var1) {
      long var2 = var1.dx() + 2L << 40 ^ var1.dz() + 2L << 36 ^ (var1.dx() != 0 ? var1.standZ() : var1.standX()) & 4294967295L;
      if (var2 != this.V) {
         this.V = var2;
         this.Ha = (float)((this.iV.nextDouble() * 2.0 - 1.0) * 2.5);
         this.Ht = -2.0F + (float)this.iV.nextDouble() * 8.0F;
      }
   }

   static class_243 g(class_243 var0, class_2338 var1, class_2350 var2, float var3, float var4) {
      int var5 = var2.method_10166() == class_2351.field_11048 ? 1 : 0;
      double var6 = var5 != 0 ? var1.method_10263() + (var2 == class_2350.field_11034 ? 1 : 0) : var1.method_10260() + (var2 == class_2350.field_11035 ? 1 : 0);
      double var8 = Math.abs(var6 - (var5 != 0 ? var0.field_1352 : var0.field_1350));
      int var10 = var5 != 0 ? var1.method_10260() : var1.method_10263();
      double var11 = class_3532.method_15350(
         (var5 != 0 ? var0.field_1350 : var0.field_1352) + Math.tan(Math.toRadians(var3)) * var8, var10 + 0.12, var10 + 1 - 0.12
      );
      double var13 = class_3532.method_15350(
         var0.field_1351 - Math.tan(Math.toRadians(var4)) * var8, var1.method_10264() + 0.12, var1.method_10264() + 1 - 0.12
      );
      return var5 != 0 ? new class_243(var6, var13, var11) : new class_243(var11, var13, var6);
   }

   private boolean u(wy.s var1, fx.i var2) {
      return this.HV.contains(var1) || this.y(var1, var2);
   }

   private void W(fx.i var1) {
      wy.s var2 = this.wC.get(this.iX);
      class_2338 var3 = this.X();
      int var4 = mc.field_1724.field_6012;
      int var5 = this.iX == this.nP && this.wC == this.i9 ? 0 : 1;
      boolean var6 = this.u(var1.bottom()).contains(bg.Y(var2.standX(), var2.standZ()));
      if (var5 != 0 || !var6 && var4 - this.iQ >= 40) {
         this.iQ = var4;
         if (!var6 || Math.abs(var2.standX() - var3.method_10263()) + Math.abs(var2.standZ() - var3.method_10260()) > 6) {
            this.C(var1, var5 == 0);
         }

         this.nP = this.iX;
         this.i9 = this.wC;
      }
   }

   private List<wy.s> b(fx.i var1) {
      int var2 = mc.field_1724.field_6012;
      if (this.HW == this.iB && var2 - this.n5 < 40) {
         this.HX.removeIf(this.HV::contains);
      } else {
         this.HW = this.iB;
         this.n5 = var2;
         ArrayList var3 = new ArrayList();

         for (wy.s var5 : this.Hf) {
            if (!this.u(var5, var1)) {
               var3.add(var5);
            }
         }

         for (wy.s var7 : this.np) {
            if (!this.u(var7, var1)) {
               var3.add(var7);
            }
         }

         this.HX = var3;
      }

      return this.HX;
   }

   private boolean C(fx.i var1, boolean var2) {
      class_2338 var3 = this.X();
      Set var4 = this.u(var1.bottom());
      wy.s var5 = com.larpclient.wy.x(this.b(var1), var4, var3.method_10263(), var3.method_10260());
      if (var5 == null) {
         return false;
      }

      boolean var6 = var4.contains(bg.Y(var5.standX(), var5.standZ()));
      if (var2 && !var6) {
         return false;
      }

      if (this.iX < this.wC.size() && this.wC.get(this.iX).equals(var5)) {
         return true;
      }

      int var7 = this.wC.indexOf(var5);
      if (var7 < 0) {
         this.wC = this.wC == this.np ? this.Hf : this.np;
         var7 = this.wC.indexOf(var5);
         this.T();
      }

      this.iX = var7;
      this.Hh = -1;
      this.wN = -1;
      this.v.clear();
      if (this.Q++ < 6) {
         q8.G(
            this.getName(),
            "Layer {}: going to the nearest face instead, stand {} of {} ({}) at {}, {}, {}, lanes {}",
            this.Hz + 1,
            this.iX + 1,
            this.wC.size(),
            var5.kind(),
            var5.standX(),
            var5.standZ(),
            var6 ? "on the floor" : "off the floor",
            this.wC == this.np ? "reversed" : "as planned"
         );
      }

      return true;
   }

   private wy.s r(wy.s var1, fx.i var2) {
      if (this.wN != this.iX) {
         this.i8.clear();
      }

      if (this.wN == this.iX && this.p == this.iB) {
         return this.h;
      }

      this.wN = this.iX;
      this.p = this.iB;
      this.h = null;
      int var3 = var2.bottom();

      for (wy.a var5 : com.larpclient.wy.Z(this.u(var3), var1.standX(), var1.standZ(), 32)) {
         int var6 = var5.x() + var5.dx();
         int var7 = var5.z() + var5.dz();
         if (!this.i8.contains(bg.Y(var6, var7)) && this.x(new class_2338(var6, var3 - 1, var7))) {
            class_2338 var8 = new class_2338(var6, var3 + 1, var7);
            class_2338 var9 = new class_2338(var6, var3, var7);
            boolean var10 = this.t(var8);
            boolean var11 = this.t(var9);
            if ((!var10 || !var11) && (var10 || this.c(var8, var2)) && (var11 || this.c(var9, var2))) {
               ArrayList var12 = new ArrayList(2);
               if (!var10) {
                  var12.add(new wy.y(var6, var3 + 1, var7));
               }

               if (!var11) {
                  var12.add(new wy.y(var6, var3, var7));
               }

               this.h = new wy.s(wy.f.H, var5.x(), var3, var5.z(), var5.dx(), var5.dz(), var12, false);
               break;
            }
         }
      }

      return this.h;
   }

   private boolean Z(wy.s var1, fx.i var2) {
      wy.s var3 = this.r(var1, var2);
      if (var3 == null) {
         return false;
      }

      class_2338 var4 = this.X();
      wy.y var5 = var3.cells().getFirst();
      long var6 = bg.Y(var5.x(), var5.z());
      if (var4.method_10263() == var3.standX() && var4.method_10260() == var3.standZ()) {
         this.G();
         if (this.L() && this.h(var3, var2)) {
            this.HJ = (boolean)1;
            return true;
         } else {
            class_2338 var13 = this.i(var3, var2, true);
            if (var13 == null) {
               this.v.clear();
               this.i8.add(var6);
               this.p = -1;
               return true;
            } else {
               this.HJ = (boolean)1;
               this.T(var13, "Digging a way to the next stand");
               return true;
            }
         }
      } else {
         long var8 = bg.Y(var3.standX(), var3.standZ());
         if (this.o != var8 || this.D.isEmpty() && this.iu == 0 || mc.field_1724.field_6012 - this.X > 40) {
            this.o = var8;
            this.X = mc.field_1724.field_6012;
            this.Hh = -1;
            int var10 = var3.standX();
            int var11 = var3.standZ();
            List var12 = this.C(var2.bottom(), (var2x, var3x) -> var2x == var10 && var3x == var11);
            if (var12 == null) {
               this.G();
               this.i8.add(var6);
               this.p = -1;
               return true;
            }

            if (var12.isEmpty()) {
               return true;
            }

            this.D = var12;
            this.iu = 0;
            this.n3 = 0;
            this.M(var2, var3);
         }

         bv.i(this);
         this.iZ = new class_2338(var5.x(), var5.y(), var5.z());
         this.I(var2);
         return true;
      }
   }

   private boolean h(wy.s var1, fx.i var2) {
      wy.y var3 = var1.drillCentre();
      class_2338 var4 = new class_2338(var3.x(), var3.y(), var3.z());
      if (!this.v.contains(var4) && this.c(var4, var2)) {
         class_2350 var5 = s(var1).method_10153();
         int var6 = this.G(var4, var5, var2.bottom());
         return var6 >= 2 && this.S(var4, var6) && this.F(var4, var5, "Drilling a way to the next stand (" + var6 + " blocks)");
      } else {
         return false;
      }
   }

   private String K(class_2338 var1) {
      return class_7923.field_41175.method_10221(mc.field_1687.method_8320(var1).method_26204()).method_12832();
   }

   private String t(wy.s var1, fx.i var2) {
      class_2338 var3 = new class_2338(var1.standX(), var2.bottom(), var1.standZ());
      return String.format(
         Locale.ROOT,
         "stand %d of %d (%s) at %d, %d; player at %s, reachable floor %d cells; feet block %s%s, head %s%s, floor %s",
         this.iX + 1,
         this.wC.size(),
         var1.kind(),
         var1.standX(),
         var1.standZ(),
         this.X().method_23854(),
         this.u(var2.bottom()).size(),
         this.K(var3),
         this.X(var3) ? " (set aside)" : "",
         this.K(var3.method_10084()),
         this.X(var3.method_10084()) ? " (set aside)" : "",
         this.K(var3.method_10074())
      );
   }

   private void D(wy.s var1, fx.i var2) {
      if (this.wN != this.iX && this.nw < 3) {
         this.nw++;
         q8.G(this.getName(), "Layer {}: {}; digging a way to it", this.Hz + 1, this.t(var1, var2));
      }
   }

   private void v(wy.s var1, fx.i var2) {
      if (this.l++ < 3) {
         q8.G(this.getName(), "Layer {}: no way to dig to {}; passed over", this.Hz + 1, this.t(var1, var2));
      }
   }

   private class_2338 O(fx.i var1) {
      if (this.ns != null && this.w3 == this.iB && this.c(this.ns, var1)) {
         return this.ns;
      }

      this.w3 = this.iB;
      this.ns = null;
      double var2 = mc.field_1724.method_23317();
      double var4 = mc.field_1724.method_23321();
      ArrayList var6 = new ArrayList();

      for (class_2338 var8 : this.j) {
         if (this.c(var8, var1) && this.Y(var8)) {
            var6.add(var8);
         }
      }

      var6.sort(Comparator.comparingDouble(var4x -> Math.pow(var4x.method_10263() + 0.5 - var2, 2.0) + Math.pow(var4x.method_10260() + 0.5 - var4, 2.0)));
      int var10 = 0;

      for (class_2338 var9 : var6) {
         if (var10++ >= 32) {
            break;
         }

         if (this.b(var9, var1)) {
            this.ns = var9;
            break;
         }
      }

      return this.ns;
   }

   static class_2350 s(wy.s var0) {
      return var0.dx() > 0
         ? class_2350.field_11034
         : (var0.dx() < 0 ? class_2350.field_11039 : (var0.dz() > 0 ? class_2350.field_11035 : class_2350.field_11043));
   }

   private boolean F(wy.s var1, fx.i var2) {
      if (var1.kind() != wy.f.D && var1.kind() != wy.f.S) {
         return false;
      } else {
         wy.y var3 = var1.drillCentre();
         class_2338 var4 = new class_2338(var3.x(), var3.y(), var3.z());
         if (!this.v.contains(var4) && this.c(var4, var2)) {
            class_2350 var5 = s(var1).method_10153();
            int var6 = this.G(var4, var5, var2.bottom());
            this.C(var1);
            return var6 >= 2 && this.S(var4, var6) && this.a(var4, var5, "Cutting a 3x3 face at " + var4.method_23854() + " (" + var6 + " blocks)", true);
         } else {
            return false;
         }
      }
   }

   private boolean L() {
      return this.R() >= 0 && this.R();
   }

   static List<class_2338> m(class_2338 var0, class_2350 var1) {
      ArrayList var2 = new ArrayList(9);
      class_2351 var3 = var1.method_10166();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            var2.add(
               var3 == class_2351.field_11048
                  ? var0.method_10069(0, var4, var5)
                  : (var3 == class_2351.field_11052 ? var0.method_10069(var4, 0, var5) : var0.method_10069(var4, var5, 0))
            );
         }
      }

      return var2;
   }

   private int G(class_2338 var1, class_2350 var2, int var3) {
      int var4 = 0;

      for (class_2338 var6 : m(var1, var2)) {
         if (bv.P(var6)) {
            if (!this.u(var6) || var6.method_10264() < var3 || this.K(var6) || this.S(var6)) {
               return -1;
            }

            var4++;
         }
      }

      return var4;
   }

   private boolean S(class_2338 var1, int var2) {
      class_2680 var3 = mc.field_1687.method_8320(var1);
      class_1661 var4 = mc.field_1724.method_31548();
      class_1799 var5 = var4.method_5438(this.R());
      class_1799 var6 = var4.method_5438(Math.max(0, this.n(var3, this.R())));
      return var5.method_7924(var3) * var2 >= var6.method_7924(var3) && (!var3.method_29291() || var5.method_7951(var3));
   }

   private boolean F(class_2338 var1, class_2350 var2, String var3) {
      return this.a(var1, var2, var3, false);
   }

   private boolean a(class_2338 var1, class_2350 var2, String var3, boolean var4) {
      if (this.nf.contains(var1)) {
         return false;
      }

      this.HH = var1.method_10062();
      this.HL = var2;
      this.nU = var4;
      class_3965 var5 = bv.z(this, var1, this.Y());
      if (var5 != null && var5.method_17780() == var2) {
         this.iK = (boolean)1;
         this.iU.clear();

         for (class_2338 var7 : m(var1, var2)) {
            if (bv.P(var7)) {
               this.iU.put(var7.method_10062(), mc.field_1687.method_8320(var7));
            }
         }

         this.c(var1);
         this.K(var1, true, var3);
         return true;
      } else {
         this.HH = null;
         return false;
      }
   }

   private void Q(class_2338 var1, String var2) {
      if (this.L()) {
         class_3965 var3 = bv.z(this, var1, this.Y());
         if (var3 != null) {
            int var4 = this.G(var1, var3.method_17780(), this.v());
            if (var4 >= 2 && this.S(var1, var4) && this.F(var1, var3.method_17780(), "Cutting a 3x3 at " + var1.method_23854() + " (" + var4 + " blocks)")) {
               return;
            }
         }
      }

      this.T(var1, var2);
   }

   private boolean y(wy.s var1, fx.i var2) {
      if (var1.kind() == wy.f.Z) {
         return true;
      }

      for (wy.y var4 : var1.cells()) {
         class_2338 var5 = new class_2338(var4.x(), var4.y(), var4.z());
         if (!this.v.contains(var5) && this.c(var5, var2) && !this.q(var1, var5)) {
            return false;
         }
      }

      return true;
   }

   private boolean q(wy.s var1, class_2338 var2) {
      if (!this.iw.getValue() && var1.drill() && !this.H1 && this.R() >= 0) {
         wy.y var3 = var1.drillCentre();
         return var2.method_10263() != var3.x() || var2.method_10260() != var3.z() || var2.method_10264() > var3.y();
      } else {
         return false;
      }
   }

   private void K(boolean var1) {
      int var2 = !var1 && !this.HJ ? 0 : 1;
      if (var2 != 0) {
         this.H9 = -1000000000;
      }

      this.HJ = (boolean)0;
      if (this.iX < this.wC.size()) {
         this.HV.add(this.wC.get(this.iX));
      }

      this.iX++;
      this.Hh = -1;
      this.v.clear();
      if (var2 != 0) {
         this.Hq++;
         if (this.iV.nextDouble() < 0.035 && (this.H1 || this.R() < 0)) {
            this.nY = mc.field_1724.field_6012 + this.iV.nextInt(8, 22);
            this.HG = mc.field_1724.method_36454() + (float)((this.iV.nextBoolean() ? 1 : -1) * (20.0 + this.iV.nextDouble() * 45.0));
            this.d = class_3532.method_15363(mc.field_1724.method_36455() - 12.0F + (float)this.iV.nextDouble() * 24.0F, -30.0F, 60.0F);
         }
      }
   }

   private void n() {
      bv.i(this);
      this.iT = class_243.field_1353;
      this.aimAt(mc.field_1724.method_33571().method_1019(class_243.method_1030(this.d, this.HG).method_1021(6.0)), 12.0F);
      this.m("Looking around");
   }

   private boolean f() {
      class_238 var1 = mc.field_1724.method_5829().method_1009(4.0, 6.0, 4.0);
      if (mc.field_1687.method_18467(class_1540.class, var1).isEmpty()) {
         return false;
      }

      bv.i(this);
      this.iT = class_243.field_1353;
      this.m("Waiting for falling blocks to land");
      return true;
   }

   private class_2338 i(wy.s var1, fx.i var2, boolean var3) {
      double var4 = this.Y();
      int var6 = 0;

      for (wy.y var8 : var1.cells()) {
         class_2338 var9 = new class_2338(var8.x(), var8.y(), var8.z());
         if (!this.v.contains(var9) && this.c(var9, var2) && !this.q(var1, var9)) {
            if (!this.V(var9)) {
               if (var3) {
                  this.v.add(var9);
               }
            } else {
               if (!var3 && var6++ >= 8) {
                  return null;
               }

               class_3965 var10 = bv.z(this, var9, var4);
               if (var10 != null && this.W(var9, var10)) {
                  return var9;
               }

               if (var3) {
                  this.v.add(var9);
                  q8.v(this.getName(), "No open face of {} from the stand at {}, {}; later", var9.method_23854(), var1.standX(), var1.standZ());
               }
            }
         }
      }

      return null;
   }

   private class_2338 a(wy.s var1, fx.i var2) {
      if (var1.cells().isEmpty()) {
         return null;
      }

      Integer var3 = this.w6.get(var1.cells().getFirst().key());
      if (var3 != null && var3 != 0) {
         double var4 = this.Y();
         class_243 var6 = mc.field_1724.method_33571();
         int var7 = var1.standX();
         int var8 = var1.standZ();
         int var9 = var7 + var1.dx();
         int var10 = var8 + var1.dz();
         ArrayList var11 = new ArrayList();

         for (class_2338 var13 : this.j) {
            Integer var14 = this.w6.get(com.larpclient.wy.y(var13.method_10263(), var13.method_10264(), var13.method_10260()));
            if (var14 != null && var14 < var3 && !this.v.contains(var13)) {
               int var15 = (Math.abs(var13.method_10263() - var7) > 1 || Math.abs(var13.method_10260() - var8) > 1)
                     && (Math.abs(var13.method_10263() - var9) > 1 || Math.abs(var13.method_10260() - var10) > 1)
                  ? 0
                  : 1;
               if (var15 != 0 && !(var6.method_1025(class_243.method_24953(var13)) > var4 * var4) && this.c(var13, var2)) {
                  var11.add(var13);
               }
            }
         }

         if (var11.isEmpty()) {
            return null;
         }

         float var19 = mc.field_1724.method_36454();
         float var20 = mc.field_1724.method_36455();
         class_2338 var21 = this.i1;
         var11.sort(Comparator.comparingDouble(var6x -> Y(var6, var6x, var19, var20, var4, var21)));
         int var22 = 0;

         for (class_2338 var17 : var11) {
            if (var22++ >= 8) {
               break;
            }

            class_3965 var18 = bv.z(this, var17, var4);
            if (var18 != null && this.W(var17, var18)) {
               return var17;
            }

            this.v.add(var17);
         }

         return null;
      } else {
         return null;
      }
   }

   private void T(class_2338 var1, String var2) {
      this.HH = null;
      this.iK = (boolean)0;
      this.iU.clear();
      this.c(var1);
      this.K(var1, false, var2);
   }

   private boolean V(wy.s var1, fx.i var2) {
      class_2338 var3 = null;

      for (wy.y var5 : var1.cells()) {
         class_2338 var6 = new class_2338(var5.x(), var5.y(), var5.z());
         if (this.c(var6, var2)) {
            var3 = var6;
            break;
         }
      }

      if (this.J == this.iX && mc.field_1724.field_6012 - this.wA < 40) {
         return false;
      }

      if (!this.u(var2.bottom()).contains(bg.Y(var1.standX(), var1.standZ()))) {
         return false;
      }

      if (this.Hh != this.iX || this.D.isEmpty() && this.iu == 0 || mc.field_1724.field_6012 - this.X > 40) {
         this.Hh = this.iX;
         this.X = mc.field_1724.field_6012;
         int var7 = var1.standX();
         int var8 = var1.standZ();
         List var9 = this.C(var2.bottom(), (var2x, var3x) -> var2x == var7 && var3x == var8);
         if (var9 == null) {
            this.G();
            this.Hh = -1;
            this.J = this.iX;
            this.wA = mc.field_1724.field_6012;
            return false;
         }

         if (var9.isEmpty()) {
            return true;
         }

         this.D = var9;
         this.iu = 0;
         this.n3 = 0;
         this.M(var2, var1);
         q8.v(this.getName(), "{} {} of {}: walking {} steps to {}, {}", var1.kind(), this.iX + 1, this.wC.size(), this.D.size(), var7, var8);
      }

      bv.i(this);
      this.iZ = var3;
      this.I(var2);
      return true;
   }

   private void M(fx.i var1, wy.s var2) {
      this.nk = this.D;
      this.wj = null;
      bg.o var3 = this.D.getLast();
      boolean var4 = e3.c(var3.x(), var1.bottom(), var3.z());
      double var5 = var4 ? 0.0 : -0.18 + this.iV.nextDouble() * 0.19999999999999998;
      double var7 = var4 ? 0.0 : (this.iV.nextDouble() * 2.0 - 1.0) * 0.12;
      this.H3 = var2.dx() * var5 - var2.dz() * var7;
      this.nE = var2.dz() * var5 + var2.dx() * var7;
      this.z = var4 ? 0.245 : 0.15 + this.iV.nextDouble() * 0.15;
      this.E = 0.35 + this.iV.nextDouble() * 0.65;
      this.ws = this.iV.nextDouble() * Math.PI * 2.0;
   }

   private boolean q(fx.i var1) {
      int var2 = this.v();
      wy.s var3 = null;

      for (wy.s var5 : this.wC) {
         if (var5.kind() == wy.f.Z && var5.standY() == var2) {
            var3 = var5;
            break;
         }
      }

      if (var3 == null) {
         return this.V("no step planned from Y " + var2);
      }

      if (var2 != this.il) {
         this.il = var2;
         this.iO = 0;
      }

      if (this.f()) {
         return true;
      }

      class_2338 var13 = this.X();
      int var14 = var3.standX() + var3.dx();
      int var6 = var3.standZ() + var3.dz();
      class_2338 var7 = var3.cells().isEmpty()
         ? new class_2338(var14, var2 - 1, var6)
         : new class_2338(var3.cells().getFirst().x(), var3.cells().getFirst().y(), var3.cells().getFirst().z());
      if (var13.method_10263() == var14 && var13.method_10260() == var6 && this.t(new class_2338(var14, var2 - 1, var6))) {
         this.M(var3, var2);
         return ++this.iO > 80 ? this.V("could not step down to " + new class_2338(var14, var2 - 1, var6).method_23854()) : true;
      }

      if (var13.method_10263() == var3.standX() && var13.method_10260() == var3.standZ()) {
         this.G();

         for (int var15 = var2 - 1; var15 <= var2 + 1; var15++) {
            if (!var1.contains(var15) && !this.t(new class_2338(var14, var15, var6))) {
               return this.V(
                  new class_2338(var14, var15, var6).method_23854()
                     + (this.HU.X().contains(var14, var15, var6) ? " was left by the layer above" : " above the area")
                     + " and is in the way"
               );
            }
         }

         class_2338 var16 = new class_2338(var14, var2 - 1, var6);

         for (wy.y var18 : var3.cells()) {
            class_2338 var11 = new class_2338(var18.x(), var18.y(), var18.z());
            if (!this.t(var11)) {
               class_238 var12 = mc.field_1724.method_5829();
               if (var11.method_10264() == var2 - 1
                  && var12.field_1320 > var14
                  && var12.field_1323 < var14 + 1
                  && var12.field_1324 > var6
                  && var12.field_1321 < var6 + 1) {
                  bv.i(this);
                  this.g(new class_243(var3.standX() + 0.5 - var3.dx() * 0.1, mc.field_1724.method_23318(), var3.standZ() + 0.5 - var3.dz() * 0.1));
                  this.m("Stepping back from the edge");
                  if (++this.iO > 80) {
                     return this.V("could not get clear of " + var16.method_23854());
                  }

                  return true;
               }

               if (!this.c(var11, var1)) {
                  return this.V(var11.method_23854() + " cannot be mined (set aside, beside a fluid or unbreakable)");
               }

               if (var11.equals(var16) && !this.j(var16, var1)) {
                  return this.V("the drop at " + var16.method_23854() + " is not safe");
               }

               this.H8 = (boolean)1;
               this.T(var11, "Cutting a step down to layer " + (this.Hz + 1));
               return true;
            }
         }

         if (!this.j(var16, var1)) {
            return this.V("the drop at " + var16.method_23854() + " is not safe");
         }

         this.M(var3, var2);
         return ++this.iO > 80 ? this.V("could not step down to " + var16.method_23854()) : true;
      } else {
         if (this.HO != var2 || this.D.isEmpty() && this.iu == 0) {
            this.HO = var2;
            int var8 = var3.standX();
            int var9 = var3.standZ();
            List var10 = this.C(var2, (var2x, var3x) -> var2x == var8 && var3x == var9);
            if (var10 == null) {
               return this.V("no way to the step at " + var8 + ", " + var2 + ", " + var9);
            }

            this.D = var10;
            this.iu = 0;
            this.nk = List.of();
            this.n3 = 0;
         }

         bv.i(this);
         this.iZ = var7;
         this.T(var2);
         this.m("Walking to the step down to layer " + (this.Hz + 1));
         return true;
      }
   }

   private void M(wy.s var1, int var2) {
      bv.i(this);
      this.n3 = 0;
      int var3 = var1.standX() + var1.dx();
      int var4 = var1.standZ() + var1.dz();
      class_243 var5 = mc.field_1724.method_73189();
      class_243 var6 = new class_243(var3 + 0.5 - var5.field_1352, 0.0, var4 + 0.5 - var5.field_1350);
      double var7 = var6.method_1033();
      double var9 = var7 < 1.0E-6 ? 0.0 : mc.field_1724.method_18798().method_18805(1.0, 0.0, 1.0).method_1026(var6.method_1029());
      boolean var11 = this.iN.V(var7 + 0.2, var9, cr.a(this));
      this.iT = var11 ? var6 : class_243.field_1353;
      this.aimAt(new class_243(var3 + 0.5 + var1.dx() * 0.7, var2 - 1, var4 + 0.5 + var1.dz() * 0.7), 12.0F);
      this.m("Stepping down to layer " + (this.Hz + 1));
   }

   private boolean V(String var1) {
      this.Hg = (boolean)1;
      this.G();
      this.H8 = (boolean)0;
      q8.G(this.getName(), "Layer {}: the stair down is not used ({}); digging down at the corner instead", this.Hz + 1, var1);
      return false;
   }

   private void X(Set<class_2338> var1, String var2) {
      int[] var3 = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
      int[] var4 = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

      for (class_2338 var6 : var1) {
         var3[0] = Math.min(var3[0], var6.method_10263());
         var3[1] = Math.min(var3[1], var6.method_10264());
         var3[2] = Math.min(var3[2], var6.method_10260());
         var4[0] = Math.max(var4[0], var6.method_10263());
         var4[1] = Math.max(var4[1], var6.method_10264());
         var4[2] = Math.max(var4[2], var6.method_10260());
      }

      fx.o var12 = fx.o.of(var3, var4);
      this.nI = var2.isBlank() ? "the block set" : var2;
      class_2338 var13 = this.X();
      double var7 = Math.max(
         Math.max(var12.minX() - var13.method_10263(), var13.method_10263() - var12.maxX()),
         Math.max(var12.minZ() - var13.method_10260(), var13.method_10260() - var12.maxZ())
      );
      if (var7 > 24.0) {
         this.Q("stand next to " + this.nI + " to mine it out (you are " + (int)var7 + " blocks away)");
      } else {
         int var9 = this.i();
         int var10 = var9 < 0 && this.R() >= 0 ? 1 : 0;
         if (var9 < 0 && var10 == 0) {
            this.Q("put a pickaxe in your hotbar");
         } else {
            this.HB = var1;
            this.HU = new fx(var12, this.wy.getValue().intValue(), this.nu.getValue().intValue(), var13.method_10263(), var13.method_10260());
            this.He = mc.field_1687;
            this.nB = mc.field_1724;
            this.iJ.V();
            this.H1 = (boolean)1;
            this.Hz = this.I = 0;
            this.w = System.currentTimeMillis();
            this.wR = this.HS = this.q();
            this.wd = mc.field_1724.field_6012;
            this.wk = 0L;
            String var11 = mc.field_1724.method_31548().method_5438(var10 != 0 ? this.R() : var9).method_7964().getString();
            this.L(
               "mining out "
                  + this.nI
                  + ": "
                  + this.wR
                  + " blocks in "
                  + var12.describe()
                  + " with "
                  + var11
                  + (var10 != 0 ? ", only where its cut stays inside the schematic" : "")
                  + ", from the top down."
            );
            if (this.ij.getValue() && tn.h(var0 -> var0.method_58694(class_9334.field_50075) != null) < 0) {
               this.L("heads up: no food in the hotbar.");
            }

            q8.G(this.getName(), "Block set started: {} cells of {} in {}, {} still standing, tool {}", var1.size(), this.nI, var12.describe(), this.wR, var11);
         }
      }
   }

   private int q() {
      int var1 = 0;

      for (class_2338 var3 : this.HB) {
         if (bv.P(var3)) {
            var1++;
         }
      }

      return var1;
   }

   private void Y() {
      if (!mc.field_1724.method_24828() && !mc.field_1724.method_5799() && !this.w0.G()) {
         bv.i(this);
         this.m("Waiting to land");
      } else {
         this.P();
         this.s();
         this.r();
         if (mc.field_1724.field_6012 - this.wd >= 100) {
            this.wd = mc.field_1724.field_6012;
            this.HS = this.q();
         }

         if (this.Hr) {
            this.t();
         } else {
            if (mc.field_1724.field_6012 - this.ie >= 40) {
               this.x();
            }

            int var1 = this.a();
            if (var1 < 0) {
               if (mc.field_1724.field_6012 - this.ie > 2) {
                  this.x();
               } else {
                  byte var5 = 0;

                  for (class_2338 var4 : this.j) {
                     if (this.wt.containsKey(var4) && !this.nc.contains(var4) && bv.P(var4)) {
                        this.wt.remove(var4);
                        var5 = 1;
                     }
                  }

                  if (var5 != 0) {
                     q8.v(this.getName(), "Retrying block-set cells set aside");
                     this.w0.i();
                  } else {
                     this.w0.i();
                     this.HS = this.q();
                     if (this.n7.getValue()) {
                        this.Hr = (boolean)1;
                        this.m("Picking up drops");
                        q8.G(this.getName(), "Picking up drops around {}", this.nI);
                     } else {
                        this.N();
                     }
                  }
               }
            } else {
               this.Hz = var1;
               fx.i var2 = this.HU.V(var1);
               class_2338 var3 = this.w0.G() && this.w0.y() != null ? null : this.D(var2);
               if (var3 != null) {
                  this.w0.i();
                  this.Q(var3, null);
               } else {
                  bv.i(this);
                  this.j(var2);
               }
            }
         }
      }
   }

   private void x() {
      this.ie = mc.field_1724.field_6012;
      this.j.clear();
      this.wT.clear();
      int var1 = mc.field_1724.field_6012;
      this.wt.values().removeIf(var1x -> var1x <= var1);

      for (class_2338 var3 : this.HB) {
         if (mc.field_1687.method_22340(var3) && bv.P(var3)) {
            this.j.add(var3);
         }
      }

      this.j
         .sort(
            Comparator.<class_2338>comparingInt(var1x -> this.HU.J(var1x.method_10264()))
               .thenComparingLong(var1x -> this.HU.l(var1x.method_10263(), var1x.method_10264(), var1x.method_10260(), this.HU.J(var1x.method_10264())))
         );
   }

   private int a() {
      for (class_2338 var2 : this.j) {
         int var3 = this.HU.J(var2.method_10264());
         if (var3 >= 0 && this.c(var2, this.HU.V(var3))) {
            return var3;
         }
      }

      return -1;
   }

   private void j(fx.i var1) {
      class_2338 var2 = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.2, mc.field_1724.method_23321());
      if (!this.w0.G() || mc.field_1724.field_6012 - this.H4 > 100) {
         this.H4 = mc.field_1724.field_6012;
         class_243 var3 = mc.field_1724.method_33571();
         this.i.clear();
         this.j
            .stream()
            .filter(var2x -> this.c(var2x, var1))
            .sorted(Comparator.comparingDouble(var1x -> var3.method_1025(class_243.method_24953(var1x))))
            .limit(8L)
            .forEach(this.i::add);
         if (this.i.isEmpty()) {
            this.w0.i();
            return;
         }

         final double var4 = Math.pow(this.Y() - 0.4, 2.0);
         final double var6 = mc.field_1724.method_5751();
         final ArrayList var8 = new ArrayList<>(this.i);
         g8.i var9 = new g8.i() {
            @Override
            public boolean reached(int var1, int var2x, int var3x) {
               for (class_2338 var5 : var8) {
                  if (var5.method_10263() != var1 || var5.method_10260() != var3x || var5.method_10264() > var2x || var5.method_10264() < var2x - 1) {
                     double var6x = var1 + 0.5 - (var5.method_10263() + 0.5);
                     double var8x = var2x + var6 - (var5.method_10264() + 0.5);
                     double var10 = var3x + 0.5 - (var5.method_10260() + 0.5);
                     if (var6x * var6x + var8x * var8x + var10 * var10 <= var4) {
                        return true;
                     }
                  }
               }

               return false;
            }

            @Override
            public double estimate(int var1, int var2x, int var3x) {
               double var4x = Double.MAX_VALUE;

               for (class_2338 var7 : var8) {
                  var4x = Math.min(
                     var4x,
                     Math.sqrt(
                        Math.pow(var1 + 0.5 - var7.method_10263() - 0.5, 2.0)
                           + Math.pow(var2x + var6 - var7.method_10264() - 0.5, 2.0)
                           + Math.pow(var3x + 0.5 - var7.method_10260() - 0.5, 2.0)
                     )
                  );
               }

               return Math.max(0.0, var4x - Math.sqrt(var4));
            }
         };
         fx.o var10 = this.HU.X();
         g8.l var11 = new g8.l(
            Math.min(var10.minX(), var2.method_10263()) - 8,
            Math.min(var10.minY(), var2.method_10264()) - 4,
            Math.min(var10.minZ(), var2.method_10260()) - 8,
            Math.max(var10.maxX(), var2.method_10263()) + 8,
            Math.max(var10.maxY(), var2.method_10264()) + 4,
            Math.max(var10.maxZ(), var2.method_10260()) + 8
         );
         cm var12 = new cm(var1x -> this.n(var1x, this.R()), false);
         if (this.nT) {
            var12.M(this.HZ.X().minX(), this.HZ.X().minY(), this.HZ.X().minZ(), this.HZ.X().maxX(), this.HZ.X().maxY(), this.HZ.X().maxZ());
         } else {
            var12.S(this.HB);
         }

         g8.t var13 = g8.O(var12, var2.method_10263(), var2.method_10264(), var2.method_10260(), var9, var11, g8.b.digging(12000));
         if (!var13.found() || var13.steps().isEmpty()) {
            this.w0.i();
            String var14 = var13.found() ? "not visible from any standing spot nearby" : "no way to stand within reach";

            for (class_2338 var16 : this.i) {
               this.j(var16, var14);
            }

            q8.v(this.getName(), "Block set: {} for {} cells near {}", var14, this.i.size(), this.i.getFirst().method_23854());
            if (this.O() >= 3) {
               this.e(var1, var14);
            }

            this.wK++;
            return;
         }

         this.w0.X(var13.steps());
         q8.v(this.getName(), "Block set route of {} steps toward {}", var13.steps().size(), this.i.getFirst().method_23854());
      }

      e3.l var17 = this.w0.d(this.Y(), var1x -> this.n(var1x, this.R()), this.H2.getValue());
      this.iT = this.w0.g();
      this.HE = this.w0.t();
      this.i4 = this.w0.V();
      this.wx = this.w0.C();
      this.m("Walking to " + (this.i.isEmpty() ? "the next block" : this.i.getFirst().method_23854()));
      if (var17 == e3.l.L) {
         this.w0.i();
         this.iG = 0;
      } else if (var17 == e3.l.s) {
         q8.v(this.getName(), "Block set route stuck: {}", this.w0.D());
         this.w0.i();
         if (++this.iG >= 3) {
            this.iG = 0;

            for (class_2338 var5 : this.i) {
               this.j(var5, "stuck on the way");
            }
         }
      }
   }

   private void w() {
      if (this.nz > 0) {
         q8.G(this.getName(), "Gave up on {} blocks in the last 10 s (first {}): {}", this.nz, this.Hy, this.io);
         this.nz = 0;
         this.Hy = this.io = null;
      }

      if (this.iH != 0) {
         q8.G(this.getName(), "Set aside {} blocks in {} places in the last 10 s (first around {}): {}", this.iY, this.iH, this.wa, this.HA);
         this.iY = this.iH = 0;
         this.wa = this.HA = null;
      }
   }

   private Set<Long> u(int var1) {
      class_2338 var2 = this.X();
      long var3 = bg.Y(var2.method_10263(), var2.method_10260());
      int var5 = mc.field_1724.field_6012;
      if (var1 != this.ir || this.iB != this.W || var5 - this.H9 >= 40 || var3 != this.HD && !this.n9.contains(var3)) {
         this.HD = var3;
         this.ir = var1;
         this.W = this.iB;
         this.H9 = var5;
         fx.o var6 = this.HU.X();
         bg.t var7 = new bg.t(
            Math.min(var6.minX(), var2.method_10263()) - 2,
            Math.min(var6.minZ(), var2.method_10260()) - 2,
            Math.max(var6.maxX(), var2.method_10263()) + 2,
            Math.max(var6.maxZ(), var2.method_10260()) + 2
         );
         HashSet var8 = new HashSet();
         ArrayDeque var9 = new ArrayDeque();
         var8.add(var3);
         var9.add(new long[]{var2.method_10263(), var2.method_10260()});
         this.c = (boolean)(this.U.getValue() && this.HB == null ? 1 : 0);

         try {
            while (!var9.isEmpty() && var8.size() < 20000) {
               long[] var10 = (long[])var9.poll();

               for (int[] var14 : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                  int var15 = (int)var10[0] + var14[0];
                  int var16 = (int)var10[1] + var14[1];
                  long var17 = bg.Y(var15, var16);
                  if (!var8.contains(var17) && var7.contains(var15, var16) && this.d(var15, var1, var16)) {
                     var8.add(var17);
                     var9.add(new long[]{var15, var16});
                  }
               }
            }
         } finally {
            this.c = (boolean)0;
         }

         this.n9 = var8;
         return this.n9;
      } else {
         return this.n9;
      }
   }

   private boolean b(class_2338 var1, fx.i var2) {
      Set var3 = this.u(var2.bottom());
      if (var3.size() > 4000) {
         return true;
      }

      double var4 = this.Y() - 0.4;
      double var6 = var2.bottom() + mc.field_1724.method_5751();
      class_243 var8 = class_243.method_24953(var1);

      for (long var10 : var3) {
         double var12 = bg.S(var10) + 0.5 - var8.field_1352;
         double var14 = var6 - var8.field_1351;
         double var16 = bg.R(var10) + 0.5 - var8.field_1350;
         if (var12 * var12 + var14 * var14 + var16 * var16 <= var4 * var4) {
            return true;
         }
      }

      return false;
   }

   private int O() {
      if (this.iB != this.nW) {
         this.nW = this.iB;
         this.wK = 0;
      }

      return this.wK;
   }

   private void N(class_2338 var1, fx.i var2, String var3) {
      int var4 = mc.field_1724.field_6012;
      int var5 = 0;

      for (class_2338 var7 : this.j) {
         if (!var7.equals(var1) && var7.method_65076(var1) <= 2 && this.c(var7, var2)) {
            this.wt.put(var7, var4 + 400);
            var5++;
         }
      }

      if (var5 != 0) {
         this.iY += var5;
         this.iH++;
         if (this.wa == null) {
            this.wa = var1.method_23854();
            this.HA = var3;
         }
      }
   }

   private void e(fx.i var1, String var2) {
      int var3 = 0;

      for (class_2338 var5 : this.j) {
         if (this.c(var5, var1)) {
            this.nc.add(var5.method_10062());
            var3++;
         }
      }

      if (var3 != 0) {
         int var6 = this.U.getValue() && this.g() >= 0 ? 1 : 0;
         String var7 = var2 + (var6 != 0 ? "" : ", and there are no blocks in the hotbar to pillar up with");
         q8.G(this.getName(), "Leaving {} blocks of {} at Y {} to {}: {}", var3, this.nI, var1.bottom(), var1.top(), var7);
         this.L("left " + var3 + " blocks at Y " + var1.bottom() + " to " + var1.top() + ": " + var7 + ".");
         this.m("Left " + var3 + " blocks out of reach");
      }
   }

   private void r() {
      if (this.Y != null && !bv.P(this.Y)) {
         if (mc.field_1687.method_8320(this.Y).method_26215()) {
            this.iB++;
            if (this.iW != null) {
               this.wQ.put(this.Y, this.iW);
               int var1 = mc.field_1724.field_6012;
               if (this.wH.size() < 256) {
                  this.wH.put(this.Y, var1);
                  int var2 = j(Math.sqrt(mc.field_1724.method_33571().method_1025(class_243.method_24953(this.Y))));
                  this.iz.put(this.Y, var2);
                  this.nt[var2]++;
               }

               this.k
                  .put(
                     this.Y,
                     String.format(
                        Locale.ROOT,
                        "broken with %s%s after %d ticks on it, %d ticks after the break before, %s",
                        mc.field_1724.method_6047().method_7964().getString(),
                        this.iK ? " as a drill face" : "",
                        var1 - this.wG,
                        var1 - this.wn,
                        mc.field_1724.method_24828() ? "on the ground" : "in the air"
                     )
                  );
               this.wn = var1;
               if (this.iK && this.iU.size() > 1 && this.R()) {
                  this.nd = var1 + 2 + this.iV.nextInt(3);
                  this.HT.clear();
                  this.HT.addAll(this.iU.keySet());
                  this.H6 = var1 + 10;
               }
            }
         }

         for (Entry var4 : this.iU.entrySet()) {
            if (!((class_2338)var4.getKey()).equals(this.Y) && mc.field_1687.method_8320((class_2338)var4.getKey()).method_26215()) {
               this.iB++;
               this.wQ.put((class_2338)var4.getKey(), (class_2680)var4.getValue());
            }
         }

         this.iU.clear();
         this.Y = null;
         this.iW = null;
      }
   }

   private class_2338 c(class_2338 var1) {
      if (this.H7 != null && !var1.equals(this.H7)) {
         this.H0.add(var1.method_10062());
      }

      class_2680 var2 = mc.field_1687.method_8320(var1);
      class_2680 var3 = this.wQ.remove(var1);
      String var4 = this.k.remove(var1);
      if (var3 != null && var3.method_26204() == var2.method_26204() && !(var2.method_26204() instanceof class_2346)) {
         q8.G(this.getName(), "Server put back {} ({}): {}", var1.method_23854(), var2.method_26204().method_9518().getString(), var4);
         this.U(var1);
      }

      if (!var1.equals(this.Y)) {
         this.wG = mc.field_1724.field_6012;
      }

      this.Y = var1;
      this.iW = var2;
      this.i1 = var1.method_10062();
      return var1;
   }

   private void E(fx.i var1) {
      this.ie = mc.field_1724.field_6012;
      this.j.clear();
      this.wT.clear();
      fx.o var2 = this.HU.X();
      int var3 = mc.field_1724.field_6012;
      this.wt.values().removeIf(var1x -> var1x <= var3);

      for (int var4 = var1.top(); var4 >= var1.bottom(); var4--) {
         for (int var5 = var2.minX(); var5 <= var2.maxX(); var5++) {
            for (int var6 = var2.minZ(); var6 <= var2.maxZ(); var6++) {
               class_2338 var7 = new class_2338(var5, var4, var6);
               if (mc.field_1687.method_22340(var7)) {
                  if (!mc.field_1687.method_8316(var7).method_15769()) {
                     this.wT.add(var7);
                  } else if (bv.P(var7)) {
                     this.j.add(var7);
                  }
               }
            }
         }
      }

      int var8 = this.Hz;
      this.j.sort(Comparator.comparingLong(var2x -> {
         Integer var3x = this.nK == var8 ? this.w6.get(com.larpclient.wy.y(var2x.method_10263(), var2x.method_10264(), var2x.method_10260())) : null;
         return var3x != null ? var3x.intValue() : 1125899906842624L + this.HU.l(var2x.method_10263(), var2x.method_10264(), var2x.method_10260(), var8);
      }));
   }

   private void U(class_2338 var1) {
      int var2 = this.r.merge(var1, 1, Integer::sum);
      if (var2 >= 3) {
         this.r.remove(var1);
         this.ix.add(var1.method_10062());
         this.wt.put(var1, mc.field_1724.field_6012 + 2400);
         this.L(var1.method_23854() + ": the server keeps putting it back, skipping it for now.");
         q8.G(this.getName(), "Server restored {} {} times; skipping for {} ticks", var1.method_23854(), 3, 2400);
      }
   }

   private boolean n() {
      int var1 = mc.field_1724.field_6012;
      if (var1 - this.iS >= 600) {
         this.iS = var1;
         int var2 = 0;

         for (int var6 : this.nt) {
            var2 += var6;
         }

         if (var2 > 0) {
            q8.G(
               this.getName(),
               "Breaks put back by eye distance: under 2 {}/{}, 2-3 {}/{}, 3-3.5 {}/{}, 3.5-4 {}/{}, 4+ {}/{}",
               this.iC[0],
               this.nt[0],
               this.iC[1],
               this.nt[1],
               this.iC[2],
               this.nt[2],
               this.iC[3],
               this.nt[3],
               this.iC[4],
               this.nt[4]
            );
         }
      }

      class_243 var7 = mc.field_1724.method_33571();
      this.wH
         .entrySet()
         .removeIf(
            var3 -> {
               int var4 = var1 - var3.getValue();
               if (var4 <= 200 && var4 >= 0) {
                  class_2680 var5 = mc.field_1687.method_8320(var3.getKey());
                  if (var5.method_26215()) {
                     return false;
                  }

                  Integer var6x = this.iz.remove(var3.getKey());
                  if (var6x != null) {
                     this.iC[var6x]++;
                  }

                  bv.M(this.getName(), var3.getKey());
                  q8.G(
                     this.getName(),
                     "{} came back as {} {} ticks after the break, {} blocks from the eye",
                     var3.getKey().method_23854(),
                     var5.method_26204().method_9518().getString(),
                     var4,
                     String.format(Locale.ROOT, "%.1f", Math.sqrt(var7.method_1025(class_243.method_24953((class_2382)var3.getKey()))))
                  );
                  if (var4 <= 5 && !(var5.method_26204() instanceof class_2346)) {
                     this.HM.c(var3.getKey().method_10063());
                  }

                  return true;
               } else {
                  this.iz.remove(var3.getKey());
                  return true;
               }
            }
         );
      if (!this.HM.r()) {
         return false;
      }

      q8.G(this.getName(), "Stopping: the server took back {} of the last {} breaks", this.HM.c(), 10);
      bv.i(this);
      this.Q("the server put back " + this.HM.c() + " of your last 10 breaks, so it is refusing them and digging on only draws attention");
      return true;
   }

   private void P() {
      if (mc.field_1724.field_6012 - this.n4 >= 600) {
         this.n4 = mc.field_1724.field_6012;
         this.r.clear();
      }
   }

   private boolean X(class_2338 var1) {
      Integer var2 = this.wt.get(var1);
      return var2 != null && var2 > mc.field_1724.field_6012 || this.nc.contains(var1);
   }

   private void j(class_2338 var1, String var2) {
      int var3 = this.M.merge(var1, 1, Integer::sum);
      if (var3 >= 3) {
         this.nc.add(var1);
         if (this.nz++ == 0) {
            this.Hy = var1.method_23854();
            this.io = var2;
         }
      } else {
         this.wt.put(var1, mc.field_1724.field_6012 + 400);
         q8.v(this.getName(), "Skipping {} for now: {}", var1.method_23854(), var2);
      }

      this.m("Set aside " + var1.method_23854() + ": " + var2);
      if (var1.equals(this.Y)) {
         this.Y = null;
      }

      if (var1.equals(this.iZ)) {
         this.iZ = null;
      }
   }

   private boolean S(class_2338 var1) {
      for (class_2350 var5 : class_2350.values()) {
         if (var5 != class_2350.field_11033 && !mc.field_1687.method_8316(var1.method_10093(var5)).method_15769()) {
            return true;
         }
      }

      return false;
   }

   private boolean R(class_2338 var1) {
      if (this.H1) {
         return true;
      }

      int var2 = this.HU.c(var1.method_10263(), var1.method_10260(), this.Hz);
      return !this.HU.S(var2, this.Hz) ? true : this.wB.contains(this.C(var1.method_10263(), var1.method_10260()));
   }

   private class_2338 D(fx.i var1) {
      double var2 = this.Y();
      class_243 var4 = mc.field_1724.method_33571();
      if (this.Y == null
         || !this.c(this.Y, var1)
         || !(var4.method_1025(class_243.method_24953(this.Y)) <= var2 * var2)
         || (this.iK ? !this.I(this.Y) : !this.W(this.Y, bv.z(this, this.Y, var2)))) {
         this.Y = null;
         this.iW = null;
         this.iK = (boolean)0;
         this.iU.clear();
         if (!this.H1 && this.R() >= 0) {
            class_2338 var5 = this.U(var1, var2, var4);
            if (var5 != null) {
               this.iK = (boolean)1;
               this.iU.clear();
               int var15 = this.HU.c(var5.method_10263(), var5.method_10260(), this.Hz);
               boolean var17 = this.HU.l();

               for (int var18 = -1; var18 <= 1; var18++) {
                  for (int var19 = -1; var19 <= 1; var19++) {
                     class_2338 var21 = var17 ? var5.method_10069(0, var18, var19) : var5.method_10069(var19, var18, 0);
                     if (bv.P(var21)) {
                        this.iU.put(var21, mc.field_1687.method_8320(var21));
                     }
                  }
               }

               q8.v(this.getName(), "Drill face at {} in row {} ({} blocks)", var5.method_23854(), var15 + 1, this.iU.size());
               return this.c(var5);
            }
         }

         ArrayList var13 = new ArrayList();

         for (class_2338 var7 : this.j) {
            if (this.c(var7, var1) && this.R(var7) && !(var4.method_1025(class_243.method_24953(var7)) > var2 * var2)) {
               var13.add(var7);
            }
         }

         float var14 = mc.field_1724.method_36454();
         float var16 = mc.field_1724.method_36455();
         class_2338 var8 = this.i1;
         var13.sort(Comparator.comparingDouble(var6 -> Y(var4, var6, var14, var16, var2, var8)));
         int var9 = 0;

         for (class_2338 var11 : var13) {
            if (var9++ >= 16) {
               break;
            }

            if (this.W(var11, bv.z(this, var11, var2))) {
               return this.c(var11);
            }
         }

         for (class_2338 var22 : var13.stream()
            .sorted(Comparator.comparingLong(var1x -> this.HU.l(var1x.method_10263(), var1x.method_10264(), var1x.method_10260(), this.Hz)))
            .limit(4L)
            .toList()) {
            class_2338 var12 = this.v(var4, var22, var2);
            if (var12 != null && this.c(var12, var1) && this.V(var12) && this.W(var12, bv.z(this, var12, var2))) {
               q8.v(this.getName(), "Digging through {} toward {}", var12.method_23854(), var22.method_23854());
               return this.c(var12);
            }
         }

         return null;
      } else {
         return this.Y;
      }
   }

   static double Y(class_243 var0, class_2338 var1, float var2, float var3, double var4, class_2338 var6) {
      double var7 = Math.sqrt(var0.method_1025(class_243.method_24953(var1)));
      double var9 = k(var0, var1, var2, var3) + 10.0 * var7;
      if (var7 > var4 - 0.6) {
         var9 += 40.0;
      }

      if (var6 != null && var1.method_19455(var6) == 1) {
         var9 -= 15.0;
      }

      return var9;
   }

   static double k(class_243 var0, class_2338 var1, float var2, float var3) {
      float[] var4 = t9.X(var0, class_243.method_24953(var1));
      return Math.hypot(class_3532.method_15393(var4[0] - var2), var4[1] - var3);
   }

   private class_2338 v(class_243 var1, class_2338 var2, double var3) {
      class_243 var5 = class_243.method_24953(var2);
      if (var1.method_1025(var5) > var3 * var3) {
         return null;
      }

      class_3965 var6 = mc.field_1687.method_17742(new class_3959(var1, var5, class_3960.field_17559, class_242.field_1348, mc.field_1724));
      return var6.method_17783() == class_240.field_1332 && !var6.method_17777().equals(var2) ? var6.method_17777() : null;
   }

   private boolean c(class_2338 var1, fx.i var2) {
      return var2.contains(var1.method_10264()) && this.u(var1) && bv.P(var1) && !this.X(var1) && !this.S(var1) && !this.K(var1);
   }

   private boolean K(class_2338 var1) {
      class_238 var2 = mc.field_1724.method_5829();
      return var1.method_10264() == this.v() - 1
         && var2.field_1320 > var1.method_10263()
         && var2.field_1323 < var1.method_10263() + 1
         && var2.field_1324 > var1.method_10260()
         && var2.field_1321 < var1.method_10260() + 1;
   }

   private int R() {
      String var1 = this.F.getValue().trim().toLowerCase(Locale.ROOT);
      if (!var1.isEmpty() && this.isInGame()) {
         class_1661 var2 = mc.field_1724.method_31548();

         for (int var3 = 0; var3 < 9; var3++) {
            class_1799 var4 = var2.method_5438(var3);
            if (!var4.method_7960() && var4.method_7964().getString().toLowerCase(Locale.ROOT).contains(var1)) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private long C(int var1, int var2) {
      int var3 = this.HU.c(var1, var2, this.Hz);
      int var4 = this.HU.l() ? var1 : var2;
      return (long)var3 << 32 ^ var4 & 4294967295L;
   }

   private class_2338 P(int var1, int var2, fx.i var3) {
      int var4 = this.HU.b(var1, this.Hz);
      return this.HU.l() ? new class_2338(var2, var3.bottom() + 1, var4) : new class_2338(var4, var3.bottom() + 1, var2);
   }

   private class_2350 O(int var1) {
      int var2 = this.HU.l(var1, this.Hz);
      return this.HU.l() ? (var2 > 0 ? class_2350.field_11039 : class_2350.field_11034) : (var2 > 0 ? class_2350.field_11043 : class_2350.field_11035);
   }

   private boolean I(class_2338 var1) {
      if (!var1.equals(this.HH)) {
         return this.v(var1);
      }

      class_3965 var2 = bv.z(this, var1, this.Y());
      return var2 != null && var2.method_17780() == this.HL;
   }

   private boolean v(class_2338 var1) {
      class_3965 var2 = bv.z(this, var1, this.Y());
      int var3 = this.HU.c(var1.method_10263(), var1.method_10260(), this.Hz);
      return var2 != null && var2.method_17780() == this.O(var3);
   }

   private class_2338 U(fx.i var1, double var2, class_243 var4) {
      HashSet var5 = new HashSet();

      for (class_2338 var7 : this.j) {
         if (this.c(var7, var1)) {
            int var8 = this.HU.c(var7.method_10263(), var7.method_10260(), this.Hz);
            if (this.HU.S(var8, this.Hz) && !this.wB.contains(this.C(var7.method_10263(), var7.method_10260()))) {
               long var9 = this.C(var7.method_10263(), var7.method_10260());
               if (var5.add(var9)) {
                  class_2338 var11 = this.P(var8, this.HU.l() ? var7.method_10263() : var7.method_10260(), var1);
                  if (this.nc.contains(var11) || !this.X(var11) || !bv.P(var11)) {
                     if (this.c(var11, var1)) {
                        if (!(var4.method_1025(class_243.method_24953(var11)) > var2 * var2) && this.v(var11)) {
                           return var11;
                        }

                        return null;
                     }

                     this.wB.add(var9);
                  }
               }
            }
         }
      }

      return null;
   }

   private double Y() {
      return Math.min(this.it.getValue(), mc.field_1724.method_55754());
   }

   private void b(class_2338 var1, boolean var2) {
      this.K(var1, var2, null);
   }

   private void K(class_2338 var1, boolean var2, String var3) {
      int var4 = this.R();
      class_2338 var5 = var1.method_10062();
      if (!this.V(var5)) {
         bv.i(this);
         this.no = null;
         this.j(var5, "out of reach from here");
      } else {
         if (var5.equals(this.no)) {
            if (var5.equals(this.HH) && this.im >= 60 && !this.i(var5)) {
               q8.G(this.getName(), "Drill hit on {} dropped: the crosshair stayed off its {} face for {} ticks", var5.method_23854(), this.HL, 60);
               this.nf.add(var5);
               this.HH = null;
               this.iK = (boolean)0;
               this.iU.clear();
               this.K(var5, false, var3);
               return;
            }

            if (++this.im > 200) {
               class_3965 var6 = bv.L(var5, this.Y());
               q8.G(
                  this.getName(),
                  "Could not break {} ({}) in {} ticks; crosshair {}, distance {}, phase {}",
                  var5.method_23854(),
                  class_7923.field_41175.method_10221(mc.field_1687.method_8320(var5).method_26204()).method_12832(),
                  200,
                  var6 == null ? "off the block" : "on " + var6.method_17780(),
                  String.format(Locale.ROOT, "%.2f", Math.sqrt(mc.field_1724.method_33571().method_1025(class_243.method_24953(var5)))),
                  this.nD
               );
               this.no = null;
               this.im = 0;
               bv.i(this);
               this.wV = null;
               this.j(var5, "could not break it");
               return;
            }
         } else {
            this.no = var5;
            this.im = 0;
         }

         if (var4 < 0 || !mc.field_1724.method_5715() && !mc.field_1724.method_18276() || !var2 && this.n(mc.field_1687.method_8320(var5), var4) != var4) {
            this.p(var5, var2 || var4 >= 0 && this.n(mc.field_1687.method_8320(var5), var4) == var4);
            boolean var7 = bv.q(
               this,
               var5,
               this.Y(),
               true,
               12.0F,
               var4x -> {
                  int var5x = var2 && var4 >= 0 ? var4 : this.n(var4x, var4);
                  this.iR = var5x;
                  if (var5x >= 0 && this.v(var5x) && this.Hi.add(mc.field_1724.method_31548().method_5438(var5x).method_7909())) {
                     q8.G(
                        this.getName(),
                        "Auto Tool: {} for {}",
                        mc.field_1724.method_31548().method_5438(var5x).method_7964().getString(),
                        class_7923.field_41175.method_10221(var4x.method_26204()).method_12832()
                     );
                  }

                  if (var5x == var4 && this.wP.G("Auto") && this.Z == null && this.H7 == null) {
                     this.p(var5);
                  }

                  return var5x;
               },
               () -> !this.U() && !this.S(var5) && this.C(var5) && this.i(var5)
            );
            this.m(!var7 ? "Waiting for a reachable face" : (var3 != null ? var3 : (var2 ? "Cutting a 3x3 face at " : "Mining ") + var5.method_23854()));
            if (!var7) {
               this.j(var5, "no reachable face");
            }
         } else {
            this.n3 = 0;
            bv.i(this);
            this.m("Standing up before the drill hit");
         }
      }
   }

   private void p(class_2338 var1, boolean var2) {
      if (var2 || !this.i0.getValue()) {
         this.n3 = 0;
      } else if (this.H8) {
         this.i4 = (boolean)1;
      } else {
         int var3 = mc.field_1724.field_6012;
         if (!var1.equals(this.HR)) {
            this.HR = var1;
            if (var3 >= this.n3 && this.iV.nextDouble() < 0.015) {
               this.n3 = var3 + this.iV.nextInt(20, 51);
            }
         }

         if (var3 < this.n3) {
            this.i4 = (boolean)1;
         }
      }
   }

   private boolean C(class_2338 var1) {
      int var2 = mc.field_1724.field_6012;
      if (!var1.equals(this.Hj)) {
         this.Hj = var1.method_10062();
         this.HP = -1000000000;
         this.R = z(this.iV.nextDouble());
      }

      if (this.HP == -1000000000) {
         if (bv.L(var1, this.Y()) == null) {
            return false;
         }

         this.HP = var2;
      }

      return var2 - this.HP >= this.R;
   }

   static int z(double var0) {
      return (int)Math.min(6.0, Math.floor(-Math.log(1.0 - Math.min(var0, 0.999999)) * 1.3));
   }

   private boolean i(class_2338 var1) {
      if (this.HH != null && var1.equals(this.HH)) {
         class_3965 var2 = bv.L(var1, this.Y());
         return var2 != null && var2.method_17780() == this.HL;
      } else {
         return true;
      }
   }

   private int i() {
      class_2680 var1 = class_2246.field_10340.method_9564();
      int var2 = this.h(var1, this.R());
      return var2 >= 0 && mc.field_1724.method_31548().method_5438(var2).method_7951(var1) ? var2 : -1;
   }

   private int n(class_2680 var1, int var2) {
      int var3 = this.i();
      class_1661 var4 = mc.field_1724.method_31548();
      int var5 = var3 >= 0 ? var3 : (var2 >= 0 ? var2 : var4.method_67532());
      if (!this.wl.getValue()) {
         return var5;
      }

      ArrayList var6 = new ArrayList(9);

      for (int var7 = 0; var7 < 9; var7++) {
         var6.add(var4.method_5438(var7));
      }

      int var9 = r(var6, var1, var2, var5, var3 < 0, this.n());
      int var8 = var4.method_67532();
      return var8 != var9 && var8 >= 0 && var8 < 9 && this.h((class_1799)var6.get(var8), (class_1799)var6.get(var9), var1, var8 == var2 && var3 >= 0)
         ? var8
         : var9;
   }

   private boolean h(class_1799 var1, class_1799 var2, class_2680 var3, boolean var4) {
      if (!var1.method_7960() && var1.method_7963() && !var4 && !var1.method_31573(class_3489.field_42611) && tn.p(var1) > this.n()) {
         e9.r var5 = e9.s(var1, var3, true);
         e9.r var6 = e9.s(var2, var3, true);
         return var5.usable() && var5.harvest() == var6.harvest() && var5.speed() >= var6.speed();
      } else {
         return false;
      }
   }

   static int r(List<class_1799> var0, class_2680 var1, int var2, int var3, boolean var4, int var5) {
      ArrayList var6 = new ArrayList(var0.size());

      for (int var7 = 0; var7 < var0.size(); var7++) {
         class_1799 var8 = (class_1799)var0.get(var7);
         e9.r var9 = e9.s(var8, var1, true);
         int var10 = var7 != var3 && (var8.method_7960() || var8.method_31573(class_3489.field_42611) || var7 == var2 && !var4 || tn.p(var8) <= var5) ? 0 : 1;
         var6.add(var10 != 0 ? var9 : new e9.r(var9.speed(), var9.harvest(), false));
      }

      return e9.J(var6, var3);
   }

   private int n() {
      return this.wM.getValue() ? this.wI.getValue().intValue() : 1;
   }

   private boolean v(int var1) {
      return var1 != this.R() && var1 != this.i();
   }

   private boolean D() {
      return this.R() >= 0 && this.i() < 0;
   }

   private boolean R() {
      if (this.wP.G("3x3")) {
         return true;
      } else {
         return this.wP.G("One Block") ? false : !Boolean.FALSE.equals(this.Z);
      }
   }

   private void Q() {
      if (!this.ii && this.wP.G("Auto") && this.Z == null) {
         this.ii = (boolean)1;
         this.L(
            "blocks are left because a 3x3 drill cut would reach outside the area, and no drill hit has shown yet how your drill cuts. If it is a normal pickaxe, set Drill Cut to One Block, or add a plain pickaxe to the hotbar."
         );
         q8.G(this.getName(), "Drill Cut hint sent: drill-only, cut unknown, blocks left for cutting outside the area");
      }
   }

   private void p(class_2338 var1) {
      class_3965 var2 = bv.L(var1, this.Y());
      class_2350 var3 = var2 != null ? var2.method_17780() : class_2350.field_11036;
      this.H7 = var1;
      this.w5.clear();
      class_2351 var4 = var3.method_10166();

      for (int var5 = -1; var5 <= 1; var5++) {
         for (int var6 = -1; var6 <= 1; var6++) {
            if (var5 != 0 || var6 != 0) {
               class_2338 var7 = var4 == class_2351.field_11048
                  ? var1.method_10069(0, var5, var6)
                  : (var4 == class_2351.field_11052 ? var1.method_10069(var5, 0, var6) : var1.method_10069(var5, var6, 0));
               if (bv.P(var7)) {
                  this.w5.put(var7.method_10062(), mc.field_1687.method_8320(var7));
               }
            }
         }
      }

      this.Hl = 0;
      this.H0.clear();
      q8.v(this.getName(), "Watching the first drill hit at {} on {} ({} blocks around it)", var1.method_23854(), var3, this.w5.size());
   }

   private void s() {
      if (this.H7 != null) {
         if (this.w5.isEmpty()) {
            this.H7 = null;
         } else {
            if (this.Hl == 0) {
               if (bv.P(this.H7)) {
                  return;
               }

               this.Hl = mc.field_1724.field_6012 + 10;
            }

            int var1 = 0;
            int var2 = 0;

            for (class_2338 var4 : this.w5.keySet()) {
               if (!this.H0.contains(var4)) {
                  var2++;
                  if (mc.field_1687.method_8320(var4).method_26215()) {
                     var1++;
                  }
               }
            }

            int var5 = var2 > 0 && var1 * 2 >= var2 ? 1 : 0;
            if (var5 != 0 || var2 == 0 || mc.field_1724.field_6012 >= this.Hl) {
               if (var5 != 0) {
                  this.Z = true;
                  this.nq = 0;
                  q8.G(this.getName(), "Drill check: {} of {} blocks around the hit broke, so the drill cuts 3x3", var1, var2);
                  this.L("your drill cuts 3x3 here, so it mines full 3x3 faces.");
               } else if (var2 > 0 && ++this.nq >= 3) {
                  this.Z = false;
                  q8.G(
                     this.getName(),
                     "Drill check: {} hits in a row broke one block ({} of {} around the last), so the drill breaks one block at a time",
                     3,
                     var1,
                     var2
                  );
                  this.L("your drill broke one block on 3 hits in a row here, so it is used like a normal pickaxe. Set Drill Cut to 3x3 if it really cuts 3x3.");
                  this.H1 = (boolean)1;
                  this.nc.removeAll(this.w_);
                  this.M.keySet().removeAll(this.w_);
                  this.w_.clear();
                  this.wt.clear();
               } else {
                  q8.G(this.getName(), "Drill check: {} of {} blocks around the hit broke; checking the next drill hit too", var1, var2);
               }

               this.H7 = null;
               this.w5.clear();
               this.H0.clear();
            }
         }
      }
   }

   private boolean W(class_2338 var1, class_2350 var2) {
      class_2351 var3 = var2.method_10166();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            class_2338 var6 = var3 == class_2351.field_11048
               ? var1.method_10069(0, var4, var5)
               : (var3 == class_2351.field_11052 ? var1.method_10069(var4, 0, var5) : var1.method_10069(var4, var5, 0));
            if (!this.u(var6) && bv.P(var6)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean u(class_2338 var1) {
      return this.HU.X().contains(var1.method_10263(), var1.method_10264(), var1.method_10260()) && (this.HB == null || this.HB.contains(var1));
   }

   private boolean W(class_2338 var1, class_3965 var2) {
      if (var2 == null) {
         return false;
      }

      if (this.D() && this.R() && !this.W(var1, var2.method_17780())) {
         if (this.n(mc.field_1687.method_8320(var1), this.R()) != this.R()) {
            return true;
         }

         this.w_.add(var1.method_10062());
         return false;
      } else {
         return true;
      }
   }

   private int h(class_2680 var1, int var2) {
      class_1661 var3 = mc.field_1724.method_31548();
      int var4 = -1;
      float var5 = -1.0F;

      for (int var6 = 0; var6 < 9; var6++) {
         if (var6 != var2) {
            class_1799 var7 = var3.method_5438(var6);
            float var8 = var7.method_7924(var1) + (var7.method_7951(var1) ? 1000 : 0);
            if (var8 > var5) {
               var5 = var8;
               var4 = var6;
            }
         }
      }

      return var4;
   }

   private boolean g(fx.i var1) {
      if (this.wT.isEmpty()) {
         return false;
      }

      double var2 = this.Y();
      class_243 var4 = mc.field_1724.method_33571();
      int var5 = this.g();

      for (class_2338 var7 : this.wT) {
         if (!mc.field_1687.method_8316(var7).method_15769()
            && this.L.getOrDefault(var7, 0) < 60
            && !(var4.method_1025(class_243.method_24953(var7)) > var2 * var2)) {
            if (var5 < 0) {
               this.B("no solid block in your hotbar to plug lava or water with");
               return false;
            }

            class_1792 var8 = mc.field_1724.method_31548().method_5438(var5).method_7909();
            class_2338 var9 = var7.method_10062();
            if (fk.c(this, var9, var8, var2, true, true) != null) {
               if (this.U("plug " + (mc.field_1687.method_8316(var9).method_15767(class_3486.field_15518) ? "lava" : "water"))) {
                  return true;
               }

               if (!this.h(var5)) {
                  this.m("Taking out " + mc.field_1724.method_31548().method_5438(var5).method_7964().getString() + " to plug a fluid");
                  return true;
               }

               if (mc.field_1724.field_6012 < this.iv) {
                  bv.i(this);
                  this.m("Steadying before the next block");
                  return true;
               }

               boolean var10 = fk.m(
                  this,
                  var9,
                  var8,
                  var2,
                  true,
                  true,
                  () -> !mc.field_1687.method_8316(var9).method_15769() && !this.U(),
                  () -> this.t(var9, var8, true),
                  var2x -> {
                     this.e(var9);
                     q8.v(this.getName(), "Plug at {} {}", var9.method_23854(), var2x ? "placed" : "rejected");
                  }
               );
               if (var10) {
                  if (this.L.merge(var9, 1, Integer::sum) == 1) {
                     q8.G(
                        this.getName(),
                        "Plugging {} at {}",
                        mc.field_1687.method_8316(var9).method_15767(class_3486.field_15518) ? "lava" : "water",
                        var9.method_23854()
                     );
                  }

                  bv.i(this);
                  this.m("Plugging " + (mc.field_1687.method_8316(var9).method_15767(class_3486.field_15518) ? "lava" : "water") + " at " + var9.method_23854());
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean h(int var1) {
      class_1661 var2 = mc.field_1724.method_31548();
      if (var2.method_67532() != var1) {
         bv.i(this);
         var2.method_61496(var1);
         this.wm = var1;
         this.HN = mc.field_1724.field_6012;
         return false;
      }

      if (this.wm != var1) {
         this.wm = var1;
         this.HN = mc.field_1724.field_6012;
      }

      return mc.field_1724.field_6012 - this.HN >= 3;
   }

   private float E() {
      q586814c6d8f3640b var1 = (q586814c6d8f3640b)mc.field_1724;
      return (float)Math.hypot(class_3532.method_15393(mc.field_1724.method_36454() - var1.larpYRotLast()), mc.field_1724.method_36455() - var1.larpXRotLast());
   }

   private boolean t(class_2338 var1, class_1792 var2, boolean var3) {
      int var4 = mc.field_1724.field_6012;
      if (!var1.equals(this.iM)) {
         this.iM = var1.method_10062();
         this.nx = 0;
         this.ip = A(this.iV.nextDouble());
         this.C = -1000000000;
      }

      if (this.C != var4) {
         this.C = var4;
         int var5 = var3 && !(mc.field_1724.method_18798().method_37268() < 4.0E-4) ? 0 : 1;
         int var6 = var5 != 0 && this.E() <= 1.5F && fk.c(this, var1, var2, this.Y(), false, true) != null ? 1 : 0;
         this.nx = var6 != 0 ? this.nx + 1 : 0;
      }

      return this.nx >= this.ip && var4 >= this.iv;
   }

   static int A(double var0) {
      return 2 + (int)Math.round(4.0 * var0 * var0);
   }

   static int T(double var0) {
      return 8 + (int)Math.round(8.0 * var0 * var0);
   }

   private void e(class_2338 var1) {
      int var2 = mc.field_1724.field_6012;
      this.iv = var2 + T(this.iV.nextDouble());
      this.HK = var1.method_10062();
      this.nZ = var2 + 5;
      this.wf.merge(var1.method_10062(), 1, Integer::sum);
      this.iM = null;
   }

   private boolean U(String var1) {
      class_437 var2 = mc.field_1755;
      if (var2 != null && (!this.allowsScreenInteractions() || var2 instanceof class_465)) {
         this.m("Waiting for the menu to close to " + var1);
         return true;
      } else {
         return false;
      }
   }

   private int g() {
      class_1661 var1 = mc.field_1724.method_31548();
      int var2 = this.R();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var3 != var2) {
            class_1799 var4 = var1.method_5438(var3);
            if (var4.method_7909() instanceof class_1747 var5 && v(var5)) {
               return var3;
            }
         }
      }

      return -1;
   }

   static boolean v(class_1747 var0) {
      class_2248 var1 = var0.method_7711();
      class_2680 var2 = var1.method_9564();
      return !(var1 instanceof class_2346)
         && !var2.method_31709()
         && !var2.method_50011()
         && var2.method_26234(class_2682.field_12294, class_2338.field_10980)
         && var1 != class_2246.field_10295
         && var1 != class_2246.field_10375
         && var1 != class_2246.field_10092;
   }

   private void B(String var1) {
      if (!var1.equals(this.nv)) {
         this.nv = var1;
         this.L(var1 + ".");
      }
   }

   private class_2338 m(fx.i var1) {
      if (this.iB != this.b) {
         this.n.clear();
         this.b = this.iB;
      }

      class_2338 var2 = null;
      class_2338 var3 = null;
      double var4 = 36.0;
      double var6 = mc.field_1724.method_23317();
      double var8 = mc.field_1724.method_23321();
      int var10 = !this.H1 && this.R() >= 0 ? 1 : 0;

      for (class_2338 var12 : this.j) {
         double var13 = var12.method_10263() + 0.5 - var6;
         double var15 = var12.method_10260() + 0.5 - var8;
         double var17 = var13 * var13 + var15 * var15;
         if ((var2 == null || !(var17 >= var4)) && this.c(var12, var1)) {
            boolean var19 = this.R(var12);
            if (var19 || var10 != 0) {
               if (var2 == null) {
                  var2 = var12;
               }

               if (var19 && var17 < var4 && !this.n.contains(var12) && this.Y(var12)) {
                  var4 = var17;
                  var3 = var12;
               }
            }
         }
      }

      this.ny = var3 != null && !var3.equals(var2) ? var3 : null;
      return var3 != null ? var3 : var2;
   }

   private boolean Y(class_2338 var1) {
      for (class_2350 var5 : class_2350.values()) {
         class_2338 var6 = var1.method_10093(var5);
         if (mc.field_1687.method_8320(var6).method_26220(mc.field_1687, var6).method_1110()) {
            return true;
         }
      }

      return false;
   }

   private void t(class_2338 var1, fx.i var2) {
      if (!this.b(var1, var2)) {
         this.j(var1, "no way to walk within reach");
         this.N(var1, var2, "no way to walk within reach");
         this.G();
      } else {
         int var3 = !this.H1 && this.R() >= 0 && !this.R(var1) ? 1 : 0;
         class_2338 var4 = var3 != 0
            ? this.P(this.HU.c(var1.method_10263(), var1.method_10260(), this.Hz), this.HU.l() ? var1.method_10263() : var1.method_10260(), var2)
            : var1;
         if (!var4.equals(this.iZ) || mc.field_1724.field_6012 - this.X > 40 || this.D.isEmpty() && this.iu == 0) {
            this.iZ = var4;
            this.X = mc.field_1724.field_6012;
            List var5 = this.C(var2.bottom(), var3 != 0 ? this.X(var4) : this.r(var4, var2));
            if ((var5 == null || var5.isEmpty()) && var1.equals(this.ny)) {
               this.n.add(var1.method_10062());
               this.ny = null;
               this.G();
               return;
            }

            if (var5 == null || var5.isEmpty()) {
               if (var3 != 0) {
                  this.wB.add(this.C(var1.method_10263(), var1.method_10260()));
                  q8.v(
                     this.getName(), "Opening slice at {} with the pickaxe: {}", var4.method_23854(), var5 == null ? "no route to the row line" : "not in line"
                  );
               } else {
                  if (var5 != null
                     && this.v(mc.field_1724.method_33571(), var1, this.Y()) instanceof class_2338 var6
                     && this.c(var6, var2)
                     && this.W(var6, bv.z(this, var6, this.Y()))) {
                     this.G();
                     this.c(var6);
                     this.iK = (boolean)0;
                     this.b(var6, false);
                     return;
                  }

                  if (var5 != null && this.w_.contains(var1)) {
                     this.Q();
                  }

                  String var8 = var5 == null
                     ? "no way to walk within reach"
                     : (this.w_.contains(var1) ? "the drill would cut outside the area" : "not visible from any standing spot nearby");
                  this.j(var1, var8);
                  if (this.O() >= 2) {
                     this.N(var1, var2, var8);
                  }

                  this.wK++;
               }

               this.G();
               return;
            }

            this.D = var5;
            this.iu = 0;
            this.n3 = 0;
            this.H(var2, var4);
            q8.v(this.getName(), "Route of {} steps toward {}", this.D.size(), var4.method_23854());
         }

         this.I(var2);
      }
   }

   private void H(fx.i var1, class_2338 var2) {
      bg.o var3 = this.D.getLast();
      bg.o var4 = this.nk.isEmpty() ? null : this.nk.getLast();
      int var5 = var2.equals(this.wj) && var3.equals(var4) ? 1 : 0;
      this.nk = this.D;
      this.wj = var2;
      if (var5 == 0) {
         boolean var6 = e3.c(var3.x(), var1.bottom(), var3.z());
         this.H3 = var6 ? 0.0 : (this.iV.nextDouble() * 2.0 - 1.0) * 0.18;
         this.nE = var6 ? 0.0 : (this.iV.nextDouble() * 2.0 - 1.0) * 0.18;
         this.z = var6 ? 0.245 : 0.15 + this.iV.nextDouble() * 0.15;
         this.E = 0.35 + this.iV.nextDouble() * 0.65;
         this.ws = this.iV.nextDouble() * Math.PI * 2.0;
      }
   }

   private class_243 h(int var1, double var2) {
      bg.o var4 = this.D.get(var1);
      int var5 = this.D == this.nk && var1 == this.D.size() - 1 ? 1 : 0;
      return new class_243(var4.x() + 0.5 + (var5 != 0 ? this.H3 : 0.0), var2, var4.z() + 0.5 + (var5 != 0 ? this.nE : 0.0));
   }

   static boolean j(class_243 var0, class_243 var1) {
      double var2 = Math.hypot(var0.field_1352, var0.field_1350);
      double var4 = Math.hypot(var1.field_1352, var1.field_1350);
      return !(var2 < 1.0E-6) && !(var4 < 1.0E-6) ? (var0.field_1352 * var1.field_1352 + var0.field_1350 * var1.field_1350) / (var2 * var4) >= iD : true;
   }

   public static void f(class_746 var0) {
      if (ih != null && ih.isEnabled()) {
         sh var1 = ih;
         if (var1 != null && var1.HU != null && var0 == var1.nB) {
            var1.ic = var0.method_73189();
            var1.Hm = var0.method_18798().method_37267();
         }
      }
   }

   public static void I(class_746 var0) {
      if (ih != null && ih.isEnabled()) {
         sh var1 = ih;
         if (var1 != null && var1.HU != null && var0 == var1.nB && var1.isRuntimeActive()) {
            class_243 var2 = var1.ic;
            var1.ic = null;
            double var3 = var2 == null ? -1.0 : var2.method_1022(var0.method_73189());
            if (!(var3 >= 0.0) || !(var3 < 0.001)) {
               int var5 = ++var1.Hc;
               if (var5 <= 30 || var5 % 10 == 0) {
                  q8.G(
                     var1.getName(),
                     "Server {} you {} blocks (correction {} this run) while: {}; walking {}, sprint {}, sneak {}, on ground {}, speed {}",
                     var3 > 16.0 ? "teleported" : "moved",
                     var3 < 0.0 ? "?" : String.format(Locale.ROOT, "%.2f", var3),
                     var5,
                     var1.getStatus(),
                     var1.iT.method_1027() > 1.0E-6,
                     var0.method_5624(),
                     var0.method_5715(),
                     var0.method_24828(),
                     String.format(Locale.ROOT, "%.3f", var1.Hm)
                  );
               }
            }
         }
      }
   }

   private bg.i r(class_2338 var1, fx.i var2) {
      double var3 = this.Y() - 0.4;
      class_243 var5 = class_243.method_24953(var1);
      double var6 = var2.bottom() + mc.field_1724.method_5751();
      return (var6x, var7) -> {
         double var8 = var6x + 0.5 - var5.field_1352;
         double var10 = var7 + 0.5 - var5.field_1350;
         double var12 = var6 - var5.field_1351;
         return var8 * var8 + var12 * var12 + var10 * var10 <= var3 * var3
            && (var6x != var1.method_10263() || var7 != var1.method_10260())
            && bv.j(new class_243(var6x + 0.5, var6, var7 + 0.5), var1, var3);
      };
   }

   private bg.i X(class_2338 var1) {
      int var2 = this.HU.c(var1.method_10263(), var1.method_10260(), this.Hz);
      int var3 = this.HU.l(var2, this.Hz);
      int var4 = this.HU.b(var2, this.Hz);
      int var5 = this.HU.l() ? var1.method_10263() : var1.method_10260();
      int var6 = Math.max(1, (int)Math.floor(this.Y() - 0.5));
      class_2350 var7 = this.O(var2);
      double var8 = var1.method_10264() - 1 + mc.field_1724.method_5751();
      double var10 = this.Y() - 0.4;
      class_243 var12 = class_243.method_24953(var1);
      return (var12x, var13) -> {
         int var14 = this.HU.l() ? var13 : var12x;
         int var15 = this.HU.l() ? var12x : var13;
         int var16 = (var5 - var15) * var3;
         if (var16 < 1 || var16 > var6) {
            return false;
         }

         if (var14 == var4) {
            return true;
         }

         if (Math.abs(var14 - var4) == 1 && var16 >= 2) {
            class_243 var17 = new class_243(var12x + 0.5, var8, var13 + 0.5);
            if (var17.method_1025(var12) > var10 * var10) {
               return false;
            }

            class_3965 var18 = mc.field_1687.method_17742(new class_3959(var17, var12, class_3960.field_17559, class_242.field_1348, mc.field_1724));
            return var18.method_17783() == class_240.field_1332 && var18.method_17777().equals(var1) && var18.method_17780() == var7;
         } else {
            return false;
         }
      };
   }

   private boolean q() {
      fx.o var1 = this.HU.X();
      class_2338 var2 = this.X();
      byte var3 = 3;
      int var4 = var2.method_10263() >> 4;
      int var5 = var2.method_10260() >> 4;
      int var6 = Math.max(var1.minX() - 2 >> 4, var4 - var3);
      int var7 = Math.min(var1.maxX() + 2 >> 4, var4 + var3);
      int var8 = Math.max(var1.minZ() - 2 >> 4, var5 - var3);
      int var9 = Math.min(var1.maxZ() + 2 >> 4, var5 + var3);

      for (int var10 = var6; var10 <= var7; var10++) {
         for (int var11 = var8; var11 <= var9; var11++) {
            if (!mc.field_1687.method_2935().method_12123(var10, var11)) {
               return false;
            }
         }
      }

      return true;
   }

   private List<bg.o> C(int var1, bg.i var2) {
      fx.o var3 = this.HU.X();
      class_2338 var4 = this.X();
      bg.t var5 = new bg.t(
         Math.min(var3.minX(), var4.method_10263()) - 2,
         Math.min(var3.minZ(), var4.method_10260()) - 2,
         Math.max(var3.maxX(), var4.method_10263()) + 2,
         Math.max(var3.maxZ(), var4.method_10260()) + 2
      );
      this.c = (boolean)0;
      List var6 = bg.R(
         (var3x, var4x) -> this.d(var3x, var1, var4x) || var3x == var4.method_10263() && var4x == var4.method_10260(),
         var4.method_10263(),
         var4.method_10260(),
         var2,
         var5,
         20000
      );
      if (var6 == null && this.U.getValue()) {
         this.c = (boolean)1;

         try {
            return bg.R(
               (var3x, var4x) -> this.d(var3x, var1, var4x) || var3x == var4.method_10263() && var4x == var4.method_10260(),
               var4.method_10263(),
               var4.method_10260(),
               var2,
               var5,
               20000
            );
         } finally {
            this.c = (boolean)0;
         }
      } else {
         return var6;
      }
   }

   private boolean t(class_2338 var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1);
      return var2.method_26220(mc.field_1687, var1).method_1110()
         && mc.field_1687.method_8316(var1).method_15769()
         && !var2.method_27852(class_2246.field_10343)
         && !var2.method_27852(class_2246.field_10036)
         && !var2.method_27852(class_2246.field_22089)
         && !var2.method_27852(class_2246.field_16999)
         && !var2.method_27852(class_2246.field_27879);
   }

   private boolean x(class_2338 var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1);
      return (var2.method_26206(mc.field_1687, var1, class_2350.field_11036) || this.C(var2, var1))
         && !var2.method_27852(class_2246.field_10092)
         && mc.field_1687.method_8316(var1).method_15769();
   }

   private boolean C(class_2680 var1, class_2338 var2) {
      class_265 var3 = var1.method_26220(mc.field_1687, var2);
      return C(var3);
   }

   static boolean C(class_265 var0) {
      if (var0.method_1110()) {
         return false;
      }

      class_238 var1 = var0.method_1107();
      return var1.field_1325 >= 0.75
         && var1.field_1325 <= 1.0
         && var1.field_1323 <= 0.0
         && var1.field_1320 >= 1.0
         && var1.field_1321 <= 0.0
         && var1.field_1324 >= 1.0;
   }

   static int a(double var0) {
      return class_3532.method_15357(var0 + 0.25);
   }

   private int v() {
      return a(mc.field_1724.method_23318());
   }

   private class_2338 X() {
      return new class_2338(mc.field_1724.method_31477(), this.v(), mc.field_1724.method_31479());
   }

   private boolean Z(class_2338 var1) {
      if (this.HB == null && this.c && !this.x.contains(var1) && this.U.getValue() && fk.v(var1) && mc.field_1687.method_8316(var1).method_15769()) {
         for (class_2350 var5 : class_2350.values()) {
            if (var5 != class_2350.field_11036 && fk.V(var1.method_10093(var5))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   boolean d(int var1, int var2, int var3) {
      class_2338 var4 = new class_2338(var1, var2, var3);
      if (!mc.field_1687.method_22340(var4)) {
         return false;
      }

      class_2338 var5 = var4.method_10074();
      return this.t(var4) && this.t(var4.method_10084()) && (this.x(var5) || this.Z(var5) && this.g() >= 0);
   }

   private void I(fx.i var1) {
      if (this.iu >= this.D.size()) {
         this.G();
      } else {
         class_243 var2 = mc.field_1724.method_73189();
         int var3 = this.U(var2, var1.bottom());

         while (this.iu < var3 && this.X(var2, this.iu, var3)) {
            this.iu++;
         }

         int var4 = var3 > this.iu ? 1 : 0;
         bg.o var5 = this.D.get(this.iu);
         class_243 var6 = this.h(this.iu, var2.field_1351);
         int var7 = this.iu == this.D.size() - 1 ? 1 : 0;
         double var8 = this.D == this.nk ? this.z : 0.245;
         if (var2.method_1025(var6) < (var7 != 0 ? var8 * var8 : (var4 != 0 ? 0.49 : 0.12))) {
            this.iu++;
            if (this.iu >= this.D.size()) {
               this.G();
               this.m("Arrived; looking for the next block");
               return;
            }

            var5 = this.D.get(this.iu);
            var6 = this.h(this.iu, var2.field_1351);
            var7 = this.iu == this.D.size() - 1 ? 1 : 0;
         }

         class_2338 var10 = new class_2338(var5.x(), var1.bottom() - 1, var5.z());
         if (var10.equals(this.HK) && mc.field_1724.field_6012 < this.nZ) {
            this.m("Waiting for the placed block to land");
         } else if (!this.x(var10)) {
            if (!this.p(var10)) {
               this.x.add(var10.method_10062());
               this.s("could not bridge the gap at " + var10.method_23854());
            }
         } else {
            double var11 = Math.sqrt(var2.method_1025(var6));

            for (int var13 = this.iu + 1; var13 < this.D.size(); var13++) {
               bg.o var14 = this.D.get(var13 - 1);
               bg.o var15 = this.D.get(var13);
               var11 += Math.hypot(var15.x() - var14.x(), var15.z() - var14.z());
            }

            class_243 var24 = (var4 != 0 && var3 < this.D.size() ? this.h(var3, var2.field_1351) : var6).method_1020(var2).method_18805(1.0, 0.0, 1.0);
            class_243 var25 = mc.field_1724.method_18798();
            double var26 = var24.method_1027() < 1.0E-6 ? 0.0 : var25.method_18805(1.0, 0.0, 1.0).method_1026(var24.method_1029());
            bg.o var17 = this.D.get(this.D.size() - 1);
            this.i4 = (boolean)(var11 < 0.9 && e3.c(var17.x(), var1.bottom(), var17.z()) ? 1 : 0);
            boolean var18 = this.iN.V(this.i4 ? var11 : var11 - 0.1, var26, cr.a(this));
            this.iT = var18 ? var24 : class_243.field_1353;
            this.wx = (boolean)(var18 && this.H2.getValue() && !this.i4 && var11 > 4.0 && mc.field_1724.method_7344().method_7586() > 6 ? 1 : 0);
            if (this.iZ == null
               || this.Hr
               || !(mc.field_1724.method_33571().method_1025(class_243.method_24953(this.iZ)) <= 64.0)
               || !(var11 < 1.5) && !j(var24, class_243.method_24953(this.iZ).method_1020(var2))) {
               double var19 = mc.field_1724.method_23320() - (this.D == this.nk ? this.E : 0.6) + 0.12 * Math.sin(mc.field_1724.field_6012 * 0.09 + this.ws);
               ArrayList var21 = new ArrayList(this.D.size() - this.iu);

               for (int var22 = this.iu; var22 < this.D.size(); var22++) {
                  var21.add(new class_243(this.D.get(var22).x() + 0.5, var19, this.D.get(var22).z() + 0.5));
               }

               this.aimAt(e3.l(new class_243(var2.field_1352, var19, var2.field_1350), var21, 3.0), 12.0F);
            } else {
               this.aimAt(class_243.method_24953(this.iZ), 12.0F);
            }

            this.y(var18);
            this.m("Walking to " + (this.iZ == null ? "the next block" : this.iZ.method_23854()));
         }
      }
   }

   private int U(class_243 var1, int var2) {
      if (Math.abs(var1.field_1351 - var2) > 0.6 && this.v() != var2) {
         return this.iu;
      }

      int var3 = this.iu;

      for (int var4 = this.iu + 1; var4 < Math.min(this.D.size(), this.iu + 1 + 8); var3 = var4++) {
         bg.o var5 = this.D.get(var4);
         if (!e3.g(mc.field_1687, var1, var5.x() + 0.5, var5.z() + 0.5, var2)) {
            break;
         }
      }

      return var3;
   }

   private boolean X(class_243 var1, int var2, int var3) {
      bg.o var4 = this.D.get(var2);
      bg.o var5 = this.D.get(var3);
      return Math.hypot(var5.x() + 0.5 - var1.field_1352, var5.z() + 0.5 - var1.field_1350) <= Math.hypot(var5.x() - var4.x(), var5.z() - var4.z()) + 0.05;
   }

   private void y(boolean var1) {
      class_243 var2 = mc.field_1724.method_73189();
      if (var1 && this.T != null && var2.method_1025(this.T) < 1.0E-4) {
         this.wp++;
      } else {
         this.wp = 0;
      }

      this.T = var2;
      if (this.wp > 40) {
         this.s("movement blocked");
      }
   }

   private void s(String var1) {
      if (++this.Hn >= 3 && this.iZ != null) {
         this.j(this.iZ, var1);
      }

      q8.v(this.getName(), "Replanning route: {}", var1);
      this.D = List.of();
      this.iu = 0;
      this.wp = 0;
      this.X = -1000000000;
   }

   private boolean p(class_2338 var1) {
      int var2 = this.g();
      if (var2 >= 0 && this.U.getValue()) {
         this.i4 = (boolean)1;
         if (this.U("bridge a gap")) {
            return true;
         } else if (!this.h(var2)) {
            this.m("Taking out " + mc.field_1724.method_31548().method_5438(var2).method_7964().getString() + " to bridge a gap");
            return true;
         } else {
            class_1792 var3 = mc.field_1724.method_31548().method_5438(var2).method_7909();
            class_2338 var4 = var1.method_10062();
            if (this.wf.getOrDefault(var4, 0) >= 3) {
               return false;
            } else if (mc.field_1724.field_6012 < this.iv) {
               this.m("Steadying before the next block");
               return true;
            } else {
               boolean var5 = fk.m(this, var4, var3, this.Y(), true, true, () -> fk.v(var4) && !this.U(), () -> this.t(var4, var3, true), var2x -> {
                  this.e(var4);
                  q8.v(this.getName(), "Bridge block at {} {}", var4.method_23854(), var2x ? "placed" : "rejected");
               });
               this.m(var5 ? "Bridging a gap at " + var4.method_23854() : "Cannot reach the gap at " + var4.method_23854());
               return var5;
            }
         }
      } else {
         return false;
      }
   }

   private void X(fx.i var1) {
      if (!this.H1 && this.R() >= 0) {
         this.H1 = (boolean)1;
         if (this.iw.getValue()) {
            q8.G(this.getName(), "Layer {}: drill faces done, sweeping leftovers", this.Hz + 1);
            this.ie = -1000000000;
            return;
         }
      }

      int var2 = 0;

      for (class_2338 var4 : this.j) {
         if (bv.P(var4)) {
            var2++;
         }
      }

      q8.G(this.getName(), "Layer {} of {} done; {} blocks left (held back fluid, unreachable, set aside or not swept)", this.Hz + 1, this.HU.r(), var2);
      this.Hz++;
      this.H1 = (boolean)(this.R() < 0 ? 1 : 0);
      this.in = (boolean)0;
      this.nO = -1;
      this.wV = null;
      this.j.clear();
      this.wT.clear();
      this.ie = -1000000000;
      this.Y = this.iZ = null;
      this.G();
      if (this.Hz >= this.HU.r()) {
         this.Hz = this.HU.r() - 1;
         if (this.n7.getValue()) {
            this.Hr = (boolean)1;
            this.m("Picking up drops");
            q8.G(this.getName(), "Picking up drops in the area");
         } else {
            this.N();
         }
      } else {
         this.m("Moving down to layer " + (this.Hz + 1));
      }
   }

   private void N() {
      if (this.nT) {
         this.HB = null;
         this.HU = this.HZ;
         this.I = 0;
         this.wR = this.ik;
      } else if (this.HB == null && this.F()) {
         return;
      }

      fx.o var1 = this.HU.X();
      int var2 = 0;
      if (this.HB != null) {
         var2 = this.q();
      } else {
         for (int var3 = var1.minY(); var3 <= this.HU.V(this.I).top(); var3++) {
            for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
               for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
                  if (bv.P(new class_2338(var4, var3, var5))) {
                     var2++;
                  }
               }
            }
         }
      }

      String var15 = w(System.currentTimeMillis() - this.w);
      if (this.HB != null) {
         String var17 = this.nI
            + " mined out in "
            + var15
            + ", "
            + Math.max(0, this.wR - var2)
            + " blocks mined"
            + (var2 > 0 ? ", " + var2 + " left standing (next to lava or water, unreachable or put back by the server)" : "");
         this.L(var17 + ".");
         q8.G(this.getName(), "Block set done: {}", var17);
         if (this.iF.getValue()) {
            com.larpclient.wt.T().k("Auto Area Miner", this.nI + " mined out in " + var15 + (var2 > 0 ? ", " + var2 + " left" : ""), wt.s.C, 8000L);
         }

         this.disable();
      } else {
         int var16 = 0;
         int var18 = 0;
         int var6 = 0;
         int var7 = 0;
         class_2338 var8 = null;
         ArrayList var9 = new ArrayList();

         for (int var10 = this.HU.V(this.I).top(); var10 >= var1.minY(); var10--) {
            for (int var11 = var1.minX(); var11 <= var1.maxX(); var11++) {
               for (int var12 = var1.minZ(); var12 <= var1.maxZ(); var12++) {
                  class_2338 var13 = new class_2338(var11, var10, var12);
                  if (bv.P(var13)) {
                     String var14;
                     if (this.S(var13)) {
                        var16++;
                        var14 = "holds back a fluid";
                     } else if (this.ix.contains(var13)) {
                        var18++;
                        var14 = "put back by the server";
                     } else if (this.w_.contains(var13)) {
                        var6++;
                        var14 = "drill would cut outside";
                     } else {
                        var7++;
                        var14 = "not reached";
                        if (var8 == null) {
                           var8 = var13;
                        }
                     }

                     if (var9.size() < 24) {
                        var9.add(
                           var13.method_23854()
                              + " "
                              + class_7923.field_41175.method_10221(mc.field_1687.method_8320(var13).method_26204()).method_12832()
                              + " ("
                              + var14
                              + ")"
                        );
                     }
                  }
               }
            }
         }

         int var19 = Math.max(0, this.wR - var2);
         int var20 = this.I > 0 ? this.m(this.HU.V(this.I).top()) : 0;
         String var21;
         if (var2 == 0) {
            var21 = "area cleared in " + var15 + (this.wR >= 0 ? ", " + var19 + " blocks mined" : "");
         } else {
            ArrayList var22 = new ArrayList();
            if (var16 > 0) {
               var22.add(var16 + " next to lava or water, left so the area does not flood");
            }

            if (var18 > 0) {
               var22.add(var18 + " the server keeps putting back");
            }

            if (var6 > 0) {
               var22.add(var6 + " at the edges, where your drill would cut outside the area (add a plain pickaxe to clear them)");
            }

            if (var7 > 0) {
               var22.add(var7 + " it could not reach or see, the first at " + var8.method_23854());
            }

            var21 = "finished in "
               + var15
               + (this.wR >= 0 ? ", " + var19 + " blocks mined" : "")
               + ", but "
               + var2
               + " blocks are still standing: "
               + String.join("; ", var22);
         }

         if (var20 > 0) {
            var21 = var21 + ". The " + var20 + " blocks in the layers above where you started were left as they are";
         }

         this.L(var21 + ".");
         q8.G(this.getName(), "Done: {}", var21);
         if (!var9.isEmpty()) {
            q8.G(this.getName(), "Still standing: {}{}", String.join(", ", var9), var2 > var9.size() ? " and more" : "");
         }

         if (this.iF.getValue()) {
            com.larpclient.wt.T()
               .k(
                  "Auto Area Miner",
                  var2 == 0 ? "Area cleared in " + var15 : "Finished with " + var2 + " blocks still standing",
                  var2 == 0 ? wt.s.C : wt.s.a,
                  8000L
               );
         }

         this.nM = (boolean)1;
         this.disable();
      }
   }

   private boolean F() {
      fx.o var1 = this.HU.X();
      if (var1.volume() > 2000000L) {
         return false;
      }

      HashSet var2 = new HashSet();
      int var3 = 0;
      int var4 = this.HU.V(this.I).top();

      for (int var5 = var1.maxY(); var5 >= var1.minY(); var5--) {
         for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
            for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
               class_2338 var8 = new class_2338(var6, var5, var7);
               if (bv.P(var8) && !this.S(var8) && !this.ix.contains(var8)) {
                  var2.add(var8);
                  if (var5 > var4) {
                     var3++;
                  }
               }
            }
         }
      }

      if (!var2.isEmpty() && var2.size() <= 20000) {
         String var11 = var3 == var2.size()
            ? var2.size() + " blocks in the layers above where you started"
            : (var3 > 0 ? var2.size() + " blocks (" + var3 + " of them above where you started)" : var2.size() + " blocks");
         this.L("checking the whole area: " + var11 + " are still standing, so the miner goes back for them.");
         q8.G(this.getName(), "Final check: {} blocks still standing ({} above layer {}); mining them as a block set", var2.size(), var3, this.I + 1);
         this.HZ = this.HU;
         this.ik = this.wR < 0 ? -1 : this.wR + var3;
         this.nT = (boolean)1;
         this.HB = var2;
         this.nI = "the area's leftovers";
         class_2338 var12 = this.X();
         int[] var13 = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
         int[] var14 = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

         for (class_2338 var10 : var2) {
            var13[0] = Math.min(var13[0], var10.method_10263());
            var13[1] = Math.min(var13[1], var10.method_10264());
            var13[2] = Math.min(var13[2], var10.method_10260());
            var14[0] = Math.max(var14[0], var10.method_10263());
            var14[1] = Math.max(var14[1], var10.method_10264());
            var14[2] = Math.max(var14[2], var10.method_10260());
         }

         this.HU = new fx(fx.o.of(var13, var14), this.wy.getValue().intValue(), this.nu.getValue().intValue(), var12.method_10263(), var12.method_10260());
         this.H1 = (boolean)1;
         this.Hz = this.I = 0;
         this.j.clear();
         this.wT.clear();
         this.ie = -1000000000;
         this.Y = this.iZ = null;
         this.iW = null;
         this.G();
         this.Hr = (boolean)0;
         this.a = this.w2 = 0;
         this.i3.clear();
         this.wt.clear();
         this.M.clear();
         this.nc.clear();
         this.iU.clear();
         this.iK = (boolean)0;
         this.w0.i();
         this.H4 = -1000000000;
         this.iG = 0;
         this.i.clear();
         this.HS = this.q();
         this.wd = mc.field_1724.field_6012;
         this.m("Going back for " + var2.size() + " blocks");
         return true;
      } else {
         return false;
      }
   }

   private String S() {
      return this.wY.getValue().trim() + "|" + this.Hw.getValue().trim() + "|" + this.wy.getValue().intValue();
   }

   private int I() {
      fx.o var1 = this.HU.X();
      if (var1.volume() > 200000L) {
         return -2;
      }

      class_2339 var2 = new class_2339();

      for (int var3 = var1.maxY(); var3 >= var1.minY(); var3--) {
         for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
            for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
               if (bv.P(var2.method_10103(var4, var3, var5)) && !this.S(var2)) {
                  return this.HU.J(var3);
               }
            }
         }
      }

      return -1;
   }

   private int m(int var1) {
      fx.o var2 = this.HU.X();
      if (var2.volume() > 200000L) {
         return 0;
      }

      int var3 = 0;
      class_2339 var4 = new class_2339();

      for (int var5 = var1 + 1; var5 <= var2.maxY(); var5++) {
         for (int var6 = var2.minX(); var6 <= var2.maxX(); var6++) {
            for (int var7 = var2.minZ(); var7 <= var2.maxZ(); var7++) {
               if (bv.P(var4.method_10103(var6, var5, var7))) {
                  var3++;
               }
            }
         }
      }

      return var3;
   }

   private boolean h(class_2338 var1) {
      fx.o var2 = this.HU.X();
      if (!var2.containsColumn(var1.method_10263(), var1.method_10260())) {
         return false;
      }

      class_2339 var3 = new class_2339();

      for (int var4 = var1.method_10264() + 1; var4 <= var2.maxY(); var4++) {
         if (!mc.field_1687.method_8320(var3.method_10103(var1.method_10263(), var4, var1.method_10260())).method_26220(mc.field_1687, var3).method_1110()) {
            return false;
         }
      }

      return true;
   }

   private void t() {
      bv.i(this);
      fx.o var1 = this.HU.X();
      int var2 = this.v();
      class_238 var3 = new class_238(var1.minX() - 1, var2 - 1, var1.minZ() - 1, var1.maxX() + 2, var2 + 2, var1.maxZ() + 2);
      List var4 = mc.field_1687.method_8390(class_1542.class, var3, var1x -> var1x.method_5805() && !this.i3.contains(var1x.method_5628()));
      if (!var4.isEmpty() && ++this.a <= 1200) {
         class_243 var5 = mc.field_1724.method_73189();
         class_1542 var6 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.method_73189().method_1025(var5))).orElseThrow();
         class_2338 var7 = class_2338.method_49638(var6.method_73189());
         if (var7.method_10263() == mc.field_1724.method_31477() && var7.method_10260() == mc.field_1724.method_31479()) {
            this.g(class_243.method_24955(var7));
            if (++this.w2 > 30) {
               this.i3.add(var6.method_5628());
               this.w2 = 0;
            }

            this.m("Picking up drops (" + var4.size() + " left)");
         } else {
            this.w2 = 0;
            if (this.D.isEmpty() || this.iZ == null || !this.iZ.equals(var7)) {
               List var8 = this.C(var2, (var1x, var2x) -> var1x == var7.method_10263() && var2x == var7.method_10260());
               if (var8 == null || var8.isEmpty()) {
                  this.i3.add(var6.method_5628());
                  this.G();
                  return;
               }

               this.D = var8;
               this.iu = 0;
               this.iZ = var7;
            }

            this.T(var2);
            this.m("Picking up drops (" + var4.size() + " left)");
         }
      } else {
         if (!var4.isEmpty()) {
            q8.G(this.getName(), "Left {} drops after {} ticks of picking up", var4.size(), 1200);
         }

         this.N();
      }
   }

   private class_2338 P(int var1) {
      int[] var2 = this.HU.O(0, var1);
      int var3 = (var2[0] + var2[1]) / 2;
      fx.o var4 = this.HU.X();
      int var5 = this.HU.l(0, var1) > 0 ? (this.HU.l() ? var4.minX() : var4.minZ()) : (this.HU.l() ? var4.maxX() : var4.maxZ());
      return this.HU.l() ? new class_2338(var5, 0, var3) : new class_2338(var3, 0, var5);
   }

   private void u(fx.i var1) {
      if (!this.O() || this.Hg || !this.wC.stream().anyMatch(var0 -> var0.kind() == wy.f.Z) || !this.q(var1)) {
         class_2338 var2 = this.X();
         class_2338 var3 = var2.method_10074();
         if (this.wV == null) {
            class_2338 var4 = this.P(this.Hz);
            int var5 = var2.method_10264();
            List var6 = this.C(
               var5, (var4x, var5x) -> var4x == var4.method_10263() && var5x == var4.method_10260() && this.j(new class_2338(var4x, var5 - 1, var5x), var1)
            );
            if (var6 == null) {
               var6 = this.C(var5, (var3x, var4x) -> this.HU.X().containsColumn(var3x, var4x) && this.j(new class_2338(var3x, var5 - 1, var4x), var1));
            }

            if (var6 == null) {
               this.Q("there is no safe place to dig down into layer " + (this.Hz + 1) + "; stand on top of the area");
               return;
            }

            bg.o var7 = var6.isEmpty() ? new bg.o(var2.method_10263(), var2.method_10260()) : (bg.o)var6.getLast();
            this.wV = new class_2338(var7.x(), var5, var7.z());
            this.D = var6;
            this.iu = 0;
            this.iZ = null;
            q8.v(this.getName(), "Digging down to layer {} at {}", this.Hz + 1, this.wV.method_23854());
         }

         if (var2.method_10263() == this.wV.method_10263() && var2.method_10260() == this.wV.method_10260()) {
            this.G();
            if (!this.j(var3, var1)) {
               this.wV = null;
               this.m("Looking for a safe place to dig down");
            } else {
               class_243 var8 = class_243.method_24955(var2);
               if (mc.field_1724.method_73189().method_18805(1.0, 0.0, 1.0).method_1025(var8.method_18805(1.0, 0.0, 1.0)) > 0.03) {
                  this.g(var8);
                  this.m("Lining up to dig down");
               } else if (!bv.P(var3)) {
                  this.m("Waiting to drop into layer " + (this.Hz + 1));
               } else {
                  this.c(var3);
                  this.iK = (boolean)0;
                  this.K(var3, false, "Digging down to layer " + (this.Hz + 1));
                  this.wV = var2.method_10074();
               }
            }
         } else if (this.D.isEmpty()) {
            this.wV = null;
            this.G();
         } else {
            this.T(var2.method_10264());
         }
      }
   }

   private void T(int var1) {
      fx.i var2 = new fx.i(this.Hz, var1, var1);
      this.I(var2);
   }

   private boolean j(class_2338 var1, fx.i var2) {
      if (this.HU.X().contains(var1.method_10263(), var1.method_10264(), var1.method_10260()) && mc.field_1687.method_22340(var1) && !this.X(var1)) {
         if (this.D() && this.R() && bv.P(var1) && !this.W(var1, class_2350.field_11036)) {
            return false;
         }

         if (!bv.P(var1) && !mc.field_1687.method_8320(var1).method_26220(mc.field_1687, var1).method_1110()) {
            return false;
         }

         for (class_2350 var6 : class_2350.values()) {
            if (var6 != class_2350.field_11036 && !mc.field_1687.method_8316(var1.method_10093(var6)).method_15769()) {
               return false;
            }
         }

         int var7 = var1.method_10264() + 1;

         for (int var8 = var1.method_10264() - 1; var8 >= var1.method_10264() - 3 - 1; var8--) {
            class_2338 var9 = new class_2338(var1.method_10263(), var8, var1.method_10260());
            if (!mc.field_1687.method_8316(var9).method_15769()) {
               return false;
            }

            class_2680 var10 = mc.field_1687.method_8320(var9);
            if (!var10.method_26220(mc.field_1687, var9).method_1110()) {
               return var8 + 1 >= var2.bottom() && fx.K(var7, var8, 3) && !var10.method_27852(class_2246.field_10092);
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void g(class_243 var1) {
      class_243 var2 = mc.field_1724.method_73189();
      class_243 var3 = var1.method_1020(var2).method_18805(1.0, 0.0, 1.0);
      double var4 = var3.method_1033();
      double var6 = var4 < 1.0E-6 ? 0.0 : mc.field_1724.method_18798().method_18805(1.0, 0.0, 1.0).method_1026(var3.method_1029());
      this.i4 = (boolean)1;
      boolean var8 = this.iN.V(var4, var6, cr.a(this));
      this.iT = var8 ? var3 : class_243.field_1353;
   }

   private void N(fx.i var1) {
      class_2338 var2 = this.X();
      if (!this.U.getValue()) {
         this.Q("you are below layer " + (this.Hz + 1) + "; turn on Place Blocks to pillar up, or climb back up");
      } else if (this.g() < 0) {
         this.Q("you are below layer " + (this.Hz + 1) + " and there is no solid block in your hotbar to pillar up with");
      } else {
         class_2338 var3 = var2.method_10086(2);
         if (this.t(var3)) {
            class_243 var4 = class_243.method_24955(var2);
            if (mc.field_1724.method_73189().method_18805(1.0, 0.0, 1.0).method_1025(var4.method_18805(1.0, 0.0, 1.0)) > 0.03) {
               this.g(var4);
               this.m("Lining up to pillar up");
            } else {
               bv.i(this);
               if (!this.U("pillar up")) {
                  if (!this.h(this.g())) {
                     this.m("Taking out a block to pillar up");
                  } else if (mc.field_1724.method_36455() < 84.0F || this.E() > 1.5F) {
                     this.z();
                     this.m("Looking down to pillar up");
                  } else if (mc.field_1724.field_6012 < this.iv) {
                     this.z();
                     this.m("Steadying before the next block");
                  } else {
                     this.Hp = 86.0F + (float)this.iV.nextDouble() * 3.0F;
                     this.wu = var2.method_10062();
                     this.wi = mc.field_1724.field_6012;
                     this.B = (boolean)0;
                     this.HE = (boolean)1;
                     this.m("Pillaring up to layer " + (this.Hz + 1));
                  }
               }
            }
         } else {
            if (this.HU.X().contains(var3.method_10263(), var3.method_10264(), var3.method_10260()) && bv.P(var3) && !this.S(var3)) {
               this.c(var3);
               this.iK = (boolean)0;
               this.K(var3, false, "Clearing headroom to pillar up");
            } else {
               this.Q("there is no room to pillar up back to layer " + (this.Hz + 1));
            }
         }
      }
   }

   private void y() {
      this.z();
      this.i4 = (boolean)0;
      int var1 = this.g();
      if (!this.B
         && var1 >= 0
         && !mc.field_1724.method_24828()
         && mc.field_1724.method_23318() >= this.wu.method_10264() + 1.05
         && mc.field_1724.field_6012 >= this.iv
         && fk.v(this.wu)) {
         class_1792 var2 = mc.field_1724.method_31548().method_5438(var1).method_7909();
         class_2338 var3 = this.wu;
         fk.m(
            this,
            var3,
            var2,
            this.Y(),
            true,
            true,
            () -> fk.v(var3) && mc.field_1724.method_23318() >= var3.method_10264() + 1,
            () -> ((q586814c6d8f3640b)mc.field_1724).larpYLast() >= var3.method_10264() + 1.02 && this.E() <= 1.5F,
            var2x -> {
               this.e(var3);
               if (var2x) {
                  this.B = (boolean)1;
               }
            }
         );
      }

      if (mc.field_1724.method_24828() && mc.field_1724.field_6012 - this.wi > 2) {
         int var4 = this.v() > this.wu.method_10264() ? 1 : 0;
         if (var4 == 0 && ++this.w8 >= 5) {
            this.wu = null;
            this.Q("pillaring up failed five times");
         } else {
            if (var4 != 0) {
               this.w8 = 0;
            }

            this.wu = null;
         }
      } else {
         if (mc.field_1724.field_6012 - this.wi > 30) {
            this.wu = null;
            this.w8++;
         }
      }
   }

   private void z() {
      if (cr.a(this)) {
         float[] var1 = this.iN
            .I(
               mc.field_1724.method_36454(),
               mc.field_1724.method_36455(),
               mc.field_1724.method_36454(),
               this.Hp,
               mc.field_1724.method_36455() >= this.Hp - 1.5F,
               true
            );
         t9.O(this, t9.v.e, var1[0], var1[1], 1.0);
      }
   }

   @Override
   protected boolean holdsToolBetweenBlocks() {
      return true;
   }

   @Override
   public boolean aimsAtOpenFaces(class_2338 var1) {
      if (this.O()) {
         return true;
      } else if (this.HU != null && this.Hz < this.HU.r() && !this.H1 && this.R() >= 0 && !this.R(var1)) {
         int var2 = this.HU.c(var1.method_10263(), var1.method_10260(), this.Hz);
         return !var1.equals(this.P(var2, this.HU.l() ? var1.method_10263() : var1.method_10260(), this.HU.V(this.Hz)));
      } else {
         return true;
      }
   }

   @Override
   public boolean aimsAtFaceCentre(class_2338 var1) {
      return var1.equals(this.HH);
   }

   @Override
   public void digFinished(class_2338 var1) {
      if (this.HU != null && mc.field_1724 != null) {
         this.HM.q(var1.method_10063());
         if (this.wH.size() < 256) {
            this.wH.put(var1.method_10062(), mc.field_1724.field_6012);
         }
      }
   }

   @Override
   public boolean aimFaceAllowed(class_2338 var1, class_2350 var2) {
      if (this.HH != null && var1.equals(this.HH)) {
         return var2 == this.HL;
      }

      if (this.HU != null && this.D() && this.R()) {
         if (this.n(mc.field_1687.method_8320(var1), this.R()) == this.R() && !this.W(var1, var2)) {
            this.w_.add(var1.method_10062());
            return false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private boolean V(class_2338 var1) {
      double var2 = this.Y();
      return mc.field_1724.method_33571().method_1025(class_243.method_24953(var1)) <= var2 * var2;
   }

   private boolean Z() {
      for (class_2338 var2 : this.HT) {
         if (bv.P(var2)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean trustsSupport(class_2338 var1) {
      if (mc.field_1724 == null) {
         return true;
      } else {
         return !var1.equals(this.Y) && !this.iU.containsKey(var1) ? mc.field_1724.field_6012 >= this.H6 || !this.HT.contains(var1) : false;
      }
   }

   @Override
   public boolean waitsForSentLook() {
      return true;
   }

   @Override
   protected void aimAt(class_243 var1, float var2) {
      if (this.isEnabled()) {
         if (cr.a(this)) {
            if (this.nU && this.Y != null && this.Y.equals(this.HH)) {
               var1 = g(mc.field_1724.method_33571(), this.HH, this.HL, this.Ha, this.Ht);
            }

            float[] var3 = t9.X(mc.field_1724.method_33571(), var1);
            class_3965 var4 = this.Y == null ? null : bv.L(this.Y, this.Y());
            int var5 = var4 == null || this.HH != null && this.Y.equals(this.HH) && var4.method_17780() != this.HL ? 0 : 1;
            float[] var6 = this.iN.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var3[0], var3[1], (boolean)var5);
            t9.O(this, t9.v.e, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.iN.I(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var1, var2, false, true);
         t9.O(this, t9.v.e, var3[0], var3[1], 1.0);
      }
   }

   public static class_10185 k() {
      if (ih != null && ih.isEnabled()) {
         sh var0 = ih;
         class_310 var1 = class_310.method_1551();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.HU != null
            && var1.field_1724 != null
            && var1.field_1724 == var0.nB
            && var1.field_1724.field_6012 == var0.A
            && !com.larpclient.wp.d()
            && !ff.e()
            && !(var1.field_1755 instanceof class_465)
            && (!t9.I() || t9.k(t9.v.e))) {
            int var2 = var0.iT.method_1027() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.HE && !var0.i4) {
               return null;
            }

            class_10185 var3 = var2 != 0
               ? var0.iN.V(var1.field_1724.method_36454(), var0.iT)
               : new class_10185(false, false, false, false, false, false, false);
            return new class_10185(var3.comp_3159(), var3.comp_3160(), var3.comp_3161(), var3.comp_3162(), var0.HE, var0.i4, var0.wx);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void L(String var1) {
      if (mc != null && mc.field_1724 != null) {
         mc.field_1724.method_7353(class_2561.method_43470("Larp Auto Area Miner: " + var1), false);
      }
   }

   private void Q(String var1) {
      q8.G(this.getName(), "Stopped: {}", var1);
      this.L("stopped, " + var1 + ".");
      com.larpclient.wt.T().U("Auto Area Miner stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), wt.s.a);
      this.m("Stopped: " + var1);
      this.disable();
   }

   private void J() {
      if (this.isInGame()) {
         if (this.isRuntimeActive() && this.HU != null && mc.field_1687 == this.He) {
            try {
               int var1 = ez.V(0.25);
               if (this.iq.getValue()) {
                  fx.o var2 = this.HU.X();
                  class_12180.method_75541(
                        new class_238(var2.minX(), var2.minY(), var2.minZ(), var2.maxX() + 1, var2.maxY() + 1, var2.maxZ() + 1),
                        class_12179.method_75537(ei.D(var1, 0.9), 1.5F, 0)
                     )
                     .method_75533();
                  if (this.Hz < this.HU.r()) {
                     fx.i var3 = this.HU.V(this.Hz);
                     class_12180.method_75541(
                        new class_238(var2.minX(), var3.bottom(), var2.minZ(), var2.maxX() + 1, var3.top() + 1, var2.maxZ() + 1),
                        class_12179.method_75537(ei.D(var1, 0.5), 1.0F, ei.D(var1, 0.06))
                     );
                  }

                  if (this.Y != null) {
                     class_12180.method_75541(
                           new class_238(this.Y).method_1014(0.01),
                           class_12179.method_75537(ei.D(this.iK ? -14249 : var1, 1.0), 2.0F, ei.D(this.iK ? -14249 : var1, 0.18))
                        )
                        .method_75533();
                  }
               }

               if (this.we.getValue() && this.iu < this.D.size()) {
                  double var9 = mc.field_1724.method_23318() + 0.05;
                  class_243 var4 = new class_243(mc.field_1724.method_23317(), var9, mc.field_1724.method_23321());

                  for (int var5 = this.iu; var5 < this.D.size(); var5++) {
                     bg.o var6 = this.D.get(var5);
                     class_243 var7 = new class_243(var6.x() + 0.5, var9, var6.z() + 0.5);
                     class_12180.method_75546(var4, var7, ei.D(var1, 0.85), 2.0F).method_75533();
                     var4 = var7;
                  }
               }
            } catch (RuntimeException var8) {
               q8.O(this.getName(), "Area overlay failed", var8);
            }
         } else {
            this.F();
         }
      }
   }

   private void F() {
      if (this.Hx) {
         this.X();
      } else if (System.currentTimeMillis() <= this.wk) {
         fx.o var1 = this.s();
         if (var1 != null) {
            try {
               int var2 = ez.V(0.25);
               class_12180.method_75541(
                     new class_238(var1.minX(), var1.minY(), var1.minZ(), var1.maxX() + 1, var1.maxY() + 1, var1.maxZ() + 1),
                     class_12179.method_75537(ei.D(var2, 0.95), 2.0F, ei.D(var2, 0.08))
                  )
                  .method_75533();

               for (String var6 : new String[]{this.wY.getValue(), this.Hw.getValue()}) {
                  int[] var7 = fx.C(var6);
                  if (var7 != null) {
                     class_12180.method_75541(
                           new class_238(var7[0], var7[1], var7[2], var7[0] + 1, var7[1] + 1, var7[2] + 1).method_1014(0.02),
                           class_12179.method_75537(-1, 2.0F, ei.D(var2, 0.35))
                        )
                        .method_75533();
                  }
               }
            } catch (RuntimeException var8) {
               q8.O(this.getName(), "Area preview failed", var8);
            }
         }
      }
   }

   private void X() {
      try {
         int var1 = ez.V(0.25);
         int[] var2 = fx.C(this.wY.getValue());
         int[] var3 = fx.C(this.Hw.getValue());
         int[] var4 = this.wc == null ? null : new int[]{this.wc.method_10263(), this.wc.method_10264(), this.wc.method_10260()};
         int[] var5 = var2 != null ? var2 : var4;
         int[] var6 = var3 != null ? var3 : var4;
         if (var5 != null && var6 != null) {
            fx.o var7 = fx.o.of(var5, var6);
            class_12180.method_75541(
                  new class_238(var7.minX(), var7.minY(), var7.minZ(), var7.maxX() + 1, var7.maxY() + 1, var7.maxZ() + 1),
                  class_12179.method_75537(ei.D(var1, 0.95), 2.0F, ei.D(var1, 0.08))
               )
               .method_75533();
         }

         for (int[] var10 : new int[][]{var2, var3}) {
            if (var10 != null) {
               class_12180.method_75541(
                     new class_238(var10[0], var10[1], var10[2], var10[0] + 1, var10[1] + 1, var10[2] + 1).method_1014(0.02),
                     class_12179.method_75537(-1, 2.0F, ei.D(var1, 0.35))
                  )
                  .method_75533();
            }
         }

         if (this.wc != null) {
            class_12180.method_75541(new class_238(this.wc).method_1014(0.03), class_12179.method_75537(-14249, 2.0F, 872400983)).method_75533();
         }
      } catch (RuntimeException var11) {
         q8.O(this.getName(), "Corner picking overlay failed", var11);
      }
   }

   private int C() {
      if (this.HB != null) {
         return this.q();
      }

      fx.o var1 = this.HU == null ? this.s() : this.HU.X();
      if (var1 != null && var1.volume() <= 200000L) {
         int var2 = 0;
         class_2339 var3 = new class_2339();
         int var4 = this.HU == null ? var1.maxY() : this.HU.V(this.I).top();

         for (int var5 = var1.minY(); var5 <= var4; var5++) {
            for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
               for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
                  if (bv.P(var3.method_10103(var6, var5, var7))) {
                     var2++;
                  }
               }
            }
         }

         return var2;
      } else {
         return -1;
      }
   }

   private String U() {
      StringBuilder var1 = new StringBuilder("Layer ").append(Math.min(this.Hz + 1, this.HU.r())).append(" of ").append(this.HU.r());
      if (this.wR > 0 && this.HS >= 0) {
         var1.append(", ").append(this.l()).append("% done");
      }

      var1.append(", ").append(w(System.currentTimeMillis() - this.w));
      return var1.toString();
   }

   private int l() {
      return this.wR > 0 && this.HS >= 0 ? Math.clamp(Math.round(100.0 * (this.wR - this.HS) / this.wR), 0, 100) : 0;
   }

   static String w(long var0) {
      return tc.T(var0);
   }

   @Override
   public tc.g hudCard() {
      if (!this.isInGame()) {
         return null;
      }

      if (this.w7.getValue() && this.isRuntimeActive() && this.HU != null && mc.field_1687 == this.He) {
         int var6 = this.wR > 0 && this.HS >= 0 ? 1 : 0;
         String var7 = this.getStatus();
         return new tc.g(
            "Layer " + Math.min(this.Hz + 1, this.HU.r()) + " of " + this.HU.r(),
            var6 != 0 ? this.l() + "%" : "",
            var7 != null && !var7.isBlank() ? var7 : "Starting",
            w(System.currentTimeMillis() - this.w),
            var6 != 0 ? Math.clamp((double)(this.wR - this.HS) / this.wR, 0.0, 1.0) : -1.0
         );
      }

      if (!this.Hx) {
         return null;
      }

      fx.o var1 = this.s();
      int var2 = (this.iy[0] ? 1 : 0) + (this.iy[1] ? 1 : 0);
      FreecamModule var3 = this.y();
      double var4 = var3 == null ? 0.0 : var3.R();
      return new tc.g(
         var1 == null ? "Choose the area" : var1.describe(),
         var2 + " of 2",
         var2 == 2 ? "Esc to finish, scroll changes camera speed" : "Left click corner 1, right click corner 2",
         var4 > 0.0 ? Math.round(var4) + " b/s" : "",
         var2 / 2.0
      );
   }

   public fx d() {
      return this.HU;
   }

   public int y() {
      return this.Hz;
   }

   public String w() {
      return this.nD.name();
   }

   private enum e {
      R,
      M,
      w;
   }
}
