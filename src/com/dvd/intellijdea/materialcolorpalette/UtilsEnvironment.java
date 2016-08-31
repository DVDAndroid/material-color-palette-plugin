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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiUtilBase;

/**
 * @author dvdandroid
 */
class UtilsEnvironment {

    public static void insertInEditor(final String text) {
        Project project = getOpenProject();
        Editor editor = getEditor(project);

        if (project != null && editor != null && text != null && !text.isEmpty()) {
            CaretModel caretModel = editor.getCaretModel();
            final Integer currentOffset = caretModel.getOffset();
            final SelectionModel selectionModel = editor.getSelectionModel();

            CommandProcessor.getInstance().executeCommand(project, () -> ApplicationManager.getApplication().runWriteAction(() -> {
                Integer textLen = text.length();
                Document document = editor.getDocument();

                if (selectionModel.hasSelection()) {
                    int selectionStart = selectionModel.getSelectionStart();
                    document.replaceString(selectionStart, selectionModel.getSelectionEnd(), text);
                    selectionModel.removeSelection();
                    editor.getCaretModel().moveToOffset(selectionStart + textLen);
                } else {
                    document.insertString(currentOffset, text);
                    editor.getCaretModel().moveToOffset(currentOffset + textLen);
                }

                VirtualFile file = FileDocumentManager.getInstance().getFile(document);
                if (file != null) {
                    PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
                    if (psiFile != null) {
                        CodeStyleManager.getInstance(project).reformatText(psiFile, currentOffset, currentOffset + textLen);
                    }
                }
            }), "Paste", UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);

            VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (virtualFile != null) {
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
            }
        }
    }

    private static Project getOpenProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();

        return (projects.length > 0) ? projects[0] : null;
    }

    private static Editor getEditor(Project curProject) {
        if (curProject == null) {
            curProject = getOpenProject();
        }
        if (curProject != null) {
            FileEditorManager manager = FileEditorManager.getInstance(curProject);

            return manager.getSelectedTextEditor();
        }

        return null;
    }

}
