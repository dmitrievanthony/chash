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

package com.dmitrievanthony.chash.router;

import com.dmitrievanthony.chash.hash.Hasher;
import java.util.Arrays;

public class ConsistentHashingRouter implements Hasher {

    private final Hasher hasher;

    private final int numberOfNodes;

    private final int segmentsPerNode;

    private final int[] ring;

    private final int[] nodes;

    public ConsistentHashingRouter(Hasher hasher, int numberOfNodes) {
        this(hasher, numberOfNodes, 64);
    }

    public ConsistentHashingRouter(Hasher hasher, int numberOfNodes, int segmentsPerNode) {
        this.hasher = hasher;
        this.numberOfNodes = numberOfNodes;
        this.segmentsPerNode = segmentsPerNode;

        this.ring = new int[numberOfNodes * segmentsPerNode];
        this.nodes = new int[numberOfNodes * segmentsPerNode];

        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < segmentsPerNode; j++) {
                ring[i * segmentsPerNode + j] = hasher.calculate(i * segmentsPerNode + j);
                nodes[i * segmentsPerNode + j] = i;
            }
        }

        qsort(ring, nodes);
    }

    @Override
    public int calculate(int key) {
        int hash = hasher.calculate(key);

        int idx = Arrays.binarySearch(ring, hash);

        if (idx < 0)
            idx = -(idx + 1);

        if (idx == nodes.length)
            idx = 0;

        return nodes[idx];
    }

    private void qsort(int[] a, int[] b) {
        qsort(a, b, 0, a.length - 1);
    }

    private void qsort(int[] a, int[] b, int from, int to) {
        if (from >= to)
            return;

        int pivot = a[(from + to) / 2];
        int i = from, j = to;

        while (i <= j) {
            while (a[i] < pivot)
                i++;
            while (a[j] > pivot)
                j--;

            if (i <= j) {
                swap(a, i, j);
                swap(b, i, j);
                i++;
                j--;
            }
        }

        qsort(a, b, from, j);
        qsort(a, b, i, to);
    }

    private void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
