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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import static com.dvd.intellijdea.materialcolorpalette.Colors.allColors;

/**
 * @author dvdandroid
 */
public class Palette {

    public JPanel panel;
    private JList<MaterialColor> parentColors;
    private JList<MaterialColor> childrenColors;
    private int lastIndex;

    public Palette() {
        DefaultListModel<MaterialColor> listModel = new DefaultListModel<>();
        for (MaterialColor[] allColor : allColors) {
            listModel.addElement(allColor[5]);
        }

        parentColors.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        parentColors.setCellRenderer(new ColorRender());
        parentColors.setModel(listModel);
        parentColors.setVisibleRowCount(5);

        childrenColors.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        childrenColors.setCellRenderer(new ColorRender());

        parentColors.addMouseListener(new RightClickPopup());
        childrenColors.addMouseListener(new RightClickPopup());
    }

    class RightClickPopup extends MouseAdapter {

        private static final String PASTE = "Paste %s color as %s";

        private JPopupMenu popup;

        @Override
        public void mousePressed(MouseEvent e) {
            JList list = (JList) e.getSource();
            if (list.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()) {
                list.clearSelection();
                return;
            }

            int index = list.locationToIndex(e.getPoint());

            if (index != -1 && !list.getCellBounds(index, index).contains(e.getPoint())) {
                return;
            }

            if (list == parentColors) {
                DefaultListModel<MaterialColor> children = new DefaultListModel<>();

                for (int i = 0; i < allColors[index].length; i++) {
                    children.addElement(allColors[index][i]);
                }

                lastIndex = index;

                childrenColors.setModel(children);
                childrenColors.setVisibleRowCount(4);

                if (SwingUtilities.isRightMouseButton(e)) {
                    buildPopup(list, index);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            } else {
                buildPopup(list, index);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }

            list.setSelectedIndex(index);
        }

        private void buildPopup(JList list, int index) {
            popup = new JPopupMenu();

            if (list == childrenColors) {
                JMenuItem hexThisItem = new JMenuItem(String.format(PASTE, "this", "HEX code"));
                hexThisItem.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[lastIndex][index].hexCode));

                JMenuItem colorResThisItem = new JMenuItem(String.format(PASTE, "this", "resource"));
                colorResThisItem.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[lastIndex][index].colorRes));

                popup.add(hexThisItem);
                popup.add(colorResThisItem);

                return;
            }

            JMenuItem hex500Item = new JMenuItem(String.format(PASTE, "primary", "HEX code"));
            hex500Item.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[index][5].hexCode));

            JMenuItem colorRes500Item = new JMenuItem(String.format(PASTE, "primary", "resource"));
            colorRes500Item.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[index][5].colorRes));


            JMenuItem hex700Item = new JMenuItem(String.format(PASTE, "dark primary", "HEX code"));
            hex700Item.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[index][7].hexCode));

            JMenuItem colorRes700Item = new JMenuItem(String.format(PASTE, "dark primary", "resource"));
            colorRes700Item.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[index][7].colorRes));

            this.popup.add(hex500Item);
            this.popup.add(colorRes500Item);
            this.popup.addSeparator();
            this.popup.add(hex700Item);
            this.popup.add(colorRes700Item);

            if (index < 16) {
                JMenuItem hexAccentItem = new JMenuItem(String.format(PASTE, "accent", "HEX code"));
                hexAccentItem.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[index][11].hexCode));

                JMenuItem colorResAccentItem = new JMenuItem(String.format(PASTE, "accent", "resource"));
                colorResAccentItem.addActionListener(e -> UtilsEnvironment.insertInEditor(allColors[index][11].colorRes));

                popup.addSeparator();
                popup.add(hexAccentItem);
                popup.add(colorResAccentItem);
            }
        }

    }

}