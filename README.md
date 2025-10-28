<h2> Sonarqube: </h2>
<li>
Create a postgres sql database from docker.
  
```
docker run -d --name sonarqube-db \
  --network sonar-net \
  -e POSTGRES_USER=sonar -e POSTGRES_PASSWORD=sonar -e POSTGRES_DB=sonarqube \
  -v pgdata:/var/lib/postgresql/data \
  postgres:15-alpine
```

</li>
<li>
Create a sonarqube docker and sonarqube needs database to store the logs so postgres is mandatory.
  
```
docker run -d \
  --name sonarqube \
  -p 9000:9000 \
  --network sonar-net \
  --ulimit nofile=65536:65536 \
  --ulimit nproc=4096 \
  -e SONAR_JDBC_URL=jdbc:postgresql://sonarqube-db:5432/sonarqube \
  -e SONAR_JDBC_USERNAME=sonar \
  -e SONAR_JDBC_PASSWORD=sonar \
  -e SONAR_SEARCH_JAVAOPTS="-Xms512m -Xmx512m" \
  -e SONAR_WEB_JAVAOPTS="-Xms128m -Xmx256m" \
  -e SONAR_CE_JAVAOPTS="-Xms128m -Xmx256m" \
  sonarqube:lts-community
```

</li>
<li>
Elastic Search is embedded into sonarqube just informing no need to do any extra thing with that.
</li>

<h3>ngrok</h3>
If you running any application in local but want to use in remote. Ngrok helps in doing that basically it install an agent in your local and it route the traffic that was getting at remote api to this local.

```
ngrok http 9000
```
