package com.larpclient.gui.widget;

import com.larpclient.LarpClient;
import com.larpclient.account.AccountVault;
import com.larpclient.account.MicrosoftAccounts;
import com.larpclient.config.ProxySettings;
import com.larpclient.gui.screen.HomeLayout;
import com.larpclient.gui.theme.Theme;
import com.larpclient.network.ProxyRouter;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.FormattedCharSequence;

public final class AccountSwitcherPanel {
   static final UUID nO_PROXY_ID = new UUID(0L, 0L);
   private static final int bUTTON_LINE_FORM = -2;
   private static final int fOOTER_PADDING = 20;
   private final Screen parent;
   private final MicrosoftAccounts accounts = MicrosoftAccounts.INSTANCE;
   private final Animation fade = new Animation(0.0);
   private final TextField tokenField;
   private final TextField proxyHostField;
   private final TextField proxyUsernameField;
   private final TextField proxyPasswordField;
   private HomeLayout.Rect anchor = new HomeLayout.Rect(8, 8, 120, 22);
   private AccountSwitcherPanel.Mode mode = AccountSwitcherPanel.Mode.ACCOUNTS;
   private ProxySettings.Type proxyType = ProxySettings.Type.SOCKS5;
   private boolean open;
   private boolean keyboardNav;
   private boolean confirmingArchive;
   private int scroll;
   private int selectedRow;
   private int selectedButton;
   private int focusedIndex;
   private int lastHostLength;
   private int lastMouseX = Integer.MIN_VALUE;
   private int lastMouseY;
   private String formError = "";
   private UUID pendingRemoveId;
   private User openingUser;

   public AccountSwitcherPanel(Screen var1) {
      this.parent = var1;
      this.tokenField = new TextField("Paste access token", var1x -> this.formError = "", 8192).masked().onSubmit(this::setAnchor);
      this.proxyHostField = new TextField("Proxy host:port", this::onHostChanged, 300).onSubmit(this::saveProxy);
      this.proxyUsernameField = new TextField("Username (optional)", var1x -> this.formError = "", 255).onSubmit(this::saveProxy);
      this.proxyPasswordField = new TextField("Password (optional)", var1x -> this.formError = "", 255).masked().onSubmit(this::saveProxy);
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setAnchor(HomeLayout.Rect var1) {
      this.anchor = var1;
   }

   public void toggle() {
      if (this.open) {
         this.close();
      } else {
         this.open = (boolean)1;
         this.confirmingArchive = (boolean)0;
         this.pendingRemoveId = null;
         this.keyboardNav = (boolean)0;
         this.selectedRow = this.selectedButton = 0;
         this.mode = AccountSwitcherPanel.Mode.ACCOUNTS;
         this.resetFields();
         this.openingUser = Minecraft.getInstance().getUser();
         this.fade.snapTo(0.0);
         this.accounts.open(this.parent);
      }
   }

   public void close() {
      if (this.open) {
         this.accounts.cancel(this.parent);
      }

      this.open = (boolean)0;
      this.confirmingArchive = (boolean)0;
      this.pendingRemoveId = null;
      this.mode = AccountSwitcherPanel.Mode.ACCOUNTS;
      this.resetFields();
   }

   public static List<AccountSwitcherPanel.AccountRow> buildAccountRows(User var0, User var1, List<AccountVault.Entry> var2, boolean var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      AccountVault.Entry var5 = var2.stream().filter(var1x -> var1x.id().equals(var0.getProfileId())).findFirst().orElse(null);
      var4.put(
         var0.getProfileId(),
         new AccountSwitcherPanel.AccountRow(var0.getProfileId(), var0.getName(), true, var5 != null, false, var5 == null ? "" : currentRows(var5))
      );

      for (AccountVault.Entry var7 : var2) {
         var4.putIfAbsent(var7.id(), new AccountSwitcherPanel.AccountRow(var7.id(), var7.name(), false, true, false, currentRows(var7)));
      }

      if (var3 && var1 != null) {
         var4.putIfAbsent(var1.getProfileId(), new AccountSwitcherPanel.AccountRow(var1.getProfileId(), var1.getName(), false, false, true, ""));
      }

      return List.copyOf(var4.values());
   }

   public static List<AccountSwitcherPanel.AccountRow> buildProxyRows(List<ProxySettings> var0, UUID var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new AccountSwitcherPanel.AccountRow(nO_PROXY_ID, "No proxy", var1 == null, false, false, "Direct"));

      for (ProxySettings var4 : var0) {
         var2.add(
            new AccountSwitcherPanel.AccountRow(var4.proxyId(), var4.label(), var4.proxyId().equals(var1), true, false, var4.authenticated() ? "Login" : "")
         );
      }

      return List.copyOf(var2);
   }

   private static String currentRows(AccountVault.Entry var0) {
      if (!var0.token()) {
         return "";
      }

      Instant var1 = var0.tokenExpiry();
      return var1 != null && var1.isBefore(Instant.now()) ? "Expired" : "Token";
   }

   private List<AccountSwitcherPanel.AccountRow> currentRows() {
      if (this.mode == AccountSwitcherPanel.Mode.PROXIES) {
         return buildProxyRows(this.accounts.proxies(), this.accounts.activeProxyId());
      }

      Minecraft var1 = Minecraft.getInstance();
      return buildAccountRows(var1.getUser(), this.accounts.launcher(), this.accounts.entries(), this.accounts.canReturn(var1));
   }

