package org.jabref.logic.crawler;

import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jabref.logic.util.io.FileUtil;
import org.jabref.model.study.Study;
import org.jabref.model.study.StudyDatabase;
import org.jabref.model.study.StudyQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudyYamlParserTest {
    @TempDir
    static Path testDirectory;
    Study expectedStudy;

    @BeforeEach
    void setupStudy() throws Exception {
        Path destination = testDirectory.resolve("study.yml");
        URL studyDefinition = StudyYamlParser.class.getResource("study.yml");
        FileUtil.copyFile(Path.of(studyDefinition.toURI()), destination, true);

        List<String> authors = List.of("Jab Ref");
        String studyName = "TestStudyName";
        List<String> researchQuestions = List.of("Question1", "Question2");
        List<StudyQuery> queryEntries = List.of(new StudyQuery("Quantum"), new StudyQuery("Cloud Computing"), new StudyQuery("\"Software Engineering\""));
        Map<String, String> librarySpecificQuery = new HashMap<>();
        librarySpecificQuery.put("ArXiv", "quantum arxiv query");
        queryEntries.get(0).setQueryRefinements(librarySpecificQuery);
        List<StudyDatabase> libraryEntries = List.of(new StudyDatabase("Springer", true), new StudyDatabase("ArXiv", true), new StudyDatabase("IEEEXplore", false));

        expectedStudy = new Study(authors, studyName, researchQuestions, queryEntries, libraryEntries);
    }

    @Test
    public void parseStudyFileSuccessfully() throws Exception {
        Study study = new StudyYamlParser().parseStudyYamlFile(testDirectory.resolve("study.yml"));

        assertEquals(expectedStudy, study);
    }

    @Test
    public void writeStudyFileSuccessfully() throws Exception {
        new StudyYamlParser().writeStudyYamlFile(expectedStudy, testDirectory.resolve("study.yml"));

        Study study = new StudyYamlParser().parseStudyYamlFile(testDirectory.resolve("study.yml"));

        assertEquals(expectedStudy, study);
    }
}
