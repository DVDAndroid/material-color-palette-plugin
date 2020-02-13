/*
 * Copyright 2020 dvdandroid
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

import com.dvd.intellijdea.materialcolorpalette.Colors.allColors
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class Palette {

    lateinit var panel: JPanel
    private lateinit var parentColors: JList<MaterialColor>
    private lateinit var childrenColors: JList<MaterialColor>
    private var lastIndex: Int = 0

    init {
        val listModel = DefaultListModel<MaterialColor>()
        allColors.forEach { listModel.addElement(it[5]) }

        parentColors.apply {
            layoutOrientation = JList.HORIZONTAL_WRAP
            cellRenderer = ColorRender(false)
            model = listModel
            visibleRowCount = 5

            addMouseListener(RightClickPopup())
        }

        childrenColors.apply {
            layoutOrientation = JList.HORIZONTAL_WRAP
            cellRenderer = ColorRender(true)

            addMouseListener(RightClickPopup())
        }
    }

    inner class RightClickPopup : MouseAdapter() {

        override fun mousePressed(event: MouseEvent?) {
            val list = event?.source as JList<*>
            if (list.locationToIndex(event.point) == -1 && !event.isShiftDown) {
                list.clearSelection()
                return
            }

            val index = list.locationToIndex(event.point)
            if (index != -1 && !list.getCellBounds(index, index).contains(event.point)) return

            if (list == parentColors) {
                val children = DefaultListModel<MaterialColor>()

                allColors[index].forEach { children.addElement(it) }

                lastIndex = index

                childrenColors.apply {
                    model = children
                    visibleRowCount = 4
                }

                if (SwingUtilities.isRightMouseButton(event)) {
                    buildPopup(list, index).show(event.component, event.x, event.y)
                }
            } else {
                buildPopup(list, index).show(event.component, event.x, event.y)
            }

            list.selectedIndex = index
        }

        private fun buildPopup(list: JList<*>, index: Int): JPopupMenu {
            val popup = JPopupMenu()

            if (list == childrenColors) {
                val hexThisItem = JMenuItem(String.format(PASTE, "this", "HEX code"))
                hexThisItem.addActionListener { UtilsEnvironment.insertInEditor(allColors[lastIndex][index].hexCode) }

                val colorResThisItem = JMenuItem(String.format(PASTE, "this", "resource"))
                colorResThisItem.addActionListener { UtilsEnvironment.insertInEditor(allColors[lastIndex][index].colorRes) }

                val clipboardThisItem = JMenuItem(String.format(COPY, "this"))
                clipboardThisItem.addActionListener { copyToClipboard(allColors[lastIndex][index].hexCode) }

                popup.add(hexThisItem)
                popup.add(colorResThisItem)
                popup.add(clipboardThisItem)

                return popup
            }

            val hex500Item = JMenuItem(String.format(PASTE, "primary", "HEX code"))
            hex500Item.addActionListener { UtilsEnvironment.insertInEditor(allColors[index][5].hexCode) }

            val colorRes500Item = JMenuItem(String.format(PASTE, "primary", "resource"))
            colorRes500Item.addActionListener { UtilsEnvironment.insertInEditor(allColors[index][5].colorRes) }

            val clipboard500Item = JMenuItem(String.format(COPY, "primary"))
            clipboard500Item.addActionListener { copyToClipboard(allColors[index][5].hexCode) }

            val hex700Item = JMenuItem(String.format(PASTE, "dark primary", "HEX code"))
            hex700Item.addActionListener { UtilsEnvironment.insertInEditor(allColors[index][7].hexCode) }

            val colorRes700Item = JMenuItem(String.format(PASTE, "dark primary", "resource"))
            colorRes700Item.addActionListener { UtilsEnvironment.insertInEditor(allColors[index][7].colorRes) }

            val clipboard700Item = JMenuItem(String.format(COPY, "dark primary"))
            clipboard700Item.addActionListener { copyToClipboard(allColors[index][7].hexCode) }

            popup.apply {
                add(hex500Item)
                add(colorRes500Item)
                add(clipboard500Item)
                addSeparator()
                add(hex700Item)
                add(colorRes700Item)
                add(clipboard700Item)
            }


            if (index < 16) {
                val hexAccentItem = JMenuItem(String.format(PASTE, "accent", "HEX code"))
                hexAccentItem.addActionListener { UtilsEnvironment.insertInEditor(allColors[index][11].hexCode) }

                val colorResAccentItem = JMenuItem(String.format(PASTE, "accent", "resource"))
                colorResAccentItem.addActionListener { UtilsEnvironment.insertInEditor(allColors[index][11].colorRes) }

                val clipboardAccentItem = JMenuItem(String.format(COPY, "accent"))
                clipboardAccentItem.addActionListener { copyToClipboard(allColors[index][11].hexCode) }

                popup.apply {
                    addSeparator()
                    add(hexAccentItem)
                    add(colorResAccentItem)
                    add(clipboardAccentItem)
                }
            }

            return popup
        }

        private fun copyToClipboard(colorRes: String) =
            Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(colorRes), null)
    }

    companion object {
        private const val COPY = "Copy %s HEX color in clipboard"
        private const val PASTE = "Paste %s color as %s"
    }

}
