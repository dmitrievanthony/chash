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

package com.dmitrievanthony.chash;

import com.dmitrievanthony.chash.estimator.Estimator;
import com.dmitrievanthony.chash.estimator.generator.SequentialDataGenerator;
import com.dmitrievanthony.chash.hash.Hasher;
import com.dmitrievanthony.chash.hash.ShiftMultHasher;
import com.dmitrievanthony.chash.router.ConsistentHashingRouter;
import com.dmitrievanthony.chash.router.RendezvousHashingRouter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import static com.dmitrievanthony.chash.estimator.generator.DataGenerator.*;

public class Application {

    public static void main(String... args) {
        Estimator estimator = new Estimator();

        Hasher consistentHasher = new ConsistentHashingRouter(new ShiftMultHasher(), 100);
        Hasher rendezvousHasher = new RendezvousHashingRouter(new ShiftMultHasher(), 100);

        System.out.printf("========== Consistent vs. Rendezvous ===========\n");
        System.out.printf("Consistent hashing: %s\n", estimator.estimate(consistentHasher, 100, seq(1_000_000)));
        System.out.printf("Rendezvous hashing: %s\n", estimator.estimate(rendezvousHasher, 100, seq(1_000_000)));

        for (int i = 1; i < 6; i++) {
            Hasher consistentHasherUpdated = new ConsistentHashingRouter(new ShiftMultHasher(), 100 - i);
            Hasher rendezvousHasherUpdated = new RendezvousHashingRouter(new ShiftMultHasher(), 100 - i);

            System.out.printf("================ Remove %d nodes ================\n", i);
            System.out.printf("Consistent hashing: %f%%\n",
                100 * estimator.diff(consistentHasher, consistentHasherUpdated, seq(1_000_000)));
            System.out.printf("Rendezvous hashing: %f%%\n",
                100 * estimator.diff(rendezvousHasher, rendezvousHasherUpdated, seq(1_000_000)));
        }
    }
}
