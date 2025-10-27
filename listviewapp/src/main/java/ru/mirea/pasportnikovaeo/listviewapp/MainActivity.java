package ru.mirea.pasportnikovaeo.listviewapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView booksListView;
    private List<Book> booksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksListView = findViewById(R.id.booksListView);
        initializeBooksList();
        setupAdapter();
    }

    private void initializeBooksList() {
        booksList = new ArrayList<>();

        // Добавляем более 30 книг для чтения на 30 лет
        booksList.add(new Book("Фёдор Достоевский", "Братья Карамазовы", 1880));
        booksList.add(new Book("Лев Толстой", "Война и мир", 1869));
        booksList.add(new Book("Михаил Булгаков", "Мастер и Маргарита", 1967));
        booksList.add(new Book("Антуан де Сент-Экзюпери", "Маленький принц", 1943));
        booksList.add(new Book("Джордж Оруэлл", "1984", 1949));
        booksList.add(new Book("Олдос Хаксли", "О дивный новый мир", 1932));
        booksList.add(new Book("Рэй Брэдбери", "451 градус по Фаренгейту", 1953));
        booksList.add(new Book("Габриэль Гарсия Маркес", "Сто лет одиночества", 1967));
        booksList.add(new Book("Фрэнсис Скотт Фицджеральд", "Великий Гэтсби", 1925));
        booksList.add(new Book("Эрнест Хемингуэй", "Старик и море", 1952));
        booksList.add(new Book("Харпер Ли", "Убить пересмешника", 1960));
        booksList.add(new Book("Джон Р. Р. Толкин", "Властелин колец", 1954));
        booksList.add(new Book("Джоан Роулинг", "Гарри Поттер и философский камень", 1997));
        booksList.add(new Book("Стивен Кинг", "Зелёная миля", 1996));
        booksList.add(new Book("Пауло Коэльо", "Алхимик", 1988));
        booksList.add(new Book("Айн Рэнд", "Атлант расправил плечи", 1957));
        booksList.add(new Book("Владимир Набоков", "Лолита", 1955));
        booksList.add(new Book("Александр Солженицын", "Архипелаг ГУЛАГ", 1973));
        booksList.add(new Book("Умберто Эко", "Имя розы", 1980));
        booksList.add(new Book("Курт Воннегут", "Бойня номер пять", 1969));
        booksList.add(new Book("Джек Лондон", "Мартин Иден", 1909));
        booksList.add(new Book("Иван Тургенев", "Отцы и дети", 1862));
        booksList.add(new Book("Николай Гоголь", "Мёртвые души", 1842));
        booksList.add(new Book("Александр Пушкин", "Евгений Онегин", 1833));
        booksList.add(new Book("Михаил Лермонтов", "Герой нашего времени", 1840));
        booksList.add(new Book("Антон Чехов", "Рассказы", 1904));
        booksList.add(new Book("Иван Бунин", "Тёмные аллеи", 1943));
        booksList.add(new Book("Александр Дюма", "Граф Монте-Кристо", 1844));
        booksList.add(new Book("Виктор Гюго", "Отверженные", 1862));
        booksList.add(new Book("Оноре де Бальзак", "Человеческая комедия", 1842));
        booksList.add(new Book("Гюстав Флобер", "Госпожа Бовари", 1856));
        booksList.add(new Book("Эмиль Золя", "Жерминаль", 1885));
        booksList.add(new Book("Марсель Пруст", "В поисках утраченного времени", 1913));
        booksList.add(new Book("Джейн Остин", "Гордость и предубеждение", 1813));
        booksList.add(new Book("Шарлотта Бронте", "Джейн Эйр", 1847));
        booksList.add(new Book("Эмили Бронте", "Грозовой перевал", 1847));
        booksList.add(new Book("Чарльз Диккенс", "Большие надежды", 1861));
        booksList.add(new Book("Марк Твен", "Приключения Гекльберри Финна", 1884));
        booksList.add(new Book("Герман Мелвилл", "Моби Дик", 1851));
        booksList.add(new Book("Данте Алигьери", "Божественная комедия", 1320));
    }

    private void setupAdapter() {
        ArrayAdapter<Book> adapter = new ArrayAdapter<Book>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                booksList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Book book = getItem(position);
                text1.setText(book.getTitle());
                text2.setText(book.getAuthor() + " (" + book.getYear() + ")");

                return view;
            }
        };

        booksListView.setAdapter(adapter);
    }
}