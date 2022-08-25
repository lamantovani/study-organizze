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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lasmantovani.organizze.R;
import com.lasmantovani.organizze.config.ConfiguracaoFirebase;
import com.lasmantovani.organizze.databinding.FragmentFirstBinding;
import com.lasmantovani.organizze.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;


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
        configuraCalendarView();
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarResumo();
    }

    public void recuperarResumo() {
        usuarioRef = ConfiguracaoFirebase.getUsuarioRef();
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal ;

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

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Log.i("CalendarView", (date.getMonth() + 1) + "/" + date.getYear());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Evento", "Evento foi removido");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
    }
}