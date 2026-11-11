package com.larpclient.util.entity;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public interface WalkRigAccess {
   void larpRestoreWalkRig();

   void larpApplyJoints(float[] var1, AvatarRenderState var2);
}
