package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.c5;
import com.larpclient.cr;
import com.larpclient.cw;
import com.larpclient.ei;
import com.larpclient.fk;
import com.larpclient.gv;
import com.larpclient.gz;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.s5;
import com.larpclient.t9;
import com.larpclient.ug;
import com.larpclient.w6;
import com.larpclient.wp;
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
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_10185;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1733;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_465;
import net.minecraft.class_476;
import net.minecraft.class_495;
import net.minecraft.class_7923;
import net.minecraft.class_9290;
import net.minecraft.class_9334;
import net.minecraft.class_12187.class_12188;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import net.minecraft.class_4587.class_4665;

public final class OrderDropperModule extends Module {
   public static final String Wd = "Drop Only";
   public static final String WD = "Fill Shulkers";
   private static final int B = 20;
   private static final int U = 60;
   private static final int O = 60;
   private static final int W_ = 120;
   private static final int R = 50;
   private static final int mH = 3;
   private static final int WX = 3;
   private static final int w = -13180806;
   private static final int M = -11702;
   private static final int v = -2061766;
   private static final int WU = -1876386;
   private static final int a = -854792;
   private static final int k = -1017711;
   private static final double Wb = 24.0;
   private static final double s = 10.0;
   private static final int Wj = 32;
   private static final long WP = 450000000L;
   private static final long L = 2000000000L;
   private static final long mb = 400000000L;
   private static final double V = 0.012;
   private static OrderDropperModule Wx;
   private final li WW = this.addSetting(
      new li(
         "Mode",
         "Drop Only drops every page where you stand. Fill Shulkers drops a page at your feet, picks it up, stores it in the selected shulkers and walks back, until the orders are empty",
         "Fill Shulkers",
         "Drop Only",
         "Fill Shulkers"
      )
   );
   private final aj mh = this.addSetting(
         new aj("Order Item", "Item whose orders are emptied, such as totem_of_undying. Use the held-item button to fill it in", "")
      )
      .A("totem_of_undying");
   private final lw T = this.addSetting(
      new lw(
         "Select Shulker Key",
         "Hold and sweep your crosshair over shulker boxes to select them; a sweep that starts on a selected box removes instead. Tap it for click selection: right-click adds, left-click removes, sneak-click covers an area",
         -1
      )
   );
   private final lo G = this.addSetting(new lo("Click Delay", "Ticks between menu clicks and item moves", 4.0, 2.0, 20.0, 1.0));
   private final l3 WB = this.addSetting(
      new l3("Stop When Hurt", "Stop at once when you take damage. Menus block Auto Totem, so this protects you while it works", true)
   );
   private final l3 WC = this.addSetting(new l3("Show Messages", "Report orders, pages, trips and the final summary in chat", true));
   private final lo W5 = this.addSetting(
      new lo("Fill Delay", "Ticks between shulker transfers. Lower is faster. Reopens the box to verify the result before leaving", 2.0, 1.0, 20.0, 1.0)
   );
   private final l3 Wl = this.addSetting(
      new l3("AFK Mode", "Release the mouse and keep running when you switch windows. Your pause-on-focus-loss preference is restored when stopped", false)
   );
   private final li Wk = this.addSetting(
      new li(
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
   private final lo D = this.addSetting(new lo("Highlight Opacity", "How strongly the material covers selected shulkers", 0.85, 0.2, 1.0, 0.05));
   private final List<class_2338> A = new ArrayList<>();
   private final Set<class_2338> W9 = new HashSet<>();
   private final Map<class_2338, Integer> Wm = new HashMap<>();
   private final Set<class_2338> Wi = new HashSet<>();
   private final Map<class_2338, Object> W8 = new HashMap<>();
   private class_1937 WE;
   private boolean We;
   private boolean F;
   private Boolean mA;
   private class_2338 Y;
   private int S;
   private long u;
   private final Map<class_2338, OrderDropperModule.j> Wp = new HashMap<>();
   private boolean h;
   private boolean Wc;
   private boolean WT;
   private boolean m;
   private boolean WQ;
   private boolean E;
   private class_2338 b;
   private class_2338 W4;
   private class_2338 mP;
   private long Wu;
   private final List<double[]> WN = new ArrayList<>();
   private final List<Integer> Wf = new ArrayList<>();
   private long C = Long.MIN_VALUE;
   private String W0;
   private class_1792 WM;
   private Object n;
   private Object l;
   private Object mq;
   private OrderDropperModule.p e;
   private String m4 = "";
   private String Wn = "";
   private String W6 = "Idle";
   private class_243 WS;
   private float Wv;
   private float f;
   private float I;
   private final Set<String> Wo = new HashSet<>();
   private final Set<Integer> WR = new HashSet<>();
   private boolean WJ;
   private boolean X;
   private boolean H;
   private boolean WA;
   private int WV;
   private int x;
   private int WG;
   private int K;
   private int mQ;
   private int Z;
   private int i;
   private int Wg;
   private int WZ;
   private OrderDropperModule.e y;
   private int W3;
   private int Wq;
   private int z;
   private class_1703 t;
   private class_1703 WO;
   private int WI;
   private int g;
   private int W;
   private int WK;
   private class_2338 Wa;
   private double Wz;
   private int Q;
   private int W7;
   private int p;
   private int mm;
   private boolean W2;
   private boolean mF;
   private boolean j;
   private int q;
   private int WL;
   private int W1;
   private int WY;
   private boolean Ww;
   private boolean WH;
   private boolean Wr;
   private boolean Ws;
   private boolean d;
   private class_243 Wh;
   private final Set<class_243> o = new HashSet<>();
   private boolean c;
   private boolean Wt;
   private int J;
   private int N;
   private int P;
   private int mk;
   private int WF;
   private int r;
   private static final class_2350[][] Wy = new class_2350[6][];

   public OrderDropperModule() {
      super("Order Dropper", "Empty your DonutSMP orders for one item by dropping pages, or fill selected shulkers with them", Category.PLAYER, -1);
      Wx = this;
      this.W5.O(this::V);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var1 -> this.R());
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.Q());
   }

   public List<lb<?>> j() {
      return List.of(this.WW, this.mh);
   }

   public List<lb<?>> E() {
      return List.of(this.T, this.W5, this.Wk, this.D);
   }

   public List<lb<?>> v() {
      return List.of(this.G, this.Wl, this.WB, this.WC);
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.W6 : (this.Ww ? "Paused" : "Idle");
   }

   private boolean V() {
      return this.WW.G("Fill Shulkers");
   }

   public String T() {
      String var1 = gz.X(this.mh.getValue());
      String var2 = var1.isEmpty() ? "No order item chosen" : "Item: " + var1;
      int var3 = this.J().size();
      return var2
         + ". "
         + var3
         + (var3 == 1 ? " shulker" : " shulkers")
         + " selected"
         + (var3 == 0 ? "" : ", " + this.Wm.size() + " scanned, " + this.W9.size() + " full, " + this.Wi.size() + " unavailable")
         + (this.Ww ? ". Progress saved for this world session." : ".");
   }

   public void p() {
      if (!this.isInGame()) {
         throw new IllegalArgumentException("Join a world first.");
      }

      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      mc.method_1507(null);
      this.g();
   }

   public void A() {
      if (this.isInGame() && !mc.field_1724.method_6047().method_7960()) {
         this.mh.Y(H(mc.field_1724.method_6047()));
         LarpClient var1 = LarpClient.b();
         if (var1 != null && var1.e() != null) {
            var1.e().U();
         }
      } else {
         throw new IllegalArgumentException("Hold the order's item first.");
      }
   }

