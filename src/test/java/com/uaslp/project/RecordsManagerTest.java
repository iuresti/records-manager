package com.uaslp.project;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class RecordsManagerTest {

    private Path fileCreated = Paths.get("test.rec");

    @AfterEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(fileCreated);
    }

    @Test
    public void givenAnEmptyRecord_whenSave_thenRecordIsSavedSuccessfully() throws IOException {
        // Given:
        RecordsManager recordsManager = new RecordsManager("test.rec", 5);
        GameRecord record = new GameRecord();

        record.setPlayerName("Ivan");
        record.setScore(30);

        // When:
        recordsManager.save(record);
        List<GameRecord> records = recordsManager.getRecords();

        // Then:
        assertThat(Files.exists(fileCreated)).isTrue();
        assertThat(records.size()).isEqualTo(1);
        assertThat(records).containsExactly(record);

    }

    @Test
    public void givenAnExistentRecord_whenSave_thenRecordIsSavedSuccessfully() throws IOException {
        // Given:
        RecordsManager recordsManager = new RecordsManager("test.rec", 5);

        recordsManager.save(new GameRecord("Ivan", 30));
        recordsManager.save(new GameRecord("Israel", 20));
        recordsManager.save(new GameRecord("Juan", 50));

        // When:
        List<GameRecord> records = recordsManager.getRecords();

        // Then:
        assertThat(Files.exists(fileCreated)).isTrue();
        assertThat(records.size()).isEqualTo(3);

        Iterator<GameRecord> iterator = records.iterator();

        validateGameRecord(iterator, "Juan", 50);
        validateGameRecord(iterator, "Ivan", 30);
        validateGameRecord(iterator, "Israel", 20);
    }

    @Test
    public void givenAnFullRecord_whenSave_thenRecordIsReplacedSuccessfully() throws IOException {
        // Given:
        RecordsManager recordsManager = new RecordsManager("test.rec", 5);

        recordsManager.save(new GameRecord("Ivan", 30));
        recordsManager.save(new GameRecord("Israel", 20));
        recordsManager.save(new GameRecord("Juan", 50));
        recordsManager.save(new GameRecord("Lupe", 80));
        recordsManager.save(new GameRecord("Iris", 10));

        // When:
        recordsManager.save(new GameRecord("Maria", 5));
        recordsManager.save(new GameRecord("Mario", 40));

        // Then:
        List<GameRecord> records = recordsManager.getRecords();
        assertThat(Files.exists(fileCreated)).isTrue();
        assertThat(records.size()).isEqualTo(5);

        Iterator<GameRecord> iterator = records.iterator();

        validateGameRecord(iterator, "Lupe", 80);
        validateGameRecord(iterator, "Juan", 50);
        validateGameRecord(iterator, "Mario", 40);
        validateGameRecord(iterator, "Ivan", 30);
        validateGameRecord(iterator, "Israel", 20);
    }

    private void validateGameRecord(Iterator<GameRecord> iterator, String playerName, int score) {
        GameRecord record;
        assertThat(iterator.hasNext()).isTrue();
        record = iterator.next();
        assertThat(record.getPlayerName()).isEqualTo(playerName);
        assertThat(record.getScore()).isEqualTo(score);
    }

}
