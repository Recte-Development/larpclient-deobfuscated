package com.larpclient;

import com.larpclient.module.Module;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class _7 extends _2 implements ac {
   private static _7 cM;
   private static boolean cd;
   static final int q = 2;
   static final int ca = 10;
   static final double k = 10.0;
   static final double cI = 110.0;
   static final double cb = 72.0;
   static final double I = 30.0;
   static final int cX = 6;
   static final int c2 = 4;
   static final int cf = 400;
   static final int m = 3600;
   static final double cj = 1500.0;
   static final int N = 200;
   private final com.larpclient.module.e cY = this.addSetting(
      new com.larpclient.module.e("Destination", "Where to fly, as X Z (for example 1200 -3400). X Y Z works too; the height is not used", "").L("X Z")
   );
   private final com.larpclient.module.b ct = this.g(
      "Flight Height",
      "Height to cruise at. Lower on long trips over flat land saves rockets; higher clears mountains and loads fewer chunks",
      290.0,
      180.0,
      320.0,
      10.0
   );
   private final com.larpclient.module.m cQ = this.C(
      "Auto Mending", "Land on the way and throw XP bottles from your hotbar to repair a Mending elytra long before it can break", true
   );
   private final com.larpclient.module.m g = this.C(
      "Log Off On Arrival", "Leave the server after landing, with a trip summary. Also leaves if the trip has to stop. Never in singleplayer", true
   );
   private final com.larpclient.module.m Y = this.C("Waypoint Line", "Draw the planned flight, a beacon at the destination and the landing spot", true);
   private final com.larpclient.module.m u = this.C(
      "Status Card", "Show a card at the top of the screen with distance, time left, rockets and elytra health", true
   );
   private final com.larpclient.module.o A = this.addSetting(
      new com.larpclient.module.o("Notifications", "How the pilot tells you what it is doing", "Toast", "Toast", "Chat", "Off")
   );
   private final com.larpclient.module.m cp = this.C(
      "Auto Buy Rockets", "On DonutSMP, buy Flight Duration 3 rockets from /ah on the ground when you run low, within the price limit and budget", false
   );
   private final com.larpclient.module.b cl = this.g("Rocket Stacks", "How many stacks of rockets to keep; it buys up to this many", 3.0, 1.0, 3.0, 1.0);
   private final com.larpclient.module.m cU = this.C(
      "Auto Buy XP Bottles", "On DonutSMP, buy XP bottles from /ah on the ground to mend a Mending elytra, within the price limit and budget", false
   );
   private final com.larpclient.module.b c = this.g("XP Bottle Stacks", "How many stacks of XP bottles to keep; it buys up to this many", 2.0, 1.0, 3.0, 1.0);
   private final com.larpclient.module.o c1 = this.addSetting(
      new com.larpclient.module.o(
         "Price Limit",
         "Most it pays per stack of 64. Cheap: rockets $35K, XP $320K. Normal: rockets $60K, XP $450K. High: rockets $150K, XP $700K",
         "Normal",
         pc.x.toArray(String[]::new)
      )
   );
   private final com.larpclient.module.o cB = this.addSetting(
      new com.larpclient.module.o("Trip Budget", "Most money it spends on one trip", "$2M", pc.d.toArray(String[]::new))
   );
   private final vk cv = new vk(this.getName());
   private final sy c5 = new sy(this).H(true);
   private final dn J = new dn();
   private final dn.o l = new dn.o();
   private final vu.x X = new vu.x();
   private final hw cH = new hw();
   private final bt j = new bt(this);
   private final Map<Long, Integer> G = new HashMap<>();
   private final List<Vec3> cG = new ArrayList<>();
   private final List<Vec3> U = new ArrayList<>();
   private _7.h ck = _7.h.z;
   private _7.d S = _7.d.c;
   private _7.k cn = _7.k.M;
   private double H;
   private double t;
   private boolean c0;
   private Object V;
   private Object cN;
   private Object cT;
   private ResourceKey<Level> cW;
   private int b;
   private int cR;
   private int F;
   private int C = -1000;
   private int p = -1000;
   private int cZ;
   private dn.g cx;
   private dn.l cr;
   private int c9 = -1000;
   private int R;
   private int o;
   private Vec3 r;
   private boolean cS;
   private boolean e;
   private double B = Double.MAX_VALUE;
   private int cK;
   private int c3 = -1000;
   private int cJ = -1000;
   private int co = 3;
   private int ty = -1000;
   private int cL = -1000;
   private int a;
   private int Z;
   private Float cF;
   private boolean d;
   private float D;
   private float cA;
   private boolean cC;
   private boolean x;
   private boolean T;
   private int cq = Integer.MIN_VALUE;
   private int ch = -1;
   private Vec3 L;
   private int i;
   private int W;
   private int cO;
   private int O;
   private int cs = -1000;
   private float c7 = 20.0F;
   private int c8 = -1000;
   private Vec3 E;
   private Vec3 w;
   private int c6;
   private int cg;
   private double s;
   private double f;
   private double cu;
   private long cD;
   private String cP = "";
   private String ce;
   private String c_;
   private long v;
   private int cw;
   private int c4;
   private final EnumSet<pc.c> cz = EnumSet.noneOf(pc.c.class);
   private String t5 = "";
   private int tY = -10000;
   private int P;
   private int cc;
   private final List<pc.c> cE = new ArrayList<>();
   private long ci;
   private int y;
   private boolean cV;
   static final double K = 48.0;
   private int cm = Integer.MIN_VALUE;
   private int cy = -1000;

   public _7() {
      super("Auto Elytra Pilot", "Fly to an X Z on an elytra: takeoff, cruise, landing, rockets and mending stops");
      cM = this;
      this.cY.B("Destination (X Z)");
      this.cl.a(this.cp::getValue);
      this.c.a(this.cU::getValue);
      this.c1.a(() -> this.cp.getValue() || this.cU.getValue());
      this.cB.a(() -> this.cp.getValue() || this.cU.getValue());
      if (!cd) {
         cd = (boolean)1;
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            _7 var2 = cM;
            if (!var1 && var2 != null && var2.cv.W()) {
               vk.a(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            _7 var1 = cM;
            if (var1 != null) {
               var1.Z();
            }
         });
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            _7 var1 = cM;
            if (var1 != null) {
               var1.M();
            }
         });
      }
   }

   public static _7 E() {
      return cM;
   }

   @Override
   public boolean allowsScreenInteractions() {
      return mc == null || !(d1.E() instanceof AbstractContainerScreen);
   }

   @Override
   public void disable() {
      this.ce = null;
      super.disable();
   }

   public static double[] g(String var0) {
      if (var0 == null) {
         return null;
      }

      String[] var1 = var0.trim().replace(',', ' ').split("\\s+");
      if (var1.length != 2 && var1.length != 3) {
         return null;
      }

      double[] var2 = new double[var1.length];

      try {
         for (int var3 = 0; var3 < var1.length; var3++) {
            var2[var3] = Double.parseDouble(var1[var3]);
         }
      } catch (NumberFormatException var7) {
         return null;
      }

      double var8 = var2[0];
      double var5 = var2[var1.length - 1];
      return Double.isFinite(var8) && Double.isFinite(var5) && !(Math.abs(var8) > 2.9999984E7) && !(Math.abs(var5) > 2.9999984E7)
         ? new double[]{var8, var5}
         : null;
   }

   static String N(double var0, double var2) {
      return (long)Math.floor(var0) + " " + (long)Math.floor(var2);
   }

   static String Y(double var0) {
      return String.format(Locale.ROOT, "%,d", Math.round(var0));
   }

   @Override
   public List<ac.b> commands() {
      return List.of(
         new ac.b("Set the destination", "Fly to where you look", this::z),
         new ac.b("Set the destination", "Remember where you stand", this::g),
         new ac.b("Set the destination", "Clear destination", this::I),
         new ac.b("Buying on DonutSMP", "Buy rockets and XP now", this::v)
      );
   }

   private String z() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      Vec3 var1 = mc.player.getEyePosition();
      Vec3 var2 = mc.player.getLookAngle();
      double var3 = Math.max(64.0, mc.options.getEffectiveRenderDistance() * 16.0);
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var1.add(var2.scale(var3)), Block.OUTLINE, Fluid.ANY, mc.player));
      Vec3 var6;
      String var7;
      if (var5.getType() == Type.BLOCK) {
         var6 = var5.getLocation();
         var7 = "the block you look at";
      } else {
         Vec3 var8 = new Vec3(var2.x, 0.0, var2.z);
         if (var8.lengthSqr() < 1.0E-6) {
            return "Look toward the horizon, not straight up or down.";
         }

         var6 = mc.player.position().add(var8.normalize().scale(1000.0));
         var7 = "1,000 blocks along your view (nothing loaded is in sight there)";
      }

      this.cY.T(N(var6.x, var6.z));
      this.F();
      double var10 = Math.hypot(var6.x - mc.player.getX(), var6.z - mc.player.getZ());
      _q.z(this.getName(), "Destination set from the view to {} ({} blocks)", this.cY.getValue(), Y(var10));
      return "Destination " + this.cY.getValue().replace(' ', ',').replace(",", ", ") + ", " + var7 + ", " + Y(var10) + " blocks away.";
   }

   private String g() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      this.cY.T(N(mc.player.getX(), mc.player.getZ()));
      this.F();
      return "Destination set to where you stand: " + this.cY.getValue().replace(" ", ", ") + ". Fly away and turn the pilot on to come back.";
   }

   private String v() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "The pilot buys on its own during a trip.";
      }

      if (!this.cE.isEmpty()) {
         return "Already buying; move to stop it.";
      }

      if (!this.cp.getValue() && !this.cU.getValue()) {
         return "Turn on Auto Buy Rockets or Auto Buy XP Bottles first.";
      }

      if (mc.player.onGround() && !(mc.player.getDeltaMovement().horizontalDistance() > 0.01)) {
         if (this.cp.getValue()) {
            this.cE.add(pc.c.x);
         }

         if (this.cU.getValue()) {
            this.cE.add(pc.c.T);
         }

         this.ci = 0L;
         this.y = 0;
         _q.z(this.getName(), "Buying now: {}", this.cE);
         return "Buying "
            + (this.cp.getValue() ? "rockets" : "")
            + (this.cE.size() == 2 ? " and " : "")
            + (this.cU.getValue() ? "XP bottles" : "")
            + " on /ah, at most "
            + this.c1.getValue().toLowerCase(Locale.ROOT)
            + " prices and "
            + pc.b(pc.V(this.cB.getValue()))
            + ". Moving stops it.";
      } else {
         return "Stand still on the ground first.";
      }
   }

   private void Z() {
      if (!this.cE.isEmpty()) {
         if (!this.isInGame() || this.isRuntimeActive()) {
            this.cv.B("the restock was interrupted");
            this.cE.clear();
         } else if (this.L()) {
            this.cv.B("you moved");
            this.r("Stopped buying: you moved.");
         } else {
            pc.c var1 = this.cE.getFirst();
            int var2 = var1 == pc.c.x ? 1 : 0;
            if (!this.cv.W()) {
               int var9 = var2 != 0 ? sy.a(mc.player) : this.I();
               int var4 = (var2 != 0 ? this.l() : this.C()) - var9;
               long var5 = pc.V(this.cB.getValue());
               long var7 = var5 < 0L ? -1L : Math.max(0L, var5 - this.ci);
               if (var4 > 0 && var7 != 0L) {
                  this.cv.L(var1, var4, pc.H(this.c1.getValue(), var1), var7);
               } else {
                  this.cE.removeFirst();
                  if (this.cE.isEmpty()) {
                     this.r(null);
                  }
               }
            } else if (this.cv.t() != vk.q.M) {
               this.ci = this.ci + this.cv.u();
               this.y = this.y + this.cv.j();
               String var3 = var2 != 0 ? "rockets" : "XP bottles";
               if (this.cv.j() > 0) {
                  this.b(ib.v.x, "Bought " + this.cv.j() + " " + var3 + " for " + pc.b(this.cv.u()));
               }

               if (this.cv.E() == vk.q.w) {
                  this.b(ib.v.o, "Could not buy " + var3 + ": " + this.cv.a());
               }

               this.cE.removeFirst();
               if (this.cE.isEmpty() || this.cv.M()) {
                  this.r(null);
               }
            }
         }
      }
   }

   private void r(String var1) {
      this.cE.clear();
      String var2 = var1 != null ? var1 : (this.y == 0 ? "Bought nothing." : "Done buying: " + this.y + " items for " + pc.b(this.ci) + ".");
      _q.z(this.getName(), "Buying now ended: {}", var2);
      this.S(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
   }

   private String I() {
      if (this.isRuntimeActive()) {
         return "Turn the pilot off before clearing the destination.";
      }

      this.cY.T("");
      this.F();
      return "Destination cleared.";
   }

   private void F() {
      LarpClient var1 = LarpClient.N();
      if (var1 != null && var1.h() != null) {
         var1.h().z();
      }
   }

   @Override
   public String summary() {
      double[] var1 = g(this.cY.getValue());
      if (var1 == null) {
         return this.cY.getValue().isBlank()
            ? "Type a destination as X Z, or press Fly to where you look."
            : "\"" + this.cY.getValue() + "\" is not a destination; type it as X Z, for example 1200 -3400.";
      }

      String var2 = "Destination: " + N(var1[0], var1[1]).replace(" ", ", ");
      if (!this.isInGame()) {
         return var2 + ".";
      }

      double var3 = Math.hypot(var1[0] - mc.player.getX(), var1[1] - mc.player.getZ());
      return this.isRuntimeActive() && this.c0 ? var2 + ". " + this.K() + "." : var2 + ", " + Y(var3) + " blocks away. Turn the pilot on to fly there.";
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs an elytra and firework rockets in your offhand or hotbar.";
      }

      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      String var2 = var1.is(Items.ELYTRA)
         ? "elytra " + bt.J(var1) + " of " + var1.getMaxDamage() + " uses" + (bt.e(var1) ? " (Mending)" : "")
         : (this.S() >= 0 ? "elytra in your hotbar, not worn yet" : "no elytra worn");
      return "Rockets " + sy.a(mc.player) + ", XP bottles " + this.I() + ", " + var2 + ".";
   }

   @Override
   public List<ac.r> settingGroups() {
      return List.of(
         new ac.r("Destination", List.of(this.cY)),
         new ac.r("While flying", List.of(this.cQ, this.g)),
         new ac.r("Buying on DonutSMP", List.of(this.cp, this.cl, this.cU, this.c, this.c1, this.cB)),
         new ac.r("Display", List.of(this.Y, this.u, this.A)),
         ac.r.advanced("Advanced", List.of(this.ct))
      );
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.s();
      if (this.ce != null) {
         String var11 = this.ce;
         this.ce = null;
         this.w("it stopped earlier because " + var11 + ". Turn it on again to fly from here");
      } else {
         double[] var1 = g(this.cY.getValue());
         if (var1 == null) {
            this.w(this.cY.getValue().isBlank() ? "type a destination as X Z first" : "type the destination as X Z, for example 1200 -3400");
         } else {
            String var2 = ac.K(this);
            if (var2 != null) {
               this.w("turn off " + var2 + " first; one bot runs at a time");
            } else if (mc.level.dimension() == Level.NETHER) {
               this.w("it flies in the Overworld and the End, not the Nether");
            } else if (!this.x() && this.S() < 0) {
               this.w("wear an elytra, or put one in your hotbar");
            } else {
               int var3 = sy.a(mc.player);
               if (var3 == 0 && !this.cp.getValue()) {
                  sy.v var12 = sy.w(mc.player, 0);
                  this.w(
                     var12.starsOnly()
                        ? "your rockets have firework stars, which hurt when boosting; use plain rockets"
                        : (
                           this.p()
                              ? "put firework rockets in your offhand or hotbar; the pilot never clicks inventory slots"
                              : "you need firework rockets in your offhand or hotbar"
                        )
                  );
               } else {
                  this.H = Math.floor(var1[0]) + 0.5;
                  this.t = Math.floor(var1[1]) + 0.5;
                  double var4 = this.D();
                  if (var4 <= 30.0) {
                     this.w("you are already at the destination");
                  } else {
                     this.c0 = (boolean)1;
                     sy.z(this);
                     ArrayList var6 = new ArrayList();

                     for (String var8 : List.of("ElytraFly", "ElytraHelper", "ElytraMotion")) {
                        Module var9 = LarpClient.N().j().U(var8);
                        if (var9 != null && var9.isEnabled()) {
                           var6.add(var8);
                        }
                     }

                     if (!var6.isEmpty()) {
                        _q.z(this.getName(), "{} paused while the pilot flies", String.join(", ", var6));
                     }

                     this.V = mc.level;
                     this.cN = mc.player;
                     this.cT = mc.getConnection() == null ? null : mc.getConnection().getConnection();
                     this.cW = mc.level.dimension();
                     this.cD = System.currentTimeMillis();
                     this.f = var4;
                     this.P = var3;
                     this.c7 = mc.player.getHealth();
                     this.E = mc.player.position();
                     this.j.v();
                     this.Z(_7.h.z);
                     this.b(ib.v.f, "Flying to " + N(this.H, this.t).replace(" ", ", ") + " (" + Y(var4) + " blocks)");
                     this.S(
                        "flying to "
                           + N(this.H, this.t).replace(" ", ", ")
                           + ", "
                           + Y(var4)
                           + " blocks, at height "
                           + this.ct.getValue().intValue()
                           + " with "
                           + var3
                           + " rockets."
                     );
                     ArrayList var13 = new ArrayList();
                     double var14 = var4 / 1000.0 * 2.6 + 5.0;
                     if (var3 < var14 && !this.cp.getValue()) {
                        var13.add("about " + (int)Math.ceil(var14) + " rockets are needed and you have " + var3 + "; it lands safely when they run out");
                     }

                     if (this.p()) {
                        var13.add("rockets in your main inventory are not used, only the offhand and hotbar");
                     }

                     if (!var6.isEmpty()) {
                        var13.add(String.join(" and ", var6) + (var6.size() == 1 ? " pauses" : " pause") + " while the pilot flies and resumes after it lands");
                     }

                     ItemStack var10 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
                     if (this.cQ.getValue()
                        && var10.is(Items.ELYTRA)
                        && bt.e(var10)
                        && this.I() == 0
                        && !this.cU.getValue()
                        && bt.J(var10) < E(var4) / (1 + this.X()) + this.e()) {
                        var13.add("no XP bottles in the hotbar to mend the elytra with on the way");
                     }

                     if (var10.is(Items.ELYTRA) && !bt.e(var10) && this.S() < 0 && bt.J(var10) < E(var4)) {
                        var13.add("the elytra has " + bt.J(var10) + " uses left, no Mending and no spare, so it may land early");
                     }

                     if (bt.a(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                        var13.add("no food in the hotbar");
                     }

                     if (this.cp.getValue() || this.cU.getValue()) {
                        var13.add(
                           "it may buy "
                              + (this.cp.getValue() ? "rockets" : "")
                              + (this.cp.getValue() && this.cU.getValue() ? " and " : "")
                              + (this.cU.getValue() ? "XP bottles" : "")
                              + " from /ah on the ground, at most "
                              + this.c1.getValue().toLowerCase(Locale.ROOT)
                              + " prices and "
                              + pc.b(pc.V(this.cB.getValue()))
                              + " this trip"
                        );
                     }

                     if (!var13.isEmpty()) {
                        this.S("heads up: " + String.join("; ", var13) + ".");
                     }

                     _q.z(
                        this.getName(),
                        "Started: to {} from {}, {} blocks, height {}, {} rockets, elytra {} uses, {}",
                        N(this.H, this.t),
                        mc.player.blockPosition().toShortString(),
                        Y(var4),
                        this.ct.getValue().intValue(),
                        var3,
                        this.p(),
                        this.cW
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.c0) {
         this.ce = "the world, the player or the connection changed mid-flight";
         _q.z(this.getName(), "Interrupted in {} after {} blocks: {}", this.ck, Y(this.s), this.ce);
      }

      super.onDisable();
      this.j.b();
      this.c5.x();
      this.s();
      if (this.c_ != null) {
         this.P(this.c_);
         this.c_ = null;
      }
   }

   private void s() {
      sy.v(this);
      this.cv.B("the trip ended");
      this.v = 0L;
      this.cw = this.c4 = 0;
      this.cz.clear();
      this.c0 = (boolean)0;
      this.V = this.cN = this.cT = null;
      this.cW = null;
      this.ck = _7.h.z;
      this.S = _7.d.c;
      this.cn = _7.k.M;
      this.b = this.cR = this.F = 0;
      this.C = this.p = this.c9 = -1000;
      this.cZ = 0;
      this.cx = null;
      this.cr = null;
      this.R = this.o = 0;
      this.r = null;
      this.cS = this.e = (boolean)0;
      this.B = Double.MAX_VALUE;
      this.cK = 0;
      this.c3 = this.cJ = this.ty = -1000;
      this.co = 3;
      this.cL = -1000;
      this.a = this.Z = 0;
      this.cF = null;
      this.d = (boolean)1;
      this.cC = (boolean)0;
      this.x = this.T = (boolean)0;
      this.cq = Integer.MIN_VALUE;
      this.ch = -1;
      this.L = null;
      this.i = this.W = this.cO = this.O = 0;
      this.cs = -1000;
      this.c8 = -1000;
      this.E = this.w = null;
      this.c6 = 0;
      this.cg = 0;
      this.s = this.f = this.cu = 0.0;
      this.cP = "";
      this.cG.clear();
      this.U.clear();
      this.G.clear();
      this.cH.W();
      this.P = this.cc = 0;
      this.cm = Integer.MIN_VALUE;
      this.cy = -1000;
   }

   private void w(String var1) {
      _q.z(this.getName(), "Not started: {}", var1);
      this.S("not started, " + var1 + ".");
      ib.L().t("Auto Elytra Pilot", n(var1), ib.v.o);
      this.P("Not started: " + var1);
      this.c_ = this.getStatus();
      this.disable();
   }

   @Override
   public void onTick() {
      this.c5.v();
      this.x = this.T = (boolean)0;
      this.cC = (boolean)0;
      boolean var2 = false;
      byte var1 = this.c0;
      int var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (mc.level == this.V && mc.player == this.cN) {
            if (mc.level.dimension() != this.cW) {
               this.l(false, "you changed dimension");
            } else if (!mc.player.isAlive()) {
               this.l(false, "you died");
            } else {
               byte var4 = 1;
               int var3 = this.b;
               this.b = var3 + var4;
               this.cq = mc.player.tickCount;
               this.S();
               if (ij.y()) {
                  this.P("Paused while Freecam is on");
               } else if (this.L()) {
                  this.cH.W();
                  this.P("Paused while you steer");
               } else {
                  if (!this.g() && !this.X()) {
                     switch (this.ck) {
                        case z:
                           this.J();
                           break;
                        case i:
                           this.o();
                           break;
                        case o:
                           this.P();
                           break;
                        case Y:
                           this.T();
                           break;
                        case V:
                           this.h();
                           break;
                        case C:
                           this.e();
                           break;
                        case u:
                           this.Y();
                           break;
                        case l:
                           this.t();
                           break;
                        case c:
                           this.G();
                     }
                  }

                  int var9 = this.isEnabled();
                  boolean var6 = false;
                  byte var5 = (byte)var9;
                  var9 = Integer.signum(var5);
                  if (((var9 | -var9) >>> 31 ^ 1) == 0) {
                     boolean var8 = false;
                     byte var7 = this.c0;
                     var9 = Integer.signum(var7);
                     if (((var9 | -var9) >>> 31 ^ 1) == 0) {
                        this.A();
                     }
                  }
               }
            }
         } else {
            this.l(false, "the world or the player changed");
         }
      }
   }

   private boolean L() {
      return mc.isWindowActive()
         && d1.E() == null
         && (nz.K(mc.options.keyUp) || nz.K(mc.options.keyDown) || nz.K(mc.options.keyLeft) || nz.K(mc.options.keyRight) || nz.K(mc.options.keyJump));
   }

   private void Z(_7.h var1) {
      if (var1 == _7.h.C && this.ck != _7.h.C) {
         this.c9 = -1000;
         this.R = 0;
      }

      if (var1 == _7.h.u && this.ck != _7.h.u) {
         this.cn = _7.k.M;
         this.cz.clear();
      }

      if (var1 == _7.h.i) {
         this.a = 0;
         this.Z++;
         this.cF = null;
         this.r = null;
         this.o = 0;
         this.p = -1000;
         this.cZ = 0;
         if (this.S != _7.d.d) {
            this.S = _7.d.c;
         }
      }

      if (var1 == _7.h.Y) {
         this.Z = 0;
      }

      if (var1 == _7.h.V && this.ck != _7.h.V) {
         this.B = Double.MAX_VALUE;
         this.cK = this.b;
      }

      if (var1 != _7.h.V) {
         this.e = (boolean)0;
      }

      if (var1 != this.ck) {
         _q.z(this.getName(), "{} -> {} at {} ({} blocks to go, job {})", this.ck, var1, mc.player.blockPosition().toShortString(), Y(this.D()), this.S);
      }

      this.ck = var1;
      this.cR = this.b;
   }

   private int x() {
      return this.b - this.cR;
   }

   private void J() {
      if (mc.player.isInWater()) {
         this.L();
      } else if (this.O()) {
         this.Z(_7.h.o);
      } else if (!mc.player.onGround()) {
         this.P("Waiting to land");
      } else {
         this.P("Getting ready");
         if (!this.x()) {
            if (!this.P()) {
               this.l(false, "there is no usable elytra left");
            }
         } else if (this.D() <= 30.0) {
            this.S = _7.d.c;
            this.Z(_7.h.l);
         } else if (this.x() >= 4) {
            _7.k var1 = this.u(true);
            if (var1 != _7.k.M) {
               this.S = _7.d.r;
               this.Z(_7.h.u);
            } else if (sy.a(mc.player) == 0) {
               this.l(false, this.b());
            } else {
               int var2 = this.p();
               if (var2 >= 0 && var2 < 30) {
                  this.l(false, "the elytra is almost broken and cannot be mended here");
               } else if (this.Z >= 6) {
                  this.l(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.S = _7.d.c;
                  this.Z(_7.h.i);
               }
            }
         }
      }
   }

   private void o() {
      if (mc.player.isInWater()) {
         this.L();
      } else if (this.O()) {
         this.Z(_7.h.o);
      } else if (!this.x()) {
         this.Z(_7.h.z);
      } else if (!this.c5.T()) {
         this.Z(_7.h.z);
      } else {
         this.P("Taking off");
         if (mc.player.onGround()) {
            if (this.b - this.cL >= 14) {
               if (this.a >= 5) {
                  this.l(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.d = this.n(16);
                  if (!this.d) {
                     this.cF = this.a();
                     if (this.cF == null) {
                        this.l(false, "there is no open sky to take off from; stand somewhere open");
                        return;
                     }
                  }

                  float var1 = this.d ? mc.player.getYRot() : this.cF;
                  float var2 = this.d ? -88.0F : -35.0F;
                  this.M(var1, var2);
                  this.c5.h(3);
                  if (Math.abs(Mth.wrapDegrees(var1 - mc.player.getYRot())) <= 10.0F && Math.abs(var2 - mc.player.getXRot()) <= 10.0F) {
                     this.c5.T();
                     this.cL = this.b;
                     this.a++;
                     _q.c(this.getName(), "Takeoff jump {} ({})", this.a, this.d ? "straight up" : "toward open sky at yaw " + (int)var1);
                  }
               }
            }
         } else {
            this.M(this.d ? mc.player.getYRot() : this.cF, this.d ? -88.0F : -35.0F);
            if (mc.player.getDeltaMovement().y < 0.12 && this.b - this.cL >= 2) {
               this.a();
            }
         }
      }
   }

   private void a() {
      if (this.b - this.cL >= 2) {
         this.c5.T();
         this.cL = this.b;
      } else {
         this.c5.d();
      }
   }

   private void P() {
      if (this.K()) {
         double var1 = this.D();
         this.l.y = this.L(var1);
         if (!this.o()) {
            double var3 = mc.player.getY();
            boolean var5 = this.D();
            this.P("Climbing to " + (int)(Math.round(this.l.y / 10.0) * 10L));
            if (var3 >= this.l.y - 15.0 || !var5 && this.x() > 600) {
               this.Z(var1 <= this.C() ? _7.h.V : _7.h.Y);
            } else if (var5) {
               this.M(mc.player.getYRot(), -80.0F);
               if (this.z() && mc.player.getXRot() < -60.0F) {
                  this.q();
               }
            } else {
               this.M(this.H, this.t, Double.NaN, true);
            }
         }
      }
   }

   private void T() {
      if (this.K()) {
         double var1 = this.D();
         this.l.y = this.L(var1);
         if (!this.o()) {
            if (var1 <= this.C()) {
               this.S = _7.d.c;
               this.r = null;
               this.o = 0;
               this.Z(_7.h.V);
            } else {
               this.P("Flying to " + N(this.H, this.t).replace(" ", ", "));
               this.O();
               this.M(this.H, this.t, Double.NaN, true);
            }
         }
      }
   }

   private void h() {
      if (this.K()) {
         if (this.S != _7.d.c || !this.o()) {
            int var1 = this.S != _7.d.c ? 1 : 0;
            if (this.r == null && this.b - this.p >= (var1 != 0 ? 10 : 20)) {
               this.Y((boolean)var1);
            }

            double var2 = this.r != null ? this.r.x : this.H;
            double var4 = this.r != null ? this.r.z : this.t;
            double var6 = this.r != null ? this.r.y : this.y(var2, var4);
            double var8 = Math.hypot(var2 - mc.player.getX(), var4 - mc.player.getZ());
            double var10 = mc.player.getY() - var6;
            this.l.y = this.L(this.D());
            this.P(
               this.S == _7.d.c
                  ? (this.r == null ? "Descending, looking for a landing spot" : "Descending to land")
                  : (this.S == _7.d.r ? "Landing for a stop" : "Landing to stop safely")
            );
            if (this.r == null && this.S != _7.d.c) {
               this.M(this.H, this.t, Double.NaN, var1 == 0 || sy.a(mc.player) > 3);
            } else {
               if (this.r != null) {
                  if (var10 < this.B - 3.0) {
                     this.B = var10;
                     this.cK = this.b;
                  } else if (this.b - this.cK > 700) {
                     this.b(ib.v.f, "Cannot get down to this landing spot, picking another");
                     this.n();
                     this.cK = this.b;
                     return;
                  }

                  if (var8 <= 50.0 && var10 <= 28.0 && var10 >= -1.0) {
                     this.Z(_7.h.C);
                     return;
                  }
               }

               double var12 = var6 + 14.0 + Math.max(0.0, var8 - 45.0) * 0.095;
               double var14 = mc.player.getY() - var12;
               if (!this.e && var8 < 170.0 && var10 > 32.0 && (var14 > 22.0 || var8 < 60.0)) {
                  this.e = (boolean)1;
                  _q.c(this.getName(), "Circling down: {} blocks above the slope, {} across", (int)var14, (int)var8);
               }

               if (this.e && (var10 <= 26.0 || var14 < 8.0 && var8 >= 60.0 || var8 > 260.0)) {
                  this.e = (boolean)0;
               }

               if (this.e) {
                  double var16 = Math.atan2(mc.player.getZ() - var4, mc.player.getX() - var2) + Math.toRadians(55.0);
                  this.l.w = var14 > 40.0 ? 3.0 : 1.9;
                  this.M(var2 + Math.cos(var16) * 42.0, var4 + Math.sin(var16) * 42.0, var6, true);
               } else {
                  this.l.w = 1.45;
                  this.M(var2, var4, var6, true);
               }
            }
         }
      }
   }

   private void e() {
      if (mc.player.isInWater()) {
         if (mc.player.isUnderWater()) {
            this.T = (boolean)1;
         } else {
            this.v();
         }
      } else if (mc.player.onGround() && !this.O()) {
         this.v();
      } else if (!this.O()) {
         this.P("Landing");
         if (mc.player.getDeltaMovement().y < -0.3 && !mc.player.hasEffect(MobEffects.LEVITATION)) {
            this.a();
         }
      } else if (this.r == null) {
         this.Z(_7.h.V);
      } else {
         double var1 = Math.hypot(this.r.x - mc.player.getX(), this.r.z - mc.player.getZ());
         double var3 = mc.player.getY() - this.r.y;
         if (!(var3 < -4.0) && this.x() <= 700 && this.R <= 24 && !(var1 > 90.0)) {
            this.P("Landing (" + (int)var1 + " blocks out, " + (int)Math.max(0.0, var3) + " up)");
            if (this.b - this.c9 >= 2 || this.cr == null) {
               this.c9 = this.b;
               this.f();
               this.cr = this.J.w(this::D, this::W, this.X, this.r.x, this.r.y, this.r.z, this.U(), this.q());
               this.R = this.cr.cost() >= 900.0 ? this.R + 1 : 0;
            }

            this.M(this.cr.yaw(), this.cr.pitch());
         } else {
            this.m(var3 < -4.0 ? "below the spot" : (var1 > 90.0 ? "too far from the spot" : "the approach did not work out"));
         }
      }
   }

   private void v() {
      if (this.S == _7.d.c) {
         double var1 = this.r == null ? Double.MAX_VALUE : Math.hypot(this.r.x - this.H, this.r.z - this.t);
         double var3 = this.r == null ? Double.MAX_VALUE : Math.hypot(this.r.x - mc.player.getX(), this.r.z - mc.player.getZ());
         if (this.D() <= 72.0 || var3 <= 25.0 || this.D() <= var1 + 20.0) {
            this.Z(_7.h.l);
            return;
         }

         _q.z(this.getName(), "Touched down {} blocks short of the spot; taking off again", Y(var3));
         if (this.r != null) {
            this.n();
         }

         this.Z(_7.h.z);
      } else if (this.S == _7.d.d) {
         if (this.D() <= 72.0) {
            _q.z(this.getName(), "Stopped at the destination; counting it as arrived ({})", this.cP);
            this.Z(_7.h.l);
         } else {
            this.Z(_7.h.c);
         }
      } else {
         this.Z(_7.h.u);
      }
   }

   private void m(String var1) {
      this.o++;
      this.b(ib.v.f, "Go-around (" + var1 + ")");
      if (this.o >= 4) {
         this.n();
         this.o = 0;
      }

      this.Z(_7.h.V);
   }

   private void Y() {
      if (mc.player.isInWater()) {
         this.L();
      } else if (this.O()) {
         this.Z(_7.h.C);
      } else if (!mc.player.onGround()) {
         this.P("Waiting to land");
      } else if (this.R()) {
         this.j.s();
         this.cv.B("a threat at the stop");
         int var3 = sy.a(mc.player) < 2 || !this.x() || this.p() >= 0 && this.p() < 30 ? 0 : 1;
         if (var3 != 0) {
            this.b(ib.v.o, "Danger at the landing spot, taking off");
            this.cG.add(mc.player.position());
            this.Z(_7.h.i);
         } else {
            this.l(false, "a mob reached the stop and there was no way to take off");
         }
      } else {
         if (this.cn == _7.k.M) {
            this.cn = this.u(false);
            this.F = this.b;
            if (this.cn == _7.k.M) {
               if (!this.x()) {
                  if (this.P()) {
                     return;
                  }

                  this.l(false, "there is no usable elytra left");
                  return;
               }

               if (sy.a(mc.player) == 0) {
                  this.l(false, this.b());
                  return;
               }

               int var2 = this.p();
               if (var2 >= 0 && var2 < 40) {
                  this.l(false, "the elytra is too worn to fly on and cannot be mended");
                  return;
               }

               if (this.D() <= 30.0) {
                  this.S = _7.d.c;
                  this.Z(_7.h.l);
                  return;
               }

               this.S = _7.d.c;
               this.b(ib.v.x, "Stop done: " + sy.a(mc.player) + " rockets, " + this.I() + " XP bottles");
               this.Z(_7.h.i);
               return;
            }

            _q.z(this.getName(), "Stop task: {}", this.cn);
         }

         int var1 = this.cn != _7.k.l && this.cn != _7.k.t ? 0 : 1;
         if (this.b - this.F > (var1 != 0 ? 3600 : 400)) {
            _q.z(this.getName(), "Stop task {} timed out", this.cn);
            if (var1 != 0) {
               this.cv.B("it took too long");
               this.cz.add(this.cn == _7.k.l ? pc.c.x : pc.c.T);
            }

            this.I();
         } else {
            switch (this.cn) {
               case w:
                  this.P("Eating");
                  if (!this.j.T(4)) {
                     this.I();
                  }
                  break;
               case C:
                  this.c();
                  break;
               case K:
                  this.P("Putting on the spare elytra");
                  if (!this.P()) {
                     this.I();
                  }
                  break;
               case l:
                  this.V(pc.c.x);
                  break;
               case t:
                  this.V(pc.c.T);
                  break;
               default:
                  this.cn = _7.k.M;
            }
         }
      }
   }

   private void I() {
      this.j.s();
      this.cn = _7.k.M;
   }

   private boolean S(pc.c var1) {
      boolean var2 = var1 == pc.c.x ? this.cp.getValue() : this.cU.getValue();
      return var2 && !this.cz.contains(var1) && !this.cV && this.e() > 0L;
   }

   private long e() {
      long var1 = pc.V(this.cB.getValue());
      return var1 < 0L ? Long.MAX_VALUE : Math.max(0L, var1 - this.v);
   }

   private int l() {
      return this.cl.getValue().intValue() * 64;
   }

   private int C() {
      return this.c.getValue().intValue() * 64;
   }

   private void V(pc.c var1) {
      int var2 = var1 == pc.c.x ? 1 : 0;
      if (!this.cv.W()) {
         int var7 = var2 != 0 ? sy.a(mc.player) : this.I();
         int var8 = (var2 != 0 ? this.l() : this.C()) - var7;
         if (var8 <= 0) {
            this.I();
         } else {
            long var5 = this.e();
            this.cv.L(var1, var8, pc.H(this.c1.getValue(), var1), var5 == Long.MAX_VALUE ? -1L : var5);
            this.P("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah");
         }
      } else {
         vk.q var3 = this.cv.t();
         this.P("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah: " + this.cv.j() + " so far");
         if (var3 != vk.q.M) {
            this.v = this.v + this.cv.u();
            if (var2 != 0) {
               this.cw = this.cw + this.cv.j();
            } else {
               this.c4 = this.c4 + this.cv.j();
            }

            if (this.cv.M()) {
               this.cV = (boolean)1;
            }

            String var4 = var2 != 0 ? "rockets" : "XP bottles";
            if (this.cv.j() > 0) {
               this.b(ib.v.x, "Bought " + this.cv.j() + " " + var4 + " for " + pc.b(this.cv.u()) + (this.cv.a().isEmpty() ? "" : " (" + this.cv.a() + ")"));
            }

            if (var3 == vk.q.w || !this.cv.a().isEmpty()) {
               this.cz.add(var1);
            }

            if (var3 == vk.q.w) {
               this.b(ib.v.o, "Could not buy " + var4 + ": " + this.cv.a());
            }

            this.I();
         }
      }
   }

   private _7.k u(boolean var1) {
      LocalPlayer var2 = mc.player;
      if ((var2.getFoodData().getFoodLevel() <= 10 || var2.getHealth() < 15.0F && var2.getFoodData().getFoodLevel() < 20)
         && bt.a(var0 -> var0.get(DataComponents.FOOD) != null) >= 0
         && this.J()) {
         return _7.k.w;
      } else {
         int var3 = this.p();
         int var4 = this.t();
         boolean var5 = this.k();
         int var6 = var5 && this.S(pc.c.T) ? 1 : 0;
         if (var3 >= 0 && var3 <= this.e() && (!var5 || this.I() <= 0 && var6 == 0) && this.S() >= 0) {
            return _7.k.K;
         } else {
            int var7 = var5 && var3 >= 0 && var3 < var4 * (var1 ? 0.9 : 0.97) ? 1 : 0;
            int var8 = sy.a(mc.player);
            if (var7 != 0 && this.I() == 0 && var6 != 0) {
               return _7.k.t;
            } else if (var7 != 0 && this.I() > 0) {
               return _7.k.C;
            } else if (this.D() < 1500.0 && var8 >= 4) {
               return _7.k.M;
            } else if (var8 < this.l() / 2 && this.S(pc.c.x)) {
               return _7.k.l;
            } else {
               return var6 != 0 && this.I() < this.C() / 2 ? _7.k.t : _7.k.M;
            }
         }
      }
   }

   private boolean J() {
      return !mc.player.isUsingItem() || mc.player.getUseItem().get(DataComponents.FOOD) != null;
   }

   private void c() {
      int var1 = this.p();
      int var2 = this.t();
      ItemStack var3 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      if (var1 >= 0 && var3.isDamaged() && var1 < var2) {
         if (this.b - this.F < 2) {
            this.i = var1;
            this.W = this.b;
         }

         if (var1 > this.i) {
            this.i = var1;
            this.W = this.b;
         }

         if (this.b - this.W > 200) {
            this.cO++;
            this.b(ib.v.o, "Mending stopped working here (other Mending gear may be taking the XP)");
            this.I();
         } else {
            this.P("Mending the elytra: " + var1 * 10 / Math.max(1, var2) * 10 + "%");
            if (!this.j.w(() -> this.O++)) {
               _q.z(this.getName(), "Mending ended at {} of {} uses: no XP bottles left in the hotbar", var1, var2);
               this.I();
            }
         }
      } else {
         if (var1 >= 0) {
            this.cO = 0;
            this.b(ib.v.x, "Elytra repaired");
         }

         this.I();
      }
   }

   private void t() {
      this.P("Arrived");
      if (mc.player.isInWater()) {
         if (mc.player.isUnderWater()) {
            this.T = (boolean)1;
         } else if (this.x() > 20) {
            this.l(true, null);
         }
      } else if (!mc.player.onGround()) {
         if (this.O()) {
            this.S = _7.d.c;
            this.Z(_7.h.C);
         }
      } else {
         Vec3 var1 = mc.player.getDeltaMovement();
         if (this.x() >= 10 || Math.hypot(var1.x, var1.z) <= 0.03) {
            this.l(true, null);
         }
      }
   }

   private void G() {
      this.P("Stopping: " + this.cP);
      if (!mc.player.onGround() && !mc.player.isInWater()) {
         if (this.O()) {
            this.Z(_7.h.C);
         }
      } else {
         this.l(false, this.cP.isEmpty() ? "the trip could not go on" : this.cP);
      }
   }

   private void M(double var1, double var3, double var5, boolean var7) {
      if (this.b - this.C >= 2 || this.cx == null) {
         this.C = this.b;
         this.G.clear();
         this.f();
         this.l.q = var1;
         this.l.P = var3;
         this.l.u = var5;
         this.l.g = 14.0;
         int var8 = this.cW == Level.END ? 1 : 0;
         this.l.X = var8 != 0 ? 185.0 : 262.0;
         if (!Double.isNaN(var5)) {
            this.l.X = Math.min(this.l.X, Math.max(this.k(), var5) + 48.0);
         }

         this.l.e = var8 != 0 ? 35.0 : Double.NEGATIVE_INFINITY;
         this.l.N = this.co;
         this.l.V = this.q();
         this.l.T = (boolean)(!var7 || this.b < this.ty || !this.a() && !(mc.player.getY() < this.l.y - 50.0) ? 0 : 1);
         int var9 = sy.a(mc.player);
         this.l.O = var9 < 12 ? 140.0 : (var9 < 32 ? 95.0 : 70.0);
         this.cx = this.J.B(this::D, this.X, this.U(), this.z(), this.l);
         this.r();
      }

      this.M(this.cx.yaw(), this.cx.pitch());
      if (this.cx.rocket()
         && this.z()
         && Math.abs(mc.player.getXRot() - this.cx.pitch()) < 20.0F
         && Math.abs(Mth.wrapDegrees(this.cx.yaw() - mc.player.getYRot())) < 30.0F) {
         this.q();
      }
   }

   private void r() {
      this.U.clear();
      vu.x var1 = new vu.x().i(this.X);
      int var2 = this.cx.rocket() ? vu.r(this.l.N) : this.U();

      for (int var3 = 0; var3 < 60; var3++) {
         var1.A = vu.p(var1.A, this.cx.yaw(), 12.0F);
         var1.G = vu.p(var1.G, this.cx.pitch(), 9.0F);
         vu.n(var1, this.l.V);
         vu.a(var1);
         if (var2-- > 0) {
            vu.M(var1);
         }

         if (var3 % 3 == 0) {
            this.U.add(new Vec3(var1.K, var1.J, var1.R));
         }
      }
   }

   private void f() {
      Vec3 var1 = mc.player.getDeltaMovement();
      this.X.r(mc.player.getX(), mc.player.getY(), mc.player.getZ(), var1.x, var1.y, var1.z, mc.player.getYRot(), mc.player.getXRot());
   }

   private double q() {
      return mc.player.hasEffect(MobEffects.SLOW_FALLING) && mc.player.getDeltaMovement().y <= 0.0 ? 0.01 : 0.08;
   }

   private boolean N() {
      if (this.b < this.cJ - 12 && this.b - this.c3 < 4) {
         return true;
      }

      if (this.b - this.c3 > 80) {
         return false;
      }

      LocalPlayer var1 = mc.player;
      return !mc.level
         .getEntitiesOfClass(
            FireworkRocketEntity.class, var1.getBoundingBox().inflate(3.0), var1x -> var1x.isAlive() && var1x.getOwner() == var1 && !var1x.isShotAtAngle()
         )
         .isEmpty();
   }

   private int U() {
      return this.N() ? Math.max(0, this.cJ - this.b) : 0;
   }

   private boolean z() {
      return this.O() && this.c5.T() && this.b - this.c3 >= 10 && !this.N() && !mc.player.isUsingItem() && this.allowsScreenInteractions();
   }

   private void q() {
      this.c5.h(3);
      sy.v var1 = sy.w(mc.player, 3);
      int var2 = var1.rocket() == null ? 3 : Math.max(1, var1.rocket().duration());
      this.c5.O(() -> this.isRuntimeActive() && this.c0 && this.O() && mc.player == this.cN, 10.0, 3, () -> {
         this.c3 = this.b;
         this.co = var2;
         this.cJ = this.b + vu.r(var2);
         this.cc++;
      });
   }

   private void M(float var1, float var2) {
      this.D = var1;
      this.cA = Mth.clamp(var2, -90.0F, 90.0F);
      this.cC = (boolean)1;
   }

   private void A() {
      if (this.cC && _p.Q(this)) {
         float var1 = Math.abs(Mth.wrapDegrees(this.D - mc.player.getYRot())) + Math.abs(this.cA - mc.player.getXRot());
         float[] var2 = this.cH.M(mc.player.getYRot(), mc.player.getXRot(), this.D, this.cA, var1 < 1.5F);
         b6.P(this, b6.w.x, var2[0], var2[1], 1.0);
      }
   }

   private boolean O() {
      return mc.player.isFallFlying() && !mc.player.isInWater();
   }

   private boolean K() {
      if (this.O()) {
         return true;
      }

      if (!mc.player.onGround()) {
         if (mc.player.isInWater()) {
            this.Z(_7.h.z);
            return false;
         }

         this.P("Opening the elytra");
         if (mc.player.getDeltaMovement().y < 0.0 && !mc.player.hasEffect(MobEffects.LEVITATION)) {
            this.a();
         }

         return false;
      } else {
         if (this.ck != _7.h.C && this.ck != _7.h.V) {
            this.Z(_7.h.z);
         } else {
            this.v();
         }

         return false;
      }
   }

   private boolean X() {
      if (this.ck == _7.h.i || this.O() || mc.player.onGround() || mc.player.hasEffect(MobEffects.LEVITATION)) {
         return false;
      }

      if (!this.x() || mc.player.onClimbable() || mc.player.isInWater()) {
         return false;
      }

      if (mc.player.getDeltaMovement().y > -0.5) {
         return false;
      }

      if (mc.player.fallDistance < 3.0 && !this.G(10)) {
         return false;
      }

      this.P("Falling: opening the elytra");
      this.a();
      this.M(mc.player.getYRot(), 8.0F);
      return true;
   }

   private boolean G(int var1) {
      BlockPos var2 = mc.player.blockPosition();

      for (int var3 = 1; var3 <= var1; var3++) {
         if (!mc.level.getBlockState(var2.below(var3)).getCollisionShape(mc.level, var2.below(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private boolean g() {
      int var1 = this.p();
      if (this.O() && var1 >= 0 && var1 <= 6 && this.b - this.cs >= 6) {
         this.cs = this.b;
         if (this.S() < 0) {
            return false;
         }

         _q.z(this.getName(), "Elytra at {} uses mid-air; swapping in the spare", var1);
         return this.P();
      } else {
         return false;
      }
   }

   private void O() {
      int var1 = mc.player.getFoodData().getFoodLevel();
      if (var1 > 10 && !mc.player.isUsingItem()) {
         this.j.s();
      } else if (!this.N() && !(mc.player.getY() < 70.0) && (this.cx == null || !this.cx.wide())) {
         if (this.j.T(4)) {
            this.P("Eating while cruising");
         }
      }
   }

   private boolean o() {
      if (this.ck == _7.h.V && this.S != _7.d.c) {
         return false;
      }

      int var1 = this.p();
      int var2 = sy.a(mc.player);
      double var3 = this.D();
      int var5 = this.X();
      int var6 = (var1 < 0 || var1 > E(var3) / (1 + var5) * 1.35 + 25.0) && var2 > var3 / 1000.0 * 2.6 + 5.0 ? 1 : 0;
      int var7 = var1 >= 0 && var1 <= (var5 >= 1 ? 40 : 60) ? 1 : 0;
      int var8 = var2 <= 3 ? 1 : 0;
      if (var6 != 0 && var7 == 0 && var8 == 0) {
         return false;
      }

      int var9 = var1 >= 0 && var1 <= this.e() ? 1 : 0;
      int var10 = !this.k() || this.I() <= 0 && !this.S(pc.c.T) ? 0 : 1;
      boolean var11 = this.S(pc.c.x);
      _7.d var12 = null;
      if (var9 == 0 || var10 == 0 && this.S() < 0) {
         if (var9 != 0 && var1 <= 40) {
            var12 = _7.d.d;
         }
      } else {
         var12 = _7.d.r;
      }

      if (var12 == null && var2 <= 20 && var11) {
         var12 = _7.d.r;
      }

      if (var12 == null && var2 <= 6) {
         var12 = _7.d.d;
      }

      if (var12 == null && var7 != 0) {
         var12 = var10 == 0 && this.S() < 0 ? _7.d.d : _7.d.r;
      }

      if (var12 == null && var8 != 0) {
         var12 = var11 ? _7.d.r : _7.d.d;
      }

      if (var12 == null) {
         return false;
      }

      if (var12 == _7.d.d && this.cP.isEmpty()) {
         this.cP = var2 <= 6 ? this.b() : "the elytra is worn out and cannot be mended";
      }

      this.S = var12;
      this.r = null;
      this.o = 0;
      this.p = -1000;
      this.b(
         ib.v.f, var12 == _7.d.r ? "Landing for a stop (" + (var9 != 0 ? "mend or swap the elytra" : "elytra low") + ")" : "Landing to stop safely: " + this.cP
      );
      this.Z(_7.h.V);
      return true;
   }

   static double E(double var0) {
      return var0 / 27.0 + 50.0;
   }

   private int e() {
      int var1 = this.X();
      int var2 = this.t();
      return Math.max(var1 >= 3 ? 50 : (var1 >= 1 ? 80 : 110), (int)(var2 * (var1 >= 3 ? 0.15 : (var1 >= 1 ? 0.2 : 0.27))));
   }

   private boolean k() {
      return this.cQ.getValue() && bt.e(mc.player.getItemBySlot(EquipmentSlot.CHEST)) && this.cO < 3;
   }

   private String b() {
      int var1 = sy.a(mc.player);
      return var1 == 0 ? "it ran out of rockets" : "only " + var1 + " rockets are left";
   }

   private boolean R() {
      if (this.b - this.c8 < 30 && mc.player.getHealth() < 14.0F) {
         return true;
      }

      AABB var1 = mc.player.getBoundingBox().inflate(9.0, 4.0, 9.0);

      for (Entity var3 : mc.level.getEntities(mc.player, var1, var0 -> var0.isAlive() && var0 instanceof Enemy)) {
         if (!(var3 instanceof NeutralMob var4 && !var4.isAngry()) && var3 instanceof LivingEntity var5 && mc.player.hasLineOfSight(var5)) {
            return true;
         }
      }

      return false;
   }

   private void Y(boolean var1) {
      this.p = this.b;
      double var2 = mc.player.getX();
      double var4 = mc.player.getZ();
      _e.d var7 = new _e.d() {
         @Override
         public int surface(int var1, int var2x) {
            return _7.this.D(var1, var2x);
         }

         @Override
         public _e.t ground(int var1, int var2x, int var3) {
            return _7.this.n(var1, var2x, var3);
         }
      };
      _e.k var6;
      if (this.S == _7.d.c) {
         if (this.D(Mth.floor(this.H), Mth.floor(this.t)) == Integer.MIN_VALUE) {
            return;
         }

         this.cZ++;
         var6 = _e.d(var7, this.H, this.t, var2, var4, 96, false, false);
         if (var6 == null) {
            var6 = _e.d(var7, this.H, this.t, var2, var4, 96, false, true);
         }

         if (var6 == null) {
            var6 = _e.d(var7, this.H, this.t, var2, var4, 160, false, true);
         }

         if (var6 == null) {
            var6 = _e.d(var7, this.H, this.t, var2, var4, 160, true, false);
         }

         if (var6 == null && this.cZ >= 12) {
            var6 = _e.d(var7, this.H, this.t, var2, var4, 300, true, true);
         }

         if (var6 == null && this.cZ >= 24) {
            var6 = _e.d(var7, var2, var4, var2, var4, 220, true, true);
         }

         if (var6 == null && this.cZ >= 40) {
            this.cP = "there is no safe place to land near the destination";
            this.S = _7.d.d;
            return;
         }
      } else {
         Vec3 var8 = Vec3.directionFromRotation(0.0F, mc.player.getYRot());
         double var9 = var1 ? 30.0 : 110.0;
         double var11 = var2 + var8.x * var9;
         double var13 = var4 + var8.z * var9;
         var6 = _e.d(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, false);
         if (var6 == null) {
            var6 = _e.d(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, true);
         }

         if (var6 == null && var1) {
            var6 = _e.d(var7, var2, var4, var2, var4, 200, true, true);
         }
      }

      for (int var15 = 0; var6 != null && var15 < 4 && this.u(var6); var15++) {
         double var16 = Math.toRadians(90 * var15 + 45);
         var6 = _e.d(var7, var6.x() + Math.cos(var16) * 60.0, var6.z() + Math.sin(var16) * 60.0, var2, var4, 90, this.S != _7.d.c && var1, true);
      }

      if (var6 != null && !this.u(var6)) {
         this.r = new Vec3(var6.x() + 0.5, var6.y(), var6.z() + 0.5);
         this.cS = var6.water();
         _q.z(
            this.getName(),
            "Landing spot {} {} {}{} ({} blocks from the destination, score {})",
            var6.x(),
            var6.y(),
            var6.z(),
            var6.water() ? " in water" : "",
            Y(Math.hypot(this.r.x - this.H, this.r.z - this.t)),
            String.format(Locale.ROOT, "%.1f", var6.score())
         );
      }
   }

   private boolean u(_e.k var1) {
      for (Vec3 var3 : this.cG) {
         if (Math.hypot(var3.x - var1.x() - 0.5, var3.z - var1.z() - 0.5) < 12.0) {
            return true;
         }
      }

      return false;
   }

   private void n() {
      if (this.r != null) {
         this.cG.add(this.r);
         _q.z(this.getName(), "Landing spot {} set aside", BlockPos.containing(this.r).toShortString());
      }

      this.r = null;
      this.e = (boolean)0;
      this.B = Double.MAX_VALUE;
   }

   private boolean W(int var1, int var2, int var3) {
      _e.t var4 = this.n(var1, var2, var3);
      return var4 == _e.t.X ? this.cS || this.S == _7.d.d : var4 != _e.t.t && var4 != _e.t.H;
   }

   int D(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.G.get(var3);
      if (var5 != null) {
         return var5;
      }

      LevelChunk var6 = mc.level.getChunkSource().getChunk(var1 >> 4, var2 >> 4, ChunkStatus.FULL, false);
      int var7;
      if (var6 == null) {
         var7 = Integer.MIN_VALUE;
      } else {
         int var8 = var6.getHeight(Types.MOTION_BLOCKING, var1 & 15, var2 & 15) + 1;
         var7 = var8 <= mc.level.getMinY() ? mc.level.getMinY() - 64 : var8;
      }

      this.G.put(var3, var7);
      return var7;
   }

   private double y(double var1, double var3) {
      int var5 = this.D(Mth.floor(var1), Mth.floor(var3));
      return var5 != Integer.MIN_VALUE && var5 > mc.level.getMinY() + 1 ? var5 : (this.cW == Level.END ? 60.0 : 68.0);
   }

   _e.t n(int var1, int var2, int var3) {
      if (var2 - 1 < mc.level.getMinY()) {
         return _e.t.P;
      }

      BlockPos var4 = new BlockPos(var1, var2 - 1, var3);
      BlockState var5 = mc.level.getBlockState(var4);
      FluidState var6 = var5.getFluidState();
      if (var6.is(FluidTags.LAVA)) {
         return _e.t.t;
      }

      if (var6.is(FluidTags.WATER)) {
         return _e.t.X;
      }

      if (var5.is(BlockTags.LEAVES)) {
         return _e.t.h;
      }

      if (var5.is(Blocks.MAGMA_BLOCK)
         || var5.is(BlockTags.CAMPFIRES)
         || var5.is(Blocks.CACTUS)
         || var5.is(Blocks.SWEET_BERRY_BUSH)
         || var5.is(BlockTags.FIRE)
         || var5.is(Blocks.POWDER_SNOW)
         || var5.is(Blocks.POINTED_DRIPSTONE)
         || var5.is(Blocks.WITHER_ROSE)
         || var5.is(Blocks.COBWEB)) {
         return _e.t.H;
      }

      if (!var5.is(Blocks.SLIME_BLOCK) && !var5.is(Blocks.HONEY_BLOCK)) {
         VoxelShape var7 = var5.getCollisionShape(mc.level, var4);
         if (var7.isEmpty()) {
            return _e.t.P;
         } else {
            AABB var8 = var7.bounds();
            if (!(var8.maxY > 1.01) && !(var8.maxY < 0.45) && !(var8.maxX - var8.minX < 0.9) && !(var8.maxZ - var8.minZ < 0.9)) {
               BlockState var9 = mc.level.getBlockState(var4.above());
               return var9.getFluidState().isEmpty() && var9.getCollisionShape(mc.level, var4.above()).isEmpty() ? _e.t.K : _e.t.P;
            } else {
               return _e.t.P;
            }
         }
      } else {
         return _e.t.P;
      }
   }

   private boolean a() {
      Vec3 var1 = mc.player.getDeltaMovement();
      double var2 = Math.hypot(var1.x, var1.z);
      Vec3 var4 = var2 > 0.2 ? new Vec3(var1.x / var2, 0.0, var1.z / var2) : Vec3.directionFromRotation(0.0F, mc.player.getYRot());
      int var5 = Mth.floor(mc.player.getX() + var4.x * 110.0);
      int var6 = Mth.floor(mc.player.getZ() + var4.z * 110.0);
      return mc.level.getChunkSource().getChunk(var5 >> 4, var6 >> 4, ChunkStatus.FULL, false) != null;
   }

   private int k() {
      if (this.b - this.cy >= 20 || this.cm == Integer.MIN_VALUE) {
         this.cy = this.b;
         int var1 = mc.player.getBlockX();
         int var2 = mc.player.getBlockZ();
         int var3 = Integer.MIN_VALUE;

         for (short var4 = -160; var4 <= 160; var4 += 16) {
            for (short var5 = -160; var5 <= 160; var5 += 16) {
               int var6 = this.D(var1 + var4, var2 + var5);
               if (var6 != Integer.MIN_VALUE) {
                  var3 = Math.max(var3, var6);
               }
            }
         }

         this.cm = var3 == Integer.MIN_VALUE ? (int)this.y(this.H, this.t) : var3;
      }

      return this.cm;
   }

   private boolean D() {
      int var1 = mc.player.getBlockX();
      int var2 = mc.player.getBlockZ();
      int var3 = Integer.MIN_VALUE;

      for (byte var4 = -24; var4 <= 24; var4 += 6) {
         for (byte var5 = -24; var5 <= 24; var5 += 6) {
            var3 = Math.max(var3, this.D(var1 + var4, var2 + var5));
         }
      }

      return var3 != Integer.MIN_VALUE && mc.player.getY() < var3 + 20 && this.x() < 200;
   }

   private boolean n(int var1) {
      AABB var2 = mc.player.getBoundingBox().deflate(0.02, 0.0, 0.02);
      int var3 = Mth.floor(mc.player.getY() + 0.6);

      for (int var4 = Mth.floor(var2.minX); var4 <= Mth.floor(var2.maxX); var4++) {
         for (int var5 = Mth.floor(var2.minZ); var5 <= Mth.floor(var2.maxZ); var5++) {
            for (int var6 = var3; var6 <= var3 + var1; var6++) {
               BlockPos var7 = new BlockPos(var4, var6, var5);
               if (!mc.level.getBlockState(var7).getCollisionShape(mc.level, var7).isEmpty()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private Float a() {
      Vec3 var1 = mc.player.getEyePosition();
      if (this.F(var1, new Vec3(0.0, 1.0, 0.0), 2.2)) {
         return null;
      }

      Vec3 var2 = mc.player.position();
      float var3 = mc.player.getYRot();

      for (float var7 : new float[]{0.0F, 45.0F, -45.0F, 90.0F, -90.0F, 135.0F, -135.0F, 180.0F}) {
         Vec3 var8 = Vec3.directionFromRotation(-32.0F, var3 + var7);
         Vec3 var9 = new Vec3(-var8.z, 0.0, var8.x).normalize().scale(0.35);
         byte var10 = 0;

         for (Vec3 var14 : new Vec3[]{
            var2.add(0.0, 1.2, 0.0), var2.add(0.0, 1.8, 0.0), var2.add(0.0, 1.2, 0.0).add(var9), var2.add(0.0, 1.2, 0.0).subtract(var9)
         }) {
            if (this.F(var14, var8, 22.0)) {
               var10 = 1;
               break;
            }
         }

         if (var10 == 0) {
            return var3 + var7;
         }
      }

      return null;
   }

   private boolean F(Vec3 var1, Vec3 var2, double var3) {
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var1.add(var2.normalize().scale(var3)), Block.COLLIDER, Fluid.NONE, mc.player));
      return var5.getType() == Type.BLOCK;
   }

   private boolean x() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) && LivingEntity.canGlideUsing(var1, EquipmentSlot.CHEST);
   }

   private int p() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) && var1.isDamageableItem() ? var1.getMaxDamage() - var1.getDamageValue() : -1;
   }

   private int t() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) ? var1.getMaxDamage() : 432;
   }

   private int X() {
      ItemStack var1 = mc.player.getItemBySlot(EquipmentSlot.CHEST);

      for (Entry var3 : var1.getEnchantments().entrySet()) {
         if (((Holder)var3.getKey()).is(Enchantments.UNBREAKING)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   private int S() {
      int var1 = Math.max(0, this.p());
      int var2 = -1;
      int var3 = var1;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = mc.player.getInventory().getItem(var4);
         if (var5.is(Items.ELYTRA) && LivingEntity.canGlideUsing(var5, EquipmentSlot.CHEST)) {
            int var6 = var5.getMaxDamage() - var5.getDamageValue();
            if (var6 >= 60 && var6 > var3 + 20) {
               var2 = var4;
               var3 = var6;
            }
         }
      }

      return var2;
   }

   private boolean P() {
      int var1 = this.S();
      if (var1 < 0) {
         return false;
      }

      _p.w(this, () -> {
         int var1x = this.S();
         if (var1x >= 0) {
            Inventory var2 = mc.player.getInventory();
            var2.setSelectedSlot(var1x);
            if (mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND).consumesAction()) {
               h8.i(mc.player, InteractionHand.MAIN_HAND);
               _q.z(this.getName(), "Put on the spare elytra from hotbar slot {}", var1x + 1);
               this.b(ib.v.f, "Put on the spare elytra");
            }
         }
      }, () -> this.isRuntimeActive() && this.c0 && mc.player == this.cN);
      return true;
   }

   private int I() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = mc.player.getInventory().getItem(var2);
         if (var3.is(Items.EXPERIENCE_BOTTLE)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   private boolean p() {
      for (int var1 = 9; var1 < 36; var1++) {
         if (sy.M(mc.player.getInventory().getItem(var1))) {
            return true;
         }
      }

      return false;
   }

   private void L() {
      if (this.ch < 0) {
         this.ch = this.b;
      }

      if (this.b - this.ch > 1200) {
         this.l(false, "it was stuck in water and could not reach the shore");
      } else {
         this.P("Swimming to shore");
         this.T = (boolean)1;
         if (this.b % 20 == 0 || this.L == null) {
            this.L = this.c(32);
         }

         if (this.L == null) {
            this.M(mc.player.getYRot(), 0.0F);
         } else {
            float[] var1 = b6.R(mc.player.getEyePosition(), this.L);
            this.M(var1[0], 0.0F);
            if (Math.abs(Mth.wrapDegrees(var1[0] - mc.player.getYRot())) < 30.0F) {
               this.x = (boolean)1;
            }
         }
      }
   }

   private Vec3 c(int var1) {
      int var2 = mc.player.getBlockX();
      int var3 = mc.player.getBlockZ();
      Vec3 var4 = null;
      double var5 = Double.MAX_VALUE;

      for (int var7 = -var1; var7 <= var1; var7 += 2) {
         for (int var8 = -var1; var8 <= var1; var8 += 2) {
            int var9 = this.D(var2 + var7, var3 + var8);
            if (var9 != Integer.MIN_VALUE && this.n(var2 + var7, var9, var3 + var8) == _e.t.K) {
               double var10 = var7 * var7 + var8 * var8;
               if (var10 < var5) {
                  var5 = var10;
                  var4 = new Vec3(var2 + var7 + 0.5, var9, var3 + var8 + 0.5);
               }
            }
         }
      }

      return var4;
   }

   public static Input z() {
      if (cM != null && cM.isEnabled()) {
         _7 var0 = cM;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null
               && var0.isRuntimeActive()
               && var0.c0
               && var1.player != null
               && var1.player == var0.cN
               && var1.player.tickCount == var0.cq
               && (var0.x || var0.T)
               && !ij.y()
               && !dt.x()
               && !(d1.E() instanceof AbstractContainerScreen)
            ? new Input(var0.x, false, false, false, var0.T, false, false)
            : null;
      } else {
         return null;
      }
   }

   private double D() {
      return Math.hypot(this.H - mc.player.getX(), this.t - mc.player.getZ());
   }

   private double L(double var1) {
      double var3 = this.y(this.H, this.t);
      double var5 = var3 + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095 + 30.0;
      return Math.max(this.cW == Level.END ? 150.0 : 140.0, Math.min(this.ct.getValue(), var5));
   }

   private double C() {
      double var1 = this.y(this.H, this.t);
      double var3 = Math.max(0.0, mc.player.getY() - var1 - 14.0);
      return 45.0 + var3 / 0.095 + 60.0;
   }

   private void S() {
      Vec3 var1 = mc.player.position();
      if (this.E != null) {
         double var2 = Math.hypot(var1.x - this.E.x, var1.z - this.E.z);
         if (this.w != null && var1.distanceTo(this.E.add(this.w)) > 4.0 && this.O()) {
            if (this.b - this.cg > 200) {
               this.cg = this.b;
               this.c6 = 0;
            }

            this.c6++;
            if (this.c6 >= 3 && this.b >= this.ty) {
               this.ty = this.b + 60;
               _q.z(this.getName(), "The server pulled the player back {} times in 10 seconds; holding rockets for 3 seconds", this.c6);
            }
         } else if (var2 < 10.0) {
            this.s += var2;
         }

         double var4 = (Math.hypot(this.H - this.E.x, this.t - this.E.z) - this.D()) * 20.0;
         this.cu = this.cu == 0.0 ? var4 : this.cu * 0.97 + var4 * 0.03;
      }

      this.E = var1;
      this.w = mc.player.getDeltaMovement();
      float var6 = mc.player.getHealth();
      if (var6 < this.c7) {
         this.c8 = this.b;
      }

      this.c7 = var6;
   }

   private String K() {
      double var1 = this.D();
      StringBuilder var3 = new StringBuilder(Y(var1)).append(" blocks left");
      if (this.cu > 2.0 && this.ck != _7.h.l) {
         var3.append(", about ").append(ac.Q((long)(var1 / this.cu * 1000.0)));
      }

      return var3.toString();
   }

   private void l(boolean var1, String var2) {
      if (!this.c0) {
         this.disable();
      } else {
         String var3 = ac.Q(System.currentTimeMillis() - this.cD);
         String var4 = BlockPos.containing(mc.player.position()).toShortString();
         String var5 = Y(this.s)
            + " blocks in "
            + var3
            + ", "
            + this.cc
            + " rockets"
            + (this.O > 0 ? ", " + this.O + " XP bottles" : "")
            + (
               this.v > 0L
                  ? ", bought "
                     + (this.cw > 0 ? this.cw + " rockets" : "")
                     + (this.cw > 0 && this.c4 > 0 ? " and " : "")
                     + (this.c4 > 0 ? this.c4 + " XP bottles" : "")
                     + " for "
                     + pc.b(this.v)
                  : ""
            );
         String var6 = var1
            ? "arrived at " + N(this.H, this.t).replace(" ", ", ") + ": " + var5 + "."
            : "stopped at " + var4 + ", " + var2 + ". " + n(var5) + ".";
         _q.z(this.getName(), var1 ? "Arrived: {}" : "Stopped: {}", var6);
         this.S(var6);
         this.b(var1 ? ib.v.x : ib.v.o, n(var6));
         this.P(var1 ? "Arrived" : "Stopped: " + var2);
         this.c_ = this.getStatus();
         int var7 = !this.g.getValue() || mc.getConnection() == null || mc.isLocalServer() || !var1 && !mc.player.onGround() && !mc.player.isInWater() ? 0 : 1;
         this.c0 = (boolean)0;
         this.disable();
         if (var7 != 0) {
            _q.z(this.getName(), "Logging off after the trip");
            mc.getConnection().getConnection().disconnect(Component.literal("Larp Auto Elytra Pilot: " + var6));
         } else if (this.g.getValue() && mc.isLocalServer()) {
            _q.z(this.getName(), "Log Off On Arrival skipped in singleplayer");
         }
      }
   }

   private void S(String var1) {
      if (mc != null && mc.player != null) {
         mc.player.sendSystemMessage(Component.literal("Larp Auto Elytra Pilot: " + var1));
      }
   }

   private void b(ib.v var1, String var2) {
      _q.z(this.getName(), "Notice: {}", var2);
      if (!var2.equals(this.t5) || this.b - this.tY >= 100) {
         this.t5 = var2;
         this.tY = this.b;
         switch ((String)this.A.getValue()) {
            case "Toast":
               ib.L().N("Auto Elytra Pilot", var2, var1, 5000L);
               break;
            case "Chat":
               this.S(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         }
      }
   }

   private static String n(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   private void M() {
      if (this.isInGame() && this.Y.getValue() && this.isRuntimeActive() && this.c0 && mc.level == this.V) {
         try {
            int var1 = com.larpclient.c.A(0.25);
            double var2 = this.y(this.H, this.t);
            Gizmos.line(new Vec3(this.H, var2, this.t), new Vec3(this.H, var2 + 256.0, this.t), vv.N(var1, 0.9), 3.0F).setAlwaysOnTop();
            Vec3 var4 = mc.player.position();
            if (!this.U.isEmpty() && this.ck != _7.h.C && mc.player.isFallFlying()) {
               for (Vec3 var6 : this.U) {
                  Gizmos.line(var4, var6, vv.N(var1, 0.8), 2.0F).setAlwaysOnTop();
                  var4 = var6;
               }
            }

            Gizmos.line(var4, new Vec3(this.H, Math.max(var2, var4.y - 40.0), this.t), vv.N(var1, 0.35), 1.5F).setAlwaysOnTop();
            if (this.r != null) {
               Gizmos.cuboid(
                     new AABB(this.r.x - 1.5, this.r.y, this.r.z - 1.5, this.r.x + 1.5, this.r.y + 0.1, this.r.z + 1.5),
                     GizmoStyle.strokeAndFill(-14249, 2.0F, 872400983)
                  )
                  .setAlwaysOnTop();
            }
         } catch (RuntimeException var7) {
            _q.g(this.getName(), "Waypoint line failed", var7);
         }
      }
   }

   @Override
   public ac.g hudCard() {
      if (this.isInGame() && this.u.getValue() && this.isRuntimeActive() && this.c0 && mc.level == this.V) {
         double var1 = this.f <= 0.0 ? -1.0 : Math.clamp(1.0 - this.D() / this.f, 0.0, 1.0);
         int var3 = this.p();
         String var4 = this.getStatus();
         return new ac.g(
            this.K(),
            var1 < 0.0 ? "" : Math.round(var1 * 100.0) + "%",
            var4 != null && !var4.isBlank() ? var4 : "Starting",
            sy.a(mc.player) + " rockets" + (var3 >= 0 ? ", elytra " + var3 + "/" + this.t() : ""),
            var1
         );
      } else {
         return null;
      }
   }

   public _7.h y() {
      return this.ck;
   }

   public boolean T() {
      return this.c0;
   }

   public double l() {
      return this.c0 && mc != null && mc.player != null ? this.D() : -1.0;
   }

   public int c() {
      return this.cc;
   }

   public Vec3 W() {
      return this.r;
   }

   enum d {
      c,
      r,
      d;
   }

   public enum h {
      z,
      i,
      o,
      Y,
      V,
      C,
      u,
      l,
      c;
   }

   enum k {
      M,
      w,
      C,
      K,
      l,
      t;
   }
}
