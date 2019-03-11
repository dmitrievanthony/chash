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

package com.dmitrievanthony.chash.estimator.generator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class DataGenerator implements Iterator<Integer> {

    private int count;

    public static DataGenerator seq(int count) {
        return new SequentialDataGenerator(count);
    }

    public static DataGenerator uniform(int count) {
        return new UniformDataGenerator(count);
    }

    public static DataGenerator normal(int count) {
        return new NormalDataGenerator(count);
    }

    public DataGenerator(int count) {
        this.count = count;
    }

    abstract Integer nextInternal();

    @Override public boolean hasNext() {
        return count > 0;
    }

    @Override public Integer next() {
        if (!hasNext())
            throw new NoSuchElementException();

        Integer next = nextInternal();
        count--;

        return next;
    }
}
