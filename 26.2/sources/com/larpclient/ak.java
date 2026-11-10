package com.larpclient;

import com.larp.obf.qa300483f54d804a3.q1881b7c262e73011;
import com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb.qaee82196bfd62460;
import com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213.q3ddfad491c6be35c;
import com.larpclient.module.StaffListModule;
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
import net.minecraft.util.Util;
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

public final class ak extends ar implements s1, t1.s {
   private static ak jt;
   private static boolean Nc;
   static final int EC = 4096;
   static final int Nu = 2048;
   static final int jc = 24;
   static final int vK = 12;
   static final double vf = 0.3;
   private static final int F = 600;
   static final int jC = 3;
   static final int XB = 40;
   static final int j0 = 40;
   static final int vr = 400;
   static final int Ez = 3;
   static final int jh = 2400;
   static final int Xg = 20;
   static final int Y = 8;
   static final int S = 1200;
   static final long jY = 300000L;
   static final int X5 = 3;
   static final long j4 = 5000L;
   static final long vL = 10000L;
   static final long jK = 120000L;
   static final int N3 = 3;
   static final long NL = 3000L;
   private static final int v_ = 0;
   private static final int ED = 1;
   private static final int Ns = 2;
   private static final int jo = 3;
   static final int vS = 16000;
   static final int vZ = 4;
   static final double vu = 32.0;
   static final int ND = -1000000000;
   static final int vq = 10;
   static final int NY = 200000;
   static final float N5 = 2.0F;
   static final int x = 100;
   static final int XW = 6;
   static final int vD = 900;
   static final int J = 60;
   static final int ja = 400;
   static final int vV = 32;
   static final int Nt = 10;
   static final double Xi = 35.0;
   static final int EZ = 8;
   static final int Xb = 600;
   static final int vH = 3;
   static final float b = 1.6F;
   static final float Nw = 6.0F;
   static final float EJ = 26.0F;
   static final int jf = 600;
   static final int EA = 80;
   static final int Xs = 200;
   static final int jI = 24;
   static final int NV = 3;
   static final int vE = 80;
   static final int Nz = 160;
   static final int X1 = 3;
   static final int X_ = 120;
   static final double XF = 0.66;
   static final double vN = 0.54;
   static final double NP = 0.08;
   static final double q = 0.25;
   static final int c = 64;
   static final int jX = 3;
   static final int Xm = 3;
   static final int vc = 120;
   static final int Xu = 2;
   static final int Nl = 1200;
   static final int NU = 200;
   static final int Ek = 400;
   static final int EI = 400;
   static final int y = 2;
   static final int j7 = 2;
   static final int XM = 400;
   static final int X = 3;
   static final double H = 0.6;
   private final com.larpclient.module.g vn = this.addSetting(
      new com.larpclient.module.g("Schematic", "File name in the schematics folder (a .litematic or .schem file). The list at the top picks one", "")
         .F("house.litematic")
   );
   private final com.larpclient.module.g X6 = this.addSetting(
      new com.larpclient.module.g("Origin", "Where the schematic's origin goes, as x y z. The buttons under Set where it goes set it", "").F("x y z")
   );
   private final com.larpclient.module.l Xd = this.addSetting(
      new com.larpclient.module.l("Rotation", "Turn the schematic clockwise around its origin, seen from above", "0", "0", "90", "180", "270")
   );
   private final com.larpclient.module.l XV = this.addSetting(
      new com.larpclient.module.l("Mirror", "Mirror the schematic first: X flips east and west, Z flips north and south", "None", "None", "X", "Z")
   );
   private final com.larpclient.module.s vv = this.U(
      "Auto Move", "Walk to blocks that are out of reach. Off: only blocks within reach of where you stand", true
   );
   private final com.larpclient.module.s Nf = this.U("Sprint", "Sprint on longer walks", true);
   private final com.larpclient.module.s En = this.U("Safe Sneak", "Crouch next to drops so the bot never walks off an edge while it places", true);
   private final com.larpclient.module.s j1 = this.U(
      "Refill Hotbar",
      "Move the block it needs from your inventory into the hotbar, standing still. Only empty slots and blocks the build no longer needs are replaced",
      true
   );
   private final com.larpclient.module.e m = this.Z("Reach", "How far blocks are placed from, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final com.larpclient.module.e Xf = this.Z("Place Delay", "Ticks between two placements (20 ticks are one second)", 3.0, 1.0, 20.0, 1.0);
   private final com.larpclient.module.s vp = this.U("Auto Eat", "Eat food from your hotbar at 10 hunger or less", true);
   private final com.larpclient.module.s e = this.U(
      "Show Preview", "Draw the schematic in the world: missing blocks in the theme color, wrong blocks red, wrongly turned blocks orange", true
   );
   private final com.larpclient.module.s j6 = this.U("Show Correct", "Also draw the blocks that are already right, in green", false);
   private final com.larpclient.module.e jT = this.Z("Preview Range", "How far from you the preview draws blocks", 48.0, 8.0, 160.0, 8.0);
   private final com.larpclient.module.s jk = this.U("Show Path", "Draw the route the builder walks and the block it places next", true);
   private final com.larpclient.module.s B = this.U("Status Card", "Show a card at the top of the screen with the progress and what the builder is doing", true);
   private final com.larpclient.module.s v2 = this.U("Notify When Done", "Show a notification when the build is finished", true);
   private final com.larpclient.module.s vg = this.U(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final com.larpclient.module.s v3 = this.U(
      "Temporary Pillars", "Pillar up on throwaway blocks to place blocks that are too high to reach from the ground, then dig the pillar away", true
   );
   private final com.larpclient.module.e g = this.Z("Max Pillar Height", "Tallest temporary pillar, in blocks", 6.0, 1.0, 64.0, 1.0);
   private final com.larpclient.module.l t = this.addSetting(
      new com.larpclient.module.l(
         "Pillar Block",
         "Throwaway block the pillars are built of. Never one the schematic uses",
         "Dirt",
         "Dirt",
         "Cobblestone",
         "Netherrack",
         "Cobbled Deepslate"
      )
   );
   private final com.larpclient.module.s NF = this.U(
      "Restock From Containers", "When a block runs out, walk to a chest, barrel or shulker box you marked and take more from it", true
   );
   private final com.larpclient.module.s W = this.U(
      "Put Back Spares", "On a restock trip, put blocks the build no longer needs into the container, so the marked containers are the build's stockpile", true
   );
   private final com.larpclient.module.v j3 = this.addSetting(
      new com.larpclient.module.v(
         "Mark Restock Key", "Look at a chest, barrel or shulker box and press this to mark it as a restock container, or to unmark it", -1
      )
   );
   private final com.larpclient.module.g C = this.addSetting(
      new com.larpclient.module.g(
            "Restock Containers", "Marked containers, as dimension x y z, separated by commas. The Mark Restock Key and the Restock buttons set them", ""
         )
         .F("minecraft:overworld x y z")
   );
   private final com.larpclient.module.e jQ = this.Z(
      "Restock Distance", "Farthest a marked container may be for the builder to walk to it", 64.0, 8.0, 256.0, 8.0
   );
   private final com.larpclient.module.s Np = this.U("Show Restock Containers", "Outline the marked containers in the world", true);
   private final com.larpclient.module.s Ed = this.U(
      "Stop On Staff Nearby", "Stop building and log off when someone Staff List counts as staff comes within range, invisible ones included", true
   );
   private final com.larpclient.module.e XX = this.Z("Staff Range", "How close staff may come before the builder stops, in blocks", 48.0, 8.0, 128.0, 4.0);
   private final com.larpclient.module.e Nv = this.Z("Log Off Delay", "Seconds between stopping and logging off", 5.0, 0.0, 30.0, 0.5);
   private final com.larpclient.module.g V = this.addSetting(
      new com.larpclient.module.g(
         "Log Off Message", "Shown on the disconnect screen when staff nearby makes the builder log off", "Auto Builder stopped: staff nearby"
      )
   );
   private final com.larpclient.module.s Nm = this.U(
      "Buy Missing Blocks",
      "When a block runs out and no marked container has it, buy it from /ah on DonutSMP, then go on building. Spends your in-game money",
      false
   );
   private final com.larpclient.module.e vY = this.Z(
         "Max Price Per Item", "Most it pays for one block, whatever the size of the listing. Type amounts like 50, 2K or 1.5M", 100.0, 1.0, 1.0E7, 1.0
      )
      .h();
   private final com.larpclient.module.e jq = this.Z(
         "Max Price Per Buy", "Most it pays for one listing. Type amounts like 10K, 5M or 1B", 10000.0, 100.0, 1.0E10, 100.0
      )
      .h();
   private final com.larpclient.module.e N7 = this.Z(
         "Max Total Spend", "Most it spends in one run, all purchases together. Type amounts like 100K, 10M or 1.5B", 100000.0, 1000.0, 1.0E12, 1000.0
      )
      .h();
   private final com.larpclient.module.l Ny = this.addSetting(
      new com.larpclient.module.l(
         "Placement Mode",
         "Normal: your camera turns to every block with the builder, like a player's. Aggressive: while it places, your camera stays where you look and only the look sent to the server turns, easing back to your look afterwards. Walking still turns your camera",
         "Normal",
         "Normal",
         "Aggressive"
      )
   );
   private final com.larpclient.module.s NN = this.U(
      "Follow Recording",
      "Build in the order you placed the blocks when you recorded this schematic with the Build Recorder. Ignored when it has no recording",
      true
   );
   private final com.larpclient.module.s jW = this.U(
      "Relog On Ghost Blocks",
      "When the server keeps taking placed blocks back (8 in a minute), log off, rejoin the same server and carry on building. At most once every 5 minutes and 3 times a build, never while temporary blocks stand",
      false
   );
   private static final String X9 = "No schematic";
   private int Ef;
   private final com.larpclient.module.l A = new com.larpclient.module.l(
      "File", "A .litematic or .schem file from the schematics folder. New files appear here by themselves", "No schematic", "No schematic"
   ) {
      public String getValue() {
         String var1 = ak.this.vn.getValue().trim();
         return !var1.isEmpty() && !ak.this.XN ? var1 : "No schematic";
      }

      @Override
      public boolean acceptsValue(String var1) {
         return true;
      }

      @Override
      public void setValue(String var1) {
         ak.this.K(var1);
      }

      @Override
      public boolean isSearchable() {
         return this.a().size() > 8;
      }

      @Override
      public String getModeLabel(String var1) {
         return var1.replaceFirst("(?i)\\.(litematic|schem|schematic)$", "");
      }
   };
   private volatile ak.m jJ;
   private volatile boolean vi;
   private int NT;
   private String jM = "";
   private cb vI;
   private String v8;
   private ak.t Eo;
   private boolean vW = (boolean)1;
   private boolean XN;
   private boolean jE;
   private Object E2;
   private Object vM;
   private String NW;
   private String NX;
   private final hc XP = new hc(hc.h.QUICK);
   private final hk XH = new hk(this);
   private final gu j_ = new gu(this, this.XP, this::v);
   private final Map<Integer, Integer> vs = new HashMap<>();
   private final Map<Integer, Integer> vG = new HashMap<>();
   private final Map<Integer, Integer> XJ = new HashMap<>();
   private final Set<Integer> ve = new HashSet<>();
   private final Set<Integer> I = new HashSet<>();
   private final Map<Integer, String> jr = new HashMap<>();
   private final Map<Item, Integer> Xj = new LinkedHashMap<>();
   private s_.k v1;
   private s_.k vX;
   private int X0 = -1000000000;
   private ak.n Xo = ak.n.B;
   private BlockState Q;
   private final Map<Integer, ak.n> vJ = new HashMap<>();
   private final Set<Integer> je = new HashSet<>();
   private Integer j2;
   private int X4;
   private int v7 = -1000000000;
   private int E9 = -1000000000;
   private int XR;
   private int XS;
   private final Map<Integer, BlockState> NQ = new HashMap<>();
   private s_.a p;
   private ak.l XZ;
   private final List<BlockPos> vB = new ArrayList<>();
   private final Set<Long> w = new HashSet<>();
   private int jV;
   private int Nk;
   private int Ea;
   private int Xy;
   private s_.z vQ;
   private ak.y Xv;
   private final List<BlockPos> vT = new ArrayList<>();
   private final Set<Long> vP = new HashSet<>();
   private final Map<Integer, Integer> jm = new HashMap<>();
   private int XL;
   private int jG;
   private int XK = -1000000000;
   private int X3;
   private int Ng;
   private int v6;
   private final Set<Long> XA = new HashSet<>();
   private BlockPos jx;
   private int u;
   private int jU;
   private final Map<Integer, Integer> Nq = new HashMap<>();
   private final Set<Integer> XU = new HashSet<>();
   private int jR;
   private long jD = Long.MIN_VALUE;
   private long jn = Long.MIN_VALUE;
   private boolean Xr;
   private int NC;
   private int NM;
   private final List<BlockPos> NR = new ArrayList<>();
   private final Set<Long> El = new HashSet<>();
   private boolean Xk;
   private int js;
   private int r = -1000000000;
   private final SplittableRandom NB = new SplittableRandom();
   private int NS;
   private int jv = -1000000000;
   private int N0 = -1000000000;
   private double vk = 0.6;
   private double X2;
   private boolean jd;
   private boolean L;
   private float Xt = Float.NaN;
   private float jp;
   private float v4;
   private int Xx;
   private int N6;
   private int ju;
   private final List<BlockPos> i = new ArrayList<>();
   private boolean XG;
   private final Set<BlockPos> Xn = new HashSet<>();
   private final Set<Integer> j9 = new HashSet<>();
   private List<s_.j> vh;
   private final Set<Integer> EU = new HashSet<>();
   private final Set<Integer> NK = new HashSet<>();
   private s_.j R;
   private s_.j X7;
   private s_.a O;
   private final List<BlockPos> Ni = new ArrayList<>();
   private int EP;
   private int f;
   private int N_ = -1;
   private BlockPos a;
   private int Nd;
   private int va = -1000000000;
   private int vx;
   private int No;
   private int Nb;
   private int Nr;
   private Direction E;
   private BlockPos N4;
   private int Xw;
   private final List<List<s_.k>> vo = new ArrayList<>();
   private int G;
   private int jg;
   private ak.k N8;
   private ak.f Xq;
   private final Set<ak.f> vj = new HashSet<>();
   private final Map<Item, Integer> Na = new HashMap<>();
   private final Set<Integer> Eh = new HashSet<>();
   private int T;
   private int vz;
   private int Z;
   private int j8;
   private int N2 = -1000000000;
   private int jB = Integer.MAX_VALUE;
   private int Eq;
   private boolean D;
   private String NH;
   private final t6 ji = new t6("Auto Builder");
   private Item vy;
   private long E5;
   private int j5;
   private int Et;
   private boolean XQ;
   private final Set<Item> Nj = new HashSet<>();
   static final long NJ = 10000L;
   static final long jA = 2000L;
   private boolean vF;
   private boolean jL;
   private boolean vU;
   private long U;
   private long XI;
   private ak.d h;
   private final List<ak.f> NA = new ArrayList<>();
   private final Map<ak.f, Map<Item, Integer>> Xc = new HashMap<>();
   private final Map<ak.f, Integer> NO = new HashMap<>();
   private final Set<ak.f> Nh = new HashSet<>();
   private final Set<ak.f> j = new HashSet<>();
   private final Set<ak.f> jN = new HashSet<>();
   private final Map<Item, Integer> vA = new LinkedHashMap<>();
   private final Map<Item, Integer> vC = new LinkedHashMap<>();
   private final Map<Item, Integer> Ne = new LinkedHashMap<>();
   private final Map<Item, Integer> z = new LinkedHashMap<>();
   private final Map<Item, Long> jS = new LinkedHashMap<>();
   private int jZ;
   private final lm Xe = new lm();
   private boolean jF;
   private final List<Integer> jb = new ArrayList<>();
   private final Set<Integer> NG = new HashSet<>();
   private BitSet EE;
   private int[] NE;
   private ak.t NZ;
   private String XE;
   private int vw = -1000000000;
   private final ArrayDeque<Integer> XC = new ArrayDeque<>();
   private int o = 0;
   private int XO;
   private int Nx;
   private ServerData vt;
   private long Xh;
   private long vl;
   private long P;
   private long X8;
   private boolean n;
   private boolean vd;
   private BlockHitResult XD;
   private int XY;
   private int v0 = -1000000000;
   private int Xa = -1;
   private int vO;
   private int jO = -1000000000;
   private int Nn = -1000000000;
   private int v5;
   private int vm;
   private int jz = -1000000000;
   private int jy;
   private int M;
   private final s_.m jP = new s_.m();
   private BlockPos XT;
   private final Map<Integer, Integer> NI = new HashMap<>();
   private List<s_.k> v9 = List.of();
   private BlockPos vR;
   private long N9;
   private int jH;
   private String Xp = "";
   private Vec3 Xl = Vec3.ZERO;
   private boolean N1;
   private boolean jl;
   private boolean jw;
   private int jj = Integer.MIN_VALUE;
   private String s = "";
   private boolean d;
   private final Map<Item, String> vb = new LinkedHashMap<>();
   private static final Pattern Xz = Pattern.compile("(?i)\\.(litematic|schematic|schem)$");

   static List<ak.f> s(String var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         return var1;
      }

      for (String var5 : var0.split(",")) {
         String[] var6 = var5.trim().split(" +");
         if (var6.length == 4 && !var6[0].isEmpty()) {
            try {
               ak.f var7 = new ak.f(var6[0], Integer.parseInt(var6[1]), Integer.parseInt(var6[2]), Integer.parseInt(var6[3]));
               if (!var1.contains(var7)) {
                  var1.add(var7);
               }
            } catch (NumberFormatException var8) {
            }
         }
      }

      return var1;
   }

   static String g(List<ak.f> var0) {
      ArrayList var1 = new ArrayList();

      for (ak.f var3 : var0) {
         var1.add(var3.toString());
      }

      return String.join(", ", var1);
   }

   public ak() {
      super("Auto Builder", "Build a .litematic or .schem schematic block by block, walking to what is out of reach; or mine it out");
      jt = this;
      this.Xf.h("Ticks Between Blocks");
      this.vg.h("Pause In Other Menus");
      this.vn.h("Schematic (file name)");
      this.X6.h("Origin (x y z)");
      this.Xd.h("Rotation");
      this.C.h("Restock Containers");
      this.XX.P(this.Ed::getValue);
      this.Nv.P(this.Ed::getValue);
      this.V.P(this.Ed::getValue);
      this.W.P(this.NF::getValue);
      this.jQ.P(this.NF::getValue);
      this.g.P(this.v3::getValue);
      this.t.P(this.v3::getValue);
      this.j6.P(this.e::getValue);
      this.jT.P(this.e::getValue);
      this.vn.o(var1 -> this.B());
      this.X6.o(var1 -> this.vW = (boolean)1);
      this.Xd.o(var1 -> this.vW = (boolean)1);
      this.XV.o(var1 -> this.vW = (boolean)1);
      if (!Nc) {
         Nc = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            ak var1 = jt;
            if (var1 != null) {
               var1.Q();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            ak var2 = jt;
            if (!var1 && var2 != null && var2.ji.G()) {
               t6.g(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            ak var1 = jt;
            if (var1 != null) {
               var1.D();
               var1.w();
            }
         });
      }
   }

   public static ak g() {
      return jt;
   }

   public static Path u() {
      return FabricLoader.getInstance().getGameDir().resolve("schematics");
   }

   @Override
   public void disable() {
      this.NW = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      Screen var1 = getMc() == null ? null : mc.gui.screen();
      return var1 == null || var1 instanceof ChatScreen || var1 instanceof PauseScreen || !this.vg.getValue();
   }

   @Override
   public List<s1.a> commands() {
      return List.of(
         new s1.a("Schematic", "Next schematic", () -> this.z(1)),
         new s1.a("Schematic", "Previous schematic", () -> this.z(-1)),
         new s1.a("Schematic", "Reload it", this::K),
         new s1.a("Schematic", "Unload schematic", this::N),
         new s1.a("Schematic", "Open the schematics folder", this::w),
         new s1.a("Where it goes", "Origin at my feet", this::b),
         new s1.a("Where it goes", "Origin on the block I look at", this::J),
         new s1.a("Where it goes", "Turn it 90 degrees", this::r),
         new s1.a("Move it", "East (+X)", () -> this.R(1, 0, 0)),
         new s1.a("Move it", "West (-X)", () -> this.R(-1, 0, 0)),
         new s1.a("Move it", "Up", () -> this.R(0, 1, 0)),
         new s1.a("Move it", "Down", () -> this.R(0, -1, 0)),
         new s1.a("Move it", "South (+Z)", () -> this.R(0, 0, 1)),
         new s1.a("Move it", "North (-Z)", () -> this.R(0, 0, -1)),
         new s1.a("Restock", "Mark the container I look at", this::C),
         new s1.a("Restock", "Clear the restock marks", this::z),
         new s1.a("Restock", "Stock the materials", this::n),
         new s1.a("Build Recorder", "Start recording", this::B),
         new s1.a("Build Recorder", "Stop and save the recording", this::T),
         new s1.a("Build Recorder", "Delete the recording", this::P),
         new s1.a("Build", "List the materials", this::D),
         new s1.a("Build", "Mine it out instead", this::f)
      );
   }

   @Override
   public String summary() {
      String var1 = this.vn.getValue().trim();
      if (var1.isEmpty()) {
         return "Put .litematic or .schem files in the schematics folder and press Next schematic.";
      }

      if (this.XN) {
         return "No schematic loaded. Next schematic or Reload it loads " + var1 + " or another one again.";
      }

      if (this.vi) {
         return "Loading " + var1 + "...";
      }

      if (this.v8 != null) {
         return var1 + ": " + this.v8 + ".";
      }

      if (this.vI == null) {
         return "Loading " + var1 + "...";
      }

      String var2 = this.vI.o() + ": " + this.vI.w() + " x " + this.vI.K() + " x " + this.vI.N() + ", " + this.vI.h() + " blocks";
      cb.b var3 = this.p();
      if (var3 == null) {
         return var2 + ". Set the origin to place it.";
      }

      String var4 = " at " + var3.origin() + this.Y();
      if (this.jE && this.isRuntimeActive()) {
         return var2 + var4 + ". " + this.S() + "." + this.U();
      }

      ak.t var5 = this.Eo;
      return var5 != null && var5.s && this.isInGame()
         ? var2
            + var4
            + ". "
            + var5.K(b_.i.o)
            + " missing, "
            + (var5.K(b_.i.y) + var5.K(b_.i.t))
            + " wrong, "
            + var5.K(b_.i.j)
            + " done"
            + (this.NX != null ? ". " + this.NX : "")
            + "."
            + this.U()
         : var2 + var4 + "." + this.U();
   }

   private String Y() {
      String var1 = this.Xd.getValue().equals("0") ? "" : ", turned " + this.Xd.getValue();
      return this.XV.getValue().equals("None") ? var1 : var1 + ", mirrored " + this.XV.getValue();
   }

   @Override
   public String detail() {
      ak.t var1 = this.Eo;
      if (var1 != null && var1.s && this.isInGame()) {
         Map var2 = this.h(var1);
         if (var2.isEmpty()) {
            return var1.K(b_.i.o) > 0 ? "Missing blocks cannot be placed yet; List the materials says why." : "";
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

            int var7 = this.E((Item)var6.getKey());
            var3.append(var6.getValue())
               .append(' ')
               .append(e((Item)var6.getKey()))
               .append(var7 >= var6.getValue() ? "" : " (" + (var7 == 0 ? "none" : var7) + " on you)");
         }

         return var3.append('.').toString();
      } else {
         return "";
      }
   }

   @Override
   public List<s1.w> settingGroups() {
      BooleanSupplier var1 = () -> this.p() != null;
      return List.of(
         new s1.w("Schematic", List.of(this.A))
            .with(
               new s1.r(
                  this::L,
                  List.of(
                     List.of(
                        this.T("Reload", "Reload it").availableWhen(() -> !this.vn.getValue().isBlank()),
                        this.T("Unload", "Unload schematic"),
                        this.T("Open folder", "Open the schematics folder")
                     )
                  ),
                  this::preview,
                  true
               )
            ),
         new s1.w("Placement", List.of(this.Xd, this.XV))
            .with(
               new s1.r(
                  this::c,
                  List.of(
                     List.of(this.T("At my feet", "Origin at my feet"), this.T("Block I look at", "Origin on the block I look at")),
                     List.of(
                        this.T("West", "West (-X)").availableWhen(var1),
                        this.T("East", "East (+X)").availableWhen(var1),
                        this.T("North", "North (-Z)").availableWhen(var1),
                        this.T("South", "South (+Z)").availableWhen(var1)
                     ),
                     List.of(this.T("Up", "Up").availableWhen(var1), this.T("Down", "Down").availableWhen(var1))
                  )
               )
            ),
         new s1.w("Blocks", List.of(this.j1)).with(new s1.r(this::I, List.of(List.of(this.T("List in chat", "List the materials"))))),
         new s1.w("While building", List.of(this.Ny, this.NN, this.vv, this.Nf, this.En, this.vp, this.v3)),
         new s1.w("Material chests", List.of(this.NF, this.W, this.j3))
            .with(
               new s1.r(
                  this::a,
                  List.of(
                     List.of(this.T("Select chest", "Mark the container I look at"), this.T("Clear chests", "Clear the restock marks")),
                     List.of(this.T("Stock materials", "Stock the materials"))
                  )
               )
            ),
         new s1.w("Buying on DonutSMP", List.of(this.Nm, this.vY, this.jq, this.N7)),
         new s1.w("Safety", List.of(this.Ed, this.XX, this.jW)),
         new s1.w("Display", List.of(this.e, this.j6, this.jk, this.B, this.v2, this.Np)),
         s1.w.advanced("Advanced", List.of(this.vn, this.X6, this.m, this.Xf, this.jT, this.vg, this.g, this.t, this.C, this.jQ, this.Nv, this.V))
            .with(
               new s1.r(
                  () -> "",
                  List.of(
                     List.of(
                        this.T("Record build", "Start recording"),
                        this.T("Save recording", "Stop and save the recording"),
                        this.T("Delete recording", "Delete the recording"),
                        this.T("Mine it out", "Mine it out instead")
                     )
                  )
               )
            )
      );
   }

   private String L() {
      String var1 = this.vn.getValue().trim();
      if (this.A.a().size() <= 1 && var1.isEmpty()) {
         return "The schematics folder is empty.";
      }

      if (!var1.isEmpty() && !this.XN) {
         if (!this.vi && (this.vI != null || this.v8 != null)) {
            return this.v8 != null
               ? "Could not load " + var1 + ": " + this.v8 + "."
               : this.vI.w() + " x " + this.vI.K() + " x " + this.vI.N() + ", " + String.format(Locale.ROOT, "%,d", this.vI.h()) + " blocks";
         } else {
            return "Loading.";
         }
      } else {
         return "";
      }
   }

   private String c() {
      cb.b var1 = this.p();
      return var1 == null ? "Not set." : "Origin at " + var1.origin() + this.Y();
   }

   private int s(ak.t var1) {
      int var2 = 0;

      for (Entry var4 : this.h(var1).entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.E((Item)var4.getKey()));
      }

      return var2;
   }

   private String I() {
      ak.t var1 = this.Eo;
      if (var1 != null && this.isInGame() && var1.s) {
         String var2 = this.detail();
         return var2.isEmpty() ? "Nothing more is needed." : var2;
      } else {
         return "";
      }
   }

   @Override
   public void panelTick() {
      if (this.Ef-- <= 0) {
         this.Ef = 40;
         ArrayList var1 = new ArrayList();
         var1.add("No schematic");
         var1.addAll(cb.P(u()));
         this.A.G(var1);
      }
   }

   @Override
   public s1.o status() {
      if (this.jE && this.isRuntimeActive()) {
         return new s1.o(this.S() + ".", false);
      }

      String var1 = this.vn.getValue().trim();
      if (var1.isEmpty() || this.XN) {
         return new s1.o("Choose a schematic, then set where it goes.", false);
      }

      if (this.v8 != null) {
         return new s1.o("Could not load " + var1 + ": " + this.v8 + ".", true);
      }

      if (this.vi || this.vI == null) {
         return new s1.o("Loading " + var1 + ".", false);
      }

      if (this.p() == null) {
         return new s1.o("Set where it goes, then turn the module on.", false);
      }

      if (!this.isInGame()) {
         return new s1.o("Schematic set. Join the world it goes in.", false);
      }

      ak.t var2 = this.Eo;
      if (var2 != null && var2.s) {
         String var3 = s1.U(this);
         if (var3 != null) {
            return new s1.o(var3 + " is running. One bot runs at a time.", true);
         }

         if (this.jF) {
            return new s1.o("Build Recorder is recording. Stop and save it first.", true);
         }

         BlockPos var4 = mc.player.blockPosition();
         int var5 = Math.max(Math.max(var2.E - var4.getX(), var4.getX() - var2.m), Math.max(var2.v - var4.getZ(), var4.getZ() - var2.k));
         if (var5 > 32.0) {
            return new s1.o("You are " + var5 + " blocks from the build. Walk next to it.", true);
         }

         int var6 = var2.K(b_.i.o);
         int var7 = var2.K(b_.i.y) + var2.K(b_.i.t);
         if (var6 == 0 && var7 == 0) {
            return new s1.o("Every block already matches the schematic.", false);
         }

         String var8 = String.format(Locale.ROOT, "Will place %,d blocks", var6) + (var7 > 0 ? ", " + var7 + " wrong ones are left alone" : "") + ".";
         int var9 = this.s(var2);
         if (var9 == 0) {
            return new s1.o(var8, false);
         }

         int var10 = 0;

         for (ak.f var12 : s(this.C.getValue())) {
            if (var12.dimension().equals(this.W())) {
               var10++;
            }
         }

         int var13 = (!this.NF.getValue() || var10 <= 0) && !this.Nm.getValue() ? 0 : 1;
         return new s1.o(var8 + String.format(Locale.ROOT, " You are short of %,d.", var9), var13 == 0);
      } else {
         return new s1.o("Checking the world.", false);
      }
   }

   private String z(int var1) {
      List var2 = cb.P(u());
      if (var2.isEmpty()) {
         return "No schematics in " + u() + ". Put .litematic or .schem files there.";
      }

      int var3 = var2.indexOf(this.vn.getValue().trim());
      int var4 = var3 < 0 ? (var1 > 0 ? 0 : var2.size() - 1) : Math.floorMod(var3 + var1, var2.size());
      if (this.jE) {
         return "Turn the builder off before picking another schematic.";
      }

      this.r((String)var2.get(var4));
      return "Picked " + (String)var2.get(var4) + " (" + (var4 + 1) + " of " + var2.size() + ").";
   }

   private void r(String var1) {
      boolean var2 = var1.equals(this.vn.getValue().trim());
      this.vn.k(var1);
      if (var2 && this.XN) {
         this.B();
      }

      this.v();
   }

   private void K(String var1) {
      if ("No schematic".equals(var1)) {
         this.N();
      } else if (this.jE) {
         this.m("turn the builder off before picking another schematic.");
      } else {
         this.r(var1);
      }
   }

   private String K() {
      if (this.vn.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      if (this.jE) {
         return "Turn the builder off before reloading.";
      }

      this.B();
      return "Reloading " + this.vn.getValue().trim() + ".";
   }

   private String N() {
      if (this.jE) {
         this.m("turn the builder off before unloading the schematic.");
         return "Turn the builder off before unloading the schematic.";
      }

      boolean var1 = ae.G("the schematic was unloaded");
      if (this.XN || this.vI == null && !this.vi && this.v8 == null) {
         if (!var1) {
            this.m("no schematic is loaded.");
         }

         return var1 ? "Stopped Mine Out; no schematic was loaded." : "No schematic is loaded.";
      } else {
         String var2 = this.vI != null ? this.vI.o() : this.vn.getValue().trim();
         if (this.jF) {
            this.P("the schematic was unloaded");
         }

         this.NT++;
         this.jJ = null;
         this.vi = (boolean)0;
         this.vI = null;
         this.v8 = null;
         this.Eo = null;
         this.vW = (boolean)0;
         this.NE = null;
         this.NZ = null;
         this.NX = null;
         this.XN = (boolean)1;
         com.larpclient.h.A(this.getName(), "Unloaded {}{}; settings kept", var2, var1 ? " after stopping its Mine Out" : "");
         this.m(
            "unloaded "
               + var2
               + (var1 ? " and stopped mining it out" : "")
               + ". Your settings are kept; pick one in the list or press Reload to load one again."
         );
         return "Unloaded " + var2 + (var1 ? ", Mine Out stopped." : ".");
      }
   }

   private String w() {
      Path var1 = u();

      try {
         Files.createDirectories(var1);
      } catch (IOException var3) {
         return "Could not create " + var1 + ": " + var3.getMessage();
      }

      Util.getPlatform().openPath(var1);
      return "Opened " + var1 + ". Put .litematic or .schem files there, then choose one in the list.";
   }

   private String b() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.jE) {
         return "Turn the builder off before moving the schematic.";
      }

      BlockPos var1 = mc.player.blockPosition();
      return this.K(var1.getX(), var1.getY(), var1.getZ());
   }

