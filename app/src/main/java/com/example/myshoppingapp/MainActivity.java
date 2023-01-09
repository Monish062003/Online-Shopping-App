package com.example.myshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myshoppingapp.adaptor.ProductAdapter;
import com.example.myshoppingapp.login_signin.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button mLogoutButton;
    private Button mAddProductButton;
    private RecyclerView mProductRecyclerView;
    private ProductAdapter mProductAdapter;
    private List<Product> mProductList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mProductReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mAddProductButton = (Button) findViewById(R.id.add_product_button);
        mProductRecyclerView = (RecyclerView) findViewById(R.id.product_recycler_view);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mProductReference = mFirestore.collection("products");

        mProductList = new ArrayList<>();
        mProductAdapter = new ProductAdapter(mProductList);
        mProductRecyclerView.setAdapter(mProductAdapter);
        mProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProductReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Product product = snapshot.toObject(Product.class);
                    mProductList.add(product);
                    mProductAdapter.notifyDataSetChanged();
                }
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}