import * as React from 'react';

import { StyleSheet, View } from 'react-native';
import PixcanvasView, { PointType } from 'react-native-pixcanvas';

const getPoints = (op?: PointType[]): PointType[] => {
  const pm = (op || []).reduce((target, next) => {
    target[`${next.x}_${next.y}`] = next.color;
    return target;
  }, {} as Record<string, string>);
  const points: PointType[] = [];
  for (let i = 0; i < 32; i++) {
    for (let j = 0; j < 32; j++) {
      const color = pm[`${i}_${j}`] || '#FFFFFF';
      points.push({
        x: i,
        y: j,
        color,
      });
    }
  }
  return points;
};

const data = [
  { x: 1, y: 1, color: '#ffaa00' },
  { x: 2, y: 3, color: '#ffaa00' },
  { x: 3, y: 3, color: '#0000ff' },
  { x: 4, y: 3, color: '#ffaa00' },
  { x: 5, y: 3, color: '#00ffff' },
  { x: 6, y: 1, color: '#ffaa00' },
  { x: 7, y: 3, color: '#ffaa00' },
  { x: 8, y: 3, color: '#0000ff' },
  { x: 9, y: 3, color: '#ffaa00' },
  { x: 10, y: 3, color: '#00ffff' },
];

export default function App() {
  const [points, setPoints] = React.useState<PointType[]>();

  React.useEffect(() => {
    setPoints([...getPoints(), ...data]);
  }, []);

  return (
    <View style={styles.container}>
      <PixcanvasView
        data={{
          size: 300,
          radio: 32,
          points: points!,
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#fff',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
