package net.kimleo.rec;

import kotlin.Pair;
import net.kimleo.rec.init.Initializer;
import net.kimleo.rec.loader.strategy.DefaultLoadingStrategy;
import net.kimleo.rec.repository.RecRepository;
import net.kimleo.rec.rule.Result;
import net.kimleo.rec.rule.RuleLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.exit;
import static java.nio.file.Files.lines;
import static net.kimleo.rec.scripting.Scripting.runjs;
import static net.kimleo.rec.util.Sys.die;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            runOverPath(".");
            return;
        }

        switch (args[0]) {
            case "init":
                if (args.length < 2) {
                    die("You need provide a file to initialize!");
                }
                String fileName = args[1];
                if (Files.exists(Paths.get(fileName))) {
                    die("File %s cannot be found", fileName);
                }

                HashMap<String, String> properties = new HashMap<>();
                Arrays.stream(args).skip(2).forEach(property -> {
                    if (!property.contains("=")) die("Unexpected parameter format, should be <param>=<value>.");
                    String[] parts = property.split("=");
                    properties.put(parts[0].trim(), parts[1].trim());
                });

                new Initializer(fileName, properties).init();
                break;
            case "js":
                if (args.length != 2) {
                    die("You need to provide the script file.");
                }

                File file = new File(args[1]);

                if (!file.exists()) {
                    die("File <%s> cannot be found", file.getName());
                }

                runjs(new FileReader(file), file.getName());
                break;
            default:
                if (args.length != 1 || !Files.exists(Paths.get(args[0])))
                    die("You should run with a folder contains rec files!");
                runOverPath(args[0]);
                break;
        }


    }

    private static void runOverPath(String basePath) throws IOException {
        File path = new File(basePath);
        if (path.exists()) {
            RecRepository repo = DefaultLoadingStrategy.repo(basePath);
            Stream<String> lines = lines(Paths.get(basePath, "default.rule"));

            new RuleLoader().load(lines.collect(Collectors.toList())).stream()
                    .map(ruleRunner -> ruleRunner.runOn(repo))
                    .filter(((Predicate<Pair<Boolean, List<Result>>>) Pair::getFirst)
                            .negate())
                    .forEach(pair -> {
                        pair.getSecond().forEach(it -> System.out.println(it.getDetails()));
                    });
        }
    }
}
