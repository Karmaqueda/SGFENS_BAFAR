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

/**
 *
 * @author ISCesarMartinez poseidon24@hotmail.com
 * @date 13-ene-2014
 */
public class IntegerCustomBinder {

    public static String printInt3LeadZeros(Integer value) {
        if (value == null) {
            return null;
        }
        String result = String.valueOf(value);
        for (int x = 0, length = 3 - result.length(); x < length; x++) {
            result = "0" + result;
        }
        return result;
    }

    public static Integer parseInt(String value) {
        Integer intResult;
        try {
            intResult = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return intResult;
    }
}