   private String J() {
      if (!this.isInGame()) {
         return "Join a world first.";
      } else if (this.jE) {
         return "Turn the builder off before moving the schematic.";
      } else if (mc.hitResult instanceof BlockHitResult var1 && var1.getType() == Type.BLOCK) {
         BlockPos var3 = var1.getBlockPos().relative(var1.getDirection());
         return this.K(var3.getX(), var3.getY(), var3.getZ());
      } else {
         return "Look at a block first.";
      }
   }

   private String K(int var1, int var2, int var3) {
      this.X6.k(u1.j(var1, var2, var3));
      this.v();
      return "Origin set to " + var1 + " " + var2 + " " + var3 + ".";
   }

   private String r() {
      if (this.jE) {
         return "Turn the builder off before turning the schematic.";
      }

      this.Xd.U();
      this.v();
      return "Turned to " + this.Xd.getValue() + " degrees.";
   }

   private String R(int var1, int var2, int var3) {
      if (this.jE) {
         return "Turn the builder off before moving the schematic.";
      }

      int[] var4 = u1.K(this.X6.getValue());
      return var4 == null ? "Set the origin first." : this.K(var4[0] + var1, var4[1] + var2, var4[2] + var3);
   }

   private String D() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ak.t var1 = this.Eo;
      if (var1 == null) {
         return this.vI == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var1.s) {
         return "Still checking the world; try again in a moment.";
      }

      Map var2 = this.h(var1);
      this.m("materials for " + this.vI.o() + " (" + var1.K(b_.i.o) + " missing, " + var1.K(b_.i.y) + " wrong blocks, " + var1.K(b_.i.t) + " turned wrong):");

      for (Entry var4 : var2.entrySet()) {
         int var5 = this.E((Item)var4.getKey());
         this.m(
            "  "
               + var4.getValue()
               + " "
               + e((Item)var4.getKey())
               + ", you carry "
               + var5
               + (var5 >= var4.getValue() ? "" : ", short by " + ((Integer)var4.getValue() - var5))
         );
      }

      TreeMap var6 = new TreeMap();

      for (int var7 = 0; var7 < var1.U; var7++) {
         String var9 = var1.P[var1.y[var7]];
         if (var9 != null && var1.i(var7) != b_.i.j) {
            var6.merge(cb.g(this.vI.r().get(var1.y[var7])).replace("minecraft:", "") + " (" + var9 + ")", 1, Integer::sum);
         }
      }

      for (Entry var10 : var6.entrySet()) {
         this.m("  left out: " + var10.getValue() + " " + (String)var10.getKey());
      }

