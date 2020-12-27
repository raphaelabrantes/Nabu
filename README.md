##Nabu - the Announcer
___
An desktop app that display information, metrics and notifications. <br>
It should:
<div>
    <ul>
        <li>open up at start</li>
        <li>display graphs and metrics</li>
        <li>display notifications and events</li>
        <li>display weather and news</li>
        <li>display twitter top topics(?)</li>
    </ul>
</div>
It might not be:
<div>
    <ul>
        <li>efficient</li>
        <li>best practice</li>
        <li>quality code</li>
        <li>innovative</li>
        <li>working on Windows</li>
    </ul>
</div>

#Getting Started
___
####Run city.py script, that will generate the SQLITE3 Nabu.db datebase.<br>

```shell
$ sudo apt install sqlite3
$ python3 scripts/city.py
```

###Building
```shell
$ ./gradlew jar
```

###Running
```shell
$ ./gradlew execute
```

###Clean Build and Run
```shell
$ ./gradlew clean jar execute
```
