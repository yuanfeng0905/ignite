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

package org.apache.ignite.ml.preprocessing.encoding.stringencoder;

import java.util.Map;
import org.apache.ignite.ml.math.exceptions.preprocessing.UnknownStringValue;
import org.apache.ignite.ml.math.functions.IgniteBiFunction;

/**
 * Preprocessing function that makes String encoding.
 *
 * @param <K> Type of a key in {@code upstream} data.
 * @param <V> Type of a value in {@code upstream} data.
 */
public class StringEncoderPreprocessor<K, V> implements IgniteBiFunction<K, V, double[]> {
    /** */
    private static final long serialVersionUID = 6237812226382623469L;

    /** Filling values. */
    private final Map<String, Integer>[] encodingValues;

    /** Base preprocessor. */
    private final IgniteBiFunction<K, V, String[]> basePreprocessor;

    /**
     * Constructs a new instance of String Encoder preprocessor.
     *
     * @param basePreprocessor Base preprocessor.
     */
    public StringEncoderPreprocessor(Map<String, Integer>[] encodingValues,
        IgniteBiFunction<K, V, String[]> basePreprocessor) {
        this.encodingValues = encodingValues;
        this.basePreprocessor = basePreprocessor;
    }

    /**
     * Applies this preprocessor.
     *
     * @param k Key.
     * @param v Value.
     * @return Preprocessed row.
     */
    @Override public double[] apply(K k, V v) {
        String[] tmp = basePreprocessor.apply(k, v);
        double[] res = new double[tmp.length];

        for (int i = 0; i < res.length; i++) {
            if (encodingValues[i].containsKey(tmp[i]))
                res[i] = encodingValues[i].get(tmp[i]);
            else
                throw new UnknownStringValue(tmp[i]);
        }
        return res;
    }
}
