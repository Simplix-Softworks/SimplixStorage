# lightningstorage
lightningstorage - A Libary to store data a better way.

Documentation & Wiki comming soon

#### Maven

1. Place this in your repository-section: 
>Example Pom: https://pastebin.com/eiyRZYyi 

```xml
    <repository>
        <id>TheWarKing-Public-Storage<id>
        <url>http://thewarking.de:8081/repository/Storage/</url>
    <repository>
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
