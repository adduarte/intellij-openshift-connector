package org.jboss.tools.intellij.openshift.actions.storage;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jboss.tools.intellij.openshift.actions.OdoAction;
import org.jboss.tools.intellij.openshift.tree.LazyMutableTreeNode;
import org.jboss.tools.intellij.openshift.tree.application.ComponentNode;
import org.jboss.tools.intellij.openshift.tree.application.PersistentVolumeClaimNode;
import org.jboss.tools.intellij.openshift.utils.UIHelper;
import org.jboss.tools.intellij.openshift.utils.odo.Odo;

import javax.swing.JOptionPane;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DeleteStorageAction extends OdoAction {
  public DeleteStorageAction() {
    super(PersistentVolumeClaimNode.class);
  }

  @Override
  public void actionPerformed(AnActionEvent anActionEvent, TreePath path, Object selected, Odo odo) {
    PersistentVolumeClaimNode storageNode = (PersistentVolumeClaimNode) selected;
    ComponentNode componentNode = (ComponentNode) storageNode.getParent();
    LazyMutableTreeNode applicationNode = (LazyMutableTreeNode) ((TreeNode) componentNode).getParent();
    LazyMutableTreeNode projectNode = (LazyMutableTreeNode) applicationNode.getParent();
    CompletableFuture.runAsync(() -> {
      try {
          odo.deleteStorage(projectNode.toString(), applicationNode.toString(), componentNode.toString(), storageNode.toString());
          componentNode.reload();
      }
      catch (IOException e) {
        UIHelper.executeInUI(() -> JOptionPane.showMessageDialog(null, "Error: " + e.getLocalizedMessage(), "Create storage", JOptionPane.ERROR_MESSAGE));
      }
    });
  }
}