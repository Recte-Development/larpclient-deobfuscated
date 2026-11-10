package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.bc;
import com.larpclient.c5;
import com.larpclient.cf;
import com.larpclient.ck;
import com.larpclient.hh;
import com.larpclient.j4;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
import com.larpclient.uh;
import com.larpclient.wi;
import com.larpclient.zj;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class OrderDropperModule extends Module {
   public static final String s = "Drop Only";
   public static final String T = "Fill Shulkers";
   private static final int h1 = 20;
   private static final int h4 = 60;
   private static final int m = 60;
   private static final int o = 120;
   private static final int hh = 50;
   private static final int ho = 3;
   private static final int y = 3;
   private static final int h3 = -13180806;
   private static final int d = -11702;
   private static final int si = -2061766;
   private static final int F = -1876386;
   private static final int f = -854792;
   private static final int hv = -1017711;
   private static final double Q = 24.0;
   private static final double t = 10.0;
   private static final int hQ = 32;
   private static final long hX = 450000000L;
   private static final long h5 = 2000000000L;
   private static final long hN = 400000000L;
   private static final double V = 0.012;
   private static OrderDropperModule hT;
   private final l g = this.addSetting(
      new l(
         "Mode",
         "Drop Only drops every page where you stand. Fill Shulkers drops a page at your feet, picks it up, stores it in the selected shulkers and walks back, until the orders are empty",
         "Fill Shulkers",
         "Drop Only",
         "Fill Shulkers"
      )
   );
   private final g X = this.addSetting(
         new g("Order Item", "Item whose orders are emptied, such as totem_of_undying. Use the held-item button to fill it in", "")
      )
      .F("totem_of_undying");
   private final v hb = this.addSetting(
      new v(
         "Select Shulker Key",
         "Hold and sweep your crosshair over shulker boxes to select them; a sweep that starts on a selected box removes instead. Tap it for click selection: right-click adds, left-click removes, sneak-click covers an area",
         -1
      )
   );
   private final e h8 = this.addSetting(new e("Click Delay", "Ticks between menu clicks and item moves", 4.0, 2.0, 20.0, 1.0));
   private final s hI = this.addSetting(
      new s("Stop When Hurt", "Stop at once when you take damage. Menus block Auto Totem, so this protects you while it works", true)
   );
   private final s h2 = this.addSetting(new s("Show Messages", "Report orders, pages, trips and the final summary in chat", true));
   private final e hw = this.addSetting(
      new e("Fill Delay", "Ticks between shulker transfers. Lower is faster. Reopens the box to verify the result before leaving", 2.0, 1.0, 20.0, 1.0)
   );
   private final s sP = this.addSetting(
      new s("AFK Mode", "Release the mouse and keep running when you switch windows. Your pause-on-focus-loss preference is restored when stopped", false)
   );
   private final l ha = this.addSetting(
      new l(
         "Highlight Style",
         "Material worn by selected shulkers, shared with Self Visuals. Colors follow each box's state",
         "Camo",
         "Camo",
         "Glow",
         "Metallic",
         "Marble",
         "Glitter",
         "Solid",
         "Ghost"
      )
   );
   private final e c = this.addSetting(new e("Highlight Opacity", "How strongly the material covers selected shulkers", 0.85, 0.2, 1.0, 0.05));
   private final List<BlockPos> hn = new ArrayList<>();
   private final Set<BlockPos> G = new HashSet<>();
   private final Map<BlockPos, Integer> sC = new HashMap<>();
   private final Set<BlockPos> sp = new HashSet<>();
   private final Map<BlockPos, Object> hj = new HashMap<>();
   private Level hZ;
   private boolean C;
   private boolean M;
   private Boolean N;
   private BlockPos h0;
   private int Z;
   private long hu;
   private final Map<BlockPos, OrderDropperModule.f> P = new HashMap<>();
   private boolean h6;
   private boolean i;
   private boolean hs;
   private boolean p;
   private boolean hE;
   private boolean e;
   private BlockPos W;
   private BlockPos q;
   private BlockPos B;
   private long U;
   private final List<double[]> hy = new ArrayList<>();
   private final List<Integer> E = new ArrayList<>();
   private long n = Long.MIN_VALUE;
   private String sJ;
   private Item hD;
   private Object hd;
   private Object hM;
   private Object hO;
   private OrderDropperModule.z hi;
   private String u = "";
   private String s6 = "";
   private String K = "Idle";
   private Vec3 he;
   private float hz;
   private float hk;
   private float hl;
   private final Set<String> I = new HashSet<>();
   private final Set<Integer> sc = new HashSet<>();
   private boolean x;
   private boolean hY;
   private boolean h7;
   private boolean hC;
   private int b;
   private int hF;
   private int O;
   private int hx;
   private int l;
   private int r;
   private int D;
   private int hG;
   private int S;
   private OrderDropperModule.o hr;
   private int hR;
   private int k;
   private int sK;
   private AbstractContainerMenu hK;
   private AbstractContainerMenu Y;
   private int h_;
   private int j;
   private int hP;
   private int A;
   private BlockPos hW;
   private double hm;
   private int sz;
   private int L;
   private int a;
   private int hL;
   private boolean hc;
   private boolean hU;
   private boolean ht;
   private int v;
   private int h9;
   private int hf;
   private int sF;
   private boolean h;
   private boolean hS;
   private boolean H;
   private boolean hJ;
   private boolean hB;
   private Vec3 hq;
   private final Set<Vec3> J = new HashSet<>();
   private boolean sD;
   private boolean hV;
   private int hH;
   private int hg;
   private int w;
   private int R;
   private int z;
   private int hp;
   private static final Direction[][] hA = new Direction[6][];

   public OrderDropperModule() {
      super("Order Dropper", "Empty your DonutSMP orders for one item by dropping pages, or fill selected shulkers with them", Category.PLAYER, -1);
      hT = this;
      this.hw.P(this::h);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var1 -> this.f());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.o());
   }

   public List<q<?>> g() {
      return List.of(this.g, this.X);
   }

   public List<q<?>> t() {
      return List.of(this.hb, this.hw, this.ha, this.c);
   }

   public List<q<?>> U() {
      return List.of(this.h8, this.sP, this.hI, this.h2);
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.K : (this.h ? "Paused" : "Idle");
   }

   private boolean h() {
      return this.g.O("Fill Shulkers");
   }

   public String c() {
      String var1 = ck.g(this.X.getValue());
      String var2 = var1.isEmpty() ? "No order item chosen" : "Item: " + var1;
      int var3 = this.z().size();
      return var2
         + ". "
         + var3
         + (var3 == 1 ? " shulker" : " shulkers")
         + " selected"
         + (var3 == 0 ? "" : ", " + this.sC.size() + " scanned, " + this.G.size() + " full, " + this.sp.size() + " unavailable")
         + (this.h ? ". Progress saved for this world session." : ".");
   }

   public void w() {
      if (!this.isInGame()) {
         throw new IllegalArgumentException("Join a world first.");
      }

      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      mc.gui.setScreen(null);
      this.P();
   }

   public void Z() {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         this.X.k(z(mc.player.getMainHandItem()));
         LarpClient var1 = LarpClient.R();
         if (var1 != null && var1.o() != null) {
            var1.o().C();
         }
      } else {
         throw new IllegalArgumentException("Hold the order's item first.");
      }
   }

   public void h() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      this.t();
      this.hn.clear();
      this.q = null;
   }

   public void t() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before resetting progress.");
      }

      this.Y();
      this.V();
      this.sC.clear();
      this.G.clear();
      this.sp.clear();
      this.hj.clear();
      this.h = (boolean)0;
   }

   public void m() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before rescanning shulkers.");
      }

      this.sC.clear();
      this.G.clear();
      this.sp.clear();
      this.hj.clear();
   }

   private List<BlockPos> z() {
      Minecraft var1 = getMc();
      return var1 != null && var1.level == this.hZ ? this.hn : List.of();
   }

   @Override
   public void onEnable() {
      if (this.isInGame() && mc.player.connection != null) {
         if (this.h6) {
            this.M();
         }

         String var1 = ck.g(this.X.getValue());
         int var2 = this.h && this.hd == mc.player && this.hM == mc.level && this.hO == mc.player.connection && this.u.equals(var1) && this.h() ? 1 : 0;
         if (var2 == 0) {
            this.V();
            this.h = (boolean)0;
         }

         this.u = var1;
         if (this.u.isEmpty() || !BuiltInRegistries.ITEM.containsKey(Identifier.tryParse(this.u))) {
            this.s("Choose the Order Item first, for example totem_of_undying, or hold it and use the held-item button.");
         } else if (this.h() && !((Item)BuiltInRegistries.ITEM.getValue(Identifier.parse(this.u))).canFitInsideContainerItems()) {
            this.s("This item cannot go inside a shulker box. Choose Drop Only for shulker box orders.");
         } else if (this.h() && this.z().isEmpty()) {
            this.s("Select at least one placed shulker box first: look at it and press the Select Shulker Key.");
         } else {
            this.hd = mc.player;
            this.hM = mc.level;
            this.hO = mc.player.connection;
            if (var2 == 0) {
               this.he = mc.player.position();
               this.hz = mc.player.getYRot();
               this.hk = mc.player.getXRot();

               for (int var3 = 0; var3 < 36; var3++) {
                  if (this.R(mc.player.getInventory().getItem(var3))) {
                     this.sc.add(var3);
                  }
               }
            }

            this.hl = this.K();
            if (var2 != 0 && this.H) {
               this.x = (boolean)0;
               this.H = (boolean)0;
            }

            this.h = this.h();
            this.sp.clear();
            this.r();
            this.hS = (boolean)(this.h() && this.z().stream().anyMatch(var1x -> !this.sC.containsKey(var1x) && !this.sp.contains(var1x)) ? 1 : 0);
            if (this.hS) {
               this.A();
            } else if (var2 != 0 && this.z()) {
               this.A();
            } else {
               this.U(var2 != 0 ? OrderDropperModule.z.z : OrderDropperModule.z.i);
            }

            this.x(
               this.h()
                  ? (var2 != 0 ? "Resumed. " : "Started. ") + "Emptying " + this.u + " orders into " + this.z().size() + " shulkers."
                  : "Started. Dropping every page of your " + this.u + " orders here."
            );
            com.larpclient.h.A("OrderDropper", "Started mode={} item={} shulkers={} start={}", this.g.getValue(), this.u, this.z().size(), this.he);
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.hd != null
         && this.isInGame()
         && mc.player == this.hd
         && mc.player.containerMenu != mc.player.inventoryMenu
         && mc.player.containerMenu.getCarried().isEmpty()
         && this.K()) {
         mc.player.closeContainer();
      }

      if (this.hW != null && (this.hi == OrderDropperModule.z.e || this.sD || this.R > 0)) {
         this.sC.remove(this.hW);
      }

      this.Y();
      this.hi = null;
      this.hc = this.hU = this.ht = (boolean)0;
      this.hr = null;
      this.O = 0;
      this.h7 = this.hC = (boolean)0;
      this.b = this.l = this.r = this.D = this.hG = 0;
      this.hW = null;
      this.hq = null;
      this.J.clear();
      this.j();
   }

   private void V() {
      this.hd = this.hM = this.hO = null;
      this.hi = null;
      this.he = null;
      this.K = "Idle";
      this.I.clear();
      this.sc.clear();
      this.x = this.hY = this.h7 = this.hC = (boolean)0;
      this.b = this.hF = this.O = this.hx = this.l = this.r = this.D = this.hG = this.S = 0;
      this.hr = null;
      this.sK = 0;
      this.hW = null;
      this.s6 = "";
      this.sz = this.L = this.a = this.hL = 0;
      this.hc = this.hU = this.ht = (boolean)0;
      this.v = this.h9 = this.hf = this.sF = 0;
      this.hS = this.sD = this.hV = this.H = (boolean)0;
      this.hq = null;
      this.hH = this.hg = this.w = this.R = this.z = this.hp = 0;
      this.J.clear();
      this.j();
   }

   @Override
   public void onTick() {
      this.hc = this.hU = this.ht = (boolean)0;
      if (this.hi != null && this.isInGame() && mc.player.connection != null) {
         if (mc.player == this.hd && mc.level == this.hM && mc.player.connection == this.hO) {
            this.u();
            if (!mc.player.isAlive()) {
               this.E("You died.");
            } else {
               float var1 = this.K();
               if (this.hI.getValue()) {
                  float var13;
                  int var10000 = (var13 = var1 - (this.hl - 0.01F)) == 0.0F ? 0 : (var13 < 0.0F ? -1 : 1);
                  boolean var7 = false;
                  int var6 = var10000;
                  if ((Integer.signum(var6) >>> 31 ^ 1) == 0) {
                     this.E("You took damage.");
                     return;
                  }
               }

               this.hl = var1;
               if (this.R()) {
                  this.E("You moved.");
               } else if (this.he != null && mc.player.getY() < this.he.y - 2.5) {
                  this.E("You fell below the start spot.");
               } else if (this.hi != OrderDropperModule.z.i
                  && mc.gui.screen() instanceof ContainerScreen
                  && ck.s(mc.gui.screen().getTitle().getString()) != ck.n.A) {
                  this.K = "Closing Orders before continuing";
                  int var10 = ++this.b;
                  byte var3 = 100;
                  int var2 = var10;
                  if ((-Integer.compare(var2, var3) >>> 31 ^ 1) == 0) {
                     this.E("The Orders menu kept reopening.");
                  } else {
                     var10 = this.b % 4;
                     byte var5 = 1;
                     int var4 = var10;
                     var10 = Integer.compare(var4, var5);
                     if ((var10 | -var10) >>> 31 == 0) {
                        this.T();
                     }
                  }
               } else {
                  byte var9 = 1;
                  int var8 = this.hx;
                  this.hx = var8 + var9;
                  switch (this.hi) {
                     case i:
                        this.y();
                        break;
                     case c:
                        this.L();
                        break;
                     case k:
                        this.g();
                        break;
                     case x:
                        this.Q();
                        break;
                     case e:
                        this.x();
                        break;
                     case z:
                        this.D();
                        break;
                     case r:
                        this.l();
                  }
               }
            }
         } else {
            this.E("The world changed.");
         }
      }
   }

   private void y() {
      if (this.O > 0) {
         this.O--;
      } else {
         Screen var1 = mc.gui.screen();
         if (this.hC) {
            if (var1 != null) {
               this.l = 0;
               if (++this.b > 100) {
                  this.E("The Orders menu kept reopening.");
                  return;
               }

               if (this.b % 4 == 1 && this.K()) {
                  this.T();
               }

               return;
            }

            if (++this.l < 4) {
               return;
            }

            this.hC = (boolean)0;
         }

         if (var1 == null) {
            if (this.hr != null) {
               this.hr = null;
            }

            if (this.h()) {
               if (this.hY) {
                  this.U(OrderDropperModule.z.c);
                  return;
               }

               if (this.Z() == 0) {
                  if (this.z()) {
                     this.A();
                  } else {
                     this.E("Your inventory is full of other items. Make room and enable again.");
                  }

                  return;
               }

               if (this.v() > 0) {
                  this.U(OrderDropperModule.z.c);
                  return;
               }

               if (this.x) {
                  this.B();
                  return;
               }

               if (mc.player.getXRot() < 88.0F) {
                  this.K = "Looking down";
                  j6.U(this, j6.l.s, mc.player.getYRot(), 90.0F, 0.5);
                  return;
               }
            } else if (this.x) {
               this.U();
               return;
            }

            if (++this.l >= 5) {
               if (this.r <= 0 || ++this.r >= 60) {
                  if (++this.D > 3) {
                     this.E("The /orders menu did not open after 3 tries.");
                  } else {
                     this.K = "Opening /orders";
                     mc.player.connection.sendCommand("orders");
                     this.r = 1;
                     com.larpclient.h.U("OrderDropper", "Sent /orders (attempt {})", this.D);
                  }
               }
            }
         } else {
            this.l = 0;
            if (!(var1 instanceof AbstractContainerScreen)) {
               this.K = "Waiting for the open screen to close";
            } else {
               ChestMenu var2 = this.i();
               if (var2 == null) {
                  this.E("A different menu opened: " + var1.getTitle().getString() + ".");
               } else {
                  this.D = this.r = 0;
                  if (this.hr != null) {
                     int var5 = var2.containerId == this.hR && w(var2) == this.k ? 0 : 1;
                     if (var5 != 0) {
                        this.hr = null;
                        this.O = this.k();
                        this.hG = 0;
                     } else if (++this.sK > (this.hr == OrderDropperModule.o.i ? 20 : 60)) {
                        if (this.hr == OrderDropperModule.o.i && ++this.hG <= 3) {
                           this.hr = null;
                        } else {
                           this.E("The server did not respond to a menu click.");
                        }
                     }
                  } else {
                     ItemStack var3 = var2.getCarried();
                     if (var3.isEmpty() || this.R(var3) && ck.s(var1.getTitle().getString()) == ck.n.z) {
                        this.hF = 0;
                        ck.n var4 = ck.s(var1.getTitle().getString());
                        if (this.d(var2, var4)) {
                           switch (var4) {
                              case v:
                                 this.K = "Opening Your Orders";
                                 this.b(var2, ck.p.j, OrderDropperModule.o.i);
                                 break;
                              case u:
                                 this.i(var2);
                                 break;
                              case e:
                                 this.K = "Opening Collect";
                                 this.b(var2, ck.p.L, OrderDropperModule.o.i);
                                 break;
                              case z:
                                 this.N(var2);
                                 break;
                              case A:
                                 this.E("An unexpected Orders menu opened: " + var1.getTitle().getString() + ".");
                           }
                        }
                     } else {
                        if (++this.hF > 60) {
                           this.E("The cursor still holds " + var3.getHoverName().getString() + ".");
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void Q(int var0) {
      OrderDropperModule var1 = hT;
      Minecraft var2 = getMc();
      if (var1 != null && var1.hi == OrderDropperModule.z.i && var2 != null && var2.player != null) {
         AbstractContainerMenu var3 = var2.player.containerMenu;
         if (var3 instanceof ChestMenu && var3.containerId == var0) {
            var1.hK = var3;
            var1.j = 0;
         }
      }
   }

   private void j() {
      this.hK = this.Y = null;
      this.h_ = this.j = this.hP = this.A = 0;
   }

   private boolean d(ChestMenu var1, ck.n var2) {
      int var3 = w(var1);
      if (this.Y == var1 && this.h_ == var3) {
         this.j++;
      } else {
         this.Y = var1;
         this.h_ = var3;
         this.j = 0;
      }

      String var4 = null;
      if (this.hK != var1) {
         var4 = "server contents";
      } else if (this.j < 2) {
         var4 = "settled contents";
      } else if (var2 == ck.n.v && this.D(var1, ck.p.j) == -1) {
         var4 = "YOUR_ORDERS button";
      } else if (var2 == ck.n.e && this.D(var1, ck.p.L) == -1) {
         var4 = "COLLECT button";
      } else if (var2 == ck.n.u) {
         byte var5 = 0;

         for (int var6 = 0; var6 < Math.min(45, var1.getRowCount() * 9); var6++) {
            if (!var1.getSlot(var6).getItem().isEmpty()) {
               var5 = 1;
               break;
            }
         }

         if (var5 == 0) {
            var4 = "your order entries or empty-order placeholders";
         }
      }

      if (var4 == null) {
         this.hP = 0;
         return true;
      } else {
         this.K = "Waiting for Orders: " + var4;
         if (++this.hP <= 60) {
            return false;
         } else if (++this.A > 3) {
            this.E("The Orders menu did not load " + var4 + " after 3 reloads. Progress retained.");
            return false;
         } else {
            com.larpclient.h.g(
               "OrderDropper",
               "Reloading Orders: missing={} title={} id={} synced={} retry={}/{}",
               var4,
               mc.gui.screen().getTitle().getString(),
               var1.containerId,
               this.hK == var1,
               this.A,
               3
            );
            this.hP = 0;
            this.O();
            return false;
         }
      }
   }

   private void i(ChestMenu var1) {
      for (int var2 = 0; var2 < 45 && var2 < var1.getRowCount() * 9; var2++) {
         ItemStack var3 = var1.getSlot(var2).getItem();
         if (!var3.isEmpty() && ck.E(z(var3), this.u, y(var3))) {
            String var4 = y(var3);
            if (!this.I.contains(var4)) {
               this.s6 = var4;
               this.S = 0;
               this.K = "Opening your " + var3.getHoverName().getString() + " order";
               com.larpclient.h.U("OrderDropper", "Opening order in slot {}: {}", var2, var4);
               this.M(var1, var2, ContainerInput.PICKUP, OrderDropperModule.o.i);
               return;
            }
         }
      }

      this.x = (boolean)1;
      this.x(this.I.isEmpty() ? "You have no " + this.u + " orders with items to collect." : "All " + this.u + " orders are empty.");
      com.larpclient.h.A("OrderDropper", "Orders finished; emptied={} pages={} taken={}", this.I.size(), this.v, this.h9);
      this.O();
   }

   private void N(ChestMenu var1) {
      int var2 = 0;
      int var3 = -1;

      for (int var4 = 0; var4 < 45; var4++) {
         if (!var1.getSlot(var4).getItem().isEmpty()) {
            var2++;
            if (var3 < 0) {
               var3 = var4;
            }
         }
      }

      int var6 = this.D(var1, ck.p.l);
      int var5 = this.D(var1, ck.p.W);
      if (!var1.getCarried().isEmpty()) {
         this.M(var1);
      } else if (var2 != 0) {
         this.S = 0;
         if (var6 >= 0) {
            if (this.h() && this.hY) {
               this.O();
            } else if (this.h() && var2 + this.J() > this.a()) {
               this.x("The selected shulkers have room for " + this.a() + " stacks; this page has " + var2 + ". Select more shulkers to continue.");
               com.larpclient.h.A("OrderDropper", "Not dropping: page={} carrying={} room={}", var2, this.J(), this.a());
               this.x = (boolean)1;
               this.H = (boolean)1;
               this.O();
            } else {
               this.K = "Dropping page " + (this.v + 1);
               this.b(var1, ck.p.l, OrderDropperModule.o.T);
               this.v++;
               this.hY = (boolean)1;
               com.larpclient.h.U("OrderDropper", "Dropped page {} ({} stacks)", this.v, var2);
            }
         } else if (this.Z() != 0) {
            this.K = "Taking items";
            this.M(var1, var3, ContainerInput.PICKUP, OrderDropperModule.o.T);
         } else if (this.h() && this.z()) {
            this.O();
         } else {
            this.E("This order has no Drop button and your inventory is full. Make room and enable again.");
         }
      } else if (var5 >= 0 && ++this.S <= 3) {
         this.K = "Next page";
         this.b(var1, ck.p.W, OrderDropperModule.o.T);
      } else {
         this.I.add(this.s6);
         this.x("Order emptied.");
         com.larpclient.h.A("OrderDropper", "Order emptied after {} pages: {}", this.v, this.s6);
         this.d();
      }
   }

   private void M(ChestMenu var1) {
      for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
         Slot var3 = var1.getSlot(var2);
         if (var3.container == mc.player.getInventory() && var3.getContainerSlot() < 36 && var3.getItem().isEmpty()) {
            this.M(var1, var2, ContainerInput.PICKUP, OrderDropperModule.o.T);
            this.h9++;
            return;
         }
      }

      this.E("No empty inventory slot for the item on the cursor.");
   }

   private int D(ChestMenu var1, ck.p var2) {
      int var3 = -1;

      for (int var4 = var1.getRowCount() == 6 ? 45 : 0; var4 < var1.getRowCount() * 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && ck.o(z(var5), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -2;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void b(ChestMenu var1, ck.p var2, OrderDropperModule.o var3) {
      int var4 = this.D(var1, var2);
      if (var4 == -2) {
         this.E("Two " + var2 + " buttons found; this menu layout needs review.");
      } else if (var4 < 0) {
         this.E("No " + var2 + " button in this menu.");
      } else {
         this.M(var1, var4, ContainerInput.PICKUP, var3);
      }
   }

   private void M(AbstractContainerMenu var1, int var2, ContainerInput var3, OrderDropperModule.o var4) {
      ItemStack var5 = var1.getSlot(var2).getItem();
      if (var2 < 54 && var1 instanceof ChestMenu && ck.o(z(var5), var5.getHoverName().getString()) == ck.p.S) {
         this.E("Refused to click a Sell button.");
      } else {
         this.hr = var4;
         this.hR = var1.containerId;
         this.sK = 0;
         this.hP = this.A = 0;
         mc.gameMode.handleContainerInput(var1.containerId, var2, 0, var3, mc.player);
         this.k = w(var1);
      }
   }

   private void d() {
      this.s6 = "";
      this.S = 0;
      this.O();
   }

   private void O() {
      this.hC = (boolean)1;
      this.b = 0;
      this.hr = null;
      this.l = 0;
      this.r = 0;
      this.D = 0;
      this.T();
   }

   private void T() {
      if (mc.player.containerMenu != mc.player.inventoryMenu && mc.player.containerMenu.getCarried().isEmpty()) {
         mc.player.closeContainer();
      }
   }

   private ChestMenu i() {
      if (mc.gui.screen() instanceof ContainerScreen var1
         && mc.player.containerMenu instanceof ChestMenu var2
         && var1.getMenu() == var2
         && (var2.getRowCount() == 6 || var2.getRowCount() == 3)) {
         ck.n var5 = ck.s(var1.getTitle().getString());
         return var5 == ck.n.A && !ck.d(var1.getTitle().getString()).startsWith("orders") ? null : var2;
      } else {
         return null;
      }
   }

   private boolean K() {
      return mc.gui.screen() instanceof ShulkerBoxScreen ? this.hW != null : mc.gui.screen() != null && ck.s(mc.gui.screen().getTitle().getString()) != ck.n.A;
   }

   private void L() {
      this.K = "Picking up items";
      int var1 = this.v();
      if (this.Z() <= 0 || this.hx >= 120 || var1 <= 0 && this.hx >= 10) {
         if (var1 > 0 && this.Z() > 0) {
            com.larpclient.h.g("OrderDropper", "{} item stacks stayed out of pickup reach", var1);
         }

         if (this.Z() != 0 && (!this.x || !this.z())) {
            if (var1 > 0 && !this.z()) {
               this.E("The dropped items landed out of pickup reach.");
            } else if (this.x) {
               this.B();
            } else {
               this.J();
            }
         } else {
            this.A();
         }
      }
   }

   private void J() {
      this.hY = (boolean)0;
      this.U(OrderDropperModule.z.i);
   }

   private void B() {
      if (this.z()) {
         this.A();
      } else {
         this.U();
      }
   }

   private void A() {
      this.hW = null;
      this.r();
      Vec3 var1 = mc.player.position();
      double var2 = Double.MAX_VALUE;

      for (BlockPos var5 : this.z()) {
         if (!this.sp.contains(var5) && (this.hS ? !this.sC.containsKey(var5) : !this.G.contains(var5))) {
            double var6 = Vec3.atCenterOf(var5).distanceToSqr(var1);
            if (var6 < var2) {
               var2 = var6;
               this.hW = var5;
            }
         }
      }

      if (this.hW == null) {
         if (this.hS) {
            this.hS = (boolean)0;
            this.U(OrderDropperModule.z.z);
         } else {
            this.H = (boolean)1;
            if (this.f(this.he) > 0.4) {
               this.U(OrderDropperModule.z.z);
               this.x("No usable shulkers remain; walking back.");
               this.x = (boolean)1;
            } else {
               this.E("No usable shulkers remain. Select more or rescan, then enable again.");
            }
         }
      } else {
         if (!this.hS) {
            this.sF++;
         }

         this.sD = this.hV = (boolean)0;
         this.w = this.R = this.z = this.hp = 0;
         this.hq = null;
         this.J.clear();
         this.hm = Double.MAX_VALUE;
         this.sz = 0;
         this.U(OrderDropperModule.z.k);
         com.larpclient.h.U("OrderDropper", "Trip {} to shulker {} carrying {} stacks", this.sF, this.hW.toShortString(), this.J());
      }
   }

   private void g() {
      if (!this.q(this.hW)) {
         this.m("is missing or unloaded");
      } else if (mc.gui.screen() != null) {
         this.K = "Close the screen to continue";
      } else if (!this.G()) {
         this.K = this.hS ? "Walking to scan shulker" : "Walking to shulker";
         if (this.r() && this.U(mc.player.getEyePosition()) != null) {
            this.L = this.hL = 0;
            this.h7 = (boolean)0;
            this.U(OrderDropperModule.z.x);
         } else {
            if (this.hq != null && this.f(this.hq) < 0.15 && Math.abs(mc.player.getY() - this.hq.y) < 0.35) {
               this.J.add(this.hq);
               com.larpclient.h.U(
                  "OrderDropper", "Replanning shulker {} from eye={} after arriving at {}", this.hW.toShortString(), mc.player.getEyePosition(), this.hq
               );
               this.hq = null;
            }

            if (this.hq == null) {
               this.hq = this.O();
               this.hm = Double.MAX_VALUE;
               this.sz = 0;
            }

            if (this.hq == null) {
               com.larpclient.h.g(
                  "OrderDropper",
                  "No visible standing spot for {}: eye={} reach={} tried={}",
                  this.hW.toShortString(),
                  mc.player.getEyePosition(),
                  this.m(),
                  this.J.size()
               );
               this.m("has no clear reachable standing spot");
            } else {
               this.o(this.hq, 0.1);
            }
         }
      }
   }

   private void Q() {
      if (!this.q(this.hW)) {
         this.m("is missing or unloaded");
      } else if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.h7 = (boolean)0;
         this.O = this.k();
         this.U(OrderDropperModule.z.e);
      } else if (!this.r() && !this.h7) {
         this.hq = null;
         this.hm = Double.MAX_VALUE;
         this.sz = 0;
         this.U(OrderDropperModule.z.k);
      } else {
         this.K = "Opening shulker";
         if (this.h7) {
            if (++this.a <= 60) {
               return;
            }

            this.h7 = (boolean)0;
            if (++this.hL >= 3) {
               this.m("did not open");
               return;
            }
         }

         if (mc.gui.screen() == null) {
            if (!this.G()) {
               BlockHitResult var6 = this.U(mc.player.getEyePosition());
               if (var6 == null) {
                  this.hq = null;
                  this.U(OrderDropperModule.z.k);
               } else {
                  Vec3 var7 = i(this.hW, var6);
                  float[] var9 = j6.s(mc.player.getEyePosition(), var7);
                  j6.U(this, j6.l.s, var9[0], var9[1], 0.5);
                  BlockHitResult var4 = ta.D(this.hW, this.m());
                  if (var4 == null) {
                     this.L = 0;
                     if (this.hx > 60) {
                        this.m("could not be aimed at");
                     }
                  } else if (++this.L >= 2 && !mc.player.isShiftKeyDown()) {
                     boolean var5 = ta.k(this, () -> {
                        if (mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, var4).consumesAction()) {
                           ks.p(mc.player, InteractionHand.MAIN_HAND);
                        }
                     }, () -> mc.gui.screen() == null);
                     if (var5) {
                        this.h7 = (boolean)1;
                        this.a = 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void x() {
      if (mc.gui.screen() instanceof ShulkerBoxScreen var1 && mc.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.K = this.hS ? "Scanning shulker" : "Filling shulker";
         if (!var2.getCarried().isEmpty()) {
            this.E("The cursor holds an item inside the shulker.");
         } else if (this.O <= 0 || --this.O <= 0) {
            if (this.hS) {
               this.P(var2);
               mc.player.closeContainer();
               this.A();
            } else {
               if (this.sD) {
                  int var7 = this.M(var2);
                  int var4 = this.K(var2);
                  int var5 = Math.max(0, var7 - this.hH);
                  this.hf = this.hf + Math.max(0, var4 - this.hg);
                  com.larpclient.h.A(
                     "OrderDropper",
                     "Verified shulker {}: items added={} ({} -> {}), carrying={} stacks",
                     this.hW.toShortString(),
                     var5,
                     this.hH,
                     var7,
                     this.J()
                  );
                  this.hH = var7;
                  this.hg = var4;
                  this.P(var2);
                  this.sD = (boolean)0;
                  this.R = 0;
                  if (var5 > 0) {
                     this.z = 0;
                  } else if (++this.z > 3 && this.z()) {
                     this.E("Shulker transfers kept being rejected. Progress retained; check the server and enable again.");
                     return;
                  }
               }

               if (!this.hV) {
                  this.hH = this.M(var2);
                  this.hg = this.K(var2);
                  this.hV = (boolean)1;
               }

               if (this.R >= 36) {
                  this.v();
               } else {
                  for (int var8 = 27; var8 < var2.slots.size(); var8++) {
                     Slot var10 = var2.getSlot(var8);
                     ItemStack var11 = var10.getItem();
                     if (this.R(var11) && (var10.container != mc.player.getInventory() || !this.sc.contains(var10.getContainerSlot())) && this.T(var2, var11)) {
                        mc.gameMode.handleContainerInput(var2.containerId, var8, 0, ContainerInput.QUICK_MOVE, mc.player);
                        this.R++;
                        this.w = 0;
                        this.O = this.hw.getValue().intValue();
                        return;
                     }
                  }

                  if (!this.z() && ++this.w < Math.max(10, this.hw.getValue().intValue())) {
                     this.K = "Waiting for remaining items";
                  } else if (this.R > 0) {
                     this.v();
                  } else {
                     this.P(var2);
                     boolean var9 = this.z();
                     if (var9) {
                        this.G.add(this.hW);
                        this.x("Shulker full (" + this.G.size() + "/" + this.z().size() + ").");
                     }

                     mc.player.closeContainer();
                     com.larpclient.h.U("OrderDropper", "Closed shulker {}; full={} carrying={}", this.hW.toShortString(), var9, this.J());
                     if (var9) {
                        this.A();
                     } else {
                        this.U(OrderDropperModule.z.z);
                     }
                  }
               }
            }
         }
      } else {
         this.sC.remove(this.hW);
         if (++this.hp > 3) {
            this.m("kept closing before filling finished");
         } else {
            this.hq = null;
            this.hm = Double.MAX_VALUE;
            this.sz = 0;
            this.U(OrderDropperModule.z.k);
         }
      }
   }

   private void v() {
      this.sD = (boolean)1;
      this.h7 = (boolean)0;
      this.L = this.hL = 0;
      mc.player.closeContainer();
      this.U(OrderDropperModule.z.x);
      com.larpclient.h.U("OrderDropper", "Reopening {} to verify transfers", this.hW.toShortString());
   }

   private int M(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.R(var1.getSlot(var3).getItem())) {
            var2 += var1.getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   private int K(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.R(var1.getSlot(var3).getItem())) {
            var2++;
         }
      }

      return var2;
   }

   private boolean T(ShulkerBoxMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < 27; var3++) {
         Slot var4 = var1.getSlot(var3);
         ItemStack var5 = var4.getItem();
         if (var5.isEmpty() ? var4.mayPlace(var2) : ItemStack.isSameItemSameComponents(var5, var2) && var5.getCount() < var4.getMaxStackSize(var5)) {
            return true;
         }
      }

      return false;
   }

   private void m(String var1) {
      this.sp.add(this.hW);
      this.sC.remove(this.hW);
      this.G.remove(this.hW);
      this.x("Skipped a shulker that " + var1 + ".");
      com.larpclient.h.g("OrderDropper", "Shulker {} {}; skipped", this.hW.toShortString(), var1);
      this.A();
   }

   private void D() {
      this.K = "Walking back";
      if (this.f(this.he) > 0.3) {
         this.o(this.he, 0.3);
      } else if (this.h() && this.v() > 0 && this.Z() > 0) {
         this.U(OrderDropperModule.z.c);
      } else if (this.z() && this.z().stream().anyMatch(var1 -> !this.G.contains(var1) && !this.sp.contains(var1))) {
         this.A();
      } else if (!this.x) {
         this.J();
      } else {
         this.U();
      }
   }

   private void U() {
      if (this.h() && this.f(this.he) > 0.3) {
         this.U(OrderDropperModule.z.z);
      } else if (this.h() && (this.z() || this.v() > 0)) {
         this.E("Storage unavailable with items still to store. Select more shulkers or rescan, then enable again.");
      } else {
         this.U(OrderDropperModule.z.r);
      }
   }

   private void l() {
      this.K = "Finishing";
      if (!this.h() || this.hx >= 20 || !(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - this.hz)) > 1.0F) && !(Math.abs(mc.player.getXRot() - this.hk) > 1.0F)
         )
       {
         this.N(
            "Done: " + this.v + " pages dropped, " + this.h9 + " stacks taken" + (this.h() ? ", " + this.hf + " stacks stored in " + this.sF + " trips." : ".")
         );
         com.larpclient.h.A("OrderDropper", "Finished pages={} taken={} stored={} trips={}", this.v, this.h9, this.hf, this.sF);
         this.h = (boolean)0;
         this.disable();
      } else {
         j6.U(this, j6.l.s, this.hz, this.hk, 0.5);
      }
   }

   private void o(Vec3 var1, double var2) {
      if (!this.G()) {
         double var4 = this.f(var1);
         if (var4 < this.hm - 0.05) {
            this.hm = var4;
            this.sz = 0;
         } else if (++this.sz > 50) {
            this.E("The way is blocked; stopped where it was.");
            return;
         }

         Vec3 var6 = var1.subtract(mc.player.position());
         float var7 = (float)Math.toDegrees(Math.atan2(var6.z, var6.x)) - 90.0F;
         j6.U(this, j6.l.s, var7, 25.0F, 0.35);
         this.hc = (boolean)(Math.abs(Mth.wrapDegrees(mc.player.getYRot() - var7)) < 35.0F ? 1 : 0);
         this.hU = (boolean)(this.hc && mc.player.horizontalCollision && mc.player.onGround() ? 1 : 0);
         this.ht = (boolean)(var2 > 0.0 && var4 < 1.0 ? 1 : 0);
      }
   }

   public static Input z() {
      if (hT != null && hT.isEnabled()) {
         OrderDropperModule var0 = hT;
         if (var0 == null || !var0.isRuntimeActive() || getMc().gui.screen() != null || p_.V() || j6.j() && !j6.g(j6.l.s)) {
            return null;
         } else if (!var0.hc) {
            return d() ? new Input(false, false, false, false, false, false, false) : null;
         } else {
            return new Input(true, false, false, false, var0.hU, var0.ht, false);
         }
      } else {
         return null;
      }
   }

   private boolean R() {
      if (mc.gui.screen() == null && mc.isWindowActive()) {
         Options var1 = mc.options;
         return c5.J(var1.keyUp) || c5.J(var1.keyDown) || c5.J(var1.keyLeft) || c5.J(var1.keyRight) || c5.J(var1.keyJump);
      } else {
         return false;
      }
   }

   private void f() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.getWindow() != null) {
         if (this.hZ != null && var1.level != this.hZ && !this.isRuntimeActive()) {
            this.h6 = (boolean)0;
            this.W = null;
            this.t();
            this.hn.clear();
            this.P.clear();
            this.q = null;
            this.hZ = null;
         }

         if (this.h6) {
            if (!this.isInGame() || var1.level != this.hZ) {
               this.h6 = (boolean)0;
               this.W = null;
            } else if (!this.isRuntimeActive() && var1.gui.screen() == null) {
               if (System.nanoTime() - this.U > 2000000000L) {
                  this.N();
               }
            } else {
               this.M();
            }
         }
      }
   }

   private void W() {
      if (this.hZ != mc.level) {
         this.t();
         this.hn.clear();
         this.P.clear();
         this.q = null;
         this.hZ = mc.level;
      }
   }

   private void P() {
      this.W();
      this.h6 = (boolean)1;
      this.q = this.B = this.W = null;
      this.i = this.hs = this.p = this.hE = this.e = (boolean)0;
      if (mc.gameMode != null) {
         mc.gameMode.stopDestroyBlock();
      }

      this.N();
      com.larpclient.h.U("OrderDropper", "Selection started with {} shulkers", this.hn.size());
   }

   private void M() {
      this.h6 = (boolean)0;
      this.W = this.B = null;
      this.p = this.hE = this.e = (boolean)0;
      this.r("Shulker selection saved. " + this.hn.size() + " selected.");
      com.larpclient.h.A("OrderDropper", "Selection finished with {} shulkers", this.hn.size());
   }

   private void N() {
      int var1 = this.hb.getValue();
      this.r(
         this.hn.size()
            + " selected. Right-click adds, left-click removes, hold to sweep, sneak-click for an area. "
            + (bc.a(var1) ? bc.G(var1) + " or Esc" : "Esc")
            + " to finish."
      );
   }

   public static boolean C() {
      OrderDropperModule var0 = hT;
      return var0 != null && var0.h6;
   }

   private void I() {
      if (!this.isInGame()) {
         this.W = null;
         this.C = (boolean)0;
      } else {
         this.s();
         int var1 = this.C && !this.M ? 1 : 0;
         if ((this.h6 || var1 != 0) && mc.gui.screen() == null) {
            HitResult var2 = mc.player.pick(24.0, mc.getDeltaTracker().getGameTimeDeltaPartialTick(false), false);
            this.W = var2 instanceof BlockHitResult var3 && var2.getType() == Type.BLOCK && this.q(var3.getBlockPos()) ? var3.getBlockPos().immutable() : null;
            if (var1 != 0) {
               this.n(this.W);
            } else {
               boolean var7 = c5.J(mc.options.keyUse);
               boolean var4 = c5.J(mc.options.keyAttack);
               if (!var7) {
                  this.i = (boolean)1;
               }

               if (!var4) {
                  this.hs = (boolean)1;
               }

               int var5 = var7 && this.i ? 1 : 0;
               int var6 = var4 && this.hs && var5 == 0 ? 1 : 0;
               if ((var5 == 0 || this.p) && (var6 == 0 || this.hE)) {
                  if ((var5 != 0 || var6 != 0) && !this.e && this.W != null && !this.W.equals(this.B)) {
                     if (this.L(this.W, (boolean)var5)) {
                        this.Z((boolean)var5, 1);
                     }

                     this.B = this.q = this.W;
                  }
               } else {
                  this.P((boolean)var5);
               }

               this.p = (boolean)var5;
               this.hE = (boolean)var6;
            }
         } else {
            this.W = null;
         }
      }
   }

   private void s() {
      int var1 = this.hb.getValue();
      int var2 = bc.a(var1) && mc.gui.screen() == null && mc.isWindowActive() && bc.I(mc.getWindow().handle(), var1) ? 1 : 0;
      if (var2 != 0 && !this.C) {
         this.M = this.isRuntimeActive();
         if (this.M) {
            this.r("Turn Order Dropper off before changing shulkers.");
         } else {
            this.W();
            this.N = null;
            this.h0 = null;
            this.Z = 0;
            this.hu = System.nanoTime();
         }
      } else if (var2 == 0 && this.C && !this.M) {
         if (this.N == null && System.nanoTime() - this.hu < 400000000L) {
            if (this.h6) {
               this.M();
            } else {
               this.P();
            }
         } else if (this.N != null) {
            com.larpclient.h.A("OrderDropper", "Key sweep {} {} shulkers; total {}", this.N ? "added" : "removed", this.Z, this.hn.size());
         }
      }

      this.C = (boolean)var2;
   }

   private void n(BlockPos var1) {
      if (var1 != null && !var1.equals(this.h0)) {
         this.h0 = var1;
         if (this.N == null) {
            this.N = !this.hn.contains(var1);
         }

         if (this.L(var1, this.N)) {
            this.q = var1;
            this.Z(this.N, ++this.Z);
         }
      }
   }

   private void P(boolean var1) {
      this.B = null;
      this.e = (boolean)0;
      if (this.W == null) {
         this.r("Look at a placed shulker box.");
      } else {
         if (c5.J(mc.options.keyShift) && this.q != null && !this.q.equals(this.W)) {
            this.e = (boolean)1;
            List var2 = ck.Q(v(this.q), v(this.W));
            if (var2.isEmpty()) {
               this.r("That area is too large. Keep each side within 32 blocks.");
               return;
            }

            int var3 = 0;

            for (ck.s var5 : var2) {
               if (this.L(new BlockPos(var5.x(), var5.y(), var5.z()), var1)) {
                  var3++;
               }
            }

            this.Z(var1, var3);
         } else {
            if (this.L(this.W, var1)) {
               this.Z(var1, 1);
            } else {
               this.r((var1 ? "Already selected; left-click removes it. " : "Not selected. ") + this.hn.size() + " selected.");
            }

            this.B = this.W;
         }

         this.q = this.W;
      }
   }

   private boolean L(BlockPos var1, boolean var2) {
      if (var2 ? !this.hn.contains(var1) && this.q(var1) : this.hn.remove(var1)) {
         if (var2) {
            this.hn.add(var1.immutable());
         } else {
            this.G.remove(var1);
            this.sC.remove(var1);
            this.sp.remove(var1);
            this.hj.remove(var1);
         }

         this.P.put(var1.immutable(), new OrderDropperModule.f(System.nanoTime(), var2));
         return true;
      } else {
         return false;
      }
   }

   private void Z(boolean var1, int var2) {
      this.r((var1 ? "Added " : "Removed ") + var2 + (var2 == 1 ? " shulker. " : " shulkers. ") + this.hn.size() + " selected.");
      com.larpclient.h.U("OrderDropper", "{} {} shulkers; total {}", var1 ? "Added" : "Removed", var2, this.hn.size());
   }

   private static ck.s v(BlockPos var0) {
      return new ck.s(var0.getX(), var0.getY(), var0.getZ());
   }

   private int R(BlockPos var1, boolean var2) {
      return this.sp.contains(var1) ? -1876386 : (this.G.contains(var1) ? -2061766 : (this.e(var1, var2) ? -11702 : -13180806));
   }

   private static int W(int var0) {
      return var0 == -11702 ? -10733056 : (var0 == -2061766 ? -11918843 : (var0 == -1876386 ? -11923692 : -16040668));
   }

   private boolean e(BlockPos var1, boolean var2) {
      return var2 && var1.equals(this.hW) && (this.hi == OrderDropperModule.z.k || this.hi == OrderDropperModule.z.x || this.hi == OrderDropperModule.z.e);
   }

   private String n(BlockPos var1, boolean var2) {
      if (this.sp.contains(var1)) {
         return "Skipped";
      }

      if (this.e(var1, var2)) {
         return this.hS ? "Scanning" : (this.hi == OrderDropperModule.z.e ? "Filling" : (this.hi == OrderDropperModule.z.k ? "Next" : "Opening"));
      }

      if (this.G.contains(var1)) {
         return "Full";
      }

      Integer var3 = this.sC.get(var1);
      return var3 == null ? null : var3 + " free";
   }

   private void o() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null) {
         try {
            this.I();
            if (var1.level != this.hZ) {
               return;
            }

            long var2 = System.nanoTime();
            if (!this.hn.isEmpty()) {
               this.x(var1.gameRenderer.mainCamera().position());
            }

            this.p(var2);
            if (this.W != null) {
               this.z();
            }
         } catch (RuntimeException var4) {
            com.larpclient.h.w("OrderDropper", "Shulker highlight failed", var4);
         }
      }
   }

   private void x(Vec3 var1) {
      boolean var2 = this.isRuntimeActive();

      for (BlockPos var4 : this.hn) {
         String var5 = this.n(var4, var2);
         Vec3 var6 = Vec3.atCenterOf(var4);
         if (var5 != null && var1.distanceToSqr(var6) < 100.0) {
            Gizmos.billboardText(var5, var6, Style.forColorAndCentered(-854792).withScale(0.4F)).setAlwaysOnTop();
         }
      }
   }

   private void p(long var1) {
      Iterator var3 = this.P.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         double var5 = 1.0 - (var1 - ((OrderDropperModule.f)var4.getValue()).at()) / 4.5E8;
         if (var5 <= 0.0) {
            var3.remove();
         } else {
            int var7 = ((OrderDropperModule.f)var4.getValue()).added() ? -13180806 : -1017711;
            Gizmos.cuboid(new AABB((BlockPos)var4.getKey()).inflate(0.02 + 0.06 * (1.0 - var5)), GizmoStyle.fill(uh.H(var7, 0.4 * var5))).setAlwaysOnTop();
         }
      }
   }

   private void z() {
      boolean var1 = this.hn.contains(this.W);
      Gizmos.cuboid(new AABB(this.W).inflate(0.012), GizmoStyle.strokeAndFill(-854792, 2.5F, uh.H(var1 ? -1017711 : -13180806, 0.2))).setAlwaysOnTop();
      if (this.q != null && !this.q.equals(this.W) && !this.C && c5.J(mc.options.keyShift)) {
         int var2 = !ck.Q(v(this.q), v(this.W)).isEmpty() ? 1 : 0;
         AABB var3 = new AABB(this.q).minmax(new AABB(this.W)).inflate(0.03);
         Gizmos.cuboid(var3, GizmoStyle.strokeAndFill(var2 != 0 ? -854792 : -1017711, 1.5F, uh.H(var2 != 0 ? -854792 : -1017711, 0.06))).setAlwaysOnTop();
      }
   }

   public static OrderDropperModule V() {
      return hT;
   }

   public void F(PoseStack var1, hh var2, Vec3 var3) {
      if (this.isEnabled()) {
         Minecraft var4 = getMc();
         if (var4 != null && var4.level != null && var4.level == this.hZ && !this.hn.isEmpty()) {
            long var5 = zj.m();

            try {
               this.N(this.isRuntimeActive());
               VertexConsumer var7 = j4.b(var2, this.t());
               Pose var8 = var1.last();
               double var9 = cf.F();
               double var11 = var9 * 0.08 % 1.0;
               double var13 = 0.5 + 0.5 * Math.sin(var9 * 6.0);
               double var15 = this.c.getValue();

               for (int var17 = 0; var17 < this.hy.size(); var17++) {
                  double[] var18 = this.hy.get(var17);
                  int var19 = this.E.get(var17);
                  double var20 = var19 == -11702 ? var15 * (0.7 + 0.3 * var13) : var15;

                  for (int var22 = 0; var22 < 4; var22++) {
                     int var23 = var22 * 5;
                     j4.q(
                        var7,
                        var8,
                        (float)(var18[var23] - var3.x),
                        (float)(var18[var23 + 1] - var3.y),
                        (float)(var18[var23 + 2] - var3.z),
                        (float)var18[var23 + 3],
                        (float)var18[var23 + 4],
                        (float)var18[20],
                        (float)var18[21],
                        (float)var18[22],
                        var19,
                        W(var19),
                        var20,
                        0.6,
                        1.0,
                        var11
                     );
                  }
               }
            } finally {
               zj.l("Order Dropper selection", var5);
            }
         }
      }
   }

   private j4.j t() {
      return switch ((String)this.ha.getValue()) {
         case "Glow" -> j4.j.u;
         case "Metallic" -> j4.j.T;
         case "Marble" -> j4.j.r;
         case "Glitter" -> j4.j.z;
         case "Solid" -> j4.j.K;
         case "Ghost" -> j4.j.F;
         default -> j4.j.N;
      };
   }

   private void N(boolean var1) {
      long var2 = this.hn.size();

      for (BlockPos var5 : this.hn) {
         var2 = var2 * 31L + var5.asLong() * 17L + this.R(var5, var1);
      }

      if (var2 != this.n) {
         this.n = var2;
         this.hy.clear();
         this.E.clear();
         HashSet var36 = new HashSet<>(this.hn);

         for (BlockPos var6 : this.hn) {
            int var7 = this.R(var6, var1);

            for (Direction var11 : Direction.values()) {
               if (!var36.contains(var6.relative(var11))) {
                  Direction[] var12 = hA[var11.ordinal()];
                  Direction var13 = var12[0];
                  Direction var14 = var12[1];
                  double var15 = var36.contains(var6.relative(var13.getOpposite())) ? 0.0 : -0.012;
                  double var17 = var36.contains(var6.relative(var13)) ? 1.0 : 1.012;
                  double var19 = var36.contains(var6.relative(var14.getOpposite())) ? 0.0 : -0.012;
                  double var21 = var36.contains(var6.relative(var14)) ? 1.0 : 1.012;
                  double[] var23 = new double[23];
                  double[][] var24 = new double[][]{{var15, var19}, {var17, var19}, {var17, var21}, {var15, var21}};

                  for (int var25 = 0; var25 < 4; var25++) {
                     double var26 = var24[var25][0];
                     double var28 = var24[var25][1];
                     double var30 = P(var6.getX(), var11.getStepX(), var13.getStepX(), var14.getStepX(), var26, var28);
                     double var32 = P(var6.getY(), var11.getStepY(), var13.getStepY(), var14.getStepY(), var26, var28);
                     double var34 = P(var6.getZ(), var11.getStepZ(), var13.getStepZ(), var14.getStepZ(), var26, var28);
                     var23[var25 * 5] = var30;
                     var23[var25 * 5 + 1] = var32;
                     var23[var25 * 5 + 2] = var34;
                     var23[var25 * 5 + 3] = Math.floorMod(
                           var13.getAxis() == Axis.X ? var6.getX() : (var13.getAxis() == Axis.Y ? var6.getY() : var6.getZ()), 256
                        )
                        + var26;
                     var23[var25 * 5 + 4] = Math.floorMod(
                           var14.getAxis() == Axis.X ? var6.getX() : (var14.getAxis() == Axis.Y ? var6.getY() : var6.getZ()), 256
                        )
                        + var28;
                  }

                  var23[20] = var11.getStepX();
                  var23[21] = var11.getStepY();
                  var23[22] = var11.getStepZ();
                  this.hy.add(var23);
                  this.E.add(var7);
               }
            }
         }
      }
   }

   private static double P(int var0, int var1, int var2, int var3, double var4, double var6) {
      double var8 = var1 > 0 ? var0 + 1 + 0.012 : (var1 < 0 ? var0 - 0.012 : var0);
      return var8 + (var2 != 0 ? var4 : 0.0) + (var3 != 0 ? var6 : 0.0);
   }

   private void U(OrderDropperModule.z var1) {
      if (this.hi != var1) {
         com.larpclient.h.U("OrderDropper", "Phase {} -> {}", this.hi, var1);
      }

      this.hi = var1;
      this.hx = 0;
      if (var1 == OrderDropperModule.z.k || var1 == OrderDropperModule.z.z) {
         this.hm = Double.MAX_VALUE;
         this.sz = 0;
      }
   }

   private int k() {
      return this.h8.getValue().intValue();
   }

   private float K() {
      return mc.player.getHealth() + mc.player.getAbsorptionAmount();
   }

   private double m() {
      return Math.min(mc.player.blockInteractionRange(), 4.5);
   }

   private double f(Vec3 var1) {
      Vec3 var2 = mc.player.position();
      return Math.hypot(var1.x - var2.x, var1.z - var2.z);
   }

   private boolean q(BlockPos var1) {
      return var1 != null && mc.level.hasChunkAt(var1) && mc.level.getBlockState(var1).getBlock() instanceof ShulkerBoxBlock;
   }

   private boolean R(ItemStack var1) {
      if (!var1.isEmpty() && !this.u.isEmpty()) {
         if (!this.u.equals(this.sJ)) {
            Identifier var2 = Identifier.tryParse(this.u);
            this.hD = var2 == null ? null : (Item)BuiltInRegistries.ITEM.getOptional(var2).orElse(null);
            this.sJ = this.u;
         }

         return var1.getItem() == this.hD;
      } else {
         return false;
      }
   }

   private int Z() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (mc.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private int J() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.sc.contains(var2) && this.R(mc.player.getInventory().getItem(var2))) {
            var1++;
         }
      }

      return var1;
   }

   private boolean z() {
      return this.J() > 0;
   }

   private int a() {
      this.r();
      int var1 = 0;

      for (BlockPos var3 : this.z()) {
         if (!this.G.contains(var3) && !this.sp.contains(var3)) {
            var1 += this.sC.getOrDefault(var3, 0);
         }
      }

      return var1;
   }

   private void P(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (var1.getSlot(var3).getItem().isEmpty()) {
            var2++;
         }
      }

      this.sC.put(this.hW, var2);
      this.hj.put(this.hW, mc.level.getBlockEntity(this.hW));
      this.sp.remove(this.hW);
      if (var2 == 0) {
         this.G.add(this.hW);
      } else {
         this.G.remove(this.hW);
      }

      com.larpclient.h.A("OrderDropper", "Scanned shulker {}: free slots={}", this.hW.toShortString(), var2);
   }

   private void r() {
      for (BlockPos var2 : this.z()) {
         if (!this.q(var2)) {
            if (this.sp.add(var2)) {
               com.larpclient.h.g("OrderDropper", "Selected shulker {} is missing or unloaded; capacity removed", var2.toShortString());
            }

            this.sC.remove(var2);
            this.G.remove(var2);
            this.hj.remove(var2);
         } else if (this.hj.containsKey(var2) && this.hj.get(var2) != mc.level.getBlockEntity(var2)) {
            this.sC.remove(var2);
            this.G.remove(var2);
            this.hj.remove(var2);
         }
      }
   }

   private static AABB D(BlockPos var0, Direction var1) {
      return new AABB(var0).expandTowards(var1.getStepX() * 0.5, var1.getStepY() * 0.5, var1.getStepZ() * 0.5).inflate(0.1);
   }

   private AABB t() {
      return D(this.hW, (Direction)mc.level.getBlockState(this.hW).getValue(ShulkerBoxBlock.FACING));
   }

   private boolean r() {
      return !this.t().intersects(mc.player.getBoundingBox());
   }

   private Vec3 O() {
      return m(
         this.hW,
         mc.player.position(),
         mc.player.getBoundingBox(),
         this.t(),
         var0 -> mc.level.noCollision(mc.player, var0),
         var1 -> !this.J.contains(var1) && this.U(var1.add(0.0, mc.player.getEyeHeight(), 0.0)) != null
      );
   }

   private BlockHitResult U(Vec3 var1) {
      double var2 = this.m();
      if (H(this.hW, var1) > var2 * var2) {
         return null;
      }

      List var4 = mc.level
         .getEntities(mc.player, new AABB(this.hW).minmax(new AABB(var1, var1)).inflate(1.0), var0 -> var0.isPickable() && !var0.isSpectator());
      return w(this.hW, var1, var2, (var1x, var2x) -> {
         BlockHitResult var3 = mc.level.clip(new ClipContext(var1x, var2x, Block.OUTLINE, Fluid.NONE, mc.player));
         if (var3.getType() != Type.BLOCK) {
            return null;
         }

         for (Entity var5 : var4) {
            if (wi.s(var1x, var3.getLocation(), var5.getBoundingBox())) {
               return null;
            }
         }

         return var3;
      });
   }

   private static double H(BlockPos var0, Vec3 var1) {
      double var2 = Math.clamp(var1.x, var0.getX(), var0.getX() + 1.0);
      double var4 = Math.clamp(var1.y, var0.getY(), var0.getY() + 1.0);
      double var6 = Math.clamp(var1.z, var0.getZ(), var0.getZ() + 1.0);
      return var1.distanceToSqr(var2, var4, var6);
   }

   private static BlockHitResult w(BlockPos var0, Vec3 var1, double var2, BiFunction<Vec3, Vec3, BlockHitResult> var4) {
      if (Double.isFinite(var2) && !(var2 <= 0.0) && !(H(var0, var1) > var2 * var2 + 1.0E-7)) {
         ArrayList var5 = new ArrayList(28);
         var5.add(Vec3.atCenterOf(var0));
         Vec3 var6 = Vec3.atCenterOf(var0);

         for (Direction var10 : Direction.values()) {
            double var11 = (var1.x - var6.x) * var10.getStepX() + (var1.y - var6.y) * var10.getStepY() + (var1.z - var6.z) * var10.getStepZ();
            if (!(var11 <= 0.5)) {
               for (Vec3 var14 : t1.F(var0, var10)) {
                  if (var1.distanceToSqr(var14) <= var2 * var2 + 1.0E-7) {
                     var5.add(var14);
                  }
               }
            }
         }

         for (Vec3 var16 : var5) {
            Vec3 var17 = var16.subtract(var1);
            if (!(var17.lengthSqr() < 1.0E-8)) {
               BlockHitResult var18 = (BlockHitResult)var4.apply(var1, var1.add(var17.normalize().scale(var2)));
               if (var18 != null
                  && var18.getType() == Type.BLOCK
                  && var18.getBlockPos().equals(var0)
                  && !(var1.distanceToSqr(var18.getLocation()) > var2 * var2 + 1.0E-7)) {
                  float[] var19 = j6.s(var1, i(var0, var18));
                  BlockHitResult var12 = (BlockHitResult)var4.apply(var1, var1.add(Vec3.directionFromRotation(var19[1], var19[0]).scale(var2)));
                  if (var12 != null
                     && var12.getType() == Type.BLOCK
                     && var12.getBlockPos().equals(var0)
                     && var1.distanceToSqr(var12.getLocation()) <= var2 * var2 + 1.0E-7) {
                     return var18;
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static Vec3 i(BlockPos var0, BlockHitResult var1) {
      Vec3 var2 = var1.getLocation();
      return new Vec3(
         Math.clamp(var2.x, var0.getX() + 0.02, var0.getX() + 0.98),
         Math.clamp(var2.y, var0.getY() + 0.02, var0.getY() + 0.98),
         Math.clamp(var2.z, var0.getZ() + 0.02, var0.getZ() + 0.98)
      );
   }

   private static Vec3 m(BlockPos var0, Vec3 var1, AABB var2, AABB var3, Predicate<AABB> var4, Predicate<Vec3> var5) {
      Vec3 var6 = Vec3.atBottomCenterOf(var0);
      ArrayList var7 = new ArrayList(72);

      for (double var11 : new double[]{1.25, 1.8, 2.5}) {
         for (int var16 : new int[]{0, -1, 1}) {
            for (int var17 = -1; var17 <= 1; var17++) {
               for (int var18 = -1; var18 <= 1; var18++) {
                  if (var17 != 0 || var18 != 0) {
                     Vec3 var19 = new Vec3(var6.x + var17 * var11, var1.y + var16, var6.z + var18 * var11);
                     AABB var20 = var2.move(var19.subtract(var1));
                     if (!var3.intersects(var20) && var4.test(var20) && !var4.test(var20.move(0.0, -0.15, 0.0))) {
                        var7.add(var19);
                     }
                  }
               }
            }
         }
      }

      var7.sort(Comparator.comparingDouble(var1x -> var1x.distanceToSqr(var1)));

      for (Vec3 var22 : var7) {
         if (var5.test(var22)) {
            return var22;
         }
      }

      return null;
   }

   private boolean G() {
      if (!ta.Y() || j6.j() && !j6.g(j6.l.s)) {
         this.K = "Waiting for camera or rotation control";
         this.hx = Math.max(0, this.hx - 1);
         return true;
      } else {
         return false;
      }
   }

   public static boolean d() {
      if (hT != null && hT.isEnabled()) {
         OrderDropperModule var0 = hT;
         return var0 != null && var0.isRuntimeActive() && var0.hi != null && var0.sP.getValue();
      } else {
         return false;
      }
   }

   private void u() {
      if (!this.sP.getValue()) {
         this.Y();
      } else {
         if (!this.hJ) {
            this.hB = mc.options.pauseOnLostFocus;
            this.hJ = (boolean)1;
            com.larpclient.h.A("OrderDropper", "AFK mouse released; pause on focus loss temporarily disabled");
         }

         mc.options.pauseOnLostFocus = (boolean)0;
         if (mc.mouseHandler.isMouseGrabbed()) {
            mc.mouseHandler.releaseMouse();
         }
      }
   }

   private void Y() {
      if (this.hJ) {
         mc.options.pauseOnLostFocus = this.hB;
         this.hJ = (boolean)0;
         com.larpclient.h.A("OrderDropper", "AFK ended; pause on focus loss restored");
      }
   }

   private int v() {
      Vec3 var1 = this.he == null ? mc.player.position() : this.he;
      AABB var2 = new AABB(var1, var1).inflate(2.5, 1.5, 2.5);
      return mc.level.getEntitiesOfClass(ItemEntity.class, var2, var1x -> var1x.isAlive() && this.R(var1x.getItem())).size();
   }

   private static int w(AbstractContainerMenu var0) {
      int var1 = 31 * var0.containerId + var0.getStateId();

      for (Slot var3 : var0.slots) {
         var1 = 31 * var1 + ItemStack.hashItemAndComponents(var3.getItem()) * 7 + var3.getItem().getCount();
      }

      ItemStack var4 = var0.getCarried();
      return 31 * var1 + ItemStack.hashItemAndComponents(var4) + var4.getCount();
   }

   private static List<String> y(ItemStack var0) {
      ItemLore var1 = (ItemLore)var0.get(DataComponents.LORE);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (Component var4 : var1.lines()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static String y(ItemStack var0) {
      StringBuilder var1 = new StringBuilder(z(var0));

      for (String var3 : y(var0)) {
         if (!ck.d(var3).contains("until")) {
            var1.append('|').append(var3);
         }
      }

      return var1.toString();
   }

   private static String z(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private void s(String var1) {
      this.N(var1);
      com.larpclient.h.A("OrderDropper", "Not started: {}", var1);
      this.disable();
   }

   private void E(String var1) {
      com.larpclient.h.g("OrderDropper", "Stopped in phase {}: {}", this.hi, var1);
      this.N("Stopped: " + var1);
      this.disable();
   }

   private void r(String var1) {
      this.U = System.nanoTime();
      if (mc.player != null) {
         mc.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private void x(String var1) {
      if (this.h2.getValue()) {
         this.N(var1);
      }
   }

   private void N(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Order Dropper] " + var1), false);
      }
   }

   static {
      hA[Direction.DOWN.ordinal()] = new Direction[]{Direction.EAST, Direction.SOUTH};
      hA[Direction.UP.ordinal()] = new Direction[]{Direction.SOUTH, Direction.EAST};
      hA[Direction.NORTH.ordinal()] = new Direction[]{Direction.UP, Direction.EAST};
      hA[Direction.SOUTH.ordinal()] = new Direction[]{Direction.EAST, Direction.UP};
      hA[Direction.WEST.ordinal()] = new Direction[]{Direction.SOUTH, Direction.UP};
      hA[Direction.EAST.ordinal()] = new Direction[]{Direction.UP, Direction.SOUTH};
   }

   private record f(long at, boolean added) {
   }

   private enum o {
      i,
      T;
   }

   private enum z {
      i,
      c,
      k,
      x,
      e,
      z,
      r;
   }
}
