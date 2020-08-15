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

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout

class ToolWindowFactory : com.intellij.openapi.wm.ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: com.intellij.openapi.wm.ToolWindow) {
        val newToolWindow = ToolWindow()
        val content = ContentFactory.SERVICE.getInstance().createContent(newToolWindow, "", false)

        toolWindow.apply {
            toolWindow.title = "Material Palette"
            toolWindow.setAvailable(true, null)

            toolWindow.contentManager.addContent(content)
        }
    }

    inner class ToolWindow : SimpleToolWindowPanel(false) {
        init {
            add(Palette().panel, BorderLayout.CENTER)
        }
    }

}
