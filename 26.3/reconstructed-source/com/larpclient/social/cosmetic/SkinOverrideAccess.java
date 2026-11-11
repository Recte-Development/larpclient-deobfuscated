package com.larpclient.social.cosmetic;

import com.larpclient.render.entity.LegTrail;

public interface SkinOverrideAccess {
   int hEADLESS = 0;
   int kORBLOX = 1;
   int rOBLOX = 2;

   int larp$headless();

   LegTrail larp$korblox();

   void larp$roblox(int var1, LegTrail var2);
}