   private AccountSwitcherPanel.View currentView() {
      if (this.confirmingArchive) {
         return AccountSwitcherPanel.View.CONFIRM_ARCHIVE;
      }

      if (this.accounts.busy()) {
         return this.accounts.code().isBlank() ? AccountSwitcherPanel.View.LOADING : AccountSwitcherPanel.View.DEVICE_CODE;
      }

      if (!this.accounts.unlocked()) {
         return AccountSwitcherPanel.View.LOCKED;
      }

      return switch (this.mode) {
         case ACCOUNTS -> AccountSwitcherPanel.View.ACCOUNT_LIST;
         case TOKEN -> AccountSwitcherPanel.View.TOKEN_FORM;
         case PROXIES -> AccountSwitcherPanel.View.PROXY_LIST;
         case ADD_PROXY -> AccountSwitcherPanel.View.PROXY_FORM;
      };
   }

   private static boolean isListView(AccountSwitcherPanel.View var0) {
      return var0 == AccountSwitcherPanel.View.ACCOUNT_LIST || var0 == AccountSwitcherPanel.View.PROXY_LIST;
   }

   private static boolean isFormView(AccountSwitcherPanel.View var0) {
      return var0 == AccountSwitcherPanel.View.TOKEN_FORM || var0 == AccountSwitcherPanel.View.PROXY_FORM;
   }

   public static AccountSwitcherPanel.PanelLayout computeLayout(
      int var0, int var1, HomeLayout.Rect var2, int var3, int var4, AccountSwitcherPanel.View var5, boolean var6
   ) {
      int var7 = Math.max(1, Math.min(280, var0 - 16));
      int var8 = isListView(var5) && var6 ? 28 : 0;

      int var9 = switch (var5) {
         case ACCOUNT_LIST, PROXY_LIST -> Math.min(6, Math.max(1, var3)) * 32 + 46 + var8;
         case LOADING -> 94;
         case DEVICE_CODE -> 166;
         case LOCKED -> 150;
         case CONFIRM_ARCHIVE -> 124;
         case TOKEN_FORM -> 158;
         case PROXY_FORM -> 200;
      };
      int var10 = Math.clamp(var2.left() + var2.width() / 2 - var7 / 2, 8, Math.max(8, var0 - var7 - 8));
      int var11 = var2.top() + var2.height() + 4;
      int var12 = isListView(var5) ? 78 + var8 : var9;
      int var13 = var1 - 8 - var11;
      int var14 = var2.top() - 12;
      int var15 = var13 < var12 && var14 > var13 ? 1 : 0;
      int var16 = var15 != 0 ? var14 : var13;
      int var17 = Math.max(1, Math.min(Math.min(var9, Math.max(var12, var16)), var1 - 16));
      int var18 = var15 != 0 ? var2.top() - var17 - 4 : var11;
      int var19 = Math.clamp(var18, 8, Math.max(8, var1 - var17 - 8));
      int var20 = Math.max(1, (var17 - 46 - var8) / 32);
      return new AccountSwitcherPanel.PanelLayout(
         new HomeLayout.Rect(var10, var19, var7, var17),
         new HomeLayout.Rect(var10 + 6, var19 + var17 - 34, var7 - 12, 28),
         var20,
         Math.clamp(var4, 0, Math.max(0, var3 - var20)),
         var8
      );
   }

   public static HomeLayout.Rect isListView(AccountSwitcherPanel.PanelLayout var0, AccountSwitcherPanel.View var1, int var2) {
      HomeLayout.Rect var3 = var0.panel();
      int var4 = var1 == AccountSwitcherPanel.View.PROXY_FORM ? 4 : 1;
      int var5 = var3.top() + (var1 == AccountSwitcherPanel.View.TOKEN_FORM ? 50 : 26);
      int var6 = var0.footer().top() - 4 - 20;
      int var7 = Math.max(12, Math.min(26, (var6 - var5) / var4));
      return new HomeLayout.Rect(var3.left() + 10, var5 + var2 * var7, var3.width() - 20, Math.max(10, Math.min(20, var7 - 4)));
   }

   private AccountSwitcherPanel.PanelLayout layoutFor(AccountSwitcherPanel.View var1, int var2) {
      return computeLayout(this.parent.width, this.parent.height, this.anchor, var2, this.scroll, var1, !this.buildButtons(var1).isBlank());
   }

   private static boolean contains(HomeLayout.Rect var0, double var1, double var3) {
      return var1 >= var0.left() && var3 >= var0.top() && var1 < var0.left() + var0.width() && var3 < var0.top() + var0.height();
   }

   private String buildButtons(AccountSwitcherPanel.View var1) {
      if (isFormView(var1)) {
         return !this.formError.isEmpty() ? this.formError : (this.accounts.error() ? this.accounts.status() : "");
      } else {
         return var1 == AccountSwitcherPanel.View.PROXY_LIST && this.accounts.status().isBlank() && ProxyRouter.getBlockedReason() != null
            ? ProxyRouter.getBlockedReason()
            : this.accounts.status();
      }
   }

   private boolean isErrorStatus(AccountSwitcherPanel.View var1) {
      if (isFormView(var1)) {
         return true;
      } else {
         return var1 == AccountSwitcherPanel.View.PROXY_LIST && this.accounts.status().isBlank()
            ? ProxyRouter.getBlockedReason() != null
            : this.accounts.error();
      }
   }

   private static boolean isProxyConfigured() {
      return ProxyRouter.getActiveProxy() != null || ProxyRouter.getBlockedReason() != null;
   }

   private static String proxyLabel() {
      return ProxyRouter.getActiveProxy() != null ? "Proxy: On" : (ProxyRouter.getBlockedReason() != null ? "Proxy: Paused" : "Proxy: Off");
   }

   private int fieldsFor(AccountSwitcherPanel.PanelLayout var1, int var2, int var3, int var4) {
      for (int var5 = var1.scroll(); var5 < Math.min(var2, var1.scroll() + var1.capacity()); var5++) {
         if (contains(var1.row(var5), var3, var4)) {
            return var5;
         }
      }

      return -1;
   }

