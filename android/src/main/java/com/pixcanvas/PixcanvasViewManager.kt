package com.pixcanvas

import android.view.View
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.bridge.ReadableMap
import android.util.Log
import com.facebook.react.common.MapBuilder
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReactContext


class PixcanvasViewManager(val context: ReactContext) : SimpleViewManager<View>() {
  override fun getName() = "PixcanvasView"
  private val COMMAND_SAVE_ALBUM = 1
  private val COMMAND_SHARE =2

  override fun createViewInstance(reactContext: ThemedReactContext): View {
    return PixCanvasView(reactContext)
  }

  @ReactProp(name = "data")
   fun setData(view: PixCanvasView, data: ReadableMap) {
    Log.d("wccc", "${data.toString()}")
      try {
          view.setData(data)
      } catch (e: Exception) {
          Log.e("PixCanvasViewManager", Log.getStackTraceString(e))
      }
  }

  override fun getCommandsMap(): MutableMap<String, Int>? {
    return MapBuilder.of(
        "save",
        COMMAND_SAVE_ALBUM,
        "share",
        COMMAND_SHARE
    )
  }

  fun receiveCommand(root: PixCanvasView, commandId: Int, args: ReadableArray?) {
    when (commandId) {
        COMMAND_SAVE_ALBUM ->
            root.saveToAlbum()
        COMMAND_SHARE ->
            root.share()
    }
  }
}
