<h2> Sonarqube: </h2>
<li>
1. Create a postgres sql database from docker.
   '''
docker run -d --name sonarqube-db \
  --network sonar-net \
  -e POSTGRES_USER=sonar -e POSTGRES_PASSWORD=sonar -e POSTGRES_DB=sonarqube \
  -v pgdata:/var/lib/postgresql/data \
  postgres:15-alpine
   '''
</li>
<li>
3. Create a sonarqube docker and sonarqube needs database to store the logs so postgres is mandatory.
'''
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
'''
</li>
<li>
5. Elastic Search is embedded into sonarqube just informing no need to do any extra thing with that.
</li>
