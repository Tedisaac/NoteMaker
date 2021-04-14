package com.example.notemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddNoteActivity extends AppCompatActivity {
    EditText titlelayout,descriptionlayout;
    Button addNote;

    String title,description;

    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titlelayout = findViewById(R.id.title1);
        descriptionlayout = findViewById(R.id.description1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("UserData");
        currentUser = firebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();

        addNote = findViewById(R.id.addnote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titlelayout.getText().toString();
                description = descriptionlayout.getText().toString();

                Map<String, String> userNote = new HashMap<>();
                userNote.put("title",title);
                userNote.put("description",description);

                reference.child(userId).push().setValue(userNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(AddNoteActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddNoteActivity.this,NoteActivity.class));
                            finish();
                        }else
                            {
                                Toast.makeText(AddNoteActivity.this, "Adding Failed", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }
        });
    }
}