      return var2.isEmpty() && var6.isEmpty() ? "Nothing is missing." : "Listed " + (var2.size() + var6.size()) + " materials in chat.";
   }

   private String f() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (!this.jE && !this.isRuntimeActive()) {
         ak.t var1 = this.Eo;
         if (var1 == null) {
            return "Load a schematic and set its origin first.";
         }

         ArrayList var2 = new ArrayList(var1.U);

         for (int var3 = 0; var3 < var1.U; var3++) {
            var2.add(var1.S(var3));
         }

         String var4 = ae.p(var2, "the " + this.vI.o() + " schematic");
         if (var4 != null) {
            return var4;
         }

         com.larpclient.h.A(this.getName(), "Mine Out: handed {} cells of {} at {} to Auto Area Miner", var2.size(), this.vI.o(), var1.h.origin());
         return "Auto Area Miner is mining out " + this.vI.o() + ".";
      } else {
         return "Turn the builder off first; one bot runs at a time.";
      }
   }

   private Map<Item, Integer> h(ak.t var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.U; var3++) {
         if (var1.i(var3) == b_.i.o && var1.P[var1.y[var3]] == null && !var1.v(var3)) {
            BlockState var4 = var1.g(var3);
            BlockItem var5 = b_.B(var4);
            int var6 = b_.d(var4) && this.isInGame() ? b_.c(var4, mc.level.getBlockState(var1.S(var3))) : 1;
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

   private static String e(Item var0) {
      return new ItemStack(var0).getHoverName().getString();
   }

   private int E(Item var1) {
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

   private void v() {
      LarpClient var1 = LarpClient.R();
      if (var1 != null && var1.o() != null) {
         var1.o().C();
      }
   }

   private void B() {
      this.XN = (boolean)0;
      int var1 = ++this.NT;
      String var2 = this.vn.getValue().trim();
      this.vI = null;
      this.v8 = null;
      this.Eo = null;
      this.vW = (boolean)1;
      this.jM = var2;
      if (var2.isEmpty()) {
         this.vi = (boolean)0;
      } else if (!var2.contains("/") && !var2.contains("\\") && !var2.contains("..")) {
         this.vi = (boolean)1;
         Path var3 = u().resolve(var2);
         Thread var4 = new Thread(() -> {
            ak.m var4x;
            try {
               if (!Files.isRegularFile(var3)) {
                  var4x = new ak.m(var2, null, "no such file in the schematics folder");
               } else {
                  var4x = new ak.m(var2, cb.f(var3), null);
               }
            } catch (IOException | RuntimeException var6) {
               var4x = new ak.m(var2, null, var6.getMessage() == null ? var6.getClass().getSimpleName() : var6.getMessage());
            }

            if (var1 == this.NT) {
               this.jJ = var4x;
            }
         }, "Larp-SchematicLoad");
         var4.setDaemon(true);
         var4.start();
      } else {
         this.v8 = "only a file name inside the schematics folder works";
      }
   }

   private void D() {
      this.i();
      if (this.XN) {
         if (this.jF) {
            this.P("the schematic was unloaded");
         }
      } else {
         ak.m var1 = this.jJ;
         if (var1 != null) {
            this.jJ = null;
            this.vi = (boolean)0;
            if (var1.file().equals(this.jM)) {
               this.vI = var1.schematic();
               this.v8 = var1.error();
               this.vW = (boolean)1;
               if (this.vI != null) {
                  com.larpclient.h.A(
                     this.getName(),
                     "Loaded {} ({}): {}, {} regions, {} palette entries",
                     var1.file(),
                     this.vI.A(),
                     this.vI.e(),
                     this.vI.G().size(),
                     this.vI.r().size()
                  );
               } else {
                  com.larpclient.h.A(this.getName(), "Could not load {}: {}", var1.file(), this.v8);
               }
            }
         }

         if (this.vI == null && !this.vi && this.v8 == null && !this.vn.getValue().isBlank()) {
            this.B();
         } else {
            if (this.vW) {
               this.vW = (boolean)0;
               cb.b var2 = this.p();
               this.Eo = this.vI != null && var2 != null ? new ak.t(this.vI, var2) : null;
               if (this.Eo != null) {
                  com.larpclient.h.U(
                     this.getName(),
                     "Layout at {}{}: box {} to {}",
                     var2.origin(),
                     this.Y(),
                     this.Eo.E + " " + this.Eo.S + " " + this.Eo.v,
                     this.Eo.m + " " + this.Eo.J + " " + this.Eo.k
                  );
               }
            }

            ak.t var3 = this.Eo;
            if (var3 != this.NZ) {
               if (this.jF) {
                  this.P("the schematic or its origin changed");
               }

               this.Y(var3);
            }

            if (var3 != null && this.isInGame()) {
               this.I(var3);
               if (var3.D != mc.level) {
                  var3.D = mc.level;
                  var3.M = 0;
                  var3.s = (boolean)0;
                  Arrays.fill(var3.X, 0);
                  var3.f.clear();
               }

               this.s(var3, 4096);
            } else {
               if (this.jF) {
                  this.P("the schematic or the world changed");
               }
            }
         }
      }
   }

   private cb.b p() {
      int[] var1 = u1.K(this.X6.getValue());
      if (var1 == null) {
         return null;
      }

      int var2;
      try {
         var2 = Integer.parseInt(this.Xd.getValue());
      } catch (NumberFormatException var6) {
         var2 = 0;
      }
      cb.g var3 = switch ((String)this.XV.getValue()) {
         case "X" -> cb.g.L;
         case "Z" -> cb.g.r;
         default -> cb.g.A;
      };
      return new cb.b(var1[0], var1[1], var1[2], var2, var3);
   }

   private void s(ak.t var1, int var2) {
      if (var1.U != 0) {
         MutableBlockPos var3 = new MutableBlockPos();
         double var4 = this.jT.getValue();
         double var6 = mc.player.getX();
         double var8 = mc.player.getY();
         double var10 = mc.player.getZ();

         for (int var12 = 0; var12 < var2; var12++) {
            int var13 = var1.M;
            b_.i var14 = this.i(var1, var13, var3);
            var1.X[var14.ordinal()]++;
            if (var14 != b_.i.V && var1.g(var13) != null) {
               double var15 = var3.getX() + 0.5 - var6;
               double var17 = var3.getY() + 0.5 - var8;
               double var19 = var3.getZ() + 0.5 - var10;
               double var21 = var15 * var15 + var17 * var17 + var19 * var19;
               if (var21 <= var4 * var4 && (var14 != b_.i.j || this.j6.getValue())) {
                  var1.f.add(new long[]{(long)var21, var13});
                  if (var1.f.size() > 2048) {
                     var1.f.poll();
                  }
               }
            }

            if (++var1.M >= var1.U) {
               var1.M = 0;
               System.arraycopy(var1.X, 0, var1.t, 0, var1.t.length);
               Arrays.fill(var1.X, 0);
               var1.s = (boolean)1;
               int[] var23 = new int[var1.f.size()];
               int var16 = 0;

               for (long[] var18 : var1.f) {
                  var23[var16++] = (int)var18[1];
               }

               var1.Q = var23;
               var1.f.clear();
               if (var1.U < var2) {
                  break;
               }
            }
         }
      }
   }

   private b_.i i(ak.t var1, int var2, MutableBlockPos var3) {
      var3.set(var1.T[var2]);
      BlockState var4 = var1.g(var2);
      b_.i var5;
      if (!mc.level.hasChunkAt(var3)) {
         var5 = b_.i.V;
      } else if (var4 == null) {
         var5 = b_.i.j;
      } else {
         var5 = b_.j(var4, mc.level.getBlockState(var3));
      }

      var1.G[var2] = (byte)var5.ordinal();
      return var5;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.q();
      this.v5 = 0;
      this.NX = null;
      if (this.NW != null) {
         String var4 = this.NW;
         this.NW = null;
         if (this.vU) {
            this.vU = (boolean)0;
            this.XI = 0L;
            this.j("stocking stopped earlier because " + var4 + ". Press Stock materials to go on");
         } else {
            this.j("it stopped earlier because " + var4 + ". Turn it on again to carry on");
         }
      } else if (this.o != 3 && this.o != 2) {
         this.Nx = 0;
         this.n = (boolean)0;
         int var3 = System.currentTimeMillis() - this.XI < 2000L ? 1 : 0;
         this.XI = 0L;
         String var5 = s1.U(this);
         if (var5 != null) {
            this.j("turn off " + var5 + " first; one bot runs at a time");
         } else if (var3 != 0) {
            this.I();
         } else {
            this.s();
         }
      } else {
         ServerData var1 = mc.getCurrentServer();
         int var2 = var1 != null && this.vt != null && var1.ip.equalsIgnoreCase(this.vt.ip) ? 1 : 0;
         this.o = 0;
         this.vt = null;
         if (var2 == 0) {
            this.j("it logged off to resync blocks and you joined another server. Turn it on again to carry on");
         } else {
            this.X8 = System.currentTimeMillis() + 3000L;
            com.larpclient.h.A(this.getName(), "Rejoined after the relog; carrying on in {} ms", 3000L);
            this.e("Rejoined; waiting for the world to load");
         }
      }
   }

   private void s() {
      if (this.jF) {
         this.j("the Build Recorder is recording; stop and save it first");
      } else if (ae.J()) {
         this.j("Auto Area Miner is mining a schematic out; turn it off first");
      } else if (this.XN) {
         this.j("the schematic is unloaded; pick it in the list or press Reload first");
      } else if (this.vI == null) {
         this.j(this.v8 != null ? "the schematic did not load: " + this.v8 : "pick a schematic first");
      } else {
         if (this.vW) {
            this.D();
         }

         ak.t var1 = this.Eo;
         if (var1 == null) {
            this.j("set the origin first");
         } else {
            BlockPos var2 = mc.player.blockPosition();
            double var3 = Math.max(Math.max(var1.E - var2.getX(), var2.getX() - var1.m), Math.max(var1.v - var2.getZ(), var2.getZ() - var1.k));
            if (var3 > 32.0) {
               this.j("stand next to the build to start (you are " + (int)var3 + " blocks away)");
            } else {
               if (!var1.s || var1.U <= 200000) {
                  this.s(var1, var1.U);
               }

               this.jE = (boolean)1;
               this.E2 = mc.level;
               this.vM = mc.player;
               this.XH.k();
               this.N9 = System.currentTimeMillis();
               this.jH = var1.K(b_.i.o);
               this.jy = var1.K(b_.i.j);
               this.m(
                  "building " + this.vI.o() + " at " + var1.h.origin() + this.Y() + ": " + this.jH + " blocks to place, " + var1.K(b_.i.j) + " already done."
               );
               ArrayList var5 = new ArrayList();
               Map var6 = this.h(var1);
               ArrayList var7 = new ArrayList();

               for (Entry var9 : var6.entrySet()) {
                  int var10 = this.E((Item)var9.getKey());
                  if (var10 < (Integer)var9.getValue()) {
                     var7.add((Integer)var9.getValue() - var10 + " " + e((Item)var9.getKey()));
                  }
               }

               int var12 = 0;

               for (ak.f var15 : s(this.C.getValue())) {
                  if (var15.dimension().equals(this.W())) {
                     var12++;
                  }
               }

               if (!var7.isEmpty()) {
                  var5.add(
                     "you are short of "
                        + String.join(", ", var7.subList(0, Math.min(4, var7.size())))
                        + (var7.size() > 4 ? " and " + (var7.size() - 4) + " more" : "")
                        + (this.NF.getValue() && var12 > 0 ? " (it restocks from " + var12 + " marked " + (var12 == 1 ? "container)" : "containers)") : "")
                  );
               }

               int var14 = var1.K(b_.i.y) + var1.K(b_.i.t);
               int var16 = 0;

               for (int var11 = 0; var11 < var1.U; var11++) {
                  if (var1.i(var11) == b_.i.t && b_.F(var1.g(var11), mc.level.getBlockState(var1.S(var11))) > 0) {
                     var16++;
                  }
               }

               if (var14 > var16) {
                  var5.add(var14 - var16 + " cells hold another block or face the wrong way; they are left as they are");
               }

               if (var16 > 0) {
                  this.m(var16 + " blocks are set by right clicks (repeater delays, comparator modes, notes, open doors or levers).");
               }

               if (this.vp.getValue() && hk.J(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                  var5.add("no food in the hotbar");
               }

               if (!var5.isEmpty()) {
                  this.m("heads up: " + String.join("; ", var5) + ".");
               }

               com.larpclient.h.A(
                  this.getName(),
                  "Started: {} at {}{}, {} to place, {} done, {} wrong, box {} {} {} to {} {} {}",
                  this.vI.o(),
                  var1.h.origin(),
                  this.Y(),
                  this.jH,
                  var1.K(b_.i.j),
                  var14,
                  var1.E,
                  var1.S,
                  var1.v,
                  var1.m,
                  var1.J,
                  var1.k
               );
               com.larpclient.h.A(this.getName(), "Player at {} when the run started", this.E().toShortString());
               if (this.jH == 0) {
                  this.J();
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.jE && this.o != 0) {
         com.larpclient.h.A(this.getName(), "Logged off for the relog after {} blocks", this.v5);
      } else if (this.isEnabled() && this.jE) {
         this.NW = "the world, the player or the connection changed";
         this.vU = this.vF;
         com.larpclient.h.A(this.getName(), "Interrupted after {} blocks: {}", this.v5, this.NW);
      }

      super.onDisable();
      t1.v(this);
      l0.h(this);
      if (!this.vB.isEmpty()) {
         BlockPos var1 = this.vB.getFirst();
         this.m("left " + this.vB.size() + " temporary " + (this.vB.size() == 1 ? "block" : "blocks") + " in the pillar at " + var1.toShortString() + ".");
         com.larpclient.h.A(this.getName(), "Stopped with {} temporary blocks standing from {}", this.vB.size(), var1.toShortString());
      }

      if (!this.Ni.isEmpty()) {
         this.m(
            "left "
               + this.Ni.size()
               + " temporary "
               + (this.Ni.size() == 1 ? "block" : "blocks")
               + " in the tower at "
               + this.Ni.getFirst().toShortString()
               + "."
         );
         com.larpclient.h.A(
            this.getName(), "Stopped with {} tower blocks standing beside a doorway from {}", this.Ni.size(), this.Ni.getFirst().toShortString()
         );
      }

      if (!this.i.isEmpty()) {
         this.m("left " + this.i.size() + " temporary " + (this.i.size() == 1 ? "block" : "blocks") + " starting at " + this.i.getFirst().toShortString() + ".");
         com.larpclient.h.A(this.getName(), "Stopped with {} temporary blocks not swept up, from {}", this.i.size(), this.i.getFirst().toShortString());
      }

      if (!this.NR.isEmpty()) {
         this.m(
            "left " + this.NR.size() + " temporary walkway " + (this.NR.size() == 1 ? "block" : "blocks") + " at " + this.NR.getFirst().toShortString() + "."
         );
         com.larpclient.h.A(this.getName(), "Stopped with {} walkway blocks from {}", this.NR.size(), this.NR.getFirst().toShortString());
      }

      if (!this.vT.isEmpty()) {
         this.m("left " + this.vT.size() + " temporary " + (this.vT.size() == 1 ? "block" : "blocks") + " at " + this.vT.getFirst().toShortString() + ".");
         com.larpclient.h.A(this.getName(), "Stopped with {} temporary support blocks at {}", this.vT.size(), this.vT.getFirst().toShortString());
      }

      this.XH.F();
      this.q();
   }

   private void q() {
      this.jE = (boolean)0;
      this.E2 = this.vM = null;
      this.vs.clear();
      this.vG.clear();
      this.XJ.clear();
      this.ve.clear();
      this.jr.clear();
      this.I.clear();
      this.XU.clear();
      this.Nq.clear();
      this.jx = null;
      this.XA.clear();
      this.XK = -1000000000;
      this.Xj.clear();
      this.NI.clear();
      this.vJ.clear();
      this.je.clear();
      this.j2 = null;
      this.v7 = this.E9 = -1000000000;
      this.XR = this.XS = 0;
      this.NQ.clear();
      this.Xo = ak.n.B;
      this.Q = null;
      this.p = null;
      this.XZ = null;
      this.vB.clear();
      this.w.clear();
      this.jV = this.Nk = this.Ea = this.Xy = 0;
      this.NR.clear();
      this.E = null;
      this.N4 = null;
      this.Xw = 0;
      this.Xt = Float.NaN;
      this.jp = this.v4 = 0.0F;
      this.Xx = this.N6 = this.NS = this.ju = 0;
      this.jv = this.N0 = -1000000000;
      this.jd = this.L = (boolean)0;
      this.vh = null;
      this.EU.clear();
      this.NK.clear();
      this.j9.clear();
      this.XG = (boolean)0;
      this.Xn.clear();
      this.f = 0;
      this.N_ = -1;
      this.R = this.X7 = null;
      this.O = null;
      this.Ni.clear();
      this.EP = 0;
      this.El.clear();
      this.Nr = 0;
      this.Xk = (boolean)0;
      this.vx = this.No = 0;
      this.i.clear();
      this.a = null;
      this.Nd = 0;
      this.Nb = 0;
      this.vQ = null;
      this.Xv = null;
      this.vT.clear();
      this.vP.clear();
      this.NC = this.NM = this.G = this.jg = this.XL = this.jR = 0;
      this.jm.clear();
      this.jD = Long.MIN_VALUE;
      this.jn = Long.MIN_VALUE;
      this.Xr = (boolean)0;
      this.vo.clear();
      this.N8 = null;
      this.Xq = null;
      this.vj.clear();
      this.Na.clear();
      this.Eh.clear();
      this.T = this.vz = this.Z = this.j8 = 0;
      this.N2 = -1000000000;
      this.jB = Integer.MAX_VALUE;
      this.Eq = 0;
      this.NH = null;
      this.vw = -1000000000;
      if (this.ji.G()) {
         this.ji.Q("the builder stopped");
      }

      this.vy = null;
      this.E5 = 0L;
      this.j5 = this.Et = 0;
      this.XQ = (boolean)0;
      this.Nj.clear();
      this.vF = this.jL = (boolean)0;
      this.h = null;
      this.NA.clear();
      this.Xc.clear();
      this.NO.clear();
      this.Nh.clear();
      this.j.clear();
      this.jN.clear();
      this.vA.clear();
      this.vC.clear();
      this.Ne.clear();
      this.z.clear();
      this.jS.clear();
      this.vb.clear();
      this.jZ = 0;
      this.vX = null;
      this.X0 = -1000000000;
      this.XC.clear();
      this.X8 = 0L;
      this.v1 = null;
      this.XD = null;
      this.XY = 0;
      this.v0 = -1000000000;
      this.Xa = -1;
      this.vO = 0;
      this.jO = -1000000000;
      this.Nn = -1000000000;
      this.vm = this.jy = this.M = 0;
      this.jP.v();
      this.XT = null;
      this.jz = -1000000000;
      this.v9 = List.of();
      this.vR = null;
      this.j_.H();
      this.XP.x();
      this.Xl = Vec3.ZERO;
      this.N1 = this.jl = this.jw = (boolean)0;
      this.jj = Integer.MIN_VALUE;
      this.Xp = "";
      this.jH = 0;
   }

   @Override
   public void onTick() {
      byte var6 = 1;
      int var5 = this.js;
      this.js = var5 + var6;
      this.Xl = Vec3.ZERO;
      this.N1 = this.jl = this.jw = (boolean)0;
      if (!this.jE) {
         if (this.X8 != 0L) {
            this.S();
         }
      } else if (mc.level == this.E2 && mc.player == this.vM) {
         ak.t var1 = this.Eo;
         if (var1 == null) {
            this.j("the schematic or its origin changed");
         } else {
            this.jj = mc.player.tickCount;
            int var10001;
            if (!mc.player.isShiftKeyDown()) {
               var10001 = -1000000000;
            } else {
               int var8 = -1000000000;
               int var7 = this.va;
               var10001 = Integer.compare(var7, var8);
               var10001 = (var10001 | -var10001) >>> 31 == 0 ? mc.player.tickCount : this.va;
            }

            this.va = var10001;
            this.o();
            if (this.NH == null && (!this.Ed.getValue() || !this.V())) {
               String var2 = hk.j();
               if (var2 != null) {
                  this.z();
                  this.XH.B();
                  this.j_.H();
                  this.N1 = (boolean)1;
                  this.e(var2 + ": swimming up");
               } else if (this.vp.getValue() && this.XH.r(10)) {
                  this.z();
                  this.e("Eating");
               } else if (mc.gui.screen() instanceof AbstractSignEditScreen var3 && mc.player.tickCount - this.v0 < 40) {
                  var3.onClose();
               } else if (this.j2 == null || !this.y(var1)) {
                  if (this.N8 != ak.k.n || (this.vF ? !this.g() : !this.u(var1))) {
                     if (this.vy != null) {
                        this.W();
                     } else if (!this.I()) {
                        this.K();
                        this.e(mc.gui.screen() != null ? "Paused while a screen is open" : "Waiting for world or interaction control");
                     } else if (this.Z()) {
                        this.K();
                        this.j_.H();
                        this.XP.x();
                        this.e("Paused for manual movement");
                     } else if (mc.gui.screen() instanceof AbstractContainerScreen) {
                        this.K();
                        this.e("Holding still while an inventory menu is open");
                     } else {
                        this.R(var1);
                        if (this.z()) {
                           this.C();
                        } else if (this.XZ == ak.l.f) {
                           this.f(var1);
                        } else if (this.XZ == ak.l.N) {
                           this.Y();
                        } else if (this.XZ == ak.l.H) {
                           this.N();
                        } else if (this.XZ == ak.l.E) {
                           this.U();
                        } else if (this.XZ == ak.l.j) {
                           this.V();
                        } else if (!mc.player.onGround() && !mc.player.isInWater()) {
                           if (this.j_.i()) {
                              this.u();
                           } else {
                              this.z();
                              this.e("Waiting to land");
                           }
                        } else if (this.vF) {
                           this.l(var1);
                        } else if (this.v1 != null) {
                           if (this.B()) {
                              p_.p(p_.o.i);
                           }

                           if (this.Xo == ak.n.B) {
                              this.e(var1);
                           } else {
                              this.J(var1);
                           }
                        } else if (this.a() && p_.A() == p_.o.R) {
                           this.K();
                           this.e("Turning back to your look");
                        } else if (this.M > 0) {
                           this.g();
                        } else {
                           int var10000 = mc.player.tickCount - this.v0;
                           int var10 = this.Xf.getValue().intValue();
                           int var9 = var10000;
                           if ((Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
                              this.c();
                           } else {
                              var10000 = mc.player.tickCount - this.v0;
                              int var12 = this.Xf.getValue().intValue() + this.NS;
                              int var11 = var10000;
                              if ((Integer.compare(var11, var12) >>> 31 ^ 1) == 0) {
                                 this.c();
                              } else if (this.c()) {
                                 if (this.j_.i()) {
                                    this.u();
                                 } else {
                                    this.c();
                                 }
                              } else {
                                 this.z(var1);
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               this.R();
            }
         }
      } else {
         this.j("the world or player changed");
      }
   }

   private boolean c() {
      BlockStatePredictionHandler var1 = ((qaee82196bfd62460)mc.level).LarpRig$getPendingUpdateManager();
      if (((q3ddfad491c6be35c)var1).larp$unconfirmed().isEmpty()) {
         this.X0 = -1000000000;
         return false;
      }

      int var2 = mc.player.tickCount;
      if (this.X0 == -1000000000) {
         this.X0 = var2;
      }

      if (var2 - this.X0 == 40) {
         com.larpclient.h.A(this.getName(), "The server has not confirmed a block change for {} ticks; going on", 40);
      }

      if (var2 - this.X0 >= 40) {
         return false;
      }

      this.e("Waiting for the server to confirm the last block");
      return true;
   }

   private void K() {
      this.z();
      this.Xl = Vec3.ZERO;
   }

   private void z() {
      t1.v(this);
      this.v1 = null;
      this.XD = null;
   }

   private boolean Z() {
      return mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            c5.J(mc.options.keyUp)
               || c5.J(mc.options.keyDown)
               || c5.J(mc.options.keyLeft)
               || c5.J(mc.options.keyRight)
               || c5.J(mc.options.keyJump)
               || c5.J(mc.options.keyShift)
         );
   }

   private double j() {
      return Math.min(this.m.getValue(), mc.player.blockInteractionRange());
   }

   private BlockPos E() {
      return BlockPos.containing(mc.player.getX(), mc.player.getY() + 0.2, mc.player.getZ());
   }

   private void R(ak.t var1) {
      if (!this.NI.isEmpty()) {
         MutableBlockPos var2 = new MutableBlockPos();
         Iterator var3 = this.NI.entrySet().iterator();

         while (var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            int var5 = (Integer)var4.getKey();
            b_.i var6 = this.i(var1, var5, var2);
            BlockState var7 = this.NQ.get(var5);
            int var8 = var6 != b_.i.o || var7 != null && !mc.level.getBlockState(var2).equals(var7) ? 0 : 1;
            if (var6 == b_.i.o && var8 == 0) {
               if (mc.player.tickCount - (Integer)var4.getValue() > 20) {
                  var3.remove();
                  this.NQ.remove(var5);
               }
            } else if (var8 != 0) {
               var3.remove();
               this.NQ.remove(var5);
               int var9 = this.XJ.merge(var5, 1, Integer::sum);
               this.XC.addLast(mc.player.tickCount);
               com.larpclient.h.U(this.getName(), "The server took back {} ({} times)", var2.toShortString(), var9);
               if (var9 >= 3) {
                  this.XJ.remove(var5);
                  this.vs.put(var5, mc.player.tickCount + 2400);
                  this.m(var2.toShortString() + ": the server keeps removing it, skipping it for now.");
                  com.larpclient.h.A(this.getName(), "Server removed {} {} times; skipping for {} ticks", var2.toShortString(), 3, 2400);
               }
            } else if (var6 == b_.i.t) {
               var3.remove();
               this.NQ.remove(var5);
               if (b_.F(var1.g(var5), mc.level.getBlockState(var2)) < 0) {
                  com.larpclient.h.A(
                     this.getName(), "Placed {} facing the wrong way: {} instead of {}", var2.toShortString(), mc.level.getBlockState(var2), var1.g(var5)
                  );
                  if (this.Nq.merge(var5, 1, Integer::sum) <= 2) {
                     this.u = var5;
                     this.jx = var2.immutable();
                     this.jU = mc.player.tickCount;
                  }
               }
            } else if (mc.player.tickCount - (Integer)var4.getValue() > 20) {
               var3.remove();
               this.NQ.remove(var5);
            }
         }
      }
   }

   private void z(ak.t var1) {
      BlockPos var2 = this.E();
      if (!this.Xk) {
         this.Xk = (boolean)1;
         if (this.b(var1)) {
            return;
         }
      }

      if (this.jx == null || !this.B(var1)) {
         if (this.XZ == null && this.Xv == null && this.i.contains(var2.below())) {
            if (this.XK == -1000000000) {
               this.XK = mc.player.tickCount;
            }

            if (mc.player.tickCount - this.XK <= 200) {
               this.e("Coming off a temporary block");
               if (!this.S(var2.below())) {
                  this.L(var2.below());
               }

               return;
            }

            com.larpclient.h.A(this.getName(), "Could not dig {} from on top of it; going on without it", var2.below().toShortString());
            l0.r(this.getName(), var2.below());
            this.i.remove(var2.below());
         }

         this.XK = -1000000000;
         if (this.Xv == null || !this.I(var1)) {
            if (this.XZ == ak.l.G && this.XG) {
               this.E();
            } else if (this.XZ == ak.l.C) {
               this.F(var1);
            } else if (this.N8 == ak.k.v) {
               this.X(var1);
            } else if (this.N8 == ak.k.I) {
               this.t();
            } else {
               List var3 = this.z(var1);
               if (!this.Y(var3)) {
                  if (this.X7 != null) {
                     this.b(var1);
                  } else if (var3 == null && this.v()) {
                     this.M();
                  } else {
                     if (this.XZ == ak.l.G && this.R != null && this.Ni.isEmpty() && this.p != null) {
                        this.X();
                     }

                     if (this.G <= 0 || this.XZ != null || this.Xv != null || !this.A(var1)) {
                        if (this.Xj.isEmpty() || var3 != null && this.jB >= this.Eq || !this.NF.getValue() || this.XZ != null || !this.Q(var1)) {
                           if (this.Xj.isEmpty() || var3 != null && this.jB >= this.Eq || !this.Nm.getValue() || this.XZ != null || !this.c(var1)) {
                              long var4 = this.v5 * 1000003L + this.vo.size() * 1009L + this.ve.size() * 31L + this.vs.size();
                              long var6 = var2.asLong() * 31L + var4;
                              if (this.v3.getValue()
                                 && this.vv.getValue()
                                 && this.Xv == null
                                 && (this.XZ == null || this.XZ == ak.l.G)
                                 && !this.v()
                                 && var6 != this.jn) {
                                 for (List var9 : this.vo) {
                                    if (this.u(var1, var9)) {
                                       return;
                                    }
                                 }

                                 this.jn = var6;
                              }

                              if (this.v3.getValue() && this.vv.getValue() && this.XZ == null && this.Xv == null && !this.v() && var4 != this.jD) {
                                 for (List var17 : this.vo) {
                                    if (this.b(var1, var17)) {
                                       return;
                                    }
                                 }

                                 this.jD = var4;
                              }

                              if (var3 == null) {
                                 if (this.XZ == ak.l.G) {
                                    this.h("the build is done");
                                 } else {
                                    this.g(var1);
                                 }
                              } else {
                                 c6 var16 = new c6(var0 -> -1, false);
                                 double var18 = this.j();
                                 int var11 = this.j_.i() && mc.player.tickCount - this.jz <= 100 ? 0 : 1;
                                 s_.y var12 = s_.l(
                                    var16,
                                    var3,
                                    this.I,
                                    var2.getX(),
                                    var2.getY(),
                                    var2.getZ(),
                                    var18,
                                    var1.f(),
                                    var2x -> this.z(var1, var2x),
                                    (var2x, var3x, var4x, var5) -> this.a(var1, var2x) != null,
                                    (var2x, var3x, var4x, var5) -> this.z(var1, var2x, var3x, var4x, var5),
                                    this.XZ == ak.l.G ? 120 : 24,
                                    (boolean)var11,
                                    this.jP.e(),
                                    this.vX,
                                    this.vv.getValue() && this.XZ != ak.l.G
                                 );
                                 if ("keep walking".equals(var12.reason())) {
                                    this.u();
                                 } else if (var12.place() != null) {
                                    this.j_.H();
                                    this.v9 = List.of();
                                    this.jP.x();
                                    this.e(var1, var12.place());
                                 } else if (this.XZ != ak.l.G || !this.Y(var1, var16, var2, var18)) {
                                    if (this.XZ != ak.l.G || !var12.walk() || !this.j(var1, var12)) {
                                       if (this.XZ != ak.l.G || !this.NR.isEmpty() || !this.F(var1, var12.walkingFor())) {
                                          if (this.XZ != ak.l.G || !this.I(var1, var12.walkingFor())) {
                                             if (this.XZ == ak.l.G) {
                                                this.h("nothing more to place from the top");
                                             } else if (var12.walk() && this.jd) {
                                                this.jd = (boolean)0;
                                                this.L = (boolean)1;
                                                this.M = 12;
                                             } else {
                                                if (var12.walk() && this.jP.p()) {
                                                   com.larpclient.h.U(
                                                      this.getName(),
                                                      "Nothing placeable from {}; leaving that spot out until a block goes down ({} left out)",
                                                      var2.toShortString(),
                                                      this.jP.e().size()
                                                   );
                                                }

                                                if (var12.walk()) {
                                                   if (!this.vv.getValue()) {
                                                      this.c();
                                                      this.e("Nothing in reach; walk closer (Auto Move is off)");
                                                      this.n("nothing is in reach; walk closer or turn on Auto Move");
                                                   } else {
                                                      this.f(var1, var12);
                                                   }
                                                } else {
                                                   this.j_.H();
                                                   if (var12.reason() == null) {
                                                      this.g(var1);
                                                   } else if ("no standing spot within reach".equals(var12.reason()) && this.Q()) {
                                                      this.J(this.R);
                                                   } else if (!"no standing spot within reach".equals(var12.reason())
                                                      || !this.v3.getValue()
                                                      || !this.k(var1, var12.walkingFor())) {
                                                      for (s_.k var14 : var12.walkingFor()) {
                                                         this.E(var14.id(), var12.reason());
                                                      }

                                                      this.e("Setting aside " + var12.walkingFor().size() + " blocks: " + var12.reason());
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

   private List<s_.k> z(ak.t var1) {
      MutableBlockPos var2 = new MutableBlockPos();
      int var3 = mc.player.tickCount;
      this.vs.values().removeIf(var1x -> var1x <= var3);
      this.Xj.clear();
      this.vJ.clear();
      this.XS = 0;
      this.jB = Integer.MAX_VALUE;
      this.vo.clear();
      this.a(var1);
      if (this.NN.getValue() && this.NE != null && this.NZ == var1) {
         for (int var7 : this.NE) {
            if (var1.g(var7) != null
               && var1.P[var1.y[var7]] == null
               && !var1.v(var7)
               && !this.ve.contains(var7)
               && !this.vs.containsKey(var7)
               && !this.NK.contains(var7)
               && this.i(var1, var7, var2) == b_.i.o) {
               BlockState var8 = var1.g(var7);
               int var9 = var2.getY() - var1.S;
               BlockItem var10 = b_.B(var8);
               if (var10 != null && this.E(var10) != 0) {
                  if (b_.J(var8) && mc.level.getBlockState(var2.below()).canBeReplaced()) {
                     this.XS++;
                  } else {
                     s_.k var11 = new s_.k(var2.getX(), var2.getY(), var2.getZ(), var7);
                     this.vJ.put(var7, ak.n.B);
                     if (this.z(var1, var11)) {
                        this.Eq = var9;
                        return List.of(var11);
                     }
                  }
               } else {
                  this.Xj.merge(var10, 1, Integer::sum);
                  if (var10 != null) {
                     this.jB = Math.min(this.jB, var9);
                  }
               }
            }
         }

         this.vJ.clear();
      }

      HashSet var14 = this.v() && !this.R.aims().isEmpty() ? new HashSet<>(this.R.aims().stream().map(s_.k::id).toList()) : null;

      for (int var15 = 0; var15 + 1 < var1.a.length; var15++) {
         ArrayList var16 = null;
         byte var17 = 0;

         for (int var18 = var1.a[var15]; var18 < var1.a[var15 + 1]; var18++) {
            int var19 = var1.K[var18];
            if (var1.g(var19) != null
               && var1.P[var1.y[var19]] == null
               && !var1.v(var19)
               && !this.ve.contains(var19)
               && !this.vs.containsKey(var19)
               && !this.EU.contains(var19)
               && !this.NK.contains(var19)
               && (var14 == null || var14.contains(var19))) {
               b_.i var20 = this.i(var1, var19, var2);
               BlockState var21 = var1.g(var19);
               ak.n var12;
               if (var20 == b_.i.o) {
                  var12 = ak.n.B;
               } else if (var20 == b_.i.t && b_.F(var21, mc.level.getBlockState(var2)) > 0) {
                  var12 = ak.n.p;
               } else {
                  if (var20 != b_.i.j || !var1.C.containsKey(var19) || this.je.contains(var19)) {
                     continue;
                  }

                  var12 = ak.n.F;
               }

               if (var12 == ak.n.B) {
                  BlockItem var13 = b_.B(var21);
                  if (var13 == null || this.E(var13) == 0) {
                     this.Xj.merge(var13, 1, Integer::sum);
                     if (var13 != null) {
                        this.jB = Math.min(this.jB, var15);
                     }
                     continue;
                  }

                  if (b_.J(var21) && mc.level.getBlockState(var2.below()).canBeReplaced()) {
                     this.XS++;
                     continue;
                  }
               }

               this.vJ.put(var19, var12);
               if (var16 == null) {
                  var16 = new ArrayList();
               }

               s_.k var22 = new s_.k(var2.getX(), var2.getY(), var2.getZ(), var19);
               var16.add(var22);
               if (var17 == 0 && this.z(var1, var22)) {
                  var17 = 1;
               }
            }
         }

         if (var16 != null && var17 != 0) {
            this.Eq = var15;
            return this.M(var1, var16);
         }

         if (var16 != null) {
            this.vo.add(var16);
         }
      }

      return null;
   }

   private void a(ak.t var1) {
      if (this.vh == null) {
         this.c(var1);
         this.O(var1);
      }

      if (!this.vh.isEmpty() && this.X7 == null) {
         MutableBlockPos var2 = new MutableBlockPos();

         for (s_.j var4 : List.copyOf(this.vh)) {
            byte var5 = 0;
            if (!var4.aims().isEmpty()) {
               for (s_.k var7 : var4.aims()) {
                  if (this.i(var1, var7.id(), var2) == b_.i.o) {
                     var5 = 1;
                     break;
                  }
               }

               if (var5 == 0) {
                  this.J(var4);
               }
            }
         }
      }
   }

   private void c(ak.t var1) {
      this.NK.clear();

      for (int var2 = 0; var2 < var1.U; var2++) {
         if (var1.g(var2) != null && mc.level.isOutsideBuildHeight(BlockPos.getY(var1.T[var2]))) {
            this.NK.add(var2);
         }
      }

      if (!this.NK.isEmpty()) {
         String var3 = "Y " + mc.level.getMinY() + " to " + mc.level.getMaxY();
         this.m(
            this.NK.size() + " blocks of the schematic are outside this world's height limits (" + var3 + ") and are left out; move the origin to build them."
         );
         com.larpclient.h.A(this.getName(), "{} cells are outside the height limits ({}) and are left out", this.NK.size(), var3);
      }
   }

   private void O(ak.t var1) {
      this.vh = new ArrayList<>();
      this.EU.clear();
      if (this.v3.getValue() && this.vv.getValue() && (!this.NN.getValue() || this.NE == null)) {
         ArrayList var2 = new ArrayList();
         MutableBlockPos var3 = new MutableBlockPos();

         for (int var4 = 0; var4 < var1.U; var4++) {
            BlockState var5 = var1.g(var4);
            if (var5 != null && var1.P[var1.y[var4]] == null && !var1.v(var4) && b_.f(var5) && this.i(var1, var4, var3) == b_.i.o) {
               var2.add(new s_.k(var3.getX(), var3.getY(), var3.getZ(), var4));
            }
         }

         if (!var2.isEmpty()) {
            HashSet var10 = new HashSet();

            for (s_.k var6 : var2) {
               var10.add(var6.id());
            }

            s_.u var12 = (var3x, var4x, var5x) -> {
               int var6x = var1.M(var3x, var4x, var5x);
               return var6x >= 0
                  && !var10.contains(var6x)
                  && var1.g(var6x) != null
                  && var1.P[var1.y[var6x]] == null
                  && !var1.v(var6x)
                  && this.i(var1, var6x, new MutableBlockPos()) == b_.i.o;
            };
            this.vh.addAll(s_.F((var2x, var3x, var4x) -> this.e(var1, var2x, var3x, var4x), var12, var2, var1.f(), this.j() - 0.3));

            for (s_.j var7 : this.vh) {
               for (int[] var9 : var7.cells()) {
                  this.EU.add(var1.M(var9[0], var9[1], var9[2]));
               }

               com.larpclient.h.A(
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

   private boolean e(ak.t var1, int var2, int var3, int var4) {
      BlockPos var5 = new BlockPos(var2, var3, var4);
      int var6 = var1.M(var2, var3, var4);
      int var7 = var6 >= 0 && var1.g(var6) != null && var1.P[var1.y[var6]] == null ? 1 : 0;
      return (var7 != 0 ? var1.g(var6) : mc.level.getBlockState(var5)).getCollisionShape(mc.level, var5).isEmpty();
   }

   private boolean q(ak.t var1, s_.y var2) {
      if (this.Xv == null && this.vh != null) {
         for (s_.j var4 : this.vh) {
            if (var4 != this.R && this.L(var1, var4, var2.walkingFor())) {
               Item var5 = this.p();
               if (var5 == null) {
                  return false;
               }

               s_.a var6 = s_.B(new c6(var0 -> -1, false), var4, this.g.getValue().intValue(), var1::x, var2.walkingFor());
               if (var6 == null) {
                  return false;
               }

               if (this.E(var5) < var6.height()) {
                  this.G = var6.height();
                  return true;
               }

               this.p = var6;
               this.XZ = ak.l.C;
               this.jV = this.Nk = mc.player.tickCount;
               this.j_.H();
               this.v9 = List.of();
               this.R = var4;
               com.larpclient.h.A(
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

   private boolean j(ak.t var1, s_.y var2) {
      if (this.R == null && this.p != null && !var2.stands().isEmpty() && this.NR.isEmpty() && this.v5 != this.N_) {
         BlockPos var3 = this.E();
         HashSet var4 = new HashSet();

         for (long var6 : var2.stands()) {
            if (zf.d(var6) >= var3.getY() - 1) {
               var4.add(var6);
            }
         }

         if (!var4.isEmpty() && this.c(var1, var4, new BlockPos(var2.toward().x(), var2.toward().y(), var2.toward().z()))) {
            this.f = this.v5;
            this.R = new s_.j(List.of(), var3.getX(), var3.getY(), var3.getZ(), List.of(), Set.of());
            this.vh.add(this.R);
            this.v9 = var2.walkingFor();
            this.X();
            this.u();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean L(ak.t var1, s_.j var2, List<s_.k> var3) {
      for (s_.k var5 : var3) {
         if (!var2.aims().stream().noneMatch(var1x -> var1x.id() == var5.id())) {
            for (long var7 : var2.space()) {
               if (this.z(var1, var5, zf.W(var7), zf.d(var7), zf.F(var7))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean Q() {
      return this.v() && this.R.aims().isEmpty();
   }

   private boolean v() {
      return this.R != null && !this.Ni.isEmpty() && this.X7 == null;
   }

   private void X() {
      this.O = this.p;
      this.Ni.addAll(this.vB);
      this.vB.clear();
      this.p = null;
      this.XZ = null;
      this.jP.v();
      if (this.R.aims().isEmpty()) {
         com.larpclient.h.A(this.getName(), "Walking off the tower at {} {} {} along the build", this.R.outX(), this.R.outY(), this.R.outZ());
      } else {
         com.larpclient.h.A(
            this.getName(), "Walking off the tower at {} {} {} for {} blocks", this.R.outX(), this.R.outY(), this.R.outZ(), this.R.aims().size()
         );
      }
   }

   private void M() {
      byte var1 = 0;

      for (s_.k var3 : this.R.aims()) {
         if (this.vs.remove(var3.id()) != null) {
            var1 = 1;
         }
      }

      if (var1 == 0) {
         this.J(this.R);
      }
   }

   private void J(s_.j var1) {
      if (var1 == this.R && !this.Ni.isEmpty()) {
         this.X7 = var1;
         this.EP = mc.player.tickCount;
         this.j_.H();
         com.larpclient.h.A(this.getName(), "Going back to the tower at {} {} {}", var1.outX(), var1.outY(), var1.outZ());
      } else {
         this.e(var1);
      }
   }

   private void e(s_.j var1) {
      this.vh.remove(var1);
      if (this.Eo != null) {
         for (int[] var3 : var1.cells()) {
            int var4 = this.Eo.M(var3[0], var3[1], var3[2]);
            this.EU.remove(var4);
            this.I.remove(var4);
         }
      }

      this.jP.v();
   }

   private void b(ak.t var1) {
      s_.j var2 = this.X7;
      BlockPos var3 = this.E();
      BlockPos var4 = new BlockPos(var2.outX(), var2.outY(), var2.outZ());
      if (var3.equals(var4)) {
         this.j_.H();
         if (var2.aims().isEmpty() && this.v5 == this.f) {
            this.N_ = this.v5;
         }

         Vec3 var6 = Vec3.atBottomCenterOf(var4).subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
         if (var6.length() >= 0.25 && mc.player.tickCount - this.EP <= 600) {
            this.jl = (boolean)1;
            this.Xl = var6;
            this.e("Stepping to the middle of the tower");
         } else {
            this.vB.addAll(this.Ni);
            this.Ni.clear();
            this.p = this.O;
            this.O = null;
            this.XZ = ak.l.G;
            this.Ea = this.v5;
            this.X7 = null;
            this.R = null;
            this.e(var2);
         }
      } else {
         this.e("Going back to the tower");
         int var5 = mc.player.tickCount - this.EP > 600 ? 1 : 0;
         if (var5 != 0 || !this.j_.i() && !this.c(var1, Set.of(zf.O(var4.getX(), var4.getY(), var4.getZ())), var4)) {
            com.larpclient.h.A(
               this.getName(),
               "No way back to the tower from {} ({}); it is swept up at the end",
               var3.toShortString(),
               var5 != 0 ? "took too long" : "no route"
            );
            if (var2.aims().isEmpty() && this.v5 == this.f) {
               this.N_ = this.v5;
            }

            this.j_.H();
            this.i.addAll(this.Ni);
            this.Ni.clear();
            this.O = null;
            this.X7 = null;
            this.R = null;
            this.e(var2);
         } else {
            this.u();
         }
      }
   }

   private List<s_.k> M(ak.t var1, List<s_.k> var2) {
      HashSet var3 = new HashSet();

      for (s_.k var5 : var2) {
         BlockState var6 = var1.g(var5.id());
         if (b_.W(var6) && this.vJ.getOrDefault(var5.id(), ak.n.B) == ak.n.B) {
            Direction var7 = (Direction)var6.getValue(BlockStateProperties.FACING);
            if (!var7.getAxis().isVertical()) {
               for (int var8 = 1; var8 <= 3; var8++) {
                  var3.add(zf.O(var5.x() + var7.getStepX() * var8, var5.y(), var5.z() + var7.getStepZ() * var8));
               }
            }
         }
      }

      if (var3.isEmpty()) {
         return var2;
      }

      List var9 = var2.stream().filter(var2x -> b_.W(var1.g(var2x.id())) || !var3.contains(zf.O(var2x.x(), var2x.y(), var2x.z()))).toList();
      return var9.stream().anyMatch(var2x -> this.z(var1, var2x)) ? var9 : var2;
   }

   private boolean z(ak.t var1, s_.k var2) {
      BlockPos var3 = new BlockPos(var2.x(), var2.y(), var2.z());
      return this.vJ.getOrDefault(var2.id(), ak.n.B) != ak.n.B ? true : b_.g(var3) || mc.level.getBlockState(var3).is(var1.g(var2.id()).getBlock());
   }

   private BlockHitResult a(ak.t var1, s_.k var2) {
      BlockState var3 = var1.g(var2.id());
      BlockPos var4 = new BlockPos(var2.x(), var2.y(), var2.z());
      if (this.vJ.getOrDefault(var2.id(), ak.n.B) != ak.n.B) {
         return b_.x(this, var4, mc.player.getEyePosition(), this.j(), var3);
      }

      ItemStack var5 = this.d(b_.B(var3));
      if (var5.isEmpty()) {
         return null;
      }

      if (mc.player.getBoundingBox().intersects(new AABB(var4))) {
         return null;
      }

      BlockPos var6 = b_.n(var3, var4);
      return var6 != null && mc.player.getBoundingBox().intersects(new AABB(var6))
         ? null
         : b_.Z(this, mc.player, var5, var4, var3, mc.player.getEyePosition(), this.j(), true);
   }

   private boolean z(ak.t var1, s_.k var2, int var3, int var4, int var5) {
      BlockState var6 = var1.g(var2.id());
      BlockPos var7 = new BlockPos(var2.x(), var2.y(), var2.z());
      double var8 = this.j() - 0.3;
      if (this.vJ.getOrDefault(var2.id(), ak.n.B) != ak.n.B) {
         return b_.x(this, var7, new Vec3(var3 + 0.5, var4 + 1.62, var5 + 0.5), var8, var6) != null;
      }

      ItemStack var10 = this.d(b_.B(var6));
      if (var10.isEmpty()) {
         return false;
      }

      BlockPos var11 = b_.n(var6, var7);
      if (var11 == null || var11.getX() != var3 || var11.getZ() != var5 || var11.getY() != var4 && var11.getY() != var4 + 1) {
         for (double var15 : new double[]{1.62, mc.player.getEyeHeight(Pose.CROUCHING)}) {
            if (b_.s(this, mc.player, var10, var7, var6, new Vec3(var3 + 0.5, var4 + var15, var5 + 0.5), var8, true, true) == null) {
               return false;
            }

            if (!this.En.getValue()) {
               break;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private ItemStack d(Item var1) {
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

   private void E(int var1, String var2) {
      int var3 = this.vG.merge(var1, 1, Integer::sum);
      String var4 = this.Eo == null ? "?" : this.Eo.S(var1).toShortString();
      if (var3 >= 3) {
         this.ve.add(var1);
         this.jr.put(var1, var2);
         com.larpclient.h.A(this.getName(), "Giving up on {}: {}", var4, var2);
      } else {
         this.vs.put(var1, mc.player.tickCount + 400);
         com.larpclient.h.U(this.getName(), "Setting {} aside for now: {}", var4, var2);
      }
   }

   private void g(ak.t var1) {
      this.j_.H();
      this.jP.v();
      if (!this.vs.isEmpty()) {
         byte var2 = 0;

         for (Integer var4 : new ArrayList<>(this.vs.keySet())) {
            if (!this.ve.contains(var4)) {
               this.vs.remove(var4);
               var2 = 1;
            }
         }

         if (var2 != 0) {
            com.larpclient.h.U(this.getName(), "Retrying blocks set aside");
            this.e("Trying the blocks set aside again");
            return;
         }
      }

      if (!this.i.isEmpty()) {
         this.v(var1);
      } else if (!this.ve.isEmpty() && this.XZ == null && this.Ni.isEmpty() && this.d()) {
         BlockPos var6 = this.E();
         this.j("no way to walk from where you stand at " + var6.toShortString() + "; stand on the floor beside the build and start again");
      } else if (!this.ve.isEmpty() && this.v5 > this.jg) {
         this.jg = this.v5;
         com.larpclient.h.A(this.getName(), "Trying the {} blocks given up on again, now that more of the build stands", this.ve.size());
         this.j9.clear();
         this.ve.clear();
         this.jr.clear();
         this.vG.clear();
         this.e("Trying the blocks given up on again");
      } else if (this.jm.values().stream().anyMatch(var0 -> var0 >= 2) && this.v5 > this.jR) {
         this.jR = this.v5;
         this.jm.clear();
         this.XL = this.v5;
         this.jD = Long.MIN_VALUE;
         this.jn = Long.MIN_VALUE;
         com.larpclient.h.A(this.getName(), "Trying temporary supports and towers once more, {} blocks placed so far", this.v5);
         this.e("Trying towers again");
      } else if (this.vh != null && !this.vh.isEmpty()) {
         this.J(this.vh.getFirst());
      } else if (!this.Xj.isEmpty()) {
         ArrayList var5 = new ArrayList();

         for (Entry var8 : this.Xj.entrySet()) {
            var5.add(var8.getValue() + " " + (var8.getKey() == null ? "unplaceable" : e((Item)var8.getKey())));
         }

         this.j(
            "out of blocks: "
               + String.join(", ", var5.subList(0, Math.min(5, var5.size())))
               + (var5.size() > 5 ? " and more" : "")
               + (this.vj.isEmpty() ? "" : " (the marked containers have none)")
         );
      } else {
         this.J();
      }
   }

   private void e(ak.t var1, s_.k var2) {
      this.v1 = var2;
      this.Xo = this.vJ.getOrDefault(var2.id(), ak.n.B);
      this.XY = mc.player.tickCount;
      this.XD = null;
      if (this.B()) {
         p_.p(p_.o.i);
      }

      if (this.Xo == ak.n.B) {
         this.e(var1);
      } else {
         this.J(var1);
      }
   }

   private void e(ak.t var1) {
      s_.k var2 = this.v1;
      BlockPos var3 = new BlockPos(var2.x(), var2.y(), var2.z());
      MutableBlockPos var4 = new MutableBlockPos();
      if (this.i(var1, var2.id(), var4) != b_.i.o) {
         this.z();
      } else if (mc.player.tickCount - this.XY > 40) {
         com.larpclient.h.U(this.getName(), "Placing {} took too long: {}", var3.toShortString(), t1.L(this));
         this.z();
         this.E(var2.id(), "could not place it");
      } else {
         BlockState var5 = var1.g(var2.id());
         BlockItem var6 = b_.B(var5);
         int var7 = this.v(var6);
         if (var7 < 0) {
            this.J(var6);
         } else {
            this.c();
            if (!this.j(var7)) {
               this.e("Taking out " + mc.player.getInventory().getItem(var7).getHoverName().getString());
            } else {
               BlockHitResult var8 = this.a(var1, var2);
               if (var8 == null) {
                  this.z();
               } else {
                  this.XD = var8;
                  this.Q = mc.level.getBlockState(var3);
                  BlockPos var9 = var8.getBlockPos();
                  this.Y(var9);
                  double var10 = this.j();
                  boolean var12 = t1.a(
                     this,
                     var3,
                     var8,
                     var6,
                     var10,
                     true,
                     () -> t1.U(var3, var6) && !this.Z(),
                     () -> t1.U(this, var9, var10) && b_.p(mc.player, var8) && b_.C(mc.player, var9) && this.g(var3, var9, var8.getDirection(), var5),
                     var3x -> this.o(var1, var2, var3x)
                  );
                  this.e(
                     var12
                        ? "Placing " + var5.getBlock().getName().getString() + " at " + var3.toShortString()
                        : "Cannot place at " + var3.toShortString() + ": " + t1.L(this)
                  );
               }
            }
         }
      }
   }

   private void Y(BlockPos var1) {
      if (b_.q(mc.level.getBlockState(var1))) {
         this.jl = (boolean)1;
      }
   }

   private boolean g(BlockPos var1) {
      return !b_.q(mc.level.getBlockState(var1)) || this.va != -1000000000 && mc.player.tickCount - this.va >= 3;
   }

   private boolean g(BlockPos var1, BlockPos var2, Direction var3, BlockState var4) {
      if (!this.g(var2)) {
         return false;
      }

      if (!b_.Q(var4)) {
         return true;
      }

      Vec3 var5 = mc.player.getLookAngle();
      Vec3 var6 = mc.player.getEyePosition();
      BlockHitResult var7 = t1.d(this, var6, var2, var5, this.j());
      if (var7 != null && var7.getDirection() == var3) {
         ItemStack var8 = this.d(b_.B(var4));
         BlockState var9 = mc.level.getBlockState(var1);
         q1881b7c262e73011 var10 = (q1881b7c262e73011)mc.player;
         if (!(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - var10.larpYRotLast())) > 2.0F) && !(Math.abs(mc.player.getXRot() - var10.larpXRotLast()) > 2.0F)) {
            for (float[] var14 : new float[][]{{mc.player.getYRot(), mc.player.getXRot()}, {var10.larpYRotLast(), var10.larpXRotLast()}}) {
               BlockState var15 = b_.E(mc.player, var8, var7, var14[0], var14[1]);
               if (!b_.F(var4, var9, var15)) {
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

   private void o(ak.t var1, s_.k var2, boolean var3) {
      if (var3 && this.XD != null) {
         com.larpclient.h.U(
            this.getName(),
            "Click for {} {} {}: {} face of {}, held {}, player at {}",
            var2.x(),
            var2.y(),
            var2.z(),
            this.XD.getDirection().getSerializedName(),
            this.XD.getBlockPos().toShortString(),
            mc.player.getMainHandItem().getHoverName().getString(),
            mc.player.blockPosition().toShortString()
         );
      }

      this.v0 = mc.player.tickCount;
      this.v1 = null;
      this.XD = null;
      if (!var3) {
         com.larpclient.h.U(this.getName(), "Placement at {} {} {} was not accepted", var2.x(), var2.y(), var2.z());
      } else {
         this.vX = var2;
         this.v5++;
         this.jd = (boolean)0;
         this.NS = this.NB.nextInt(5) + (this.NB.nextInt(100) < 6 ? 8 + this.NB.nextInt(16) : 0);
         this.NI.put(var2.id(), mc.player.tickCount);
         if (this.Q != null) {
            this.NQ.put(var2.id(), this.Q);
         }
      }
   }

   private void J(ak.t var1) {
      s_.k var2 = this.v1;
      BlockPos var3 = new BlockPos(var2.x(), var2.y(), var2.z());
      MutableBlockPos var4 = new MutableBlockPos();
      b_.i var5 = this.i(var1, var2.id(), var4);
      BlockState var6 = var1.g(var2.id());
      int var7 = this.Xo == ak.n.p
         ? (var5 == b_.i.t && b_.F(var6, mc.level.getBlockState(var3)) > 0 ? 1 : 0)
         : (var5 == b_.i.j && !this.je.contains(var2.id()) && this.j2 == null ? 1 : 0);
      if (var7 == 0) {
         this.z();
      } else if (mc.player.tickCount - this.XY > 40) {
         com.larpclient.h.U(this.getName(), "Clicking {} took too long: {}", var3.toShortString(), t1.L(this));
         this.z();
         this.E(var2.id(), this.Xo == ak.n.p ? "could not adjust it" : "could not open the crafter");
      } else {
         this.Xl = Vec3.ZERO;
         this.jl = (boolean)0;
         int var8 = this.K();
         if (var8 >= 0 && !this.j(var8)) {
            this.e("Putting the block away");
         } else {
            BlockHitResult var9 = this.a(var1, var2);
            if (var9 == null) {
               this.z();
            } else {
               this.XD = var9;
               ItemStack var10 = mc.player.getMainHandItem();
               Item var11 = var10.isEmpty() ? Items.AIR : var10.getItem();
               double var12 = this.j();
               boolean var14 = t1.S(
                  this,
                  var3,
                  var11,
                  var12,
                  true,
                  false,
                  () -> !this.Z(),
                  () -> !mc.player.isShiftKeyDown()
                     && mc.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4
                     && t1.U(this, var3, var12)
                     && b_.R(var6, mc.player.getYRot())
                     && b_.R(var6, ((q1881b7c262e73011)mc.player).larpYRotLast()),
                  var2x -> this.K(var2, var2x)
               );
               String var15 = this.Xo == ak.n.F ? "Opening the crafter at " : "Adjusting " + var6.getBlock().getName().getString() + " at ";
               this.e(var14 ? var15 + var3.toShortString() : "Cannot click " + var3.toShortString() + ": " + t1.L(this));
            }
         }
      }
   }

   private int K() {
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

   private void K(s_.k var1, boolean var2) {
      this.v0 = mc.player.tickCount;
      ak.n var3 = this.Xo;
      this.v1 = null;
      this.XD = null;
      this.Xo = ak.n.B;
      if (!var2) {
         com.larpclient.h.U(this.getName(), "Click on {} {} {} was not accepted", var1.x(), var1.y(), var1.z());
      } else {
         this.vX = var1;
         if (var3 == ak.n.F) {
            this.j2 = var1.id();
            this.X4 = mc.player.tickCount;
            this.v7 = -1000000000;
         } else {
            this.XR++;
         }
      }
   }

   private boolean y(ak.t var1) {
      int var2 = mc.player.tickCount;
      int var3 = this.j2;
      BlockPos var4 = var1.S(var3);
      this.K();
      if (mc.gui.screen() instanceof CrafterScreen var5) {
         if (this.v7 == -1000000000) {
            this.v7 = var2;
         }

         if (var2 - this.X4 > 100) {
            this.j2 = null;
            mc.player.closeContainer();
            this.E(var3, "could not set the crafter's slots");
            return true;
         }

         this.e("Setting the crafter's slots at " + var4.toShortString());
         if (var2 - this.v7 >= 6 && var2 - this.E9 >= 4) {
            CrafterMenu var10 = (CrafterMenu)var5.getMenu();
            int[] var7 = var1.C.get(var3);

            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = Arrays.binarySearch(var7, var8) >= 0 ? 1 : 0;
               if (var10.isSlotDisabled(var8) != var9) {
                  if (var9 != 0 && !var10.getSlot(var8).getItem().isEmpty()) {
                     this.j2 = null;
                     mc.player.closeContainer();
                     this.E(var3, "a crafter slot to disable holds an item");
                     return true;
                  }

                  var10.setSlotState(var8, var9 == 0);
                  mc.gameMode.handleSlotStateChanged(var8, var10.containerId, var9 == 0);
                  this.E9 = var2;
                  return true;
               }
            }

            this.je.add(var3);
            this.j2 = null;
            mc.player.closeContainer();
            this.v0 = var2;
            com.larpclient.h.A(this.getName(), "Crafter at {}: disabled slots {}", var4.toShortString(), Arrays.toString(var7));
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.X4 <= 50) {
         this.e("Waiting for the crafter at " + var4.toShortString() + " to open");
         return true;
      } else {
         this.j2 = null;
         this.E(var3, "the crafter did not open");
         return false;
      }
   }

   private int v(Item var1) {
      Inventory var2 = mc.player.getInventory();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var2.getItem(var3).is(var1)) {
            return var3;
         }
      }

      return -1;
   }

   private boolean j(int var1) {
      Inventory var2 = mc.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         var2.setSelectedSlot(var1);
         this.Xa = var1;
         this.vO = mc.player.tickCount;
         return false;
      }

      if (this.Xa != var1) {
         this.Xa = var1;
         this.vO = mc.player.tickCount;
      }

      return mc.player.tickCount - this.vO >= 3;
   }

   private void J(Item var1) {
      s_.k var2 = this.v1;
      if (!this.j1.getValue()) {
         this.z();
         this.n("put " + e(var1) + " in your hotbar (Refill Hotbar is off)");
         this.E(var2.id(), "not in the hotbar");
      } else if (this.H(var1) == -2) {
         this.z();
         if (this.s.startsWith("make room")) {
            this.n(this.s);
         }

         this.E(var2.id(), this.s.startsWith("make room") ? "no hotbar room" : this.s);
      }
   }

   private int H(Item var1) {
      int var2 = this.v(var1);
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
         this.s = "no " + e(var1) + " left";
         return -2;
      }

      int var11 = -1;

      for (int var6 = 0; var6 < 9 && var11 < 0; var6++) {
         if (var3.getItem(var6).isEmpty()) {
            var11 = var6;
         }
      }

      Map var12 = this.r();

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
         this.s = "make room in your hotbar for " + e(var1) + " (an empty slot, or a block)";
         return -2;
      }

      this.Xl = Vec3.ZERO;
      this.jl = (boolean)0;
      if (mc.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4) {
         this.Nn = -1000000000;
         this.e("Stopping to move a block into the hotbar");
         return -1;
      }

      if (this.Nn == -1000000000) {
         this.Nn = mc.player.tickCount;
      }

      if (mc.player.tickCount - this.Nn >= 4 && mc.player.tickCount - this.jO >= 4) {
         InventoryMenu var15 = mc.player.inventoryMenu;
         mc.gameMode.handleContainerInput(var15.containerId, var4, var11, ContainerInput.SWAP, mc.player);
         this.jO = mc.player.tickCount;
         this.Nn = -1000000000;
         com.larpclient.h.U(this.getName(), "Moved {} from inventory slot {} to hotbar slot {}", e(var1), var4, var11 + 1);
         this.e("Moved " + e(var1) + " into hotbar slot " + (var11 + 1));
         return -1;
      } else {
         this.e("Moving " + e(var1) + " into the hotbar");
         return -1;
      }
   }

   private Map<Item, Integer> r() {
      ak.t var1 = this.Eo;
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      }

      for (int var3 = 0; var3 < var1.U; var3++) {
         if (var1.i(var3) == b_.i.o && var1.g(var3) != null && !var1.v(var3)) {
            BlockItem var4 = b_.B(var1.g(var3));
            if (var4 != null) {
               var2.merge(var4, 1, Integer::sum);
            }
         }
      }

      return var2;
   }

   private void f(ak.t var1, s_.y var2) {
      BlockPos var3 = this.E();
      s_.k var4 = var2.toward();
      BlockPos var5 = new BlockPos(var4.x(), var4.y(), var4.z());
      if (!this.j_.i() || !var5.equals(this.vR) && mc.player.tickCount - this.jz > 20 || mc.player.tickCount - this.jz > 100) {
         this.jz = mc.player.tickCount;
         this.vR = var5;
         this.v9 = var2.walkingFor();
         zf.s var6 = new zf.s(
            Math.min(var1.E, var3.getX()) - 10,
            Math.min(var1.S, var3.getY()) - 6,
            Math.min(var1.v, var3.getZ()) - 10,
            Math.max(var1.m, var3.getX()) + 10,
            Math.max(var1.J, var3.getY()) + 6,
            Math.max(var1.k, var3.getZ()) + 10
         );
         zf.v var7 = zf.B(
            new c6(var0 -> -1, false),
            var3.getX(),
            var3.getY(),
            var3.getZ(),
            zf.f.k(var2.stands(), var4.x(), var4.y() - 1, var4.z(), this.j()),
            var6,
            zf.z.walking(16000)
         );
         if (!var7.found() || var7.steps().isEmpty()) {
            this.j_.H();
            if (this.Q()) {
               this.J(this.R);
               return;
            }

            com.larpclient.h.U(this.getName(), "No route to stand within reach of {} ({} cells searched)", var5.toShortString(), var7.explored());
            if (this.v3.getValue() && this.XZ == null && this.q(var1, var2)) {
               return;
            }

            if (this.v3.getValue() && this.XZ == null && this.k(var1, var2.walkingFor())) {
               return;
            }

            if (this.v3.getValue() && this.XZ == null && this.J(var1, var2)) {
               return;
            }

            for (s_.k var13 : var2.walkingFor()) {
               this.E(var13.id(), "no way to walk within reach");
            }

            return;
         }

         if (this.v() && this.R.cells().isEmpty() && this.O != null && var7.steps().stream().anyMatch(var1x -> var1x.y() < this.R.outY() - 1)) {
            zf.v var8 = zf.B(
               new c6(var0 -> -1, false),
               this.O.x(),
               this.O.ground(),
               this.O.z(),
               zf.f.k(var2.stands(), var4.x(), var4.y() - 1, var4.z(), this.j()),
               var6,
               zf.z.walking(16000)
            );
            if (var8.found()) {
               this.j_.H();
               com.larpclient.h.U(this.getName(), "The spot for {} needs a drop off the build; the tower comes down first", var5.toShortString());
               this.J(this.R);
               return;
            }
         }

         this.j_.M(var7.steps());
         this.j_.B();
         this.vk = 0.3 + this.NB.nextDouble() * 0.7;
         this.X2 = this.NB.nextDouble() * Math.PI * 2.0;
         BlockItem var11 = this.vJ.getOrDefault(var4.id(), ak.n.B) == ak.n.B ? b_.B(var1.g(var4.id())) : null;
         int var9 = var11 == null ? -1 : this.v(var11);
         if (var9 >= 0) {
            mc.player.getInventory().setSelectedSlot(var9);
         }

         zf.h var10 = var7.steps().getLast();
         this.XT = new BlockPos(var10.x(), var10.y(), var10.z());
         com.larpclient.h.U(this.getName(), "Route of {} steps to {} for {}", var7.steps().size(), this.XT.toShortString(), var5.toShortString());
      }

      this.u();
   }

   private void u() {
      gu.r var1 = this.j_.S(this.j(), var0 -> -1, this.Nf.getValue());
      this.Xl = this.j_.q();
      this.N1 = (boolean)(!this.j_.t() && !this.A() ? 0 : 1);
      this.jl = (boolean)(!this.j_.o() && (!this.En.getValue() || !this.R() || this.j_.e().isEmpty() || this.j_.e().getFirst().move() != zf.b.t) ? 0 : 1);
      this.jw = (boolean)(this.j_.O() && !this.jl ? 1 : 0);
      this.e("Walking to " + (this.vR == null ? "the next block" : this.vR.toShortString()));
      if (var1 == gu.r.c) {
         this.j_.H();
         this.vm = 0;
         this.M = 12;
         this.jd = (boolean)1;
         BlockPos var2 = this.E();
         this.jP.v(var2.getX(), var2.getY(), var2.getZ());
      } else if (var1 == gu.r.J) {
         com.larpclient.h.U(this.getName(), "Route stuck: {}", this.j_.d());
         this.j_.H();
         if (this.XZ == ak.l.C) {
            this.Q("stuck on the way to it");
            return;
         }

         if (this.Xv == ak.y.X) {
            this.B("stuck on the way to it");
            return;
         }

         if (this.Xv == ak.y.L) {
            return;
         }

         if (this.XT != null) {
            this.jP.D(this.XT.getX(), this.XT.getY(), this.XT.getZ());
         }

         if (++this.vm >= 3) {
            this.vm = 0;

            for (s_.k var3 : this.v9) {
               this.E(var3.id(), "stuck on the way");
            }
         }
      }
   }

   private void g() {
      this.M--;
      if (!this.L) {
         if (mc.player.getDeltaMovement().horizontalDistance() < 0.03) {
            this.M = 0;
         }

         this.e("Stopping");
      } else {
         if (this.M <= 0) {
            this.L = (boolean)0;
         }

         BlockPos var1 = this.E();
         Vec3 var2 = Vec3.atBottomCenterOf(var1);
         Vec3 var3 = var2.subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
         double var4 = mc.player.getDeltaMovement().horizontalDistance();
         if (var3.length() < 0.12 && var4 < 0.01) {
            this.M = 0;
            this.L = (boolean)0;
         } else {
            double var6 = var3.lengthSqr() < 1.0E-6 ? 0.0 : mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var3.normalize());
            this.jl = (boolean)1;
            this.Xl = this.XP.u(var3.length(), var6, ta.E(this)) ? var3 : Vec3.ZERO;
            this.e("Stopping on the spot");
         }
      }
   }

   private boolean A() {
      int var1 = mc.player.tickCount;
      if (this.j_.m() && this.j_.O() && mc.player.isSprinting() && mc.player.onGround()) {
         if (!mc.level.noCollision(mc.player, mc.player.getBoundingBox().move(0.0, 1.3, 0.0))) {
            return false;
         }

         if (this.jv != -1000000000 && var1 < this.jv) {
            this.N6++;
            return true;
         }

         if (this.N0 != -1000000000 && var1 < this.N0) {
            return false;
         }

         this.N0 = var1 + 30 + this.NB.nextInt(110);
         if (this.NB.nextInt(100) >= 60) {
            return false;
         }

         this.jv = var1 + 12 + this.NB.nextInt(45);
         this.N6++;
         return true;
      } else {
         return false;
      }
   }

   private void o() {
      float var1 = mc.player.getYRot();
      float var2 = Float.isNaN(this.Xt) ? 0.0F : Mth.wrapDegrees(var1 - this.Xt);
      this.Xt = var1;
      this.v4 = Math.max(this.v4, Math.abs(var2));
      if (Math.abs(var2) > 26.0F && this.ju < 12) {
         this.ju++;
         com.larpclient.h.A(
            this.getName(),
            "Sharp turn of {} degrees in one tick while: {} (rotation owner {})",
            String.format(Locale.ROOT, "%.1f", var2),
            this.getStatus(),
            com.larpclient.j6.j() && com.larpclient.j6.g(j6.l.o) ? "bot" : "other"
         );
      }

      if (Math.abs(var2) > 6.0F && Math.abs(this.jp) > 6.0F && Math.signum(var2) != Math.signum(this.jp)) {
         this.Xx++;
      }

      this.jp = var2;
   }

   private void c() {
      if (this.En.getValue() && mc.player.onGround() && this.R()) {
         this.jl = (boolean)1;
      }
   }

   private boolean R() {
      BlockPos var1 = this.E();

      for (Direction var3 : Plane.HORIZONTAL) {
         BlockPos var4 = var1.relative(var3);
         if (this.Z(var4) && this.Z(var4.below()) && this.Z(var4.below(2))) {
            return true;
         }
      }

      for (int[] var5 : new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}) {
         BlockPos var6 = var1.offset(var5[0], 0, var5[1]);
         if (this.Z(var6) && this.Z(var6.below()) && this.Z(var6.below(2))) {
            return true;
         }
      }

      return false;
   }

   private boolean Z(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getCollisionShape(mc.level, var1).isEmpty();
   }

   private Item p() {
      Item var1 = switch ((String)this.t.getValue()) {
         case "Cobblestone" -> Items.COBBLESTONE;
         case "Netherrack" -> Items.NETHERRACK;
         case "Cobbled Deepslate" -> Items.COBBLED_DEEPSLATE;
         default -> Items.DIRT;
      };
      return this.Eo != null && e(this.Eo, var1) ? null : var1;
   }

   private static boolean e(ak.t var0, Item var1) {
      for (BlockState var5 : var0.q) {
         if (var5 != null && b_.B(var5) == var1) {
            return true;
         }
      }

      return false;
   }

   private boolean k(ak.t var1, List<s_.k> var2) {
      if (this.Xv != null) {
         return false;
      }

      Item var3 = this.p();
      if (var3 == null) {
         this.n("the build uses " + this.t.getValue().toLowerCase(Locale.ROOT) + "; pick another Pillar Block for temporary pillars");
         return false;
      }

      BlockPos var4 = this.E();
      if (this.v5 > this.Nr) {
         this.El.clear();
      }

      HashSet var5 = new HashSet<>(this.w);
      var5.addAll(this.El);
      var2 = var2.stream().filter(var1x -> !this.j9.contains(var1x.id())).toList();
      if (var2.isEmpty()) {
         return false;
      } else {
         s_.a var6 = s_.R(
            new c6(var0 -> -1, false),
            var2,
            this.G(var1, 2),
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.j(),
            this.g.getValue().intValue(),
            var1::x,
            (var2x, var3x, var4x, var5x) -> this.c(var1, var2x, var3x, var4x, var5x),
            var5,
            600
         );
         if (var6 == null) {
            com.larpclient.h.U(this.getName(), "No pillar reaches {} blocks", var2.size());
            this.l(var1, (s_.k)var2.getFirst());
            return false;
         } else if (this.E(var3) < var6.height()) {
            this.G = var6.height();
            return true;
         } else {
            this.p = var6;
            this.XZ = ak.l.C;
            this.jV = this.Nk = mc.player.tickCount;
            this.j_.H();
            this.v9 = List.of();
            com.larpclient.h.U(this.getName(), "Planned a pillar of {} at {} {} {}", var6.height(), var6.x(), var6.ground(), var6.z());
            return true;
         }
      }
   }

   private void l(ak.t var1, s_.k var2) {
      if (this.XU.add(var2.id())) {
         BlockState var3 = var1.g(var2.id());
         ItemStack var4 = this.d(b_.B(var3));
         if (!var4.isEmpty()) {
            c6 var5 = new c6(var0 -> -1, false);
            BlockPos var6 = new BlockPos(var2.x(), var2.y(), var2.z());
            int var7 = (int)Math.floor(this.j());
            int var8 = this.g.getValue().intValue();
            TreeMap var9 = new TreeMap();
            String var10 = null;

            for (int var11 = -var7; var11 <= var7; var11++) {
               for (int var12 = -var7; var12 <= var7; var12++) {
                  if ((var11 != 0 || var12 != 0) && !(var11 * var11 + var12 * var12 > this.j() * this.j())) {
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
                           if (!s_.J(var5, var13, var15, var14, var24, var1::x, var2.y())) {
                              var9.merge("column taken", 1, Integer::sum);
                              if (var10 == null) {
                                 for (int var26 = var15; var26 <= var15 + var24 + 1 && var10 == null; var26++) {
                                    if (!var5.open(var13, var26, var14) || var1.x(var13, var26, var14)) {
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

                           String var17 = b_.R(this, mc.player, var4, var6, var3, new Vec3(var13 + 0.5, var15 + var24 + 1.62, var14 + 0.5), this.j() - 0.3);
                           var9.merge(var17 == null ? "would work" : var17, 1, Integer::sum);
                        }
                     }
                  }
               }
            }

            StringBuilder var19 = new StringBuilder();
            if (b_.W(var3)) {
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
                           String var18 = b_.R(this, mc.player, var4, var6, var3, new Vec3(var22 + 0.5, var25 + var27 + 1.62, var23 + 0.5), this.j() - 0.3);
                           var19.append(" h")
                              .append(var27)
                              .append('=')
                              .append(var18 == null ? "ok" : var18)
                              .append(s_.J(var5, var22, var25, var23, Math.max(1, var27), var1::x, var2.y()) ? "" : "(taken)");
                        }
                        break;
                     }
                  }
               }
            }

            if (var10 != null) {
               var19.append("; first column taken at ").append(var10);
            }

            com.larpclient.h.U(this.getName(), "Why no tower for {} at {}: {}{}", var3.getBlock().getName().getString(), var6.toShortString(), var9, var19);
         }
      }
   }

   private boolean c(ak.t var1, s_.k var2, int var3, int var4, int var5) {
      return (!this.I.contains(var2.id()) || !var1.f().containsColumn(var3, var5)) && this.z(var1, var2, var3, var4, var5);
   }

   private boolean J(ak.t var1, s_.y var2) {
      if (this.Xv == null && this.R == null) {
         Item var3 = this.p();
         if (var3 == null) {
            return false;
         }

         if (this.v5 > this.Nr) {
            this.El.clear();
         }

         HashSet var4 = new HashSet<>(this.w);
         var4.addAll(this.El);
         s_.a var5 = s_.O(new c6(var0 -> -1, false), var2.stands(), var2.walkingFor(), this.g.getValue().intValue(), var1::x, var4);
         if (var5 == null) {
            return false;
         } else if (this.E(var3) < var5.height()) {
            this.G = var5.height();
            return true;
         } else {
            this.p = var5;
            this.XZ = ak.l.C;
            this.jV = this.Nk = mc.player.tickCount;
            this.j_.H();
            this.v9 = List.of();
            this.R = new s_.j(List.of(), var5.x(), var5.topY(), var5.z(), List.copyOf(var2.walkingFor()), Set.of());
            this.vh.add(this.R);
            com.larpclient.h.A(
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

   private List<s_.k> G(ak.t var1, int var2) {
      ArrayList var3 = new ArrayList();
      MutableBlockPos var4 = new MutableBlockPos();
      int var5 = Math.min(this.Eq + var2, var1.a.length - 1);

      for (int var6 = var1.a[this.Eq]; var6 < var1.a[var5]; var6++) {
         int var7 = var1.K[var6];
         if (var1.g(var7) != null
            && var1.P[var1.y[var7]] == null
            && !var1.v(var7)
            && !this.ve.contains(var7)
            && !this.EU.contains(var7)
            && this.i(var1, var7, var4) == b_.i.o) {
            var3.add(new s_.k(var4.getX(), var4.getY(), var4.getZ(), var7));
         }
      }

      return var3;
   }

   private boolean Y(ak.t var1, c6 var2, BlockPos var3, double var4) {
      ArrayList var6 = new ArrayList();
      int var7 = var1.S + this.Eq;

      for (s_.k var9 : this.G(var1, 65)) {
         if (var9.y() > var7) {
            var6.add(var9);
         }
      }

      for (int var12 = var7 + 1; var12 <= var7 + 64; var12++) {
         int var13 = var12;
         List var10 = this.M(var1, var6.stream().filter(var1x -> var1x.y() == var13).toList());
         if (!var10.isEmpty()) {
            s_.y var11 = s_.l(
               var2,
               var10,
               this.I,
               var3.getX(),
               var3.getY(),
               var3.getZ(),
               var4,
               var1.f(),
               var2x -> this.z(var1, var2x),
               (var2x, var3x, var4x, var5) -> this.a(var1, var2x) != null,
               (var2x, var3x, var4x, var5) -> this.z(var1, var2x, var3x, var4x, var5),
               120,
               false,
               this.jP.e(),
               this.vX,
               false
            );
            if (var11.place() != null) {
               this.vJ.put(var11.place().id(), ak.n.B);
               this.e(var1, var11.place());
               return true;
            }
         }
      }

      return false;
   }

   private boolean b(ak.t var1) {
      Item var2 = this.p();
      if (var2 != null && this.v3.getValue()) {
         Block var3 = L(var2);
         BlockPos var4 = this.E();
         ArrayList var5 = new ArrayList();

         BlockPos var6;
         for (var6 = var4.below();
            var5.size() < 16 && mc.level.getBlockState(var6).is(var3) && !var1.x(var6.getX(), var6.getY(), var6.getZ()) && this.h(var6) >= 2;
            var6 = var6.below()
         ) {
            var5.add(var6);
         }

         if (var5.size() >= 3 && !this.Z(var6)) {
            Collections.reverse(var5);
            this.vB.clear();
            this.vB.addAll(var5);
            this.p = new s_.a(var4.getX(), var6.getY() + 1, var4.getZ(), var5.size(), List.of());
            this.XZ = ak.l.G;
            this.Ea = this.v5;
            this.Nb = this.v5;
            this.m("you are standing on a tower left from an earlier run; it is used and then dug away.");
            com.larpclient.h.A(this.getName(), "Took over the leftover tower of {} at {}", var5.size(), ((BlockPos)var5.getFirst()).toShortString());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int h(BlockPos var1) {
      int var2 = 0;

      for (Direction var4 : Plane.HORIZONTAL) {
         if (this.Z(var1.relative(var4))) {
            var2++;
         }
      }

      return var2;
   }

   private boolean d() {
      BlockPos var1 = this.E();
      return !s_.G(
         new c6(var0 -> -1, false),
         var1.getX(),
         var1.getY(),
         var1.getZ(),
         new u1.y(var1.getX() - 3, var1.getY() - 3, var1.getZ() - 3, var1.getX() + 3, var1.getY() + 3, var1.getZ() + 3)
      );
   }

   private boolean F(ak.t var1, List<s_.k> var2) {
      Item var3 = this.p();
      if (var3 != null && this.p != null && !var2.isEmpty()) {
         int var4 = s_.D(
            new c6(var0 -> -1, false),
            var2,
            this.p,
            this.g.getValue().intValue(),
            this.E(var3),
            var1::x,
            (var2x, var3x, var4x, var5) -> this.c(var1, var2x, var3x, var4x, var5)
         );
         if (var4 == 0) {
            return false;
         }

         com.larpclient.h.A(this.getName(), "Raising the pillar at {} {} from {} to {} for the next blocks", this.p.x(), this.p.z(), this.p.height(), var4);
         this.p = new s_.a(this.p.x(), this.p.ground(), this.p.z(), var4, this.p.unlocks());
         this.XZ = ak.l.f;
         this.Nk = mc.player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private boolean I(ak.t var1, List<s_.k> var2) {
      Item var3 = this.p();
      if (var3 != null && this.NR.size() < 24 && this.E(var3) != 0) {
         if (this.v5 != this.vx) {
            this.vx = this.v5;
            this.No = 0;
         }

         if (this.No >= 3) {
            return false;
         }

         this.No++;
         BlockPos var4 = this.E();
         ArrayList var5 = new ArrayList();

         for (s_.k var7 : this.G(var1, 2)) {
            if (this.z(var1, var7)) {
               var5.add(var7);
            }
         }

         HashSet var10 = new HashSet();

         for (BlockPos var8 : this.NR) {
            var10.add(zf.O(var8.getX(), 0, var8.getZ()));
         }

         if (this.p != null) {
            var10.add(zf.O(this.p.x(), 0, this.p.z()));
         }

         int[] var12 = s_.L(
            new c6(var0 -> -1, false),
            var5,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            3,
            this.j(),
            var1::x,
            (var2x, var3x, var4x, var5x) -> this.c(var1, var2x, var3x, var4x, var5x),
            var10
         );
         if (var12 == null) {
            return false;
         }

         for (Direction var9 : Plane.HORIZONTAL) {
            if (var9.getStepX() == var12[0] && var9.getStepZ() == var12[1]) {
               this.E = var9;
            }
         }

         this.N4 = var4.below().relative(this.E);
         this.XZ = ak.l.H;
         this.Xw = mc.player.tickCount;
         com.larpclient.h.A(
            this.getName(),
            "Stepping {} along the top instead of digging down, {} blocks still to place up here ({} walkway blocks so far)",
            this.E.getName(),
            var5.size(),
            this.NR.size()
         );
         return true;
      } else {
         return false;
      }
   }

   private void d(String var1) {
      com.larpclient.h.A(this.getName(), "Walkway stopped: {}", var1);
      t1.v(this);
      this.N4 = null;
      this.XZ = ak.l.G;
      this.h(var1);
   }

   private void N() {
      Item var1 = this.p();
      if (var1 == null || mc.player.tickCount - this.Xw > 120) {
         this.d("a walkway block did not go down");
      } else if (!t1.t(this.N4)) {
         this.NR.add(this.N4);
         this.XZ = ak.l.E;
         this.Xw = mc.player.tickCount;
      } else {
         this.jl = (boolean)1;
         if (mc.player.onGround()) {
            int var2 = this.H(var1);
            if (var2 == -2) {
               this.d("no " + e(var1) + " left");
            } else if (var2 >= 0 && this.j(var2)) {
               BlockPos var3 = this.N4.relative(this.E.getOpposite());
               Vec3 var4 = Vec3.atCenterOf(var3);
               double var5 = this.E.getStepX();
               double var7 = this.E.getStepZ();
               double var9 = (mc.player.getX() - var4.x) * var5 + (mc.player.getZ() - var4.z) * var7;
               double var11 = var7 != 0.0 ? var4.x - mc.player.getX() : 0.0;
               double var13 = var5 != 0.0 ? var4.z - mc.player.getZ() : 0.0;
               this.Xl = var9 < 0.66 ? new Vec3(var5 + var11, 0.0, var7 + var13) : new Vec3(var11, 0.0, var13);
               Vec3 var15 = mc.player.getEyePosition();
               Vec3 var16 = var4.add(var5 * 0.5, 0.3, var7 * 0.5);
               float var17 = this.E.getOpposite().toYRot();
               float var18 = (float)Math.toDegrees(Math.atan2(var15.y - var16.y, Math.max(var9 - 0.5, 0.08)));
               this.aimPlacement(var17, var18);
               this.e("Laying a walkway block");
               if (!(var9 < 0.54)) {
                  double var19 = this.j();
                  BlockHitResult var21 = t1.d(this, var15, var3, Vec3.directionFromRotation(var18, var17), var19);
                  if (var21 != null && var21.getDirection() == this.E) {
                     BlockPos var22 = this.N4;
                     t1.a(
                        this,
                        var22,
                        var21,
                        var1,
                        var19,
                        true,
                        () -> t1.t(var22) && !this.Z(),
                        () -> t1.U(this, var3, var19) && b_.p(mc.player, var21) && b_.C(mc.player, var3),
                        var1x -> this.v0 = mc.player.tickCount
                     );
                  }
               }
            } else {
               this.e("Taking out " + e(var1) + " for the walkway");
            }
         }
      }
   }

   private void U() {
      if (mc.player.tickCount - this.Xw > 120) {
         this.d("could not step onto the walkway");
      } else {
         this.jl = (boolean)1;
         this.e("Stepping along the walkway");
         Vec3 var1 = Vec3.atBottomCenterOf(this.N4.above()).subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
         if (var1.length() < 0.25) {
            this.N4 = null;
            this.XZ = ak.l.G;
            this.Ea = this.v5;
            this.jP.v();
         } else {
            this.Xl = var1;
         }
      }
   }

   private void V() {
      ak.t var1 = this.Eo;
      if (var1 == null) {
         this.NR.clear();
         this.XZ = ak.l.N;
      } else if (this.NR.isEmpty()) {
         l0.h(this);
         this.XZ = ak.l.N;
         this.Nk = mc.player.tickCount;
      } else if (mc.player.tickCount - this.Nk <= 80) {
         this.jl = (boolean)1;
         Item var2 = this.p();
         Block var3 = L(var2 == null ? Items.DIRT : var2);
         BlockPos var4 = this.NR.getLast();
         BlockPos var5 = this.NR.size() >= 2 ? this.NR.get(this.NR.size() - 2) : (this.vB.isEmpty() ? null : this.vB.getLast());
         if (var5 == null) {
            this.NR.clear();
         } else {
            Vec3 var6 = Vec3.atBottomCenterOf(var5.above()).subtract(mc.player.position()).multiply(1.0, 0.0, 1.0);
            if (mc.player.onGround() && mc.player.getY() < var5.getY() + 0.5) {
               l0.h(this);
               com.larpclient.h.A(
                  this.getName(),
                  "Fell off the walkway at {} with {} blocks left; sweeping them and the tower up instead",
                  this.E().toShortString(),
                  this.NR.size()
               );
               this.a(this.NR);
               this.a(this.vB);
               this.p = null;
               this.XZ = null;
            } else if (var6.length() > 0.25) {
               l0.h(this);
               this.e("Walking back along the walkway");
               this.Xl = var6;
            } else if (mc.level.getBlockState(var4).is(var3) && !var1.x(var4.getX(), var4.getY(), var4.getZ())) {
               if (!this.S(var4)) {
                  this.e("Cannot see the walkway block at " + var4.toShortString());
               }
            } else {
               this.NR.removeLast();
               this.Nk = mc.player.tickCount;
            }
         }
      } else {
         l0.h(this);
         com.larpclient.h.A(
            this.getName(),
            "Walkway could not be taken up from the top with {} blocks left at {}; sweeping them and the tower up instead",
            this.NR.size(),
            this.NR.getFirst().toShortString()
         );
         com.larpclient.h.A(
            this.getName(),
            "Walkway stall: player {}, next block {}, on ground {}, speed {}, rotation owner other than the bot {}, keys tick {}, screen {}",
            mc.player.position(),
            this.NR.getLast().toShortString(),
            mc.player.onGround(),
            mc.player.getDeltaMovement().horizontalDistance(),
            com.larpclient.j6.j() && !com.larpclient.j6.g(j6.l.o),
            this.jj == mc.player.tickCount,
            mc.gui.screen() != null
         );
         this.a(this.NR);
         this.a(this.vB);
         this.p = null;
         this.XZ = null;
      }
   }

   private boolean A(ak.t var1) {
      Item var2 = this.p();
      if (var2 == null || this.E(var2) >= this.G) {
         this.G = 0;
         return false;
      }

      if (this.NF.getValue() && this.Q(var1)) {
         return true;
      }

      if (this.Nm.getValue() && !this.XQ && !this.Nj.contains(var2) && this.c(var1)) {
         return true;
      }

      this.j("out of " + e(var2) + " for temporary blocks; carry at least " + this.G + (this.Nm.getValue() ? "" : " or turn on Buy Missing Blocks"));
      return true;
   }

   private boolean G(ak.t var1, s_.k var2) {
      BlockState var3 = var1.g(var2.id());
      BlockPos var4 = new BlockPos(var2.x(), var2.y(), var2.z());
      return var3.getBlock() instanceof FenceGateBlock
         ? true
         : !var3.hasBlockEntity() && !b_.X(var3) && !b_.Q(var3) && !b_.J(var3) && var3.isCollisionShapeFullBlock(mc.level, var4);
   }

   private boolean u(ak.t var1, List<s_.k> var2) {
      Item var3 = this.p();
      if (var3 != null && !this.y(((s_.k)var2.getFirst()).y())) {
         BlockPos var4 = this.E();
         s_.z var5 = s_.L(
            new c6(var0 -> -1, false),
            var2,
            var1::x,
            (var0, var1x, var2x) -> b_.g(new BlockPos(var0, var1x, var2x)),
            var2x -> this.G(var1, var2x),
            this.vP,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            var2x -> this.u(var1, var2x)
         );
         if (var5 == null) {
            return false;
         } else if (this.E(var3) < var5.height()) {
            this.G = var5.height();
            return true;
         } else {
            this.vQ = var5;
            this.Xv = ak.y.X;
            this.NC = this.NM = mc.player.tickCount;
            this.vT.clear();
            this.j_.H();
            this.v9 = List.of();
            com.larpclient.h.A(
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

   private boolean Y(List<s_.k> var1) {
      int var2 = mc.player.tickCount;
      if (this.v5 != this.X3 || var2 - this.v6 > 5) {
         this.X3 = this.v5;
         this.Ng = var2;
      }

      this.v6 = var2;
      if (var2 - this.Ng <= 1200) {
         return false;
      }

      this.Ng = var2;
      com.larpclient.h.A(this.getName(), "No block went down for {} ticks ({}); setting aside what it was trying", 1200, this.XZ);
      if (var1 != null) {
         for (s_.k var4 : var1) {
            this.E(var4.id(), "no progress");
         }
      }

      this.z();
      this.j_.H();
      if (this.p != null && this.XZ != null && this.XZ != ak.l.N && this.XZ != ak.l.j && this.XZ != ak.l.C) {
         this.h("no block went down for a minute");
      }

      return true;
   }

   private boolean B(ak.t var1) {
      if (this.i(var1, this.u, new MutableBlockPos()) == b_.i.t && mc.player.tickCount - this.jU <= 400) {
         this.e("Breaking a block that faces the wrong way");
         return this.S(this.jx);
      } else {
         l0.h(this);
         this.jx = null;
         return false;
      }
   }

   private boolean y(int var1) {
      if (this.v5 > this.XL) {
         this.jm.clear();
      }

      return this.jm.getOrDefault(var1, 0) >= 2;
   }

   private void L(int var1) {
      if (this.v5 > this.XL) {
         this.jm.clear();
      }

      this.XL = this.v5;
      if (this.jm.merge(var1, 1, Integer::sum) == 2) {
         com.larpclient.h.A(this.getName(), "Two temporary supports in a row gave nothing at Y {}; no more there until a block of the build goes down", var1);
      }
   }

   private boolean u(ak.t var1, s_.z var2) {
      BlockPos var3 = this.E();
      s_.k var4 = var2.seed();
      if (this.w(var2, var3.getX(), var3.getY(), var3.getZ(), this.XZ == ak.l.G)) {
         return true;
      }

      if (this.XZ == ak.l.G) {
         return false;
      }

      c6 var5 = new c6(var0 -> -1, false);
      Set var6 = s_.N(var5, var4, this.j(), var1.f(), false, (var2x, var3x, var4x, var5x) -> this.w(var2, var3x, var4x, var5x, false), Set.of());
      if (var6.isEmpty()) {
         return false;
      }

      zf.s var7 = new zf.s(
         Math.min(var1.E, var3.getX()) - 10,
         Math.min(var1.S, var3.getY()) - 6,
         Math.min(var1.v, var3.getZ()) - 10,
         Math.max(var1.m, var3.getX()) + 10,
         Math.max(var1.J, var3.getY()) + 6,
         Math.max(var1.k, var3.getZ()) + 10
      );
      return zf.B(var5, var3.getX(), var3.getY(), var3.getZ(), zf.f.k(var6, var4.x(), var4.y() - 1, var4.z(), this.j()), var7, zf.z.walking(16000)).found();
   }

   private boolean w(s_.z var1, int var2, int var3, int var4, boolean var5) {
      Item var6 = this.p();
      if (var6 != null && s_.y(var1, var2, var3, var4)) {
         BlockState var7 = this.Eo == null ? null : this.Eo.g(var1.seed().id());
         if (var7 != null && var7.getBlock() instanceof FenceGateBlock) {
            Direction var8 = (Direction)var7.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (var1.x() - var1.seed().x() != var8.getStepX() || var1.z() - var1.seed().z() != var8.getStepZ()) {
               return false;
            }
         }

         double var14 = this.j() - 0.3;
         if (s_.D(var2, var3, var4, var1.seed()) > var14 * var14) {
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

         return this.a(var6, new BlockPos(var1.x(), var1.ground(), var1.z()), new Vec3(var2 + 0.5, var3 + 1.62, var4 + 0.5), var14, true) != null;
      } else {
         return false;
      }
   }

   private boolean b(ak.t var1, List<s_.k> var2) {
      Item var3 = this.p();
      if (var3 != null && !this.y(((s_.k)var2.getFirst()).y())) {
         BlockPos var4 = this.E();
         if (this.v5 > this.Nr) {
            this.El.clear();
         }

         HashSet var5 = new HashSet<>(this.w);
         var5.addAll(this.El);
         s_.a var6 = s_.O(
            new c6(var0 -> -1, false),
            var2,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.j(),
            this.g.getValue().intValue(),
            var1::x,
            (var0, var1x, var2x) -> b_.g(new BlockPos(var0, var1x, var2x)),
            var2x -> this.G(var1, var2x),
            (var1x, var2x, var3x, var4x) -> this.w(var1x, var2x, var3x, var4x, true),
            this.vP,
            var5,
            600
         );
         if (var6 == null) {
            return false;
         } else if (this.E(var3) < var6.height() + 3) {
            this.G = var6.height() + 3;
            return true;
         } else {
            this.p = var6;
            this.Xr = (boolean)1;
            this.jG = ((s_.k)var2.getFirst()).y();
            this.XZ = ak.l.C;
            this.jV = this.Nk = mc.player.tickCount;
            this.j_.H();
            this.v9 = List.of();
            com.larpclient.h.A(
               this.getName(),
               "Layer Y {} is too high to start from the floor: tower of {} at {} {} {} to stand on",
               ((s_.k)var2.getFirst()).y(),
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

   private boolean I(ak.t var1) {
      switch (this.Xv) {
         case X:
            this.A(var1);
            break;
         case A:
            this.V(var1);
            break;
         case W:
            s_.k var2 = this.vQ.seed();
            int var3 = this.i(var1, var2.id(), new MutableBlockPos()) != b_.i.o ? 1 : 0;
            if (var3 == 0 && !this.vs.containsKey(var2.id()) && !this.ve.contains(var2.id()) && mc.player.tickCount - this.NC <= 400) {
               return false;
            }

            if (var3 == 0) {
               this.vP.add(zf.O(var2.x(), var2.y(), var2.z()));
               this.L(var2.y());
            }

            this.Xv = ak.y.L;
            this.NM = mc.player.tickCount;
            this.h(var1);
            break;
         case L:
            this.h(var1);
      }

      return true;
   }

   private void B(String var1) {
      s_.k var2 = this.vQ.seed();
      com.larpclient.h.A(this.getName(), "Temporary support at {} {} {} left out: {}", this.vQ.x(), this.vQ.ground(), this.vQ.z(), var1);
      this.vP.add(zf.O(var2.x(), var2.y(), var2.z()));
      this.L(var2.y());
      this.j_.H();
      t1.v(this);
      if (this.vT.isEmpty()) {
         this.vQ = null;
         this.Xv = null;
      } else {
         this.Xv = ak.y.L;
         this.NM = mc.player.tickCount;
      }
   }

   private BlockPos d() {
      return new BlockPos(this.vQ.x(), this.vQ.ground() + this.vT.size(), this.vQ.z());
   }

   private BlockHitResult a(Item var1, BlockPos var2, Vec3 var3, double var4, boolean var6) {
      ItemStack var7 = this.d(var1);
      return var7.isEmpty() ? null : b_.s(this, mc.player, var7, var2, L(var1).defaultBlockState(), var3, var4, true, var6);
   }

   private boolean y() {
      return mc.player.getBoundingBox().intersects(new AABB(this.vQ.x(), this.vQ.ground(), this.vQ.z(), this.vQ.x() + 1, this.vQ.top() + 1, this.vQ.z() + 1));
   }

   private void A(ak.t var1) {
      if (mc.player.tickCount - this.NC > 400) {
         this.B("the walk took too long");
      } else {
         Item var2 = this.p();
         if (var2 == null) {
            this.B("the block is used by the build");
         } else {
            BlockPos var3 = this.d();
            if (this.j_.i()) {
               this.u();
            } else if (!this.y() && this.a(var2, var3, mc.player.getEyePosition(), this.j(), false) != null) {
               this.Xv = ak.y.A;
               this.NM = mc.player.tickCount;
            } else if (this.XZ == ak.l.G) {
               this.B("no click from the tower's top");
            } else {
               BlockPos var4 = this.E();
               s_.k var5 = this.vQ.seed();
               double var6 = this.j() - 0.3;
               Set var8 = s_.N(
                  new c6(var0 -> -1, false),
                  new s_.k(var3.getX(), var3.getY(), var3.getZ(), -1),
                  this.j(),
                  var1.f(),
                  false,
                  (var6x, var7, var8x, var9) -> (var7 != this.vQ.x() || var9 != this.vQ.z())
                     && s_.D(var7, var8x, var9, var5) <= var6 * var6
                     && this.a(var2, var3, new Vec3(var7 + 0.5, var8x + 1.62, var9 + 0.5), var6, true) != null,
                  Set.of()
               );
               var8.remove(zf.O(var4.getX(), var4.getY(), var4.getZ()));
               if (var8.isEmpty()) {
                  this.B("no spot to place it from");
               } else if (!this.c(var1, var8, var3)) {
                  this.B("no way to walk to it");
               } else {
                  this.u();
               }
            }
         }
      }
   }

   private boolean c(ak.t var1, Set<Long> var2, BlockPos var3) {
      BlockPos var4 = this.E();
      zf.s var5 = new zf.s(
         Math.min(var1.E, var4.getX()) - 10,
         Math.min(var1.S, var4.getY()) - 6,
         Math.min(var1.v, var4.getZ()) - 10,
         Math.max(var1.m, var4.getX()) + 10,
         Math.max(var1.J, var4.getY()) + 6,
         Math.max(var1.k, var4.getZ()) + 10
      );
      zf.v var6 = zf.B(
         new c6(var0 -> -1, false),
         var4.getX(),
         var4.getY(),
         var4.getZ(),
         zf.f.k(var2, var3.getX(), var3.getY() - 1, var3.getZ(), this.j()),
         var5,
         zf.z.walking(16000)
      );
      if (var6.found() && !var6.steps().isEmpty()) {
         this.j_.M(var6.steps());
         this.vR = var3;
         zf.h var7 = var6.steps().getLast();
         this.XT = new BlockPos(var7.x(), var7.y(), var7.z());
         return true;
      } else {
         return false;
      }
   }

   private void V(ak.t var1) {
      Item var2 = this.p();
      if (var2 == null) {
         this.B("the block is used by the build");
      } else {
         Block var3 = L(var2);

         while (!this.vT.isEmpty() && !mc.level.getBlockState(this.vT.getLast()).is(var3)) {
            this.vT.removeLast();
         }

         if (this.vT.size() >= this.vQ.height()) {
            this.Xv = ak.y.W;
            this.NC = mc.player.tickCount;
            this.jP.v();
         } else if (mc.player.tickCount - this.NM > 40) {
            this.B("it did not go down");
         } else {
            int var4 = this.H(var2);
            if (var4 == -2) {
               this.B("no " + e(var2) + " left");
            } else if (var4 >= 0 && this.j(var4)) {
               this.c();
               BlockPos var5 = this.d();
               BlockHitResult var6 = this.y() ? null : this.a(var2, var5, mc.player.getEyePosition(), this.j(), false);
               if (var6 == null) {
                  this.Xv = ak.y.X;
               } else {
                  BlockPos var7 = var6.getBlockPos();
                  this.Y(var7);
                  double var8 = this.j();
                  t1.a(
                     this,
                     var5,
                     var6,
                     var2,
                     var8,
                     true,
                     () -> t1.t(var5) && !this.Z(),
                     () -> t1.U(this, var7, var8) && b_.p(mc.player, var6) && b_.C(mc.player, var7) && this.g(var7),
                     var2x -> {
                        this.v0 = mc.player.tickCount;
                        if (var2x && !this.vT.contains(var5)) {
                           this.vT.add(var5);
                           this.NM = mc.player.tickCount;
                        }
                     }
                  );
                  this.e("Placing a temporary block at " + var5.toShortString());
               }
            } else {
               this.e("Taking out " + e(var2) + " for a temporary block");
            }
         }
      }
   }

   private void h(ak.t var1) {
      Item var2 = this.p();

      for (Block var3 = L(var2 == null ? Items.DIRT : var2);
         !this.vT.isEmpty() && !mc.level.getBlockState(this.vT.getLast()).is(var3);
         this.NM = mc.player.tickCount
      ) {
         this.vT.removeLast();
      }

      if (this.vT.isEmpty()) {
         l0.h(this);
         this.vQ = null;
         this.Xv = null;
         this.jP.v();
         this.e("Temporary block removed");
      } else if (this.j_.i()) {
         this.u();
      } else if (mc.player.tickCount - this.NM > 200) {
         l0.h(this);
         com.larpclient.h.A(this.getName(), "Could not dig the temporary block at {} right away; sweeping it up instead", this.vT.getFirst().toShortString());
         this.a(this.vT);
         this.vQ = null;
         this.Xv = null;
      } else {
         this.c();
         BlockPos var4 = this.vT.getLast();
         if (!this.S(var4)) {
            if (this.XZ != ak.l.G) {
               BlockPos var5 = this.E();
               Set var6 = s_.N(
                  new c6(var0 -> -1, false),
                  new s_.k(var4.getX(), var4.getY(), var4.getZ(), -1),
                  this.j(),
                  var1.f(),
                  false,
                  (var1x, var2x, var3x, var4x) -> var2x != var4.getX() || var4x != var4.getZ(),
                  Set.of()
               );
               var6.remove(zf.O(var5.getX(), var5.getY(), var5.getZ()));
               if (!var6.isEmpty() && this.c(var1, var6, var4)) {
                  this.u();
               }
            }
         }
      }
   }

   private String D(ak.t var1, s_.k var2) {
      BlockState var3 = var1.g(var2.id());
      ItemStack var4 = this.d(b_.B(var3));
      if (var4.isEmpty()) {
         return "none carried";
      }

      BlockPos var5 = new BlockPos(var2.x(), var2.y(), var2.z());
      if (mc.player.getBoundingBox().intersects(new AABB(var5))) {
         return "the body is in the cell";
      }

      String var6 = b_.R(this, mc.player, var4, var5, var3, mc.player.getEyePosition(), this.j());
      return var6 == null ? "steady look missed" : var6;
   }

   private String g() {
      ak.t var1 = this.Eo;
      if (var1 != null && this.p != null) {
         StringBuilder var2 = new StringBuilder();
         MutableBlockPos var3 = new MutableBlockPos();

         for (s_.k var5 : this.p.unlocks()) {
            if (this.i(var1, var5.id(), var3) == b_.i.o) {
               String var6 = !this.vs.containsKey(var5.id()) && !this.ve.contains(var5.id())
                  ? (this.a(var1, var5) == null ? "no click from here: " + this.D(var1, var5) : "click was possible")
                  : "set aside";
               var2.append(var2.isEmpty() ? "; planned but not placed: " : ", ")
                  .append(var1.g(var5.id()).getBlock().getName().getString())
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

   private void a(List<BlockPos> var1) {
      Item var2 = this.p();
      Block var3 = L(var2 == null ? Items.DIRT : var2);

      for (BlockPos var5 : var1) {
         if (mc.level.getBlockState(var5).is(var3) && (this.Eo == null || !this.Eo.x(var5.getX(), var5.getY(), var5.getZ())) && !this.i.contains(var5)) {
            this.i.add(var5);
         }
      }

      var1.clear();
   }

   private void v(ak.t var1) {
      Item var2 = this.p();
      Block var3 = L(var2 == null ? Items.DIRT : var2);
      this.i.removeIf(var1x -> !mc.level.getBlockState(var1x).is(var3));
      if (this.i.isEmpty()) {
         l0.h(this);
         this.a = null;
      } else if (this.j_.i()) {
         this.u();
      } else {
         if (this.a == null || !this.i.contains(this.a)) {
            this.a = this.i.stream().max(Comparator.comparingInt(Vec3i::getY)).orElseThrow();
            this.Nd = mc.player.tickCount;
         }

         if (mc.player.tickCount - this.Nd > 160) {
            l0.h(this);
            this.m("could not reach the temporary block at " + this.a.toShortString() + "; break it by hand.");
            com.larpclient.h.A(this.getName(), "Sweep gave up on the temporary block at {}", this.a.toShortString());
            l0.r(this.getName(), this.a);
            this.i.remove(this.a);
            this.a = null;
         } else {
            this.e("Removing the temporary blocks left standing");
            if (!this.S(this.a)) {
               BlockPos var4 = this.E();
               BlockPos var5 = this.a;
               Set var6 = s_.N(
                  new c6(var0 -> -1, false),
                  new s_.k(var5.getX(), var5.getY(), var5.getZ(), -1),
                  this.j(),
                  var1.f(),
                  false,
                  (var2x, var3x, var4x, var5x) -> (var3x != var5.getX() || var5x != var5.getZ()) && this.v(var3x, var4x, var5x, var5),
                  Set.of()
               );
               var6.remove(zf.O(var4.getX(), var4.getY(), var4.getZ()));
               if (!var6.isEmpty() && this.c(var1, var6, var5) || !this.b(var1, var5)) {
                  if (!var6.isEmpty() && (!this.j_.e().isEmpty() || this.c(var1, var6, var5))) {
                     this.u();
                  } else {
                     this.Nd = mc.player.tickCount - 160 - 1;
                  }
               }
            }
         }
      }
   }

   private boolean b(ak.t var1, BlockPos var2) {
      Item var3 = this.p();
      if (var3 != null && this.v3.getValue() && this.E(var3) != 0 && !this.Xn.contains(var2)) {
         this.Xn.add(var2);
         BlockPos var4 = this.E();
         s_.k var5 = new s_.k(var2.getX(), var2.getY(), var2.getZ(), -1);
         s_.a var6 = s_.I(
            new c6(var0 -> -1, false),
            List.of(var5),
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.j(),
            this.g.getValue().intValue(),
            var1::x,
            (var2x, var3x, var4x, var5x) -> (var3x != var2.getX() || var5x != var2.getZ()) && this.v(var3x, var4x, var5x, var2),
            this.w,
            600
         );
         if (var6 != null && this.E(var3) >= var6.height()) {
            this.j_.H();
            this.p = new s_.a(var6.x(), var6.ground(), var6.z(), var6.height(), List.of());
            this.XZ = ak.l.C;
            this.jV = this.Nk = mc.player.tickCount;
            this.XG = (boolean)1;
            com.larpclient.h.A(
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

   private void E() {
      Item var1 = this.p();
      Block var2 = L(var1 == null ? Items.DIRT : var1);
      this.i.removeIf(var1x -> !mc.level.getBlockState(var1x).is(var2));
      BlockPos var3 = this.E();
      ArrayList var4 = new ArrayList<>(this.i);
      var4.sort(Comparator.<BlockPos>comparingInt(var0 -> var0.getY()).reversed());

      for (BlockPos var6 : var4) {
         if ((var6.getX() != var3.getX() || var6.getZ() != var3.getZ()) && this.v(var3.getX(), var3.getY(), var3.getZ(), var6)) {
            if (!var6.equals(this.a)) {
               this.a = var6;
               this.Nd = mc.player.tickCount;
            }

            if (mc.player.tickCount - this.Nd > 160) {
               l0.h(this);
               com.larpclient.h.A(this.getName(), "Sweep gave up on the temporary block at {} from a tower", var6.toShortString());
               l0.r(this.getName(), var6);
               this.m("could not break the temporary block at " + var6.toShortString() + "; break it by hand.");
               this.i.remove(var6);
               this.a = null;
               return;
            }

            this.e("Removing the temporary blocks left standing");
            if (this.S(var6)) {
               return;
            }
         }
      }

      this.XG = (boolean)0;
      this.h("the temporary blocks in reach are gone");
   }

   private boolean v(int var1, int var2, int var3, BlockPos var4) {
      Vec3 var5 = new Vec3(var1 + 0.5, var2 + 1.62, var3 + 0.5);
      double var6 = this.j() - 0.3;

      for (Direction var11 : Direction.values()) {
         Vec3 var12 = Vec3.atCenterOf(var4).add(var11.getStepX() * 0.5, var11.getStepY() * 0.5, var11.getStepZ() * 0.5);
         Vec3 var13 = new Vec3(var11.getStepX(), var11.getStepY(), var11.getStepZ());
         if (!(var5.subtract(var12).dot(var13) <= 0.02)
            && !(var5.distanceToSqr(var12) > var6 * var6)
            && t1.d(this, var5, var4, var12.subtract(var5), var6) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean S(BlockPos var1) {
      return l0.z(this, var1, this.j(), true, 12.0F, l0::W, () -> !this.Z());
   }

   private void Q(String var1) {
      com.larpclient.h.U(this.getName(), "Pillar at {} {}: {}", this.p.x(), this.p.z(), var1);
      this.XG = (boolean)0;
      if (this.R != null && this.Ni.isEmpty()) {
         if (this.R.cells().isEmpty()) {
            this.vh.remove(this.R);
         }

         this.R = null;
      }

      this.w.add(zf.O(this.p.x(), 0, this.p.z()));
      this.j_.H();
      t1.v(this);
      if (this.vB.isEmpty()) {
         if (this.Xr) {
            this.L(this.jG);
            this.Xr = (boolean)0;
         }

         this.p = null;
         this.XZ = null;
      } else {
         this.h(var1);
      }
   }

   private void F(ak.t var1) {
      BlockPos var2 = this.E();
      BlockPos var3 = new BlockPos(this.p.x(), this.p.ground(), this.p.z());
      if (mc.player.tickCount - this.jV > 600) {
         this.Q("the walk took too long");
      } else if (var2.equals(var3) && !this.j_.i()) {
         this.XZ = ak.l.f;
         this.Nk = mc.player.tickCount;
         this.Xy++;
         this.Nb = this.v5;
         com.larpclient.h.A(
            this.getName(),
            "Pillar of {} at {} {} {} for {} blocks now, in reach of more above",
            this.p.height(),
            this.p.x(),
            this.p.ground(),
            this.p.z(),
            this.p.unlocks().size()
         );
         this.e("Building a pillar of " + this.p.height());
      } else {
         if (!this.j_.i()) {
            zf.s var4 = new zf.s(
               Math.min(var1.E, var2.getX()) - 10,
               Math.min(var1.S, var2.getY()) - 6,
               Math.min(var1.v, var2.getZ()) - 10,
               Math.max(var1.m, var2.getX()) + 10,
               Math.max(var1.J, var2.getY()) + 6,
               Math.max(var1.k, var2.getZ()) + 10
            );
            zf.v var5 = zf.B(
               new c6(var0 -> -1, false),
               var2.getX(),
               var2.getY(),
               var2.getZ(),
               zf.f.k(Set.of(zf.O(var3.getX(), var3.getY(), var3.getZ())), var3.getX(), var3.getY(), var3.getZ(), 0.0),
               var4,
               zf.z.walking(16000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.Q("no way to walk to it");
               return;
            }

            this.j_.M(var5.steps());
            this.vR = var3;
            this.XT = var3;
         }

         this.u();
      }
   }

   private void f(ak.t var1) {
      Item var2 = this.p();
      int var3 = this.n();
      if (var2 == null) {
         this.Q("the pillar block is used by the build");
      } else if (var3 >= this.p.height()) {
         if (!mc.player.onGround()) {
            this.e("Landing on the pillar");
         } else {
            this.XZ = ak.l.G;
            this.Ea = this.v5;
            this.jP.v();
            com.larpclient.h.U(this.getName(), "On a pillar of {} at {} {}", var3, this.p.x(), this.p.z());
         }
      } else if (mc.player.tickCount - this.Nk > 80) {
         this.Q("a pillar block did not go down");
      } else {
         BlockPos var4 = new BlockPos(this.p.x(), this.p.ground() + var3, this.p.z());
         BlockPos var5 = var4.below();
         int var6 = this.H(var2);
         if (var6 == -2) {
            this.Q("no " + e(var2) + " left");
         } else if (var6 >= 0 && this.j(var6)) {
            this.jl = (boolean)(var3 > 0 ? 1 : 0);
            Vec3 var7 = new Vec3(var4.getX() + 0.5, var4.getY(), var4.getZ() + 0.5);
            float[] var8 = com.larpclient.j6.s(mc.player.getEyePosition(), var7);
            this.aimPlacement(var8[0], var8[1]);
            double var9 = this.j();
            if (!mc.player.onGround()) {
               if (!(mc.player.getY() < var4.getY() + 1.02)) {
                  BlockHitResult var11 = new BlockHitResult(var7, Direction.UP, var5, false);
                  t1.a(
                     this,
                     var4,
                     var11,
                     var2,
                     var9,
                     true,
                     () -> t1.t(var4) && !this.Z(),
                     () -> t1.U(this, var5, var9) && mc.player.getY() >= var4.getY() + 1.02,
                     var2x -> {
                        this.v0 = mc.player.tickCount;
                        if (var2x) {
                           this.vB.add(var4);
                           this.Nk = mc.player.tickCount;
                        }
                     }
                  );
               }
            } else {
               if (t1.U(this, var5, var9) && (var3 == 0 || mc.level.getBlockState(var5).is(L(var2)))) {
                  this.N1 = (boolean)1;
               }

               this.e("Building a pillar: " + var3 + " of " + this.p.height());
            }
         } else {
            this.e("Taking out " + e(var2) + " for a pillar");
         }
      }
   }

   private static Block L(Item var0) {
      return var0 instanceof BlockItem var1 ? var1.getBlock() : null;
   }

   private int n() {
      Block var1 = L(this.p() == null ? Items.DIRT : this.p());

      while (!this.vB.isEmpty() && !mc.level.getBlockState(this.vB.getLast()).is(var1)) {
         this.vB.removeLast();
      }

      return this.vB.size();
   }

   private void h(String var1) {
      t1.v(this);
      this.j_.H();
      this.XZ = this.NR.isEmpty() ? ak.l.N : ak.l.j;
      this.Nk = mc.player.tickCount;
      if (this.p != null) {
         this.El.add(zf.O(this.p.x(), 0, this.p.z()));
         this.Nr = this.v5;
      }

      if (this.v5 == this.Ea && this.p != null) {
         this.w.add(zf.O(this.p.x(), 0, this.p.z()));
      }

      if (this.p != null && this.Eo != null && this.v5 == this.Ea) {
         MutableBlockPos var2 = new MutableBlockPos();

         for (s_.k var4 : this.p.unlocks()) {
            if (var4.id() >= 0 && this.i(this.Eo, var4.id(), var2) == b_.i.o) {
               this.j9.add(var4.id());
            }
         }
      }

      if (this.Xr && this.v5 == this.Nb) {
         this.L(this.jG);
      }

      this.Xr = (boolean)0;
      if (this.p != null) {
         com.larpclient.h.A(
            this.getName(),
            "Tower at {} {} comes down after {} blocks, {} walkway blocks: {}{}",
            this.p.x(),
            this.p.z(),
            this.v5 - this.Nb,
            this.NR.size(),
            var1,
            this.g()
         );
      }
   }

   private void Y() {
      Item var1 = this.p();

      for (Block var2 = L(var1 == null ? Items.DIRT : var1);
         !this.vB.isEmpty() && !mc.level.getBlockState(this.vB.getLast()).is(var2);
         this.Nk = mc.player.tickCount
      ) {
         this.vB.removeLast();
      }

      if (this.vB.isEmpty()) {
         l0.h(this);
         this.p = null;
         this.XZ = null;
         this.jP.v();
         this.M = 12;
         this.e("Pillar removed");
      } else if (!mc.player.onGround() && !mc.player.isInWater()) {
         this.e("Coming down");
      } else if (mc.player.tickCount - this.Nk > 200) {
         l0.h(this);
         BlockPos var4 = this.vB.getFirst();
         com.larpclient.h.A(
            this.getName(), "Could not dig the pillar at {} from its top with {} blocks left; sweeping them up instead", var4.toShortString(), this.vB.size()
         );
         this.a(this.vB);
         this.p = null;
         this.XZ = null;
      } else {
         this.jl = (boolean)1;
         BlockPos var3 = this.vB.getLast();
         if (!this.S(var3)) {
            this.L(var3);
         }
      }
   }

   private void L(BlockPos var1) {
      if (this.XA.add(var1.asLong())) {
         com.larpclient.h.A(
            this.getName(),
            "Miner refused {}: wait {}, allowed {}, breakable {}, seen {}, state {}, player {} eye {}, block above feet {}, at head {}",
            var1.toShortString(),
            this.e(),
            ta.S(this),
            l0.B(var1),
            l0.L(this, var1, this.j()) != null,
            mc.level.getBlockState(var1).getBlock().getName().getString(),
            mc.player.position(),
            mc.player.getEyePosition(),
            mc.level.getBlockState(mc.player.blockPosition()).getBlock().getName().getString(),
            mc.level.getBlockState(mc.player.blockPosition().above()).getBlock().getName().getString()
         );
      }
   }

   private static boolean p(BlockState var0) {
      Block var1 = var0.getBlock();
      return var1 instanceof ChestBlock || var1 instanceof BarrelBlock || var1 instanceof ShulkerBoxBlock;
   }

   private String W() {
      return mc.level.dimension().identifier().toString();
   }

   private String C() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (mc.hitResult instanceof BlockHitResult var1 && var1.getType() == Type.BLOCK && p(mc.level.getBlockState(var1.getBlockPos()))) {
         BlockPos var7 = var1.getBlockPos();
         ak.f var3 = new ak.f(this.W(), var7.getX(), var7.getY(), var7.getZ());
         List var4 = s(this.C.getValue());
         boolean var5 = var4.remove(var3);
         if (!var5) {
            var4.add(var3);
         }

         this.C.k(g(var4));
         this.v();
         String var6 = mc.level.getBlockState(var7).getBlock().getName().getString();
         com.larpclient.h.A(this.getName(), "{} restock container {} at {} ({} marked)", var5 ? "Unmarked" : "Marked", var6, var7.toShortString(), var4.size());
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

   private String z() {
      int var1 = s(this.C.getValue()).size();
      this.C.k("");
      this.v();
      return var1 == 0 ? "No containers were marked." : "Cleared " + var1 + " restock " + (var1 == 1 ? "mark." : "marks.");
   }

   private void i() {
      int var1 = this.j3.getValue() != -1
            && this.isInGame()
            && mc.gui.screen() == null
            && mc.isWindowActive()
            && bc.I(mc.getWindow().handle(), this.j3.getValue())
         ? 1
         : 0;
      if (var1 != 0 && !this.D) {
         this.m(this.C());
      }

      this.D = (boolean)var1;
   }

   private boolean Q(ak.t var1) {
      this.Na.clear();

      for (Entry var3 : this.h(var1).entrySet()) {
         int var4 = (Integer)var3.getValue() - this.E((Item)var3.getKey());
         if (var4 > 0) {
            this.Na.put((Item)var3.getKey(), var4);
         }
      }

      Item var11 = this.v3.getValue() ? this.p() : null;
      if (this.G > 0 && var11 != null && this.E(var11) < 32) {
         this.Na.put(var11, 32 - this.E(var11));
      }

      if (this.Na.isEmpty()) {
         return false;
      }

      if (var11 != null && this.E(var11) < 32) {
         this.Na.put(var11, 32 - this.E(var11));
      }

      ak.f var12 = null;
      double var13 = this.jQ.getValue() * this.jQ.getValue();
      String var6 = this.W();

      for (ak.f var8 : s(this.C.getValue())) {
         if (var8.dimension().equals(var6) && !this.vj.contains(var8)) {
            double var9 = mc.player.distanceToSqr(Vec3.atCenterOf(var8.pos()));
            if (!(var9 > var13)) {
               if (mc.level.hasChunkAt(var8.pos()) && !p(mc.level.getBlockState(var8.pos()))) {
                  this.vj.add(var8);
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

      this.Xq = var12;
      this.N8 = ak.k.v;
      this.T = mc.player.tickCount;
      this.vz = this.Z = 0;
      this.Eh.clear();
      this.j_.H();
      this.v9 = List.of();
      t1.v(this);
      ArrayList var14 = new ArrayList();

      for (Entry var16 : this.Na.entrySet()) {
         var14.add(var16.getValue() + " " + e((Item)var16.getKey()));
      }

      com.larpclient.h.A(this.getName(), "Restock trip to {} for {}", var12.pos().toShortString(), String.join(", ", var14));
      this.e("Going to the restock container at " + var12.pos().toShortString());
      return true;
   }

   private void I(String var1) {
      if (this.vF) {
         this.W(var1);
      } else {
         if (this.vz == 0 && this.Xq != null) {
            this.vj.add(this.Xq);
         } else if (this.vz > 0) {
            this.vj.clear();
         }

         com.larpclient.h.A(
            this.getName(), "Restock at {}: took {}, put back {} ({})", this.Xq == null ? "?" : this.Xq.pos().toShortString(), this.vz, this.Z, var1
         );
         if (this.vz > 0 || this.Z > 0) {
            this.j8++;
            this.m("restocked at " + this.Xq.pos().toShortString() + ": took " + this.vz + (this.Z > 0 ? ", put back " + this.Z : "") + ".");
         }

         this.N8 = null;
         this.Xq = null;
         this.j_.H();
         t1.v(this);
         this.M = 12;
      }
   }

   private Set<Long> c(BlockPos var1) {
      c6 var2 = new c6(var0 -> -1, false);
      HashSet var3 = new HashSet();
      double var4 = (this.j() - 0.3) * (this.j() - 0.3);

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
                     && b_.x(this, var1, new Vec3(var9 + 0.5, var10 + 1.62, var11 + 0.5), this.j() - 0.3, null) != null) {
                     var3.add(zf.O(var9, var10, var11));
                  }
               }
            }
         }
      }

      return var3;
   }

   private void X(ak.t var1) {
      BlockPos var2 = this.Xq.pos();
      if (mc.player.tickCount - this.T > 900) {
         this.I("the walk took too long");
      } else if (mc.level.hasChunkAt(var2) && p(mc.level.getBlockState(var2))) {
         if (!this.j_.i() && b_.x(this, var2, mc.player.getEyePosition(), this.j(), null) != null) {
            this.N8 = ak.k.I;
            this.T = mc.player.tickCount;
         } else {
            if (!this.j_.i()) {
               BlockPos var3 = this.E();
               Set var4 = this.c(var2);
               if (var4.isEmpty()) {
                  this.I("no spot to stand at it");
                  return;
               }

               zf.s var5 = new zf.s(
                  Math.min(var2.getX(), var3.getX()) - 12,
                  Math.min(var2.getY(), var3.getY()) - 6,
                  Math.min(var2.getZ(), var3.getZ()) - 12,
                  Math.max(var2.getX(), var3.getX()) + 12,
                  Math.max(var2.getY(), var3.getY()) + 6,
                  Math.max(var2.getZ(), var3.getZ()) + 12
               );
               zf.v var6 = zf.B(
                  new c6(var0 -> -1, false),
                  var3.getX(),
                  var3.getY(),
                  var3.getZ(),
                  zf.f.k(var4, var2.getX(), var2.getY(), var2.getZ(), this.j()),
                  var5,
                  zf.z.walking(32000)
               );
               if (!var6.found()) {
                  this.I("no way to walk to it");
                  return;
               }

               if (var6.steps().isEmpty()) {
                  this.N8 = ak.k.I;
                  this.T = mc.player.tickCount;
                  return;
               }

               this.j_.M(var6.steps());
               this.vR = var2;
               zf.h var7 = var6.steps().getLast();
               this.XT = new BlockPos(var7.x(), var7.y(), var7.z());
            }

            this.u();
         }
      } else {
         this.I("the container is gone");
      }
   }

   private void t() {
      BlockPos var1 = this.Xq.pos();
      if (mc.player.tickCount - this.T > 60) {
         this.I("it did not open");
      } else {
         this.Xl = Vec3.ZERO;
         this.jl = (boolean)0;
         int var2 = this.K();
         if (var2 >= 0 && !this.j(var2)) {
            this.e("Putting the block away");
         } else {
            ItemStack var3 = mc.player.getMainHandItem();
            Item var4 = var3.isEmpty() ? Items.AIR : var3.getItem();
            double var5 = this.j();
            t1.S(
               this,
               var1,
               var4,
               var5,
               true,
               false,
               () -> !this.Z(),
               () -> !mc.player.isShiftKeyDown() && mc.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 && t1.U(this, var1, var5),
               var1x -> {
                  this.v0 = mc.player.tickCount;
                  if (var1x) {
                     this.N8 = ak.k.n;
                     this.T = mc.player.tickCount;
                     this.v7 = -1000000000;
                  }
               }
            );
            this.e("Opening the restock container at " + var1.toShortString());
         }
      }
   }

   private boolean u(ak.t var1) {
      int var2 = mc.player.tickCount;
      this.K();
      Screen var3 = mc.gui.screen();
      AbstractContainerMenu var4 = mc.player.containerMenu;
      if (var3 instanceof AbstractContainerScreen && var4 != mc.player.inventoryMenu) {
         if (this.v7 == -1000000000) {
            this.v7 = var2;
         }

         if (var2 - this.T > 400) {
            mc.player.closeContainer();
            this.I("the clicks took too long");
            return true;
         }

         this.e("Restocking at " + this.Xq.pos().toShortString());
         if (var2 - this.v7 >= 6 && var2 - this.N2 >= 4 && var4.getCarried().isEmpty()) {
            int var5 = var4.slots.size() - 36;
            if (var5 <= 0) {
               mc.player.closeContainer();
               this.I("not a container menu");
               return true;
            }

            if (this.W.getValue()) {
               Map var6 = this.r();
               Item var7 = this.v3.getValue() ? this.p() : null;

               for (int var8 = var5; var8 < var4.slots.size(); var8++) {
                  ItemStack var9 = ((Slot)var4.slots.get(var8)).getItem();
                  if (!var9.isEmpty()
                     && var9.getItem() instanceof BlockItem
                     && !var6.containsKey(var9.getItem())
                     && !var9.is(var7)
                     && !this.Eh.contains(var8)
                     && k(var4, 0, var5, var9)) {
                     int var10 = var9.getCount();
                     this.U(var4, var8);
                     this.Z += var10;
                     this.Eh.add(var8);
                     return true;
                  }
               }
            }

            for (int var11 = 0; var11 < var5; var11++) {
               ItemStack var12 = ((Slot)var4.slots.get(var11)).getItem();
               int var13 = var12.isEmpty() ? 0 : this.Na.getOrDefault(var12.getItem(), 0);
               if (var13 > 0 && k(var4, var5, var4.slots.size(), var12)) {
                  int var14 = var12.getCount();
                  Item var15 = var12.getItem();
                  this.U(var4, var11);
                  this.vz += var14;
                  this.Na.merge(var15, -var14, Integer::sum);
                  return true;
               }
            }

            mc.player.closeContainer();
            this.I(this.Na.values().stream().anyMatch(var0 -> var0 > 0) ? "took what it had" : "took everything needed");
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.T <= 60) {
         this.e("Waiting for the container to open");
         return true;
      } else {
         this.I("it did not open");
         return false;
      }
   }

   private void U(AbstractContainerMenu var1, int var2) {
      mc.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, mc.player);
      this.N2 = mc.player.tickCount;
   }

   private static boolean k(AbstractContainerMenu var0, int var1, int var2, ItemStack var3) {
      for (int var4 = var1; var4 < var2; var4++) {
         ItemStack var5 = ((Slot)var0.slots.get(var4)).getItem();
         if (var5.isEmpty() || ItemStack.isSameItemSameComponents(var5, var3) && var5.getCount() < var5.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   private String a() {
      if (!this.isInGame()) {
         return "";
      }

      int var1 = 0;

      for (ak.f var3 : s(this.C.getValue())) {
         if (var3.dimension().equals(this.W())) {
            var1++;
         }
      }

      String var4 = var1 == 0 ? "No chests selected. Look at one and press Select chest." : var1 + (var1 == 1 ? " chest selected." : " chests selected.");
      return this.vF && this.jE ? var4 + " " + this.Z() + "." : var4;
   }

   private String n() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.jE) {
         return this.vF ? "Already stocking." : "Turn the builder off first; it stocks between builds.";
      }

      String var1 = s1.U(this);
      if (var1 != null) {
         return "Turn off " + var1 + " first; one bot runs at a time.";
      }

      if (this.jF) {
         return "Stop and save the Build Recorder first.";
      }

      ak.t var2 = this.Eo;
      if (var2 == null) {
         return this.vI == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var2.s) {
         return "Still checking the world; try again in a moment.";
      }

      if (this.h(var2).isEmpty()) {
         return "The build needs no more blocks.";
      }

      int var3 = this.h().size();
      if (var3 == 0) {
         return "Select the material chests first: look at a chest, barrel or shulker box within "
            + this.jQ.getValue().intValue()
            + " blocks and press Select chest.";
      }

      long var4 = System.currentTimeMillis();
      if (var4 - this.U > 10000L) {
         this.U = var4;
         return "Press Stock materials again within 10 seconds to start. It checks the "
            + var3
            + (var3 == 1 ? " chest" : " chests")
            + " for what they already hold, buys only what is still short from /ah, cheapest first, at most "
            + com.larpclient.je.z(this.vY.getValue().longValue())
            + " a block and "
            + com.larpclient.je.z(this.N7.getValue().longValue())
            + " in all, and puts it into them.";
      }

      this.U = 0L;
      this.XI = var4;
      this.enable();
      if (this.vF && this.jE) {
         return "Stocking: checking " + var3 + (var3 == 1 ? " chest" : " chests") + " first.";
      }

      this.XI = 0L;
      return this.NX != null ? this.NX + "." : "Stocking did not start.";
   }

   private List<ak.f> h() {
      ArrayList var1 = new ArrayList();
      double var2 = this.jQ.getValue() * this.jQ.getValue();
      String var4 = this.W();

      for (ak.f var6 : s(this.C.getValue())) {
         if (var6.dimension().equals(var4) && mc.player.distanceToSqr(Vec3.atCenterOf(var6.pos())) <= var2) {
            var1.add(var6);
         }
      }

      return var1;
   }

   private void I() {
      if (this.vW) {
         this.D();
      }

      ak.t var1 = this.Eo;
      if (var1 != null && var1.s) {
         List var2 = this.h();
         if (var2.isEmpty()) {
            this.j("no material chests are selected within Restock Distance");
         } else {
            if (var1.U <= 200000) {
               this.s(var1, var1.U);
            }

            this.jE = (boolean)1;
            this.vF = (boolean)1;
            this.d = (boolean)0;
            this.E2 = mc.level;
            this.vM = mc.player;
            this.XH.k();
            this.N9 = System.currentTimeMillis();
            this.h = ak.d.g;
            this.NA.addAll(var2);
            com.larpclient.h.A(this.getName(), "Stocking started for {}: reading {} containers within {} blocks", this.vI.o(), var2.size(), this.jQ.getValue());
            this.m("stocking " + this.vI.o() + ": checking " + var2.size() + (var2.size() == 1 ? " chest" : " chests") + " for what they hold first.");
         }
      } else {
         this.j("set the schematic and its origin first");
      }
   }

   private void l(ak.t var1) {
      if (this.N8 == ak.k.v) {
         this.X(var1);
      } else if (this.N8 == ak.k.I) {
         this.t();
      } else if (this.h == ak.d.g) {
         ak.f var6 = this.M(this.NA);
         if (var6 != null) {
            this.P(var6);
         } else {
            this.y(var1);
         }
      } else {
         if (this.h == ak.d.l) {
            ak.a var2 = this.Y();
            if (var2 == ak.a.Z) {
               return;
            }

            if (var2 == ak.a.G) {
               this.d = (boolean)1;
            }

            this.h = ak.d.r;
         }

         if (this.vC.isEmpty()) {
            if (!this.d) {
               this.h = ak.d.l;
            } else {
               this.G(null);
            }
         } else {
            ArrayList var5 = new ArrayList();

            for (ak.f var4 : this.Xc.keySet()) {
               if (!this.Nh.contains(var4) && (this.NO.getOrDefault(var4, 0) > 0 || !Collections.disjoint(this.Xc.get(var4).keySet(), this.vC.keySet()))) {
                  var5.add(var4);
               }
            }

            ak.f var7 = this.M(var5);
            if (var7 == null) {
               this.G("the selected chests are full");
            } else {
               this.P(var7);
            }
         }
      }
   }

   private ak.f M(Collection<ak.f> var1) {
      ak.f var2 = null;
      double var3 = Double.MAX_VALUE;

      for (ak.f var6 : var1) {
         if (!this.j.contains(var6) && !this.jN.contains(var6)) {
            double var7 = mc.player.distanceToSqr(Vec3.atCenterOf(var6.pos()));
            if (var7 < var3) {
               var2 = var6;
               var3 = var7;
            }
         }
      }

      return var2;
   }

   private void P(ak.f var1) {
      this.Xq = var1;
      this.N8 = ak.k.v;
      this.T = mc.player.tickCount;
      this.vz = this.Z = 0;
      this.Eh.clear();
      this.jL = (boolean)0;
      this.j_.H();
      this.v9 = List.of();
      t1.v(this);
      com.larpclient.h.A(
         this.getName(), "Stocking: going to {} to {}", var1.pos().toShortString(), this.h == ak.d.g ? "read it" : "put " + Z(this.vC) + " blocks in"
      );
      this.e((this.h == ak.d.g ? "Checking the chest at " : "Taking the materials to the chest at ") + var1.pos().toShortString());
   }

   private void W(String var1) {
      ak.f var2 = this.Xq;
      com.larpclient.h.A(this.getName(), "Stocking at {}: {}", var2 == null ? "?" : var2.pos().toShortString(), var1);
      if (var2 != null) {
         if (this.h == ak.d.g) {
            this.NA.remove(var2);
         }

         if (!this.jL && this.j.add(var2)) {
            this.m("could not use the chest at " + var2.pos().toShortString() + ": " + var1 + "; going on without it.");
         }
      }

      this.N8 = null;
      this.Xq = null;
      this.j_.H();
      t1.v(this);
   }

   private boolean g() {
      int var1 = mc.player.tickCount;
      this.K();
      Screen var2 = mc.gui.screen();
      AbstractContainerMenu var3 = mc.player.containerMenu;
      if (var2 instanceof AbstractContainerScreen && var3 != mc.player.inventoryMenu) {
         if (this.v7 == -1000000000) {
            this.v7 = var1;
         }

         if (var1 - this.T > 400) {
            mc.player.closeContainer();
            this.I("the clicks took too long");
            return true;
         }

         if (var1 - this.v7 >= 6 && var1 - this.N2 >= 4 && var3.getCarried().isEmpty()) {
            int var4 = var3.slots.size() - 36;
            if (var4 <= 0) {
               mc.player.closeContainer();
               this.I("not a container menu");
               return true;
            }

            ak.f var5 = this.Xq;
            if (this.h == ak.d.r) {
               this.e("Putting materials into the chest at " + var5.pos().toShortString());

               for (int var6 = var4; var6 < var3.slots.size(); var6++) {
                  ItemStack var7 = ((Slot)var3.slots.get(var6)).getItem();
                  if (!var7.isEmpty() && this.vC.getOrDefault(var7.getItem(), 0) > 0 && var7.getComponentsPatch().isEmpty() && k(var3, 0, var4, var7)) {
                     Item var8 = var7.getItem();
                     int var9 = var7.getCount();
                     this.U(var3, var6);
                     int var10 = this.vC.get(var8) - var9;
                     if (var10 > 0) {
                        this.vC.put(var8, var10);
                     } else {
                        this.vC.remove(var8);
                     }

                     this.z.merge(var8, var9, Integer::sum);
                     this.jL = (boolean)1;
                     return true;
                  }
               }

               if (!this.vC.isEmpty()) {
                  this.Nh.add(var5);
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

            if (!this.Xc.containsKey(var5)) {
               com.larpclient.h.A(
                  this.getName(),
                  "Stocking: the container at {} holds {} kinds of plain items, {} of {} slots free",
                  var5.pos().toShortString(),
                  var11.size(),
                  var12,
                  var4
               );
            }

            this.Xc.put(var5, var11);
            this.NO.put(var5, var12);
            ak.f var14 = this.Z(var5);
            if (var14 != null) {
               this.jN.add(var14);
               this.NA.remove(var14);
               this.Xc.remove(var14);
               this.NO.remove(var14);
            }

            this.jL = (boolean)1;
            mc.player.closeContainer();
            this.I(this.h == ak.d.g ? "read it" : (this.vC.isEmpty() ? "put everything in" : "it is full"));
            return true;
         } else {
            return true;
         }
      } else if (var1 - this.T <= 60) {
         this.e("Waiting for the chest to open");
         return true;
      } else {
         this.I("it did not open");
         return false;
      }
   }

   private ak.f Z(ak.f var1) {
      BlockState var2 = mc.level.getBlockState(var1.pos());
      if (var2.getBlock() instanceof ChestBlock && var2.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
         BlockPos var3 = var1.pos().relative(ChestBlock.getConnectedDirection(var2));
         return new ak.f(var1.dimension(), var3.getX(), var3.getY(), var3.getZ());
      } else {
         return null;
      }
   }

   private void y(ak.t var1) {
      int var2 = 0;

      for (ak.f var4 : this.Xc.keySet()) {
         if (!this.jN.contains(var4)) {
            var2++;
         }
      }

      if (var2 == 0) {
         this.j("none of the selected chests could be opened");
      } else {
         LinkedHashMap var10 = new LinkedHashMap<>(this.h(var1));
         Item var11 = this.v3.getValue() ? this.p() : null;
         if (var11 != null) {
            var10.merge(var11, 32, Integer::sum);
         }

         HashMap var5 = new HashMap();

         for (Map var7 : this.Xc.values()) {
            var7.forEach((var1x, var2x) -> var5.merge(var1x, var2x, Integer::sum));
         }

         for (Item var15 : var10.keySet()) {
            var5.merge(var15, this.E(var15), Integer::sum);
         }

         this.vA.putAll(X(var10, var5));
         this.jZ = 0;

         for (int var16 : this.NO.values()) {
            this.jZ += var16;
         }

         int var14 = Z(var10);
         int var17 = var14 - Z(this.vA);
         com.larpclient.h.A(
            this.getName(), "Stocking plan: {} chests read, {} of {} blocks at hand, {} free chest slots; to buy {}", var2, var17, var14, this.jZ, this.vA
         );
         this.m(
            "checked "
               + var2
               + (var2 == 1 ? " chest" : " chests")
               + ": with your inventory they hold "
               + var17
               + " of the "
               + var14
               + " blocks the build still needs."
         );
         if (this.vA.isEmpty()) {
            this.G("nothing needs buying");
         } else {
            for (Entry var9 : this.vA.entrySet()) {
               this.m("  to buy: " + var9.getValue() + " " + e((Item)var9.getKey()));
            }

            this.h = ak.d.l;
         }
      }
   }

   static Map<Item, Integer> X(Map<Item, Integer> var0, Map<Item, Integer> var1) {
      LinkedHashMap var2 = new LinkedHashMap();

      for (Entry var4 : var0.entrySet()) {
         int var5 = (Integer)var4.getValue() - var1.getOrDefault(var4.getKey(), 0);
         if (var5 > 0) {
            var2.put((Item)var4.getKey(), var5);
         }
      }

      return var2;
   }

   static int Z(Map<Item, Integer> var0) {
      int var1 = 0;

      for (int var3 : var0.values()) {
         var1 += var3;
      }

      return var1;
   }

   private ak.a Y() {
      if (this.XQ) {
         return ak.a.G;
      }

      long var1 = this.N7.getValue().longValue() - this.E5;
      if (var1 <= 0L) {
         this.n("Max Total Spend is used up; nothing more is bought");
         return ak.a.G;
      }

      for (Entry var4 : this.vA.entrySet()) {
         Item var5 = (Item)var4.getKey();
         if ((Integer)var4.getValue() > 0 && !this.Nj.contains(var5)) {
            if (this.jZ <= 0) {
               this.n("the selected chests have no free slots left; select more chests");
               return ak.a.G;
            }

            int var6 = new ItemStack(var5).getMaxStackSize();
            int var7 = Math.min((Integer)var4.getValue(), this.jZ * var6);
            int var8 = this.T(var5);
            if (var8 < Math.min(var7, var6)) {
               if (!this.vC.isEmpty()) {
                  return ak.a.X;
               }

               this.n("no free inventory room to buy " + e(var5) + " into; empty a few slots");
               return ak.a.G;
            }

            var7 = Math.min(var7, var8);
            this.vy = var5;
            this.Et = 0;
            this.j_.H();
            this.v9 = List.of();
            t1.v(this);
            this.ji.Q(var5, var7, this.vY.getValue().longValue(), this.jq.getValue().longValue(), var1);
            this.m(
               "buying "
                  + var7
                  + " "
                  + e(var5)
                  + " from /ah, cheapest first: at most "
                  + com.larpclient.je.z(this.vY.getValue().longValue())
                  + " a block, "
                  + com.larpclient.je.z(this.jq.getValue().longValue())
                  + " a listing, "
                  + com.larpclient.je.z(var1)
                  + " left to spend."
            );
            return ak.a.Z;
         }
      }

      return ak.a.G;
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

   private void R(Item var1, int var2, long var3, String var5) {
      if (var1 != null) {
         if (var2 > 0) {
            int var6 = new ItemStack(var1).getMaxStackSize();
            int var7 = this.vA.getOrDefault(var1, 0) - var2;
            if (var7 > 0) {
               this.vA.put(var1, var7);
            } else {
               this.vA.remove(var1);
            }

            this.vC.merge(var1, var2, Integer::sum);
            this.Ne.merge(var1, var2, Integer::sum);
            this.jZ -= (var2 + var6 - 1) / var6;
         } else {
            this.jS.put(var1, var3);
            this.vb.put(var1, var5 != null && !var5.isEmpty() ? var5 : "nothing bought");
         }
      }
   }

   private void G(String var1) {
      String var2 = ae.I(System.currentTimeMillis() - this.N9);
      StringBuilder var3 = new StringBuilder("stocking done in ").append(var2);
      if (var1 != null) {
         var3.append(" (").append(var1).append(')');
      }

      var3.append(": bought ")
         .append(Z(this.Ne))
         .append(" blocks for ")
         .append(com.larpclient.je.z(this.E5))
         .append(", put ")
         .append(Z(this.z))
         .append(" into the chests");
      this.m(var3 + ".");

      for (Entry var5 : this.Ne.entrySet()) {
         this.m("  bought " + var5.getValue() + " " + e((Item)var5.getKey()) + ", put away " + this.z.getOrDefault(var5.getKey(), 0));
      }

      if (!this.vC.isEmpty()) {
         ArrayList var7 = new ArrayList();

         for (Entry var6 : this.vC.entrySet()) {
            var7.add(var6.getValue() + " " + e((Item)var6.getKey()));
         }

         this.m("  still in your inventory, no chest had room: " + String.join(", ", var7));
      }

      for (Entry var10 : this.vA.entrySet()) {
         String var11 = this.vb.containsKey(var10.getKey())
            ? this.vb.get(var10.getKey())
            : (
               this.XQ
                  ? "out of money"
                  : (this.E5 >= this.N7.getValue().longValue() ? "Max Total Spend reached" : (this.jZ <= 0 ? "no chest room left" : "not bought"))
            );
         this.m("  still short: " + var10.getValue() + " " + e((Item)var10.getKey()) + " (" + var11 + ")");
      }

      com.larpclient.h.A(this.getName(), "Stocking done: {}; still short {}; reasons {}", var3, this.vA, this.vb);
      this.NX = "Last stocking: bought " + Z(this.Ne) + ", put away " + Z(this.z) + (this.vA.isEmpty() ? "" : ", " + Z(this.vA) + " still short");
      s0.s()
         .k(
            "Auto Builder",
            "Stocking done: " + Z(this.z) + " blocks put away" + (this.vA.isEmpty() ? "" : ", " + Z(this.vA) + " still short"),
            this.vA.isEmpty() ? s0.u.m : s0.u.p,
            8000L
         );
      this.jE = (boolean)0;
      this.disable();
   }

   private String Z() {
      String var1 = this.h == ak.d.g ? "checking the chests" : (this.h == ak.d.l ? "buying" : "putting materials away");
      return "Stocking, "
         + var1
         + ": bought "
         + Z(this.Ne)
         + " for "
         + com.larpclient.je.z(this.E5)
         + ", "
         + Z(this.z)
         + " put away, "
         + ae.I(System.currentTimeMillis() - this.N9);
   }

   private Path o() {
      return u().resolve(ha.i(this.vn.getValue().trim()));
   }

   private void Y(ak.t var1) {
      this.NZ = var1;
      this.NE = null;
      this.XE = null;
      if (var1 != null && !this.vn.getValue().isBlank()) {
         try {
            List var2 = ha.a(this.o());
            if (var2.isEmpty()) {
               return;
            }

            ArrayList var3 = new ArrayList();

            for (int[] var5 : var2) {
               int var6 = var1.C(var5[0], var5[1], var5[2]);
               if (var6 >= 0) {
                  var3.add(var6);
               }
            }

            this.NE = var3.stream().mapToInt(Integer::intValue).toArray();
            com.larpclient.h.A(
               this.getName(),
               "Recorded order of {}: {} cells ({} not in the schematic)",
               this.vn.getValue().trim(),
               this.NE.length,
               var2.size() - this.NE.length
            );
         } catch (IOException var7) {
            this.XE = var7.getMessage();
            com.larpclient.h.A(this.getName(), "Could not read the recorded order: {}", var7.toString());
         }
      }
   }

   private String B() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.jE) {
         return "Turn the builder off before recording.";
      }

      ak.t var1 = this.Eo;
      if (var1 == null) {
         return this.vI == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (this.jF) {
         return "Already recording: " + this.jb.size() + " blocks so far.";
      }

      if (var1.U > 200000) {
         return "This schematic is too big to record (" + var1.U + " blocks).";
      }

      this.EE = new BitSet(var1.U);

      for (int var2 = 0; var2 < var1.U; var2++) {
         BlockState var3 = var1.g(var2);
         if (var3 != null && mc.level.getBlockState(var1.S(var2)).is(var3.getBlock())) {
            this.EE.set(var2);
         }
      }

      this.jb.clear();
      this.NG.clear();
      this.jF = (boolean)1;
      com.larpclient.h.A(this.getName(), "Build Recorder started on {} ({} of {} blocks already stand)", this.vI.o(), this.EE.cardinality(), var1.U);
      return "Recording: place the blocks of " + this.vI.o() + " in the order you want them built, then press Stop and save the recording.";
   }

   private void I(ak.t var1) {
      if (this.jF) {
         BlockPos var2 = mc.player.blockPosition();
         MutableBlockPos var3 = new MutableBlockPos();

         for (int var4 = Math.max(var1.E, var2.getX() - 8); var4 <= Math.min(var1.m, var2.getX() + 8); var4++) {
            for (int var5 = Math.max(var1.S, var2.getY() - 8); var5 <= Math.min(var1.J, var2.getY() + 8); var5++) {
               for (int var6 = Math.max(var1.v, var2.getZ() - 8); var6 <= Math.min(var1.k, var2.getZ() + 8); var6++) {
                  int var7 = var1.M(var4, var5, var6);
                  if (var7 >= 0
                     && !this.NG.contains(var7)
                     && !this.EE.get(var7)
                     && var1.g(var7) != null
                     && mc.level.getBlockState(var3.set(var4, var5, var6)).is(var1.g(var7).getBlock())) {
                     this.jb.add(var7);
                     this.NG.add(var7);
                  }
               }
            }
         }
      }
   }

   private void P(String var1) {
      this.jF = (boolean)0;
      com.larpclient.h.A(this.getName(), "Build Recorder stopped without saving: {} ({} blocks recorded)", var1, this.jb.size());
      this.m("the Build Recorder stopped without saving: " + var1 + ".");
      this.jb.clear();
      this.NG.clear();
   }

   private String T() {
      if (!this.jF) {
         return "The Build Recorder is not recording.";
      }

      ak.t var1 = this.Eo;
      this.jF = (boolean)0;
      if (var1 != null && !this.jb.isEmpty()) {
         ArrayList var2 = new ArrayList();

         for (int var4 : this.jb) {
            var2.add(new int[]{var1.L.Z(var4), var1.L.L(var4), var1.L.d(var4)});
         }

         Path var6 = this.o();

         try {
            ha.T(var6, var1.L.o(), var2);
         } catch (IOException var5) {
            return "Could not save the recording: " + var5.getMessage();
         }

         int var7 = var2.size();
         this.jb.clear();
         this.NG.clear();
         this.Y(var1);
         com.larpclient.h.A(this.getName(), "Saved the build order of {} blocks to {}", var7, var6.getFileName());
         return "Saved the order of " + var7 + " blocks to " + var6.getFileName() + ". Follow Recording builds in this order.";
      } else {
         this.jb.clear();
         this.NG.clear();
         return "Nothing was placed, so nothing was saved.";
      }
   }

   private String P() {
      if (this.jF) {
         this.P("you deleted the recording");
      }

      if (this.vn.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      try {
         boolean var1 = Files.deleteIfExists(this.o());
         this.Y(this.Eo);
         return var1 ? "Deleted the recorded order of " + this.vn.getValue().trim() + "." : "This schematic has no recording.";
      } catch (IOException var2) {
         return "Could not delete the recording: " + var2.getMessage();
      }
   }

   private String U() {
      if (this.jF) {
         return " Recording the build order: " + this.jb.size() + " blocks so far.";
      } else if (this.XE != null) {
         return " Its recording could not be read: " + this.XE + ".";
      } else {
         return this.NE != null && this.NE.length > 0
            ? " Recorded order: " + this.NE.length + " blocks" + (this.NN.getValue() ? ", followed." : ", not followed.")
            : "";
      }
   }

   private boolean c(ak.t var1) {
      if (this.XQ) {
         return false;
      }

      long var2 = this.N7.getValue().longValue() - this.E5;
      if (var2 <= 0L) {
         this.n("Max Total Spend is used up; buying stops for this run");
         return false;
      }

      Item var4 = null;
      int var5 = 0;
      Item var6 = this.G > 0 ? this.p() : null;
      if (var6 != null && !this.Nj.contains(var6) && this.E(var6) < 32) {
         var4 = var6;
         var5 = 32 - this.E(var6);
      } else {
         for (Entry var8 : this.h(var1).entrySet()) {
            Item var9 = (Item)var8.getKey();
            if (!this.Nj.contains(var9) && this.Xj.containsKey(var9)) {
               int var10 = (Integer)var8.getValue() - this.E(var9);
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
         this.n("no free inventory slot to buy " + e(var4) + " into");
         return false;
      } else {
         this.vy = var4;
         this.Et = 0;
         this.j_.H();
         this.v9 = List.of();
         t1.v(this);
         this.ji.Q(var4, var5, this.vY.getValue().longValue(), this.jq.getValue().longValue(), var2);
         this.m(
            "buying "
               + var5
               + " "
               + e(var4)
               + " from /ah: at most "
               + com.larpclient.je.z(this.vY.getValue().longValue())
               + " a block, "
               + com.larpclient.je.z(this.jq.getValue().longValue())
               + " a listing, "
               + com.larpclient.je.z(var2)
               + " left to spend."
         );
         return true;
      }
   }

   private void W() {
      this.K();
      this.jl = (boolean)0;
      if (this.Z() && this.ji.G()) {
         this.ji.Q("you moved");
      }

      if (this.ji.G() && this.Et < 4) {
         this.Et = mc.player.onGround() && mc.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 ? this.Et + 1 : 0;
         this.e("Stopping to buy " + e(this.vy));
      } else if (this.ji.G() && this.ji.u() == t6.y.b) {
         this.e("Buying " + e(this.vy) + " from /ah");
      } else {
         this.E5 = this.E5 + this.ji.j();
         this.j5 = this.j5 + this.ji.r();
         String var1 = e(this.vy);
         if (this.ji.r() > 0) {
            this.m(
               "bought "
                  + this.ji.r()
                  + " "
                  + var1
                  + " for "
                  + com.larpclient.je.z(this.ji.j())
                  + (this.ji.u().isEmpty() ? "" : " (" + this.ji.u() + ")")
                  + "."
            );
            s0.s().V("Auto Builder", "Bought " + this.ji.r() + " " + var1 + " for " + com.larpclient.je.z(this.ji.j()), s0.u.m);
         }

         if (this.ji.r() == 0) {
            this.Nj.add(this.vy);
            this.m("could not buy " + var1 + ": " + (this.ji.u().isEmpty() ? "nothing bought" : this.ji.u()) + ".");
         }

         if (this.ji.o()) {
            this.XQ = (boolean)1;
         }

         if (this.vF) {
            this.R(this.vy, this.ji.r(), this.ji.Q(), this.ji.u());
         }

         com.larpclient.h.A(
            this.getName(),
            "Buying {} ended {}: bought {} for {}; this run {} for {}",
            var1,
            this.ji.g(),
            this.ji.r(),
            com.larpclient.je.z(this.ji.j()),
            this.j5,
            com.larpclient.je.z(this.E5)
         );
         this.vy = null;
         this.v0 = mc.player.tickCount;
      }
   }

   private boolean V() {
      double var1 = this.XX.getValue();
      double var3 = var1 * var1;
      String var5 = null;

      for (StaffListModule.e var7 : StaffListModule.g()) {
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

      this.NH = var5;
      return true;
   }

   private void R() {
      int var1 = mc.player.tickCount;
      if (this.vw == -1000000000) {
         this.K();
         this.j_.H();
         this.XH.B();
         l0.h(this);
         if (mc.gui.screen() instanceof AbstractContainerScreen) {
            mc.player.closeContainer();
         }

         this.j2 = null;
         this.N8 = null;
         if (this.ji.G()) {
            this.ji.Q("staff nearby");
         }

         this.vy = null;
         this.vw = var1 + (int)Math.round(this.Nv.getValue() * 20.0);
         com.larpclient.h.A(this.getName(), "Staff nearby: {}; logging off in {} s", this.NH, this.Nv.getValue());
         this.m("staff nearby: " + this.NH + ". Stopping and logging off.");
      }

      this.K();
      this.e("Staff nearby: " + this.NH);
      if (var1 >= this.vw) {
         ClientPacketListener var2 = mc.getConnection();
         String var3 = "staff came near: " + this.NH;
         if (!this.vB.isEmpty()) {
            var3 = var3 + ", " + this.vB.size() + " temporary blocks left standing";
         }

         if (var2 != null && !mc.isLocalServer()) {
            com.larpclient.h.A(this.getName(), "Logging off: {}", var3);
            this.j("logged off, " + var3);
            var2.getConnection().disconnect(Component.literal(this.V.getValue().isBlank() ? "Auto Builder stopped: staff nearby" : this.V.getValue()));
         } else {
            com.larpclient.h.A(this.getName(), "Staff stop in singleplayer: stopped without logging off ({})", var3);
            this.j(var3 + " (singleplayer: not logged off)");
         }
      }
   }

   private boolean z() {
      int var1 = mc.player.tickCount;

      while (!this.XC.isEmpty() && var1 - this.XC.peekFirst() > 1200) {
         this.XC.removeFirst();
      }

      if (this.jW.getValue() && this.XC.size() >= 8) {
         long var2 = this.P == 0L ? Long.MAX_VALUE : System.currentTimeMillis() - this.P;
         if (!a(this.XC.size(), var2, this.Nx)) {
            if (this.Nx >= 3 && !this.n) {
               this.n = (boolean)1;
               com.larpclient.h.A(this.getName(), "Relog On Ghost Blocks: {} relogs this build already; not relogging again", 3);
               this.m("the server keeps taking placed blocks back, but it already relogged 3 times this build, so it builds on without relogging.");
            }

            return false;
         } else {
            return !mc.isLocalServer() && mc.getCurrentServer() != null && mc.getConnection() != null
               ? this.vB.isEmpty()
                  && this.Ni.isEmpty()
                  && this.i.isEmpty()
                  && this.NR.isEmpty()
                  && this.vT.isEmpty()
                  && this.XZ == null
                  && this.vQ == null
                  && this.N8 == null
                  && this.vy == null
                  && !this.ji.G()
                  && this.j2 == null
                  && !this.vF
                  && !(mc.gui.screen() instanceof AbstractContainerScreen)
                  && mc.player.onGround()
               : false;
         }
      } else {
         return false;
      }
   }

   static boolean a(int var0, long var1, int var3) {
      return var0 >= 8 && var1 >= 300000L && var3 < 3;
   }

   private void C() {
      int var1 = this.XC.size();
      this.vt = mc.getCurrentServer();
      this.o = 1;
      this.Nx++;
      this.P = System.currentTimeMillis();
      this.XO = 0;
      this.vd = (boolean)0;
      this.K();
      this.j_.H();
      this.XP.x();
      this.XH.B();
      com.larpclient.h.A(
         this.getName(),
         "Relog On Ghost Blocks: the server took back {} placed blocks in {} ticks; logging off and rejoining {} (relog {} of {}, {} blocks placed)",
         var1,
         1200,
         this.vt.ip,
         this.Nx,
         3,
         this.v5
      );
      this.m("the server took back " + var1 + " placed blocks in the last minute. Logging off and rejoining to sync up, then building on.");
      this.e("Logging off to resync blocks");
   }

   private void w() {
      if (this.o != 0) {
         Minecraft var1 = Minecraft.getInstance();
         if (!this.isEnabled()) {
            this.R("the builder was turned off", false);
         } else {
            long var2 = System.currentTimeMillis();
            if (this.o == 1) {
               this.Xh = var2;
               this.vl = var2 + 5000L;
               this.o = 2;
               if (var1.level != null && var1.getConnection() != null) {
                  com.larpclient.h.A(this.getName(), "Logging off {} to resync blocks", this.vt.ip);
                  var1.disconnectFromWorld(Component.literal("Larp Auto Builder: rejoining to resync blocks"));
               }
            } else {
               if (var1.level == null) {
                  this.vd = (boolean)1;
               }

               if (this.o == 3 || this.vd && var1.level != null) {
                  this.o = 3;
                  if (var2 - this.Xh > 120000L) {
                     this.R("it rejoined but the build did not start again", true);
                  }
               } else if (var1.level != null) {
                  if (var2 - this.Xh > 120000L) {
                     this.R("the log off did not go through", true);
                  }
               } else if (var2 - this.Xh > 120000L) {
                  this.R("it could not rejoin within 120 seconds", true);
               } else {
                  Screen var4 = var1.gui.screen();
                  if (!(var4 instanceof ConnectScreen) && var2 >= this.vl) {
                     if (this.XO >= 3) {
                        this.R("the server did not take 3 joins", true);
                     } else {
                        this.XO++;
                        this.vl = var2 + 10000L;
                        com.larpclient.h.A(this.getName(), "Rejoining {} to resync blocks (try {} of {})", this.vt.ip, this.XO, 3);
                        Object var5 = var4 != null ? var4 : new JoinMultiplayerScreen(new TitleScreen());
                        ConnectScreen.startConnecting((Screen)var5, var1, ServerAddress.parseString(this.vt.ip), this.vt, false, null);
                     }
                  }
               }
            }
         }
      }
   }

   private void R(String var1, boolean var2) {
      com.larpclient.h.A(this.getName(), "Relog On Ghost Blocks gave up: {}", var1);
      this.o = 0;
      this.vt = null;
      if (this.isEnabled()) {
         this.NW = "it logged off to resync blocks and " + var1;
      }

      if (var2) {
         s0.s().V("Auto Builder relog", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), s0.u.p);
      }
   }

   private void S() {
      if (System.currentTimeMillis() >= this.X8 && mc.level.hasChunkAt(mc.player.blockPosition())) {
         this.X8 = 0L;
         String var1 = s1.U(this);
         if (var1 != null) {
            this.j("turn off " + var1 + " first; one bot runs at a time");
         } else {
            com.larpclient.h.A(this.getName(), "Carrying on after the relog ({} of {} this build)", this.Nx, 3);
            this.m("rejoined; building on (relog " + this.Nx + " of 3 this build).");
            this.s();
         }
      } else {
         this.e("Rejoined; waiting for the world to load");
      }
   }

   private void j(ak.t var1) {
      TreeMap var2 = new TreeMap();
      TreeMap var3 = new TreeMap();
      MutableBlockPos var4 = new MutableBlockPos();
      int var5 = 0;
      int var6 = 0;

      for (int var7 = 0; var7 < var1.U; var7++) {
         if (var1.g(var7) != null && var1.i(var7) == b_.i.o) {
            if (var1.P[var1.y[var7]] != null) {
               var5++;
            } else if (this.NK.contains(var7)) {
               var6++;
            } else {
               this.i(var1, var7, var4);
               var2.merge(var4.getY(), 1, Integer::sum);
               var3.merge(var1.g(var7).getBlock().getName().getString(), 1, Integer::sum);
            }
         }
      }

      com.larpclient.h.A(
         this.getName(),
         "Left over: {} that are never placed (fluids and the like), {} outside the height limits; placeable, by Y: {}; by block: {}",
         var5,
         var6,
         var2,
         var3
      );
      int var9 = 0;

      for (int var8 = 0; var8 < var1.U && var9 < 12; var8++) {
         if (var1.g(var8) != null && (var1.i(var8) == b_.i.t || var1.i(var8) == b_.i.y)) {
            this.i(var1, var8, var4);
            com.larpclient.h.A(this.getName(), "Wrong at {}: {} instead of {}", var4.toShortString(), mc.level.getBlockState(var4), var1.g(var8));
            var9++;
         }
      }
   }

   private void J() {
      ak.t var1 = this.Eo;
      if (var1 != null && var1.U <= 200000) {
         this.s(var1, var1.U);
      }

      int var2 = var1 == null ? 0 : var1.K(b_.i.o);
      int var3 = var1 == null ? 0 : var1.K(b_.i.y) + var1.K(b_.i.t);
      if (var1 != null) {
         this.j(var1);
      }

      com.larpclient.h.A(
         this.getName(),
         "Movement: sharpest one-tick turn {} degrees, {} sudden reversals, {} sprint-jumps",
         String.format(Locale.ROOT, "%.1f", this.v4),
         this.Xx,
         this.N6
      );
      String var4 = ae.I(System.currentTimeMillis() - this.N9);
      TreeMap var5 = new TreeMap();

      for (String var7 : this.jr.values()) {
         var5.merge(var7, 1, Integer::sum);
      }

      StringBuilder var10 = new StringBuilder(this.vI == null ? "build" : this.vI.o())
         .append(" done in ")
         .append(var4)
         .append(", ")
         .append(this.v5)
         .append(" blocks placed");
      if (this.XR > 0) {
         var10.append(", ").append(this.XR).append(" right clicks to set blocks");
      }

      if (!this.je.isEmpty()) {
         var10.append(", ").append(this.je.size()).append(this.je.size() == 1 ? " crafter" : " crafters").append(" set");
      }

      if (this.Xy > 0) {
         var10.append(", ").append(this.Xy).append(this.Xy == 1 ? " pillar" : " pillars");
      }

      if (this.j5 > 0) {
         var10.append(", bought ").append(this.j5).append(" blocks for ").append(com.larpclient.je.z(this.E5));
      }

      if (this.j8 > 0) {
         var10.append(", ").append(this.j8).append(this.j8 == 1 ? " restock trip" : " restock trips");
      }

      if (var2 > 0) {
         var10.append(", ").append(var2).append(" still missing");
      }

      if (this.XS > 0) {
         var10.append(", ").append(this.XS).append(" would fall with nothing below them");
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

      this.NX = "Last run: " + this.v5 + " placed" + (var2 > 0 ? ", " + var2 + " missing" : "");
      this.m(var10 + ".");
      com.larpclient.h.A(this.getName(), "Done: {}", var10);
      if (this.v2.getValue()) {
         s0.s()
            .k(
               "Auto Builder",
               (this.vI == null ? "Build" : this.vI.o()) + " done in " + var4 + (var2 > 0 ? ", " + var2 + " missing" : ""),
               var2 > 0 ? s0.u.p : s0.u.m,
               8000L
            );
      }

      this.jE = (boolean)0;
      this.disable();
   }

   private boolean S() {
      if (this.r == this.js) {
         return true;
      }

      this.r = this.js;
      return false;
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (ta.E(this) && !this.w()) {
            if (!this.S()) {
               float[] var3 = com.larpclient.j6.s(mc.player.getEyePosition(), var1);
               float[] var4 = this.XP.Q(mc.player.getYRot(), mc.player.getXRot(), var3[0], var3[1], false);
               com.larpclient.j6.U(this, j6.l.o, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.w()) {
            if (!this.S()) {
               int var3 = Math.hypot(Mth.wrapDegrees(var1 - mc.player.getYRot()), var2 - mc.player.getXRot()) < 1.6F ? 1 : 0;
               float[] var4 = this.XP.c(mc.player.getYRot(), mc.player.getXRot(), var1, var2, (boolean)var3, true);
               com.larpclient.j6.U(this, j6.l.o, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   private void v(Vec3 var1) {
      if (ta.E(this) && !this.w()) {
         if (!this.S()) {
            Vec3 var2 = mc.player.getEyePosition();
            Vec3 var3 = var1.add(0.0, 0.6 - this.vk + 0.12 * Math.sin(mc.player.tickCount * 0.09 + this.X2), 0.0);
            if (this.vR != null
               && this.j_.e().size() <= 2
               && var2.distanceToSqr(Vec3.atCenterOf(this.vR)) <= 64.0
               && ae.Y(this.j_.q(), Vec3.atCenterOf(this.vR).subtract(var2))) {
               var3 = Vec3.atCenterOf(this.vR);
            }

            float[] var4 = com.larpclient.j6.s(var2, var3);
            float[] var5 = this.XP.Q(mc.player.getYRot(), mc.player.getXRot(), var4[0], var4[1], false);
            com.larpclient.j6.U(this, j6.l.o, var5[0], var5[1], 1.0);
         }
      }
   }

   private boolean a() {
      return this.Ny.getValue().equals("Aggressive");
   }

   public boolean B() {
      return !this.isEnabled()
         ? false
         : this.a()
            && this.jE
            && this.isRuntimeActive()
            && mc.player != null
            && mc.player == this.vM
            && !this.j_.i()
            && this.XZ == null
            && this.N8 == null
            && this.vy == null
            && this.j2 == null
            && this.NH == null
            && (this.v1 != null || mc.player.tickCount - this.v0 < 10)
            && !this.Z();
   }

   private boolean w() {
      if (!this.a()) {
         return false;
      }

      p_.o var1 = p_.A();
      return var1 == p_.o.R || var1 == p_.o.i && !this.B();
   }

   public lm f() {
      return this.Xe;
   }

   public double e() {
      return 35.0;
   }

   public static Input Q() {
      if (jt != null && jt.isEnabled()) {
         ak var0 = jt;
         Minecraft var1 = Minecraft.getInstance();
         int var2 = p_.A() == p_.o.i ? 1 : 0;
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.jE
            && var1.player != null
            && var1.player == var0.vM
            && var1.player.tickCount == var0.jj
            && !p_.V()
            && (!cj.b() || var2 != 0)
            && !(var1.gui.screen() instanceof AbstractContainerScreen)
            && (!com.larpclient.j6.j() || com.larpclient.j6.g(j6.l.o))) {
            int var3 = var0.Xl.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 != 0 && var3 != 0) {
               return null;
            }

            if (var3 == 0 && !var0.N1 && !var0.jl) {
               return null;
            }

            Input var4 = var3 != 0 ? var0.XP.e(var1.player.getYRot(), var0.Xl) : new Input(false, false, false, false, false, false, false);
            return new Input(var4.forward(), var4.backward(), var4.left(), var4.right(), var0.N1, var0.jl, var0.jw);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void m(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Auto Builder: " + var1));
      }
   }

   private void n(String var1) {
      if (!var1.equals(this.Xp)) {
         this.Xp = var1;
         this.m(var1 + ".");
      }
   }

   private void j(String var1) {
      com.larpclient.h.A(this.getName(), "Stopped: {}", var1);
      if (this.Eo != null && mc.level != null && this.Eo.U <= 200000) {
         this.s(this.Eo, this.Eo.U);
         this.j(this.Eo);
      }

      this.m("stopped, " + var1 + ".");
      s0.s().V("Auto Builder stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), s0.u.p);
      this.e("Stopped: " + var1);
      this.NX = "Stopped: " + var1;
      this.jE = (boolean)0;
      this.disable();
   }

   private int W() {
      ak.t var1 = this.Eo;
      if (var1 != null && this.jH > 0) {
         int var2 = var1.K(b_.i.o);
         return Math.clamp(Math.round(100.0 * (this.jH - Math.min(this.jH, var2)) / this.jH), 0, 100);
      } else {
         return this.jE ? 0 : 100;
      }
   }

   private String S() {
      return this.vF ? this.Z() : this.W() + "% done, " + this.v5 + " placed, " + ae.I(System.currentTimeMillis() - this.N9);
   }

   private void Q() {
      if (this.isInGame()) {
         if (this.Np.getValue()) {
            this.x();
         }

         if (this.e.getValue()) {
            ak.t var1 = this.Eo;
            if (var1 != null && var1.D == mc.level) {
               try {
                  int var2 = ho.B(0.25);
                  Gizmos.cuboid(new AABB(var1.E, var1.S, var1.v, var1.m + 1, var1.J + 1, var1.k + 1), GizmoStyle.stroke(uh.H(var2, 0.8), 1.5F));
                  cb.b var3 = var1.h;
                  Gizmos.cuboid(
                        new AABB(var3.originX(), var3.originY(), var3.originZ(), var3.originX() + 1, var3.originY() + 1, var3.originZ() + 1).inflate(0.04),
                        GizmoStyle.strokeAndFill(-14249, 2.0F, 1090504791)
                     )
                     .setAlwaysOnTop();
                  boolean var4 = this.j6.getValue();

                  for (int var8 : var1.Q) {
                     if (var8 < var1.U) {
                        b_.i var9 = var1.i(var8);

                        int var10 = switch (var9) {
                           case o -> var2;
                           case y -> -45747;
                           case t -> -24765;
                           case j -> var4 ? -11870592 : 0;
                           case V -> 0;
                        };
                        if (var10 != 0) {
                           BlockPos var11 = var1.S(var8);
                           float var12 = var9 == b_.i.o ? 0.12F : (var9 == b_.i.j ? 0.08F : 0.3F);
                           Gizmos.cuboid(new AABB(var11).deflate(0.03), GizmoStyle.strokeAndFill(uh.H(var10, 0.55), 1.0F, uh.H(var10, var12)));
                        }
                     }
                  }

                  if (this.jE && this.jk.getValue()) {
                     s_.k var14 = this.v1;
                     if (var14 != null) {
                        Gizmos.cuboid(
                              new AABB(new BlockPos(var14.x(), var14.y(), var14.z())).inflate(0.01),
                              GizmoStyle.strokeAndFill(uh.H(var2, 1.0), 2.0F, uh.H(var2, 0.25))
                           )
                           .setAlwaysOnTop();
                     }

                     if (this.j_.i()) {
                        Vec3 var15 = mc.player.position().add(0.0, 0.05, 0.0);

                        for (zf.h var17 : this.j_.e()) {
                           Vec3 var18 = new Vec3(var17.x() + 0.5, var17.y() + 0.05, var17.z() + 0.5);
                           Gizmos.line(var15, var18, uh.H(var2, 0.85), 2.0F).setAlwaysOnTop();
                           var15 = var18;
                        }
                     }
                  }
               } catch (RuntimeException var13) {
                  com.larpclient.h.w(this.getName(), "Preview failed", var13);
               }
            }
         }
      }
   }

   private void x() {
      List var1 = s(this.C.getValue());
      if (!var1.isEmpty()) {
         try {
            String var2 = this.W();
            double var3 = Math.max(32.0, this.jQ.getValue());

            for (ak.f var6 : var1) {
               if (var6.dimension().equals(var2) && !(mc.player.distanceToSqr(Vec3.atCenterOf(var6.pos())) > var3 * var3)) {
                  int var7 = this.Xq != null && this.Xq.equals(var6) ? 1 : 0;
                  int var8 = var7 != 0 ? -11870592 : -10443270;
                  Gizmos.cuboid(new AABB(var6.pos()).inflate(0.02), GizmoStyle.strokeAndFill(uh.H(var8, 0.9), 2.0F, uh.H(var8, 0.12))).setAlwaysOnTop();
               }
            }
         } catch (RuntimeException var9) {
            com.larpclient.h.w(this.getName(), "Restock outlines failed", var9);
         }
      }
   }

   @Override
   public s1.u hudCard() {
      if (this.isInGame() && this.B.getValue() && this.jE && this.isRuntimeActive() && mc.level == this.E2) {
         ak.t var1 = this.Eo;
         int var2 = var1 != null && var1.s ? 1 : 0;
         String var3 = Xz.matcher(this.vn.getValue().trim()).replaceFirst("");
         String var4 = this.getStatus();
         return this.vF
            ? new s1.u(
               "Stocking " + (var3.isEmpty() ? "materials" : var3), "", var4 != null && !var4.isBlank() ? var4 : "Starting", Z(this.z) + " put away", -1.0
            )
            : new s1.u(
               var3.isEmpty() ? "Schematic" : var3,
               var2 != 0 ? this.W() + "%" : "",
               var4 != null && !var4.isBlank() ? var4 : "Starting",
               this.v5 + " placed" + (var2 != 0 ? ", " + var1.K(b_.i.o) + " missing" : ""),
               var2 != 0 ? this.W() / 100.0 : -1.0
            );
      } else {
         return null;
      }
   }

   @Override
   public jd preview() {
      return new jy(this);
   }

   cb G() {
      return this.XN ? null : this.vI;
   }

   String l() {
      String var1 = this.vn.getValue().trim();
      if (this.vi) {
         return "Loading " + var1;
      } else {
         return this.v8 != null ? "Could not load " + (var1.isEmpty() ? "the schematic" : var1) + ": " + this.v8 : "No schematic loaded";
      }
   }

   public boolean M() {
      return this.jE;
   }

   public int X() {
      return this.v5;
   }

   public cb Z() {
      return this.vI;
   }

   public String o() {
      return this.v8;
   }

   public boolean X() {
      return this.vi;
   }

   public int[] i() {
      ak.t var1 = this.Eo;
      return var1 != null && var1.s ? (int[])var1.t.clone() : null;
   }

   private enum a {
      Z,
      X,
      G;
   }

   enum d {
      g,
      l,
      r;
   }

   record f(String dimension, int x, int y, int z) {
      BlockPos pos() {
         return new BlockPos(this.x, this.y, this.z);
      }

      @Override
      public String toString() {
         return this.dimension + " " + this.x + " " + this.y + " " + this.z;
      }
   }

   enum k {
      v,
      I,
      n;
   }

   enum l {
      C,
      f,
      G,
      N,
      H,
      E,
      j;
   }

   private record m(String file, cb schematic, String error) {
   }

   enum n {
      B,
      p,
      F;
   }

   static final class t {
      final cb L;
      final cb.b h;
      final int U;
      final long[] T;
      final int[] y;
      final int[] K;
      final int[] a;
      final BlockState[] q;
      final String[] P;
      final boolean[] O;
      final Map<Integer, int[]> C = new HashMap<>();
      final byte[] G;
      final int E;
      final int S;
      final int v;
      final int m;
      final int J;
      final int k;
      int M;
      final int[] t = new int[b_.i.values().length];
      final int[] X = new int[b_.i.values().length];
      boolean s;
      int[] Q = new int[0];
      final PriorityQueue<long[]> f = new PriorityQueue<>((var0, var1x) -> Long.compare(var1x[0], var0[0]));
      Object D;
      private Map<Long, Integer> b;
      private Map<Long, Integer> i;

      t(cb var1, cb.b var2) {
         this.L = var1;
         this.h = var2;
         this.U = var1.h();
         this.T = new long[this.U];
         this.y = new int[this.U];
         this.G = new byte[this.U];
         BlockState[] var3 = new BlockState[var1.r().size()];
         this.P = new String[var3.length];
         this.O = new boolean[var3.length];

         for (int var4 = 0; var4 < var3.length; var4++) {
            BlockState var5 = b_.f(var1.r().get(var4));
            var3[var4] = var5 == null ? null : b_.J(var5, var2);
            this.P[var4] = var5 == null ? "unknown block" : b_.i(var3[var4]);
            this.O[var4] = (boolean)(var5 != null && b_.D(var3[var4]) ? 1 : 0);
         }

         this.q = var3;
         int[] var10 = var2.bounds(var1);
         this.E = var10[0];
         this.S = var10[1];
         this.v = var10[2];
         this.m = var10[3];
         this.J = var10[4];
         this.k = var10[5];
         int var11 = this.J - this.S + 1;
         this.a = new int[var11 + 1];

         for (int var6 = 0; var6 < this.U; var6++) {
            int[] var7 = var2.world(var1.Z(var6), var1.L(var6), var1.d(var6));
            this.T[var6] = BlockPos.asLong(var7[0], var7[1], var7[2]);
            this.y[var6] = var1.A(var6);
            this.G[var6] = (byte)b_.i.V.ordinal();
            this.a[var7[1] - this.S + 1]++;
            BlockState var8 = var3[this.y[var6]];
            if (var8 != null && var8.getBlock() instanceof CrafterBlock) {
               int[] var9 = var1.j(var6);
               if (var9.length > 0) {
                  this.C.put(var6, var9);
               }
            }
         }

         for (int var12 = 1; var12 <= var11; var12++) {
            this.a[var12] = this.a[var12] + this.a[var12 - 1];
         }

         this.K = new int[this.U];
         int[] var13 = Arrays.copyOf(this.a, var11);
         int var14 = 0;

         while (var14 < this.U) {
            this.K[var13[BlockPos.getY(this.T[var14]) - this.S]++] = var14++;
         }
      }

      boolean x(int var1, int var2, int var3) {
         if (var1 >= this.E && var1 <= this.m && var2 >= this.S && var2 <= this.J && var3 >= this.v && var3 <= this.k) {
            if (this.b == null) {
               HashMap var4 = new HashMap(Math.max(16, this.U * 2));

               for (int var5 = 0; var5 < this.U; var5++) {
                  var4.put(this.T[var5], var5);
               }

               this.b = var4;
            }

            return this.b.containsKey(BlockPos.asLong(var1, var2, var3));
         } else {
            return false;
         }
      }

      boolean e(int var1, int var2, int var3) {
         int var4 = this.M(var1, var2, var3);
         return var4 >= 0 && this.g(var4) != null && this.P[this.y[var4]] == null;
      }

      int M(int var1, int var2, int var3) {
         return !this.x(var1, var2, var3) ? -1 : this.b.get(BlockPos.asLong(var1, var2, var3));
      }

      int C(int var1, int var2, int var3) {
         if (this.i == null) {
            HashMap var4 = new HashMap(Math.max(16, this.U * 2));

            for (int var5 = 0; var5 < this.U; var5++) {
               var4.put(zf.O(this.L.Z(var5), this.L.L(var5), this.L.d(var5)), var5);
            }

            this.i = var4;
         }

         return this.i.getOrDefault(zf.O(var1, var2, var3), -1);
      }

      BlockState g(int var1) {
         return this.q[this.y[var1]];
      }

      boolean v(int var1) {
         return this.O[this.y[var1]];
      }

      BlockPos S(int var1) {
         return BlockPos.of(this.T[var1]);
      }

      b_.i i(int var1) {
         return b_.i.values()[this.G[var1]];
      }

      int K(b_.i var1) {
         return this.t[var1.ordinal()];
      }

      u1.y f() {
         return new u1.y(this.E, this.S, this.v, this.m, this.J, this.k);
      }
   }

   enum y {
      X,
      A,
      W,
      L;
   }
}
