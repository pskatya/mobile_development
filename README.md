Было переделано приложение под архитектуру MVVM. Логику из Activity перенесли в ViewModel, добавили LiveData для автоматического обновления экрана. 
<img width="1917" height="1012" alt="image" src="https://github.com/user-attachments/assets/97006719-5118-4142-a691-b3db247874fe" />
Создали фабрику для правильного создания ViewModel. Теперь при повороте экрана данные не теряются, код стал лучше организован и проще для тестирования.
<img width="1919" height="987" alt="image" src="https://github.com/user-attachments/assets/5590ab90-d167-4af1-9449-0302508e9883" />
