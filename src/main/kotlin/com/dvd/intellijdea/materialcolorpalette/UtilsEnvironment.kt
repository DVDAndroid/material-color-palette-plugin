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

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiUtilBase
import java.util.concurrent.TimeUnit

object UtilsEnvironment {

    private val openProject: Project?
        get() = DataManager.getInstance().dataContextFromFocusAsync.blockingGet(1, TimeUnit.SECONDS)?.getData(
            PlatformDataKeys.PROJECT
        ) as Project

    fun insertInEditor(text: String?) {
        val project = openProject
        val editor = getEditor(project)

        ProjectManager.getInstance().openProjects

        if (project != null && editor != null && text != null && text.isNotEmpty()) {
            val caretModel = editor.caretModel
            val currentOffset = caretModel.offset
            val selectionModel = editor.selectionModel

            CommandProcessor.getInstance().executeCommand(project, {
                ApplicationManager.getApplication().runWriteAction {
                    val textLen = text.length
                    val document = editor.document

                    if (selectionModel.hasSelection()) {
                        val selectionStart = selectionModel.selectionStart
                        document.replaceString(selectionStart, selectionModel.selectionEnd, text)
                        selectionModel.removeSelection()
                        editor.caretModel.moveToOffset(selectionStart + textLen)
                    } else {
                        document.insertString(currentOffset, text)
                        editor.caretModel.moveToOffset(currentOffset + textLen)
                    }

                    FileDocumentManager.getInstance().getFile(document)?.let {
                        PsiUtilBase.getPsiFileInEditor(editor, project)?.let { psiFile ->
                            CodeStyleManager.getInstance(project)
                                .reformatText(psiFile, currentOffset, currentOffset + textLen)
                        }
                    }
                }
            }, "Paste", UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION)

            FileDocumentManager.getInstance().getFile(editor.document)?.let { file ->
                FileEditorManager.getInstance(project).openFile(file, true)
            }
        }
    }

    private fun getEditor(curProject: Project?): Editor? {
        var pr = curProject
        if (pr == null) pr = openProject

        if (pr != null) {
            val manager = FileEditorManager.getInstance(pr)

            return manager.selectedTextEditor
        }

        return null
    }

}
