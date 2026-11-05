package com.larpclient.module;

import com.larpclient._7;
import com.larpclient.cd;
import com.larpclient.e9;
import com.larpclient.h;
import com.larpclient.ki;
import com.larpclient.kx;
import com.larpclient.m5;
import com.larpclient.mt;
import com.larpclient.ou;
import com.larpclient.ov;
import com.larpclient.ta;
import com.larpclient.tj;
import com.larpclient.ty;
import com.larpclient.w8;
import com.larpclient.wu;
import com.larpclient.ww;
import com.larpclient.xh;
import com.larpclient.xy;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class KillFXModule extends Module {
   public static final int I = 4;
   public static final double f = 0.45;
   private static KillFXModule J;
   private final d6 h = this.addSetting(
      new d6(
         "Style",
         "Choose a finisher or vary each kill",
         "Random",
         "Random",
         "Execution",
         "Singularity",
         "Soul Pyre",
         "Star of David",
         "Tung Tung",
         "Tung Tung Floss",
         "Sakura",
         "Shatter"
      )
   );
   private final d6 N = this.addSetting(new d6("Colors", "Authored palettes, your theme or custom colors", "Style", "Style", "Theme", "Custom"));
   private final dk M = this.addSetting(new dk("Primary Color", "Main energy color", -10101505));
   private final dk d = this.addSetting(new dk("Secondary Color", "Trail and impact color", -32547));
   private final dh E = this.addSetting(new dh("Bloom", "Use the shared world bloom pass", true));
   private final dh B = this.addSetting(new dh("Sound", "Local windup and finishing cues", true));
   private final d9 x = this.addSetting(new d9("Volume", "Local sound volume", 0.55, 0.0, 1.0, 0.05));
   private final d9 v = this.addSetting(new d9("Render Distance", "Maximum distance in blocks", 48.0, 8.0, 64.0, 4.0));
   private final ou l = new ou();
   private final cd K = new cd();
   private final ArrayList<KillFXModule.m> U = new ArrayList<>();
   private List<KillFXModule.k> c = List.of();
   private Object t;
   private Object P;
   private int z = -1;
   private int a = -1;
   private long Q;
   private Vec3 j;
   private static final RenderStateDataKey<KillFXModule.p> q = RenderStateDataKey.create(() -> "Larp KillFX confirmed victim");

   public KillFXModule() {
      super("KillFX", "Play cinematic finishers when a player you damaged dies", Category.VISUALS, -1);
      J = this;
      this.M.H(() -> this.N.S("Custom"));
      this.d.H(() -> this.N.S("Custom"));
      this.x.H(() -> this.B.P());
      this.h.h(var1 -> {
         this.c();
         mt.K("KillFX", "Style changed: {}; active finishers cleared", var1);
      });
   }

   public static KillFXModule l() {
      return J;
   }

   public List<df<?>> q() {
      return List.of(this.h, this.N, this.M, this.d);
   }

   public df<?> d() {
      return this.h;
   }

   public List<df<?>> B() {
      return List.of(this.E, this.B, this.x);
   }

   public List<df<?>> F() {
      return List.of(this.v);
   }

   public String i() {
      return switch ((String)this.h.P()) {
         case "Execution" -> "Lifts the victim into a double helix, slices the body and drops the skinned pieces onto terrain.";
         case "Singularity" -> "Twists the body into a collapsing star vortex, then releases a sharp ring of light.";
         case "Soul Pyre" -> "Burns the skin away in glowing fragments while spiralling soul embers rise from the body.";
         case "Star of David" -> "Lifts the body and turns its six skinned parts into two interlocking triangles facing your view.";
         case "Tung Tung" -> "Summons a towering Tung Tung Sahur, who raises his wooden bat and slams the victim to the ground.";
         case "Tung Tung Floss" -> "Sahur crouches, jumps onto the fallen player and dances the Floss with swinging arms and counter-swaying hips.";
         case "Sakura" -> "A petal shockwave drops the victim, the body blooms into a swirling sphere under a pillar of light, then bursts and settles into a glowing yin-yang seal.";
         case "Shatter" -> "Frost climbs the frozen victim and cracks of light spread before the body shatters into ice shards and tumbling pieces that melt away.";
         default -> "Varies between all finishers without repeating the last animation.";
      };
   }

   @Override
   public void onEnable() {
      this.c();
      mt.K("KillFX", "Enabled: {}; server death events only, four playing finishers maximum", this.h.P());
   }

   @Override
   public void onDisable() {
      this.c();
   }

   public void c() {
      this.l.k();
      this.U.clear();
      this.c = List.of();
      this.K.O();
      this.t = this.P = null;
      this.j = null;
      this.z = -1;
   }

   private boolean y() {
      Minecraft var1 = getMc();
      if (this.S() && var1.level != null && var1.player != null) {
         if (this.t != var1.level || this.P != var1.player || this.z != m5.k || this.j != null && this.j.distanceToSqr(var1.player.position()) > 256.0) {
            this.c();
            this.t = var1.level;
            this.P = var1.player;
            this.z = m5.k;
         }

         this.j = var1.player.position();
         return true;
      } else {
         this.c();
         return false;
      }
   }

   public void h(ClientboundDamageEventPacket var1) {
      if (this.isEnabled()) {
         if (this.y()) {
            Minecraft var2 = getMc();
            Entity var3 = var2.level.getEntity(var1.entityId());
            if (var3 instanceof Player && var3 != var2.player) {
               this.l.f(var3, var1.sourceCauseId() == var2.player.getId(), ty.P());
            }
         }
      }
   }

   public void e(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (this.y()) {
            Minecraft var2 = getMc();
            if (var1.getEntity(var2.level) instanceof AbstractClientPlayer var4 && var4 != var2.player) {
               if (var1.getEventId() == 35) {
                  this.l.L(var4);
                  this.U.removeIf(var1x -> var1x.n == var4);
                  this.c = this.c.stream().filter(var1x -> var1x.owner.n != var4).toList();
               } else if (var1.getEventId() == 3 && this.l.E(var4, ty.P())) {
                  if (!(var4.position().distanceToSqr(var2.player.position()) > this.v.P() * this.v.P())) {
                     double var5 = ty.P();
                     ta.e var7 = HalloweenModule.E() ? ta.e.t : this.Q();
                     int var8 = var7 == ta.e.t ? -19633 : (var7 == ta.e.W ? -5144321 : -10101505);
                     int var9 = var7 == ta.e.t ? -8915729 : (var7 == ta.e.W ? -8328705 : -32547);
                     if (var7 == ta.e.q) {
                        var8 = -8474625;
                        var9 = -1444097;
                     }

                     if (var7 == ta.e.H || var7 == ta.e.d) {
                        var8 = -18594;
                        var9 = -5188;
                     }

                     if (var7 == ta.e.c) {
                        var8 = -22842;
                        var9 = -3337;
                     }

                     if (var7 == ta.e.S) {
                        var8 = -7938817;
                        var9 = -1114881;
                     }

                     if (this.N.S("Custom")) {
                        var8 = this.M.P();
                        var9 = this.d.P();
                     } else if (this.N.S("Theme")) {
                        var8 = com.larpclient.f.C;
                        var9 = com.larpclient.f.O;
                     }

                     long var10 = var4.getUUID().getMostSignificantBits() ^ var4.getUUID().getLeastSignificantBits() ^ ++this.Q;
                     double var12 = HalloweenModule.E()
                        ? Math.atan2(var2.player.getX() - var4.getX(), var2.player.getZ() - var4.getZ())
                        : Math.toRadians(var4.yBodyRot);
                     ta var14 = new ta(var7, var4.position(), var12, var5, var10, var4.getSkin().model() == PlayerModelType.SLIM);
                     List var15 = this.U.stream().filter(var0 -> !var0.z()).toList();
                     if (var15.size() >= 4) {
                        ((KillFXModule.m)var15.getFirst()).M(var5);
                        mt.U("KillFX", "Finisher limit reached; retiring the oldest ({}) over {}s", ((KillFXModule.m)var15.getFirst()).P.m, 0.45);
                     }

                     while (this.U.size() >= 8) {
                        this.U.removeFirst();
                     }

                     KillFXModule.m var16 = new KillFXModule.m(var4, var14, var8, var9, this.E.P(), this.B.P(), this.x.P());
                     var16.Z = HalloweenModule.E();
                     this.U.add(var16);
                     if (!var16.Z) {
                        this.n(
                           var16,
                           var7 == ta.e.t
                              ? SoundEvents.FIRECHARGE_USE
                              : (
                                 var7 == ta.e.c
                                    ? SoundEvents.CHERRY_LEAVES_BREAK
                                    : (
                                       var7 == ta.e.S
                                          ? SoundEvents.PLAYER_HURT_FREEZE
                                          : (
                                             var7 == ta.e.d
                                                ? SoundEvents.PLAYER_ATTACK_SWEEP
                                                : (var7 == ta.e.H ? SoundEvents.IRON_GOLEM_ATTACK : SoundEvents.AMETHYST_BLOCK_RESONATE)
                                          )
                                    )
                              ),
                           0.8F
                        );
                     }

                     mt.U(
                        "KillFX",
                        "Confirmed player death: id={}, style={}, source=recent observed server damage",
                        var4.getId(),
                        var16.Z ? "Soul Harvest" : var7
                     );
                  }
               }
            }
         }
      }
   }

   private ta.e Q() {
      if (!this.h.S("Random")) {
         return switch ((String)this.h.P()) {
            case "Execution" -> ta.e.f;
            case "Singularity" -> ta.e.W;
            case "Star of David" -> ta.e.q;
            case "Tung Tung" -> ta.e.H;
            case "Tung Tung Floss" -> ta.e.d;
            case "Sakura" -> ta.e.c;
            case "Shatter" -> ta.e.S;
            default -> ta.e.t;
         };
      } else {
         int var1 = ta.e.values().length;
         int var2 = Math.floorMod(this.Q * 31L + Double.doubleToLongBits(ty.P()), var1);
         if (var2 == this.a) {
            var2 = (var2 + 1) % var1;
         }

         this.a = var2;
         return ta.e.values()[var2];
      }
   }

   public void J() {
      if (this.isEnabled()) {
         if (this.y()) {
            Minecraft var1 = getMc();
            double var2 = ty.P();
            this.l.P(var2, var1x -> var1x instanceof Entity var2x && !var2x.isRemoved() && var2x.level() == var1.level);
            this.U
               .removeIf(
                  var3 -> (var3.Z ? var2 - var3.P.K >= 2.2 : var3.P.l(var2))
                     || var3.Z && !HalloweenModule.E()
                     || !var3.Z && !this.isRuntimeActive()
                     || var3.Z && var3.d && !var3.n.isRemoved() && var3.n.isAlive()
                     || var3.z() && var2 - var3.v >= 0.45
               );

            for (KillFXModule.m var5 : this.U) {
               if (!var5.Z && var5.d && !var5.n.isRemoved() && var5.n.isAlive() && !var5.z()) {
                  var5.M(var2);
                  mt.U("KillFX", "Victim id={} alive again; retiring its {} finisher", var5.n.getId(), var5.P.m);
               }

               var5.d = (boolean)(var5.d | (!var5.n.isAlive() ? 1 : 0));
               if (!var5.Z) {
                  var5.P.H(var2, (var1x, var2x) -> w8.G(var1.level, var1x, var2x));
                  KillFXModule.n[] var6 = X(var5.P.m);

                  while (var5.t < var6.length && var2 - var5.P.K >= var6[var5.t].time()) {
                     KillFXModule.n var7 = var6[var5.t++];
                     var5.L = (boolean)1;
                     if (!var5.z()) {
                        this.n(var5, var7.event(), var7.pitch());
                     }
                  }
               } else if (!var5.L && var2 - var5.P.K >= 0.78) {
                  var5.L = (boolean)1;
                  mt.U("Halloween", "Pumpkin smash impact: victim={}, age={}s", var5.n.getId(), var2 - var5.P.K);
               }
            }
         }
      }
   }

   private static KillFXModule.n[] X(ta.e var0) {
      return switch (var0) {
         case f -> new KillFXModule.n[]{new KillFXModule.n(0.85, (SoundEvent)SoundEvents.TRIDENT_THUNDER.value(), 1.25F)};
         case W -> new KillFXModule.n[]{new KillFXModule.n(1.9, (SoundEvent)SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), 1.25F)};
         case d -> new KillFXModule.n[]{new KillFXModule.n(1.62, SoundEvents.PLAYER_BIG_FALL, 1.25F)};
         case H -> new KillFXModule.n[]{new KillFXModule.n(1.56, SoundEvents.ANVIL_LAND, 1.25F)};
         case q -> new KillFXModule.n[]{new KillFXModule.n(1.85, SoundEvents.AMETHYST_BLOCK_RESONATE, 1.25F)};
         case t -> new KillFXModule.n[]{new KillFXModule.n(1.25, SoundEvents.BEACON_DEACTIVATE, 1.25F)};
         case c -> new KillFXModule.n[]{
            new KillFXModule.n(1.0, SoundEvents.BEACON_ACTIVATE, 1.4F),
            new KillFXModule.n(3.2, (SoundEvent)SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), 1.1F),
            new KillFXModule.n(5.0, SoundEvents.AMETHYST_BLOCK_CHIME, 0.9F)
         };
         case S -> new KillFXModule.n[]{
            new KillFXModule.n(0.95, SoundEvents.AMETHYST_BLOCK_CHIME, 1.6F),
            new KillFXModule.n(1.32, SoundEvents.GLASS_BREAK, 0.75F),
            new KillFXModule.n(1.87, SoundEvents.AMETHYST_CLUSTER_FALL, 1.3F)
         };
      };
   }

   private void n(KillFXModule.m var1, SoundEvent var2, float var3) {
      if (var1.o && !(var1.f <= 0.0) && getMc().level != null) {
         Vec3 var4 = var1.P.J;
         float var10006 = (float)var1.f;
         getMc().level.playLocalSound(var4.x, var4.y, var4.z, var2, SoundSource.PLAYERS, var10006, var3, false);
      }
   }

   public void P(CameraRenderState var1) {
      if (this.isEnabled()) {
         Vec3 var2 = var1.pos;
         this.c = List.of();
         if (this.y() && !this.U.isEmpty()) {
            Vector3f var3 = var1.orientation.transform(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var4 = var1.orientation.transform(new Vector3f(0.0F, 1.0F, 0.0F));
            Vec3 var5 = new Vec3(var3.x, var3.y, var3.z);
            Vec3 var6 = new Vec3(var4.x, var4.y, var4.z);
            this.K.F();
            ArrayList var7 = new ArrayList();

            for (KillFXModule.m var9 : this.U) {
               if (!(var9.P.J.distanceToSqr(var2) > this.v.P() * this.v.P())) {
                  if (var9.Z) {
                     if (HalloweenModule.E()) {
                        double var19 = ty.K() - var9.P.K;
                        Vec3 var20 = var9.P.J;
                        if (var1.cullFrustum == null
                           || var1.cullFrustum.isVisible(new AABB(var20.x - 3.5, var20.y, var20.z - 3.5, var20.x + 3.5, var20.y + 5.6, var20.z + 3.5))) {
                           wu.H();
                           if (var9.K == null) {
                              if (tj.s(var9.n, 1.0F) instanceof AvatarRenderState var23) {
                                 var9.K = var23;
                              } else {
                                 mt.B("Halloween", "Victim skin unavailable; keeping native corpse while the intact pumpkin and wave render", null);
                              }
                           }

                           ta.y[] var22 = var9.K == null ? null : ov.P(var9.P, var19);
                           var7.add(
                              new KillFXModule.k(
                                 var9, var9.K, var22, var19, var2, null, null, null, ov.s(var20, var9.P.v, var19, HalloweenModule.X().s()), this.z, 1.0
                              )
                           );
                        }
                     }
                  } else {
                     if (var9.K == null) {
                        if (!(tj.s(var9.n, 1.0F) instanceof AvatarRenderState var11)) {
                           mt.B("KillFX", "Native avatar snapshot unavailable; leaving original corpse visible", null);
                           continue;
                        }

                        var9.K = var11;
                     }

                     double var18 = Math.max(0.0, ty.K() - var9.P.K);
                     xy.b var12 = var9.P.m == ta.e.q ? xy.s(xy.c(var9.P, var18), var2, var5, var6) : xy.y;
                     ta.y[] var13 = new ta.y[6];
                     double var14 = var9.C(ty.K());

                     for (int var16 = 0; var16 < var13.length; var16++) {
                        ta.y var17 = var9.P.m(var16, var18, ty.g(), var12);
                        var13[var16] = var14 >= 1.0
                           ? var17
                           : new ta.y(
                              var17.position(),
                              var17.rx(),
                              var17.ry(),
                              var17.rz(),
                              var17.scale(),
                              var17.alpha() * var14,
                              var17.dissolve(),
                              var17.landed(),
                              var17.orientation(),
                              var17.stretch()
                           );
                     }

                     _7.l var24 = null;
                     e9.i var25 = null;
                     if (var9.P.m == ta.e.H) {
                        ww.l();
                        if (var9.X == null) {
                           var9.X = new _7(ww.r());
                           mt.U("KillFX", "Tung Tung ready: shared Sahur mesh, height={} blocks, articulated bat strike at {}s", 3.6, 1.56);
                        }

                        var24 = var9.X.S(var9.P, var18, var2);
                     }

                     if (var9.P.m == ta.e.d) {
                        ww.l();
                        if (var9.U == null) {
                           var9.U = new e9(ww.r());
                           mt.U("KillFX", "Tung Tung Floss ready: articulated jump, landing at {}s, four Floss cycles, expiry={}s", 1.62, 7.5);
                        }

                        var25 = var9.U.a(var9.P, var18, var2);
                     }

                     var7.add(new KillFXModule.k(var9, var9.K, var13, var18, var2, var12, var24, var25, null, this.z, var14));
                  }
               }
            }

            this.c = List.copyOf(var7);
         }
      }
   }

   public void f(Entity var1, EntityRenderState var2) {
      if (this.isEnabled()) {
         if (this.S() && var1.level() == this.t) {
            for (KillFXModule.m var4 : this.U) {
               if (var4.n == var1) {
                  var2.setData(q, new KillFXModule.p(var1, this.t, this.z));
                  return;
               }
            }
         }
      }
   }

   public boolean o(EntityRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.S() && com.larpclient.h.V() && com.larpclient.h.R("KillFX") && !tj.r()) {
         KillFXModule.p var2 = (KillFXModule.p)var1.getData(q);
         if (var2 != null && var2.world == getMc().level && var2.generation == m5.k) {
            for (KillFXModule.k var4 : this.c) {
               if (var4.skin != null
                  && var4.parts != null
                  && var4.owner.n == var2.victim
                  && this.U.contains(var4.owner)
                  && var4.generation == var2.generation
                  && (var4.age < 0.25 || var1 instanceof LivingEntityRenderState var5 && var5.deathTime > 0.0F)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean S() {
      return this.isRuntimeActive() || HalloweenModule.E();
   }

   public boolean U() {
      return !this.isEnabled()
         ? false
         : this.S() && this.c.stream().anyMatch(var0 -> var0.soul != null ? var0.soul.pumpkin() != null && HalloweenModule.X().s().O() : var0.owner.m);
   }

   public void X(ki var1, PoseStack var2, xh var3, boolean var4) {
      if (this.isEnabled()) {
         if (this.S()) {
            long var5 = kx.G();

            try {
               for (KillFXModule.k var8 : this.c) {
                  if (var8.soul != null) {
                     if (HalloweenModule.E() && var8.generation == m5.k && (!var4 || HalloweenModule.X().s().O())) {
                        if (!var4 && var8.skin != null && var8.parts != null) {
                           this.K.Z(var8.skin, var8.owner.P, var8.parts, var8.camera, var2, var3, -1, false);
                        }

                        ov.W(var1, var2, var3, var8.soul, var8.camera, var4);
                     }
                  } else if (var8.generation == m5.k && (!var4 || var8.owner.m)) {
                     this.K.Z(var8.skin, var8.owner.P, var8.parts, var8.camera, var2, var3, var8.owner.T, var4);
                     if (!var4 && var8.strike != null) {
                        ww.d(var2, var3, var8.strike.transform(), var8.strike.vertices(), var8.strike.colors(), var8.strike.opacity() * var8.fade);
                     }

                     if (!var4 && var8.floss != null) {
                        ww.d(var2, var3, var8.floss.transform(), var8.floss.vertices(), var8.floss.colors(), var8.floss.opacity() * var8.fade);
                     }

                     com.larpclient.i.S(
                        var1, var8.owner.P, var8.parts, var8.age, var8.owner.T, var8.owner.Q, var4, var8.fade, var8.starPlane, var8.strike, var8.floss
                     );
                  }
               }
            } finally {
               kx.u("KillFX", var5);
            }
         }
      }
   }

   private record k(
      KillFXModule.m owner,
      AvatarRenderState skin,
      ta.y[] parts,
      double age,
      Vec3 camera,
      xy.b starPlane,
      _7.l strike,
      e9.i floss,
      ov.q soul,
      int generation,
      double fade
   ) {
   }

   private static final class m {
      final AbstractClientPlayer n;
      final ta P;
      final int T;
      final int Q;
      final boolean m;
      final boolean o;
      final double f;
      AvatarRenderState K;
      _7 X;
      e9 U;
      boolean L;
      boolean d;
      boolean Z;
      int t;
      double v = Double.NaN;

      m(AbstractClientPlayer var1, ta var2, int var3, int var4, boolean var5, boolean var6, double var7) {
         this.n = var1;
         this.P = var2;
         this.T = var3;
         this.Q = var4;
         this.m = var5;
         this.o = var6;
         this.f = var7;
      }

      boolean z() {
         return !Double.isNaN(this.v);
      }

      void M(double var1) {
         if (!this.z()) {
            this.v = var1;
         }
      }

      double C(double var1) {
         return this.z() ? 1.0 - ta.k((var1 - this.v) / 0.45) : 1.0;
      }
   }

   private record n(double time, SoundEvent event, float pitch) {
   }

   private record p(Object victim, Object world, int generation) {
   }
}
