package boring.owl.parapp.entities.response


object Message {
    const val OK = "ОК"
    const val INTERNAL = "Внутренняя ошибка"
    const val LOGIN = "Имя пользователя или пароль неверны"
    const val LOGIN_SHORT = "Имя пользователя слишком короткое"
    const val PASSWORD_SHORT = "Пароль слишком короткий"
    const val USERNAME_SHORT = "Имя пользователя слишком короткое"
    const val USER_EXISTS = "Пользователь с таким именем уже существует"
    const val USER_REGISTERED = "Пользователь успешно зарегистрирован"
    const val NOT_FOUND = "Объект не найден"
    const val DELETED = "Объект успешно удален"
    const val ADDED = "Объект успешно добавлен"
    const val UPDATED = "Объект успешно обновлен"
    const val UNAUTHORIZED = "Пользователь не авторизован"
}
