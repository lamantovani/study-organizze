package com.lasmantovani.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lasmantovani.organizze.R;
import com.lasmantovani.organizze.config.ConfiguracaoFirebase;
import com.lasmantovani.organizze.helper.DateCustom;
import com.lasmantovani.organizze.model.Movimentacao;
import com.lasmantovani.organizze.model.Usuario;

public class ReceitasActivity extends AppCompatActivity {

    private EditText campoValor;
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.editReceitaValor);
        campoData = findViewById(R.id.editReceitaData);
        campoCategoria = findViewById(R.id.editReceitaCategoria);
        campoDescricao = findViewById(R.id.editReceitaDescricao);

        campoData.setText(DateCustom.dataAtual());
        recuperarReceitaTotal();
    }

    public void recuperarReceitaTotal() {
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getUsuarioRef();

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void salvarReceita(View view) {
        if (validaCamposReceita()) {
            Movimentacao movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");
            movimentacao.salvar(data);
            atualizarReceita(valorRecuperado);
            finish();
        }
    }

    public boolean validaCamposReceita() {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDecricao = campoDescricao.getText().toString();

        if (textoValor.isEmpty()) {
            Toast.makeText(this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textoData.isEmpty()) {
            Toast.makeText(this, "Data não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textoCategoria.isEmpty()) {
            Toast.makeText(this, "Categoria não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (textoDecricao.isEmpty()) {
            Toast.makeText(this, "Descrição não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void atualizarReceita(Double receita) {
        ConfiguracaoFirebase.getUsuarioRef().child(ConfiguracaoFirebase.DATABASE_RECEITA_TOTAL).setValue(receitaTotal + receita);
    }
}