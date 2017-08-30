package com.afap.treeview;


import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String id;
    private String text;
    private int level = 0; // 节点级别，更节点为0


    private TreeNode parent;
    private List<TreeNode> childs;


    public TreeNode(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChilds() {
        return childs;
    }

    public void setChilds(List<TreeNode> childs) {
        this.childs = childs;
    }

    public void addChild(TreeNode node) {
        if (childs == null) {
            childs = new ArrayList<>();
        }
        childs.add(node);
    }


}
