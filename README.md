
# LightningStorage
LightningStorage - A Library to store data in a better way.


![LightingStorage](https://proxy.spigotmc.org/fe4eabc0f5de21c5ab0ca87a0003528e1b4ddf54?url=https%3A%2F%2Fi.ibb.co%2F3FyWVp2%2FLightning-Storage-Wallpaper.png)

[![](https://jitpack.io/v/JavaFactoryDev/LightningStorage.svg)](https://jitpack.io/#JavaFactoryDev/LightningStorage)

Do you want to save your config files easily and **independently** from Bukkit or BungeeCord?<br>
Want to do more than just use simple .yml files to store data?<br>
Are you looking for a powerful "bukkitlike" (Very similar to Bukkit config) library to store data in files?<br>

**Then this library is just right for you.**

I was looking for a library that I could use to store data with Bukkit like methods 
without being depended on Bukkit/BungeeCord. But there was nothing out there so I decided to write my own library.
Of course there are a few libraries with bukkitlike methods but no one has the features that I need.
ThunderBolt-2 for example only supports Json files but does not support nested objects.
Now I'am publishing this library because I think libraries of high quality should be publicly available for everyone
Now it is here: **LightningStorage**!

LightningStorage is extremely fast & good at **storing data reliably**! <br>
It also supports **nested objects**!<br>
Like bukkit it has a contains check.
LightningStorage is licensed under the Apache2 license, which means that
you can also **use it in private projects** that are not open source.

If you have any ideas to add or issues just open a issue page. I will do my best to help.
<br>
For more details, see the [wiki](https://github.com/JavaFactoryDev/LightningStorage/wiki) 

<br>
At the moment LightningStorage supports four file types:

<br>

#### Json:
A very fast and slim file format.
It is much faster than yaml files and is therefore better suited for storing
 larger amounts of data, such as player data (rank, money, playtime, etc).
>https://stackoverflow.com/questions/2451732/how-is-it-that-json-serialization-is-so-much-faster-than-yaml-serialization-in-p/2452043#2452043


#### Yaml:
Yaml files are not as fast as json files, but they are easier 
to read and are therefore more suitable than configuration files, 
as you often find them in bukkit plugins in the form of "config.yml".

#### Toml:
Toml is a compromise between the readybility of Yaml and the performance of Json, thus being a quite good way to go.


#### LightningFile
LightningFile is our own implementation of a config file format. It has quite good performance (about as fast as Json) but is way more readable.

#### How to setup

1. Place this in your repository-section: 

```xml
<!-- JitPack-Repo -->
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```       

2. Place this in your dependency-section: 

```xml
<dependency>
  <groupId>com.github.JavaFactoryDev</groupId>
  <artifactId>LightningStorage</artifactId>
  <version>3.0.1</version>
</dependency>
```       
    

3. Important! Use a shade plugin to make sure that the library is shaded into your final .jar file when your
plugin is compiled. 
The relocation is optional but heavily recommended. (Just change 'yourpackage.yourname' to the needed values) 

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-shade-plugin</artifactId>
  <version>3.1.0</version>
  <executions>
    <execution>
    <phase>package</phase>
    <goals>
      <goal>shade</goal>
    </goals>
    </execution>
  </executions>
  <configuration>
    <createDependencyReducedPom>false</createDependencyReducedPom>
    <relocations>
      <relocation>
        <pattern>de.leonhard.storage</pattern>
        <shadedPattern>yourpackage.yourname.storage</shadedPattern>
      </relocation>
    </relocations>
  </configuration>
</plugin>
```      


**Library's used:**

LightningStorage uses a powerful combination of libraries to provide the best usability: 

>MIT-org.json Copyright (c) 2002 JSON.org <br>
>YAMLBEANS - Copyright (c) 2008 Nathan Sweet, Copyright (c) 2006 Ola Bini <br>
>TOML-Lib - Copyright (c) 2016 Guillaume Raffin.

