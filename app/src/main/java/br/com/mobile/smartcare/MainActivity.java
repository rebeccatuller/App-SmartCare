package br.com.mobile.smartcare;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import br.com.mobile.smartcare.modelo.DAO;

public class MainActivity extends AppCompatActivity {

    EditText senhaET;
    EditText emailET;

    ImageButton entrar;
    ImageButton cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        emailET = (EditText) findViewById(R.id.editTextEmailLogin);
        senhaET = (EditText) findViewById(R.id.editTextSenhaLogin);


        entrar = (ImageButton) findViewById(R.id.buttonEntrar);
        cadastrar = (ImageButton) findViewById(R.id.buttonCadastrar);


        entrar.getBackground().setColorFilter(getResources().getColor(R.color.buttons), PorterDuff.Mode.MULTIPLY);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String senha = senhaET.getText().toString();

                boolean vazio = false;

                if(email.isEmpty()){
                    emailET.setError("Campo Obrigatório");
                    vazio = true;
                }

                if(senha.isEmpty()){
                    senhaET.setError("Campo Obrigatório");
                    vazio = true;
                }

                if(!vazio){
                    DAO dao = new DAO(getThis());

                    if (dao.login(email, senha)){
                        Intent intent = new Intent(getThis(), ConsultaActivity.class);
                        startActivity(intent);
                    } else {
                        emailET.setError("Usuário ou senha incorretos");
                    }
                }


            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getThis(), NewUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private MainActivity getThis(){
        return this;
    }
}
