package com.lasmantovani.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lasmantovani.organizze.helper.Base64Custom;

public class ConfiguracaoFirebase {

    public static final String DATABASE_USUARIOS = "usuarios";
    public static final String DATABASE_MOVIMENTACAO = "movimentacao";
    public static final String DATABASE_DESPESA_TOTAL = "despesaTotal";
    public static final String DATABASE_RECEITA_TOTAL = "receitaTotal";

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    // Retorna a instância do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase() {
        if (firebase == null)
            firebase = FirebaseDatabase.getInstance().getReference();
        return  firebase;
    }

    // Retorna a instância do FirebaseAuth
    public static FirebaseAuth getFarebaseAutenticacao() {
        if (autenticacao == null)
            autenticacao = FirebaseAuth.getInstance();
        return autenticacao;
    }

    // Retorna o idUsuario em Base64
    public static String getIdUsuario() {
        return Base64Custom.codificarBase64(getFarebaseAutenticacao().getCurrentUser().getEmail());
    }

    // Retorna o usurarioRef
    public static DatabaseReference getUsuarioRef() {
        return getFirebaseDatabase().child(ConfiguracaoFirebase.DATABASE_USUARIOS).child(getIdUsuario());
    }
}
