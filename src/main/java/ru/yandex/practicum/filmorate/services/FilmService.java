package ru.yandex.practicum.filmorate.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    @Getter
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Optional<Film> addLike(int filmID, int userId, boolean like) {
        if (filmID <= 0 || userId <= 0) {
            log.error("Ошибка, лайк не добавлен:{} - filmID, {} - userId", filmID, userId);
            throw new ValidationException("invalid data");
        }
        if (!filmStorage.getFilms().containsKey(filmID) || !userStorage.getUsers().containsKey(userId)) {
            log.error("Ошибка, фильм или пользователь не найдены:{} - filmID, {} - userId", filmID, userId);
            throw new FilmNotFoundException("Film or user is not found");
        }
        Film film = filmStorage.getFilms().get(filmID);

        if (film == null) {
            log.error("Ошибка, фильм не найден:{}", filmID);
            throw new FilmNotFoundException("Film is not found");
        }

        Set<Integer> likeScore = film.getLikeScore();

        if (like) {
            likeScore.add(userId);
            film.setLikeScore(likeScore);
            return Optional.of(film);
        }

        likeScore.remove(userId);
        film.setLikeScore(likeScore);
        return Optional.of(film);
    }

    public List<Film> getTenTheMostPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("invalid data");
        }
        return filmStorage.getAllFilms().stream().sorted(Comparator.<Film>comparingInt(film -> film.getLikeScore().size()).reversed()) // Explicit type declaration
                .limit(count).collect(Collectors.toList());
    }
}
