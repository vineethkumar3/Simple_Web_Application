pipeline {
    agent {
        label 'worker'
    }
    tools {
        maven 'maven_3.9.11'
    }
    environment {
        DOCKER_IMAGE = 'my_web_app'
        DOCKER_REPO= '454212'
        WAR_NAME = 'myapplication.war'
        DOCKER_HUB_REPO= 'jenkinsrepo'
        KUBE_SERVER = 'https://collocative-unfondly-shandi.ngrok-free.dev'
        SONARQUBE_ENV = 'MySonar'
        SONAR_TOKEN = credentials('sonarqube')
    }
    stages {
        stage('Clean Workspace') {
            steps{
                cleanWs()
            }
        }
        stage('CheckOut') {
            steps{
                git url: 'https://github.com/vineethkumar3/Simple_Web_Application.git', branch: params.Branch
            }
        }
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
        stage('Build') {
            steps {
              sh 'pwd'
              sh 'mvn package'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar, target/**/*.war', onlyIfSuccessful: false
                }
            }
        }
        stage ('Docker Build') {
            steps {
                withCredentials([string(credentialsId: '454212-docker-repo-access-key', variable: 'DOCKER_TOKEN')]){
                sh '''
                echo ${DOCKER_TOKEN}
                sudo docker rmi ${DOCKER_IMAGE}:v1 || true
                sudo usermod -aG docker $USER
                sudo docker build -t ${DOCKER_IMAGE}:latest .
                sudo docker tag ${DOCKER_IMAGE}:latest ${DOCKER_REPO}/${DOCKER_HUB_REPO}:v2
                echo "${DOCKER_TOKEN}" | docker login -u 454212 --password-stdin
                docker push ${DOCKER_REPO}/${DOCKER_HUB_REPO}:v2
                '''
                //sh 'sudo docker run -p 9090:8080 my_web_app'
                }
            }
        }
        stage ('ECR Push'){
            steps {
                withAWS(credentials:'AWS_ACCESS_KEY') {
                sh '''
                  aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 879467241318.dkr.ecr.us-east-1.amazonaws.com
                  docker tag ${DOCKER_REPO}/${DOCKER_HUB_REPO}:v2  879467241318.dkr.ecr.us-east-1.amazonaws.com/web-app:latest
                  docker push 879467241318.dkr.ecr.us-east-1.amazonaws.com/web-app:latest
                  '''
                }
            }
        }
        stage('Deploy') {
            steps {
                sh 'echo "Skipped"'
                //sh 'sudo mv ./target/devops.war /var/lib/tomcat10/webapps/${WAR_NAME}'
                //sh 'sudo chown tomcat:tomcat /var/lib/tomcat10/webapps/${WAR_NAME}'
                //sh 'sudo chmod 644 /var/lib/tomcat10/webapps/${WAR_NAME}'
            }
        }
        stage('Kubernetes') {
            steps {
              withKubeConfig(
                  [credentialsId: 'ee133e58-c3c3-4725-8cb3-ecdce9c5efc7', serverUrl: "${env.KUBE_SERVER}"])
                  {
                      withAWS(credentials:'AWS_ACCESS_KEY') {
                          sh '''
                          aws ecr get-login-password --region us-east-1 | \
                            kubectl create secret docker-registry ecr-secret \
                            --docker-server=879467241318.dkr.ecr.us-east-1.amazonaws.com \
                            --docker-username=AWS \
                            --docker-password=$(aws ecr get-login-password --region us-east-1) \
                            --namespace dev
                      
                      kubectl apply -f my_deployment_file.yml
                      kubectl set image deployment/my-deployment my-web-app=879467241318.dkr.ecr.us-east-1.amazonaws.com/web-app:latest -n dev
                      
                      '''   
                      }
                  }
            }
        }
    }
    post {
        always {
        slackSend channel: 'sample_java', message: '${env.BUILD_STATUS}'
        mail bcc: '', body: 'success', cc: '', from: '', replyTo: '', subject: 'Build status', to: 'vineeth12boddu@gmail.com'
        }
    }
}
