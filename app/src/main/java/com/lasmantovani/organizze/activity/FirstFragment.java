package com.lasmantovani.organizze.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lasmantovani.organizze.R;
import com.lasmantovani.organizze.databinding.FragmentFirstBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private MaterialCalendarView calendarView;
    private TextView textoSaldacao, textoSaldo;

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

}