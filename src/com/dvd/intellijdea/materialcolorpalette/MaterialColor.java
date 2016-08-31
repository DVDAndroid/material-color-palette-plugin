/*
 * Copyright 2016 dvdandroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dvd.intellijdea.materialcolorpalette;

class MaterialColor {

    private static final String COLOR_RES = "<color name=\"%s\">%s</color>";

    public final String fixedName;
    public final String hexCode;
    public final String colorRes;

    public MaterialColor(String name, String hexCode) {
        this.hexCode = hexCode;

        this.fixedName = capitalize(name);
        this.colorRes = String.format(COLOR_RES, name, hexCode);
    }

    private String capitalize(String str) {
        String[] words = str.split("_");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1));
            if (i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    @Override
    public String toString() {
        return fixedName;
    }
}
