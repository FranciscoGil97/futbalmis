package com.francisco.futbalmis;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.francisco.futbalmis.Fragments.FragmentElegirLigasFavoritas;
import com.francisco.futbalmis.Fragments.FragmentLigas;
import com.francisco.futbalmis.Servicios.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button acceder, registrar, googleButton, invitadoButton;
    LinearLayout auth;
    private FragmentTransaction FT;
    private final int GOOGLE_SIGN_IN = 100;
    private final int MAINACTIVITY_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Futbalmis);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        logout();
        email = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        acceder = findViewById(R.id.accederButton);
        registrar = findViewById(R.id.registrarButton);
        auth = findViewById(R.id.authLayout);
        googleButton = findViewById(R.id.googleButton);
        invitadoButton = findViewById(R.id.invitadoButton);

        acceder.setOnClickListener(this);
        registrar.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        invitadoButton.setOnClickListener(this);

        session();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == acceder.getId()) {
            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(command -> {
                    if (command.isSuccessful()) {
                        guardaSesion(command.getResult().getUser().getEmail(), null);
                        //mostrar ligas
                        irAMainActivity(command.getResult().getUser().getEmail(), null);
                    } else {
                        showAlert();
                    }
                });
            }
        } else if (v.getId() == registrar.getId()) {
            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(command -> {
                    if (command.isSuccessful()) {
                        guardaSesion(command.getResult().getUser().getEmail(), null);
                        //Elegir ligas favoritas
                        elegirLigas(email.getText().toString());
                    } else {
                        showAlert();
                    }
                });
            }
        } else if (v.getId() == googleButton.getId()) {
            GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleClient = GoogleSignIn.getClient(this, googleConf);
            googleClient.signOut();
            startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SIGN_IN);
        } else if (v.getId() == invitadoButton.getId()) {
            guardaSesion("Invitado",null);
            irAMainActivity("Invitado", null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                boolean[] existe = {false};
                db.collection("users").document(account.getEmail()).get().addOnSuccessListener(command -> {
                    existe[0] = command.exists();
                });
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, task1 -> {
                        if (task1.isSuccessful()) {
                            SharedPreferences prefs = getSharedPreferences("preferencias", MODE_PRIVATE);
                            String email = prefs.getString("email", null);
                            if (email == null)
                                guardaSesion(task1.getResult().getUser().getEmail(), task1.getResult().getUser().getPhotoUrl().toString());

                            if (existe[0])
                                irAMainActivity(task1.getResult().getUser().getEmail(), task1.getResult().getUser().getPhotoUrl().toString());
                            else elegirLigas(task1.getResult().getUser().getEmail());
                        } else {
                            showAlert();
                        }
                    });

                }


            } catch (ApiException ex) {
                System.err.println(ex.getMessage());
                showAlert();
            }
        } else if (requestCode == MAINACTIVITY_CODE) {
            finish();
        }
    }

    public void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("ERROR")
                .setMessage("Se ha producido un error autenticando al usuario")
                .setPositiveButton("Aceptar", null)
                .create()
                .show();
    }

    public void elegirLigas(String email) {
        cargarFragment(new FragmentElegirLigasFavoritas(this, email,new ArrayList<>()));
    }

    private void cargarFragment(Fragment f) {
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = findViewById(R.id.progressBarLigas);
        progressBar.setVisibility(View.GONE);
        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setVisibility(View.GONE);
        FT = getSupportFragmentManager().beginTransaction();
        FT.add(R.id.ligasFragment, f);
        FT.commit();
        Log.e("Numero de fragment", getSupportFragmentManager().getFragments().size() + "");
        FT = null;
    }

    public void irAMainActivity(String email, String urlFoto) {

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivityForResult(mainActivity, MAINACTIVITY_CODE);
    }

    public void guardaSesion(String email, String urlFoto) {
        SharedPreferences prefs = getSharedPreferences("preferencias", MODE_PRIVATE);
        prefs.edit()
                .putString("email", email)
                .putString("foto", urlFoto)
                .apply();
    }

    private void session() {
        SharedPreferences prefs = getSharedPreferences("preferencias", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String urlFoto = prefs.getString("foto", null);

        if (email != null) {
            Log.e("SESION INICIADA", email);
            auth.setVisibility(View.GONE);
            irAMainActivity(email, urlFoto);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}