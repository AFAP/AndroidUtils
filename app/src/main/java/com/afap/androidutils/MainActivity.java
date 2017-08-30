package com.afap.androidutils;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afap.treeview.TreeNode;
import com.afap.treeview.TreeNodeViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private TreeNodeViewAdapter mAdapter;

    private List<TreeNode> mValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mValues = new ArrayList<>();
        test();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TreeNodeViewAdapter(mValues, this);
        mRecyclerView.setAdapter(mAdapter);
    }


    void test() {
        TreeNode rootNode = new TreeNode("id-000", "我是ROOT");
        TreeNode node1_1 = new TreeNode("id-000", "一级节点-1");
        TreeNode node1_2 = new TreeNode("id-000", "一级节点-2");
        TreeNode node1_3 = new TreeNode("id-000", "一级节点-3");

        for (int i = 0; i < 20; i++) {
            TreeNode node = new TreeNode("id-" + i, "content" + i);
            node1_1.addChild(node);
        }
        rootNode.addChild(node1_1);
        rootNode.addChild(node1_2);
        rootNode.addChild(node1_3);

        mValues.add(node);
    }
}
