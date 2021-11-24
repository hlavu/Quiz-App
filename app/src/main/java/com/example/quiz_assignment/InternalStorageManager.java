package com.example.quiz_assignment;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class InternalStorageManager {
    String filename = "result.txt";

    public void resetTheStorage(Activity activity){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE); // reset
            fileOutputStream.write("".getBytes());

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void saveNewResultInInternalPrivateFile(Activity activity, int numOfCorrect){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = activity.openFileOutput(filename, Context.MODE_APPEND); // continue writting
            fileOutputStream.write((String.valueOf(numOfCorrect) + "$").getBytes());

        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }

    }

    public ArrayList<String> getFromInternalPrivateFile(Activity activity)  {
        FileInputStream fileInputStream = null;
        int read;
        ArrayList<String> results = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        try {
            fileInputStream = activity.openFileInput(filename);
            while(( read = fileInputStream.read() )!= -1 ){
                buffer.append((char)read);
            }
            results  =  fromStringToList( buffer.toString());
        }catch (IOException ex){ex.printStackTrace();}
        finally {
            try {
                fileInputStream.close();
            }catch (IOException ex){ex.printStackTrace();}

        }
        return results;
    }

    private ArrayList<String> fromStringToList(String str){
        // there is a $ between tasks
        ArrayList<String> results = new ArrayList();
        int index = 0;
        for (int i = 0 ; i < str.toCharArray().length ; i++){
            if (str.toCharArray()[i] == '$'){
                String result = str.substring(index,i);
                results.add(result);
                index = i + 1;
            }
        }

        return results;

    }
}
