package com.example.practice.movies.presentation

import com.example.practice.movies.presentation.model.CountryUiModel
import com.example.practice.movies.presentation.model.GenreUiModel
import com.example.practice.movies.presentation.model.MovieUiModel

object MockData {
    fun getMovies(): List<MovieUiModel> = listOf(
        MovieUiModel(
            id = 1,
            name = "Интерстеллар",
            alternativeName = "Interstellar",
            description = "Когда засуха приводит человечество к продовольственному кризису, коллектив исследователей и учёных отправляется сквозь червоточину в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека и найти планету с подходящими для человечества условиями.",
            year = 2014,
            posterUrl = "https://avatars.mds.yandex.net/get-mpic/11763878/2a0000018b4350ed816ef542700f80914efa/orig",
            genres = listOf(
                GenreUiModel(name = "фантастика"),
                GenreUiModel(name = "драма"),
                GenreUiModel(name = "приключения")
            ),
            countries = listOf(
                CountryUiModel(name = "США"),
                CountryUiModel(name = "Великобритания")
            )
        ),
        MovieUiModel(
            id = 2,
            name = null,
            alternativeName = "The Godfather",
            description = "Криминальная сага, повествующая о нью-йоркской сицилийской мафиозной семье Корлеоне. Фильм охватывает период 1945-1955 годов.",
            year = 1972,
            posterUrl = "https://www.boredpanda.com/blog/wp-content/uploads/2020/10/movie-missing-point-description-21-5f97d772367a8__700.jpg",
            genres = listOf(
                GenreUiModel(name = "криминал"),
                GenreUiModel(name = "драма")
            ),
            countries = listOf(
                CountryUiModel(name = "США")
            )
        ),
        MovieUiModel(
            id = 3,
            name = "Побег из Шоушенка",
            alternativeName = null,
            description = "Бухгалтер Энди Дюфрейн обвинён в убийстве собственной жены и её любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решётки.",
            year = 1994,
            posterUrl = "https://static.tvtropes.org/pmwiki/pub/images/6fa79a3251cbf9c1c929aaec71ebb1309c57566a61d490045de285525914f285_ur12002c1600_ri__waifu2x_art_noise1.png",
            genres = listOf(
                GenreUiModel(name = "драма")
            ),
            countries = listOf(
                CountryUiModel(name = "США")
            )
        ),
        MovieUiModel(
            id = 4,
            name = "Назад в будущее",
            alternativeName = "Back to the Future",
            description = "Подросток Марти с помощью машины времени, сооружённой его другом-профессором доком Брауном, попадает из 80-х в далёкие 50-е. Там он встречается со своими будущими родителями, ещё подростками, и другом-профессором, совсем молодым.",
            year = 1985,
            posterUrl = null,
            genres = listOf(
                GenreUiModel(name = "фантастика"),
                GenreUiModel(name = "комедия"),
                GenreUiModel(name = "приключения"),
            ),
            countries = listOf(
                CountryUiModel(name = "США")
            )
        ),
        MovieUiModel(
            id = 5,
            name = null,
            alternativeName = null,
            description = "Несколько связанных между собой историй из жизни бандитов Лос-Анджелеса.",
            year = 1994,
            posterUrl = "https://avatars.mds.yandex.net/get-mpic/11763878/2a0000018b432ab71a13344429f98285e6ae/orig",
            genres = listOf(
                GenreUiModel(name = "криминал"),
                GenreUiModel(name = "драма")
            ),
            countries = listOf(
                CountryUiModel(name = "США")
            )
        ),
        MovieUiModel(
            id = 6,
            name = "Зеленая книга",
            alternativeName = "Green Book",
            description = "1960-е годы. После закрытия нью-йоркского ночного клуба на ремонт вышибала Тони по прозвищу Болтун ищет подработку на пару месяцев. Как раз в это время чернокожий пианист Дон Ширли собирается в турне по южным штатам, где ему нужен водитель и телохранитель.",
            year = 2018,
            posterUrl = "https://i.scdn.co/image/ab67616d0000b273a8305711307027f3b0c6d4eb",
            genres = listOf(
                GenreUiModel(name = "драма"),
                GenreUiModel(name = "биография"),
                GenreUiModel(name = "комедия")
            ),
            countries = emptyList()
        ),
        MovieUiModel(
            id = 7,
            name = "Леон",
            alternativeName = "Léon: The Professional",
            description = null,
            year = 1994,
            posterUrl = "https://m.media-amazon.com/images/I/71PY8+SZeDL.jpg",
            genres = listOf(
                GenreUiModel(name = "криминал"),
                GenreUiModel(name = "драма"),
                GenreUiModel(name = "боевик")
            ),
            countries = listOf(
                CountryUiModel(name = "Франция"),
                CountryUiModel(name = "США")
            )
        ),
        MovieUiModel(
            id = 8,
            name = "Джокер",
            alternativeName = "Joker",
            description = "Готэм, начало 1980-х годов. Комик Артур Флек живёт с больной матерью, которая с детства учит его «ходить с улыбкой». Пытаясь нести в мир хорошее и дарить людям радость, Артур сталкивается с человеческой жестокостью и постепенно приходит к выводу, что этот мир получит от него не добрую улыбку, а ухмылку злодея Джокера.",
            year = 2019,
            posterUrl = "https://i.pinimg.com/736x/14/b3/8d/14b38df3a0cb181b4f465bfa54e29f11.jpg",
            genres = listOf(
                GenreUiModel(name = "драма"),
                GenreUiModel(name = "криминал"),
                GenreUiModel(name = "триллер")
            ),
            countries = listOf(
                CountryUiModel(name = "США"),
                CountryUiModel(name = "Канада")
            )
        ),
        MovieUiModel(
            id = 9,
            name = "Аватар",
            alternativeName = "Avatar",
            description = "Бывший морпех Джейк Салли прикован к инвалидному креслу. Несмотря на немощное тело, Джейк в душе по-прежнему остается воином. Он получает задание совершить путешествие в несколько световых лет к базе землян на планете Пандора, где корпорации добывают редкий минерал, имеющий огромное значение для выхода Земли из энергетического кризиса.",
            year = 2009,
            posterUrl = "https://images.kinorium.com/movie/poster/372833/w1500_53524769.jpg",
            genres = listOf(
                GenreUiModel(name = "фантастика"),
                GenreUiModel(name = "приключения"),
                GenreUiModel(name = "боевик")
            ),
            countries = listOf(
                CountryUiModel(name = "США"),
                CountryUiModel(name = "Великобритания")
            )
        ),
        MovieUiModel(
            id = 10,
            name = "Остров проклятых",
            alternativeName = "Shutter Island",
            description = "Два американских судебных пристава отправляются на один из островов в штате Массачусетс, чтобы расследовать исчезновение пациентки клиники для умалишенных преступников. При проведении расследования им придется столкнуться с паутиной лжи, обрушившимся ураганом и бунтом обитателей клиники.",
            year = 2010,
            posterUrl = "https://images.kinorium.com/movie/poster/431590/w1500_37534317.jpg",
            genres = listOf(
                GenreUiModel(name = "триллер"),
                GenreUiModel(name = "детектив"),
                GenreUiModel(name = "драма")
            ),
            countries = listOf(
                CountryUiModel(name = "США")
            )
        )
    )
}