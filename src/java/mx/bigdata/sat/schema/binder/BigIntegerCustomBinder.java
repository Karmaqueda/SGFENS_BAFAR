/*
 * Copyright 2014 ISCesarMartinez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.bigdata.sat.schema.binder;

import java.math.BigInteger;

/**
 *
 * @author ISCesarMartinez poseidon24@hotmail.com
 * @date 13-ene-2014
 */
public class BigIntegerCustomBinder {
    
    public static String printBigInt18LeadZeros(BigInteger value) {
        if (value == null) {
            return null;
        }
        String result = value.toString();
        for (int x = 0, length = 18 - result.length(); x < length; x++) {
            result = "0" + result;
        }
        return result;
    }

    public static BigInteger parseBigInt(String value) {
        BigInteger intResult;
        try {
            intResult = new BigInteger(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return intResult;
    }
}
