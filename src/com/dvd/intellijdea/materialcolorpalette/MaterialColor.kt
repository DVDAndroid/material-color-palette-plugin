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

package com.dvd.intellijdea.materialcolorpalette

internal class MaterialColor(name: String, val hexCode: String) {

    val fixedName: String
    val colorRes: String

    init {
        this.fixedName = capitalize(name)
        this.colorRes = String.format(COLOR_RES, name, hexCode)
    }

    private fun capitalize(str: String): String {
        val words = str.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val ret = StringBuilder()
        for (i in words.indices) {
            ret.append(Character.toUpperCase(words[i][0]))
            ret.append(words[i].substring(1))
            if (i < words.size - 1) {
                ret.append(' ')
            }
        }
        return ret.toString()
    }

    override fun toString() = fixedName

    companion object {
        private val COLOR_RES = "<color name=\"%s\">%s</color>"
    }
}
