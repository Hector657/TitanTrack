package com.titantrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.titantrack.R;
import com.titantrack.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // --- Intercepción de arranque: si ya hay sesión, saltar al Dashboard ---
        if (session.isLoggedIn()) {
            goToDashboard();
            return; // no cargamos la pantalla de login
        }

        setContentView(R.layout.activity_login);

        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView goRegister = findViewById(R.id.txtGoRegister);

        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            boolean valid = true;

            // Validación: correo
            if (TextUtils.isEmpty(email)) {
                inputEmail.setError("El correo es obligatorio");
                valid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                inputEmail.setError("Formato de correo no válido");
                valid = false;
            }

            // Validación: contraseña
            if (TextUtils.isEmpty(password)) {
                inputPassword.setError("La contraseña es obligatoria");
                valid = false;
            } else if (password.length() < 6) {
                inputPassword.setError("Mínimo 6 caracteres");
                valid = false;
            }

            // Si todo es válido: guardar sesión y entrar
            if (valid) {
                session.setLogin(email);
                goToDashboard();
            }
        });

        goRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void goToDashboard() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish(); // cierra el Login para que "atrás" no vuelva a él
    }
}