   private static List<HomeLayout.Rect> splitColumns(HomeLayout.Rect var0, int var1) {
      int var2 = (var0.width() - 4 * (var1 - 1)) / var1;
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1; var4++) {
         var3.add(
            new HomeLayout.Rect(
               var0.left() + var4 * (var2 + 4),
               var0.top(),
               var4 == var1 - 1 ? var0.left() + var0.width() - (var0.left() + var4 * (var2 + 4)) : var2,
               var0.height()
            )
         );
      }

      return var3;
   }

   private List<AccountSwitcherPanel.PanelButton> buildButtons(
      AccountSwitcherPanel.PanelLayout var1, AccountSwitcherPanel.View var2, List<AccountSwitcherPanel.AccountRow> var3, int var4
   ) {
      ArrayList var5 = new ArrayList();
      HomeLayout.Rect var6 = var1.footer();
      HomeLayout.Rect var7 = new HomeLayout.Rect(var6.left(), var6.top() - 30, var6.width(), 24);
      switch (var2) {
         case ACCOUNT_LIST:
         case PROXY_LIST:
            int var17 = var2 == AccountSwitcherPanel.View.PROXY_LIST ? 1 : 0;
            if (var4 >= var1.scroll() && var4 < Math.min(var3.size(), var1.scroll() + var1.capacity())) {
               AccountSwitcherPanel.AccountRow var20 = (AccountSwitcherPanel.AccountRow)var3.get(var4);
               HomeLayout.Rect var23 = var1.row(var4);
               int var24 = var23.left() + var23.width() - 4;
               if (var20.accountId().equals(this.pendingRemoveId)) {
                  var5.add(new AccountSwitcherPanel.PanelButton(new HomeLayout.Rect(var24 - 124, var23.top() + 4, 64, 24), "Remove?", () -> {
                     this.pendingRemoveId = null;
                     if (var17) {
                        this.accounts.removeProxy(this.parent, var20.accountId());
                     } else {
                        this.accounts.remove(this.parent, var20.accountId());
                     }
                  }, true, false, var4));
                  var5.add(
                     new AccountSwitcherPanel.PanelButton(
                        new HomeLayout.Rect(var24 - 56, var23.top() + 4, 56, 24), "Cancel", () -> this.pendingRemoveId = null, false, false, var4
                     )
                  );
               } else {
                  if (!var20.current()) {
                     var5.add(
                        new AccountSwitcherPanel.PanelButton(
                           new HomeLayout.Rect(var24 - (var20.saved() ? 124 : 56), var23.top() + 4, 56, 24), var17 != 0 ? "Use" : "Switch", () -> {
                              if (var17) {
                                 if (var20.accountId().equals(nO_PROXY_ID)) {
                                    this.accounts.proxyOff(this.parent);
                                 } else {
                                    this.accounts.useProxy(this.parent, var20.accountId());
                                 }
                              } else if (var20.launcher()) {
                                 this.accounts.returnToLauncher(this.parent);
                              } else {
                                 this.accounts.switchTo(this.parent, var20.accountId());
                              }
                           }, false, false, var4
                        )
                     );
                  }

                  if (var20.saved()) {
                     var5.add(
                        new AccountSwitcherPanel.PanelButton(
                           new HomeLayout.Rect(var24 - 64, var23.top() + 4, 64, 24),
                           "Remove",
                           () -> this.pendingRemoveId = var20.accountId(),
                           true,
                           false,
                           var4
                        )
                     );
                  }
               }
            }

            if (var17 != 0) {
               List var21 = splitColumns(var6, 2);
               var5.add(
                  new AccountSwitcherPanel.PanelButton(
                     (HomeLayout.Rect)var21.get(0), "+ Add proxy", () -> this.setMode(AccountSwitcherPanel.Mode.ADD_PROXY), false, false, var3.size()
                  )
               );
               var5.add(
                  new AccountSwitcherPanel.PanelButton(
                     (HomeLayout.Rect)var21.get(1), "Back", () -> this.setMode(AccountSwitcherPanel.Mode.ACCOUNTS), false, false, var3.size()
                  )
               );
            } else {
               List var22 = splitColumns(var6, 3);
               var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var22.get(0), "+ Microsoft", () -> {
                  this.pendingRemoveId = null;
                  this.accounts.add(this.parent);
               }, false, false, var3.size()));
               var5.add(
                  new AccountSwitcherPanel.PanelButton(
                     (HomeLayout.Rect)var22.get(1), "+ Token", () -> this.setMode(AccountSwitcherPanel.Mode.TOKEN), false, false, var3.size()
                  )
               );
               var5.add(
                  new AccountSwitcherPanel.PanelButton(
                     (HomeLayout.Rect)var22.get(2),
                     proxyLabel(),
                     () -> this.setMode(AccountSwitcherPanel.Mode.PROXIES),
                     false,
                     isProxyConfigured(),
                     var3.size()
                  )
               );
            }
            break;
         case LOADING:
            var5.add(new AccountSwitcherPanel.PanelButton(var6, "Cancel", () -> this.accounts.cancel(this.parent), false, false, 0));
            break;
         case DEVICE_CODE:
            List var16 = splitColumns(var7, 2);
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var16.get(0), "Open Microsoft", this.accounts::openBrowser, false, false, 0));
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var16.get(1), "Copy code", this.accounts::copyCode, false, false, 0));
            var5.add(new AccountSwitcherPanel.PanelButton(var6, "Cancel", () -> this.accounts.cancel(this.parent), false, false, 0));
            break;
         case LOCKED:
            List var15 = splitColumns(var7, 2);
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var15.get(0), "Retry", () -> this.accounts.open(this.parent), false, false, 0));
            if (this.accounts.exists()) {
               var5.add(
                  new AccountSwitcherPanel.PanelButton(
                     (HomeLayout.Rect)var15.get(1), "Start new list", () -> this.confirmingArchive = (boolean)1, false, false, 0
                  )
               );
            }

            if (isProxyConfigured()) {
               List var19 = splitColumns(var6, 2);
               var5.add(
                  new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var19.get(0), "Proxy off", () -> this.accounts.proxyOff(this.parent), false, false, 0)
               );
               var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var19.get(1), "Close", this::close, false, false, 0));
            } else {
               var5.add(new AccountSwitcherPanel.PanelButton(var6, "Close", this::close, false, false, 0));
            }
            break;
         case CONFIRM_ARCHIVE:
            List var14 = splitColumns(var6, 2);
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var14.get(0), "Start new list", () -> {
               this.confirmingArchive = (boolean)0;
               this.accounts.archive(this.parent);
            }, true, false, 0));
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var14.get(1), "Cancel", () -> this.confirmingArchive = (boolean)0, false, false, 0));
            break;
         case TOKEN_FORM:
            List var13 = splitColumns(var6, 3);
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var13.get(0), "Paste", this::pasteToken, false, false, 0));
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var13.get(1), "Sign in", this::setAnchor, false, false, 0));
            var5.add(
               new AccountSwitcherPanel.PanelButton(
                  (HomeLayout.Rect)var13.get(2), "Cancel", () -> this.setMode(AccountSwitcherPanel.Mode.ACCOUNTS), false, false, 0
               )
            );
            break;
         case PROXY_FORM:
            List var8 = splitColumns(isListView(var1, var2, 0), 3);

            for (ProxySettings.Type var12 : ProxySettings.Type.values()) {
               var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var8.get(var12.ordinal()), var12.LABEL, () -> {
                  this.proxyType = var12;
                  this.formError = "";
               }, false, var12 == this.proxyType, -2));
            }

            List var18 = splitColumns(var6, 2);
            var5.add(new AccountSwitcherPanel.PanelButton((HomeLayout.Rect)var18.get(0), "Save and use", this::saveProxy, false, false, 0));
            var5.add(
               new AccountSwitcherPanel.PanelButton(
                  (HomeLayout.Rect)var18.get(1), "Cancel", () -> this.setMode(AccountSwitcherPanel.Mode.PROXIES), false, false, 0
               )
            );
      }

      return var5;
   }

   private List<TextField> fieldsFor(AccountSwitcherPanel.View var1) {
      return var1 == AccountSwitcherPanel.View.TOKEN_FORM
         ? List.of(this.tokenField)
         : (var1 == AccountSwitcherPanel.View.PROXY_FORM ? List.of(this.proxyHostField, this.proxyUsernameField, this.proxyPasswordField) : List.of());
   }

   private List<Object> focusOrder(AccountSwitcherPanel.View var1, List<AccountSwitcherPanel.PanelButton> var2) {
      ArrayList var3 = new ArrayList();

      for (AccountSwitcherPanel.PanelButton var5 : var2) {
         if (var5.line() == -2) {
            var3.add(var5);
         }
      }

      var3.addAll(this.fieldsFor(var1));

      for (AccountSwitcherPanel.PanelButton var7 : var2) {
         if (var7.line() != -2) {
            var3.add(var7);
         }
      }

      return var3;
   }

   private void layoutFields(AccountSwitcherPanel.PanelLayout var1, AccountSwitcherPanel.View var2) {
      List var3 = this.fieldsFor(var2);

      for (int var4 = 0; var4 < var3.size(); var4++) {
         HomeLayout.Rect var5 = isListView(var1, var2, var2 == AccountSwitcherPanel.View.PROXY_FORM ? var4 + 1 : var4);
         ((TextField)var3.get(var4)).bounds(var5.left(), var5.top(), var5.width());
         ((TextField)var3.get(var4)).height = var5.height();
      }
   }

   private void updateFieldFocus(List<Object> var1) {
      for (TextField var3 : List.of(this.tokenField, this.proxyHostField, this.proxyUsernameField, this.proxyPasswordField)) {
         var3.focused = (boolean)(this.focusedIndex >= 0 && this.focusedIndex < var1.size() && var1.get(this.focusedIndex) == var3 ? 1 : 0);
      }
   }

   private void setMode(AccountSwitcherPanel.Mode var1) {
      this.mode = var1;
      this.pendingRemoveId = null;
      this.scroll = 0;
      this.selectedRow = 0;
      this.selectedButton = 0;
      this.formError = "";
      this.accounts.clearMessage();
      if (var1 == AccountSwitcherPanel.Mode.ACCOUNTS || var1 == AccountSwitcherPanel.Mode.PROXIES) {
         this.resetFields();
      }

      AccountSwitcherPanel.View var2 = this.currentView();
      if (isFormView(var2)) {
         List var3 = this.focusOrder(var2, this.buildButtons(this.layoutFor(var2, 0), var2, List.of(), -1));
         this.focusedIndex = var3.indexOf(this.fieldsFor(var2).getFirst());
         this.updateFieldFocus(var3);
      }
   }

   private void resetFields() {
      for (TextField var2 : List.of(this.tokenField, this.proxyHostField, this.proxyUsernameField, this.proxyPasswordField)) {
         var2.text = "";
         var2.blur();
      }

      this.lastHostLength = 0;
      this.focusedIndex = 0;
      this.formError = "";
      this.proxyType = ProxySettings.Type.SOCKS5;
   }

   private void setAnchor() {
      if (this.currentView() == AccountSwitcherPanel.View.TOKEN_FORM) {
         this.formError = "";
         this.accounts.addToken(this.parent, this.tokenField.text, () -> {
            this.tokenField.text = "";
            this.setMode(AccountSwitcherPanel.Mode.ACCOUNTS);
         });
      }
   }

   private void pasteToken() {
      String var1 = Minecraft.getInstance().keyboardHandler.getClipboard();
      this.tokenField.text = "";
      this.tokenField.focused = (boolean)1;
      this.tokenField.setSuggestionProvider(var1);
      AccountSwitcherPanel.View var2 = this.currentView();
      List var3 = this.focusOrder(var2, this.buildButtons(this.layoutFor(var2, 0), var2, List.of(), -1));
      this.focusedIndex = var3.indexOf(this.tokenField);
      this.updateFieldFocus(var3);
   }

   private void saveProxy() {
      if (this.currentView() == AccountSwitcherPanel.View.PROXY_FORM) {
         ProxySettings var1;
         try {
            var1 = ProxySettings.parse(this.proxyType, this.proxyHostField.text, this.proxyUsernameField.text, this.proxyPasswordField.text);
         } catch (IllegalArgumentException var3) {
            this.formError = var3.getMessage();
            return;
         }

         this.formError = "";
         this.accounts.saveProxy(this.parent, var1, () -> this.setMode(AccountSwitcherPanel.Mode.PROXIES));
      }
   }

   private void onHostChanged(String var1) {
      this.formError = "";
      int var2 = this.lastHostLength;
      this.lastHostLength = var1.length();
      if (var1.length() - var2 > 1) {
         ProxySettings.ParsedProxy var3 = ProxySettings.split(var1, this.proxyType);
         if (var3 != null) {
            this.proxyType = var3.type();
            this.proxyHostField.text = var3.address();
            this.lastHostLength = this.proxyHostField.text.length();
            if (!var3.username().isEmpty() || !var3.password().isEmpty()) {
               this.proxyUsernameField.text = var3.username();
               this.proxyPasswordField.text = var3.password();
            }
         }
      }
   }

   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.open) {
         if (!this.accounts.busy() && Minecraft.getInstance().getUser() != this.openingUser) {
            this.close();
         } else {
            AccountSwitcherPanel.View var4 = this.currentView();
            List var5 = this.currentRows();
            AccountSwitcherPanel.PanelLayout var6 = this.layoutFor(var4, var5.size());
            this.scroll = var6.scroll();
            if (var2 != this.lastMouseX || var3 != this.lastMouseY) {
               this.keyboardNav = (boolean)0;
               this.lastMouseX = var2;
               this.lastMouseY = var3;
            }

            boolean var7 = isListView(var4);
            int var8 = !var7 ? -1 : (this.keyboardNav ? this.selectedRow : this.fieldsFor(var6, var5.size(), var2, var3));
            if (var7 && this.pendingRemoveId != null) {
               for (int var9 = 0; var9 < var5.size(); var9++) {
                  if (((AccountSwitcherPanel.AccountRow)var5.get(var9)).accountId().equals(this.pendingRemoveId)) {
                     var8 = var9;
                  }
               }
            }

            List var34 = this.buildButtons(var6, var4, var5, var8);
            List var10 = isFormView(var4) ? this.focusOrder(var4, var34) : List.of();
            boolean var11 = LarpClient.getInstance().getConfigManager().getPreferences().motion;
            double var12 = var11 ? this.fade.animateTo(1.0, 140) : 1.0;
            HomeLayout.Rect var14 = var6.panel();
            String var15 = this.buildButtons(var4);
            var1.nextStratum();
            GlassPainter.fillRoundedRect(var1, var14.left(), var14.top(), var14.width(), var14.height(), 5, ColorUtil.multiplyAlpha(-99347436, var12));
            GlassPainter.drawBorder(var1, var14.left(), var14.top(), var14.width(), var14.height(), 5, ColorUtil.multiplyAlpha(-13290187, var12));
            var1.enableScissor(var14.left(), var14.top(), var14.left() + var14.width(), var14.top() + var14.height());

            try {
               if (var7) {
                  for (int var35 = this.scroll; var35 < Math.min(var5.size(), this.scroll + var6.capacity()); var35++) {
                     AccountSwitcherPanel.AccountRow var38 = (AccountSwitcherPanel.AccountRow)var5.get(var35);
                     HomeLayout.Rect var41 = var6.row(var35);
                     int var19 = var35 == var8 ? 1 : 0;
                     int var20 = var35;
                     int var21 = var19 != 0 && var34.stream().anyMatch(var1x -> var1x.line() == var20) ? 1 : 0;
                     if (var19 != 0) {
                        GlassPainter.fillRoundedRect(
                           var1, var41.left(), var41.top(), var41.width(), var41.height(), 3, ColorUtil.multiplyAlpha(-14342875, var12)
                        );
                     }

                     String var22 = var38.current() ? (var4 == AccountSwitcherPanel.View.PROXY_LIST ? "Active" : "Current") : var38.note();
                     int var23 = !var22.isEmpty() && var21 == 0 ? FontRenderer.getDefaultWidth(var22) + 18 : 14;
                     if (var21 != 0) {
                        for (AccountSwitcherPanel.PanelButton var25 : var34) {
                           if (var25.line() == var35) {
                              var23 = Math.max(var23, var41.left() + var41.width() - var25.box().left() + 8);
                           }
                        }
                     }

                     FontRenderer.drawTextRaw(
                        var1,
                        FontRenderer.drawTextWithFace(var38.name(), var41.width() - var23),
                        var41.left() + 8,
                        var41.top() + 11,
                        ColorUtil.multiplyAlpha(var38.current() ? Theme.textPrimary : Theme.textSecondary, var12),
                        var38.current() ? FontRenderer.Face.MEDIUM : FontRenderer.Face.REGULAR
                     );
                     if (!var22.isEmpty() && var21 == 0) {
                        FontRenderer.drawDefaultText(
                           var1,
                           var22,
                           var41.left() + var41.width() - 8 - FontRenderer.getDefaultWidth(var22),
                           var41.top() + 11,
                           ColorUtil.multiplyAlpha("Expired".equals(var22) ? -24915 : Theme.textMuted, var12)
                        );
                     }
                  }

                  if (var5.size() > var6.capacity()) {
                     int var36 = var6.capacity() * 32;
                     int var39 = Math.max(8, var36 * var6.capacity() / var5.size());
                     int var42 = (var36 - var39) * this.scroll / (var5.size() - var6.capacity());
                     DrawUtil.fillRect(
                        var1, var14.left() + var14.width() - 3, var14.top() + 6 + var42, 1, var39, ColorUtil.multiplyAlpha(Theme.textSecondary, var12)
                     );
                  }

                  if (var6.statusHeight() > 0) {
                     focusOrder(
                        var1,
                        var15,
                        var14.left() + 10,
                        var6.footer().top() - var6.statusHeight() + 1,
                        var14.width() - 20,
                        2,
                        this.isErrorStatus(var4) ? -24915 : Theme.textMuted,
                        var12
                     );
                  }

                  DrawUtil.fillRect(var1, var14.left() + 6, var6.footer().top() - 3, var14.width() - 12, 1, ColorUtil.multiplyAlpha(-13619152, var12));
               } else if (!isFormView(var4)) {
                  if (var4 == AccountSwitcherPanel.View.DEVICE_CODE) {
                     focusOrder(var1, "Microsoft sign-in", var14.left() + 10, var14.top() + 12, var14.width() - 20, 1, Theme.textPrimary, var12);
                     focusOrder(var1, "microsoft.com/link", var14.left() + 10, var14.top() + 30, var14.width() - 20, 1, Theme.textSecondary, var12);
                     FontRenderer.drawCenteredText(
                        var1, this.accounts.code(), var14.left() + var14.width() / 2, var14.top() + 49, ColorUtil.multiplyAlpha(Theme.textPrimary, var12)
                     );
                     focusOrder(
                        var1, "Approve only the code shown here.", var14.left() + 10, var14.top() + 69, var14.width() - 20, 2, Theme.textSecondary, var12
                     );
                  } else if (var4 == AccountSwitcherPanel.View.CONFIRM_ARCHIVE) {
                     focusOrder(var1, "Start a new saved list?", var14.left() + 10, var14.top() + 12, var14.width() - 20, 1, Theme.textPrimary, var12);
                     focusOrder(
                        var1,
                        "The old encrypted file will be kept for recovery. Add your accounts again.",
                        var14.left() + 10,
                        var14.top() + 34,
                        var14.width() - 20,
                        3,
                        Theme.textSecondary,
                        var12
                     );
                  } else {
                     focusOrder(
                        var1,
                        this.accounts.status().isBlank() ? "Loading saved accounts..." : this.accounts.status(),
                        var14.left() + 10,
                        var14.top() + 12,
                        var14.width() - 20,
                        Math.max(1, (var6.footer().top() - var14.top() - (var4 == AccountSwitcherPanel.View.LOCKED ? 46 : 18)) / 10),
                        this.accounts.error() ? -24915 : Theme.textSecondary,
                        var12
                     );
                  }
               } else {
                  this.layoutFields(var6, var4);
                  int var16 = var4 == AccountSwitcherPanel.View.TOKEN_FORM ? 1 : 0;
                  focusOrder(
                     var1,
                     var16 != 0 ? "Sign in with a token" : "Add a proxy for server traffic",
                     var14.left() + 10,
                     var14.top() + 10,
                     var14.width() - 20,
                     1,
                     Theme.textPrimary,
                     var12
                  );
                  if (var16 != 0) {
                     focusOrder(
                        var1,
                        "Paste a Minecraft access token. It is checked with Minecraft and saved encrypted until it expires.",
                        var14.left() + 10,
                        var14.top() + 24,
                        var14.width() - 20,
                        2,
                        Theme.textSecondary,
                        var12
                     );
                  }

                  this.updateFieldFocus(var10);

                  for (TextField var18 : this.fieldsFor(var4)) {
                     var18.render(var1, var2, var3);
                  }

                  if (!var15.isBlank()) {
                     focusOrder(
                        var1,
                        var15,
                        var14.left() + 10,
                        var6.footer().top() - 4 - 20,
                        var14.width() - 20,
                        2,
                        this.isErrorStatus(var4) ? -24915 : Theme.textMuted,
                        var12
                     );
                  }

                  DrawUtil.fillRect(var1, var14.left() + 6, var6.footer().top() - 3, var14.width() - 12, 1, ColorUtil.multiplyAlpha(-13619152, var12));
               }

               for (int var37 = 0; var37 < var34.size(); var37++) {
                  AccountSwitcherPanel.PanelButton var40 = (AccountSwitcherPanel.PanelButton)var34.get(var37);
                  HomeLayout.Rect var43 = var40.box();
                  int var44 = this.keyboardNav && this.isButtonFocused(var4, var34, var10, var37) ? 1 : 0;
                  int var45 = !this.keyboardNav && contains(var43, var2, var3) ? 1 : 0;
                  if (var40.selected() || var44 != 0 || var45 != 0) {
                     GlassPainter.fillRoundedRect(
                        var1,
                        var43.left(),
                        var43.top(),
                        var43.width(),
                        var43.height(),
                        3,
                        ColorUtil.multiplyAlpha(var40.selected() ? -13882324 : -13421773, var12)
                     );
                  }

                  if (var44 != 0) {
                     GlassPainter.drawBorder(
                        var1, var43.left(), var43.top(), var43.width(), var43.height(), 3, ColorUtil.multiplyAlpha(Theme.textSecondary, var12)
                     );
                  }

                  int var46 = FontRenderer.getDefaultWidth(var40.label());
                  float var47 = currentRows(var46, var43.width() - 12);
                  var1.pose().pushMatrix();

                  try {
                     var1.pose().translate(var43.left() + (var43.width() - var46 * var47) / 2.0F, var43.top() + (var43.height() - 9.0F * var47) / 2.0F);
                     var1.pose().scale(var47, var47);
                     FontRenderer.drawDefaultText(
                        var1,
                        var40.label(),
                        0,
                        0,
                        ColorUtil.multiplyAlpha(
                           var40.destructive() ? -24915 : (!var40.selected() && var45 == 0 && var44 == 0 ? Theme.textSecondary : Theme.textPrimary), var12
                        )
                     );
                  } finally {
                     var1.pose().popMatrix();
                  }
               }
            } finally {
               var1.disableScissor();
            }
         }
      }
   }

   private boolean isButtonFocused(AccountSwitcherPanel.View var1, List<AccountSwitcherPanel.PanelButton> var2, List<Object> var3, int var4) {
      if (!isFormView(var1)) {
         if (!isListView(var1)) {
            return this.selectedButton == var4;
         }

         int var5 = ((AccountSwitcherPanel.PanelButton)var2.get(var4)).line();
         int var6 = 0;
         if (var5 != this.selectedRow) {
            return false;
         }

         for (int var7 = 0; var7 < var4; var7++) {
            if (((AccountSwitcherPanel.PanelButton)var2.get(var7)).line() == var5) {
               var6++;
            }
         }

         return var6 == this.selectedButton;
      } else {
         return this.focusedIndex >= 0 && this.focusedIndex < var3.size() && var3.get(this.focusedIndex) == var2.get(var4);
      }
   }

   public static float currentRows(int var0, int var1) {
      return Math.min(1.0F, (float)Math.max(1, var1) / Math.max(1, var0));
   }

   private static void focusOrder(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      Minecraft var9 = Minecraft.getInstance();
      List var10 = var9.font.split(FontRenderer.toComponent(var1, FontRenderer.Face.REGULAR), var4);

      for (int var11 = 0; var11 < Math.min(var5, var10.size()); var11++) {
         var0.text(var9.font, (FormattedCharSequence)var10.get(var11), var2, var3 + var11 * 10, ColorUtil.multiplyAlpha(var6, var7), false);
      }
   }

   public boolean mouseClicked(MouseButtonEvent var1) {
      if (!this.open) {
         return false;
      }

      AccountSwitcherPanel.View var2 = this.currentView();
      List var3 = this.currentRows();
      AccountSwitcherPanel.PanelLayout var4 = this.layoutFor(var2, var3.size());
      if (!contains(var4.panel(), var1.x(), var1.y())) {
         this.close();
         return true;
      }

      if (KeyCodes.mouseButtonOf(var1) != 0) {
         return true;
      }

      this.keyboardNav = (boolean)0;
      boolean var5 = isListView(var2);
      int var6 = var5 ? this.fieldsFor(var4, var3.size(), (int)var1.x(), (int)var1.y()) : -1;
      if (var5 && this.pendingRemoveId != null) {
         for (int var7 = 0; var7 < var3.size(); var7++) {
            if (((AccountSwitcherPanel.AccountRow)var3.get(var7)).accountId().equals(this.pendingRemoveId)) {
               var6 = var7;
            }
         }
      }

      List var11 = this.buildButtons(var4, var2, var3, var6);

      for (AccountSwitcherPanel.PanelButton var9 : var11) {
         if (contains(var9.box(), var1.x(), var1.y())) {
            var9.action().run();
            return true;
         }
      }

      if (isFormView(var2)) {
         this.layoutFields(var4, var2);
         List var12 = this.focusOrder(var2, var11);

         for (TextField var10 : this.fieldsFor(var2)) {
            if (var10.contains(var1.x(), var1.y())) {
               this.focusedIndex = var12.indexOf(var10);
            }
         }

         this.updateFieldFocus(var12);
      }

      if (var6 >= 0) {
         this.selectedRow = var6;
         this.selectedButton = 0;
      }

      return true;
   }

   public boolean charTyped(CharacterEvent var1) {
      if (!this.open) {
         return false;
      }

      for (TextField var3 : this.fieldsFor(this.currentView())) {
         if (var3.focused) {
            var3.character(var1.codepoint());
         }
      }

      return true;
   }

   public boolean pasteToken(double var1, double var3, double var5) {
      if (!this.open) {
         return false;
      }

      AccountSwitcherPanel.View var7 = this.currentView();
      List var8 = this.currentRows();
      AccountSwitcherPanel.PanelLayout var9 = this.layoutFor(var7, var8.size());
      if (isListView(var7) && contains(var9.panel(), var1, var3) && var5 != 0.0) {
         this.scroll = Math.clamp(var9.scroll() + (var5 > 0.0 ? -1 : 1), 0, Math.max(0, var8.size() - var9.capacity()));
         this.pendingRemoveId = null;
      }

      return true;
   }

   public boolean keyPressed(KeyEvent var1) {
      if (!this.open) {
         return false;
      }

      int var2 = KeyCodes.keyOf(var1);
      AccountSwitcherPanel.View var3 = this.currentView();
      if (var2 == 256) {
         if (this.confirmingArchive) {
            this.confirmingArchive = (boolean)0;
         } else if (this.pendingRemoveId != null) {
            this.pendingRemoveId = null;
         } else if (var3 == AccountSwitcherPanel.View.TOKEN_FORM || var3 == AccountSwitcherPanel.View.PROXY_LIST) {
            this.setMode(AccountSwitcherPanel.Mode.ACCOUNTS);
         } else if (var3 == AccountSwitcherPanel.View.PROXY_FORM) {
            this.setMode(AccountSwitcherPanel.Mode.PROXIES);
         } else {
            this.close();
         }

         return true;
      } else {
         this.keyboardNav = (boolean)1;
         List var4 = this.currentRows();
         AccountSwitcherPanel.PanelLayout var5 = this.layoutFor(var3, var4.size());
         int var6 = (var1.modifiers() & 1) != 0 ? 1 : 0;
         int var7 = var2 != 257 && var2 != 335 && var2 != 32 ? 0 : 1;
         if (isListView(var3)) {
            this.selectedRow = Math.clamp(this.selectedRow, 0, var4.size());
            int var14 = var4.size() + 1;
            int var15 = countLineButtons(this.buildButtons(var5, var3, var4, this.selectedRow), this.selectedRow);
            if (var2 == 264 || var2 == 265) {
               this.selectedRow = Math.floorMod(this.selectedRow + (var2 == 265 ? -1 : 1), var14);
               this.selectedButton = 0;
               this.pendingRemoveId = null;
            } else if (var2 == 258 && var6 == 0) {
               if (this.selectedButton < var15 - 1) {
                  this.selectedButton++;
               } else {
                  this.selectedRow = Math.floorMod(this.selectedRow + 1, var14);
                  this.selectedButton = 0;
                  this.pendingRemoveId = null;
               }
            } else if (var2 == 258) {
               if (this.selectedButton > 0) {
                  this.selectedButton--;
               } else {
                  this.selectedRow = Math.floorMod(this.selectedRow - 1, var14);
                  this.pendingRemoveId = null;
                  this.selectedButton = Math.max(0, countLineButtons(this.buildButtons(var5, var3, var4, this.selectedRow), this.selectedRow) - 1);
               }
            } else if (var2 == 268) {
               this.selectedRow = 0;
               this.selectedButton = 0;
            } else if (var2 == 269) {
               this.selectedRow = var4.size();
               this.selectedButton = 0;
            } else if (var2 == 263 || var2 == 262) {
               this.selectedButton = Math.clamp(this.selectedButton + (var2 == 262 ? 1 : -1), 0, Math.max(0, var15 - 1));
            }

            if (this.selectedRow < var4.size()) {
               this.scroll = Math.clamp(
                  this.selectedRow < this.scroll
                     ? this.selectedRow
                     : (this.selectedRow >= this.scroll + var5.capacity() ? this.selectedRow - var5.capacity() + 1 : this.scroll),
                  0,
                  Math.max(0, var4.size() - var5.capacity())
               );
            }

            if (var7 != 0) {
               var5 = this.layoutFor(var3, var4.size());
               int var16 = 0;

               for (AccountSwitcherPanel.PanelButton var12 : this.buildButtons(var5, var3, var4, this.selectedRow)) {
                  if (var12.line() == this.selectedRow && var16++ == this.selectedButton) {
                     var12.action().run();
                     break;
                  }
               }
            }

            return true;
         } else {
            List var8 = this.buildButtons(var5, var3, var4, -1);
            if (isFormView(var3)) {
               List var9 = this.focusOrder(var3, var8);
               this.focusedIndex = Math.clamp(this.focusedIndex, 0, var9.size() - 1);
               Object var10 = var9.get(this.focusedIndex);
               if (var2 == 258) {
                  this.focusedIndex = Math.floorMod(this.focusedIndex + (var6 != 0 ? -1 : 1), var9.size());
                  this.updateFieldFocus(var9);
                  return true;
               } else if (var10 instanceof TextField var17) {
                  var17.focused = (boolean)1;
                  var17.key(var2);
                  return true;
               } else {
                  if (var2 != 264 && var2 != 262 && var2 != 265 && var2 != 263) {
                     if (var7 != 0 && var10 instanceof AccountSwitcherPanel.PanelButton var11) {
                        var11.action().run();
                     }
                  } else {
                     this.focusedIndex = Math.floorMod(this.focusedIndex + (var2 != 265 && var2 != 263 ? 1 : -1), var9.size());
                     this.updateFieldFocus(var9);
                  }

                  return true;
               }
            } else {
               if (var2 == 258 || var2 == 264 || var2 == 265) {
                  this.selectedButton = Math.floorMod(this.selectedButton + (var2 != 265 && var6 == 0 ? 1 : -1), Math.max(1, var8.size()));
               }

               if (var7 != 0 && this.selectedButton >= 0 && this.selectedButton < var8.size()) {
                  ((AccountSwitcherPanel.PanelButton)var8.get(this.selectedButton)).action().run();
               }

               return true;
            }
         }
      }
   }

   private static int countLineButtons(List<AccountSwitcherPanel.PanelButton> var0, int var1) {
      int var2 = 0;

      for (AccountSwitcherPanel.PanelButton var4 : var0) {
         if (var4.line() == var1) {
            var2++;
         }
      }

      return var2;
   }

   public record AccountRow(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
      private final UUID accountId;

      public AccountRow(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
         this.accountId = id;
         this.name = name;
         this.current = current;
         this.saved = saved;
         this.launcher = launcher;
         this.note = note;
      }
   }

   private enum Mode {
      ACCOUNTS,
      TOKEN,
      PROXIES,
      ADD_PROXY;
   }

   private record PanelButton(HomeLayout.Rect box, String label, Runnable action, boolean destructive, boolean selected, int line) {
   }

   public record PanelLayout(HomeLayout.Rect panel, HomeLayout.Rect footer, int capacity, int scroll, int statusHeight) {
      public HomeLayout.Rect row(int var1) {
         return new HomeLayout.Rect(this.panel.left() + 6, this.panel.top() + 6 + (var1 - this.scroll) * 32, this.panel.width() - 12, 32);
      }
   }

   public enum View {
      ACCOUNT_LIST,
      LOADING,
      DEVICE_CODE,
      LOCKED,
      CONFIRM_ARCHIVE,
      TOKEN_FORM,
      PROXY_LIST,
      PROXY_FORM;
   }
}
