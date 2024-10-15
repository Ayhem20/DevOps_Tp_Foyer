pipeline {
    agent any
    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling...'
                    git branch: 'ayhem', 
                    url: 'https://github.com/Ayhem20/DevOps_Tp_Foyer.git',
                    credentialsId: '8f9b2f59-5031-4710-ba76-f57fadc1a5de'
            }
        }
        stage('Maven Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
    }
}
