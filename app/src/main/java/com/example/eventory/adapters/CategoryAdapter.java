package com.example.eventory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventory.R;
import com.example.eventory.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<CategoryModel> categoryModelList;
    private Context context;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    public List<CategoryModel> getLayoutModelList() {
        return categoryModelList;
    }

    public void setLayoutModelList(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {


        CategoryModel categoryModel = categoryModelList.get(position);
        holder.category.setText(categoryModel.getCategory());


        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManager.setInitialPrefetchItemCount(categoryModel.getCardModelList().size());

        CardAdapter cardAdapter = new CardAdapter(context, categoryModel.getCardModelList());

        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(cardAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);
        holder.recyclerView.setHasFixedSize(true);
        cardAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View view) {
            super(view);

            category = view.findViewById(R.id.category_name);
            recyclerView = view.findViewById(R.id.recycle_view);

        }
    }
}
