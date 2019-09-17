package com.example.books.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.books.R;
import com.example.books.models.entities.BookAndAuthor;
import com.example.books.viewmodels.DetailsViewModel;
import com.squareup.picasso.Picasso;


import static com.example.books.ui.LibraryFragment.BOOK_ID_KEY;

public class DetailsFragment extends Fragment {
    private long bookID;
    private DetailsViewModel detailsViewModel;
    private TextView ratingValue;
    private RatingBar ratingBar;
    private TextView releaseDate;
    private TextView price;
    private TextView title;
    private TextView author;
    private EditText description;
    private ImageView icon;
    private Group group;
    private ImageView editButton;
    private ImageView doneButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView() != null ? getView() : inflater.inflate(R.layout.detailed_fragment, container, false);
        ratingValue = view.findViewById(R.id.detailed_rating_value);
        ratingBar = view.findViewById(R.id.detailed_ratingBar);
        releaseDate = view.findViewById(R.id.detailed_date);
        price = view.findViewById(R.id.detailed_price);
        title = view.findViewById(R.id.detailed_title);
        description = view.findViewById(R.id.detailed_description);
        author = view.findViewById(R.id.detailed_author);
        icon = view.findViewById(R.id.details_icon);
        group = view.findViewById(R.id.group);
        editButton = view.findViewById(R.id.detailed_edit_button);
        doneButton = view.findViewById(R.id.detailed_edit_done);

        editButton.setOnClickListener(view1 -> {
           fadeOutAnimation(editButton);
           fadeInAnimation(doneButton);
            description.setEnabled(true);
            description.requestFocus();
        });

        doneButton.setOnClickListener(view1 -> {
            fadeOutAnimation(doneButton);
            fadeInAnimation(editButton);
            description.setEnabled(false);
            detailsViewModel.updateDescription(description.getText().toString());
        });

        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        detailsViewModel.getBookLiveData().observe(getViewLifecycleOwner(), bookObserver);
        detailsViewModel.getEventLiveData().observe(getViewLifecycleOwner(), eventObserver);

        if (getArguments() != null) {
            bookID = getArguments().getLong(BOOK_ID_KEY);
            detailsViewModel.getBook(bookID);
        }
        return view;
    }

    private Observer<String> eventObserver = s ->
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

    private Observer<BookAndAuthor> bookObserver = bookAndAuthor -> {
        ratingValue.setText(bookAndAuthor.getBook().getRating());
        ratingBar.setRating(Float.valueOf(bookAndAuthor.getBook().getRating()));
        releaseDate.setText(bookAndAuthor.getBook().getYear());
        price.setText(bookAndAuthor.getBook().getPrice());
        title.setText(bookAndAuthor.getBook().getTitle());
        author.setText(bookAndAuthor.getAuthorName());
        description.setText(bookAndAuthor.getBook().getDesc());
        Picasso.get().load(bookAndAuthor.getBook().getImage()).into(icon);
        group.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
    };

    private void fadeOutAnimation(View v){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        v.startAnimation(animation);
        v.setVisibility(View.GONE);
    }
    private void fadeInAnimation(View v){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        v.startAnimation(animation);
        v.setVisibility(View.VISIBLE);
    }
}
