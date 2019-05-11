# lightningstorage
lightningstorage - A Library to store data a better way.

Do you want to save your config files easily and **independently** from Bukkit or BungeeCord?<br>
Want to do more than just use simple .yml files to back up data?<br>
Are you looking for a powerful "bukkitlike" (Very similar to Bukkit config) library to store data in files?<br>

**Then this library is just right for you.**

I was looking for a library that I could use to store data with Bukkit like methods 
without being depended on Bukkit/BungeeCord. But there was nothing out there so I decided to write my own library.
Of course there are a few libraries with bukkitlike methods but no one has the features that I need.
ThunderBolt-2 for example only supports Json files and does not support nested objects for example.
Now I'am publishing this library because I think libraries of high quality should be publicly available for everyone
Now it is here: **LightningStorage**!

<br>

### Table of contents:

<br>
-1. [A few information] <br>
-2. [Supported file types] <br>
-3. [How to setup](https://github.com/JavaFactoryDev/LightningStorage/wiki/How-to-setup) <br>
-4. [Usage](https://github.com/JavaFactoryDev/LightningStorage/wiki/Usage) <br>
-5. [Real world examples][Comming soon] <br>

<br>

LightningStorage is extremely fast & good at **storing data reliably**! <br>
It also supports **nested objects**!<br>
Like bukkit it has a contains check - for both json and yaml.
LightningStorage is licensed under the MIT license, which means that
you can also **use it in private projects** that are not open source.

If you have any ideas to add or issues just open a issue page. I will do my best to help.

<br>
At the moment LightningStorage supports two file types:

#### Json:
A very fast and slim file format.
It is much faster than yaml files and is therefore better suited for storing
 larger amounts of data, such as player data (rank, money, playtime, etc).
>https://stackoverflow.com/questions/2451732/how-is-it-that-json-serialization-is-so-much-faster-than-yaml-serialization-in-p/2452043#2452043


#### Yaml:
Yaml files are not as fast as json files, but they are easier 
to read and are therefore more suitable than configuration files, 
as you often find them in bukkit plugins in the form of "config.yml".

More file formats will be supported soon





### How to setup

1. Place this in your repository-section: 
>Example Pom: https://pastebin.com/eiyRZYyi 

```xml
    <repository>
        <id>TheWarKing-Public-Storage</id>
        <url>http://thewarking.de:8081/repository/Storage/</url>
    </repository>
```       


2. Place this in your dependency-section: 

```xml

    <dependency>
            <groupId>de.leonhard</groupId>
            <artifactId>LightningStorage</artifactId>
            <version>1.0</version>
            <scope>compile</scope>
    </dependency>
```       
    

3. Important! Use a shade plugin to make shure that the libary is shaded into your final .jar file when your
plugin is compiled. 
The relocation is optional but heavily recommended.

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
				<pattern>de.leonhard</pattern>
				<shadedPattern>yourpackage.yourname.storage</shadedPattern>
			</relocation>
		</relocations>
	</configuration>
</plugin>
```       


**Library's used**

LightningStorage uses a powerful combination of libraries to provide best usability: 

>MIT-org.json Copyright (c) 2002 JSON.org <br>
>YAMLBEANS - Copyright (c) 2008 Nathan Sweet, Copyright (c) 2006 Ola Bini <br>
