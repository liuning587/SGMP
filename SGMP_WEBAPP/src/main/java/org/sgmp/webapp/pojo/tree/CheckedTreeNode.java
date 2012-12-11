package org.sgmp.webapp.pojo.tree;

public class CheckedTreeNode extends TreeNode {

    /**
     * 
     */
    private static final long serialVersionUID = -1993436299745114456L;

    private boolean checked;

    /**
     * 
     */
    public CheckedTreeNode() {
        super();
    }

    /**
     * 
     * @param id
     * @param text
     * @param leaf
     * @param checked
     */
    public CheckedTreeNode(String id, String text, boolean leaf, boolean checked) {
        super(id, text, leaf);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}