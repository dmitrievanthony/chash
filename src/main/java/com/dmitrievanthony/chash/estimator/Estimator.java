/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmitrievanthony.chash.estimator;

import com.dmitrievanthony.chash.hash.Hasher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Estimator {

    public Estimation estimate(Hasher hasher, int buckets, Iterator<Integer> generator) {
        Map<Integer, Integer> counter = new HashMap<>();

        while (generator.hasNext()) {
            int key = generator.next();
            int hash = hasher.calculate(key) % buckets;

            if (!counter.containsKey(hash)) {
                counter.put(hash, 1);
            }
            else {
                counter.put(hash, counter.get(hash) + 1);
            }
        }

        int[] histogram = counterToHistogram(counter);

        double mean = mean(histogram);
        double std = std(histogram);

        return new Estimation(mean, std);
    }

    public double diff(Hasher a, Hasher b, Iterator<Integer> generator) {
        int diff = 0, total = 0;

        while (generator.hasNext()) {
            int key = generator.next();
            int aHash = a.calculate(key);
            int bHash = b.calculate(key);

            if (aHash != bHash)
                diff++;

            total++;
        }

        return 1.0 * diff / total;
    }

    private int[] counterToHistogram(Map<Integer, Integer> counter) {
        int[] histogram = new int[counter.size()];

        for (int i = 0; i < counter.size(); i++) {
            histogram[i] = counter.get(i);
        }

        return histogram;
    }

    private static double mean(int[] arr) {
        double sum = 0;

        for (int e : arr)
            sum += e;

        return sum / arr.length;
    }

    private static double std(int[] arr) {
        double mean = mean(arr);

        double sum = 0;
        for (int e : arr)
            sum += Math.pow(e - mean, 2);

        return Math.sqrt(sum / arr.length);
    }

    public static class Estimation {

        private final double mean;

        private final double std;

        public Estimation(double mean, double std) {
            this.mean = mean;
            this.std = std;
        }

        public double getMean() {
            return mean;
        }

        public double getStd() {
            return std;
        }

        @Override public String toString() {
            return "{" +
                "mean=" + mean +
                ", std=" + std +
                '}';
        }
    }
}
