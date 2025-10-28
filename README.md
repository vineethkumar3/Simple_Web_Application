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
<h3>Integrate Sonarqube into jenkins</h3>

```
stage('SonarQube Analysis') {
            steps {
                script {
                    // Use Jenkins SonarQube environment
                    withSonarQubeEnv("${SONARQUBE_ENV}") {
                        sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=MyApplication \
                          -Dsonar.host.url=${SONAR_HOST_URL} \
                          -Dsonar.login=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    // Wait for SonarQube to finish analysis
                    waitForQualityGate abortPipeline: true
                }
            }
        }
```
Here for the Quality Gate, to get the status of the scan we have to do the webhook to our project. Then only waitForQualityGate will fetch the status.
