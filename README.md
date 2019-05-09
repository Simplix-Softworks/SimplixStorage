# lightningstorage
lightningstorage - A Libary to store data a better way.

Do you want to save your Config files easily and independently from Bukkit or BungeeCord?
Want to do more than just use simple .yml files to back up data?
Are you looking for a powerful "bukkitlike" (Very similar to bukkit config) libary to store data in files?


Then this libary is just right for you.

LightningStorage is extremely fast & good at storing data reliably!

LightningStorage is licensed under the MIT license, which means that
you can also see him in 

At the moment LightningStorage supports two file types:


#### Json:
A very fast and slim file format.
It is much faster than yaml files and is therefore better suited for storing
 larger amounts of data, such as player data (rank, money, playtime, etc).
>https://stackoverflow.com/questions/2451732/how-is-it-that-json-serialization-is-so-much-faster-than-yaml-serialization-in-p/2452043#2452043


#### Yaml:
Yaml files are not as fast as json files, but they are easier 
to read and are therefore more suitable than configurations files, 
as you often find them in bukkit plugins in the form of "config.yml".





### How to setup

#### Maven

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
        





**Libary's used**

>MIT-org.json Copyright (c) 2002 JSON.org <br>
>YAMLBEANS - Copyright (c) 2008 Nathan Sweet, Copyright (c) 2006 Ola Bini <br>
