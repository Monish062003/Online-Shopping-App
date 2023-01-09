package com.example.myshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewOrderActivity extends AppCompatActivity {
    private TextView mProductNameTextView;
    private TextView mProductPriceTextView;
    private TextView mProductQuantityTextView;
    private Button mAddToCartButton;
    private Button mBuyNowButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        mProductNameTextView = (TextView) findViewById(R.id.product_name_text_view);
        mProductPriceTextView = (TextView) findViewById(R.id.product_price_text_view);
        mProductQuantityTextView = (TextView) findViewById(R.id.product_quantity_text_view);
        mAddToCartButton = (Button) findViewById(R.id.add_to_cart_button);
        mBuyNowButton = (Button) findViewById(R.id.buy_now_button);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        final String productId = intent.getStringExtra("product_id");

        mFirestore.collection("products").document(productId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Product product = documentSnapshot.toObject(Product.class);
                mProductNameTextView.setText(product.getName());
                mProductPriceTextView.setText(product.getPrice());
                mProductQuantityTextView.setText(product.getQuantity());

                mAddToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userId = mAuth.getCurrentUser().getUid();
                        mFirestore.collection("users").document(userId).collection("cart").add(product);
                        startActivity(new Intent(ViewOrderActivity.this, ProfileActivity.class));
                    }
                });

                mBuyNowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent paymentIntent = new Intent(ViewOrderActivity.this, PaymentActivity.class);
                        paymentIntent.putExtra("product_id", productId);
                        paymentIntent.putExtra("product_price", product.getPrice());
                        startActivity(paymentIntent);
                    }
                });
            }
        });
    }
}