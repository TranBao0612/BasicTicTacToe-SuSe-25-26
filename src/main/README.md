# Console App without Server-CLient architecture
```bash
mvn exec:java -Dexec.mainClass="org.myproject" -Dexec.args="1"
```
or
```bash
mvn exec:java -Dexec.mainClass="org.myproject" -Dexec.args="2"
```
or
```bash
mvn exec:java@App -Dexec.args="2"
```


# Server-Client App: Single user, single thread (TCP, Stateful)
## Server
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_singlethread_singleuser.Server"
```
## Client
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_singlethread_singleuser.Client"
```

# Server-Client App: Multi user, multi thread (TCP, Stateful)
## Server with 1 Thread/Client
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_multithread_multiuser.Server"
```
## Server with Thread Pool
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_multithread_multiuser.ServerThreadPool"
```
## Client
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_multithread_multiuser.Client"
```

# Server-Client App: Multi user, single thread (TCP, Stateless)
## Server
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_stateless_tcp.Server"
```
## Client
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_stateless_tcp.Client"
```

# Server-Client App: Multi user, single thread (HTTP, Stateless)
## Server (No Jakarta)
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_stateless_http.Server"
```
## Server (with Jakarta)
```bash
mvn clean package

# Copy the .war file in /target and paste in /webapps in Tomcat

cd "<path to /bin folder of Tomcat>"
.\startup.bat
```
## Client (Console)
```bash
mvn exec:java -Dexec.mainClass="org.myproject.sc_stateless_http.Client"
```
## Client (with Front-end)
Open `src\main\java\org\myproject\sc_stateless_http\Client.html` in a browser.