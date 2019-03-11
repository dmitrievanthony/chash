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

public class RendezvousHashingRouter implements Hasher {

    private final Hasher hasher;

    private final int numberOfNodes;

    public RendezvousHashingRouter(Hasher hasher, int numberOfNodes) {
        this.hasher = hasher;
        this.numberOfNodes = numberOfNodes;
    }

    @Override
    public int calculate(int key) {
        int res = -1, maxHash = Integer.MIN_VALUE;

        for (int node = 0; node < numberOfNodes; node++) {
            int hash = hasher.calculate(key ^ node);

            if (hash > maxHash) {
                maxHash = hash;
                res = node;
            }
        }

        return res;
    }
}
