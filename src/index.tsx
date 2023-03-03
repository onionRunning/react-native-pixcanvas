import React, { useRef, useImperativeHandle, forwardRef } from 'react';
import {
  requireNativeComponent,
  View,
  findNodeHandle,
  UIManager,
  ViewStyle,
} from 'react-native';
export interface PointType {
  x: number;
  y: number;
  color: string;
}

type PixCanvasProps = {
  data: {
    size: number;
    points: PointType[];
    radio: number;
    hideGridLine?: boolean;
  };
  style?: ViewStyle;
};

const NativePixCanvas = requireNativeComponent<PixCanvasProps>('PixcanvasView');

const PixCanvas: React.ForwardRefRenderFunction<any, PixCanvasProps> = (
  { data, style },
  ref
) => {
  const canvasRef = useRef<any>(null);

  useImperativeHandle(ref, () => ({
    // 保存到相册
    saveToAlbum() {
      UIManager.dispatchViewManagerCommand(
        findNodeHandle(canvasRef.current),
        (UIManager as any).PixCanvas.Commands.save,
        []
      );
    },
    // 系统级别分享
    share() {
      UIManager.dispatchViewManagerCommand(
        findNodeHandle(canvasRef.current),
        (UIManager as any).PixCanvas.Commands.share,
        []
      );
    },
  }));

  const viewStyle = {
    backgroundColor: '#fff',
    width: data.size,
    height: data.size,
  };
  const pixStyle = {
    flex: 1,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center',
    alignItems: 'center',
  } as ViewStyle;
  console.info(data, '------');
  return (
    <View style={[viewStyle, style]}>
      <NativePixCanvas
        data={{ ...data, hideGridLine: data?.hideGridLine ?? false }}
        ref={canvasRef}
        style={pixStyle}
      />
    </View>
  );
};

export default forwardRef(PixCanvas);
