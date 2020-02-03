package com.pfa.projet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextView membre;
    private FirebaseAuth mAuth;
    private EditText Log;
    private EditText Pass;
    private Button Conn;
    private String email;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        membre=findViewById(R.id.membre);
        Log=findViewById(R.id.LoginTxt);
        Pass=findViewById(R.id.PassTxt);
        Conn=findViewById(R.id.BtnConn);
        membre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NouveauMembre.class));
            }});
        Conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Log.getText().toString().trim();
                password=Pass.getText().toString().trim();
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Log.setError("Email non Valid");
                    Log.requestFocus();
                    return;
                }
                else if (password.isEmpty() || password.length() < 6) {
                    Pass.setError("le mot de passe il faut dépasser plus de 6 characteres");
                    Pass.requestFocus();
                    return;
                }
                else{

                    Toast.makeText(MainActivity.this, "teeeest", Toast.LENGTH_SHORT).show();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        startActivity(new Intent(MainActivity.this, ListeBanques.class));
                                        Toast.makeText(MainActivity.this, "Sign successfully", Toast.LENGTH_LONG).show();
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, "Vous netes pas un membre !!", Toast.LENGTH_LONG).show();

                                    }

                                }
                            });
                }

            }
        });
    }
}
