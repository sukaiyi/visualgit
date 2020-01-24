package com.sukaiyi.visualgit.common;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GitLogParserTest {

    @Test
    void testParse(){
        String repoPath = "C:\\Users\\HT-Dev\\Documents\\Projects\\hms";
        List<GitCommitInfo> commitInfos = Optional.of(repoPath)
                .map(GitLogFetcher::fetch)
                .map(GitLogParser::parse)
                .orElse(Collections.emptyList());
        for (GitCommitInfo commitInfo : commitInfos) {
            assertNotNull(commitInfo);
            assertNotNull(commitInfo.getAuthor());
            assertNotNull(commitInfo.getAuthor().getTimestamp());
            assertNotNull(commitInfo.getInsertions());
            assertTrue(commitInfo.getInsertions() >= 0);
            assertNotNull(commitInfo.getDeletions());
            assertTrue(commitInfo.getDeletions() >= 0);
        }
    }

}