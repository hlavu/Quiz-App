package com.example.quiz_assignment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentProcess extends Fragment {
    TextView question;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View myView = inflater.inflate(R.layout.fragment_layout, container, false );
       question = (TextView) myView.findViewById(R.id.question);
       Question questionObj = getArguments().getParcelable("question");
       question.setText(questionObj.getQuestion());
       question.setBackgroundColor(Color.parseColor(questionObj.getColor()));
       return myView;
    }
}
