package com.example.quiz_assignment;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    String question;
    String answer;
    String color;

    public Question(String question,  String answer, String color) {
        this.question = question;
        this.answer = answer;
        this.color = color;
    }

    protected Question(Parcel in) {
        question = in.readString();
        answer = in.readString();
        color = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getColor() {
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(answer);
        parcel.writeString(color);
    }
}
