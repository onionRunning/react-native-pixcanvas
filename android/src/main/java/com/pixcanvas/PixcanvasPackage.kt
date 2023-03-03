package com.pixcanvas

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager


class PixcanvasPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return emptyList()
  }

  override fun createViewManagers(
      reactContext: ReactApplicationContext
  ) = mutableListOf<ViewManager<out View, out ReactShadowNode<*>>>().also {
      it.add(PixcanvasViewManager(reactContext))
  }
}
