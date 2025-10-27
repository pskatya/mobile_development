package ru.mirea.pasportnikovaeo.recyclerviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<Event> events = getEventsData();
        EventAdapter adapter = new EventAdapter(events, this);
        recyclerView.setAdapter(adapter);
    }

    private List<Event> getEventsData() {
        List<Event> events = new ArrayList<>();

        events.add(new Event("Великая Октябрьская революция",
                "Революция в России, приведшая к установлению советской власти",
                "ic_revolution", 1917));

        events.add(new Event("Первый полет в космос",
                "Юрий Гагарин стал первым человеком, полетевшим в космос",
                "ic_space", 1961));

        events.add(new Event("Вторая мировая война",
                "Крупнейший вооружённый конфликт в истории человечества",
                "ic_war", 1939));

        events.add(new Event("Распад СССР",
                "Прекращение существования Советского Союза",
                "ic_ussr", 1991));

        events.add(new Event("Изобретение телефона",
                "Александр Белл запатентовал первый телефон",
                "ic_phone", 1876));

        events.add(new Event("Открытие Америки",
                "Христофор Колумб открыл Америку",
                "ic_america", 1492));

        events.add(new Event("Французская революция",
                "Крупнейшая трансформация социальной и политической систем Франции",
                "ic_france", 1789));

        events.add(new Event("Первый компьютер",
                "Создание первой электронно-вычислительной машины",
                "ic_computer", 1946));

        events.add(new Event("Высадка на Луну",
                "Американские астронавты высадились на Луну",
                "ic_moon", 1969));

        events.add(new Event("Падение Берлинской стены",
                "Объединение Восточной и Западной Германии",
                "ic_berlin", 1989));

        return events;
    }
}