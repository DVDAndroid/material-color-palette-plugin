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

import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.GridBagLayout
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.*

/**
 * @author dvdandroid
 */
internal class ColorRender(private val children: Boolean) : JPanel(), ListCellRenderer<MaterialColor> {

    private var icon: JLabel? = null
    private var name: JLabel? = null

    init {
        try {
            val bufImg = ImageIO.read(javaClass.getResource("/icons/ic_check_circle.png"))
            icon = JLabel(ImageIcon(bufImg))
        } catch (ignored: IOException) {
        }

        name = JLabel()
    }

    override fun getListCellRendererComponent(list: JList<out MaterialColor>, color: MaterialColor, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        val size = 100

        layout = GridBagLayout()
        background = Color.decode(color.hexCode)
        toolTipText = "${color.fixedName} (${color.hexCode})"

        minimumSize = Dimension(size, size)
        maximumSize = Dimension(size, size)
        preferredSize = Dimension(size, size)

        if (children) {
            name!!.text = "A?\\d+".toRegex().find(color.fixedName)?.groups?.get(0)?.value
        } else {
            name!!.text = "\\D+".toRegex().find(color.fixedName)?.groups?.get(0)?.value
        }

        add(name)
        if (isSelected) {
            add(icon)
        } else {
            remove(icon!!)
        }

        return this
    }

}