package com.example;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KMeans {
    public static void main(String[] args) {
        Random random = new Random();
        List<Point> points = IntStream.rangeClosed(0, 20)
                .mapToObj(i -> new Point(random.nextDouble(), random.nextDouble()))
                .collect(Collectors.toList());

        Integer clusterNum = 3;

        // choice center points randomly
        List<Point> centers = new ArrayList<>();
        for (int i = 0; i < clusterNum; i++) {
            centers.set(i, points.get(i));
        }

        while (true) {
            System.out.println("Hello");

            // Set cluster for each points.
            Boolean changed = false;
            Map<Point, Integer> point2cluster = new HashMap<>();
            for (Point point : points) {
                double score = Double.MAX_VALUE;
                Integer cluster = null;
                for (int i = 0; i < centers.size(); i++) {
                    Point center = centers.get(i);
                    double l = Point.length(point, center);
                    // より近いものがある
                    if (l < score) {
                        score = l;
                        cluster = i;
                    }
                }
                if (!Objects.equals(cluster, point2cluster.get(point))) {
                    changed = true;
                    point2cluster.put(point, cluster);
                }
            }
            if (changed) {
                // 各クラスタの中心点を探し直す
                for (int i = 0; i < clusterNum; i++) {
                    final int I = i;
                    List<Point> clusterPoints = point2cluster.entrySet()
                            .stream()
                            .filter(it -> it.getValue() == I)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());

                    Map<Point, Double> point2Score = new HashMap<>();
                    for (Point a : clusterPoints) {
                        Double score = 0.0;
                        for (Point b : clusterPoints) {
                            if (a == b) {
                                continue;
                            }
                            score += Point.length(a, b);
                        }
                        point2Score.put(a, score);
                    }
                    Point center = point2Score.entrySet().stream()
                            .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                            .findFirst()
                            .map(Map.Entry::getKey)
                            .orElseThrow(() -> new InvalidStateException("Missing enough items"));
                    centers.set(i, center);
                }
            } else {
                // 中心が変更されないので終了
                System.out.println(point2cluster);
                break;
            }
        }
    }

}
