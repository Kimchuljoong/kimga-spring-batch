package kr.co.kimga.batch.service.file;

import kr.co.kimga.batch.util.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SplitFilePartitionerTest {

    @Test
    void testMultipleFiles() throws IOException {
        List<File> files = createFiles(3);
        SplitFilePartitioner partitioner = new SplitFilePartitioner(files);

        Map<String, ExecutionContext> partitions = partitioner.partition(2);

        Assertions.assertThat(partitions).hasSize(3)
                .isEqualTo(Map.of(
                        "partition0", new ExecutionContext(Map.of("file", files.get(0))),
                        "partition1", new ExecutionContext(Map.of("file", files.get(1))),
                        "partition2", new ExecutionContext(Map.of("file", files.get(2)))
                ));

    }

    private static List<File> createFiles(int count) throws IOException {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(FileUtils.createTempFile("test", ".tmp"));
        }
        return files;
    }

    @Test
    void testEmpty() {
        SplitFilePartitioner partitioner = new SplitFilePartitioner(new ArrayList<>());

        Map<String, ExecutionContext> partitions = partitioner.partition(2);
        Assertions.assertThat(partitions).isEmpty();
    }
}