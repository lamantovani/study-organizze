package com.lasmantovani.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lasmantovani.organizze.R;
import com.lasmantovani.organizze.config.ConfiguracaoFirebase;
import com.lasmantovani.organizze.helper.DateCustom;
import com.lasmantovani.organizze.model.Movimentacao;
import com.lasmantovani.organizze.model.Usuario;

public class DespesasActivity extends AppCompatActivity {

    private EditText campoValor;
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFarebaseAutenticacao();
    private double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editDespesaValor);
        campoData = findViewById(R.id.editDespesaData);
        campoCategoria = findViewById(R.id.editDespesaCategoria);
        campoDescricao = findViewById(R.id.editDespesaDescricao);

        campoData.setText(DateCustom.dataAtual());
        recuperarDespesaTotal();

    }

    public void salvarDespesa(View view) {
        if (validarCamposDespesas()) {
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d");
            movimentacao.salvar(data);
            atualizarDespesa(valorRecuperado);
            finish();
        }
    }

    public boolean validarCamposDespesas() {

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (textoValor.isEmpty()) {
            Toast.makeText(this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textoData.isEmpty())  {
            Toast.makeText(this, "Data não foi preenchida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textoCategoria.isEmpty()) {
            Toast.makeText(this, "Categoria não foi preenchida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textoDescricao.isEmpty()){
            Toast.makeText(this, "Descrição não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void recuperarDespesaTotal() {

        DatabaseReference usuarioRef = ConfiguracaoFirebase.getUsuarioRef();

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void atualizarDespesa(Double despesa) {
        ConfiguracaoFirebase.getUsuarioRef().child(ConfiguracaoFirebase.DATABASE_DESPESA_TOTAL).setValue(despesaTotal + despesa);
    }
}