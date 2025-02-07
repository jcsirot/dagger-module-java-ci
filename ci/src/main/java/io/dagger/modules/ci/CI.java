package io.dagger.modules.ci;

import io.dagger.client.CacheVolume;
import io.dagger.client.Container;
import io.dagger.client.DaggerQueryException;
import io.dagger.client.Directory;
import io.dagger.client.File;
import io.dagger.client.Service;
import io.dagger.module.AbstractModule;
import io.dagger.module.annotation.Default;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;
import java.util.List;
import java.util.concurrent.ExecutionException;

/** Echo Server CI targets */
@Object
public class CI extends AbstractModule {

  /** Publish the application container after building and testing it on-the-fly */
  @Function
  public String publish(Directory source, @Default("ttl.sh/echo-server:1h") String imageName)
      throws ExecutionException, DaggerQueryException, InterruptedException {
    return build(source, false).publish(imageName);
  }

  /** Run the application locally */
  @Function
  public Service dev(Directory source)
      throws ExecutionException, DaggerQueryException, InterruptedException {
    return pack(source).asService();
  }

  /** Build the application and optionally run the tests */
  @Function
  public Container build(Directory source, @Default("false") boolean skipTests) {
    return buildEnv(source)
        .withExec(List.of("mvn", "clean", "test", "-DskipTests=%s".formatted(skipTests)));
  }

  /** Package the application into a container */
  @Function
  public Container pack(Directory source) {
    File jar = build(source, true)
        .directory("./target")
        .file("echo-server-1.0-SNAPSHOT.jar");
    return dag.container().from("eclipse-temurin:17")
        .withFile("/usr/local/echo-server/app.jar", jar)
        .withExposedPort(8080)
        .withEntrypoint(List.of("java", "-jar", "/usr/local/echo-server/app.jar"));
  }

  /** Return the result of running unit tests */
   @Function
  public String test(Directory source)
      throws ExecutionException, DaggerQueryException, InterruptedException {
    return buildEnv(source).withExec(List.of("mvn", "clean", "test")).stdout();
  }

  /** Build a ready-to-use development environment */
  @Function
  public Container buildEnv(Directory source) {
    CacheVolume mavenCache = dag.cacheVolume("m2");
    return dag
        .container()
        .from("maven:3-eclipse-temurin-17")
        .withDirectory("/src", source)
        .withMountedCache(".m2/", mavenCache)
        .withWorkdir("/src");
  }

}
