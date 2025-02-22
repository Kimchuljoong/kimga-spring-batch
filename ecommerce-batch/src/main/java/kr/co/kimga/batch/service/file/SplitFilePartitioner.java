package kr.co.kimga.batch.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SplitFilePartitioner implements Partitioner {

    private final List<File> splitFiles;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitionMap = new HashMap<>();
        for (int i = 0; i < splitFiles.size(); i++) {
            ExecutionContext context = new ExecutionContext();
            context.put("file", splitFiles.get(i));
            partitionMap.put("partition" + i, context);
        }

        return partitionMap;
    }
}
