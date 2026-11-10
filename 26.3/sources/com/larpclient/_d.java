package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828.qa6b3f1c381a58196;
import com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e.q0f2922fe81ac8eae;
import com.larpclient.module.StaffListModule;
import com.mojang.blaze3d.Blaze3D;
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
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.CrafterScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class _d extends _2 implements ac, vi.d {
   private static _d m1;
   private static boolean c;
   static final int n7 = 4096;
   static final int Uw = 2048;
   static final int s = 24;
   static final int lC = 12;
   static final double CX = 0.3;
   private static final int lb = 600;
   static final int li = 3;
   static final int Cx = 40;
   static final int m7 = 40;
   static final int Cn = 400;
   static final int US = 3;
   static final int mp = 2400;
   static final int Ui = 20;
   static final int lN = 8;
   static final int mG = 1200;
   static final long w = 300000L;
   static final int CD = 3;
   static final long lJ = 5000L;
   static final long lP = 10000L;
   static final long lx = 120000L;
   static final int UE = 3;
   static final long lv = 3000L;
   private static final int U = 0;
   private static final int CV = 1;
   private static final int mN = 2;
   private static final int U6 = 3;
   static final int ml = 16000;
   static final int C4 = 4;
   static final double lq = 32.0;
   static final int la = -1000000000;
   static final int Uq = 10;
   static final int CP = 200000;
   static final float CN = 2.0F;
   static final int G = 100;
   static final int lH = 6;
   static final int Ca = 900;
   static final int O = 60;
   static final int Ce = 400;
   static final int S = 32;
   static final int l_ = 10;
   static final double lo = 35.0;
   static final int nh = 8;
   static final int U5 = 600;
   static final int CT = 3;
   static final float n3 = 1.6F;
   static final float X = 6.0F;
   static final float lh = 26.0F;
   static final int mJ = 600;
   static final int nI = 80;
   static final int lE = 200;
   static final int CO = 24;
   static final int lI = 3;
   static final int CG = 80;
   static final int U7 = 160;
   static final int UV = 3;
   static final int mo = 120;
   static final double CS = 0.66;
   static final double mR = 0.54;
   static final double lX = 0.08;
   static final double le = 0.25;
   static final int mf = 64;
   static final int l0 = 3;
   static final int J = 3;
   static final int mw = 120;
   static final int mV = 2;
   static final int Ua = 1200;
   static final int UI = 200;
   static final int m5 = 400;
   static final int r = 400;
   static final int CF = 2;
   static final int Ug = 2;
   static final int mY = 400;
   static final int mZ = 3;
   static final double CW = 0.6;
   private final com.larpclient.module.e l2 = this.addSetting(
      new com.larpclient.module.e("Schematic", "File name in the schematics folder (a .litematic or .schem file). The list at the top picks one", "")
         .L("house.litematic")
   );
   private final com.larpclient.module.e m2 = this.addSetting(
      new com.larpclient.module.e("Origin", "Where the schematic's origin goes, as x y z. The buttons under Set where it goes set it", "").L("x y z")
   );
   private final com.larpclient.module.o Ci = this.addSetting(
      new com.larpclient.module.o("Rotation", "Turn the schematic clockwise around its origin, seen from above", "0", "0", "90", "180", "270")
   );
   private final com.larpclient.module.o C9 = this.addSetting(
      new com.larpclient.module.o("Mirror", "Mirror the schematic first: X flips east and west, Z flips north and south", "None", "None", "X", "Z")
   );
   private final com.larpclient.module.m Ur = this.C(
      "Auto Move", "Walk to blocks that are out of reach. Off: only blocks within reach of where you stand", true
   );
   private final com.larpclient.module.m nN = this.C("Sprint", "Sprint on longer walks", true);
   private final com.larpclient.module.m F = this.C("Safe Sneak", "Crouch next to drops so the bot never walks off an edge while it places", true);
   private final com.larpclient.module.m lu = this.C(
      "Refill Hotbar",
      "Move the block it needs from your inventory into the hotbar, standing still. Only empty slots and blocks the build no longer needs are replaced",
      true
   );
   private final com.larpclient.module.b mH = this.g("Reach", "How far blocks are placed from, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final com.larpclient.module.b lS = this.g("Place Delay", "Ticks between two placements (20 ticks are one second)", 3.0, 1.0, 20.0, 1.0);
   private final com.larpclient.module.m Ud = this.C("Auto Eat", "Eat food from your hotbar at 10 hunger or less", true);
   private final com.larpclient.module.m UF = this.C(
      "Show Preview", "Draw the schematic in the world: missing blocks in the theme color, wrong blocks red, wrongly turned blocks orange", true
   );
   private final com.larpclient.module.m U3 = this.C("Show Correct", "Also draw the blocks that are already right, in green", false);
   private final com.larpclient.module.b CA = this.g("Preview Range", "How far from you the preview draws blocks", 48.0, 8.0, 160.0, 8.0);
   private final com.larpclient.module.m UR = this.C("Show Path", "Draw the route the builder walks and the block it places next", true);
   private final com.larpclient.module.m C = this.C("Status Card", "Show a card at the top of the screen with the progress and what the builder is doing", true);
   private final com.larpclient.module.m nM = this.C("Notify When Done", "Show a notification when the build is finished", true);
   private final com.larpclient.module.m Ul = this.C(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final com.larpclient.module.m CL = this.C(
      "Temporary Pillars", "Pillar up on throwaway blocks to place blocks that are too high to reach from the ground, then dig the pillar away", true
   );
   private final com.larpclient.module.b mk = this.g("Max Pillar Height", "Tallest temporary pillar, in blocks", 6.0, 1.0, 64.0, 1.0);
   private final com.larpclient.module.o Uk = this.addSetting(
      new com.larpclient.module.o(
         "Pillar Block",
         "Throwaway block the pillars are built of. Never one the schematic uses",
         "Dirt",
         "Dirt",
         "Cobblestone",
         "Netherrack",
         "Cobbled Deepslate"
      )
   );
   private final com.larpclient.module.m t = this.C(
      "Restock From Containers", "When a block runs out, walk to a chest, barrel or shulker box you marked and take more from it", true
   );
   private final com.larpclient.module.m Cl = this.C(
      "Put Back Spares", "On a restock trip, put blocks the build no longer needs into the container, so the marked containers are the build's stockpile", true
   );
   private final com.larpclient.module.x l9 = this.addSetting(
      new com.larpclient.module.x(
         "Mark Restock Key", "Look at a chest, barrel or shulker box and press this to mark it as a restock container, or to unmark it", -1
      )
   );
   private final com.larpclient.module.e lB = this.addSetting(
      new com.larpclient.module.e(
            "Restock Containers", "Marked containers, as dimension x y z, separated by commas. The Mark Restock Key and the Restock buttons set them", ""
         )
         .L("minecraft:overworld x y z")
   );
   private final com.larpclient.module.b mm = this.g(
      "Restock Distance", "Farthest a marked container may be for the builder to walk to it", 64.0, 8.0, 256.0, 8.0
   );
   private final com.larpclient.module.m lk = this.C("Show Restock Containers", "Outline the marked containers in the world", true);
   private final com.larpclient.module.m p = this.C(
      "Stop On Staff Nearby", "Stop building and log off when someone Staff List counts as staff comes within range, invisible ones included", true
   );
   private final com.larpclient.module.b nW = this.g("Staff Range", "How close staff may come before the builder stops, in blocks", 48.0, 8.0, 128.0, 4.0);
   private final com.larpclient.module.b Cb = this.g("Log Off Delay", "Seconds between stopping and logging off", 5.0, 0.0, 30.0, 0.5);
   private final com.larpclient.module.e U0 = this.addSetting(
      new com.larpclient.module.e(
         "Log Off Message", "Shown on the disconnect screen when staff nearby makes the builder log off", "Auto Builder stopped: staff nearby"
      )
   );
   private final com.larpclient.module.m lL = this.C(
      "Buy Missing Blocks",
      "When a block runs out and no marked container has it, buy it from /ah on DonutSMP, then go on building. Spends your in-game money",
      false
   );
   private final com.larpclient.module.b f = this.g(
         "Max Price Per Item", "Most it pays for one block, whatever the size of the listing. Type amounts like 50, 2K or 1.5M", 100.0, 1.0, 1.0E7, 1.0
      )
      .J();
   private final com.larpclient.module.b mh = this.g(
         "Max Price Per Buy", "Most it pays for one listing. Type amounts like 10K, 5M or 1B", 10000.0, 100.0, 1.0E10, 100.0
      )
      .J();
   private final com.larpclient.module.b l8 = this.g(
         "Max Total Spend", "Most it spends in one run, all purchases together. Type amounts like 100K, 10M or 1.5B", 100000.0, 1000.0, 1.0E12, 1000.0
      )
      .J();
   private final com.larpclient.module.o Us = this.addSetting(
      new com.larpclient.module.o(
         "Placement Mode",
         "Normal: your camera turns to every block with the builder, like a player's. Aggressive: while it places, your camera stays where you look and only the look sent to the server turns, easing back to your look afterwards. Walking still turns your camera",
         "Normal",
         "Normal",
         "Aggressive"
      )
   );
   private final com.larpclient.module.m UT = this.C(
      "Follow Recording",
      "Build in the order you placed the blocks when you recorded this schematic with the Build Recorder. Ignored when it has no recording",
      true
   );
   private final com.larpclient.module.m UA = this.C(
      "Relog On Ghost Blocks",
      "When the server keeps taking placed blocks back (8 in a minute), log off, rejoin the same server and carry on building. At most once every 5 minutes and 3 times a build, never while temporary blocks stand",
      false
   );
   private static final String nd = "No schematic";
   private int U2;
   private final com.larpclient.module.o Cq = new com.larpclient.module.o(
      "File", "A .litematic or .schem file from the schematics folder. New files appear here by themselves", "No schematic", "No schematic"
   ) {
      public String getValue() {
         String var1 = _d.this.l2.getValue().trim();
         return !var1.isEmpty() && !_d.this.UH ? var1 : "No schematic";
      }

      @Override
      public boolean acceptsValue(String var1) {
         return true;
      }

      @Override
      public void setValue(String var1) {
         _d.this.e(var1);
      }

      @Override
      public boolean isSearchable() {
         return this.j().size() > 8;
      }

      @Override
      public String getModeLabel(String var1) {
         return var1.replaceFirst("(?i)\\.(litematic|schem|schematic)$", "");
      }
   };
   private volatile _d.p Ub;
   private volatile boolean ll;
   private int UU;
   private String C5 = "";
   private o4 lz;
   private String l;
   private _d.t mt;
   private boolean Cj = (boolean)1;
   private boolean UH;
   private boolean UW;
   private Object mj;
   private Object lG;
   private String lp;
   private String lf;
   private final hw UX = new hw(hw.b.QUICK);
   private final bt ld = new bt(this);
   private final ad q = new ad(this, this.UX, this::n);
   private final Map<Integer, Integer> mO = new HashMap<>();
   private final Map<Integer, Integer> nY = new HashMap<>();
   private final Map<Integer, Integer> l3 = new HashMap<>();
   private final Set<Integer> mX = new HashSet<>();
   private final Set<Integer> UO = new HashSet<>();
   private final Map<Integer, String> C0 = new HashMap<>();
   private final Map<Item, Integer> mM = new LinkedHashMap<>();
   private id.w UM;
   private id.w lm;
   private int my = -1000000000;
   private _d.w C2 = _d.w.v;
   private BlockState lj;
   private final Map<Integer, _d.w> CJ = new HashMap<>();
   private final Set<Integer> nV = new HashSet<>();
   private Integer m_;
   private int T;
   private int lT = -1000000000;
   private int mD = -1000000000;
   private int mU;
   private int CI;
   private final Map<Integer, BlockState> lO = new HashMap<>();
   private id.s U9;
   private _d.y ma;
   private final List<BlockPos> l1 = new ArrayList<>();
   private final Set<Long> CZ = new HashSet<>();
   private int ly;
   private int Un;
   private int i;
   private int nB;
   private id.u C1;
   private _d.f lD;
   private final List<BlockPos> UJ = new ArrayList<>();
   private final Set<Long> d = new HashSet<>();
   private final Map<Integer, Integer> np = new HashMap<>();
   private int lQ;
   private int E;
   private int Uo = -1000000000;
   private int UK;
   private int nO;
   private int no;
   private final Set<Long> UB = new HashSet<>();
   private BlockPos CU;
   private int lW;
   private int lZ;
   private final Map<Integer, Integer> I = new HashMap<>();
   private final Set<Integer> mA = new HashSet<>();
   private int m0;
   private long W = Long.MIN_VALUE;
   private long Ue = Long.MIN_VALUE;
   private boolean Uy;
   private int CM;
   private int Uc;
   private final List<BlockPos> lA = new ArrayList<>();
   private final Set<Long> Y = new HashSet<>();
   private boolean UZ;
   private int U1;
   private int CQ = -1000000000;
   private final SplittableRandom UN = new SplittableRandom();
   private int CC;
   private int H = -1000000000;
   private int ln = -1000000000;
   private double k = 0.6;
   private double mg;
   private boolean m8;
   private boolean Uu;
   private float ns = Float.NaN;
   private float P;
   private float ls;
   private int C7;
   private int lY;
   private int mz;
   private final List<BlockPos> m4 = new ArrayList<>();
   private boolean Cd;
   private final Set<BlockPos> n4 = new HashSet<>();
   private final Set<Integer> U8 = new HashSet<>();
   private List<id.p> Cw;
   private final Set<Integer> me = new HashSet<>();
   private final Set<Integer> lU = new HashSet<>();
   private id.p l5;
   private id.p Ux;
   private id.s e;
   private final List<BlockPos> md = new ArrayList<>();
   private int C_;
   private int Um;
   private int nl = -1;
   private BlockPos nQ;
   private int mr;
   private int ms = -1000000000;
   private int Uj;
   private int mW;
   private int Cf;
   private int mb;
   private Direction Ct;
   private BlockPos CB;
   private int mu;
   private final List<List<id.w>> Cg = new ArrayList<>();
   private int mB;
   private int j;
   private _d.j Ck;
   private _d.c Cv;
   private final Set<_d.c> V = new HashSet<>();
   private final Map<Item, Integer> Z = new HashMap<>();
   private final Set<Integer> ni = new HashSet<>();
   private int CY;
   private int Ch;
   private int Cu;
   private int mE;
   private int mT = -1000000000;
   private int lg = Integer.MAX_VALUE;
   private int A;
   private boolean nD;
   private String C8;
   private final vk x = new vk("Auto Builder");
   private Item R;
   private long l6;
   private int Cz;
   private int lt;
   private boolean n9;
   private final Set<Item> L = new HashSet<>();
   static final long lR = 10000L;
   static final long N = 2000L;
   private boolean mC;
   private boolean D;
   private boolean Cm;
   private long Ut;
   private long mP;
   private _d.h lc;
   private final List<_d.c> m3 = new ArrayList<>();
   private final Map<_d.c, Map<Item, Integer>> CK = new HashMap<>();
   private final Map<_d.c, Integer> Cc = new HashMap<>();
   private final Set<_d.c> C3 = new HashSet<>();
   private final Set<_d.c> Up = new HashSet<>();
   private final Set<_d.c> Cy = new HashSet<>();
   private final Map<Item, Integer> m6 = new LinkedHashMap<>();
   private final Map<Item, Integer> K = new LinkedHashMap<>();
   private final Map<Item, Integer> UL = new LinkedHashMap<>();
   private final Map<Item, Integer> lr = new LinkedHashMap<>();
   private final Map<Item, Long> UC = new LinkedHashMap<>();
   private int a;
   private final _b Uv = new _b();
   private boolean UY;
   private final List<Integer> mL = new ArrayList<>();
   private final Set<Integer> lM = new HashSet<>();
   private BitSet Co;
   private int[] Uh;
   private _d.t mn;
   private String mI;
   private int UD = -1000000000;
   private final ArrayDeque<Integer> mi = new ArrayDeque<>();
   private int lV = 0;
   private int nn;
   private int nv;
   private ServerData mS;
   private long y;
   private long v;
   private long UP;
   private long b;
   private boolean UQ;
   private boolean m;
   private BlockHitResult CH;
   private int mQ;
   private int Cs = -1000000000;
   private int u = -1;
   private int mq;
   private int UG = -1000000000;
   private int Cp = -1000000000;
   private int m9;
   private int mF;
   private int mK = -1000000000;
   private int l4;
   private int nx;
   private final id.b l7 = new id.b();
   private BlockPos Uz;
   private final Map<Integer, Integer> U4 = new HashMap<>();
   private List<id.w> mv = List.of();
   private BlockPos Cr;
   private long C6;
   private int lw;
   private String lK = "";
   private Vec3 CE = Vec3.ZERO;
   private boolean o;
   private boolean B;
   private boolean lF;
   private int mx = Integer.MIN_VALUE;
   private String g = "";
   private boolean CR;
   private final Map<Item, String> Uf = new LinkedHashMap<>();
   private static final Pattern U_ = Pattern.compile("(?i)\\.(litematic|schematic|schem)$");

   static List<_d.c> Z(String var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         return var1;
      }

      for (String var5 : var0.split(",")) {
         String[] var6 = var5.trim().split(" +");
         if (var6.length == 4 && !var6[0].isEmpty()) {
            try {
               _d.c var7 = new _d.c(var6[0], Integer.parseInt(var6[1]), Integer.parseInt(var6[2]), Integer.parseInt(var6[3]));
               if (!var1.contains(var7)) {
                  var1.add(var7);
               }
            } catch (NumberFormatException var8) {
            }
         }
      }

      return var1;
   }

   static String D(List<_d.c> var0) {
      ArrayList var1 = new ArrayList();

      for (_d.c var3 : var0) {
         var1.add(var3.toString());
      }

      return String.join(", ", var1);
   }

   public _d() {
      super("Auto Builder", "Build a .litematic or .schem schematic block by block, walking to what is out of reach; or mine it out");
      m1 = this;
      this.lS.B("Ticks Between Blocks");
      this.Ul.B("Pause In Other Menus");
      this.l2.B("Schematic (file name)");
      this.m2.B("Origin (x y z)");
      this.Ci.B("Rotation");
      this.lB.B("Restock Containers");
      this.nW.a(this.p::getValue);
      this.Cb.a(this.p::getValue);
      this.U0.a(this.p::getValue);
      this.Cl.a(this.t::getValue);
      this.mm.a(this.t::getValue);
      this.mk.a(this.CL::getValue);
      this.Uk.a(this.CL::getValue);
      this.U3.a(this.UF::getValue);
      this.CA.a(this.UF::getValue);
      this.l2.w(var1 -> this.l());
      this.m2.w(var1 -> this.Cj = (boolean)1);
      this.Ci.w(var1 -> this.Cj = (boolean)1);
      this.C9.w(var1 -> this.Cj = (boolean)1);
      if (!c) {
         c = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            _d var1 = m1;
            if (var1 != null) {
               var1.U();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            _d var2 = m1;
            if (!var1 && var2 != null && var2.x.W()) {
               vk.a(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            _d var1 = m1;
            if (var1 != null) {
               var1.F();
               var1.A();
            }
         });
      }
   }

   public static _d Z() {
      return m1;
   }

   public static Path G() {
      return FabricLoader.getInstance().getGameDir().resolve("schematics");
   }

   @Override
   public void disable() {
      this.lp = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      Screen var1 = getMc() == null ? null : mc.gui.screen();
      return var1 == null || var1 instanceof ChatScreen || var1 instanceof PauseScreen || !this.Ul.getValue();
   }

   @Override
   public List<ac.b> commands() {
      return List.of(
         new ac.b("Schematic", "Next schematic", () -> this.d(1)),
         new ac.b("Schematic", "Previous schematic", () -> this.d(-1)),
         new ac.b("Schematic", "Reload it", this::m),
         new ac.b("Schematic", "Unload schematic", this::c),
         new ac.b("Schematic", "Open the schematics folder", this::U),
         new ac.b("Where it goes", "Origin at my feet", this::l),
         new ac.b("Where it goes", "Origin on the block I look at", this::s),
         new ac.b("Where it goes", "Turn it 90 degrees", this::I),
         new ac.b("Move it", "East (+X)", () -> this.Y(1, 0, 0)),
         new ac.b("Move it", "West (-X)", () -> this.Y(-1, 0, 0)),
         new ac.b("Move it", "Up", () -> this.Y(0, 1, 0)),
         new ac.b("Move it", "Down", () -> this.Y(0, -1, 0)),
         new ac.b("Move it", "South (+Z)", () -> this.Y(0, 0, 1)),
         new ac.b("Move it", "North (-Z)", () -> this.Y(0, 0, -1)),
         new ac.b("Restock", "Mark the container I look at", this::r),
         new ac.b("Restock", "Clear the restock marks", this::G),
         new ac.b("Restock", "Stock the materials", this::N),
         new ac.b("Build Recorder", "Start recording", this::j),
         new ac.b("Build Recorder", "Stop and save the recording", this::T),
         new ac.b("Build Recorder", "Delete the recording", this::O),
         new ac.b("Build", "List the materials", this::Q),
         new ac.b("Build", "Mine it out instead", this::J)
      );
   }

   @Override
   public String summary() {
      String var1 = this.l2.getValue().trim();
      if (var1.isEmpty()) {
         return "Put .litematic or .schem files in the schematics folder and press Next schematic.";
      }

      if (this.UH) {
         return "No schematic loaded. Next schematic or Reload it loads " + var1 + " or another one again.";
      }

      if (this.ll) {
         return "Loading " + var1 + "...";
      }

      if (this.l != null) {
         return var1 + ": " + this.l + ".";
      }

      if (this.lz == null) {
         return "Loading " + var1 + "...";
      }

      String var2 = this.lz.e() + ": " + this.lz.g() + " x " + this.lz.C() + " x " + this.lz.L() + ", " + this.lz.B() + " blocks";
      o4.h var3 = this.Y();
      if (var3 == null) {
         return var2 + ". Set the origin to place it.";
      }

      String var4 = " at " + var3.origin() + this.f();
      if (this.UW && this.isRuntimeActive()) {
         return var2 + var4 + ". " + this.R() + "." + this.V();
      }

      _d.t var5 = this.mt;
      return var5 != null && var5.h && this.isInGame()
         ? var2
            + var4
            + ". "
            + var5.y(qf.s.N)
            + " missing, "
            + (var5.y(qf.s.I) + var5.y(qf.s.X))
            + " wrong, "
            + var5.y(qf.s.j)
            + " done"
            + (this.lf != null ? ". " + this.lf : "")
            + "."
            + this.V()
         : var2 + var4 + "." + this.V();
   }

   private String f() {
      String var1 = this.Ci.getValue().equals("0") ? "" : ", turned " + this.Ci.getValue();
      return this.C9.getValue().equals("None") ? var1 : var1 + ", mirrored " + this.C9.getValue();
   }

   @Override
   public String detail() {
      _d.t var1 = this.mt;
      if (var1 != null && var1.h && this.isInGame()) {
         Map var2 = this.g(var1);
         if (var2.isEmpty()) {
            return var1.y(qf.s.N) > 0 ? "Missing blocks cannot be placed yet; List the materials says why." : "";
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

            int var7 = this.B((Item)var6.getKey());
            var3.append(var6.getValue())
               .append(' ')
               .append(F((Item)var6.getKey()))
               .append(var7 >= var6.getValue() ? "" : " (" + (var7 == 0 ? "none" : var7) + " on you)");
         }

         return var3.append('.').toString();
      } else {
         return "";
      }
   }

   @Override
   public List<ac.r> settingGroups() {
      BooleanSupplier var1 = () -> this.Y() != null;
      return List.of(
         new ac.r("Schematic", List.of(this.Cq))
            .with(
               new ac.m(
                  this::H,
                  List.of(
                     List.of(
                        this.k("Reload", "Reload it").availableWhen(() -> !this.l2.getValue().isBlank()),
                        this.k("Unload", "Unload schematic"),
                        this.k("Open folder", "Open the schematics folder")
                     )
                  ),
                  this::preview,
                  true
               )
            ),
         new ac.r("Placement", List.of(this.Ci, this.C9))
            .with(
               new ac.m(
                  this::w,
                  List.of(
                     List.of(this.k("At my feet", "Origin at my feet"), this.k("Block I look at", "Origin on the block I look at")),
                     List.of(
                        this.k("West", "West (-X)").availableWhen(var1),
                        this.k("East", "East (+X)").availableWhen(var1),
                        this.k("North", "North (-Z)").availableWhen(var1),
                        this.k("South", "South (+Z)").availableWhen(var1)
                     ),
                     List.of(this.k("Up", "Up").availableWhen(var1), this.k("Down", "Down").availableWhen(var1))
                  )
               )
            ),
         new ac.r("Blocks", List.of(this.lu)).with(new ac.m(this::M, List.of(List.of(this.k("List in chat", "List the materials"))))),
         new ac.r("While building", List.of(this.Us, this.UT, this.Ur, this.nN, this.F, this.Ud, this.CL)),
         new ac.r("Material chests", List.of(this.t, this.Cl, this.l9))
            .with(
               new ac.m(
                  this::X,
                  List.of(
                     List.of(this.k("Select chest", "Mark the container I look at"), this.k("Clear chests", "Clear the restock marks")),
                     List.of(this.k("Stock materials", "Stock the materials"))
                  )
               )
            ),
         new ac.r("Buying on DonutSMP", List.of(this.lL, this.f, this.mh, this.l8)),
         new ac.r("Safety", List.of(this.p, this.nW, this.UA)),
         new ac.r("Display", List.of(this.UF, this.U3, this.UR, this.C, this.nM, this.lk)),
         ac.r.advanced("Advanced", List.of(this.l2, this.m2, this.mH, this.lS, this.CA, this.Ul, this.mk, this.Uk, this.lB, this.mm, this.Cb, this.U0))
            .with(
               new ac.m(
                  () -> "",
                  List.of(
                     List.of(
                        this.k("Record build", "Start recording"),
                        this.k("Save recording", "Stop and save the recording"),
                        this.k("Delete recording", "Delete the recording"),
                        this.k("Mine it out", "Mine it out instead")
                     )
                  )
               )
            )
      );
   }

   private String H() {
      String var1 = this.l2.getValue().trim();
      if (this.Cq.j().size() <= 1 && var1.isEmpty()) {
         return "The schematics folder is empty.";
      }

      if (!var1.isEmpty() && !this.UH) {
         if (!this.ll && (this.lz != null || this.l != null)) {
            return this.l != null
               ? "Could not load " + var1 + ": " + this.l + "."
               : this.lz.g() + " x " + this.lz.C() + " x " + this.lz.L() + ", " + String.format(Locale.ROOT, "%,d", this.lz.B()) + " blocks";
         } else {
            return "Loading.";
         }
      } else {
         return "";
      }
   }

   private String w() {
      o4.h var1 = this.Y();
      return var1 == null ? "Not set." : "Origin at " + var1.origin() + this.f();
   }

   private int H(_d.t var1) {
      int var2 = 0;

      for (Entry var4 : this.g(var1).entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.B((Item)var4.getKey()));
      }

      return var2;
   }

   private String M() {
      _d.t var1 = this.mt;
      if (var1 != null && this.isInGame() && var1.h) {
         String var2 = this.detail();
         return var2.isEmpty() ? "Nothing more is needed." : var2;
      } else {
         return "";
      }
   }

   @Override
   public void panelTick() {
      if (this.U2-- <= 0) {
         this.U2 = 40;
         ArrayList var1 = new ArrayList();
         var1.add("No schematic");
         var1.addAll(o4.X(G()));
         this.Cq.y(var1);
      }
   }

   @Override
   public ac.u status() {
      if (this.UW && this.isRuntimeActive()) {
         return new ac.u(this.R() + ".", false);
      }

      String var1 = this.l2.getValue().trim();
      if (var1.isEmpty() || this.UH) {
         return new ac.u("Choose a schematic, then set where it goes.", false);
      }

      if (this.l != null) {
         return new ac.u("Could not load " + var1 + ": " + this.l + ".", true);
      }

      if (this.ll || this.lz == null) {
         return new ac.u("Loading " + var1 + ".", false);
      }

      if (this.Y() == null) {
         return new ac.u("Set where it goes, then turn the module on.", false);
      }

      if (!this.isInGame()) {
         return new ac.u("Schematic set. Join the world it goes in.", false);
      }

      _d.t var2 = this.mt;
      if (var2 != null && var2.h) {
         String var3 = ac.K(this);
         if (var3 != null) {
            return new ac.u(var3 + " is running. One bot runs at a time.", true);
         }

         if (this.UY) {
            return new ac.u("Build Recorder is recording. Stop and save it first.", true);
         }

         BlockPos var4 = mc.player.blockPosition();
         int var5 = Math.max(Math.max(var2.u - var4.getX(), var4.getX() - var2.k), Math.max(var2.B - var4.getZ(), var4.getZ() - var2.V));
         if (var5 > 32.0) {
            return new ac.u("You are " + var5 + " blocks from the build. Walk next to it.", true);
         }

         int var6 = var2.y(qf.s.N);
         int var7 = var2.y(qf.s.I) + var2.y(qf.s.X);
         if (var6 == 0 && var7 == 0) {
            return new ac.u("Every block already matches the schematic.", false);
         }

         String var8 = String.format(Locale.ROOT, "Will place %,d blocks", var6) + (var7 > 0 ? ", " + var7 + " wrong ones are left alone" : "") + ".";
         int var9 = this.H(var2);
         if (var9 == 0) {
            return new ac.u(var8, false);
         }

         int var10 = 0;

         for (_d.c var12 : Z(this.lB.getValue())) {
            if (var12.dimension().equals(this.P())) {
               var10++;
            }
         }

         int var13 = (!this.t.getValue() || var10 <= 0) && !this.lL.getValue() ? 0 : 1;
         return new ac.u(var8 + String.format(Locale.ROOT, " You are short of %,d.", var9), var13 == 0);
      } else {
         return new ac.u("Checking the world.", false);
      }
   }

   private String d(int var1) {
      List var2 = o4.X(G());
      if (var2.isEmpty()) {
         return "No schematics in " + G() + ". Put .litematic or .schem files there.";
      }

      int var3 = var2.indexOf(this.l2.getValue().trim());
      int var4 = var3 < 0 ? (var1 > 0 ? 0 : var2.size() - 1) : Math.floorMod(var3 + var1, var2.size());
      if (this.UW) {
         return "Turn the builder off before picking another schematic.";
      }

      this.w((String)var2.get(var4));
      return "Picked " + (String)var2.get(var4) + " (" + (var4 + 1) + " of " + var2.size() + ").";
   }

   private void w(String var1) {
      boolean var2 = var1.equals(this.l2.getValue().trim());
      this.l2.T(var1);
      if (var2 && this.UH) {
         this.l();
      }

      this.H();
   }

   private void e(String var1) {
      if ("No schematic".equals(var1)) {
         this.c();
      } else if (this.UW) {
         this.K("turn the builder off before picking another schematic.");
      } else {
         this.w(var1);
      }
   }

   private String m() {
      if (this.l2.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      if (this.UW) {
         return "Turn the builder off before reloading.";
      }

      this.l();
      return "Reloading " + this.l2.getValue().trim() + ".";
   }

   private String c() {
      if (this.UW) {
         this.K("turn the builder off before unloading the schematic.");
         return "Turn the builder off before unloading the schematic.";
      }

      boolean var1 = _6.m("the schematic was unloaded");
      if (this.UH || this.lz == null && !this.ll && this.l == null) {
         if (!var1) {
            this.K("no schematic is loaded.");
         }

         return var1 ? "Stopped Mine Out; no schematic was loaded." : "No schematic is loaded.";
      } else {
         String var2 = this.lz != null ? this.lz.e() : this.l2.getValue().trim();
         if (this.UY) {
            this.G("the schematic was unloaded");
         }

         this.UU++;
         this.Ub = null;
         this.ll = (boolean)0;
         this.lz = null;
         this.l = null;
         this.mt = null;
         this.Cj = (boolean)0;
         this.Uh = null;
         this.mn = null;
         this.lf = null;
         this.UH = (boolean)1;
         _q.z(this.getName(), "Unloaded {}{}; settings kept", var2, var1 ? " after stopping its Mine Out" : "");
         this.K(
            "unloaded "
               + var2
               + (var1 ? " and stopped mining it out" : "")
               + ". Your settings are kept; pick one in the list or press Reload to load one again."
         );
         return "Unloaded " + var2 + (var1 ? ", Mine Out stopped." : ".");
      }
   }

   private String U() {
      Path var1 = G();

      try {
         Files.createDirectories(var1);
      } catch (IOException var3) {
         return "Could not create " + var1 + ": " + var3.getMessage();
      }

      Blaze3D.openPath(var1);
      return "Opened " + var1 + ". Put .litematic or .schem files there, then choose one in the list.";
   }

   private String l() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.UW) {
         return "Turn the builder off before moving the schematic.";
      }

      BlockPos var1 = mc.player.blockPosition();
      return this.L(var1.getX(), var1.getY(), var1.getZ());
   }

   private String s() {
      if (!this.isInGame()) {
         return "Join a world first.";
      } else if (this.UW) {
         return "Turn the builder off before moving the schematic.";
      } else if (mc.hitResult instanceof BlockHitResult var1 && var1.getType() == Type.BLOCK) {
         BlockPos var3 = var1.getBlockPos().relative(var1.getDirection());
         return this.L(var3.getX(), var3.getY(), var3.getZ());
      } else {
         return "Look at a block first.";
      }
   }

   private String L(int var1, int var2, int var3) {
      this.m2.T(ol.Q(var1, var2, var3));
      this.H();
      return "Origin set to " + var1 + " " + var2 + " " + var3 + ".";
   }

   private String I() {
      if (this.UW) {
         return "Turn the builder off before turning the schematic.";
      }

      this.Ci.C();
      this.H();
      return "Turned to " + this.Ci.getValue() + " degrees.";
   }

   private String Y(int var1, int var2, int var3) {
      if (this.UW) {
         return "Turn the builder off before moving the schematic.";
      }

      int[] var4 = ol.z(this.m2.getValue());
      return var4 == null ? "Set the origin first." : this.L(var4[0] + var1, var4[1] + var2, var4[2] + var3);
   }

   private String Q() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      _d.t var1 = this.mt;
      if (var1 == null) {
         return this.lz == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var1.h) {
         return "Still checking the world; try again in a moment.";
      }

      Map var2 = this.g(var1);
      this.K("materials for " + this.lz.e() + " (" + var1.y(qf.s.N) + " missing, " + var1.y(qf.s.I) + " wrong blocks, " + var1.y(qf.s.X) + " turned wrong):");

      for (Entry var4 : var2.entrySet()) {
         int var5 = this.B((Item)var4.getKey());
         this.K(
            "  "
               + var4.getValue()
               + " "
               + F((Item)var4.getKey())
               + ", you carry "
               + var5
               + (var5 >= var4.getValue() ? "" : ", short by " + ((Integer)var4.getValue() - var5))
         );
      }

      TreeMap var6 = new TreeMap();

      for (int var7 = 0; var7 < var1.P; var7++) {
         String var9 = var1.g[var1.U[var7]];
         if (var9 != null && var1.C(var7) != qf.s.j) {
            var6.merge(o4.D(this.lz.u().get(var1.U[var7])).replace("minecraft:", "") + " (" + var9 + ")", 1, Integer::sum);
         }
      }

      for (Entry var10 : var6.entrySet()) {
         this.K("  left out: " + var10.getValue() + " " + (String)var10.getKey());
      }

      return var2.isEmpty() && var6.isEmpty() ? "Nothing is missing." : "Listed " + (var2.size() + var6.size()) + " materials in chat.";
   }

   private String J() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (!this.UW && !this.isRuntimeActive()) {
         _d.t var1 = this.mt;
         if (var1 == null) {
            return "Load a schematic and set its origin first.";
         }

         ArrayList var2 = new ArrayList(var1.P);

         for (int var3 = 0; var3 < var1.P; var3++) {
            var2.add(var1.s(var3));
         }

         String var4 = _6.b(var2, "the " + this.lz.e() + " schematic");
         if (var4 != null) {
            return var4;
         }

         _q.z(this.getName(), "Mine Out: handed {} cells of {} at {} to Auto Area Miner", var2.size(), this.lz.e(), var1.b.origin());
         return "Auto Area Miner is mining out " + this.lz.e() + ".";
      } else {
         return "Turn the builder off first; one bot runs at a time.";
      }
   }

   private Map<Item, Integer> g(_d.t var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.P; var3++) {
         if (var1.C(var3) == qf.s.N && var1.g[var1.U[var3]] == null && !var1.W(var3)) {
            BlockState var4 = var1.c(var3);
            BlockItem var5 = qf.h(var4);
            int var6 = qf.W(var4) && this.isInGame() ? qf.I(var4, mc.level.getBlockState(var1.s(var3))) : 1;
            if (var5 != null && var6 > 0) {
               var2.merge(var5, var6, Integer::sum);
            }
         }
      }

      ArrayList var7 = new ArrayList(var2.entrySet());
      var7.sort((var0, var1x) -> (Integer)var1x.getValue() - (Integer)var0.getValue());
      LinkedHashMap var8 = new LinkedHashMap();

      for (Entry var10 : var7) {
         var8.put((Item)var10.getKey(), (Integer)var10.getValue());
      }

      return var8;
   }

   private static String F(Item var0) {
      return new ItemStack(var0).getHoverName().getString();
   }

   private int B(Item var1) {
      if (!this.isInGame()) {
         return 0;
      }

      Inventory var2 = mc.player.getInventory();
      int var3 = 0;

      for (int var4 = 0; var4 < 36; var4++) {
         if (var2.getItem(var4).is(var1)) {
            var3 += var2.getItem(var4).getCount();
         }
      }

      if (mc.player.getOffhandItem().is(var1)) {
         var3 += mc.player.getOffhandItem().getCount();
      }

      return var3;
   }

   private void H() {
      LarpClient var1 = LarpClient.N();
      if (var1 != null && var1.h() != null) {
         var1.h().z();
      }
   }

   private void l() {
      this.UH = (boolean)0;
      int var1 = ++this.UU;
      String var2 = this.l2.getValue().trim();
      this.lz = null;
      this.l = null;
      this.mt = null;
      this.Cj = (boolean)1;
      this.C5 = var2;
      if (var2.isEmpty()) {
         this.ll = (boolean)0;
      } else if (!var2.contains("/") && !var2.contains("\\") && !var2.contains("..")) {
         this.ll = (boolean)1;
         Path var3 = G().resolve(var2);
         Thread var4 = new Thread(() -> {
            _d.p var4x;
            try {
               if (!Files.isRegularFile(var3)) {
                  var4x = new _d.p(var2, null, "no such file in the schematics folder");
               } else {
                  var4x = new _d.p(var2, o4.n(var3), null);
               }
            } catch (IOException | RuntimeException var6) {
               var4x = new _d.p(var2, null, var6.getMessage() == null ? var6.getClass().getSimpleName() : var6.getMessage());
            }

            if (var1 == this.UU) {
               this.Ub = var4x;
            }
         }, "Larp-SchematicLoad");
         var4.setDaemon(true);
         var4.start();
      } else {
         this.l = "only a file name inside the schematics folder works";
      }
   }

   private void F() {
      this.Z();
      if (this.UH) {
         if (this.UY) {
            this.G("the schematic was unloaded");
         }
      } else {
         _d.p var1 = this.Ub;
         if (var1 != null) {
            this.Ub = null;
            this.ll = (boolean)0;
            if (var1.file().equals(this.C5)) {
               this.lz = var1.schematic();
               this.l = var1.error();
               this.Cj = (boolean)1;
               if (this.lz != null) {
                  _q.z(
                     this.getName(),
                     "Loaded {} ({}): {}, {} regions, {} palette entries",
                     var1.file(),
                     this.lz.v(),
                     this.lz.Y(),
                     this.lz.g().size(),
                     this.lz.u().size()
                  );
               } else {
                  _q.z(this.getName(), "Could not load {}: {}", var1.file(), this.l);
               }
            }
         }

         if (this.lz == null && !this.ll && this.l == null && !this.l2.getValue().isBlank()) {
            this.l();
         } else {
            if (this.Cj) {
               this.Cj = (boolean)0;
               o4.h var2 = this.Y();
               this.mt = this.lz != null && var2 != null ? new _d.t(this.lz, var2) : null;
               if (this.mt != null) {
                  _q.c(
                     this.getName(),
                     "Layout at {}{}: box {} to {}",
                     var2.origin(),
                     this.f(),
                     this.mt.u + " " + this.mt.F + " " + this.mt.B,
                     this.mt.k + " " + this.mt.m + " " + this.mt.V
                  );
               }
            }

            _d.t var3 = this.mt;
            if (var3 != this.mn) {
               if (this.UY) {
                  this.G("the schematic or its origin changed");
               }

               this.e(var3);
            }

            if (var3 != null && this.isInGame()) {
               this.E(var3);
               if (var3.o != mc.level) {
                  var3.o = mc.level;
                  var3.Y = 0;
                  var3.h = (boolean)0;
                  Arrays.fill(var3.S, 0);
                  var3.s.clear();
               }

               this.h(var3, 4096);
            } else {
               if (this.UY) {
                  this.G("the schematic or the world changed");
               }
            }
         }
      }
   }

   private o4.h Y() {
      int[] var1 = ol.z(this.m2.getValue());
      if (var1 == null) {
         return null;
      }

      int var2;
      try {
         var2 = Integer.parseInt(this.Ci.getValue());
      } catch (NumberFormatException var6) {
         var2 = 0;
      }
      o4.p var3 = switch ((String)this.C9.getValue()) {
         case "X" -> o4.p.e;
         case "Z" -> o4.p.n;
         default -> o4.p.W;
      };
      return new o4.h(var1[0], var1[1], var1[2], var2, var3);
   }

   private void h(_d.t var1, int var2) {
      if (var1.P != 0) {
         MutableBlockPos var3 = new MutableBlockPos();
         double var4 = this.CA.getValue();
         double var6 = mc.player.getX();
         double var8 = mc.player.getY();
         double var10 = mc.player.getZ();

         for (int var12 = 0; var12 < var2; var12++) {
            int var13 = var1.Y;
            qf.s var14 = this.C(var1, var13, var3);
            var1.S[var14.ordinal()]++;
            if (var14 != qf.s.x && var1.c(var13) != null) {
               double var15 = var3.getX() + 0.5 - var6;
               double var17 = var3.getY() + 0.5 - var8;
               double var19 = var3.getZ() + 0.5 - var10;
               double var21 = var15 * var15 + var17 * var17 + var19 * var19;
               if (var21 <= var4 * var4 && (var14 != qf.s.j || this.U3.getValue())) {
                  var1.s.add(new long[]{(long)var21, var13});
                  if (var1.s.size() > 2048) {
                     var1.s.poll();
                  }
               }
            }

            if (++var1.Y >= var1.P) {
               var1.Y = 0;
               System.arraycopy(var1.S, 0, var1.y, 0, var1.y.length);
               Arrays.fill(var1.S, 0);
               var1.h = (boolean)1;
               int[] var23 = new int[var1.s.size()];
               int var16 = 0;

               for (long[] var18 : var1.s) {
                  var23[var16++] = (int)var18[1];
               }

               var1.i = var23;
               var1.s.clear();
               if (var1.P < var2) {
                  break;
               }
            }
         }
      }
   }

   private qf.s C(_d.t var1, int var2, MutableBlockPos var3) {
      var3.set(var1.d[var2]);
      BlockState var4 = var1.c(var2);
      qf.s var5;
      if (!mc.level.hasChunkAt(var3)) {
         var5 = qf.s.x;
      } else if (var4 == null) {
         var5 = qf.s.j;
      } else {
         var5 = qf.j(var4, mc.level.getBlockState(var3));
      }

      var1.E[var2] = (byte)var5.ordinal();
      return var5;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.i();
      this.m9 = 0;
      this.lf = null;
      if (this.lp != null) {
         String var4 = this.lp;
         this.lp = null;
         if (this.Cm) {
            this.Cm = (boolean)0;
            this.mP = 0L;
            this.h("stocking stopped earlier because " + var4 + ". Press Stock materials to go on");
         } else {
            this.h("it stopped earlier because " + var4 + ". Turn it on again to carry on");
         }
      } else if (this.lV != 3 && this.lV != 2) {
         this.nv = 0;
         this.UQ = (boolean)0;
         int var3 = System.currentTimeMillis() - this.mP < 2000L ? 1 : 0;
         this.mP = 0L;
         String var5 = ac.K(this);
         if (var5 != null) {
            this.h("turn off " + var5 + " first; one bot runs at a time");
         } else if (var3 != 0) {
            this.t();
         } else {
            this.G();
         }
      } else {
         ServerData var1 = mc.getCurrentServer();
         int var2 = var1 != null && this.mS != null && var1.ip.equalsIgnoreCase(this.mS.ip) ? 1 : 0;
         this.lV = 0;
         this.mS = null;
         if (var2 == 0) {
            this.h("it logged off to resync blocks and you joined another server. Turn it on again to carry on");
         } else {
            this.b = System.currentTimeMillis() + 3000L;
            _q.z(this.getName(), "Rejoined after the relog; carrying on in {} ms", 3000L);
            this.P("Rejoined; waiting for the world to load");
         }
      }
   }

   private void G() {
      if (this.UY) {
         this.h("the Build Recorder is recording; stop and save it first");
      } else if (_6.p()) {
         this.h("Auto Area Miner is mining a schematic out; turn it off first");
      } else if (this.UH) {
         this.h("the schematic is unloaded; pick it in the list or press Reload first");
      } else if (this.lz == null) {
         this.h(this.l != null ? "the schematic did not load: " + this.l : "pick a schematic first");
      } else {
         if (this.Cj) {
            this.F();
         }

         _d.t var1 = this.mt;
         if (var1 == null) {
            this.h("set the origin first");
         } else {
            BlockPos var2 = mc.player.blockPosition();
            double var3 = Math.max(Math.max(var1.u - var2.getX(), var2.getX() - var1.k), Math.max(var1.B - var2.getZ(), var2.getZ() - var1.V));
            if (var3 > 32.0) {
               this.h("stand next to the build to start (you are " + (int)var3 + " blocks away)");
            } else {
               if (!var1.h || var1.P <= 200000) {
                  this.h(var1, var1.P);
               }

               this.UW = (boolean)1;
               this.mj = mc.level;
               this.lG = mc.player;
               this.ld.v();
               this.C6 = System.currentTimeMillis();
               this.lw = var1.y(qf.s.N);
               this.l4 = var1.y(qf.s.j);
               this.K(
                  "building " + this.lz.e() + " at " + var1.b.origin() + this.f() + ": " + this.lw + " blocks to place, " + var1.y(qf.s.j) + " already done."
               );
               ArrayList var5 = new ArrayList();
               Map var6 = this.g(var1);
               ArrayList var7 = new ArrayList();

               for (Entry var9 : var6.entrySet()) {
                  int var10 = this.B((Item)var9.getKey());
                  if (var10 < (Integer)var9.getValue()) {
                     var7.add((Integer)var9.getValue() - var10 + " " + F((Item)var9.getKey()));
                  }
               }

               int var12 = 0;

               for (_d.c var15 : Z(this.lB.getValue())) {
                  if (var15.dimension().equals(this.P())) {
                     var12++;
                  }
               }

               if (!var7.isEmpty()) {
                  var5.add(
                     "you are short of "
                        + String.join(", ", var7.subList(0, Math.min(4, var7.size())))
                        + (var7.size() > 4 ? " and " + (var7.size() - 4) + " more" : "")
                        + (this.t.getValue() && var12 > 0 ? " (it restocks from " + var12 + " marked " + (var12 == 1 ? "container)" : "containers)") : "")
                  );
               }

               int var14 = var1.y(qf.s.I) + var1.y(qf.s.X);
               int var16 = 0;

               for (int var11 = 0; var11 < var1.P; var11++) {
                  if (var1.C(var11) == qf.s.X && qf.n(var1.c(var11), mc.level.getBlockState(var1.s(var11))) > 0) {
                     var16++;
                  }
               }

               if (var14 > var16) {
                  var5.add(var14 - var16 + " cells hold another block or face the wrong way; they are left as they are");
               }

               if (var16 > 0) {
                  this.K(var16 + " blocks are set by right clicks (repeater delays, comparator modes, notes, open doors or levers).");
               }

               if (this.Ud.getValue() && bt.a(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                  var5.add("no food in the hotbar");
               }

               if (!var5.isEmpty()) {
                  this.K("heads up: " + String.join("; ", var5) + ".");
               }

               _q.z(
                  this.getName(),
                  "Started: {} at {}{}, {} to place, {} done, {} wrong, box {} {} {} to {} {} {}",
                  this.lz.e(),
                  var1.b.origin(),
                  this.f(),
                  this.lw,
                  var1.y(qf.s.j),
                  var14,
                  var1.u,
                  var1.F,
                  var1.B,
                  var1.k,
                  var1.m,
                  var1.V
               );
               _q.z(this.getName(), "Player at {} when the run started", this.r().toShortString());
               if (this.lw == 0) {
                  this.y();
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.UW && this.lV != 0) {
         _q.z(this.getName(), "Logged off for the relog after {} blocks", this.m9);
      } else if (this.isEnabled() && this.UW) {
         this.lp = "the world, the player or the connection changed";
         this.Cm = this.mC;
         _q.z(this.getName(), "Interrupted after {} blocks: {}", this.m9, this.lp);
      }

      super.onDisable();
      vi.J(this);
      vs.B(this);
      if (!this.l1.isEmpty()) {
         BlockPos var1 = this.l1.getFirst();
         this.K("left " + this.l1.size() + " temporary " + (this.l1.size() == 1 ? "block" : "blocks") + " in the pillar at " + var1.toShortString() + ".");
         _q.z(this.getName(), "Stopped with {} temporary blocks standing from {}", this.l1.size(), var1.toShortString());
      }

      if (!this.md.isEmpty()) {
         this.K(
            "left "
               + this.md.size()
               + " temporary "
               + (this.md.size() == 1 ? "block" : "blocks")
               + " in the tower at "
               + this.md.getFirst().toShortString()
               + "."
         );
         _q.z(this.getName(), "Stopped with {} tower blocks standing beside a doorway from {}", this.md.size(), this.md.getFirst().toShortString());
      }

      if (!this.m4.isEmpty()) {
         this.K(
            "left " + this.m4.size() + " temporary " + (this.m4.size() == 1 ? "block" : "blocks") + " starting at " + this.m4.getFirst().toShortString() + "."
         );
         _q.z(this.getName(), "Stopped with {} temporary blocks not swept up, from {}", this.m4.size(), this.m4.getFirst().toShortString());
      }

      if (!this.lA.isEmpty()) {
         this.K(
            "left " + this.lA.size() + " temporary walkway " + (this.lA.size() == 1 ? "block" : "blocks") + " at " + this.lA.getFirst().toShortString() + "."
         );
         _q.z(this.getName(), "Stopped with {} walkway blocks from {}", this.lA.size(), this.lA.getFirst().toShortString());
      }

      if (!this.UJ.isEmpty()) {
         this.K("left " + this.UJ.size() + " temporary " + (this.UJ.size() == 1 ? "block" : "blocks") + " at " + this.UJ.getFirst().toShortString() + ".");
         _q.z(this.getName(), "Stopped with {} temporary support blocks at {}", this.UJ.size(), this.UJ.getFirst().toShortString());
      }

      this.ld.b();
      this.i();
   }

   private void i() {
      this.UW = (boolean)0;
      this.mj = this.lG = null;
      this.mO.clear();
      this.nY.clear();
      this.l3.clear();
      this.mX.clear();
      this.C0.clear();
      this.UO.clear();
      this.mA.clear();
      this.I.clear();
      this.CU = null;
      this.UB.clear();
      this.Uo = -1000000000;
      this.mM.clear();
      this.U4.clear();
      this.CJ.clear();
      this.nV.clear();
      this.m_ = null;
      this.lT = this.mD = -1000000000;
      this.mU = this.CI = 0;
      this.lO.clear();
      this.C2 = _d.w.v;
      this.lj = null;
      this.U9 = null;
      this.ma = null;
      this.l1.clear();
      this.CZ.clear();
      this.ly = this.Un = this.i = this.nB = 0;
      this.lA.clear();
      this.Ct = null;
      this.CB = null;
      this.mu = 0;
      this.ns = Float.NaN;
      this.P = this.ls = 0.0F;
      this.C7 = this.lY = this.CC = this.mz = 0;
      this.H = this.ln = -1000000000;
      this.m8 = this.Uu = (boolean)0;
      this.Cw = null;
      this.me.clear();
      this.lU.clear();
      this.U8.clear();
      this.Cd = (boolean)0;
      this.n4.clear();
      this.Um = 0;
      this.nl = -1;
      this.l5 = this.Ux = null;
      this.e = null;
      this.md.clear();
      this.C_ = 0;
      this.Y.clear();
      this.mb = 0;
      this.UZ = (boolean)0;
      this.Uj = this.mW = 0;
      this.m4.clear();
      this.nQ = null;
      this.mr = 0;
      this.Cf = 0;
      this.C1 = null;
      this.lD = null;
      this.UJ.clear();
      this.d.clear();
      this.CM = this.Uc = this.mB = this.j = this.lQ = this.m0 = 0;
      this.np.clear();
      this.W = Long.MIN_VALUE;
      this.Ue = Long.MIN_VALUE;
      this.Uy = (boolean)0;
      this.Cg.clear();
      this.Ck = null;
      this.Cv = null;
      this.V.clear();
      this.Z.clear();
      this.ni.clear();
      this.CY = this.Ch = this.Cu = this.mE = 0;
      this.mT = -1000000000;
      this.lg = Integer.MAX_VALUE;
      this.A = 0;
      this.C8 = null;
      this.UD = -1000000000;
      if (this.x.W()) {
         this.x.B("the builder stopped");
      }

      this.R = null;
      this.l6 = 0L;
      this.Cz = this.lt = 0;
      this.n9 = (boolean)0;
      this.L.clear();
      this.mC = this.D = (boolean)0;
      this.lc = null;
      this.m3.clear();
      this.CK.clear();
      this.Cc.clear();
      this.C3.clear();
      this.Up.clear();
      this.Cy.clear();
      this.m6.clear();
      this.K.clear();
      this.UL.clear();
      this.lr.clear();
      this.UC.clear();
      this.Uf.clear();
      this.a = 0;
      this.lm = null;
      this.my = -1000000000;
      this.mi.clear();
      this.b = 0L;
      this.UM = null;
      this.CH = null;
      this.mQ = 0;
      this.Cs = -1000000000;
      this.u = -1;
      this.mq = 0;
      this.UG = -1000000000;
      this.Cp = -1000000000;
      this.mF = this.l4 = this.nx = 0;
      this.l7.w();
      this.Uz = null;
      this.mK = -1000000000;
      this.mv = List.of();
      this.Cr = null;
      this.q.V();
      this.UX.W();
      this.CE = Vec3.ZERO;
      this.o = this.B = this.lF = (boolean)0;
      this.mx = Integer.MIN_VALUE;
      this.lK = "";
      this.lw = 0;
   }

   @Override
   public void onTick() {
      byte var6 = 1;
      int var5 = this.U1;
      this.U1 = var5 + var6;
      this.CE = Vec3.ZERO;
      this.o = this.B = this.lF = (boolean)0;
      if (!this.UW) {
         if (this.b != 0L) {
            this.K();
         }
      } else if (mc.level == this.mj && mc.player == this.lG) {
         _d.t var1 = this.mt;
         if (var1 == null) {
            this.h("the schematic or its origin changed");
         } else {
            this.mx = mc.player.tickCount;
            int var10001;
            if (!mc.player.isShiftKeyDown()) {
               var10001 = -1000000000;
            } else {
               int var8 = -1000000000;
               int var7 = this.ms;
               var10001 = Integer.compare(var7, var8);
               var10001 = (var10001 | -var10001) >>> 31 == 0 ? mc.player.tickCount : this.ms;
            }

            this.ms = var10001;
            this.Q();
            if (this.C8 == null && (!this.p.getValue() || !this.j())) {
               String var2 = bt.L();
               if (var2 != null) {
                  this.E();
                  this.ld.s();
                  this.q.V();
                  this.o = (boolean)1;
                  this.P(var2 + ": swimming up");
               } else if (this.Ud.getValue() && this.ld.T(10)) {
                  this.E();
                  this.P("Eating");
               } else if (mc.gui.screen() instanceof AbstractSignEditScreen var3 && mc.player.tickCount - this.Cs < 40) {
                  var3.onClose();
               } else if (this.m_ == null || !this.a(var1)) {
                  if (this.Ck != _d.j.m || (this.mC ? !this.i() : !this.X(var1))) {
                     if (this.R != null) {
                        this.B();
                     } else if (!this.Z()) {
                        this.z();
                        this.P(mc.gui.screen() != null ? "Paused while a screen is open" : "Waiting for world or interaction control");
                     } else if (this.W()) {
                        this.z();
                        this.q.V();
                        this.UX.W();
                        this.P("Paused for manual movement");
                     } else if (mc.gui.screen() instanceof AbstractContainerScreen) {
                        this.z();
                        this.P("Holding still while an inventory menu is open");
                     } else {
                        this.l(var1);
                        if (this.R()) {
                           this.J();
                        } else if (this.ma == _d.y.m) {
                           this.d(var1);
                        } else if (this.ma == _d.y.D) {
                           this.T();
                        } else if (this.ma == _d.y.J) {
                           this.v();
                        } else if (this.ma == _d.y.E) {
                           this.h();
                        } else if (this.ma == _d.y.e) {
                           this.W();
                        } else if (!mc.player.onGround() && !mc.player.isInWater()) {
                           if (this.q.X()) {
                              this.R();
                           } else {
                              this.E();
                              this.P("Waiting to land");
                           }
                        } else if (this.mC) {
                           this.V(var1);
                        } else if (this.UM != null) {
                           if (this.m()) {
                              ij.V(ij.r.z);
                           }

                           if (this.C2 == _d.w.v) {
                              this.i(var1);
                           } else {
                              this.S(var1);
                           }
                        } else if (this.B() && ij.L() == ij.r.o) {
                           this.z();
                           this.P("Turning back to your look");
                        } else if (this.nx > 0) {
                           this.Y();
                        } else {
                           int var10000 = mc.player.tickCount - this.Cs;
                           int var10 = this.lS.getValue().intValue();
                           int var9 = var10000;
                           if ((Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
                              this.e();
                           } else {
                              var10000 = mc.player.tickCount - this.Cs;
                              int var12 = this.lS.getValue().intValue() + this.CC;
                              int var11 = var10000;
                              if ((Integer.compare(var11, var12) >>> 31 ^ 1) == 0) {
                                 this.e();
                              } else if (this.P()) {
                                 if (this.q.X()) {
                                    this.R();
                                 } else {
                                    this.e();
                                 }
                              } else {
                                 this.W(var1);
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               this.f();
            }
         }
      } else {
         this.h("the world or player changed");
      }
   }

   private boolean P() {
      BlockStatePredictionHandler var1 = ((qa6b3f1c381a58196)mc.level).LarpRig$getPendingUpdateManager();
      if (((q0f2922fe81ac8eae)var1).larp$unconfirmed().isEmpty()) {
         this.my = -1000000000;
         return false;
      }

      int var2 = mc.player.tickCount;
      if (this.my == -1000000000) {
         this.my = var2;
      }

      if (var2 - this.my == 40) {
         _q.z(this.getName(), "The server has not confirmed a block change for {} ticks; going on", 40);
      }

      if (var2 - this.my >= 40) {
         return false;
      }

      this.P("Waiting for the server to confirm the last block");
      return true;
   }

   private void z() {
      this.E();
      this.CE = Vec3.ZERO;
   }

   private void E() {
      vi.J(this);
      this.UM = null;
      this.CH = null;
   }

   private boolean W() {
      return mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            nz.K(mc.options.keyUp)
               || nz.K(mc.options.keyDown)
               || nz.K(mc.options.keyLeft)
               || nz.K(mc.options.keyRight)
               || nz.K(mc.options.keyJump)
               || nz.K(mc.options.keyShift)
         );
   }

   private double Y() {
      return Math.min(this.mH.getValue(), mc.player.blockInteractionRange());
   }

   private BlockPos r() {
      return BlockPos.containing(mc.player.getX(), mc.player.getY() + 0.2, mc.player.getZ());
   }

   private void l(_d.t var1) {
      if (!this.U4.isEmpty()) {
         MutableBlockPos var2 = new MutableBlockPos();
         Iterator var3 = this.U4.entrySet().iterator();

         while (var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            int var5 = (Integer)var4.getKey();
            qf.s var6 = this.C(var1, var5, var2);
            BlockState var7 = this.lO.get(var5);
            int var8 = var6 != qf.s.N || var7 != null && !mc.level.getBlockState(var2).equals(var7) ? 0 : 1;
            if (var6 == qf.s.N && var8 == 0) {
               if (mc.player.tickCount - (Integer)var4.getValue() > 20) {
                  var3.remove();
                  this.lO.remove(var5);
               }
            } else if (var8 != 0) {
               var3.remove();
               this.lO.remove(var5);
               int var9 = this.l3.merge(var5, 1, Integer::sum);
               this.mi.addLast(mc.player.tickCount);
               _q.c(this.getName(), "The server took back {} ({} times)", var2.toShortString(), var9);
               if (var9 >= 3) {
                  this.l3.remove(var5);
                  this.mO.put(var5, mc.player.tickCount + 2400);
                  this.K(var2.toShortString() + ": the server keeps removing it, skipping it for now.");
                  _q.z(this.getName(), "Server removed {} {} times; skipping for {} ticks", var2.toShortString(), 3, 2400);
               }
            } else if (var6 == qf.s.X) {
               var3.remove();
               this.lO.remove(var5);
               if (qf.n(var1.c(var5), mc.level.getBlockState(var2)) < 0) {
                  _q.z(this.getName(), "Placed {} facing the wrong way: {} instead of {}", var2.toShortString(), mc.level.getBlockState(var2), var1.c(var5));
                  if (this.I.merge(var5, 1, Integer::sum) <= 2) {
                     this.lW = var5;
                     this.CU = var2.immutable();
                     this.lZ = mc.player.tickCount;
                  }
               }
            } else if (mc.player.tickCount - (Integer)var4.getValue() > 20) {
               var3.remove();
               this.lO.remove(var5);
            }
         }
      }
   }

   private void W(_d.t var1) {
      BlockPos var2 = this.r();
      if (!this.UZ) {
         this.UZ = (boolean)1;
         if (this.F(var1)) {
            return;
         }
      }

      if (this.CU == null || !this.W(var1)) {
         if (this.ma == null && this.lD == null && this.m4.contains(var2.below())) {
            if (this.Uo == -1000000000) {
               this.Uo = mc.player.tickCount;
            }

            if (mc.player.tickCount - this.Uo <= 200) {
               this.P("Coming off a temporary block");
               if (!this.m(var2.below())) {
                  this.p(var2.below());
               }

               return;
            }

            _q.z(this.getName(), "Could not dig {} from on top of it; going on without it", var2.below().toShortString());
            vs.j(this.getName(), var2.below());
            this.m4.remove(var2.below());
         }

         this.Uo = -1000000000;
         if (this.lD == null || !this.Z(var1)) {
            if (this.ma == _d.y.G && this.Cd) {
               this.O();
            } else if (this.ma == _d.y.c) {
               this.m(var1);
            } else if (this.Ck == _d.j.i) {
               this.J(var1);
            } else if (this.Ck == _d.j.u) {
               this.g();
            } else {
               List var3 = this.v(var1);
               if (!this.A(var3)) {
                  if (this.Ux != null) {
                     this.U(var1);
                  } else if (var3 == null && this.y()) {
                     this.p();
                  } else {
                     if (this.ma == _d.y.G && this.l5 != null && this.md.isEmpty() && this.U9 != null) {
                        this.r();
                     }

                     if (this.mB <= 0 || this.ma != null || this.lD != null || !this.I(var1)) {
                        if (this.mM.isEmpty() || var3 != null && this.lg >= this.A || !this.t.getValue() || this.ma != null || !this.v(var1)) {
                           if (this.mM.isEmpty() || var3 != null && this.lg >= this.A || !this.lL.getValue() || this.ma != null || !this.d(var1)) {
                              long var4 = this.m9 * 1000003L + this.Cg.size() * 1009L + this.mX.size() * 31L + this.mO.size();
                              long var6 = var2.asLong() * 31L + var4;
                              if (this.CL.getValue()
                                 && this.Ur.getValue()
                                 && this.lD == null
                                 && (this.ma == null || this.ma == _d.y.G)
                                 && !this.y()
                                 && var6 != this.Ue) {
                                 for (List var9 : this.Cg) {
                                    if (this.C(var1, var9)) {
                                       return;
                                    }
                                 }

                                 this.Ue = var6;
                              }

                              if (this.CL.getValue() && this.Ur.getValue() && this.ma == null && this.lD == null && !this.y() && var4 != this.W) {
                                 for (List var17 : this.Cg) {
                                    if (this.U(var1, var17)) {
                                       return;
                                    }
                                 }

                                 this.W = var4;
                              }

                              if (var3 == null) {
                                 if (this.ma == _d.y.G) {
                                    this.y("the build is done");
                                 } else {
                                    this.G(var1);
                                 }
                              } else {
                                 d1 var16 = new d1(var0 -> -1, false);
                                 double var18 = this.Y();
                                 int var11 = this.q.X() && mc.player.tickCount - this.mK <= 100 ? 0 : 1;
                                 id.i var12 = id.H(
                                    var16,
                                    var3,
                                    this.UO,
                                    var2.getX(),
                                    var2.getY(),
                                    var2.getZ(),
                                    var18,
                                    var1.l(),
                                    var2x -> this.x(var1, var2x),
                                    (var2x, var3x, var4x, var5) -> this.M(var1, var2x) != null,
                                    (var2x, var3x, var4x, var5) -> this.Q(var1, var2x, var3x, var4x, var5),
                                    this.ma == _d.y.G ? 120 : 24,
                                    (boolean)var11,
                                    this.l7.h(),
                                    this.lm,
                                    this.Ur.getValue() && this.ma != _d.y.G
                                 );
                                 if ("keep walking".equals(var12.reason())) {
                                    this.R();
                                 } else if (var12.place() != null) {
                                    this.q.V();
                                    this.mv = List.of();
                                    this.l7.t();
                                    this.E(var1, var12.place());
                                 } else if (this.ma != _d.y.G || !this.Z(var1, var16, var2, var18)) {
                                    if (this.ma != _d.y.G || !var12.walk() || !this.f(var1, var12)) {
                                       if (this.ma != _d.y.G || !this.lA.isEmpty() || !this.v(var1, var12.walkingFor())) {
                                          if (this.ma != _d.y.G || !this.J(var1, var12.walkingFor())) {
                                             if (this.ma == _d.y.G) {
                                                this.y("nothing more to place from the top");
                                             } else if (var12.walk() && this.m8) {
                                                this.m8 = (boolean)0;
                                                this.Uu = (boolean)1;
                                                this.nx = 12;
                                             } else {
                                                if (var12.walk() && this.l7.I()) {
                                                   _q.c(
                                                      this.getName(),
                                                      "Nothing placeable from {}; leaving that spot out until a block goes down ({} left out)",
                                                      var2.toShortString(),
                                                      this.l7.h().size()
                                                   );
                                                }

                                                if (var12.walk()) {
                                                   if (!this.Ur.getValue()) {
                                                      this.e();
                                                      this.P("Nothing in reach; walk closer (Auto Move is off)");
                                                      this.q("nothing is in reach; walk closer or turn on Auto Move");
                                                   } else {
                                                      this.s(var1, var12);
                                                   }
                                                } else {
                                                   this.q.V();
                                                   if (var12.reason() == null) {
                                                      this.G(var1);
                                                   } else if ("no standing spot within reach".equals(var12.reason()) && this.f()) {
                                                      this.b(this.l5);
                                                   } else if (!"no standing spot within reach".equals(var12.reason())
                                                      || !this.CL.getValue()
                                                      || !this.Y(var1, var12.walkingFor())) {
                                                      for (id.w var14 : var12.walkingFor()) {
                                                         this.w(var14.id(), var12.reason());
                                                      }

                                                      this.P("Setting aside " + var12.walkingFor().size() + " blocks: " + var12.reason());
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

   private List<id.w> v(_d.t var1) {
      MutableBlockPos var2 = new MutableBlockPos();
      int var3 = mc.player.tickCount;
      this.mO.values().removeIf(var1x -> var1x <= var3);
      this.mM.clear();
      this.CJ.clear();
      this.CI = 0;
      this.lg = Integer.MAX_VALUE;
      this.Cg.clear();
      this.M(var1);
      if (this.UT.getValue() && this.Uh != null && this.mn == var1) {
         for (int var7 : this.Uh) {
            if (var1.c(var7) != null
               && var1.g[var1.U[var7]] == null
               && !var1.W(var7)
               && !this.mX.contains(var7)
               && !this.mO.containsKey(var7)
               && !this.lU.contains(var7)
               && this.C(var1, var7, var2) == qf.s.N) {
               BlockState var8 = var1.c(var7);
               int var9 = var2.getY() - var1.F;
               BlockItem var10 = qf.h(var8);
               if (var10 != null && this.B(var10) != 0) {
                  if (qf.n(var8) && mc.level.getBlockState(var2.below()).canBeReplaced()) {
                     this.CI++;
                  } else {
                     id.w var11 = new id.w(var2.getX(), var2.getY(), var2.getZ(), var7);
                     this.CJ.put(var7, _d.w.v);
                     if (this.x(var1, var11)) {
                        this.A = var9;
                        return List.of(var11);
                     }
                  }
               } else {
                  this.mM.merge(var10, 1, Integer::sum);
                  if (var10 != null) {
                     this.lg = Math.min(this.lg, var9);
                  }
               }
            }
         }

         this.CJ.clear();
      }

      HashSet var14 = this.y() && !this.l5.aims().isEmpty() ? new HashSet<>(this.l5.aims().stream().map(id.w::id).toList()) : null;

      for (int var15 = 0; var15 + 1 < var1.z.length; var15++) {
         ArrayList var16 = null;
         byte var17 = 0;

         for (int var18 = var1.z[var15]; var18 < var1.z[var15 + 1]; var18++) {
            int var19 = var1.M[var18];
            if (var1.c(var19) != null
               && var1.g[var1.U[var19]] == null
               && !var1.W(var19)
               && !this.mX.contains(var19)
               && !this.mO.containsKey(var19)
               && !this.me.contains(var19)
               && !this.lU.contains(var19)
               && (var14 == null || var14.contains(var19))) {
               qf.s var20 = this.C(var1, var19, var2);
               BlockState var21 = var1.c(var19);
               _d.w var12;
               if (var20 == qf.s.N) {
                  var12 = _d.w.v;
               } else if (var20 == qf.s.X && qf.n(var21, mc.level.getBlockState(var2)) > 0) {
                  var12 = _d.w.m;
               } else {
                  if (var20 != qf.s.j || !var1.p.containsKey(var19) || this.nV.contains(var19)) {
                     continue;
                  }

                  var12 = _d.w.e;
               }

               if (var12 == _d.w.v) {
                  BlockItem var13 = qf.h(var21);
                  if (var13 == null || this.B(var13) == 0) {
                     this.mM.merge(var13, 1, Integer::sum);
                     if (var13 != null) {
                        this.lg = Math.min(this.lg, var15);
                     }
                     continue;
                  }

                  if (qf.n(var21) && mc.level.getBlockState(var2.below()).canBeReplaced()) {
                     this.CI++;
                     continue;
                  }
               }

               this.CJ.put(var19, var12);
               if (var16 == null) {
                  var16 = new ArrayList();
               }

               id.w var22 = new id.w(var2.getX(), var2.getY(), var2.getZ(), var19);
               var16.add(var22);
               if (var17 == 0 && this.x(var1, var22)) {
                  var17 = 1;
               }
            }
         }

         if (var16 != null && var17 != 0) {
            this.A = var15;
            return this.T(var1, var16);
         }

         if (var16 != null) {
            this.Cg.add(var16);
         }
      }

      return null;
   }

   private void M(_d.t var1) {
      if (this.Cw == null) {
         this.T(var1);
         this.w(var1);
      }

      if (!this.Cw.isEmpty() && this.Ux == null) {
         MutableBlockPos var2 = new MutableBlockPos();

         for (id.p var4 : List.copyOf(this.Cw)) {
            byte var5 = 0;
            if (!var4.aims().isEmpty()) {
               for (id.w var7 : var4.aims()) {
                  if (this.C(var1, var7.id(), var2) == qf.s.N) {
                     var5 = 1;
                     break;
                  }
               }

               if (var5 == 0) {
                  this.b(var4);
               }
            }
         }
      }
   }

   private void T(_d.t var1) {
      this.lU.clear();

      for (int var2 = 0; var2 < var1.P; var2++) {
         if (var1.c(var2) != null && mc.level.isOutsideBuildHeight(BlockPos.getY(var1.d[var2]))) {
            this.lU.add(var2);
         }
      }

      if (!this.lU.isEmpty()) {
         String var3 = "Y " + mc.level.getMinY() + " to " + mc.level.getMaxY();
         this.K(
            this.lU.size() + " blocks of the schematic are outside this world's height limits (" + var3 + ") and are left out; move the origin to build them."
         );
         _q.z(this.getName(), "{} cells are outside the height limits ({}) and are left out", this.lU.size(), var3);
      }
   }

   private void w(_d.t var1) {
      this.Cw = new ArrayList<>();
      this.me.clear();
      if (this.CL.getValue() && this.Ur.getValue() && (!this.UT.getValue() || this.Uh == null)) {
         ArrayList var2 = new ArrayList();
         MutableBlockPos var3 = new MutableBlockPos();

         for (int var4 = 0; var4 < var1.P; var4++) {
            BlockState var5 = var1.c(var4);
            if (var5 != null && var1.g[var1.U[var4]] == null && !var1.W(var4) && qf.Y(var5) && this.C(var1, var4, var3) == qf.s.N) {
               var2.add(new id.w(var3.getX(), var3.getY(), var3.getZ(), var4));
            }
         }

         if (!var2.isEmpty()) {
            HashSet var10 = new HashSet();

            for (id.w var6 : var2) {
               var10.add(var6.id());
            }

            id.o var12 = (var3x, var4x, var5x) -> {
               int var6x = var1.E(var3x, var4x, var5x);
               return var6x >= 0
                  && !var10.contains(var6x)
                  && var1.c(var6x) != null
                  && var1.g[var1.U[var6x]] == null
                  && !var1.W(var6x)
                  && this.C(var1, var6x, new MutableBlockPos()) == qf.s.N;
            };
            this.Cw.addAll(id.H((var2x, var3x, var4x) -> this.x(var1, var2x, var3x, var4x), var12, var2, var1.l(), this.Y() - 0.3));

            for (id.p var7 : this.Cw) {
               for (int[] var9 : var7.cells()) {
                  this.me.add(var1.E(var9[0], var9[1], var9[2]));
               }

               _q.z(
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

   private boolean x(_d.t var1, int var2, int var3, int var4) {
      BlockPos var5 = new BlockPos(var2, var3, var4);
      int var6 = var1.E(var2, var3, var4);
      int var7 = var6 >= 0 && var1.c(var6) != null && var1.g[var1.U[var6]] == null ? 1 : 0;
      return (var7 != 0 ? var1.c(var6) : mc.level.getBlockState(var5)).getCollisionShape(mc.level, var5).isEmpty();
   }

   private boolean W(_d.t var1, id.i var2) {
      if (this.lD == null && this.Cw != null) {
         for (id.p var4 : this.Cw) {
            if (var4 != this.l5 && this.d(var1, var4, var2.walkingFor())) {
               Item var5 = this.g();
               if (var5 == null) {
                  return false;
               }

               id.s var6 = id.B(new d1(var0 -> -1, false), var4, this.mk.getValue().intValue(), var1::c, var2.walkingFor());
               if (var6 == null) {
                  return false;
               }

               if (this.B(var5) < var6.height()) {
                  this.mB = var6.height();
                  return true;
               }

               this.U9 = var6;
               this.ma = _d.y.c;
               this.ly = this.Un = mc.player.tickCount;
               this.q.V();
               this.mv = List.of();
               this.l5 = var4;
               _q.z(
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

   private boolean f(_d.t var1, id.i var2) {
      if (this.l5 == null && this.U9 != null && !var2.stands().isEmpty() && this.lA.isEmpty() && this.m9 != this.nl) {
         BlockPos var3 = this.r();
         HashSet var4 = new HashSet();

         for (long var6 : var2.stands()) {
            if (hn.V(var6) >= var3.getY() - 1) {
               var4.add(var6);
            }
         }

         if (!var4.isEmpty() && this.i(var1, var4, new BlockPos(var2.toward().x(), var2.toward().y(), var2.toward().z()))) {
            this.Um = this.m9;
            this.l5 = new id.p(List.of(), var3.getX(), var3.getY(), var3.getZ(), List.of(), Set.of());
            this.Cw.add(this.l5);
            this.mv = var2.walkingFor();
            this.r();
            this.R();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean d(_d.t var1, id.p var2, List<id.w> var3) {
      for (id.w var5 : var3) {
         if (!var2.aims().stream().noneMatch(var1x -> var1x.id() == var5.id())) {
            for (long var7 : var2.space()) {
               if (this.Q(var1, var5, hn.k(var7), hn.V(var7), hn.m(var7))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean f() {
      return this.y() && this.l5.aims().isEmpty();
   }

   private boolean y() {
      return this.l5 != null && !this.md.isEmpty() && this.Ux == null;
   }

   private void r() {
      this.e = this.U9;
      this.md.addAll(this.l1);
      this.l1.clear();
      this.U9 = null;
      this.ma = null;
      this.l7.w();
      if (this.l5.aims().isEmpty()) {
         _q.z(this.getName(), "Walking off the tower at {} {} {} along the build", this.l5.outX(), this.l5.outY(), this.l5.outZ());
      } else {
         _q.z(this.getName(), "Walking off the tower at {} {} {} for {} blocks", this.l5.outX(), this.l5.outY(), this.l5.outZ(), this.l5.aims().size());
      }
   }

   private void p() {
      byte var1 = 0;

      for (id.w var3 : this.l5.aims()) {
         if (this.mO.remove(var3.id()) != null) {
            var1 = 1;
         }
      }

      if (var1 == 0) {
         this.b(this.l5);
      }
   }

   private void b(id.p var1) {
      if (var1 == this.l5 && !this.md.isEmpty()) {
         this.Ux = var1;
         this.C_ = mc.player.tickCount;
         this.q.V();
         _q.z(this.getName(), "Going back to the tower at {} {} {}", var1.outX(), var1.outY(), var1.outZ());
      } else {
         this.r(var1);
      }
   }

   private void r(id.p var1) {
      this.Cw.remove(var1);
      if (this.mt != null) {
         for (int[] var3 : var1.cells()) {
            int var4 = this.mt.E(var3[0], var3[1], var3[2]);
            this.me.remove(var4);
            this.UO.remove(var4);
         }
      }

      this.l7.w();
   }

   private void U(_d.t var1) {
      id.p var2 = this.Ux;
      BlockPos var3 = this.r();
      BlockPos var4 = new BlockPos(var2.outX(), var2.outY(), var2.outZ());
      if (var3.equals(var4)) {
         this.q.V();
         if (var2.aims().isEmpty() && this.m9 == this.Um) {
            this.nl = this.m9;
         }

         Vec3 var6 = Vec3.atBottomCenterOf(var4).subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
         if (var6.length() >= 0.25 && mc.player.tickCount - this.C_ <= 600) {
            this.B = (boolean)1;
            this.CE = var6;
            this.P("Stepping to the middle of the tower");
         } else {
            this.l1.addAll(this.md);
            this.md.clear();
            this.U9 = this.e;
            this.e = null;
            this.ma = _d.y.G;
            this.i = this.m9;
            this.Ux = null;
            this.l5 = null;
            this.r(var2);
         }
      } else {
         this.P("Going back to the tower");
         int var5 = mc.player.tickCount - this.C_ > 600 ? 1 : 0;
         if (var5 != 0 || !this.q.X() && !this.i(var1, Set.of(hn.f(var4.getX(), var4.getY(), var4.getZ())), var4)) {
            _q.z(
               this.getName(),
               "No way back to the tower from {} ({}); it is swept up at the end",
               var3.toShortString(),
               var5 != 0 ? "took too long" : "no route"
            );
            if (var2.aims().isEmpty() && this.m9 == this.Um) {
               this.nl = this.m9;
            }

            this.q.V();
            this.m4.addAll(this.md);
            this.md.clear();
            this.e = null;
            this.Ux = null;
            this.l5 = null;
            this.r(var2);
         } else {
            this.R();
         }
      }
   }

   private List<id.w> T(_d.t var1, List<id.w> var2) {
      HashSet var3 = new HashSet();

      for (id.w var5 : var2) {
         BlockState var6 = var1.c(var5.id());
         if (qf.q(var6) && this.CJ.getOrDefault(var5.id(), _d.w.v) == _d.w.v) {
            Direction var7 = (Direction)var6.getValue(BlockStateProperties.FACING);
            if (!var7.getAxis().isVertical()) {
               for (int var8 = 1; var8 <= 3; var8++) {
                  var3.add(hn.f(var5.x() + var7.getStepX() * var8, var5.y(), var5.z() + var7.getStepZ() * var8));
               }
            }
         }
      }

      if (var3.isEmpty()) {
         return var2;
      }

      List var9 = var2.stream().filter(var2x -> qf.q(var1.c(var2x.id())) || !var3.contains(hn.f(var2x.x(), var2x.y(), var2x.z()))).toList();
      return var9.stream().anyMatch(var2x -> this.x(var1, var2x)) ? var9 : var2;
   }

   private boolean x(_d.t var1, id.w var2) {
      BlockPos var3 = new BlockPos(var2.x(), var2.y(), var2.z());
      return this.CJ.getOrDefault(var2.id(), _d.w.v) != _d.w.v ? true : qf.i(var3) || mc.level.getBlockState(var3).is(var1.c(var2.id()).getBlock());
   }

   private BlockHitResult M(_d.t var1, id.w var2) {
      BlockState var3 = var1.c(var2.id());
      BlockPos var4 = new BlockPos(var2.x(), var2.y(), var2.z());
      if (this.CJ.getOrDefault(var2.id(), _d.w.v) != _d.w.v) {
         return qf.k(this, var4, mc.player.getEyePosition(), this.Y(), var3);
      }

      ItemStack var5 = this.G(qf.h(var3));
      if (var5.isEmpty()) {
         return null;
      }

      if (mc.player.getBoundingBox().intersects(new AABB(var4))) {
         return null;
      }

      BlockPos var6 = qf.p(var3, var4);
      return var6 != null && mc.player.getBoundingBox().intersects(new AABB(var6))
         ? null
         : qf.E(this, mc.player, var5, var4, var3, mc.player.getEyePosition(), this.Y(), true);
   }

   private boolean Q(_d.t var1, id.w var2, int var3, int var4, int var5) {
      BlockState var6 = var1.c(var2.id());
      BlockPos var7 = new BlockPos(var2.x(), var2.y(), var2.z());
      double var8 = this.Y() - 0.3;
      if (this.CJ.getOrDefault(var2.id(), _d.w.v) != _d.w.v) {
         return qf.k(this, var7, new Vec3(var3 + 0.5, var4 + 1.62, var5 + 0.5), var8, var6) != null;
      }

      ItemStack var10 = this.G(qf.h(var6));
      if (var10.isEmpty()) {
         return false;
      }

      BlockPos var11 = qf.p(var6, var7);
      if (var11 == null || var11.getX() != var3 || var11.getZ() != var5 || var11.getY() != var4 && var11.getY() != var4 + 1) {
         for (double var15 : new double[]{1.62, mc.player.getEyeHeight(Pose.CROUCHING)}) {
            if (qf.L(this, mc.player, var10, var7, var6, new Vec3(var3 + 0.5, var4 + var15, var5 + 0.5), var8, true, true) == null) {
               return false;
            }

            if (!this.F.getValue()) {
               break;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private ItemStack G(Item var1) {
      if (var1 == null) {
         return ItemStack.EMPTY;
      }

      Inventory var2 = mc.player.getInventory();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2.getItem(var3).is(var1)) {
            return var2.getItem(var3);
         }
      }

      return ItemStack.EMPTY;
   }

   private void w(int var1, String var2) {
      int var3 = this.nY.merge(var1, 1, Integer::sum);
      String var4 = this.mt == null ? "?" : this.mt.s(var1).toShortString();
      if (var3 >= 3) {
         this.mX.add(var1);
         this.C0.put(var1, var2);
         _q.z(this.getName(), "Giving up on {}: {}", var4, var2);
      } else {
         this.mO.put(var1, mc.player.tickCount + 400);
         _q.c(this.getName(), "Setting {} aside for now: {}", var4, var2);
      }
   }

   private void G(_d.t var1) {
      this.q.V();
      this.l7.w();
      if (!this.mO.isEmpty()) {
         byte var2 = 0;

         for (Integer var4 : new ArrayList<>(this.mO.keySet())) {
            if (!this.mX.contains(var4)) {
               this.mO.remove(var4);
               var2 = 1;
            }
         }

         if (var2 != 0) {
            _q.c(this.getName(), "Retrying blocks set aside");
            this.P("Trying the blocks set aside again");
            return;
         }
      }

      if (!this.m4.isEmpty()) {
         this.P(var1);
      } else if (!this.mX.isEmpty() && this.ma == null && this.md.isEmpty() && this.L()) {
         BlockPos var6 = this.r();
         this.h("no way to walk from where you stand at " + var6.toShortString() + "; stand on the floor beside the build and start again");
      } else if (!this.mX.isEmpty() && this.m9 > this.j) {
         this.j = this.m9;
         _q.z(this.getName(), "Trying the {} blocks given up on again, now that more of the build stands", this.mX.size());
         this.U8.clear();
         this.mX.clear();
         this.C0.clear();
         this.nY.clear();
         this.P("Trying the blocks given up on again");
      } else if (this.np.values().stream().anyMatch(var0 -> var0 >= 2) && this.m9 > this.m0) {
         this.m0 = this.m9;
         this.np.clear();
         this.lQ = this.m9;
         this.W = Long.MIN_VALUE;
         this.Ue = Long.MIN_VALUE;
         _q.z(this.getName(), "Trying temporary supports and towers once more, {} blocks placed so far", this.m9);
         this.P("Trying towers again");
      } else if (this.Cw != null && !this.Cw.isEmpty()) {
         this.b(this.Cw.getFirst());
      } else if (!this.mM.isEmpty()) {
         ArrayList var5 = new ArrayList();

         for (Entry var8 : this.mM.entrySet()) {
            var5.add(var8.getValue() + " " + (var8.getKey() == null ? "unplaceable" : F((Item)var8.getKey())));
         }

         this.h(
            "out of blocks: "
               + String.join(", ", var5.subList(0, Math.min(5, var5.size())))
               + (var5.size() > 5 ? " and more" : "")
               + (this.V.isEmpty() ? "" : " (the marked containers have none)")
         );
      } else {
         this.y();
      }
   }

   private void E(_d.t var1, id.w var2) {
      this.UM = var2;
      this.C2 = this.CJ.getOrDefault(var2.id(), _d.w.v);
      this.mQ = mc.player.tickCount;
      this.CH = null;
      if (this.m()) {
         ij.V(ij.r.z);
      }

      if (this.C2 == _d.w.v) {
         this.i(var1);
      } else {
         this.S(var1);
      }
   }

   private void i(_d.t var1) {
      id.w var2 = this.UM;
      BlockPos var3 = new BlockPos(var2.x(), var2.y(), var2.z());
      MutableBlockPos var4 = new MutableBlockPos();
      if (this.C(var1, var2.id(), var4) != qf.s.N) {
         this.E();
      } else if (mc.player.tickCount - this.mQ > 40) {
         _q.c(this.getName(), "Placing {} took too long: {}", var3.toShortString(), vi.k(this));
         this.E();
         this.w(var2.id(), "could not place it");
      } else {
         BlockState var5 = var1.c(var2.id());
         BlockItem var6 = qf.h(var5);
         int var7 = this.P(var6);
         if (var7 < 0) {
            this.P(var6);
         } else {
            this.e();
            if (!this.f(var7)) {
               this.P("Taking out " + mc.player.getInventory().getItem(var7).getHoverName().getString());
            } else {
               BlockHitResult var8 = this.M(var1, var2);
               if (var8 == null) {
                  this.E();
               } else {
                  this.CH = var8;
                  this.lj = mc.level.getBlockState(var3);
                  BlockPos var9 = var8.getBlockPos();
                  this.M(var9);
                  double var10 = this.Y();
                  boolean var12 = vi.V(
                     this,
                     var3,
                     var8,
                     var6,
                     var10,
                     true,
                     () -> vi.O(var3, var6) && !this.W(),
                     () -> vi.V(this, var9, var10) && qf.F(mc.player, var8) && qf.x(mc.player, var9) && this.G(var3, var9, var8.getDirection(), var5),
                     var3x -> this.t(var1, var2, var3x)
                  );
                  this.P(
                     var12
                        ? "Placing " + var5.getBlock().getName().getString() + " at " + var3.toShortString()
                        : "Cannot place at " + var3.toShortString() + ": " + vi.k(this)
                  );
               }
            }
         }
      }
   }

   private void M(BlockPos var1) {
      if (qf.g(mc.level.getBlockState(var1))) {
         this.B = (boolean)1;
      }
   }

   private boolean y(BlockPos var1) {
      return !qf.g(mc.level.getBlockState(var1)) || this.ms != -1000000000 && mc.player.tickCount - this.ms >= 3;
   }

   private boolean G(BlockPos var1, BlockPos var2, Direction var3, BlockState var4) {
      if (!this.y(var2)) {
         return false;
      }

      if (!qf.v(var4)) {
         return true;
      }

      Vec3 var5 = mc.player.getLookAngle();
      Vec3 var6 = mc.player.getEyePosition();
      BlockHitResult var7 = vi.y(this, var6, var2, var5, this.Y());
      if (var7 != null && var7.getDirection() == var3) {
         ItemStack var8 = this.G(qf.h(var4));
         BlockState var9 = mc.level.getBlockState(var1);
         q71cffb54eb30bf94 var10 = (q71cffb54eb30bf94)mc.player;
         if (!(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - var10.larpYRotLast())) > 2.0F) && !(Math.abs(mc.player.getXRot() - var10.larpXRotLast()) > 2.0F)) {
            for (float[] var14 : new float[][]{{mc.player.getYRot(), mc.player.getXRot()}, {var10.larpYRotLast(), var10.larpXRotLast()}}) {
               BlockState var15 = qf.Q(mc.player, var8, var7, var14[0], var14[1]);
               if (!qf.D(var4, var9, var15)) {
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

   private void t(_d.t var1, id.w var2, boolean var3) {
      if (var3 && this.CH != null) {
         _q.c(
            this.getName(),
            "Click for {} {} {}: {} face of {}, held {}, player at {}",
            var2.x(),
            var2.y(),
            var2.z(),
            this.CH.getDirection().getSerializedName(),
            this.CH.getBlockPos().toShortString(),
            mc.player.getMainHandItem().getHoverName().getString(),
            mc.player.blockPosition().toShortString()
         );
      }

      this.Cs = mc.player.tickCount;
      this.UM = null;
      this.CH = null;
      if (!var3) {
         _q.c(this.getName(), "Placement at {} {} {} was not accepted", var2.x(), var2.y(), var2.z());
      } else {
         this.lm = var2;
         this.m9++;
         this.m8 = (boolean)0;
         this.CC = this.UN.nextInt(5) + (this.UN.nextInt(100) < 6 ? 8 + this.UN.nextInt(16) : 0);
         this.U4.put(var2.id(), mc.player.tickCount);
         if (this.lj != null) {
            this.lO.put(var2.id(), this.lj);
         }
      }
   }

   private void S(_d.t var1) {
      id.w var2 = this.UM;
      BlockPos var3 = new BlockPos(var2.x(), var2.y(), var2.z());
      MutableBlockPos var4 = new MutableBlockPos();
      qf.s var5 = this.C(var1, var2.id(), var4);
      BlockState var6 = var1.c(var2.id());
      int var7 = this.C2 == _d.w.m
         ? (var5 == qf.s.X && qf.n(var6, mc.level.getBlockState(var3)) > 0 ? 1 : 0)
         : (var5 == qf.s.j && !this.nV.contains(var2.id()) && this.m_ == null ? 1 : 0);
      if (var7 == 0) {
         this.E();
      } else if (mc.player.tickCount - this.mQ > 40) {
         _q.c(this.getName(), "Clicking {} took too long: {}", var3.toShortString(), vi.k(this));
         this.E();
         this.w(var2.id(), this.C2 == _d.w.m ? "could not adjust it" : "could not open the crafter");
      } else {
         this.CE = Vec3.ZERO;
         this.B = (boolean)0;
         int var8 = this.g();
         if (var8 >= 0 && !this.f(var8)) {
            this.P("Putting the block away");
         } else {
            BlockHitResult var9 = this.M(var1, var2);
            if (var9 == null) {
               this.E();
            } else {
               this.CH = var9;
               ItemStack var10 = mc.player.getMainHandItem();
               Item var11 = var10.isEmpty() ? Items.AIR : var10.getItem();
               double var12 = this.Y();
               boolean var14 = vi.i(
                  this,
                  var3,
                  var11,
                  var12,
                  true,
                  false,
                  () -> !this.W(),
                  () -> !mc.player.isShiftKeyDown()
                     && mc.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4
                     && vi.V(this, var3, var12)
                     && qf.R(var6, mc.player.getYRot())
                     && qf.R(var6, ((q71cffb54eb30bf94)mc.player).larpYRotLast()),
                  var2x -> this.c(var2, var2x)
               );
               String var15 = this.C2 == _d.w.e ? "Opening the crafter at " : "Adjusting " + var6.getBlock().getName().getString() + " at ";
               this.P(var14 ? var15 + var3.toShortString() : "Cannot click " + var3.toShortString() + ": " + vi.k(this));
            }
         }
      }
   }

   private int g() {
      Inventory var1 = mc.player.getInventory();
      if (!(var1.getSelectedItem().getItem() instanceof BlockItem)) {
         return var1.getSelectedSlot();
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.getItem(var2).isEmpty()) {
            return var2;
         }
      }

      for (int var3 = 0; var3 < 9; var3++) {
         if (!(var1.getItem(var3).getItem() instanceof BlockItem)) {
            return var3;
         }
      }

      return -1;
   }

   private void c(id.w var1, boolean var2) {
      this.Cs = mc.player.tickCount;
      _d.w var3 = this.C2;
      this.UM = null;
      this.CH = null;
      this.C2 = _d.w.v;
      if (!var2) {
         _q.c(this.getName(), "Click on {} {} {} was not accepted", var1.x(), var1.y(), var1.z());
      } else {
         this.lm = var1;
         if (var3 == _d.w.e) {
            this.m_ = var1.id();
            this.T = mc.player.tickCount;
            this.lT = -1000000000;
         } else {
            this.mU++;
         }
      }
   }

   private boolean a(_d.t var1) {
      int var2 = mc.player.tickCount;
      int var3 = this.m_;
      BlockPos var4 = var1.s(var3);
      this.z();
      if (mc.gui.screen() instanceof CrafterScreen var5) {
         if (this.lT == -1000000000) {
            this.lT = var2;
         }

         if (var2 - this.T > 100) {
            this.m_ = null;
            mc.player.closeContainer();
            this.w(var3, "could not set the crafter's slots");
            return true;
         }

         this.P("Setting the crafter's slots at " + var4.toShortString());
         if (var2 - this.lT >= 6 && var2 - this.mD >= 4) {
            CrafterMenu var10 = (CrafterMenu)var5.getMenu();
            int[] var7 = var1.p.get(var3);

            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = Arrays.binarySearch(var7, var8) >= 0 ? 1 : 0;
               if (var10.isSlotDisabled(var8) != var9) {
                  if (var9 != 0 && !var10.getSlot(var8).getItem().isEmpty()) {
                     this.m_ = null;
                     mc.player.closeContainer();
                     this.w(var3, "a crafter slot to disable holds an item");
                     return true;
                  }

                  var10.setSlotState(var8, var9 == 0);
                  mc.gameMode.handleSlotStateChanged(var8, var10.containerId, var9 == 0);
                  this.mD = var2;
                  return true;
               }
            }

            this.nV.add(var3);
            this.m_ = null;
            mc.player.closeContainer();
            this.Cs = var2;
            _q.z(this.getName(), "Crafter at {}: disabled slots {}", var4.toShortString(), Arrays.toString(var7));
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.T <= 50) {
         this.P("Waiting for the crafter at " + var4.toShortString() + " to open");
         return true;
      } else {
         this.m_ = null;
         this.w(var3, "the crafter did not open");
         return false;
      }
   }

   private int P(Item var1) {
      Inventory var2 = mc.player.getInventory();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var2.getItem(var3).is(var1)) {
            return var3;
         }
      }

      return -1;
   }

   private boolean f(int var1) {
      Inventory var2 = mc.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         var2.setSelectedSlot(var1);
         this.u = var1;
         this.mq = mc.player.tickCount;
         return false;
      }

      if (this.u != var1) {
         this.u = var1;
         this.mq = mc.player.tickCount;
      }

      return mc.player.tickCount - this.mq >= 3;
   }

   private void P(Item var1) {
      id.w var2 = this.UM;
      if (!this.lu.getValue()) {
         this.E();
         this.q("put " + F(var1) + " in your hotbar (Refill Hotbar is off)");
         this.w(var2.id(), "not in the hotbar");
      } else if (this.D(var1) == -2) {
         this.E();
         if (this.g.startsWith("make room")) {
            this.q(this.g);
         }

         this.w(var2.id(), this.g.startsWith("make room") ? "no hotbar room" : this.g);
      }
   }

   private int D(Item var1) {
      int var2 = this.P(var1);
      if (var2 >= 0) {
         return var2;
      }

      Inventory var3 = mc.player.getInventory();
      int var4 = -1;

      for (int var5 = 9; var5 < 36; var5++) {
         if (var3.getItem(var5).is(var1)) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         this.g = "no " + F(var1) + " left";
         return -2;
      }

      int var11 = -1;

      for (int var6 = 0; var6 < 9 && var11 < 0; var6++) {
         if (var3.getItem(var6).isEmpty()) {
            var11 = var6;
         }
      }

      Map var12 = this.J();

      for (int var7 = 0; var7 < 9 && var11 < 0; var7++) {
         ItemStack var8 = var3.getItem(var7);
         if (var8.getItem() instanceof BlockItem && !var12.containsKey(var8.getItem()) && var7 != var3.getSelectedSlot()) {
            var11 = var7;
         }
      }

      int var13 = Integer.MAX_VALUE;
      if (var11 < 0) {
         for (int var14 = 0; var14 < 9; var14++) {
            ItemStack var9 = var3.getItem(var14);
            if (var9.getItem() instanceof BlockItem && !var9.is(var1) && var14 != var3.getSelectedSlot()) {
               int var10 = var12.getOrDefault(var9.getItem(), 0);
               if (var10 < var13) {
                  var13 = var10;
                  var11 = var14;
               }
            }
         }
      }

      if (var11 < 0) {
         this.g = "make room in your hotbar for " + F(var1) + " (an empty slot, or a block)";
         return -2;
      }

      this.CE = Vec3.ZERO;
      this.B = (boolean)0;
      if (mc.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4) {
         this.Cp = -1000000000;
         this.P("Stopping to move a block into the hotbar");
         return -1;
      }

      if (this.Cp == -1000000000) {
         this.Cp = mc.player.tickCount;
      }

      if (mc.player.tickCount - this.Cp >= 4 && mc.player.tickCount - this.UG >= 4) {
         InventoryMenu var15 = mc.player.inventoryMenu;
         mc.gameMode.handleContainerInput(var15.containerId, var4, var11, ContainerInput.SWAP, mc.player);
         this.UG = mc.player.tickCount;
         this.Cp = -1000000000;
         _q.c(this.getName(), "Moved {} from inventory slot {} to hotbar slot {}", F(var1), var4, var11 + 1);
         this.P("Moved " + F(var1) + " into hotbar slot " + (var11 + 1));
         return -1;
      } else {
         this.P("Moving " + F(var1) + " into the hotbar");
         return -1;
      }
   }

   private Map<Item, Integer> J() {
      _d.t var1 = this.mt;
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      }

      for (int var3 = 0; var3 < var1.P; var3++) {
         if (var1.C(var3) == qf.s.N && var1.c(var3) != null && !var1.W(var3)) {
            BlockItem var4 = qf.h(var1.c(var3));
            if (var4 != null) {
               var2.merge(var4, 1, Integer::sum);
            }
         }
      }

      return var2;
   }

   private void s(_d.t var1, id.i var2) {
      BlockPos var3 = this.r();
      id.w var4 = var2.toward();
      BlockPos var5 = new BlockPos(var4.x(), var4.y(), var4.z());
      if (!this.q.X() || !var5.equals(this.Cr) && mc.player.tickCount - this.mK > 20 || mc.player.tickCount - this.mK > 100) {
         this.mK = mc.player.tickCount;
         this.Cr = var5;
         this.mv = var2.walkingFor();
         hn.j var6 = new hn.j(
            Math.min(var1.u, var3.getX()) - 10,
            Math.min(var1.F, var3.getY()) - 6,
            Math.min(var1.B, var3.getZ()) - 10,
            Math.max(var1.k, var3.getX()) + 10,
            Math.max(var1.m, var3.getY()) + 6,
            Math.max(var1.V, var3.getZ()) + 10
         );
         hn.l var7 = hn.a(
            new d1(var0 -> -1, false),
            var3.getX(),
            var3.getY(),
            var3.getZ(),
            hn.c.Q(var2.stands(), var4.x(), var4.y() - 1, var4.z(), this.Y()),
            var6,
            hn.m.walking(16000)
         );
         if (!var7.found() || var7.steps().isEmpty()) {
            this.q.V();
            if (this.f()) {
               this.b(this.l5);
               return;
            }

            _q.c(this.getName(), "No route to stand within reach of {} ({} cells searched)", var5.toShortString(), var7.explored());
            if (this.CL.getValue() && this.ma == null && this.W(var1, var2)) {
               return;
            }

            if (this.CL.getValue() && this.ma == null && this.Y(var1, var2.walkingFor())) {
               return;
            }

            if (this.CL.getValue() && this.ma == null && this.X(var1, var2)) {
               return;
            }

            for (id.w var13 : var2.walkingFor()) {
               this.w(var13.id(), "no way to walk within reach");
            }

            return;
         }

         if (this.y() && this.l5.cells().isEmpty() && this.e != null && var7.steps().stream().anyMatch(var1x -> var1x.y() < this.l5.outY() - 1)) {
            hn.l var8 = hn.a(
               new d1(var0 -> -1, false),
               this.e.x(),
               this.e.ground(),
               this.e.z(),
               hn.c.Q(var2.stands(), var4.x(), var4.y() - 1, var4.z(), this.Y()),
               var6,
               hn.m.walking(16000)
            );
            if (var8.found()) {
               this.q.V();
               _q.c(this.getName(), "The spot for {} needs a drop off the build; the tower comes down first", var5.toShortString());
               this.b(this.l5);
               return;
            }
         }

         this.q.s(var7.steps());
         this.q.B();
         this.k = 0.3 + this.UN.nextDouble() * 0.7;
         this.mg = this.UN.nextDouble() * Math.PI * 2.0;
         BlockItem var11 = this.CJ.getOrDefault(var4.id(), _d.w.v) == _d.w.v ? qf.h(var1.c(var4.id())) : null;
         int var9 = var11 == null ? -1 : this.P(var11);
         if (var9 >= 0) {
            mc.player.getInventory().setSelectedSlot(var9);
         }

         hn.g var10 = var7.steps().getLast();
         this.Uz = new BlockPos(var10.x(), var10.y(), var10.z());
         _q.c(this.getName(), "Route of {} steps to {} for {}", var7.steps().size(), this.Uz.toShortString(), var5.toShortString());
      }

      this.R();
   }

   private void R() {
      ad.s var1 = this.q.f(this.Y(), var0 -> -1, this.nN.getValue());
      this.CE = this.q.L();
      this.o = (boolean)(!this.q.p() && !this.u() ? 0 : 1);
      this.B = (boolean)(!this.q.v() && (!this.F.getValue() || !this.A() || this.q.I().isEmpty() || this.q.I().getFirst().move() != hn.y.R) ? 0 : 1);
      this.lF = (boolean)(this.q.d() && !this.B ? 1 : 0);
      this.P("Walking to " + (this.Cr == null ? "the next block" : this.Cr.toShortString()));
      if (var1 == ad.s.Z) {
         this.q.V();
         this.mF = 0;
         this.nx = 12;
         this.m8 = (boolean)1;
         BlockPos var2 = this.r();
         this.l7.H(var2.getX(), var2.getY(), var2.getZ());
      } else if (var1 == ad.s.d) {
         _q.c(this.getName(), "Route stuck: {}", this.q.I());
         this.q.V();
         if (this.ma == _d.y.c) {
            this.g("stuck on the way to it");
            return;
         }

         if (this.lD == _d.f.J) {
            this.Z("stuck on the way to it");
            return;
         }

         if (this.lD == _d.f.r) {
            return;
         }

         if (this.Uz != null) {
            this.l7.r(this.Uz.getX(), this.Uz.getY(), this.Uz.getZ());
         }

         if (++this.mF >= 3) {
            this.mF = 0;

            for (id.w var3 : this.mv) {
               this.w(var3.id(), "stuck on the way");
            }
         }
      }
   }

   private void Y() {
      this.nx--;
      if (!this.Uu) {
         if (mc.player.getDeltaMovement().horizontalDistance() < 0.03) {
            this.nx = 0;
         }

         this.P("Stopping");
      } else {
         if (this.nx <= 0) {
            this.Uu = (boolean)0;
         }

         BlockPos var1 = this.r();
         Vec3 var2 = Vec3.atBottomCenterOf(var1);
         Vec3 var3 = var2.subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
         double var4 = mc.player.getDeltaMovement().horizontalDistance();
         if (var3.length() < 0.12 && var4 < 0.01) {
            this.nx = 0;
            this.Uu = (boolean)0;
         } else {
            double var6 = var3.lengthSqr() < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var3.normalize());
            this.B = (boolean)1;
            this.CE = this.UX.g(var3.length(), var6, _p.Q(this)) ? var3 : Vec3.ZERO;
            this.P("Stopping on the spot");
         }
      }
   }

   private boolean u() {
      int var1 = mc.player.tickCount;
      if (this.q.e() && this.q.d() && mc.player.isSprinting() && mc.player.onGround()) {
         if (!mc.level.noCollision(mc.player, mc.player.getBoundingBox().move(0.0, 1.3, 0.0))) {
            return false;
         }

         if (this.H != -1000000000 && var1 < this.H) {
            this.lY++;
            return true;
         }

         if (this.ln != -1000000000 && var1 < this.ln) {
            return false;
         }

         this.ln = var1 + 30 + this.UN.nextInt(110);
         if (this.UN.nextInt(100) >= 60) {
            return false;
         }

         this.H = var1 + 12 + this.UN.nextInt(45);
         this.lY++;
         return true;
      } else {
         return false;
      }
   }

   private void Q() {
      float var1 = mc.player.getYRot();
      float var2 = Float.isNaN(this.ns) ? 0.0F : Mth.wrapDegrees(var1 - this.ns);
      this.ns = var1;
      this.ls = Math.max(this.ls, Math.abs(var2));
      if (Math.abs(var2) > 26.0F && this.mz < 12) {
         this.mz++;
         _q.z(
            this.getName(),
            "Sharp turn of {} degrees in one tick while: {} (rotation owner {})",
            String.format(Locale.ROOT, "%.1f", var2),
            this.getStatus(),
            b6.x() && b6.c(b6.w.x) ? "bot" : "other"
         );
      }

      if (Math.abs(var2) > 6.0F && Math.abs(this.P) > 6.0F && Math.signum(var2) != Math.signum(this.P)) {
         this.C7++;
      }

      this.P = var2;
   }

   private void e() {
      if (this.F.getValue() && mc.player.onGround() && this.A()) {
         this.B = (boolean)1;
      }
   }

   private boolean A() {
      BlockPos var1 = this.r();

      for (Direction var3 : Plane.HORIZONTAL) {
         BlockPos var4 = var1.relative(var3);
         if (this.t(var4) && this.t(var4.below()) && this.t(var4.below(2))) {
            return true;
         }
      }

      for (int[] var5 : new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}) {
         BlockPos var6 = var1.offset(var5[0], 0, var5[1]);
         if (this.t(var6) && this.t(var6.below()) && this.t(var6.below(2))) {
            return true;
         }
      }

      return false;
   }

   private boolean t(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getCollisionShape(mc.level, var1).isEmpty();
   }

   private Item g() {
      Item var1 = switch ((String)this.Uk.getValue()) {
         case "Cobblestone" -> Items.COBBLESTONE;
         case "Netherrack" -> Items.NETHERRACK;
         case "Cobbled Deepslate" -> Items.COBBLED_DEEPSLATE;
         default -> Items.DIRT;
      };
      return this.mt != null && H(this.mt, var1) ? null : var1;
   }

   private static boolean H(_d.t var0, Item var1) {
      for (BlockState var5 : var0.X) {
         if (var5 != null && qf.h(var5) == var1) {
            return true;
         }
      }

      return false;
   }

   private boolean Y(_d.t var1, List<id.w> var2) {
      if (this.lD != null) {
         return false;
      }

      Item var3 = this.g();
      if (var3 == null) {
         this.q("the build uses " + this.Uk.getValue().toLowerCase(Locale.ROOT) + "; pick another Pillar Block for temporary pillars");
         return false;
      }

      BlockPos var4 = this.r();
      if (this.m9 > this.mb) {
         this.Y.clear();
      }

      HashSet var5 = new HashSet<>(this.CZ);
      var5.addAll(this.Y);
      var2 = var2.stream().filter(var1x -> !this.U8.contains(var1x.id())).toList();
      if (var2.isEmpty()) {
         return false;
      } else {
         id.s var6 = id.i(
            new d1(var0 -> -1, false),
            var2,
            this.E(var1, 2),
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.Y(),
            this.mk.getValue().intValue(),
            var1::c,
            (var2x, var3x, var4x, var5x) -> this.T(var1, var2x, var3x, var4x, var5x),
            var5,
            600
         );
         if (var6 == null) {
            _q.c(this.getName(), "No pillar reaches {} blocks", var2.size());
            this.e(var1, (id.w)var2.getFirst());
            return false;
         } else if (this.B(var3) < var6.height()) {
            this.mB = var6.height();
            return true;
         } else {
            this.U9 = var6;
            this.ma = _d.y.c;
            this.ly = this.Un = mc.player.tickCount;
            this.q.V();
            this.mv = List.of();
            _q.c(this.getName(), "Planned a pillar of {} at {} {} {}", var6.height(), var6.x(), var6.ground(), var6.z());
            return true;
         }
      }
   }

   private void e(_d.t var1, id.w var2) {
      if (this.mA.add(var2.id())) {
         BlockState var3 = var1.c(var2.id());
         ItemStack var4 = this.G(qf.h(var3));
         if (!var4.isEmpty()) {
            d1 var5 = new d1(var0 -> -1, false);
            BlockPos var6 = new BlockPos(var2.x(), var2.y(), var2.z());
            int var7 = (int)Math.floor(this.Y());
            int var8 = this.mk.getValue().intValue();
            TreeMap var9 = new TreeMap();
            String var10 = null;

            for (int var11 = -var7; var11 <= var7; var11++) {
               for (int var12 = -var7; var12 <= var7; var12++) {
                  if ((var11 != 0 || var12 != 0) && !(var11 * var11 + var12 * var12 > this.Y() * this.Y())) {
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
                           if (!id.N(var5, var13, var15, var14, var24, var1::c, var2.y())) {
                              var9.merge("column taken", 1, Integer::sum);
                              if (var10 == null) {
                                 for (int var26 = var15; var26 <= var15 + var24 + 1 && var10 == null; var26++) {
                                    if (!var5.open(var13, var26, var14) || var1.c(var13, var26, var14)) {
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

                           String var17 = qf.j(this, mc.player, var4, var6, var3, new Vec3(var13 + 0.5, var15 + var24 + 1.62, var14 + 0.5), this.Y() - 0.3);
                           var9.merge(var17 == null ? "would work" : var17, 1, Integer::sum);
                        }
                     }
                  }
               }
            }

            StringBuilder var19 = new StringBuilder();
            if (qf.q(var3)) {
               Direction var20 = (Direction)var3.getValue(BlockStateProperties.FACING);
               var19.append("; below ").append(mc.level.getBlockState(var6.below()).getBlock().getName().getString());

               for (int var21 = 1; var21 <= 4; var21++) {
                  int var22 = var2.x() + var20.getStepX() * var21;
                  int var23 = var2.z() + var20.getStepZ() * var21;
                  var19.append("; ")
                     .append(var21)
                     .append(" out: ")
                     .append(mc.level.getBlockState(new BlockPos(var22, var2.y(), var23)).getBlock().getName().getString());

                  for (int var25 = var2.y(); var25 >= var2.y() - var8 - 3; var25--) {
                     if (var5.loaded(var22, var25, var23)
                        && var5.floor(var22, var25 - 1, var23)
                        && var5.open(var22, var25, var23)
                        && var5.open(var22, var25 + 1, var23)) {
                        for (int var27 = 0; var27 <= Math.min(var8, var2.y() - var25 + 1); var27++) {
                           String var18 = qf.j(this, mc.player, var4, var6, var3, new Vec3(var22 + 0.5, var25 + var27 + 1.62, var23 + 0.5), this.Y() - 0.3);
                           var19.append(" h")
                              .append(var27)
                              .append('=')
                              .append(var18 == null ? "ok" : var18)
                              .append(id.N(var5, var22, var25, var23, Math.max(1, var27), var1::c, var2.y()) ? "" : "(taken)");
                        }
                        break;
                     }
                  }
               }
            }

            if (var10 != null) {
               var19.append("; first column taken at ").append(var10);
            }

            _q.c(this.getName(), "Why no tower for {} at {}: {}{}", var3.getBlock().getName().getString(), var6.toShortString(), var9, var19);
         }
      }
   }

   private boolean T(_d.t var1, id.w var2, int var3, int var4, int var5) {
      return (!this.UO.contains(var2.id()) || !var1.l().containsColumn(var3, var5)) && this.Q(var1, var2, var3, var4, var5);
   }

   private boolean X(_d.t var1, id.i var2) {
      if (this.lD == null && this.l5 == null) {
         Item var3 = this.g();
         if (var3 == null) {
            return false;
         }

         if (this.m9 > this.mb) {
            this.Y.clear();
         }

         HashSet var4 = new HashSet<>(this.CZ);
         var4.addAll(this.Y);
         id.s var5 = id.E(new d1(var0 -> -1, false), var2.stands(), var2.walkingFor(), this.mk.getValue().intValue(), var1::c, var4);
         if (var5 == null) {
            return false;
         } else if (this.B(var3) < var5.height()) {
            this.mB = var5.height();
            return true;
         } else {
            this.U9 = var5;
            this.ma = _d.y.c;
            this.ly = this.Un = mc.player.tickCount;
            this.q.V();
            this.mv = List.of();
            this.l5 = new id.p(List.of(), var5.x(), var5.topY(), var5.z(), List.copyOf(var2.walkingFor()), Set.of());
            this.Cw.add(this.l5);
            _q.z(
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

   private List<id.w> E(_d.t var1, int var2) {
      ArrayList var3 = new ArrayList();
      MutableBlockPos var4 = new MutableBlockPos();
      int var5 = Math.min(this.A + var2, var1.z.length - 1);

      for (int var6 = var1.z[this.A]; var6 < var1.z[var5]; var6++) {
         int var7 = var1.M[var6];
         if (var1.c(var7) != null
            && var1.g[var1.U[var7]] == null
            && !var1.W(var7)
            && !this.mX.contains(var7)
            && !this.me.contains(var7)
            && this.C(var1, var7, var4) == qf.s.N) {
            var3.add(new id.w(var4.getX(), var4.getY(), var4.getZ(), var7));
         }
      }

      return var3;
   }

   private boolean Z(_d.t var1, d1 var2, BlockPos var3, double var4) {
      ArrayList var6 = new ArrayList();
      int var7 = var1.F + this.A;

      for (id.w var9 : this.E(var1, 65)) {
         if (var9.y() > var7) {
            var6.add(var9);
         }
      }

      for (int var12 = var7 + 1; var12 <= var7 + 64; var12++) {
         int var13 = var12;
         List var10 = this.T(var1, var6.stream().filter(var1x -> var1x.y() == var13).toList());
         if (!var10.isEmpty()) {
            id.i var11 = id.H(
               var2,
               var10,
               this.UO,
               var3.getX(),
               var3.getY(),
               var3.getZ(),
               var4,
               var1.l(),
               var2x -> this.x(var1, var2x),
               (var2x, var3x, var4x, var5) -> this.M(var1, var2x) != null,
               (var2x, var3x, var4x, var5) -> this.Q(var1, var2x, var3x, var4x, var5),
               120,
               false,
               this.l7.h(),
               this.lm,
               false
            );
            if (var11.place() != null) {
               this.CJ.put(var11.place().id(), _d.w.v);
               this.E(var1, var11.place());
               return true;
            }
         }
      }

      return false;
   }

   private boolean F(_d.t var1) {
      Item var2 = this.g();
      if (var2 != null && this.CL.getValue()) {
         Block var3 = O(var2);
         BlockPos var4 = this.r();
         ArrayList var5 = new ArrayList();

         BlockPos var6;
         for (var6 = var4.below();
            var5.size() < 16 && mc.level.getBlockState(var6).is(var3) && !var1.c(var6.getX(), var6.getY(), var6.getZ()) && this.Y(var6) >= 2;
            var6 = var6.below()
         ) {
            var5.add(var6);
         }

         if (var5.size() >= 3 && !this.t(var6)) {
            Collections.reverse(var5);
            this.l1.clear();
            this.l1.addAll(var5);
            this.U9 = new id.s(var4.getX(), var6.getY() + 1, var4.getZ(), var5.size(), List.of());
            this.ma = _d.y.G;
            this.i = this.m9;
            this.Cf = this.m9;
            this.K("you are standing on a tower left from an earlier run; it is used and then dug away.");
            _q.z(this.getName(), "Took over the leftover tower of {} at {}", var5.size(), ((BlockPos)var5.getFirst()).toShortString());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int Y(BlockPos var1) {
      int var2 = 0;

      for (Direction var4 : Plane.HORIZONTAL) {
         if (this.t(var1.relative(var4))) {
            var2++;
         }
      }

      return var2;
   }

   private boolean L() {
      BlockPos var1 = this.r();
      return !id.h(
         new d1(var0 -> -1, false),
         var1.getX(),
         var1.getY(),
         var1.getZ(),
         new ol.c(var1.getX() - 3, var1.getY() - 3, var1.getZ() - 3, var1.getX() + 3, var1.getY() + 3, var1.getZ() + 3)
      );
   }

   private boolean v(_d.t var1, List<id.w> var2) {
      Item var3 = this.g();
      if (var3 != null && this.U9 != null && !var2.isEmpty()) {
         int var4 = id.S(
            new d1(var0 -> -1, false),
            var2,
            this.U9,
            this.mk.getValue().intValue(),
            this.B(var3),
            var1::c,
            (var2x, var3x, var4x, var5) -> this.T(var1, var2x, var3x, var4x, var5)
         );
         if (var4 == 0) {
            return false;
         }

         _q.z(this.getName(), "Raising the pillar at {} {} from {} to {} for the next blocks", this.U9.x(), this.U9.z(), this.U9.height(), var4);
         this.U9 = new id.s(this.U9.x(), this.U9.ground(), this.U9.z(), var4, this.U9.unlocks());
         this.ma = _d.y.m;
         this.Un = mc.player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private boolean J(_d.t var1, List<id.w> var2) {
      Item var3 = this.g();
      if (var3 != null && this.lA.size() < 24 && this.B(var3) != 0) {
         if (this.m9 != this.Uj) {
            this.Uj = this.m9;
            this.mW = 0;
         }

         if (this.mW >= 3) {
            return false;
         }

         this.mW++;
         BlockPos var4 = this.r();
         ArrayList var5 = new ArrayList();

         for (id.w var7 : this.E(var1, 2)) {
            if (this.x(var1, var7)) {
               var5.add(var7);
            }
         }

         HashSet var10 = new HashSet();

         for (BlockPos var8 : this.lA) {
            var10.add(hn.f(var8.getX(), 0, var8.getZ()));
         }

         if (this.U9 != null) {
            var10.add(hn.f(this.U9.x(), 0, this.U9.z()));
         }

         int[] var12 = id.d(
            new d1(var0 -> -1, false),
            var5,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            3,
            this.Y(),
            var1::c,
            (var2x, var3x, var4x, var5x) -> this.T(var1, var2x, var3x, var4x, var5x),
            var10
         );
         if (var12 == null) {
            return false;
         }

         for (Direction var9 : Plane.HORIZONTAL) {
            if (var9.getStepX() == var12[0] && var9.getStepZ() == var12[1]) {
               this.Ct = var9;
            }
         }

         this.CB = var4.below().relative(this.Ct);
         this.ma = _d.y.J;
         this.mu = mc.player.tickCount;
         _q.z(
            this.getName(),
            "Stepping {} along the top instead of digging down, {} blocks still to place up here ({} walkway blocks so far)",
            this.Ct.getName(),
            var5.size(),
            this.lA.size()
         );
         return true;
      } else {
         return false;
      }
   }

   private void C(String var1) {
      _q.z(this.getName(), "Walkway stopped: {}", var1);
      vi.J(this);
      this.CB = null;
      this.ma = _d.y.G;
      this.y(var1);
   }

   private void v() {
      Item var1 = this.g();
      if (var1 == null || mc.player.tickCount - this.mu > 120) {
         this.C("a walkway block did not go down");
      } else if (!vi.i(this.CB)) {
         this.lA.add(this.CB);
         this.ma = _d.y.E;
         this.mu = mc.player.tickCount;
      } else {
         this.B = (boolean)1;
         if (mc.player.onGround()) {
            int var2 = this.D(var1);
            if (var2 == -2) {
               this.C("no " + F(var1) + " left");
            } else if (var2 >= 0 && this.f(var2)) {
               BlockPos var3 = this.CB.relative(this.Ct.getOpposite());
               Vec3 var4 = Vec3.atCenterOf(var3);
               double var5 = this.Ct.getStepX();
               double var7 = this.Ct.getStepZ();
               double var9 = (mc.player.getX() - var4.x) * var5 + (mc.player.getZ() - var4.z) * var7;
               double var11 = var7 != 0.0 ? var4.x - mc.player.getX() : 0.0;
               double var13 = var5 != 0.0 ? var4.z - mc.player.getZ() : 0.0;
               this.CE = var9 < 0.66 ? new Vec3(var5 + var11, 0.0, var7 + var13) : new Vec3(var11, 0.0, var13);
               Vec3 var15 = mc.player.getEyePosition();
               Vec3 var16 = var4.add(var5 * 0.5, 0.3, var7 * 0.5);
               float var17 = this.Ct.getOpposite().toYRot();
               float var18 = (float)Math.toDegrees(Math.atan2(var15.y - var16.y, Math.max(var9 - 0.5, 0.08)));
               this.aimPlacement(var17, var18);
               this.P("Laying a walkway block");
               if (!(var9 < 0.54)) {
                  double var19 = this.Y();
                  BlockHitResult var21 = vi.y(this, var15, var3, Vec3.directionFromRotation(var18, var17), var19);
                  if (var21 != null && var21.getDirection() == this.Ct) {
                     BlockPos var22 = this.CB;
                     vi.V(
                        this,
                        var22,
                        var21,
                        var1,
                        var19,
                        true,
                        () -> vi.i(var22) && !this.W(),
                        () -> vi.V(this, var3, var19) && qf.F(mc.player, var21) && qf.x(mc.player, var3),
                        var1x -> this.Cs = mc.player.tickCount
                     );
                  }
               }
            } else {
               this.P("Taking out " + F(var1) + " for the walkway");
            }
         }
      }
   }

   private void h() {
      if (mc.player.tickCount - this.mu > 120) {
         this.C("could not step onto the walkway");
      } else {
         this.B = (boolean)1;
         this.P("Stepping along the walkway");
         Vec3 var1 = Vec3.atBottomCenterOf(this.CB.above()).subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
         if (var1.length() < 0.25) {
            this.CB = null;
            this.ma = _d.y.G;
            this.i = this.m9;
            this.l7.w();
         } else {
            this.CE = var1;
         }
      }
   }

   private void W() {
      _d.t var1 = this.mt;
      if (var1 == null) {
         this.lA.clear();
         this.ma = _d.y.D;
      } else if (this.lA.isEmpty()) {
         vs.B(this);
         this.ma = _d.y.D;
         this.Un = mc.player.tickCount;
      } else if (mc.player.tickCount - this.Un <= 80) {
         this.B = (boolean)1;
         Item var2 = this.g();
         Block var3 = O(var2 == null ? Items.DIRT : var2);
         BlockPos var4 = this.lA.getLast();
         BlockPos var5 = this.lA.size() >= 2 ? this.lA.get(this.lA.size() - 2) : (this.l1.isEmpty() ? null : this.l1.getLast());
         if (var5 == null) {
            this.lA.clear();
         } else {
            Vec3 var6 = Vec3.atBottomCenterOf(var5.above()).subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
            if (mc.player.onGround() && mc.player.getY() < var5.getY() + 0.5) {
               vs.B(this);
               _q.z(
                  this.getName(),
                  "Fell off the walkway at {} with {} blocks left; sweeping them and the tower up instead",
                  this.r().toShortString(),
                  this.lA.size()
               );
               this.W(this.lA);
               this.W(this.l1);
               this.U9 = null;
               this.ma = null;
            } else if (var6.length() > 0.25) {
               vs.B(this);
               this.P("Walking back along the walkway");
               this.CE = var6;
            } else if (mc.level.getBlockState(var4).is(var3) && !var1.c(var4.getX(), var4.getY(), var4.getZ())) {
               if (!this.m(var4)) {
                  this.P("Cannot see the walkway block at " + var4.toShortString());
               }
            } else {
               this.lA.removeLast();
               this.Un = mc.player.tickCount;
            }
         }
      } else {
         vs.B(this);
         _q.z(
            this.getName(),
            "Walkway could not be taken up from the top with {} blocks left at {}; sweeping them and the tower up instead",
            this.lA.size(),
            this.lA.getFirst().toShortString()
         );
         _q.z(
            this.getName(),
            "Walkway stall: player {}, next block {}, on ground {}, speed {}, rotation owner other than the bot {}, keys tick {}, screen {}",
            mc.player.position(),
            this.lA.getLast().toShortString(),
            mc.player.onGround(),
            mc.player.getDeltaMovement().horizontalDistance(),
            b6.x() && !b6.c(b6.w.x),
            this.mx == mc.player.tickCount,
            mc.gui.screen() != null
         );
         this.W(this.lA);
         this.W(this.l1);
         this.U9 = null;
         this.ma = null;
      }
   }

   private boolean I(_d.t var1) {
      Item var2 = this.g();
      if (var2 == null || this.B(var2) >= this.mB) {
         this.mB = 0;
         return false;
      }

      if (this.t.getValue() && this.v(var1)) {
         return true;
      }

      if (this.lL.getValue() && !this.n9 && !this.L.contains(var2) && this.d(var1)) {
         return true;
      }

      this.h("out of " + F(var2) + " for temporary blocks; carry at least " + this.mB + (this.lL.getValue() ? "" : " or turn on Buy Missing Blocks"));
      return true;
   }

   private boolean s(_d.t var1, id.w var2) {
      BlockState var3 = var1.c(var2.id());
      BlockPos var4 = new BlockPos(var2.x(), var2.y(), var2.z());
      return var3.getBlock() instanceof FenceGateBlock
         ? true
         : !var3.hasBlockEntity() && !qf.H(var3) && !qf.v(var3) && !qf.n(var3) && var3.isCollisionShapeFullBlock(mc.level, var4);
   }

   private boolean C(_d.t var1, List<id.w> var2) {
      Item var3 = this.g();
      if (var3 != null && !this.j(((id.w)var2.getFirst()).y())) {
         BlockPos var4 = this.r();
         id.u var5 = id.h(
            new d1(var0 -> -1, false),
            var2,
            var1::c,
            (var0, var1x, var2x) -> qf.i(new BlockPos(var0, var1x, var2x)),
            var2x -> this.s(var1, var2x),
            this.d,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            var2x -> this.g(var1, var2x)
         );
         if (var5 == null) {
            return false;
         } else if (this.B(var3) < var5.height()) {
            this.mB = var5.height();
            return true;
         } else {
            this.C1 = var5;
            this.lD = _d.f.J;
            this.CM = this.Uc = mc.player.tickCount;
            this.UJ.clear();
            this.q.V();
            this.mv = List.of();
            _q.z(
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

   private boolean A(List<id.w> var1) {
      int var2 = mc.player.tickCount;
      if (this.m9 != this.UK || var2 - this.no > 5) {
         this.UK = this.m9;
         this.nO = var2;
      }

      this.no = var2;
      if (var2 - this.nO <= 1200) {
         return false;
      }

      this.nO = var2;
      _q.z(this.getName(), "No block went down for {} ticks ({}); setting aside what it was trying", 1200, this.ma);
      if (var1 != null) {
         for (id.w var4 : var1) {
            this.w(var4.id(), "no progress");
         }
      }

      this.E();
      this.q.V();
      if (this.U9 != null && this.ma != null && this.ma != _d.y.D && this.ma != _d.y.e && this.ma != _d.y.c) {
         this.y("no block went down for a minute");
      }

      return true;
   }

   private boolean W(_d.t var1) {
      if (this.C(var1, this.lW, new MutableBlockPos()) == qf.s.X && mc.player.tickCount - this.lZ <= 400) {
         this.P("Breaking a block that faces the wrong way");
         return this.m(this.CU);
      } else {
         vs.B(this);
         this.CU = null;
         return false;
      }
   }

   private boolean j(int var1) {
      if (this.m9 > this.lQ) {
         this.np.clear();
      }

      return this.np.getOrDefault(var1, 0) >= 2;
   }

   private void R(int var1) {
      if (this.m9 > this.lQ) {
         this.np.clear();
      }

      this.lQ = this.m9;
      if (this.np.merge(var1, 1, Integer::sum) == 2) {
         _q.z(this.getName(), "Two temporary supports in a row gave nothing at Y {}; no more there until a block of the build goes down", var1);
      }
   }

   private boolean g(_d.t var1, id.u var2) {
      BlockPos var3 = this.r();
      id.w var4 = var2.seed();
      if (this.U(var2, var3.getX(), var3.getY(), var3.getZ(), this.ma == _d.y.G)) {
         return true;
      }

      if (this.ma == _d.y.G) {
         return false;
      }

      d1 var5 = new d1(var0 -> -1, false);
      Set var6 = id.L(var5, var4, this.Y(), var1.l(), false, (var2x, var3x, var4x, var5x) -> this.U(var2, var3x, var4x, var5x, false), Set.of());
      if (var6.isEmpty()) {
         return false;
      }

      hn.j var7 = new hn.j(
         Math.min(var1.u, var3.getX()) - 10,
         Math.min(var1.F, var3.getY()) - 6,
         Math.min(var1.B, var3.getZ()) - 10,
         Math.max(var1.k, var3.getX()) + 10,
         Math.max(var1.m, var3.getY()) + 6,
         Math.max(var1.V, var3.getZ()) + 10
      );
      return hn.a(var5, var3.getX(), var3.getY(), var3.getZ(), hn.c.Q(var6, var4.x(), var4.y() - 1, var4.z(), this.Y()), var7, hn.m.walking(16000)).found();
   }

   private boolean U(id.u var1, int var2, int var3, int var4, boolean var5) {
      Item var6 = this.g();
      if (var6 != null && id.s(var1, var2, var3, var4)) {
         BlockState var7 = this.mt == null ? null : this.mt.c(var1.seed().id());
         if (var7 != null && var7.getBlock() instanceof FenceGateBlock) {
            Direction var8 = (Direction)var7.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (var1.x() - var1.seed().x() != var8.getStepX() || var1.z() - var1.seed().z() != var8.getStepZ()) {
               return false;
            }
         }

         double var14 = this.Y() - 0.3;
         if (id.C(var2, var3, var4, var1.seed()) > var14 * var14) {
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

         return this.w(var6, new BlockPos(var1.x(), var1.ground(), var1.z()), new Vec3(var2 + 0.5, var3 + 1.62, var4 + 0.5), var14, true) != null;
      } else {
         return false;
      }
   }

   private boolean U(_d.t var1, List<id.w> var2) {
      Item var3 = this.g();
      if (var3 != null && !this.j(((id.w)var2.getFirst()).y())) {
         BlockPos var4 = this.r();
         if (this.m9 > this.mb) {
            this.Y.clear();
         }

         HashSet var5 = new HashSet<>(this.CZ);
         var5.addAll(this.Y);
         id.s var6 = id.Z(
            new d1(var0 -> -1, false),
            var2,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.Y(),
            this.mk.getValue().intValue(),
            var1::c,
            (var0, var1x, var2x) -> qf.i(new BlockPos(var0, var1x, var2x)),
            var2x -> this.s(var1, var2x),
            (var1x, var2x, var3x, var4x) -> this.U(var1x, var2x, var3x, var4x, true),
            this.d,
            var5,
            600
         );
         if (var6 == null) {
            return false;
         } else if (this.B(var3) < var6.height() + 3) {
            this.mB = var6.height() + 3;
            return true;
         } else {
            this.U9 = var6;
            this.Uy = (boolean)1;
            this.E = ((id.w)var2.getFirst()).y();
            this.ma = _d.y.c;
            this.ly = this.Un = mc.player.tickCount;
            this.q.V();
            this.mv = List.of();
            _q.z(
               this.getName(),
               "Layer Y {} is too high to start from the floor: tower of {} at {} {} {} to stand on",
               ((id.w)var2.getFirst()).y(),
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

   private boolean Z(_d.t var1) {
      switch (this.lD) {
         case J:
            this.o(var1);
            break;
         case O:
            this.y(var1);
            break;
         case u:
            id.w var2 = this.C1.seed();
            int var3 = this.C(var1, var2.id(), new MutableBlockPos()) != qf.s.N ? 1 : 0;
            if (var3 == 0 && !this.mO.containsKey(var2.id()) && !this.mX.contains(var2.id()) && mc.player.tickCount - this.CM <= 400) {
               return false;
            }

            if (var3 == 0) {
               this.d.add(hn.f(var2.x(), var2.y(), var2.z()));
               this.R(var2.y());
            }

            this.lD = _d.f.r;
            this.Uc = mc.player.tickCount;
            this.u(var1);
            break;
         case r:
            this.u(var1);
      }

      return true;
   }

   private void Z(String var1) {
      id.w var2 = this.C1.seed();
      _q.z(this.getName(), "Temporary support at {} {} {} left out: {}", this.C1.x(), this.C1.ground(), this.C1.z(), var1);
      this.d.add(hn.f(var2.x(), var2.y(), var2.z()));
      this.R(var2.y());
      this.q.V();
      vi.J(this);
      if (this.UJ.isEmpty()) {
         this.C1 = null;
         this.lD = null;
      } else {
         this.lD = _d.f.r;
         this.Uc = mc.player.tickCount;
      }
   }

   private BlockPos A() {
      return new BlockPos(this.C1.x(), this.C1.ground() + this.UJ.size(), this.C1.z());
   }

   private BlockHitResult w(Item var1, BlockPos var2, Vec3 var3, double var4, boolean var6) {
      ItemStack var7 = this.G(var1);
      return var7.isEmpty() ? null : qf.L(this, mc.player, var7, var2, O(var1).defaultBlockState(), var3, var4, true, var6);
   }

   private boolean X() {
      return mc.player.getBoundingBox().intersects(new AABB(this.C1.x(), this.C1.ground(), this.C1.z(), this.C1.x() + 1, this.C1.top() + 1, this.C1.z() + 1));
   }

   private void o(_d.t var1) {
      if (mc.player.tickCount - this.CM > 400) {
         this.Z("the walk took too long");
      } else {
         Item var2 = this.g();
         if (var2 == null) {
            this.Z("the block is used by the build");
         } else {
            BlockPos var3 = this.A();
            if (this.q.X()) {
               this.R();
            } else if (!this.X() && this.w(var2, var3, mc.player.getEyePosition(), this.Y(), false) != null) {
               this.lD = _d.f.O;
               this.Uc = mc.player.tickCount;
            } else if (this.ma == _d.y.G) {
               this.Z("no click from the tower's top");
            } else {
               BlockPos var4 = this.r();
               id.w var5 = this.C1.seed();
               double var6 = this.Y() - 0.3;
               Set var8 = id.L(
                  new d1(var0 -> -1, false),
                  new id.w(var3.getX(), var3.getY(), var3.getZ(), -1),
                  this.Y(),
                  var1.l(),
                  false,
                  (var6x, var7, var8x, var9) -> (var7 != this.C1.x() || var9 != this.C1.z())
                     && id.C(var7, var8x, var9, var5) <= var6 * var6
                     && this.w(var2, var3, new Vec3(var7 + 0.5, var8x + 1.62, var9 + 0.5), var6, true) != null,
                  Set.of()
               );
               var8.remove(hn.f(var4.getX(), var4.getY(), var4.getZ()));
               if (var8.isEmpty()) {
                  this.Z("no spot to place it from");
               } else if (!this.i(var1, var8, var3)) {
                  this.Z("no way to walk to it");
               } else {
                  this.R();
               }
            }
         }
      }
   }

   private boolean i(_d.t var1, Set<Long> var2, BlockPos var3) {
      BlockPos var4 = this.r();
      hn.j var5 = new hn.j(
         Math.min(var1.u, var4.getX()) - 10,
         Math.min(var1.F, var4.getY()) - 6,
         Math.min(var1.B, var4.getZ()) - 10,
         Math.max(var1.k, var4.getX()) + 10,
         Math.max(var1.m, var4.getY()) + 6,
         Math.max(var1.V, var4.getZ()) + 10
      );
      hn.l var6 = hn.a(
         new d1(var0 -> -1, false),
         var4.getX(),
         var4.getY(),
         var4.getZ(),
         hn.c.Q(var2, var3.getX(), var3.getY() - 1, var3.getZ(), this.Y()),
         var5,
         hn.m.walking(16000)
      );
      if (var6.found() && !var6.steps().isEmpty()) {
         this.q.s(var6.steps());
         this.Cr = var3;
         hn.g var7 = var6.steps().getLast();
         this.Uz = new BlockPos(var7.x(), var7.y(), var7.z());
         return true;
      } else {
         return false;
      }
   }

   private void y(_d.t var1) {
      Item var2 = this.g();
      if (var2 == null) {
         this.Z("the block is used by the build");
      } else {
         Block var3 = O(var2);

         while (!this.UJ.isEmpty() && !mc.level.getBlockState(this.UJ.getLast()).is(var3)) {
            this.UJ.removeLast();
         }

         if (this.UJ.size() >= this.C1.height()) {
            this.lD = _d.f.u;
            this.CM = mc.player.tickCount;
            this.l7.w();
         } else if (mc.player.tickCount - this.Uc > 40) {
            this.Z("it did not go down");
         } else {
            int var4 = this.D(var2);
            if (var4 == -2) {
               this.Z("no " + F(var2) + " left");
            } else if (var4 >= 0 && this.f(var4)) {
               this.e();
               BlockPos var5 = this.A();
               BlockHitResult var6 = this.X() ? null : this.w(var2, var5, mc.player.getEyePosition(), this.Y(), false);
               if (var6 == null) {
                  this.lD = _d.f.J;
               } else {
                  BlockPos var7 = var6.getBlockPos();
                  this.M(var7);
                  double var8 = this.Y();
                  vi.V(
                     this,
                     var5,
                     var6,
                     var2,
                     var8,
                     true,
                     () -> vi.i(var5) && !this.W(),
                     () -> vi.V(this, var7, var8) && qf.F(mc.player, var6) && qf.x(mc.player, var7) && this.y(var7),
                     var2x -> {
                        this.Cs = mc.player.tickCount;
                        if (var2x && !this.UJ.contains(var5)) {
                           this.UJ.add(var5);
                           this.Uc = mc.player.tickCount;
                        }
                     }
                  );
                  this.P("Placing a temporary block at " + var5.toShortString());
               }
            } else {
               this.P("Taking out " + F(var2) + " for a temporary block");
            }
         }
      }
   }

   private void u(_d.t var1) {
      Item var2 = this.g();

      for (Block var3 = O(var2 == null ? Items.DIRT : var2);
         !this.UJ.isEmpty() && !mc.level.getBlockState(this.UJ.getLast()).is(var3);
         this.Uc = mc.player.tickCount
      ) {
         this.UJ.removeLast();
      }

      if (this.UJ.isEmpty()) {
         vs.B(this);
         this.C1 = null;
         this.lD = null;
         this.l7.w();
         this.P("Temporary block removed");
      } else if (this.q.X()) {
         this.R();
      } else if (mc.player.tickCount - this.Uc > 200) {
         vs.B(this);
         _q.z(this.getName(), "Could not dig the temporary block at {} right away; sweeping it up instead", this.UJ.getFirst().toShortString());
         this.W(this.UJ);
         this.C1 = null;
         this.lD = null;
      } else {
         this.e();
         BlockPos var4 = this.UJ.getLast();
         if (!this.m(var4)) {
            if (this.ma != _d.y.G) {
               BlockPos var5 = this.r();
               Set var6 = id.L(
                  new d1(var0 -> -1, false),
                  new id.w(var4.getX(), var4.getY(), var4.getZ(), -1),
                  this.Y(),
                  var1.l(),
                  false,
                  (var1x, var2x, var3x, var4x) -> var2x != var4.getX() || var4x != var4.getZ(),
                  Set.of()
               );
               var6.remove(hn.f(var5.getX(), var5.getY(), var5.getZ()));
               if (!var6.isEmpty() && this.i(var1, var6, var4)) {
                  this.R();
               }
            }
         }
      }
   }

   private String L(_d.t var1, id.w var2) {
      BlockState var3 = var1.c(var2.id());
      ItemStack var4 = this.G(qf.h(var3));
      if (var4.isEmpty()) {
         return "none carried";
      }

      BlockPos var5 = new BlockPos(var2.x(), var2.y(), var2.z());
      if (mc.player.getBoundingBox().intersects(new AABB(var5))) {
         return "the body is in the cell";
      }

      String var6 = qf.j(this, mc.player, var4, var5, var3, mc.player.getEyePosition(), this.Y());
      return var6 == null ? "steady look missed" : var6;
   }

   private String q() {
      _d.t var1 = this.mt;
      if (var1 != null && this.U9 != null) {
         StringBuilder var2 = new StringBuilder();
         MutableBlockPos var3 = new MutableBlockPos();

         for (id.w var5 : this.U9.unlocks()) {
            if (this.C(var1, var5.id(), var3) == qf.s.N) {
               String var6 = !this.mO.containsKey(var5.id()) && !this.mX.contains(var5.id())
                  ? (this.M(var1, var5) == null ? "no click from here: " + this.L(var1, var5) : "click was possible")
                  : "set aside";
               var2.append(var2.isEmpty() ? "; planned but not placed: " : ", ")
                  .append(var1.c(var5.id()).getBlock().getName().getString())
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

   private void W(List<BlockPos> var1) {
      Item var2 = this.g();
      Block var3 = O(var2 == null ? Items.DIRT : var2);

      for (BlockPos var5 : var1) {
         if (mc.level.getBlockState(var5).is(var3) && (this.mt == null || !this.mt.c(var5.getX(), var5.getY(), var5.getZ())) && !this.m4.contains(var5)) {
            this.m4.add(var5);
         }
      }

      var1.clear();
   }

   private void P(_d.t var1) {
      Item var2 = this.g();
      Block var3 = O(var2 == null ? Items.DIRT : var2);
      this.m4.removeIf(var1x -> !mc.level.getBlockState(var1x).is(var3));
      if (this.m4.isEmpty()) {
         vs.B(this);
         this.nQ = null;
      } else if (this.q.X()) {
         this.R();
      } else {
         if (this.nQ == null || !this.m4.contains(this.nQ)) {
            this.nQ = this.m4.stream().max(Comparator.comparingInt(Vec3i::getY)).orElseThrow();
            this.mr = mc.player.tickCount;
         }

         if (mc.player.tickCount - this.mr > 160) {
            vs.B(this);
            this.K("could not reach the temporary block at " + this.nQ.toShortString() + "; break it by hand.");
            _q.z(this.getName(), "Sweep gave up on the temporary block at {}", this.nQ.toShortString());
            vs.j(this.getName(), this.nQ);
            this.m4.remove(this.nQ);
            this.nQ = null;
         } else {
            this.P("Removing the temporary blocks left standing");
            if (!this.m(this.nQ)) {
               BlockPos var4 = this.r();
               BlockPos var5 = this.nQ;
               Set var6 = id.L(
                  new d1(var0 -> -1, false),
                  new id.w(var5.getX(), var5.getY(), var5.getZ(), -1),
                  this.Y(),
                  var1.l(),
                  false,
                  (var2x, var3x, var4x, var5x) -> (var3x != var5.getX() || var5x != var5.getZ()) && this.H(var3x, var4x, var5x, var5),
                  Set.of()
               );
               var6.remove(hn.f(var4.getX(), var4.getY(), var4.getZ()));
               if (!var6.isEmpty() && this.i(var1, var6, var5) || !this.A(var1, var5)) {
                  if (!var6.isEmpty() && (!this.q.I().isEmpty() || this.i(var1, var6, var5))) {
                     this.R();
                  } else {
                     this.mr = mc.player.tickCount - 160 - 1;
                  }
               }
            }
         }
      }
   }

   private boolean A(_d.t var1, BlockPos var2) {
      Item var3 = this.g();
      if (var3 != null && this.CL.getValue() && this.B(var3) != 0 && !this.n4.contains(var2)) {
         this.n4.add(var2);
         BlockPos var4 = this.r();
         id.w var5 = new id.w(var2.getX(), var2.getY(), var2.getZ(), -1);
         id.s var6 = id.O(
            new d1(var0 -> -1, false),
            List.of(var5),
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.Y(),
            this.mk.getValue().intValue(),
            var1::c,
            (var2x, var3x, var4x, var5x) -> (var3x != var2.getX() || var5x != var2.getZ()) && this.H(var3x, var4x, var5x, var2),
            this.CZ,
            600
         );
         if (var6 != null && this.B(var3) >= var6.height()) {
            this.q.V();
            this.U9 = new id.s(var6.x(), var6.ground(), var6.z(), var6.height(), List.of());
            this.ma = _d.y.c;
            this.ly = this.Un = mc.player.tickCount;
            this.Cd = (boolean)1;
            _q.z(
               this.getName(),
               "Tower of {} at {} {} {} to dig the temporary block at {} left out of reach",
               var6.height(),
               var6.x(),
               var6.ground(),
               var6.z(),
               var2.toShortString()
            );
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void O() {
      Item var1 = this.g();
      Block var2 = O(var1 == null ? Items.DIRT : var1);
      this.m4.removeIf(var1x -> !mc.level.getBlockState(var1x).is(var2));
      BlockPos var3 = this.r();
      ArrayList var4 = new ArrayList<>(this.m4);
      var4.sort(Comparator.<BlockPos>comparingInt(var0 -> var0.getY()).reversed());

      for (BlockPos var6 : var4) {
         if ((var6.getX() != var3.getX() || var6.getZ() != var3.getZ()) && this.H(var3.getX(), var3.getY(), var3.getZ(), var6)) {
            if (!var6.equals(this.nQ)) {
               this.nQ = var6;
               this.mr = mc.player.tickCount;
            }

            if (mc.player.tickCount - this.mr > 160) {
               vs.B(this);
               _q.z(this.getName(), "Sweep gave up on the temporary block at {} from a tower", var6.toShortString());
               vs.j(this.getName(), var6);
               this.K("could not break the temporary block at " + var6.toShortString() + "; break it by hand.");
               this.m4.remove(var6);
               this.nQ = null;
               return;
            }

            this.P("Removing the temporary blocks left standing");
            if (this.m(var6)) {
               return;
            }
         }
      }

      this.Cd = (boolean)0;
      this.y("the temporary blocks in reach are gone");
   }

   private boolean H(int var1, int var2, int var3, BlockPos var4) {
      Vec3 var5 = new Vec3(var1 + 0.5, var2 + 1.62, var3 + 0.5);
      double var6 = this.Y() - 0.3;

      for (Direction var11 : Direction.values()) {
         Vec3 var12 = Vec3.atCenterOf(var4).add(var11.getStepX() * 0.5, var11.getStepY() * 0.5, var11.getStepZ() * 0.5);
         Vec3 var13 = new Vec3(var11.getStepX(), var11.getStepY(), var11.getStepZ());
         if (!(var5.subtract(var12).dot(var13) <= 0.02)
            && !(var5.distanceToSqr(var12) > var6 * var6)
            && vi.y(this, var5, var4, var12.subtract(var5), var6) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean m(BlockPos var1) {
      return vs.i(this, var1, this.Y(), true, 12.0F, vs::q, () -> !this.W());
   }

   private void g(String var1) {
      _q.c(this.getName(), "Pillar at {} {}: {}", this.U9.x(), this.U9.z(), var1);
      this.Cd = (boolean)0;
      if (this.l5 != null && this.md.isEmpty()) {
         if (this.l5.cells().isEmpty()) {
            this.Cw.remove(this.l5);
         }

         this.l5 = null;
      }

      this.CZ.add(hn.f(this.U9.x(), 0, this.U9.z()));
      this.q.V();
      vi.J(this);
      if (this.l1.isEmpty()) {
         if (this.Uy) {
            this.R(this.E);
            this.Uy = (boolean)0;
         }

         this.U9 = null;
         this.ma = null;
      } else {
         this.y(var1);
      }
   }

   private void m(_d.t var1) {
      BlockPos var2 = this.r();
      BlockPos var3 = new BlockPos(this.U9.x(), this.U9.ground(), this.U9.z());
      if (mc.player.tickCount - this.ly > 600) {
         this.g("the walk took too long");
      } else if (var2.equals(var3) && !this.q.X()) {
         this.ma = _d.y.m;
         this.Un = mc.player.tickCount;
         this.nB++;
         this.Cf = this.m9;
         _q.z(
            this.getName(),
            "Pillar of {} at {} {} {} for {} blocks now, in reach of more above",
            this.U9.height(),
            this.U9.x(),
            this.U9.ground(),
            this.U9.z(),
            this.U9.unlocks().size()
         );
         this.P("Building a pillar of " + this.U9.height());
      } else {
         if (!this.q.X()) {
            hn.j var4 = new hn.j(
               Math.min(var1.u, var2.getX()) - 10,
               Math.min(var1.F, var2.getY()) - 6,
               Math.min(var1.B, var2.getZ()) - 10,
               Math.max(var1.k, var2.getX()) + 10,
               Math.max(var1.m, var2.getY()) + 6,
               Math.max(var1.V, var2.getZ()) + 10
            );
            hn.l var5 = hn.a(
               new d1(var0 -> -1, false),
               var2.getX(),
               var2.getY(),
               var2.getZ(),
               hn.c.Q(Set.of(hn.f(var3.getX(), var3.getY(), var3.getZ())), var3.getX(), var3.getY(), var3.getZ(), 0.0),
               var4,
               hn.m.walking(16000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.g("no way to walk to it");
               return;
            }

            this.q.s(var5.steps());
            this.Cr = var3;
            this.Uz = var3;
         }

         this.R();
      }
   }

   private void d(_d.t var1) {
      Item var2 = this.g();
      int var3 = this.D();
      if (var2 == null) {
         this.g("the pillar block is used by the build");
      } else if (var3 >= this.U9.height()) {
         if (!mc.player.onGround()) {
            this.P("Landing on the pillar");
         } else {
            this.ma = _d.y.G;
            this.i = this.m9;
            this.l7.w();
            _q.c(this.getName(), "On a pillar of {} at {} {}", var3, this.U9.x(), this.U9.z());
         }
      } else if (mc.player.tickCount - this.Un > 80) {
         this.g("a pillar block did not go down");
      } else {
         BlockPos var4 = new BlockPos(this.U9.x(), this.U9.ground() + var3, this.U9.z());
         BlockPos var5 = var4.below();
         int var6 = this.D(var2);
         if (var6 == -2) {
            this.g("no " + F(var2) + " left");
         } else if (var6 >= 0 && this.f(var6)) {
            this.B = (boolean)(var3 > 0 ? 1 : 0);
            Vec3 var7 = new Vec3(var4.getX() + 0.5, var4.getY(), var4.getZ() + 0.5);
            float[] var8 = b6.R(mc.player.getEyePosition(), var7);
            this.aimPlacement(var8[0], var8[1]);
            double var9 = this.Y();
            if (!mc.player.onGround()) {
               if (!(mc.player.getY() < var4.getY() + 1.02)) {
                  BlockHitResult var11 = new BlockHitResult(var7, Direction.UP, var5, false);
                  vi.V(
                     this,
                     var4,
                     var11,
                     var2,
                     var9,
                     true,
                     () -> vi.i(var4) && !this.W(),
                     () -> vi.V(this, var5, var9) && mc.player.getY() >= var4.getY() + 1.02,
                     var2x -> {
                        this.Cs = mc.player.tickCount;
                        if (var2x) {
                           this.l1.add(var4);
                           this.Un = mc.player.tickCount;
                        }
                     }
                  );
               }
            } else {
               if (vi.V(this, var5, var9) && (var3 == 0 || mc.level.getBlockState(var5).is(O(var2)))) {
                  this.o = (boolean)1;
               }

               this.P("Building a pillar: " + var3 + " of " + this.U9.height());
            }
         } else {
            this.P("Taking out " + F(var2) + " for a pillar");
         }
      }
   }

   private static Block O(Item var0) {
      return var0 instanceof BlockItem var1 ? var1.getBlock() : null;
   }

   private int D() {
      Block var1 = O(this.g() == null ? Items.DIRT : this.g());

      while (!this.l1.isEmpty() && !mc.level.getBlockState(this.l1.getLast()).is(var1)) {
         this.l1.removeLast();
      }

      return this.l1.size();
   }

   private void y(String var1) {
      vi.J(this);
      this.q.V();
      this.ma = this.lA.isEmpty() ? _d.y.D : _d.y.e;
      this.Un = mc.player.tickCount;
      if (this.U9 != null) {
         this.Y.add(hn.f(this.U9.x(), 0, this.U9.z()));
         this.mb = this.m9;
      }

      if (this.m9 == this.i && this.U9 != null) {
         this.CZ.add(hn.f(this.U9.x(), 0, this.U9.z()));
      }

      if (this.U9 != null && this.mt != null && this.m9 == this.i) {
         MutableBlockPos var2 = new MutableBlockPos();

         for (id.w var4 : this.U9.unlocks()) {
            if (var4.id() >= 0 && this.C(this.mt, var4.id(), var2) == qf.s.N) {
               this.U8.add(var4.id());
            }
         }
      }

      if (this.Uy && this.m9 == this.Cf) {
         this.R(this.E);
      }

      this.Uy = (boolean)0;
      if (this.U9 != null) {
         _q.z(
            this.getName(),
            "Tower at {} {} comes down after {} blocks, {} walkway blocks: {}{}",
            this.U9.x(),
            this.U9.z(),
            this.m9 - this.Cf,
            this.lA.size(),
            var1,
            this.q()
         );
      }
   }

   private void T() {
      Item var1 = this.g();

      for (Block var2 = O(var1 == null ? Items.DIRT : var1);
         !this.l1.isEmpty() && !mc.level.getBlockState(this.l1.getLast()).is(var2);
         this.Un = mc.player.tickCount
      ) {
         this.l1.removeLast();
      }

      if (this.l1.isEmpty()) {
         vs.B(this);
         this.U9 = null;
         this.ma = null;
         this.l7.w();
         this.nx = 12;
         this.P("Pillar removed");
      } else if (!mc.player.onGround() && !mc.player.isInWater()) {
         this.P("Coming down");
      } else if (mc.player.tickCount - this.Un > 200) {
         vs.B(this);
         BlockPos var4 = this.l1.getFirst();
         _q.z(this.getName(), "Could not dig the pillar at {} from its top with {} blocks left; sweeping them up instead", var4.toShortString(), this.l1.size());
         this.W(this.l1);
         this.U9 = null;
         this.ma = null;
      } else {
         this.B = (boolean)1;
         BlockPos var3 = this.l1.getLast();
         if (!this.m(var3)) {
            this.p(var3);
         }
      }
   }

   private void p(BlockPos var1) {
      if (this.UB.add(var1.asLong())) {
         _q.z(
            this.getName(),
            "Miner refused {}: wait {}, allowed {}, breakable {}, seen {}, state {}, player {} eye {}, block above feet {}, at head {}",
            var1.toShortString(),
            this.B(),
            _p.D(this),
            vs.W(var1),
            vs.W(this, var1, this.Y()) != null,
            mc.level.getBlockState(var1).getBlock().getName().getString(),
            mc.player.position(),
            mc.player.getEyePosition(),
            mc.level.getBlockState(mc.player.blockPosition()).getBlock().getName().getString(),
            mc.level.getBlockState(mc.player.blockPosition().above()).getBlock().getName().getString()
         );
      }
   }

   private static boolean T(BlockState var0) {
      Block var1 = var0.getBlock();
      return var1 instanceof ChestBlock || var1 instanceof BarrelBlock || var1 instanceof ShulkerBoxBlock;
   }

   private String P() {
      return mc.level.dimension().identifier().toString();
   }

   private String r() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (mc.hitResult instanceof BlockHitResult var1 && var1.getType() == Type.BLOCK && T(mc.level.getBlockState(var1.getBlockPos()))) {
         BlockPos var7 = var1.getBlockPos();
         _d.c var3 = new _d.c(this.P(), var7.getX(), var7.getY(), var7.getZ());
         List var4 = Z(this.lB.getValue());
         boolean var5 = var4.remove(var3);
         if (!var5) {
            var4.add(var3);
         }

         this.lB.T(D(var4));
         this.H();
         String var6 = mc.level.getBlockState(var7).getBlock().getName().getString();
         _q.z(this.getName(), "{} restock container {} at {} ({} marked)", var5 ? "Unmarked" : "Marked", var6, var7.toShortString(), var4.size());
         return (var5 ? "Unmarked the " : "Marked the ")
            + var6.toLowerCase(Locale.ROOT)
            + " at "
            + var7.toShortString()
            + " as a restock container ("
            + var4.size()
            + " marked).";
      } else {
         return "Look at a chest, barrel or shulker box first.";
      }
   }

   private String G() {
      int var1 = Z(this.lB.getValue()).size();
      this.lB.T("");
      this.H();
      return var1 == 0 ? "No containers were marked." : "Cleared " + var1 + " restock " + (var1 == 1 ? "mark." : "marks.");
   }

   private void Z() {
      int var1 = this.l9.getValue() != -1
            && this.isInGame()
            && mc.gui.screen() == null
            && mc.isWindowActive()
            && p4.Y(mc.getWindow().handle(), this.l9.getValue())
         ? 1
         : 0;
      if (var1 != 0 && !this.nD) {
         this.K(this.r());
      }

      this.nD = (boolean)var1;
   }

   private boolean v(_d.t var1) {
      this.Z.clear();

      for (Entry var3 : this.g(var1).entrySet()) {
         int var4 = (Integer)var3.getValue() - this.B((Item)var3.getKey());
         if (var4 > 0) {
            this.Z.put((Item)var3.getKey(), var4);
         }
      }

      Item var11 = this.CL.getValue() ? this.g() : null;
      if (this.mB > 0 && var11 != null && this.B(var11) < 32) {
         this.Z.put(var11, 32 - this.B(var11));
      }

      if (this.Z.isEmpty()) {
         return false;
      }

      if (var11 != null && this.B(var11) < 32) {
         this.Z.put(var11, 32 - this.B(var11));
      }

      _d.c var12 = null;
      double var13 = this.mm.getValue() * this.mm.getValue();
      String var6 = this.P();

      for (_d.c var8 : Z(this.lB.getValue())) {
         if (var8.dimension().equals(var6) && !this.V.contains(var8)) {
            double var9 = mc.player.distanceToSqr(Vec3.atCenterOf(var8.pos()));
            if (!(var9 > var13)) {
               if (mc.level.hasChunkAt(var8.pos()) && !T(mc.level.getBlockState(var8.pos()))) {
                  this.V.add(var8);
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

      this.Cv = var12;
      this.Ck = _d.j.i;
      this.CY = mc.player.tickCount;
      this.Ch = this.Cu = 0;
      this.ni.clear();
      this.q.V();
      this.mv = List.of();
      vi.J(this);
      ArrayList var14 = new ArrayList();

      for (Entry var16 : this.Z.entrySet()) {
         var14.add(var16.getValue() + " " + F((Item)var16.getKey()));
      }

      _q.z(this.getName(), "Restock trip to {} for {}", var12.pos().toShortString(), String.join(", ", var14));
      this.P("Going to the restock container at " + var12.pos().toShortString());
      return true;
   }

   private void W(String var1) {
      if (this.mC) {
         this.R(var1);
      } else {
         if (this.Ch == 0 && this.Cv != null) {
            this.V.add(this.Cv);
         } else if (this.Ch > 0) {
            this.V.clear();
         }

         _q.z(this.getName(), "Restock at {}: took {}, put back {} ({})", this.Cv == null ? "?" : this.Cv.pos().toShortString(), this.Ch, this.Cu, var1);
         if (this.Ch > 0 || this.Cu > 0) {
            this.mE++;
            this.K("restocked at " + this.Cv.pos().toShortString() + ": took " + this.Ch + (this.Cu > 0 ? ", put back " + this.Cu : "") + ".");
         }

         this.Ck = null;
         this.Cv = null;
         this.q.V();
         vi.J(this);
         this.nx = 12;
      }
   }

   private Set<Long> Z(BlockPos var1) {
      d1 var2 = new d1(var0 -> -1, false);
      HashSet var3 = new HashSet();
      double var4 = (this.Y() - 0.3) * (this.Y() - 0.3);

      for (int var6 = -4; var6 <= 4; var6++) {
         for (int var7 = -4; var7 <= 4; var7++) {
            for (int var8 = -3; var8 <= 1; var8++) {
               int var9 = var1.getX() + var6;
               int var10 = var1.getY() + var8;
               int var11 = var1.getZ() + var7;
               if (var6 != 0 || var7 != 0) {
                  double var12 = var9 + 0.5 - (var1.getX() + 0.5);
                  double var14 = var10 + 1.62 - (var1.getY() + 0.5);
                  double var16 = var11 + 0.5 - (var1.getZ() + 0.5);
                  if (!(var12 * var12 + var14 * var14 + var16 * var16 > var4)
                     && var2.loaded(var9, var10, var11)
                     && var2.floor(var9, var10 - 1, var11)
                     && var2.open(var9, var10, var11)
                     && var2.open(var9, var10 + 1, var11)
                     && qf.k(this, var1, new Vec3(var9 + 0.5, var10 + 1.62, var11 + 0.5), this.Y() - 0.3, null) != null) {
                     var3.add(hn.f(var9, var10, var11));
                  }
               }
            }
         }
      }

      return var3;
   }

   private void J(_d.t var1) {
      BlockPos var2 = this.Cv.pos();
      if (mc.player.tickCount - this.CY > 900) {
         this.W("the walk took too long");
      } else if (mc.level.hasChunkAt(var2) && T(mc.level.getBlockState(var2))) {
         if (!this.q.X() && qf.k(this, var2, mc.player.getEyePosition(), this.Y(), null) != null) {
            this.Ck = _d.j.u;
            this.CY = mc.player.tickCount;
         } else {
            if (!this.q.X()) {
               BlockPos var3 = this.r();
               Set var4 = this.Z(var2);
               if (var4.isEmpty()) {
                  this.W("no spot to stand at it");
                  return;
               }

               hn.j var5 = new hn.j(
                  Math.min(var2.getX(), var3.getX()) - 12,
                  Math.min(var2.getY(), var3.getY()) - 6,
                  Math.min(var2.getZ(), var3.getZ()) - 12,
                  Math.max(var2.getX(), var3.getX()) + 12,
                  Math.max(var2.getY(), var3.getY()) + 6,
                  Math.max(var2.getZ(), var3.getZ()) + 12
               );
               hn.l var6 = hn.a(
                  new d1(var0 -> -1, false),
                  var3.getX(),
                  var3.getY(),
                  var3.getZ(),
                  hn.c.Q(var4, var2.getX(), var2.getY(), var2.getZ(), this.Y()),
                  var5,
                  hn.m.walking(32000)
               );
               if (!var6.found()) {
                  this.W("no way to walk to it");
                  return;
               }

               if (var6.steps().isEmpty()) {
                  this.Ck = _d.j.u;
                  this.CY = mc.player.tickCount;
                  return;
               }

               this.q.s(var6.steps());
               this.Cr = var2;
               hn.g var7 = var6.steps().getLast();
               this.Uz = new BlockPos(var7.x(), var7.y(), var7.z());
            }

            this.R();
         }
      } else {
         this.W("the container is gone");
      }
   }

   private void g() {
      BlockPos var1 = this.Cv.pos();
      if (mc.player.tickCount - this.CY > 60) {
         this.W("it did not open");
      } else {
         this.CE = Vec3.ZERO;
         this.B = (boolean)0;
         int var2 = this.g();
         if (var2 >= 0 && !this.f(var2)) {
            this.P("Putting the block away");
         } else {
            ItemStack var3 = mc.player.getMainHandItem();
            Item var4 = var3.isEmpty() ? Items.AIR : var3.getItem();
            double var5 = this.Y();
            vi.i(
               this,
               var1,
               var4,
               var5,
               true,
               false,
               () -> !this.W(),
               () -> !mc.player.isShiftKeyDown() && mc.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 && vi.V(this, var1, var5),
               var1x -> {
                  this.Cs = mc.player.tickCount;
                  if (var1x) {
                     this.Ck = _d.j.m;
                     this.CY = mc.player.tickCount;
                     this.lT = -1000000000;
                  }
               }
            );
            this.P("Opening the restock container at " + var1.toShortString());
         }
      }
   }

   private boolean X(_d.t var1) {
      int var2 = mc.player.tickCount;
      this.z();
      Screen var3 = mc.gui.screen();
      AbstractContainerMenu var4 = mc.player.containerMenu;
      if (var3 instanceof AbstractContainerScreen && var4 != mc.player.inventoryMenu) {
         if (this.lT == -1000000000) {
            this.lT = var2;
         }

         if (var2 - this.CY > 400) {
            mc.player.closeContainer();
            this.W("the clicks took too long");
            return true;
         }

         this.P("Restocking at " + this.Cv.pos().toShortString());
         if (var2 - this.lT >= 6 && var2 - this.mT >= 4 && var4.getCarried().isEmpty()) {
            int var5 = var4.slots.size() - 36;
            if (var5 <= 0) {
               mc.player.closeContainer();
               this.W("not a container menu");
               return true;
            }

            if (this.Cl.getValue()) {
               Map var6 = this.J();
               Item var7 = this.CL.getValue() ? this.g() : null;

               for (int var8 = var5; var8 < var4.slots.size(); var8++) {
                  ItemStack var9 = ((Slot)var4.slots.get(var8)).getItem();
                  if (!var9.isEmpty()
                     && var9.getItem() instanceof BlockItem
                     && !var6.containsKey(var9.getItem())
                     && !var9.is(var7)
                     && !this.ni.contains(var8)
                     && K(var4, 0, var5, var9)) {
                     int var10 = var9.getCount();
                     this.b(var4, var8);
                     this.Cu += var10;
                     this.ni.add(var8);
                     return true;
                  }
               }
            }

            for (int var11 = 0; var11 < var5; var11++) {
               ItemStack var12 = ((Slot)var4.slots.get(var11)).getItem();
               int var13 = var12.isEmpty() ? 0 : this.Z.getOrDefault(var12.getItem(), 0);
               if (var13 > 0 && K(var4, var5, var4.slots.size(), var12)) {
                  int var14 = var12.getCount();
                  Item var15 = var12.getItem();
                  this.b(var4, var11);
                  this.Ch += var14;
                  this.Z.merge(var15, -var14, Integer::sum);
                  return true;
               }
            }

            mc.player.closeContainer();
            this.W(this.Z.values().stream().anyMatch(var0 -> var0 > 0) ? "took what it had" : "took everything needed");
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.CY <= 60) {
         this.P("Waiting for the container to open");
         return true;
      } else {
         this.W("it did not open");
         return false;
      }
   }

   private void b(AbstractContainerMenu var1, int var2) {
      mc.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, mc.player);
      this.mT = mc.player.tickCount;
   }

   private static boolean K(AbstractContainerMenu var0, int var1, int var2, ItemStack var3) {
      for (int var4 = var1; var4 < var2; var4++) {
         ItemStack var5 = ((Slot)var0.slots.get(var4)).getItem();
         if (var5.isEmpty() || ItemStack.isSameItemSameComponents(var5, var3) && var5.getCount() < var5.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   private String X() {
      if (!this.isInGame()) {
         return "";
      }

      int var1 = 0;

      for (_d.c var3 : Z(this.lB.getValue())) {
         if (var3.dimension().equals(this.P())) {
            var1++;
         }
      }

      String var4 = var1 == 0 ? "No chests selected. Look at one and press Select chest." : var1 + (var1 == 1 ? " chest selected." : " chests selected.");
      return this.mC && this.UW ? var4 + " " + this.o() + "." : var4;
   }

   private String N() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.UW) {
         return this.mC ? "Already stocking." : "Turn the builder off first; it stocks between builds.";
      }

      String var1 = ac.K(this);
      if (var1 != null) {
         return "Turn off " + var1 + " first; one bot runs at a time.";
      }

      if (this.UY) {
         return "Stop and save the Build Recorder first.";
      }

      _d.t var2 = this.mt;
      if (var2 == null) {
         return this.lz == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var2.h) {
         return "Still checking the world; try again in a moment.";
      }

      if (this.g(var2).isEmpty()) {
         return "The build needs no more blocks.";
      }

      int var3 = this.K().size();
      if (var3 == 0) {
         return "Select the material chests first: look at a chest, barrel or shulker box within "
            + this.mm.getValue().intValue()
            + " blocks and press Select chest.";
      }

      long var4 = System.currentTimeMillis();
      if (var4 - this.Ut > 10000L) {
         this.Ut = var4;
         return "Press Stock materials again within 10 seconds to start. It checks the "
            + var3
            + (var3 == 1 ? " chest" : " chests")
            + " for what they already hold, buys only what is still short from /ah, cheapest first, at most "
            + pc.b(this.f.getValue().longValue())
            + " a block and "
            + pc.b(this.l8.getValue().longValue())
            + " in all, and puts it into them.";
      }

      this.Ut = 0L;
      this.mP = var4;
      this.enable();
      if (this.mC && this.UW) {
         return "Stocking: checking " + var3 + (var3 == 1 ? " chest" : " chests") + " first.";
      }

      this.mP = 0L;
      return this.lf != null ? this.lf + "." : "Stocking did not start.";
   }

   private List<_d.c> K() {
      ArrayList var1 = new ArrayList();
      double var2 = this.mm.getValue() * this.mm.getValue();
      String var4 = this.P();

      for (_d.c var6 : Z(this.lB.getValue())) {
         if (var6.dimension().equals(var4) && mc.player.distanceToSqr(Vec3.atCenterOf(var6.pos())) <= var2) {
            var1.add(var6);
         }
      }

      return var1;
   }

   private void t() {
      if (this.Cj) {
         this.F();
      }

      _d.t var1 = this.mt;
      if (var1 != null && var1.h) {
         List var2 = this.K();
         if (var2.isEmpty()) {
            this.h("no material chests are selected within Restock Distance");
         } else {
            if (var1.P <= 200000) {
               this.h(var1, var1.P);
            }

            this.UW = (boolean)1;
            this.mC = (boolean)1;
            this.CR = (boolean)0;
            this.mj = mc.level;
            this.lG = mc.player;
            this.ld.v();
            this.C6 = System.currentTimeMillis();
            this.lc = _d.h.c;
            this.m3.addAll(var2);
            _q.z(this.getName(), "Stocking started for {}: reading {} containers within {} blocks", this.lz.e(), var2.size(), this.mm.getValue());
            this.K("stocking " + this.lz.e() + ": checking " + var2.size() + (var2.size() == 1 ? " chest" : " chests") + " for what they hold first.");
         }
      } else {
         this.h("set the schematic and its origin first");
      }
   }

   private void V(_d.t var1) {
      if (this.Ck == _d.j.i) {
         this.J(var1);
      } else if (this.Ck == _d.j.u) {
         this.g();
      } else if (this.lc == _d.h.c) {
         _d.c var6 = this.N(this.m3);
         if (var6 != null) {
            this.p(var6);
         } else {
            this.I(var1);
         }
      } else {
         if (this.lc == _d.h.x) {
            _d.v var2 = this.w();
            if (var2 == _d.v.N) {
               return;
            }

            if (var2 == _d.v.D) {
               this.CR = (boolean)1;
            }

            this.lc = _d.h.l;
         }

         if (this.K.isEmpty()) {
            if (!this.CR) {
               this.lc = _d.h.x;
            } else {
               this.H((String)null);
            }
         } else {
            ArrayList var5 = new ArrayList();

            for (_d.c var4 : this.CK.keySet()) {
               if (!this.C3.contains(var4) && (this.Cc.getOrDefault(var4, 0) > 0 || !Collections.disjoint(this.CK.get(var4).keySet(), this.K.keySet()))) {
                  var5.add(var4);
               }
            }

            _d.c var7 = this.N(var5);
            if (var7 == null) {
               this.H("the selected chests are full");
            } else {
               this.p(var7);
            }
         }
      }
   }

   private _d.c N(Collection<_d.c> var1) {
      _d.c var2 = null;
      double var3 = Double.MAX_VALUE;

      for (_d.c var6 : var1) {
         if (!this.Up.contains(var6) && !this.Cy.contains(var6)) {
            double var7 = mc.player.distanceToSqr(Vec3.atCenterOf(var6.pos()));
            if (var7 < var3) {
               var2 = var6;
               var3 = var7;
            }
         }
      }

      return var2;
   }

   private void p(_d.c var1) {
      this.Cv = var1;
      this.Ck = _d.j.i;
      this.CY = mc.player.tickCount;
      this.Ch = this.Cu = 0;
      this.ni.clear();
      this.D = (boolean)0;
      this.q.V();
      this.mv = List.of();
      vi.J(this);
      _q.z(this.getName(), "Stocking: going to {} to {}", var1.pos().toShortString(), this.lc == _d.h.c ? "read it" : "put " + R(this.K) + " blocks in");
      this.P((this.lc == _d.h.c ? "Checking the chest at " : "Taking the materials to the chest at ") + var1.pos().toShortString());
   }

   private void R(String var1) {
      _d.c var2 = this.Cv;
      _q.z(this.getName(), "Stocking at {}: {}", var2 == null ? "?" : var2.pos().toShortString(), var1);
      if (var2 != null) {
         if (this.lc == _d.h.c) {
            this.m3.remove(var2);
         }

         if (!this.D && this.Up.add(var2)) {
            this.K("could not use the chest at " + var2.pos().toShortString() + ": " + var1 + "; going on without it.");
         }
      }

      this.Ck = null;
      this.Cv = null;
      this.q.V();
      vi.J(this);
   }

   private boolean i() {
      int var1 = mc.player.tickCount;
      this.z();
      Screen var2 = mc.gui.screen();
      AbstractContainerMenu var3 = mc.player.containerMenu;
      if (var2 instanceof AbstractContainerScreen && var3 != mc.player.inventoryMenu) {
         if (this.lT == -1000000000) {
            this.lT = var1;
         }

         if (var1 - this.CY > 400) {
            mc.player.closeContainer();
            this.W("the clicks took too long");
            return true;
         }

         if (var1 - this.lT >= 6 && var1 - this.mT >= 4 && var3.getCarried().isEmpty()) {
            int var4 = var3.slots.size() - 36;
            if (var4 <= 0) {
               mc.player.closeContainer();
               this.W("not a container menu");
               return true;
            }

            _d.c var5 = this.Cv;
            if (this.lc == _d.h.l) {
               this.P("Putting materials into the chest at " + var5.pos().toShortString());

               for (int var6 = var4; var6 < var3.slots.size(); var6++) {
                  ItemStack var7 = ((Slot)var3.slots.get(var6)).getItem();
                  if (!var7.isEmpty() && this.K.getOrDefault(var7.getItem(), 0) > 0 && var7.getComponentsPatch().isEmpty() && K(var3, 0, var4, var7)) {
                     Item var8 = var7.getItem();
                     int var9 = var7.getCount();
                     this.b(var3, var6);
                     int var10 = this.K.get(var8) - var9;
                     if (var10 > 0) {
                        this.K.put(var8, var10);
                     } else {
                        this.K.remove(var8);
                     }

                     this.lr.merge(var8, var9, Integer::sum);
                     this.D = (boolean)1;
                     return true;
                  }
               }

               if (!this.K.isEmpty()) {
                  this.C3.add(var5);
               }
            }

            HashMap var11 = new HashMap();
            int var12 = 0;

            for (int var13 = 0; var13 < var4; var13++) {
               ItemStack var15 = ((Slot)var3.slots.get(var13)).getItem();
               if (var15.isEmpty()) {
                  var12++;
               } else if (var15.getComponentsPatch().isEmpty()) {
                  var11.merge(var15.getItem(), var15.getCount(), Integer::sum);
               }
            }

            if (!this.CK.containsKey(var5)) {
               _q.z(
                  this.getName(),
                  "Stocking: the container at {} holds {} kinds of plain items, {} of {} slots free",
                  var5.pos().toShortString(),
                  var11.size(),
                  var12,
                  var4
               );
            }

            this.CK.put(var5, var11);
            this.Cc.put(var5, var12);
            _d.c var14 = this.U(var5);
            if (var14 != null) {
               this.Cy.add(var14);
               this.m3.remove(var14);
               this.CK.remove(var14);
               this.Cc.remove(var14);
            }

            this.D = (boolean)1;
            mc.player.closeContainer();
            this.W(this.lc == _d.h.c ? "read it" : (this.K.isEmpty() ? "put everything in" : "it is full"));
            return true;
         } else {
            return true;
         }
      } else if (var1 - this.CY <= 60) {
         this.P("Waiting for the chest to open");
         return true;
      } else {
         this.W("it did not open");
         return false;
      }
   }

   private _d.c U(_d.c var1) {
      BlockState var2 = mc.level.getBlockState(var1.pos());
      if (var2.getBlock() instanceof ChestBlock && var2.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
         BlockPos var3 = var1.pos().relative(ChestBlock.getConnectedDirection(var2));
         return new _d.c(var1.dimension(), var3.getX(), var3.getY(), var3.getZ());
      } else {
         return null;
      }
   }

   private void I(_d.t var1) {
      int var2 = 0;

      for (_d.c var4 : this.CK.keySet()) {
         if (!this.Cy.contains(var4)) {
            var2++;
         }
      }

      if (var2 == 0) {
         this.h("none of the selected chests could be opened");
      } else {
         LinkedHashMap var10 = new LinkedHashMap<>(this.g(var1));
         Item var11 = this.CL.getValue() ? this.g() : null;
         if (var11 != null) {
            var10.merge(var11, 32, Integer::sum);
         }

         HashMap var5 = new HashMap();

         for (Map var7 : this.CK.values()) {
            var7.forEach((var1x, var2x) -> var5.merge(var1x, var2x, Integer::sum));
         }

         for (Item var15 : var10.keySet()) {
            var5.merge(var15, this.B(var15), Integer::sum);
         }

         this.m6.putAll(S(var10, var5));
         this.a = 0;

         for (int var16 : this.Cc.values()) {
            this.a += var16;
         }

         int var14 = R(var10);
         int var17 = var14 - R(this.m6);
         _q.z(this.getName(), "Stocking plan: {} chests read, {} of {} blocks at hand, {} free chest slots; to buy {}", var2, var17, var14, this.a, this.m6);
         this.K(
            "checked "
               + var2
               + (var2 == 1 ? " chest" : " chests")
               + ": with your inventory they hold "
               + var17
               + " of the "
               + var14
               + " blocks the build still needs."
         );
         if (this.m6.isEmpty()) {
            this.H("nothing needs buying");
         } else {
            for (Entry var9 : this.m6.entrySet()) {
               this.K("  to buy: " + var9.getValue() + " " + F((Item)var9.getKey()));
            }

            this.lc = _d.h.x;
         }
      }
   }

   static Map<Item, Integer> S(Map<Item, Integer> var0, Map<Item, Integer> var1) {
      LinkedHashMap var2 = new LinkedHashMap();

      for (Entry var4 : var0.entrySet()) {
         int var5 = (Integer)var4.getValue() - var1.getOrDefault(var4.getKey(), 0);
         if (var5 > 0) {
            var2.put((Item)var4.getKey(), var5);
         }
      }

      return var2;
   }

   static int R(Map<Item, Integer> var0) {
      int var1 = 0;

      for (int var3 : var0.values()) {
         var1 += var3;
      }

      return var1;
   }

   private _d.v w() {
      if (this.n9) {
         return _d.v.D;
      }

      long var1 = this.l8.getValue().longValue() - this.l6;
      if (var1 <= 0L) {
         this.q("Max Total Spend is used up; nothing more is bought");
         return _d.v.D;
      }

      for (Entry var4 : this.m6.entrySet()) {
         Item var5 = (Item)var4.getKey();
         if ((Integer)var4.getValue() > 0 && !this.L.contains(var5)) {
            if (this.a <= 0) {
               this.q("the selected chests have no free slots left; select more chests");
               return _d.v.D;
            }

            int var6 = new ItemStack(var5).getMaxStackSize();
            int var7 = Math.min((Integer)var4.getValue(), this.a * var6);
            int var8 = this.T(var5);
            if (var8 < Math.min(var7, var6)) {
               if (!this.K.isEmpty()) {
                  return _d.v.W;
               }

               this.q("no free inventory room to buy " + F(var5) + " into; empty a few slots");
               return _d.v.D;
            }

            var7 = Math.min(var7, var8);
            this.R = var5;
            this.lt = 0;
            this.q.V();
            this.mv = List.of();
            vi.J(this);
            this.x.p(var5, var7, this.f.getValue().longValue(), this.mh.getValue().longValue(), var1);
            this.K(
               "buying "
                  + var7
                  + " "
                  + F(var5)
                  + " from /ah, cheapest first: at most "
                  + pc.b(this.f.getValue().longValue())
                  + " a block, "
                  + pc.b(this.mh.getValue().longValue())
                  + " a listing, "
                  + pc.b(var1)
                  + " left to spend."
            );
            return _d.v.N;
         }
      }

      return _d.v.D;
   }

   private int T(Item var1) {
      int var2 = new ItemStack(var1).getMaxStackSize();
      int var3 = 0;
      Inventory var4 = mc.player.getInventory();

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var4.getItem(var5);
         if (var6.isEmpty()) {
            var3 += var2;
         } else if (var6.is(var1) && var6.getComponentsPatch().isEmpty()) {
            var3 += Math.max(0, var2 - var6.getCount());
         }
      }

      return var3;
   }

   private void O(Item var1, int var2, long var3, String var5) {
      if (var1 != null) {
         if (var2 > 0) {
            int var6 = new ItemStack(var1).getMaxStackSize();
            int var7 = this.m6.getOrDefault(var1, 0) - var2;
            if (var7 > 0) {
               this.m6.put(var1, var7);
            } else {
               this.m6.remove(var1);
            }

            this.K.merge(var1, var2, Integer::sum);
            this.UL.merge(var1, var2, Integer::sum);
            this.a -= (var2 + var6 - 1) / var6;
         } else {
            this.UC.put(var1, var3);
            this.Uf.put(var1, var5 != null && !var5.isEmpty() ? var5 : "nothing bought");
         }
      }
   }

   private void H(String var1) {
      String var2 = _6.t(System.currentTimeMillis() - this.C6);
      StringBuilder var3 = new StringBuilder("stocking done in ").append(var2);
      if (var1 != null) {
         var3.append(" (").append(var1).append(')');
      }

      var3.append(": bought ").append(R(this.UL)).append(" blocks for ").append(pc.b(this.l6)).append(", put ").append(R(this.lr)).append(" into the chests");
      this.K(var3 + ".");

      for (Entry var5 : this.UL.entrySet()) {
         this.K("  bought " + var5.getValue() + " " + F((Item)var5.getKey()) + ", put away " + this.lr.getOrDefault(var5.getKey(), 0));
      }

      if (!this.K.isEmpty()) {
         ArrayList var7 = new ArrayList();

         for (Entry var6 : this.K.entrySet()) {
            var7.add(var6.getValue() + " " + F((Item)var6.getKey()));
         }

         this.K("  still in your inventory, no chest had room: " + String.join(", ", var7));
      }

      for (Entry var10 : this.m6.entrySet()) {
         String var11 = this.Uf.containsKey(var10.getKey())
            ? this.Uf.get(var10.getKey())
            : (
               this.n9
                  ? "out of money"
                  : (this.l6 >= this.l8.getValue().longValue() ? "Max Total Spend reached" : (this.a <= 0 ? "no chest room left" : "not bought"))
            );
         this.K("  still short: " + var10.getValue() + " " + F((Item)var10.getKey()) + " (" + var11 + ")");
      }

      _q.z(this.getName(), "Stocking done: {}; still short {}; reasons {}", var3, this.m6, this.Uf);
      this.lf = "Last stocking: bought " + R(this.UL) + ", put away " + R(this.lr) + (this.m6.isEmpty() ? "" : ", " + R(this.m6) + " still short");
      ib.L()
         .N(
            "Auto Builder",
            "Stocking done: " + R(this.lr) + " blocks put away" + (this.m6.isEmpty() ? "" : ", " + R(this.m6) + " still short"),
            this.m6.isEmpty() ? ib.v.x : ib.v.o,
            8000L
         );
      this.UW = (boolean)0;
      this.disable();
   }

   private String o() {
      String var1 = this.lc == _d.h.c ? "checking the chests" : (this.lc == _d.h.x ? "buying" : "putting materials away");
      return "Stocking, "
         + var1
         + ": bought "
         + R(this.UL)
         + " for "
         + pc.b(this.l6)
         + ", "
         + R(this.lr)
         + " put away, "
         + _6.t(System.currentTimeMillis() - this.C6);
   }

   private Path h() {
      return G().resolve(qs.y(this.l2.getValue().trim()));
   }

   private void e(_d.t var1) {
      this.mn = var1;
      this.Uh = null;
      this.mI = null;
      if (var1 != null && !this.l2.getValue().isBlank()) {
         try {
            List var2 = qs.K(this.h());
            if (var2.isEmpty()) {
               return;
            }

            ArrayList var3 = new ArrayList();

            for (int[] var5 : var2) {
               int var6 = var1.X(var5[0], var5[1], var5[2]);
               if (var6 >= 0) {
                  var3.add(var6);
               }
            }

            this.Uh = var3.stream().mapToInt(Integer::intValue).toArray();
            _q.z(
               this.getName(),
               "Recorded order of {}: {} cells ({} not in the schematic)",
               this.l2.getValue().trim(),
               this.Uh.length,
               var2.size() - this.Uh.length
            );
         } catch (IOException var7) {
            this.mI = var7.getMessage();
            _q.z(this.getName(), "Could not read the recorded order: {}", var7.toString());
         }
      }
   }

   private String j() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.UW) {
         return "Turn the builder off before recording.";
      }

      _d.t var1 = this.mt;
      if (var1 == null) {
         return this.lz == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (this.UY) {
         return "Already recording: " + this.mL.size() + " blocks so far.";
      }

      if (var1.P > 200000) {
         return "This schematic is too big to record (" + var1.P + " blocks).";
      }

      this.Co = new BitSet(var1.P);

      for (int var2 = 0; var2 < var1.P; var2++) {
         BlockState var3 = var1.c(var2);
         if (var3 != null && mc.level.getBlockState(var1.s(var2)).is(var3.getBlock())) {
            this.Co.set(var2);
         }
      }

      this.mL.clear();
      this.lM.clear();
      this.UY = (boolean)1;
      _q.z(this.getName(), "Build Recorder started on {} ({} of {} blocks already stand)", this.lz.e(), this.Co.cardinality(), var1.P);
      return "Recording: place the blocks of " + this.lz.e() + " in the order you want them built, then press Stop and save the recording.";
   }

   private void E(_d.t var1) {
      if (this.UY) {
         BlockPos var2 = mc.player.blockPosition();
         MutableBlockPos var3 = new MutableBlockPos();

         for (int var4 = Math.max(var1.u, var2.getX() - 8); var4 <= Math.min(var1.k, var2.getX() + 8); var4++) {
            for (int var5 = Math.max(var1.F, var2.getY() - 8); var5 <= Math.min(var1.m, var2.getY() + 8); var5++) {
               for (int var6 = Math.max(var1.B, var2.getZ() - 8); var6 <= Math.min(var1.V, var2.getZ() + 8); var6++) {
                  int var7 = var1.E(var4, var5, var6);
                  if (var7 >= 0
                     && !this.lM.contains(var7)
                     && !this.Co.get(var7)
                     && var1.c(var7) != null
                     && mc.level.getBlockState(var3.set(var4, var5, var6)).is(var1.c(var7).getBlock())) {
                     this.mL.add(var7);
                     this.lM.add(var7);
                  }
               }
            }
         }
      }
   }

   private void G(String var1) {
      this.UY = (boolean)0;
      _q.z(this.getName(), "Build Recorder stopped without saving: {} ({} blocks recorded)", var1, this.mL.size());
      this.K("the Build Recorder stopped without saving: " + var1 + ".");
      this.mL.clear();
      this.lM.clear();
   }

   private String T() {
      if (!this.UY) {
         return "The Build Recorder is not recording.";
      }

      _d.t var1 = this.mt;
      this.UY = (boolean)0;
      if (var1 != null && !this.mL.isEmpty()) {
         ArrayList var2 = new ArrayList();

         for (int var4 : this.mL) {
            var2.add(new int[]{var1.j.j(var4), var1.j.v(var4), var1.j.P(var4)});
         }

         Path var6 = this.h();

         try {
            qs.Q(var6, var1.j.e(), var2);
         } catch (IOException var5) {
            return "Could not save the recording: " + var5.getMessage();
         }

         int var7 = var2.size();
         this.mL.clear();
         this.lM.clear();
         this.e(var1);
         _q.z(this.getName(), "Saved the build order of {} blocks to {}", var7, var6.getFileName());
         return "Saved the order of " + var7 + " blocks to " + var6.getFileName() + ". Follow Recording builds in this order.";
      } else {
         this.mL.clear();
         this.lM.clear();
         return "Nothing was placed, so nothing was saved.";
      }
   }

   private String O() {
      if (this.UY) {
         this.G("you deleted the recording");
      }

      if (this.l2.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      try {
         boolean var1 = Files.deleteIfExists(this.h());
         this.e(this.mt);
         return var1 ? "Deleted the recorded order of " + this.l2.getValue().trim() + "." : "This schematic has no recording.";
      } catch (IOException var2) {
         return "Could not delete the recording: " + var2.getMessage();
      }
   }

   private String V() {
      if (this.UY) {
         return " Recording the build order: " + this.mL.size() + " blocks so far.";
      } else if (this.mI != null) {
         return " Its recording could not be read: " + this.mI + ".";
      } else {
         return this.Uh != null && this.Uh.length > 0
            ? " Recorded order: " + this.Uh.length + " blocks" + (this.UT.getValue() ? ", followed." : ", not followed.")
            : "";
      }
   }

   private boolean d(_d.t var1) {
      if (this.n9) {
         return false;
      }

      long var2 = this.l8.getValue().longValue() - this.l6;
      if (var2 <= 0L) {
         this.q("Max Total Spend is used up; buying stops for this run");
         return false;
      }

      Item var4 = null;
      int var5 = 0;
      Item var6 = this.mB > 0 ? this.g() : null;
      if (var6 != null && !this.L.contains(var6) && this.B(var6) < 32) {
         var4 = var6;
         var5 = 32 - this.B(var6);
      } else {
         for (Entry var8 : this.g(var1).entrySet()) {
            Item var9 = (Item)var8.getKey();
            if (!this.L.contains(var9) && this.mM.containsKey(var9)) {
               int var10 = (Integer)var8.getValue() - this.B(var9);
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
         if (mc.player.getInventory().getItem(var13).isEmpty()) {
            var12++;
         }
      }

      var5 = Math.min(var5, var12 * new ItemStack(var4).getMaxStackSize());
      if (var5 <= 0) {
         this.q("no free inventory slot to buy " + F(var4) + " into");
         return false;
      } else {
         this.R = var4;
         this.lt = 0;
         this.q.V();
         this.mv = List.of();
         vi.J(this);
         this.x.p(var4, var5, this.f.getValue().longValue(), this.mh.getValue().longValue(), var2);
         this.K(
            "buying "
               + var5
               + " "
               + F(var4)
               + " from /ah: at most "
               + pc.b(this.f.getValue().longValue())
               + " a block, "
               + pc.b(this.mh.getValue().longValue())
               + " a listing, "
               + pc.b(var2)
               + " left to spend."
         );
         return true;
      }
   }

   private void B() {
      this.z();
      this.B = (boolean)0;
      if (this.W() && this.x.W()) {
         this.x.B("you moved");
      }

      if (this.x.W() && this.lt < 4) {
         this.lt = mc.player.onGround() && mc.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 ? this.lt + 1 : 0;
         this.P("Stopping to buy " + F(this.R));
      } else if (this.x.W() && this.x.t() == vk.q.M) {
         this.P("Buying " + F(this.R) + " from /ah");
      } else {
         this.l6 = this.l6 + this.x.u();
         this.Cz = this.Cz + this.x.j();
         String var1 = F(this.R);
         if (this.x.j() > 0) {
            this.K("bought " + this.x.j() + " " + var1 + " for " + pc.b(this.x.u()) + (this.x.a().isEmpty() ? "" : " (" + this.x.a() + ")") + ".");
            ib.L().t("Auto Builder", "Bought " + this.x.j() + " " + var1 + " for " + pc.b(this.x.u()), ib.v.x);
         }

         if (this.x.j() == 0) {
            this.L.add(this.R);
            this.K("could not buy " + var1 + ": " + (this.x.a().isEmpty() ? "nothing bought" : this.x.a()) + ".");
         }

         if (this.x.M()) {
            this.n9 = (boolean)1;
         }

         if (this.mC) {
            this.O(this.R, this.x.j(), this.x.o(), this.x.a());
         }

         _q.z(
            this.getName(), "Buying {} ended {}: bought {} for {}; this run {} for {}", var1, this.x.E(), this.x.j(), pc.b(this.x.u()), this.Cz, pc.b(this.l6)
         );
         this.R = null;
         this.Cs = mc.player.tickCount;
      }
   }

   private boolean j() {
      double var1 = this.nW.getValue();
      double var3 = var1 * var1;
      String var5 = null;

      for (StaffListModule.o var7 : StaffListModule.F()) {
         if (var7.info() != null) {
            Player var8 = mc.level.getPlayerByUUID(var7.info().getProfile().id());
            if (var8 != null && var8 != mc.player) {
               double var9 = var8.distanceToSqr(mc.player);
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

      this.C8 = var5;
      return true;
   }

   private void f() {
      int var1 = mc.player.tickCount;
      if (this.UD == -1000000000) {
         this.z();
         this.q.V();
         this.ld.s();
         vs.B(this);
         if (mc.gui.screen() instanceof AbstractContainerScreen) {
            mc.player.closeContainer();
         }

         this.m_ = null;
         this.Ck = null;
         if (this.x.W()) {
            this.x.B("staff nearby");
         }

         this.R = null;
         this.UD = var1 + (int)Math.round(this.Cb.getValue() * 20.0);
         _q.z(this.getName(), "Staff nearby: {}; logging off in {} s", this.C8, this.Cb.getValue());
         this.K("staff nearby: " + this.C8 + ". Stopping and logging off.");
      }

      this.z();
      this.P("Staff nearby: " + this.C8);
      if (var1 >= this.UD) {
         ClientPacketListener var2 = mc.getConnection();
         String var3 = "staff came near: " + this.C8;
         if (!this.l1.isEmpty()) {
            var3 = var3 + ", " + this.l1.size() + " temporary blocks left standing";
         }

         if (var2 != null && !mc.isLocalServer()) {
            _q.z(this.getName(), "Logging off: {}", var3);
            this.h("logged off, " + var3);
            var2.getConnection().disconnect(Component.literal(this.U0.getValue().isBlank() ? "Auto Builder stopped: staff nearby" : this.U0.getValue()));
         } else {
            _q.z(this.getName(), "Staff stop in singleplayer: stopped without logging off ({})", var3);
            this.h(var3 + " (singleplayer: not logged off)");
         }
      }
   }

   private boolean R() {
      int var1 = mc.player.tickCount;

      while (!this.mi.isEmpty() && var1 - this.mi.peekFirst() > 1200) {
         this.mi.removeFirst();
      }

      if (this.UA.getValue() && this.mi.size() >= 8) {
         long var2 = this.UP == 0L ? Long.MAX_VALUE : System.currentTimeMillis() - this.UP;
         if (!F(this.mi.size(), var2, this.nv)) {
            if (this.nv >= 3 && !this.UQ) {
               this.UQ = (boolean)1;
               _q.z(this.getName(), "Relog On Ghost Blocks: {} relogs this build already; not relogging again", 3);
               this.K("the server keeps taking placed blocks back, but it already relogged 3 times this build, so it builds on without relogging.");
            }

            return false;
         } else {
            return !mc.isLocalServer() && mc.getCurrentServer() != null && mc.getConnection() != null
               ? this.l1.isEmpty()
                  && this.md.isEmpty()
                  && this.m4.isEmpty()
                  && this.lA.isEmpty()
                  && this.UJ.isEmpty()
                  && this.ma == null
                  && this.C1 == null
                  && this.Ck == null
                  && this.R == null
                  && !this.x.W()
                  && this.m_ == null
                  && !this.mC
                  && !(mc.gui.screen() instanceof AbstractContainerScreen)
                  && mc.player.onGround()
               : false;
         }
      } else {
         return false;
      }
   }

   static boolean F(int var0, long var1, int var3) {
      return var0 >= 8 && var1 >= 300000L && var3 < 3;
   }

   private void J() {
      int var1 = this.mi.size();
      this.mS = mc.getCurrentServer();
      this.lV = 1;
      this.nv++;
      this.UP = System.currentTimeMillis();
      this.nn = 0;
      this.m = (boolean)0;
      this.z();
      this.q.V();
      this.UX.W();
      this.ld.s();
      _q.z(
         this.getName(),
         "Relog On Ghost Blocks: the server took back {} placed blocks in {} ticks; logging off and rejoining {} (relog {} of {}, {} blocks placed)",
         var1,
         1200,
         this.mS.ip,
         this.nv,
         3,
         this.m9
      );
      this.K("the server took back " + var1 + " placed blocks in the last minute. Logging off and rejoining to sync up, then building on.");
      this.P("Logging off to resync blocks");
   }

   private void A() {
      if (this.lV != 0) {
         Minecraft var1 = Minecraft.getInstance();
         if (!this.isEnabled()) {
            this.y("the builder was turned off", false);
         } else {
            long var2 = System.currentTimeMillis();
            if (this.lV == 1) {
               this.y = var2;
               this.v = var2 + 5000L;
               this.lV = 2;
               if (var1.level != null && var1.getConnection() != null) {
                  _q.z(this.getName(), "Logging off {} to resync blocks", this.mS.ip);
                  var1.disconnectFromWorld(Component.literal("Larp Auto Builder: rejoining to resync blocks"));
               }
            } else {
               if (var1.level == null) {
                  this.m = (boolean)1;
               }

               if (this.lV == 3 || this.m && var1.level != null) {
                  this.lV = 3;
                  if (var2 - this.y > 120000L) {
                     this.y("it rejoined but the build did not start again", true);
                  }
               } else if (var1.level != null) {
                  if (var2 - this.y > 120000L) {
                     this.y("the log off did not go through", true);
                  }
               } else if (var2 - this.y > 120000L) {
                  this.y("it could not rejoin within 120 seconds", true);
               } else {
                  Screen var4 = var1.gui.screen();
                  if (!(var4 instanceof ConnectScreen) && var2 >= this.v) {
                     if (this.nn >= 3) {
                        this.y("the server did not take 3 joins", true);
                     } else {
                        this.nn++;
                        this.v = var2 + 10000L;
                        _q.z(this.getName(), "Rejoining {} to resync blocks (try {} of {})", this.mS.ip, this.nn, 3);
                        Object var5 = var4 != null ? var4 : new JoinMultiplayerScreen(new TitleScreen());
                        ConnectScreen.startConnecting((Screen)var5, var1, ServerAddress.parseString(this.mS.ip), this.mS, false, null);
                     }
                  }
               }
            }
         }
      }
   }

   private void y(String var1, boolean var2) {
      _q.z(this.getName(), "Relog On Ghost Blocks gave up: {}", var1);
      this.lV = 0;
      this.mS = null;
      if (this.isEnabled()) {
         this.lp = "it logged off to resync blocks and " + var1;
      }

      if (var2) {
         ib.L().t("Auto Builder relog", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), ib.v.o);
      }
   }

   private void K() {
      if (System.currentTimeMillis() >= this.b && mc.level.hasChunkAt(mc.player.blockPosition())) {
         this.b = 0L;
         String var1 = ac.K(this);
         if (var1 != null) {
            this.h("turn off " + var1 + " first; one bot runs at a time");
         } else {
            _q.z(this.getName(), "Carrying on after the relog ({} of {} this build)", this.nv, 3);
            this.K("rejoined; building on (relog " + this.nv + " of 3 this build).");
            this.G();
         }
      } else {
         this.P("Rejoined; waiting for the world to load");
      }
   }

   private void X(_d.t var1) {
      TreeMap var2 = new TreeMap();
      TreeMap var3 = new TreeMap();
      MutableBlockPos var4 = new MutableBlockPos();
      int var5 = 0;
      int var6 = 0;

      for (int var7 = 0; var7 < var1.P; var7++) {
         if (var1.c(var7) != null && var1.C(var7) == qf.s.N) {
            if (var1.g[var1.U[var7]] != null) {
               var5++;
            } else if (this.lU.contains(var7)) {
               var6++;
            } else {
               this.C(var1, var7, var4);
               var2.merge(var4.getY(), 1, Integer::sum);
               var3.merge(var1.c(var7).getBlock().getName().getString(), 1, Integer::sum);
            }
         }
      }

      _q.z(
         this.getName(),
         "Left over: {} that are never placed (fluids and the like), {} outside the height limits; placeable, by Y: {}; by block: {}",
         var5,
         var6,
         var2,
         var3
      );
      int var9 = 0;

      for (int var8 = 0; var8 < var1.P && var9 < 12; var8++) {
         if (var1.c(var8) != null && (var1.C(var8) == qf.s.X || var1.C(var8) == qf.s.I)) {
            this.C(var1, var8, var4);
            _q.z(this.getName(), "Wrong at {}: {} instead of {}", var4.toShortString(), mc.level.getBlockState(var4), var1.c(var8));
            var9++;
         }
      }
   }

   private void y() {
      _d.t var1 = this.mt;
      if (var1 != null && var1.P <= 200000) {
         this.h(var1, var1.P);
      }

      int var2 = var1 == null ? 0 : var1.y(qf.s.N);
      int var3 = var1 == null ? 0 : var1.y(qf.s.I) + var1.y(qf.s.X);
      if (var1 != null) {
         this.X(var1);
      }

      _q.z(
         this.getName(),
         "Movement: sharpest one-tick turn {} degrees, {} sudden reversals, {} sprint-jumps",
         String.format(Locale.ROOT, "%.1f", this.ls),
         this.C7,
         this.lY
      );
      String var4 = _6.t(System.currentTimeMillis() - this.C6);
      TreeMap var5 = new TreeMap();

      for (String var7 : this.C0.values()) {
         var5.merge(var7, 1, Integer::sum);
      }

      StringBuilder var10 = new StringBuilder(this.lz == null ? "build" : this.lz.e())
         .append(" done in ")
         .append(var4)
         .append(", ")
         .append(this.m9)
         .append(" blocks placed");
      if (this.mU > 0) {
         var10.append(", ").append(this.mU).append(" right clicks to set blocks");
      }

      if (!this.nV.isEmpty()) {
         var10.append(", ").append(this.nV.size()).append(this.nV.size() == 1 ? " crafter" : " crafters").append(" set");
      }

      if (this.nB > 0) {
         var10.append(", ").append(this.nB).append(this.nB == 1 ? " pillar" : " pillars");
      }

      if (this.Cz > 0) {
         var10.append(", bought ").append(this.Cz).append(" blocks for ").append(pc.b(this.l6));
      }

      if (this.mE > 0) {
         var10.append(", ").append(this.mE).append(this.mE == 1 ? " restock trip" : " restock trips");
      }

      if (var2 > 0) {
         var10.append(", ").append(var2).append(" still missing");
      }

      if (this.CI > 0) {
         var10.append(", ").append(this.CI).append(" would fall with nothing below them");
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

      this.lf = "Last run: " + this.m9 + " placed" + (var2 > 0 ? ", " + var2 + " missing" : "");
      this.K(var10 + ".");
      _q.z(this.getName(), "Done: {}", var10);
      if (this.nM.getValue()) {
         ib.L()
            .N(
               "Auto Builder",
               (this.lz == null ? "Build" : this.lz.e()) + " done in " + var4 + (var2 > 0 ? ", " + var2 + " missing" : ""),
               var2 > 0 ? ib.v.o : ib.v.x,
               8000L
            );
      }

      this.UW = (boolean)0;
      this.disable();
   }

   private boolean C() {
      if (this.CQ == this.U1) {
         return true;
      }

      this.CQ = this.U1;
      return false;
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (_p.Q(this) && !this.h()) {
            if (!this.C()) {
               float[] var3 = b6.R(mc.player.getEyePosition(), var1);
               float[] var4 = this.UX.M(mc.player.getYRot(), mc.player.getXRot(), var3[0], var3[1], false);
               b6.P(this, b6.w.x, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.h()) {
            if (!this.C()) {
               int var3 = Math.hypot(Mth.wrapDegrees(var1 - mc.player.getYRot()), var2 - mc.player.getXRot()) < 1.6F ? 1 : 0;
               float[] var4 = this.UX.n(mc.player.getYRot(), mc.player.getXRot(), var1, var2, (boolean)var3, true);
               b6.P(this, b6.w.x, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   private void n(Vec3 var1) {
      if (_p.Q(this) && !this.h()) {
         if (!this.C()) {
            Vec3 var2 = mc.player.getEyePosition();
            Vec3 var3 = var1.add(0.0, 0.6 - this.k + 0.12 * Math.sin(mc.player.tickCount * 0.09 + this.mg), 0.0);
            if (this.Cr != null
               && this.q.I().size() <= 2
               && var2.distanceToSqr(Vec3.atCenterOf(this.Cr)) <= 64.0
               && _6.T(this.q.L(), Vec3.atCenterOf(this.Cr).subtract(var2))) {
               var3 = Vec3.atCenterOf(this.Cr);
            }

            float[] var4 = b6.R(var2, var3);
            float[] var5 = this.UX.M(mc.player.getYRot(), mc.player.getXRot(), var4[0], var4[1], false);
            b6.P(this, b6.w.x, var5[0], var5[1], 1.0);
         }
      }
   }

   private boolean B() {
      return this.Us.getValue().equals("Aggressive");
   }

   public boolean m() {
      return !this.isEnabled()
         ? false
         : this.B()
            && this.UW
            && this.isRuntimeActive()
            && mc.player != null
            && mc.player == this.lG
            && !this.q.X()
            && this.ma == null
            && this.Ck == null
            && this.R == null
            && this.m_ == null
            && this.C8 == null
            && (this.UM != null || mc.player.tickCount - this.Cs < 10)
            && !this.W();
   }

   private boolean h() {
      if (!this.B()) {
         return false;
      }

      ij.r var1 = ij.L();
      return var1 == ij.r.o || var1 == ij.r.z && !this.m();
   }

   public _b N() {
      return this.Uv;
   }

   public double g() {
      return 35.0;
   }

   public static Input a() {
      if (m1 != null && m1.isEnabled()) {
         _d var0 = m1;
         Minecraft var1 = Minecraft.getInstance();
         int var2 = ij.L() == ij.r.z ? 1 : 0;
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.UW
            && var1.player != null
            && var1.player == var0.lG
            && var1.player.tickCount == var0.mx
            && !ij.y()
            && (!dt.x() || var2 != 0)
            && !(var1.gui.screen() instanceof AbstractContainerScreen)
            && (!b6.x() || b6.c(b6.w.x))) {
            int var3 = var0.CE.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 != 0 && var3 != 0) {
               return null;
            }

            if (var3 == 0 && !var0.o && !var0.B) {
               return null;
            }

            Input var4 = var3 != 0 ? var0.UX.q(var1.player.getYRot(), var0.CE) : new Input(false, false, false, false, false, false, false);
            return new Input(var4.forward(), var4.backward(), var4.left(), var4.right(), var0.o, var0.B, var0.lF);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void K(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Auto Builder: " + var1));
      }
   }

   private void q(String var1) {
      if (!var1.equals(this.lK)) {
         this.lK = var1;
         this.K(var1 + ".");
      }
   }

   private void h(String var1) {
      _q.z(this.getName(), "Stopped: {}", var1);
      if (this.mt != null && mc.level != null && this.mt.P <= 200000) {
         this.h(this.mt, this.mt.P);
         this.X(this.mt);
      }

      this.K("stopped, " + var1 + ".");
      ib.L().t("Auto Builder stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), ib.v.o);
      this.P("Stopped: " + var1);
      this.lf = "Stopped: " + var1;
      this.UW = (boolean)0;
      this.disable();
   }

   private int L() {
      _d.t var1 = this.mt;
      if (var1 != null && this.lw > 0) {
         int var2 = var1.y(qf.s.N);
         return Math.clamp(Math.round(100.0 * (this.lw - Math.min(this.lw, var2)) / this.lw), 0, 100);
      } else {
         return this.UW ? 0 : 100;
      }
   }

   private String R() {
      return this.mC ? this.o() : this.L() + "% done, " + this.m9 + " placed, " + _6.t(System.currentTimeMillis() - this.C6);
   }

   private void U() {
      if (this.isInGame()) {
         if (this.lk.getValue()) {
            this.b();
         }

         if (this.UF.getValue()) {
            _d.t var1 = this.mt;
            if (var1 != null && var1.o == mc.level) {
               try {
                  int var2 = com.larpclient.c.A(0.25);
                  Gizmos.cuboid(new AABB(var1.u, var1.F, var1.B, var1.k + 1, var1.m + 1, var1.V + 1), GizmoStyle.stroke(vv.N(var2, 0.8), 1.5F));
                  o4.h var3 = var1.b;
                  Gizmos.cuboid(
                        new AABB(var3.originX(), var3.originY(), var3.originZ(), var3.originX() + 1, var3.originY() + 1, var3.originZ() + 1).inflate(0.04),
                        GizmoStyle.strokeAndFill(-14249, 2.0F, 1090504791)
                     )
                     .setAlwaysOnTop();
                  boolean var4 = this.U3.getValue();

                  for (int var8 : var1.i) {
                     if (var8 < var1.P) {
                        qf.s var9 = var1.C(var8);

                        int var10 = switch (var9) {
                           case N -> var2;
                           case I -> -45747;
                           case X -> -24765;
                           case j -> var4 ? -11870592 : 0;
                           case x -> 0;
                        };
                        if (var10 != 0) {
                           BlockPos var11 = var1.s(var8);
                           float var12 = var9 == qf.s.N ? 0.12F : (var9 == qf.s.j ? 0.08F : 0.3F);
                           Gizmos.cuboid(new AABB(var11).deflate(0.03), GizmoStyle.strokeAndFill(vv.N(var10, 0.55), 1.0F, vv.N(var10, var12)));
                        }
                     }
                  }

                  if (this.UW && this.UR.getValue()) {
                     id.w var14 = this.UM;
                     if (var14 != null) {
                        Gizmos.cuboid(
                              new AABB(new BlockPos(var14.x(), var14.y(), var14.z())).inflate(0.01),
                              GizmoStyle.strokeAndFill(vv.N(var2, 1.0), 2.0F, vv.N(var2, 0.25))
                           )
                           .setAlwaysOnTop();
                     }

                     if (this.q.X()) {
                        Vec3 var15 = mc.player.position().add(0.0, 0.05, 0.0);

                        for (hn.g var17 : this.q.I()) {
                           Vec3 var18 = new Vec3(var17.x() + 0.5, var17.y() + 0.05, var17.z() + 0.5);
                           Gizmos.line(var15, var18, vv.N(var2, 0.85), 2.0F).setAlwaysOnTop();
                           var15 = var18;
                        }
                     }
                  }
               } catch (RuntimeException var13) {
                  _q.g(this.getName(), "Preview failed", var13);
               }
            }
         }
      }
   }

   private void b() {
      List var1 = Z(this.lB.getValue());
      if (!var1.isEmpty()) {
         try {
            String var2 = this.P();
            double var3 = Math.max(32.0, this.mm.getValue());

            for (_d.c var6 : var1) {
               if (var6.dimension().equals(var2) && !(mc.player.distanceToSqr(Vec3.atCenterOf(var6.pos())) > var3 * var3)) {
                  int var7 = this.Cv != null && this.Cv.equals(var6) ? 1 : 0;
                  int var8 = var7 != 0 ? -11870592 : -10443270;
                  Gizmos.cuboid(new AABB(var6.pos()).inflate(0.02), GizmoStyle.strokeAndFill(vv.N(var8, 0.9), 2.0F, vv.N(var8, 0.12))).setAlwaysOnTop();
               }
            }
         } catch (RuntimeException var9) {
            _q.g(this.getName(), "Restock outlines failed", var9);
         }
      }
   }

   @Override
   public ac.g hudCard() {
      if (this.isInGame() && this.C.getValue() && this.UW && this.isRuntimeActive() && mc.level == this.mj) {
         _d.t var1 = this.mt;
         int var2 = var1 != null && var1.h ? 1 : 0;
         String var3 = U_.matcher(this.l2.getValue().trim()).replaceFirst("");
         String var4 = this.getStatus();
         return this.mC
            ? new ac.g(
               "Stocking " + (var3.isEmpty() ? "materials" : var3), "", var4 != null && !var4.isBlank() ? var4 : "Starting", R(this.lr) + " put away", -1.0
            )
            : new ac.g(
               var3.isEmpty() ? "Schematic" : var3,
               var2 != 0 ? this.L() + "%" : "",
               var4 != null && !var4.isBlank() ? var4 : "Starting",
               this.m9 + " placed" + (var2 != 0 ? ", " + var1.y(qf.s.N) + " missing" : ""),
               var2 != 0 ? this.L() / 100.0 : -1.0
            );
      } else {
         return null;
      }
   }

   @Override
   public j0 preview() {
      return new j9(this);
   }

   o4 B() {
      return this.UH ? null : this.lz;
   }

   String e() {
      String var1 = this.l2.getValue().trim();
      if (this.ll) {
         return "Loading " + var1;
      } else {
         return this.l != null ? "Could not load " + (var1.isEmpty() ? "the schematic" : var1) + ": " + this.l : "No schematic loaded";
      }
   }

   public boolean v() {
      return this.UW;
   }

   public int J() {
      return this.m9;
   }

   public o4 e() {
      return this.lz;
   }

   public String E() {
      return this.l;
   }

   public boolean U() {
      return this.ll;
   }

   public int[] H() {
      _d.t var1 = this.mt;
      return var1 != null && var1.h ? (int[])var1.y.clone() : null;
   }

   record c(String dimension, int x, int y, int z) {
      BlockPos pos() {
         return new BlockPos(this.x, this.y, this.z);
      }

      @Override
      public String toString() {
         return this.dimension + " " + this.x + " " + this.y + " " + this.z;
      }
   }

   enum f {
      J,
      O,
      u,
      r;
   }

   enum h {
      c,
      x,
      l;
   }

   enum j {
      i,
      u,
      m;
   }

   private record p(String file, o4 schematic, String error) {
   }

   static final class t {
      final o4 j;
      final o4.h b;
      final int P;
      final long[] d;
      final int[] U;
      final int[] M;
      final int[] z;
      final BlockState[] X;
      final String[] g;
      final boolean[] I;
      final Map<Integer, int[]> p = new HashMap<>();
      final byte[] E;
      final int u;
      final int F;
      final int B;
      final int k;
      final int m;
      final int V;
      int Y;
      final int[] y = new int[qf.s.values().length];
      final int[] S = new int[qf.s.values().length];
      boolean h;
      int[] i = new int[0];
      final PriorityQueue<long[]> s = new PriorityQueue<>((var0, var1x) -> Long.compare(var1x[0], var0[0]));
      Object o;
      private Map<Long, Integer> Z;
      private Map<Long, Integer> A;

      t(o4 var1, o4.h var2) {
         this.j = var1;
         this.b = var2;
         this.P = var1.B();
         this.d = new long[this.P];
         this.U = new int[this.P];
         this.E = new byte[this.P];
         BlockState[] var3 = new BlockState[var1.u().size()];
         this.g = new String[var3.length];
         this.I = new boolean[var3.length];

         for (int var4 = 0; var4 < var3.length; var4++) {
            BlockState var5 = qf.f(var1.u().get(var4));
            var3[var4] = var5 == null ? null : qf.r(var5, var2);
            this.g[var4] = var5 == null ? "unknown block" : qf.N(var3[var4]);
            this.I[var4] = (boolean)(var5 != null && qf.s(var3[var4]) ? 1 : 0);
         }

         this.X = var3;
         int[] var10 = var2.bounds(var1);
         this.u = var10[0];
         this.F = var10[1];
         this.B = var10[2];
         this.k = var10[3];
         this.m = var10[4];
         this.V = var10[5];
         int var11 = this.m - this.F + 1;
         this.z = new int[var11 + 1];

         for (int var6 = 0; var6 < this.P; var6++) {
            int[] var7 = var2.world(var1.j(var6), var1.v(var6), var1.P(var6));
            this.d[var6] = BlockPos.asLong(var7[0], var7[1], var7[2]);
            this.U[var6] = var1.Y(var6);
            this.E[var6] = (byte)qf.s.x.ordinal();
            this.z[var7[1] - this.F + 1]++;
            BlockState var8 = var3[this.U[var6]];
            if (var8 != null && var8.getBlock() instanceof CrafterBlock) {
               int[] var9 = var1.I(var6);
               if (var9.length > 0) {
                  this.p.put(var6, var9);
               }
            }
         }

         for (int var12 = 1; var12 <= var11; var12++) {
            this.z[var12] = this.z[var12] + this.z[var12 - 1];
         }

         this.M = new int[this.P];
         int[] var13 = Arrays.copyOf(this.z, var11);
         int var14 = 0;

         while (var14 < this.P) {
            this.M[var13[BlockPos.getY(this.d[var14]) - this.F]++] = var14++;
         }
      }

      boolean c(int var1, int var2, int var3) {
         if (var1 >= this.u && var1 <= this.k && var2 >= this.F && var2 <= this.m && var3 >= this.B && var3 <= this.V) {
            if (this.Z == null) {
               HashMap var4 = new HashMap(Math.max(16, this.P * 2));

               for (int var5 = 0; var5 < this.P; var5++) {
                  var4.put(this.d[var5], var5);
               }

               this.Z = var4;
            }

            return this.Z.containsKey(BlockPos.asLong(var1, var2, var3));
         } else {
            return false;
         }
      }

      boolean f(int var1, int var2, int var3) {
         int var4 = this.E(var1, var2, var3);
         return var4 >= 0 && this.c(var4) != null && this.g[this.U[var4]] == null;
      }

      int E(int var1, int var2, int var3) {
         return !this.c(var1, var2, var3) ? -1 : this.Z.get(BlockPos.asLong(var1, var2, var3));
      }

      int X(int var1, int var2, int var3) {
         if (this.A == null) {
            HashMap var4 = new HashMap(Math.max(16, this.P * 2));

            for (int var5 = 0; var5 < this.P; var5++) {
               var4.put(hn.f(this.j.j(var5), this.j.v(var5), this.j.P(var5)), var5);
            }

            this.A = var4;
         }

         return this.A.getOrDefault(hn.f(var1, var2, var3), -1);
      }

      BlockState c(int var1) {
         return this.X[this.U[var1]];
      }

      boolean W(int var1) {
         return this.I[this.U[var1]];
      }

      BlockPos s(int var1) {
         return BlockPos.of(this.d[var1]);
      }

      qf.s C(int var1) {
         return qf.s.values()[this.E[var1]];
      }

      int y(qf.s var1) {
         return this.y[var1.ordinal()];
      }

      ol.c l() {
         return new ol.c(this.u, this.F, this.B, this.k, this.m, this.V);
      }
   }

   private enum v {
      N,
      W,
      D;
   }

   enum w {
      v,
      m,
      e;
   }

   enum y {
      c,
      m,
      G,
      D,
      J,
      E,
      e;
   }
}
