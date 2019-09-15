package com.example.books.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.models.entities.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BooksViewHolder> {

    private List<Book> bookList = new ArrayList<>();
    private OnItemClickListener listener;

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibraryAdapter.BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BooksViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.BooksViewHolder holder, int position) {
        holder.subTitle.setText(bookList.get(position).getSubtitle());
        holder.title.setText(bookList.get(position).getTitle());
        Picasso.get().load(bookList.get(position).getImage()).into(holder.icon);
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView title;
        final TextView subTitle;

        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.library_icon);
            title = itemView.findViewById(R.id.library_title);
            subTitle = itemView.findViewById(R.id.library_subtitle);

            itemView.setOnClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION && listener != null) {
                    listener.click(bookList.get(getAdapterPosition()).getBookId());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void click(long id);
    }

}