   public void J() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      this.Z();
      this.A.clear();
      this.W4 = null;
   }

   public void Z() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before resetting progress.");
      }

      this.M();
      this.i();
      this.Wm.clear();
      this.W9.clear();
      this.Wi.clear();
      this.W8.clear();
      this.Ww = (boolean)0;
   }

   public void r() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before rescanning shulkers.");
      }

      this.Wm.clear();
      this.W9.clear();
      this.Wi.clear();
      this.W8.clear();
   }

   private List<class_2338> J() {
      class_310 var1 = getMc();
      return var1 != null && var1.field_1687 == this.WE ? this.A : List.of();
   }

   @Override
   public void onEnable() {
      if (this.isInGame() && mc.field_1724.field_3944 != null) {
         if (this.h) {
            this.G();
         }

         String var1 = gz.X(this.mh.getValue());
         int var2 = this.Ww && this.n == mc.field_1724 && this.l == mc.field_1687 && this.mq == mc.field_1724.field_3944 && this.m4.equals(var1) && this.V()
            ? 1
            : 0;
         if (var2 == 0) {
            this.i();
            this.Ww = (boolean)0;
         }

         this.m4 = var1;
         if (this.m4.isEmpty() || !class_7923.field_41178.method_10250(class_2960.method_12829(this.m4))) {
            this.e("Choose the Order Item first, for example totem_of_undying, or hold it and use the held-item button.");
         } else if (this.V() && !((class_1792)class_7923.field_41178.method_63535(class_2960.method_60654(this.m4))).method_31568()) {
            this.e("This item cannot go inside a shulker box. Choose Drop Only for shulker box orders.");
         } else if (this.V() && this.J().isEmpty()) {
            this.e("Select at least one placed shulker box first: look at it and press the Select Shulker Key.");
         } else {
            this.n = mc.field_1724;
            this.l = mc.field_1687;
            this.mq = mc.field_1724.field_3944;
            if (var2 == 0) {
               this.WS = mc.field_1724.method_73189();
               this.Wv = mc.field_1724.method_36454();
               this.f = mc.field_1724.method_36455();

               for (int var3 = 0; var3 < 36; var3++) {
                  if (this.l(mc.field_1724.method_31548().method_5438(var3))) {
                     this.WR.add(var3);
                  }
               }
            }

            this.I = this.p();
            if (var2 != 0 && this.Wr) {
               this.WJ = (boolean)0;
               this.Wr = (boolean)0;
            }

            this.Ww = this.V();
            this.Wi.clear();
            this.n();
            this.WH = (boolean)(this.V() && this.J().stream().anyMatch(var1x -> !this.Wm.containsKey(var1x) && !this.Wi.contains(var1x)) ? 1 : 0);
            if (this.WH) {
               this.F();
            } else if (var2 != 0 && this.E()) {
               this.F();
            } else {
               this.w(var2 != 0 ? OrderDropperModule.p.s : OrderDropperModule.p.A);
            }

            this.j(
               this.V()
                  ? (var2 != 0 ? "Resumed. " : "Started. ") + "Emptying " + this.m4 + " orders into " + this.J().size() + " shulkers."
                  : "Started. Dropping every page of your " + this.m4 + " orders here."
            );
            q8.G("OrderDropper", "Started mode={} item={} shulkers={} start={}", this.WW.getValue(), this.m4, this.J().size(), this.WS);
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.n != null
         && this.isInGame()
         && mc.field_1724 == this.n
         && mc.field_1724.field_7512 != mc.field_1724.field_7498
         && mc.field_1724.field_7512.method_34255().method_7960()
         && this.v()) {
         mc.field_1724.method_7346();
      }

      if (this.Wa != null && (this.e == OrderDropperModule.p.k || this.c || this.mk > 0)) {
         this.Wm.remove(this.Wa);
      }

      this.M();
      this.e = null;
      this.W2 = this.mF = this.j = (boolean)0;
      this.y = null;
      this.WG = 0;
      this.H = this.WA = (boolean)0;
      this.WV = this.mQ = this.Z = this.i = this.Wg = 0;
      this.Wa = null;
      this.Wh = null;
      this.o.clear();
      this.q();
   }

   private void i() {
      this.n = this.l = this.mq = null;
      this.e = null;
      this.WS = null;
      this.W6 = "Idle";
      this.Wo.clear();
      this.WR.clear();
      this.WJ = this.X = this.H = this.WA = (boolean)0;
      this.WV = this.x = this.WG = this.K = this.mQ = this.Z = this.i = this.Wg = this.WZ = 0;
      this.y = null;
      this.z = 0;
      this.Wa = null;
      this.Wn = "";
      this.Q = this.W7 = this.p = this.mm = 0;
      this.W2 = this.mF = this.j = (boolean)0;
      this.q = this.WL = this.W1 = this.WY = 0;
      this.WH = this.c = this.Wt = this.Wr = (boolean)0;
      this.Wh = null;
      this.J = this.N = this.P = this.mk = this.WF = this.r = 0;
      this.o.clear();
      this.q();
   }

   @Override
   public void onTick() {
      this.W2 = this.mF = this.j = (boolean)0;
      if (this.e != null && this.isInGame() && mc.field_1724.field_3944 != null) {
         if (mc.field_1724 == this.n && mc.field_1687 == this.l && mc.field_1724.field_3944 == this.mq) {
            this.u();
            if (!mc.field_1724.method_5805()) {
               this.y("You died.");
            } else {
               float var1 = this.p();
               if (this.WB.getValue() && var1 < this.I - 0.01F) {
                  this.y("You took damage.");
               } else {
                  this.I = var1;
                  if (this.d()) {
                     this.y("You moved.");
                  } else if (this.WS != null && mc.field_1724.method_23318() < this.WS.field_1351 - 2.5) {
                     this.y("You fell below the start spot.");
                  } else {
                     if (this.e != OrderDropperModule.p.A) {
                        int var10000 = mc.field_1755 instanceof class_476;
                        boolean var7 = false;
                        byte var6 = (byte)var10000;
                        var10000 = Integer.signum(var6);
                        if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && gz.l(mc.field_1755.method_25440().getString()) != gz.a.b) {
                           this.W6 = "Closing Orders before continuing";
                           var10000 = ++this.WV;
                           byte var9 = 100;
                           int var8 = var10000;
                           if ((-Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
                              this.y("The Orders menu kept reopening.");
                              return;
                           }

                           var10000 = this.WV % 4;
                           byte var5 = 1;
                           int var4 = var10000;
                           var10000 = Integer.compare(var4, var5);
                           if ((var10000 | -var10000) >>> 31 == 0) {
                              this.j();
                           }

                           return;
                        }
                     }

                     byte var3 = 1;
                     int var2 = this.K;
                     this.K = var2 + var3;
                     switch (this.e) {
                        case A:
                           this.t();
                           break;
                        case m:
                           this.s();
                           break;
                        case b:
                           this.W();
                           break;
                        case Q:
                           this.K();
                           break;
                        case k:
                           this.N();
                           break;
                        case s:
                           this.B();
                           break;
                        case f:
                           this.b();
                     }
                  }
               }
            }
         } else {
            this.y("The world changed.");
         }
      }
   }

   private void t() {
      if (this.WG > 0) {
         this.WG--;
      } else {
         class_437 var1 = mc.field_1755;
         if (this.WA) {
            if (var1 != null) {
               this.mQ = 0;
               if (++this.WV > 100) {
                  this.y("The Orders menu kept reopening.");
                  return;
               }

               if (this.WV % 4 == 1 && this.v()) {
                  this.j();
               }

               return;
            }

            if (++this.mQ < 4) {
               return;
            }

            this.WA = (boolean)0;
         }

         if (var1 == null) {
            if (this.y != null) {
               this.y = null;
            }

            if (this.V()) {
               if (this.X) {
                  this.w(OrderDropperModule.p.m);
                  return;
               }

               if (this.q() == 0) {
                  if (this.E()) {
                     this.F();
                  } else {
                     this.y("Your inventory is full of other items. Make room and enable again.");
                  }

                  return;
               }

               if (this.c() > 0) {
                  this.w(OrderDropperModule.p.m);
                  return;
               }

               if (this.WJ) {
                  this.S();
                  return;
               }

               if (mc.field_1724.method_36455() < 88.0F) {
                  this.W6 = "Looking down";
                  t9.O(this, t9.v.G, mc.field_1724.method_36454(), 90.0F, 0.5);
                  return;
               }
            } else if (this.WJ) {
               this.y();
               return;
            }

            if (++this.mQ >= 5) {
               if (this.Z <= 0 || ++this.Z >= 60) {
                  if (++this.i > 3) {
                     this.y("The /orders menu did not open after 3 tries.");
                  } else {
                     this.W6 = "Opening /orders";
                     mc.field_1724.field_3944.method_45730("orders");
                     this.Z = 1;
                     q8.v("OrderDropper", "Sent /orders (attempt {})", this.i);
                  }
               }
            }
         } else {
            this.mQ = 0;
            if (!(var1 instanceof class_465)) {
               this.W6 = "Waiting for the open screen to close";
            } else {
               class_1707 var2 = this.O();
               if (var2 == null) {
                  this.y("A different menu opened: " + var1.method_25440().getString() + ".");
               } else {
                  this.i = this.Z = 0;
                  if (this.y != null) {
                     int var5 = var2.field_7763 == this.W3 && S(var2) == this.Wq ? 0 : 1;
                     if (var5 != 0) {
                        this.y = null;
                        this.WG = this.G();
                        this.Wg = 0;
                     } else if (++this.z > (this.y == OrderDropperModule.e.q ? 20 : 60)) {
                        if (this.y == OrderDropperModule.e.q && ++this.Wg <= 3) {
                           this.y = null;
                        } else {
                           this.y("The server did not respond to a menu click.");
                        }
                     }
                  } else {
                     class_1799 var3 = var2.method_34255();
                     if (var3.method_7960() || this.l(var3) && gz.l(var1.method_25440().getString()) == gz.a.k) {
                        this.x = 0;
                        gz.a var4 = gz.l(var1.method_25440().getString());
                        if (this.l(var2, var4)) {
                           switch (var4) {
                              case f:
                                 this.W6 = "Opening Your Orders";
                                 this.Q(var2, gz.w.W, OrderDropperModule.e.q);
                                 break;
                              case i:
                                 this.Q(var2);
                                 break;
                              case V:
                                 this.W6 = "Opening Collect";
                                 this.Q(var2, gz.w.c, OrderDropperModule.e.q);
                                 break;
                              case k:
                                 this.g(var2);
                                 break;
                              case b:
                                 this.y("An unexpected Orders menu opened: " + var1.method_25440().getString() + ".");
                           }
                        }
                     } else {
                        if (++this.x > 60) {
                           this.y("The cursor still holds " + var3.method_7964().getString() + ".");
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void F(int var0) {
      OrderDropperModule var1 = Wx;
      class_310 var2 = getMc();
      if (var1 != null && var1.e == OrderDropperModule.p.A && var2 != null && var2.field_1724 != null) {
         class_1703 var3 = var2.field_1724.field_7512;
         if (var3 instanceof class_1707 && var3.field_7763 == var0) {
            var1.t = var3;
            var1.g = 0;
         }
      }
   }

   private void q() {
      this.t = this.WO = null;
      this.WI = this.g = this.W = this.WK = 0;
   }

   private boolean l(class_1707 var1, gz.a var2) {
      int var3 = S(var1);
      if (this.WO == var1 && this.WI == var3) {
         this.g++;
      } else {
         this.WO = var1;
         this.WI = var3;
         this.g = 0;
      }

      String var4 = null;
      if (this.t != var1) {
         var4 = "server contents";
      } else if (this.g < 2) {
         var4 = "settled contents";
      } else if (var2 == gz.a.f && this.p(var1, gz.w.W) == -1) {
         var4 = "YOUR_ORDERS button";
      } else if (var2 == gz.a.V && this.p(var1, gz.w.c) == -1) {
         var4 = "COLLECT button";
      } else if (var2 == gz.a.i) {
         byte var5 = 0;

         for (int var6 = 0; var6 < Math.min(45, var1.method_17388() * 9); var6++) {
            if (!var1.method_7611(var6).method_7677().method_7960()) {
               var5 = 1;
               break;
            }
         }

         if (var5 == 0) {
            var4 = "your order entries or empty-order placeholders";
         }
      }

      if (var4 == null) {
         this.W = 0;
         return true;
      } else {
         this.W6 = "Waiting for Orders: " + var4;
         if (++this.W <= 60) {
            return false;
         } else if (++this.WK > 3) {
            this.y("The Orders menu did not load " + var4 + " after 3 reloads. Progress retained.");
            return false;
         } else {
            q8.L(
               "OrderDropper",
               "Reloading Orders: missing={} title={} id={} synced={} retry={}/{}",
               var4,
               mc.field_1755.method_25440().getString(),
               var1.field_7763,
               this.t == var1,
               this.WK,
               3
            );
            this.W = 0;
            this.I();
            return false;
         }
      }
   }

   private void Q(class_1707 var1) {
      for (int var2 = 0; var2 < 45 && var2 < var1.method_17388() * 9; var2++) {
         class_1799 var3 = var1.method_7611(var2).method_7677();
         if (!var3.method_7960() && gz.k(H(var3), this.m4, A(var3))) {
            String var4 = s(var3);
            if (!this.Wo.contains(var4)) {
               this.Wn = var4;
               this.WZ = 0;
               this.W6 = "Opening your " + var3.method_7964().getString() + " order";
               q8.v("OrderDropper", "Opening order in slot {}: {}", var2, var4);
               this.J(var1, var2, class_1713.field_7790, OrderDropperModule.e.q);
               return;
            }
         }
      }

      this.WJ = (boolean)1;
      this.j(this.Wo.isEmpty() ? "You have no " + this.m4 + " orders with items to collect." : "All " + this.m4 + " orders are empty.");
      q8.G("OrderDropper", "Orders finished; emptied={} pages={} taken={}", this.Wo.size(), this.q, this.WL);
      this.I();
   }

   private void g(class_1707 var1) {
      int var2 = 0;
      int var3 = -1;

      for (int var4 = 0; var4 < 45; var4++) {
         if (!var1.method_7611(var4).method_7677().method_7960()) {
            var2++;
            if (var3 < 0) {
               var3 = var4;
            }
         }
      }

      int var6 = this.p(var1, gz.w.A);
      int var5 = this.p(var1, gz.w.t);
      if (!var1.method_34255().method_7960()) {
         this.X(var1);
      } else if (var2 != 0) {
         this.WZ = 0;
         if (var6 >= 0) {
            if (this.V() && this.X) {
               this.I();
            } else if (this.V() && var2 + this.u() > this.x()) {
               this.j("The selected shulkers have room for " + this.x() + " stacks; this page has " + var2 + ". Select more shulkers to continue.");
               q8.G("OrderDropper", "Not dropping: page={} carrying={} room={}", var2, this.u(), this.x());
               this.WJ = (boolean)1;
               this.Wr = (boolean)1;
               this.I();
            } else {
               this.W6 = "Dropping page " + (this.q + 1);
               this.Q(var1, gz.w.A, OrderDropperModule.e.D);
               this.q++;
               this.X = (boolean)1;
               q8.v("OrderDropper", "Dropped page {} ({} stacks)", this.q, var2);
            }
         } else if (this.q() != 0) {
            this.W6 = "Taking items";
            this.J(var1, var3, class_1713.field_7790, OrderDropperModule.e.D);
         } else if (this.V() && this.E()) {
            this.I();
         } else {
            this.y("This order has no Drop button and your inventory is full. Make room and enable again.");
         }
      } else if (var5 >= 0 && ++this.WZ <= 3) {
         this.W6 = "Next page";
         this.Q(var1, gz.w.t, OrderDropperModule.e.D);
      } else {
         this.Wo.add(this.Wn);
         this.j("Order emptied.");
         q8.G("OrderDropper", "Order emptied after {} pages: {}", this.q, this.Wn);
         this.a();
      }
   }

   private void X(class_1707 var1) {
      for (int var2 = var1.method_17388() * 9; var2 < var1.field_7761.size(); var2++) {
         class_1735 var3 = var1.method_7611(var2);
         if (var3.field_7871 == mc.field_1724.method_31548() && var3.method_34266() < 36 && var3.method_7677().method_7960()) {
            this.J(var1, var2, class_1713.field_7790, OrderDropperModule.e.D);
            this.WL++;
            return;
         }
      }

      this.y("No empty inventory slot for the item on the cursor.");
   }

   private int p(class_1707 var1, gz.w var2) {
      int var3 = -1;

      for (int var4 = var1.method_17388() == 6 ? 45 : 0; var4 < var1.method_17388() * 9; var4++) {
         class_1799 var5 = var1.method_7611(var4).method_7677();
         if (!var5.method_7960() && gz.N(H(var5), var5.method_7964().getString()) == var2) {
            if (var3 >= 0) {
               return -2;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void Q(class_1707 var1, gz.w var2, OrderDropperModule.e var3) {
      int var4 = this.p(var1, var2);
      if (var4 == -2) {
         this.y("Two " + var2 + " buttons found; this menu layout needs review.");
      } else if (var4 < 0) {
         this.y("No " + var2 + " button in this menu.");
      } else {
         this.J(var1, var4, class_1713.field_7790, var3);
      }
   }

   private void J(class_1703 var1, int var2, class_1713 var3, OrderDropperModule.e var4) {
      class_1799 var5 = var1.method_7611(var2).method_7677();
      if (var2 < 54 && var1 instanceof class_1707 && gz.N(H(var5), var5.method_7964().getString()) == gz.w.h) {
         this.y("Refused to click a Sell button.");
      } else {
         this.y = var4;
         this.W3 = var1.field_7763;
         this.z = 0;
         this.W = this.WK = 0;
         mc.field_1761.method_2906(var1.field_7763, var2, 0, var3, mc.field_1724);
         this.Wq = S(var1);
      }
   }

   private void a() {
      this.Wn = "";
      this.WZ = 0;
      this.I();
   }

   private void I() {
      this.WA = (boolean)1;
      this.WV = 0;
      this.y = null;
      this.mQ = 0;
      this.Z = 0;
      this.i = 0;
      this.j();
   }

   private void j() {
      if (mc.field_1724.field_7512 != mc.field_1724.field_7498 && mc.field_1724.field_7512.method_34255().method_7960()) {
         mc.field_1724.method_7346();
      }
   }

   private class_1707 O() {
      if (mc.field_1755 instanceof class_476 var1
         && mc.field_1724.field_7512 instanceof class_1707 var2
         && var1.method_17577() == var2
         && (var2.method_17388() == 6 || var2.method_17388() == 3)) {
         gz.a var5 = gz.l(var1.method_25440().getString());
         return var5 == gz.a.b && !gz.w(var1.method_25440().getString()).startsWith("orders") ? null : var2;
      } else {
         return null;
      }
   }

   private boolean v() {
      return mc.field_1755 instanceof class_495 ? this.Wa != null : mc.field_1755 != null && gz.l(mc.field_1755.method_25440().getString()) != gz.a.b;
   }

   private void s() {
      this.W6 = "Picking up items";
      int var1 = this.c();
      if (this.q() <= 0 || this.K >= 120 || var1 <= 0 && this.K >= 10) {
         if (var1 > 0 && this.q() > 0) {
            q8.L("OrderDropper", "{} item stacks stayed out of pickup reach", var1);
         }

         if (this.q() != 0 && (!this.WJ || !this.E())) {
            if (var1 > 0 && !this.E()) {
               this.y("The dropped items landed out of pickup reach.");
            } else if (this.WJ) {
               this.S();
            } else {
               this.d();
            }
         } else {
            this.F();
         }
      }
   }

   private void d() {
      this.X = (boolean)0;
      this.w(OrderDropperModule.p.A);
   }

   private void S() {
      if (this.E()) {
         this.F();
      } else {
         this.y();
      }
   }

   private void F() {
      this.Wa = null;
      this.n();
      class_243 var1 = mc.field_1724.method_73189();
      double var2 = Double.MAX_VALUE;

      for (class_2338 var5 : this.J()) {
         if (!this.Wi.contains(var5) && (this.WH ? !this.Wm.containsKey(var5) : !this.W9.contains(var5))) {
            double var6 = class_243.method_24953(var5).method_1025(var1);
            if (var6 < var2) {
               var2 = var6;
               this.Wa = var5;
            }
         }
      }

      if (this.Wa == null) {
         if (this.WH) {
            this.WH = (boolean)0;
            this.w(OrderDropperModule.p.s);
         } else {
            this.Wr = (boolean)1;
            if (this.S(this.WS) > 0.4) {
               this.w(OrderDropperModule.p.s);
               this.j("No usable shulkers remain; walking back.");
               this.WJ = (boolean)1;
            } else {
               this.y("No usable shulkers remain. Select more or rescan, then enable again.");
            }
         }
      } else {
         if (!this.WH) {
            this.WY++;
         }

         this.c = this.Wt = (boolean)0;
         this.P = this.mk = this.WF = this.r = 0;
         this.Wh = null;
         this.o.clear();
         this.Wz = Double.MAX_VALUE;
         this.Q = 0;
         this.w(OrderDropperModule.p.b);
         q8.v("OrderDropper", "Trip {} to shulker {} carrying {} stacks", this.WY, this.Wa.method_23854(), this.u());
      }
   }

   private void W() {
      if (!this.l(this.Wa)) {
         this.Q("is missing or unloaded");
      } else if (mc.field_1755 != null) {
         this.W6 = "Close the screen to continue";
      } else if (!this.a()) {
         this.W6 = this.WH ? "Walking to scan shulker" : "Walking to shulker";
         if (this.z() && this.N(mc.field_1724.method_33571()) != null) {
            this.W7 = this.mm = 0;
            this.H = (boolean)0;
            this.w(OrderDropperModule.p.Q);
         } else {
            if (this.Wh != null && this.S(this.Wh) < 0.15 && Math.abs(mc.field_1724.method_23318() - this.Wh.field_1351) < 0.35) {
               this.o.add(this.Wh);
               q8.v("OrderDropper", "Replanning shulker {} from eye={} after arriving at {}", this.Wa.method_23854(), mc.field_1724.method_33571(), this.Wh);
               this.Wh = null;
            }

            if (this.Wh == null) {
               this.Wh = this.K();
               this.Wz = Double.MAX_VALUE;
               this.Q = 0;
            }

            if (this.Wh == null) {
               q8.L(
                  "OrderDropper",
                  "No visible standing spot for {}: eye={} reach={} tried={}",
                  this.Wa.method_23854(),
                  mc.field_1724.method_33571(),
                  this.B(),
                  this.o.size()
               );
               this.Q("has no clear reachable standing spot");
            } else {
               this.U(this.Wh, 0.1);
            }
         }
      }
   }

   private void K() {
      if (!this.l(this.Wa)) {
         this.Q("is missing or unloaded");
      } else if (mc.field_1755 instanceof class_495 var1 && mc.field_1724.field_7512 instanceof class_1733 var2 && var1.method_17577() == var2) {
         this.H = (boolean)0;
         this.WG = this.G();
         this.w(OrderDropperModule.p.k);
      } else if (!this.z() && !this.H) {
         this.Wh = null;
         this.Wz = Double.MAX_VALUE;
         this.Q = 0;
         this.w(OrderDropperModule.p.b);
      } else {
         this.W6 = "Opening shulker";
         if (this.H) {
            if (++this.p <= 60) {
               return;
            }

            this.H = (boolean)0;
            if (++this.mm >= 3) {
               this.Q("did not open");
               return;
            }
         }

         if (mc.field_1755 == null) {
            if (!this.a()) {
               class_3965 var6 = this.N(mc.field_1724.method_33571());
               if (var6 == null) {
                  this.Wh = null;
                  this.w(OrderDropperModule.p.b);
               } else {
                  class_243 var7 = I(this.Wa, var6);
                  float[] var9 = t9.X(mc.field_1724.method_33571(), var7);
                  t9.O(this, t9.v.G, var9[0], var9[1], 0.5);
                  class_3965 var4 = cr.Y(this.Wa, this.B());
                  if (var4 == null) {
                     this.W7 = 0;
                     if (this.K > 60) {
                        this.Q("could not be aimed at");
                     }
                  } else if (++this.W7 >= 2 && !mc.field_1724.method_5715()) {
                     boolean var5 = cr.P(this, () -> {
                        if (mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, var4).method_23665()) {
                           w6.H(mc.field_1724, class_1268.field_5808);
                        }
                     }, () -> mc.field_1755 == null);
                     if (var5) {
                        this.H = (boolean)1;
                        this.p = 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void N() {
      if (mc.field_1755 instanceof class_495 var1 && mc.field_1724.field_7512 instanceof class_1733 var2 && var1.method_17577() == var2) {
         this.W6 = this.WH ? "Scanning shulker" : "Filling shulker";
         if (!var2.method_34255().method_7960()) {
            this.y("The cursor holds an item inside the shulker.");
         } else if (this.WG <= 0 || --this.WG <= 0) {
            if (this.WH) {
               this.I(var2);
               mc.field_1724.method_7346();
               this.F();
            } else {
               if (this.c) {
                  int var7 = this.F(var2);
                  int var4 = this.h(var2);
                  int var5 = Math.max(0, var7 - this.J);
                  this.W1 = this.W1 + Math.max(0, var4 - this.N);
                  q8.G(
                     "OrderDropper", "Verified shulker {}: items added={} ({} -> {}), carrying={} stacks", this.Wa.method_23854(), var5, this.J, var7, this.u()
                  );
                  this.J = var7;
                  this.N = var4;
                  this.I(var2);
                  this.c = (boolean)0;
                  this.mk = 0;
                  if (var5 > 0) {
                     this.WF = 0;
                  } else if (++this.WF > 3 && this.E()) {
                     this.y("Shulker transfers kept being rejected. Progress retained; check the server and enable again.");
                     return;
                  }
               }

               if (!this.Wt) {
                  this.J = this.F(var2);
                  this.N = this.h(var2);
                  this.Wt = (boolean)1;
               }

               if (this.mk >= 36) {
                  this.c();
               } else {
                  for (int var8 = 27; var8 < var2.field_7761.size(); var8++) {
                     class_1735 var10 = var2.method_7611(var8);
                     class_1799 var11 = var10.method_7677();
                     if (this.l(var11) && (var10.field_7871 != mc.field_1724.method_31548() || !this.WR.contains(var10.method_34266())) && this.u(var2, var11)) {
                        mc.field_1761.method_2906(var2.field_7763, var8, 0, class_1713.field_7794, mc.field_1724);
                        this.mk++;
                        this.P = 0;
                        this.WG = this.W5.getValue().intValue();
                        return;
                     }
                  }

                  if (!this.E() && ++this.P < Math.max(10, this.W5.getValue().intValue())) {
                     this.W6 = "Waiting for remaining items";
                  } else if (this.mk > 0) {
                     this.c();
                  } else {
                     this.I(var2);
                     boolean var9 = this.E();
                     if (var9) {
                        this.W9.add(this.Wa);
                        this.j("Shulker full (" + this.W9.size() + "/" + this.J().size() + ").");
                     }

                     mc.field_1724.method_7346();
                     q8.v("OrderDropper", "Closed shulker {}; full={} carrying={}", this.Wa.method_23854(), var9, this.u());
                     if (var9) {
                        this.F();
                     } else {
                        this.w(OrderDropperModule.p.s);
                     }
                  }
               }
            }
         }
      } else {
         this.Wm.remove(this.Wa);
         if (++this.r > 3) {
            this.Q("kept closing before filling finished");
         } else {
            this.Wh = null;
            this.Wz = Double.MAX_VALUE;
            this.Q = 0;
            this.w(OrderDropperModule.p.b);
         }
      }
   }

   private void c() {
      this.c = (boolean)1;
      this.H = (boolean)0;
      this.W7 = this.mm = 0;
      mc.field_1724.method_7346();
      this.w(OrderDropperModule.p.Q);
      q8.v("OrderDropper", "Reopening {} to verify transfers", this.Wa.method_23854());
   }

   private int F(class_1733 var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.l(var1.method_7611(var3).method_7677())) {
            var2 += var1.method_7611(var3).method_7677().method_7947();
         }
      }

      return var2;
   }

   private int h(class_1733 var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.l(var1.method_7611(var3).method_7677())) {
            var2++;
         }
      }

      return var2;
   }

   private boolean u(class_1733 var1, class_1799 var2) {
      for (int var3 = 0; var3 < 27; var3++) {
         class_1735 var4 = var1.method_7611(var3);
         class_1799 var5 = var4.method_7677();
         if (var5.method_7960() ? var4.method_7680(var2) : class_1799.method_31577(var5, var2) && var5.method_7947() < var4.method_7676(var5)) {
            return true;
         }
      }

      return false;
   }

   private void Q(String var1) {
      this.Wi.add(this.Wa);
      this.Wm.remove(this.Wa);
      this.W9.remove(this.Wa);
      this.j("Skipped a shulker that " + var1 + ".");
      q8.L("OrderDropper", "Shulker {} {}; skipped", this.Wa.method_23854(), var1);
      this.F();
   }

   private void B() {
      this.W6 = "Walking back";
      if (this.S(this.WS) > 0.3) {
         this.U(this.WS, 0.3);
      } else if (this.V() && this.c() > 0 && this.q() > 0) {
         this.w(OrderDropperModule.p.m);
      } else if (this.E() && this.J().stream().anyMatch(var1 -> !this.W9.contains(var1) && !this.Wi.contains(var1))) {
         this.F();
      } else if (!this.WJ) {
         this.d();
      } else {
         this.y();
      }
   }

   private void y() {
      if (this.V() && this.S(this.WS) > 0.3) {
         this.w(OrderDropperModule.p.s);
      } else if (this.V() && (this.E() || this.c() > 0)) {
         this.y("Storage unavailable with items still to store. Select more shulkers or rescan, then enable again.");
      } else {
         this.w(OrderDropperModule.p.f);
      }
   }

   private void b() {
      this.W6 = "Finishing";
      if (!this.V()
         || this.K >= 20
         || !(Math.abs(class_3532.method_15393(mc.field_1724.method_36454() - this.Wv)) > 1.0F) && !(Math.abs(mc.field_1724.method_36455() - this.f) > 1.0F)) {
         this.o(
            "Done: " + this.q + " pages dropped, " + this.WL + " stacks taken" + (this.V() ? ", " + this.W1 + " stacks stored in " + this.WY + " trips." : ".")
         );
         q8.G("OrderDropper", "Finished pages={} taken={} stored={} trips={}", this.q, this.WL, this.W1, this.WY);
         this.Ww = (boolean)0;
         this.disable();
      } else {
         t9.O(this, t9.v.G, this.Wv, this.f, 0.5);
      }
   }

   private void U(class_243 var1, double var2) {
      if (!this.a()) {
         double var4 = this.S(var1);
         if (var4 < this.Wz - 0.05) {
            this.Wz = var4;
            this.Q = 0;
         } else if (++this.Q > 50) {
            this.y("The way is blocked; stopped where it was.");
            return;
         }

         class_243 var6 = var1.method_1020(mc.field_1724.method_73189());
         float var7 = (float)Math.toDegrees(Math.atan2(var6.field_1350, var6.field_1352)) - 90.0F;
         t9.O(this, t9.v.G, var7, 25.0F, 0.35);
         this.W2 = (boolean)(Math.abs(class_3532.method_15393(mc.field_1724.method_36454() - var7)) < 35.0F ? 1 : 0);
         this.mF = (boolean)(this.W2 && mc.field_1724.field_5976 && mc.field_1724.method_24828() ? 1 : 0);
         this.j = (boolean)(var2 > 0.0 && var4 < 1.0 ? 1 : 0);
      }
   }

   public static class_10185 d() {
      if (Wx != null && Wx.isEnabled()) {
         OrderDropperModule var0 = Wx;
         if (var0 == null || !var0.isRuntimeActive() || getMc().field_1755 != null || wp.d() || t9.I() && !t9.k(t9.v.G)) {
            return null;
         } else if (!var0.W2) {
            return R() ? new class_10185(false, false, false, false, false, false, false) : null;
         } else {
            return new class_10185(true, false, false, false, var0.mF, var0.j, false);
         }
      } else {
         return null;
      }
   }

   private boolean d() {
      if (mc.field_1755 == null && mc.method_1569()) {
         class_315 var1 = mc.field_1690;
         return com.larpclient.j.W(var1.field_1894)
            || com.larpclient.j.W(var1.field_1881)
            || com.larpclient.j.W(var1.field_1913)
            || com.larpclient.j.W(var1.field_1849)
            || com.larpclient.j.W(var1.field_1903);
      } else {
         return false;
      }
   }

   private void R() {
      class_310 var1 = getMc();
      if (var1 != null && var1.method_22683() != null) {
         if (this.WE != null && var1.field_1687 != this.WE && !this.isRuntimeActive()) {
            this.h = (boolean)0;
            this.b = null;
            this.Z();
            this.A.clear();
            this.Wp.clear();
            this.W4 = null;
            this.WE = null;
         }

         if (this.h) {
            if (!this.isInGame() || var1.field_1687 != this.WE) {
               this.h = (boolean)0;
               this.b = null;
            } else if (!this.isRuntimeActive() && var1.field_1755 == null) {
               if (System.nanoTime() - this.Wu > 2000000000L) {
                  this.C();
               }
            } else {
               this.G();
            }
         }
      }
   }

   private void L() {
      if (this.WE != mc.field_1687) {
         this.Z();
         this.A.clear();
         this.Wp.clear();
         this.W4 = null;
         this.WE = mc.field_1687;
      }
   }

   private void g() {
      this.L();
      this.h = (boolean)1;
      this.W4 = this.mP = this.b = null;
      this.Wc = this.WT = this.m = this.WQ = this.E = (boolean)0;
      if (mc.field_1761 != null) {
         mc.field_1761.method_2925();
      }

      this.C();
      q8.v("OrderDropper", "Selection started with {} shulkers", this.A.size());
   }

   private void G() {
      this.h = (boolean)0;
      this.b = this.mP = null;
      this.m = this.WQ = this.E = (boolean)0;
      this.l("Shulker selection saved. " + this.A.size() + " selected.");
      q8.G("OrderDropper", "Selection finished with {} shulkers", this.A.size());
   }

   private void C() {
      int var1 = this.T.getValue();
      this.l(
         this.A.size()
            + " selected. Right-click adds, left-click removes, hold to sweep, sneak-click for an area. "
            + (s5.A(var1) ? s5.S(var1) + " or Esc" : "Esc")
            + " to finish."
      );
   }

   public static boolean r() {
      OrderDropperModule var0 = Wx;
      return var0 != null && var0.h;
   }

   private void z() {
      if (!this.isInGame()) {
         this.b = null;
         this.We = (boolean)0;
      } else {
         this.e();
         int var1 = this.We && !this.F ? 1 : 0;
         if ((this.h || var1 != 0) && mc.field_1755 == null) {
            class_239 var2 = mc.field_1724.method_5745(24.0, mc.method_61966().method_60637(false), false);
            this.b = var2 instanceof class_3965 var3 && var2.method_17783() == class_240.field_1332 && this.l(var3.method_17777())
               ? var3.method_17777().method_10062()
               : null;
            if (var1 != 0) {
               this.A(this.b);
            } else {
               boolean var7 = com.larpclient.j.W(mc.field_1690.field_1904);
               boolean var4 = com.larpclient.j.W(mc.field_1690.field_1886);
               if (!var7) {
                  this.Wc = (boolean)1;
               }

               if (!var4) {
                  this.WT = (boolean)1;
               }

               int var5 = var7 && this.Wc ? 1 : 0;
               int var6 = var4 && this.WT && var5 == 0 ? 1 : 0;
               if ((var5 == 0 || this.m) && (var6 == 0 || this.WQ)) {
                  if ((var5 != 0 || var6 != 0) && !this.E && this.b != null && !this.b.equals(this.mP)) {
                     if (this.P(this.b, (boolean)var5)) {
                        this.H((boolean)var5, 1);
                     }

                     this.mP = this.W4 = this.b;
                  }
               } else {
                  this.k((boolean)var5);
               }

               this.m = (boolean)var5;
               this.WQ = (boolean)var6;
            }
         } else {
            this.b = null;
         }
      }
   }

   private void e() {
      int var1 = this.T.getValue();
      int var2 = s5.A(var1) && mc.field_1755 == null && mc.method_1569() && s5.m(mc.method_22683().method_4490(), var1) ? 1 : 0;
      if (var2 != 0 && !this.We) {
         this.F = this.isRuntimeActive();
         if (this.F) {
            this.l("Turn Order Dropper off before changing shulkers.");
         } else {
            this.L();
            this.mA = null;
            this.Y = null;
            this.S = 0;
            this.u = System.nanoTime();
         }
      } else if (var2 == 0 && this.We && !this.F) {
         if (this.mA == null && System.nanoTime() - this.u < 400000000L) {
            if (this.h) {
               this.G();
            } else {
               this.g();
            }
         } else if (this.mA != null) {
            q8.G("OrderDropper", "Key sweep {} {} shulkers; total {}", this.mA ? "added" : "removed", this.S, this.A.size());
         }
      }

      this.We = (boolean)var2;
   }

   private void A(class_2338 var1) {
      if (var1 != null && !var1.equals(this.Y)) {
         this.Y = var1;
         if (this.mA == null) {
            this.mA = !this.A.contains(var1);
         }

         if (this.P(var1, this.mA)) {
            this.W4 = var1;
            this.H(this.mA, ++this.S);
         }
      }
   }

   private void k(boolean var1) {
      this.mP = null;
      this.E = (boolean)0;
      if (this.b == null) {
         this.l("Look at a placed shulker box.");
      } else {
         if (com.larpclient.j.W(mc.field_1690.field_1832) && this.W4 != null && !this.W4.equals(this.b)) {
            this.E = (boolean)1;
            List var2 = gz.J(R(this.W4), R(this.b));
            if (var2.isEmpty()) {
               this.l("That area is too large. Keep each side within 32 blocks.");
               return;
            }

            int var3 = 0;

            for (gz.f var5 : var2) {
               if (this.P(new class_2338(var5.x(), var5.y(), var5.z()), var1)) {
                  var3++;
               }
            }

            this.H(var1, var3);
         } else {
            if (this.P(this.b, var1)) {
               this.H(var1, 1);
            } else {
               this.l((var1 ? "Already selected; left-click removes it. " : "Not selected. ") + this.A.size() + " selected.");
            }

            this.mP = this.b;
         }

         this.W4 = this.b;
      }
   }

   private boolean P(class_2338 var1, boolean var2) {
      if (var2 ? !this.A.contains(var1) && this.l(var1) : this.A.remove(var1)) {
         if (var2) {
            this.A.add(var1.method_10062());
         } else {
            this.W9.remove(var1);
            this.Wm.remove(var1);
            this.Wi.remove(var1);
            this.W8.remove(var1);
         }

         this.Wp.put(var1.method_10062(), new OrderDropperModule.j(System.nanoTime(), var2));
         return true;
      } else {
         return false;
      }
   }

   private void H(boolean var1, int var2) {
      this.l((var1 ? "Added " : "Removed ") + var2 + (var2 == 1 ? " shulker. " : " shulkers. ") + this.A.size() + " selected.");
      q8.v("OrderDropper", "{} {} shulkers; total {}", var1 ? "Added" : "Removed", var2, this.A.size());
   }

   private static gz.f R(class_2338 var0) {
      return new gz.f(var0.method_10263(), var0.method_10264(), var0.method_10260());
   }

   private int g(class_2338 var1, boolean var2) {
      return this.Wi.contains(var1) ? -1876386 : (this.W9.contains(var1) ? -2061766 : (this.D(var1, var2) ? -11702 : -13180806));
   }

   private static int T(int var0) {
      return var0 == -11702 ? -10733056 : (var0 == -2061766 ? -11918843 : (var0 == -1876386 ? -11923692 : -16040668));
   }

   private boolean D(class_2338 var1, boolean var2) {
      return var2 && var1.equals(this.Wa) && (this.e == OrderDropperModule.p.b || this.e == OrderDropperModule.p.Q || this.e == OrderDropperModule.p.k);
   }

   private String i(class_2338 var1, boolean var2) {
      if (this.Wi.contains(var1)) {
         return "Skipped";
      }

      if (this.D(var1, var2)) {
         return this.WH ? "Scanning" : (this.e == OrderDropperModule.p.k ? "Filling" : (this.e == OrderDropperModule.p.b ? "Next" : "Opening"));
      }

      if (this.W9.contains(var1)) {
         return "Full";
      }

      Integer var3 = this.Wm.get(var1);
      return var3 == null ? null : var3 + " free";
   }

   private void Q() {
      class_310 var1 = getMc();
      if (var1 != null && var1.field_1687 != null) {
         try {
            this.z();
            if (var1.field_1687 != this.WE) {
               return;
            }

            long var2 = System.nanoTime();
            if (!this.A.isEmpty()) {
               this.D(var1.field_1773.method_19418().method_71156());
            }

            this.L(var2);
            if (this.b != null) {
               this.m();
            }
         } catch (RuntimeException var4) {
            q8.O("OrderDropper", "Shulker highlight failed", var4);
         }
      }
   }

   private void D(class_243 var1) {
      boolean var2 = this.isRuntimeActive();

      for (class_2338 var4 : this.A) {
         String var5 = this.i(var4, var2);
         class_243 var6 = class_243.method_24953(var4);
         if (var5 != null && var1.method_1025(var6) < 100.0) {
            class_12180.method_75551(var5, var6, class_12188.method_75566(-854792).method_75565(0.4F)).method_75533();
         }
      }
   }

   private void L(long var1) {
      Iterator var3 = this.Wp.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         double var5 = 1.0 - (var1 - ((OrderDropperModule.j)var4.getValue()).at()) / 4.5E8;
         if (var5 <= 0.0) {
            var3.remove();
         } else {
            int var7 = ((OrderDropperModule.j)var4.getValue()).added() ? -13180806 : -1017711;
            class_12180.method_75541(
                  new class_238((class_2338)var4.getKey()).method_1014(0.02 + 0.06 * (1.0 - var5)), class_12179.method_75539(ei.D(var7, 0.4 * var5))
               )
               .method_75533();
         }
      }
   }

   private void m() {
      boolean var1 = this.A.contains(this.b);
      class_12180.method_75541(new class_238(this.b).method_1014(0.012), class_12179.method_75537(-854792, 2.5F, ei.D(var1 ? -1017711 : -13180806, 0.2)))
         .method_75533();
      if (this.W4 != null && !this.W4.equals(this.b) && !this.We && com.larpclient.j.W(mc.field_1690.field_1832)) {
         int var2 = !gz.J(R(this.W4), R(this.b)).isEmpty() ? 1 : 0;
         class_238 var3 = new class_238(this.W4).method_991(new class_238(this.b)).method_1014(0.03);
         class_12180.method_75541(var3, class_12179.method_75537(var2 != 0 ? -854792 : -1017711, 1.5F, ei.D(var2 != 0 ? -854792 : -1017711, 0.06)))
            .method_75533();
      }
   }

   public static OrderDropperModule I() {
      return Wx;
   }

   public void M(class_4587 var1, cw var2, class_243 var3) {
      if (this.isEnabled()) {
         class_310 var4 = getMc();
         if (var4 != null && var4.field_1687 != null && var4.field_1687 == this.WE && !this.A.isEmpty()) {
            long var5 = c5.X();

            try {
               this.h(this.isRuntimeActive());
               class_4588 var7 = gv.f(var2, this.L());
               class_4665 var8 = var1.method_23760();
               double var9 = qo.x();
               double var11 = var9 * 0.08 % 1.0;
               double var13 = 0.5 + 0.5 * Math.sin(var9 * 6.0);
               double var15 = this.D.getValue();

               for (int var17 = 0; var17 < this.WN.size(); var17++) {
                  double[] var18 = this.WN.get(var17);
                  int var19 = this.Wf.get(var17);
                  double var20 = var19 == -11702 ? var15 * (0.7 + 0.3 * var13) : var15;

                  for (int var22 = 0; var22 < 4; var22++) {
                     int var23 = var22 * 5;
                     gv.X(
                        var7,
                        var8,
                        (float)(var18[var23] - var3.field_1352),
                        (float)(var18[var23 + 1] - var3.field_1351),
                        (float)(var18[var23 + 2] - var3.field_1350),
                        (float)var18[var23 + 3],
                        (float)var18[var23 + 4],
                        (float)var18[20],
                        (float)var18[21],
                        (float)var18[22],
                        var19,
                        T(var19),
                        var20,
                        0.6,
                        1.0,
                        var11
                     );
                  }
               }
            } finally {
               c5.w("Order Dropper selection", var5);
            }
         }
      }
   }

   private gv.y L() {
      return switch ((String)this.Wk.getValue()) {
         case "Glow" -> gv.y.M;
         case "Metallic" -> gv.y.r;
         case "Marble" -> gv.y.Z;
         case "Glitter" -> gv.y.X;
         case "Solid" -> gv.y.p;
         case "Ghost" -> gv.y.x;
         default -> gv.y.y;
      };
   }

   private void h(boolean var1) {
      long var2 = this.A.size();

      for (class_2338 var5 : this.A) {
         var2 = var2 * 31L + var5.method_10063() * 17L + this.g(var5, var1);
      }

      if (var2 != this.C) {
         this.C = var2;
         this.WN.clear();
         this.Wf.clear();
         HashSet var36 = new HashSet<>(this.A);

         for (class_2338 var6 : this.A) {
            int var7 = this.g(var6, var1);

            for (class_2350 var11 : class_2350.values()) {
               if (!var36.contains(var6.method_10093(var11))) {
                  class_2350[] var12 = Wy[var11.ordinal()];
                  class_2350 var13 = var12[0];
                  class_2350 var14 = var12[1];
                  double var15 = var36.contains(var6.method_10093(var13.method_10153())) ? 0.0 : -0.012;
                  double var17 = var36.contains(var6.method_10093(var13)) ? 1.0 : 1.012;
                  double var19 = var36.contains(var6.method_10093(var14.method_10153())) ? 0.0 : -0.012;
                  double var21 = var36.contains(var6.method_10093(var14)) ? 1.0 : 1.012;
                  double[] var23 = new double[23];
                  double[][] var24 = new double[][]{{var15, var19}, {var17, var19}, {var17, var21}, {var15, var21}};

                  for (int var25 = 0; var25 < 4; var25++) {
                     double var26 = var24[var25][0];
                     double var28 = var24[var25][1];
                     double var30 = c(var6.method_10263(), var11.method_10148(), var13.method_10148(), var14.method_10148(), var26, var28);
                     double var32 = c(var6.method_10264(), var11.method_10164(), var13.method_10164(), var14.method_10164(), var26, var28);
                     double var34 = c(var6.method_10260(), var11.method_10165(), var13.method_10165(), var14.method_10165(), var26, var28);
                     var23[var25 * 5] = var30;
                     var23[var25 * 5 + 1] = var32;
                     var23[var25 * 5 + 2] = var34;
                     var23[var25 * 5 + 3] = Math.floorMod(
                           var13.method_10166() == class_2351.field_11048
                              ? var6.method_10263()
                              : (var13.method_10166() == class_2351.field_11052 ? var6.method_10264() : var6.method_10260()),
                           256
                        )
                        + var26;
                     var23[var25 * 5 + 4] = Math.floorMod(
                           var14.method_10166() == class_2351.field_11048
                              ? var6.method_10263()
                              : (var14.method_10166() == class_2351.field_11052 ? var6.method_10264() : var6.method_10260()),
                           256
                        )
                        + var28;
                  }

                  var23[20] = var11.method_10148();
                  var23[21] = var11.method_10164();
                  var23[22] = var11.method_10165();
                  this.WN.add(var23);
                  this.Wf.add(var7);
               }
            }
         }
      }
   }

   private static double c(int var0, int var1, int var2, int var3, double var4, double var6) {
      double var8 = var1 > 0 ? var0 + 1 + 0.012 : (var1 < 0 ? var0 - 0.012 : var0);
      return var8 + (var2 != 0 ? var4 : 0.0) + (var3 != 0 ? var6 : 0.0);
   }

   private void w(OrderDropperModule.p var1) {
      if (this.e != var1) {
         q8.v("OrderDropper", "Phase {} -> {}", this.e, var1);
      }

      this.e = var1;
      this.K = 0;
      if (var1 == OrderDropperModule.p.b || var1 == OrderDropperModule.p.s) {
         this.Wz = Double.MAX_VALUE;
         this.Q = 0;
      }
   }

   private int G() {
      return this.G.getValue().intValue();
   }

   private float p() {
      return mc.field_1724.method_6032() + mc.field_1724.method_6067();
   }

   private double B() {
      return Math.min(mc.field_1724.method_55754(), 4.5);
   }

   private double S(class_243 var1) {
      class_243 var2 = mc.field_1724.method_73189();
      return Math.hypot(var1.field_1352 - var2.field_1352, var1.field_1350 - var2.field_1350);
   }

   private boolean l(class_2338 var1) {
      return var1 != null && mc.field_1687.method_22340(var1) && mc.field_1687.method_8320(var1).method_26204() instanceof class_2480;
   }

   private boolean l(class_1799 var1) {
      if (!var1.method_7960() && !this.m4.isEmpty()) {
         if (!this.m4.equals(this.W0)) {
            class_2960 var2 = class_2960.method_12829(this.m4);
            this.WM = var2 == null ? null : (class_1792)class_7923.field_41178.method_17966(var2).orElse(null);
            this.W0 = this.m4;
         }

         return var1.method_7909() == this.WM;
      } else {
         return false;
      }
   }

   private int q() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (mc.field_1724.method_31548().method_5438(var2).method_7960()) {
            var1++;
         }
      }

      return var1;
   }

   private int u() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.WR.contains(var2) && this.l(mc.field_1724.method_31548().method_5438(var2))) {
            var1++;
         }
      }

      return var1;
   }

   private boolean E() {
      return this.u() > 0;
   }

   private int x() {
      this.n();
      int var1 = 0;

      for (class_2338 var3 : this.J()) {
         if (!this.W9.contains(var3) && !this.Wi.contains(var3)) {
            var1 += this.Wm.getOrDefault(var3, 0);
         }
      }

      return var1;
   }

   private void I(class_1733 var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (var1.method_7611(var3).method_7677().method_7960()) {
            var2++;
         }
      }

      this.Wm.put(this.Wa, var2);
      this.W8.put(this.Wa, mc.field_1687.method_8321(this.Wa));
      this.Wi.remove(this.Wa);
      if (var2 == 0) {
         this.W9.add(this.Wa);
      } else {
         this.W9.remove(this.Wa);
      }

      q8.G("OrderDropper", "Scanned shulker {}: free slots={}", this.Wa.method_23854(), var2);
   }

   private void n() {
      for (class_2338 var2 : this.J()) {
         if (!this.l(var2)) {
            if (this.Wi.add(var2)) {
               q8.L("OrderDropper", "Selected shulker {} is missing or unloaded; capacity removed", var2.method_23854());
            }

            this.Wm.remove(var2);
            this.W9.remove(var2);
            this.W8.remove(var2);
         } else if (this.W8.containsKey(var2) && this.W8.get(var2) != mc.field_1687.method_8321(var2)) {
            this.Wm.remove(var2);
            this.W9.remove(var2);
            this.W8.remove(var2);
         }
      }
   }

   private static class_238 p(class_2338 var0, class_2350 var1) {
      return new class_238(var0).method_1012(var1.method_10148() * 0.5, var1.method_10164() * 0.5, var1.method_10165() * 0.5).method_1014(0.1);
   }

   private class_238 i() {
      return p(this.Wa, (class_2350)mc.field_1687.method_8320(this.Wa).method_11654(class_2480.field_11496));
   }

   private boolean z() {
      return !this.i().method_994(mc.field_1724.method_5829());
   }

   private class_243 K() {
      return S(
         this.Wa,
         mc.field_1724.method_73189(),
         mc.field_1724.method_5829(),
         this.i(),
         var0 -> mc.field_1687.method_8587(mc.field_1724, var0),
         var1 -> !this.o.contains(var1) && this.N(var1.method_1031(0.0, mc.field_1724.method_5751(), 0.0)) != null
      );
   }

   private class_3965 N(class_243 var1) {
      double var2 = this.B();
      if (h(this.Wa, var1) > var2 * var2) {
         return null;
      }

      List var4 = mc.field_1687
         .method_8333(
            mc.field_1724, new class_238(this.Wa).method_991(new class_238(var1, var1)).method_1014(1.0), var0 -> var0.method_5863() && !var0.method_7325()
         );
      return g(this.Wa, var1, var2, (var1x, var2x) -> {
         class_3965 var3 = mc.field_1687.method_17742(new class_3959(var1x, var2x, class_3960.field_17559, class_242.field_1348, mc.field_1724));
         if (var3.method_17783() != class_240.field_1332) {
            return null;
         }

         for (class_1297 var5 : var4) {
            if (ug.U(var1x, var3.method_17784(), var5.method_5829())) {
               return null;
            }
         }

         return var3;
      });
   }

   private static double h(class_2338 var0, class_243 var1) {
      double var2 = Math.clamp(var1.field_1352, var0.method_10263(), var0.method_10263() + 1.0);
      double var4 = Math.clamp(var1.field_1351, var0.method_10264(), var0.method_10264() + 1.0);
      double var6 = Math.clamp(var1.field_1350, var0.method_10260(), var0.method_10260() + 1.0);
      return var1.method_1028(var2, var4, var6);
   }

   private static class_3965 g(class_2338 var0, class_243 var1, double var2, BiFunction<class_243, class_243, class_3965> var4) {
      if (Double.isFinite(var2) && !(var2 <= 0.0) && !(h(var0, var1) > var2 * var2 + 1.0E-7)) {
         ArrayList var5 = new ArrayList(28);
         var5.add(class_243.method_24953(var0));
         class_243 var6 = class_243.method_24953(var0);

         for (class_2350 var10 : class_2350.values()) {
            double var11 = (var1.field_1352 - var6.field_1352) * var10.method_10148()
               + (var1.field_1351 - var6.field_1351) * var10.method_10164()
               + (var1.field_1350 - var6.field_1350) * var10.method_10165();
            if (!(var11 <= 0.5)) {
               for (class_243 var14 : fk.n(var0, var10)) {
                  if (var1.method_1025(var14) <= var2 * var2 + 1.0E-7) {
                     var5.add(var14);
                  }
               }
            }
         }

         for (class_243 var16 : var5) {
            class_243 var17 = var16.method_1020(var1);
            if (!(var17.method_1027() < 1.0E-8)) {
               class_3965 var18 = (class_3965)var4.apply(var1, var1.method_1019(var17.method_1029().method_1021(var2)));
               if (var18 != null
                  && var18.method_17783() == class_240.field_1332
                  && var18.method_17777().equals(var0)
                  && !(var1.method_1025(var18.method_17784()) > var2 * var2 + 1.0E-7)) {
                  float[] var19 = t9.X(var1, I(var0, var18));
                  class_3965 var12 = (class_3965)var4.apply(var1, var1.method_1019(class_243.method_1030(var19[1], var19[0]).method_1021(var2)));
                  if (var12 != null
                     && var12.method_17783() == class_240.field_1332
                     && var12.method_17777().equals(var0)
                     && var1.method_1025(var12.method_17784()) <= var2 * var2 + 1.0E-7) {
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

   private static class_243 I(class_2338 var0, class_3965 var1) {
      class_243 var2 = var1.method_17784();
      return new class_243(
         Math.clamp(var2.field_1352, var0.method_10263() + 0.02, var0.method_10263() + 0.98),
         Math.clamp(var2.field_1351, var0.method_10264() + 0.02, var0.method_10264() + 0.98),
         Math.clamp(var2.field_1350, var0.method_10260() + 0.02, var0.method_10260() + 0.98)
      );
   }

   private static class_243 S(class_2338 var0, class_243 var1, class_238 var2, class_238 var3, Predicate<class_238> var4, Predicate<class_243> var5) {
      class_243 var6 = class_243.method_24955(var0);
      ArrayList var7 = new ArrayList(72);

      for (double var11 : new double[]{1.25, 1.8, 2.5}) {
         for (int var16 : new int[]{0, -1, 1}) {
            for (int var17 = -1; var17 <= 1; var17++) {
               for (int var18 = -1; var18 <= 1; var18++) {
                  if (var17 != 0 || var18 != 0) {
                     class_243 var19 = new class_243(var6.field_1352 + var17 * var11, var1.field_1351 + var16, var6.field_1350 + var18 * var11);
                     class_238 var20 = var2.method_997(var19.method_1020(var1));
                     if (!var3.method_994(var20) && var4.test(var20) && !var4.test(var20.method_989(0.0, -0.15, 0.0))) {
                        var7.add(var19);
                     }
                  }
               }
            }
         }
      }

      var7.sort(Comparator.comparingDouble(var1x -> var1x.method_1025(var1)));

      for (class_243 var22 : var7) {
         if (var5.test(var22)) {
            return var22;
         }
      }

      return null;
   }

   private boolean a() {
      if (!cr.f() || t9.I() && !t9.k(t9.v.G)) {
         this.W6 = "Waiting for camera or rotation control";
         this.K = Math.max(0, this.K - 1);
         return true;
      } else {
         return false;
      }
   }

   public static boolean R() {
      if (Wx != null && Wx.isEnabled()) {
         OrderDropperModule var0 = Wx;
         return var0 != null && var0.isRuntimeActive() && var0.e != null && var0.Wl.getValue();
      } else {
         return false;
      }
   }

   private void u() {
      if (!this.Wl.getValue()) {
         this.M();
      } else {
         if (!this.Ws) {
            this.d = mc.field_1690.field_1837;
            this.Ws = (boolean)1;
            q8.G("OrderDropper", "AFK mouse released; pause on focus loss temporarily disabled");
         }

         mc.field_1690.field_1837 = (boolean)0;
         if (mc.field_1729.method_1613()) {
            mc.field_1729.method_1610();
         }
      }
   }

   private void M() {
      if (this.Ws) {
         mc.field_1690.field_1837 = this.d;
         this.Ws = (boolean)0;
         q8.G("OrderDropper", "AFK ended; pause on focus loss restored");
      }
   }

   private int c() {
      class_243 var1 = this.WS == null ? mc.field_1724.method_73189() : this.WS;
      class_238 var2 = new class_238(var1, var1).method_1009(2.5, 1.5, 2.5);
      return mc.field_1687.method_8390(class_1542.class, var2, var1x -> var1x.method_5805() && this.l(var1x.method_6983())).size();
   }

   private static int S(class_1703 var0) {
      int var1 = 31 * var0.field_7763 + var0.method_37421();

      for (class_1735 var3 : var0.field_7761) {
         var1 = 31 * var1 + class_1799.method_57355(var3.method_7677()) * 7 + var3.method_7677().method_7947();
      }

      class_1799 var4 = var0.method_34255();
      return 31 * var1 + class_1799.method_57355(var4) + var4.method_7947();
   }

   private static List<String> A(class_1799 var0) {
      class_9290 var1 = (class_9290)var0.method_58694(class_9334.field_49632);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (class_2561 var4 : var1.comp_2400()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static String s(class_1799 var0) {
      StringBuilder var1 = new StringBuilder(H(var0));

      for (String var3 : A(var0)) {
         if (!gz.w(var3).contains("until")) {
            var1.append('|').append(var3);
         }
      }

      return var1.toString();
   }

   private static String H(class_1799 var0) {
      return class_7923.field_41178.method_10221(var0.method_7909()).toString();
   }

   private void e(String var1) {
      this.o(var1);
      q8.G("OrderDropper", "Not started: {}", var1);
      this.disable();
   }

   private void y(String var1) {
      q8.L("OrderDropper", "Stopped in phase {}: {}", this.e, var1);
      this.o("Stopped: " + var1);
      this.disable();
   }

   private void l(String var1) {
      this.Wu = System.nanoTime();
      if (mc.field_1724 != null) {
         mc.field_1724.method_7353(class_2561.method_43470(var1), true);
      }
   }

   private void j(String var1) {
      if (this.WC.getValue()) {
         this.o(var1);
      }
   }

   private void o(String var1) {
      if (this.isInGame()) {
         mc.method_44714().method_44736(class_2561.method_43470("[Order Dropper] " + var1), false);
      }
   }

   static {
      Wy[class_2350.field_11033.ordinal()] = new class_2350[]{class_2350.field_11034, class_2350.field_11035};
      Wy[class_2350.field_11036.ordinal()] = new class_2350[]{class_2350.field_11035, class_2350.field_11034};
      Wy[class_2350.field_11043.ordinal()] = new class_2350[]{class_2350.field_11036, class_2350.field_11034};
      Wy[class_2350.field_11035.ordinal()] = new class_2350[]{class_2350.field_11034, class_2350.field_11036};
      Wy[class_2350.field_11039.ordinal()] = new class_2350[]{class_2350.field_11035, class_2350.field_11036};
      Wy[class_2350.field_11034.ordinal()] = new class_2350[]{class_2350.field_11036, class_2350.field_11035};
   }

   private enum e {
      q,
      D;
   }

   private record j(long at, boolean added) {
   }

   private enum p {
      A,
      m,
      b,
      Q,
      k,
      s,
      f;
   }
}
