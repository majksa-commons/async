# Async
_[Majksa Commons](//github.com/majksa-commons)_

<p>
    <a href="//github.com/majksa-commons/async/releases"><img src="https://img.shields.io/github/v/release/majksa-commons/async"></a>
    <a href="https://jitpack.io/#majksa-commons/async"><img src="https://img.shields.io/jitpack/v/majksa-commons/async"></a>
    <a href="//github.com/majksa-commons/async/commits/main"><img src="https://img.shields.io/github/last-commit/majksa-commons/async"></a>
    <a href="//github.com/majksa-commons/async/releases"><img src="https://img.shields.io/github/downloads/majksa-commons/async/total"></a>
    <a href="//github.com/majksa-commons/async/blob/main/LICENSE.md"><img src="https://img.shields.io/github/license/majksa-commons/async"></a>
    <a href="//github.com/majksa-commons/async"><img src="https://img.shields.io/github/languages/code-size/majksa-commons/async"></a>
    <a href="//github.com/majksa-commons/async/issues"><img src="https://img.shields.io/github/issues-raw/majksa-commons/async"></a>
    <a href="//java.com"><img src="https://img.shields.io/badge/java-8-orange"></a>
</p>

Java framework improving your experience handling asynchronous tasks in java.

## Summary
1. [Installation](#installation)
    1. [Gradle](#gradle)
    2. [Maven](#maven)  
2. [How to use](#how-to-use)
3. [Built With](#built-with)
4. [Authors](#authors)
5. [License](#license)

## Installation
Make sure to replace `%version%` with the latest version number, or a commit hash, e.g. `1.0.0`.
You can find this library [HERE](https://jitpack.io/#majksa-commons/async)

###  Maven
Register the repository
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Now add the dependency itself
```xml
<dependency>
    <groupId>com.github.majksa-commons</groupId>
    <artifactId>async</artifactId>
    <version>%version%</version>
</dependency>
```
###  Gradle
Register the repository
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
Now add the dependency itself
```gradle
dependencies {
    implementation 'com.github.majksa-commons:async:%version%'
}
```

## How to use
### Awaited object
This feature gives you the opportunity to await a state of an object as a CompletableFuture.<br>
For example, you want to wait for an event to happen and then use the event in the main thread.<br>
1. Start of with creating the object
```java
final AwaitedObject<Integer> awaitedInteger = new AwaitedObject();
```
2. Next you need to pass this object to the event listener. This line is very specific on the event listener itself but it should give you a brief idea on how to use it.
```java
listener.onUserLogin(user -> awaitedInteger.set(user.getId()));
```
3. Finally, you can get the CompletableFuture by calling get method on the awaited integer.
```java
awaitedInteger.get().thenAccept(System.out::println);
```

### Scheduled task
Using this, you can schedule a runnable to be executed on a Date. You can also easily set a Date to reschedule it or cancel it.
Example usage:
```java
// The result holder
final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
// The original date
final Date date = ...;
// Schedule the task
final ScheduledTask schedule = ScheduledTask.schedule(instance.getTime(), () -> atomicBoolean.set(true));

// Change the date of task to happen in 3 seconds
final Calendar calendar = Calendar.getInstance();
calendar.add(Calendar.SECOND, 3);
schedule.setDate(calendar.getTime());

// Await the task
schedule.get().join();
// atomicBoolean.get() is now set to true
```

## Built With

* [Java 8](https://java.com)

## Authors
* [Majksa (@maxa-ondrej)](https://github.com/maxa-ondrej)

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE](LICENSE) file for details