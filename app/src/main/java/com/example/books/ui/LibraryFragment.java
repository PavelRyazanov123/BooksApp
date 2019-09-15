package com.example.books.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.books.adapters.LibraryAdapter;
import com.example.books.viewmodels.LibraryViewModel;
import com.example.books.R;
import com.example.books.models.entities.Book;

import java.util.List;


public class LibraryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LibraryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private LibraryAdapter libraryAdapter;
    private LibraryViewModel libraryViewModel;
    private SwipeRefreshLayout refreshLayout;
    public static final String BOOK_ID_KEY = "BOOK_ID_KEY";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView() != null ? getView() : inflater.inflate(R.layout.fragment_library, container, false);
        recyclerView = view.findViewById(R.id.library_recycler);
        refreshLayout = view.findViewById(R.id.library_refresh);
        refreshLayout.setOnRefreshListener(this);
        libraryAdapter = new LibraryAdapter();
        libraryAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(libraryAdapter);

        libraryViewModel = ViewModelProviders.of(this).get(LibraryViewModel.class);
        libraryViewModel.getBookLiveData().observe(getViewLifecycleOwner(), booksObserver);
        libraryViewModel.getRefreshingFlag().observe(getViewLifecycleOwner(), refreshingObserver);
        return view;
    }

    private Observer<Boolean> refreshingObserver = isRefreshing ->
            refreshLayout.setRefreshing(isRefreshing);

    private Observer<List<Book>> booksObserver = bookList -> {
        libraryAdapter.setBookList(bookList);
        libraryAdapter.notifyDataSetChanged();
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        libraryViewModel.loadBooks();
    }

    @Override
    public void click(long id) {
        if (getActivity() != null) {
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(BOOK_ID_KEY, id);
            detailsFragment.setArguments(bundle);
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
