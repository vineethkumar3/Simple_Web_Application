pipeline
{
    agent any
    stages
    {
        stage("CheckoutSC")
        {
            steps
            {
                git branch: 'main', url: 'https://github.com/rajesh-305/Project1.git'
                slackSend channel: 'project-october-centralized-resources', message: 'SourceCode Checkout Successful'
                mail bcc: '', body: 'Source Code Checked Out Successfully', cc: '', from: '', replyTo: '', subject: 'Source Code Checkout Successful for Project ID 1234', to: 'projects2488@gmail.com'
            }
        }
        stage("Buildtheartifact")
        {
            steps
            {
                sh 'mvn package'
                  slackSend channel: 'project-october-centralized-resources', message: 'Build Successful'
                mail bcc: '', body: 'Build Success!', cc: '', from: '', replyTo: '', subject: 'Builds successfully done for project ID 1234', to: 'projects2488@gmail.com'
            }
        }
        stage("DeploytoTEst")
        {
            steps
            {
                deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: 'tomcat', path: '', url: 'http://172.31.46.173:8080')], contextPath: 'app3', war: '**/*.war'
                  slackSend channel: 'project-october-centralized-resources', message: 'Artifacts deployed successfully: url: http://51.20.6.232:8080/app3'
                mail bcc: '', body: 'http://51.20.6.232:8080/app3', cc: '', from: '', replyTo: '', subject: 'Deployment successful: Application is available on URL http://51.20.6.232:8080/app3', to: 'projects2488@gmail.com'
            }
        }
    }
    post { 
        failure { 
            echo 'The Project Failed'
             slackSend channel: 'project-october-centralized-resources', message: 'Project Failed!!! Immediate action required'
                mail bcc: '', body: 'Project Failed!!!!', cc: '', from: '', replyTo: '', subject: 'Project ID 1234 Failed. Fix the issues : http://51.21.202.147:8080/', to: 'projects2488@gmail.com'
        }
    }
    
    
}
