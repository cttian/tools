package lambda;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectIterater {

	public static void main(String[] args) {
		final List<String> test = Arrays.asList("abc", "def", "qqq", "pdb");
		test.forEach(name -> System.out.println(name));
		test.forEach(System.out::println);
		test.stream().map(String::length).forEach(System.out::println);
		test.stream().map(name -> name.length()).forEach(System.out::println);
		
		List<String> collect = test.stream().filter(name -> name.startsWith("a")).collect(Collectors.toList());
		System.out.println(String.format("find %d string", collect.size()));
		
		Predicate<String> startsWithA = name -> name.startsWith("a");
		long count = test.stream().filter(startsWithA).count();
		long count2 = test.stream().filter(startsWithA).count();
		
		Function<String, Predicate<String>> startWithLetter = letter -> { return name -> name.startsWith(letter);};
		test.stream().filter(startWithLetter.apply("a")).count();
		test.stream().filter(startWithLetter.apply("d")).count();
		startWithLetter = letter -> name -> name.startsWith(letter);
		
		pickName(test, "t");
	}
	
	public static void pickName(final List<String> names, final String startingLetter){
		final Optional<String> option = names.stream().filter(name -> name.startsWith(startingLetter)).findFirst();
		System.out.println(String.format("A name starts with %s:%s", startingLetter, option.orElse("No name find")));
	}
	
	public static void listFiles(String path){
		try {
			Files.list(Paths.get("c:")).filter(Files::isDirectory).forEach(System.out::println);
			new File(".").listFiles(file -> file.isHidden());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
