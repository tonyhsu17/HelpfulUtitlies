[![Release](https://jitpack.io/v/tonyhsu17/HelpfulUtitlies.svg)](https://jitpack.io/#tonyhsu17/HelpfulUtitlies)
# Helpful Utilities
Common utilities that provide helpful methods and classes for general purpose uses

## Classes
EventCenter - Singleton class to handle passing events to other classes without any direct relations.  
HistoryLog - Manages persistent (file-based) history log for any purposes.  
Logger - An alternative to using System.out.println  
Scheduler - Provides running a method on a separate thread for an definitive number of times.  


### Where can I get the latest release?
You can download source and binaries from releases page.

Alternatively you can pull it from the central Jitpack repositories:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
 
<dependency>
    <groupId>com.github.tonyhsu17</groupId>
    <artifactId>HelpfulUtitlies</artifactId>
    <version>1.0</version>
</dependency>
```
