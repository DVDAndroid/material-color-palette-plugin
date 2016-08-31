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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * @author dvdandroid
 */
class ColorRender extends JPanel implements ListCellRenderer<MaterialColor> {

    private JLabel icon;

    public ColorRender() {
        try {
            BufferedImage bufImg = ImageIO.read(getClass().getResource("/icons/ic_check_circle.png"));
            icon = new JLabel(new ImageIcon(bufImg));
        } catch (IOException ignored) {
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MaterialColor> list, MaterialColor color, int index, boolean isSelected, boolean cellHasFocus) {
        final int size = 100;

        setLayout(new GridBagLayout());
        setBackground(Color.decode(color.hexCode));
        setToolTipText(color.fixedName);

        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
        setPreferredSize(new Dimension(size, size));

        if (isSelected) {
            add(icon);
        } else {
            remove(icon);
        }

        return this;
    }

}