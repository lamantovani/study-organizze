package com.lasmantovani.organizze.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lasmantovani.organizze.R;
import com.lasmantovani.organizze.adapter.AdapterMovimentacao;
import com.lasmantovani.organizze.config.ConfiguracaoFirebase;
import com.lasmantovani.organizze.databinding.FragmentFirstBinding;
import com.lasmantovani.organizze.model.Movimentacao;
import com.lasmantovani.organizze.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FirstFragment extends Fragment {

    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private FragmentFirstBinding binding;
    private MaterialCalendarView calendarView;
    private TextView textoSaldacao, textoSaldo;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFarebaseAutenticacao();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textoSaldacao = view.findViewById(R.id.textSaldacao);
        textoSaldo = view.findViewById(R.id.textSaldo);
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerMovimentos);
        configuraCalendarView();

        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    public void recuperarMovimentacao() {
        movimentacaoRef = firebaseRef.child(ConfiguracaoFirebase.DATABASE_MOVIMENTACAO)
                .child(ConfiguracaoFirebase.getIdUsuario())
                .child(mesAnoSelecionado);

        Log.i("DADOS", "mes: " + mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movimentacoes.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Log.i("DADOS", "retorno: " + dados.toString());
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacoes.add(movimentacao);
                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void recuperarResumo() {
        usuarioRef = ConfiguracaoFirebase.getUsuarioRef();
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultado = decimalFormat.format(resumoUsuario);

                textoSaldacao.setText("Olá, " + usuario.getNome());
                textoSaldo.setText("R$ " + resultado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.i("Evento", "Evento foi adicionado");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void configuraCalendarView() {
        String meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Stembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);

        String semanas[] = {"Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"};
        calendarView.setWeekDayLabels(semanas);

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", dataAtual.getMonth());
        mesAnoSelecionado = mesSelecionado + String.valueOf(dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mesAnoSelecionado = String.format("%02d%d", date.getMonth(), date.getYear());
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacao();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacao();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Evento", "Evento foi removido");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        usuarioRef.removeEventListener(valueEventListenerMovimentacoes);
    }
}