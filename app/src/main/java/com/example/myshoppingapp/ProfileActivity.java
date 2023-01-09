package com.example.myshoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myshoppingapp.adaptor.CartAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView mUsernameTextView;
    private TextView mEmailTextView;
    private RecyclerView mCartRecyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private CartAdapter mCartAdapter;
    private List<Product> mCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUsernameTextView = (TextView) findViewById(R.id.username_text_view);
        mEmailTextView = (TextView) findViewById(R.id.email_text_view);
        mCartRecyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        String userId = mAuth.getCurrentUser().getUid();
        mFirestore.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                mUsernameTextView.setText(user.getUsername());
                mEmailTextView.setText(user.getEmail());
            }
        });

        mCartList = new ArrayList<>();
        mCartAdapter = new CartAdapter(mCartList);
        mCartRecyclerView.setAdapter(mCartAdapter);
        mCartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mFirestore.collection("users").document(userId).collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Product product = snapshot.toObject(Product.class);
                    mCartList.add(product);
                    mCartAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}