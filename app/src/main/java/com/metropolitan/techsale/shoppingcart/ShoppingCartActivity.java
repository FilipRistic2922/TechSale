package com.metropolitan.techsale.shoppingcart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.metropolitan.techsale.R;
import com.metropolitan.techsale.items.model.Item;
import com.metropolitan.techsale.payment.PaymentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textTotal;
    private List<Item> itemList = new ArrayList<>();
    private ListSection<Item> itemSection;
    private Consumer<List<Item>> cartListener = (items -> {
        double total = 0;
        for (Item i : items) {
            total += i.getPrice();
        }
        itemSection.clear();
        itemSection.addAll(items);
        if (textTotal != null)
            textTotal.setText("Total: " + total + "");
        if (recyclerView != null && recyclerView.getAdapter() != null)
            recyclerView.getAdapter().notifyDataSetChanged();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewShoppingCart);
        textTotal = findViewById(R.id.textViewTotal);
        MultiViewAdapter adapter = new MultiViewAdapter();
        adapter.registerItemBinders(new CartItemBinder());
        itemSection = new ListSection<>();

        List<Item> items = ShoppingCart.getInstance(this).getItems();
        Toast.makeText(this, "Items in cart: " + items.size(), Toast.LENGTH_SHORT).show();

        itemList.clear();
        itemList.addAll(items);
        itemSection.addAll(itemList);
        adapter.addSection(itemSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);

        double total = 0;
        for (Item i : items) {
            total += i.getPrice();
        }
        textTotal.setText(String.format("Total: %s", total));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("random_tag", "On Restart");
        cartListener.accept(ShoppingCart.getInstance(this).getItems());
        ShoppingCart.getInstance(this).addListener(cartListener);
    }


    public void onClickEmpty(View view) {
        ShoppingCart.getInstance(this).removeAll();
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        Toast.makeText(this, "Shopping Cart is cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void pay(View view){
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("Total", Double.valueOf(textTotal.getText().toString().substring(7)));
        startActivity(intent);
    }
}
