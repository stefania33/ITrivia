package com.example.triviaapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.triviaapp.Common.Common;
import com.example.triviaapp.Interface.ItemClickListener;
import com.example.triviaapp.ViewHolder.CategoryView;
import com.example.triviaapp.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    private RecyclerView listCategory;
    private FirebaseRecyclerAdapter<Category, CategoryView> adapter;
    private DatabaseReference categories;


    static CategoryFragment newInstance() {
        return new CategoryFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categories = FirebaseDatabase.getInstance().getReference("Category");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFragment = inflater.inflate(R.layout.fragment_categories, container, false);

        listCategory = myFragment.findViewById(R.id.listCategory);
        listCategory.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(), 2);
        listCategory.setLayoutManager(gridLayoutManager);

        loadCategories();
        return myFragment;
    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryView>(Category.class, R.layout.category_layout, CategoryView.class, categories) {
            @Override
            protected void populateViewHolder(CategoryView categoryView, final Category category, int i) {
                categoryView.category_name.setText(category.getName());
                Picasso.get().load(category.getImage()).into(categoryView.category_image);

                categoryView.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean click) {
                        Intent startGame = new Intent(getActivity(), Start.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName = category.getName();
                        startActivity(startGame);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);
    }

}


