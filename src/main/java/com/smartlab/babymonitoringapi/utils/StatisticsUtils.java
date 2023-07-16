package com.smartlab.babymonitoringapi.utils;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsUtils {
    public static Map<String, Float> calculateStatistics(List<SensorData> sensorDataList) {

        Float mean = calculateMean(sensorDataList);
        Float median = calculateMedian(sensorDataList);
        Float mode = calculateMode(sensorDataList);
        Float standardDeviation = calculateStandardDeviation(sensorDataList);
        Float variance = calculateVariance(sensorDataList);
        Float range = calculateRange(sensorDataList);
        Float min = calculateMin(sensorDataList);
        Float max = calculateMax(sensorDataList);

        return Map.of( "mean", mean, "median", median, "mode", mode, "standardDeviation", standardDeviation, "variance", variance, "range", range, "min", min, "max", max);
    }

    private static Float calculateMax(List<SensorData> sensorDataList) {
        return sensorDataList.stream().map(SensorData::getValue).max(Float::compareTo).orElse(0f);
    }

    private static Float calculateMin(List<SensorData> sensorDataList) {
        return sensorDataList.stream().map(SensorData::getValue).min(Float::compareTo).orElse(0f);
    }

    private static Float calculateRange(List<SensorData> sensorDataList) {
        return sensorDataList.stream().map(SensorData::getValue).max(Float::compareTo).orElse(0f) - sensorDataList.stream().map(SensorData::getValue).min(Float::compareTo).orElse(0f);
    }

    private static Float calculateVariance(List<SensorData> sensorDataList) {
        int sum = 0;
        double mean;

        for (SensorData sensorData : sensorDataList) {
            sum += sensorData.getValue();
        }

        mean = (double) sum / sensorDataList.size();

        double squaredDiffSum = 0;

        for (SensorData sensorData : sensorDataList) {
            double diff = sensorData.getValue() - mean;
            squaredDiffSum += Math.pow(diff, 2);
        }

        float variance = (float) (squaredDiffSum / sensorDataList.size());

        return variance;
    }

    private static Float calculateStandardDeviation(List<SensorData> sensorDataList) {
        int sum = 0;
        double mean;

        for (SensorData sensorData : sensorDataList) {
            sum += sensorData.getValue();
        }

        mean = (double) sum / sensorDataList.size();

        double squaredDiffSum = 0;

        for (SensorData sensorData : sensorDataList) {
            double diff = sensorData.getValue() - mean;
            squaredDiffSum += Math.pow(diff, 2);
        }

        double variance = squaredDiffSum / sensorDataList.size();

        float standardDeviation = (float) Math.sqrt(variance);

        return standardDeviation;
    }

    private static Float calculateMode(List<SensorData> sensorDataList) {
        Map<Float, Integer> map = new HashMap<>();

        for (SensorData sensorData : sensorDataList) {
            if (map.containsKey(sensorData.getValue())) {
                map.put(sensorData.getValue(), map.get(sensorData.getValue()) + 1);
            } else {
                map.put(sensorData.getValue(), 1);
            }
        }

        float max = 0;
        float mode = 0;

        for (Map.Entry<Float, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                mode = entry.getKey();
            }
        }

        return mode;
    }

    private static Float calculateMedian(List<SensorData> sensorDataList) {
        Float median = 0f;

        int index = sensorDataList.size() / 2;

        if (sensorDataList.size() % 2 == 0) {
            median = (sensorDataList.get(index - 1).getValue() + sensorDataList.get(index).getValue()) / 2;
        } else {
            median = sensorDataList.get(index).getValue();
        }

        return median;
    }

    private static Float calculateMean(List<SensorData> sensorDataList) {
        Float sum = 0f;
        Float count = 0f;

        for (SensorData sensorData : sensorDataList) {
            sum += sensorData.getValue();
            count++;
        }

        float mean = (float) (sum / count);

        return mean;
    }
}
